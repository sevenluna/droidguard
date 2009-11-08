/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * @author Ken
 * 
 */
public class DetectorTest extends Activity implements DetectorEventListener {

	private Detector detector;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detectortest);
		detector = new AccelemeterDetector(this);
		detector.registerListener(this);
	}

	@Override
	public void onDetectorChangeDetected(DetectorEvent event) {
		TextView text = (TextView) findViewById(R.id.text);
		Log.d("detectTest", "Level changed to(" + System.currentTimeMillis()
				+ "): " + event.changeLevel);
		text.setText("Level changed to(" + System.currentTimeMillis() + "): "
				+ event.changeLevel);
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
	}
}
