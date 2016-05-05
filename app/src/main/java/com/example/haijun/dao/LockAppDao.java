package com.example.haijun.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.haijun.db.MyLockAppDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haijun on 2016/4/1.
 */
public class LockAppDao  {

    private final SQLiteDatabase readableDatabase;
    private final MyLockAppDBHelper myLockAppDBHelper;
    Context context;

    public LockAppDao(Context context) {
        myLockAppDBHelper = new MyLockAppDBHelper(context,"applack.db",null,1);
        readableDatabase = myLockAppDBHelper.getReadableDatabase();
        this.context = context;
    }

    public void insertIntoDB(String packageName){
        /*ContentValues contentValues = new ContentValues();
        contentValues.put("packagename",packageName);
        readableDatabase.insert("lockApp",null,contentValues);*/

        ContentValues contentValues = new ContentValues();
        contentValues.put("packagename",packageName);

        context.getContentResolver().insert(Uri.parse("content://com.haijun.mobilemanage"),contentValues);

    }

    public void deleteFromDB(String packageName){
        //readableDatabase.delete("lockApp", "packagename=?", new String[]{packageName});
        context.getContentResolver().delete(Uri.parse("content://com.haijun.mobilemanage"),"packagename=?", new String[]{packageName});
    }

    public boolean isLocked(String packageName){
        boolean flag = false;
        //Cursor cursor = this.readableDatabase.query("lockApp", new String[]{"packagename"}, "packagename=?", new String[]{packageName}, null, null, null);
        Cursor query = context.getContentResolver().query(Uri.parse("content://com.haijun.mobilemanage"),
                new String[]{"packagename"}, "packagename=?", new String[]{packageName}, null);
        while (query.moveToNext()){
            flag=true;
        }
        return flag;
    }

    public List<String>getAllLockedApp(){
        List<String> applList = new ArrayList<>();
        Cursor lockApp = readableDatabase.query("lockApp", new String[]{"packagename"}, null, null, null, null, null, null);
        while (lockApp.moveToNext()){
            applList.add(lockApp.getString(0));
        }
        return applList;
    }
}
