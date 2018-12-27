package com.linhuiba.business.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baselib.app.activity.BaseActivity;
import com.linhuiba.business.BuildConfig;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.UserApi;
import com.linhuiba.business.fragment.MyselfFragment;
import com.linhuiba.business.mvpmodel.LoginMvpModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.connector.Constants;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/15.
 */

public class SettingActivity extends BaseMvpActivity {
    @InjectView(R.id.about_version_textview)
    TextView mabout_version_textview;
    private Dialog mShareDialog;
    private Bitmap mShareBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.inject(this);
        TitleBarUtils.setTitleText(this,getResources().getString(R.string.myselfinfo_setting_text));
        TitleBarUtils.showBackImg(this,true);
        mabout_version_textview.setText("v"+ BuildConfig.VERSION_NAME);
        if (!LoginManager.isLogin()) {
            this.finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mShareBitmap != null) {
            mShareBitmap.recycle();
        }
    }

    @OnClick({
            R.id.exit_btn,
            R.id.setting_modify_pw_layout,
            R.id.setting_push_msg_layout,
            R.id.helpcenter_layout,
            R.id.aboutus_layout,
            R.id.setting_share_linhuiba_rl
    })
    public void setting_click(View view) {
        switch (view.getId()) {
            case R.id.exit_btn:
                //退出登录接口
                LoginMvpModel.loginOut();
                //退出登录时清空token
                LoginManager.getInstance().clearLoginInfo();
                this.finish();
                //退出登录时清除公告信息
                LoginManager.getInstance().setNoticesUrl("");
                LoginManager.getInstance().setNoticescount(0);
                LoginManager.getInstance().setNoticesid(0);
                LoginManager.getInstance().setNoticesTitle("");
                //退出登录时清空保存的 评价提醒时间
                LoginManager.getInstance().setHome_review_show_time(0);
                //友盟退出登录统计
                MobclickAgent.onProfileSignOff();

                break;
            case R.id.setting_modify_pw_layout:
                Intent modifypw = new Intent(this,ModifyPasswordActivity.class);
                startActivityForResult(modifypw, 1);
                break;
            case R.id.setting_push_msg_layout:
                //2018/11/13 通知权限跳转设置
                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 9) {
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", SettingActivity.this.getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    localIntent.setAction(Intent.ACTION_VIEW);
                    localIntent.setClassName("com.android.settings",
                            "com.android.settings.InstalledAppDetails");
                    localIntent.putExtra("com.android.settings.ApplicationPkgName",
                            SettingActivity.this.getPackageName());
                }
                startActivity(localIntent);
                break;
            case R.id.helpcenter_layout:
                Intent help = new Intent(this, AboutUsActivity.class);
                help.putExtra("type", Config.HELP_WEB_INT);
                startActivity(help);
                break;
            case R.id.aboutus_layout:
                Intent aboutusintent = new Intent(this, AboutUsActivity.class);
                aboutusintent.putExtra("type", Config.ABOUT_WEB_INT);
                startActivity(aboutusintent);
                break;
            case R.id.setting_share_linhuiba_rl:
                shareLinhuiba();
                break;
            default:
                break;
        }
    }
    private void shareLinhuiba() {
        if (mShareBitmap == null) {
            mShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sharelogo);
        }
        IWXAPI api = WXAPIFactory.createWXAPI(this, com.linhuiba.business.connector.Constants.APP_ID);
        api.registerApp(com.linhuiba.business.connector.Constants.APP_ID);
        final View myView = SettingActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
        mShareDialog = new AlertDialog.Builder(SettingActivity.this).create();
        if (mShareDialog!= null && mShareDialog.isShowing()) {
            mShareDialog.dismiss();
        }
        com.linhuiba.business.connector.Constants constants = new com.linhuiba.business.connector.Constants(SettingActivity.this,
                "");
        constants.share_showPopupWindow(SettingActivity.this,myView,mShareDialog,api,Config.DOWNLOAD_LINHUIBA_URL,
                getResources().getString(R.string.module_myselfinfo_share_linhuiba_title),
                getResources().getString(R.string.module_myselfinfo_share_linhuiba_description), mShareBitmap);
    }
}
