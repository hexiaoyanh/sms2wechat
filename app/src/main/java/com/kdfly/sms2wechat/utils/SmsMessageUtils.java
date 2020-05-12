package com.kdfly.sms2wechat.utils;

import android.telephony.SmsMessage;

/**
 * Utils about android.telephony.SmsMessage
 */
public class SmsMessageUtils {

    private static final int SMS_CHARACTER_LIMIT = 160;
    private SmsMessage[] smsMessage;

    public SmsMessageUtils(SmsMessage[] smsmessage) {
        smsMessage = smsmessage;
    }

    public String getMessageBody() {
        if (smsMessage.length == 1) {
            return smsMessage[0].getDisplayMessageBody();
        } else {
            StringBuilder sb = new StringBuilder(SMS_CHARACTER_LIMIT * smsMessage.length);
            for (SmsMessage messagePart : smsMessage) {
                sb.append(messagePart.getDisplayMessageBody());
            }
            return sb.toString();
        }
    }

//    public static String getTimetampMillis()
}
