package com.trainigs.skillup.simplechat.ui;

import android.app.Application;

/**
 * Created by Irakli on 4/23/2016
 */
public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}