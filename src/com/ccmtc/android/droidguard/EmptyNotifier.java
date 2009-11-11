/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.content.Context;

/**
 * A notifier doing nothing.
 * 
 * @author Ken
 * 
 */
public final class EmptyNotifier extends Notifier {

	/**
	 * Type of the notifier pretends to be.
	 */
	private final int type;

	/**
	 * Create a new instance of {@link EmptyNotifier}.
	 * 
	 * @param context
	 *            Context of the notifier.
	 * @param type
	 *            Type of the notifier pretends to be.
	 */
	public EmptyNotifier(Context context, int type) {
		super(context);
		this.type = type;
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
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Notifier#getType()
	 */
	@Override
	public int getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Notifier#stop()
	 */
	@Override
	public void stop() {
		// Do nothing.
	}

}
