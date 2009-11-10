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
 * @author Ken
 * 
 */
public class SysNotification {

	public static final int NOTIFICATION_RUNNING_ID = 1;

	public static void Set(Context context) {
		NotificationManager mgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon, context
				.getText(R.string.notification_service_running), System
				.currentTimeMillis());
		Intent intent = new Intent();
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		notification.setLatestEventInfo(context, context
				.getText(R.string.app_name), context
				.getText(R.string.notification_service_running), contentIntent);
		mgr.notify(NOTIFICATION_RUNNING_ID, notification);
	}

	public static void Unset(Context context) {
		NotificationManager mgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mgr.cancel(NOTIFICATION_RUNNING_ID);
	}
}
