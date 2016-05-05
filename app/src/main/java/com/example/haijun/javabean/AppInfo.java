package com.example.haijun.javabean;

import android.graphics.drawable.Drawable;

/**
 * Created by haijun on 2016/3/31.
 */
public class AppInfo {
    private Drawable icon;
    private String lable;
    private boolean isSDLoad;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    private String packageName;

    public AppInfo(Drawable icon, String lable, boolean isSDLoad) {
        this.icon = icon;
        this.lable = lable;
        this.isSDLoad = isSDLoad;
    }

    public AppInfo(Drawable icon, String lable, boolean isSDLoad, String packageName) {
        this.icon = icon;
        this.lable = lable;
        this.isSDLoad = isSDLoad;
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public boolean isSDLoad() {
        return isSDLoad;
    }

    public void setIsSDLoad(boolean isSDLoad) {
        this.isSDLoad = isSDLoad;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "icon=" + icon +
                ", lable='" + lable + '\'' +
                ", isSDLoad=" + isSDLoad +
                '}';
    }
}
