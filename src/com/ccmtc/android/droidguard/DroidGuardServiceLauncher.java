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
		startService(new Intent(this,
				com.ccmtc.android.droidguard.DroidGuardService.class));
		finish();
	}
}
