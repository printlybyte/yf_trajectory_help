package com.yinfeng.yf_trajectory_help.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.yinfeng.yf_trajectory_help.MainActivity;
import com.yinfeng.yf_trajectory_help.R;
import com.yinfeng.yf_trajectory_help.sensor.LightSensorUtils;
import com.yinfeng.yf_trajectory_help.utils.NotificationManagerUtils;
import com.zhy.http.okhttp.utils.L;

public class SensorService extends Service {
    public SensorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("");
        builder.setContentText("");
        builder.setWhen(System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pt = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pt);
        Notification notification = builder.build();
        startForeground(1001, notification);




    }

    private LightSensorUtils mLightSensorUtils;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mLightSensorUtils.unRegisterSensor();
        super.onDestroy();
    }
}
