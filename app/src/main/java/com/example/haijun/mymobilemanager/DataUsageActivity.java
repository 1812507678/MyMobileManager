package com.example.haijun.mymobilemanager;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.haijun.javabean.DataUseApp;

import java.util.ArrayList;
import java.util.List;

public class DataUsageActivity extends Activity {

    private TextView tv_datause_up;
    private TextView tv_datause_down;
    private List<DataUseApp> dataUseAppList;
    private ListView lv_datause_ditail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_usage);

        tv_datause_up = (TextView) findViewById(R.id.tv_datause_up);
        tv_datause_down = (TextView) findViewById(R.id.tv_datause_down);
        lv_datause_ditail = (ListView) findViewById(R.id.lv_datause_ditail);

        initData();
    }

    private void initData(){
        final TrafficStats trafficStats = new TrafficStats();
        long mobileRxBytes = trafficStats.getMobileRxBytes();
        long mobileTxBytes = trafficStats.getMobileTxBytes();

        dataUseAppList = new ArrayList<>();

        tv_datause_up.setText(Formatter.formatFileSize(this,mobileTxBytes));
        tv_datause_down.setText(Formatter.formatFileSize(this,mobileRxBytes));

        new AsyncTask(){

            @Override
            protected Object doInBackground(Object[] params) {
                PackageManager packageManager = DataUsageActivity.this.getPackageManager();
                List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);
                for (ApplicationInfo app:installedApplications) {
                    Drawable drawable = app.loadIcon(packageManager);
                    CharSequence charSequence = app.loadLabel(packageManager);
                    int uid = app.uid;
                    String uidRxBytes = Formatter.formatFileSize(DataUsageActivity.this, trafficStats.getUidRxBytes(uid));
                    String uidTxBytes = Formatter.formatFileSize(DataUsageActivity.this,trafficStats.getUidTxBytes(uid));
                    dataUseAppList.add(new DataUseApp(drawable,charSequence.toString(),uidRxBytes,uidTxBytes));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                MyAdapter myAdapter = new MyAdapter();
                lv_datause_ditail.setAdapter(myAdapter);
            }


        }.execute();
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return dataUseAppList.size();
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
            DataUseApp dataUseApp = dataUseAppList.get(position);
            View inflate = View.inflate(DataUsageActivity.this, R.layout.item_datause, null);
            ImageView iv_itemusdata_image = (ImageView) inflate.findViewById(R.id.iv_itemusdata_image);
            TextView tv_itemusdata_appname = (TextView) inflate.findViewById(R.id.tv_itemusdata_appname);
            TextView tv_itemusdata_rxdata = (TextView) inflate.findViewById(R.id.tv_itemusdata_rxdata);
            TextView tv_itemusdata_txdata = (TextView) inflate.findViewById(R.id.tv_itemusdata_txdata);

            iv_itemusdata_image.setImageDrawable(dataUseApp.getIcon());
            tv_itemusdata_appname.setText(dataUseApp.getLable());
            tv_itemusdata_rxdata.setText("下载:" + dataUseApp.getRxData());
            tv_itemusdata_txdata.setText("上传:"+dataUseApp.getTxData());


            return inflate;
        }
    }
}
