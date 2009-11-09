/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.content.Context;

/**
 * The super class for all notifiers.
 * 
 * @author Ken
 * 
 */
public abstract class Notifier extends Object {

	/**
	 * The context of the {@link Notifier}, e.g. {@link Activity} or
	 * {@link Service}.
	 */
	protected final Context context;

	/**
	 * Creates a new instance of {@link Notifier} with a given context.
	 * 
	 * @param context
	 *            The context with the notifier.
	 */
	protected Notifier(Context context) {
		this.context = context;
	}

	/**
	 * Gets if this {@link Notifier} can execute a notification.
	 * 
	 * @return True if a notification can be executed currently. Otherwise,
	 *         false.
	 */
	public abstract boolean canExecute();

	/**
	 * Execute a notification.
	 */
	public abstract void execute();

	/**
	 * If the notification is currently going, stop it.
	 */
	public abstract void stop();
}
