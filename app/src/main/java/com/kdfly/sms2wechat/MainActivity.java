package com.kdfly.sms2wechat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Filter;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity:";
    private PhoneReceiver phoneReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt1 = findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_edit(v);
            }
        });

    }

    public void alert_edit(View view) {
        final EditText et = new EditText(this);

        SharedPreferences read = getSharedPreferences("serverJ", MODE_PRIVATE);
        String value = read.getString("key", "");
        et.setText(value);

        new AlertDialog.Builder(this).setTitle("请输入您在Server酱的key")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String key = et.getText().toString().trim();
                        SharedPreferences.Editor editor = getSharedPreferences("serverJ", MODE_PRIVATE).edit();
                        editor.putString("key", key);
                        editor.commit();
                    }
                }).setNegativeButton("取消", null).show();
    }
}
