package com.example.haijun.mymobilemanager;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;
import android.widget.Toast;

import com.example.haijun.javabean.AppInfo;
import com.example.haijun.utils.PackageUtils;

import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase {
    public ApplicationTest() {
        super(Application.class);
        Log.i("haha", "ok");
        System.out.println("okokokoko");
    }
    /*public void myoTest(){
        String availableSdSize = PackageUtils.getAvailableSdSize(getContext());
        String avaiableRomSize = PackageUtils.getAvaiableRomSize(getContext());
        List<AppInfo> packageNames = PackageUtils.getPackageNames(getContext());

        Log.i("availableSdSize",availableSdSize+"");
        Log.i("avaiableRomSize",avaiableRomSize+"");
        for (AppInfo appInfo:packageNames) {
            Log.i("AppInfo",appInfo.toString());
        }
    }*/






}