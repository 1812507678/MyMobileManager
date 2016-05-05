package com.example.haijun.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * Created by haijun on 2016/4/5.
 */
public class AntiVirusDao {
    public static boolean isVirus(String name,Context context){
        boolean isVirus = false;
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory()+"/"+"antivirus.db", null, 0);
        Cursor datable = sqLiteDatabase.query("datable", null, "md5=?", new String[]{name}, null, null, null, null);
        if (datable.moveToNext()){
            isVirus = true;
        }
        return isVirus;
    }
}
