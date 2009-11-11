/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author Ken
 * 
 */
public class SysBroadcastReceiver extends BroadcastReceiver {

	private static final String logTag = "DroidGuardSysBroadcastReceiver";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(logTag, "System broadcast received.");
		context.startService(new Intent(context, DroidGuardService.class));

		// TODO Create a notification allowing user to restart service.
	}

}
