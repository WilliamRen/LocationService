/*
 * Copyright (C) 2011 Guillaume Delente
 *
 * This file is part of OpenBike.
 *
 * OpenBike is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * OpenBike is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenBike.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.gdelente.android.service;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import fr.gdelente.android.utils.ILastLocationFinder;
import fr.gdelente.android.utils.PlatformSpecificImplementationFactory;

public class LocationService extends Service implements LocationListener {

	private final IBinder mBinder = new LocationBinder();
	private ArrayList<LocationListener> mListeners = new ArrayList<LocationListener>();
	private Location mLastLocation = null;
	private ILastLocationFinder mLocationFinder = null;
	// Maximum distance accuracy of the last known location before requesting
	// location update
	private static final int MAX_DISTANCE_LIMIT = 1000;
	// Maximum time of the last known location before requesting location update
	private static final long MAX_TIME_LIMIT = 5 * 60 * 1000;

	/**
	 * Class used for the client Binder. Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocationBinder extends Binder {
		public LocationService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return LocationService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d("LocationService",
				"OnLocationChanged : " + location.getAccuracy());
		mLastLocation = location;
		for (LocationListener listener : mListeners) {
			listener.onLocationChanged(location);
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Put enabled providers in extras
	}

	public Location getLocation() {
		return mLastLocation;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("LocationService", "onCreate");
		mLocationFinder = PlatformSpecificImplementationFactory
				.getLastLocationFinder(this, this);
		Location location = mLocationFinder.getLastBestLocation(
				MAX_DISTANCE_LIMIT, MAX_TIME_LIMIT);
		if (location != null) {
			onLocationChanged(location);
		}

	}

	@Override
	public void onDestroy() {
		Log.d("LocationService", "onDestroy");
		mLocationFinder.cancel();
		super.onDestroy();
	}

	public void addLocationListener(LocationListener locationListener) {
		mListeners.add(locationListener);
		if (mLastLocation != null)
			locationListener.onLocationChanged(mLastLocation);
	}

	public void removeLocationListener(LocationListener locationListener) {
		mListeners.remove(locationListener);
	}

}