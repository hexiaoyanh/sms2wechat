package com.kdfly.sms2wechat;

import android.content.Context;
import android.widget.Toast;

public class T {
    public static void quick(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void slow(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    public static void p(String content){
        System.out.println(content);
    }
}
