package com.example.haijun.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by haijun on 2016/3/31.
 */
public class RunningServiceUtils {

    public static boolean isrunning(String serviceName,Context context){

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo runService:runningServices) {
            Log.i("RunningService",runService.service.getClassName());
            if (serviceName.equals(runService.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}
