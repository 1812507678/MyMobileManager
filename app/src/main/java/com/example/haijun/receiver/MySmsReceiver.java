package com.example.haijun.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.haijun.com.example.haijun.application.MyApplication;
import com.example.haijun.mymobilemanager.R;
import com.example.haijun.seriver.MyUpdateLocationService;

public class MySmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.i("MySmsReceiver", "onReceive");
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        for (Object object:pdus){
            SmsMessage fromPdu = SmsMessage.createFromPdu((byte[]) object);
            String messageBody = fromPdu.getMessageBody();
            String originatingAddress = fromPdu.getOriginatingAddress();
            //Log.i("MySmsReceiver", messageBody);
            if (messageBody.equals("alarm")){
                playAlarm(context);
            }
            else if (messageBody.equals("location")){
                getLocation(context);
            }
            else if (messageBody.equals("wipedata")){
                wipedata(context);
            }
            else if (messageBody.equals("lockscreen")){
                lockscreen(context);
            }
        }
    }

    private void lockscreen(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
        devicePolicyManager.lockNow();
        devicePolicyManager.resetPassword("123", 0);
        //Log.i("MySmsReceiver", "lockscreen");

    }

    private void wipedata(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
        devicePolicyManager.wipeData(0);
        //Log.i("MySmsReceiver", "wipedata");
    }

    private void playAlarm(Context context) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        //Log.i("MySmsReceiver","playAlarm");
    }

    public void getLocation(Context context) {
        context.startService(new Intent(context, MyUpdateLocationService.class));
        String latitude = MyApplication.sharedPreferences.getString("latitude", "");
        String longitude = MyApplication.sharedPreferences.getString("longitude", "");
        //Log.i("MySmsReceiver",latitude);
        //Log.i("MySmsReceiver",longitude);

    }
}
