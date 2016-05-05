package com.example.haijun.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by haijun on 2016/4/5.
 */
public class MyBlackNumberDBHelper extends SQLiteOpenHelper{


    public MyBlackNumberDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "blacknum.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table blacknum(_id Integer primary key autoincrement,number varchar(14),mode int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
