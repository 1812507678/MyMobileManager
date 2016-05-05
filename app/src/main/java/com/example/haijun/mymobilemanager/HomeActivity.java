package com.example.haijun.mymobilemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haijun.com.example.haijun.application.MyApplication;
import com.example.haijun.utils.Md5Utils;

public class HomeActivity extends Activity {

    private GridView gv_home_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gv_home_content = (GridView) findViewById(R.id.gv_home_content);
        initContentView();

    }

    //初始化页面9个GrawView的模块
    private void initContentView() {
        final String[] lilterTitles = new String[]{"手机防盗","通讯卫士","软件管理","进程管理",
                "流量统计","手机杀毒","缓存清理","高级工具","设置中心"};
        final int[] imageIds = new int[]{R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,R.drawable.taskmanager,
                        R.drawable.netmanager,R.drawable.trojan,R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings};

        gv_home_content.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 9;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                //每一个条目用一个线性布局显示
                View inflate = View.inflate(HomeActivity.this, R.layout.item_gridview, null);
                ImageView iv_itemgridview_image = (ImageView) inflate.findViewById(R.id.iv_itemgridview_image);
                TextView tv_itemgridview_title = (TextView) inflate.findViewById(R.id.tv_itemgridview_title);
                iv_itemgridview_image.setImageResource(imageIds[position]);
                tv_itemgridview_title.setText(lilterTitles[position]);
                return inflate;
            }
        });

        gv_home_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showPasswordDialog();
                        break;
                    case 1:
                        startActivity(new Intent(HomeActivity.this,TelephoneManageActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(HomeActivity.this,PackageManagerActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(HomeActivity.this,ProcessManagerActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(HomeActivity.this,DataUsageActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(HomeActivity.this,ScanVirusActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(HomeActivity.this,ClearCacheActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(HomeActivity.this, AdvanceToolActivity.class));
                        break;
                    //设置中心
                    case 8:
                        startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                        break;
                }
            }
        });
    }

    private void showPasswordDialog() {
        String phonesafe_pwd = MyApplication.sharedPreferences.getString("phonesafe_pwd", "");
        if (phonesafe_pwd.isEmpty()){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            View inflate = View.inflate(this, R.layout.activity_home_setpassword_dialog, null);
            alertDialog.setView(inflate);

            Button bt_home_dialog_input_ensure = (Button) inflate.findViewById(R.id.bt_home_dialog_input_ensure);
            Button bt_home_dialog_input_cancel = (Button) inflate.findViewById(R.id.bt_home_dialog_input_cancel);
            final EditText ed_home_dialog_setPassword = (EditText) inflate.findViewById(R.id.ed_home_dialog_setPassword);
            final EditText ed_home_dialog_confirmPassword = (EditText) inflate.findViewById(R.id.ed_home_dialog_confirmPassword);

            bt_home_dialog_input_ensure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String firstPassword = ed_home_dialog_setPassword.getText().toString();
                    String secednPassword = ed_home_dialog_confirmPassword.getText().toString();
                    if (!firstPassword.isEmpty() && !secednPassword.isEmpty()){
                        if (firstPassword.equals(secednPassword)){
                            String md5MessagePassword = Md5Utils.getMd5Message(firstPassword);
                            MyApplication.sharedPreferences.edit().putString("phonesafe_pwd",md5MessagePassword).commit();
                            Toast.makeText(HomeActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(HomeActivity.this, PhoneSafeActivity.class));
                            alertDialog.dismiss();
                        }
                        else {
                            Toast.makeText(HomeActivity.this,"密码不一致，请重新设置",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(HomeActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            bt_home_dialog_input_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }
        else {
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            View inflate = View.inflate(this, R.layout.activity_home_checkpassword_dialog, null);
            alertDialog.setView(inflate);
            final EditText ed_home_dialog_inputPassword = (EditText) inflate.findViewById(R.id.ed_home_dialog_inputPassword);
            Button bt_home_dialog_chenck_ensure = (Button) inflate.findViewById(R.id.bt_home_dialog_chenck_ensure);
            Button bt_home_dialog_chenck_cancel = (Button) inflate.findViewById(R.id.bt_home_dialog_chenck_cancel);

            bt_home_dialog_chenck_ensure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String password = ed_home_dialog_inputPassword.getText().toString();
                    String md5MessagePassword = Md5Utils.getMd5Message(password);
                    String savePassword = MyApplication.sharedPreferences.getString("phonesafe_pwd", "");
                    if (!md5MessagePassword.isEmpty() && !savePassword.isEmpty()){
                        if (md5MessagePassword.equals(savePassword)){
                            alertDialog.dismiss();
                            startActivity(new Intent(HomeActivity.this,PhoneSafeActivity.class));
                        }
                        else {
                            Toast.makeText(HomeActivity.this,"密码错误，请重新的输入",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(HomeActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            bt_home_dialog_chenck_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }
}

