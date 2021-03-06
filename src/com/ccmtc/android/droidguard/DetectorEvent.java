/**
 * 
 */
package com.ccmtc.android.droidguard;

import java.util.EventObject;

/**
 * @author Ken
 * 
 */
public class DetectorEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4053329384414833796L;

	public final int sourceType;

	public final int changeLevel;

	public DetectorEvent(Object source, int sourceType, int level) {
		super(source);
		this.sourceType = sourceType;
		this.changeLevel = level;
	}

}
