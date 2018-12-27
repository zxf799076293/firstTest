package com.linhuiba.linhuipublic.config;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

/**
 * Created by Administrator on 2016/12/31.
 */

public class BaseApplication extends MultiDexApplication {
    private static final String TAG = "TgrassApplication";

    public static BaseApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public static BaseApplication getInstance() {
        return context;
    }

    public String getAppVersionName() {
        try {
            PackageManager pm = getPackageManager();
            if (pm != null) {
                PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
                String versionName = pi.versionName;
                if (versionName != null && versionName.length() > 0) {
                    return versionName;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return "";
    }


}
