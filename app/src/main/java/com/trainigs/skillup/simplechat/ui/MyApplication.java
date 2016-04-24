package com.trainigs.skillup.simplechat.ui;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

/**
 * Created by Irakli on 4/23/2016
 */
public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    private SharedPreferences prefs;

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public String getNumber(){
        return MyApplication.getInstance().getPrefs().getString("number", "");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        prefs = getSharedPreferences("com.trainigs.skillup.simplechat", Context.MODE_PRIVATE);
    }
}