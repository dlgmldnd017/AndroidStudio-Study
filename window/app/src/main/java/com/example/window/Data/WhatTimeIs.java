package com.example.window.Data;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WhatTimeIs extends AppCompatActivity {
    public static String now_time(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd HH:mm");
        String formatDate = sdfNow.format(date);

        return "업데이트: " + formatDate;
    }

    public static int is_alarmTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfNow = new SimpleDateFormat("HH");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfNow1 = new SimpleDateFormat("mm");
        int hour = Integer.parseInt(sdfNow.format(date)) *60;
        int min = Integer.parseInt(sdfNow1.format(date));
        int cur_hour_plus_min = hour + min;

        return cur_hour_plus_min;
    }
}
