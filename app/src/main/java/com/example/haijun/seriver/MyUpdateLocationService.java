package com.example.haijun.seriver;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.haijun.com.example.haijun.application.MyApplication;

public class MyUpdateLocationService extends Service {
    public MyUpdateLocationService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates("gps", 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //获取经纬度
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                MyApplication.sharedPreferences.edit().putString("latitude",latitude+"").commit();
                MyApplication.sharedPreferences.edit().putString("longitude",longitude+"").commit();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }
}
