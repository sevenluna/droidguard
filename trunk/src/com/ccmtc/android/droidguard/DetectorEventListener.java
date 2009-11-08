/**
 * 
 */
package com.ccmtc.android.droidguard;

import java.util.EventListener;

/**
 * @author Ken
 * 
 */
public interface DetectorEventListener extends EventListener {
	void onChangeDetected(DetectorEvent event);
}
