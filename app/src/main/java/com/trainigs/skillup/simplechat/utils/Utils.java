package com.trainigs.skillup.simplechat.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.trainigs.skillup.simplechat.ui.MyApplication;

/**
 * Created by Irakli on 4/22/2016
 */
public final class Utils {
    private Utils() {
    }

    public static String parseNumber(String number) {
        if (TextUtils.isEmpty(number))
            return "";
        if (number.contains(" "))
            number = number.replace(" ", "");
        if (number.contains("-"))
            number = number.replace("-", "");
        if (number.contains("+"))
            number = number.replace("+", "");
        if (number.contains("("))
            number = number.replace("(", "");
        if (number.contains(")"))
            number = number.replace(")", "");
        if (number.contains(","))
            number = number.replace(",", "");
        return number;
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}