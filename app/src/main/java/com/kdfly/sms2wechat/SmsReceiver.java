package com.kdfly.sms2wechat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
//                Log.i("123123", msg + "," + ss + "," + from);
                sendSms2ServerJ(context, msg);
            }
        }
    }

    private void sendSms2ServerJ(Context c, String s){
        OkHttpClient mOkHttpClient = new OkHttpClient();
//        RequestBody requestBody = RequestBody.create(null, s);
        String s1 = "您收到一条短信";

        //分析一下是否为验证码短信。
        String captchas = StringUtils.tryToGetCaptchas(s);
        if(!captchas.equals("")) {
            s1 = String.format(c.getResources().getString(R.string.notify_msg), captchas);
        }else{
            s1 = s;
        }

//        T.quick(c, s1 + "\n" + s);

        SharedPreferences read = c.getSharedPreferences("serverJ", Context.MODE_PRIVATE);
        String key = read.getString("key", "");
        if (key==""){
            T.quick(c, "请先设置Server酱的key后再试。");
            return;
        }

        Request request = new Request.Builder()
                .url("https://sc.ftqq.com/" + key + ".send?text=" + s1 + "&desp=" + s)
                .get()
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                T.quick(c, "服务器维护中，请稍后再试。");
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                        System.out.println(Rsa.encryptByPublicKey("imei"));
//                        System.out.println("反馈结果：" + response.body().string());

                if (response.code() == 200) {
                    String t2 = response.body().string();
                    if (!t2.isEmpty()) {
                        Looper.prepare();
                        T.quick(c, "发送成功。");
                        Looper.loop();
                    }
                } else {
                    Looper.prepare();
                    T.quick(c, "发送失败，无法连接到远程Server酱服务器。");
                    Looper.loop();
                }
            }
        });
    }
}
