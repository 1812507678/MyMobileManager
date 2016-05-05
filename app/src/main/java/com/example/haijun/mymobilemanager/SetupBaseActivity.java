package com.example.haijun.mymobilemanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by haijun on 2016/3/29.
 */
public abstract class SetupBaseActivity extends Activity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this,new MyGestureListener());
    }


    class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float startX = e1.getX();
            float endX = e2.getX();
            if (endX-startX>50){//右滑
                preverioussetup(null);
            }
            else if (startX - endX>50){//左滑
                nextsetup(null);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public abstract void preverioussetup(View view);
    public abstract void nextsetup(View view);

}
