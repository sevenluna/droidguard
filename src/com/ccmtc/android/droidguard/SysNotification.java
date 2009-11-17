/**
 * 
 */
package com.ccmtc.android.droidguard;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Creates notifications in the system notification bar.
 * 
 * @author Ken
 * 
 */
public class SysNotification {

	/**
	 * A notification indicates that service is running.
	 */
	public static final int NOTIFICATION_RUNNING = 1;

	/**
	 * A notification indicates that service has been stopped and can be
	 * restarted.
	 */
	public static final int NOTIFICATION_STOPPED = 2;

	/**
	 * Set a notification of the specified event. See {@link SysNotification}
	 * for all available events. If the event is not valid, this request will be
	 * ignored.
	 * 
	 * @param context
	 *            Context of the caller.
	 * @param event
	 *            The event to be notified about.
	 */
	public static void Set(Context context, int event) {
		NotificationManager mgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		switch (event) {
		case NOTIFICATION_RUNNING:
			notifyRunning(context, mgr);
			break;
		case NOTIFICATION_STOPPED:
			notifyStopped(context, mgr);
			break;
		default:
			break;
		}
	}

	/**
	 * Create a notification notifying the user of service stopped and can be
	 * restarted.
	 * 
	 * @param context
	 *            Context of the caller.
	 * @param mgr
	 *            The {@link NotificationManager} instance.
	 */
	private static void notifyStopped(Context context, NotificationManager mgr) {
		Notification notification = new Notification(R.drawable.icon, context
				.getText(R.string.notification_service_stopped), System
				.currentTimeMillis());
		Intent intent = new Intent(context, DroidGuardService.class);
		PendingIntent contentIntent = PendingIntent.getService(context, 0,
				intent, 0);
		notification.setLatestEventInfo(context, context
				.getText(R.string.app_name), context
				.getText(R.string.notification_service_stopped), contentIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		mgr.notify(NOTIFICATION_STOPPED, notification);
	}

	/**
	 * Create a notification notifying the user of service running.
	 * 
	 * @param context
	 *            Context of the caller.
	 * @param mgr
	 *            The {@link NotificationManager} instance.
	 */
	private static void notifyRunning(Context context, NotificationManager mgr) {
		Notification notification = new Notification(R.drawable.icon, context
				.getText(R.string.notification_service_running), System
				.currentTimeMillis());
		Intent intent = new Intent(context, DroidGuardService.class);
		PendingIntent contentIntent = PendingIntent.getService(context, 0,
				intent, 0);
		notification.setLatestEventInfo(context, context
				.getText(R.string.app_name), context
				.getText(R.string.notification_service_running), contentIntent);
		notification.flags = Notification.FLAG_ONGOING_EVENT
				| Notification.FLAG_NO_CLEAR;
		mgr.notify(NOTIFICATION_RUNNING, notification);
	}

	/**
	 * Cancel the notification of the specified type. If the event is not valid,
	 * this request will be ignored.
	 * 
	 * @param context
	 *            Context of the caller.
	 * @param event
	 *            The event to be canceled.
	 */
	public static void Unset(Context context, int event) {
		NotificationManager mgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mgr.cancel(event);
	}
}
