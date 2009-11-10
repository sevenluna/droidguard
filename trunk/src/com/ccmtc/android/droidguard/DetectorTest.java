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

	private Detector[] detectors;

	// private Detector detector;
	private Notifier notifier;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detectortest);
		Button stopButton = (Button) findViewById(R.id.stop);
		stopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != notifier) {
					notifier.stop();
				}
			}
		});

		PrefStore.toggleDetector(this,
				DetectorManager.DETECTOR_TYPE_ORIENTATION, true);
		detectors = new Detector[DetectorManager.DETECTOR_COUNT];
		for (int i = 0; i < detectors.length; i++) {
			if (PrefStore.isDetectorSelected(this, i)) {
				Log.d("DetectorTest", "detector " + i + " is selected.");
				detectors[i] = DetectorManager.createDetector(this, i);
				detectors[i].registerListener(this);
			}
		}
		// detector = new AccelerometerDetector(this);
		// detector.registerListener(this);
		notifier = new RingtoneNotifier(this);
		SysNotification.Set(this);
	}

	/* (non-Javadoc)
	 * @see com.ccmtc.android.droidguard.DetectorEventListener#onDetectorChangeDetected(com.ccmtc.android.droidguard.DetectorEvent)
	 */
	@Override
	public void onDetectorChangeDetected(DetectorEvent event) {
		TextView text = (TextView) findViewById(R.id.text);
		Log.d("detectTest", "Level changed to(" + System.currentTimeMillis()
				+ "): " + event.changeLevel);
		text.setText("Level changed to " + event.changeLevel
				+ ". Source type: " + event.sourceType);
		if (event.changeLevel > Detector.DETECTOR_CHANGELEVEL_MEDIUM
				&& notifier.canExecute()) {
			notifier.execute();
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
