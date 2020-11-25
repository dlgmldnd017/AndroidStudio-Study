package com.example.window.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.window.Data.WhatTimeIs;
import com.example.window.MainActivity;
import com.example.window.R;
import com.example.window.ui.deviceSetting.DeviceSettingFragment;
import com.example.window.ui.home.HomeFragment;

public class Window extends AppCompatActivity {
    TextView textView3;
    EditText editText1, editText2;
    Button button1;
    public static boolean isChecked = false;
    public static String name[] = new String[3];
    public static final String[] Devices_Name = new String[3];  //기기 추가는 3개 밖에 안됨.
    public static final String[] Devices_address = new String[3];
    public static final String[] Devices_Temp = new String[3];
    public static final String[] Devices_WheatherData = new String[3];
    public static final String[] Devices_dust10 = new String[3];
    public static final String[] Devices_dust25 = new String[3];
    public static final String[] Devices_update_time = new String[3];
    public static final String[][] Devices_reserve_time = new String[3][];
    public static final String[] Estimated_dust = new String[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);

        textView3 = findViewById(R.id.window_textview3);
        editText1 = findViewById(R.id.window_editText1);
        editText2 = findViewById(R.id.window_editText2);
        textView3.setText(DeviceSettingFragment.address);

        button1 = findViewById(R.id.window_button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText1.getText().toString().equals("") || editText2.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "빈칸을 확인해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Window.this);
                    builder.setTitle("기기 설정");
                    builder.setMessage("\""+editText1.getText().toString()+"\"" + " 기기를 추가 하시겠습니까?");
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "설정 완료", Toast.LENGTH_LONG).show();
                            name[DeviceSettingFragment.i++] = editText1.getText().toString();
                            isChecked = true;
                            Devices_Name[DeviceSettingFragment.count] = editText1.getText().toString();
                            Devices_address[DeviceSettingFragment.count] = textView3.getText().toString();
                            Devices_Temp[DeviceSettingFragment.count] = HomeFragment.tempData;
                            Devices_WheatherData[DeviceSettingFragment.count] = HomeFragment.weatherData;
                            Devices_dust10[DeviceSettingFragment.count] = HomeFragment.dust10;
                            Devices_dust25[DeviceSettingFragment.count] = HomeFragment.dust25;
                            Devices_update_time[DeviceSettingFragment.count++] = WhatTimeIs.now_time();
                            finish();
                        }
                    });

                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                }
        });
    }
}
