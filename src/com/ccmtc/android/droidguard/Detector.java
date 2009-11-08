/**
 * 
 */
package com.ccmtc.android.droidguard;

import java.util.Vector;

import android.content.Context;

/**
 * @author Ken
 * 
 */
public abstract class Detector {

	public static final int DETECTOR_CHANGELEVEL_TINY = 0;

	public static final int DETECTOR_CHANGELEVEL_LOW = 1;

	public static final int DETECTOR_CHANGELEVEL_NORMAL = 2;

	public static final int DETECTOR_CHANGELEVEL_HIGH = 3;

	public static final int DETECTOR_CHANGELEVEL_SIGNIFICANT = 4;

	private final Vector<DetectorEventListener> listeners = new Vector<DetectorEventListener>();
	
	protected final Context context;

	protected Detector(Context context) {
		this.context = context;
	}
	
	public abstract int getType(); 
	
	public abstract boolean start();
	
	public abstract boolean stop();

	public final boolean registerListener(DetectorEventListener listener) {
		return listeners.add(listener);
	}

	public final boolean unregisterListener(DetectorEventListener listener) {
		return listeners.remove(listener);
	}
	
	protected void onDetectorEvent(DetectorEvent event){
		for(DetectorEventListener listener:listeners){
			listener.onChangeDetected(event);
		}
	}
}
