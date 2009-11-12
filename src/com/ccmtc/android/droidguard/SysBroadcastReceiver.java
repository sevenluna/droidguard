/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Receives system broadcasts of SMS received or phone ringing when the service
 * should be stopped.
 * 
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
		Log.d(logTag, "System broadcast received. Intent action: "
				+ intent.getAction());
		if ((isSmsReceived(intent) && isToStopOnPhoneRinging(context))
				|| isPhoneRinging(intent) && isToStopOnSmsReceived(context)) {
			Log.d(logTag, "Phone ringing or sms received, stopping service.");
			SysNotification.Set(context, SysNotification.NOTIFICATION_STOPPED);
			context.startService(new Intent(context, DroidGuardService.class));
		}
	}

	/**
	 * @param intent
	 * @param action
	 * @return
	 */
	private boolean isPhoneRinging(Intent intent) {
		return (intent.getAction().equalsIgnoreCase(
				TelephonyManager.ACTION_PHONE_STATE_CHANGED) && intent
				.getStringExtra(TelephonyManager.EXTRA_STATE).equalsIgnoreCase(
						TelephonyManager.EXTRA_STATE_RINGING));
	}

	/**
	 * @param action
	 * @return
	 */
	private boolean isSmsReceived(Intent intent) {
		return (intent.getAction()
				.equalsIgnoreCase("android.provider.Telephony.SMS_RECEIVED"));
	}

	/**
	 * @param context
	 * @return
	 */
	private boolean isToStopOnPhoneRinging(Context context) {
		return PrefStore.isStopServiceOnIncomingCall(context);
	}

	/**
	 * @param context
	 * @return
	 */
	private boolean isToStopOnSmsReceived(Context context) {
		return PrefStore.isStopServiceOnSms(context);
	}
}
