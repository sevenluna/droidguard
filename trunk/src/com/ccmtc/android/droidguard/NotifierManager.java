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

	/**
	 * This notifier rings the phone with the current notification ringtone as
	 * its notifying method.
	 */
	public static final int NOTIFIER_TYPE_RINGTONE = 0;

	/**
	 * This notifier vibrates the phone with the current notification ringtone
	 * as its notifying method.
	 */
	public static final int NOTIFIER_TYPE_VIBERATION = 1;

	public static final int NOTIFIER_TYPE_CALL = 2;

	/**
	 * Total count of all available notifiers.
	 */
	public static final int NOTIFIER_COUNT = 3;

	/**
	 * Create a specified notifier. Developer should use this method instead of
	 * creating notifiers directly with their constructors.
	 * 
	 * @param context
	 *            Context of the caller.
	 * @param type
	 *            Type of the notifier to be created. See
	 *            {@link NotifierManager} for all available types.
	 * @return The notifier created. If the type is not valid, an
	 *         {@link EmptyNotifier} is created.
	 */
	public static Notifier createNotifier(Context context, int type) {
		switch (type) {
		case NOTIFIER_TYPE_RINGTONE:
			return new RingtoneNotifier(context);
			// break;
		case NOTIFIER_TYPE_VIBERATION:
			return new VibrationNotifier(context);
			// break;
		default:
			return new EmptyNotifier(context, type);
			// break;
		}
	}

}
