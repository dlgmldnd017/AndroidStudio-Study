package com.example.window.ui.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.IntentCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.window.Adapter.Window;
import com.example.window.Data.ReceiveFromServer;
import com.example.window.FQA.FQA;
import com.example.window.MainActivity;
import com.example.window.R;

public class SettingFragment extends Fragment {
    private SettingViewModel notificationsViewModel;
    ProgressDialog progressDialog;
    AlertDialog dialog;
    Handler handler;



    static int a = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        notificationsViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_setting, container, false);

        Button button1 = root.findViewById(R.id.setting_button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("리셋 설정");
                builder.setMessage("주의! 저장된 데이터가 없어집니다.!!");


                builder.setPositiveButton("리셋", new DialogInterface.OnClickListener() {
                    @SuppressLint("HandlerLeak")
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        final MainActivity mainActivity  = (MainActivity) getActivity();
                        a = 2;

                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setProgress(0);
                        progressDialog.setMax(30);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("리셋 중");
                        progressDialog.show();

                        handler = new Handler(){
                            @SuppressLint("HandlerLeak")
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
                                if(msg.arg1 == 100){
                                    restart();
                                    dialog.setCancelable(false);
                                    dialog.setMessage("리셋 완료");
                                    dialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            progressDialog.dismiss();
                                        }
                                    });
                                }
                            }
                        };
                        switch (a){
                            case 2:
                                Thread t = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for(int i =0; i<=200; i++){
                                            progressDialog.setProgress(i);
                                            Message msg = handler.obtainMessage();
                                            msg.arg1 = i*4;
                                            handler.sendMessage(msg);
                                            try {
                                                Thread.sleep(50);
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
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });

        Button button2 = root.findViewById(R.id.setting_button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("푸시 알림 설정");

                builder.setPositiveButton("켜기", new DialogInterface.OnClickListener() {
                    @SuppressLint("HandlerLeak")
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        MainActivity.isPush = true;
                    }
                });
                builder.setNegativeButton("끄기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.isPush = false;
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });

        Button button3 = root.findViewById(R.id.setting_button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FQA.class);
                startActivity(intent);
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

    public void restart() {
        MainActivity mainActivity = (MainActivity) getActivity();
        PackageManager packageManager = mainActivity.getApplicationContext().getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(mainActivity.getApplicationContext().getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        mainActivity.getApplicationContext().startActivity(mainIntent);
        System.exit(0);

        mainActivity.finish();
        startActivity(new Intent(mainActivity.getApplicationContext(), MainActivity.class));
    }
}
