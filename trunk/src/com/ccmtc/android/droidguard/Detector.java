/**
 * 
 */
package com.ccmtc.android.droidguard;

import java.util.Vector;

import android.content.Context;

/**
 * The super class of detectors.
 * 
 * @author Ken
 */
public abstract class Detector {

	/**
	 * Very small change. Only satisfied in a perfect environment and almost
	 * always misalarms in common situation.
	 */
	public static final int DETECTOR_CHANGELEVEL_TINY = 0;

	/**
	 * Small change. Can be satisfied in a good environment in reality and
	 * rarely misalarms.
	 */
	public static final int DETECTOR_CHANGELEVEL_LOW = 1;

	/**
	 * Medium change. Should be the best choice in most situations and never
	 * misalarms when used correctly.
	 */
	public static final int DETECTOR_CHANGELEVEL_MEDIUM = 2;

	/**
	 * Big change. Never misalarms in most use.
	 */
	public static final int DETECTOR_CHANGELEVEL_HIGH = 3;

	/**
	 * Significant change. Never misalarms.
	 */
	public static final int DETECTOR_CHANGELEVEL_SIGNIFICANT = 4;

	private final Vector<DetectorEventListener> listeners = new Vector<DetectorEventListener>();

	/**
	 * The context of the {@link Detector}, e.g. {@link Activity} or
	 * {@link Service}.
	 */
	protected final Context context;

	protected Detector(Context context) {
		this.context = context;
	}

	/**
	 * Get type of {@link Detector}. The value should be a constant defined in
	 * {@link DetectorManager}.
	 * 
	 * @return Type of the {@link Detector} instance.
	 */
	public abstract int getType();

	/**
	 * Start the detector.
	 * 
	 * @return True if successful. Otherwise, false.
	 */
	public abstract boolean start();

	/**
	 * Stops the detector.
	 * 
	 * @return True if successful. Otherwise, false.
	 */
	public abstract boolean stop();

	/**
	 * Register a {@link DetectorEventListener} object to listen to the
	 * {@link Detector} instance's DetectorChangeDetected event.
	 * 
	 * @param listener The {@link DetectorEventListener} object to register.
	 * @return True if successful. Otherwise, false.
	 */
	public final boolean registerListener(DetectorEventListener listener) {
		return listeners.add(listener);
	}

	/**
	 * Unregister a {@link DetectorEventListener} object to listen to the
	 * {@link Detector} instance's DetectorChangeDetected event.
	 * 
	 * @param listener The {@link DetectorEventListener} object to unregister.
	 * @return True if successful. Otherwise, false.
	 */
	public final boolean unregisterListener(DetectorEventListener listener) {
		return listeners.remove(listener);
	}

	/**
	 * Raise a {@link DetectorChangeDetected} event.
	 * 
	 * @param changeLevel
	 *            The level of the change. Should be constants defined in
	 *            {@link Detector}.
	 */
	protected void onDetectorEvent(int changeLevel) {
		for (DetectorEventListener listener : listeners) {
			listener.onDetectorChangeDetected(new DetectorEvent(
					this, getType(), changeLevel));
		}
	}
}
