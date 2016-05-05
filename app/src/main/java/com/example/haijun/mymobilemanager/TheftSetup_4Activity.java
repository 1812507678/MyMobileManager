package com.example.haijun.mymobilemanager;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.haijun.receiver.MyDeviceAdminReceiver;

public class TheftSetup_4Activity extends SetupBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theft_setup_4);
    }

    public void preverioussetup(View view){
        startActivity(new Intent(this, TheftSetup_3Activity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void nextsetup(View view) {
        Toast.makeText(this,"设置完成",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, PhoneSafeActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    //激活管理员权限
    public void active(View view){
        // Launch the activity to have the user enable our admin.
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        ComponentName mDeviceAdminSample = new ComponentName(this,MyDeviceAdminReceiver.class);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"设备管理h");
        startActivityForResult(intent, 100);
    }
}
