package com.trainigs.skillup.simplechat.ui;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by Irakli on 4/23/2016
 */
public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    private String mPhoneNumber;

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}