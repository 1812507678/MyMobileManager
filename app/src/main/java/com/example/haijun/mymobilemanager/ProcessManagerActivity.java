package com.example.haijun.mymobilemanager;

import android.app.ActivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.haijun.javabean.ProcessInfo;
import com.example.haijun.utils.ProcessUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProcessManagerActivity extends ActionBarActivity {

    private ListView lv_process_appinfo;
    private TextView tv_process_num;
    private TextView tv_process_ram;
    private List<ProcessInfo> processInfoList;
    private List<ProcessInfo> userprocessInfoList;
    private List<ProcessInfo> sysProcessInfoList;
    private MyAdapter myAdapter;
    private boolean isUser = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager);
        lv_process_appinfo = (ListView) findViewById(R.id.lv_process_appinfo);
        tv_process_num = (TextView) findViewById(R.id.tv_process_num);
        tv_process_ram = (TextView) findViewById(R.id.tv_process_ram);

        processInfoList = new ArrayList<>();
        userprocessInfoList = new ArrayList<>();
        sysProcessInfoList = new ArrayList<>();
        myAdapter = new MyAdapter();

        initData();


    }

    private void initData() {
        sysProcessInfoList.clear();
        userprocessInfoList.clear();
        processInfoList = ProcessUtils.getProcessList(this);
        int processCount = ProcessUtils.getProcessCount(this);
        tv_process_num.setText(processCount + "");
        String[] totalRamSize = ProcessUtils.getTotalRamSize(this);
        tv_process_ram.setText(totalRamSize[1] + "/" + totalRamSize[0]);

        for (ProcessInfo processInfo:processInfoList) {
            if (processInfo.isSystem()){
                sysProcessInfoList.add(processInfo);
            }
            else {
                userprocessInfoList.add(processInfo);
            }
        }

        lv_process_appinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProcessInfo processInfo;
                if (isUser) {
                    processInfo = userprocessInfoList.get(position);
                } else {
                    processInfo = processInfoList.get(position);
                }
                CheckBox cb_processlist_check = (CheckBox) view.findViewById(R.id.cb_processlist_check);
                if (cb_processlist_check.isChecked()) {
                    cb_processlist_check.setChecked(false);
                    processInfo.setIsCheck(false);
                } else {
                    cb_processlist_check.setChecked(true);
                    processInfo.setIsCheck(true);
                }
            }
        });
        lv_process_appinfo.setAdapter(myAdapter);
    }


    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if (isUser){
                //Log.i("getCount:User",""+userprocessInfoList.size());
                return userprocessInfoList.size();
            }
            else {
                //Log.i("getCount:all",""+userprocessInfoList.size());
                return processInfoList.size();
            }
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
            ProcessInfo processInfo;
            if (isUser){
                processInfo = userprocessInfoList.get(position);
            }
            else {
                processInfo = processInfoList.get(position);
            }
            View inflate;
            MyHander myHander;
            if (convertView!=null){
                inflate = convertView;
                myHander = (MyHander) convertView.getTag();
            }
            else {
                myHander = new MyHander();
                inflate = View.inflate(ProcessManagerActivity.this, R.layout.item_processlist, null);
                myHander.iv_processlist_icon = (ImageView) inflate.findViewById(R.id.iv_processlist_icon);
                myHander.tv_processlist_appname = (TextView) inflate.findViewById(R.id.tv_processlist_appname);
                myHander.tv_processlist_ram = (TextView) inflate.findViewById(R.id.tv_processlist_ram);
                myHander.cb_processlist_check = (CheckBox) inflate.findViewById(R.id.cb_processlist_check);
                inflate.setTag(myHander);
            }
            myHander.iv_processlist_icon.setImageDrawable(processInfo.getAppicon());
            myHander.tv_processlist_appname.setText(processInfo.getAppname());
            myHander.tv_processlist_ram.setText(processInfo.getAppram() + "");
            myHander.cb_processlist_check.setChecked(processInfo.isCheck());
            return inflate;
        }

        class MyHander{
            ImageView iv_processlist_icon ;
            TextView tv_processlist_appname ;
            TextView tv_processlist_ram;
            CheckBox cb_processlist_check;
        }
    }

    public void updateshow(View view){
        initData();
    }

    public void updateSelect(boolean isChenk){
        if (isUser){
            for (ProcessInfo processInfo:userprocessInfoList) {
                if (processInfo.getPackagename().equals("com.example.haijun.mymobilemanager")){
                    continue;
                }
                processInfo.setIsCheck(isChenk);
            }
        }
        else {
            for (ProcessInfo processInfo:processInfoList) {
                if (processInfo.getPackagename().equals("com.example.haijun.mymobilemanager")){
                    continue;
                }
                processInfo.setIsCheck(isChenk);
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    public void selectall(View view){
        updateSelect(true);
    }

    public void deselectall(View view){
        updateSelect(false);
    }

    public void showuser(View view){
        if (isUser){
            isUser = false;
            //Log.i("showuser","all");
        }
        else {
            isUser = true;
            //Log.i("showuser","user");
        }
        if (myAdapter!=null){
            myAdapter.notifyDataSetChanged();
        }
    }

    public void killselect(View view){
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ProcessInfo> deleteProcessList = new ArrayList<>();
        if (isUser){
            for (ProcessInfo processInfo:userprocessInfoList) {
                if (processInfo.isCheck()){
                    activityManager.killBackgroundProcesses(processInfo.getPackagename());
                    deleteProcessList.add(processInfo);
                }
            }
            for (ProcessInfo processInfo:deleteProcessList) {
                userprocessInfoList.remove(processInfo);
            }
        }
        else {
            for (ProcessInfo processInfo:processInfoList) {
                if (processInfo.isCheck()){
                    activityManager.killBackgroundProcesses(processInfo.getPackagename());
                    deleteProcessList.add(processInfo);
                }
            }
            for (ProcessInfo processInfo:deleteProcessList) {
                processInfoList.remove(processInfo);
            }
        }
        //myAdapter.notifyDataSetChanged();
        initData();
    }
}
