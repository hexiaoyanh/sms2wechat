package com.kdfly.sms2wechat.utils;

import android.content.Context;

public class SPUtils {

    /**
     * 获取验证码短信关键字(正则表达式)
     */
    public static String getSMSCodeKeywords(Context context) {
        return PreferenceUtils.getString(context,
                PrefConst.KEY_SMSCODE_KEYWORDS, PrefConst.SMSCODE_KEYWORDS_DEFAULT);
    }


}
