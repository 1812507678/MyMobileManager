package com.example.haijun.mymobilemanager;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.haijun.dao.NumberLoactionDao;

public class QueryLocationActivity extends Activity {

    private EditText et_querylocation_num;
    private TextView tv_querylocation_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_location);

        et_querylocation_num = (EditText) findViewById(R.id.et_querylocation_num);
        tv_querylocation_result = (TextView) findViewById(R.id.tv_querylocation_result);

        et_querylocation_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String numberLocation = NumberLoactionDao.getNumberLocation(s.toString(),QueryLocationActivity.this);
                tv_querylocation_result.setText(numberLocation);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    //查询输入电话号码的类型
    public void query(View view){
        String number = et_querylocation_num.getText().toString();
        String numberLocation = NumberLoactionDao.getNumberLocation(number,this);
        tv_querylocation_result.setText(numberLocation);
    }
}

