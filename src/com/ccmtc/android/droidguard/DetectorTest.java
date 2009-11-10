/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Ken
 * 
 */
public class DetectorTest extends Activity implements DetectorEventListener {

	private static final String logTag = "DetectorTest";

	private Detector[] detectors;
	private Notifier[] notifiers;

	TextView text;

	// private Detector detector;
	// private Notifier notifier;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detectortest);
		text = (TextView) findViewById(R.id.text);
		Button stopButton = (Button) findViewById(R.id.stop);
		stopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (Notifier notifier : notifiers) {
					if (null != notifier) {
						notifier.stop();
					}
				}
			}
		});

		// Set detector and notifier for debugging.
		//PrefStore.resetAll(this);
		PrefStore.toggleDetector(this,
				DetectorManager.DETECTOR_TYPE_ORIENTATION, true);
		PrefStore.setDetectorSensitivity(this,
				DetectorManager.DETECTOR_TYPE_ORIENTATION,
				Detector.DETECTOR_CHANGELEVEL_MEDIUM);
		PrefStore.toggleNotifier(this,
				NotifierManager.NOTIFIER_TYPE_VIBERATION, true);

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

		// notifier = new RingtoneNotifier(this);
		SysNotification.Set(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ccmtc.android.droidguard.DetectorEventListener#onDetectorChangeDetected
	 * (com.ccmtc.android.droidguard.DetectorEvent)
	 */
	@Override
	public void onDetectorChangeDetected(DetectorEvent event) {
		Log.d(logTag, "ChangeLevel: " + event.changeLevel);
		text.setText("ChangeLevel: " + event.changeLevel + ". Source type: "
				+ event.sourceType);

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

	@Override
	public void onResume() {
		super.onResume();
		startAllDetectors();
	}

	@Override
	public void onStop() {
		stopAllDetectors();
		super.onStop();
	}

	@Override
	public void onDestroy() {
		stopAllDetectors();
		super.onDestroy();
		SysNotification.Unset(this);
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
}
