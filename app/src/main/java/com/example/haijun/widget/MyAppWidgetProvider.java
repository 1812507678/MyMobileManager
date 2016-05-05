package com.example.haijun.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.haijun.mymobilemanager.R;
import com.example.haijun.utils.ProcessUtils;

/**
 * Created by haijun on 2016/4/4.
 */
public class MyAppWidgetProvider extends AppWidgetProvider{
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i("MyAppWidgetProvider","onReceive");

    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.i("MyAppWidgetProvider", "onEnabled");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.i("MyAppWidgetProvider", "onDeleted");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i("MyAppWidgetProvider", "onUpdate");
        ComponentName componentName = new ComponentName(context,MyAppWidgetProvider.class);
        RemoteViews remoteView = new RemoteViews("com.example.haijun.mymobilemanager", R.layout.processmanager_appwidget);

        int processCount = ProcessUtils.getProcessCount(context);
        String[] totalRamSize = ProcessUtils.getTotalRamSize(context);
        remoteView.setTextViewText(R.id.tv_processwidget_count,"总进程数："+processCount);
        remoteView.setTextViewText(R.id.tv_processwidget_memory, "可用内存：" + totalRamSize[1]);
        Intent intent  = new Intent();
        intent.setAction("com.example.haijun.com");
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteView.setOnClickPendingIntent(R.id.btn_widget_clear,broadcast);
        appWidgetManager.updateAppWidget(componentName,remoteView);

    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.i("MyAppWidgetProvider", "onDisabled");
    }


}
