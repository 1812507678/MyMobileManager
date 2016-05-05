package com.example.haijun.com.example.haijun.application;

import android.app.ActivityManager;
import android.app.Application;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.haijun.javabean.ProcessInfo;
import com.example.haijun.mymobilemanager.R;
import com.example.haijun.seriver.BlackNumberService;
import com.example.haijun.seriver.MyNumberLocationService;
import com.example.haijun.utils.ProcessUtils;
import com.example.haijun.widget.MyAppWidgetProvider;

import java.util.List;

/**
 * Created by haijun on 2016/3/25.
 */
public class MyApplication extends Application {


    public static SharedPreferences sharedPreferences;
    private MyReceive myReceive;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

        myReceive = new MyReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.haijun.com");
        //动态注册广播，桌面widget清理内存
        registerReceiver(myReceive,intentFilter);

        //设置软件锁，默认当应用关闭时，软件锁服务也关闭，每次打开应用软件锁重置
        sharedPreferences.edit().putBoolean("isSoftLocked",false).commit();
        startService(new Intent(this, MyNumberLocationService.class));
        //Log.i("MyApplication","MyNumberLocationService start");

        //启动黑名单就开启屏蔽电话短信服务
        boolean blacknumber = sharedPreferences.getBoolean("blacknumber", true);
        if (blacknumber) {
            startService(new Intent(this, BlackNumberService.class));

        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(myReceive);
    }

    class MyReceive extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.i("MyApplication", "onReceive");
            ComponentName componentName = new ComponentName(context,MyAppWidgetProvider.class);
            RemoteViews remoteView = new RemoteViews("com.example.haijun.mymobilemanager", R.layout.processmanager_appwidget);

            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ProcessInfo> processList = ProcessUtils.getProcessList(context);
            for (ProcessInfo processInfo:processList) {
                if (processInfo.getPackagename().equals("com.example.haijun.mymobilemanager")){
                    continue;
                }
                activityManager.killBackgroundProcesses(processInfo.getPackagename());
                //Log.i("MyApplication", processInfo.getPackagename());
            }

            int processCount = ProcessUtils.getProcessCount(context);
            String[] totalRamSize = ProcessUtils.getTotalRamSize(context);
            remoteView.setTextViewText(R.id.tv_processwidget_count,"总进程数："+processCount);
            remoteView.setTextViewText(R.id.tv_processwidget_memory, "可用内存：" + totalRamSize[1]);

            AppWidgetManager instance = AppWidgetManager.getInstance(context);

            instance.updateAppWidget(componentName,remoteView);
            Toast.makeText(context,"清理成功",Toast.LENGTH_SHORT).show();
        }
    }
}
