/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * @author Ken
 * 
 */
public class LocationDetector extends Detector implements LocationListener {

	public static final float DISTANCE_THRESHOLD = 10;

	private final LocationManager locationMgr;

	private Location lastLocation = null;

	private boolean benchmarkSet = false;

	/**
	 * @param context
	 */
	public LocationDetector(Context context) {
		super(context);
		locationMgr = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Detector#getType()
	 */
	@Override
	public int getType() {
		return DetectorManager.DETECTOR_TYPE_LOCATION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Detector#start()
	 */
	@Override
	public boolean start() {
		if (!locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return false;
		}

		locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				this);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Detector#stop()
	 */
	@Override
	public boolean stop() {
		locationMgr.removeUpdates(this);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onLocationChanged(android.location.
	 * Location)
	 */
	@Override
	public void onLocationChanged(Location location) {
		if (!benchmarkSet) {
			if (null != lastLocation
					&& location.distanceTo(lastLocation) < DISTANCE_THRESHOLD) {
				benchmarkSet = true;
			}
			lastLocation = location;
		} else {
			float distance = location.distanceTo(lastLocation);
			int changeLevel;
			if (distance < 7) {
				changeLevel = Detector.DETECTOR_CHANGELEVEL_TINY;
			} else if (distance < 12) {
				changeLevel = Detector.DETECTOR_CHANGELEVEL_LOW;
			} else if (distance < 16) {
				changeLevel = Detector.DETECTOR_CHANGELEVEL_MEDIUM;
			} else if (distance < 20) {
				changeLevel = Detector.DETECTOR_CHANGELEVEL_HIGH;
			} else {
				changeLevel = Detector.DETECTOR_CHANGELEVEL_SIGNIFICANT;
			}
			super.onDetectorEvent(changeLevel);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	@Override
	public void onProviderDisabled(String provider) {
		// Ignore.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled(String provider) {
		// Ignore.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String,
	 * int, android.os.Bundle)
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Ignore.
	}
}
