package com.example.haijun.mymobilemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdvanceToolActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_tool_activty);

    }

    public void querylocation(View view){
        startActivity(new Intent(this,QueryLocationActivity.class));
    }
}
