package com.example.haijun.javabean;

/**
 * Created by haijun on 2016/3/28.
 */
public class Contact  {
    private String name;
    private String Phonenum;

    public Contact() {
    }

    public Contact(String phonenum, String name) {
        Phonenum = phonenum;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenum() {
        return Phonenum;
    }

    public void setPhonenum(String phonenum) {
        Phonenum = phonenum;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", Phonenum='" + Phonenum + '\'' +
                '}';
    }
}
