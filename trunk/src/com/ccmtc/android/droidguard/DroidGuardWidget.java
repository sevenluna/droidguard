package com.ccmtc.android.droidguard;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;

public class DroidGuardWidget extends AppWidgetProvider {
	private static final String logTag = "DroidGuardWidget";

	private boolean update;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean op = prefs.getBoolean(IntentReceiver.ENABLED, false);

		prefs.edit().putBoolean(IntentReceiver.ENABLED, (update ? !op : op))
				.commit();

		final RemoteViews remote = new RemoteViews(context.getPackageName(),
				R.layout.widget);

		int resource = (update ? !op : op) ? R.drawable.on : R.drawable.off;

		Log.d(logTag, new Boolean(op).toString());
		Log.d(logTag, new Boolean(update).toString());

		remote.setImageViewResource(R.id.img, resource);
		remote.setOnClickPendingIntent(R.id.img, PendingIntent.getBroadcast(
				context, 0, new Intent(context, DroidGuardWidget.class)
						.setAction("UPDATE"), 0));

		ComponentName cn = new ComponentName(context, DroidGuardWidget.class);
		appWidgetManager.updateAppWidget(cn, remote);

		if ((update ? !op : op)) {
			Intent intent = new Intent(context, DroidGuardService.class);
			context.startService(intent);

		} else {
			Intent intent = new Intent(context, DroidGuardService.class);
			context.stopService(intent);

		}

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// funny things happen unless we intercept all broadcasts and
		// call onUpdate ourselves
		String action = intent.getAction();

		if (action != null) {
			if (action.equals("UPDATE")
					|| intent.getAction()
					.equalsIgnoreCase("android.provider.Telephony.SMS_RECEIVED")
					||( intent.getAction().equalsIgnoreCase(
							TelephonyManager.ACTION_PHONE_STATE_CHANGED)
					&& intent.getStringExtra(TelephonyManager.EXTRA_STATE)
							.equalsIgnoreCase(
									TelephonyManager.EXTRA_STATE_RINGING))) {
				update = true;
				final AppWidgetManager manager = AppWidgetManager
						.getInstance(context);
				onUpdate(context, manager, manager
						.getAppWidgetIds(new ComponentName(context,
								DroidGuardWidget.class)));
			}

			else if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
				super.onReceive(context, intent);
			}

			else {
				super.onReceive(context, intent);
			}
		}

		else {
			super.onReceive(context, intent);
		}
	}

}
