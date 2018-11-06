package com.kdfly.sms2wechat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndPermission.with(this)
                .runtime()
                .permission(Permission.RECEIVE_SMS)
                .onGranted(permissions -> {
                    // Storage permission are allowed.
                    System.out.println("通过短信读取权限。");
                })
                .onDenied(permissions -> {
                    // Storage permission are not allowed.
                    System.out.println("拒绝短信读取权限。");
                })
                .start();

    }
}
