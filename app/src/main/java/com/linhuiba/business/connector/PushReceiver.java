package com.linhuiba.business.connector;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.linhuiba.business.R;
import com.linhuiba.business.activity.MainTabActivity;

public class PushReceiver extends BroadcastReceiver {
    // FIXME: 2018/12/7 本地通知
    private NotificationManager manager;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        manager = (NotificationManager)context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        int noticeId = intent.getIntExtra("noticeId", 0);
        String noticeStr = intent.getStringExtra("noticeStr");
        Intent playIntent = new Intent(context, MainTabActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("通知Title").setContentText(noticeStr).setSmallIcon(R.drawable.sharelogo).setDefaults(Notification.DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true);
        manager.notify(noticeId, builder.build());
        Log.v("and_log","收到推送：onReceive: "+ noticeStr);
    }
}
