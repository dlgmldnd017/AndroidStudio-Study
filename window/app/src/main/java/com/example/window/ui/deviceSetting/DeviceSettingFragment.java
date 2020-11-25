package com.example.window.ui.deviceSetting;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.window.Adapter.Window;
import com.example.window.Dust.DustParsing;
import com.example.window.Dust.WeatherApiParsing;
import com.example.window.GPS.GpsTracker;
import com.example.window.MainActivity;
import com.example.window.R;
import com.example.window.ui.home.HomeFragment;

public class DeviceSettingFragment extends Fragment{
    private GpsTracker gpsTracker = new GpsTracker(getContext());
    static int a = 1;       //case문을 받기 위한 조건 변수
    ProgressDialog progressDialog;
    Handler handler;
    public static String address = "현재 위치 확인 안됨";       //현재 위치 정보를 받아오는 변수
    public static boolean CheckLocation = false;               //현재 위치를 받아오는 것을 성공한다면 true
    public static int count= 0;   //현재 기기를 추가한 숫자.
    public static int i=0;    //현재 기기의 위치에 필요한 숫자.
    public ImageButton button1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DeviceSettingViewModel dashboardViewModel = ViewModelProviders.of(this).get(DeviceSettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_devicesetting, container, false);

        final TextView textView = root.findViewById(R.id.deviceSetting_textView1);
        textView.setText(address);

        button1 = root.findViewById(R.id.deviceSetting_button2);
        button1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View v) {
                gpsTracker = new GpsTracker(getContext());
                final double latitude = gpsTracker.getLatitude();
                final double longitude = gpsTracker.getLongitude();

                final MainActivity mainActivity = (MainActivity) getActivity();
                address = mainActivity.A(latitude, longitude);

                a = 2;

                progressDialog = new ProgressDialog(getContext());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("위치를 확인하고 있습니다.");
                progressDialog.show();

                handler = new Handler() {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
                        if (msg.arg1 == 100) {
                            dialog.setCancelable(false);
                            if (address.equals("주소를 찾을 수가 없습니다.")) {
                                dialog.setMessage("위치 확인 실패");
                                dialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog.dismiss();
                                        textView.setText(address);
                                    }
                                });
                                dialog.show();
                            }else if(!MainActivity.isNetwork){
                                dialog.setMessage("네트워크 확인 실패");
                                dialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog.dismiss();
                                        textView.setText("지오코더 서비스 사용불가");
                                    }
                                });
                                dialog.show();
                            }else {
                                final WeatherApiParsing weatherApiActivity = new WeatherApiParsing();
                                final DustParsing dustApiActivity = new DustParsing();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        address = mainActivity.A(latitude, longitude);
                                        textView.setText(address);
                                        weatherApiActivity.tempGetApi();
                                        weatherApiActivity.weatherGetApi();
                                        dustApiActivity.getDust10Api();
                                        dustApiActivity.getDust25Api();
                                    }
                                }).start();
                                dialog.setMessage("위치 확인 성공");
                                textView.setText(address);
                                dialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        CheckLocation = true;
                                        textView.setText(address);
                                        dialog.dismiss();
                                        progressDialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                        }
                    }
                };
                switch (a) {
                    case 2:
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i <= 140; i++) {
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

        Button button2 = root.findViewById(R.id.deviceSetting_button1);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                if (address.equals("주소를 찾을 수가 없습니다.") || address.equals("현재 위치 확인 안됨") || address.equals("지오코더 서비스 사용불가")) {
                    dialog.setMessage("현재 위치를 확인해주세요.");
                    dialog.setNegativeButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                } else if(DeviceSettingFragment.count == 3){
                    dialog.setMessage("현재 제어 가능한 기기를 초과하였습니다.");
                    dialog.setNegativeButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                }else{
                    Intent intent = new Intent(getActivity(), Window.class);
                    startActivity(intent);
                }
            }
        });

        return root;
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}