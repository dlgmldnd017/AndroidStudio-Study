package com.example.window.ui.home;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.window.Adapter.Window;
import com.example.window.Data.SendToServer;
import com.example.window.Data.WhatTimeIs;
import com.example.window.MainActivity;
import com.example.window.R;
import com.example.window.Service.PushService;
import com.example.window.ui.alarm.AlarmFragment;
import com.example.window.ui.deviceSetting.DeviceSettingFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    SendToServer sendToServer = new SendToServer();
    public static CardView cardView_one,cardView_two,cardView_three;
    public static int openState;
    public static boolean open_state = false;
    public static int now_device = 0;
    static int a = 1;       //case문을 받기 위한 조건 변수
    static int b = 0;
    public static String Estimated_Dust="";
    ProgressDialog progressDialog;
    Handler handler;

    public static Switch switch1,switch2,switch3,switch4,switch5,switch6;

    ImageButton button1;
    Button button2;
    ImageView imageView;
    TextView textView1, textView2, textView3,textView4,textView5,textView6;
    public static TextView[]  textViews = new TextView[6];
    int[] images = {R.drawable.sunny, R.drawable.cloudy, R.drawable.rainny};
    public static String address = DeviceSettingFragment.address;       //DeviceSettingFragment에서 위치를 받아오는 변수
    public static String tempData = "--℃";      //메인화면에 기온을 알려주는 변수
    public static String weatherData = "--";    //메인화면에 날씨 정보를 알려주는 변수
    public static String dust10 = "미세먼지: ---";
    public static String dust25= "초미세먼지: ---";
    public static boolean isPush = false;
    public static int c = 0;
    public static int d = 1;

    public HomeFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);
        imageView = root.findViewById(R.id.Home_imageView1);

        switch1 = root.findViewById(R.id.home_switch1);
        switch2 = root.findViewById(R.id.home_switch2);
        switch3 = root.findViewById(R.id.home_switch3);
        switch4 = root.findViewById(R.id.home_switch4);
        switch5 = root.findViewById(R.id.home_switch5);
        switch6 = root.findViewById(R.id.home_switch6);

        textView1 = root.findViewById(R.id.home_textView1);
        textView2 = root.findViewById(R.id.home_textView2);
        textView3 = root.findViewById(R.id.home_textView3);
        textView4 = root.findViewById(R.id.home_textView4);
        textView5 = root.findViewById(R.id.home_textView5);
        textView6 = root.findViewById(R.id.textView999);
        textViews[0] = root.findViewById(R.id.home_textview6);
        textViews[1] = root.findViewById(R.id.home_textview7);
        textViews[2] = root.findViewById(R.id.home_textview8);
        textViews[3] = root.findViewById(R.id.home_textview9);
        textViews[4] = root.findViewById(R.id.home_textview10);
        textViews[5] = root.findViewById(R.id.home_textview11);

        textViews[1].setText(Estimated_Dust);

        button1 = root.findViewById(R.id.home_button1);     //해당 기기의 날씨 정보 업데이트
        button1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View v) {
                final MainActivity mainActivity = (MainActivity) getActivity();
                progressDialog = new ProgressDialog(mainActivity);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("업데이트 중...");
                progressDialog.show();
                a=2;

                handler = new Handler() {
                    @SuppressLint("HandlerLeak")
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
                        if (msg.arg1 == 100) {
                            dialog.setCancelable(false);
                            if (!Window.isChecked) {
                                dialog.setMessage("기기가 확인이 되지 않습니다.");
                                dialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog.dismiss();
                                    }
                                });
                                dialog.show();
                            } else if (address.equals("주소를 찾을 수가 없습니다.")) {
                                dialog.setMessage("위치 설정과 네트워크를 다시 한번 확인해주세요.");
                                dialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }else{
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tempGetApi(Window.Devices_address[now_device]); //Window.Devices_address[now_device]
                                        weatherGetApi(Window.Devices_address[now_device]);
                                        getDust10Api(Window.Devices_address[now_device]);
                                        getDust25Api(Window.Devices_address[now_device]);
                                        Window.Devices_update_time[now_device] = WhatTimeIs.now_time();
                                        if(Window.isChecked) {       //초기 메인화면 구성
                                            textView1.setText(Window.Devices_address[now_device]);
                                            textView2.setText(Window.Devices_Temp[now_device]);
                                            textView3.setText(Window.Devices_WheatherData[now_device]);
                                            textView4.setText(Window.Devices_dust10[now_device]);
                                            textView5.setText(Window.Devices_dust25[now_device]);
                                            textView6.setText(Window.Devices_update_time[now_device]);
                                            if (now_device == 1) {
                                                textViews[now_device + 1].setText(Window.name[now_device]);
                                            } else if (now_device == 2) {
                                                textViews[now_device + 2].setText(Window.name[now_device]);
                                            } else {
                                                textViews[now_device].setText(Window.name[now_device]);
                                            }
                                        }
                                    }
                                }).start();
                                selectImage(imageView);
                                progressDialog.dismiss();
                            }
                        }
                    }
                };
                switch (a) {
                    case 2:
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i <= 220; i++) {
                                    progressDialog.setProgress(i);
                                    Message msg = handler.obtainMessage();
                                    msg.arg1 = i * 4;
                                    handler.sendMessage(msg);
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        t.start();
                        break;
                }
            }
        });

        button2 = root.findViewById(R.id.home_button2);     //기기 변경과 동시에 해당 기기에 대한 날씨 정보 변경
        button2.setOnClickListener(new View.OnClickListener() {
                                       @RequiresApi(api = Build.VERSION_CODES.M)
                                       @Override
                                       public void onClick(View v) {
                                           String[] str;
                                           if (DeviceSettingFragment.count == 2 || DeviceSettingFragment.count == 3) {
                                               if(DeviceSettingFragment.count == 2){
                                                   str = new String[2];
                                                   str[0] = Window.Devices_Name[0];
                                                   str[1] = Window.Devices_Name[1];
                                               }else{
                                                   str = Window.Devices_Name;
                                               }
                                               final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                               builder.setTitle("현재 날씨 변경");
                                               builder.setSingleChoiceItems(str, now_device,
                                                       new DialogInterface.OnClickListener() {
                                                           public void onClick(DialogInterface dialog, int whichButton) {
                                                               textView1.setText(Window.Devices_address[whichButton]);
                                                               textView2.setText(Window.Devices_Temp[whichButton]);
                                                               textView3.setText(Window.Devices_WheatherData[whichButton]);
                                                               textView4.setText(Window.Devices_dust10[whichButton]);
                                                               textView5.setText(Window.Devices_dust25[whichButton]);
                                                               textView6.setText(Window.Devices_update_time[whichButton]);
                                                               now_device = whichButton;
                                                               dialog.cancel();

                                                           }
                                                       });
                                               builder.show();
                                           }
                                       }
                                   });

        cardView_one = root.findViewById(R.id.home_one);
        cardView_two = root.findViewById(R.id.home_two);
        cardView_three = root.findViewById(R.id.home_three);


        switch1.setChecked(isPush);
        switch2.setChecked(open_state);

        if(Window.isChecked){       //초기 메인화면 구성
            textView1.setText(Window.Devices_address[now_device]);
            textView2.setText(Window.Devices_Temp[now_device]);
            textView3.setText(Window.Devices_WheatherData[now_device]);
            textView4.setText(Window.Devices_dust10[now_device]);
            textView5.setText(Window.Devices_dust25[now_device]);
            textView6.setText(Window.Devices_update_time[now_device]);
            if(now_device == 1){
                textViews[now_device+1].setText(Window.name[now_device]);
            }else if(now_device == 2){
                textViews[now_device+2].setText(Window.name[now_device]);
            }else{
                textViews[now_device].setText(Window.name[now_device]);
            }

            for(int i=0; i<DeviceSettingFragment.count; i++){
                if(DeviceSettingFragment.count==1){
                    cardView_one.setVisibility(View.VISIBLE);
                }else if(DeviceSettingFragment.count==2){
                    cardView_one.setVisibility(View.VISIBLE);
                    cardView_two.setVisibility(View.VISIBLE);
                }else{
                    cardView_one.setVisibility(View.VISIBLE);
                    cardView_two.setVisibility(View.VISIBLE);
                    cardView_three.setVisibility(View.VISIBLE);
                }
                textViews[i*2].setText(Window.name[i]);
            }
        }

        if(!isPush){
            PushService.index2=0;
            PushService.index3=0;
        }

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!MainActivity.isPush){
                    isPush = false;
                    switch1.setChecked(false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("푸시 알림을 허용해주세요.");
                    builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }else {
                    isPush = isChecked;
                    Toast.makeText(getContext(), "자동화 실행", Toast.LENGTH_LONG).show();
                }
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {       //환기 ON/OFF에 대해서 서버에게 전달하는 메소드
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Toast.makeText(getContext(), "창문 열림", Toast.LENGTH_LONG).show();
                    openState = 1;
                    open_state = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendToServer.sendToServer();
                        }
                    }).start();

                }else {
                    Toast.makeText(getContext(), "창문 닫음", Toast.LENGTH_LONG).show();
                    openState = 0;
                    open_state = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendToServer.sendToServer();
                        }
                    }).start();
                }

            }
        });


        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                textViews[1].setText(Estimated_Dust);
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch2.setChecked(open_state);
                    }
                }, 0);
            }
        };

        Timer timer = new Timer();
        timer.schedule(tt,0,3500);

        TimerTask tt1 = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                textViews[1].setText(Estimated_Dust);
                for(int i=0; i<1; i++){
                    for(int j = 0; j< AlarmFragment.reserving_data1.size(); j++){
                        if(WhatTimeIs.is_alarmTime() == AlarmFragment.reserving_data1.get(j)) {
                            if (b == 0) {
                                b = 1;
                                open_state = true;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sendToServer.sendToServer();
                                    }
                                }).start();
                            }else{
                                break;
                            }
                        }
                    }
                }
            }
        };
        Timer timer1 = new Timer();
        timer1.schedule(tt1,0,5000);

        selectImage(imageView);

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void selectImage(ImageView imageView){
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(imageView);
        if(Window.Devices_WheatherData[0]== null){
            Glide.with(getContext()).load(R.drawable.cloudy).into(gifImage);
        }else{
            String[] arr = Window.Devices_WheatherData[0].split(",");
            if(arr[0].equals("흐림") || arr[0].equals("구름많음")){
                Glide.with(getContext()).load(R.drawable.cloudy).into(gifImage);
            }else if(arr[0].equals("비")){
                Glide.with(getContext()).load(R.drawable.rainny).into(gifImage);
            }else if(arr[0].equals("맑음")){
                Glide.with(getContext()).load(R.drawable.sunny).into(gifImage);
            }else{
                Glide.with(getContext()).load(R.drawable.cloudy ).into(gifImage);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void tempGetApi(String address2){
        String[] arr = address2.split(" ");
        String path = "http://search.naver.com/search.naver?sm=top_hty&fbm=0&ie=utf8&query="+ arr[0] + "+" +arr[1] + "+" + arr[2] + "+" + arr[3] + "+날씨";
        try {
            Document document = Jsoup.connect(path).get();
            Elements elements = document.select("span.todaytemp");
            for(Element element: elements){
               Window.Devices_Temp[now_device] = element.text() + "℃";
               break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void weatherGetApi(String address2){
        String[] arr = address2.split(" ");
        String path = "http://search.naver.com/search.naver?sm=top_hty&fbm=0&ie=utf8&query="+ arr[0] + "+" +arr[1] + "+" + arr[2] + "+" + arr[3] + "+날씨";
        try {
            Document document = Jsoup.connect(path).get();
            Elements elements = document.select("p.cast_txt");
            for(Element element: elements){
                Window.Devices_WheatherData[now_device] = element.text() + "\n";
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getDust10Api(String address1){
        String[] arr = address1.split(" ");
        String path = "http://search.naver.com/search.naver?sm=top_hty&fbm=0&ie=utf8&query="+ arr[0] + "+" +arr[1] + "+" + arr[2] + "+" + arr[3] + "+날씨";

        try {
            Document document = Jsoup.connect(path).get();
            Elements elements = document.select("span.num");
            int cnt = 0;
            for(Element element: elements){
                if(cnt == 4){
                  Window.Devices_dust10[now_device] = "미세먼지: " + element.text() + "\n";
                    break;
                }
                cnt++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void getDust25Api(String address1){
        String[] arr = address1.split(" ");
        String path = "http://search.naver.com/search.naver?sm=top_hty&fbm=0&ie=utf8&query="+ arr[0] + "+" +arr[1] + "+" + arr[2] + "+" + arr[3] + "+날씨";

        try {
            Document document = Jsoup.connect(path).get();
            Elements elements = document.select("span.num");
            int cnt = 0;
            for(Element element: elements){
                if(cnt == 5){
                   Window.Devices_dust25[now_device] = "초미세먼지: " + element.text() + "\n";
                    break;
                }
                cnt++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
