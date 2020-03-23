package com.yinfeng.yf_trajectory_help;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.yinfeng.yf_trajectory_help.bean.ApkDownloadBean;
import com.yinfeng.yf_trajectory_help.bean.ConmonBean_string;
import com.yinfeng.yf_trajectory_help.mdm.MDMUtils;
import com.yinfeng.yf_trajectory_help.net.GenericsCallback;
import com.yinfeng.yf_trajectory_help.net.JsonGenericsSerializator;
import com.yinfeng.yf_trajectory_help.service.PlayerMusicService;
import com.yinfeng.yf_trajectory_help.utils.ConmonUtils;
import com.yinfeng.yf_trajectory_help.utils.GsonUtils;
import com.yinfeng.yf_trajectory_help.utils.InstallAppUtils;
import com.yinfeng.yf_trajectory_help.utils.NotificationManagerUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mActivityMainHintDouble;
    private MDMUtils mdmUtils = null;
    /**
     *
     */
    private TextView mActivityMainDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mdmUtils = new MDMUtils();
        initView();
        initBroadCastEvent();
//        Toast.makeText(this, "aaaaa", Toast.LENGTH_SHORT).show();
        startPlayMusicService();


    }


    private void initBroadCastEvent() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantApi.RECEIVER_ACTION);
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
            if (action.equals(ConstantApi.RECEIVER_ACTION)) {
                String locationResult = intent.getStringExtra("result");
                if (null != locationResult && !locationResult.trim().equals("")) {
                    if (locationResult.equals(ConstantApi.KEEPLIVE_TRACK_APK)) {
                        boolean isRuning = ConmonUtils.isAppRunning(MainActivity.this, "com.yinfeng.yf_trajectory.moudle.activity.MapActivity");
                        Log.i(ConstantApi.LOG_I, isRuning + " 000");
                        InstallAppUtils.openPackage(MainActivity.this, "com.yinfeng.yf_trajectory");
                    } else if (locationResult.equals(ConstantApi.RECEVIER_DOWNLOAD_HELP_APK)) {
                        NotificationManagerUtils.startNotificationManager("轨迹助手下载成功", R.mipmap.ic_app_start_icon);
                        mdmUtils.installApk(true, "help");
                    }
                }
            }
        }
    };

    private void showToastC(String msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    private void stopPlayMusicService() {
        Intent intent = new Intent(MainActivity.this, PlayerMusicService.class);
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBroadCastEventReceiver();
    }

    private void startPlayMusicService() {
        Intent intent = new Intent(MainActivity.this, PlayerMusicService.class);
        getApplicationContext().startService(intent);
    }


    private void initView() {
        mActivityMainHintDouble = (TextView) findViewById(R.id.activity_main_hint_double);
        mActivityMainHintDouble.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
                return false;
            }
        });

        mActivityMainDisplay = (TextView) findViewById(R.id.activity_main_display);
        mActivityMainDisplay.setText("银丰轨迹助手\nv: " + AppUtils.getAppVersionName());
    }

    private int COUNTS = 10;// 点击次数
    private long[] mHits = new long[COUNTS];//记录点击次数
    private long DURATION = 3000;//有效时间

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
//            case R.id.activity_main_hint_double:
//                //将mHints数组内的所有元素左移一个位置
//                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
//                //获得当前系统已经启动的时间
//                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
//                if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
//                    // 相关逻辑操作
////                    mdmUtils.removeDisabledDeactivateMdmPackages();
//
//                    //初始化点击次数
//                    mHits = new long[COUNTS];
//                }
//
//                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION_CHECK);
        mIntent.putExtra("result", ConstantApi.CHACK_HELP_APK);
        //发送广播
        sendBroadcast(mIntent);
        requestDateHelpVersion();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }


//    private void requestDateHelpVersion() {
//
//        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        String iccid = "";
//        try {
//            iccid = tm.getSimSerialNumber();
//        } catch (Exception e) {
//            showToastC(e.getMessage());
//            return;
//        }
//        if (TextUtils.isEmpty(iccid) || iccid == null) {
//            showToastC("iccid = null");
//            return;
//        }
//
//        Map<String, Object> map = new LinkedHashMap<>();
//        map.clear();
//        map.put("iccid", iccid);
//        map.put("helpVersion", AppUtils.getAppVersionCode() + "");
//        OkHttpUtils
//                .postString()
//                .content(new Gson().toJson(map))
//                .url(Api.helpVersion)
//                .mediaType(MediaType.parse("application/json; charset=utf-8"))
//                .build()
//                .execute(new GenericsCallback<ConmonBean_string>(new JsonGenericsSerializator()) {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        showToastC("网络异常，请稍后重试" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(ConmonBean_string response, int id) {
//                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS) {
//                            Log.i("testte", "请求结果：requestDateHelpVersion " + GsonUtils.getInstance().toJson(response));
//
//                        } else {
//                            showToastC(response.getMessage());
//                        }
//                    }
//                });
//    }


    /**
     * downloadType 1= 轨迹 track   2 助手 help
     */
    private void requestDateHelpVersion() {
        if (!NetworkUtils.isConnected()) {
            showToastC("网络无链接,请稍后在试");
            return;
        }
        OkHttpUtils
                .get()
                .url(Api.appVersionGetNewVersion)
                .addParams("type", "help")
                .build()
                .execute(new GenericsCallback<ApkDownloadBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试" + e.getMessage());
                    }

                    @Override
                    public void onResponse(ApkDownloadBean response, int id) {
                        if (response != null && response.isSuccess() && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            String DownLoadUrl = response.getData().getDownLoadUrl();
                            String VersionCode = response.getData().getVersionCode();
                            int onLineVersionCode = Integer.parseInt(VersionCode);
                            if (TextUtils.isEmpty(response.getData().getDownLoadUrl())) {
                                showToastC("下载地址为空，请联系管理员");
                                return;
                            }
                            int onLocalVersionCode = AppUtils.getAppVersionCode();
                            if (onLineVersionCode > onLocalVersionCode) {
                                NotificationManagerUtils.startNotificationManager("检测到银丰助手新版本，下载中...", R.mipmap.ic_app_start_icon);
                                downloadFile(DownLoadUrl);
                            } else {
                                Log.i("testte", "无新版本");
                            }
                        } else {
                            showToastC(response.getMessage());
                        }
                        Log.i("testte", "请求结果：检测新版本" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }


    private void downloadFile(String mUrl) {
        String mPath;
        if (FileUtils.createOrExistsDir(ConstantApi.CommonApkPath)) {
            mPath = ConstantApi.CommonApkPath;
        } else {
            mPath = ConstantApi.CommonPath;
        }
        String downLoadApkName = ConstantApi.CommonHelpApkName;
        OkGo.<File>get(mUrl)
                .retryCount(3)
                .execute(new FileCallback(mPath, downLoadApkName) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        Log.i("testte", "下载成功....");


                        NotificationManagerUtils.startNotificationManager("轨迹助手下载成功", R.mipmap.ic_app_start_icon);
                        if (mdmUtils != null) {
                            mdmUtils.installApk(true, "help");
                        }


//                        Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
//                        mIntent.putExtra("result", ConstantApi.RECEVIER_DOWNLOAD_HELP_APK);
//                        sendBroadcast(mIntent);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        Log.i("testte", "下载失败,重试中....");
                        showToastC("下载失败,重试中....");
                        NotificationManagerUtils.startNotificationManager("轨迹助手下载失败，重新下载中...", R.mipmap.ic_app_start_icon);
                        requestDateHelpVersion();
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        float prgressx = progress.fraction * 100;
                        Log.i("testte", "下载进度 ：" + prgressx);
                        if (prgressx == 100) {

                        }
                    }
                });
    }

//    public static void main(String[] args) {
//        int a = 10;
//        for (int i = 0; i < 60; i++) {
//            if (i % a == 0){
//                System.out.print(i+  "  ");
//            }
//        }
//    }
}
