package com.example.haijun.mymobilemanager;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.haijun.com.example.haijun.application.MyApplication;

public class SetToastLocationActivity extends Activity {

    private LinearLayout ll_setToastlocation_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_toast_location);

        ll_setToastlocation_show = (LinearLayout) findViewById(R.id.ll_setToastlocation_show);
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        final int widthPixels = displayMetrics.widthPixels;
        final int heightPixels = displayMetrics.heightPixels;

        ll_setToastlocation_show.setOnTouchListener(new View.OnTouchListener() {
            float startX ;
            float startY ;
            float moveX;
            float moveY;
            int ll_left;
            int ll_top;
            int ll_right;
            int ll_bootom;
            int ll_width;
            int ll_height;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        /*startX = event.getRawX();
                        startY = event.getRawY();*/
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        moveX = event.getX();
                        moveY = event.getY();

                        float changeX = moveX - startX;
                        float changeY = moveY - startY;

                        float oddX = ll_setToastlocation_show.getX();
                        float oddY = ll_setToastlocation_show.getY();

                        ll_width = ll_setToastlocation_show.getWidth();
                        ll_height = ll_setToastlocation_show.getHeight();
                        //新的布局尺度
                        ll_left = (int) (oddX+changeX);
                        ll_top = (int) (oddY+changeY);
                        ll_right = (int) (oddX+changeX+ll_width);
                        ll_bootom = (int) (oddY+changeY+ll_height);

                        //判断是否拖出屏幕
                        if (ll_left<0 || ll_top<0 || ll_right>widthPixels ||ll_bootom>heightPixels){
                            break;
                        }
                        //重新布局
                        ll_setToastlocation_show.layout(ll_left,ll_top,ll_right,ll_bootom);

                        startX = moveX;
                        startY = moveY;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (ll_left<0 || ll_top<0 || ll_right>widthPixels ||ll_bootom>heightPixels){
                            break;
                        }
                        MyApplication.sharedPreferences.edit().putInt("ll_left",ll_left).commit();
                        MyApplication.sharedPreferences.edit().putInt("ll_top",ll_top).commit();
                        break;
                }
                return false;
            }
        });

        //设置双击事件
        ll_setToastlocation_show.setOnClickListener(new View.OnClickListener() {
            boolean click = false;
            long firstTime;
            @Override
            public void onClick(View v) {
                if (!click){
                    click = true;
                    firstTime = System.currentTimeMillis();
                }
                else {
                    long secendTime = System.currentTimeMillis();
                    if (secendTime-firstTime<500){//双击事件
                        int left = widthPixels/2-ll_setToastlocation_show.getWidth()/2;
                        int top = heightPixels/2-ll_setToastlocation_show.getHeight()/2;
                        int right = widthPixels/2+ll_setToastlocation_show.getWidth()/2;
                        int bootom = heightPixels/2+ll_setToastlocation_show.getHeight()/2;
                        ll_setToastlocation_show.layout(left,top,right,bootom);
                        MyApplication.sharedPreferences.edit().putInt("ll_left",left).commit();
                        MyApplication.sharedPreferences.edit().putInt("ll_top", top).commit();
                    }
                    click = false;
                }
            }
        });
    }
}
