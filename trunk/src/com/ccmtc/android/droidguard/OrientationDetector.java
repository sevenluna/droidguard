/**
 * 
 */
package com.ccmtc.android.droidguard;

import java.util.List;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * This detector uses the orientation sensor to detect environment changes.
 * 
 * @author Ken
 * 
 */
public class OrientationDetector extends Detector implements
		SensorEventListener {

	private final SensorManager sensorMgr;

	private float[] originalValues = null;

	private int retrieved = 0;

	/**
	 * Create a new instance of {@link OrientationDetector}. Do NOT call this
	 * directly - use DetectorManager.createDetector() instead.
	 * 
	 * @param context The context of this detector.
	 */
	public OrientationDetector(Context context) {
		super(context);
		sensorMgr = (SensorManager) context
				.getSystemService(android.content.Context.SENSOR_SERVICE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Detector#getType()
	 */
	@Override
	public int getType() {
		return DetectorManager.DETECTOR_TYPE_ORIENTATION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Detector#start()
	 */
	@Override
	public boolean start() {
		List<Sensor> oriSensors = sensorMgr
				.getSensorList(Sensor.TYPE_ORIENTATION);
		for (Sensor sensor : oriSensors) {
			boolean res = sensorMgr.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_NORMAL);
			Log.d("OrientationDetector", "orientation sensor registered: "
					+ res);
			if (!res) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Detector#stop()
	 */
	@Override
	public boolean stop() {
		sensorMgr.unregisterListener(this);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.hardware.SensorEventListener#onAccuracyChanged(android.hardware
	 * .Sensor, int)
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// ignore
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.hardware.SensorEventListener#onSensorChanged(android.hardware
	 * .SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
				if (retrieved == 0) {
					originalValues = event.values.clone();
					++retrieved;
				} else if (retrieved <= 10) {
					++retrieved;
					for (int i = 0; i < originalValues.length; i++) {
						originalValues[i] = (originalValues[i]
								* (retrieved - 1) + event.values[i])
								/ retrieved;
					}
				} else {
					int changeLevel = compareValues(originalValues,
							event.values);
					super.onDetectorEvent(changeLevel);
				}
			}
		}
	}

	/**
	 * Compares two float arrays item by item and calculates the biggest change
	 * level. The two arrays should have the same dimension.
	 * 
	 * @param lhs
	 *            One float array.
	 * @param rhs
	 *            The other float array.
	 * @return The biggest change level.
	 */
	private int compareValues(float[] lhs, float[] rhs) {
		int changeLevel = Detector.DETECTOR_CHANGELEVEL_TINY;
		for (int i = 0; i < lhs.length; i++) {
			int newChangeLevel = digitToChangeLevel(lhs[i] - rhs[i]);
			Log.d("oriDetect", "Axis " + i + "change value: "
					+ (lhs[i] - rhs[i]));
			if (changeLevel < newChangeLevel) {
				changeLevel = newChangeLevel;
			}
		}
		return changeLevel;
	}

	/**
	 * Convert a digit distance value into change level presentation.
	 * 
	 * @param distance
	 *            The digit value.
	 * @return The corresponding change level value.
	 */
	private int digitToChangeLevel(float distance) {
		float positiveDistance = Math.abs(distance);
		if (positiveDistance < 4) {
			return Detector.DETECTOR_CHANGELEVEL_TINY;
		}
		if (positiveDistance < 11) {
			return Detector.DETECTOR_CHANGELEVEL_LOW;
		}
		if (positiveDistance < 16) {
			return Detector.DETECTOR_CHANGELEVEL_MEDIUM;
		}
		if (positiveDistance < 21) {
			return Detector.DETECTOR_CHANGELEVEL_HIGH;
		}
		return Detector.DETECTOR_CHANGELEVEL_SIGNIFICANT;
	}

}
