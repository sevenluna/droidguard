/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.content.Context;
import android.os.Vibrator;

/**
 * This notifier vibrates the phone as its notifying method.
 * 
 * @author Ken
 * 
 */
public class VibrationNotifier extends Notifier {

	/**
	 * The time of vibration one time in milliseconds.
	 */
	public static final long VIBRATE_ONCE_TIME = 5000;

	private final Vibrator vibrator;

	/**
	 * 
	 * @param context
	 */
	public VibrationNotifier(Context context) {
		super(context);
		vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Notifier#canExecute()
	 */
	@Override
	public boolean canExecute() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Notifier#execute()
	 */
	@Override
	public void execute() {
		vibrator.vibrate(VIBRATE_ONCE_TIME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Notifier#stop()
	 */
	@Override
	public void stop() {
		vibrator.cancel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Notifier#getType()
	 */
	@Override
	public int getType() {
		return NotifierManager.NOTIFIER_TYPE_VIBERATION;
	}

}
