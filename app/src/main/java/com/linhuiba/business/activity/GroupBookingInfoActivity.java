package com.linhuiba.business.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.FieldinfoOtherResourcesAdapter;
import com.linhuiba.business.adapter.Fieldinfo_ReviewAdapter;
import com.linhuiba.business.adapter.GlideImageLoader;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.model.CommunityInfoModel;
import com.linhuiba.business.model.FieldInfoModel;
import com.linhuiba.business.model.GroupBookingInfoModel;
import com.linhuiba.business.model.GroupBookingListModel;
import com.linhuiba.business.model.PhyResInfoModel;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.business.model.ReviewModel;
import com.linhuiba.business.mvpmodel.LoginMvpModel;
import com.linhuiba.business.mvppresenter.FieldinfoMvpPresenter;
import com.linhuiba.business.mvpview.FieldinfoMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.HorizontalListView;
import com.linhuiba.business.view.MyListView;
import com.linhuiba.linhuifield.fieldview.Field_MyScrollview;
import com.linhuiba.linhuifield.fieldview.ZoomImageView;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.squareup.picasso.Picasso;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
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
import cn.magicwindow.mlink.annotation.MLinkRouter;
import okhttp3.internal.http2.Header;

/**
 * Created by Administrator on 2017/9/28.
 */
@MLinkRouter(keys={"groupBuyDetail"})
public class GroupBookingInfoActivity extends BaseMvpActivity implements Field_MyScrollview.OnScrollListener,
        Field_AddFieldChoosePictureCallBack.FieldreviewCall, FieldinfoMvpView {
    @InjectView(R.id.fieldinfo_scroll_ll) LinearLayout mFieldInfoScrollLL;
    @InjectView(R.id.fieldinfo_banner)
    Banner mFieldInfoBanner;
    @InjectView(R.id.groupbooking_info_ll) LinearLayout mGroupBookingInfoLL;
    @InjectView(R.id.fieldinfo_information_classify_layout) LinearLayout mfieldinfo_information_classify_layout;
    @InjectView(R.id.evaluation_linearlayout)
    LinearLayout mevaluation_layout;
    @InjectView(R.id.fieldinfo_navbar_titile) LinearLayout mFieldInfoNavbarTtileLL;
    @InjectView(R.id.fieldinfo_status_bar_ll) LinearLayout mFieldInfoStatusBarLL;
    @InjectView(R.id.fieldinfo_second_level_ll) LinearLayout mFieldInfoSecondLevelLL;
    @InjectView(R.id.groupbooking_info_scrollview) Field_MyScrollview mGroupBookingInfoSV;
    @InjectView(R.id.txt_fieldtitle)
    TextView mResCommunityTypeTV;
    @InjectView(R.id.groupbooking_info_origin_price)
    TextView mGroupInfoOriginPriceTV;
    @InjectView(R.id.groupbooking_info_actual_price)
    TextView mGroupInfoActualPriceTV;
    @InjectView(R.id.group_info_purchase_people_tv)
    TextView mGroupInfoPurchaseNumTV;
    @InjectView(R.id.group_info_purchase_num_ll)
    LinearLayout mGroupInfoPurchaseNumLL;
    @InjectView(R.id.groupinfo_countdown_day_tv)
    TextView mGroupInfoCountDownDayTV;
    @InjectView(R.id.groupinfo_countdown_hour_tv)
    TextView mGroupInfoCountDownHourTV;
    @InjectView(R.id.groupinfo_countdown_minute_tv)
    TextView mGroupInfoCountDownMinuteTV;
    @InjectView(R.id.groupinfo_countdown_second_tv)
    TextView mGroupInfoCountDownSecondTV;
    @InjectView(R.id.txt_field_name)
    TextView mGroupInfoResNameTV;
    @InjectView(R.id.groupinfo_number_of_people_tv)
    TextView mGroupInfoNumberOfPeopleTV;
    @InjectView(R.id.txt_transaction_success)
    TextView mGroupInfoTransactionPeopleTV;
    @InjectView(R.id.groupinfo_industry_tv)
    TextView mGroupInfoIndustryTV;

    @InjectView(R.id.groupinfo_age_level_tv)
    TextView mGroupInfoAgeLevelTV;
    @InjectView(R.id.groupinfo_consumption_level_tv)
    TextView mGroupInfoConsumptionLevelTV;
    @InjectView(R.id.groupinfo_age_label_ll)
    LinearLayout mGroupInfoAgeLabelLL;
    @InjectView(R.id.txt_address)
    TextView mGroupInfoAddressTV;
    @InjectView(R.id.fieldinfo_location_indoor_tv)
    TextView mGroupinfoLocationIndoorTV;
    @InjectView(R.id.fieldinfo_location_specific_tv)
    TextView mGroupinfoLocationTV;
    @InjectView(R.id.groupinfo_max_people_tv)
    TextView mGroupInfoMaxPeopleTV;
    @InjectView(R.id.groupinfo_min_people_tv)
    TextView mGroupInfoMinPeopleTV;
    @InjectView(R.id.groupinfo_activity_time_tv)
    TextView mGroupInfoActivityTimeTV;
    @InjectView(R.id.groupinfo_field_area_tv)
    TextView mGroupInfoFieldAreaTV;
    @InjectView(R.id.groupinfo_support_category_tv)
    TextView mGroupInfoSupportCategoryTV;
    @InjectView(R.id.groupinfo_support_category_ll)
    LinearLayout mGroupInfoSupportCategoryLL;
    @InjectView(R.id.groupinfo_contraband_category_tv)
    TextView mGroupInfoContrabandCategoryTV;
    @InjectView(R.id.groupinfo_contraband_category_ll)
    LinearLayout mGroupInfoContrabandCategoryLL;

    @InjectView(R.id.groupinfo_haspower_ll)
    LinearLayout mGroupInfoHasPowerLL;
    @InjectView(R.id.groupinfo_haschair_ll)
    LinearLayout mGroupInfoHasChairLL;
    @InjectView(R.id.groupinfo_hastent_ll)
    LinearLayout mGroupInfoHasTentLL;
    @InjectView(R.id.groupinfo_hasleaflet_ll)
    LinearLayout mGroupInfoHasLeafletLL;
    @InjectView(R.id.groupinfo_hasgoodsHelp_ll)
    LinearLayout mGroupInfoHasGoodsHelpLL;
    @InjectView(R.id.groupinfo_haspower_tmp_ll)
    LinearLayout mGroupInfoHasPowerTmpLL;
    @InjectView(R.id.groupinfo_haschair_tmp_ll)
    LinearLayout mGroupInfoHasChairTmpLL;
    @InjectView(R.id.groupinfo_hastent_tmp_ll)
    LinearLayout mGroupInfoHasTentTmpLL;
    @InjectView(R.id.groupinfo_hasleaflet_tmp_ll)
    LinearLayout mGroupInfoHasLeafletTmpLL;
    @InjectView(R.id.groupinfo_hasgoodsHelp_tmp_ll)
    LinearLayout mGroupInfoHasGoodsHelpTmpLL;

    @InjectView(R.id.groupinfo_stall_time)
    TextView mGroupInfoResStallTimeTV;
    @InjectView(R.id.groupinfo_total_area)
    TextView mGroupInfoResTotalAreaTV;
    @InjectView(R.id.groupinfo_property_requirement)
    TextView mGroupInfoResPropertyRequirementTV;
    @InjectView(R.id.groupinfo_contraband)
    TextView mGroupInfoResContrabandTV;
    @InjectView(R.id.groupinfo_peak_time)
    TextView mGroupInfoResPeakTimeTV;
    @InjectView(R.id.groupinfo_gender_ratio)
    TextView mGroupInfoGenderRatioTimeTV;
    @InjectView(R.id.community_name_text)
    TextView mGroupInfoCommunityNameTV;
    @InjectView(R.id.community_name_layout) LinearLayout mcommunity_name_layout;
    @InjectView(R.id.community_address_text)
    TextView mGroupInfoCommunityAddressTV;
    @InjectView(R.id.community_address_layout) LinearLayout mcommunity_address_layout;
    @InjectView(R.id.community_type_text)
    TextView mGroupInfoCommunityTypeTV;
    @InjectView(R.id.community_type_layout) LinearLayout mcommunity_type_layout;
    @InjectView(R.id.community_buldyear_text)
    TextView mGroupInfoCommunityBuldYearTV;
    @InjectView(R.id.community_buldyear_layout) LinearLayout mGroupInfoCommunityBuldYearLL;

    @InjectView(R.id.txt_facilities) TextView mCommunityFacilitiesTV;//配套设施
    @InjectView(R.id.txt_facilities_layout) LinearLayout mCommunityFacilitiesLL;//配套设施
    @InjectView(R.id.fieldinfo_community_occupancy_rate_ll)
    LinearLayout mCommunityOccupancyRateLL;
    @InjectView(R.id.fieldinfo_community_occupancy_rate_text)
    TextView mCommunityOccupancyRateTV;
    @InjectView(R.id.fieldinfo_community_property_costs_ll)
    LinearLayout mCommunityPropertyCostsLL;
    @InjectView(R.id.fieldinfo_community_property_costs_text)
    TextView mCommunityPropertyCostsTV;
    @InjectView(R.id.fieldinfo_community_rent_ll)
    LinearLayout mCommunityRentLL;
    @InjectView(R.id.fieldinfo_community_rent_text)
    TextView mCommunityRentTV;
    @InjectView(R.id.fieldinfo_community_number_of_enterprises_ll)
    LinearLayout mCommunityEnterprisesLL;
    @InjectView(R.id.fieldinfo_community_number_of_enterprises_text)
    TextView mCommunityEnterprisesTV;

    @InjectView(R.id.community_area_text)
    TextView mGroupInfoCommunityAreaTV;
    @InjectView(R.id.community_area_layout) LinearLayout mcommunity_area_layout;
    @InjectView(R.id.resource_communityinfo_layout) LinearLayout mresource_communityinfo_layout;
    @InjectView(R.id.resource_fieldinfo_layout) LinearLayout mresource_fieldinfo_layout;
    @InjectView(R.id.groupinfo_other_res_layout) LinearLayout mFieldinfoOtherResLL;
    @InjectView(R.id.groupinfo_other_res_gridlistview) HorizontalListView mFieldinfoOtherResHLV;
    @InjectView(R.id.groupinfo_review_listview) MyListView mreview_listview;
    @InjectView(R.id.groupinfo_review_number_txt) TextView mGroupInfoReviewNumberTV;
    @InjectView(R.id.groupbooking_shop_layout) LinearLayout mGroupInfoPayLL;
    @InjectView(R.id.groupinfo_pay_price_tv) TextView mGroupInfoPayPriceTV;
    @InjectView(R.id.groupinfo_pay_tv) TextView mGroupInfoPayTV;
    @InjectView(R.id.groupinfo_stall_area) LinearLayout mGroupInfoStallAreaLL;
    @InjectView(R.id.fieldinfo_view_visibility) RelativeLayout mGroupInfoInformationRL;
    @InjectView(R.id.groupinfo_paid_rl) RelativeLayout mGroupInfoPaidRL;
    @InjectView(R.id.groupinfo_no_data_ll)
    RelativeLayout mGroupinfoNoDataLL;
    @InjectView(R.id.fieldinfo_show_all_tv)
    TextView mFieldinfoShowAllTV;
    @InjectView(R.id.txt_number_of_people_facade_layout) LinearLayout mtxt_number_of_people_facade_layout;
    @InjectView(R.id.txt_number_of_people_facade) TextView mtxt_number_of_people_facade;
    @InjectView(R.id.txt_description_tablerow)
    LinearLayout mtxt_description_ll;
    @InjectView(R.id.txt_description)
    TextView mtxt_description;
    @InjectView(R.id.txt_park)  TextView mtxt_park;
    @InjectView(R.id.txt_park_layout)  LinearLayout mtxt_park_layout;
    @InjectView(R.id.txt_participation)  TextView mtxt_participation;
    @InjectView(R.id.participation)  LinearLayout mparticipation;
    @InjectView(R.id.txt_age_group)  TextView mtxt_age_group;
    @InjectView(R.id.age_group)  LinearLayout mage_group;
    @InjectView(R.id.txt_consumption_level) TextView mtxt_consumption_level;
    @InjectView(R.id.txt_consumption_level_ll) LinearLayout mtxt_consumption_level_ll;
    @InjectView(R.id.community_grade_text) TextView mcommunity_grade_text;
    @InjectView(R.id.community_grade_layout) LinearLayout mcommunity_grade_layout;
    @InjectView(R.id.community_business_district_text) TextView mcommunity_business_district_text;
    @InjectView(R.id.community_business_district_layout) LinearLayout mcommunity_business_district_layout;
    @InjectView(R.id.fieldinfo_community_number_of_households_text) TextView mfieldinfo_community_number_of_households_text;
    @InjectView(R.id.fieldinfo_community_number_of_households_ll) LinearLayout mfieldinfo_community_number_of_households_ll;
    @InjectView(R.id.fieldinfo_community_house_price_text) TextView mfieldinfo_community_house_price_text;
    @InjectView(R.id.fieldinfo_community_house_price_ll) LinearLayout mfieldinfo_community_house_price_ll;
    @InjectView(R.id.txt_restaurant)  TextView mtxt_restaurant;
    @InjectView(R.id.txt_restaurant_layout)  LinearLayout mtxt_restaurant_layout;
    @InjectView(R.id.community_total_number_of_people_text) TextView mcommunity_total_number_of_people_text;
    @InjectView(R.id.community_total_number_of_people_layout) LinearLayout mcommunity_total_number_of_people_layout;
    @InjectView(R.id.txt_enterprises_type) TextView mtxt_enterprises_type;
    @InjectView(R.id.txt_enterprises_type_layout) LinearLayout mtxt_enterprises_type_layout;
    @InjectView(R.id.txt_supermarket_type) TextView mtxt_supermarket_type;
    @InjectView(R.id.txt_supermarket_type_layout) LinearLayout mtxt_supermarket_type_layout;
    @InjectView(R.id.txt_shopping_mall_type) TextView mtxt_shopping_mall_type;
    @InjectView(R.id.txt_shopping_mall_type_layout) LinearLayout mtxt_shopping_mall_type_layout;
    @InjectView(R.id.number_of_commercial_layout) LinearLayout mnumber_of_commercial_layout;
    @InjectView(R.id.number_of_commercial_textview) TextView mnumber_of_commercial_textview;
    @InjectView(R.id.housing_attribute_layout) LinearLayout mhousing_attribute_layout;
    @InjectView(R.id.housing_attribute_textview) TextView mhousing_attribute_textview;
    @InjectView(R.id.averagea_ticket_price_layout) LinearLayout maveragea_ticket_price_layout;
    @InjectView(R.id.averagea_ticket_price_textivew) TextView maveragea_ticket_price_textivew;
    @InjectView(R.id.group_not_provided_service_ll) LinearLayout mGroupNoServiceLL;
    @InjectView(R.id.fieldinfo_dolocation_ll) LinearLayout mFieldinfoDoLocationLL;
    @InjectView(R.id.fieldinfo_dolocation_tv) TextView mFieldinfoDoLocationTV;
    @InjectView(R.id.fieldinfo_sales_volume_ll) LinearLayout mFieldinfoSaleVolumLL;
    @InjectView(R.id.fieldinfo_sales_volume_tv) TextView mFieldinfoSaleVolumTV;
    @InjectView(R.id.fieldinfo_people_ll) LinearLayout mFieldinfoPeopleLL;
    @InjectView(R.id.fieldinfo_people_tv) TextView mFieldinfoPeopleTV;
    private TextView mCorrectionSubmitErrorTV;//纠错奖励提交按钮
    private GlideImageLoader mFieldinfoImageLoader;
    private DisplayMetrics mDisplayMetrics;
    private RelativeLayout maction_layout_top;//taitlebar的layout
    private ImageView mFieldInfoTitleBackImg;
    private TextView mFieldInfoTitleTV;
    private RelativeLayout mFieldInfoTitleRL;
    private TextView[] mSecondNavTextList = new TextView[4];
    private ImageView[] mSecondNavImgList = new ImageView[4];
    public boolean isGetSecondNavHeight;//是否重新获取二级导航栏tab功能的高度
    public int mFieldinfoSpecificationInt;
    public int mFieldinfoCommunityInt;
    public int mFieldifnoReviewInt;
    private String mResId;
    private GroupBookingInfoModel mGroupBookingInfoModel;
    private ArrayList<String> mPicList = new ArrayList<>();
    private Dialog mZoomPictureDialog;//详情页预览大图dialog
    private boolean mIsRefreshZoomImageview = true;//详情页变了是否重新获取预览大图
    private List<ImageView> mImageViewList;//预览大图的view
    private Dialog mRemindDialog;
    private double  mDeposit;
    private ArrayList<ReviewModel> mReviewList;//评价的list
    private Fieldinfo_ReviewAdapter mFieldEvaluationAdapter;//场地评价的adapter
    private ArrayList<ResourceSearchItemModel> mFieldInfoOtherDataList = new ArrayList<>();
    private FieldinfoOtherResourcesAdapter fieldinfoRecommendResources_adapter;
    private Dialog mReviewZoomPictureDialog;
    private List<ImageView> mDialogImageViewList = new ArrayList<>();
    private boolean mDislogIsRefreshZoomImg = true;
    private int mImageListSize;//图片数量
    private int mNewPosition;//显示的图片position
    private String ShareTitleStr = "";//分享的标题
    private String mSharePYQTitleStr;
    private String SharedescriptionStr = "";//分享的描述
    private String ShareIconStr ="";//分享的图片的url
    private Bitmap ShareBitmap = null;//分享需要的bitmap
    private String share_linkurl = "";//分享的url
    private Bitmap miniShareBitmap = null;//小程序分享需要的bitmap
    private String sharewxMiniShareLinkUrl = "";//小程序分享的url
    private Dialog mShareDialog;
    private IWXAPI api;
    private String mCityIdStr;
    private boolean isShowAllFieldRes;//判断是否展开所有场地信息
    private Drawable mShowAllUpDrawable;//查看场地信息
    private Drawable mShowAllDownDrawable;//查看场地信息
    private FieldinfoMvpPresenter mGroupPresenter;
    private static final int CREATE_ORDER_REQUESTCODE = 1;
    private int mStatusBarHeight = 70;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupbooking_info);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResources().getString(R.string.groupbooking_info_activity_name_str));
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResources().getString(R.string.groupbooking_info_activity_name_str));
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGroupPresenter != null) {
            mGroupPresenter.detachView();
        }
        if (ShareBitmap != null) {
            ShareBitmap.recycle();
        }
        if (miniShareBitmap != null) {
            miniShareBitmap.recycle();
        }
    }

    private void initView() {
        ButterKnife.inject(this);
        setSteepStatusBar();
        int stausBarHeight = getSteepStatusBarHeight();
        if (stausBarHeight > 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    stausBarHeight);
            mFieldInfoStatusBarLL.setLayoutParams(layoutParams);
            mStatusBarHeight = 44 + com.linhuiba.linhuifield.connector.Constants.Px2Dp(GroupBookingInfoActivity.this,stausBarHeight);
        }
        mGroupPresenter = new FieldinfoMvpPresenter();
        mGroupPresenter.attachView(this);
        mGroupBookingInfoSV.setOnScrollListener(this);
        findViewById(R.id.all_relative_layout).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScroll(mGroupBookingInfoSV.getScrollY());
                System.out.println(mGroupBookingInfoSV.getScrollY());
            }
        });
        mFieldInfoScrollLL.setBackgroundColor(getResources().getColor(R.color.white));
        mFieldinfoImageLoader = new GlideImageLoader(GroupBookingInfoActivity.this,com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT);
        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        //顶部导航栏控件设置
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.groupbooking_info_title_tv_str));
        TitleBarUtils.showBackImg(this, true);
        maction_layout_top = (RelativeLayout)findViewById(R.id.common_title_bar).findViewById(R.id.action_layout_top);
        mFieldInfoTitleBackImg = (ImageView)findViewById(R.id.common_title_bar).findViewById(R.id.back_button_top);
        mFieldInfoTitleTV = (TextView)findViewById(R.id.common_title_bar).findViewById(R.id.title);
        mFieldInfoTitleTV.setTextColor(getResources().getColor(R.color.white));
        mFieldInfoTitleRL = (RelativeLayout)findViewById(R.id.about_title);
        mFieldInfoTitleRL.setBackgroundColor(getResources().getColor(R.color.color_null));
        //二级导航栏的控件设置
        for (int i = 0; i < mSecondNavTextList.length; i++) {
            int id = getResources().getIdentifier("fieldinfo_second_nav_text" + i, "id", getPackageName());
            TextView textView = (TextView) findViewById(id);
            mSecondNavTextList[i]= (textView);
        }
        for (int i = 0; i < mSecondNavImgList.length; i++) {
            int id = getResources().getIdentifier("fieldinfo_second_nav_img" + i, "id", getPackageName());
            ImageView imageView = (ImageView) findViewById(id);
            mSecondNavImgList[i]= (imageView);
        }
        //设置预览图片控件的大小
        int width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        int height = width * com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT /
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH;
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mFieldInfoBanner.getLayoutParams();
        //设置图片显示高度
        linearParams.height = height;
        linearParams.width = width;
        mFieldInfoBanner.setLayoutParams(linearParams);
        Intent mIntent = getIntent();
        if (mIntent.getExtras() != null &&
                mIntent.getExtras().get("ResId") != null) {
            mResId = mIntent.getExtras().get("ResId").toString();
        }
        //魔窗定义resource_id
        if (mIntent.getExtras() != null &&
                mIntent.getExtras().get("resource_id") != null) {
            mResId = mIntent.getExtras().get("resource_id").toString();
        }
        mGroupinfoNoDataLL.setVisibility(View.VISIBLE);
        initData();
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        mShowAllUpDrawable = getResources().getDrawable(R.drawable.ic_more_blue_up);
        mShowAllUpDrawable.setBounds(0, 0, mShowAllUpDrawable.getMinimumWidth(), mShowAllUpDrawable.getMinimumHeight());
        mShowAllDownDrawable = getResources().getDrawable(R.drawable.ic_more_blue);
        mShowAllDownDrawable.setBounds(0, 0, mShowAllDownDrawable.getMinimumWidth(), mShowAllDownDrawable.getMinimumHeight());
    }
    private void initData() {
        if (mResId != null && mResId.length() > 0) {
            //浏览记录
            if (LoginManager.isLogin()) {
                String parameter = "/"+ mResId;
                LoginMvpModel.sendBrowseHistories("group_detail",parameter,LoginManager.getTrackcityid());
            }
            mGroupPresenter.getGroupResInfo(mResId);
        }
    }
    @OnClick({
            R.id.groupbooking_shop_layout,
            R.id.groupinfo_show_all_information_ll,
            R.id.groupinfo_service_ll,
            R.id.groupinfo_share_ll,
            R.id.groupinfo_error_recovery_layout,
            R.id.fieldinfo_second_level_res_info_ll,
            R.id.fieldinfo_second_level_community_ll,
            R.id.fieldinfo_second_level_review_ll,
            R.id.groupinfo_stall_area,
            R.id.fieldinfo_change_explain_ll
    })
    public void GroupBookingOnClick(View view) {
        switch (view.getId()) {
            case R.id.groupbooking_shop_layout:
                if (LoginManager.isLogin()) {
                    if (mGroupBookingInfoModel.getGroup_purchase().getId() != null &&
                            mGroupBookingInfoModel.getGroup_purchase().getId() > 0) {
                        FieldApi.getGroupStatus(MyAsyncHttpClient.MyAsyncHttpClient(),getGroupStatusHandler,
                                String.valueOf(mGroupBookingInfoModel.getGroup_purchase().getId()));
                    }
                } else {
                    Intent intent = new Intent(GroupBookingInfoActivity.this, LoginActivity.class);
                    startActivityForResult(intent,CREATE_ORDER_REQUESTCODE);
                }
                break;
            case R.id.groupinfo_show_all_information_ll:
                isGetSecondNavHeight = true;
                if (isShowAllFieldRes) {
                    isShowAllFieldRes = false;
                    mFieldinfoShowAllTV.setCompoundDrawables(null, null, mShowAllDownDrawable, null);
                    mresource_communityinfo_layout.setVisibility(View.GONE);
                    mresource_fieldinfo_layout.setVisibility(View.VISIBLE);
                    mFieldinfoShowAllTV.setText(getResources().getString(R.string.fieldinfo_show_all_resource_info_str));
                } else {
                    mFieldinfoShowAllTV.setCompoundDrawables(null, null, mShowAllUpDrawable, null);
                    isShowAllFieldRes = true;
                    mresource_communityinfo_layout.setVisibility(View.VISIBLE);
                    mFieldinfoShowAllTV.setText(getResources().getString(R.string.fieldinfo_show_small_resource_info_str));
                }
                break;
            case R.id.groupinfo_share_ll:
                //2017/10/31 拼团分享
                shareGroupInfo();
                break;
            case R.id.groupinfo_service_ll:
                AndPermission.with(GroupBookingInfoActivity.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();
                break;
            case R.id.groupinfo_error_recovery_layout:
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        showRemindDialog(1);
                    }
                },10);
                break;
            case R.id.fieldinfo_second_level_res_info_ll:
                mGroupBookingInfoSV.fullScroll(ScrollView.FOCUS_UP);
                showSecondLevelView(0);
                break;
            case R.id.fieldinfo_second_level_community_ll:
                mGroupBookingInfoSV.smoothScrollTo(0, mFieldinfoCommunityInt);
                showSecondLevelView(2);
                break;
            case R.id.fieldinfo_second_level_review_ll:
                mGroupBookingInfoSV.smoothScrollTo(0, mFieldifnoReviewInt);
                showSecondLevelView(3);
                break;
            case R.id.groupinfo_stall_area:
                if (mGroupBookingInfoModel.getFirst_selling_resource_price() != null &&
                        mGroupBookingInfoModel.getFirst_selling_resource_price().get("size") != null &&
                        mGroupBookingInfoModel.getFirst_selling_resource_price().get("size").length() > 0) {
                    showRemindDialog(2);
                }
                break;
            case R.id.fieldinfo_change_explain_ll:
                showRemindDialog(3);
                break;
            default:
                break;
        }
    }
    @Override
    public void onScroll(int scrollY) {
        int width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        int height = width *
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT /
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH - com.linhuiba.linhuifield.connector.Constants.Dp2Px(this,mStatusBarHeight);
        if (!isGetSecondNavHeight && scrollY > 1 && mFieldinfoSpecificationInt == 0 && mFieldinfoCommunityInt == 0 &&
                mFieldifnoReviewInt == 0) {
            // 2017/9/23 二级导航栏变换操作
            mFieldinfoSpecificationInt = Math.max(scrollY, mGroupBookingInfoLL.getTop()) - com.linhuiba.linhuifield.connector.Constants.Dp2Px(this,112);
            mFieldinfoCommunityInt = Math.max(scrollY, mfieldinfo_information_classify_layout.getTop()) - com.linhuiba.linhuifield.connector.Constants.Dp2Px(this,112);
            mFieldifnoReviewInt = Math.max(scrollY, mevaluation_layout.getTop()) - com.linhuiba.linhuifield.connector.Constants.Dp2Px(this,112);
            if (mFieldinfoCommunityInt > 0 &&
                    mFieldifnoReviewInt > 0) {
                isGetSecondNavHeight = true;
            }
        }
        if(scrollY < 0) {
            mFieldInfoNavbarTtileLL.getBackground().mutate().setAlpha(0);
            mFieldInfoSecondLevelLL.setVisibility(View.GONE);
            mFieldInfoStatusBarLL.setBackgroundColor(getResources().getColor(R.color.color_null));
        } else {
            if (scrollY < height) {
                int progress = (int) (new Float(scrollY) / new Float(height) * 200);
                mFieldInfoNavbarTtileLL.getBackground().mutate().setAlpha(progress);
                mFieldInfoTitleBackImg.setImageResource(R.drawable.nav_ic_back_white);
                mFieldInfoTitleTV.setTextColor(getResources().getColor(R.color.white));
                mFieldInfoSecondLevelLL.setVisibility(View.GONE);
                mFieldInfoStatusBarLL.setBackgroundColor(getResources().getColor(R.color.color_null));
            } else {
                mFieldInfoNavbarTtileLL.getBackground().mutate().setAlpha(255);
                mFieldInfoTitleBackImg.setImageResource(R.drawable.ic_back_black);
                mFieldInfoTitleTV.setTextColor(getResources().getColor(R.color.title_bar_txtcolor));
                mFieldInfoSecondLevelLL.setVisibility(View.VISIBLE);
                mFieldInfoStatusBarLL.setBackgroundColor(getResources().getColor(R.color.checked_tv_color));
            }
//            if (mFieldinfoSpecificationInt > 0 && scrollY < mFieldinfoSpecificationInt) {
//                showSecondLevelView(0);
//            } else
            if (mFieldinfoCommunityInt > 0 && scrollY < mFieldinfoCommunityInt) {
                showSecondLevelView(0);
            } else if (mFieldifnoReviewInt > 0 && mFieldinfoCommunityInt > 0 &&
                    ((scrollY > mFieldinfoCommunityInt && scrollY < mFieldifnoReviewInt) ||
                            scrollY == mFieldinfoCommunityInt)) {
                showSecondLevelView(2);
            } else if (mFieldifnoReviewInt > 0 && (scrollY > mFieldifnoReviewInt || scrollY == mFieldifnoReviewInt)) {
                showSecondLevelView(3);
            }
        }
    }
    private void showSecondLevelView(int checked) {
        for (int i = 0; i < mSecondNavImgList.length; i++) {
            if (i == checked) {
                mSecondNavImgList[i].setVisibility(View.VISIBLE);
                mSecondNavTextList[i].setTextColor(getResources().getColor(R.color.default_bluebg));
            } else {
                mSecondNavTextList[i].setTextColor(getResources().getColor(R.color.register_edit_color));
                mSecondNavImgList[i].setVisibility(View.GONE);
            }
        }
    }
    private LinhuiAsyncHttpResponseHandler mOtherResHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject != null && jsonObject.get("data") != null) {
                    ArrayList<GroupBookingListModel> mHomeRecentActivityList = (ArrayList<GroupBookingListModel>) JSONArray.parseArray(jsonObject.get("data").toString(),GroupBookingListModel.class);
                    if( mHomeRecentActivityList == null ||  mHomeRecentActivityList.isEmpty()) {
                        return;
                    }
                    for (int i = 0; i < mHomeRecentActivityList.size(); i++) {
                        ResourceSearchItemModel resourceSearchItemModel = new ResourceSearchItemModel();
                        resourceSearchItemModel.setName(mHomeRecentActivityList.get(i).getSelling_resource().getPhysical_resource().getName());
                        resourceSearchItemModel.setActivity_start_date(mHomeRecentActivityList.get(i).getActivity_start());
                        resourceSearchItemModel.setDeadline(mHomeRecentActivityList.get(i).getActivity_end());
                        if (mHomeRecentActivityList.get(i).getSelling_resource().getPhysical_resource().getPhysical_resource_first_img() != null &&
                                mHomeRecentActivityList.get(i).getSelling_resource().getPhysical_resource().getPhysical_resource_first_img().get("pic_url") != null) {
                            resourceSearchItemModel.setPic_url(mHomeRecentActivityList.get(i).getSelling_resource().getPhysical_resource().getPhysical_resource_first_img().get("pic_url").toString());
                        }
                        if (mHomeRecentActivityList.get(i).getSelling_resource().getFirst_selling_resource_price() != null &&
                                mHomeRecentActivityList.get(i).getSelling_resource().getFirst_selling_resource_price().get("price") != null &&
                                mHomeRecentActivityList.get(i).getSelling_resource().getFirst_selling_resource_price().get("price").toString().length() > 0) {
                            resourceSearchItemModel.setPrice(mHomeRecentActivityList.get(i).getSelling_resource().getFirst_selling_resource_price().get("price").toString());
                        }
                        resourceSearchItemModel.setNumber_of_group_purchase_now(mHomeRecentActivityList.get(i).getNumber_of_group_purchase_now());
                        resourceSearchItemModel.setResource_id(mHomeRecentActivityList.get(i).getId());
                        mFieldInfoOtherDataList.add(resourceSearchItemModel);
                    }
                    LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            (mDisplayMetrics.widthPixels* 428 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 300 / 428 +
                                    com.linhuiba.linhuifield.connector.Constants.Dp2Px(GroupBookingInfoActivity.this,75));
                    mFieldinfoOtherResHLV.setLayoutParams(mParams);
                    mFieldinfoOtherResLL.setVisibility(View.VISIBLE);
                    fieldinfoRecommendResources_adapter = new FieldinfoOtherResourcesAdapter(GroupBookingInfoActivity.this,GroupBookingInfoActivity.this,mFieldInfoOtherDataList,1);
                    mFieldinfoOtherResHLV.setAdapter(fieldinfoRecommendResources_adapter);
                    mFieldinfoOtherResHLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent fieldinfo = new Intent(GroupBookingInfoActivity.this, GroupBookingInfoActivity.class);
                            fieldinfo.putExtra("ResId", String.valueOf(mFieldInfoOtherDataList.get(position).getResource_id()));
                            startActivity(fieldinfo);
                        }
                    });
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {

        }
    };
    private void showZoomPictureDialog(int position) {
        View myView = GroupBookingInfoActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_preview_zoom_picture_dialog, null);
        mZoomPictureDialog = new AlertDialog.Builder(GroupBookingInfoActivity.this).create();
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
    private void addCountDownTimer(long millisInFuture, final TextView dayTV,final TextView hourTV,
                                   final TextView minuteTV,final TextView secondTV) {

        class TimeCount extends CountDownTimer {
            public TimeCount(long millisInFuture, long countDownInterval) {
                super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            }

            @Override
            public void onFinish() {// 计时完毕时触发

                mGroupInfoPayLL.setEnabled(false);
                mGroupInfoPayLL.setBackgroundColor(getResources().getColor(R.color.fragment_home_exclusive_show_view_bg));
                mGroupInfoPayPriceTV.setText("");
                mGroupInfoPayTV.setTextColor(getResources().getColor(R.color.groupbooking_info_btn_str_color));
                mGroupInfoPayTV.setText(getResources().getString(R.string.groupbooding_pay_finish_tv_str));
            }

            @Override
            public void onTick(long millisUntilFinished) {// 计时过程显示
                int day = 0;
                int hour = 0;
                int minute = 0;
                int second = 0;
                second = new Long(millisUntilFinished / 1000).intValue();
                if (second > 60) {
                    minute = second / 60;         //取整
                    second = second % 60;         //取余
                }
                if (minute > 60) {
                    hour = minute / 60;
                    minute = minute % 60;
                }
                if (hour > 60) {
                    day = hour / 24;
                    hour = hour % 24;
                }
                if (day < 10) {
                    dayTV.setText("0" + String.valueOf(day));
                } else {
                    dayTV.setText(String.valueOf(day));
                }
                if (hour < 10) {
                    hourTV.setText("0"+String.valueOf(hour));
                } else {
                    hourTV.setText(String.valueOf(hour));
                }
                if (minute < 10) {
                    minuteTV.setText("0"+String.valueOf(minute));
                } else {
                    minuteTV.setText(String.valueOf(minute));
                }
                if (second < 10) {
                    secondTV.setText("0" + String.valueOf(second));
                } else {
                    secondTV.setText(String.valueOf(second));
                }
            }
        }
        TimeCount mTime = new TimeCount(millisInFuture*1000, 1000);
        mTime.start();
    }
    private void showRemindDialog(int type) {
        LayoutInflater factory = LayoutInflater.from(GroupBookingInfoActivity.this);
        final View textEntryView = factory.inflate(R.layout.activity_fieldinfo_refund_price_popuwindow, null);
        TextView mrefunt_price_detailed = (TextView) textEntryView.findViewById(R.id.refunt_price_detailed);
        ImageButton mrefunt_price_detailed_btn = (ImageButton) textEntryView.findViewById(R.id.fieldinfo_explain_close_imgbtn);
        RelativeLayout mfieldinfo_all_dialog_layout = (RelativeLayout) textEntryView.findViewById(R.id.fieldinfo_all_dialog_layout);
        RelativeLayout mfieldinfo_refunt_price_detailed_layout = (RelativeLayout) textEntryView.findViewById(R.id.fieldinfo_refunt_price_detailed_layout);
        RelativeLayout mfieldinfo_error_recovery_layout = (RelativeLayout) textEntryView.findViewById(R.id.fieldinfo_error_recovery_layout);
        LinearLayout mfieldinfo_error_recovery_top_layout = (LinearLayout) textEntryView.findViewById(R.id.fieldinfo_error_recovery_top_layout);
        TextView merror_correction_cancel_text = (TextView) textEntryView.findViewById(R.id.error_correction_cancel_text);
        mCorrectionSubmitErrorTV = (TextView) textEntryView.findViewById(R.id.error_correction_submit_text);
        TextView merror_recovery_resourcesname_text = (TextView) textEntryView.findViewById(R.id.error_recovery_resourcesname_text);
        TextView mrefunt_price_text = (TextView) textEntryView.findViewById(R.id.refunt_price_text);
        final EditText merror_recovery_content_edit = (EditText) textEntryView.findViewById(R.id.error_recovery_content_edit);
        if (type == 0 || type == 2 || type == 3) {
            mrefunt_price_detailed_btn.setVisibility(View.VISIBLE);
            mfieldinfo_refunt_price_detailed_layout.setVisibility(View.VISIBLE);
            mfieldinfo_error_recovery_layout.setVisibility(View.GONE);
            if (type == 0) {
                mrefunt_price_text.setText(getResources().getString(R.string.fieldinfo_integral_reward_tv_str));
                mrefunt_price_detailed.setText(getResources().getString(R.string.fieldinfo_integral_reward_content_str));
            } else if (type == 2) {
                mrefunt_price_text.setText(getResources().getString(R.string.groupbooking_info_stall_area_title_str));
                mrefunt_price_detailed.setText(getResources().getString(R.string.groupbooking_info_stall_area_content_first_str) +
                        mGroupBookingInfoModel.getFirst_selling_resource_price().get("size").toString() +
                        getResources().getString(R.string.groupbooking_info_stall_area_content_second_str));
            } else if (type == 3) {
                mrefunt_price_text.setText(getResources().getString(R.string.module_fieldinfo_change_explain));
                mrefunt_price_detailed.setText(getResources().getString(R.string.module_fieldinfo_cancle_order_explain)
                      );
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
            if (mGroupBookingInfoModel.getPhysical_resource().getName() != null && mGroupBookingInfoModel.getPhysical_resource().getName().length() > 0) {
                merror_recovery_resourcesname_text.setText(mGroupBookingInfoModel.getPhysical_resource().getName());
            }
        }
        mRemindDialog = new AlertDialog.Builder(this).create();
        Constants.show_dialog(textEntryView,mRemindDialog);
        mrefunt_price_detailed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRemindDialog.isShowing()) {
                    mRemindDialog.dismiss();
                }
            }
        });
        merror_correction_cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRemindDialog.isShowing()) {
                    mRemindDialog.dismiss();
                }
            }
        });
        mCorrectionSubmitErrorTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (merror_recovery_content_edit.getText().toString().trim().length() > 0) {
                    mCorrectionSubmitErrorTV.setEnabled(false);
                    showProgressDialog();
                    mGroupPresenter.releaseFeedbacks(mGroupBookingInfoModel.getId(),0,
                            merror_recovery_content_edit.getText().toString());
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.fieldinfo_error_recovery_content_remindtext));
                    return;
                }
            }
        });
    }
    public void showpreviewpicture(ArrayList<String> path,int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(path,position);
        test.showreview(this);
    }

    @Override
    public void fieldshowreviewpicture(ArrayList<String> path, int position) {
        mReviewZoomPictureDialog = new AlertDialog.Builder(this).create();
        View mView = getLayoutInflater().inflate(com.linhuiba.linhuifield.R.layout.activity_fieldinfo_preview_zoom_picture_dialog, null);
        final TextView mShowPictureSizeTV = (TextView)mView.findViewById(com.linhuiba.linhuifield.R.id.showpicture_dialog_sizetxt);
        final TextView mShowDialogPictureBackTV = (TextView)mView.findViewById(com.linhuiba.linhuifield.R.id.showpicture_dialog_back);
        ViewPager mZoomViewPage = (ViewPager)mView.findViewById(com.linhuiba.linhuifield.R.id.zoom_dialog_viewpage);
        com.linhuiba.linhuifield.connector.Constants mConstants = new com.linhuiba.linhuifield.connector.Constants(GroupBookingInfoActivity.this,GroupBookingInfoActivity.this,mNewPosition,mImageListSize,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT);
        mConstants.showPicture(mView,mShowPictureSizeTV,mZoomViewPage,
                mReviewZoomPictureDialog,mDialogImageViewList,path,mDislogIsRefreshZoomImg,
                position,false);
        mDislogIsRefreshZoomImg = false;
        mZoomViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mNewPosition = position % mDialogImageViewList.size();
            }

            @Override
            public void onPageSelected(int position) {
                mShowPictureSizeTV.setText(String.valueOf(position % mDialogImageViewList.size()+1)+"/" + String.valueOf(mDialogImageViewList.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if(requestCode == Constants.PermissionRequestCode) {
                MQConfig.init(GroupBookingInfoActivity.this, com.linhuiba.linhuipublic.config.Config.MQAppkey, new OnInitCallback() {
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
                            Intent intent = new MQIntentBuilder(GroupBookingInfoActivity.this)
                                    .setClientInfo(clientInfo)
                                    .setCustomizedId(LoginManager.getUid())
                                    .build();
                            startActivityForResult(intent,10);
                        } else {
                            Intent intent = new MQIntentBuilder(GroupBookingInfoActivity.this)
                                    .build();
                            startActivityForResult(intent,10);
                        }

                    }

                    @Override
                    public void onFailure(int code, String message) {
                        MessageUtils.showToast(getResources().getString(R.string.review_error_text));            }
                });

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CREATE_ORDER_REQUESTCODE:
                if (LoginManager.isLogin()) {
                    if (mGroupBookingInfoModel.getGroup_purchase().getId() != null &&
                            mGroupBookingInfoModel.getGroup_purchase().getId() > 0) {
                        FieldApi.getGroupStatus(MyAsyncHttpClient.MyAsyncHttpClient(),getGroupStatusHandler,
                                String.valueOf(mGroupBookingInfoModel.getGroup_purchase().getId()));
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    final View myView = GroupBookingInfoActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                    mShareDialog = new AlertDialog.Builder(GroupBookingInfoActivity.this).create();
                    share_linkurl = Config.Domain_Name + Config.SHARE_GROUP_INFO_URL + mGroupBookingInfoModel.getGroup_purchase().getId() + "?city_id=" + mCityIdStr;
                    sharewxMiniShareLinkUrl = Config.WX_MINI_SHARE_GROUP_INFO_URL+ "?city_id=" + mCityIdStr;
                    if(!isFinishing()) {
                        if (mShareDialog!= null && mShareDialog.isShowing()) {
                            mShareDialog.dismiss();
                        }
                        Constants constants = new Constants(GroupBookingInfoActivity.this,
                                ShareIconStr);
                        constants.shareWXMiniPopupWindow(GroupBookingInfoActivity.this,myView,mShareDialog,api,share_linkurl,
                                ShareTitleStr,
                                SharedescriptionStr, ShareBitmap,sharewxMiniShareLinkUrl,miniShareBitmap,mSharePYQTitleStr);
                    }
                    break;
                default:
                    break;
            }
        }

    };
    private ArrayList<HashMap<String,Object>> getsubmitorderlist() {
        ArrayList<HashMap<String,Object>> jsonArray = new ArrayList<HashMap<String,Object>>();
        String imagepath = "";
        String datestr = mGroupBookingInfoModel.getGroup_purchase().getActivity_start();
        if (mPicList != null) {
            if(mPicList.size() > 0) {
                imagepath = mPicList.get(0).toString();
            }
        }
        HashMap<String,Object> json = new HashMap<String,Object>();
        if (mGroupBookingInfoModel.getFirst_selling_resource_price() != null &&
                mGroupBookingInfoModel.getFirst_selling_resource_price().get("size") != null &&
                mGroupBookingInfoModel.getFirst_selling_resource_price().get("selling_resource_id") != null &&
                mGroupBookingInfoModel.getFirst_selling_resource_price().get("selling_resource_id").toString().length() > 0 &&
                mGroupBookingInfoModel.getFirst_selling_resource_price().get("size").toString().length() > 0) {
            json.put("id", mGroupBookingInfoModel.getFirst_selling_resource_price().get("selling_resource_id").toString());
            json.put("date",datestr);
            json.put("size",mGroupBookingInfoModel.getFirst_selling_resource_price().get("size").toString());
            json.put("custom_dimension", "");
            json.put("lease_term_type_id", 1);
            json.put("lease_term_type", getResources().getString(R.string.groupbooding_list_item_residue_tiem_day_str));
            json.put("count", 1);
            json.put("count_of_time_unit", 1);
        }
        json.put("subsidy_fee","0");
        if (mGroupBookingInfoModel.getGroup_purchase().getOrigin_price() != null) {
            json.put("price",Constants.getdoublepricestring(mGroupBookingInfoModel.getGroup_purchase().getOrigin_price(),100));
            if (mGroupBookingInfoModel.getFirst_selling_resource_price().get("price") != null &&
                    mGroupBookingInfoModel.getFirst_selling_resource_price().get("price").toString().length() > 0) {
                json.put("subsidy_fee",Constants.getdoublepricestring(mGroupBookingInfoModel.getGroup_purchase().getOrigin_price()*100 -
                        Double.parseDouble(mGroupBookingInfoModel.getFirst_selling_resource_price().get("price").toString()),1));
                json.put("actual_fee",mGroupBookingInfoModel.getFirst_selling_resource_price().get("price").toString());
            }
        }
        json.put("resourcename", mGroupBookingInfoModel.getPhysical_resource().getName());
        json.put("imagepath",imagepath);
        if (mGroupBookingInfoModel.getPhysical_resource().getField_type() != null &&
                mGroupBookingInfoModel.getPhysical_resource().getField_type().getDisplay_name() != null &&
                mGroupBookingInfoModel.getPhysical_resource().getField_type().getDisplay_name().length() > 0) {
            json.put("field_type", mGroupBookingInfoModel.getPhysical_resource().getField_type().getDisplay_name());
        } else {
            json.put("field_type", "");
        }
        json.put("discount_rate", "0");
        json.put("tax_point", "0");
        json.put("special_tax_point", "0");
        jsonArray.add(json);
        return jsonArray;
    }
    private LinhuiAsyncHttpResponseHandler getGroupStatusHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null) {
                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                if (jsonObject != null) {
                    if (jsonObject.get("flag") != null &&
                            jsonObject.get("flag").toString().length() > 0) {
                        if (Integer.parseInt(jsonObject.get("flag").toString()) == 1) {
                            MessageUtils.showDialog(GroupBookingInfoActivity.this,"",
                                    getResources().getString(R.string.groupbooding_pay_next_status_tv_str),
                                    R.string.confirm,R.string.groupbooding_paid_success_invite_friends_btn_str,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent OrderConfirm = new Intent(GroupBookingInfoActivity.this,OrderConfirmActivity.class);
                                            OrderConfirm.putExtra("ordertype",1);
                                            OrderConfirm.putExtra("submitorderlist", (Serializable) getsubmitorderlist());
                                            // : 2017/10/31 押金
                                            OrderConfirm.putExtra("deposit",mDeposit);
                                            OrderConfirm.putExtra("group_id",mGroupBookingInfoModel.getGroup_purchase().getId());
                                            OrderConfirm.putExtra("mGroupPayType",1);
                                            startActivity(OrderConfirm);
                                        }
                                    },
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                        } else if (Integer.parseInt(jsonObject.get("flag").toString()) == 2) {
                            MessageUtils.showDialog(GroupBookingInfoActivity.this,"",
                                    getResources().getString(R.string.groupbooking_info_show_other_group_tv_str),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    },
                                    null);

                        } else {
                            Intent OrderConfirm = new Intent(GroupBookingInfoActivity.this,OrderConfirmActivity.class);
                            OrderConfirm.putExtra("ordertype",1);
                            OrderConfirm.putExtra("submitorderlist", (Serializable) getsubmitorderlist());
                            // : 2017/10/31 押金
                            OrderConfirm.putExtra("deposit",mDeposit);
                            OrderConfirm.putExtra("group_id",mGroupBookingInfoModel.getGroup_purchase().getId());
                            OrderConfirm.putExtra("mGroupPayType",1);
                            startActivity(OrderConfirm);
//                Intent PaidSuccessIntent = new Intent(GroupBookingInfoActivity.this,GroupBookingPaidSuccessActivity.class);
//                startActivity(PaidSuccessIntent);
                        }
                    }
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
        }
    };
    private void shareGroupInfo() {
        if (ShareBitmap == null || miniShareBitmap == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (mGroupBookingInfoModel.getPhysical_resource().getPhysical_resource_imgs()!= null &&
                            mGroupBookingInfoModel.getPhysical_resource().getPhysical_resource_imgs().size() > 0 &&
                            mGroupBookingInfoModel.getPhysical_resource().getPhysical_resource_imgs().get(0).get("pic_url") != null &&
                            mGroupBookingInfoModel.getPhysical_resource().getPhysical_resource_imgs().get(0).get("pic_url").length() > 0) {
                        ShareIconStr = mGroupBookingInfoModel.getPhysical_resource().getPhysical_resource_imgs().get(0).get("pic_url").toString()+ com.linhuiba.linhuipublic.config.Config.Linhui_Mid_Watermark;
                        ShareBitmap = com.linhuiba.linhuifield.connector.Constants.GetLocalOrNetBitmap(ShareIconStr);//压缩Bitmap
                        ShareBitmap = com.linhuiba.linhuifield.connector.Constants.addWaterMark(ShareBitmap,SharedescriptionStr,GroupBookingInfoActivity.this);
                        ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                        miniShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 120, 120, true);//压缩Bitmap
                        if (ShareBitmap == null) {
                            ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                            ShareBitmap = com.linhuiba.linhuifield.connector.Constants.addWaterMark(ShareBitmap,SharedescriptionStr,GroupBookingInfoActivity.this);
                            ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                        }
                        if (miniShareBitmap == null) {
                            miniShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                        }
                    } else {
                        ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                        ShareBitmap = com.linhuiba.linhuifield.connector.Constants.addWaterMark(ShareBitmap,SharedescriptionStr,GroupBookingInfoActivity.this);
                        ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                        miniShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                    }
                    mHandler.sendEmptyMessage(0);
                }
            }).start();
        } else {
            View myView = GroupBookingInfoActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
            mShareDialog = new AlertDialog.Builder(GroupBookingInfoActivity.this).create();
            share_linkurl = Config.Domain_Name + Config.SHARE_GROUP_INFO_URL + mGroupBookingInfoModel.getGroup_purchase().getId()
                    + "?city_id=" + mCityIdStr;
            sharewxMiniShareLinkUrl = Config.WX_MINI_SHARE_GROUP_INFO_URL+ "?city_id=" + mCityIdStr;
            Constants constants = new Constants(GroupBookingInfoActivity.this,
                    ShareIconStr);
            constants.shareWXMiniPopupWindow(GroupBookingInfoActivity.this,myView,mShareDialog,api,share_linkurl,
                    ShareTitleStr,
                    SharedescriptionStr, ShareBitmap,sharewxMiniShareLinkUrl,miniShareBitmap,mSharePYQTitleStr);
        }
    }

    @Override
    public void onFieldinfoFailure(boolean superresult, Throwable error) {
        mGroupinfoNoDataLL.setVisibility(View.VISIBLE);
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
    }

    @Override
    public void onFieldinfoResSuccess(PhyResInfoModel resInfo) {

    }
    @Override
    public void onResReviewSuccess(ArrayList<ReviewModel> list) {
        mReviewList = list;
        if (mReviewList == null || mReviewList.size() == 0) {
            mreview_listview.setVisibility(View.GONE);
        } else {
            mreview_listview.setVisibility(View.VISIBLE);
            if (mReviewList.size() == 2) {
                ArrayList<ReviewModel> mtmpReviewList = new ArrayList<>();
                mtmpReviewList.addAll(mReviewList);
                int oneitemsize = 0;int twoitemsize = 0;
                for (int i = 0; i < mtmpReviewList.size(); i++) {
                    int itemsize = 0;
                    if (mReviewList.get(i).getContent() != null && mReviewList.get(i).getContent().length() > 0) {
                        itemsize = itemsize + mReviewList.get(i).getContent().length();
                    }
                    if (mReviewList.get(i).getImages() != null && mReviewList.get(i).getImages().size() > 0) {
                        itemsize = itemsize + mReviewList.get(i).getImages().size();
                    }
                    if (mReviewList.get(i).getTags() != null && mReviewList.get(i).getTags().size() > 0) {
                        itemsize = itemsize + mReviewList.get(i).getTags().size();
                    }
                    if (i == 0) {
                        oneitemsize = itemsize;
                    } else if (i == 1) {
                        twoitemsize = itemsize;
                    }
                }
                if (twoitemsize > oneitemsize) {
                    if (mReviewList != null) {
                        mReviewList.clear();
                    }
                    mReviewList.add(mtmpReviewList.get(1));
                    mReviewList.add(mtmpReviewList.get(0));
                }
            }
            mFieldEvaluationAdapter = new Fieldinfo_ReviewAdapter(GroupBookingInfoActivity.this, mReviewList,GroupBookingInfoActivity.this);
            mreview_listview.setAdapter(mFieldEvaluationAdapter);
        }
    }

    @Override
    public void onResReviewFailure(boolean superresult, Throwable error) {
        mreview_listview.setVisibility(View.GONE);
        if (!superresult) {
            MessageUtils.showToast(getContext(), error.getMessage());
        }
    }

    @Override
    public void onGroupinfoResSuccess(GroupBookingInfoModel resInfo) {
        mGroupBookingInfoModel = resInfo;
        if (mGroupBookingInfoModel != null) {
            mGroupinfoNoDataLL.setVisibility(View.GONE);
            if (mGroupBookingInfoModel.getPhysical_resource().getPhysical_resource_imgs() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getPhysical_resource_imgs().size() > 0) {
                for (int i = 0; i < mGroupBookingInfoModel.getPhysical_resource().getPhysical_resource_imgs().size(); i++) {
                    if (mGroupBookingInfoModel.getPhysical_resource().getPhysical_resource_imgs().get(i).get("pic_url") != null &&
                            mGroupBookingInfoModel.getPhysical_resource().getPhysical_resource_imgs().get(i).get("pic_url").toString().length() > 0) {
                        mPicList.add(mGroupBookingInfoModel.getPhysical_resource().getPhysical_resource_imgs().get(i).get("pic_url").toString() + com.linhuiba.linhuipublic.config.Config.Linhui_Max_Watermark);
                    }
                }
                if (mPicList.size() > 0) {
                    //设置图片加载器
                    mFieldInfoBanner.setImageLoader(mFieldinfoImageLoader);
                    //设置图片集合
                    mFieldInfoBanner.setImages(mPicList);
                    //设置banner动画效果
                    mFieldInfoBanner.setBannerAnimation(Transformer.Default);
                    //设置自动轮播，默认为true
                    mFieldInfoBanner.isAutoPlay(false);
                    //设置指示器位置（当banner模式中有指示器时）
                    mFieldInfoBanner.setIndicatorGravity(BannerConfig.CENTER);
                    //banner设置方法全部调用完毕时最后调用
                    mFieldInfoBanner.setOnBannerClickListener(new OnBannerClickListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            if (mZoomPictureDialog != null && mZoomPictureDialog.isShowing()) {
                                mZoomPictureDialog.dismiss();
                            }
                            showZoomPictureDialog((position - 1) % mPicList.size());
                        }
                    });
                    mFieldInfoBanner.start();
                }
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getField_type() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getField_type().getId() > 0 &&
                    mGroupBookingInfoModel.getPhysical_resource().getField_type().getDisplay_name() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getField_type().getDisplay_name().length() > 0) {
                mResCommunityTypeTV.setText(mGroupBookingInfoModel.getPhysical_resource().getField_type().getDisplay_name());
                SharedescriptionStr = mGroupBookingInfoModel.getPhysical_resource().getField_type().getDisplay_name() +"  ";
            } else {
                mResCommunityTypeTV.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getGroup_purchase().getOrigin_price() != null) {
                mGroupInfoActualPriceTV.setText(getResources().getString(R.string.order_listitem_price_unit_text) +
                        Constants.getdoublepricestring(mGroupBookingInfoModel.getGroup_purchase().getOrigin_price(),1));
                mGroupInfoActualPriceTV.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中间横线
            } else {
                mGroupInfoActualPriceTV.setText("");
            }
            if (mGroupBookingInfoModel.getFirst_selling_resource_price() != null
                    && mGroupBookingInfoModel.getFirst_selling_resource_price().get("price") != null &&
                    mGroupBookingInfoModel.getFirst_selling_resource_price().get("price").toString().length() > 0) {
                mGroupInfoOriginPriceTV.setText(com.linhuiba.linhuifield.connector.Constants.getPriceUnitStr(GroupBookingInfoActivity.this,
                        getResources().getString(R.string.order_listitem_price_unit_text) +
                                Constants.getpricestring(mGroupBookingInfoModel.getFirst_selling_resource_price().get("price").toString(),0.01),14));
                mGroupInfoPayPriceTV.setText(com.linhuiba.linhuifield.connector.Constants.getPriceUnitStr(GroupBookingInfoActivity.this,
                        getResources().getString(R.string.order_listitem_price_unit_text) +
                                Constants.getpricestring(mGroupBookingInfoModel.getFirst_selling_resource_price().get("price").toString(),0.01),12));
                // : 2017/10/19 分享时的描述
                SharedescriptionStr = SharedescriptionStr + getResources().getString(R.string.order_listitem_price_unit_text) +
                        Constants.getpricestring(mGroupBookingInfoModel.getFirst_selling_resource_price().get("price").toString(),0.01);
            } else {
                mGroupInfoOriginPriceTV.setText("");
                mGroupInfoPayPriceTV.setText("");
            }
            if (mGroupBookingInfoModel.getGroup_purchase().getNumber_of_group_purchase_now() != null) {
                mGroupInfoPurchaseNumTV.setText(String.valueOf(mGroupBookingInfoModel.getGroup_purchase().getNumber_of_group_purchase_now()));
                mGroupInfoPurchaseNumLL.setVisibility(View.VISIBLE);
            } else {
                mGroupInfoPurchaseNumTV.setText("");
                mGroupInfoPurchaseNumLL.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getGroup_purchase().getStatus() != null) {
                if (mGroupBookingInfoModel.getGroup_purchase().getStatus() == 0) {
                    mGroupInfoPayLL.setEnabled(true);
                    mGroupInfoPayLL.setBackgroundColor(getResources().getColor(R.color.price_tv_color));
                    mGroupInfoPayTV.setTextColor(getResources().getColor(R.color.white));
                    mGroupInfoPayTV.setText(getResources().getString(R.string.groupbooding_pay_tv_str));
                } else if (mGroupBookingInfoModel.getGroup_purchase().getStatus() == 1) {
                    mGroupInfoPayLL.setEnabled(false);
                    mGroupInfoPayLL.setBackgroundColor(getResources().getColor(R.color.fragment_home_exclusive_show_view_bg));
                    mGroupInfoPayPriceTV.setText("");
                    mGroupInfoPayTV.setTextColor(getResources().getColor(R.color.groupbooking_info_btn_str_color));
                    mGroupInfoPayTV.setText(getResources().getString(R.string.groupbooding_info_full_strength_str));
                } else if (mGroupBookingInfoModel.getGroup_purchase().getStatus() == 2) {
                    mGroupInfoPayLL.setEnabled(false);
                    mGroupInfoPayLL.setBackgroundColor(getResources().getColor(R.color.fragment_home_exclusive_show_view_bg));
                    mGroupInfoPayPriceTV.setText("");
                    mGroupInfoPayTV.setTextColor(getResources().getColor(R.color.groupbooking_info_btn_str_color));
                    mGroupInfoPayTV.setText(getResources().getString(R.string.groupbooding_pay_finish_tv_str));
                }
            } else {
                mGroupInfoPayLL.setEnabled(false);
                mGroupInfoPayLL.setBackgroundColor(getResources().getColor(R.color.fragment_home_exclusive_show_view_bg));
                mGroupInfoPayPriceTV.setText("");
                mGroupInfoPayTV.setTextColor(getResources().getColor(R.color.groupbooking_info_btn_str_color));
                mGroupInfoPayTV.setText(getResources().getString(R.string.groupbooding_pay_finish_tv_str));
            }
            if (mGroupBookingInfoModel.getGroup_purchase().getTime_left() != null
                    && mGroupBookingInfoModel.getGroup_purchase().getTime_left() > 0) {
                addCountDownTimer(mGroupBookingInfoModel.getGroup_purchase().getTime_left(),
                        mGroupInfoCountDownDayTV,mGroupInfoCountDownHourTV,mGroupInfoCountDownMinuteTV,
                        mGroupInfoCountDownSecondTV);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getName() != null) {
                mGroupInfoResNameTV.setText(mGroupBookingInfoModel.getPhysical_resource().getName());
                ShareTitleStr = mGroupBookingInfoModel.getPhysical_resource().getName();
                mSharePYQTitleStr = getResources().getString(R.string.fieldinfo_share_text) + "(" +
                        mGroupBookingInfoModel.getPhysical_resource().getName() + ")" +
                        getResources().getString(R.string.fieldinfo_share_linhuiba_mark_text);
            } else {
                mGroupInfoResNameTV.setText("");
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getNumber_of_people() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getNumber_of_people() > 0) {
                mGroupInfoNumberOfPeopleTV.setVisibility(View.VISIBLE);
                mGroupInfoNumberOfPeopleTV.setText(
                        String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getNumber_of_people()));
                mFieldinfoPeopleTV.setText(
                        String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getNumber_of_people()));

            } else {
                mGroupInfoNumberOfPeopleTV.setText(getResources().getString(R.string.groupbooding_no_data_str));
                mFieldinfoPeopleLL.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getNumber_of_order() != null) {
                mGroupInfoTransactionPeopleTV.setVisibility(View.VISIBLE);
                mGroupInfoTransactionPeopleTV.setText(
                        String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getNumber_of_order()));
            } else {
                mGroupInfoTransactionPeopleTV.setText(getResources().getString(R.string.groupbooding_no_data_str));
            }
            if (mGroupBookingInfoModel.getIndustry_str() != null &&
                    mGroupBookingInfoModel.getIndustry_str().length() > 0) {
                mGroupInfoIndustryTV.setText(mGroupBookingInfoModel.getIndustry_str());
            } else {
                mGroupInfoIndustryTV.setText(getResources().getString(R.string.groupbooding_no_data_str));
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getAge_level() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getAge_level().getDisplay_name() !=null &&
                    mGroupBookingInfoModel.getPhysical_resource().getAge_level().getDisplay_name().length() > 0 &&
                    !mGroupBookingInfoModel.getPhysical_resource().getAge_level().getDisplay_name()
                            .equals(getResources().getString(R.string.fieldinfo_no_edit_id_str))) {
                mGroupInfoAgeLevelTV.setText(mGroupBookingInfoModel.getPhysical_resource().getAge_level().getDisplay_name());
                mGroupInfoAgeLevelTV.setVisibility(View.VISIBLE);
                mGroupInfoAgeLabelLL.setVisibility(View.VISIBLE);
            } else {
                mGroupInfoAgeLevelTV.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getConsumption_level() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getConsumption_level().getDisplay_name() !=null &&
                    mGroupBookingInfoModel.getPhysical_resource().getConsumption_level().getDisplay_name().length() > 0 &&
                    !mGroupBookingInfoModel.getPhysical_resource().getConsumption_level().getDisplay_name()
                            .equals(getResources().getString(R.string.fieldinfo_no_edit_id_str))) {
                mGroupInfoConsumptionLevelTV.setText(getResources().getString(R.string.fieldinfo_consumption_level_str) +
                        mGroupBookingInfoModel.getPhysical_resource().getConsumption_level().getDisplay_name());
                mGroupInfoAgeLabelLL.setVisibility(View.VISIBLE);
                mGroupInfoConsumptionLevelTV.setVisibility(View.VISIBLE);
            } else {
                mGroupInfoConsumptionLevelTV.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getDetailed_address() != null && mGroupBookingInfoModel.getPhysical_resource().getCommunity().getDetailed_address().length() > 0) {
                String address = "";
                if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getDistrict() != null &&
                        mGroupBookingInfoModel.getPhysical_resource().getCommunity().getDistrict().getName() != null &&
                        mGroupBookingInfoModel.getPhysical_resource().getCommunity().getDistrict().getName().length() > 0) {
                    address = mGroupBookingInfoModel.getPhysical_resource().getCommunity().getDistrict().getName() +
                            mGroupBookingInfoModel.getPhysical_resource().getCommunity().getDetailed_address();
                } else {
                    address = mGroupBookingInfoModel.getPhysical_resource().getCommunity().getDetailed_address();
                }
                mGroupInfoAddressTV.setText(address);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getDistrict().getName() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getDistrict().getName().length() > 0 &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getCity().getName() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getCity().getName().length() > 0) {
                SharedescriptionStr = SharedescriptionStr + "\n" +
                        mGroupBookingInfoModel.getPhysical_resource().getCommunity().getCity().getName() +
                        mGroupBookingInfoModel.getPhysical_resource().getCommunity().getDistrict().getName();
            }

            if (mGroupBookingInfoModel.getPhysical_resource().getDoLocation() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getDoLocation().length() > 0) {
                mFieldinfoDoLocationTV.setText(mGroupBookingInfoModel.getPhysical_resource().getDoLocation());
                mGroupinfoLocationTV.setText(getResources().getString(R.string.fieldinfo_dolocation_tv_str)+
                        mGroupBookingInfoModel.getPhysical_resource().getDoLocation());
                if (mGroupBookingInfoModel.getPhysical_resource().getIndoor() != null) {
                    if (mGroupBookingInfoModel.getPhysical_resource().getIndoor() == 0) {
                        mGroupinfoLocationIndoorTV.setText(getResources().getString(R.string.fieldinfo_location_unindoor_tv_str));
                        mGroupinfoLocationIndoorTV.setVisibility(View.VISIBLE);
                    } else if (mGroupBookingInfoModel.getPhysical_resource().getIndoor() == 1) {
                        mGroupinfoLocationIndoorTV.setText(getResources().getString(R.string.fieldinfo_location_indoor_tv_str));
                        mGroupinfoLocationIndoorTV.setVisibility(View.VISIBLE);
                    }
                } else {
                    mGroupinfoLocationIndoorTV.setVisibility(View.GONE);
                }
            } else {
                mFieldinfoDoLocationLL.setVisibility(View.GONE);
            }

            if (mGroupBookingInfoModel.getGroup_purchase().getMin() != null &&
                    mGroupBookingInfoModel.getGroup_purchase().getMin() > 0) {
                mGroupInfoMinPeopleTV.setText(String.valueOf(mGroupBookingInfoModel.getGroup_purchase().getMin())+
                        getResources().getString(R.string.mTxt_addfield_people_unit));
            } else {
                mGroupInfoMinPeopleTV.setText("");
            }
            if (mGroupBookingInfoModel.getGroup_purchase().getMax() != null &&
                    mGroupBookingInfoModel.getGroup_purchase().getMax() > 0) {
                mGroupInfoMaxPeopleTV.setText(String.valueOf(mGroupBookingInfoModel.getGroup_purchase().getMax()) +
                        getResources().getString(R.string.mTxt_addfield_people_unit));
            } else {
                mGroupInfoMaxPeopleTV.setText(getResources().getString(R.string.groupbooding_no_data_str));
            }
            if (mGroupBookingInfoModel.getGroup_purchase().getActivity_start() != null &&
                    mGroupBookingInfoModel.getGroup_purchase().getActivity_end() != null &&
                    mGroupBookingInfoModel.getGroup_purchase().getActivity_start().length() > 0 &&
                    mGroupBookingInfoModel.getGroup_purchase().getActivity_end().length() > 0) {
                mGroupInfoActivityTimeTV.setText(mGroupBookingInfoModel.getGroup_purchase().getActivity_start() + "-"+
                        mGroupBookingInfoModel.getGroup_purchase().getActivity_end());
            } else {
                mGroupInfoActivityTimeTV.setText(getResources().getString(R.string.groupbooding_no_data_str));
            }
            if (mGroupBookingInfoModel.getFirst_selling_resource_price() != null &&
                    mGroupBookingInfoModel.getFirst_selling_resource_price().get("size") != null &&
                    mGroupBookingInfoModel.getFirst_selling_resource_price().get("size").length() > 0) {
                mGroupInfoFieldAreaTV.setText(mGroupBookingInfoModel.getFirst_selling_resource_price().get("size").toString());
            } else {
                mGroupInfoFieldAreaTV.setText(getResources().getString(R.string.groupbooding_no_data_str));
            }
            if (mGroupBookingInfoModel.getGroup_purchase().getActivity_allow() != null &&
                    mGroupBookingInfoModel.getGroup_purchase().getActivity_allow().length() > 0) {
                mGroupInfoSupportCategoryTV.setText(mGroupBookingInfoModel.getGroup_purchase().getActivity_allow());
                mGroupInfoSupportCategoryLL.setVisibility(View.VISIBLE);
                mGroupInfoContrabandCategoryLL.setVisibility(View.GONE);
            } else {
                mGroupInfoSupportCategoryLL.setVisibility(View.GONE);
                mGroupInfoSupportCategoryTV.setText(getResources().getString(R.string.order_refuse_ordertxtnull));
                // : 2017/10/19 禁摆品类字段
                if ((mGroupBookingInfoModel.getPhysical_resource().getContraband() != null &&
                        mGroupBookingInfoModel.getPhysical_resource().getContraband().size() > 0) ||
                        (mGroupBookingInfoModel.getPhysical_resource().getOther_contraband() != null &&
                                mGroupBookingInfoModel.getPhysical_resource().getOther_contraband().length() > 0)) {
                    mGroupInfoContrabandCategoryLL.setVisibility(View.GONE);
                    String contraband_str = "";
                    if (mGroupBookingInfoModel.getPhysical_resource().getContraband() != null &&
                            mGroupBookingInfoModel.getPhysical_resource().getContraband().size() > 0) {
                        for (int i = 0; i < mGroupBookingInfoModel.getPhysical_resource().getContraband().size(); i++) {
                            if (mGroupBookingInfoModel.getPhysical_resource().getContraband().get(i).getDisplay_name() != null &&
                                    mGroupBookingInfoModel.getPhysical_resource().getContraband().get(i).getDisplay_name().length() > 0) {
                                if (i != 0) {
                                    contraband_str = contraband_str + "、" + mGroupBookingInfoModel.getPhysical_resource().getContraband().get(i).getDisplay_name();
                                } else {
                                    contraband_str = contraband_str + mGroupBookingInfoModel.getPhysical_resource().getContraband().get(i).getDisplay_name();
                                }
                            }
                        }
                    }
                    if (mGroupBookingInfoModel.getPhysical_resource().getOther_contraband() != null &&
                            mGroupBookingInfoModel.getPhysical_resource().getOther_contraband().length() > 0) {
                        if (mGroupBookingInfoModel.getPhysical_resource().getContraband() != null &&
                                mGroupBookingInfoModel.getPhysical_resource().getContraband().size() > 0) {
                            contraband_str = contraband_str + "、" + mGroupBookingInfoModel.getPhysical_resource().getOther_contraband();
                        } else {
                            contraband_str = contraband_str + mGroupBookingInfoModel.getPhysical_resource().getOther_contraband();
                        }
                    }
                    mGroupInfoContrabandCategoryTV.setText(contraband_str);
                } else {
                    mGroupInfoContrabandCategoryLL.setVisibility(View.GONE);
                    mGroupInfoContrabandCategoryTV.setText(getResources().getString(R.string.order_refuse_ordertxtnull));
                }
            }
            if (mGroupBookingInfoModel.getHas_power() != null &&
                    mGroupBookingInfoModel.getHas_power() == 1) {
                mGroupInfoHasPowerLL.setVisibility(View.VISIBLE);
                mGroupInfoHasPowerTmpLL.setVisibility(View.GONE);
            } else {
                mGroupInfoHasPowerLL.setVisibility(View.GONE);
                mGroupInfoHasPowerTmpLL.setVisibility(View.VISIBLE);
            }
            if (mGroupBookingInfoModel.getHas_chair() != null &&
                    mGroupBookingInfoModel.getHas_chair() == 1) {
                mGroupInfoHasChairLL.setVisibility(View.VISIBLE);
                mGroupInfoHasChairTmpLL.setVisibility(View.GONE);
            } else {
                mGroupInfoHasChairLL.setVisibility(View.GONE);
                mGroupInfoHasChairTmpLL.setVisibility(View.VISIBLE);

            }
            if (mGroupBookingInfoModel.getHas_tent() != null &&
                    mGroupBookingInfoModel.getHas_tent() == 1) {
                mGroupInfoHasTentLL.setVisibility(View.VISIBLE);
                mGroupInfoHasTentTmpLL.setVisibility(View.GONE);
            } else {
                mGroupInfoHasTentLL.setVisibility(View.GONE);
                mGroupInfoHasTentTmpLL.setVisibility(View.VISIBLE);
            }
            if (mGroupBookingInfoModel.getLeaflet() != null &&
                    mGroupBookingInfoModel.getLeaflet() == 1) {
                mGroupInfoHasLeafletLL.setVisibility(View.VISIBLE);
                mGroupInfoHasLeafletTmpLL.setVisibility(View.GONE);
            } else {
                mGroupInfoHasLeafletLL.setVisibility(View.GONE);
                mGroupInfoHasLeafletTmpLL.setVisibility(View.VISIBLE);
            }

            if (mGroupBookingInfoModel.getOvernight_material() != null &&
                    mGroupBookingInfoModel.getOvernight_material() == 1) {
                mGroupInfoHasGoodsHelpLL.setVisibility(View.VISIBLE);
                mGroupInfoHasGoodsHelpTmpLL.setVisibility(View.GONE);
            } else {
                mGroupInfoHasGoodsHelpLL.setVisibility(View.GONE);
                mGroupInfoHasGoodsHelpTmpLL.setVisibility(View.VISIBLE);
            }
            if (mGroupInfoHasPowerLL.getVisibility() == View.GONE
                    && mGroupInfoHasChairLL.getVisibility() == View.GONE &&
                    mGroupInfoHasTentLL.getVisibility() == View.GONE &&
                    mGroupInfoHasLeafletLL.getVisibility() == View.GONE &&
                    mGroupInfoHasGoodsHelpLL.getVisibility() == View.GONE) {
                mGroupNoServiceLL.setVisibility(View.VISIBLE);
            } else {
                mGroupNoServiceLL.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getDoBegin() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getDoFinish() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getDoBegin().length() > 0 &&
                    mGroupBookingInfoModel.getPhysical_resource().getDoFinish().length() > 0) {
                mGroupInfoResStallTimeTV.setText(mGroupBookingInfoModel.getPhysical_resource().getDoBegin() + "-" +
                        mGroupBookingInfoModel.getPhysical_resource().getDoFinish());
            } else {
                mGroupInfoResStallTimeTV.setText(getResources().getString(R.string.groupbooding_no_data_str));
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getTotal_area() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getTotal_area() > 0) {
                mGroupInfoResTotalAreaTV.setText(String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getTotal_area()) +
                        getResources().getString(R.string.myselfinfo_company_demand_area_unit_text));
            } else {
                mGroupInfoResTotalAreaTV.setText(getResources().getString(R.string.groupbooding_no_data_str));
            }
            // : 2017/10/19 物业要求显示
            if ((mGroupBookingInfoModel.getPhysical_resource().getRequirement() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getRequirement().size() > 0) ||
                    (mGroupBookingInfoModel.getPhysical_resource().getProperty_requirement() != null &&
                            mGroupBookingInfoModel.getPhysical_resource().getProperty_requirement().length() > 0)) {
                String property_requirement = "";
                if (mGroupBookingInfoModel.getPhysical_resource().getRequirement() != null &&
                        mGroupBookingInfoModel.getPhysical_resource().getRequirement().size() > 0) {
                    for (int i = 0; i < mGroupBookingInfoModel.getPhysical_resource().getRequirement().size(); i++) {
                        if (mGroupBookingInfoModel.getPhysical_resource().getRequirement().get(i).getDisplay_name() != null &&
                                mGroupBookingInfoModel.getPhysical_resource().getRequirement().get(i).getDisplay_name().length() > 0) {
                            if (i != 0) {
                                property_requirement = property_requirement + "、" +
                                        mGroupBookingInfoModel.getPhysical_resource().getRequirement().get(i).getDisplay_name();
                            } else {
                                property_requirement = property_requirement +
                                        mGroupBookingInfoModel.getPhysical_resource().getRequirement().get(i).getDisplay_name();
                            }
                        }
                    }
                }
                if (mGroupBookingInfoModel.getPhysical_resource().getProperty_requirement() != null
                        && mGroupBookingInfoModel.getPhysical_resource().getProperty_requirement().length() > 0) {
                    if (mGroupBookingInfoModel.getPhysical_resource().getRequirement() != null
                            && mGroupBookingInfoModel.getPhysical_resource().getRequirement().size() > 0) {
                        property_requirement = property_requirement + "、" + mGroupBookingInfoModel.getPhysical_resource().getProperty_requirement();
                    } else {
                        property_requirement = property_requirement + mGroupBookingInfoModel.getPhysical_resource().getProperty_requirement();
                    }
                }
                mGroupInfoResPropertyRequirementTV.setText(property_requirement);
            } else {
                mGroupInfoResPropertyRequirementTV.setText(getResources().getString(R.string.groupbooding_no_data_str));
            }

            if ((mGroupBookingInfoModel.getPhysical_resource().getContraband() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getContraband().size() > 0) ||
                    (mGroupBookingInfoModel.getPhysical_resource().getOther_contraband() != null &&
                            mGroupBookingInfoModel.getPhysical_resource().getOther_contraband().length() > 0)) {
                String contraband_str = "";
                if (mGroupBookingInfoModel.getPhysical_resource().getContraband() != null &&
                        mGroupBookingInfoModel.getPhysical_resource().getContraband().size() > 0) {
                    for (int i = 0; i < mGroupBookingInfoModel.getPhysical_resource().getContraband().size(); i++) {
                        if (mGroupBookingInfoModel.getPhysical_resource().getContraband().get(i).getDisplay_name() != null &&
                                mGroupBookingInfoModel.getPhysical_resource().getContraband().get(i).getDisplay_name().length() > 0) {
                            if (i != 0) {
                                contraband_str = contraband_str + "、" + mGroupBookingInfoModel.getPhysical_resource().getContraband().get(i).getDisplay_name();
                            } else {
                                contraband_str = contraband_str + mGroupBookingInfoModel.getPhysical_resource().getContraband().get(i).getDisplay_name();
                            }
                        }
                    }
                }
                if (mGroupBookingInfoModel.getPhysical_resource().getOther_contraband() != null &&
                        mGroupBookingInfoModel.getPhysical_resource().getOther_contraband().length() > 0) {
                    if (mGroupBookingInfoModel.getPhysical_resource().getContraband() != null &&
                            mGroupBookingInfoModel.getPhysical_resource().getContraband().size() > 0) {
                        contraband_str = contraband_str + "、" + mGroupBookingInfoModel.getPhysical_resource().getOther_contraband();
                    } else {
                        contraband_str = contraband_str + mGroupBookingInfoModel.getPhysical_resource().getOther_contraband();
                    }
                }
                mGroupInfoResContrabandTV.setText(contraband_str);
            } else {
                mGroupInfoResContrabandTV.setText(getResources().getString(R.string.groupbooding_no_data_str));
            }

            if (mGroupBookingInfoModel.getPhysical_resource().getPeak_time() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getPeak_time().getDisplay_name() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getPeak_time().getDisplay_name().length() > 0) {
                mGroupInfoResPeakTimeTV.setText(mGroupBookingInfoModel.getPhysical_resource().getPeak_time().getDisplay_name());
            } else {
                mGroupInfoResPeakTimeTV.setText(getResources().getString(R.string.groupbooding_no_data_str));
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getMale_proportion() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getMale_proportion() > 0) {
                mGroupInfoGenderRatioTimeTV.setText(getResources().getString(R.string.fieldinfo_man_proportion_text) +
                        String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getMale_proportion()) + getResources().getString(R.string.fieldinfo_man_proportion_unit_text) +
                        "," + getResources().getString(R.string.fieldinfo_woman_proportion_text) +
                        String.valueOf(100 - mGroupBookingInfoModel.getPhysical_resource().getMale_proportion()) + getResources().getString(R.string.fieldinfo_man_proportion_unit_text));
            } else {
                mGroupInfoGenderRatioTimeTV.setText(getResources().getString(R.string.groupbooding_no_data_str));
            }
            //历史单量字段确认
            if (mGroupBookingInfoModel.getPhysical_resource().getInformation() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getInformation().length() > 0) {
                mFieldinfoSaleVolumTV.setText(mGroupBookingInfoModel.getPhysical_resource().getInformation());
            } else {
                mFieldinfoSaleVolumLL.setVisibility(View.GONE);
            }
            // : 2017/10/23 社区信息
//            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getName() != null &&
//                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getName().length() > 0) {
//                mGroupInfoCommunityNameTV.setText(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getName());
//                mcommunity_name_layout.setVisibility(View.GONE);
//            } else {
//                mGroupInfoCommunityNameTV.setText(getResources().getString(R.string.groupbooding_no_data_str));
//                mcommunity_name_layout.setVisibility(View.GONE);
//            }
//            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getDetailed_address() != null &&
//                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getDetailed_address().length() > 0) {
//                mGroupInfoCommunityAddressTV.setText(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getDetailed_address());
//                mcommunity_address_layout.setVisibility(View.GONE);
//            } else {
//                mcommunity_address_layout.setVisibility(View.GONE);
//            }
//            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getCommunity_type() != null &&
//                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getCommunity_type().getDisplay_name() != null &&
//                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getCommunity_type().getDisplay_name().length() > 0) {
//                mGroupInfoCommunityTypeTV.setText(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getCommunity_type().getDisplay_name());
//                mcommunity_type_layout.setVisibility(View.GONE);
//            } else {
//                mcommunity_type_layout.setVisibility(View.GONE);
//            }
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getFacilities() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getFacilities().length() > 0) {
                mCommunityFacilitiesTV.setText(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getFacilities());
                mCommunityFacilitiesLL.setVisibility(View.VISIBLE);
            } else {
                mCommunityFacilitiesLL.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getBuild_year() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getBuild_year().length() > 0) {
                mGroupInfoCommunityBuldYearTV.setText(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getBuild_year());
                mGroupInfoCommunityBuldYearLL.setVisibility(View.VISIBLE);
            } else {
                mGroupInfoCommunityBuldYearLL.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getBuilding_area() > 0) {
                mGroupInfoCommunityAreaTV.setText(String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getBuilding_area()) + "㎡");
                mcommunity_area_layout.setVisibility(View.VISIBLE);
            } else {
                mcommunity_area_layout.setVisibility(View.GONE);
            }
            //社区信息新的
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getOccupancy_rate() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getOccupancy_rate().length() > 0) {
                mCommunityOccupancyRateLL.setVisibility(View.VISIBLE);
                mCommunityOccupancyRateTV.setText(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getOccupancy_rate());
            } else {
                mCommunityOccupancyRateLL.setVisibility(View.GONE);
            }
            if (String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getProperty_costs()) != null) {
                mCommunityPropertyCostsTV.setText(com.linhuiba.business.connector.Constants.getpricestring(String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getProperty_costs()), 0.01) +
                        getResources().getString(R.string.fieldinfo_property_costs_unit_text));
            } else {
                mCommunityPropertyCostsLL.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getRent() > 0) {
                mCommunityRentTV.setText(com.linhuiba.business.connector.Constants.getpricestring(String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getRent()), 0.01) +
                        getResources().getString(R.string.fieldinfo_house_price_unit_text));
            } else {
                mCommunityRentLL.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getNumber_of_enterprises() > 0) {
                mCommunityEnterprisesTV.setText(String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getNumber_of_enterprises()).toString());
            } else {
                mCommunityEnterprisesLL.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getFacade() != null && mGroupBookingInfoModel.getPhysical_resource().getFacade() > 0) {
                mtxt_number_of_people_facade_layout.setVisibility(View.VISIBLE);
                mtxt_number_of_people_facade.setText(String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getFacade()) +"面");
            } else {
                mtxt_number_of_people_facade_layout.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getDescription() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getDescription().length() > 0) {
                mtxt_description_ll.setVisibility(View.VISIBLE);
                mtxt_description.setText(mGroupBookingInfoModel.getPhysical_resource().getDescription());
            } else {
                mtxt_description_ll.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getHas_carport() == 1) {
                mtxt_park.setText(getResources().getString(R.string.fieldinfo_carport_text));
                if (mGroupBookingInfoModel.getPhysical_resource().getCharging_standard() != null && mGroupBookingInfoModel.getPhysical_resource().getCharging_standard().length() > 0) {
                    mtxt_park.setText(getResources().getString(R.string.fieldinfo_carport_text) +","+
                            mGroupBookingInfoModel.getPhysical_resource().getCharging_standard());
                }
            } else {
                mtxt_park_layout.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getParticipation_level() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getParticipation_level().getDisplay_name() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getParticipation_level().getDisplay_name().length() > 0) {
                mparticipation.setVisibility(View.VISIBLE);
                mtxt_participation.setText(mGroupBookingInfoModel.getPhysical_resource().getParticipation_level().getDisplay_name());
            } else {
                mparticipation.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getAge_level() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getAge_level().getDisplay_name() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getAge_level().getDisplay_name().length() > 0) {
                mtxt_age_group.setText(mGroupBookingInfoModel.getPhysical_resource().getAge_level().getDisplay_name());
            } else {
                mage_group.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getConsumption_level() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getConsumption_level().getDisplay_name() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getConsumption_level().getDisplay_name().length() > 0) {
                mtxt_consumption_level_ll.setVisibility(View.VISIBLE);
                mtxt_consumption_level.setText(mGroupBookingInfoModel.getPhysical_resource().getConsumption_level().getDisplay_name());
            } else {
                mtxt_consumption_level_ll.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getConstruction_class() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getConstruction_class().getDisplay_name() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getConstruction_class().getDisplay_name().length() > 0) {
                mcommunity_grade_layout.setVisibility(View.VISIBLE);
                mcommunity_grade_text.setText(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getConstruction_class().getDisplay_name());
            } else {
                mcommunity_grade_layout.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getTradingarea() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getTradingarea().getName() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getTradingarea().getName().length() > 0) {
                mcommunity_business_district_layout.setVisibility(View.VISIBLE);
                mcommunity_business_district_text.setText(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getTradingarea().getName());
            } else {
                mcommunity_business_district_layout.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getNumber_of_households() > 0) {
                mfieldinfo_community_number_of_households_text.setText(String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getNumber_of_households()).toString());
            } else {
                mfieldinfo_community_number_of_households_ll.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getHouse_price() > 0) {
                mfieldinfo_community_house_price_text.setText(com.linhuiba.business.connector.Constants.getpricestring(String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getHouse_price()), 0.01) +
                        getResources().getString(R.string.fieldinfo_house_price_unit_text));
            } else {
                mfieldinfo_community_house_price_ll.setVisibility(View.GONE);
            }
            //配套餐厅
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getRestaurant() == 1 &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getNumber_of_seat() != null) {
                mtxt_restaurant_layout.setVisibility(View.VISIBLE);
                mtxt_restaurant.setText(getResources().getString(R.string.fieldinfo_restaurant_text) +
                        String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getNumber_of_seat()) +
                        getResources().getString(R.string.fieldinfo_restaurant_unit_text));
            } else {
                mtxt_restaurant_layout.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getTotal_number_of_people() > 0) {
                mcommunity_total_number_of_people_text.setText(String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getTotal_number_of_people()));
            } else {
                mcommunity_total_number_of_people_layout.setVisibility(View.GONE);
            }
            //企业类型
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getEnterprise_type() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getEnterprise_type().getDisplay_name() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getEnterprise_type().getDisplay_name().length() > 0) {
                mtxt_enterprises_type_layout.setVisibility(View.VISIBLE);
                mtxt_enterprises_type.setText(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getEnterprise_type().getDisplay_name());
            } else {
                mtxt_enterprises_type_layout.setVisibility(View.GONE);
            }
            //超市定位
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getSupermarket_type() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getSupermarket_type().getDisplay_name() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getSupermarket_type().getDisplay_name().length() > 0) {
                mtxt_supermarket_type_layout.setVisibility(View.VISIBLE);
                mtxt_supermarket_type.setText(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getSupermarket_type().getDisplay_name());
            } else {
                mtxt_supermarket_type_layout.setVisibility(View.GONE);
            }
            //商场定位
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getShopping_mall_type() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getShopping_mall_type().getDisplay_name() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getShopping_mall_type().getDisplay_name().length() > 0) {
                mtxt_shopping_mall_type_layout.setVisibility(View.VISIBLE);
                mtxt_shopping_mall_type.setText(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getShopping_mall_type().getDisplay_name());
            } else {
                mtxt_shopping_mall_type_layout.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getNumber_of_households() != 0) {
                mnumber_of_commercial_textview.setText(String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getNumber_of_households()).toString());
                mnumber_of_commercial_layout.setVisibility(View.VISIBLE);
            } else {
                mnumber_of_commercial_layout.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getHouse_attribute() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getHouse_attribute().getDisplay_name() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getHouse_attribute().getDisplay_name().length() > 0) {
                mhousing_attribute_textview.setText(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getHouse_attribute().getDisplay_name());
                mhousing_attribute_layout.setVisibility(View.VISIBLE);
            } else {
                mhousing_attribute_layout.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getAverage_fare() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getAverage_fare().length() > 0) {
                maveragea_ticket_price_textivew.setText(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getAverage_fare());
                maveragea_ticket_price_layout.setVisibility(View.VISIBLE);
            } else {
                maveragea_ticket_price_layout.setVisibility(View.GONE);
            }
            if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getCity() != null &&
                    mGroupBookingInfoModel.getPhysical_resource().getCommunity().getCity().getId() > 0) {
                mCityIdStr = String.valueOf(mGroupBookingInfoModel.getPhysical_resource().getCommunity().getCity().getId());
            }

            //评价列表显示
            if (mGroupBookingInfoModel.getPhysical_resource().getCount_of_reviews() != null) {
                mGroupInfoReviewNumberTV.setText("(" + mGroupBookingInfoModel.getPhysical_resource().getCount_of_reviews() + ")");
            }
            // 2018/2/28 使用mvp框架
            if (LoginManager.isLogin()) {
                //其他火爆拼团
                if (mGroupBookingInfoModel.getPhysical_resource().getCommunity().getId() != null &&
                        mGroupBookingInfoModel.getId() != null) {
                    FieldApi.getGroupBookingList(MyAsyncHttpClient.MyAsyncHttpClient(), mOtherResHandler, 1, LoginManager.getInstance().getTrackcityid(),6,mGroupBookingInfoModel.getGroup_purchase().getId());
                }
            }
            mGroupPresenter.getResInfoReview(String.valueOf(mGroupBookingInfoModel.getId()), "1", "2",false);
            mGroupInfoInformationRL.setVisibility(View.VISIBLE);
            mGroupInfoPaidRL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNearbyResSuccess(ArrayList<ResourceSearchItemModel> list) {

    }

    @Override
    public void onNearbyResMoreSuccess(ArrayList<ResourceSearchItemModel> list) {

    }

    @Override
    public void onOtherPhyResSuccess(ArrayList<ResourceSearchItemModel> list, int csort,int total) {

    }


    @Override
    public void onOtherPhyResMoreSuccess(ArrayList<ResourceSearchItemModel> list) {

    }

    @Override
    public void onCommunityInfoSuccess(CommunityInfoModel resInfo) {

    }

    @Override
    public void onRecommendResSuccess(ArrayList<ResourceSearchItemModel> list) {

    }

    @Override
    public void onRecommendResMoreSuccess(ArrayList<ResourceSearchItemModel> list) {

    }
    @Override
    public void onFeedbacksSuccess() {
        mCorrectionSubmitErrorTV.setEnabled(true);
        if (mRemindDialog.isShowing()) {
            mRemindDialog.dismiss();
        }
        MessageUtils.showToast(getResources().getString(R.string.fieldinfo_error_recovery_content_remindsuccess_text));
    }

    @Override
    public void onFeedbacksFailure(boolean superresult, Throwable error) {
        mCorrectionSubmitErrorTV.setEnabled(true);
        if (!superresult) {
            MessageUtils.showToast(getContext(), error.getMessage());
        }
        checkAccess_new(error);
    }

    @Override
    public void onEnquirySuccess(String id) {

    }

    @Override
    public void onEnquiryFailure(boolean superresult, Throwable error) {

    }
}
