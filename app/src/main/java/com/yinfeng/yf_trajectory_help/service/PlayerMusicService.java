package com.yinfeng.yf_trajectory_help.service;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle
 * 类  名：PlayerMusicService
 * 创建人：liuguodong
 * 创建时间：2019/8/2 21:01
 * ============================================
 **/

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.yinfeng.yf_trajectory_help.Api;
import com.yinfeng.yf_trajectory_help.ConstantApi;
import com.yinfeng.yf_trajectory_help.MainActivity;
import com.yinfeng.yf_trajectory_help.R;
import com.yinfeng.yf_trajectory_help.bean.UpdateAndAliveTimeBean;
import com.yinfeng.yf_trajectory_help.net.GenericsCallback;
import com.yinfeng.yf_trajectory_help.net.JsonGenericsSerializator;
import com.yinfeng.yf_trajectory_help.utils.GsonUtils;
import com.yinfeng.yf_trajectory_help.utils.InstallAppUtils;
import com.yinfeng.yf_trajectory_help.utils.PowerManagerUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Calendar;
import java.util.logging.Logger;

import okhttp3.Call;


/**
 * 循环播放一段无声音频，以提升进程优先级
 * <p>
 * Created by jianddongguo on 2017/7/11.
 * http://blog.csdn.net/andrexpert
 */

public class PlayerMusicService extends Service {
    private final static String TAG = "PlayerMusicService";
    //    private MediaPlayer mMediaPlayer;
    public static final boolean DEBUG = true;

    public static final String PACKAGE_NAME = "com.jiangdg.keepappalive";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
        startForeground(0, notification);

        if (DEBUG)
            Log.d(TAG, TAG + "---->onCreate,启动服务");
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.silent);
        mMediaPlayer.setLooping(true);

        Log.i("testte", "onCreate");
        initTimePrompt();
        initBroadCastEvent();



    }
    MediaPlayer mMediaPlayer;

    private Handler mHandler = new LoopHandler();

    private class LoopHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 101) {
                getUpdateAndAliveTime();
                mHandler.sendEmptyMessageDelayed(101, interval);
                Log.i("testte", "loop loop .... ");
            }
            if (msg.what == 102) {

            }

        }
    }

    /**
     * 整点报时
     */
    private void initTimePrompt() {
        IntentFilter timeFilter = new IntentFilter();
        timeFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mTimeReceiver, timeFilter);
    }


    public static void main(String[] args) {
        int a = 7;

        for (int i = 1; i < 24; i++) {
            if (i % a == 0) {
                System.out.print(i + "  ");
            }
        }

    }


    private int netHour = 6;
    /**
     * 分钟计时器
     */
    private BroadcastReceiver mTimeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);

            if (netHour > 10) {
                netHour = 6;
                Log.i("testte", "netHour==6");
            }
            if ((hour % netHour == 0) && (min == 10)) {
                InstallAppUtils.openPackage(getBaseContext(), "com.yinfeng.yf_trajectory");
                Log.i("testte", "start yf____");
            }

            Log.i("testte", "  min: " + min + "   hour: " + hour + "  netHour: " + netHour);

//            if (hour == PullUpApkHour && min == PullUpApkMinute) {
//                Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
//                mIntent.putExtra("result", ConstantApi.KEEPLIVE_TRACK_APK);
//                //发送广播
//                sendBroadcast(mIntent);
//
//
//            }

            //下载轨迹助手apk
//            if (hour == DownloadHelpApkHour && min == DownloadHelpApkMinute) {   //检测apk
//                wakeUpAndUnlock();
//                requestDate();
//            }
//            requestWakeLock();

//            getUpdateAndAliveTime();

            if (min % 10 == 0) {
                getUpdateAndAliveTime();
            }

//            requestWakeLock();
            Log.i("testte", "mTimeReceiver keepaline");
        }
    };


    private void getUpdateAndAliveTime() {
        OkHttpUtils
                .get()
                .url(Api.helpPullTrackTime)
                .build()
                .execute(new GenericsCallback<UpdateAndAliveTimeBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试");
                    }

                    @Override
                    public void onResponse(UpdateAndAliveTimeBean response, int id) {
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            if (TextUtils.isEmpty(response.getData())) {
                                netHour = Integer.parseInt(response.getData());
                            }
                        } else {
                            showToastC(response.getMessage());
                        }
                        Log.i("testte", "请求结果：Api.helpPullTrackTime" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }


    long interval = 1000 * 60 * 10;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("testte", "onStartCommand");
        new Thread(new Runnable() {
            @Override
            public void run() {
                startPlayMusic();
            }
        }).start();
        if (!mHandler.hasMessages(101)) {
            mHandler.sendEmptyMessage(101);
        }
        return START_STICKY;
    }

    private void startPlayMusic(){
        if(mMediaPlayer != null){

            mMediaPlayer.start();
        }
    }

    private void stopPlayMusic(){
        if(mMediaPlayer != null){

            mMediaPlayer.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayMusic();

        stopForeground(false);
        // 重启自己
        Intent intent = new Intent(getApplicationContext(), PlayerMusicService.class);
        startService(intent);

        if (mTimeReceiver != null)
            unregisterReceiver(mTimeReceiver);

        if (BroadCastEventReceiver != null) {
            unBroadCastEventReceiver();
        }

        Log.i("testte", "onStartCommand");


//        mHandler.removeMessages(101);
//        mHandler.removeMessages(102);

    }

    private void showToastC(String msg) {
        Toast.makeText(PlayerMusicService.this, "" + msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 激活系统cpu
     */
    PowerManager powerManager;

    public void requestWakeLock() {
        if (powerManager == null) {
            if (PowerManagerUtil.getInstance().isScreenOn(getApplicationContext())) {
                return;
            }
            //针对熄屏后cpu休眠导致的无法联网、定位失败问题,通过定期点亮屏幕实现联网,本操作会导致cpu无法休眠耗电量增加,谨慎使用
            powerManager = (PowerManager) getApplication().getSystemService(Context.POWER_SERVICE);
            @SuppressLint("InvalidWakeLockTag")
            PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
            wl.acquire();
            //点亮屏幕
            wl.release();
            //释放
        }
    }

    public void wakeUpAndUnlock() {
        //屏锁管理器
//        KeyguardManager km = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
//        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
//        //解锁
//        kl.disableKeyguard();
//        requestWakeLock();
    }


    private void initBroadCastEvent() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantApi.RECEIVER_ACTION_CHECK);
        registerReceiver(BroadCastEventReceiver, intentFilter);

    }

    private void unBroadCastEventReceiver() {
        if (BroadCastEventReceiver != null)
            unregisterReceiver(BroadCastEventReceiver);
    }

    private BroadcastReceiver BroadCastEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConstantApi.RECEIVER_ACTION_CHECK)) {
                String locationResult = intent.getStringExtra("result");
                if (null != locationResult && !locationResult.trim().equals("")) {
                    if (locationResult.equals(ConstantApi.CHACK_HELP_APK)) {
//                        wakeUpAndUnlock();
//                        requestDate();
                    }
                }
            }
        }
    };
}