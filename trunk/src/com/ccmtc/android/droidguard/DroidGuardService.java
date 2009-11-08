/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author Ken
 *
 */
public class DroidGuardService extends Service implements DetectorEventListener{

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate(){
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId){
		super.onStart(intent, startId);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	@Override
	public void onDetectorChangeDetected(DetectorEvent event) {
		// TODO Auto-generated method stub
		
	}
}
