package com.kdfly.sms2wechat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.kdfly.sms2wechat.BuildConfig;

public class PreferenceUtils {
    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(BuildConfig.APPLICATION_ID + "_preferences",
                Context.MODE_PRIVATE);
    }

    public static String getString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }
}
