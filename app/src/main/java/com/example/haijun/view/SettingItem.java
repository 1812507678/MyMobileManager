package com.example.haijun.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.haijun.com.example.haijun.application.MyApplication;
import com.example.haijun.mymobilemanager.R;

/**
 * Created by haijun on 2016/3/25.
 */

//设置中心模块的每一个设置条目的自定义View类，自定义控件
public class SettingItem extends RelativeLayout {

    private CheckBox ck_updateitem_chenkupdate;
    private TextView tc_updateitem_update_1;
    private TextView tv_updateitem_update;

    private String title;
    private String checkstring;
    private String uncheckstring;
    private String spkeyname;

    private MyOnClickListener myOnClickListener;

    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SettingItem(Context context) {
        super(context);
        init(null);
    }

    public interface MyOnClickListener{
        void checkOnClick();
        void cancelOnClick();
    }

    //根据不同的设置条目，对CheckBox进行自行控制
    public void setCheckBox(boolean flag){
        if (flag){
            ck_updateitem_chenkupdate.setChecked(true);
            tc_updateitem_update_1.setText(checkstring);
        }
        else {
            ck_updateitem_chenkupdate.setChecked(false);
            tc_updateitem_update_1.setText(uncheckstring);
        }
    }

    public void setMyOnClickListener(MyOnClickListener listener){
        myOnClickListener = listener;
    }

    //初始化VIew，传入的attrs为此控件xml文件中设置的属性
    private void init(AttributeSet attrs) {
        View inflate = View.inflate(getContext(),R.layout.update_item,null);
        tv_updateitem_update = (TextView) inflate.findViewById(R.id.tv_updateitem_update);
        tc_updateitem_update_1 = (TextView) inflate.findViewById(R.id.tc_updateitem_update_1);
        ck_updateitem_chenkupdate = (CheckBox) inflate.findViewById(R.id.ck_updateitem_chenkupdate);

        if (attrs!=null){
            //通过属性获取对应属性的值
            title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "udpadetitle");
            checkstring = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "checkstring");
            uncheckstring = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "uncheckstring");
            spkeyname = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "spkeyname");
        }

        //条目打开是默认的选中状态
        tv_updateitem_update.setText(title);
        if (MyApplication.sharedPreferences.getBoolean(spkeyname,true)){
            tc_updateitem_update_1.setText(checkstring);
            ck_updateitem_chenkupdate.setChecked(true);
        }
        else {
            tc_updateitem_update_1.setText(uncheckstring);
            ck_updateitem_chenkupdate.setChecked(false);
        }

        addView(inflate);
        setOnClickListener(new SeetingItemOnClickListener());
    }

    //给控件设置自定义监听器，相应用户点击事件
    class SeetingItemOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            boolean checked = ck_updateitem_chenkupdate.isChecked();
            //如果用户点击选中，则改变对应的显示值，并把数据存入sharedPreferences
            if (checked){
                ck_updateitem_chenkupdate.setChecked(false);
                tc_updateitem_update_1.setText(uncheckstring);
                MyApplication.sharedPreferences.edit().putBoolean(spkeyname,false).commit();
                if (myOnClickListener!=null){
                    myOnClickListener.cancelOnClick();
                }
            }
            else {
                ck_updateitem_chenkupdate.setChecked(true);
                tc_updateitem_update_1.setText(checkstring);
                MyApplication.sharedPreferences.edit().putBoolean(spkeyname,true).commit();
                if (myOnClickListener!=null){
                    myOnClickListener.checkOnClick();

                }
            }
        }
    }
}
