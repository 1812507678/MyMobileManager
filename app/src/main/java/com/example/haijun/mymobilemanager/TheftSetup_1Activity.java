package com.example.haijun.mymobilemanager;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

public class TheftSetup_1Activity extends SetupBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theft_setup_1);


    }

    @Override
    public void preverioussetup(View view) {
    }

    @Override
    public void nextsetup(View view) {
        startActivity(new Intent(this,TheftSetup_2Activity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

}
