package com.example.window.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.window.Adapter.Window;
import com.example.window.Data.ReceiveFromServer;
import com.example.window.MainActivity;
import com.example.window.R;
import com.example.window.ui.home.HomeFragment;

import java.util.Timer;
import java.util.TimerTask;

public class PushService extends Service {
    ReceiveFromServer receiveFromServer = new ReceiveFromServer();
    public static int Api_dust25_plus_dust10;
    public static float Est_dust25_plus_dust10;
    String[] parse_dust25, parse_dust10, dust25, dust10;
    public static String notify_info;
    public static int index2=0,index3=0,index4 = 0,index5=0,index6=0, index0 =0, index1=0;
    public static int isRainny;
    public static int isGas;
    public static boolean isError = false;
    public static boolean isThread = false;
    public static int a=0;
    public static int isCheck0=0, isCheck1=0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(HomeFragment.isPush){
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            receiveFromServer.receiveToServer();
                            String[] arr = new String[5];
                            try {
                                arr = Window.Estimated_dust[0].split(", ");      //기계에서 측정된 값 파싱
                                MainActivity.now_state = "자동화를 하고 있어요.!!";
                                if(arr[3].equals("1")){
                                    if(isCheck1 == 30){
                                        HomeFragment.open_state = true;
                                        isCheck1 =0;
                                    }
                                    isCheck1++;
                                }else if(arr[3].equals("0")){
                                    if(isCheck0==30){
                                        HomeFragment.open_state = false;
                                        isCheck0 = 0;
                                    }
                                    isCheck0++;
                                }
                            } catch (NullPointerException e) {
                                HomeFragment.isPush = false;
                                createNotification5();
                                isError = true;
                                MainActivity.now_state = "서버 연결 실패로 자동화가 중지되었습니다.";
                            }
                            if(!isError){
                                Est_dust25_plus_dust10 = Float.parseFloat(arr[0]);
                                a++;

                                HomeFragment.Estimated_Dust = Est_dust25_plus_dust10 + "㎍/㎥";
                                parse_dust25 = Window.Devices_dust25[0].split("초미세먼지: ");    //외부 api 측정된 값(1)
                                parse_dust10 = Window.Devices_dust10[0].split("미세먼지: ");    //외부 api 측정된 값(2)

                                dust25 = parse_dust25[1].split("㎍/㎥");
                                dust10 = parse_dust10[1].split("㎍/㎥");

                                isRainny = Integer.parseInt(arr[1]);
                                isGas = Integer.parseInt(arr[2]);

                                Api_dust25_plus_dust10 = Integer.parseInt(dust25[0]) + Integer.parseInt(dust10[0]);     //외부 api 값(1) + (2)

                                if(HomeFragment.open_state){    //열린 상태
                                    if(Est_dust25_plus_dust10 < Api_dust25_plus_dust10) {
                                        if ((0.0 <= Est_dust25_plus_dust10 && Est_dust25_plus_dust10 >= 190.0) && (Api_dust25_plus_dust10 >= 116)) {     //좋음 또는 보통(내부) vs 나쁨(외부)
                                            notify_info = "외부 공기가 나쁨으로 창문을 닫는 것을 추천합니다.";
                                            if (index1 == 0) {
                                                createNotification1();
                                                index1 = 1;
                                            } else {
                                                a++;
                                                if(a<50){
                                                    index1 =0;
                                                }
                                            }
                                        }
                                    }
                                    if (isRainny == 1) {
                                        if (index4 == 0) {
                                            HomeFragment.open_state = false;
                                            createNotification2();
                                            index4 = 1;
                                        }
                                    } else if (isRainny == 0) {
                                        index4 = 0;
                                    }
                                }else if(!HomeFragment.open_state){ //닫힌 상태
                                    if (Est_dust25_plus_dust10 > Api_dust25_plus_dust10) {
                                        if ((Est_dust25_plus_dust10 >= 191.0) && (0 <= Api_dust25_plus_dust10 && Api_dust25_plus_dust10 <= 45)) {     //나쁨(내부) vs 좋음(외부)
                                            notify_info = "현재 내부 공기가 나쁨, 외부 공기가 좋음으로 환기를 추천합니다.";
                                            if (index2 == 0) {
                                                createNotification1();
                                                index2 = 1;
                                                index3 = 1;
                                            }else{
                                                a++;
                                            }
                                        } else if ((Est_dust25_plus_dust10 >= 191.0) && (46 <= Api_dust25_plus_dust10 && Api_dust25_plus_dust10 <= 115)) {   //나쁨(내부) vs 보통(외부)
                                            notify_info = "현재 내부 공기가 나쁨, 외부 공기가 보통으로 환기를 추천합니다.";
                                            if (index3 == 0) {
                                                createNotification1();
                                                index3 = 1;
                                                index2 = 1;
                                            }else{
                                                a++;
                                            }
                                        } else {
                                            if (a > 500) {
                                                index2 = 0;
                                                index3 = 0;
                                            }
                                        }
                                    }

                                    if (isGas == 1) {
                                        if (index5 == 0) {
                                            HomeFragment.open_state = true;
                                            createNotification3();
                                            index5 = 1;
                                        }
                                    } else if (isGas == 0) {
                                        index5 = 0;
                                    }
                                    if (!MainActivity.isNetwork) {
                                        if (index6 == 0) {
                                            createNotification4();
                                            index6 = 1;
                                        }
                                    }
                                }
                            }
                            isThread = true;
                        }
                    });
                    thread.start();

                    if(isThread){
                        thread.interrupt();
                    }
                    /*try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
            }, 0);
        }

        return START_STICKY;
    }

    private void createNotification1() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),1, intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.drawable.zz4);
        builder.setContentTitle("환기 권장");
        builder.setContentText(notify_info);

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);
        builder.setContentIntent(pi);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assert notificationManager != null;
            notificationManager.createNotificationChannel(new NotificationChannel("2","기본채널",NotificationManager.IMPORTANCE_HIGH));
        }
        notificationManager.notify(2, builder.build());
    } //알림(2)
    private void createNotification2() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),2, intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.drawable.zz4);
        builder.setContentTitle("비 감지");
        builder.setContentText("비가와서 자동으로 창문이 닫힙니다.");

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);
        builder.setContentIntent(pi);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assert notificationManager != null;
            notificationManager.createNotificationChannel(new NotificationChannel("3","기본채널",NotificationManager.IMPORTANCE_HIGH));
        }
        notificationManager.notify(3, builder.build());
    } //알림(2)
    private void createNotification3() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),3, intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.drawable.zz4);
        builder.setContentTitle("가스 감지");
        builder.setContentText("가스가 감지되어 자동으로 창문이 열립니다.");

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);
        builder.setContentIntent(pi);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assert notificationManager != null;
            notificationManager.createNotificationChannel(new NotificationChannel("4","기본채널",NotificationManager.IMPORTANCE_HIGH));
        }
        notificationManager.notify(4, builder.build());
    } //알림(2)

    private void createNotification4() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),4, intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.drawable.zz4);
        builder.setContentTitle("인터넷 연결 실패");
        builder.setContentText("네트워크를 확인해 주세요.");

        HomeFragment.open_state = false;

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);
        builder.setContentIntent(pi);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assert notificationManager != null;
            notificationManager.createNotificationChannel(new NotificationChannel("5","기본채널",NotificationManager.IMPORTANCE_HIGH));
        }
        notificationManager.notify(5, builder.build());
    }
    private void createNotification5() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),5, intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.drawable.zz4);
        builder.setContentTitle("서버 연결 실패");
        builder.setContentText("서버 상태를 확인해 주세요.");

        HomeFragment.open_state = false;

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);
        builder.setContentIntent(pi);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assert notificationManager != null;
            notificationManager.createNotificationChannel(new NotificationChannel("6","기본채널",NotificationManager.IMPORTANCE_HIGH));
        }
        notificationManager.notify(6, builder.build());
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
