package com.example.haijun.mymobilemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haijun.dao.BlackNumberDao;

import java.util.ArrayList;
import java.util.List;

public class TelephoneManageActivity extends Activity {

    private static final int LIMIT = 15;
    private ListView lv_telephone_blackitem;
    private List<BlackNumber> blackNumberList;
    private MyAdapter myAdapter;
    private BlackNumberDao blackNumberDao;
    private int offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone_manage);
        lv_telephone_blackitem = (ListView) findViewById(R.id.lv_telephone_blackitem);

        blackNumberList = new ArrayList<>();
        blackNumberDao = new BlackNumberDao(this);
        showbackBlackNumber();
        myAdapter = new MyAdapter();
        lv_telephone_blackitem.setAdapter(myAdapter);


        //点击，进行删除
        lv_telephone_blackitem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final BlackNumber blackNumber = blackNumberList.get(position);
                new AlertDialog.Builder(TelephoneManageActivity.this)
                        .setTitle("确定删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int deleteRows = blackNumberDao.deleteBlackNumber(blackNumber.getNumber());
                                if (deleteRows>=1){  //返回1.表示删除成功
                                    blackNumberList.remove(position);
                                    myAdapter.notifyDataSetChanged();
                                    Toast.makeText(TelephoneManageActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        //点长按，进行修改
        lv_telephone_blackitem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final BlackNumber blackNumber = blackNumberList.get(position);
                new AlertDialog.Builder(TelephoneManageActivity.this)
                        .setTitle("选择修改模式")
                        .setSingleChoiceItems(new String[]{"拦截电话", "拦截短信", "全部拦截"},blackNumber.mode-1 , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int upDateRows = blackNumberDao.updateBlackNumber(blackNumber.getNumber(), which + 1);
                                if (upDateRows==1){
                                    blackNumber.setMode(which+1);
                                    myAdapter.notifyDataSetChanged();
                                    Toast.makeText(TelephoneManageActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                        }).show();

                return true;
            }
        });

        lv_telephone_blackitem.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case SCROLL_STATE_IDLE:

                        break;
                    case SCROLL_STATE_FLING:
                        int lastVisiblePosition = view.getLastVisiblePosition();
                        if (lastVisiblePosition==blackNumberList.size()-1 && blackNumberList.size()>blackNumberDao.getAllCount()){
                            offset +=LIMIT;
                            Log.i("update","update:"+offset);
                            List<BlackNumber> numbePart = blackNumberDao.getNumbePart(offset, LIMIT);
                            blackNumberList.addAll(numbePart);
                            myAdapter.notifyDataSetChanged();
                        }
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:

                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    public void showbackBlackNumber(){
        //blackNumberList = blackNumberDao.getNumbeAll();
        offset = 0;
        blackNumberList = blackNumberDao.getNumbePart(offset,LIMIT);
    }

    public void addblackNumber(View view){
        View inflate = View.inflate(this, R.layout.alert_blackinput, null);
        Button bt_alert_confirm = (Button) inflate.findViewById(R.id.bt_alert_confirm);
        Button bt_alert_cancle = (Button)  inflate.findViewById(R.id.bt_alert_cancle);
        final RadioGroup rb_alert_choosetype = (RadioGroup)inflate.findViewById(R.id.rb_alert_choosetype);
        final EditText ed_alert_number = (EditText) inflate.findViewById(R.id.ed_alert_number);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(inflate).create();
        alertDialog.show();
        bt_alert_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = ed_alert_number.getText().toString();
                if (number.isEmpty()){
                    Toast.makeText(TelephoneManageActivity.this,"请输入号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                int checkedRadioButtonId = rb_alert_choosetype.getCheckedRadioButtonId();
                int mode=-1;
                if(checkedRadioButtonId==R.id.rb_alert_phone){
                    mode =1;
                }
                else if(checkedRadioButtonId==R.id.rb_alert_msm){
                    mode =2;
                }
                else if(checkedRadioButtonId==R.id.rb_alert_all){
                    mode =3;
                }
                else{
                    Toast.makeText(TelephoneManageActivity.this,"未选择拦截模式",Toast.LENGTH_SHORT).show();
                    return;
                }
                blackNumberDao.insertToBlack(number, mode);
                blackNumberList.add(new BlackNumber(mode, number));
                myAdapter.notifyDataSetChanged();
                Toast.makeText(TelephoneManageActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }

        });
        bt_alert_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }



    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return blackNumberList.size();
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
            BlackNumber blackNumber = blackNumberList.get(position);
            View inflate = View.inflate(TelephoneManageActivity.this,R.layout.item_blacknumberlist, null);
            TextView tv_blacknum_number = (TextView) inflate.findViewById(R.id.tv_blacknum_number);
            TextView tv_blacknum_mode = (TextView) inflate.findViewById(R.id.tv_blacknum_mode);
            tv_blacknum_number.setText(blackNumber.getNumber());
            if (blackNumber.getMode()==1){
                tv_blacknum_mode.setText("拦截电话");
            }
            else if (blackNumber.getMode()==2){
                tv_blacknum_mode.setText("拦截短信");
            }
            else if (blackNumber.getMode()==3){
                tv_blacknum_mode.setText("拦截全部");
            }
            return inflate;
        }
    }

    public static class BlackNumber{
        String number;
        int mode;

        public BlackNumber(int mode, String number) {
            this.mode = mode;
            this.number = number;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }
    }
}
