package com.yinfeng.yf_trajectory_help;

import android.os.Environment;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yinfengtrajectory
 * 类  名：ConstantApi
 * 创建人：liuguodong
 * 创建时间：2019/8/1 16:55
 * ============================================
 **/
public interface ConstantApi {
    String LOG_ERR = "LOG_ERR";
    String LOG_I = "LOG_I";
    String LOG_I_ = "LOG_I_";
    String LOG_I_NET = "LOG_I_NET";

    String RECEVIER_DOWNLOAD_HELP_APK = "install_help_apk";
    String KEEPLIVE_TRACK_APK = "KEEPLIVE_TRACK_APK";
    String CHACK_HELP_APK = "CHACK_HELP_APK";

    int API_REQUEST_SUCCESS = 200;
    int API_REQUEST_ERR_901 = 901;


    // FINAL HK CONSTANS
    String HK_TOKEN = "HK_TOKEN";
    String HK_USER_BEAN = "HK_USER_BEAN";
    String INTENT_FLAG = "INTENT_FLAG";
    String INTENT_KEY = "INTENT_KEY";
    String INTENT_KEY_TWO = "INTENT_KEY_TWO";
    String INTENT_KEY_THREE = "INTENT_KEY_THREE";


    //搜索跳转
    String query_search = "query_search";
    //信息查看
    String query_info = "query_info";


    long time_dialog_min_time = Long.parseLong("1564647419000");
    long time_dialog_max_time = Long.parseLong("1880266619000");
    String RECEIVER_ACTION = "location_in_background";
    String RECEIVER_ACTION_CHECK = "RECEIVER_ACTION_CHECK";

   String CommonApkPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/yf_trajectory/";
   String CommonPath=Environment.getExternalStorageDirectory().getAbsolutePath();
    String CommonApkName="1.apk";
    String CommonHelpApkName = "2.apk";

    String isActivation = "isActivation";

}
