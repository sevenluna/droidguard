package com.ccmtc.android.droidguard;

import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.Toast;

public class DroidGuardWidget extends AppWidgetProvider  {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
		final RemoteViews remote = new RemoteViews(context.getPackageName(), R.layout.widget);
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		boolean op = prefs.getBoolean("enabled", false);
		
		int resource =!op ? R.drawable.on : R.drawable.off;
		
		remote.setImageViewResource(R.id.img, resource);
		remote.setOnClickPendingIntent(R.id.img, 
        		PendingIntent.getBroadcast(
        				context, 0, 
        				new Intent(context, DroidGuardWidget.class)
        					.setAction("UPDATE"), 
        				0)
        );
		
		ComponentName cn = new ComponentName(context, DroidGuardWidget.class);
		appWidgetManager.updateAppWidget(cn, remote);
		
		if( !op){
			CharSequence text = "start!";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();	
			
			Intent serv = new Intent(context, DroidGuardService.class);
			
			DroidGuardService droidGuardService=new DroidGuardService();
			droidGuardService.startService(serv);
			
			
		}
		else{
			CharSequence text = "stop!";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();	
			
Intent serv = new Intent(context, DroidGuardService.class);
			
			DroidGuardService droidGuardService=new DroidGuardService();
			droidGuardService.onDestroy();
		}
		
		
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//funny things happen unless we intercept all broadcasts and 
		//call onUpdate ourselves
		String action = intent.getAction();
		
		if (action != null) {
			if (action.equals("UPDATE")) {
				final AppWidgetManager manager = AppWidgetManager.getInstance(context);
				onUpdate(context, manager, 
						manager.getAppWidgetIds(new ComponentName(
								context, DroidGuardWidget.class)));
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
