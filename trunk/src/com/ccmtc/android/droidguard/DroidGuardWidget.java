package com.ccmtc.android.droidguard;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
		Log.d(logTag, new Boolean(op).toString());
		prefs.edit().putBoolean(IntentReceiver.ENABLED, (update ? !op : op))
				.commit();

		final RemoteViews remote = new RemoteViews(context.getPackageName(),
				R.layout.widget);

		int resource = (update ? !op : op) ? R.drawable.on : R.drawable.off;

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

	private void Change(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds, Intent intent) {

		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean op = prefs.getBoolean(IntentReceiver.ENABLED, false);
		int resource = (update ? !op : op) ? R.drawable.on : R.drawable.off;
		if ((intent.getAction().equals(
				"com.ccmtc.android.droidguard.ServiceStarted") && (false))
				|| (intent.getAction().equals(
						"com.ccmtc.android.droidguard.ServiceStopped") && (true))) {
			prefs.edit()
					.putBoolean(IntentReceiver.ENABLED, (update ? !op : op))
					.commit();

			final RemoteViews remote = new RemoteViews(
					context.getPackageName(), R.layout.widget);
			Log.d("change", new Boolean(op).toString());

			Log.d("change", "ok");
			remote.setImageViewResource(R.id.img, resource);
			ComponentName cn = new ComponentName(context,
					DroidGuardWidget.class);
			appWidgetManager.updateAppWidget(cn, remote);
		}

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// funny things happen unless we intercept all broadcasts and
		// call onUpdate ourselves
		String action = intent.getAction();
		Log.d("onReceive", intent.toString());
		if (action != null) {
			Log.d("onReceive", action.toString());
			if (action.equals("UPDATE")) {
				update = true;
				Log.d("onReceive", "if");
				final AppWidgetManager manager = AppWidgetManager
						.getInstance(context);
				onUpdate(context, manager, manager
						.getAppWidgetIds(new ComponentName(context,
								DroidGuardWidget.class)));
			}

			else {
				update = true;
				Log.d("onReceive", "else");
				final AppWidgetManager manager = AppWidgetManager
						.getInstance(context);
				Change(context, manager, manager
						.getAppWidgetIds(new ComponentName(context,
								DroidGuardWidget.class)), intent);
			}

		}

		else {
			super.onReceive(context, intent);
		}
	}

}
