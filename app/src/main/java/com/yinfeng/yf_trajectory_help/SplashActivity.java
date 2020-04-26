package com.yinfeng.yf_trajectory_help;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.orhanobut.hawk.Hawk;
import com.yinfeng.yf_trajectory_help.eventbus.EventBusBean;
import com.yinfeng.yf_trajectory_help.eventbus.EventBusUtils;
import com.yinfeng.yf_trajectory_help.mdm.MDMUtils;
import com.yinfeng.yf_trajectory_help.mdm.SampleDeviceReceiver;
import com.yinfeng.yf_trajectory_help.mdm.SampleEula;

import org.greenrobot.eventbus.Subscribe;


public class SplashActivity extends AppCompatActivity {
    private MDMUtils mdmUtils = null;
    private DevicePolicyManager mDevicePolicyManager = null;
    private ComponentName mAdminName = null;
    private SampleEula sampleEula = null;

    /**
     * 初始化相关组件
     */
    private void initHuaWeiHDM() {
        try {
            mdmUtils = new MDMUtils();
            mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            mAdminName = new ComponentName(this, SampleDeviceReceiver.class);
            sampleEula = new SampleEula(this, mDevicePolicyManager, mAdminName);
            sampleEula.activeProcessApp();
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 订阅接受者
     */
    @Subscribe
    public void onEventMainThread(EventBusBean event) {
        Toast.makeText(this, "" + event.getType(), Toast.LENGTH_SHORT).show();
        try {
            if (event.getType() == 1) { //激活
                //禁止关闭GPS
                boolean isActive = mdmUtils.setLocationModeDisabled(true);
                if (!isActive) {
                    Hawk.put(ConstantApi.isActivation, "2");
                    Toast.makeText(this, "此手机无权限", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                //强制开启数据服务
//            mdmUtils.forceMobiledataOn();
                //保持某应用始终运行
                mdmUtils.addPersistentApp();
                //设置应用为信任应用
                mdmUtils.setSuperWhiteListForHwSystemManger();
                //开启禁止卸载
                mdmUtils.setDisallowedUninstallPackages(true);
                //允许/禁止定位服务设置（EMUI8.0）
//            mdmUtils.setLocationServiceDisabled(false);

                //禁止反激活
                mdmUtils.addDisabledDeactivateMdmPackages();
                mdmUtils.removeDisabledDeactivateMdmPackages();

                ActivityUtils.startActivity(MainActivity.class);
                Hawk.put(ConstantApi.isActivation, "1");
                finish();

            } else if (event.getType() == 2) {  //取消

                if (mAdminName != null && mAdminName != null) {
                    sampleEula.activeProcessApp();
                } else {
                    Toast.makeText(this, "mAdminName= null", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onDestroy() {
        new EventBusUtils().unregister(this);

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new EventBusUtils().register(this);

        String isLoginStatus = Hawk.get(ConstantApi.isActivation, "");
        if (TextUtils.isEmpty(isLoginStatus)) {
//            initHuaWeiHDM();
            ActivityUtils.startActivity(MainActivity.class);
            finish();
        } else {
            ActivityUtils.startActivity(MainActivity.class);
            finish();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            finish();
        }

//        Toast.makeText(this, resultCode + "", Toast.LENGTH_SHORT).show();

    }
}
