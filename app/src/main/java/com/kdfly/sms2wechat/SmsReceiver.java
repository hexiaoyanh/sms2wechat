package com.kdfly.sms2wechat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            //获取pdus数组
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            //对SmsMessage数组赋值
            for (int i = 0; i < pdus.length; i++) {

//                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], intent.getStringExtra("format"));
            }
            for (SmsMessage smsMessage : messages) {

                //短信的内容
                String msg = smsMessage.getMessageBody();
                //短信的接收时间
                long when = smsMessage.getTimestampMillis();
                //短信发送方号码
                String from = smsMessage.getOriginatingAddress();
                Date date = new Date(when);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String ss = simpleDateFormat.format(date);
                Log.i("123123", msg + "," + ss + "," + from);
            }
        }
    }
}
