package com.example.haijun.seriver;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.haijun.dao.LockAppDao;
import com.example.haijun.javabean.AppInfo;
import com.example.haijun.mymobilemanager.LockAppActivityShow;

import java.util.ArrayList;
import java.util.List;

public class MyLockAppService extends Service {
    private static final String TAG = "MyLockAppService";
    ActivityManager activityManager;
    private String tempCurrentPackage="";
    private List<String> allLockedApp;

    public MyLockAppService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        Log.i(TAG, "onStartCommand");
        //动态注册广播接收者
        MyReceive myReceive= new MyReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.haijun.mymobilemanager");
        registerReceiver(myReceive,intentFilter);

        final LockAppDao lockAppDao = new LockAppDao(this);
        //将数据一次性读到内存中，以空间换时间,但当数据变化时需要更新allLockedApp，需要内容观察者
        allLockedApp = lockAppDao.getAllLockedApp();

        //注册内容提供者
        getContentResolver().registerContentObserver(Uri.parse("content://com.haijun.mobilemanage"), false, new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                allLockedApp = lockAppDao.getAllLockedApp();
                Log.i("ContentResolver","onChange");
            }
        });
        new Thread() {
            @Override
            public void run() {
                super.run();

                while (true) {
                    List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
                    ActivityManager.RunningAppProcessInfo runningAppProcessInfo = runningAppProcesses.get(0);
                    /*for (ActivityManager.RunningAppProcessInfo app:runningAppProcesses) {
                        Log.i("processName", app.processName);
                    }*/
                    //Log.i("processName", "--------------------------");
                    String processName = runningAppProcessInfo.processName;

                    //没上锁，并且不是临时可以运行的进程时，跳到提示页面
                    if (allLockedApp.contains(processName) && !tempCurrentPackage.equals(processName)) {
                       // Log.i("processName", allLockedApp.contains(processName) + "");
                        Intent intent1 = new Intent();
                        intent1.setClass(MyLockAppService.this, LockAppActivityShow.class);
                        intent1.putExtra("lockAppname", processName);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    class MyReceive extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            tempCurrentPackage = intent.getStringExtra("package");
            Log.i("action", action);
            Log.i("package", tempCurrentPackage);
        }
    }

}
