package com.example.haijun.mymobilemanager;

import android.test.AndroidTestCase;

import com.example.haijun.dao.AntiVirusDao;

/**
 * Created by haijun on 2016/4/5.
 */
public class VirusTest extends AndroidTestCase {
    public void testDb(){
        assertTrue(AntiVirusDao.isVirus("540e8b5fdff054be1831cfbb4cdef7f1",getContext()));
    }
}
