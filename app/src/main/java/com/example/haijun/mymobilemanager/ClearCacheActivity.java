package com.example.haijun.mymobilemanager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haijun.javabean.AppCacheInfo;
import com.example.haijun.javabean.AppInfo;
import com.example.haijun.utils.PackageUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClearCacheActivity extends Activity {

    private ProgressBar pb_clearcache_progress;
    private TextView tv_clearcache_iscleaning;
    private ListView lv_clearcache_hascache;
    private List<AppCacheInfo> packageNameList;
    private PackageManager packageManager;
    private Button bt_clearcache_clear;
    private MyAdapter myAdapter;
    private List<AppInfo> packageNames;   //所有的应用程序的包名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_cache);

        pb_clearcache_progress = (ProgressBar) findViewById(R.id.pb_clearcache_progress);
        tv_clearcache_iscleaning = (TextView) findViewById(R.id.tv_clearcache_iscleaning);
        lv_clearcache_hascache = (ListView) findViewById(R.id.lv_clearcache_hascache);
        bt_clearcache_clear = (Button) findViewById(R.id.bt_clearcache_clear);

        packageNameList = new ArrayList<>();

        init();

    }

    public void init(){
        new AsyncTask<Void,Integer,Float>(){
            @Override
            protected void onPreExecute() {
                packageNames = PackageUtils.getPackageNames(ClearCacheActivity.this);
                pb_clearcache_progress.setMax(packageNames.size());

                packageManager = getPackageManager();
                super.onPreExecute();
            }
            @Override
            protected Float doInBackground(Void... params) {
                int count=0;
                while (count<packageNames.size()){
                    try {
                        /*Log.i("doInBackground","doInBackground:running");
                        if (isCancelled()){
                            Log.i("doInBackg:isCancelled",isCancelled()+"");
                            break;
                        }*/
                        final Class<?> pmClass = ClearCacheActivity.this.getClassLoader().loadClass("android.content.pm.PackageManager");
                        Method getPackageSizeInfo = pmClass.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                        getPackageSizeInfo.invoke(packageManager, packageNames.get(count).getPackageName(), mStatsObserver);
                    }catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    publishProgress(++count);
                }

                return Float.valueOf(count);
            }

            @Override
            protected void onPostExecute(Float aFloat) {
                //Log.i("onPostExecute", "onPostExecute");
                onCancelled();
                //Log.i("onPost:isCancelled", isCancelled() + "");
                bt_clearcache_clear.setVisibility(View.VISIBLE);
                if (packageNameList.size()==0){
                    bt_clearcache_clear.setText("没有发现缓存");
                }
                else {
                    myAdapter = new MyAdapter();
                    lv_clearcache_hascache.setAdapter(myAdapter);
                }
                super.onPostExecute(aFloat);
                tv_clearcache_iscleaning.setText("扫描完成");
                Toast.makeText(ClearCacheActivity.this,"扫描完成",Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                //Log.i("onPostExecute:flag", flag+"");
                pb_clearcache_progress.setProgress(values[0]);
                pb_clearcache_progress.setSecondaryProgress(values[0] * 2);
                String packageName = packageNames.get(values[0] - 1).getPackageName();
                tv_clearcache_iscleaning.setText(packageName);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                cancel(true);
            }
        }.execute();
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return packageNameList.size();
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
            AppCacheInfo appCacheInfo = packageNameList.get(position);
            View inflate = View.inflate(ClearCacheActivity.this,R.layout.item_cachelist, null);
            ImageView iv_cachelist_icon = (ImageView) inflate.findViewById(R.id.iv_cachelist_icon);
            TextView tv_cachelist_appname = (TextView) inflate.findViewById(R.id.tv_cachelist_appname);
            TextView tv_cachelist_appcache = (TextView) inflate.findViewById(R.id.tv_cachelist_appcache);

            iv_cachelist_icon.setImageDrawable(appCacheInfo.getIcon());
            tv_cachelist_appname.setText(appCacheInfo.getName());
            String formatFileSize = Formatter.formatFileSize(ClearCacheActivity.this, appCacheInfo.getSize());
            tv_cachelist_appcache.setText(formatFileSize);

            return inflate;
        }
    }

    public void clear(View view){
        if (packageNameList.size()==0){
            return;
        }
        try {
            Class<?> aClass = getClassLoader().loadClass("android.content.pm.PackageManager");
            Method freeStorageAndNotify = aClass.getDeclaredMethod("freeStorageAndNotify", Long.TYPE, IPackageDataObserver.class);
            freeStorageAndNotify.invoke(packageManager, Long.MAX_VALUE, new MyIPackageDataObserver());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub(){

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)  {
            final long cacheSize = pStats.cacheSize;
            final String packageName = pStats.packageName;
            if (cacheSize>12288){
                try {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                    Drawable drawable = applicationInfo.loadIcon(packageManager);
                    CharSequence charSequence = applicationInfo.loadLabel(packageManager);
                    packageNameList.add(new AppCacheInfo(cacheSize,drawable,charSequence.toString()));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*int showPackageCount=0;
                    Log.i("runOnUiThread","setText");
                    if (++showPackageCount==packageNames.size()){
                        tv_clearcache_iscleaning.setText("扫描完成");
                    }
                    tv_clearcache_iscleaning.setText(packageName+":"+cacheSize);*/
                }
            });
        }
    };

    class MyIPackageDataObserver extends IPackageDataObserver.Stub {
        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
            Log.i("MyIPackageDataObserver","onRemoveCompleted");
            packageNameList.clear();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myAdapter.notifyDataSetChanged();
                    Log.i("MyIPackageDataObserver", "runOnUiThread");
                }
            });
        }
    }
}
