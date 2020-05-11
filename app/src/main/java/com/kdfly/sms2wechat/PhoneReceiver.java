package com.kdfly.sms2wechat;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.kdfly.sms2wechat.utils.PreferenceUtils;
import com.kdfly.sms2wechat.utils.SPUtils;
import com.kdfly.sms2wechat.utils.VerificationUtils;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import static com.kdfly.sms2wechat.SmsReceiver.JSON;
import static com.kdfly.sms2wechat.utils.PrefConst.KEY_DD;
import static com.kdfly.sms2wechat.utils.PrefConst.KEY_SERVERJ;

public class PhoneReceiver extends BroadcastReceiver {
    private boolean isListen = false;
    private String TAG = "PhoneReceiver:";
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: 开始监听");
        mContext = context;
        if (!isListen) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            if (tm != null) {
                tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
                isListen = true;
            }
        }
    }

    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                Log.d(TAG, "onCallStateChanged: 来电:" + incomingNumber);
                String body = "来电话啦: " + incomingNumber;
                if (SPUtils.isEnable(mContext)) {
//                      XLog.i("SmsCode disabled, exiting");
                    sendSms2ServerJ(mContext, body);
                }

                if (SPUtils.isDDEnable(mContext)) {
                    sendSms2DD(mContext, body);
                }

            }
        }
    };

    private void sendSms2ServerJ(Context c, String s) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        String s1 = "您收接到一个电话";


        final String captchas =
                VerificationUtils.parseVerificationCodeIfExists(c, s);
        if (!captchas.equals("")) {
            s1 = String.format(c.getResources().getString(R.string.notify_msg), captchas);
        } else {
            s1 = s;
        }

        String key = PreferenceUtils.getString(c, KEY_SERVERJ, "");
        if (key == "") {
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

    private void sendSms2DD(Context c, String s) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        String s1 = "您收到一条短信";


        final String captchas =
                VerificationUtils.parseVerificationCodeIfExists(c, s);
        if (!captchas.equals("")) {
            s1 = String.format(c.getResources().getString(R.string.notify_msg), captchas);
        } else {
            s1 = s;
        }


        String key = PreferenceUtils.getString(c, KEY_DD, "");
        if (key == "") {
            T.quick(c, "请先设置钉钉机器人的key后再试。");
            return;
        }
        JSONObject json = new JSONObject();
        JSONObject text_json = new JSONObject();
        try {
            if (s1.equals(s)) {
                text_json.put("content", s1);
            } else {
                text_json.put("content", s1 + "\n" + s);
            }

            json.put("msgtype", "text");
            json.put("text", text_json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url("https://oapi.dingtalk.com/robot/send?access_token=" + key)
                .post(body)
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

