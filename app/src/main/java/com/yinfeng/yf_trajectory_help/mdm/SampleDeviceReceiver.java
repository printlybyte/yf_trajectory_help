package com.yinfeng.yf_trajectory_help.mdm;//package com.yinfeng.yinfengtrajectory;


import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.yinfeng.yf_trajectory_help.R;
import com.yinfeng.yf_trajectory_help.eventbus.EventBusUtils;
import com.yinfeng.yf_trajectory_help.utils.PermissionUtilsx;


public class SampleDeviceReceiver extends DeviceAdminReceiver {


    @Override
    public void onEnabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        new EventBusUtils().send(1);
//        Toast.makeText(context, "激活ok====", Toast.LENGTH_SHORT).show();
//
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        new EventBusUtils().send(2);
        PermissionUtilsx.requestChanelActive();
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        // TODO Auto-generated method stub
//        new EventBusUtils().send(2);
        return context.getString(R.string.disable_warning);
    }

}