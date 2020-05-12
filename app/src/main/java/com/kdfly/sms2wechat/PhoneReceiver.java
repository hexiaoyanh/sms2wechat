package com.kdfly.sms2wechat;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.kdfly.sms2wechat.contact.ContactUtils;
import com.kdfly.sms2wechat.contact.MyContacts;
import com.kdfly.sms2wechat.utils.PreferenceUtils;
import com.kdfly.sms2wechat.utils.SPUtils;
import com.kdfly.sms2wechat.utils.VerificationUtils;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static com.kdfly.sms2wechat.SmsReceiver.JSON;
import static com.kdfly.sms2wechat.utils.PrefConst.KEY_DD;
import static com.kdfly.sms2wechat.utils.PrefConst.KEY_SERVERJ;

public class PhoneReceiver extends BroadcastReceiver {
    private boolean isListen = false;
    private String TAG = "PhoneReceiver:";
    private Context mContext;
    private ArrayList<MyContacts> arrayList = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: 开始监听");
        mContext = context;
        if (!isListen) {
            try {
                getContacts(context);
            } catch (SecurityException e) {
                Log.d(TAG, "onReceive: 没有通讯录权限" + e.getMessage());
            }
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
                //获取当然时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String str = formatter.format(curDate);
                String body = "来电话啦: " + incomingNumber + "(" + findIncomingnumber(incomingNumber) + ")  " + str;
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

    private void getContacts(Context context) {
        ContactUtils contactUtils = new ContactUtils();
        arrayList = contactUtils.getAllContacts(context);
        Log.d(TAG, "getContacts: " + arrayList);
    }

    private String findIncomingnumber(String incomingNumber) {
        if (arrayList.size() == 0) return "没有通讯录信息";
        for (MyContacts i :
                arrayList) {
            if (i.getPhone().equals(incomingNumber)) {
                return i.getName();
            }
        }
        return "未知电话";
    }

    private void sendSms2ServerJ(Context c, String s) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        String s1 = "您接收到一个电话";

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
        String s1 = "您接收到一个电话";

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

