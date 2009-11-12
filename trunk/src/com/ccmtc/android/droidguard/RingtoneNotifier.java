/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;

/**
 * This notifier rings the phone with the current notification ringtone as its
 * notifying method.
 * 
 * @author Ken
 * 
 */
public final class RingtoneNotifier extends Notifier {

	/**
	 * Create a new instance of {@link RingtoneNotifier}. Do not use this
	 * constructor, use {@link NotifierManager#createNotifier(Context, int)}
	 * instead.
	 * 
	 * @param context
	 *            The context of the caller.
	 */
	protected RingtoneNotifier(Context context) {
		super(context);
		ringtone = RingtoneManager.getRingtone(context,
				android.provider.Settings.System.DEFAULT_RINGTONE_URI);
	}

	private Ringtone ringtone;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Notifier#canExcute()
	 */
	@Override
	public boolean canExecute() {
		return null != ringtone && !ringtone.isPlaying();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Notifier#excute()
	 */
	@Override
	public void execute() {
		ringtone.play();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Notifier#stop()
	 */
	@Override
	public void stop() {
		if (null != ringtone && ringtone.isPlaying()) {
			ringtone.stop();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Notifier#getType()
	 */
	@Override
	public int getType() {
		return NotifierManager.NOTIFIER_TYPE_RINGTONE;
	}

}
