package com.yinfeng.yf_trajectory_help;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.yinfeng.yf_trajectory_help.mdm.MDMUtils;
import com.yinfeng.yf_trajectory_help.mdm.SampleDeviceReceiver;
import com.yinfeng.yf_trajectory_help.mdm.SampleEula;
import com.yinfeng.yf_trajectory_help.utils.LattePreference;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 关闭禁止卸载
     */
    private Button mActivityMainCloseUninstall;
    /**
     * 关闭系统更新
     */
    private Button mActivityMainCloseUpdateSystem;
    /**
     * 关闭禁止恢复出厂设置
     */
    private Button mActivityMainCloseFactorySettings;
    /**
     * 开启应用搜索
     */
    private Button mActivityMainStartAppSearch;
    /**
     * 移除激活
     */
    private Button mActivityMainRemovePositionApp;
    private DevicePolicyManager mDevicePolicyManager = null;
    private ComponentName mAdminName = null;
    private SampleEula sampleEula = null;
    private MDMUtils mdmUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {
        mActivityMainCloseUninstall = (Button) findViewById(R.id.activity_main_close_uninstall);
        mActivityMainCloseUninstall.setOnClickListener(this);
        mActivityMainCloseUpdateSystem = (Button) findViewById(R.id.activity_main_close_update_system);
        mActivityMainCloseUpdateSystem.setOnClickListener(this);
        mActivityMainCloseFactorySettings = (Button) findViewById(R.id.activity_main_close_factory_settings);
        mActivityMainCloseFactorySettings.setOnClickListener(this);
        mActivityMainStartAppSearch = (Button) findViewById(R.id.activity_main_start_app_search);
        mActivityMainStartAppSearch.setOnClickListener(this);
        mActivityMainRemovePositionApp = (Button) findViewById(R.id.activity_main_remove_position_app);
        mActivityMainRemovePositionApp.setOnClickListener(this);
        mdmUtils = new MDMUtils();



        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminName = new ComponentName(this, SampleDeviceReceiver.class);

        sampleEula = new SampleEula(this, mDevicePolicyManager, mAdminName);
        sampleEula.activeProcessApp();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.activity_main_close_uninstall:
                mdmUtils.setDisallowedUninstallPackages(false);
                break;
            case R.id.activity_main_close_update_system:
//                mdmUtils.setSystemUpdateDisabled(false);
                break;
            case R.id.activity_main_close_factory_settings:
                mdmUtils.setRestoreFactoryDisabled(false);
                break;
            case R.id.activity_main_start_app_search:
//                mdmUtils.setSearchIndexDisabled(false);
                break;
            case R.id.activity_main_remove_position_app:
                mdmUtils.removeDisabledDeactivateMdmPackages();
                break;
        }
    }
}
