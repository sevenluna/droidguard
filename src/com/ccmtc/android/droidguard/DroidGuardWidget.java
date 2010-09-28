package com.ccmtc.android.droidguard;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.util.Log;

public class DroidGuardWidget extends AppWidgetProvider {
	private static final String logTag = "DroidGuardWidget";
	private boolean update;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean op = prefs.getBoolean(IntentReceiver.ENABLED, true);
		Log.d(logTag, "op="+op);
		prefs.edit().putBoolean(IntentReceiver.ENABLED, !op).commit();
		Log.d(logTag, "op="+op);
		final RemoteViews remote = new RemoteViews(context.getPackageName(),
				R.layout.widget);

		int resource = op ? R.drawable.on : R.drawable.off;

		remote.setImageViewResource(R.id.img, resource);
		remote.setOnClickPendingIntent(R.id.img, PendingIntent.getBroadcast(
				context, 0, new Intent(context, DroidGuardWidget.class)
						.setAction("UPDATE"), 0));

		ComponentName cn = new ComponentName(context, DroidGuardWidget.class);
		appWidgetManager.updateAppWidget(cn, remote);
		Intent intent = new Intent(context, DroidGuardService.class);
		
		if ( op ) {
			context.startService(intent);
			Log.d(logTag, "update="+update+";op="+op+";start");
		} else {
			context.stopService(intent);
			Log.d(logTag, "update="+update+";op="+op+";stop");
		}

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// funny things happen unless we intercept all broadcasts and
		// call onUpdate ourselves
		String action = intent.getAction();
		Log.d(logTag, action.toString());
		if (action != null) {

			if (action.equals("UPDATE")) {
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
			else if (action.equals("APPWIDGET_UPDATE")) {
				super.onReceive(context, intent);
			}

			else {
				super.onReceive(context, intent);
			}
		} else {
			super.onReceive(context, intent);
		}
	}

}
