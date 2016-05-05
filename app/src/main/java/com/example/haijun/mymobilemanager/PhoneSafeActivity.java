package com.example.haijun.mymobilemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haijun.com.example.haijun.application.MyApplication;

public class PhoneSafeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_safe);

        TextView tv_phonesafe_number = (TextView) findViewById(R.id.tv_phonesafe_number);
        ImageView iv_phonesafe_isopen = (ImageView) findViewById(R.id.iv_phonesafe_isopen);


        String safePhoneNum = MyApplication.sharedPreferences.getString("safePhoneNum", "");
        if (safePhoneNum.isEmpty()){
            startActivity(new Intent(this,TheftSetup_1Activity.class));
        }
        else {
            tv_phonesafe_number.setText(safePhoneNum);
            boolean bindprotect = MyApplication.sharedPreferences.getBoolean("bindprotect", true);
            if (bindprotect){
                iv_phonesafe_isopen.setImageResource(R.drawable.lock);
            }
            else {
                iv_phonesafe_isopen.setImageResource(R.drawable.unlock);
            }

        }

    }

    public void setAgain(View view){
        startActivity(new Intent(this,TheftSetup_1Activity.class));
    }
}
