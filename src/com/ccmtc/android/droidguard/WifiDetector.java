package com.ccmtc.android.droidguard;

import java.util.Date;
import java.util.List;
import java.util.Timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.Button;
import android.widget.TextView;

public class WifiDetector extends Detector{
	WifiManager mainWifi;
	WifiReceiver receiverWifi;
	List<ScanResult> wifiList;
	int num;
	Timer timer = new Timer();
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

        //registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		mainWifi.setWifiEnabled(true);
        
        
        timer.schedule(new MyTask(),0,5000);
      
        
		return true;
	}
	

	class MyTask extends java.util.TimerTask{

        @Override
        public void run() {
        	mainWifi.startScan();
        }
    }

	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		return false;
	}
	

	class WifiReceiver extends BroadcastReceiver {

		public void onReceive(Context c, Intent intent) {
			
			num = 0;
			wifiList = mainWifi.getScanResults();

			for (int i = 0; i < wifiList.size(); i++) {
				
				if (wifiList.get(i).level >= (-55)) {
					num++;
				}
			}

	
			
		}
	}

}
