package com.example.haijun.mymobilemanager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haijun.com.example.haijun.application.MyApplication;
import com.example.haijun.dao.LockAppDao;
import com.example.haijun.javabean.AppInfo;
import com.example.haijun.seriver.MyLockAppService;
import com.example.haijun.utils.PackageUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PackageManagerActivity extends Activity {

    List<AppInfo> packageNamesList;
    List<AppInfo> packageNamesListSD;
    List<AppInfo> packageNamesListROM;
    private TextView tv_packageMag_sdSize;
    private TextView tv_packageMag_romSize;
    private TextView tv_packageMag_sizeshow;
    private ListView lv_packageMag_item;
    private PopupWindow popupWindow;
    private AppInfo currentAppInfo;
    private LockAppDao lockAppDao;
    private MyAsyncTask myAsyncTask;
    private MyAdapter myAdapter;
    private ImageView iv_packageM_isopen;
    private TextView tv_packageM_isopen;
    private boolean isSoftLocked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_manager);

        lv_packageMag_item = (ListView) findViewById(R.id.lv_packageMag_item);
        tv_packageMag_sdSize = (TextView) findViewById(R.id.tv_packageMag_sdSize);
        tv_packageMag_romSize = (TextView) findViewById(R.id.tv_packageMag_romSize);
        tv_packageMag_sizeshow = (TextView) findViewById(R.id.tv_packageMag_sizeshow);
        iv_packageM_isopen = (ImageView) findViewById(R.id.iv_packageM_isopen);
        tv_packageM_isopen = (TextView) findViewById(R.id.tv_packageM_isopen);
        packageNamesListSD = new ArrayList<>();
        packageNamesListROM = new ArrayList<>();
        lockAppDao = new LockAppDao(PackageManagerActivity.this);

        isSoftLocked = MyApplication.sharedPreferences.getBoolean("isSoftLocked", false);
        if (!isSoftLocked){
            iv_packageM_isopen.setImageResource(R.drawable.unlock);
            tv_packageM_isopen.setText("未上锁");
        }
        else {
            iv_packageM_isopen.setImageResource(R.drawable.lock);
            tv_packageM_isopen.setText("已上锁");
        }
        initData();
    }

    private void initData() {

        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
        lv_packageMag_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View inflate = View.inflate(PackageManagerActivity.this, R.layout.popupwindow, null);
                LinearLayout ll_popupwind_start = (LinearLayout) inflate.findViewById(R.id.ll_popupwind_start);
                LinearLayout ll_popupwind_share = (LinearLayout) inflate.findViewById(R.id.ll_popupwind_share);
                LinearLayout ll_popupwind_uninstall = (LinearLayout) inflate.findViewById(R.id.ll_popupwind_uninstall);

                ll_popupwind_start.setOnClickListener(new MyOnClickListener());
                ll_popupwind_share.setOnClickListener(new MyOnClickListener());
                ll_popupwind_uninstall.setOnClickListener(new MyOnClickListener());

                if (position < packageNamesListSD.size()) {
                    currentAppInfo = packageNamesListSD.get(position);
                } else {
                    currentAppInfo = packageNamesListROM.get(position);
                }

                if (popupWindow != null) {
                    //让之前的popwindow消失掉
                    popupWindow.dismiss();
                }
                else {
                    popupWindow = new PopupWindow(inflate, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

                }
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.TOP, location[0] + 90, location[1]);
            }
        });
        lv_packageMag_item.setOnItemLongClickListener(new MyItemOnLongClickListener());
    }

    //异步操作，实质是开辟子线程
    class MyAsyncTask extends AsyncTask{
        String availableSdSize;
        String avaiableRomSize;
        //后台执行，进行一些耗时操作
        @Override
        protected Object doInBackground(Object[] params) {
            packageNamesList = PackageUtils.getPackageNames(PackageManagerActivity.this);
            availableSdSize = PackageUtils.getAvailableSdSize(PackageManagerActivity.this);
            avaiableRomSize = PackageUtils.getAvaiableRomSize(PackageManagerActivity.this);

            for (AppInfo appInfo: packageNamesList) {
                if (appInfo.isSDLoad()){
                    packageNamesListSD.add(appInfo);
                }
                else {
                    packageNamesListROM.add(appInfo);
                }
            }
            publishProgress();
            return null;
        }
        //后台进程执行完后执行
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            tv_packageMag_sdSize.setText(availableSdSize);
            tv_packageMag_romSize.setText(avaiableRomSize);
            if (myAdapter==null){
                myAdapter = new MyAdapter();
                lv_packageMag_item.setAdapter(myAdapter);
            }
            else {
                myAdapter.notifyDataSetChanged();
            }

            lv_packageMag_item.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                //页面加载的时候，系统设置setOnScrollListener 会call到一次onScroll。
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    tv_packageMag_sizeshow.setText("用户程序:" + packageNamesListSD.size());
                    if (firstVisibleItem > packageNamesListSD.size() - visibleItemCount) {
                        tv_packageMag_sizeshow.setText("系统程序:" + packageNamesListROM.size());
                    }
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
            });

        }

    }


    class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_popupwind_start:
                    start();
                    break;
                case R.id.ll_popupwind_share:
                    share();
                    break;
                case R.id.ll_popupwind_uninstall:
                    uninstall();
                    break;
            }
        }
    }

    class MyItemOnLongClickListener implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ImageView iv_packageM_islock = (ImageView) view.findViewById(R.id.iv_packageM_islock);
            //获取当前点击的条目
            if (position < packageNamesListSD.size()) {
                currentAppInfo = packageNamesListSD.get(position);
            } else {
                currentAppInfo = packageNamesListROM.get(position);
            }
            //判断当前条目是否被上锁

            boolean locked = lockAppDao.isLocked(currentAppInfo.getPackageName());
            if (locked){
                iv_packageM_islock.setImageResource(R.drawable.unlock);
                lockAppDao.deleteFromDB(currentAppInfo.getPackageName());
                Toast.makeText(PackageManagerActivity.this,"解锁成功",Toast.LENGTH_SHORT).show();
            }
            else {
                iv_packageM_islock.setImageResource(R.drawable.lock);
                lockAppDao.insertIntoDB(currentAppInfo.getPackageName());
                Toast.makeText(PackageManagerActivity.this,"上锁成功",Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }

    private void uninstall() {
        //不一致？？？
        Log.i("getPackageName()",getPackageName());
        Log.i("currentAppInfo()",currentAppInfo.getPackageName());
        if ("com.example.haijun.mymobilemanager".equals(currentAppInfo.getPackageName())){
            Toast.makeText(this,"无法卸载自己",Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!currentAppInfo.isSDLoad()){
            Toast.makeText(this,"无法卸载系统应用",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + currentAppInfo.getPackageName()));
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("resultCode",""+resultCode);
        if (requestCode==100){
            packageNamesListSD.clear();
            packageNamesListROM.clear();
            myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute();
        }
    }

    public void  start(){
        Intent launchIntentForPackage = new Intent();

        launchIntentForPackage= getPackageManager().getLaunchIntentForPackage(currentAppInfo.getPackageName());
        startActivity(launchIntentForPackage);
    }
    private void share() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,currentAppInfo.getPackageName()+",点击下载http://www.baidu.com");
        startActivity(intent);
    }


    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return packageNamesList.size();
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
            AppInfo appInfo;
            if (position<packageNamesListSD.size()){
                appInfo = packageNamesListSD.get(position);
            }
            else {
                appInfo = packageNamesListROM.get(position-packageNamesListSD.size());
            }
            View inflate;
            MyHander myHander;
            if (convertView!=null){
                inflate = convertView;
                myHander = (MyHander) convertView.getTag();
            }
            else {
                myHander = new MyHander();
                inflate = View.inflate(PackageManagerActivity.this, R.layout.item_applist, null);
                myHander.iv_itemlist_image = (ImageView) inflate.findViewById(R.id.iv_itemlist_image);
                myHander.iv_itemlist_lockInon = (ImageView) inflate.findViewById(R.id.iv_packageM_islock);
                myHander.iv_itemgridview_appname = (TextView) inflate.findViewById(R.id.iv_itemgridview_appname);
                myHander.iv_itemgridview_isSDLoad = (TextView) inflate.findViewById(R.id.iv_itemgridview_isSDLoad);
                inflate.setTag(myHander);
            }
            myHander.iv_itemlist_image.setImageDrawable(appInfo.getIcon());
            myHander.iv_itemgridview_appname.setText(appInfo.getLable());
            if (appInfo.isSDLoad()){
                myHander.iv_itemgridview_isSDLoad.setText("安装在SD卡");
            }
            else{
                myHander.iv_itemgridview_isSDLoad.setText("安装在ROM");
            }
            if (lockAppDao.isLocked(appInfo.getPackageName())){
                myHander.iv_itemlist_lockInon.setImageResource(R.drawable.lock);
            }
            else {
                myHander.iv_itemlist_lockInon.setImageResource(R.drawable.unlock);
            }
            return inflate;
        }

        class MyHander{
            ImageView iv_itemlist_image ;
            ImageView iv_itemlist_lockInon ;
            TextView iv_itemgridview_appname;
            TextView iv_itemgridview_isSDLoad;
        }
    }

    public void startLockService(View view){
        isSoftLocked = MyApplication.sharedPreferences.getBoolean("isSoftLocked", false);
        if (isSoftLocked){
            iv_packageM_isopen.setImageResource(R.drawable.unlock);
            MyApplication.sharedPreferences.edit().putBoolean("isSoftLocked",false).commit();
            tv_packageM_isopen.setText("未上锁");
            stopService(new Intent(this, MyLockAppService.class));
            Toast.makeText(this,"软件锁已取消",Toast.LENGTH_SHORT).show();
        }
        else {
            iv_packageM_isopen.setImageResource(R.drawable.lock);
            MyApplication.sharedPreferences.edit().putBoolean("isSoftLocked",true).commit();
            tv_packageM_isopen.setText("已上锁");
            startService(new Intent(this, MyLockAppService.class));
            Toast.makeText(this,"软件已上锁，无法打开上锁的应用",Toast.LENGTH_SHORT).show();
        }
    }
}
