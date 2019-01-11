package com.linhuiba.business.connector;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootReceiver extends BroadcastReceiver {
    //2018/12/7 本地通知
    @Override
    public void onReceive(Context context, Intent intent1) {
        String action = intent1.getAction();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            resetPush(context);
        }
    }

    private void resetPush(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("funm_push", Context.MODE_PRIVATE);
        int count = sharedPreferences.getInt("noticeCount", 0);
        int noticeCount = 0;
        for (int i=0; i<count; i++) {
            long timestamp = sharedPreferences.getLong("tiemstamp_"+noticeCount, 0);
            String noticeStr = sharedPreferences.getString("noticeStr_"+noticeCount, "");
            if (timestamp !=0 && !noticeStr.equals("")) {
                Intent playerIntent = new Intent(context, PushReceiver.class);
                playerIntent.putExtra("noticeId", noticeCount);
                playerIntent.putExtra("noticeStr", noticeStr);
                PendingIntent pi = PendingIntent.getBroadcast(context, noticeCount, playerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, timestamp, pi);
            }
            noticeCount++;
        }
    }
}
