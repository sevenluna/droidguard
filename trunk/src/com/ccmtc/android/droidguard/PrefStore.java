/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Stores and manages preferences.
 * 
 * @author Ken
 * 
 */
public class PrefStore {

	/**
	 * Preference selected {@link Detector} types.
	 */
	private static final String PREF_SELECTED_DETECTORS = "selected_detectors";

	/**
	 * The default value of {@link #PREF_SELECTED_DETECTORS} preferences.
	 */
	private static final long DEFAULT_SELECTED_DETECTORS = 0;

	static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	private static long detectorTypeToBitwise(int detectorType) {
		return 0x1 << detectorType;
	}

	/**
	 * Gets the {@link #PREF_SELECTED_DETECTORS} preferences value.
	 * 
	 * @param context
	 *            The context of the preferences.
	 * @return The value of the preferences.
	 */
	private static long getBitwiseSelectedDetectors(Context context) {
		return getSharedPreferences(context).getLong(PREF_SELECTED_DETECTORS,
				DEFAULT_SELECTED_DETECTORS);
	}

	/**
	 * Gets the state of a {@link Detector}.
	 * 
	 * @param context
	 *            The context of this call.
	 * @param detectorType
	 *            Type of the {@link Detector}.
	 * @return The state of the detector.
	 */
	public static boolean isDetectorSelected(Context context, int detectorType) {
		long selectedDetectors = getBitwiseSelectedDetectors(context);
		return (selectedDetectors & detectorTypeToBitwise(detectorType)) != 0;
	}

	/**
	 * Toggles a {@link Detector}.
	 * 
	 * @param context
	 *            The context of this call.
	 * @param detectorType
	 *            The type of {@link Detector} to toggle. See
	 *            {@link DetectorManager} for type definition.
	 * @param value
	 *            The target value of the detector state.
	 */
	public static void toggleDetector(Context context, int detectorType,
			boolean value) {
		long selectedDetectors = getBitwiseSelectedDetectors(context);
		SharedPreferences.Editor editor = getSharedPreferences(context).edit();
		editor.putLong(PREF_SELECTED_DETECTORS, value ? selectedDetectors
				| detectorTypeToBitwise(detectorType) : selectedDetectors
				& ~detectorTypeToBitwise(detectorType));
		editor.commit();
	}
}
