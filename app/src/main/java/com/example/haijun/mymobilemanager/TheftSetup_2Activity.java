package com.example.haijun.mymobilemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.example.haijun.com.example.haijun.application.MyApplication;
import com.example.haijun.view.SettingItem;

public class TheftSetup_2Activity extends SetupBaseActivity {

    private SettingItem settingItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theft_setup_2);
        settingItem = (SettingItem) findViewById(R.id.si_theftsetup_2_bindsim);

        settingItem.setMyOnClickListener(new SettingItem.MyOnClickListener() {
            @Override
            public void checkOnClick() {
                Log.i("checkOnClick","checkOnClick");
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String simSerialNumber = telephonyManager.getSimSerialNumber();
                MyApplication.sharedPreferences.edit().putString("imsi",simSerialNumber).commit();
            }

            @Override
            public void cancelOnClick() {
                MyApplication.sharedPreferences.edit().putString("imsi","").commit();
            }
        });

    }

    public void preverioussetup(View view){
        startActivity(new Intent(this, TheftSetup_1Activity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void nextsetup(View view){
        startActivity(new Intent(this,TheftSetup_3Activity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }



}
