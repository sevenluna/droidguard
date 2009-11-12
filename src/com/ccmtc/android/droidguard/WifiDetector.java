package com.ccmtc.android.droidguard;

import java.util.List;
import java.util.Timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiDetector extends Detector {
	WifiManager mainWifi;
	WifiReceiver receiverWifi;

	int num;
	Timer timer = new Timer();
	Context mainContext;
	List<ScanResult> wifiListResult;

	protected WifiDetector(Context context) {
		super(context);
		mainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		
	}

	@Override
	public int getType() {
		return DetectorManager.DETECTOR_TYPE_WIFI;
	}

	@Override
	public boolean start() {
		
		receiverWifi = new WifiReceiver();
		Log.d("Wifi-start", context.toString());
		mainContext.registerReceiver(receiverWifi, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		receiverWifi = new WifiReceiver();
		Log.d("WifiDetector", mainWifi.toString());
		// registerReceiver(receiverWifi, new
		// IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

		Log.d("WifiDetector", "WifiDetector On");
		timer.schedule(new MyTask(), 0, 5000);

		return true;
	}

	class MyTask extends java.util.TimerTask {

		@Override
		public void run() {
			mainWifi.startScan();

		}
	}

	@Override
	public boolean stop() {
		timer.cancel();
		mainContext.unregisterReceiver(receiverWifi);
		return true;
	}

	class WifiReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context c, Intent intent) {
			List<ScanResult> wifiList;
			String action = intent.getAction();
			Log.d("Wifi_Receiver", "Receiver" + action);
			Log.d("Wifi_Receiver", c.toString());
			 if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
				 Log.d("Wifi_Receiver", "SCAN_RESULTS_AVAILABLE_ACTION");
				 Log.d("Wifi_Receiver", mainWifi.toString());
			 }


			
			// Log.d("Wifi", wifiList.toString());
			// int num=0;
			//        	
			// for (int i = 0; i < wifiList.size(); i++) {
			//				
			// if (wifiList.get(i).level >= (-55)) {
			// num++;
			// Log.d("WifiDetector_num",new Integer(num).toString());
			// }
			// }

		}
	}

}
