package com.example.haijun.mymobilemanager;

import android.test.AndroidTestCase;

import com.example.haijun.dao.BlackNumberDao;

/**
 * Created by haijun on 2016/4/5.
 */
public class PhoneManageTsts extends AndroidTestCase {

    private BlackNumberDao blackNumberDao;

    public void testInsert(){
        blackNumberDao = new BlackNumberDao(getContext());
        blackNumberDao.insertToBlack("192121", 1);
    }
    public void testDlete(){
        blackNumberDao = new BlackNumberDao(getContext());
        blackNumberDao.deleteBlackNumber("192121");
    }
    public void testQuery(){
        blackNumberDao = new BlackNumberDao(getContext());
        blackNumberDao.getNumberMode("192121");
    }
    public void testUpdate(){
        blackNumberDao = new BlackNumberDao(getContext());
        blackNumberDao.updateBlackNumber("192121", 1);
    }

    public void testPartQuery(){
        blackNumberDao = new BlackNumberDao(getContext());
        for (int i=0;i<100;i++){
            blackNumberDao.insertToBlack("18300"+i, 3);
        }
        blackNumberDao.getNumbePart(0, 10);
    }

    public void testGetAll(){
        blackNumberDao = new BlackNumberDao(getContext());
        int allCount = blackNumberDao.getAllCount();
        assertEquals(103,allCount);
    }

}
