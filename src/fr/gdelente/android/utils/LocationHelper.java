package fr.gdelente.android.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.os.IBinder;
import fr.gdelente.android.service.LocationService;
import fr.gdelente.android.service.LocationService.LocationBinder;

/**
 * Use an instance of this Helper in your activities to make them location
 * aware, as described in the README
 */
public class LocationHelper {

	LocationListener mLocationListener = null;
	Context mContext = null;
	private ServiceConnection mConnection = null;
	private boolean mBound = false;
	private LocationService mLocationService = null;

	public LocationHelper(Context context, LocationListener listener) {
		mContext = context;
		mLocationListener = listener;
		mConnection = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName className,
					IBinder binder) {
				mLocationService = ((LocationBinder) binder).getService();
				mLocationService.addLocationListener(mLocationListener);
				mBound = true;
			}

			@Override
			public void onServiceDisconnected(ComponentName className) {
				mBound = false;
			}
		};
	}

	public void onStop() {
		if (mBound) {
			mLocationService.removeLocationListener(mLocationListener);
			mContext.unbindService(mConnection);
			mBound = false;
		}
	}

	public void onStart() {
		Intent intent = new Intent(mContext, LocationService.class);
		mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	public Location getLocation() {
		if (mBound) {
			return mLocationService.getLocation();
		} else {
			return null;
		}
	}
}
