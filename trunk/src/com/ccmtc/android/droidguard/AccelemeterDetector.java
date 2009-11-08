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
 * @author Ken
 * 
 */
public class AccelemeterDetector extends Detector implements
		SensorEventListener {

	private final SensorManager sensorMgr;

	private float[] originalValues = null;

	private int retrieved = 0;

	public AccelemeterDetector(Context context) {
		super(context);
		sensorMgr = (SensorManager) context
				.getSystemService(android.content.Context.SENSOR_SERVICE);
	}

	@Override
	public int getType() {
		return DetectorManager.DETECTOR_TYPE_ACCELEMETER;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// ignore
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			if (event.sensor.getType() == SensorManager.SENSOR_ACCELEROMETER) {
				if (retrieved <= 10) {
					++retrieved;
					originalValues = event.values.clone();
				} else {
					int changeLevel = compareValues(originalValues,
							event.values);
					super.onDetectorEvent(changeLevel);
				}
			}
		}
	}

	private int compareValues(float[] lhs, float[] rhs) {
		int changeLevel = Detector.DETECTOR_CHANGELEVEL_TINY;
		for (int i = 0; i < lhs.length; i++) {
			int newChangeLevel = digitToChangeLevel(lhs[i] - rhs[i]);
			Log.d("accDetect", "Axis " + i + "change value: "
					+ (lhs[i] - rhs[i]));
			if (changeLevel < newChangeLevel) {
				changeLevel = newChangeLevel;
			}
		}
		return changeLevel;
	}

	private int digitToChangeLevel(float distance) {
		float positiveDistance = Math.abs(distance);
		if (positiveDistance < 2.4) {
			return Detector.DETECTOR_CHANGELEVEL_TINY;
		}
		if (positiveDistance < 3.3) {
			return Detector.DETECTOR_CHANGELEVEL_LOW;
		}
		if (positiveDistance < 5) {
			return Detector.DETECTOR_CHANGELEVEL_NORMAL;
		}
		if (positiveDistance < 8) {
			return Detector.DETECTOR_CHANGELEVEL_HIGH;
		}
		return Detector.DETECTOR_CHANGELEVEL_SIGNIFICANT;
	}

	@Override
	public boolean start() {
		try {
			Log.i("main", "starting...");
			Thread.sleep(1000);
			Log.i("main", "started");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Sensor> accSensors = sensorMgr
				.getSensorList(SensorManager.SENSOR_ACCELEROMETER);
		for (Sensor sensor : accSensors) {
			boolean res = sensorMgr.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_NORMAL);
			Log.d("AccelemeterDetector", "accelerometer sensor registered: "
					+ res);
			if (!res) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean stop() {
		sensorMgr.unregisterListener(this);
		return true;
	}

}
