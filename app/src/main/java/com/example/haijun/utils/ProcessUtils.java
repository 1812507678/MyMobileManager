package com.example.haijun.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.text.format.Formatter;
import android.util.Log;

import com.example.haijun.javabean.ProcessInfo;
import com.example.haijun.mymobilemanager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haijun on 2016/4/3.
 */
public class ProcessUtils {

    //获取当期运行的进程数量
    public static int getProcessCount(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
        return runningServices.size();

    }

    public static String[] getTotalRamSize(Context context){
        String[] ramSize = new String[2];
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            String totalSize = Formatter.formatFileSize(context,  memoryInfo.totalMem);
            ramSize[0] = totalSize;
        }
        else {
            //api小于16，用其他方法计算总内存大小
        }
        String avialSize = Formatter.formatFileSize(context,  memoryInfo.availMem);
        ramSize[1] = avialSize;
        return ramSize;
    }

    public static List<ProcessInfo> getProcessList(Context context){
        PackageManager packageManager = context.getPackageManager(); ;
        List<ProcessInfo> processInfoList = new ArrayList<>();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);

        for (ActivityManager.RunningServiceInfo appServiceInfo:runningServices) {
            String processName = appServiceInfo.service.getPackageName();
            int pid = appServiceInfo.pid;

            Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{pid});
            long totalPss = processMemoryInfo[0].getTotalPss();
            boolean isSystem = true;
            //获取默认的进程图标
            Drawable drawable = context.getResources().getDrawable(R.mipmap.ic_launcher);
            CharSequence appName = processName;
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(processName, 0);
                //是系统应用
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==1){
                    isSystem = true;
                }
                else {
                    isSystem = false;
                }
                appName = applicationInfo.loadLabel(packageManager);
                drawable = applicationInfo.loadIcon(packageManager);
                processInfoList.add(new ProcessInfo(drawable,appName.toString(),totalPss,processName,isSystem));

            } catch (PackageManager.NameNotFoundException e) {//当进程无包名时是有异常，任然新建进程对象
                e.printStackTrace();
                processInfoList.add(new ProcessInfo(drawable, appName.toString(), totalPss, processName, isSystem));
            }
        }
        return processInfoList;
    }
}
