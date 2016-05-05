package com.example.haijun.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by haijun on 2016/3/29.
 */

//数据访问层，进行数据库的查询，返回查询后的电话号码类型
public class NumberLoactionDao  {

    public static String getNumberLocation(String number,Context context){
        String resultType = "";
        //正则表达式式检验电话号码是否正确
        if (number.matches("1[3,5,7,8]\\d{9}")){
            String substring = number.substring(0, 7);
            Log.i("substring",substring);
            SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory() + "/naddress.db", null, 0);

            Cursor numinfo = sqLiteDatabase.query("numinfo", new String[]{"outkey"}, "mobileprefix =?", new String[]{substring}, null, null, null, null);
            int type = -1;
            while (numinfo.moveToNext()){
                type = numinfo.getInt(0);
                Log.i("numinfo",type+"");
            }
            if (type!=-1){
                Cursor address_tb = sqLiteDatabase.query("address_tb", new String[]{"city", "cardtype"}, "_id =?", new String[]{type+""}, null, null, null, null);
                while (address_tb.moveToNext()){
                    String city = address_tb.getString(0);
                    String cardtype = address_tb.getString(1);
                    //resultType += city;
                    resultType += cardtype;
                    Log.i("city",city);
                    Log.i("cardtype",cardtype);
                }
            }
        }
        else if (number.length()>=11){
            Toast.makeText(context,"号码格式不正确",Toast.LENGTH_SHORT).show();
        }
        return resultType;
    }
}
