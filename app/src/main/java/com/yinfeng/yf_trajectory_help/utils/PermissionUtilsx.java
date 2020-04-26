package com.yinfeng.yf_trajectory_help.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.yinfeng.yf_trajectory_help.Api;
import com.yinfeng.yf_trajectory_help.ConstantApi;
import com.yinfeng.yf_trajectory_help.R;
import com.yinfeng.yf_trajectory_help.net.GenericsCallback;
import com.yinfeng.yf_trajectory_help.net.JsonGenericsSerializator;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.utils
 * 类  名：PermissionUtilsx
 * 创建人：liuguodong
 * 创建时间：2019/9/9 18:20
 * ============================================
 **/
public class PermissionUtilsx {


    /**
     * 获取android系统版本
     */
    public static int getSystemVersion() {
        Class<?> classType = null;
        String buildVersion = null;
        try {
            classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", new Class<?>[]{String.class});
            buildVersion = (String) getMethod.invoke(classType, new Object[]{"ro.build.version.emui"});

            buildVersion = buildVersion.toString().replace("EmotionUI_", "").toString().replace(".", "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt(buildVersion);
    }

    /**
     * 检测存储权限6.0之前
     */

    /**
     * 检测电话权限6.0之前
     */


    /**
     * 检测电话权限6.0之前
     */

    @SuppressLint("HardwareIds")
    public static void requestChanelActive() {
        String iccid2 = "";
        TelephonyManager tm = (TelephonyManager) Latte.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        try {
            iccid2 = tm.getSimSerialNumber();
        } catch (Exception e) {
            Toast.makeText(Latte.getApplicationContext(), "iccid_err" + e, Toast.LENGTH_SHORT).show();
            return;
        }
        String mUrl = Api.APP_DOMAIN + "/common/relieveActive?type=help&iccid=" + iccid2;
        OkHttpUtils
                .get()
                .url(mUrl)
                .build()
                .execute(new GenericsCallback<String>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("TESTRE", "解除激活" + e.toString());
                        Toast.makeText(Latte.getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(Latte.getApplicationContext(), "" + response, Toast.LENGTH_SHORT).show();
                        Log.i("TESTRE", "解除激活" + GsonUtils.getInstance().toJson(response));
                    }

                });
    }


}
