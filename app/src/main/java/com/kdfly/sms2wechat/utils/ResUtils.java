package com.kdfly.sms2wechat.utils;

import android.content.Context;
import androidx.annotation.RawRes;

import java.io.IOException;
import java.io.InputStream;

public class ResUtils {

    private ResUtils() {

    }

    /**
     * load data from raw resources.
     * @param context context
     * @param rawId raw file id.
     * @return raw file text content.
     */
    public static String loadRawRes(Context context, @RawRes int rawId) {
        InputStream is = null;
        String data = "";
        try {
            is = context.getResources().openRawResource(rawId);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            data = new String(buffer);
        } catch (IOException e) {
            XLog.e("Error occurs when open raw file, id = " + rawId, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }


}
