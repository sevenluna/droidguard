package com.ccmtc.android.droidguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;


public class IntentReceiver extends BroadcastReceiver {
	private static final String logTag = "IntentReceiver";
	 static final String ACTION_PHONE_STATE = "android.intent.action.PHONE_STATE";
	 static final String ACTION_BATTERY_LOW = "android.intent.action.ACTION_BATTERY_LOW";
	 
	 static final String ENABLED = "enabled";
	 
	@Override
	public void onReceive(Context context, Intent intent) {

		if (! PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(ENABLED, false)) {
			return;
		}
		Log.d(logTag,new Boolean(PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(ENABLED, false)).toString());
		Intent serv = new Intent(context, DroidGuardService.class);
		serv.setAction(intent.getAction());
		serv.putExtras(intent);
		DroidGuardService.start(context, serv);
		
	}
}