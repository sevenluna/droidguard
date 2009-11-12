/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author Ken
 * 
 */
public class DroidGuardServiceLauncher extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set detector and notifier for debugging.
		PrefStore.resetAll(this);
		PrefStore.toggleDetector(this,
				DetectorManager.DETECTOR_TYPE_ORIENTATION, true);
		PrefStore.setDetectorSensitivity(this,
				DetectorManager.DETECTOR_TYPE_ORIENTATION,
				Detector.DETECTOR_CHANGELEVEL_MEDIUM);
		PrefStore.toggleNotifier(this, NotifierManager.NOTIFIER_TYPE_RINGTONE,
				true);
		PrefStore.setStopServiceOnSms(this, true);
		PrefStore.setStopServiceOnIncomingCall(this, true);

		startService(new Intent(this,
				com.ccmtc.android.droidguard.DroidGuardService.class));
		finish();
	}
}
