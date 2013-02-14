package fr.gdelente.android.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationListener;
import android.os.IBinder;

public class LocationHelper {

	LocationListener mLocationListener = null;
	Context mContext = null;
	private ServiceConnection mConnection = null;
	private boolean mBound = false;

	public LocationHelper(Context context, LocationListener listener) {
		mContext = context;
		mLocationListener = listener;
		mConnection = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName className,
					IBinder service) {
				mBound = true;
			}

			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				mBound = false;
			}
		};
	}

	public void onStop() {
		if (mBound) {
			mContext.unbindService(mConnection);
			mBound = false;
		}
	}

	public void onStart() {
		Intent intent = new Intent((Context) mLocationListener,
				LocationService.class);
		mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
}
