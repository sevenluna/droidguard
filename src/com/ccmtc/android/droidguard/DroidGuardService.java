/**
 * 
 */
package com.ccmtc.android.droidguard;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * The main service of DroidGuard.
 * 
 * @author Ken
 * 
 */
public class DroidGuardService extends Service implements DetectorEventListener {

	// public static final String EXTRA_STOP_SERVICE =
	// "com.ccmtc.android.droidguard.StopService";

	private static final String logTag = "DroidGuardService";

	private BroadcastReceiver sysBroadcastReceiver;
	private Detector[] detectors;
	private Notifier[] notifiers;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		initDetectors();
		initNotifiers();
		initSysBroadcastReceiver();
	}

	/**
	 * Initialize broadcast receiver.
	 */
	private void initSysBroadcastReceiver() {
		sysBroadcastReceiver = new SysBroadcastReceiver();
	}

	/**
	 * Initialize notifiers.
	 */
	private void initNotifiers() {
		notifiers = new Notifier[NotifierManager.NOTIFIER_COUNT];
		for (int i = 0; i < detectors.length; i++) {
			if (PrefStore.isNotifierSelected(this, i)) {
				Log.d(logTag, "notifier " + i + " is selected.");
				notifiers[i] = NotifierManager.createNotifier(this, i);
			}
		}
	}

	/**
	 * Initialize detectors.
	 */
	private void initDetectors() {
		detectors = new Detector[DetectorManager.DETECTOR_COUNT];
		for (int i = 0; i < detectors.length; i++) {
			if (PrefStore.isDetectorSelected(this, i)) {
				Log.d(logTag, "detector " + i + " is selected.");
				detectors[i] = DetectorManager.createDetector(this, i);
				detectors[i].registerListener(this);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		// TODO (G):Find a better way to determine intent.
		if (1 != startId) {
			stopSelf();
			return;
		}
		super.setForeground(true);

		// Send broadcast.
		Intent widgetIntent = new Intent(this, DroidGuardWidget.class);
		widgetIntent.setAction("com.ccmtc.android.droidguard.ServiceStarted");
		this.sendBroadcast(widgetIntent);

		// Notify the user with a toast notification.
		Toast.makeText(
				this,
				String.format(this.getText(
						R.string.toast_service_about_to_start).toString(),
						PrefStore.getStartGuardWaitSeconds(this)), 5).show();

		// Wait a set time.
		Timer startupTimer = new Timer();
		startupTimer.schedule(new StartupTimerTask(this), PrefStore
				.getStartGuardWaitSeconds(this) * 1000);
	}

	/**
	 * The timer task to perform when startup and wait for a preparing time.
	 * 
	 * @author Ken
	 */
	private class StartupTimerTask extends TimerTask {

		private final Context context;

		/**
		 * Create a new instance of {@link StartupTimerTask}.
		 * 
		 * @param context
		 *            The context of the caller.
		 */
		public StartupTimerTask(Context context) {
			this.context = context;
		}

		@Override
		public void run() {
			startAllDetectors();
			registerSysBroadcasts(context);
			// Set status bar notification.
			SysNotification.Set(context, SysNotification.NOTIFICATION_RUNNING);
			Log.d(logTag, "Service started.");
		}
	}

	/**
	 * 
	 */
	private void registerSysBroadcasts(Context context) {
		context.registerReceiver(sysBroadcastReceiver, new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED"));
		context.registerReceiver(sysBroadcastReceiver, new IntentFilter(
				TelephonyManager.ACTION_PHONE_STATE_CHANGED));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		stopAllDetectors();
		stopAllNotifiers();
		SysNotification.Unset(this, SysNotification.NOTIFICATION_RUNNING);
		this.unregisterReceiver(sysBroadcastReceiver);
		Toast.makeText(this, R.string.toast_service_stopped, 3).show();
		
		// Send broadcast.
		Intent widgetIntent = new Intent(this, DroidGuardWidget.class);
		widgetIntent.setAction("com.ccmtc.android.droidguard.ServiceStopped");
		this.sendBroadcast(widgetIntent);

		Log.d(logTag, "Service stopped.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ccmtc.android.droidguard.DetectorEventListener#onDetectorChangeDetected
	 * (com.ccmtc.android.droidguard.DetectorEvent)
	 */
	@Override
	public void onDetectorChangeDetected(DetectorEvent event) {
		Log.d(logTag, "ChangeLevel: " + event.changeLevel);

		if (event.changeLevel >= PrefStore.getDetectorSensitivity(this,
				event.sourceType)) {
			for (int i = 0; i < NotifierManager.NOTIFIER_COUNT; i++) {
				if (null != notifiers[i] && notifiers[i].canExecute()) {
					Log.d(logTag, "executing notifier " + i);
					notifiers[i].execute();
				}
			}
		}
	}

	/**
	 * Start all selected detectors.
	 */
	private void startAllDetectors() {
		for (Detector detector : detectors) {
			if (null != detector) {
				detector.start();
			}
		}
	}

	/**
	 * Stop all selected detectors.
	 */
	private void stopAllDetectors() {
		for (Detector detector : detectors) {
			if (null != detector) {
				detector.stop();
			}
		}
	}

	/**
	 * Stop all selected notifiers from notifying.
	 */
	private void stopAllNotifiers() {
		for (Notifier notifier : notifiers) {
			if (null != notifier) {
				notifier.stop();
			}
		}
	}
}
