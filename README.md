# LocationService for Android

This project aims to provide user's location as quickly as possible. It also makes it easy to share Location updates across several activities. This project is based on blabla

## Features
 * Returns the best cached location available before requesting Location updates
 * Customize the minimum accuracy and the maximum distance a location should be in order to be concidered good enough.
 * Location updates requested only once for all your activities!

## Quick Setup

#### 1. Include library

**Manual:**
 * Clone this repository and reference the library project as shown in the [Android developer website](http://developer.android.com/tools/projects/projects-eclipse.html#ReferencingLibraryProject)

#### 2. Android Manifest
``` xml
<manifest>
	<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
  	<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
	...
	<application android:name="MyApplication">
		...
	</application>
</manifest>
```

### 3. Use the LocationHelper provided and make your class implements LocationListener interface.
``` java
public class MyFragment extends Fragment implements LocationListener {

    private LocationHelper mLocationHelper = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLocationHelper = new LocationHelper(getActivity(), this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mLocationHelper.onStart();
    }

    @Override
    public void onStop() {
        mLocationHelper.onStop();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
    // TODO Auto-generated method stub
    
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
    // TODO Auto-generated method stub
    
    }
}
```