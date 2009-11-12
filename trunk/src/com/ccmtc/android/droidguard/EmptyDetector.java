/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.content.Context;

/**
 * A detector doing nothing. (Null pattern)
 * 
 * @author Ken
 * 
 */
public final class EmptyDetector extends Detector {

	private final int type;

	/**
	 * @param context
	 */
	public EmptyDetector(Context context, int type) {
		super(context);
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Detector#getType()
	 */
	@Override
	public int getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Detector#start()
	 */
	@Override
	public boolean start() {
		// Do nothing.
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccmtc.android.droidguard.Detector#stop()
	 */
	@Override
	public boolean stop() {
		// Do nothing.
		return true;
	}

}
