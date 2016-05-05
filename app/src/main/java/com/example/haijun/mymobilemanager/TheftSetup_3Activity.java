package com.example.haijun.mymobilemanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.haijun.com.example.haijun.application.MyApplication;


public class TheftSetup_3Activity extends SetupBaseActivity {

    private EditText et_theftsetup_3_inputsafeNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theft_setup_3);
        et_theftsetup_3_inputsafeNum = (EditText) findViewById(R.id.et_theftsetup_3_inputsafeNum);
    }

    public void selectNumber(View view){
        /*Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setDataAndType(Uri.parse("content://contacts"), ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, 100);*/

        startActivityForResult(new Intent(this,ContactListActivity.class), 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            if (requestCode==100){
                Uri uri = data.getData();
                String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor query = getContentResolver().query(uri, projection, null, null, null);
                query.moveToFirst();
                int columnIndex = query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String string = query.getString(columnIndex);
                et_theftsetup_3_inputsafeNum.setText(string);
            }
        }

        if (resultCode == 1000){
            if (requestCode == 200){
                String safePhoneNum = data.getStringExtra("safePhoneNum");
                et_theftsetup_3_inputsafeNum.setText(safePhoneNum);
            }
        }
    }



    public void preverioussetup(View view){
        startActivity(new Intent(this, TheftSetup_2Activity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void nextsetup(View view){
        String inputsafeNum = et_theftsetup_3_inputsafeNum.getText().toString();
        if (!inputsafeNum.isEmpty()){
            MyApplication.sharedPreferences.edit().putString("safePhoneNum",inputsafeNum).commit();
            startActivity(new Intent(this, TheftSetup_4Activity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        else {
            Toast.makeText(this,"号码不能为空",Toast.LENGTH_SHORT).show();
        }
    }


}
