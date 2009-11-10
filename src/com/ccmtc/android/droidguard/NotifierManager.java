/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.content.Context;

/**
 * @author Ken
 * 
 */
public final class NotifierManager {

	public static final int NOTIFIER_TYPE_RINGTONE = 0;

	public static final int NOTIFIER_TYPE_VIBERATION = 1;
	
	public static final int NOTIFIER_TYPE_CALL = 2;

	public static final int NOTIFIER_COUNT = 3;

	public static Notifier createNotifier(Context context, int type) {
		switch (type) {
		case NOTIFIER_TYPE_RINGTONE:
			return new RingtoneNotifier(context);
			// break;
		case NOTIFIER_TYPE_VIBERATION:
			return new VibrationNotifier(context);
			// break;
		default:
			return null;
			// break;
		}
	}

}
