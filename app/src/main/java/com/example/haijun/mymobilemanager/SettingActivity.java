package com.example.haijun.mymobilemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.haijun.com.example.haijun.application.MyApplication;
import com.example.haijun.seriver.BlackNumberService;
import com.example.haijun.seriver.MyNumberLocationService;
import com.example.haijun.utils.RunningServiceUtils;
import com.example.haijun.view.SettingItem;

public class SettingActivity extends Activity {

    private SettingItem si_setting_phonelocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        si_setting_phonelocal = (SettingItem) findViewById(R.id.si_setting_phonelocal);
        si_setting_phonelocal.setMyOnClickListener(new SettingItem.MyOnClickListener() {
            @Override
            public void checkOnClick() {
                startService(new Intent(SettingActivity.this, MyNumberLocationService.class));
                Log.i("SettingActivity","startService");
            }

            @Override
            public void cancelOnClick() {
                stopService(new Intent(SettingActivity.this, MyNumberLocationService.class));
                Log.i("SettingActivity", "stopService");
            }
        });

    }

    //跳到设置来电样式的页面
    public void setPhoneStyle(View view){
        startActivity(new Intent(this, SetToastLocationActivity.class));
    }


    //如果用户在后台将服务杀死，在打开页面时重新进行相关初始化
    @Override
    protected void onStart() {
        super.onStart();

        boolean isrunning = RunningServiceUtils.isrunning("com.example.haijun.seriver.MyNumberLocationService", this);
        Log.i("isrunning", isrunning + "");
        if (!isrunning){
            si_setting_phonelocal.setCheckBox(false);
        }
        else {
            si_setting_phonelocal.setCheckBox(true);
        }

    }

    //点击启动或关闭服务
    public void startBlackNumberService(View view){
        boolean blacknumber = MyApplication.sharedPreferences.getBoolean("blacknumber", true);
        if (blacknumber){
            stopService(new Intent(this, BlackNumberService.class));
        }
        else {
            startService(new Intent(this, BlackNumberService.class));
        }
    }
}
