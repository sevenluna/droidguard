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

	private Detector detector;
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
		detector = new AccelerometerDetector(this);
		detector.registerListener(this);
		notifier = new RingtoneNotifier(this);
		SysNotification.Set(this);
	}

	@Override
	public void onDetectorChangeDetected(DetectorEvent event) {
		TextView text = (TextView) findViewById(R.id.text);
		Log.d("detectTest", "Level changed to(" + System.currentTimeMillis()
				+ "): " + event.changeLevel);
		text.setText("Level changed to(" + System.currentTimeMillis() + "): "
				+ event.changeLevel);
		if (event.changeLevel > Detector.DETECTOR_CHANGELEVEL_MEDIUM
				&& notifier.canExecute()) {
			notifier.execute();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		detector.start();
	}

	@Override
	public void onStop() {
		detector.stop();
		super.onStop();
	}

	@Override
	public void onDestroy() {
		detector.stop();
		super.onDestroy();
		SysNotification.Unset(this);
	}
}
