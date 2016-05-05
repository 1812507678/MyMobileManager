package com.example.haijun.mymobilemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haijun.com.example.haijun.application.MyApplication;
import com.example.haijun.utils.HttpUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";
    private static final int MESSAGE_OK = -1;
    private static final int MESSAGE_URL = -2;
    private static final int MESSAGE_IO = -3;
    private static final int MESSAGE_JSON = -4;
    private static final int MESSAGE_SERVICEEX = 0;
    private int currentVersionCode;
    private String urlpath;
    private ProgressBar progressBar;
    private AlertDialog downProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView tv_splash_versionname = (TextView) findViewById(R.id.tv_splash_versionname);
        String currentVersionName = getPackIno();
        tv_splash_versionname.setText("版本：" + currentVersionName);
        boolean isUpdate = MyApplication.sharedPreferences.getBoolean("auto_update", true);
        if (isUpdate){
            getNewVersion();
        }
        else {
            waitWhile();
        }
        //把联系人归属地数据库拷贝到SD卡
        copyDBToSD("naddress.db");
        //把病毒数据库拷贝到SD卡
        copyDBToSD("antivirus.db");
    }

    public void getNewVersion(){
        progressBar  = new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
        progressBar.setMinimumHeight(10);
        urlpath = "http://192.168.3.64:8080/mobilemanager/version.json";
        getServiceInf(urlpath);
    }

    //进行一些初始化工作，或等待一会
    public void waitWhile(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1000);
                    enterHome();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void copyDBToSD(String dbName){
        AssetManager assets = getAssets();
        File file = new File(Environment.getExternalStorageDirectory()+"/"+dbName);
        if (file.exists()){
            return;
        }
        InputStream open = null;
        FileOutputStream fileOutputStream = null;
        try {
            open = assets.open(dbName);
            fileOutputStream  = new FileOutputStream(file);
            int read = -1;
            byte[] bytes = new byte[1024];
            while ((read=open.read(bytes,0,bytes.length))!=-1){
                fileOutputStream.write(bytes,0,read);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (open != null) {
                try {
                    open.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_OK:
                    String[] serviceInf = (String[]) msg.obj;
                    int newVersionCode = Integer.parseInt(serviceInf[0]);
                    final String downLoadUrl = serviceInf[1];
                    String newInformation = serviceInf[2];
                    //服务器端新版本号大于本地版本号，进行弹出选择框通知用户是否选择下载最新的软件
                    if (newVersionCode>currentVersionCode){
                        AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this)
                                .setTitle("版本更新")
                                .setMessage(newInformation)
                                .setPositiveButton("确认下载", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //弹出对话框，显示下载进度
                                        downProcess = new AlertDialog.Builder(SplashActivity.this)
                                                .setTitle("下载进度")
                                                .setView(progressBar)
                                                .create();
                                        //设置在对话框之外的其他地方点击，对话框不会消失
                                        downProcess.setCanceledOnTouchOutside(false);
                                        downProcess.show();
                                        //进行下载和安装
                                        downloadAndInstallApp(downLoadUrl);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        enterHome();
                                    }
                                })
                                .create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }

                    break;
                case MESSAGE_SERVICEEX:
                    Toast.makeText(SplashActivity.this,""+MESSAGE_SERVICEEX,Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_URL:
                    Toast.makeText(SplashActivity.this,""+MESSAGE_URL,Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_IO:
                    Toast.makeText(SplashActivity.this,""+MESSAGE_IO,Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_JSON:
                    Toast.makeText(SplashActivity.this,""+MESSAGE_JSON,Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
            }
        }
    };

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            enterHome();
        }
        return true;
    }*/

    //进入主页面
    public void enterHome(){
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }

    //下载和安装app
    private void downloadAndInstallApp(String appPath) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(this, appPath, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                FileOutputStream fileOutputStream = null;
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/mymobilemanager.apk");
                try {
                    //下载下来的apk文件保存在SD卡上
                    fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(responseBody);
                    Toast.makeText(SplashActivity.this, "下载成功，准备安装...", Toast.LENGTH_LONG).show();

                    //启动系统中专门安装app的组件进行安装app
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    startActivityForResult(intent, 100);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                super.onProgress(bytesWritten, totalSize);
                progressBar.setMax(totalSize);
                progressBar.setProgress(bytesWritten);
                //下载成功，退出下载对话框
                if (progressBar.getProgress() == progressBar.getMax()) {
                    downProcess.cancel();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_CANCELED){
            enterHome();
        }
    }

    //根据packet信息设置当前的版本号
    private String getPackIno(){
        PackageManager packageManager = getPackageManager();
        String currentVersionName = null;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            currentVersionCode = packageInfo.versionCode;
            currentVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersionName;
    }

    //获取服务器端的信息，包括最新的版本号、下载地址、新版新的有关信息
    public void getServiceInf(final String path){
        new Thread(){
            @Override
            public void run() {
                Message message = myHandler.obtainMessage();
                try {
                    URL url = new URL(path);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(2000);
                    httpURLConnection.setReadTimeout(1000);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();

                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode == 200){
                        InputStream inputStream = httpURLConnection.getInputStream();
                        String textFromInputStream = HttpUtil.getTextFromInputStream(inputStream);
                        JSONObject jsonObject = new JSONObject(textFromInputStream);
                        String versionCodeService = jsonObject.getString("versionCode");
                        String downloadurl = jsonObject.getString("downloadurl");
                        String newInformation = jsonObject.getString("newInformation");
                        String[] serviceInf = new String[]{versionCodeService,downloadurl,newInformation};
                        message.obj = serviceInf;
                        message.what = MESSAGE_OK;
                    }
                    else if (responseCode ==500){
                        message.what = MESSAGE_SERVICEEX;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what = MESSAGE_URL;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what = MESSAGE_IO;
                } catch (JSONException e) {
                    e.printStackTrace();
                    message.what = MESSAGE_JSON;
                }
                finally {
                    myHandler.sendMessage(message);
                }
            }
        }.start();
    }
}
