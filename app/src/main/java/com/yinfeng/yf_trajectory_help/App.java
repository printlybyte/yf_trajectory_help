package com.yinfeng.yf_trajectory_help;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.CrashUtils;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.DiskLogStrategy;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory_help.utils.Latte;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory_help
 * 类  名：App
 * 创建人：liuguodong
 * 创建时间：2019/8/16 12:43
 * ============================================
 **/
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Latte.init(this);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
        Hawk.init(this).build();
        initFileCrash();

        FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
                .tag("custom")
                .build();

        Logger.addLogAdapter(new DiskLogAdapter(formatStrategy));
    }


    /**
     * 本地crash 初始化
     */
    @SuppressLint("MissingPermission")
    private void initFileCrash() {
        CrashUtils.init(Environment.getExternalStorageDirectory().getAbsolutePath() + "/yfhelp_crash_file");
    }


}
