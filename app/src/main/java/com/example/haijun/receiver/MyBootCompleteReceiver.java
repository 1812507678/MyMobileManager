package com.example.haijun.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.example.haijun.com.example.haijun.application.MyApplication;

public class MyBootCompleteReceiver extends BroadcastReceiver {
    public MyBootCompleteReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean bindprotect = MyApplication.sharedPreferences.getBoolean("bindprotect", true);
        if (bindprotect){
            String imsi = MyApplication.sharedPreferences.getString("imsi", "");
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = telephonyManager.getSimSerialNumber();
            String line1Number = telephonyManager.getLine1Number();//获取当前手机号
            if (!imsi.equals(simSerialNumber)){
                String safePhoneNum = MyApplication.sharedPreferences.getString("safePhoneNum", "");
                SmsManager aDefault = SmsManager.getDefault();
                aDefault.sendTextMessage(safePhoneNum,line1Number,"手机换卡了",null,null);
            }
        }
    }
}
