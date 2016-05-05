package com.example.haijun.seriver;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.haijun.com.example.haijun.application.MyApplication;
import com.example.haijun.mymobilemanager.R;

import com.example.haijun.dao.NumberLoactionDao;

public class MyNumberLocationService extends Service {

    private WindowManager windowManager;
    private View inflate;

    public MyNumberLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(new MyPhoneStatListener(),PhoneStateListener.LISTEN_CALL_STATE);

    }

    class MyPhoneStatListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    hideLocationView();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    String numberLocation = NumberLoactionDao.getNumberLocation(incomingNumber,MyNumberLocationService.this);
                    showLocationView(numberLocation);
                    break;
            }
        }
    }



    private void showLocationView(String location) {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        final int widthPixels = displayMetrics.widthPixels;
        final int heightPixels = displayMetrics.heightPixels;

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        inflate = layoutInflater.inflate(R.layout.mynumberlocation,null);
        inflate.setBackgroundResource(R.drawable.call_locate_green);
        TextView tv_locationServ_location = (TextView) inflate.findViewById(R.id.tv_locationServ_location);
        tv_locationServ_location.setText(location);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.format = PixelFormat.TRANSLUCENT;  //透明状

        layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                |WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        layoutParams.gravity = Gravity.LEFT|Gravity.TOP;

        //获取用户自定义的显示位置
        int ll_left = MyApplication.sharedPreferences.getInt("ll_left", widthPixels / 2);
        int ll_top = MyApplication.sharedPreferences.getInt("ll_top", heightPixels / 2);
        layoutParams.x = ll_left;
        layoutParams.y = ll_top;

        windowManager.addView(inflate, layoutParams);

    }

    //隐藏自定义Toast弹出框
    private void hideLocationView() {
        Log.i("hideLocationView","hideLocationView");
        if (windowManager!=null){
            windowManager.removeView(inflate);
            Log.i("hideLocation:removeView", "removeView");
        }
    }

}





