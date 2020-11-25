package com.example.window.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.window.MainActivity;
import com.example.window.R;

public class UndeadService extends Service{

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundService();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startForegroundService(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setVibrate(new long[]{0});
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Window");
        builder.setContentText(MainActivity.now_state);


        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0, intent,PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pi);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(new NotificationChannel("default","기본채널",NotificationManager.IMPORTANCE_LOW));
        }
        startForeground(1, builder.build());
    }

}
