/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.content.Context;

/**
 * This class manages detectors.
 * 
 * @author Ken
 * 
 */
public final class DetectorManager {

	public static final int DETECTOR_TYPE_ACCELEMETER = 0;

	public static final int DETECTOR_TYPE_ORIENTATION = 1;

	public static final int DETECTOR_COUNT = 2;

	/**
	 * Create a new detector.
	 * 
	 * @param context
	 *            The context of the call.
	 * @param type
	 *            Type of detector to create.
	 * @return The detector.
	 */
	public static Detector createDetector(Context context, int type) {
		switch (type) {
		case DETECTOR_TYPE_ACCELEMETER:
			return new AccelerometerDetector(context);
			// break;
		case DETECTOR_TYPE_ORIENTATION:
			return new OrientationDetector(context);
			// break;
		default:
			return new EmptyDetector(context, type);
			// break;
		}
	}

}
