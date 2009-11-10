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
	 * Preference selected {@link Notifier} types and settings.
	 */
	private static final String PREF_SELECTED_NOTIFIERS = "notifiers";

	/**
	 * Preference value of seconds to wait before guarding starts.
	 */
	private static final String PREF_START_GUARD_WAIT_SECONDS = "start_guard_wait_seconds";

	/**
	 * Preference sensitivities of detectors.
	 */
	private static final String PREF_DETECTOR_SENSITIVITIES = "detector_sensitivities";

	/**
	 * The default value of {@link #PREF_SELECTED_DETECTORS} preferences.
	 */
	private static final long DEFAULT_SELECTED_DETECTORS = 0;

	/**
	 * The default value of {@link #PREF_START_GUARD_WAIT_SECONDS} preferences.
	 */
	private static final int DEFAULT_START_GUARD_WAIT_SECONDS = 5;

	/**
	 * The default value of {@link #PREF_DETECTOR_SENSITIVITIES} preferences.
	 */
	private static final String DEFAULT_DETECTOR_SENSITIVITIES = "";

	/**
	 *The default value of {@link #PREF_SELECTED_NOTIFIERS} preferences.
	 */
	private static final long DEFAULT_SELECTED_NOTIFIERS = 0;

	/**
	 * The default detector sensitivity setting.
	 */
	public static final int DEFAULT_DETECTOR_SENSITIVITY = Detector.DETECTOR_CHANGELEVEL_MEDIUM;

	/**
	 * Get the {@link SharedPreferences} of this application.
	 * 
	 * @param context
	 *            The context of the call.
	 * @return The {@link SharedPreferences} retrieved.
	 */
	private static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static void resetAll(Context context) {
		getSharedPreferences(context).edit()
				.putInt(PREF_START_GUARD_WAIT_SECONDS,
						DEFAULT_START_GUARD_WAIT_SECONDS).putLong(
						PREF_SELECTED_DETECTORS, DEFAULT_SELECTED_DETECTORS)
				.putString(PREF_DETECTOR_SENSITIVITIES,
						DEFAULT_DETECTOR_SENSITIVITIES).putLong(
						PREF_SELECTED_NOTIFIERS, DEFAULT_SELECTED_NOTIFIERS)
				.commit();

	}

	/**
	 * Get the value of seconds to wait before guarding starts, giving user the
	 * time to prepare.
	 * 
	 * @param context
	 *            The context of the call.
	 * @return The retrieved value.
	 */
	public static int getStartGuardWaitSeconds(Context context) {
		return getSharedPreferences(context)
				.getInt(PREF_START_GUARD_WAIT_SECONDS,
						DEFAULT_START_GUARD_WAIT_SECONDS);
	}

	/**
	 * Set the value of seconds to wait before guarding starts.
	 * 
	 * @param context
	 *            The context of the call.
	 * @param value
	 *            The value to set.
	 */
	public static void setStartGuardWaitSeconds(Context context, int value) {
		getSharedPreferences(context).edit().putInt(
				PREF_START_GUARD_WAIT_SECONDS, value).commit();
	}

	private static long typeToBitwise(int detectorType) {
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
	 * Gets the {@link #PREF_SELECTED_NOTIFIERS} preferences value.
	 * 
	 * @param context
	 *            The context of the preferences.
	 * @return The value of the preferences.
	 */
	private static long getBitwiseSelectedNotifiers(Context context) {
		return getSharedPreferences(context).getLong(PREF_SELECTED_NOTIFIERS,
				DEFAULT_SELECTED_NOTIFIERS);
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
		return (selectedDetectors & typeToBitwise(detectorType)) != 0;
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
		getSharedPreferences(context).edit().putLong(
				PREF_SELECTED_DETECTORS,
				value ? selectedDetectors | typeToBitwise(detectorType)
						: selectedDetectors & ~typeToBitwise(detectorType))
				.commit();
	}

	/**
	 * Parse a string containing a list of integers separated by "|" into
	 * integers.
	 * 
	 * @param value
	 *            The string to parse.
	 * @return The parsed integer array. If the string contains no integers,
	 *         {@code null} is returned.
	 */
	private static int[] parseInts(String value) {
		if (value.trim() == "") {
			return new int[0];
		}

		String[] splitValues = value.split("\\|");
		int[] result = new int[splitValues.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Integer.parseInt(splitValues[i]);
		}
		return result;
	}

	/**
	 * Join an array of integers into a string containing these integers
	 * separated by "-".
	 * 
	 * @param values
	 *            The integer array.
	 * @return The generated string.
	 */
	private static String joinInts(int[] values) {
		// If values contains a mass of elements, use StringBuilder instead.
		if (values.length == 0) {
			return "";
		}
		String result = Integer.toString(values[0]);
		for (int i = 0; i < values.length; i++) {
			result += "|" + values[i];
		}
		return result;
	}

	/**
	 * Get the sensitivity setting of the specified type of {@link Detector}. If
	 * it is not set, {@link #DEFAULT_DETECTOR_SENSITIVITY} is returned.
	 * 
	 * @param context
	 *            The context of the call.
	 * @param detectorType
	 *            The type of the {@link Detector}.
	 * @return The sensitivity setting.
	 */
	public static int getDetectorSensitivity(Context context, int detectorType) {
		String pref = getSharedPreferences(context).getString(
				PREF_DETECTOR_SENSITIVITIES, DEFAULT_DETECTOR_SENSITIVITIES);
		int[] currentValues = parseInts(pref);
		if (currentValues.length <= detectorType) {
			return DEFAULT_DETECTOR_SENSITIVITY;
		} else {
			return currentValues[detectorType];
		}
	}

	/**
	 * Set the sensitivity of the specified type of {@link Detector}.
	 * 
	 * @param context
	 *            The context of the call.
	 * @param detectorType
	 *            The type of the {@link Detector}.
	 * @param value
	 *            The sensitivity to set. See {@link Detector} for sensitivity
	 *            definitions.
	 */
	public static void setDetectorSensitivity(Context context,
			int detectorType, int value) {
		String pref = getSharedPreferences(context).getString(
				PREF_DETECTOR_SENSITIVITIES, DEFAULT_DETECTOR_SENSITIVITIES);
		int[] currentValues = parseInts(pref);
		int[] values = null;
		// If value of this detector has never been set, expand the array.
		if (currentValues.length <= detectorType) {
			values = new int[detectorType + 1];
			System.arraycopy(currentValues, 0, values, 0, currentValues.length);
		} else {
			values = currentValues;
		}
		values[detectorType] = value;
		getSharedPreferences(context).edit().putString(
				PREF_DETECTOR_SENSITIVITIES, joinInts(values)).commit();
	}

	/**
	 * Gets the state of a {@link Notifier}.
	 * 
	 * @param context
	 *            The context of this call.
	 * @param detectorType
	 *            Type of the {@link Notifier}.
	 * @return The state of the {@link Notifier}.
	 */
	public static boolean isNotifierSelected(Context context, int notifierType) {
		long selectedNotifiers = getBitwiseSelectedNotifiers(context);
		return (selectedNotifiers & typeToBitwise(notifierType)) != 0;
	}

	/**
	 * Toggles a {@link Notifier}.
	 * 
	 * @param context
	 *            The context of this call.
	 * @param notifierType
	 *            The type of {@link Notifier} to toggle. See
	 *            {@link NotifierManager} for type definition.
	 * @param value
	 *            The target value of the notifier state.
	 */
	public static void toggleNotifier(Context context, int notifierType,
			boolean value) {
		long selectedNotifiers = getBitwiseSelectedNotifiers(context);
		getSharedPreferences(context).edit().putLong(
				PREF_SELECTED_NOTIFIERS,
				value ? selectedNotifiers | typeToBitwise(notifierType)
						: selectedNotifiers & ~typeToBitwise(notifierType))
				.commit();
	}
}
