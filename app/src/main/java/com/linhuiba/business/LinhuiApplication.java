package com.linhuiba.business;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.SDKInitializer;
import com.baselib.app.util.MessageUtils;
import com.growingio.android.sdk.collection.Configuration;
import com.growingio.android.sdk.collection.GrowingIO;
import com.linhuiba.business.activity.AboutUsActivity;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.activity.MyCouponsActivity;
import com.linhuiba.business.activity.MyWalletActivity;
import com.linhuiba.business.activity.PublishReviewActivity;
import com.linhuiba.business.activity.SplashScreenActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.fragment.HomeFragment;
import com.linhuiba.business.model.DistrictModel;
import com.linhuiba.linhuifield.fieldactivity.FieldMainTabActivity;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesInfoModel;
import com.linhuiba.linhuifield.sqlite.ConfigCitiesModel;
import com.linhuiba.linhuifield.sqlite.ConfigCityParameterModel;
import com.linhuiba.linhuifield.sqlite.ConfigurationsModel;
import com.linhuiba.linhuipublic.config.BaseApplication;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.meiqia.core.MQMessageManager;
import com.meiqia.core.bean.MQMessage;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by snowd on 15/3/28.
 */
public class LinhuiApplication extends BaseApplication {
    private static final String UMENG_MESSAGE_SECRET = "9623b0d05e587e1f9f6e0b0bb4b3a860";
    private PushAgent mPushAgent;
    @Override
    public void onCreate() {
        super.onCreate();
        //百度地图
        SDKInitializer.initialize(getApplicationContext());
        //初始配置
        LoginManager.init(getApplicationContext());
        //友盟禁止默认的页面统计方式
        MobclickAgent.openActivityDurationTrack(false);
        //growingio
        GrowingIO.startWithConfiguration(this, new Configuration()
                .useID()
                .trackAllFragments());
        new Thread(new Runnable() {
            @Override
            public void run() {
                UMConfigure.init(getApplicationContext(), com.linhuiba.linhuipublic.config.Config.UMENG_APPKEY,"Umeng", UMConfigure.DEVICE_TYPE_PHONE, com.linhuiba.linhuipublic.config.Config.UMENG_MESSAGE_SECRET);
                //友盟推送注册
                mPushAgent = PushAgent.getInstance(getApplicationContext());
                //注册推送服务，每次调用register方法都会回调该接口 识别设备id发送到后台
                mPushAgent.register(new IUmengRegisterCallback() {
                    @Override
                    public void onSuccess(String deviceToken) {
                        //注册成功会返回device token
                        LoginManager.getInstance().setDevice_token(deviceToken);
                        Constants constants = new Constants(getApplicationContext());
                        constants.binding_devices();
                    }

                    @Override
                    public void onFailure(String s, String s1) {

                    }
                });
                UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
                    @Override
                    public void launchApp(Context context, UMessage msg) {
                        String data = msg.url;
                        //2018/7/18 测试代码，友盟推送后台设置
//                        if (msg.extra != null &&
//                                msg.extra.get("url") != null) {
//                            data = msg.extra.get("url");
//                        }
                        if (isAppAlive()) {
                            //app已启动运行状态或在后台状态
                            //进入推送消息列表页面时要判断是否登录
                            //2018/11/23 推送跳转
                            Constants.pushUrlJumpActivity(data,getApplicationContext(),true);
                        } else {
                            //app没有启动状态
                            if (data != null && data.length() > 0) {
                                LoginManager.getInstance().setUMmsg_start_app(data);
                            } else {
                                LoginManager.getInstance().setUMmsg_start_app("null_url");
                            }
                            Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                };
                UmengMessageHandler umengMessageHandler = new UmengMessageHandler() {
                    @Override
                    public Notification getNotification(Context context, UMessage msg) {
                        switch (msg.builder_id) {
                            case 1:
                                Notification.Builder builder = new Notification.Builder(context);
                                RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
                                        R.layout.umeng_meaage_title_layout);
                                myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                                myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                                myNotificationView.setImageViewBitmap(R.id.notification_large_icon,
                                        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_new));
                                myNotificationView.setImageViewResource(R.id.notification_small_icon,
                                        getSmallIconId(context, msg));
                                builder.setContent(myNotificationView)
                                        .setSmallIcon(getSmallIconId(context, msg))
                                        .setTicker(msg.ticker)
                                        .setAutoCancel(true);
                                return builder.getNotification();
                            default:
                                //默认为0，若填写的builder_id并不存在，也使用默认。
                                return super.getNotification(context, msg);
                        }
                    }
                };
                //自定义通知栏样式
                mPushAgent.setMessageHandler(umengMessageHandler);
                //打开动作
                mPushAgent.setNotificationClickHandler(notificationClickHandler);
                mPushAgent.setDisplayNotificationNumber(0);
            }
        }).start();
        //保存配置文件
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (LoginManager.getcitylist() != null && LoginManager.getcitylist().length() > 0) {
                    if (ConfigurationsModel.getInstance().getCitylist() != null &&
                            ConfigurationsModel.getInstance().getCitylist().size() > 0) {

                    } else {
                        ArrayList<ConfigCitiesModel> ConfigCitiesModels = (ArrayList<ConfigCitiesModel>) JSONArray.parseArray(LoginManager.getcitylist(),ConfigCitiesModel.class);
                        if (ConfigCitiesModels != null && ConfigCitiesModels.size() > 0) {
                            ConfigurationsModel.getInstance().setCitylist(ConfigCitiesModels);
                        }
                    }
                }
            }
        }).start();
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }

    private boolean isAppAlive() {
        boolean isRun = false;
        ActivityManager mAm = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = mAm.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo runningTask : runningTasks) {
            android.util.Log.e("run", runningTask.baseActivity.getClassName());
            if ("com.linhuiba.business.activity.MainTabActivity".equals(runningTask.baseActivity.getClassName())) {
                isRun = true;
                break;
            }
        }
        return isRun;
    }

}
