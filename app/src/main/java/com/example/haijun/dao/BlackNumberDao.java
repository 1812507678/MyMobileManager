package com.example.haijun.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.haijun.db.MyBlackNumberDBHelper;
import com.example.haijun.mymobilemanager.TelephoneManageActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haijun on 2016/4/5.
 */
public class BlackNumberDao {

    private final MyBlackNumberDBHelper myBlackNumberDBHelper;
    private final SQLiteDatabase readableDatabase;

    public BlackNumberDao(Context context) {
        myBlackNumberDBHelper = new MyBlackNumberDBHelper(context,null,null,1);
        readableDatabase = myBlackNumberDBHelper.getReadableDatabase();

    }

    public long insertToBlack(String number,int mode){
        ContentValues contentValues = new ContentValues();
        contentValues.put("number",number);
        contentValues.put("mode", mode);
        long blacknum = readableDatabase.insert("blacknum", null, contentValues);
        return blacknum;
    }

    public int deleteBlackNumber(String number){  //返回删除的行数
        int blacknum = readableDatabase.delete("blacknum", "number=?", new String[]{number});
        return blacknum;
    }

    public int getNumberMode(String number){
        int mode=-1;
        Cursor blacknum = readableDatabase.query("blacknum", new String[]{"mode"}, "number=?", new String[]{number}, null, null, null);
        while (blacknum.moveToNext()){
            mode = blacknum.getInt(0);
        }
        return mode;
    }

    public List<TelephoneManageActivity.BlackNumber> getNumbeAll(){
        List<TelephoneManageActivity.BlackNumber> blackNumberslist = new ArrayList<>();
        Cursor blacknum = readableDatabase.query("blacknum", null, null, null, null, null, null);
        while (blacknum.moveToNext()){
            String number = blacknum.getString(1);
            int mode = blacknum.getInt(2);
            blackNumberslist.add(new TelephoneManageActivity.BlackNumber(mode,number));
        }
        return blackNumberslist;
    }

    //返回从offset开始的大小为limit的行
    public List<TelephoneManageActivity.BlackNumber> getNumbePart(int offset,int limit){
        List<TelephoneManageActivity.BlackNumber> blackNumberslist = new ArrayList<>();
        Cursor blacknum = readableDatabase.query("blacknum", null, null, null, null, null, null,offset+","+limit);
        while (blacknum.moveToNext()){
            String number = blacknum.getString(1);
            int mode = blacknum.getInt(2);
            blackNumberslist.add(new TelephoneManageActivity.BlackNumber(mode,number));
        }
        return blackNumberslist;
    }

    public int updateBlackNumber(String number,int mode){  //
        ContentValues contentValues = new ContentValues();
        contentValues.put("number",number);
        contentValues.put("mode", mode);
        int blacknum = readableDatabase.update("blacknum", contentValues, "number=?", new String[]{number});
        return blacknum;
    }

    public int getAllCount(){  //

        Cursor cursor = readableDatabase.query("blacknum", new String[]{"count(*)"}, null, null, null, null, null);
        cursor.moveToNext();
        int blacknumCount = cursor.getInt(0);
        return blacknumCount;
    }
}
