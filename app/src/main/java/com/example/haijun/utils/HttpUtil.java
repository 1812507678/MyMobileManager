package com.example.haijun.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by haijun on 2016/3/24.
 */
//工具类：用于http网络传输
public class HttpUtil  {
    //将一个输入流转化为字符String
    public static String getTextFromInputStream(InputStream inputStream){
        String textInfor = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int read = -1;
        try {
            while ((read=inputStream.read(bytes,0,bytes.length))!=-1){
                byteArrayOutputStream.write(bytes,0,read);
            }
            textInfor = byteArrayOutputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textInfor;
    }
}
