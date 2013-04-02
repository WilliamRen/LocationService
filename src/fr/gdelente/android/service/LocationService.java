/*
 * Copyright (C) 2013 Guillaume Delente
 *
 * This file is part of LocationService.
 *
 * LocationService is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * LocationService is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LocationService.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.gdelente.android.service;

import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import fr.gdelente.android.utils.ILastLocationFinder;
import fr.gdelente.android.utils.PlatformSpecificImplementationFactory;

/**
 * 
 * Get user's location as quickly as possible and easily share it across
 * multiple activities via this bound service.
 * 
 * Use the LocationHelper in your app and enjoy!
 * 
 */
public class LocationService extends Service implements LocationListener {

	// Maximum distance accuracy of the last known
	// location before requesting location update
	private static final int MAX_DISTANCE_LIMIT = 100;

	// Maximum time of the last known location before requesting location update
	private static final long MAX_TIME_LIMIT = 5 * 60 * 1000;

	private final IBinder mBinder = new LocationBinder();

	private ArrayList<LocationListener> mListeners = new ArrayList<LocationListener>();
	private Location mLastLocation = null;
	private ILastLocationFinder mLocationFinder = null;
	private boolean mNoProviderEnabled = true;

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
		mLastLocation = location;
		for (LocationListener listener : mListeners) {
			listener.onLocationChanged(location);
		}
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	public Location getLocation() {
		return mLastLocation;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mLocationFinder = PlatformSpecificImplementationFactory
				.getLastLocationFinder(this, this);
		Location location = mLocationFinder.getLastBestLocation(
				MAX_DISTANCE_LIMIT, MAX_TIME_LIMIT);
		if (location != null) {
			onLocationChanged(location);
		}
		String provider = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (!"".equals(provider)) {
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			final boolean gpsEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			final boolean networkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			mNoProviderEnabled = !gpsEnabled && !networkEnabled;
		}
		//TODO provide own interface for notifying 
		// that no providers are available
		if (mNoProviderEnabled) {
			for (LocationListener listener : mListeners) {
				listener.onProviderDisabled(null);
			}
		}
	}

	@Override
	public void onDestroy() {
		mLocationFinder.cancel();
		super.onDestroy();
	}

	public void addLocationListener(LocationListener locationListener) {
		mListeners.add(locationListener);
		if (mLastLocation != null)
			locationListener.onLocationChanged(mLastLocation);
		if (mNoProviderEnabled)
			locationListener.onProviderDisabled(null);
	}

	public void removeLocationListener(LocationListener locationListener) {
		mListeners.remove(locationListener);
	}

}
