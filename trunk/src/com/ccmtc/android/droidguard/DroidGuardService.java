/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @author Ken
 * 
 */
public class DroidGuardService extends Service implements DetectorEventListener {

	public static final String EXTRA_STOP_SERVICE = "com.ccmtc.android.droidguard.StopService";

	private static final String logTag = "DroidGuardService";

	private Detector[] detectors;
	private Notifier[] notifiers;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// Set detector and notifier for debugging.
		PrefStore.resetAll(this);
		PrefStore.toggleDetector(this,
				DetectorManager.DETECTOR_TYPE_ORIENTATION, true);
		PrefStore.setDetectorSensitivity(this,
				DetectorManager.DETECTOR_TYPE_ORIENTATION,
				Detector.DETECTOR_CHANGELEVEL_MEDIUM);
		PrefStore.toggleNotifier(this, NotifierManager.NOTIFIER_TYPE_RINGTONE,
				true);

		// Initialize detectors.
		detectors = new Detector[DetectorManager.DETECTOR_COUNT];
		for (int i = 0; i < detectors.length; i++) {
			if (PrefStore.isDetectorSelected(this, i)) {
				Log.d(logTag, "detector " + i + " is selected.");
				detectors[i] = DetectorManager.createDetector(this, i);
				detectors[i].registerListener(this);
			}
		}

		// Initialize notifiers.
		notifiers = new Notifier[NotifierManager.NOTIFIER_COUNT];
		for (int i = 0; i < detectors.length; i++) {
			if (PrefStore.isNotifierSelected(this, i)) {
				Log.d(logTag, "notifier " + i + " is selected.");
				notifiers[i] = NotifierManager.createNotifier(this, i);
			}
		}

		SysNotification.Set(this);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		// TODO Find a better way.
		if (1 != startId) {
			stopSelf();
			return;
		}
		startAllDetectors();
		super.setForeground(true);
		Log.d(logTag, "Service started.");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopAllDetectors();
		stopAllNotifiers();
		SysNotification.Unset(this);
		Log.d(logTag, "Service stopped.");
	}

	@Override
	public void onDetectorChangeDetected(DetectorEvent event) {
		Log.d(logTag, "ChangeLevel: " + event.changeLevel);

		if (event.changeLevel >= PrefStore.getDetectorSensitivity(this,
				event.sourceType)) {
			for (int i = 0; i < NotifierManager.NOTIFIER_COUNT; i++) {
				if (null != notifiers[i] && notifiers[i].canExecute()) {
					Log.d(logTag, "executing notifier " + i);
					notifiers[i].execute();
				}
			}
		}
	}

	private void startAllDetectors() {
		for (Detector detector : detectors) {
			if (null != detector) {
				detector.start();
			}
		}
	}

	private void stopAllDetectors() {
		for (Detector detector : detectors) {
			if (null != detector) {
				detector.stop();
			}
		}
	}

	private void stopAllNotifiers() {
		for (Notifier notifier : notifiers) {
			if (null != notifier) {
				notifier.stop();
			}
		}
	}
}
