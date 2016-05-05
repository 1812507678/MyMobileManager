package com.example.haijun.javabean;

import android.graphics.drawable.Drawable;

/**
 * Created by haijun on 2016/4/6.
 */
public class DataUseApp {
    private Drawable icon;
    private String lable;
    private String rxData;
    private String txData;

    public DataUseApp(Drawable icon, String lable, String rxData, String txData) {
        this.icon = icon;
        this.lable = lable;
        this.rxData = rxData;
        this.txData = txData;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getRxData() {
        return rxData;
    }

    public void setRxData(String rxData) {
        this.rxData = rxData;
    }

    public String getTxData() {
        return txData;
    }

    public void setTxData(String txData) {
        this.txData = txData;
    }
}
