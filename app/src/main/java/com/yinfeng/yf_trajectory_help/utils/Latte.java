package com.yinfeng.yf_trajectory_help.utils;

import android.content.Context;

public class Latte {
    private static Context mcontext;


    public static void init(Context context) {
        mcontext = context;
    }

    public static Context getApplicationContext() {
        return mcontext;
    }


}
