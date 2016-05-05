package com.example.haijun.mymobilemanager;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haijun.dao.AntiVirusDao;
import com.example.haijun.javabean.AppInfo;
import com.example.haijun.utils.Md5Utils;
import com.example.haijun.utils.PackageUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ScanVirusActivity extends Activity {

    private ImageView iv_scanvirus_anmi;
    private List<AppScan> appScanList;
    private ProgressBar pb_scanvirus_progress;
    private ListView lv_scanvirus_showitem;
    private TextView tv_scanvirus_scantext;
    private MyAdapter adapter;
    private RotateAnimation rotateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_virus);
        iv_scanvirus_anmi = (ImageView) findViewById(R.id.iv_scanvirus_anmi);
        tv_scanvirus_scantext = (TextView) findViewById(R.id.tv_scanvirus_scantext);
        pb_scanvirus_progress = (ProgressBar) findViewById(R.id.pb_scanvirus_progress);
        lv_scanvirus_showitem = (ListView) findViewById(R.id.lv_scanvirus_showitem);

        appScanList = new ArrayList<>();
        adapter = new MyAdapter();
        lv_scanvirus_showitem.setAdapter(adapter);
        initAnimation();
        initScanVirus();

    }

    private void initAnimation(){
        rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(3000);
        rotateAnimation.setRepeatCount(-1);
        iv_scanvirus_anmi.setAnimation(rotateAnimation);
        rotateAnimation.start();
    }

    private void initScanVirus() {
        new AsyncTask(){

            private AppScan appScan;
            private List<AppInfo> packageNamelist;

            @Override
            protected void onPreExecute() {
                packageNamelist = PackageUtils.getPackageNames(ScanVirusActivity.this);
                super.onPreExecute();
                pb_scanvirus_progress.setMax(packageNamelist.size());
            }

            @Override
            protected Object doInBackground(Object[] params) {
                PackageManager packageManager = ScanVirusActivity.this.getPackageManager();
                for (int i=0;i<packageNamelist.size();i++) {
                    try {
                        ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageNamelist.get(i).getPackageName(),0);
                        String sourceDir = applicationInfo.sourceDir;
                        String apkMd5Message = Md5Utils.getApkMd5Message(sourceDir);
                        boolean virus = AntiVirusDao.isVirus(apkMd5Message, ScanVirusActivity.this);
                        appScan = new AppScan(packageNamelist.get(i).getPackageName(), virus);

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    publishProgress(++i);

                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                tv_scanvirus_scantext.setText("扫描完成");
                rotateAnimation.cancel();
                Toast.makeText(ScanVirusActivity.this, "扫描完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onProgressUpdate(Object[] values) {
                super.onProgressUpdate(values);
                appScanList.add(appScan);
                adapter.notifyDataSetChanged();
                pb_scanvirus_progress.setProgress((Integer) values[0]);
            }
        }.execute();
    }

    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return appScanList.size();
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
            AppScan appScan = appScanList.get(appScanList.size()-position-1);
            TextView textView = new TextView(ScanVirusActivity.this);
            textView.setText(appScan.getPackageName());
            if (appScan.isVirus()){
                textView.setTextColor(Color.RED);
            }
            return textView;
        }
    }



    class AppScan{
        private String packageName;
        private boolean isVirus;

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public boolean isVirus() {
            return isVirus;
        }

        public void setIsVirus(boolean isVirus) {
            this.isVirus = isVirus;
        }

        public AppScan(String packageName, boolean isVirus) {
            this.packageName = packageName;
            this.isVirus = isVirus;
        }
    }

}
