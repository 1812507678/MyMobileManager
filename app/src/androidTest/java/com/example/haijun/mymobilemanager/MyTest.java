package com.example.haijun.mymobilemanager;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.haijun.dao.LockAppDao;
import com.example.haijun.db.MyLockAppDBHelper;

import java.util.List;

/**
 * Created by haijun on 2016/4/1.
 */
public class MyTest extends AndroidTestCase {
    public void testDB(){
        MyLockAppDBHelper myLockAppDBHelper =new MyLockAppDBHelper(getContext(),"applack.db",null,1);

    }
    public void testLoakAppDao(){
        LockAppDao lockAppDao = new LockAppDao(getContext());
        lockAppDao.insertIntoDB("test");
        boolean test = lockAppDao.isLocked("test");
        System.out.println(test);
        Log.i("test",""+test);
        lockAppDao.deleteFromDB("test");

    }

    public void testLoaklist(){
        LockAppDao lockAppDao = new LockAppDao(getContext());
        lockAppDao.insertIntoDB("test");
        lockAppDao.insertIntoDB("test");
        List<String> allLockedApp = lockAppDao.getAllLockedApp();
        int size = allLockedApp.size();
        System.out.println("size:"+size);

    }
}
