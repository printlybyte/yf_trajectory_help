package com.yinfeng.yf_trajectory_help;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.yinfeng.yf_trajectory_help.utils.NotificationManagerUtils;

import java.util.logging.Logger;

import static com.yinfeng.yf_trajectory_help.utils.PermissionUtilsx.getSystemVersion;


/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.wypzh
 * 类  名：BootBroadcastReceiver
 * 创建人：liuguodong
 * 创建时间：2019/8/25 16:51
 * ============================================
 **/
public class BootBroadcastReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            if (getSystemVersion() >= 1000) {
                NotificationManagerUtils.startBootNotificationManager("请点击此处打开银丰轨迹助手", R.mipmap.ic_app_start_icon);
            }else {
                startOneActivity(context);
            }
        }catch (Exception ignored){
            Toast.makeText(context, "助手开机广播错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void startOneActivity(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
