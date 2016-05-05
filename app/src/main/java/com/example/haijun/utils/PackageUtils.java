package com.example.haijun.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import com.example.haijun.javabean.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haijun on 2016/3/31.
 */
public class PackageUtils  {

    //SD卡可用大小
    public static String getAvailableSdSize(Context context){
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long blockSizeLong;
        long availableBlocksLong;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSizeLong = statFs.getBlockSizeLong();
            availableBlocksLong = statFs.getAvailableBlocksLong();
        }
        else {
            blockSizeLong = statFs.getBlockSize();
            availableBlocksLong = statFs.getAvailableBlocks();
        }
        return Formatter.formatFileSize(context, blockSizeLong*availableBlocksLong);
    }

    //ROM卡可用大小
    public static String getAvaiableRomSize(Context context){
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        long blockSizeLong;
        long availableBlocksLong;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2){
            blockSizeLong = statFs.getBlockSizeLong();
            availableBlocksLong = statFs.getAvailableBlocksLong();
        } else {
            blockSizeLong = statFs.getBlockSize();
            availableBlocksLong = statFs.getAvailableBlocks();
        }
        return Formatter.formatFileSize(context, blockSizeLong*availableBlocksLong);
    }

    //
    public static List<AppInfo> getPackageNames(Context context){
        List<AppInfo> AppInfoList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);
        for (ApplicationInfo applicationInfo:installedApplications) {
            Drawable drawable = applicationInfo.loadIcon(packageManager);
            CharSequence charSequence = applicationInfo.loadLabel(packageManager);
            String packageName = applicationInfo.packageName;
            boolean isSdload = false;
            //是系统上安装的
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==1){
                isSdload = false;
            }
            else {
                isSdload = true;
            }
            AppInfoList.add(new AppInfo(drawable,charSequence.toString(),isSdload,packageName));
        }
        return  AppInfoList;
    }
}
