package com.linhuiba.business.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.SplashScreenViewPagerAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.linhuifield.fieldview.CircleTextProgressbar;
import com.linhuiba.linhuifield.sqlite.ConfigSqlManager;
import com.linhuiba.linhuipublic.config.Config;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.squareup.picasso.Picasso;
import com.tencent.stat.StatConfig;
import com.tencent.wxop.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.magicwindow.MLink;
import cn.magicwindow.MLinkAPIFactory;
import cn.magicwindow.MWConfiguration;
import cn.magicwindow.MagicWindowSDK;
import okhttp3.internal.http2.Header;

public class SplashScreenActivity extends BaseMvpActivity {
    @InjectView(R.id.splashscreenimageview)
    ImageView msplashscreenimageview;
    @InjectView(R.id.splash_screen_experience_text)
    TextView msplash_screen_experience_text;
    @InjectView(R.id.count_down_progressbar)
    CircleTextProgressbar mCircleTextProgressbar;
    @InjectView(R.id.startup_page_rl)
    RelativeLayout mStartupPageRL;
    @InjectView(R.id.splash_screen_viewpage)
    ViewPager mStartPageViewPage;
    @InjectView(R.id.start_page_point_ll)
    LinearLayout mStartPageLL;
    AsyncTask mEnterTask;
    private int newPosition;
    private int mImageList_size;
    private int previousPointEnale;
    private int bunneritem;
    //引导页三张图
//    private int[] imageView = { R.drawable.image_yindaoye1, R.drawable.image_yindaoye2,
//            R.drawable.image_yindaoye3};
    private int[] imageView = {};
    private List<View> mPictureList;
    private boolean isOnClickPB;
    private Constants constants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (com.linhuiba.linhuifield.connector.Constants.hasNavBar(this)) {
            hideBottomUIMenu();
        }
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.inject(this);
        //2017/12/13 魔窗
        //魔窗集成
        MWConfiguration config = new MWConfiguration(SplashScreenActivity.this);
        config.setLogEnable(true);//打开魔窗Log信息
        MagicWindowSDK.initSDK(config);
        MLink.getInstance(this);
        MLinkAPIFactory.createAPI(this).registerWithAnnotation(this);
        //腾讯移动分析接入
        StatConfig.setDebugEnable(true);
        StatService.startStatService(this, Config.TENGXUN_TA_APPKEY,
                com.tencent.stat.common.StatConstants.VERSION);
        new Thread(new Runnable() {
            public void run() {
                //友盟集成测试服务
                MobclickAgent.setDebugMode(true);
                FieldApi.getSplashScreen(MyAsyncHttpClient.MyAsyncHttpClient(), getSplashScreenHandler, LoginManager.getInstance().getCurrentCitycode());
                constants = new Constants(SplashScreenActivity.this.getContext(), SplashScreenActivity.this);
                FieldApi.getProvincesCityList(MyAsyncHttpClient.MyAsyncHttpClient(), cityListHandler,
                        LoginManager.getInstance().getProvinceListVersion(),1,1);
            }
        }).start();
       /* if (LoginManager.getInstance().isShowStartPages()) {*/
            // : 2017/8/18 获取后台配置url
            mStartupPageRL.setVisibility(View.VISIBLE);
            if (LoginManager.getInstance().getUMmsg_start_app() != null &&
                    LoginManager.getInstance().getUMmsg_start_app().length() > 0) {
                mCircleTextProgressbar.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        getConfigInfo();
                    }
                }, 1000);
            } else {
                if (LoginManager.getInstance().getSplashScreenPicUrl() != null &&
                        LoginManager.getInstance().getSplashScreenStart() != null &&
                        LoginManager.getInstance().getSplashScreenend() != null &&
                        LoginManager.getInstance().getSplashScreenPicUrl().length() > 0 &&
                        LoginManager.getInstance().getSplashScreenStart().length() > 0 &&
                        LoginManager.getInstance().getSplashScreenend().length() > 0) {
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        String curStr = formatter.format(curDate);
                        Date currentDate = formatter.parse(curStr);
                        if (currentDate.getTime() - formatter.parse(LoginManager.getInstance().getSplashScreenStart()).getTime() > 0 &&
                                formatter.parse(LoginManager.getInstance().getSplashScreenend()).getTime() - currentDate.getTime() > 0) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    Picasso.with(SplashScreenActivity.this).load(LoginManager.getInstance().getSplashScreenPicUrl()).into(msplashscreenimageview);
                                    mCircleTextProgressbar.setVisibility(View.VISIBLE);
                                }
                            }, 1000);
                            if (LoginManager.getInstance().getSplashScreenJumpUrl() != null &&
                                    LoginManager.getInstance().getSplashScreenJumpUrl().length() > 0) {
                                msplashscreenimageview.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        isOnClickPB = true;
                                        LoginManager.getInstance().setUMmsg_start_app(LoginManager.getInstance().getSplashScreenJumpUrl());
                                        getConfigInfo();
                                    }
                                });
                            }
                            // 和系统普通进度条一样，0-100。
                            mCircleTextProgressbar.setProgressType(CircleTextProgressbar.ProgressType.COUNT);
                            // 改变进度条。
                            mCircleTextProgressbar.setProgressLineWidth(4);// 进度条宽度。
                            // 设置倒计时时间毫秒，默认3000毫秒。
                            mCircleTextProgressbar.setTimeMillis(3000);
                            // 改变进度条颜色。
                            mCircleTextProgressbar.setProgressColor(getResources().getColor(R.color.checked_tv_color));
                            // 改变外部边框颜色。
                            mCircleTextProgressbar.setOutLineColor(getResources().getColor(R.color.white));
                            // 改变圆心颜色。
                            mCircleTextProgressbar.setInCircleColor(getResources().getColor(R.color.divider_list));
                            mCircleTextProgressbar.setText(getResources().getString(R.string.splash_screen_intent_progressbar_tv_str));
                            mCircleTextProgressbar.setTextColor(getResources().getColor(R.color.white));
                            // 如果需要自动倒计时，就会自动走进度。
                            mCircleTextProgressbar.start();
                            CircleTextProgressbar.OnCountdownProgressListener progressListener = new CircleTextProgressbar.OnCountdownProgressListener() {
                                @Override
                                public void onProgress(int what, int progress) {
                                    if (what == 1) {
                                        // 比如在首页，这里可以判断进度，进度到了100或者0的时候，你可以做跳过操作。
                                        if (progress == 100) {
                                            if (!isOnClickPB) {
                                                //2017/12/13 魔窗
                                                getConfigInfo();
                                            }
                                        }
                                    }
                                }
                            };
                            mCircleTextProgressbar.setCountdownProgressListener(1, progressListener);
                            mCircleTextProgressbar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    isOnClickPB = true;
                                    getConfigInfo();
                                }
                            });
                        } else {
                            mCircleTextProgressbar.setVisibility(View.GONE);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    getConfigInfo();
                                }
                            }, 1000);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    mCircleTextProgressbar.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            getConfigInfo();
                        }
                    }, 1000);
                }
            }
            /*
      } else {
            mStartupPageRL.setVisibility(View.GONE);
            mStartPageLL.setVisibility(View.VISIBLE);
            initoper();
            addView();
//            addPoint();
            LoginManager.getInstance().setShowStartPages(true);
        } */
    }
    private LinhuiAsyncHttpResponseHandler getSplashScreenHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
            if (data != null) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject.get("start") != null &&
                        jsonObject.get("end") != null &&
                        jsonObject.get("jump_url") != null &&
                        jsonObject.get("pic_url") != null) {
                    if (jsonObject.get("start").toString().length() > 0 &&
                            jsonObject.get("end").toString().length() > 0) {
                        if (jsonObject.get("pic_url").toString().length() > 0) {
                            LoginManager.getInstance().setSplashScreenStart(jsonObject.get("start").toString());
                            LoginManager.getInstance().setSplashScreenend(jsonObject.get("end").toString());
                            LoginManager.getInstance().setSplashScreenPicUrl(jsonObject.get("pic_url").toString());
                            if (jsonObject.get("jump_url").toString().length() > 0) {
                                LoginManager.getInstance().setSplashScreenJumpUrl(jsonObject.get("jump_url").toString());
                            } else {
                                LoginManager.getInstance().setSplashScreenJumpUrl("");
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };
    private LinhuiAsyncHttpResponseHandler cityListHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
            if (data != null && data.toString().length() > 0 && data instanceof JSONObject) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject.get("version") != null &&
                        jsonObject.get("province_list") != null &&
                        jsonObject.get("version").toString().length() > 0 &&
                        jsonObject.get("province_list").toString().length() > 0) {
                    if (Integer.parseInt(jsonObject.get("version").toString()) > LoginManager.getInstance().getProvinceListVersion()) {
                        JSONArray jsonArray = JSONArray.parseArray(jsonObject.get("province_list").toString());
                        if (jsonArray != null && jsonArray.size() > 0) {
                            LoginManager.getInstance().setProvinceListVersion(Integer.parseInt(jsonObject.get("version").toString()));
                            LoginManager.getInstance().setProvinceList(jsonObject.get("province_list").toString());
                        }
                    }
                }
            }
            constants.getdistrictconfig(LoginManager.getInstance().getProvinceList());
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            constants.getdistrictconfig(LoginManager.getInstance().getProvinceList());
        }
    };
    @Override
    protected void onDestroy() {
        if (mEnterTask != null) mEnterTask.cancel(true);
        super.onDestroy();
    }

    private void getConfigInfo() {
        Intent intent = null;
        if (getIntent().getData() != null) {
            intent = getIntent();
        } else {
            intent = new Intent();
        }
        intent.setClass(getContext(), MainTabActivity.class);
        startActivity(intent);
        SplashScreenActivity.this.finish();
    }
    private void initoper() {
        // 进入按钮
        msplash_screen_experience_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), MainTabActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 2.监听当前显示的页面，将对应的小圆点设置为选中状态，其它设置为未选中状态
        mStartPageViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                monitorPoint(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }
    /**
     * 添加图片到view
     */
    private void addView() {
        mPictureList = new ArrayList<View>();
        // 将imageview添加到view
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < imageView.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //小米审核没通过时的修改
//            iv.setImageBitmap(readBitMap(this,imageView[i]));
            iv.setImageResource(imageView[i]);
            mPictureList.add(iv);
        }
        // 加入适配器
        mStartPageViewPage.setAdapter(new SplashScreenViewPagerAdapter(mPictureList));

    }

    /**
     * 添加小圆点
     */
    private void addPoint() {
        // 1.根据图片多少，添加多少小圆点
        for (int i = 0; i < imageView.length; i++) {
            LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(
                    28, 28);
            if (i < 1) {
                pointParams.setMargins(0, 0, 0, 0);
            } else {
                pointParams.setMargins(40, 0, 0, 0);
            }
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(pointParams);
            iv.setBackgroundResource(R.drawable.activity_splash_screen_unselected_circle_bg);
            mStartPageLL.addView(iv);
        }
        mStartPageLL.getChildAt(0).setBackgroundResource(R.drawable.activity_splash_screen_selected_circle_bg);
    }

    /**
     * 判断小圆点
     *
     * @param position
     */
    private void monitorPoint(int position) {
//        for (int i = 0; i < imageView.length; i++) {
//            if (i == position) {
//                mStartPageLL.getChildAt(position).setBackgroundResource(
//                        R.drawable.activity_splash_screen_selected_circle_bg);
//            } else {
//                mStartPageLL.getChildAt(i).setBackgroundResource(
//                        R.drawable.activity_splash_screen_unselected_circle_bg);
//            }
//        }
        // 3.当滑动到最后一个添加按钮点击进入，
        if (position == imageView.length - 1) {
            msplash_screen_experience_text.setVisibility(View.VISIBLE);
            mStartPageLL.setVisibility(View.GONE);
        } else {
            msplash_screen_experience_text.setVisibility(View.GONE);
            mStartPageLL.setVisibility(View.VISIBLE);
        }
    }
    //小米审核没通过时的修改
    public static Bitmap readBitMap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }
}
