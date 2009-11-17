package com.ccmtc.android.droidguard;

import java.util.List;
import java.util.Timer;
import java.util.Vector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiDetector extends Detector {
	static private final int InitWifiLevel = -55;
	static private final int AlertWifiLevel = -60;
	static WifiManager mainWifi;
	static WifiReceiver receiverWifi;
	static int flag = 0;
	static List<ScanResult> wifiList;
	static Vector<ScanResult> wifiListResult = new Vector<ScanResult>();
	Timer timer = new Timer();
	static int num = 0;
	


	protected WifiDetector(Context context) {
		super(context);
		
		mainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		receiverWifi= new WifiReceiver(this);
		context.registerReceiver(receiverWifi, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		Log.d("WifiDetector", "OK");
	}


	@Override
	public int getType() {
		return DetectorManager.DETECTOR_TYPE_WIFI;
	}

	@Override
	public boolean start() {
		Log.d("WifiDetector", "start");

		timer.schedule(new MyTask(), 0, 5000);
		return true;
	}

	class MyTask extends java.util.TimerTask {
		
		
		@Override
		public void run() {
			mainWifi.startScan();
			
			Log.d("MyTask", "scan OK");
			
		}
	}

	private static int compareValues() {

		int changeLevel = Detector.DETECTOR_CHANGELEVEL_TINY;

		

		int newChangeLevel = digitToChangeLevel(num);
		if (changeLevel < newChangeLevel) {
			changeLevel = newChangeLevel;
		}
		return changeLevel;
	}

	@Override
	public boolean stop() {

		timer.cancel();
		Log.d("unregister", receiverWifi.toString());
		context.unregisterReceiver(receiverWifi);
		return true;
	}

	private static int digitToChangeLevel(int num) {
		Log.d("digitToChangeLevel",new Integer(num).toString());
		if (5 == num) {
			return Detector.DETECTOR_CHANGELEVEL_TINY;
		}
		if (4 == num) {
			return Detector.DETECTOR_CHANGELEVEL_LOW;
		}
		if (3 == num) {
			return Detector.DETECTOR_CHANGELEVEL_MEDIUM;
		}
		if (2 == num) {
			return Detector.DETECTOR_CHANGELEVEL_HIGH;
		}
		return Detector.DETECTOR_CHANGELEVEL_SIGNIFICANT;
	}

	class WifiReceiver extends BroadcastReceiver {
		
		private final WifiDetector wifiDetector;

		
		public WifiReceiver(WifiDetector wifiDetector) {
			this.wifiDetector = wifiDetector;
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("WifiReceiver", action.toString());
			Log.d("WifiReceiver", receiverWifi.toString());
			if (action != null) {
				if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {

					wifiList = mainWifi.getScanResults();
				}
			}
			
			
			if (0 == flag) {
				
				Log.d("WifiReceiver", wifiList.toString());
				for (int i = 0; i < wifiList.size(); i++) {

					if (wifiList.get(i).level > (InitWifiLevel)) {
						Log.d("WifiReceiver_wifiList", wifiList.get(i).toString());
						try {
							wifiListResult.add(wifiList.get(i));
						} catch (Exception e) {
							Log.d("WifiReceiver", e.toString());
						}

						Log.d("WifiReceiver_wifiListResult", wifiListResult.toString());
					}

				}
				flag = 1;
			}
			
			for (ScanResult InitResult : wifiListResult) {
				int flag=0;
				for (ScanResult scanResult : wifiList) {
					if (scanResult.SSID.equals(InitResult.SSID) ) {
						if(scanResult.level<AlertWifiLevel){
							num++;
							int changeLevel = compareValues();
							Log.d("changelevel",new Integer(changeLevel).toString());
							wifiDetector.onDetectorEvent(changeLevel);
						}
					}
					else{
						flag++;
					}
				}

				if(flag==wifiList.size()){
					num++;
					int changeLevel = compareValues();
					Log.d("changelevel",new Integer(changeLevel).toString());
					wifiDetector.onDetectorEvent(changeLevel);
				}
			}
			
			

		}

	}

}
