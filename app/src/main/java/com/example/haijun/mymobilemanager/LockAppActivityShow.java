package com.example.haijun.mymobilemanager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LockAppActivityShow extends ActionBarActivity {

    private EditText ed_lockshow_input;
    private String lockAppname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_app_activity_show);

        TextView tv_lockappshow_packname = (TextView) findViewById(R.id.tv_lockappshow_packname);
        ed_lockshow_input = (EditText) findViewById(R.id.ed_lockshow_input);

        Intent intent = getIntent();
        lockAppname = intent.getStringExtra("lockAppname");
        tv_lockappshow_packname.setText("此应用被列入禁止启动名单，暂时不能启动，除非你有权限，请在以下输入");
        
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    public void check(View view){
        String s = ed_lockshow_input.getText().toString();
        if (s.equals("123")){
            Intent intent = new Intent();
            intent.setAction("com.example.haijun.mymobilemanager");
            intent.putExtra("package",lockAppname);
            sendBroadcast(intent);
            finish();
        }
        else {
            onBackPressed();
        }
    }
}
