package com.linhuiba.business.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.util.MessageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.ActivityCaseAdapterTmp;
import com.linhuiba.business.adapter.GlideImageLoader;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.model.CaseInfoModel;
import com.linhuiba.business.model.CaseListModel;
import com.linhuiba.business.model.CaseThemeModel;
import com.linhuiba.business.mvppresenter.CaseMvpPresenter;
import com.linhuiba.business.mvpview.CaseMvpView;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.fieldview.Field_MyScrollview;
import com.linhuiba.linhuifield.fieldview.ZoomImageView;
import com.linhuiba.linhuipublic.config.Config;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.squareup.picasso.Picasso;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/4.
 */

public class ActivityCaseInfoActivity extends BaseMvpActivity implements Field_MyScrollview.OnScrollListener,
        CaseMvpView {
    @InjectView(R.id.caseinfo_navbar_titile)
    LinearLayout mCaseInfoNavbarTtileLL;
    @InjectView(R.id.caseinfo_status_bar_ll)
    LinearLayout mCaseInfoStatusBarLL;
    @InjectView(R.id.caseinfo_scrollview)
    Field_MyScrollview mScrollview;
    @InjectView(R.id.caseinfo_recyclerview)
    RecyclerView mCaseInfoRV;
    @InjectView(R.id.caseInfo_loadmore_ll)
    LinearLayout mCaseInfoLoadMoreLL;
    @InjectView(R.id.caseinfo_loadmore_nulldata_tv)
    TextView mCaseInfoNullTV;
    @InjectView(R.id.caseinfo_banner)
    Banner mBanner;
    @InjectView(R.id.caseinfo_name_tv)
    TextView mCaseInfoName;
    @InjectView(R.id.caseinfo_description_tv)
    TextView mCaseInfoDescription;
    @InjectView(R.id.caseinfo_address_tv)
    TextView mCaseInfoAddress;
    @InjectView(R.id.caseinfo_resource_imgv)
    ImageView mCaseInfoResourceImgv;
    @InjectView(R.id.caseinfo_resource_name)
    TextView mCaseInfoResNameTV;
    @InjectView(R.id.caseinfo_resource_price)
    TextView mCaseInfoResPriceTV;
    @InjectView(R.id.caseinfo_resource_num_people)
    TextView mCaseInfoResPeopleTV;
    @InjectView(R.id.caseinfo_resource_num_order)
    TextView mCaseInfoResNumOrderTV;
    @InjectView(R.id.caseinfo_resource_average_score)
    TextView mCaseInfoResAverageScore;
    @InjectView(R.id.caseinfo_other_cases_ll)
    LinearLayout mOtherCasesLL;
    @InjectView(R.id.caseinfo_all_ll)
    LinearLayout mCaseInfoLL;
    @InjectView(R.id.caseinfo_horizontal_sv)
    HorizontalScrollView mHorizontalScrollView;
    @InjectView(R.id.caseinfo_res_touch_ll)
    LinearLayout mCaseInfoResTouchLL;
    @InjectView(R.id.caseinfo_resource_ll)
    LinearLayout mCaseInfoResLL;
    @InjectView(R.id.caseinfo_content_ll)
    LinearLayout mCaseInfoContentLL;
    @InjectView(R.id.caseinfo_banner_num_tv)
    TextView mCaseinfoBannerNumTV;
    @InjectView(R.id.caseinfo_banner_size_tv)
    TextView mCaseinfoBannerSizeTV;
    @InjectView(R.id.caseinfo_statement_tv)
    TextView mCaseinfoStatementTV;
    @InjectView(R.id.caseinfo_statement_remind_imgbtn)
    ImageButton mCaseinfoStatementRemindImgBtn;
    @InjectView(R.id.caseinfo_statement_close_imgbtn)
    ImageButton mCaseinfoStatementCloseImgBtn;
    @InjectView(R.id.caseinfo_statement_ll)
    LinearLayout mCaseinfoStatementLL;

    private RelativeLayout mTitleBarRL;//taitlebar的layout
    private ImageView mCaseInfoTitleBackImg;
    private ImageView mCaseInfoTitleShareImg;
    private ImageView mCaseInfoTitleCardImg;
    private ImageView mCaseInfoTitleDownloadImgv;
    private TextView mCaseInfoTitleTV;
    private RelativeLayout mCaseInfoTitleRL;
    private DisplayMetrics mDisplayMetrics;
    private int mTitleBarInt;
    private String ShareTitleStr = "";//分享的标题
    private String mSharePYQTitleStr;
    private String SharedescriptionStr = "";//分享的描述
    private String ShareIconStr = "";//分享的图片的url
    private Bitmap ShareBitmap = null;//分享需要的bitmap
    private Bitmap miniShareBitmap = null;//小程序分享需要的bitmap
    private String share_linkurl = "";//微信分享的url
    private String sharewxMiniShareLinkUrl = "";//小程序分享的url
    private Dialog shareDialog;
    private IWXAPI mIWXAPI;
    private ArrayList<CaseListModel> mDatas = new ArrayList<>();
    private ActivityCaseAdapterTmp mActivityCaseAdapterTmp;
    private int page = 1;
    private CaseMvpPresenter mCaseMvpPresenter;
    private int mCaseId;
    private CaseInfoModel mCaseInfoModel;
    private ArrayList<String> mPicList = new ArrayList<String>();
    private GlideImageLoader mFieldinfoImageLoader;
    private long mClickTime;
    private Dialog mZoomPictureDialog;//详情页预览大图dialog
    private boolean mIsRefreshZoomImageview = true;//详情页变了是否重新获取预览大图
    private List<ImageView> mImageViewList;
    public GestureDetector mGestureDetector;
    private ArrayList<Integer> mFieldTypeIds = new ArrayList<>();
    private ArrayList<Integer> mIndustriesIds = new ArrayList<>();//行业
    private ArrayList<Integer> mSpreadWaysIds = new ArrayList<>();//形式 推广方式
    private ArrayList<Integer> mPromotionPurposesIds = new ArrayList<>();//目的 推广目的
    private ArrayList<Integer> mCityIds = new ArrayList<>();//城市
    private ArrayList<Integer> mCaseLabelTypeIds = new ArrayList<>();
    private int mCityId;
    private boolean isShare;
    private int mStatusBarHeight = 70;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_activity_case_info);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCaseMvpPresenter != null) {
            mCaseMvpPresenter.detachView();
        }
    }

    private void initView() {
        setSteepStatusBar();
        int stausBarHeight = getSteepStatusBarHeight();
        if (stausBarHeight > 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    stausBarHeight);
            mCaseInfoStatusBarLL.setLayoutParams(layoutParams);
            mStatusBarHeight = 44 + com.linhuiba.linhuifield.connector.Constants.Px2Dp(ActivityCaseInfoActivity.this,stausBarHeight);
        }
        mCaseMvpPresenter = new CaseMvpPresenter();
        mCaseMvpPresenter.attachView(this);
        mTitleBarRL = (RelativeLayout) findViewById(R.id.common_title_bar).findViewById(R.id.action_layout_top);
        mCaseInfoTitleShareImg = (ImageView) findViewById(R.id.common_title_bar).findViewById(R.id.action_img_top);
        mCaseInfoTitleBackImg = (ImageView) findViewById(R.id.common_title_bar).findViewById(R.id.back_button_top);
        mCaseInfoTitleCardImg = (ImageView) findViewById(R.id.common_title_bar).findViewById(R.id.business_titlebar_right_card_img);
        mCaseInfoTitleDownloadImgv = (ImageView)findViewById(R.id.common_title_bar).findViewById(R.id.business_titlebar_right_three_img);
        mCaseInfoTitleTV = (TextView) findViewById(R.id.common_title_bar).findViewById(R.id.title);
        mCaseInfoTitleTV.setTextColor(getResources().getColor(R.color.white));
        mCaseInfoTitleRL = (RelativeLayout) findViewById(R.id.about_title);
        mCaseInfoTitleRL.setBackgroundColor(getResources().getColor(R.color.color_null));
        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDisplayMetrics.widthPixels,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mCaseInfoContentLL.setLayoutParams(params);
        TitleBarUtils.showBackImg(this, true);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.module_case_title));
        mScrollview.setOnScrollListener(this);
        findViewById(R.id.caseinfo_all_ll).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScroll(mScrollview.getScrollY());
                System.out.println(mScrollview.getScrollY());
            }
        });
        TitleBarUtils.showTitleBarRightCard(this, true, R.drawable.ic_service_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //客服功能
                AndPermission.with(ActivityCaseInfoActivity.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();
            }
        });
        TitleBarUtils.showActionImg(this, true, getResources().getDrawable(R.drawable.ic_share_white), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShare) {
                    isShare = true;
                    share();
                }
            }
        });
        mIWXAPI = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        mIWXAPI.registerApp(Constants.APP_ID);
        mCaseInfoRV.setNestedScrollingEnabled(false);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if (intent.getExtras() != null &&
                    intent.getExtras().get("id") != null) {
                mCaseId = intent.getExtras().getInt("id");
            }
            if (intent.getExtras().get("community_type_ids") != null) {
                mFieldTypeIds = (ArrayList<Integer>) intent.getExtras().get("community_type_ids");
            }
            if (intent.getExtras().get("industry_ids") != null) {
                mIndustriesIds = (ArrayList<Integer>) intent.getExtras().get("industry_ids");
            }
            if (intent.getExtras().get("spread_way_ids") != null) {
                mSpreadWaysIds = (ArrayList<Integer>) intent.getExtras().get("spread_way_ids");
            }
            if (intent.getExtras().get("promotion_purpose_ids") != null) {
                mPromotionPurposesIds = (ArrayList<Integer>) intent.getExtras().get("promotion_purpose_ids");
            }
            if (intent.getExtras().get("city_ids") != null) {
                mCityIds = (ArrayList<Integer>) intent.getExtras().get("city_ids");
            }

            if (intent.getExtras().get("label_ids") != null) {
                mCaseLabelTypeIds = (ArrayList<Integer>) intent.getExtras().get("label_ids");
            }
            if (intent.getExtras().get("city_id") != null) {
                mCityId =  intent.getExtras().getInt("city_id");
            }
            if (mCityId == 0) {
                if (LoginManager.getInstance().getTrackcityid() != null &&
                        LoginManager.getInstance().getTrackcityid().length() > 0) {
                    mCityId = Integer.parseInt(LoginManager.getInstance().getTrackcityid());
                }
            }
        }
        showDefaultBanner();
        setGestureListener();
    }

    private void initData() {
        mCaseMvpPresenter.getCaseInfo(mCaseId,mFieldTypeIds,mIndustriesIds,mSpreadWaysIds,mPromotionPurposesIds,mCityIds,mCaseLabelTypeIds,mCityId);
        mCaseMvpPresenter.getOterCaseList(mCaseId, page,mCityId);
    }

    @OnClick({
            R.id.caseinfo_navbar_titile,
            R.id.caseinfo_resource_reservation,
            R.id.caseinfo_resource_imgv,
            R.id.caseinfo_resource_name,
            R.id.caseinfo_statement_tv,
            R.id.caseinfo_statement_close_imgbtn,
            R.id.caseinfo_statement_remind_imgbtn
    })
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.caseinfo_navbar_titile:
                if (System.currentTimeMillis() - mClickTime < 800) {
                    //此处做双击具体业务逻辑
                    mScrollview.smoothScrollTo(0, 0);
                } else {
                    mClickTime = System.currentTimeMillis();
                    //表示单击，此处也可以做单击的操作
                }
                break;
            case R.id.caseinfo_resource_reservation:
                gotoResInfo();
                break;
            case R.id.caseinfo_resource_imgv:
                gotoResInfo();
                break;
            case R.id.caseinfo_resource_name:
                gotoResInfo();
                break;
            case R.id.caseinfo_statement_tv:
                mCaseinfoStatementCloseImgBtn.setVisibility(View.VISIBLE);
                mCaseinfoStatementRemindImgBtn.setVisibility(View.GONE);
                mCaseinfoStatementTV.setText(getResources().getString(R.string.module_case_info_statement));
                break;
            case R.id.caseinfo_statement_close_imgbtn:
                mCaseinfoStatementCloseImgBtn.setVisibility(View.GONE);
                mCaseinfoStatementRemindImgBtn.setVisibility(View.VISIBLE);
                mCaseinfoStatementTV.setText(getResources().getString(R.string.module_fieldinfo_show_pic_statement));
                break;
            case R.id.caseinfo_statement_remind_imgbtn:
                mCaseinfoStatementCloseImgBtn.setVisibility(View.VISIBLE);
                mCaseinfoStatementRemindImgBtn.setVisibility(View.GONE);
                mCaseinfoStatementTV.setText(getResources().getString(R.string.module_case_info_statement));
                break;
            default:
                break;
        }
    }

    @Override
    public void onScroll(int scrollY) {
        int width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        mTitleBarInt = width *
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT /
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH - com.linhuiba.linhuifield.connector.Constants.Dp2Px(this, mStatusBarHeight);
        if (scrollY < 0) {
            mCaseInfoNavbarTtileLL.getBackground().mutate().setAlpha(0);
            mCaseInfoStatusBarLL.setBackgroundColor(getResources().getColor(R.color.color_null));
            mCaseInfoTitleTV.setVisibility(View.GONE);
        } else {
            if (scrollY == 0) {
                mCaseInfoTitleTV.setVisibility(View.GONE);
            } else {
                mCaseInfoTitleTV.setVisibility(View.VISIBLE);
            }
            if (scrollY < mTitleBarInt) {
                int progress = (int) (new Float(scrollY) / new Float(mTitleBarInt) * 200);
                mCaseInfoNavbarTtileLL.getBackground().mutate().setAlpha(progress);
                mCaseInfoTitleBackImg.setImageResource(R.drawable.nav_ic_back_white);
                mCaseInfoTitleShareImg.setImageResource(R.drawable.ic_share_white);
                mCaseInfoTitleCardImg.setImageResource(R.drawable.ic_service_white);
                mCaseInfoTitleDownloadImgv.setImageResource(R.drawable.ic_download_thr_ten);
                mCaseInfoTitleTV.setTextColor(getResources().getColor(R.color.white));
                mCaseInfoStatusBarLL.setBackgroundColor(getResources().getColor(R.color.color_null));
            } else {
                mCaseInfoNavbarTtileLL.getBackground().mutate().setAlpha(255);
                mCaseInfoTitleBackImg.setImageResource(R.drawable.ic_back_black);
                mCaseInfoTitleShareImg.setImageResource(R.drawable.popup_ic_share);
                mCaseInfoTitleCardImg.setImageResource(R.drawable.popup_ic_service);
                mCaseInfoTitleDownloadImgv.setImageResource(R.drawable.ic_download_black_thr_ten);

                mCaseInfoTitleTV.setTextColor(getResources().getColor(R.color.title_bar_txtcolor));
                mCaseInfoStatusBarLL.setBackgroundColor(getResources().getColor(R.color.checked_tv_color));
            }
        }
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if (requestCode == Constants.PermissionRequestCode) {
                MQConfig.init(ActivityCaseInfoActivity.this, com.linhuiba.linhuipublic.config.Config.MQAppkey, new OnInitCallback() {
                    @Override
                    public void onSuccess(String clientId) {
                        if (LoginManager.isLogin()) {
                            HashMap<String, String> clientInfo = new HashMap<>();
                            clientInfo.put("name", LoginManager.getCompany_name());
                            clientInfo.put("email", LoginManager.geteEmail());
                            if (LoginManager.getRole_id().equals("2")) {
                                clientInfo.put("comment", getResources().getString(R.string.module_myself_role_property_str));
                            } else if (LoginManager.getRole_id().equals("3")) {
                                clientInfo.put("comment", getResources().getString(R.string.module_myself_role_business_str));
                            } else if (LoginManager.getRole_id().equals("1")) {
                                clientInfo.put("comment", getResources().getString(R.string.module_myself_role_admin_str));
                            }
                            clientInfo.put("avatar", "https://banner.linhuiba.com/o_1b555h2jjoj6u1716tr12dl2rg7.jpg");
                            clientInfo.put("tel", LoginManager.getMobile());
                            // 相同的 id 会被识别为同一个顾客
                            Intent intent = new MQIntentBuilder(ActivityCaseInfoActivity.this)
                                    .setClientInfo(clientInfo)
                                    .setCustomizedId(LoginManager.getUid())
                                    .build();
                            startActivityForResult(intent, 10);
                        } else {
                            Intent intent = new MQIntentBuilder(ActivityCaseInfoActivity.this)
                                    .build();
                            startActivityForResult(intent, 10);
                        }

                    }

                    @Override
                    public void onFailure(int code, String message) {
                        MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                    }
                });

            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if (requestCode == Constants.PermissionRequestCode) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            }
        }
    };

    private void share() {
        //2017/9/23 分享功能
        //2018/7/4 title设置 朋友圈 和小程序
        if (ShareBitmap == null || miniShareBitmap == null) {
            showProgressDialog();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //2018/7/4 判断bitmap
                    if (mPicList != null && mPicList.size() > 0) {
                        ShareIconStr = mPicList.get(0)+ "?imageView2/0/w/300/h/240";
                        ShareBitmap = com.linhuiba.linhuifield.connector.Constants.GetLocalOrNetBitmap(ShareIconStr);
                        ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                        miniShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 120, 120, true);//压缩Bitmap
                        ShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 360, 288, true);//压缩Bitmap
                    }
                    if (ShareBitmap == null) {
                        ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                        ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                    }
                    if (miniShareBitmap == null) {
                        miniShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                    }
                    mHandler.sendEmptyMessage(0);
                }
            }).start();
        } else {
            final View myView = ActivityCaseInfoActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
            shareDialog = new AlertDialog.Builder(ActivityCaseInfoActivity.this).create();
            if (shareDialog != null && shareDialog.isShowing()) {
                shareDialog.dismiss();
            }
            isShare = false;
            Constants constants = new Constants(ActivityCaseInfoActivity.this,
                    ShareIconStr);
            constants.shareWXMiniPopupWindow(ActivityCaseInfoActivity.this, myView, shareDialog, mIWXAPI, share_linkurl,
                    ShareTitleStr,
                    SharedescriptionStr, ShareBitmap, sharewxMiniShareLinkUrl, miniShareBitmap, mSharePYQTitleStr);
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    hideProgressDialog();
                    final View myView = ActivityCaseInfoActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                    shareDialog = new AlertDialog.Builder(ActivityCaseInfoActivity.this).create();
                    //2018/7/4 设置url
                    if (shareDialog != null && shareDialog.isShowing()) {
                        shareDialog.dismiss();
                    }
                    isShare = false;
                    Constants constants = new Constants(ActivityCaseInfoActivity.this,
                            ShareIconStr);
                    constants.shareWXMiniPopupWindow(ActivityCaseInfoActivity.this, myView, shareDialog, mIWXAPI, share_linkurl,
                            ShareTitleStr,
                            SharedescriptionStr, ShareBitmap, sharewxMiniShareLinkUrl, miniShareBitmap, mSharePYQTitleStr);
                    break;
                default:
                    break;
            }
        }

    };

    private void showDefaultBanner() {
        mFieldinfoImageLoader = new GlideImageLoader(ActivityCaseInfoActivity.this,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT);

        //设置预览图片控件的大小
        int width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        int height = width * com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT /
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH;
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mBanner.getLayoutParams();
        //设置图片显示高度
        linearParams.height = height;
        linearParams.width = width;
        mBanner.setLayoutParams(linearParams);
    }

    private void setGestureListener() {
        mGestureDetector = new GestureDetector(this, new MyGestureListener());
        mHorizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            // 将gridview中的触摸事件回传给gestureDetector
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
//        mCaseInfoResTouchLL.setOnTouchListener(new View.OnTouchListener() {
//            // 将gridview中的触摸事件回传给gestureDetector
//            public boolean onTouch(View v, MotionEvent event) {
//                return mGestureDetector.onTouchEvent(event);
//            }
//        });
//        mCaseInfoResLL.setOnTouchListener(new View.OnTouchListener() {
//            // 将gridview中的触摸事件回传给gestureDetector
//            public boolean onTouch(View v, MotionEvent event) {
//                return mGestureDetector.onTouchEvent(event);
//            }
//        });
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        //onFling方法的第一个参数是 手指按下的位置， 第二个参数是 手指松开的位置，第三个参数是手指的速度
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1 != null && e2 != null) {
                float startX = e1.getX();//通过e1.getX（）获得手指按下位置的横坐标
                float endX = e2.getX();//通过e2.getX（）获得手指松开位置的横坐标
                float startY = e1.getY();//通过e1.getY（）获得手指按下位置的纵坐标
                float endY = e2.getY();//通过e2.getY（）获得手指松开的纵坐标
                if ((startX - endX) > 50 && Math.abs(startY - endY) < 200) {
                    //(startX - endX) > 50 是手指从按下到松开的横坐标距离大于50
                    // Math.abs(startY - endY) < 200 是手指从按下到松开的纵坐标的差的绝对值
                    if (mCaseInfoModel != null &&
                            mCaseInfoModel.getNext() != null && mCaseInfoModel.getNext() > 0) {
                        Intent caseInfoIntent = new Intent(ActivityCaseInfoActivity.this,ActivityCaseInfoActivity.class);
                        caseInfoIntent.putExtra("id",mCaseInfoModel.getNext());
                        startActivity(caseInfoIntent);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    } else {
                        MessageUtils.showToast(getResources().getString(R.string.module_caseinfo_last));
                    }
                    return true;
                }

                if ((endX - startX) > 50 && Math.abs(startY - endY) < 200) {
                    //在这里通过Intent实现界面转跳
                    if (mCaseInfoModel != null &&
                            mCaseInfoModel.getLast() != null && mCaseInfoModel.getLast() > 0) {
                        Intent caseInfoIntent = new Intent(ActivityCaseInfoActivity.this,ActivityCaseInfoActivity.class);
                        caseInfoIntent.putExtra("id",mCaseInfoModel.getLast());
                        startActivity(caseInfoIntent);
                        finish();
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    } else {
                        MessageUtils.showToast(getResources().getString(R.string.module_caseinfo_first));
                    }
                    return true;
                }
            }
            //返回值是重点：如果返回值是true则动作可以执行，如果是flase动作将无法执行
            return false;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
//        mBanner.onTouchEvent(event);
//        return false;
    }
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
////        gue.onTouchEvent(ev);
////        return super.dispatchTouchEvent(ev);
//        gue.onTouchEvent(ev); //让GestureDetector响应触碰事件
//        return super.dispatchTouchEvent(ev); //让Activity响应触碰事件
//    }
    private void show_zoom_picture_dialog(int position) {
        View myView = ActivityCaseInfoActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_preview_zoom_picture_dialog, null);
        mZoomPictureDialog = new AlertDialog.Builder(ActivityCaseInfoActivity.this).create();
        Constants.show_dialog(myView,mZoomPictureDialog);
        TextView mshowpicture_sizetxt = (TextView)myView.findViewById(R.id.showpicture_dialog_sizetxt);
        TextView mshowpicture_back = (TextView)myView.findViewById(R.id.showpicture_dialog_back);
        ViewPager mzoom_viewpage = (ViewPager)myView.findViewById(R.id.zoom_dialog_viewpage);
        mshowpicture_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mZoomPictureDialog.dismiss();
            }
        });
        if (mIsRefreshZoomImageview) {
            mImageViewList = new ArrayList<>();
            int width_new = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
            int height_new = width_new * com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT /
                    com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH;
            for (int i = 0; i < mPicList.size(); i++) {
                ZoomImageView imageView = new ZoomImageView(
                        getApplicationContext());
                Picasso.with(this).load(mPicList.get(i).toString()).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(width_new, height_new).into(imageView);
                mImageViewList.add(imageView);
            }
            mIsRefreshZoomImageview = false;
        }
        if (mImageViewList != null && mImageViewList.size() > 0) {
            com.linhuiba.linhuifield.connector.Constants.show_preview_zoom(mImageViewList,mzoom_viewpage,mshowpicture_sizetxt,position);
        }
    }
    private void gotoResInfo() {
        if (mCaseInfoModel != null &&
                mCaseInfoModel.getPhysical_resources() != null) {
            Intent resourceIntent = null;
            resourceIntent = new Intent(ActivityCaseInfoActivity.this, FieldInfoActivity.class);
            resourceIntent.putExtra("fieldId", String.valueOf(mCaseInfoModel.getPhysical_resources().getSelling_resource_id()));
            resourceIntent.putExtra("good_type", mCaseInfoModel.getPhysical_resources().getSelling_resource_id());
            startActivity(resourceIntent);
        } else {
            MessageUtils.showToast(getResources().getString(R.string.field_tupesize_errortoast));
        }

    }
    @Override
    public void onCaseListSuccess(ArrayList<CaseListModel> caseListModels) {
        mDatas = caseListModels;
        if (mDatas != null && mDatas.size() > 0) {
            ActivityCaseAdapterTmp.clearCaseInfoHeights();
            for (int i = 0; i < mDatas.size(); i++) {
                Glide.with(getApplicationContext())
                        .load(mDatas.get(i).getCover_pic_url())
                        .asBitmap()//强制Glide返回一个Bitmap对象
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                int width = bitmap.getWidth();
                                int height = mDisplayMetrics.widthPixels / 2 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(ActivityCaseInfoActivity.this,20) * bitmap.getHeight() / width;
                                ActivityCaseAdapterTmp.getCaseInfoHeights().add(height);
                            }
                        });
            }
            mOtherCasesLL.setVisibility(View.VISIBLE);
            mActivityCaseAdapterTmp = new ActivityCaseAdapterTmp(R.layout.module_recycle_item_activity_case,mDatas,ActivityCaseInfoActivity.this, ActivityCaseInfoActivity.this, 1);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            //设置布局管理器
            mCaseInfoRV.setLayoutManager(staggeredGridLayoutManager);
            //设置为垂直布局，这也是默认的
            //设置Adapter
            mCaseInfoRV.setAdapter(mActivityCaseAdapterTmp);
            mCaseInfoLoadMoreLL.setVisibility(View.GONE);
            if (mDatas.size() < 10) {
                mCaseInfoNullTV.setVisibility(View.VISIBLE);
            }
            mActivityCaseAdapterTmp.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (view.getId() == R.id.iv_img) {
//
//                } else if (view.getId() == R.id.tv_title) {
//
//                }
                    //滚动到第几个
                    Intent caseInfoIntent = new Intent(ActivityCaseInfoActivity.this,ActivityCaseInfoActivity.class);
                    caseInfoIntent.putExtra("id",mDatas.get(position).getId());
                    startActivity(caseInfoIntent);
                }
            });
            mScrollview.setOnScrollToBottomLintener(new Field_MyScrollview.OnScrollToBottomListener() {
                @Override
                public void onScrollBottomListener(boolean isBottom) {
                    if (isBottom && mCaseInfoNullTV.getVisibility() == View.GONE &&
                            mCaseInfoLoadMoreLL.getVisibility() == View.GONE) {
                        mCaseInfoLoadMoreLL.setVisibility(View.VISIBLE);
                        page ++;
                        mCaseMvpPresenter.getOterCaseList(mCaseId,page,mCityId);
                    }
                }
            });
        }
    }

    @Override
    public void onCaseListFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        mCaseInfoLoadMoreLL.setVisibility(View.GONE);
    }

    @Override
    public void onCaseListMoreSuccess(ArrayList<CaseListModel> caseListModels) {
        ArrayList<CaseListModel> tmp = caseListModels;
        if (tmp != null && tmp.size() > 0) {
            for( CaseListModel fieldDetail: tmp ){
                mDatas.add(fieldDetail);
                Glide.with(getApplicationContext())
                        .load(fieldDetail.getCover_pic_url())
                        .asBitmap()//强制Glide返回一个Bitmap对象
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                int width = bitmap.getWidth();
                                int height = mDisplayMetrics.widthPixels / 2 - com.linhuiba.linhuifield.connector.Constants.Dp2Px(ActivityCaseInfoActivity.this,20) * bitmap.getHeight() / width;
                                ActivityCaseAdapterTmp.getCaseInfoHeights().add(height);
                            }
                        });
            }
            mActivityCaseAdapterTmp.notifyDataSetChanged();
            mCaseInfoLoadMoreLL.setVisibility(View.GONE);
            if (tmp.size() < 10) {
                mCaseInfoNullTV.setVisibility(View.VISIBLE);
            }
        } else {
            mCaseInfoLoadMoreLL.setVisibility(View.GONE);
            mCaseInfoNullTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCaseListMoreFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        mCaseInfoLoadMoreLL.setVisibility(View.GONE);
        page --;
    }

    @Override
    public void onCaseInfoSuccess(CaseInfoModel caseInfoModel) {
        mCaseInfoModel = caseInfoModel;
        if (mCaseInfoModel != null) {
            if (mCaseInfoModel.getSource() == 1) {
                mCaseinfoStatementLL.setVisibility(View.VISIBLE);
            } else {
                mCaseinfoStatementLL.setVisibility(View.GONE);
            }
            if (mCaseInfoModel.getActivity_case_url().size() > 0) {
                for (int i = 0; i < mCaseInfoModel.getActivity_case_url().size(); i++) {
                    if  (mCaseInfoModel.getActivity_case_url().get(i).getActivity_case_url().length() > 0) {
                        mPicList.add(mCaseInfoModel.getActivity_case_url().get(i).getActivity_case_url().toString());
                    }
                }
                if (mPicList.size() > 0) {
                    //设置图片加载器
                    mBanner.setImageLoader(mFieldinfoImageLoader);
                    //设置图片集合
                    mBanner.setImages(mPicList);
                    //设置banner动画效果
                    mBanner.setBannerAnimation(Transformer.Default);
                    //设置自动轮播，默认为true
                    mBanner.isAutoPlay(false);
                    //设置指示器位置（当banner模式中有指示器时）
                    mBanner.setIndicatorGravity(BannerConfig.CENTER);
                    mBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
                    //banner设置方法全部调用完毕时最后调用
                    mBanner.setOnBannerClickListener(new OnBannerClickListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            if (mZoomPictureDialog != null && mZoomPictureDialog.isShowing()) {
                                mZoomPictureDialog.dismiss();
                            }
                            show_zoom_picture_dialog((position - 1) % mPicList.size());
                        }
                    });
                    mBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            if (position % mPicList.size() == 0) {
                                mCaseinfoBannerNumTV.setText(String.valueOf(mPicList.size()));
                            } else {
                                mCaseinfoBannerNumTV.setText(String.valueOf(position % mPicList.size()));
                            }
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    mCaseinfoBannerSizeTV.setText("/" + String.valueOf(mPicList.size()));
                    mBanner.start();
                    TitleBarUtils.showTitleBarRightThreeImg(this, true, R.drawable.ic_download_thr_ten, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //2019/1/17 下载案例图
                            Intent saveIntent = new Intent(ActivityCaseInfoActivity.this,ActivityCasePicSaveActivity.class);
                            saveIntent.putExtra("pic_list", (Serializable) mPicList);
                            startActivity(saveIntent);
                        }
                    });

                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (mPicList != null && mPicList.size() > 0) {
                            ShareIconStr = mPicList.get(0) + "?imageView2/0/w/300/h/240";
                            ShareBitmap = com.linhuiba.linhuifield.connector.Constants.GetLocalOrNetBitmap(ShareIconStr);
                            ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                            miniShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 120, 120, true);//压缩Bitmap
                            ShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 360, 288, true);//压缩Bitmap
                        }
                    }
                }).start();

            }
            mCaseInfoName.setText(mCaseInfoModel.getTitle());
            ShareTitleStr = mCaseInfoModel.getTitle();
            mSharePYQTitleStr = mCaseInfoModel.getTitle();
            //2018/7/4 设置url
            sharewxMiniShareLinkUrl = com.linhuiba.business.config.Config.SHARE_CASE_INFO_WXMINI_URL + mCaseId +
                    "&community_type_ids="+ JSON.toJSONString(mFieldTypeIds,true).trim() +
                    "&industry_ids="+JSON.toJSONString(mIndustriesIds,true).trim() +
                    "&spread_way_ids="+JSON.toJSONString(mSpreadWaysIds,true).trim() +
                    "&promotion_purpose_ids="+JSON.toJSONString(mPromotionPurposesIds,true).trim() +
                    "&city_ids="+JSON.toJSONString(mCityIds,true).trim() +
                    "&label_ids="+JSON.toJSONString(mCaseLabelTypeIds,true).trim() +
                    "&city_id="+String.valueOf(mCityId);
            share_linkurl = com.linhuiba.business.config.Config.Domain_Name + com.linhuiba.business.config.Config.SHARE_CASE_INFO_PYQ_URL + mCaseId +
                    "&community_type_ids="+ JSON.toJSONString(mFieldTypeIds,true).trim() +
                    "&industry_ids="+JSON.toJSONString(mIndustriesIds,true).trim() +
                    "&spread_way_ids="+JSON.toJSONString(mSpreadWaysIds,true).trim() +
                    "&promotion_purpose_ids="+JSON.toJSONString(mPromotionPurposesIds,true).trim() +
                    "&city_ids="+JSON.toJSONString(mCityIds,true).trim() +
                    "&label_ids="+JSON.toJSONString(mCaseLabelTypeIds,true).trim() +
                    "&city_id="+String.valueOf(mCityId);
            if (mCaseInfoModel.getDescription().length() > 0) {
                mCaseInfoDescription.setText(mCaseInfoModel.getDescription());
            } else {
                mCaseInfoDescription.setVisibility(View.GONE);
            }
            if (mCaseInfoModel.getAddress().length() > 0) {
                mCaseInfoAddress.setText(mCaseInfoModel.getProvince().getName() +
                        mCaseInfoModel.getCity().getName() +
                        mCaseInfoModel.getDistrict().getName()+
                        mCaseInfoModel.getAddress());
            } else {
                mCaseInfoAddress.setVisibility(View.GONE);
            }
            if (mCaseInfoModel.getPhysical_resources() != null &&
                    mCaseInfoModel.getPhysical_resources().getId() > 0) {
                mCaseInfoResLL.setVisibility(View.VISIBLE);
            } else {
                mCaseInfoResLL.setVisibility(View.GONE);
            }
            mCaseInfoResNameTV.setText(mCaseInfoModel.getPhysical_resources().getName());
            if (mCaseInfoModel.getPhysical_resources().getPrice() != null) {
                mCaseInfoResPriceTV.setText(getResources().getString(R.string.order_field_listitem_price_unit_text) +
                        com.linhuiba.linhuifield.connector.Constants.getdoublepricestring(
                                mCaseInfoModel.getPhysical_resources().getPrice(), 0.01
                        ));
            }
            if (mCaseInfoModel.getPhysical_resources().getNumber_of_people() != null) {
                mCaseInfoResPeopleTV.setText(String.valueOf(mCaseInfoModel.getPhysical_resources().getNumber_of_people()));
            }
            if (mCaseInfoModel.getPhysical_resources().getNumber_of_order() != null) {
                mCaseInfoResNumOrderTV.setText(String.valueOf(mCaseInfoModel.getPhysical_resources().getNumber_of_order()));
            }
            if (mCaseInfoModel.getPhysical_resources().getAverage_score() != null) {
                mCaseInfoResAverageScore.setText(mCaseInfoModel.getPhysical_resources().getAverage_score());
            }
            Picasso.with(this).load(R.drawable.ic_no_pic_big).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(150,120).into(mCaseInfoResourceImgv);
            if (mCaseInfoModel.getPhysical_resources().getPhysical_resource_first_img().length() > 0) {
                JSONObject jsonObject = JSONObject.parseObject(mCaseInfoModel.getPhysical_resources().getPhysical_resource_first_img());
                if (jsonObject.get("pic_url") != null) {
                    Picasso.with(this).load(jsonObject.get("pic_url").toString() + Config.Linhui_Mid_Watermark).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(150,120).into(mCaseInfoResourceImgv);
                }
            }
        }
    }

    @Override
    public void onCaseInfoFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
    }

    @Override
    public void onCaseSelectionFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onCaseSelectionSuccess(CaseThemeModel caseThemeModel) {

    }
}
