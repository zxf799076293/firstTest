package com.baselib.app.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.linhuiba.linhuipublic.config.BaseApplication;

public class NetworkUtils {
    public enum NetworkType{
        TYPE_WIFI,
        TYPE_2G,
        TYPE_3G,
        TYPE_4G,
        TYPE_OTHER
    }

    private static BroadcastReceiver mReceiver;
    private static NetworkType currentType;

    private static boolean firstRead = true;
    private static boolean lowNetworkMode = true;

    public static boolean isLowNetworkMode() {
        if (firstRead) {
            update(getConnectType(BaseApplication.getInstance()));
            firstRead = false;
        }
        return lowNetworkMode;
    }

    private static void update(NetworkType type) {
        currentType = type;
        if(type == NetworkType.TYPE_2G || type == NetworkType.TYPE_3G ){
            lowNetworkMode = true;
        }else{
            lowNetworkMode = false;
        }

    }

    public static void registerNetworkStateListener() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        if (mReceiver == null) {
            currentType = getConnectType(BaseApplication.getInstance());
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                        NetworkType newConnectType = getConnectType(context);
                        if (newConnectType != currentType) {
                            update(newConnectType);
                        } else {
                        }
                    }
                }
            };
        }
        BaseApplication.getInstance().registerReceiver(mReceiver, intentFilter);
    }


    public static NetworkType getConnectType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.isConnected()) {
            switch (activeNetInfo.getType()) {
                case ConnectivityManager.TYPE_MOBILE: {
                    switch(activeNetInfo.getSubtype()){
                            case TelephonyManager.NETWORK_TYPE_GPRS:
                            case TelephonyManager.NETWORK_TYPE_EDGE:
                            case TelephonyManager.NETWORK_TYPE_CDMA:
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                            case TelephonyManager.NETWORK_TYPE_IDEN:
                                return NetworkType.TYPE_2G;
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            case TelephonyManager.NETWORK_TYPE_EHRPD:
                            case TelephonyManager.NETWORK_TYPE_HSPAP:
                                return NetworkType.TYPE_3G;
                            case TelephonyManager.NETWORK_TYPE_LTE:
                                return NetworkType.TYPE_4G;
                            default:
                                return NetworkType.TYPE_OTHER;
                    }
                }
                case ConnectivityManager.TYPE_WIFI: {
                    return NetworkType.TYPE_WIFI;
                }
                default: {
                    return NetworkType.TYPE_OTHER;
                }
            }
        } else {
            return NetworkType.TYPE_OTHER;
        }
    }
}
