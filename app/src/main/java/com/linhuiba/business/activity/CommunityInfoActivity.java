package com.linhuiba.business.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baselib.app.util.MessageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.FieldinfoRecommendResourcesAdapter;
import com.linhuiba.business.adapter.GlideImageLoader;
import com.linhuiba.business.adapter.MyCouponsAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.OnMultiClickListener;
import com.linhuiba.business.model.CommunityInfoModel;
import com.linhuiba.business.model.GroupBookingInfoModel;
import com.linhuiba.business.model.MyCouponsModel;
import com.linhuiba.business.model.PhyResInfoModel;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.business.model.ReviewModel;
import com.linhuiba.business.mvpmodel.LoginMvpModel;
import com.linhuiba.business.mvppresenter.CouponsMvpPresenter;
import com.linhuiba.business.mvppresenter.FieldinfoMvpPresenter;
import com.linhuiba.business.mvpview.CouponsMvpView;
import com.linhuiba.business.mvpview.FieldinfoMvpView;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldAttributesModel;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuifield.fieldview.Field_MyScrollview;
import com.linhuiba.linhuifield.fieldview.ModuleViewAddfieldCommunityInfo;
import com.linhuiba.linhuifield.fieldview.OvalImageView;
import com.linhuiba.linhuifield.fieldview.ZoomImageView;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.linhuipublic.config.SupportPopupWindow;
import com.meiqia.core.MQManager;
import com.meiqia.core.callback.OnEndConversationCallback;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CommunityInfoActivity extends BaseMvpActivity implements FieldinfoMvpView,
        Field_MyScrollview.OnScrollListener, CouponsMvpView {
    @InjectView(R.id.community_info_near_rv)
    RecyclerView mCommunityNearRV;
    @InjectView(R.id.community_near_by_ll)
    LinearLayout mCommunityNearByLL;
    @InjectView(R.id.community_info_loadmore_ll)
    LinearLayout mCommunityInfoLoadMoreLL;
    @InjectView(R.id.community_info_loadmore_nulldata_tv)
    TextView mCommunityInfoNullTV;
    @InjectView(R.id.community_info_scrollview)
    Field_MyScrollview mScrollview;
    @InjectView(R.id.community_info_navbar_titile_ll)
    LinearLayout mCommunityInfoNavBarLL;
    @InjectView(R.id.community_info_status_bar_ll)
    LinearLayout mCommunityInfoStatusBarLL;
    @InjectView(R.id.community_info_name_tv)
    TextView mCommunityInfoName;
    @InjectView(R.id.community_info_address)
    TextView mCommunityInfoAddressTV;
    @InjectView(R.id.community_type_label_tv)
    TextView mCommunityInfoTypeLabelTV;
    @InjectView(R.id.community_info_banner)
    Banner mBanner;
    @InjectView(R.id.community_info_no_data_ll)
    RelativeLayout mCommunityinfoNoDataLL;

    @InjectView(R.id.community_info_type_tv)
    TextView mCommunityInfoTypeTV;
    @InjectView(R.id.community_info_area_tv)
    TextView mCommunityInfoAreaTV;
    @InjectView(R.id.community_info_tradingarea_tv)
    TextView mCommunityInfoTradingareaTV;
    @InjectView(R.id.community_info_tradingarea_ll)
    LinearLayout mCommunityInfoTradingareaLL;
    @InjectView(R.id.community_info_year_ll)
    LinearLayout mCommunityInfoYearLL;
    @InjectView(R.id.community_info_year_tv)
    TextView mCommunityInfoYearTV;
    @InjectView(R.id.community_info_desp_ll)
    LinearLayout mCommunityInfoDespLL;
    @InjectView(R.id.community_info_desp_tv)
    TextView mCommunityInfoDespTV;
    @InjectView(R.id.community_info_attributes_ll)
    LinearLayout mCommunityInfoAttributesLL;
    @InjectView(R.id.community_info_other_attribute_ll)
    LinearLayout mCommunityOtherAttributeInfoLL;
    @InjectView(R.id.community_info_label_ll)
    LinearLayout mCommunityLabelLL;
    @InjectView(R.id.community_info_label0)
    TextView mCommunityInfoLabel0TV;
    @InjectView(R.id.community_info_label1)
    TextView mCommunityInfoLabel1TV;
    @InjectView(R.id.community_info_label2)
    TextView mCommunityInfoLabel2TV;
    @InjectView(R.id.community_info_show_all_tv)
    TextView mCommunityinfoShowAllTV;
    @InjectView(R.id.community_info_other_info_ll)
    LinearLayout mCommunityOtherInfoLL;
    @InjectView(R.id.communityinfo_distance_location_ll)
    LinearLayout mCommunityDistanceLL;
    @InjectView(R.id.communityinfo_distance_location_tv)
    TextView mCommunityDistanceTV;
    //优惠券
    @InjectView(R.id.fieldinfo_receive_coupons_ll)
    LinearLayout mFieldinfoReceiveCouponsLL;
    @InjectView(R.id.fieldinfo_receive_coupons_tv)
    TextView mFieldinfoCouponsPrice;
    @InjectView(R.id.fieldinfo_coupons_tv1)
    TextView mFieldinfoCoupons1;
    @InjectView(R.id.fieldinfo_coupons_tv2)
    TextView mFieldinfoCoupons2;
    //行业
    @InjectView(R.id.community_info_industry_tv)
    TextView mCommunityInfoIndustryTV;
    @InjectView(R.id.community_info_price_no_tv)
    TextView mCommunityInfoPriceNoTV;
    @InjectView(R.id.community_info_price_tv)
    TextView mCommunityInfoPriceTV;
    //所有展位
    @InjectView(R.id.fieldinfo_other_res_gridlistview)
    RecyclerView mFieldinfoOtherResRV;
    @InjectView(R.id.fieldinfo_other_res_show_all_ll)
    LinearLayout mFieldinfoOtherResShowAllLL;
    @InjectView(R.id.fieldinfo_other_res_layout) LinearLayout mFieldinfoOtherResLL;
    @InjectView(R.id.fieldinfo_other_res_tv) TextView mFieldinfoOtherResTV;
    // FIXME: 2018/12/14 专属顾问

    @InjectView(R.id.fieldinfo_counselor_imgv)
    OvalImageView mFieldinfoCounselorImgv;
    @InjectView(R.id.fieldinfo_counselor_name_tv)
    TextView mFieldinfoCounselorNameTV;
    @InjectView(R.id.fieldinfo_counselor_description_tv)
    TextView mFieldinfoCounselorDescriptionTV;
    @InjectView(R.id.fieldinfo_counselor_ll)
    LinearLayout mFieldinfoCounseLL;
    @InjectView(R.id.communityinfo_banner_num_tv)
    TextView mCommunityinfoBannerNumTV;
    @InjectView(R.id.communityinfo_banner_size_tv)
    TextView mCommunityinfoBannerSizeTV;
    // FIXME: 2018/12/22 图文详情
    @InjectView(R.id.communityinfo_pic_word_info_ll)
    LinearLayout mCommunityInfoPicWordLL;
    @InjectView(R.id.communityinfo_pic_word_info_webview)
    BridgeWebView mCommunityInfoPicWordWebview;
    private RelativeLayout mTitleBarRL;//taitlebar的layout
    private ImageView mCommunityInfoTitleBackImg;
    private ImageView mCommunityInfoTitleShareImg;
    private TextView mCommunityInfoTitleTV;
    private RelativeLayout mCommunityInfoTitleRL;
    private FieldinfoMvpPresenter mPresenter;
    private int mCityId;
    private int mCommunityId;
    private int page;
    private CommunityInfoModel mCommunityInfoModel;
    private ArrayList<ResourceSearchItemModel> mNearByResList;
    private FieldinfoRecommendResourcesAdapter mNearByAdapter;
    private long mClickTime;
    private int mTitleBarInt;
    private DisplayMetrics mDisplayMetrics;
    private ArrayList<String> mPicList = new ArrayList<String>();
    private GlideImageLoader mFieldinfoImageLoader;
    private boolean mIsRefreshZoomImageview = true;//详情页变了是否重新获取预览大图
    private Dialog mZoomPictureDialog;//详情页预览大图dialog
    private List<ImageView> mImageViewList;
    private boolean isShowAllFieldRes = true;//判断是否展开所有场地信息
    private Drawable mShowAllUpDrawable;//查看场地信息
    private Drawable mShowAllDownDrawable;//查看场地信息
    private Dialog mFeedbacksDialog;
    private TextView mDialogErrorCorrectionTV;//纠错奖励确定按钮
    private Bitmap ShareBitmap = null;//分享需要的bitmap
    private Bitmap miniShareBitmap = null;//小程序分享需要的bitmap
    private String ShareIconStr;
    private String shareWXlinkurl = "";//微信分享的url
    private String sharewxMiniShareLinkUrl = "";//小程序分享的url
    private String ShareTitleStr = "";//分享的标题
    private String mSharePYQTitleStr;
    private String SharedescriptionStr = "";//分享的描述
    private IWXAPI mIWXAPI;
    private Dialog mShareDialog;
    public LocationClient mLocationClient = null;
    private static final int LOACTION_REQUEST_INT = 10;//权限 requestcode
    private static final int DEMAND_RESULTCODE = 6;
    private static final int DEMAND_RESULTCODE_LOGIN = 7;
    private CustomDialog mDemandSuccessDialog;
    private int mStatusBarHeight = 70;
    //优惠券
    private List<MyCouponsModel> mCanReceiveCouponsLists = new ArrayList<>();
    private List<MyCouponsModel> mReceivedCouponsLists = new ArrayList<>();
    private MyCouponsAdapter mCanReceiveAdapter;
    private MyCouponsAdapter mReceivedAdapter;
    private boolean isPwCanReceive = false;
    private boolean isPwReceived = false;
    private List<MyCouponsModel> canReceiveCouponsList = new ArrayList<>();
    private List<MyCouponsModel> receivedCouponsList = new ArrayList<>();
    private LinearLayout canReceiveAllLL;
    private LinearLayout receivedAllLL;
    private LinearLayout receivedLL;
    private LinearLayout canReceiveLL;
    private SupportPopupWindow mCouponsPW;
    private static final int COUPONS_REQUESTCODE = 5;
    public CouponsMvpPresenter mCouponsMvpPresenter;
    public boolean isReceiveClick;//是否是领取优惠券 不是兑换
    //所有展位
    private ArrayList<ResourceSearchItemModel> mFieldInfoOtherDataList = new ArrayList<>();
    private ArrayList<ResourceSearchItemModel> mFieldInfoOtherDataListTemp = new ArrayList<>();
    private FieldinfoRecommendResourcesAdapter mOtherPhyResAdapter;
    //顾问
    private final int CALL_PHONE_CODE = 110;
    private CustomDialog mIntegralDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_community_info);
        initView();
    }
    private void initView() {
        ButterKnife.inject(this);
        setSteepStatusBar();
        int stausBarHeight = getSteepStatusBarHeight();
        if (stausBarHeight > 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    stausBarHeight);
            mCommunityInfoStatusBarLL.setLayoutParams(layoutParams);
            mStatusBarHeight = 44 + com.linhuiba.linhuifield.connector.Constants.Px2Dp(CommunityInfoActivity.this,stausBarHeight);
        }
        mPresenter = new FieldinfoMvpPresenter();
        mPresenter.attachView(this);
        mCouponsMvpPresenter = new CouponsMvpPresenter();
        mCouponsMvpPresenter.attachView(this);
        mTitleBarRL = (RelativeLayout) findViewById(R.id.common_title_bar).findViewById(R.id.action_layout_top);
        mCommunityInfoTitleShareImg = (ImageView) findViewById(R.id.common_title_bar).findViewById(R.id.action_img_top);
        mCommunityInfoTitleBackImg = (ImageView) findViewById(R.id.common_title_bar).findViewById(R.id.back_button_top);
        mCommunityInfoTitleTV = (TextView) findViewById(R.id.common_title_bar).findViewById(R.id.title);
        mCommunityInfoTitleTV.setTextColor(getResources().getColor(R.color.white));
        mCommunityInfoTitleRL = (RelativeLayout) findViewById(R.id.about_title);
        mCommunityInfoTitleRL.setBackgroundColor(getResources().getColor(R.color.color_null));
        mCommunityNearRV.setNestedScrollingEnabled(false);
        mScrollview.setOnScrollListener(this);
        mFieldinfoOtherResRV.setNestedScrollingEnabled(false);
        mShowAllUpDrawable = getResources().getDrawable(R.drawable.ic_close_green);
        mShowAllUpDrawable.setBounds(0, 0, mShowAllUpDrawable.getMinimumWidth(), mShowAllUpDrawable.getMinimumHeight());
        mShowAllDownDrawable = getResources().getDrawable(R.drawable.ic_into_green);
        mShowAllDownDrawable.setBounds(0, 0, mShowAllDownDrawable.getMinimumWidth(), mShowAllDownDrawable.getMinimumHeight());
        mIWXAPI = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        mIWXAPI.registerApp(Constants.APP_ID);
        TitleBarUtils.showBackImg(this, true);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.txt_field_detail));
        findViewById(R.id.community_info_all_ll).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScroll(mScrollview.getScrollY());
                System.out.println(mScrollview.getScrollY());
            }
        });
        TitleBarUtils.showActionImg(this, true, getResources().getDrawable(R.drawable.ic_share_white), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 分享功能
                if (ShareBitmap == null || miniShareBitmap == null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (mCommunityInfoModel.getCommunity_imgs()!= null &&
                                    mCommunityInfoModel.getCommunity_imgs().size() > 0 &&
                                    mCommunityInfoModel.getCommunity_imgs().get(0).getPic_url()!= null &&
                                    mCommunityInfoModel.getCommunity_imgs().get(0).getPic_url().length() > 0) {
                                ShareIconStr = mCommunityInfoModel.getCommunity_imgs().get(0).getPic_url().toString()+ com.linhuiba.linhuipublic.config.Config.Linhui_Mid_Watermark;
                                ShareBitmap = com.linhuiba.linhuifield.connector.Constants.GetLocalOrNetBitmap(ShareIconStr);
                                ShareBitmap = com.linhuiba.linhuifield.connector.Constants.addWaterMark(ShareBitmap,SharedescriptionStr,CommunityInfoActivity.this);
                                ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                                miniShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 120, 120, true);//压缩Bitmap
                                if (ShareBitmap == null) {
                                    ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                                    ShareBitmap = com.linhuiba.linhuifield.connector.Constants.addWaterMark(ShareBitmap,SharedescriptionStr,CommunityInfoActivity.this);
                                    ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                                }
                                if (miniShareBitmap == null) {
                                    miniShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                                }
                            } else {
                                ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                                ShareBitmap = com.linhuiba.linhuifield.connector.Constants.addWaterMark(ShareBitmap,SharedescriptionStr,CommunityInfoActivity.this);
                                ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                                miniShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                            }
                            mHandler.sendEmptyMessage(0);
                        }
                    }).start();
                } else {
                    View myView = CommunityInfoActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                    mShareDialog = new AlertDialog.Builder(CommunityInfoActivity.this).create();
                    Constants constants = new Constants(CommunityInfoActivity.this,
                            ShareIconStr);
                    constants.shareWXMiniPopupWindow(CommunityInfoActivity.this,myView,mShareDialog,mIWXAPI,shareWXlinkurl,
                            ShareTitleStr,
                            SharedescriptionStr, ShareBitmap,sharewxMiniShareLinkUrl,miniShareBitmap,mSharePYQTitleStr);
                }
            }
        });

        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        //设置预览图片控件的大小
        int width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        int height = width * com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT /
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH;
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mBanner.getLayoutParams();
        //设置图片显示高度
        linearParams.height = height;
        linearParams.width = width;
        mBanner.setLayoutParams(linearParams);
        mFieldinfoImageLoader = new GlideImageLoader(CommunityInfoActivity.this,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT);
        mCommunityinfoNoDataLL.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if (intent.getExtras().get("city_id") != null) {
                mCityId = intent.getExtras().getInt("city_id");
                if (mCityId == 0) {
                    if (LoginManager.getInstance().getTrackcityid() != null &&
                            LoginManager.getInstance().getTrackcityid().length() > 0) {
                        mCityId = Integer.parseInt(LoginManager.getInstance().getTrackcityid());
                    } else {
                        mCityId = 90;
                    }
                }
            }
            if (intent.getExtras().get("id") != null) {
                mCommunityId = intent.getExtras().getInt("id");
            }
            if (mCityId > 0 && mCommunityId > 0) {
                if (LoginManager.isLogin()) {
                    String parameter = "/"+String.valueOf(mCommunityId);
                    LoginMvpModel.sendBrowseHistories("field_detail",parameter,LoginManager.getTrackcityid());
                }
                showProgressDialog();
                initData();
            }
        }
    }
    private void initData() {
        mFieldinfoOtherResLL.setVisibility(View.GONE);
        mPresenter.getCommunityInfo(mCommunityId);
        mPresenter.getNearbyResList(mCommunityId,mCityId,page,10);
        if (mCommunityId > 0) {
            if (LoginManager.isLogin()) {
                //未领取
                mCouponsMvpPresenter.getCommunityCoupons(mCommunityId,1,100,1);
                ///已领取
                mCouponsMvpPresenter.getCommunityCoupons(mCommunityId,2,1,1);
            }
            mPresenter.getOtherPhyRes(mCommunityId,1,100, 0,null);
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mCouponsMvpPresenter != null) {
            mCouponsMvpPresenter.detachView();
        }
    }

    @OnClick({
            R.id.community_info_service,
            R.id.community_info_phone,
            R.id.community_info_navbar_titile_ll,
            R.id.community_info_address_ll,
            R.id.community_info_award_ll,
            R.id.community_info_show_all_ll,
            R.id.communityinfo_change_explain_ll,
            R.id.communityinfo_no_sell_res_reserve_tv,
            R.id.fieldinfo_receive_coupons_ll,
            R.id.fieldinfo_counselor_call_tv,
            R.id.fieldinfo_counselor_wx_tv,
            R.id.community_info_counselor_tv
    })
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.community_info_service:
                AndPermission.with(CommunityInfoActivity.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();

                break;
            case R.id.community_info_phone:
                if (mCommunityInfoModel.getService_phone() != null &&
                        mCommunityInfoModel.getService_phone().length() > 0) {
                    Intent intent_mobile = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                            + mCommunityInfoModel.getService_phone().toString().trim()));
                    startActivity(intent_mobile);
                }
                break;
            case R.id.community_info_navbar_titile_ll:
                if (System.currentTimeMillis() - mClickTime < 800) {
                    //此处做双击具体业务逻辑
                    mScrollview.smoothScrollTo(0, 0);
                } else {
                    mClickTime = System.currentTimeMillis();
                    //表示单击，此处也可以做单击的操作
                }
                break;
            case R.id.community_info_address_ll:
                if (mCommunityInfoModel.getLat() != null &&
                        mCommunityInfoModel.getLng() != null &&
                        mCommunityInfoModel.getName() != null &&
                        mCommunityInfoModel.getName().length() > 0 &&
                        mCommunityInfoModel.getDetailed_address() != null &&
                        mCommunityInfoModel.getDetailed_address().length() > 0) {
                    Intent mapinfo_intent = new Intent(CommunityInfoActivity.this,FieldinfoMapinfoActivity.class);
                    mapinfo_intent.putExtra("longitude",mCommunityInfoModel.getLng());
                    mapinfo_intent.putExtra("latitude",mCommunityInfoModel.getLat());
                    mapinfo_intent.putExtra("resourcename",mCommunityInfoModel.getName());
                    mapinfo_intent.putExtra("address",mCommunityInfoModel.getDetailed_address());
                    startActivity(mapinfo_intent);
                }
                break;
            case R.id.community_info_award_ll:
                showFeedbacksDialog(1);
                break;
            case R.id.community_info_show_all_ll:
                if (isShowAllFieldRes) {
                    isShowAllFieldRes = false;
                    mCommunityinfoShowAllTV.setCompoundDrawables(null, null, mShowAllDownDrawable, null);
                    mCommunityOtherInfoLL.setVisibility(View.GONE);
                    mCommunityinfoShowAllTV.setText(getResources().getString(R.string.fieldinfo_show_all_resource_info_str));
                } else {
                    mCommunityinfoShowAllTV.setCompoundDrawables(null, null, mShowAllUpDrawable, null);
                    isShowAllFieldRes = true;
                    mCommunityOtherInfoLL.setVisibility(View.VISIBLE);
                    mCommunityinfoShowAllTV.setText(getResources().getString(R.string.fieldinfo_show_small_resource_info_str));
                }
                break;
            case R.id.communityinfo_change_explain_ll:
                showFeedbacksDialog(0);
                break;
            case R.id.communityinfo_no_sell_res_reserve_tv:
                if (mCommunityInfoModel.getId() > 0) {
                    if (LoginManager.isLogin()) {
                        startDemandActivity();
                    } else {
                        Intent intent = new Intent(CommunityInfoActivity.this, LoginActivity.class);
                        startActivityForResult(intent,DEMAND_RESULTCODE_LOGIN);
                    }
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                }
                break;
            case R.id.fieldinfo_receive_coupons_ll:
                if (LoginManager.isLogin()) {
                    if ((mCanReceiveCouponsLists != null &&
                            mCanReceiveCouponsLists.size()> 0) ||
                            (mReceivedCouponsLists != null &&
                                    mReceivedCouponsLists.size()> 0)) {
                        showCouponsPW();
                    }
                } else {
                    Intent loginIntent = new Intent(CommunityInfoActivity.this,LoginActivity.class);
                    startActivityForResult(loginIntent,COUPONS_REQUESTCODE);
                }
                break;
            case R.id.fieldinfo_counselor_call_tv:
                // FIXME: 2018/12/14 联系顾问
                AndPermission.with(CommunityInfoActivity.this)
                        .requestCode(CALL_PHONE_CODE)
                        .permission(
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.READ_PHONE_STATE)
                        .callback(listener)
                        .start();

                break;
            case R.id.fieldinfo_counselor_wx_tv:
                // FIXME: 2018/12/14 加顾问微信
                if (mCommunityInfoModel.getService_representative() != null &&
                        mCommunityInfoModel.getService_representative().getQrcode() != null &&
                        mCommunityInfoModel.getService_representative().getQrcode().length() > 0) {
                    showCounselorDialog(1);
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                }
                break;
            case R.id.community_info_counselor_tv:
                // FIXME: 2018/12/14 顾问
                if (mCommunityInfoModel.getService_representative() != null &&
                        mCommunityInfoModel.getService_representative().getId() > 0) {
                    showCounselorDialog(0);
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
                }

                break;
            default:
                break;
        }
    }
    private void startDemandActivity() {
        Intent demandIntent = new Intent(CommunityInfoActivity.this,SubmitDemandActivity.class);
        demandIntent.putExtra("community_res_id", String.valueOf(mCommunityInfoModel.getId()));
        ArrayList<Integer> cityIds = new ArrayList<>();
        if (mCommunityInfoModel.getCity() != null &&
                mCommunityInfoModel.getCity().getId() > 0) {
            cityIds.add(mCommunityInfoModel.getCity().getId());
        }
        ArrayList<Integer> communityTypeIds = new ArrayList<>();
        if (mCommunityInfoModel.getCategory() != null &&
                mCommunityInfoModel.getCategory().getId() > 0) {
            communityTypeIds.add(mCommunityInfoModel.getCategory().getId());
        }
        demandIntent.putExtra("city_ids", (Serializable) cityIds);
        demandIntent.putExtra("community_type_ids", (Serializable) communityTypeIds);
        startActivityForResult(demandIntent,DEMAND_RESULTCODE);
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if(requestCode == Constants.PermissionRequestCode) {
                MQConfig.init(CommunityInfoActivity.this, com.linhuiba.linhuipublic.config.Config.MQAppkey, new OnInitCallback() {
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
                            Intent intent = new MQIntentBuilder(CommunityInfoActivity.this)
                                    .setClientInfo(clientInfo)
                                    .setCustomizedId(LoginManager.getUid())
                                    .build();
                            startActivityForResult(intent,10);
                        } else {
                            Intent intent = new MQIntentBuilder(CommunityInfoActivity.this)
                                    .build();
                            startActivityForResult(intent,10);
                        }

                    }

                    @Override
                    public void onFailure(int code, String message) {
                        MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                    }
                });

            } else if(requestCode == LOACTION_REQUEST_INT) {
                //定位
                mLocationClient = new LocationClient(CommunityInfoActivity.this);
                mLocationClient.registerLocationListener(new MyLocationListener());//注册定位监听接口
                initLocation();
            } else if (requestCode == CALL_PHONE_CODE) {
                // FIXME: 2018/12/19 顾问电话
                if (mCommunityInfoModel.getService_representative() != null &&
                        mCommunityInfoModel.getService_representative().getTel() != null &&
                        mCommunityInfoModel.getService_representative().getTel().length() > 0) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                            + mCommunityInfoModel.getService_representative().getTel()));// FIXME: 2018/12/19 测试数据
                    if (ActivityCompat.checkSelfPermission(CommunityInfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if(requestCode == Constants.PermissionRequestCode) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            }
        }
    };

    @Override
    public void onFieldinfoFailure(boolean superresult, Throwable error) {
        mCommunityinfoNoDataLL.setVisibility(View.VISIBLE);
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
    }

    @Override
    public void onFieldinfoResSuccess(PhyResInfoModel resInfo) {

    }

    @Override
    public void onResReviewSuccess(ArrayList<ReviewModel> list) {

    }

    @Override
    public void onResReviewFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onGroupinfoResSuccess(GroupBookingInfoModel resInfo) {

    }

    @Override
    public void onNearbyResSuccess(ArrayList<ResourceSearchItemModel> list) {
        mNearByResList = list;
        if (mNearByResList != null && mNearByResList.size() > 0) {
            mCommunityNearByLL.setVisibility(View.VISIBLE);
            mNearByAdapter = new FieldinfoRecommendResourcesAdapter(this,this,
                    R.layout.activity_fieldinfo_recommend_resources_item,mNearByResList, 1);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            //设置布局管理器
            mCommunityNearRV.setLayoutManager(gridLayoutManager);
            //设置为垂直布局，这也是默认的
            //设置Adapter
            mCommunityNearRV.setAdapter(mNearByAdapter);
            mCommunityInfoLoadMoreLL.setVisibility(View.GONE);
            if (mNearByResList.size() < 10) {
                mCommunityInfoNullTV.setVisibility(View.VISIBLE);
            }
            mNearByAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent fieldinfo = null;
                    if (mNearByResList.get(position).getType() != null) {
                        if (mNearByResList.get(position).getType().equals(com.linhuiba.business.config.Config.JUMP_COMMUNITY_RES)) {
                            fieldinfo = new Intent(CommunityInfoActivity.this, CommunityInfoActivity.class);
                            fieldinfo.putExtra("city_id", mCityId);
                            fieldinfo.putExtra("id", mNearByResList.get(position).getId());
                            startActivity(fieldinfo);
                        } else if (mNearByResList.get(position).getType().equals(com.linhuiba.business.config.Config.JUMP_PHYSICAL_RES)) {
                            if (mNearByResList.get(position).getTop_physical_id() != null) {
                                fieldinfo = new Intent(CommunityInfoActivity.this, FieldInfoActivity.class);
                                fieldinfo.putExtra("fieldId", String.valueOf(mNearByResList.get(position).getTop_physical_id()));
                                fieldinfo.putExtra("community_id", mNearByResList.get(position).getId());
                                startActivity(fieldinfo);
                            }
                        } else if (mNearByResList.get(position).getType().equals(com.linhuiba.business.config.Config.JUMP_SELLING_RES)) {
                            if (mNearByResList.get(position).getTop_physical_id() != null) {
                                fieldinfo = new Intent(CommunityInfoActivity.this, FieldInfoActivity.class);
                                fieldinfo.putExtra("sell_res_id", String.valueOf(mNearByResList.get(position).getTop_physical_id()));
                                fieldinfo.putExtra("is_sell_res", true);
                                fieldinfo.putExtra("community_id", mNearByResList.get(position).getId());
                                startActivity(fieldinfo);
                            }
                        }
                    }
                }
            });
            mScrollview.setOnScrollToBottomLintener(new Field_MyScrollview.OnScrollToBottomListener() {
                @Override
                public void onScrollBottomListener(boolean isBottom) {
                    if (isBottom && mCommunityInfoNullTV.getVisibility() == View.GONE &&
                            mCommunityInfoLoadMoreLL.getVisibility() == View.GONE) {
                        mCommunityInfoLoadMoreLL.setVisibility(View.VISIBLE);
                        page ++;
                        mPresenter.getNearbyResList(mCommunityId,mCityId,page,10);
                    }
                }
            });
        } else {
            mCommunityNearByLL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNearbyResMoreSuccess(ArrayList<ResourceSearchItemModel> list) {
        ArrayList<ResourceSearchItemModel> tmp = list;
        if (tmp != null && tmp.size() > 0) {
            for( ResourceSearchItemModel fieldDetail: tmp ){
                mNearByResList.add(fieldDetail);
            }
            mNearByAdapter.notifyDataSetChanged();
            mCommunityInfoLoadMoreLL.setVisibility(View.GONE);
            if (tmp.size() < 10) {
                mCommunityInfoNullTV.setVisibility(View.VISIBLE);
            }
        } else {
            mCommunityInfoLoadMoreLL.setVisibility(View.GONE);
            mCommunityInfoNullTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onOtherPhyResSuccess(ArrayList<ResourceSearchItemModel> list,int csort,int total) {
        mFieldInfoOtherDataList = list;
        if (mFieldInfoOtherDataList != null && mFieldInfoOtherDataList.size() > 0) {
            if (mFieldInfoOtherDataList.size() > 3) {
                mFieldinfoOtherResShowAllLL.setVisibility(View.VISIBLE);
                for (int i = 0; i < 3; i++) {
                    mFieldInfoOtherDataListTemp.add(mFieldInfoOtherDataList.get(i));
                }
            } else {
                mFieldinfoOtherResShowAllLL.setVisibility(View.GONE);
                for (int i = 0; i < mFieldInfoOtherDataList.size(); i++) {
                    mFieldInfoOtherDataListTemp.add(mFieldInfoOtherDataList.get(i));
                }
            }
            mFieldinfoOtherResLL.setVisibility(View.VISIBLE);
            mFieldinfoOtherResTV.setText(getResources().getString(R.string.module_fieldinfo_other_all_physicals) +
                    "(" + String.valueOf(total) + ")");
            mOtherPhyResAdapter = new FieldinfoRecommendResourcesAdapter(this,this,
                    R.layout.activity_fieldinfo_recommend_resources_item,mFieldInfoOtherDataListTemp, 2);
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            mFieldinfoOtherResRV.setLayoutManager(gridLayoutManager);
            mFieldinfoOtherResRV.setAdapter(mOtherPhyResAdapter);
            mOtherPhyResAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent fieldinfo = new Intent(CommunityInfoActivity.this, FieldInfoActivity.class);
                    fieldinfo.putExtra("fieldId", String.valueOf(mFieldInfoOtherDataList.get(position).getId()));
                    fieldinfo.putExtra("community_id", mCommunityId);
                    startActivity(fieldinfo);
                }
            });
        } else {
            mFieldinfoOtherResLL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onOtherPhyResMoreSuccess(ArrayList<ResourceSearchItemModel> list) {

    }

    @Override
    public void onCommunityInfoSuccess(CommunityInfoModel resInfo) {
        mCommunityInfoModel = resInfo;
        if (mCommunityInfoModel.getLng() != null && mCommunityInfoModel.getLat() != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    initGetLocation();
                }
            }).start();
        }
        if (resInfo != null) {
            if (mPicList != null) {
                mPicList.clear();
            }
            if (resInfo.getCommunity_imgs() != null) {
                if (resInfo.getCommunity_imgs().size() > 0) {
                    for (int i = 0; i < resInfo.getCommunity_imgs().size(); i++) {
                        if (resInfo.getCommunity_imgs().get(i).getPic_url() != null &&
                                resInfo.getCommunity_imgs().get(i).getPic_url().length() > 0) {
                            mPicList.add(resInfo.getCommunity_imgs().get(i).getPic_url().toString() + com.linhuiba.linhuipublic.config.Config.Linhui_Max_Watermark);
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
                                showZoomPictureDialog((position - 1) % mPicList.size());
                            }
                        });
                        mBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                if (position % mPicList.size() == 0) {
                                    mCommunityinfoBannerNumTV.setText(String.valueOf(mPicList.size()));
                                } else {
                                    mCommunityinfoBannerNumTV.setText(String.valueOf(position % mPicList.size()));
                                }
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                        mCommunityinfoBannerSizeTV.setText("/" + String.valueOf(mPicList.size()));
                        mBanner.start();
                    }

                }
            }
            if (mCommunityInfoModel.getCategory() != null &&
                    mCommunityInfoModel.getCategory().getName() != null &&
                    mCommunityInfoModel.getCategory().getName().length() > 0) {
                SharedescriptionStr = SharedescriptionStr + mCommunityInfoModel.getCategory().getName() + "  ";
            }
            if (mCommunityInfoModel.getLowest_price() != null &&
                    mCommunityInfoModel.getLowest_price().length() > 0 &&
                    Double.parseDouble(mCommunityInfoModel.getLowest_price()) > 0) {
                SharedescriptionStr = SharedescriptionStr +  (Constants.getpricestring(mCommunityInfoModel.getLowest_price(), 0.01) +
                        getResources().getString(R.string.term_types_unit_txt) +
                        getResources().getString(R.string.fieldinfo_subsidy_fee_first_prompt));
            }
            if (SharedescriptionStr.length() > 0) {
                SharedescriptionStr = SharedescriptionStr + "\n";
            }
            String cityName = "";
            if (mCommunityInfoModel.getCity() != null &&
                    mCommunityInfoModel.getCity().getName() != null &&
                    mCommunityInfoModel.getCity().getName().length() > 0) {
                cityName = mCommunityInfoModel.getCity().getName();
                SharedescriptionStr = SharedescriptionStr + mCommunityInfoModel.getCity().getName();
            }
            if (mCommunityInfoModel.getDistrict() != null &&
                    mCommunityInfoModel.getDistrict().getName() != null &&
                    mCommunityInfoModel.getDistrict().getName().length() > 0) {
                SharedescriptionStr = SharedescriptionStr + mCommunityInfoModel.getDistrict().getName();
            }
            if (mCommunityInfoModel.getDetailed_address() != null&&
                    mCommunityInfoModel.getDetailed_address().length() > 0) {
                mCommunityInfoAddressTV.setText(mCommunityInfoModel.getDetailed_address());
                SharedescriptionStr = SharedescriptionStr + mCommunityInfoModel.getDetailed_address();
            } else {
                mCommunityInfoAddressTV.setText(getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            if (mCommunityInfoModel.getName() != null&&
                    mCommunityInfoModel.getName().length() > 0) {
                mCommunityInfoName.setText(mCommunityInfoModel.getName());
                ShareTitleStr = mCommunityInfoModel.getName().toString();
                mSharePYQTitleStr = getResources().getString(R.string.fieldinfo_share_text) + "(" + cityName +
                        mCommunityInfoModel.getName().toString() + ")" +
                        getResources().getString(R.string.fieldinfo_share_linhuiba_mark_text);
            } else {
                mCommunityInfoName.setText(getResources().getString(R.string.fieldinfo_no_parameter_message));
            }
            shareWXlinkurl = Config.SHARE_COMMUNITY_INFO_URL + String.valueOf(mCommunityId) + "?BackKey=1&is_app=1&"+
            "city_id="+String.valueOf(mCityId);
            sharewxMiniShareLinkUrl = Config.WX_MINI_SHARE_COMMUNITY_INFO_URL + String.valueOf(mCommunityId);

            if (mCommunityInfoModel.getLabels() != null &&
                    mCommunityInfoModel.getLabels().size() > 0) {
                mCommunityLabelLL.setVisibility(View.VISIBLE);
                for (int i = 0; i < mCommunityInfoModel.getLabels().size(); i++) {
                    if (i < 3) {
                        String label = "";
                        if (mCommunityInfoModel.getLabels().get(i).getDisplay_name() != null &&
                                mCommunityInfoModel.getLabels().get(i).getDisplay_name().length() > 0) {
                            label = mCommunityInfoModel.getLabels().get(i).getDisplay_name();
                        } else {
                            if (mCommunityInfoModel.getLabels().get(i).getName() != null &&
                                    mCommunityInfoModel.getLabels().get(i).getName().length() > 0) {
                                label = mCommunityInfoModel.getLabels().get(i).getName();
                            }
                        }
                        if (i == 0) {
                            if (label.length() > 0) {
                                mCommunityInfoLabel0TV.setVisibility(View.VISIBLE);
                                mCommunityInfoLabel0TV.setText(label);
                                mCommunityLabelLL.setVisibility(View.VISIBLE);
                            } else {
                                mCommunityInfoLabel0TV.setVisibility(View.GONE);
                            }
                        } else if (i == 1) {
                            if (label.length() > 0) {
                                mCommunityInfoLabel1TV.setVisibility(View.VISIBLE);
                                mCommunityInfoLabel1TV.setText(label);
                                mCommunityLabelLL.setVisibility(View.VISIBLE);
                            } else {
                                mCommunityInfoLabel1TV.setVisibility(View.GONE);
                            }
                        } else if (i == 2) {
                            if (label.length() > 0) {
                                mCommunityInfoLabel2TV.setVisibility(View.VISIBLE);
                                mCommunityLabelLL.setVisibility(View.VISIBLE);
                                mCommunityInfoLabel2TV.setText(label);
                            } else {
                                mCommunityInfoLabel2TV.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            } else {
                mCommunityLabelLL.setVisibility(View.GONE);
            }
            if (mCommunityInfoModel.getCategory() != null &&
                    mCommunityInfoModel.getCategory().getName() != null &&
                    mCommunityInfoModel.getCategory().getName().length() > 0) {
                mCommunityInfoTypeLabelTV.setText(mCommunityInfoModel.getCategory().getName());
                mCommunityInfoTypeLabelTV.setVisibility(View.VISIBLE);
            } else {
                mCommunityInfoTypeLabelTV.setVisibility(View.GONE);
            }
            if (mCommunityInfoModel.getCategory_full_name() != null &&
                    mCommunityInfoModel.getCategory_full_name().length() > 0) {
                mCommunityInfoTypeTV.setText(mCommunityInfoModel.getCategory_full_name());
            } else {
                mCommunityInfoTypeTV.setText(getResources().getString(R.string.order_nodata_toast));
            }
            if (mCommunityInfoModel.getBuilding_area() != null &&
                    mCommunityInfoModel.getBuilding_area().length() > 0) {
                mCommunityInfoAreaTV.setText(mCommunityInfoModel.getBuilding_area() +
                        getResources().getString(R.string.module_community_info_area_unit));
            } else {
                mCommunityInfoAreaTV.setText(getResources().getString(R.string.order_nodata_toast));
            }
            if (mCommunityInfoModel.getTradingarea() != null &&
                    mCommunityInfoModel.getTradingarea().getName() != null &&
                    mCommunityInfoModel.getTradingarea().getName().length() > 0) {
                mCommunityInfoTradingareaTV.setText(mCommunityInfoModel.getTradingarea().getName());
                mCommunityInfoTradingareaLL.setVisibility(View.VISIBLE);
            } else {
                mCommunityInfoTradingareaLL.setVisibility(View.GONE);
            }
            if (mCommunityInfoModel.getBuild_year() != null &&
                    mCommunityInfoModel.getBuild_year().length() > 0) {
                mCommunityInfoYearTV.setText(mCommunityInfoModel.getBuild_year());
                mCommunityInfoYearLL.setVisibility(View.VISIBLE);
            } else {
                mCommunityInfoYearLL.setVisibility(View.GONE);
            }
            if (mCommunityInfoModel.getDescription() != null &&
                    mCommunityInfoModel.getDescription().length() > 0) {
                mCommunityInfoDespLL.setVisibility(View.VISIBLE);
                mCommunityInfoDespTV.setText(mCommunityInfoModel.getDescription());
            } else {
                mCommunityInfoDespLL.setVisibility(View.GONE);
            }

            if (resInfo.getAttributes() != null && resInfo.getAttributes().size() > 0) {
                for (int j = 0; j < resInfo.getAttributes().size(); j++) {
                    if (resInfo.getAttributes().get(j).getName() != null &&
                            resInfo.getAttributes().get(j).getName().length() > 0) {
                        if (resInfo.getAttributes().get(j).getOptions() != null &&
                                resInfo.getAttributes().get(j).getOptions().size() > 0) {
                            if (resInfo.getAttributes().get(j).getType() == 1 ||
                                    resInfo.getAttributes().get(j).getType() == 2) {
                                String tvStr = "";
                                ArrayList<FieldAddfieldAttributesModel> options = resInfo.getAttributes().get(j).getOptions();
                                if (resInfo.getAttributes().get(j).getType() == 1) {
                                    if (options.get(0).getName() != null &&
                                            options.get(0).getName().length() > 0) {
                                        tvStr = tvStr + options.get(0).getName();
                                        if (options.get(0).getMark() != null &&
                                                options.get(0).getMark().length() > 0) {
                                            tvStr = tvStr + " (" + options.get(0).getMark() + ")";
                                        }
                                    }
                                } else {
                                    for (int k = 0; k < options.size(); k++) {
                                        if (options.get(k).getName() != null &&
                                                options.get(k).getName().length() > 0) {
                                            if (tvStr.length() == 0) {
                                                tvStr = tvStr + options.get(k).getName();
                                            } else {
                                                tvStr = tvStr + " 、" + options.get(k).getName();
                                            }
                                            if (options.get(k).getMark() != null &&
                                                    options.get(k).getMark().length() > 0) {
                                                tvStr = tvStr + " (" + options.get(k).getMark() + ")";
                                            }
                                        }
                                    }
                                }
                                final ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this,10);
                                mCommunityInfoAttributesLL.addView(moduleViewAddfieldCommunityInfo);
                                moduleViewAddfieldCommunityInfo.mTextView.setText(resInfo.getAttributes().get(j).getName() + "：");
                                moduleViewAddfieldCommunityInfo.mBackTextView.setText(tvStr);
                            } else {
                                ArrayList<FieldAddfieldAttributesModel> options = resInfo.getAttributes().get(j).getOptions();
                                if (options.get(0).getMark() != null &&
                                        options.get(0).getMark().length() > 0) {
                                    final ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this,10);
                                    mCommunityInfoAttributesLL.addView(moduleViewAddfieldCommunityInfo);
                                    moduleViewAddfieldCommunityInfo.mTextView.setText(resInfo.getAttributes().get(j).getName() + "：");
                                    moduleViewAddfieldCommunityInfo.mBackTextView.setText(options.get(0).getMark());
                                }
                            }
                        }
                    }
                }
            }
            //男女比例等数据
            if (mCommunityInfoModel.getMale_proportion() != null &&
                    mCommunityInfoModel.getMale_proportion().length() > 0) {
                ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this,10);
                mCommunityOtherAttributeInfoLL.addView(moduleViewAddfieldCommunityInfo);
                moduleViewAddfieldCommunityInfo.mTextView.setText(getResources().
                        getString(R.string.txt_editgender_ratiotxt));
                moduleViewAddfieldCommunityInfo.mBackTextView.setText(getResources().
                        getString(R.string.fieldinfo_man_proportion_text) +
                        mCommunityInfoModel.getMale_proportion() +
                        getResources().
                                getString(R.string.fieldinfo_man_proportion_unit_text) + "、" +
                        getResources().
                                getString(R.string.fieldinfo_woman_proportion_text) +
                String.valueOf(100 - Integer.parseInt(mCommunityInfoModel.getMale_proportion())) +
                        getResources().
                                getString(R.string.fieldinfo_man_proportion_unit_text));
            }
            if ((mCommunityInfoModel.getUnmarried_proportion() != null &&
                    mCommunityInfoModel.getUnmarried_proportion().length() > 0) ||
                    (mCommunityInfoModel.getMarried_unpregnant_proportion() != null &&
                            mCommunityInfoModel.getMarried_unpregnant_proportion().length() > 0) ||
                    (mCommunityInfoModel.getMarried_pregnant_proportion() != null &&
                            mCommunityInfoModel.getMarried_pregnant_proportion().length() > 0)) {
                String str = "";
                if ((mCommunityInfoModel.getUnmarried_proportion() != null &&
                        mCommunityInfoModel.getUnmarried_proportion().length() > 0)) {
                    str = str + getResources().
                            getString(R.string.module_community_info_unmarried) +
                            mCommunityInfoModel.getUnmarried_proportion() +
                            getResources().
                                    getString(R.string.fieldinfo_man_proportion_unit_text);
                }
                if (mCommunityInfoModel.getMarried_unpregnant_proportion() != null &&
                        mCommunityInfoModel.getMarried_unpregnant_proportion().length() > 0) {
                    if (str.length() > 0) {
                        str = str + "、";
                    }
                    str = str + getResources().
                            getString(R.string.module_community_info_married_unpregnant) +
                            mCommunityInfoModel.getMarried_unpregnant_proportion() +
                            getResources().
                                    getString(R.string.fieldinfo_man_proportion_unit_text);
                }
                if (mCommunityInfoModel.getMarried_pregnant_proportion() != null &&
                        mCommunityInfoModel.getMarried_pregnant_proportion().length() > 0) {
                    if (str.length() > 0) {
                        str = str + "、";
                    }
                    str = str + getResources().
                            getString(R.string.module_community_info_married_pregnant) +
                            mCommunityInfoModel.getMarried_pregnant_proportion() +
                            getResources().
                                    getString(R.string.fieldinfo_man_proportion_unit_text);
                }
                if (str.length() > 0) {
                    ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this,10);
                    mCommunityOtherAttributeInfoLL.addView(moduleViewAddfieldCommunityInfo);
                    moduleViewAddfieldCommunityInfo.mTextView.setText(getResources().
                            getString(R.string.module_community_info_marital_status));
                    moduleViewAddfieldCommunityInfo.mBackTextView.setText(str);
                }

            }
            if ((mCommunityInfoModel.getUndergraduate_proportion() != null &&
                    mCommunityInfoModel.getUndergraduate_proportion().length() > 0) ||
                    (mCommunityInfoModel.getGraduate_proportion() != null &&
                            mCommunityInfoModel.getGraduate_proportion().length() > 0) ||
                    (mCommunityInfoModel.getSpecialty_proportion() != null &&
                            mCommunityInfoModel.getSpecialty_proportion().length() > 0)) {
                String str = "";
                if ((mCommunityInfoModel.getUndergraduate_proportion() != null &&
                        mCommunityInfoModel.getUndergraduate_proportion().length() > 0)) {
                    str = str + getResources().
                            getString(R.string.module_community_info_undergraduate) +
                            mCommunityInfoModel.getUndergraduate_proportion() +
                            getResources().
                                    getString(R.string.fieldinfo_man_proportion_unit_text);
                }
                if (mCommunityInfoModel.getGraduate_proportion() != null &&
                        mCommunityInfoModel.getGraduate_proportion().length() > 0) {
                    if (str.length() > 0) {
                        str = str + "、";
                    }
                    str = str + getResources().
                            getString(R.string.module_community_info_graduate_unpregnant) +
                            mCommunityInfoModel.getGraduate_proportion() +
                            getResources().
                                    getString(R.string.fieldinfo_man_proportion_unit_text);
                }
                if (mCommunityInfoModel.getSpecialty_proportion() != null &&
                        mCommunityInfoModel.getSpecialty_proportion().length() > 0) {
                    if (str.length() > 0) {
                        str = str + "、";
                    }
                    str = str + getResources().
                            getString(R.string.module_community_info_specialty_pregnant) +
                            mCommunityInfoModel.getSpecialty_proportion() +
                            getResources().
                                    getString(R.string.fieldinfo_man_proportion_unit_text);
                }
                if (str.length() > 0) {
                    ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this,10);
                    mCommunityOtherAttributeInfoLL.addView(moduleViewAddfieldCommunityInfo);
                    moduleViewAddfieldCommunityInfo.mTextView.setText(getResources().
                            getString(R.string.module_community_info_degree_level));
                    moduleViewAddfieldCommunityInfo.mBackTextView.setText(str);
                }
            }
            if (mCommunityInfoModel.getCareer_background() != null &&
                    mCommunityInfoModel.getCareer_background().size() > 0) {
                String str = "";
                for (int i = 0; i < mCommunityInfoModel.getCareer_background().size(); i++) {
                    if (mCommunityInfoModel.getCareer_background().get(i).getDisplay_name() != null &&
                            mCommunityInfoModel.getCareer_background().get(i).getDisplay_name().length() > 0 &&
                            mCommunityInfoModel.getCareer_background().get(i).getPivot() != null &&
                            mCommunityInfoModel.getCareer_background().get(i).getPivot().get("proportion") != null &&
                            mCommunityInfoModel.getCareer_background().get(i).getPivot().get("proportion").toString().length() > 0) {
                        if (str.length() > 0) {
                            str = str + "、";
                        }
                        str = str + mCommunityInfoModel.getCareer_background().get(i).getDisplay_name() +
                                mCommunityInfoModel.getCareer_background().get(i).getPivot().get("proportion") +
                                getResources().
                                        getString(R.string.fieldinfo_man_proportion_unit_text);
                    }
                }
                if (str.length() > 0) {
                    ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this,10);
                    mCommunityOtherAttributeInfoLL.addView(moduleViewAddfieldCommunityInfo);
                    moduleViewAddfieldCommunityInfo.mTextView.setText(getResources().
                            getString(R.string.module_community_info_career_background));
                    moduleViewAddfieldCommunityInfo.mBackTextView.setText(str);
                }
            }
            if (mCommunityInfoModel.getAge_group() != null &&
                    mCommunityInfoModel.getAge_group().size() > 0) {
                String str = "";
                for (int i = 0; i < mCommunityInfoModel.getAge_group().size(); i++) {
                    if (mCommunityInfoModel.getAge_group().get(i).getDisplay_name() != null &&
                            mCommunityInfoModel.getAge_group().get(i).getDisplay_name().length() > 0 &&
                            mCommunityInfoModel.getAge_group().get(i).getPivot() != null &&
                            mCommunityInfoModel.getAge_group().get(i).getPivot().get("proportion") != null &&
                            mCommunityInfoModel.getAge_group().get(i).getPivot().get("proportion").toString().length() > 0) {
                        if (str.length() > 0) {
                            str = str + "、";
                        }
                        str = str + mCommunityInfoModel.getAge_group().get(i).getDisplay_name() +
                                mCommunityInfoModel.getAge_group().get(i).getPivot().get("proportion") +
                                getResources().
                                        getString(R.string.fieldinfo_man_proportion_unit_text);
                    }
                }
                if (str.length() > 0) {
                    ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this,10);
                    mCommunityOtherAttributeInfoLL.addView(moduleViewAddfieldCommunityInfo);
                    moduleViewAddfieldCommunityInfo.mTextView.setText(getResources().
                            getString(R.string.module_community_info_age_group));
                    moduleViewAddfieldCommunityInfo.mBackTextView.setText(str);
                }
            }
            if (mCommunityInfoModel.getPrivate_car_proportion() != null &&
                    mCommunityInfoModel.getPrivate_car_proportion().length() > 0) {
                ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this,10);
                mCommunityOtherAttributeInfoLL.addView(moduleViewAddfieldCommunityInfo);
                moduleViewAddfieldCommunityInfo.mTextView.setText(getResources().
                        getString(R.string.module_community_info_private_car));
                moduleViewAddfieldCommunityInfo.mBackTextView.setText(getResources().
                        getString(R.string.module_community_info_has_car) +
                        mCommunityInfoModel.getPrivate_car_proportion() +
                        getResources().
                                getString(R.string.fieldinfo_man_proportion_unit_text) + "、" +
                        getResources().
                                getString(R.string.module_community_info_no_car) +
                        String.valueOf(100 - Integer.parseInt(mCommunityInfoModel.getPrivate_car_proportion())) +
                        getResources().
                                getString(R.string.fieldinfo_man_proportion_unit_text));
            }
            mCommunityinfoNoDataLL.setVisibility(View.GONE);
            // FIXME: 2018/12/18 优惠券
            if (mCommunityInfoModel.getCoupons() != null && mCommunityInfoModel.getCoupons().size() > 0) {
                mFieldinfoReceiveCouponsLL.setVisibility(View.VISIBLE);
                if (mCommunityInfoModel.getCoupons().get(0).getAmount() != null &&
                        mCommunityInfoModel.getCoupons().get(0).getAmount().toString().length() > 0) {
                    mFieldinfoCouponsPrice.setText(Constants.getpricestring(mCommunityInfoModel.getCoupons().get(0).getAmount(),
                            1));
                }
                for (int i = 0; i < mCommunityInfoModel.getCoupons().size(); i++) {
                    if (i < 3) {
                        if (i == 0) {
                            mFieldinfoCoupons1.setVisibility(View.VISIBLE);
                            if (mCommunityInfoModel.getCoupons().get(i).getMin_goods_amount() != null &&
                                    mCommunityInfoModel.getCoupons().get(i).getMin_goods_amount().length() > 0 &&
                                    Double.parseDouble(mCommunityInfoModel.getCoupons().get(i).getMin_goods_amount()) > 0) {
                                mFieldinfoCoupons1.setText(getResources().getString(R.string.module_coupons_first_register_item_amount_first_str) +
                                        Constants.getpricestring(mCommunityInfoModel.getCoupons().get(i).getMin_goods_amount(), 1) +
                                        getResources().getString(R.string.module_my_coupons_item_amount_last_str) +
                                        Constants.getpricestring(mCommunityInfoModel.getCoupons().get(i).getAmount(), 1));
                            } else {
                                mFieldinfoCoupons1.setText(Constants.getpricestring(mCommunityInfoModel.getCoupons().get(i).getAmount(), 1) +
                                        getResources().getString(R.string.term_types_unit_txt) +
                                        getResources().getString(R.string.module_fieldinfo_coupons_no_threshold));
                            }

                        } else if (i == 1) {
                            mFieldinfoCoupons2.setVisibility(View.VISIBLE);
                            if (mCommunityInfoModel.getCoupons().get(i).getMin_goods_amount() != null &&
                                    mCommunityInfoModel.getCoupons().get(i).getMin_goods_amount().length() > 0 &&
                                    Double.parseDouble(mCommunityInfoModel.getCoupons().get(i).getMin_goods_amount()) > 0) {
                                mFieldinfoCoupons2.setText(getResources().getString(R.string.module_coupons_first_register_item_amount_first_str) +
                                        Constants.getpricestring(mCommunityInfoModel.getCoupons().get(i).getMin_goods_amount(), 1) +
                                        getResources().getString(R.string.module_my_coupons_item_amount_last_str) +
                                        Constants.getpricestring(mCommunityInfoModel.getCoupons().get(i).getAmount(), 1));
                            } else {
                                mFieldinfoCoupons2.setText(Constants.getpricestring(mCommunityInfoModel.getCoupons().get(i).getAmount(), 1) +
                                        getResources().getString(R.string.term_types_unit_txt) +
                                        getResources().getString(R.string.module_fieldinfo_coupons_no_threshold));
                            }
                        }
                    }
                }
            } else {
                mFieldinfoReceiveCouponsLL.setVisibility(View.GONE);
            }
            //行业
            if (mCommunityInfoModel.getIndustry_str() != null &&
                    mCommunityInfoModel.getIndustry_str().length() > 0) {
                mCommunityInfoIndustryTV.setVisibility(View.VISIBLE);
                mCommunityInfoIndustryTV.setText(getResources().getString(R.string.module_community_industry) +
                        mCommunityInfoModel.getIndustry_str());
            } else {
                mCommunityInfoIndustryTV.setVisibility(View.GONE);
            }
            //价格
            if (mCommunityInfoModel.getPhysical_resource_count() > 0) {
                if (mCommunityInfoModel.getLowest_price() != null &&
                        mCommunityInfoModel.getLowest_price().length() > 0 &&
                        Double.parseDouble(mCommunityInfoModel.getLowest_price()) > 0) {
                    mCommunityInfoPriceNoTV.setVisibility(View.GONE);
                    mCommunityInfoPriceTV.setText(Constants.getpricestring(mCommunityInfoModel.getLowest_price(), 0.01));
                } else {
                    mCommunityInfoPriceNoTV.setVisibility(View.VISIBLE);
                    mCommunityInfoPriceNoTV.setText(getResources().getString(R.string.home_hot_inquiry_tv_str));
                }
            } else {
                mCommunityInfoPriceNoTV.setVisibility(View.VISIBLE);
            }
            // FIXME: 2018/12/19 顾问
            if (mCommunityInfoModel.getService_representative() != null &&
                    mCommunityInfoModel.getService_representative().getId() > 0) {
                mFieldinfoCounseLL.setVisibility(View.VISIBLE);
                if (mCommunityInfoModel.getService_representative().getAvatar() != null &&
                        mCommunityInfoModel.getService_representative().getAvatar().length() > 0) {
                    Picasso.with(CommunityInfoActivity.this).load(mCommunityInfoModel.getService_representative().getAvatar()).resize(
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(CommunityInfoActivity.this,100),
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(CommunityInfoActivity.this,106)
                    ).into(mFieldinfoCounselorImgv);
                }
                if (mCommunityInfoModel.getService_representative().getName() != null &&
                        mCommunityInfoModel.getService_representative().getName().length() > 0) {
                    mFieldinfoCounselorNameTV.setText(mCommunityInfoModel.getService_representative().getName());
                }
                if (mCommunityInfoModel.getService_representative().getProfile() != null &&
                        mCommunityInfoModel.getService_representative().getProfile().length() > 0) {
                    mFieldinfoCounselorDescriptionTV.setText(mCommunityInfoModel.getService_representative().getProfile());
                }
            }
            // FIXME: 2018/12/22 图文详情
            if (mCommunityInfoModel.getImg_description() != null &&
                    mCommunityInfoModel.getImg_description().length() > 0) {
                mCommunityInfoPicWordLL.setVisibility(View.VISIBLE);
                mCommunityInfoPicWordWebview.loadUrl(Config.FIELDINFO_PIC_WORD_URL + "?type=1&id=" + String.valueOf(mCommunityId));
            }
        }
    }

    @Override
    public void onRecommendResSuccess(ArrayList<ResourceSearchItemModel> list) {

    }

    @Override
    public void onRecommendResMoreSuccess(ArrayList<ResourceSearchItemModel> list) {

    }

    @Override
    public void onFeedbacksSuccess() {
        mDialogErrorCorrectionTV.setEnabled(true);
        if (mFeedbacksDialog.isShowing()) {
            mFeedbacksDialog.dismiss();
        }
        MessageUtils.showToast(getResources().getString(R.string.fieldinfo_error_recovery_content_remindsuccess_text));
    }

    @Override
    public void onFeedbacksFailure(boolean superresult, Throwable error) {
        mDialogErrorCorrectionTV.setEnabled(true);
        if (!superresult) {
            MessageUtils.showToast(getContext(), error.getMessage());
        }
        checkAccess_new(error);
    }

    @Override
    public void onScroll(int scrollY) {
        int width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        mTitleBarInt = width *
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT /
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH - com.linhuiba.linhuifield.connector.Constants.Dp2Px(this, mStatusBarHeight);
        if (scrollY < 0) {
            mCommunityInfoNavBarLL.getBackground().mutate().setAlpha(0);
            mCommunityInfoStatusBarLL.setBackgroundColor(getResources().getColor(R.color.color_null));
            mCommunityInfoTitleTV.setVisibility(View.GONE);
        } else {
            if (scrollY == 0) {
                mCommunityInfoTitleTV.setVisibility(View.GONE);
            } else {
                mCommunityInfoTitleTV.setVisibility(View.VISIBLE);
            }
            if (scrollY < mTitleBarInt) {
                int progress = (int) (new Float(scrollY) / new Float(mTitleBarInt) * 200);
                mCommunityInfoNavBarLL.getBackground().mutate().setAlpha(progress);
                mCommunityInfoTitleBackImg.setImageResource(R.drawable.nav_ic_back_white);
                mCommunityInfoTitleShareImg.setImageResource(R.drawable.ic_share_white);
                mCommunityInfoTitleTV.setTextColor(getResources().getColor(R.color.white));
                mCommunityInfoStatusBarLL.setBackgroundColor(getResources().getColor(R.color.color_null));
            } else {
                mCommunityInfoNavBarLL.getBackground().mutate().setAlpha(255);
                mCommunityInfoTitleBackImg.setImageResource(R.drawable.ic_back_black);
                mCommunityInfoTitleShareImg.setImageResource(R.drawable.popup_ic_share);
                mCommunityInfoTitleTV.setTextColor(getResources().getColor(R.color.title_bar_txtcolor));
                mCommunityInfoStatusBarLL.setBackgroundColor(getResources().getColor(R.color.checked_tv_color));
            }
        }
    }
    private void showZoomPictureDialog(int position) {
        View myView = CommunityInfoActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_preview_zoom_picture_dialog, null);
        mZoomPictureDialog = new AlertDialog.Builder(CommunityInfoActivity.this).create();
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
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            int width_new = metric.widthPixels;     // 屏幕宽度（像素）
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
    private void showFeedbacksDialog(int type) {
        LayoutInflater factory = LayoutInflater.from(CommunityInfoActivity.this);
        final View textEntryView = factory.inflate(R.layout.activity_fieldinfo_refund_price_popuwindow, null);
        TextView mrefunt_price_detailed = (TextView) textEntryView.findViewById(R.id.refunt_price_detailed);
        ImageButton mrefunt_price_detailed_btn = (ImageButton) textEntryView.findViewById(R.id.fieldinfo_explain_close_imgbtn);
        RelativeLayout mfieldinfo_all_dialog_layout = (RelativeLayout) textEntryView.findViewById(R.id.fieldinfo_all_dialog_layout);
        RelativeLayout mfieldinfo_refunt_price_detailed_layout = (RelativeLayout) textEntryView.findViewById(R.id.fieldinfo_refunt_price_detailed_layout);
        RelativeLayout mfieldinfo_error_recovery_layout = (RelativeLayout) textEntryView.findViewById(R.id.fieldinfo_error_recovery_layout);
        LinearLayout mfieldinfo_error_recovery_top_layout = (LinearLayout) textEntryView.findViewById(R.id.fieldinfo_error_recovery_top_layout);
        TextView merror_correction_cancel_text = (TextView) textEntryView.findViewById(R.id.error_correction_cancel_text);
        mDialogErrorCorrectionTV = (TextView) textEntryView.findViewById(R.id.error_correction_submit_text);
        TextView merror_recovery_resourcesname_text = (TextView) textEntryView.findViewById(R.id.error_recovery_resourcesname_text);
        TextView mrefunt_price_text = (TextView) textEntryView.findViewById(R.id.refunt_price_text);
        final EditText merror_recovery_content_edit = (EditText) textEntryView.findViewById(R.id.error_recovery_content_edit);
        if (type == 0 || type == 2) {
            mrefunt_price_detailed_btn.setVisibility(View.VISIBLE);
            mfieldinfo_refunt_price_detailed_layout.setVisibility(View.VISIBLE);
            mfieldinfo_error_recovery_layout.setVisibility(View.GONE);
            if (type == 2) {
                mrefunt_price_text.setText(getResources().getString(R.string.fieldinfo_integral_reward_tv_str));
                mrefunt_price_detailed.setText(getResources().getString(R.string.fieldinfo_integral_reward_content_str));
            } else if (type == 0) {
                mrefunt_price_text.setText(getResources().getString(R.string.module_fieldinfo_change_explain));
                mrefunt_price_detailed.setText(getResources().getString(R.string.module_fieldinfo_cancle_order_explain));
            }
        } else if (type == 1) {
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            int width = metric.widthPixels - 148;     // 屏幕宽度（像素）
            int height = (width*736)/600;
            RelativeLayout.LayoutParams paramgroups= new RelativeLayout.LayoutParams(width, height+84);
            paramgroups.addRule(RelativeLayout.CENTER_IN_PARENT);
            mfieldinfo_error_recovery_top_layout.setLayoutParams(paramgroups);
            mfieldinfo_error_recovery_layout.setVisibility(View.VISIBLE);
            mfieldinfo_refunt_price_detailed_layout.setVisibility(View.GONE);
            mrefunt_price_detailed_btn.setVisibility(View.GONE);
            if (mCommunityInfoModel.getName() != null && mCommunityInfoModel.getName().length() > 0) {
                merror_recovery_resourcesname_text.setText(mCommunityInfoModel.getName());
            }
        }
        mFeedbacksDialog = new AlertDialog.Builder(this).create();
        Constants.show_dialog(textEntryView,mFeedbacksDialog);
        mrefunt_price_detailed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFeedbacksDialog.isShowing()) {
                    mFeedbacksDialog.dismiss();
                }
            }
        });
        merror_correction_cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFeedbacksDialog.isShowing()) {
                    mFeedbacksDialog.dismiss();
                }
            }
        });
        mDialogErrorCorrectionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (merror_recovery_content_edit.getText().toString().trim().length() > 0) {
                    mDialogErrorCorrectionTV.setEnabled(false);
                    showProgressDialog();
                    mPresenter.releaseFeedbacks(0,mCommunityInfoModel.getId(),merror_recovery_content_edit.getText().toString());
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.fieldinfo_error_recovery_content_remindtext));
                    return;
                }
            }
        });
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    final View myView = CommunityInfoActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                    mShareDialog = new AlertDialog.Builder(CommunityInfoActivity.this).create();
                    if (mShareDialog!= null && mShareDialog.isShowing()) {
                        mShareDialog.dismiss();
                    }
                    Constants constants = new Constants(CommunityInfoActivity.this,
                            ShareIconStr);
                    constants.shareWXMiniPopupWindow(CommunityInfoActivity.this,myView,mShareDialog,mIWXAPI,shareWXlinkurl,
                            ShareTitleStr,
                            SharedescriptionStr, ShareBitmap, sharewxMiniShareLinkUrl,miniShareBitmap,mSharePYQTitleStr);
                    break;
                case 1:
                    Bundle bundle = msg.getData();
                    if (bundle.get("distance") != null &&
                            bundle.get("distance").toString().length() > 0) {
                        mCommunityDistanceLL.setVisibility(View.VISIBLE);
                        mCommunityDistanceTV.setText(bundle.get("distance").toString());
                    }
                    break;
                default:
                    break;
            }
        }

    };
    private void initGetLocation() {
        AndPermission.with(CommunityInfoActivity.this)
                .requestCode(LOACTION_REQUEST_INT)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .callback(listener)
                .start();

    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//        option.setOpenGps(false);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    public void onMyCouponsListSuccess(ArrayList<MyCouponsModel> data) {
        mCanReceiveCouponsLists = data;
        if (mCouponsPW != null && mCouponsPW.isShowing()) {
            if (mCanReceiveCouponsLists != null && mCanReceiveCouponsLists.size() > 0) {
                canReceiveAllLL.setVisibility(View.VISIBLE);
                if (!isPwCanReceive && mCanReceiveCouponsLists.size() > 3) {
                    canReceiveLL.setVisibility(View.VISIBLE);
                    canReceiveCouponsList.clear();
                    for (int i = 0; i < 3; i++) {
                        canReceiveCouponsList.add(mCanReceiveCouponsLists.get(i));
                    }
                    mCanReceiveAdapter.notifyDataSetChanged();
                } else {
                    canReceiveLL.setVisibility(View.GONE);
                    canReceiveCouponsList.clear();
                    canReceiveCouponsList.addAll(mCanReceiveCouponsLists);
                    mCanReceiveAdapter.notifyDataSetChanged();
                }
            } else {
                canReceiveAllLL.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onMyCouponsMoreSuccess(ArrayList<MyCouponsModel> data) {
        mReceivedCouponsLists = data;
        if (mCouponsPW != null && mCouponsPW.isShowing()) {
            if (mReceivedCouponsLists != null && mReceivedCouponsLists.size() > 0) {
                receivedAllLL.setVisibility(View.VISIBLE);
                if (!isPwReceived && mReceivedCouponsLists.size() > 1) {
                    receivedLL.setVisibility(View.VISIBLE);
                    receivedCouponsList.clear();
                    receivedCouponsList.add(mReceivedCouponsLists.get(0));
                    mReceivedAdapter.notifyDataSetChanged();
                } else {
                    receivedLL.setVisibility(View.GONE);
                    receivedCouponsList.clear();
                    receivedCouponsList.addAll(mReceivedCouponsLists);
                    mReceivedAdapter.notifyDataSetChanged();
                }
            } else {
                receivedAllLL.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onMyCouponsListFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        isReceiveClick = false;
        checkAccess_new(error);
    }

    @Override
    public void onMyCouponsListMoreFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
    }

    @Override
    public void onExchangeCouponsSuccess(Response response) {
        if (response.code == 1) {
            if (isReceiveClick) {
                MessageUtils.showToast(getResources().getString(R.string.module_fieldinfo_receive_coupons_success));
                isReceiveClick = false;
            } else {
                MessageUtils.showToast(getResources().getString(R.string.response_success));
            }
            //未领取
            mCouponsMvpPresenter.getCommunityCoupons(mCommunityId,1,100,1);
            //已领取
            mCouponsMvpPresenter.getCommunityCoupons(mCommunityId,2,1,1);
        } else {
            MessageUtils.showToast(response.msg);
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                double mLocationLat = location.getLatitude();
                double mLocationLng = location.getLongitude();
                if (mCommunityInfoModel.getLat() != null &&
                        mCommunityInfoModel.getLng() != null &&
                        mLocationLat > 0 &&
                        mLocationLng > 0) {
                    int distance = Integer.parseInt(Constants.getorderdoublepricestring(DistanceUtil.getDistance(new LatLng(mLocationLat,mLocationLng)
                            , new LatLng(mCommunityInfoModel.getLat(),
                                    mCommunityInfoModel.getLng())),1));
                    String distanceStr = Constants.getpricestring(String.valueOf(distance),0.001) + "km";
                    Message msg = new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("distance",distanceStr);//bundle中也可以放序列化或包裹化的类对象数据
                    msg = mHandler.obtainMessage();//每发送一次都要重新获取
                    msg.what = 1;
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
            }
            mLocationClient.stop();
            mLocationClient = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DEMAND_RESULTCODE_LOGIN) {
            if (LoginManager.isLogin()) {
                startDemandActivity();
            }
        } else if (requestCode == COUPONS_REQUESTCODE) {
            if (LoginManager.isLogin()) {
                if (mCommunityId > 0) {
                    //未领取
                    mCouponsMvpPresenter.getCommunityCoupons(mCommunityId,1,100,1);
                    ///已领取
                    mCouponsMvpPresenter.getCommunityCoupons(mCommunityId,2,1,1);
                }
            }
        }
        switch (resultCode) {
            case DEMAND_RESULTCODE:
                showDemandSuccessDialog();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showDemandSuccessDialog() {
        if (mDemandSuccessDialog == null || !mDemandSuccessDialog.isShowing()) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.dialog_long_btn_tv:
                            mDemandSuccessDialog.dismiss();
                            break;
                        default:
                            break;
                    }
                }
            };
            CustomDialog.Builder builder = new CustomDialog.Builder(CommunityInfoActivity.this);
            mDemandSuccessDialog = builder
                    .cancelTouchout(true)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .showView(R.id.dialog_long_btn_tv,View.VISIBLE)
                    .showView(R.id.linhuiba_logo_imgv,View.GONE)
                    .showView(R.id.dialog_title_msg_tv,View.VISIBLE)
                    .showView(R.id.dialog_remind_msg_tv,View.VISIBLE)
                    .setText(R.id.dialog_remind_msg_tv,
                            getResources().getString(R.string.module_submit_demand_success_first_msg))
                    .setText(R.id.dialog_title_msg_tv,
                            getResources().getString(R.string.module_submit_demand_success_msg))
                    .addViewOnclick(R.id.dialog_long_btn_tv,uploadListener)
                    .setText(R.id.dialog_title_textview,
                            getResources().getString(R.string.module_submit_demand_success_seond_msg) + LoginManager.getServicetime()
                                    + "\n\n" + getResources().getString(R.string.module_submit_demand_success_third_msg) +
                                    mCommunityInfoModel.getService_phone().toString())
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(CommunityInfoActivity.this,mDemandSuccessDialog);
            mDemandSuccessDialog.show();
        }
    }
    //详情优惠券领取
    private void showCouponsPW () {
        isPwCanReceive = false;
        isPwReceived = false;
        View myView = CommunityInfoActivity.this.getLayoutInflater().inflate(R.layout.module_pw_fieldinfo_receive_coupon, null);
        int width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        //通过view 和宽·高，构造PopopWindow
        mCouponsPW = new SupportPopupWindow(myView, width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
        mCouponsPW.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_bg));
        mCouponsPW.getBackground().setAlpha(155);
        //设置焦点为可点击
        mCouponsPW.setFocusable(true);//可以试试设为false的结果
        //将window视图显示在myButton下面
        // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(200);

        mCouponsPW.showAtLocation(CommunityInfoActivity.this.findViewById(R.id.community_info_all_ll), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        myView.startAnimation(animation);
        mCouponsPW.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mCouponsPW.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mCouponsPW.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCouponsPW.isShowing()) {
                    mCouponsPW.dismiss();
                }
            }
        });
        RecyclerView canReceiveRV = (RecyclerView) myView.findViewById(R.id.fieldinfo_coupons_can_receive_rv);
        RecyclerView receivedRV = (RecyclerView) myView.findViewById(R.id.fieldinfo_coupons_received_rv);
        ImageButton closeBtn = (ImageButton) myView.findViewById(R.id.fieldinfo_coupons_close_btnImgv);
        LinearLayout lookMyCoupons = (LinearLayout) myView.findViewById(R.id.fieldinfo_coupons_look_ll);
        canReceiveLL = (LinearLayout) myView.findViewById(R.id.fieldinfo_coupons_can_receive_show_all_ll);
        final TextView canReceiveTV = (TextView) myView.findViewById(R.id.fieldinfo_coupons_can_receive_show_all_tv);
        receivedLL = (LinearLayout) myView.findViewById(R.id.fieldinfo_coupons_received_show_all_ll);
        final TextView receivedTV = (TextView) myView.findViewById(R.id.fieldinfo_coupons_received_show_all_tv);
        canReceiveAllLL = (LinearLayout) myView.findViewById(R.id.fieldinfo_coupons_can_receive_ll);
        receivedAllLL = (LinearLayout) myView.findViewById(R.id.fieldinfo_coupons_received_ll);
        receivedLL.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {

            }
        });

        closeBtn.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (mCouponsPW.isShowing()) {
                    mCouponsPW.dismiss();
                }
            }
        });
        lookMyCoupons.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Intent myCouponsIntent = new Intent(CommunityInfoActivity.this,MyCouponsActivity.class);
                startActivity(myCouponsIntent);
            }
        });
        if (canReceiveCouponsList != null) {
            canReceiveCouponsList.clear();
        }
        if (receivedCouponsList != null) {
            receivedCouponsList.clear();
        }
        if (mCanReceiveCouponsLists != null && mCanReceiveCouponsLists.size() > 0) {
            if (mCanReceiveCouponsLists.size() > 3) {
                canReceiveLL.setVisibility(View.VISIBLE);
                for (int i = 0; i < 3; i++) {
                    canReceiveCouponsList.add(mCanReceiveCouponsLists.get(i));
                }
            } else {
                canReceiveLL.setVisibility(View.GONE);
                canReceiveCouponsList.addAll(mCanReceiveCouponsLists);
            }
            canReceiveAllLL.setVisibility(View.VISIBLE);

        } else {
            canReceiveCouponsList = new ArrayList<>();
            canReceiveAllLL.setVisibility(View.GONE);
        }
        mCanReceiveAdapter = new MyCouponsAdapter(R.layout.module_recycle_item_coupons,canReceiveCouponsList,CommunityInfoActivity.this,
                2,CommunityInfoActivity.this,1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        canReceiveRV.setLayoutManager(linearLayoutManager);
        canReceiveRV.setAdapter(mCanReceiveAdapter);
        mCanReceiveAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!LoginManager.isLogin()) {
                    Intent loginIntent = new Intent(CommunityInfoActivity.this,LoginActivity.class);
                    startActivity(loginIntent);
                }
            }
        });
        canReceiveRV.setNestedScrollingEnabled(false);
        canReceiveLL.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (isPwCanReceive) {
                    isPwCanReceive = false;
                    canReceiveTV.setText(getResources().getString(R.string.module_coupons_fieldinfo_look_all));
                    canReceiveTV.setCompoundDrawables(null, null, mShowAllDownDrawable, null);
                    canReceiveCouponsList.clear();
                    for (int i = 0; i < 3; i++) {
                        canReceiveCouponsList.add(mCanReceiveCouponsLists.get(i));
                    }
                    mCanReceiveAdapter.notifyDataSetChanged();
                } else {
                    isPwCanReceive = true;
                    canReceiveTV.setCompoundDrawables(null, null, mShowAllUpDrawable, null);
                    canReceiveTV.setText(getResources().getString(R.string.module_coupons_fieldinfo_look_small));
                    canReceiveCouponsList.clear();
                    canReceiveCouponsList.addAll(mCanReceiveCouponsLists);
                    mCanReceiveAdapter.notifyDataSetChanged();
                }
            }
        });
        if (mReceivedCouponsLists != null && mReceivedCouponsLists.size() > 0) {
            receivedAllLL.setVisibility(View.VISIBLE);
            if (mReceivedCouponsLists.size() > 1) {
                receivedLL.setVisibility(View.VISIBLE);
                receivedCouponsList.add(mReceivedCouponsLists.get(0));
            } else {
                receivedLL.setVisibility(View.GONE);
                receivedCouponsList.addAll(mReceivedCouponsLists);
            }
        } else {
            receivedAllLL.setVisibility(View.VISIBLE);
            receivedLL.setVisibility(View.GONE);
            receivedCouponsList = new ArrayList<>();
        }
        //已领取
        mReceivedAdapter = new MyCouponsAdapter(R.layout.module_recycle_item_coupons,receivedCouponsList,CommunityInfoActivity.this,
                2,CommunityInfoActivity.this,2);
        LinearLayoutManager llManager = new LinearLayoutManager(this);
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        receivedRV.setLayoutManager(llManager);
        receivedRV.setAdapter(mReceivedAdapter);
        mReceivedAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        receivedRV.setNestedScrollingEnabled(false);
        receivedLL.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (isPwReceived) {
                    isPwReceived = false;
                    receivedTV.setText(getResources().getString(R.string.module_coupons_fieldinfo_look_all));
                    receivedTV.setCompoundDrawables(null, null, mShowAllDownDrawable, null);
                    receivedCouponsList.clear();
                    receivedCouponsList.add(mReceivedCouponsLists.get(0));
                    mReceivedAdapter.notifyDataSetChanged();
                } else {
                    isPwReceived = true;
                    receivedTV.setCompoundDrawables(null, null, mShowAllUpDrawable, null);
                    receivedTV.setText(getResources().getString(R.string.module_coupons_fieldinfo_look_small));
                    receivedCouponsList.clear();
                    receivedCouponsList.addAll(mReceivedCouponsLists);
                    mReceivedAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    public void showIntegerDialog(int method, String point, final int coupon_id) {
        if (method == 3) {
            if (mIntegralDialog == null || !mIntegralDialog.isShowing()) {
                View.OnClickListener uploadListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.btn_perfect:
                                showProgressDialog();
                                mCouponsMvpPresenter.receiveCoupons(coupon_id,1);
                                mIntegralDialog.dismiss();
                                break;
                            case R.id.btn_cancel:
                                mIntegralDialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                };
                CustomDialog.Builder builder = new CustomDialog.Builder(CommunityInfoActivity.this);
                mIntegralDialog = builder
                        .cancelTouchout(true)
                        .view(R.layout.field_activity_field_orders_success_dialog)
                        .addViewOnclick(R.id.btn_perfect,uploadListener)
                        .addViewOnclick(R.id.btn_cancel,uploadListener)
                        .setText(R.id.dialog_title_textview,
                                getResources().getString(R.string.module_use_coupons_integral_receive_dialog_msg_first) + point
                                        +getResources().getString(R.string.module_use_coupons_integral_receive_dialog_msg_second))
                        .setText(R.id.btn_perfect,
                                getResources().getString(R.string.confirm))
                        .setText(R.id.btn_cancel,
                                getResources().getString(R.string.cancel))
                        .showView(R.id.linhuiba_logo_imgv,View.GONE)
                        .showView(R.id.dialog_title_msg_tv,View.VISIBLE)
                        .build();
                com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(CommunityInfoActivity.this,mIntegralDialog);
                mIntegralDialog.show();
            }
        } else {
            showProgressDialog();
            mCouponsMvpPresenter.receiveCoupons(coupon_id,1);
        }
    }

    // FIXME: 2018/12/19 顾问
    private void showCounselorDialog(int showWxcode) {
        if (mDemandSuccessDialog == null || !mDemandSuccessDialog.isShowing()) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.app_defaylt_dialog_counselor_save_ll:
                            if (mCommunityInfoModel.getService_representative() != null &&
                                    mCommunityInfoModel.getService_representative().getQrcode() != null &&
                                    mCommunityInfoModel.getService_representative().getQrcode().length() > 0) {
                                LinearLayout counselorLL = (LinearLayout) mDemandSuccessDialog.getView().findViewById(R.id.app_defaylt_dialog_counselor_ll);
                                LinearLayout counselorWXLL = (LinearLayout) mDemandSuccessDialog.getView().findViewById(R.id.fieldinfo_counselor_save_wx_code_ll);
                                ImageView counselorWXCodeImgv = (ImageView) mDemandSuccessDialog.getView().findViewById(R.id.fieldinfo_counselor_save_wx_code_imgv);
                                counselorLL.setVisibility(View.GONE);
                                counselorWXLL.setVisibility(View.VISIBLE);
                                Picasso.with(CommunityInfoActivity.this).load(mCommunityInfoModel.getService_representative().getQrcode()
                                        + com.linhuiba.linhuipublic.config.Config.Linhui_Min_Watermark).resize(
                                        mDisplayMetrics.widthPixels - com.linhuiba.linhuifield.connector.Constants.Dp2Px(CommunityInfoActivity.this,164),
                                        mDisplayMetrics.widthPixels - com.linhuiba.linhuifield.connector.Constants.Dp2Px(CommunityInfoActivity.this,164)).into(counselorWXCodeImgv);
                            } else {
                                MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                            }
                            break;
                        case R.id.app_defaylt_dialog_counselor_call_ll:
                            AndPermission.with(CommunityInfoActivity.this)
                                    .requestCode(CALL_PHONE_CODE)
                                    .permission(
                                            Manifest.permission.CALL_PHONE,
                                            Manifest.permission.READ_PHONE_STATE)
                                    .callback(listener)
                                    .start();
                            mDemandSuccessDialog.dismiss();
                            break;
                        case R.id.app_defaylt_close_img_btn:
                            mDemandSuccessDialog.dismiss();
                            break;
                        case R.id.fieldinfo_counselor_save_wx_code_btn_ll:
                            showProgressDialog();
                            new Thread(){
                                public void run(){
                                    com.linhuiba.linhuifield.connector.Constants.saveToSystemGallery(CommunityInfoActivity.this,
                                            "https://img.linhuiba.com/FumFJpAq1EHu5J_27usq2wIAKcLI-linhuiba_watermark?v=1");// FIXME: 2018/12/19 测试url
                                    mHandler.sendEmptyMessage(2);
                                }
                            }.start();
                            break;
                        default:
                            break;
                    }
                }
            };
            String name = "";
            String call = "";
            String profile = "";
            String imgUrl = com.linhuiba.linhuipublic.config.Config.LINHUIBA_LOGO_URL;// FIXME: 2018/12/19 测试数据
            String qrcodeUrl = com.linhuiba.linhuipublic.config.Config.LINHUIBA_LOGO_URL;// FIXME: 2018/12/19 测试数据
            if (mCommunityInfoModel.getService_representative().getName() != null) {
                name = mCommunityInfoModel.getService_representative().getName();
            }
            if (mCommunityInfoModel.getService_representative().getTel() != null) {
                call = mCommunityInfoModel.getService_representative().getTel();
            }
            if (mCommunityInfoModel.getService_representative().getProfile() != null) {
                profile = mCommunityInfoModel.getService_representative().getProfile();
            }
            if (mCommunityInfoModel.getService_representative().getAvatar() != null) {
                imgUrl = mCommunityInfoModel.getService_representative().getAvatar();
            }
            if (mCommunityInfoModel.getService_representative().getQrcode() != null) {
                qrcodeUrl = mCommunityInfoModel.getService_representative().getQrcode();
            }

            CustomDialog.Builder builder = new CustomDialog.Builder(CommunityInfoActivity.this);
            mDemandSuccessDialog = builder
                    .cancelTouchout(true)
                    .view(R.layout.activity_fieldinfo_refund_price_popuwindow)
                    .addViewOnclick(R.id.app_defaylt_dialog_counselor_save_ll,uploadListener)
                    .addViewOnclick(R.id.app_defaylt_dialog_counselor_call_ll,uploadListener)
                    .addViewOnclick(R.id.app_defaylt_close_img_btn,uploadListener)
                    .addViewOnclick(R.id.fieldinfo_counselor_save_wx_code_btn_ll,uploadListener)
                    .setText(R.id.fieldinfo_counselor_name_tv,name)// FIXME: 2018/12/19 测试数据
                    .setText(R.id.app_defaylt_dialog_counselor_call_tv,
                            getResources().getString(R.string.module_fieldinfo_counselor_call) +call)// FIXME: 2018/12/19 测试数据
                    .setText(R.id.fieldinfo_counselor_description_tv,profile)
                    .setOvalImgvUrl(CommunityInfoActivity.this,R.id.fieldinfo_counselor_imgv,imgUrl,// FIXME: 2018/12/19 测试数据
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(CommunityInfoActivity.this,80),
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(CommunityInfoActivity.this,80))
                    .setImgvUrl(CommunityInfoActivity.this,R.id.app_defaylt_dialog_counselor_qrcode_imgv,qrcodeUrl,
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(CommunityInfoActivity.this,40),
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(CommunityInfoActivity.this,40))
                    .setImgvUrl(CommunityInfoActivity.this,R.id.app_defaylt_dialog_counselor_qrcode_imgv,imgUrl,
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(CommunityInfoActivity.this,40),
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(CommunityInfoActivity.this,40))
                    .showView(R.id.app_defaylt_dialog_ll,View.VISIBLE)
                    .showView(R.id.app_defaylt_dialog_counselor_ll,View.VISIBLE)
                    .showView(R.id.app_defaylt_close_img_btn,View.VISIBLE)
                    .showView(R.id.fieldinfo_counselor_contact_ll,View.GONE)
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(CommunityInfoActivity.this,mDemandSuccessDialog);
            mDemandSuccessDialog.show();
            if (showWxcode == 1) {
                LinearLayout counselorLL = (LinearLayout) mDemandSuccessDialog.getView().findViewById(R.id.app_defaylt_dialog_counselor_ll);
                LinearLayout counselorWXLL = (LinearLayout) mDemandSuccessDialog.getView().findViewById(R.id.fieldinfo_counselor_save_wx_code_ll);
                ImageView counselorWXCodeImgv = (ImageView) mDemandSuccessDialog.getView().findViewById(R.id.fieldinfo_counselor_save_wx_code_imgv);
                counselorLL.setVisibility(View.GONE);
                counselorWXLL.setVisibility(View.VISIBLE);
                Picasso.with(CommunityInfoActivity.this).load(mCommunityInfoModel.getService_representative().getQrcode()
                        + com.linhuiba.linhuipublic.config.Config.Linhui_Min_Watermark).resize(
                        mDisplayMetrics.widthPixels - com.linhuiba.linhuifield.connector.Constants.Dp2Px(CommunityInfoActivity.this,164),
                        mDisplayMetrics.widthPixels - com.linhuiba.linhuifield.connector.Constants.Dp2Px(CommunityInfoActivity.this,164)).into(counselorWXCodeImgv);
            }
        }
    }
}
