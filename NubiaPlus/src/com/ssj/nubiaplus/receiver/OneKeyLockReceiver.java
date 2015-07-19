package com.ssj.nubiaplus.receiver;

import java.lang.annotation.Annotation;

import com.ssj.nubiaplus.OneKeyLockActivity;
import com.ssj.nubiaplus.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;

public class OneKeyLockReceiver extends  AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Intent intent = new Intent(context, OneKeyLockActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

		RemoteViews view=new RemoteViews(context.getPackageName(),R.layout.onekeylock_appwidget);
		view.setOnClickPendingIntent(R.id.ib_onekeylock, pendingIntent);
		 appWidgetManager.updateAppWidget(appWidgetIds[0], view);

	}
	
}
