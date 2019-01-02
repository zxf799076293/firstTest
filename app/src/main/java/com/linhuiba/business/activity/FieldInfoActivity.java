package com.linhuiba.business.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.baselib.app.util.NetworkUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.linhuiba.business.CalendarClass.FieldinfoChooseSpecificationsAdapter;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.FieldInfoEnquiryAdapter;
import com.linhuiba.business.adapter.FieldInfoPriceSizeAdapter;
import com.linhuiba.business.adapter.FieldinfoRecommendResourcesAdapter;
import com.linhuiba.business.adapter.Fieldinfo_ReviewAdapter;
import com.linhuiba.business.adapter.GlideImageLoader;
import com.linhuiba.business.adapter.MyCouponsAdapter;
import com.linhuiba.business.adapter.ResourcesScreeningItemAdapter;
import com.linhuiba.business.adapter.ResourcesScreeningNewAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.connector.OnMultiClickListener;
import com.linhuiba.business.connector.PushReceiver;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.fragment.HomeFragment;
import com.linhuiba.business.fragment.MyselfFragment;
import com.linhuiba.business.fragment.SearchListFragment;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.model.CommunityInfoModel;
import com.linhuiba.business.model.FieldInfoModel;
import com.linhuiba.business.model.FieldInfoSellResourceModel;
import com.linhuiba.business.model.FieldInfoSellResourcePriceModel;
import com.linhuiba.business.model.FieldInfoSizeModel;
import com.linhuiba.business.model.GroupBookingInfoModel;
import com.linhuiba.business.model.MyCouponsModel;
import com.linhuiba.business.model.PhyResInfoModel;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.business.model.ReviewModel;
import com.linhuiba.business.mvpmodel.LoginMvpModel;
import com.linhuiba.business.mvppresenter.CouponsMvpPresenter;
import com.linhuiba.business.mvppresenter.FieldinfoMvpPresenter;
import com.linhuiba.business.mvppresenter.LoginMvpPresenter;
import com.linhuiba.business.mvpview.CouponsMvpView;
import com.linhuiba.business.mvpview.FieldinfoMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.HorizontalListView;
import com.linhuiba.business.view.MyGridview;
import com.linhuiba.business.view.MyListView;
import com.linhuiba.business.view.MyLoadMoreExpandablelistView;
import com.linhuiba.linhuifield.fieldactivity.FieldAddFieldGuideActivity;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldAttributesModel;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuifield.fieldview.Field_MyScrollview;
import com.linhuiba.linhuifield.fieldview.ModuleViewAddfieldCommunityInfo;
import com.linhuiba.linhuifield.fieldview.OvalImageView;
import com.linhuiba.linhuifield.fieldview.ZoomImageView;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.linhuipublic.config.SupportPopupWindow;
import com.meiqia.core.MQManager;
import com.meiqia.core.MQMessageManager;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnEndConversationCallback;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.squareup.picasso.Picasso;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.magicwindow.mlink.annotation.MLinkRouter;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Administrator on 2016/3/8.
 */
@MLinkRouter(keys={"resourceDetail"})
public class FieldInfoActivity extends BaseMvpActivity implements Field_AddFieldChoosePictureCallBack.FieldreviewCall, Field_AddFieldChoosePictureCallBack.Fieldsize_PriceunitCall,
        Field_MyScrollview.OnScrollListener,Field_AddFieldChoosePictureCallBack.AddfieldFourCall, FieldinfoMvpView, CouponsMvpView {
    @InjectView(R.id.txt_fieldtitle)
    TextView mtxt_fieldtitle;
    @InjectView(R.id.txt_field_description)
    TextView mtxt_field_description;
    @InjectView(R.id.fieldinfo_phy_size_tv)
    TextView mFieldinfoPhySizeTV;
    @InjectView(R.id.fieldinfo_number_of_people_tv)
    TextView mFieldinfoNumberOfPeopleTV;
    @InjectView(R.id.txt_transaction_success)
    TextView mtxt_transaction_success;
    @InjectView(R.id.fieldinfo_industry_tv)
    TextView mFieldInfoIndustryTV;
    @InjectView(R.id.txt_stall_time)
    TextView mtxt_stall_time;
    @InjectView(R.id.fieldinfo_location_indoor_tv)
    TextView mFieldinfoLocationIndoorTV;
    @InjectView(R.id.fieldinfo_location_specific_tv)
    TextView mFieldinfoLocationTV;
    @InjectView(R.id.txt_contraband)
    TextView mtxt_contraband;
    @InjectView(R.id.contraband_ll)
    LinearLayout mContrabandLL;

    @InjectView(R.id.txt_address)
    TextView mtxt_address;
    @InjectView(R.id.review_number_txt)
    TextView mreview_number_txt;
    @InjectView(R.id.review_listview)
    MyListView mreview_listview;
    @InjectView(R.id.evaluation_linearlayout)
    LinearLayout mevaluation_layout;
    @InjectView(R.id.fieldinfo_view_visibility)
    RelativeLayout mfieldinfo_view_visibility;
    @InjectView(R.id.txt_total_area)
    TextView mtxt_total_area;
    @InjectView(R.id.activity_overdue_btn)
    TextView mactivity_overdue_btn;
    @InjectView(R.id.txt_number_of_people_peak_time) TextView mtxt_number_of_people_peak_time;



    @InjectView(R.id.txt_gender_ratio)  TextView mtxt_gender_ratio;
    @InjectView(R.id.gender_ratio)  LinearLayout mgender_ratio;
    @InjectView(R.id.txt_property_requirement)  TextView mtxt_property_requirement;//物业要求
    @InjectView(R.id.txt_property_requirement_layout)  LinearLayout mtxt_property_requirement_layout;
    @InjectView(R.id.all_relative_layout) LinearLayout mall_relative_layout;
    @InjectView(R.id.fieldinfo_error_recovery_layout)  LinearLayout mfieldinfo_error_recovery_layout;
    @InjectView(R.id.no_resources_layout)  LinearLayout mno_resources_layout;
    @InjectView(R.id.no_resources_textview)  TextView mno_resources_textview;
    @InjectView(R.id.no_resources_btn)  Button mno_resources_btn;
    @InjectView(R.id.activityinfo_layout) LinearLayout mactivityinfo_layout;
    @InjectView(R.id.activityinfo_start_date_text) TextView mactivityinfo_start_date_text;
    @InjectView(R.id.activityinfo_end_date_text) TextView mactivityinfo_end_date_text;
    @InjectView(R.id.activityinfo_date_text) TextView mactivityinfo_date_text;
    @InjectView(R.id.fieldinfo_pricelist_layout) LinearLayout mfieldinfo_pricelist_layout;
    @InjectView(R.id.fieldinfo_address_layout) LinearLayout mfieldinfo_address_layout;
    @InjectView(R.id.fieldinfo_information_classify_layout) LinearLayout mfieldinfo_information_classify_layout;
    @InjectView(R.id.scrollview) Field_MyScrollview mscrollview;
    @InjectView(R.id.fieldinfo_other_res_gridlistview)
    RecyclerView mFieldinfoOtherResRV;
    @InjectView(R.id.fieldinfo_other_res_layout) LinearLayout mFieldinfoOtherResLL;
    @InjectView(R.id.fieldinfo_other_res_tv) TextView mFieldinfoOtherResTV;
    @InjectView(R.id.fieldinfo_activity_list_ll) LinearLayout mFieldinfoActivityLL;
    @InjectView(R.id.fieldinfo_activity_rv) RecyclerView mFieldinfoActivityRV;
    @InjectView(R.id.resource_communityinfo_layout) LinearLayout mresource_communityinfo_layout;
    @InjectView(R.id.fieldinfo_banner) Banner mFieldInfoBanner;
    @InjectView(R.id.fieldinfo_navbar_titile) LinearLayout mFieldInfoNavbarTtileLL;
    @InjectView(R.id.fieldinfo_status_bar_ll) LinearLayout mFieldInfoStatusBarLL;
    @InjectView(R.id.fieldinfo_scroll_ll) LinearLayout mFieldInfoScrollLL;
    @InjectView(R.id.fieldinfo_second_level_ll) LinearLayout mFieldInfoSecondLevelLL;
    @InjectView(R.id.fieldinfo_recommend_gridlistview) RecyclerView mFieldinfoRecommendRV;
    @InjectView(R.id.fieldinfo_loadmore_ll)
    LinearLayout mFieldInfoLoadMoreLL;
    @InjectView(R.id.fieldinfo_loadmore_nulldata_tv)
    TextView mFieldInfoNullTV;
    @InjectView(R.id.fieldinfo_recommend_typelayout) LinearLayout mFieldinfoRecommendTypeLL;
    @InjectView(R.id.fieldinfo_level_tv0)
    TextView mFieldInfoLabelTV0;
    @InjectView(R.id.fieldinfo_level_tv1)
    TextView mFieldInfoLabelTV1;
    @InjectView(R.id.fieldinfo_level_tv2)
    TextView mFieldInfoLabelTV2;

    @InjectView(R.id.fieldinfo_age_label_ll)
    LinearLayout mFieldInfoAgeLabelLL;
    @InjectView(R.id.fieldinfo_size_lv)
    MyLoadMoreExpandablelistView mFieldInfoSizeLV;
    @InjectView(R.id.fieldinfo_size_no_tv)
    TextView mFieldinfoSizeNoTV;
    @InjectView(R.id.fieldinfo_screen_view)
    View mFieldInfoScreenView;
    @InjectView(R.id.fieldinfo_enquiry_lv)
    MyListView mFieldinfoEnquiryLV;
    @InjectView(R.id.fieldinfo_no_screen_ll)
    LinearLayout mFieldinfoNoScreenLL;
    @InjectView(R.id.fieldinfo_no_screen_gv)
    MyGridview mFieldinfoNoScreenGV;
    @InjectView(R.id.fieldinfo_have_deposit_cb)
    CheckBox mFieldinfoHaveDepositCB;
    @InjectView(R.id.fieldinfo_shortcut_deposit_cb)
    CheckBox mFieldinfoDepositCB;
    @InjectView(R.id.fieldinfo_shortcut_leasetermtype_cb)
    CheckBox mFieldinfoLeaseTermTypeCB;
    @InjectView(R.id.fieldinfo_shortcut_size_cb)
    CheckBox mFieldinfoSizeCB;
    @InjectView(R.id.fieldinfo_screen_checkbox)
    TextView mFieldinfoScreenTV;
    @InjectView(R.id.fieldinfo_choose_size_ll)
    LinearLayout mFieldinfoChooseSizeLL;
    @InjectView(R.id.fieldinfo_no_data_ll)
    RelativeLayout mFieldinfoNoDataLL;
    @InjectView(R.id.fieldinfo_show_more_view_rl)
    RelativeLayout mFieldinfoShowMoreRL;
    @InjectView(R.id.fieldinfo_show_all_tv)
    TextView mFieldinfoShowAllTV;
    @InjectView(R.id.txt_number_of_people_facade) TextView mtxt_number_of_people_facade;
    @InjectView(R.id.txt_description_tablerow)
    LinearLayout mtxt_description_ll;
    @InjectView(R.id.txt_description)
    TextView mtxt_description;
    @InjectView(R.id.txt_participation)  TextView mtxt_participation;
    @InjectView(R.id.txt_consumption_level) TextView mtxt_consumption_level;
    @InjectView(R.id.fieldinfo_dolocation_tv) TextView mFieldinfoDoLocationTV;
    @InjectView(R.id.fieldinfo_sales_volume_ll) LinearLayout mFieldinfoSaleVolumLL;
    @InjectView(R.id.fieldinfo_sales_volume_tv) TextView mFieldinfoSaleVolumTV;
    //场地信息
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
    @InjectView(R.id.community_info_show_all_tv)
    TextView mCommunityinfoShowAllTV;
    @InjectView(R.id.community_info_other_info_ll)
    LinearLayout mCommunityOtherInfoLL;
    @InjectView(R.id.module_fieldinfo_focuson_imav)
    ImageView mFieldInfoFousonImgv;
    @InjectView(R.id.fieldinfo_receive_coupons_ll)
    LinearLayout mFieldinfoReceiveCouponsLL;
    @InjectView(R.id.fieldinfo_receive_coupons_tv)
    TextView mFieldinfoCouponsPrice;
    @InjectView(R.id.fieldinfo_coupons_tv1)
    TextView mFieldinfoCoupons1;
    @InjectView(R.id.fieldinfo_coupons_tv2)
    TextView mFieldinfoCoupons2;
    @InjectView(R.id.fieldinfo_coupons_tv3)
    TextView mFieldinfoCoupons3;
    @InjectView(R.id.fieldinfo_screening_condition_ll)
    LinearLayout mFieldinfoScreenConditionLL;
    @InjectView(R.id.fieldinfo_screening_price_tv)
    TextView mFieldinfoScreenPriceTV;
    @InjectView(R.id.fieldinfo_screening_area_tv)
    TextView mFieldinfoScreenAreaTV;
    @InjectView(R.id.fieldinfo_screening_person_tv)
    TextView mFieldinfoScreenPersonTV;
    @InjectView(R.id.fieldinfo_distance_location_tv)
    TextView mFieldInfoDistanceTV;
    @InjectView(R.id.fieldinfo_distance_location_ll)
    LinearLayout mFieldInfoDistanceLL;
    @InjectView(R.id.fieldinfo_no_sell_res_reserve_tv)
    TextView mFieldInfoReserveTV;
    @InjectView(R.id.module_fieldinfo_focuson_ll)
    LinearLayout mFieldinfoFocusonLL;
    @InjectView(R.id.fieldinfo_activity_description_tv)
    TextView mFieldInfoActivityDescriptionTV;
    @InjectView(R.id.fieldinfo_activity_description_ll)
    LinearLayout mFieldinfoActivityDescriptionLL;
    @InjectView(R.id.fieldinfo_bottom_ll)
    LinearLayout mFieldinfoBottomLL;
    @InjectView(R.id.fieldinfo_other_res_show_all_tv)
    TextView mFieldinfoOtherResShowAllTV;
    @InjectView(R.id.fieldinfo_other_res_show_all_ll)
    LinearLayout mFieldinfoOtherResShowAllLL;
    // FIXME: 2018/12/14 专属顾问

    @InjectView(R.id.fieldinfo_counselor_imgv)
    OvalImageView mFieldinfoCounselorImgv;
    @InjectView(R.id.fieldinfo_counselor_name_tv)
    TextView mFieldinfoCounselorNameTV;
    @InjectView(R.id.fieldinfo_counselor_description_tv)
    TextView mFieldinfoCounselorDescriptionTV;
    @InjectView(R.id.fieldinfo_counselor_ll)
    LinearLayout mFieldinfoCounseLL;

    //场地信息
    @InjectView(R.id.fieldinfo_communityinfo_ll)
    LinearLayout mCommunityLL;
    @InjectView(R.id.fieldinfo_community_name_tv)
    TextView mCommunityNameTV;
    @InjectView(R.id.fieldinfo_community_price_tv)
    TextView mCommunityPriceTV;
    @InjectView(R.id.fieldinfo_community_build_year_ll)
    LinearLayout mCommunityBuildYearLL;
    @InjectView(R.id.fieldinfo_community_build_year_tv)
    TextView mCommunityBuildYearTV;
    @InjectView(R.id.fieldinfo_community_people_ll)
    LinearLayout mCommunityPeopleLL;
    @InjectView(R.id.fieldinfo_community_people_tv)
    TextView mCommunityPeopleTV;
    @InjectView(R.id.fieldinfo_community_business_ll)
    LinearLayout mCommunityBusinessLL;
    @InjectView(R.id.fieldinfo_community_business_tv)
    TextView mCommunityBusinessTV;
    @InjectView(R.id.fieldinfo_community_imgv)
    OvalImageView mCommunityImgv;
    @InjectView(R.id.fieldinfo_banner_num_tv)
    TextView mFieldinfoBannerNumTV;
    @InjectView(R.id.fieldinfo_banner_size_tv)
    TextView mFieldinfoBannerSizeTV;
    @InjectView(R.id.fieldinfo_recommend_ll)
    LinearLayout mFieldinfoRecommendLL;
    // FIXME: 2018/12/22 图文详情
    @InjectView(R.id.fieldinfo_pic_word_info_ll)
    LinearLayout mFieldinfoPicWordLL;
    @InjectView(R.id.fieldinfo_pic_word_info_webview)
    BridgeWebView mFieldinfoPicWordWebview;

    private ArrayList<String> mPicList = new ArrayList<String>();//展位+案例图片
    private ArrayList<String> mCasePicList = new ArrayList<String>();//案例图片
    private ArrayList<String> JsonArrayDatelistString = new ArrayList<String>();//选择规格后的所有时间的组合string
    private String getfieldid;//展位id
    private ArrayList<ReviewModel> mReviewList;//评价的list
    private Fieldinfo_ReviewAdapter mFieldEvaluationAdapter;//场地评价的adapter
    private IWXAPI api;
    private Dialog pw;
    private String ShareTitleStr = "";//分享的标题
    private String mSharePYQTitleStr;
    private String SharedescriptionStr = "";//分享的描述
    private String ShareIconStr = "";//分享的图片的url
    private Bitmap ShareBitmap = null;//分享需要的bitmap
    private Bitmap miniShareBitmap = null;//小程序分享需要的bitmap
    public PhyResInfoModel resourceinfolist = new PhyResInfoModel();//场地详情的model
    private ArrayList<FieldInfoSizeModel> mDimensionsDataList = new ArrayList<>();//筛选完后的list 界面上显示的
    private ArrayList<FieldInfoSizeModel> mDimensionsDataScreenList = new ArrayList<>();//复制的所有规格的list  所有的
    public String size = "";//选中的规格大小
    public String lease_term_type = "";//选中的规格时间
    public int mLeaseTermTypeId;//选中的规格时间单位
    public String custom_dimension= "";//选中的特殊规格
    private SimpleDateFormat chooseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public ArrayList<String> Closing_dates = new ArrayList<>();//不能选择的规格的时间
    public HashMap<String, String> weatherliststr = new HashMap<>();//天气的string
    private RelativeLayout maction_layout_top;//taitlebar的layout
    private ImageView mFieldInfoTitleBackImg;
    private ImageView mFieldInfoTitleShareImg;
    private ImageView mFieldInfoTitleCardImg;
    private RelativeLayout mFieldinfoTitleCardRL;
    private QBadgeView mCardSizeBv;
    private TextView mFieldInfoTitleTV;
    private RelativeLayout mFieldInfoTitleRL;
    private HashMap<String,String> term_type_days_map = new HashMap<String, String>();//规格中的时间的周期（例：半年是几天）
    public Date checkedstart_date;//选择规格的开始时间
    public Date checkedend_date;//选择规格的结束时间
    private Dialog newdialog;
    private String share_linkurl = "";//微信分享的url
    private String sharewxMiniShareLinkUrl = "";//小程序分享的url
    public boolean isGetSecondNavHeight;//是否重新获取二级导航栏tab功能的高度
    private FieldinfoRecommendResourcesAdapter mOtherPhyResAdapter;
    private FieldinfoRecommendResourcesAdapter mActivityAdapter;
    private ArrayList<ResourceSearchItemModel> mFieldInfoOtherDataList = new ArrayList<>();
    private ArrayList<ResourceSearchItemModel> mFieldInfoOtherDataListTemp = new ArrayList<>();
    private ArrayList<ResourceSearchItemModel> mFieldInfoActivityList = new ArrayList<>();
    private int previousPointEnale;//图片滑动隐藏的上一点的position
    //根据size中的判断
    public int days_in_advance = 2;//提前几天预约

    private Dialog zoom_picture_dialog;//详情页预览大图dialog
    private boolean mIsRefreshZoomImageview = true;//详情页变了是否重新获取预览大图
    private List<ImageView> mImageViewList;
    //几天起订在size中
    private int minimum_order_quantity = 1;//几天起订
    //押金在size中
    private double deposit = 0;//押金
    private TextView txt_error_correction_submit;//纠错奖励提交按钮
    private GlideImageLoader mFieldinfoImageLoader;
    private DisplayMetrics mDisplayMetrics;
    private Dialog mReviewZoomPictureDialog;
    private List<ImageView> mDialogImageViewList = new ArrayList<>();
    private boolean mDislogIsRefreshZoomImg = true;
    private int mImageListSize;//图片数量
    private int mNewPosition;//显示的图片position
    public int mFieldinfoRecommendInt;//推荐
    public int mFieldinfoSpecificationInt;//规格
    public int mFieldinfoCommunityInt;//详情
    public int mFieldifnoReviewInt;//评价
    private TextView[] mSecondNavTextList = new TextView[4];
    private ImageView[] mSecondNavImgList = new ImageView[4];
    private Dialog mFieldinfoChooseSizeDialog;
    private String mCurrentDateStr = "";
    private int mCurrentYear= 0;
    private int mCurrentMonth = 0;
    private int mCurrentDay = 0;
    private TextView mCurrentMonthTV;
    private ImageButton mPrevMonthImgBtn;
    private ImageButton mNextMonthImgBtn;
    /** 每次添加gridview到viewflipper中时给的标记 */
    public int mGVFlag = 0;
    private ViewFlipper mViewFlipper = null;//日历装gridview的控件
    private LinearLayout mViewFlipperLL;
    private FieldinfoChooseSpecificationsAdapter mChooseSpecificationsAdapter = null;//日历控件的adapter
    private int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    public ArrayList<Date> datelist = new ArrayList<>();//选中的下单或加入购物车的时间数组
    private MyGridview mGridView = null;//日历控件
    private ScrollView mGridScroView = null;//日历控件
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private float Touchx1 = 0;
    private float Touchx2 = 0;
    public GestureDetector myGridViewGestureListener;
    private int mFieldinfoRecommendPageInt;//推荐列表加载的page
    private ArrayList<ResourceSearchItemModel> mRecommendDateList = new ArrayList<>();
    private FieldinfoRecommendResourcesAdapter mRecommendAdapter;
    private int mFavoriteStatus;
    private int mSizeGroupItemPosition;
    private int mSizeChildItemPosition;
    private String mResAddressStr = "";
    private int mPayId;
    private HashMap<String, Integer> mLeaseTermTypeMap = new HashMap<>();
    private ArrayList<String> mLeaseTermTypeStrList = new ArrayList<String>();//筛选显示的时间单位的list
    private ArrayList<String> mSizeStrList = new ArrayList<String>();
    private ArrayList<String> mDepositStrList = new ArrayList<String>();
    private ArrayList<String> mCustomDimensionStrList = new ArrayList<String>();
    private ArrayList<String> mLeaseTermTypeCheckedStrList = new ArrayList<String>();
    private ArrayList<String> mSizeCheckedStrList = new ArrayList<String>();
    private ArrayList<String> mDepositCheckedStrList = new ArrayList<String>();
    private ArrayList<String> mCustomDimensionCheckedStrList = new ArrayList<String>();
    private SupportPopupWindow mFieldinfoScreenPW;
    private FieldInfoPriceSizeAdapter adapter;
    private int mTitleBarInt;
    private CheckBox mFieldinfoPwDepositCB;
    private CheckBox mFieldinfoPwHaveDepositCB;
    private CheckBox mFieldinfoPwLeaseTermTypeCB;
    private CheckBox mFieldinfoPwSizeCB;
    private TextView mFieldinfoPwScreenTV;
    private View mFieldinfoScreenPwView;
    private Drawable mSortGreyUpDrawable;//排序
    private Drawable mSortGreyDownDrawable;//排序
    private Drawable mSortBlueUpDrawable;//排序
    private Drawable mSortBlueDownDrawable;//排序
    private Drawable mShowAllUpDrawable;//收起场地信息
    private Drawable mShowAllDownDrawable;//查看场地信息
    private ArrayList<HashMap<Object,Object>> screemDataList;
    //2018/1/10 规格天和周几的配置
    public HashMap<Integer,Integer> mWeekLeaseTermMap = new HashMap<Integer,Integer>();//0-6代表周一到七 对应id
    public HashMap<Integer,String> mWeekLeaseTermNameMap = new HashMap<Integer,String>();//0-6代表的显示的周一到周日的名称（可能是星期几）
    public HashMap<Integer,String> mWeekLeaseTermPriceMap = new HashMap<Integer,String>();//周一到周日对应的价格
    public HashMap<Integer,String> mWeekLeaseTermDepositMap = new HashMap<Integer,String>();//周一到周日对应的押金
    private boolean isShowAllFieldRes = true;//判断是否展开所有场地信息
    private FieldinfoMvpPresenter mFieldinfoMvpPresenter;
    private static final int ADD_CARTS_REQUESTCODE = 1;
    private static final int CREATE_ORDER_REQUESTCODE = 2;
    private static final int ENQUIRY_REQUESTCODE = 3;
    private static final int COUPONS_REQUESTCODE = 5;
    private static final int DEMAND_RESULTCODE = 6;
    private static final int DEMAND_RESULTCODE_LOGIN = 7;
    private int mEnquiryClickPosition;
    private int mCommunityId;
    private boolean isShowAllCommunity = true;
    public String mActivityStartDate;
    public String mDeadline;
    private SupportPopupWindow mCouponsPW;
    public CouponsMvpPresenter mCouponsMvpPresenter;
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
    private ApiResourcesModel mApiResourcesModel = new ApiResourcesModel();
    private String mScreenMinPrice = "";
    private String mScreenMaxPrice = "";
    public boolean isReceiveClick;//是否是领取优惠券 不是兑换
    private CustomDialog mIntegralDialog;//积分兑换的dialog
    public int mCsort = 1;//展位几
    private boolean isDaySpecification;//是否是以天为单位的押金
    private TextView mDepositTV;//弹出选择规格页面的押金
    private boolean isSellResource;//是否是供给
    private String mActivityDescription;//活动描述
    private boolean isActivityRes;//是否是活动供给
    private String mSellResId;
    private CustomDialog mDemandSuccessDialog;//需求提交完成dialog
    public LocationClient mLocationClient = null;
    private static final int LOACTION_REQUEST_INT = 10;//权限 requestcode
    private int mStatusBarHeight = 70;
    //分享的界面
    private View mShareView;
    private ImageView mShareImageView;//分享的图
    private TextView mShareDescriptionTV;//分享的地址
    private LinearLayout mSharePriceLL;
    private TextView mSharePriceTV;//分享的价格
    private ImageView mShareSubsidyImgv;//分享的优惠
    private TextView mShareActivityTimeTV;//分享的活动时间
    public boolean isShareSubsidy;
    private boolean isShowAllOtherRes = false;//判断是否展开所有场地其他展位
    private final int CALL_PHONE_CODE = 110;
    private final int CALL_SERVICE_PHONE_CODE = 111;
    private int mCityId;
    private int mCategoryId;
    private boolean isRefreshSize;//刷新规格使用
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_info_new);
        ButterKnife.inject(this);
        initview();
    }
    private void initview() {
        if (LoginManager.getInstance().getPhyinfo_show_guide() == 0) {
            Intent addfield = new Intent(FieldInfoActivity.this, FieldAddFieldGuideActivity.class);
            addfield.putExtra("show_type",5);
            startActivity(addfield);
        }
        mFieldinfoMvpPresenter = new FieldinfoMvpPresenter();
        mFieldinfoMvpPresenter.attachView(this);
        mCouponsMvpPresenter = new CouponsMvpPresenter();
        mCouponsMvpPresenter.attachView(this);
        setSteepStatusBar();
        int stausBarHeight = getSteepStatusBarHeight();
        if (stausBarHeight > 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    stausBarHeight);
            mFieldInfoStatusBarLL.setLayoutParams(layoutParams);
            mStatusBarHeight = 44 + com.linhuiba.linhuifield.connector.Constants.Px2Dp(FieldInfoActivity.this,stausBarHeight);
        }
        mFieldInfoScrollLL.setBackgroundColor(getResources().getColor(R.color.white));
        mFieldinfoImageLoader = new GlideImageLoader(FieldInfoActivity.this,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH,
                        com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT);
        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        //顶部导航栏控件设置
        TitleBarUtils.showBackImg(this, true);
        maction_layout_top = (RelativeLayout)findViewById(R.id.common_title_bar).findViewById(R.id.action_layout_top);
        mFieldInfoTitleShareImg = (ImageView)findViewById(R.id.common_title_bar).findViewById(R.id.action_img_top);
        mFieldInfoTitleBackImg = (ImageView)findViewById(R.id.common_title_bar).findViewById(R.id.back_button_top);
        mFieldInfoTitleCardImg = (ImageView)findViewById(R.id.common_title_bar).findViewById(R.id.business_titlebar_right_card_img);
        mFieldinfoTitleCardRL = (RelativeLayout)findViewById(R.id.business_titlebar_right_rl);
        mFieldInfoTitleTV = (TextView)findViewById(R.id.common_title_bar).findViewById(R.id.title);
        mFieldInfoTitleTV.setTextColor(getResources().getColor(R.color.white));
        mFieldInfoTitleRL = (RelativeLayout)findViewById(R.id.about_title);
        mFieldInfoTitleRL.setBackgroundColor(getResources().getColor(R.color.color_null));
        mFieldInfoTitleTV.setVisibility(View.GONE);
        mFieldinfoOtherResRV.setNestedScrollingEnabled(false);
        mFieldinfoActivityRV.setNestedScrollingEnabled(false);
        mFieldinfoRecommendRV.setNestedScrollingEnabled(false);
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
        //
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        Intent getSearchlistintent = getIntent();
        if (getSearchlistintent.getExtras() != null) {
            //2017/12/13 魔窗
            String id = getSearchlistintent.getStringExtra("resource_id");
            getfieldid = getSearchlistintent.getStringExtra("fieldId");
            if (getSearchlistintent.getExtras().get("community_id") != null) {
                mCommunityId = getSearchlistintent.getExtras().getInt("community_id");
            }
            if (getSearchlistintent.getExtras().get("model") != null) {
                mApiResourcesModel = (ApiResourcesModel) getSearchlistintent.getExtras().get("model");
            }
            if (getSearchlistintent.getExtras().get("is_sell_res") != null) {
                isSellResource = getSearchlistintent.getBooleanExtra("is_sell_res",false);
                mSellResId = getSearchlistintent.getStringExtra("sell_res_id");
            }
            if (id != null) {
                getfieldid = id;
            }
            TitleBarUtils.setTitleText(this,getResources().getString(R.string.module_fieldinfo_title));
        }
        mscrollview.setOnScrollListener(this);
        findViewById(R.id.all_relative_layout).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScroll(mscrollview.getScrollY());
                System.out.println(mscrollview.getScrollY());
            }
        });
        mreview_listview.setFocusable(false);
        mreview_listview.setVisibility(View.GONE);
        //2018/1/18 先显示固定的ui
        mFieldinfoNoDataLL.setVisibility(View.VISIBLE);
        mno_resources_layout.setVisibility(View.GONE);
        maction_layout_top.setVisibility(View.VISIBLE);
        // FIXME: 2018/12/10 分享修改
        mShareView = LayoutInflater.from(FieldInfoActivity.this).inflate(R.layout.module_share_bg, null);
        mShareImageView = (ImageView) mShareView.findViewById(R.id.app_share_imgv);
        mShareDescriptionTV = (TextView) mShareView.findViewById(R.id.app_share_descriptive);
        mSharePriceLL = (LinearLayout) mShareView.findViewById(R.id.app_share_price_ll);
        mSharePriceTV = (TextView) mShareView.findViewById(R.id.app_share_price_tv);
        mShareSubsidyImgv = (ImageView) mShareView.findViewById(R.id.app_share_subsidy_imgv);
        mShareActivityTimeTV = (TextView) mShareView.findViewById(R.id.app_share_activity_time_tv);

        TitleBarUtils.showActionImg(this, true, getResources().getDrawable(R.drawable.ic_share_white), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 分享功能
                if (ShareBitmap == null || miniShareBitmap == null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (resourceinfolist.getPhysical_resource().getPhysical_resource_imgs()!= null &&
                                    resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().size() > 0 &&
                                    resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().get(0).get("pic_url") != null &&
                                    resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().get(0).get("pic_url").length() > 0) {
//                                ShareIconStr = resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().get(0).get("pic_url").toString()+ com.linhuiba.linhuipublic.config.Config.Linhui_Mid_Watermark;
//                                ShareBitmap = com.linhuiba.linhuifield.connector.Constants.GetLocalOrNetBitmap(ShareIconStr);
//                                ShareBitmap = com.linhuiba.linhuifield.connector.Constants.addWaterMark(ShareBitmap,SharedescriptionStr,FieldInfoActivity.this);
                                // FIXME: 2018/12/10 分享修改
                                ShareBitmap = com.linhuiba.linhuifield.connector.Constants.addWaterMark(mShareView,FieldInfoActivity.this);
                                ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                                ShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 370, 296, true);//压缩Bitmap
                                miniShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 120, 120, true);//压缩Bitmap
                                if (ShareBitmap == null) {
//                                    ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                                    ShareBitmap = com.linhuiba.linhuifield.connector.Constants.addWaterMark(mShareView,FieldInfoActivity.this);
                                    ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                                    ShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 370, 296, true);//压缩Bitmap
                                }
                                if (miniShareBitmap == null) {
                                    miniShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                                }
                            } else {
//                                ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                                ShareBitmap = com.linhuiba.linhuifield.connector.Constants.addWaterMark(mShareView,FieldInfoActivity.this);
                                ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                                ShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 370, 296, true);//压缩Bitmap
                                miniShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                            }
                            mHandler.sendEmptyMessage(0);
                        }
                    }).start();
                } else {
                    View myView = FieldInfoActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                    pw = new AlertDialog.Builder(FieldInfoActivity.this).create();
                    if (pw!= null && pw.isShowing()) {
                        pw.dismiss();
                    }
                    Constants constants = new Constants(FieldInfoActivity.this,
                            ShareIconStr);
                    constants.shareWXMiniPopupWindow(FieldInfoActivity.this,myView,pw,api,share_linkurl,
                            ShareTitleStr,
                            SharedescriptionStr, ShareBitmap,sharewxMiniShareLinkUrl,miniShareBitmap,mSharePYQTitleStr);
                }
            }
        });
        TitleBarUtils.showTitleBarRightCard(this, true, R.drawable.nav_ic_shopping_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartitemsintent = new Intent(FieldInfoActivity.this, ResourcesCartItemsActivity.class);
                startActivity(cartitemsintent);
            }
        });
        mCardSizeBv = new QBadgeView(FieldInfoActivity.this.getContext());
        mCardSizeBv.bindTarget(mFieldinfoTitleCardRL).setShowShadow(false).setBadgeGravity(Gravity.END|Gravity.TOP).setBadgeBackgroundColor(getResources().getColor(R.color.price_tv_color));
        mfieldinfo_error_recovery_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        show_paysuccess_PopupWindow(1);
                    }
                },10);
            }
        });
        mFieldInfoSizeLV.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;//返回true,表示不可点击
            }
        });

        //筛选控件
        mFieldinfoScreenPwView = FieldInfoActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_screen_pw, null);
        mFieldinfoPwHaveDepositCB = (CheckBox) mFieldinfoScreenPwView.findViewById(R.id.fieldinfo_pw_have_deposit_cb);
        mFieldinfoPwDepositCB = (CheckBox) mFieldinfoScreenPwView.findViewById(R.id.fieldinfo_pw_deposit_cb);
        mFieldinfoPwLeaseTermTypeCB = (CheckBox) mFieldinfoScreenPwView.findViewById(R.id.fieldinfo_pw_leasetermtype_cb);
        mFieldinfoPwSizeCB = (CheckBox) mFieldinfoScreenPwView.findViewById(R.id.fieldinfo_pw_size_cb);
        mFieldinfoPwScreenTV = (TextView) mFieldinfoScreenPwView.findViewById(R.id.fieldinfo_pw_screen_tv);
        mSortGreyUpDrawable = getResources().getDrawable(R.drawable.ic_open_gary_one_button_normal_three);
        mSortGreyUpDrawable.setBounds(0, 0, mSortGreyUpDrawable.getMinimumWidth(), mSortGreyUpDrawable.getMinimumHeight());
        mSortGreyDownDrawable = getResources().getDrawable(R.drawable.ic_open_gary_button_normal_three);
        mSortGreyDownDrawable.setBounds(0, 0, mSortGreyDownDrawable.getMinimumWidth(), mSortGreyDownDrawable.getMinimumHeight());
        mSortBlueUpDrawable = getResources().getDrawable(R.drawable.ic_openup_button_blue_normal_three);
        mSortBlueUpDrawable.setBounds(0, 0, mSortBlueUpDrawable.getMinimumWidth(), mSortBlueUpDrawable.getMinimumHeight());
        mSortBlueDownDrawable = getResources().getDrawable(R.drawable.ic_open_one_button_blue_normal_three);
        mSortBlueDownDrawable.setBounds(0, 0, mSortBlueDownDrawable.getMinimumWidth(), mSortBlueDownDrawable.getMinimumHeight());
        mShowAllUpDrawable = getResources().getDrawable(R.drawable.ic_close_green);
        mShowAllUpDrawable.setBounds(0, 0, mShowAllUpDrawable.getMinimumWidth(), mShowAllUpDrawable.getMinimumHeight());
        mShowAllDownDrawable = getResources().getDrawable(R.drawable.ic_into_green);
        mShowAllDownDrawable.setBounds(0, 0, mShowAllDownDrawable.getMinimumWidth(), mShowAllDownDrawable.getMinimumHeight());
        initdata();
    }
    private void initdata() {
        mFieldinfoOtherResLL.setVisibility(View.GONE);
        mFieldinfoActivityLL.setVisibility(View.GONE);
        mFieldinfoRecommendTypeLL.setVisibility(View.GONE);
        if (isSellResource) {
            if (mSellResId != null && mSellResId.length() > 0) {
                //2018/11/14 供给详情接口调用
                mFieldinfoMvpPresenter.getSellResInfo(mSellResId);
                if (LoginManager.isLogin()) {
                    String parameter = "/"+ mSellResId;
                    LoginMvpModel.sendBrowseHistories("activity_detail",parameter,LoginManager.getTrackcityid());
                }
            }
        } else {
            if (getfieldid != null && getfieldid.length() > 0) {
                //展位详情接口调用
                // FIXME: 2018/12/18 测试id
                mFieldinfoMvpPresenter.getResInfo(getfieldid,mApiResourcesModel);
                //浏览记录
                if (LoginManager.isLogin()) {
                    String parameter = "/"+ getfieldid;
                    LoginMvpModel.sendBrowseHistories("booth_detail",parameter,LoginManager.getTrackcityid());
                }
                //其他展位
                if (mCommunityId > 0) {
                    mFieldinfoMvpPresenter.getOtherPhyRes(mCommunityId,1,100, Integer.parseInt(getfieldid),mApiResourcesModel);
                }
                //展位下的所有供给（活动）
                mFieldinfoMvpPresenter.getSellRes(getfieldid,"3");
            }
        }
        if (mCommunityId > 0) {
            if (LoginManager.isLogin()) {
                //未领取
                mCouponsMvpPresenter.getCommunityCoupons(mCommunityId,1,100,1);
                ///已领取
                mCouponsMvpPresenter.getCommunityCoupons(mCommunityId,2,1,1);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResources().getString(R.string.fieldinfo_activity_name_str));
        MobclickAgent.onResume(this);
        if (LoginManager.isLogin()) {
            getCardSize();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResources().getString(R.string.fieldinfo_activity_name_str));
        MobclickAgent.onPause(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ShareBitmap != null) {
            ShareBitmap.recycle();
        }
        if (miniShareBitmap != null) {
            miniShareBitmap.recycle();
        }
        if (pw!= null && pw.isShowing()) {
            pw.dismiss();
        }
        if (mFieldinfoMvpPresenter != null) {
            mFieldinfoMvpPresenter.detachView();
        }
        if (mCouponsMvpPresenter != null) {
            mCouponsMvpPresenter.detachView();
        }
    }

    private LinhuiAsyncHttpResponseHandler FavoriteslistitemHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            if (mFavoriteStatus == 1) {
                mFieldInfoFousonImgv.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_ic_collection));
                mFavoriteStatus = 0;
                MessageUtils.showToast(getResources().getString(R.string.myselfinfo_deletefocuson_txt));
            } else {
                mFieldInfoFousonImgv.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_collection_red));
                mFavoriteStatus = 1;
                MessageUtils.showToast(getResources().getString(R.string.myselfinfo_addfocuson_txt));
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            if (!superresult) {
                MessageUtils.showToast(getContext(), error.getMessage());
            }
            checkAccess_new(error);
        }
    };
    @OnClick({
            R.id.evaluation_layout,
            R.id.fieldinfo_address_layout,
            R.id.fieldinfo_second_level_res_info_ll,
            R.id.fieldinfo_second_level_specification_ll,
            R.id.fieldinfo_second_level_community_ll,
            R.id.fieldinfo_second_level_review_ll,
            R.id.fieldinfo_show_all_ll,
            R.id.fieldinfo_have_deposit_cb,
            R.id.fieldinfo_shortcut_deposit_cb,
            R.id.fieldinfo_shortcut_leasetermtype_cb,
            R.id.fieldinfo_shortcut_size_cb,
            R.id.fieldinfo_screen_checkbox,
            R.id.fieldinfo_mobile_tv,
            R.id.fieldinfo_change_explain_ll,
            R.id.fieldinfo_service_tv,
            R.id.community_info_show_all_ll,
            R.id.module_fieldinfo_focuson_ll,
            R.id.fieldinfo_industry_tv,
            R.id.fieldinfo_receive_coupons_ll,
            R.id.fieldinfo_screening_price_tv,
            R.id.fieldinfo_screening_area_tv,
            R.id.fieldinfo_screening_person_tv,
            R.id.fieldinfo_no_sell_res_reserve_tv,
            R.id.fieldinfo_other_res_show_all_ll,
            R.id.fieldinfo_counselor_call_tv,
            R.id.fieldinfo_counselor_wx_tv,
            R.id.fieldinfo_goto_communityinfo_ll,
            R.id.fieldinfo_counselor_tv
    })
    public void fieldinfoclick(View view) {
        switch (view.getId()) {
            case R.id.evaluation_layout:
                if (LoginManager.isLogin()) {
                    Intent review = new Intent(FieldInfoActivity.this, FieldEvaluationActivity.class);
                    review.putExtra("fieldid", getfieldid);
                    startActivity(review);
                } else {
                    new AlertDialog.Builder(FieldInfoActivity.this)
                            .setTitle(getResources().getString(R.string.module_fieldinfo_more_review))
                            .setNegativeButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent Lofinintent = new Intent(FieldInfoActivity.this, LoginActivity.class);
                                    startActivity(Lofinintent);
                                }
                            })
                            .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();

                                }
                            }).show();
                }
                break;
            case R.id.fieldinfo_address_layout:
                Intent mapinfo_intent = new Intent(FieldInfoActivity.this,FieldinfoMapinfoActivity.class);
                mapinfo_intent.putExtra("longitude",resourceinfolist.getPhysical_resource().getCommunity().getLng());
                mapinfo_intent.putExtra("latitude",resourceinfolist.getPhysical_resource().getCommunity().getLat());
                mapinfo_intent.putExtra("resourcename",resourceinfolist.getPhysical_resource().getName());
                mapinfo_intent.putExtra("address",mResAddressStr);
                startActivity(mapinfo_intent);
                break;
            case R.id.fieldinfo_second_level_res_info_ll:
                mscrollview.smoothScrollTo(0, mFieldinfoRecommendInt);
                showSecondLevelView(0);
                break;
            case R.id.fieldinfo_second_level_specification_ll:
                mscrollview.smoothScrollTo(0, mFieldinfoSpecificationInt);
                showSecondLevelView(1);
                break;
            case R.id.fieldinfo_second_level_community_ll:
                mscrollview.smoothScrollTo(0, mFieldinfoCommunityInt);
                showSecondLevelView(2);
                break;
            case R.id.fieldinfo_second_level_review_ll:
                mscrollview.smoothScrollTo(0, mFieldifnoReviewInt);
                showSecondLevelView(3);
                break;
            case R.id.fieldinfo_show_all_ll:
                isGetSecondNavHeight = true;
                if (isShowAllFieldRes) {
                    isShowAllFieldRes = false;
                    mFieldinfoShowAllTV.setCompoundDrawables(null, null, mShowAllDownDrawable, null);
                    mresource_communityinfo_layout.setVisibility(View.GONE);
                    mFieldinfoShowAllTV.setText(getResources().getString(R.string.fieldinfo_show_all_resource_info_str));
                } else {
                    mFieldinfoShowAllTV.setCompoundDrawables(null, null, mShowAllUpDrawable, null);
                    isShowAllFieldRes = true;
                    mresource_communityinfo_layout.setVisibility(View.VISIBLE);
                    mFieldinfoShowAllTV.setText(getResources().getString(R.string.fieldinfo_show_small_resource_info_str));
                }
                break;
            case R.id.fieldinfo_have_deposit_cb:
                if (mFieldinfoHaveDepositCB.isChecked()) {
                    mDepositCheckedStrList.add(getResources().getString(R.string.fieldinfo_have_deposit_str));
                    updataSizeLv(true);
                } else {
                    mDepositCheckedStrList.remove(getResources().getString(R.string.fieldinfo_have_deposit_str));
                    updataSizeLv(true);
                }
                setScreenStatus(null);
                break;
            case R.id.fieldinfo_shortcut_deposit_cb:
                if (mFieldinfoDepositCB.isChecked()) {
                    mDepositCheckedStrList.add(getResources().getString(R.string.fieldinfo_no_deposit_str));
                    updataSizeLv(true);
                } else {
                    mDepositCheckedStrList.remove(getResources().getString(R.string.fieldinfo_no_deposit_str));
                    updataSizeLv(true);
                }
                setScreenStatus(null);
                break;
            case R.id.fieldinfo_shortcut_leasetermtype_cb:
                if (mFieldinfoLeaseTermTypeCB.isChecked()) {
                    mLeaseTermTypeCheckedStrList.add(mFieldinfoLeaseTermTypeCB.getText().toString().split(" ")[1]);
                    updataSizeLv(true);
                } else {
                    mLeaseTermTypeCheckedStrList.remove(mFieldinfoLeaseTermTypeCB.getText().toString().split(" ")[1]);
                    updataSizeLv(true);
                }
                setScreenStatus(null);
                break;
            case R.id.fieldinfo_shortcut_size_cb:
                if (mFieldinfoSizeCB.isChecked()) {
                    mSizeCheckedStrList.add(mFieldinfoSizeCB.getText().toString());
                    updataSizeLv(true);
                } else {
                    mSizeCheckedStrList.remove(mFieldinfoSizeCB.getText().toString());
                    updataSizeLv(true);
                }
                setScreenStatus(null);
                break;
            case R.id.fieldinfo_screen_checkbox:
                if (mFieldinfoScreenPW != null &&
                        mFieldinfoScreenPW.isShowing()) {
                    mFieldinfoScreenPW.dismiss();
                } else {
                    showResourceScreeningPw();
                }
                break;
            case R.id.fieldinfo_mobile_tv:
                if (resourceinfolist.getService_phone() != null &&
                        resourceinfolist.getService_phone().length() > 0) {
                    showCounselorDialog(0,true,CALL_SERVICE_PHONE_CODE);
                }
                break;
            case R.id.fieldinfo_change_explain_ll:
                show_paysuccess_PopupWindow(0);
                break;
            case R.id.fieldinfo_service_tv:
                //客服功能
                AndPermission.with(FieldInfoActivity.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();
                break;
            case R.id.community_info_show_all_ll:
                isGetSecondNavHeight = true;
                if (isShowAllCommunity) {
                    isShowAllCommunity = false;
                    mCommunityinfoShowAllTV.setCompoundDrawables(null, null, mShowAllDownDrawable, null);
                    mCommunityOtherInfoLL.setVisibility(View.GONE);
                    mCommunityinfoShowAllTV.setText(getResources().getString(R.string.fieldinfo_show_all_resource_info_str));
                } else {
                    mCommunityinfoShowAllTV.setCompoundDrawables(null, null, mShowAllUpDrawable, null);
                    isShowAllCommunity = true;
                    mCommunityOtherInfoLL.setVisibility(View.VISIBLE);
                    mCommunityinfoShowAllTV.setText(getResources().getString(R.string.fieldinfo_show_small_resource_info_str));
                }
                break;
            case R.id.module_fieldinfo_focuson_ll:
                if (LoginManager.isLogin()) {
                    if (resourceinfolist.getSize().size() > 0 && resourceinfolist.getSize().get(0).getResource().size() > 0) {
                        if (mFavoriteStatus == 0) {
                            FieldApi.addfavoriteslistitem(MyAsyncHttpClient.MyAsyncHttpClient_version_two(), FavoriteslistitemHandler,
                                    String.valueOf(resourceinfolist.getSize().get(0).getResource().get(0).getId()));
                        } else {
                            FieldApi.deletefavoriteslistitem(FieldInfoActivity.this, MyAsyncHttpClient.MyAsyncHttpClient(), FavoriteslistitemHandler,
                                    String.valueOf(resourceinfolist.getSize().get(0).getResource().get(0).getId()));
                        }
                    } else {
                        MessageUtils.showToast(getResources().getString(R.string.field_tupesize_errortoast));
                    }
                } else {
                    Intent login = new Intent(FieldInfoActivity.this, LoginActivity.class);
                    startActivity(login);
                }
                break;
            case R.id.fieldinfo_industry_tv:
                if (resourceinfolist.getIndustry_str() != null &&
                        resourceinfolist.getIndustry_str().length() > 0) {
                    show_paysuccess_PopupWindow(3);
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
                    Intent loginIntent = new Intent(FieldInfoActivity.this,LoginActivity.class);
                    startActivityForResult(loginIntent,COUPONS_REQUESTCODE);
                }
                break;
            case R.id.fieldinfo_screening_price_tv:
                if (mCommunityId > 0) {
                    mApiResourcesModel.setMin_price(null);
                    mApiResourcesModel.setMax_price(null);
                }
                // FIXME: 2018/12/27 刷新规格请求接口
                if (getfieldid != null && getfieldid.length() > 0) {
                    //展位详情接口调用
                    // FIXME: 2018/12/18 测试id
                    isRefreshSize = true;
                    mFieldinfoMvpPresenter.getResInfo(getfieldid,mApiResourcesModel);
                }
                mFieldinfoScreenPriceTV.setVisibility(View.GONE);
                if (mFieldinfoScreenPersonTV.getVisibility() == View.GONE &&
                        mFieldinfoScreenAreaTV.getVisibility() == View.GONE) {
                    mFieldinfoScreenConditionLL.setVisibility(View.GONE);
                }
                break;
            case R.id.fieldinfo_screening_area_tv:
                if (mCommunityId > 0) {
                    mApiResourcesModel.setMax_area(null);
                    mApiResourcesModel.setMin_area(null);
                }
                mFieldinfoScreenAreaTV.setVisibility(View.GONE);
                if (mFieldinfoScreenPersonTV.getVisibility() == View.GONE &&
                        mFieldinfoScreenPriceTV.getVisibility() == View.GONE) {
                    mFieldinfoScreenConditionLL.setVisibility(View.GONE);
                }
                break;
            case R.id.fieldinfo_screening_person_tv:
                if (mCommunityId > 0) {
                    mApiResourcesModel.setMin_person_flow(null);
                    mApiResourcesModel.setMax_person_flow(null);
                }
                mFieldinfoScreenPersonTV.setVisibility(View.GONE);
                if (mFieldinfoScreenPriceTV.getVisibility() == View.GONE &&
                        mFieldinfoScreenAreaTV.getVisibility() == View.GONE) {
                    mFieldinfoScreenConditionLL.setVisibility(View.GONE);
                }
                break;
            case R.id.fieldinfo_no_sell_res_reserve_tv:
                if (resourceinfolist.getPhysical_resource().getId() != null) {
                    if (LoginManager.isLogin()) {
                        startDemandActivity();
                    } else {
                        Intent intent = new Intent(FieldInfoActivity.this, LoginActivity.class);
                        startActivityForResult(intent,DEMAND_RESULTCODE_LOGIN);
                    }
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                }
                break;
            case R.id.fieldinfo_other_res_show_all_ll:
                if (isShowAllOtherRes) {
                    isShowAllOtherRes = false;
                    mFieldinfoOtherResShowAllTV.setCompoundDrawables(null, null, mShowAllDownDrawable, null);
                    mFieldinfoOtherResShowAllTV.setText(getResources().getString(R.string.module_fieldinfo_other_res_show));
                    // FIXME: 2018/12/14 其他展位收起
                    mFieldInfoOtherDataListTemp.clear();
                    for (int i = 0; i < 3; i++) {
                        mFieldInfoOtherDataListTemp.add(mFieldInfoOtherDataList.get(i));
                    }
                    mOtherPhyResAdapter.notifyDataSetChanged();
                } else {
                    mFieldinfoOtherResShowAllTV.setCompoundDrawables(null, null, mShowAllUpDrawable, null);
                    isShowAllOtherRes = true;
                    mFieldinfoOtherResShowAllTV.setText(getResources().getString(R.string.module_fieldinfo_other_res_pack_up));
                    mFieldInfoOtherDataListTemp.clear();
                    mFieldInfoOtherDataListTemp.addAll(mFieldInfoOtherDataList);
                    mOtherPhyResAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.fieldinfo_counselor_call_tv:
                // FIXME: 2018/12/14 联系顾问
                if (resourceinfolist.getService_representative() != null &&
                        resourceinfolist.getService_representative().getTel() != null &&
                        resourceinfolist.getService_representative().getTel().length() > 0) {
                    showCounselorDialog(0,true,CALL_PHONE_CODE);
                }
                break;
            case R.id.fieldinfo_counselor_wx_tv:
                // FIXME: 2018/12/14 加顾问微信
                if (resourceinfolist.getService_representative() != null &&
                        resourceinfolist.getService_representative().getQrcode() != null &&
                        resourceinfolist.getService_representative().getQrcode().length() > 0) {
                    showCounselorDialog(1,false,0);
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                }
                break;
            case R.id.fieldinfo_goto_communityinfo_ll:
                Intent fieldinfo = new Intent(FieldInfoActivity.this, CommunityInfoActivity.class);
                if (resourceinfolist.getPhysical_resource().getCommunity().getCity() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getCity().getId() > 0) {
                    fieldinfo.putExtra("city_id", resourceinfolist.getPhysical_resource().getCommunity().getCity().getId());
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getId() > 0) {
                    fieldinfo.putExtra("id", resourceinfolist.getPhysical_resource().getCommunity().getId());
                }
                startActivity(fieldinfo);
                break;
            case R.id.fieldinfo_counselor_tv:
                // FIXME: 2018/12/20 顾问
                if (resourceinfolist.getService_representative() != null &&
                        resourceinfolist.getService_representative().getId() > 0) {
                    showCounselorDialog(0,false,0);
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_CARTS_REQUESTCODE) {
            if (LoginManager.isLogin()) {
                showProgressDialog();
                FieldApi.addshopcart_items(MyAsyncHttpClient.MyAsyncHttpClient_version_two(), addToShoppingCartHandler, getsubmitjsonstr());
                if (mFieldinfoChooseSizeDialog != null &&
                        mFieldinfoChooseSizeDialog.isShowing()) {
                    mFieldinfoChooseSizeDialog.dismiss();
                }
            }
        } else if (requestCode == CREATE_ORDER_REQUESTCODE) {
            if (LoginManager.isLogin()) {
                createOrderIntent();
            }
        } else if (requestCode == ENQUIRY_REQUESTCODE) {
            if (LoginManager.isLogin()) {
                startEnquiryIntent(mEnquiryClickPosition);
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
        } else if (requestCode == DEMAND_RESULTCODE_LOGIN) {
            if (LoginManager.isLogin()) {
                startDemandActivity();
            }
        }
        if (resultCode == 4) {
            if (data.getExtras() != null && data.getExtras().get("id") != null
                    && data.getExtras().getInt("id") > 0) {
                getfieldid = String.valueOf(data.getExtras().getInt("id"));
                Intent fieldinfo = null;
                fieldinfo = new Intent(FieldInfoActivity.this, FieldInfoActivity.class);
                fieldinfo.putExtra("fieldId", getfieldid);
                startActivity(fieldinfo);
                finish();
            }
        } else if (resultCode == DEMAND_RESULTCODE) {
            showDemandSuccessDialog();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private LinhuiAsyncHttpResponseHandler addToShoppingCartHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (response.code == 1) {
                if (pw != null) {
                    if (pw.isShowing()) {
                        pw.dismiss();
                    }
                }
                if (mCardSizeBv != null &&
                        mCardSizeBv.getBadgeText()  != null &&
                        mCardSizeBv.getBadgeText().toString().length() > 0) {
                    int addCardInt = 1;
                    if (mLeaseTermTypeId == -1) {
                        if (datelist.size() > 0) {
                            addCardInt = datelist.size();
                        }
                    }
                    mCardSizeBv.setBadgeNumber(addCardInt + Integer.parseInt(mCardSizeBv.getBadgeText().toString().trim()));
                } else {
                    int addCardInt = 1;
                    if (mLeaseTermTypeId == -1) {
                        if (datelist.size() > 0) {
                            addCardInt = datelist.size();
                        }
                    }
                    mCardSizeBv.setBadgeNumber(addCardInt);
                }
                LayoutInflater mLayoutInflater = LayoutInflater.from(FieldInfoActivity.this);
                final View mDialogView = mLayoutInflater.inflate(R.layout.activity_fieldinfo_dialogview, null);
                final Dialog mSuccessDialgo = new AlertDialog.Builder(FieldInfoActivity.this).create();
                Constants.show_dialog(mDialogView,mSuccessDialgo);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (mSuccessDialgo.isShowing()) {
                            mSuccessDialgo.dismiss();
                        }
                    }
                }, 500);
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess_new(error);
        }
    };
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    final View myView = FieldInfoActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                    pw = new AlertDialog.Builder(FieldInfoActivity.this).create();
                    if (pw!= null && pw.isShowing()) {
                        pw.dismiss();
                    }
                    Constants constants = new Constants(FieldInfoActivity.this,
                            ShareIconStr);
                    constants.shareWXMiniPopupWindow(FieldInfoActivity.this,myView,pw,api,share_linkurl,
                            ShareTitleStr,
                            SharedescriptionStr, ShareBitmap, sharewxMiniShareLinkUrl,miniShareBitmap,mSharePYQTitleStr);
                    break;
                case 1:
                    Bundle bundle = msg.getData();
                    if (bundle.get("distance") != null &&
                            bundle.get("distance").toString().length() > 0) {
                        mFieldInfoDistanceLL.setVisibility(View.VISIBLE);
                        mFieldInfoDistanceTV.setText(bundle.get("distance").toString());
                    }
                    break;
                case 2:
                    hideProgressDialog();
                    mIntegralDialog.dismiss();
                    MessageUtils.showToast(getResources().getString(R.string.save_success_prompt));
                    break;
                default:
                    break;
            }
        }

    };

    //判断date是否是周末
    private boolean getdateweektype(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
        {
            return true;
        }
        else {
            return false;
        }
    }
    private JSON getsubmitjsonstr() {
        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        if (mLeaseTermTypeId == -1) {//天
            for(int i = 0; i < datelist.size(); i++) {
                String datestr = chooseDateFormat.format(datelist.get(i));
                HashMap<String,Object> json = new HashMap<String,Object>();
                json.put("id", mDimensionsDataList.get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getId());
                json.put("date",datestr);
                json.put("size",size);
                json.put("count", 1);
                json.put("count_of_time_unit", 1);
                json.put("custom_dimension", custom_dimension);
                int weekInt = com.linhuiba.linhuifield.connector.Constants.getDayForWeek(datestr);
                if (weekInt > -1) {
                    if (mWeekLeaseTermPriceMap.get(
                            mWeekLeaseTermMap.get(weekInt)) != null) {
                        json.put("lease_term_type_id", mWeekLeaseTermMap.get(weekInt));
                    }
                }
                jsonArray.add(json);
            }
        } else {
            String datestr = chooseDateFormat.format(checkedstart_date);
            JSONObject json = new JSONObject();
            json.put("id", mDimensionsDataList.get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getId());
            json.put("date",datestr);
            json.put("size",size);
            json.put("lease_term_type_id", mLeaseTermTypeId);
            json.put("custom_dimension", custom_dimension);
            json.put("count", 1);
            json.put("count_of_time_unit", 1);
            jsonArray.add(json);
        }
        return jsonArray;
    }
    private ArrayList<HashMap<String,Object>> getsubmitorderlist() {
        String itemworkprice = "0";
        String imagepath = "";
        String mSubsidyStr = "0";
        if (mPicList != null) {
            if(mPicList.size() > 0) {
                imagepath = mPicList.get(0).toString();
            }
        }
        if (mLeaseTermTypeId != -1) {
            if (resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getPrice() != null &&
                    resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getPrice() > 0) {
                itemworkprice = String.valueOf(resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getPrice());
                if (resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getMin_after_subsidy() != null &&
                        resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getMin_after_subsidy() > 0) {
                    mSubsidyStr = Constants.getdoublepricestring(Double.parseDouble(
                            String.valueOf(resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getPrice())) -
                                    resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getMin_after_subsidy(),1);
                }

            }
        }
        ArrayList<HashMap<String,Object>> jsonArray = new ArrayList<HashMap<String,Object>>();
        if (mLeaseTermTypeId == -1) {//天
            for(int i = 0; i < datelist.size(); i++) {
                String datestr = chooseDateFormat.format(datelist.get(i));
                HashMap<String,Object> json = new HashMap<String,Object>();
                json.put("id", mPayId);
                json.put("community_id", mCommunityId);
                json.put("date",datestr);
                json.put("size",size);
                json.put("count", 1);
                json.put("custom_dimension", custom_dimension);
                json.put("count_of_time_unit", 1);
                json.put("price",itemworkprice);
                json.put("subsidy_fee",mSubsidyStr);
                int weekInt = com.linhuiba.linhuifield.connector.Constants.getDayForWeek(datestr);
                if (weekInt > -1) {
                    if (mWeekLeaseTermPriceMap.get(
                            mWeekLeaseTermMap.get(weekInt)) != null) {
                        json.put("lease_term_type_id", mWeekLeaseTermMap.get(weekInt));
                        json.put("lease_term_type", mWeekLeaseTermNameMap.get(weekInt));
                        if (Integer.parseInt(mWeekLeaseTermPriceMap.get(
                                mWeekLeaseTermMap.get(weekInt))) > 0) {
                            json.put("price",mWeekLeaseTermPriceMap.get(
                                    mWeekLeaseTermMap.get(weekInt)));
                            if (resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSubsidy_rate() != null) {
                                json.put("subsidy_fee",Constants.getdoublepricestring(
                                        Double.parseDouble(mWeekLeaseTermPriceMap.get(
                                                mWeekLeaseTermMap.get(weekInt))) *
                                                resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSubsidy_rate()* 0.01,1));
                            }
                        }
                    }
                }
                json.put("resourcename", resourceinfolist.getPhysical_resource().getName());
                json.put("imagepath",imagepath);
                if (resourceinfolist.getPhysical_resource().getIs_activity() == 0) {
                    if (resourceinfolist.getPhysical_resource().getField_type() != null &&
                            resourceinfolist.getPhysical_resource().getField_type().getDisplay_name() != null &&
                            resourceinfolist.getPhysical_resource().getField_type().getDisplay_name().length() > 0) {
                        json.put("field_type", resourceinfolist.getPhysical_resource().getField_type().getDisplay_name());
                    } else {
                        json.put("field_type", "");
                    }
                } else if (resourceinfolist.getPhysical_resource().getIs_activity() == 1) {
                    if (resourceinfolist.getPhysical_resource().getActivity_type() != null &&
                            resourceinfolist.getPhysical_resource().getActivity_type().getDisplay_name() != null &&
                            resourceinfolist.getPhysical_resource().getActivity_type().getDisplay_name().length() > 0) {
                        json.put("field_type", resourceinfolist.getPhysical_resource().getActivity_type().getDisplay_name());
                    } else {
                        json.put("field_type", "");
                    }
                }

                if (resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSubsidy_rate() != null &&
                        resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSubsidy_rate()  > 0) {
                    json.put("discount_rate", Constants.getdoublepricestring(resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSubsidy_rate()* 0.01,1));
                } else {
                    json.put("discount_rate", "0");
                }
                if (resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getTax_point() != null) {
                    if (resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getTax_point() > 0) {
                        json.put("tax_point", Constants.getdoublepricestring(resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getTax_point(),1));
                    } else {
                        json.put("tax_point", "0");
                    }
                } else {
                    json.put("tax_point", "0");
                }
                if (resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSpecial_tax_point() != null) {
                    if (resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSpecial_tax_point() > 0) {
                        json.put("special_tax_point", Constants.getdoublepricestring(resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSpecial_tax_point(),1));
                    } else {
                        json.put("special_tax_point", "0");
                    }
                } else {
                    json.put("special_tax_point", "0");
                }
                jsonArray.add(json);
            }
        } else {
            String datestr = chooseDateFormat.format(checkedstart_date);
            HashMap<String,Object> json = new HashMap<String,Object>();
            json.put("id", mDimensionsDataList.get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getId());
            json.put("community_id", mCommunityId);
            json.put("date",datestr);
            json.put("size",size);
            json.put("custom_dimension", custom_dimension);
            json.put("lease_term_type_id", mLeaseTermTypeId);
            json.put("lease_term_type", lease_term_type);
            json.put("count", 1);
            json.put("count_of_time_unit", 1);
            json.put("price",itemworkprice);
            json.put("subsidy_fee",mSubsidyStr);
            json.put("resourcename", resourceinfolist.getPhysical_resource().getName());
            json.put("imagepath",imagepath);
            if (resourceinfolist.getPhysical_resource().getField_type() != null &&
                    resourceinfolist.getPhysical_resource().getField_type().getDisplay_name() != null &&
                    resourceinfolist.getPhysical_resource().getField_type().getDisplay_name().length() > 0) {
                json.put("field_type", resourceinfolist.getPhysical_resource().getField_type().getDisplay_name());
            } else {
                json.put("field_type", "");
            }
            if (resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSubsidy_rate() != null &&
                    resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSubsidy_rate()  > 0) {
                json.put("discount_rate", Constants.getdoublepricestring(resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSubsidy_rate()* 0.01,1));
            } else {
                json.put("discount_rate", "0");
            }
            if (resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getTax_point() != null) {
                if (resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getTax_point() > 0) {
                    json.put("tax_point", Constants.getdoublepricestring(resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getTax_point(),1));
                } else {
                    json.put("tax_point", "0");
                }
            } else {
                json.put("tax_point", "0");
            }
            if (resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSpecial_tax_point() != null) {
                if (resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSpecial_tax_point() > 0) {
                    json.put("special_tax_point", Constants.getdoublepricestring(resourceinfolist.getSize().get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSpecial_tax_point(),1));
                } else {
                    json.put("special_tax_point", "0");
                }
            } else {
                json.put("special_tax_point", "0");
            }
            jsonArray.add(json);
        }
        return jsonArray;
    }
    @Override
    public void fieldshowreviewpicture(ArrayList<String> path, int position) {
        initPreviewZoomView(path,position);
    }
    public void showpreviewpicture(ArrayList<String> path,int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(path,position);
        test.showreview(this);
    }
    private void initPreviewZoomView(ArrayList<String> pathtmp,int position) {
        mReviewZoomPictureDialog = new AlertDialog.Builder(this).create();
        View mView = getLayoutInflater().inflate(com.linhuiba.linhuifield.R.layout.activity_fieldinfo_preview_zoom_picture_dialog, null);
        final TextView mShowPictureSizeTV = (TextView)mView.findViewById(com.linhuiba.linhuifield.R.id.showpicture_dialog_sizetxt);
        final TextView mShowDialogPictureBackTV = (TextView)mView.findViewById(com.linhuiba.linhuifield.R.id.showpicture_dialog_back);
        ViewPager mZoomViewPage = (ViewPager)mView.findViewById(com.linhuiba.linhuifield.R.id.zoom_dialog_viewpage);
        com.linhuiba.linhuifield.connector.Constants mConstants = new com.linhuiba.linhuifield.connector.Constants(FieldInfoActivity.this,FieldInfoActivity.this,mNewPosition,mImageListSize,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT);
        mConstants.showPicture(mView,mShowPictureSizeTV,mZoomViewPage,
                mReviewZoomPictureDialog,mDialogImageViewList,pathtmp,mDislogIsRefreshZoomImg,
                position,false);
        mZoomViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mNewPosition = position % mDialogImageViewList.size();
            }

            @Override
            public void onPageSelected(int position) {
                showProgressDialog();
                mShowPictureSizeTV.setText(String.valueOf(position % mDialogImageViewList.size()+1)+"/" + String.valueOf(mDialogImageViewList.size()));
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void show_paysuccess_PopupWindow(final int type) {
        if (newdialog != null && newdialog.isShowing()) {
            newdialog.dismiss();
        }
        LayoutInflater factory = LayoutInflater.from(FieldInfoActivity.this);
        final View textEntryView = factory.inflate(R.layout.activity_fieldinfo_refund_price_popuwindow, null);
        TextView mrefunt_price_detailed = (TextView) textEntryView.findViewById(R.id.refunt_price_detailed);
        ImageButton mrefunt_price_detailed_btn = (ImageButton) textEntryView.findViewById(R.id.fieldinfo_explain_close_imgbtn);
        RelativeLayout mfieldinfo_all_dialog_layout = (RelativeLayout) textEntryView.findViewById(R.id.fieldinfo_all_dialog_layout);
        RelativeLayout mfieldinfo_refunt_price_detailed_layout = (RelativeLayout) textEntryView.findViewById(R.id.fieldinfo_refunt_price_detailed_layout);
        RelativeLayout mfieldinfo_error_recovery_layout = (RelativeLayout) textEntryView.findViewById(R.id.fieldinfo_error_recovery_layout);
        LinearLayout mfieldinfo_error_recovery_top_layout = (LinearLayout) textEntryView.findViewById(R.id.fieldinfo_error_recovery_top_layout);
        TextView merror_correction_cancel_text = (TextView) textEntryView.findViewById(R.id.error_correction_cancel_text);
        txt_error_correction_submit = (TextView) textEntryView.findViewById(R.id.error_correction_submit_text);
        TextView merror_recovery_resourcesname_text = (TextView) textEntryView.findViewById(R.id.error_recovery_resourcesname_text);
        TextView mrefunt_price_text = (TextView) textEntryView.findViewById(R.id.refunt_price_text);
        final EditText merror_recovery_content_edit = (EditText) textEntryView.findViewById(R.id.error_recovery_content_edit);
        if (type == 0 || type == 2 || type == 3) {
            mrefunt_price_detailed_btn.setVisibility(View.VISIBLE);
            mfieldinfo_refunt_price_detailed_layout.setVisibility(View.VISIBLE);
            mfieldinfo_error_recovery_layout.setVisibility(View.GONE);
            if (type == 2) {
                mrefunt_price_text.setText(getResources().getString(R.string.fieldinfo_integral_reward_tv_str));
                mrefunt_price_detailed.setText(getResources().getString(R.string.fieldinfo_integral_reward_content_str));
            } else if (type == 0) {
                mrefunt_price_text.setText(getResources().getString(R.string.module_fieldinfo_change_explain));
                mrefunt_price_detailed.setText(getResources().getString(R.string.module_fieldinfo_cancle_order_explain));
            } else if (type == 3) {
                mrefunt_price_text.setText(getResources().getString(R.string.fieldinfo_industry_tv_str));
                mrefunt_price_detailed.setText(resourceinfolist.getIndustry_str());
            }
        } else if (type == 1) {
            int width = mDisplayMetrics.widthPixels - 148;     // 屏幕宽度（像素）
            int height = (width*736)/600;
            RelativeLayout.LayoutParams paramgroups= new RelativeLayout.LayoutParams(width, height+84);
            paramgroups.addRule(RelativeLayout.CENTER_IN_PARENT);
            mfieldinfo_error_recovery_top_layout.setLayoutParams(paramgroups);
            mfieldinfo_error_recovery_layout.setVisibility(View.VISIBLE);
            mfieldinfo_refunt_price_detailed_layout.setVisibility(View.GONE);
            mrefunt_price_detailed_btn.setVisibility(View.GONE);
            if (type == 1) {
                String name = "";
                if (resourceinfolist.getPhysical_resource().getCommunity().getName() != null && resourceinfolist.getPhysical_resource().getCommunity().getName().length() > 0) {
                    name = (resourceinfolist.getPhysical_resource().getCommunity().getName());
                }
                if (resourceinfolist.getPhysical_resource().getName() != null && resourceinfolist.getPhysical_resource().getName().length() > 0) {
                    name = name + (resourceinfolist.getPhysical_resource().getName());
                }
                merror_recovery_resourcesname_text.setText(name);
            }
        }
        newdialog = new AlertDialog.Builder(this).create();
        Constants.show_dialog(textEntryView,newdialog);
        mrefunt_price_detailed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newdialog.isShowing()) {
                    newdialog.dismiss();
                }
            }
        });
        merror_correction_cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newdialog.isShowing()) {
                    newdialog.dismiss();
                }
            }
        });
        txt_error_correction_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (merror_recovery_content_edit.getText().toString().trim().length() > 0) {
                    txt_error_correction_submit.setEnabled(false);
                    showProgressDialog();
                    if (type == 1) {
                        mFieldinfoMvpPresenter.releaseFeedbacks(resourceinfolist.getPhysical_resource().getId(),0,merror_recovery_content_edit.getText().toString());
                    }
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.fieldinfo_error_recovery_content_remindtext));
                    return;
                }
            }
        });
    }
    @Override
    public void onScroll(int scrollY) {
        int width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        mTitleBarInt = width *
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT /
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH - com.linhuiba.linhuifield.connector.Constants.Dp2Px(this,mStatusBarHeight);
        if (!isGetSecondNavHeight) {
            if (scrollY > 1 && mFieldinfoSpecificationInt == 0 && mFieldinfoCommunityInt == 0 &&
                    mFieldifnoReviewInt == 0) {
                //2017/9/23 二级导航栏变换操作
                mFieldinfoSpecificationInt = Math.max(scrollY, mfieldinfo_pricelist_layout.getTop()) - com.linhuiba.linhuifield.connector.Constants.Dp2Px(this,112);
                mFieldinfoCommunityInt = Math.max(scrollY, mfieldinfo_information_classify_layout.getTop()) - com.linhuiba.linhuifield.connector.Constants.Dp2Px(this,112);
                mFieldifnoReviewInt = Math.max(scrollY, mevaluation_layout.getTop()) - com.linhuiba.linhuifield.connector.Constants.Dp2Px(this,112);
                mFieldinfoRecommendInt = Math.max(scrollY, mFieldinfoRecommendLL.getTop()) - com.linhuiba.linhuifield.connector.Constants.Dp2Px(this,112);
            }
        } else {
            mFieldinfoCommunityInt = 0;
            mFieldifnoReviewInt = 0;
            mFieldinfoRecommendInt = 0;
            mFieldinfoCommunityInt = Math.max(scrollY, mfieldinfo_information_classify_layout.getTop()) - com.linhuiba.linhuifield.connector.Constants.Dp2Px(this,112);
            mFieldifnoReviewInt = Math.max(scrollY, mevaluation_layout.getTop()) - com.linhuiba.linhuifield.connector.Constants.Dp2Px(this,112);
            mFieldinfoRecommendInt = Math.max(scrollY, mFieldinfoRecommendLL.getTop()) - com.linhuiba.linhuifield.connector.Constants.Dp2Px(this,112);
            if (mFieldinfoSpecificationInt > 0 && mFieldinfoCommunityInt > 0 &&
                    mFieldifnoReviewInt > 0 && mFieldinfoRecommendInt > 0) {
                isGetSecondNavHeight = false;
            }
        }

        if(scrollY < 0) {
            mFieldInfoNavbarTtileLL.getBackground().mutate().setAlpha(0);
            mFieldInfoSecondLevelLL.setVisibility(View.GONE);
            mFieldInfoStatusBarLL.setBackgroundColor(getResources().getColor(R.color.color_null));
            mFieldInfoTitleTV.setVisibility(View.GONE);
        } else {
            if (scrollY == 0) {
                mFieldInfoTitleTV.setVisibility(View.GONE);
            } else {
                mFieldInfoTitleTV.setVisibility(View.VISIBLE);
                if (scrollY > 50 && mFieldinfoBottomLL.getVisibility() == View.GONE) {
                    mFieldinfoBottomLL.setVisibility(View.VISIBLE);
                }
            }
            if (scrollY < mTitleBarInt) {
                int progress = (int) (new Float(scrollY) / new Float(mTitleBarInt) * 200);
                mFieldInfoNavbarTtileLL.getBackground().mutate().setAlpha(progress);
                mFieldInfoTitleBackImg.setImageResource(R.drawable.nav_ic_back_white);
                mFieldInfoTitleShareImg.setImageResource(R.drawable.ic_share_white);
                mFieldInfoTitleCardImg.setImageResource(R.drawable.nav_ic_shopping_white);
                mFieldInfoTitleTV.setTextColor(getResources().getColor(R.color.white));
                mFieldInfoSecondLevelLL.setVisibility(View.GONE);
                mFieldInfoStatusBarLL.setBackgroundColor(getResources().getColor(R.color.color_null));
            } else {
                mFieldInfoNavbarTtileLL.getBackground().mutate().setAlpha(255);
                mFieldInfoTitleBackImg.setImageResource(R.drawable.ic_back_black);
                mFieldInfoTitleShareImg.setImageResource(R.drawable.popup_ic_share);
                mFieldInfoTitleCardImg.setImageResource(R.drawable.nav_ic_shopping_black);
                mFieldInfoTitleTV.setTextColor(getResources().getColor(R.color.title_bar_txtcolor));
                mFieldInfoSecondLevelLL.setVisibility(View.VISIBLE);
                mFieldInfoStatusBarLL.setBackgroundColor(getResources().getColor(R.color.checked_tv_color));
            }
            if (mFieldinfoSpecificationInt > 0 && mFieldinfoCommunityInt > 0 &&
                    ((scrollY > mFieldinfoSpecificationInt && scrollY < mFieldinfoCommunityInt) ||
                            scrollY == mFieldinfoSpecificationInt)) {
                showSecondLevelView(1);
            } else if (mFieldifnoReviewInt > 0 && mFieldinfoCommunityInt > 0 &&
                    ((scrollY > mFieldinfoCommunityInt && scrollY < mFieldifnoReviewInt) ||
                            scrollY == mFieldinfoCommunityInt)) {
                showSecondLevelView(2);
            } else if (mFieldifnoReviewInt > 0 && mFieldinfoRecommendInt > 0 &&
                    ((scrollY > mFieldifnoReviewInt && scrollY < mFieldinfoRecommendInt) ||
                            scrollY == mFieldifnoReviewInt)) {
                showSecondLevelView(3);
            } else if (mFieldinfoRecommendInt > 0 &&
                    (scrollY > mFieldinfoRecommendInt || scrollY == mFieldinfoRecommendInt)) {
                showSecondLevelView(0);
            }
        }
    }
    //选择规格回调
    public void prices_list_OnClickListener(int position,String str) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position,str);
        test.getfieldsize_pricenuit(this);
    }
    @Override
    public void getfieldsize_pricenuit(int position, String str) {
        if (position == -1) {
            if (str.equals(getResources().getString(R.string.fieldinfo_no_deposit_str))) {
                if (mFieldinfoPwDepositCB.isChecked()) {
                    mFieldinfoPwDepositCB.setChecked(false);
                } else {
                    mFieldinfoPwDepositCB.setChecked(true);
                }
            } else if (str.equals(getResources().getString(R.string.fieldinfo_have_deposit_str))) {
                if (mFieldinfoPwHaveDepositCB.isChecked()) {
                    mFieldinfoPwHaveDepositCB.setChecked(false);
                } else {
                    mFieldinfoPwHaveDepositCB.setChecked(true);
                }
            } else if (mFieldinfoPwLeaseTermTypeCB.getText().toString().split(" ")[1].equals(str)) {
                if (mFieldinfoPwLeaseTermTypeCB.isChecked()) {
                    mFieldinfoPwLeaseTermTypeCB.setChecked(false);
                } else {
                    mFieldinfoPwLeaseTermTypeCB.setChecked(true);
                }
            } else if (mFieldinfoPwSizeCB.getText().toString().equals(str)) {
                if (mFieldinfoPwSizeCB.isChecked()) {
                    mFieldinfoPwSizeCB.setChecked(false);
                } else {
                    mFieldinfoPwSizeCB.setChecked(true);
                }
            }
            if (!getScreenTrueStatus()) {
                setScreenStatus(0);
            } else {
                setScreenStatus(1);
            }
        } else {
            if(str.equals(getResources().getString(R.string.fieldinfo_enquiry_str))) {
                //2018/1/9 询价按钮操作
                mEnquiryClickPosition = position;
                if (LoginManager.isLogin()) {
                    startEnquiryIntent(mEnquiryClickPosition);
                } else {
                    Intent intent = new Intent(FieldInfoActivity.this, LoginActivity.class);
                    startActivityForResult(intent,ENQUIRY_REQUESTCODE);
                }

            } else {//规格选择
                mSizeGroupItemPosition = position;
                mSizeChildItemPosition = Integer.valueOf(str);
                if (mWeekLeaseTermPriceMap != null) {
                    mWeekLeaseTermPriceMap.clear();
                }
                if (mWeekLeaseTermMap != null) {
                    mWeekLeaseTermMap.clear();
                }
                if (mWeekLeaseTermNameMap != null) {
                    mWeekLeaseTermNameMap.clear();
                }
                isDaySpecification = false;
                if (mDimensionsDataList.get(mSizeGroupItemPosition).getDimension().getLease_term_type() != null &&
                        mDimensionsDataList.get(mSizeGroupItemPosition).getDimension().getLease_term_type().equals(
                                getResources().getString(R.string.groupbooding_list_item_residue_tiem_day_str)
                        )) {
                    isDaySpecification = true;
                    mWeekLeaseTermDepositMap = new HashMap<>();
                    for (int i = 0; i < mDimensionsDataList.get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSelling_resource_price().size(); i++) {
                        FieldInfoSellResourcePriceModel model = mDimensionsDataList.get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSelling_resource_price().get(i);
                        if (model != null &&
                                model.getLease_term_type_id() != null &&
                                model.getPrice() != null) {
                            mWeekLeaseTermPriceMap.put(model.getLease_term_type_id(),String.valueOf(model.getPrice()));
                            if (model.getLease_term_type() != null &&
                                    model.getLease_term_type().getId() > 0 &&
                                    model.getLease_term_type().getName() != null) {
                                if (model.getLease_term_type().getName().equals("Mon")) {
                                    mWeekLeaseTermMap.put(0, model.getLease_term_type().getId());
                                    mWeekLeaseTermNameMap.put(0, model.getLease_term_type().getDisplay_name());
                                    if (model.getDeposit() != null && model.getDeposit().length() > 0) {
                                        mWeekLeaseTermDepositMap.put(0,model.getDeposit());
                                    }
                                } else if (model.getLease_term_type().getName().equals("Tue")) {
                                    mWeekLeaseTermMap.put(1, model.getLease_term_type().getId());
                                    mWeekLeaseTermNameMap.put(1, model.getLease_term_type().getDisplay_name());
                                    if (model.getDeposit() != null && model.getDeposit().length() > 0) {
                                        mWeekLeaseTermDepositMap.put(1,model.getDeposit());
                                    }
                                } else if (model.getLease_term_type().getName().equals("Wed")) {
                                    mWeekLeaseTermMap.put(2, model.getLease_term_type().getId());
                                    mWeekLeaseTermNameMap.put(2, model.getLease_term_type().getDisplay_name());
                                    if (model.getDeposit() != null && model.getDeposit().length() > 0) {
                                        mWeekLeaseTermDepositMap.put(2,model.getDeposit());
                                    }
                                } else if (model.getLease_term_type().getName().equals("Thu")) {
                                    mWeekLeaseTermMap.put(3, model.getLease_term_type().getId());
                                    mWeekLeaseTermNameMap.put(3, model.getLease_term_type().getDisplay_name());
                                    if (model.getDeposit() != null && model.getDeposit().length() > 0) {
                                        mWeekLeaseTermDepositMap.put(3,model.getDeposit());
                                    }
                                } else if (model.getLease_term_type().getName().equals("Fri")) {
                                    mWeekLeaseTermMap.put(4, model.getLease_term_type().getId());
                                    mWeekLeaseTermNameMap.put(4, model.getLease_term_type().getDisplay_name());
                                    if (model.getDeposit() != null && model.getDeposit().length() > 0) {
                                        mWeekLeaseTermDepositMap.put(4,model.getDeposit());
                                    }
                                } else if (model.getLease_term_type().getName().equals("Sat")) {
                                    mWeekLeaseTermMap.put(5, model.getLease_term_type().getId());
                                    mWeekLeaseTermNameMap.put(5, model.getLease_term_type().getDisplay_name());
                                    if (model.getDeposit() != null && model.getDeposit().length() > 0) {
                                        mWeekLeaseTermDepositMap.put(5,model.getDeposit());
                                    }
                                } else if (model.getLease_term_type().getName().equals("Sun")) {
                                    mWeekLeaseTermMap.put(6, model.getLease_term_type().getId());
                                    mWeekLeaseTermNameMap.put(6, model.getLease_term_type().getDisplay_name());
                                    if (model.getDeposit() != null && model.getDeposit().length() > 0) {
                                        mWeekLeaseTermDepositMap.put(6,model.getDeposit());
                                    }
                                }
                            }
                        }
                    }
                }

                mPayId = mDimensionsDataList.get(position).getResource().get(mSizeChildItemPosition).getId();
                if (mDimensionsDataList.get(position).getDimension().getSize() != null && mDimensionsDataList.get(position).getDimension().getSize().length() > 0) {
                    size = mDimensionsDataList.get(position).getDimension().getSize();
                } else {
                    size = "";
                }
                if (mDimensionsDataList.get(position).getDimension().getLease_term_type() != null &&
                        mDimensionsDataList.get(position).getDimension().getLease_term_type().length() > 0) {
                    lease_term_type = mDimensionsDataList.get(position).getDimension().getLease_term_type();
                    if (!lease_term_type.equals(getResources().getString(R.string.groupbooding_list_item_residue_tiem_day_str))) {
                        if (mDimensionsDataList.get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSelling_resource_price().get(0) != null &&
                                mDimensionsDataList.get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSelling_resource_price().get(0).getLease_term_type_id() != null) {
                            mLeaseTermTypeId = mDimensionsDataList.get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition).getSelling_resource_price().get(0).getLease_term_type_id();
                        } else {
                            lease_term_type = "";
                            mLeaseTermTypeId = 0;
                        }
                    } else {
                        mLeaseTermTypeId = -1;
                    }
                } else {
                    lease_term_type = "";
                    mLeaseTermTypeId = 0;
                }
                if (mDimensionsDataList.get(position).getDimension().getCustom_dimension() != null &&
                        mDimensionsDataList.get(position).getDimension().getCustom_dimension().length() > 0) {
                    custom_dimension = mDimensionsDataList.get(position).getDimension().getCustom_dimension();
                } else {
                    custom_dimension = "";
                }
                if (mFieldinfoChooseSizeDialog == null ||
                        (mFieldinfoChooseSizeDialog != null &&
                                !mFieldinfoChooseSizeDialog.isShowing())) {
                    showChooseSpecificationsView(mSizeGroupItemPosition,mSizeChildItemPosition);
                }
            }
        }

    }
    private void showZoomPicDialog(int position) {
        View myView = FieldInfoActivity.this.getLayoutInflater().inflate(R.layout.module_dialog_fieldinfo_look_price, null);
        zoom_picture_dialog = new AlertDialog.Builder(FieldInfoActivity.this).create();
        Constants.show_dialog(myView,zoom_picture_dialog);
        // FIXME: 2018/12/17 预览图片
        TextView mshowpicture_back = (TextView)myView.findViewById(R.id.fieldinfo_look_pic_back_tv);
        TextView mShowPictureTitleTV = (TextView)myView.findViewById(R.id.fieldinfo_look_pic_title_tv);
        ViewPager mzoom_viewpage = (ViewPager)myView.findViewById(R.id.fieldinfo_look_pic_zoom_dialog_viewpage);
        LinearLayout mShowPicSizeLL = (LinearLayout)myView.findViewById(R.id.fieldinfo_look_pic_size_ll);
        final LinearLayout mShowPicstatementLL = (LinearLayout)myView.findViewById(R.id.fieldinfo_look_pic_statement_ll);
        final TextView mShowPicstatementConfirmTV = (TextView)myView.findViewById(R.id.fieldinfo_look_pic_statement_confirm_tv);
        final TextView mShowPicstatementTV = (TextView)myView.findViewById(R.id.fieldinfo_look_pic_statement_tv);
        final TextView mShowPicPhyTV = (TextView)myView.findViewById(R.id.fieldinfo_look_pic_phy_tv);
        final TextView mShowPicCaseTV = (TextView)myView.findViewById(R.id.fieldinfo_look_pic_case_tv);
        final TextView mShowPicNumTV = (TextView)myView.findViewById(R.id.fieldinfo_look_pic_num_tv);
        final TextView mShowPicSizeTV = (TextView)myView.findViewById(R.id.fieldinfo_look_pic_size_tv);
        final ImageButton mShowPicallImgBtn = (ImageButton)myView.findViewById(R.id.fieldinfo_look_pic_show_all_imgBtn);
        mShowPictureTitleTV.setText(ShareTitleStr);
        mShowPicSizeTV.setText("/"+String.valueOf(mPicList.size()));
        int width_new = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        int height_new = width_new * com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT /
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH;
        int margintop = mDisplayMetrics.heightPixels / 2 + (height_new / 2 + com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldInfoActivity.this,75 - 30 - 64));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mShowPicSizeLL.getLayoutParams());
        lp.setMargins(0, margintop, com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldInfoActivity.this,12), 0);
        mShowPicSizeLL.setLayoutParams(lp);
        // FIXME: 2018/12/18 案例数量 展位图片最后是第几个
        int phyEndInt = mPicList.size();
        if (mCasePicList != null && mCasePicList.size() > 0) {
            phyEndInt = mPicList.size() - mCasePicList.size();
        }
        mShowPicPhyTV.setVisibility(View.GONE);
        mShowPicCaseTV.setVisibility(View.GONE);
        if (phyEndInt > 0) {
            mShowPicPhyTV.setVisibility(View.VISIBLE);
            if (mPicList.size() > phyEndInt) {
                mShowPicCaseTV.setVisibility(View.VISIBLE);
            }
        } else {
            mShowPicCaseTV.setVisibility(View.VISIBLE);
        }
        if (position > phyEndInt) {
            mShowPicCaseTV.setTextColor(getResources().getColor(R.color.white));
            mShowPicCaseTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_splash_screen_selected_text_bg));
            mShowPicPhyTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
            mShowPicPhyTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_fieldlist_activitys_overdue_subsidy_bg));
        } else {
            mShowPicPhyTV.setTextColor(getResources().getColor(R.color.white));
            mShowPicPhyTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_splash_screen_selected_text_bg));
            mShowPicCaseTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
            mShowPicCaseTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_fieldlist_activitys_overdue_subsidy_bg));
        }
        mshowpicture_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoom_picture_dialog.dismiss();
            }
        });
        mShowPicstatementTV.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                mShowPicstatementLL.setVisibility(View.VISIBLE);
            }
        });

        mShowPicstatementConfirmTV.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                mShowPicstatementLL.setVisibility(View.GONE);
            }
        });
        final int finalPhyEndInt = phyEndInt;
        mShowPicallImgBtn.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Intent pictureIntent = new Intent(FieldInfoActivity.this,FieldinfoLookPictureActivity.class);
                pictureIntent.putExtra("pic_list",(Serializable)mPicList);
                pictureIntent.putExtra("phy_end", finalPhyEndInt);
                pictureIntent.putExtra("phy_name", ShareTitleStr);
                startActivity(pictureIntent);
                zoom_picture_dialog.dismiss();
            }
        });
        if (mIsRefreshZoomImageview) {
            mImageViewList = new ArrayList<>();
            for (int i = 0; i < mPicList.size(); i++) {
                ZoomImageView imageView = new ZoomImageView(
                        getApplicationContext());
                Picasso.with(this).load(mPicList.get(i).toString()).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(width_new, height_new).into(imageView);
                mImageViewList.add(imageView);
            }
            // FIXME: 2018/12/26 设置循环的数据
            ZoomImageView imageView2 = new ZoomImageView(
                    getApplicationContext());
            Picasso.with(this).load(mPicList.get(mPicList.size() - 1).toString()).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(width_new, height_new).into(imageView2);
            mImageViewList.add(0,imageView2);
            ZoomImageView imageView1 = new ZoomImageView(
                    getApplicationContext());
            Picasso.with(this).load(mPicList.get(0).toString()).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(width_new, height_new).into(imageView1);
            mImageViewList.add(imageView1);
            mIsRefreshZoomImageview = false;
        }
        if (mImageViewList != null && mImageViewList.size() > 0) {
            com.linhuiba.linhuifield.connector.Constants.showFieldinfoPic(mImageViewList,mzoom_viewpage,position,
                    mShowPicNumTV,mShowPicPhyTV,mShowPicCaseTV,phyEndInt,FieldInfoActivity.this,mShowPicstatementLL);
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
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if(requestCode == Constants.PermissionRequestCode) {
                MQConfig.init(FieldInfoActivity.this, com.linhuiba.linhuipublic.config.Config.MQAppkey, new OnInitCallback() {
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
                            Intent intent = new MQIntentBuilder(FieldInfoActivity.this)
                                    .setClientInfo(clientInfo)
                                    .setCustomizedId(LoginManager.getUid())
                                    .build();
                            startActivityForResult(intent,10);
                        } else {
                            Intent intent = new MQIntentBuilder(FieldInfoActivity.this)
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
                mLocationClient = new LocationClient(FieldInfoActivity.this);
                mLocationClient.registerLocationListener(new MyLocationListener());//注册定位监听接口
                initLocation();
            } else if (requestCode == CALL_PHONE_CODE) {
                // FIXME: 2018/12/19 顾问电话
                if (resourceinfolist.getService_representative() != null &&
                        resourceinfolist.getService_representative().getTel() != null &&
                        resourceinfolist.getService_representative().getTel().length() > 0) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                            + resourceinfolist.getService_representative().getTel()));// FIXME: 2018/12/19 测试数据
                    if (ActivityCompat.checkSelfPermission(FieldInfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
            } else if (requestCode == CALL_SERVICE_PHONE_CODE) {
                // FIXME: 2018/12/19 顾问电话
                if (resourceinfolist.getService_phone() != null &&
                        resourceinfolist.getService_phone().length() > 0) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                            + resourceinfolist.getService_phone()));// FIXME: 2018/12/19 测试数据
                    if (ActivityCompat.checkSelfPermission(FieldInfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    //弹出选择规格界面
    private void showChooseSpecificationsView(int mGroupItemPosition, int mChildItemPosition) {
        if (datelist != null) {
            datelist.clear();
        }
        jumpMonth = 0;
        jumpYear = 0;
        LayoutInflater factory = LayoutInflater.from(FieldInfoActivity.this);
        final View myView = factory.inflate(R.layout.activity_fieldinfo_choose_specifications_dialog, null);
        mCurrentMonthTV = (TextView)myView.findViewById(R.id.fieldinfo_choosepecifications_currentMonth);
        mPrevMonthImgBtn = (ImageButton)myView.findViewById(R.id.fieldinfo_choosepecifications_prevMonth);
        mNextMonthImgBtn = (ImageButton)myView.findViewById(R.id.fieldinfo_choosepecifications_nextMonth);
        mViewFlipper = (ViewFlipper) myView.findViewById(R.id.fieldinfo_choosepecifications_flipper);
        mViewFlipperLL= (LinearLayout) myView.findViewById(R.id.fieldinfo_viewflipperlayout);
        mGridScroView = (ScrollView) myView.findViewById(R.id.choosepecifications_scrollview);

        Button mChooseDatePaidOrderBtn = (Button)myView.findViewById(R.id.fieldinfo_choose_paid_order_btn);
        Button mChooseDateAddCardBtn = (Button) myView.findViewById(R.id.fieldinfo_choose_addcard_button);
        ImageView mChooseDateDialogCloseImg = (ImageView)myView.findViewById(R.id.fieldinfo_choose_date_close_img);
        mDepositTV = (TextView) myView.findViewById(R.id.fieldinfo_choose_specifications_deposit_tv);
        TextView mChoseDateSizeTV = (TextView) myView.findViewById(R.id.fieldinfo_choose_specifications_stall_size_tv);
        TextView mChoseDateTiemUnitTV = (TextView) myView.findViewById(R.id.fieldinfo_choose_specifications_time_unit_tv);
        TextView mChoseDateDimensionTV = (TextView) myView.findViewById(R.id.fieldinfo_choose_specifications_special_size_tv);
        TextView mChoseDateMinOrderDayTV = (TextView) myView.findViewById(R.id.fieldinfo_choose_specifications_min_order_day_tv);
        TextView mFieldinfoDayInAdvanceTV  = (TextView) myView.findViewById(R.id.fieldinfo_choose_specifications_days_in_advance_tv);
        TextView mFieldinfoDayInAdvanceFirstTV  = (TextView) myView.findViewById(R.id.fieldinfo_choose_size_reserve_first_tv);
        TextView mFieldinfoDayInAdvanceSecondTV  = (TextView) myView.findViewById(R.id.fieldinfo_choose_size_reserve_second_tv);
        TextView mDescriptionTV  = (TextView) myView.findViewById(R.id.fieldinfo_choose_specifications_description_tv);

        mChoseDateSizeTV.setText(mDimensionsDataList.get(mGroupItemPosition).getDimension().getSize());
        mChoseDateTiemUnitTV.setText(mDimensionsDataList.get(mGroupItemPosition).getDimension().getLease_term_type());
        if (mDimensionsDataList.get(mGroupItemPosition).getDimension().getCustom_dimension() != null &&
                mDimensionsDataList.get(mGroupItemPosition).getDimension().getCustom_dimension().length() > 0) {
            mChoseDateDimensionTV.setText(mDimensionsDataList.get(mGroupItemPosition).getDimension().getCustom_dimension());
        }
        //活动显示
        if (isActivityRes) {
            if (mActivityDescription != null && mActivityDescription.length() > 0) {
                mDescriptionTV.setText(mActivityDescription);
                mDescriptionTV.setVisibility(View.INVISIBLE);

            } else {
                mDescriptionTV.setVisibility(View.INVISIBLE);
            }
        } else {
            mDescriptionTV.setVisibility(View.INVISIBLE);
        }
        //不能下单的日期列表
        if (Closing_dates != null) {
            Closing_dates.clear();
        }
        if (mDimensionsDataList.get(mGroupItemPosition).getResource().get(mChildItemPosition).getCalendars() != null) {
            if (mDimensionsDataList.get(mGroupItemPosition).getResource().get(mChildItemPosition).getCalendars().size() > 0) {
                for (int i = 0; i < mDimensionsDataList.get(mGroupItemPosition).getResource().get(mChildItemPosition).getCalendars().size(); i++) {
                    if (mDimensionsDataList.get(mGroupItemPosition).getResource().get(mChildItemPosition).getCalendars().get(i).get("exp_date") != null &&
                            mDimensionsDataList.get(mGroupItemPosition).getResource().get(mChildItemPosition).getCalendars().get(i).get("exp_date").length() > 0) {
                        Closing_dates.add(mDimensionsDataList.get(mGroupItemPosition).getResource().get(mChildItemPosition).getCalendars().get(i).get("exp_date"));
                    }
                }
            }
        }
        if (mDimensionsDataList.get(mGroupItemPosition).getResource().get(mChildItemPosition).getMinimum_booking_days() != null &&
                mDimensionsDataList.get(mGroupItemPosition).getResource().get(mChildItemPosition).getMinimum_booking_days() > 1) {
            minimum_order_quantity = mDimensionsDataList.get(mGroupItemPosition).getResource().get(mChildItemPosition).getMinimum_booking_days();
        }
        mChoseDateMinOrderDayTV.setText(String.valueOf(minimum_order_quantity) +
                getResources().getString(R.string.groupbooding_list_item_residue_tiem_day_str));
        //2018/11/13 押金显示
        if (isDaySpecification && datelist.size() > 0) {
            deposit = Double.parseDouble(getMaxDeposit(datelist));
        } else {
            if (mDimensionsDataList.get(mGroupItemPosition).getResource().get(mChildItemPosition).getSelling_resource_price().get(0).getDeposit() != null &&
                    mDimensionsDataList.get(mGroupItemPosition).getResource().get(mChildItemPosition).getSelling_resource_price().get(0).getDeposit().length() > 0) {
                deposit = Double.parseDouble(mDimensionsDataList.get(mGroupItemPosition).getResource().get(mChildItemPosition).getSelling_resource_price().get(0).getDeposit());
            }
        }
        if (deposit > 0) {
            mDepositTV.setText(getResources().getString(R.string.order_listitem_price_unit_text) +
                    Constants.getdoublepricestring(deposit,1));
        } else {
            mDepositTV.setText(getResources().getString(R.string.search_deposit_label_tv_str));
        }
        if (mDimensionsDataList.get(mGroupItemPosition).getResource().get(mChildItemPosition).getDays_in_advance() != null) {
            days_in_advance = mDimensionsDataList.get(mGroupItemPosition).getResource().get(mChildItemPosition).getDays_in_advance();
        }
        if (days_in_advance == 0) {
            mFieldinfoDayInAdvanceTV.setText(getResources().getString(R.string.module_fieldinfo_intraday_reserve_str));
            mFieldinfoDayInAdvanceFirstTV.setVisibility(View.GONE);
            mFieldinfoDayInAdvanceSecondTV.setVisibility(View.GONE);
        } else {
            mFieldinfoDayInAdvanceTV.setText(String.valueOf(days_in_advance));
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        mCurrentDateStr = sdf.format(date); // 当期日期
        mCurrentYear = Integer.parseInt(mCurrentDateStr.split("-")[0]);
        mCurrentMonth = Integer.parseInt(mCurrentDateStr.split("-")[1]);
        mCurrentDay = Integer.parseInt(mCurrentDateStr.split("-")[2]);
        //活动自动跳到活动选择月
        if (mActivityStartDate != null &&
                mActivityStartDate.length() > 0 &&
                resourceinfolist.getPhysical_resource().getIs_activity() == 1) {
            String[] startDate;
            startDate = mActivityStartDate.split("-");
            if (startDate != null && startDate.length > 2) {
                int activityMonth = Integer.parseInt(startDate[1]);
                int activityYear = Integer.parseInt(startDate[0]);
                if (activityYear - mCurrentYear > 0) {
                    if (activityMonth - mCurrentMonth > 0) {
                        jumpMonth = activityMonth - mCurrentMonth + 12 * (activityYear - mCurrentYear);
                    } else if (activityMonth - mCurrentMonth < 0) {
                        jumpMonth = (12  - (mCurrentMonth - activityMonth)) + 12 * (activityYear - mCurrentYear - 1);
                    } else {
                        jumpMonth = 12 * (activityYear - mCurrentYear);
                    }
                } else if (activityYear - activityYear == 0) {
                    if (activityMonth - mCurrentMonth > 0) {
                        jumpMonth = activityMonth - mCurrentMonth;
                    }
                }
            }
        }
        mViewFlipper.removeAllViews();
        FieldinfoChooseSpecificationsAdapter.clear_mapitemclick();
        FieldinfoChooseSpecificationsAdapter.clear_mapitemprice();
        FieldinfoChooseSpecificationsAdapter.clear_mapitemtxt_itembg();
        FieldinfoChooseSpecificationsAdapter.clear_mapitemweather();
        mChooseSpecificationsAdapter = new FieldinfoChooseSpecificationsAdapter(FieldInfoActivity.this, getResources(),
                jumpMonth, jumpYear, mCurrentYear, mCurrentMonth, mCurrentDay,datelist,
                mDimensionsDataList.get(mGroupItemPosition).getDimension(),
                mDimensionsDataList.get(mGroupItemPosition).getResource().get(mChildItemPosition),
                FieldInfoActivity.this,1);
        addGridView();
        mGridView.setAdapter(mChooseSpecificationsAdapter);
        mViewFlipper.addView(mGridView, 0);
        addTextToTopTextView(mCurrentMonthTV);

        mFieldinfoChooseSizeDialog = new AlertDialog.Builder(FieldInfoActivity.this).create();
        mFieldinfoChooseSizeDialog.setCancelable(false);
        Constants.show_dialog(myView,mFieldinfoChooseSizeDialog);
        setOnMonthImgBtnClick();
        //日历左右滑动事件监听切换
        myGridViewGestureListener = new GestureDetector(this, new MyGestureListener());
        mChooseDateDialogCloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFieldinfoChooseSizeDialog.dismiss();
            }
        });
        mChooseDateAddCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datelist.size() > 0) {
                    if (LoginManager.isLogin()) {
                        if (resourceinfolist.getPhysical_resource().getIs_activity() == 1) {
                            if (datelist.size() < minimum_order_quantity) {
                                MessageUtils.showToast(getResources().getString(R.string.choose_datelist_size_under_minimum_order_quantity_first_text) +
                                        String.valueOf(minimum_order_quantity) +getResources().getString(R.string.choose_datelist_size_under_minimum_order_quantity));
                                return;
                            }
                        }
                        if ((mLeaseTermTypeId > 0 && size.length() > 0 && datelist.size() > 0) ||
                                (mLeaseTermTypeId == -1 && size.length() > 0 && datelist.size() > 0)) {
                            showProgressDialog();
                            FieldApi.addshopcart_items(MyAsyncHttpClient.MyAsyncHttpClient_version_two(), addToShoppingCartHandler, getsubmitjsonstr());
                            mFieldinfoChooseSizeDialog.dismiss();
                        } else {
                            MessageUtils.showToast(FieldInfoActivity.this, getResources().getString(R.string.fieldinfo_diolog_choosespecifications_no_size_msg));
                        }
                    } else {
                        Intent loginintent = new Intent(FieldInfoActivity.this, LoginActivity.class);
                        startActivityForResult(loginintent,ADD_CARTS_REQUESTCODE);
                    }
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.choose_datelist_size_zero));
                }
            }
        });
        mChooseDatePaidOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datelist.size() > 0) {
                    if (LoginManager.isLogin()) {
                        if (resourceinfolist.getPhysical_resource().getIs_activity() == 1) {
                            if (datelist.size() < minimum_order_quantity) {
                                MessageUtils.showToast(getResources().getString(R.string.choose_datelist_size_under_minimum_order_quantity_first_text) +
                                        String.valueOf(minimum_order_quantity) +getResources().getString(R.string.choose_datelist_size_under_minimum_order_quantity));
                                return;
                            }
                        }
                        if ((mLeaseTermTypeId > 0 && size.length() > 0 && datelist.size() > 0) ||
                                (mLeaseTermTypeId == -1 && size.length() > 0 && datelist.size() > 0)) {
                            createOrderIntent();
                        } else {
                            MessageUtils.showToast(FieldInfoActivity.this, getResources().getString(R.string.fieldinfo_diolog_choosespecifications_no_size_msg));
                        }
                    } else {
                        Intent loginintent = new Intent(FieldInfoActivity.this, LoginActivity.class);
                        startActivityForResult(loginintent,CREATE_ORDER_REQUESTCODE);
                    }
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.choose_datelist_size_zero));
                }
            }
        });
    }
    public void setOnMonthImgBtnClick() {
        mPrevMonthImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrevMonthClick(mGVFlag);
            }
        });
        mNextMonthImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextMonthClick(mGVFlag);
            }
        });
    }
    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    public void NextMonthClick(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月
        FieldinfoChooseSpecificationsAdapter.clear_mapitemclick();
        FieldinfoChooseSpecificationsAdapter.clear_mapitemprice();
        FieldinfoChooseSpecificationsAdapter.clear_mapitemtxt_itembg();
        FieldinfoChooseSpecificationsAdapter.clear_mapitemweather();
        mChooseSpecificationsAdapter = new FieldinfoChooseSpecificationsAdapter(this, this.getResources(),
                jumpMonth, jumpYear, mCurrentYear, mCurrentMonth, mCurrentDay,datelist,
                mDimensionsDataList.get(mSizeGroupItemPosition).getDimension(),
                mDimensionsDataList.get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition),
                this,1);
        mGridView.setAdapter(mChooseSpecificationsAdapter);
        addTextToTopTextView(mCurrentMonthTV); // 移动到下一月后，将当月显示在头标题中
        gvFlag++;
        mViewFlipper.addView(mGridView, gvFlag);
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
        mViewFlipper.showNext();
        mViewFlipper.removeViewAt(0);
    }

    /**
     * 移动到上一个月
     *
     * @param gvFlag
     */
    public void PrevMonthClick(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth--; // 上一个月
        FieldinfoChooseSpecificationsAdapter.clear_mapitemclick();
        FieldinfoChooseSpecificationsAdapter.clear_mapitemprice();
        FieldinfoChooseSpecificationsAdapter.clear_mapitemtxt_itembg();
        FieldinfoChooseSpecificationsAdapter.clear_mapitemweather();
        mChooseSpecificationsAdapter = new FieldinfoChooseSpecificationsAdapter(this, this.getResources(),
                jumpMonth, jumpYear, mCurrentYear, mCurrentMonth, mCurrentDay,datelist,
                mDimensionsDataList.get(mSizeGroupItemPosition).getDimension(),
                mDimensionsDataList.get(mSizeGroupItemPosition).getResource().get(mSizeChildItemPosition),
                this,1);
        mGridView.setAdapter(mChooseSpecificationsAdapter);
        gvFlag++;
        addTextToTopTextView(mCurrentMonthTV); // 移动到上一月后，将当月显示在头标题中
        mViewFlipper.addView(mGridView, gvFlag);
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
        mViewFlipper.showPrevious();
        mViewFlipper.removeViewAt(0);
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

    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
            float a = (e1.getX() - e2.getX());
            if (e1.getX() - e2.getX() > 100) {
                // 像左滑动
                NextMonthClick(gvFlag);
                return true;
            } else if (e1.getX() - e2.getX() < -100) {
                // 向右滑动
                PrevMonthClick(gvFlag);
                return true;
            }
            return false;
        }
    }
    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        // 取得屏幕的宽度和高度
        int width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        mGridView = new MyGridview(this);
        mGridView.setBackgroundColor(getResources().getColor(R.color.white));
        mGridView.setPadding(26,0,26,0);
        mGridView.setNumColumns(7);
        mGridView.setColumnWidth(width / 7);
        // mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);

        mGridView.setGravity(Gravity.CENTER);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除mGridView边框
        mGridView.setVerticalSpacing(0);
        mGridView.setHorizontalSpacing(0);
        mGridView.setLayoutParams(params);
        mGridScroView.smoothScrollTo(0, 0);
    }
    /**
     * 添加头部的年份 闰哪月等信息
     *
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        // draw = getResources().getDrawable(R.drawable.top_day);
        // view.setBackgroundDrawable(draw);
        textDate.append(mChooseSpecificationsAdapter.getShowYear()).append("年").append(mChooseSpecificationsAdapter.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
    }
    //点击日期的回调
    @Override
    public void getAddfieldFouritemcall(int position) {
    // 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
        int startPosition = mChooseSpecificationsAdapter.getStartPositon();//本月第一天position
        int endPosition = mChooseSpecificationsAdapter.getEndPosition();//本月最后一天position
        String[] datestrarray = FieldinfoChooseSpecificationsAdapter.dayNumber[position].split("\\.");
        Date choosedate = null;
        try {
            choosedate = mSimpleDateFormat.parse(datestrarray[3]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String scheduleDay = mChooseSpecificationsAdapter.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
        // String scheduleLunarDay =
        // mChooseSpecificationsAdapter.getDateByClickItem(position).split("\\.")[1];
        // //这一天的阴历
        String scheduleYear = "";
        String scheduleMonth = "";
        scheduleYear = mChooseSpecificationsAdapter.getShowYear();
        scheduleMonth = mChooseSpecificationsAdapter.getShowMonth();
        if (position >=  FieldinfoChooseSpecificationsAdapter.daysOfMonth +
                FieldinfoChooseSpecificationsAdapter.dayOfWeek) {
            if (Integer.parseInt(scheduleMonth) == 12) {
                scheduleYear = String.valueOf(Integer.parseInt(scheduleYear) + 1);
                scheduleMonth = "01";
            } else {
                scheduleMonth = String.valueOf(Integer.parseInt(scheduleMonth) + 1);
            }
        } else if (position < FieldinfoChooseSpecificationsAdapter.dayOfWeek) {
            if (Integer.parseInt(scheduleMonth) == 1) {
                scheduleYear = String.valueOf(Integer.parseInt(scheduleYear) - 1);
                scheduleMonth = "12";
            } else {
                scheduleMonth = String.valueOf(Integer.parseInt(scheduleMonth) - 1);
            }
        }
        boolean isActivity = false;
        if (mActivityStartDate != null &&
                mActivityStartDate.length() > 0 &&
                mDeadline != null &&
                mDeadline.length() > 0) {
            isActivity = true;
        }
            Date clickdate = null;
            try {
                clickdate = mSimpleDateFormat.parse(scheduleYear + "-" + scheduleMonth + "-" + scheduleDay);
                if (mLeaseTermTypeId == -1) {
                    if (minimum_order_quantity > 1 && mActivityStartDate != null &&
                            mActivityStartDate.length() > 0 && mDeadline != null &&
                            mDeadline.length() > 0 && datelist.size() == 0) {
                        if (datelist != null) {
                            datelist.clear();
                        }
                        datelist = Constants.getChooseActivityDate(datestrarray[3],mDeadline,minimum_order_quantity,Closing_dates,mWeekLeaseTermPriceMap,mWeekLeaseTermMap);
                        if (datelist.size() > 0) {
                            for (int i = 0; i < FieldinfoChooseSpecificationsAdapter.dayNumber.length; i++) {
                                String[] strarray = FieldinfoChooseSpecificationsAdapter.dayNumber[i].split("\\.");
                                if (i < FieldinfoChooseSpecificationsAdapter.daysOfMonth + FieldinfoChooseSpecificationsAdapter.dayOfWeek && i >= FieldinfoChooseSpecificationsAdapter.dayOfWeek) {
                                    Date datetmp = mSimpleDateFormat.parse(strarray[3]);
                                    if ((Integer)FieldinfoChooseSpecificationsAdapter.getmapitemtxt_itembg().get(i) == 3) {
                                        if (!datelist.contains(datetmp)) {
                                            FieldinfoChooseSpecificationsAdapter.getmapitemtxt_itembg().put(i, 1);
                                        }
                                    } else {
                                        if (datelist.contains(datetmp)) {
                                            FieldinfoChooseSpecificationsAdapter.getmapitemtxt_itembg().put(i, 3);
                                        }
                                    }
                                }
                            }
                            mChooseSpecificationsAdapter.notifyDataSetChanged();
                        }
                    } else {
                        if (datelist.contains(clickdate)) {
                            datelist.remove(clickdate);
                            if (position % 7 == 0 || position % 7 == 6) {
                                FieldinfoChooseSpecificationsAdapter.getmapitemtxt_itembg().put(position, 2);
                            } else {
                                FieldinfoChooseSpecificationsAdapter.getmapitemtxt_itembg().put(position, 1);
                            }
                            mChooseSpecificationsAdapter.notifyDataSetChanged();
                        } else {
                            if (Constants.date_interval(datestrarray[3],days_in_advance,isActivity)) {//选择规格 判断日期是否在选择区间
                                if ((isActivity &&
                                        mActivityStartDate != null && mDeadline != null && mDeadline.length() > 0 &&
                                        mActivityStartDate.length() > 0)) {
                                    if (Constants.isdeadline_date(mActivityStartDate,mDeadline,datestrarray[3])) {
                                        int weekInt = com.linhuiba.linhuifield.connector.Constants.getDayForWeek(datestrarray[3]);
                                        if (weekInt > -1) {
                                            if (mWeekLeaseTermPriceMap.get(
                                                    mWeekLeaseTermMap.get(weekInt)) != null) {
                                                datelist.add(clickdate);
                                                FieldinfoChooseSpecificationsAdapter.getmapitemtxt_itembg().put(position, 3);
                                                mChooseSpecificationsAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                } else {
                                    int weekInt = com.linhuiba.linhuifield.connector.Constants.getDayForWeek(datestrarray[3]);
                                    if (weekInt > -1) {
                                        if (mWeekLeaseTermPriceMap.get(
                                                mWeekLeaseTermMap.get(weekInt)) != null) {
                                            datelist.add(clickdate);
                                            FieldinfoChooseSpecificationsAdapter.getmapitemtxt_itembg().put(position, 3);
                                            mChooseSpecificationsAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (mLeaseTermTypeId > 0) {
                        if (datelist != null) {
                            datelist.clear();
                        }
                        //根据点击的开始时间算出结束时间
                        checkedstart_date = clickdate;
                        //选择规格时 算出周月年 的结束时间
                        checkedend_date = Constants.getchoose_enddate(clickdate,mDimensionsDataList.get(mSizeGroupItemPosition).getResource().get(0).getSelling_resource_price().get(0).getLease_term_type().getPeriod() - 1);
                        //重新设置日历显示 刷新 ui
                        for (int i = 0; i < FieldinfoChooseSpecificationsAdapter.dayNumber.length; i++) {
                            String[] strarray = FieldinfoChooseSpecificationsAdapter.dayNumber[i].split("\\.");
                            if (i < FieldinfoChooseSpecificationsAdapter.daysOfMonth + FieldinfoChooseSpecificationsAdapter.dayOfWeek && i >= FieldinfoChooseSpecificationsAdapter.dayOfWeek) {
                                if (Constants.date_interval(strarray[3],days_in_advance,isActivity)) {//选择规格 判断日期是否在选择区间
                                    Date datetmp = mSimpleDateFormat.parse(strarray[3]);
                                    if (Constants.ischoosedate(checkedstart_date,checkedend_date,datetmp)) {
                                        if (!datelist.contains(datetmp)) {
                                            datelist.add(datetmp);
                                        }
                                        FieldinfoChooseSpecificationsAdapter.getmapitemtxt_itembg().put(i, 3);
                                    } else {
                                        if (mDeadline != null &&
                                                mActivityStartDate != null &&
                                                mDeadline.length() > 0 && mActivityStartDate.length() > 0 &&
                                                !Constants.isdeadline_date(mActivityStartDate,mDeadline,strarray[3])) {
                                            FieldinfoChooseSpecificationsAdapter.getmapitemtxt_itembg().put(i, 4);
                                        } else {
                                            if (i % 7 == 0 || i % 7 == 6) {
                                                FieldinfoChooseSpecificationsAdapter.getmapitemtxt_itembg().put(i, 2);
                                            } else {
                                                FieldinfoChooseSpecificationsAdapter.getmapitemtxt_itembg().put(i, 1);
                                            }
                                        }
                                    }
                                } else {
                                    if (datelist.size() > 0) {
                                        Date datetmp = null;
                                        try {
                                            datetmp = mSimpleDateFormat.parse(strarray[3]);
                                            if (Constants.ischoosedate(checkedstart_date,checkedend_date,datetmp)) {
                                                if (!datelist.contains(datetmp)) {
                                                    datelist.add(datetmp);
                                                }
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        if (datelist.contains(datetmp)) {
                                            FieldinfoChooseSpecificationsAdapter.getmapitemtxt_itembg().put(i, 3);
                                        } else {
                                            FieldinfoChooseSpecificationsAdapter.getmapitemtxt_itembg().put(i, 4);
                                        }
                                    }
                                }
                            }
                        }
                        mChooseSpecificationsAdapter.notifyDataSetChanged();
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            //2018/11/12 押金显示
            if (isDaySpecification && datelist.size() > 0) {
                deposit = Double.parseDouble(getMaxDeposit(datelist));
            } else {
                if (datelist.size() == 0) {
                    deposit = 0;
                }
            }
            if (deposit > 0) {
                mDepositTV.setText(getResources().getString(R.string.order_listitem_price_unit_text) +
                        Constants.getdoublepricestring(deposit,1));
            } else {
                mDepositTV.setText(getResources().getString(R.string.search_deposit_label_tv_str));
            }

    }
//    }
    //点击日期的回调
    public void chooseDateCallBack(int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position);
        test.AddfieldFouritemcall(this);
    }
    private void getCardSize() {
        FieldApi.getshopcart_itemscount(MyAsyncHttpClient.MyAsyncHttpClient(), getShoppingCartCountHandler);
    }
    private LinhuiAsyncHttpResponseHandler getShoppingCartCountHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null) {
                JSONObject jsonobject = JSON.parseObject(data.toString());
                if (jsonobject.get("count") != null && jsonobject.get("count").toString().length() > 0) {
                    if (jsonobject.get("count").toString().equals("0")) {
                        mCardSizeBv.hide(false);
                    } else {
                        mCardSizeBv.setBadgeNumber(Integer.parseInt(jsonobject.get("count").toString()));
                    }
                } else {
                    mCardSizeBv.hide(false);
                }
            } else {
                mCardSizeBv.hide(false);
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
        }
    };
    private void showResourceScreeningPw() {
        mscrollview.smoothScrollTo(0, mTitleBarInt);
        final ListView fieldinfoScreenLv = (ListView) mFieldinfoScreenPwView.findViewById(R.id.fieldinfo_srceen_pw_lv);
        Button mresetbtn = (Button) mFieldinfoScreenPwView.findViewById(R.id.fieldinfo_srceen_pw_reset_btn);
        Button mconfirmbtn = (Button) mFieldinfoScreenPwView.findViewById(R.id.fieldinfo_srceen_pw_confirm_btn);
        //通过view 和宽·高，构造PopopWindow
        int width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        mFieldinfoScreenPW = new SupportPopupWindow(mFieldinfoScreenPwView, width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
        mFieldinfoScreenPW.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_bg));
        mFieldinfoScreenPW.getBackground().setAlpha(155);
        //设置焦点为可点击
        mFieldinfoScreenPW.setFocusable(true);
//        mFieldinfoScreenPW.setFocusable(false);//可以试试设为false的结果
        mFieldinfoScreenPW.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mFieldinfoScreenPW.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //将window视图显示在myButton下面
        mFieldinfoScreenPW.showAsDropDown(mFieldInfoScreenView);
        if (mFieldinfoDepositCB.isChecked()) {
            mFieldinfoPwDepositCB.setChecked(true);
        } else {
            mFieldinfoPwDepositCB.setChecked(false);
        }
        if (mFieldinfoHaveDepositCB.isChecked()) {
            mFieldinfoPwHaveDepositCB.setChecked(true);
        } else {
            mFieldinfoPwHaveDepositCB.setChecked(false);
        }
        if (mFieldinfoLeaseTermTypeCB.isChecked()) {
            mFieldinfoPwLeaseTermTypeCB.setChecked(true);
        } else {
            mFieldinfoPwLeaseTermTypeCB.setChecked(false);
        }
        if (mFieldinfoSizeCB.isChecked()) {
            mFieldinfoPwSizeCB.setChecked(true);
        } else {
            mFieldinfoPwSizeCB.setChecked(false);
        }
        mFieldinfoScreenPW.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setScreenStatus(null);
            }
        });
        mFieldinfoScreenPwView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        screemDataList = new ArrayList<>();
        //筛选押金item数据
        if (mDepositStrList.size() > 0) {
            ArrayList<HashMap<Object,Object>> mDepositTypeList = new ArrayList<HashMap<Object,Object>>();
            for (int i = 0; i < mDepositStrList.size(); i++) {
                HashMap<Object,Object> map = new HashMap<Object,Object>();
                map.put("type","deposit");
                map.put("deposit",mDepositStrList.get(i));
                mDepositTypeList.add(map);
            }
            HashMap<Object,Object> mDepositTypeMap = new HashMap<Object,Object>();
            mDepositTypeMap.put("type","deposit");
            mDepositTypeMap.put("datalist",mDepositTypeList);
            screemDataList.add(mDepositTypeMap);
        }
        //筛选计价单位item数据
        //2018/1/8 周几剔除
        if (mLeaseTermTypeStrList.size() > 0) {
            ArrayList<HashMap<Object,Object>> mLeaseTermTypeList = new ArrayList<HashMap<Object,Object>>();
            for (int i = 0; i < mLeaseTermTypeStrList.size(); i++) {
                HashMap<Object,Object> map = new HashMap<Object,Object>();
                map.put("type","lease_term_type_id");
                map.put("lease_term_type_id",mLeaseTermTypeStrList.get(i));
                mLeaseTermTypeList.add(map);
            }
            HashMap<Object,Object> mLeaseTermTypeMap = new HashMap<Object,Object>();
            mLeaseTermTypeMap.put("type","lease_term_type_id");
            mLeaseTermTypeMap.put("datalist",mLeaseTermTypeList);
            screemDataList.add(mLeaseTermTypeMap);
        }
        //筛选摊位大小item数据
        if (mSizeStrList.size() > 0) {
            ArrayList<HashMap<Object,Object>> mSizeTypeList = new ArrayList<HashMap<Object,Object>>();
            for (int i = 0; i < mSizeStrList.size(); i++) {
                HashMap<Object,Object> map = new HashMap<Object,Object>();
                map.put("type","size");
                map.put("size",mSizeStrList.get(i));
                mSizeTypeList.add(map);
            }
            HashMap<Object,Object> mSizeTypeMap = new HashMap<Object,Object>();
            mSizeTypeMap.put("type","size");
            mSizeTypeMap.put("datalist",mSizeTypeList);
            screemDataList.add(mSizeTypeMap);
        }
        //筛选特殊规格 仅限规格 item数据
        if (mCustomDimensionStrList.size() > 0) {
            ArrayList<HashMap<Object,Object>> mCustomDimensionTypeList = new ArrayList<HashMap<Object,Object>>();
            for (int i = 0; i < mCustomDimensionStrList.size(); i++) {
                HashMap<Object,Object> map = new HashMap<Object,Object>();
                map.put("type","customDimension");
                map.put("customDimension",mCustomDimensionStrList.get(i));
                mCustomDimensionTypeList.add(map);
            }
            HashMap<Object,Object> mCustomDimensionTypeMap = new HashMap<Object,Object>();
            mCustomDimensionTypeMap.put("type","customDimension");
            mCustomDimensionTypeMap.put("datalist",mCustomDimensionTypeList);
            screemDataList.add(mCustomDimensionTypeMap);
        }
        //价格
        HashMap<Object,Object> price_map = new HashMap<Object,Object>();
        price_map.put("type","price");
        price_map.put("itemtype",1);
        screemDataList.add(price_map);
        for (int j = 0;j < screemDataList.size(); j++ ) {
            if (screemDataList.get(j).get("datalist") != null &&
                    ((ArrayList<HashMap<Object, Object>>) screemDataList.get(j).get("datalist")).size() > 0) {
                ArrayList<HashMap<Object, Object>> data_new_temp = new ArrayList<>();
                data_new_temp.addAll(((ArrayList<HashMap<Object, Object>>) (screemDataList.get(j).get("datalist"))));
                for (int i = 0; i < data_new_temp.size(); i++) {
                    ResourcesScreeningItemAdapter.getresourcescreeninglist().put(
                            data_new_temp.get(i).get("type").toString() +
                                    data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(), false);
                    if (mDepositCheckedStrList.contains(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())) {
                        ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString() + data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(), true);
                    }
                    if (mLeaseTermTypeMap.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()) != null) {
                        if (mLeaseTermTypeCheckedStrList.contains(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString().toString())) {
                            ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString() + data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(), true);
                        }
                    }
                    if (mSizeCheckedStrList.contains(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())) {
                        ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString() + data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(), true);
                    }
                    if (mCustomDimensionCheckedStrList.contains(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())) {
                        ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString() + data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(), true);
                    }
                }
            } else {
                if (screemDataList.get(j).get("type").toString().equals("price")) {
                    ResourcesScreeningNewAdapter.getedittextmap().put("pricemin",mScreenMinPrice);
                    ResourcesScreeningNewAdapter.getedittextmap().put("pricemax",mScreenMaxPrice);
//                    if (apiResourcesModel.getMin_year() != null) {
//                        ResourcesScreeningNewAdapter.getedittextmap().put("yearmin",String.valueOf(apiResourcesModel.getMin_year()));
//                    }  else {
//                        ResourcesScreeningNewAdapter.getedittextmap().put("yearmin","");
//                    }
//                    if (apiResourcesModel.getMax_year() != null) {
//                        ResourcesScreeningNewAdapter.getedittextmap().put("yearmax",String.valueOf(apiResourcesModel.getMax_year()));
//                    }  else {
//                        ResourcesScreeningNewAdapter.getedittextmap().put("yearmax","");
//                    }
                }
            }
        }
        final ResourcesScreeningNewAdapter resourcesScreeningNewAdapter = new ResourcesScreeningNewAdapter(FieldInfoActivity.this,
                FieldInfoActivity.this,screemDataList,1);
        fieldinfoScreenLv.setAdapter(resourcesScreeningNewAdapter);
        mresetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int j = 0;j < screemDataList.size(); j++ ) {
                    if (screemDataList.get(j).get("datalist") != null &&
                            ((ArrayList<HashMap<Object, Object>>) screemDataList.get(j).get("datalist")).size() > 0) {
                        ArrayList<HashMap<Object, Object>> data_new_temp = new ArrayList<>();
                        data_new_temp.addAll(((ArrayList<HashMap<Object, Object>>) (screemDataList.get(j).get("datalist"))));
                        for (int i = 0; i < data_new_temp.size(); i++) {
                            ResourcesScreeningItemAdapter.getresourcescreeninglist().put(
                                    data_new_temp.get(i).get("type").toString() +
                                            data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(), false);
                        }
                    } else {
                        ResourcesScreeningNewAdapter.getedittextmap().put(screemDataList.get(j).get("type").toString()+"min","");
                        ResourcesScreeningNewAdapter.getedittextmap().put(screemDataList.get(j).get("type").toString()+"max","");
                    }
                }
                resourcesScreeningNewAdapter.notifyDataSetChanged();
                setScreenStatus(0);
            }
        });
        mconfirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭软键盘
                InputMethodManager imm = (InputMethodManager) FieldInfoActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(fieldinfoScreenLv.getWindowToken(), 0);
                }
                fieldinfoScreenLv.clearFocus();
                if (mDepositCheckedStrList != null) {
                    mDepositCheckedStrList.clear();
                }
                if (mLeaseTermTypeCheckedStrList != null) {
                    mLeaseTermTypeCheckedStrList.clear();
                }
                if (mSizeCheckedStrList != null) {
                    mSizeCheckedStrList.clear();
                }
                if (mCustomDimensionCheckedStrList != null) {
                    mCustomDimensionCheckedStrList.clear();
                }
                for (int j = 0;j < screemDataList.size(); j++ ) {
                    if (screemDataList.get(j).get("datalist") != null &&
                            ((ArrayList<HashMap<Object,Object>>)screemDataList.get(j).get("datalist")).size() > 0) {
                        ArrayList<HashMap<Object,Object>> data_new_temp = new ArrayList<>();
                        data_new_temp.addAll(((ArrayList<HashMap<Object,Object>>) (screemDataList.get(j).get("datalist"))));
                        for (int i = 0; i <data_new_temp.size(); i++) {
                            if ((boolean)ResourcesScreeningItemAdapter.getresourcescreeninglist().get(data_new_temp.get(i).get("type").toString()+data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())) {
                                if (data_new_temp.get(i).get("type").toString().equals("deposit")) {
                                    mDepositCheckedStrList.add(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString());
                                } else if (data_new_temp.get(i).get("type").toString().equals("lease_term_type_id")) {
                                    mLeaseTermTypeCheckedStrList.add(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString());
                                } else if (data_new_temp.get(i).get("type").toString().equals("size")) {
                                    mSizeCheckedStrList.add(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString());
                                } else if (data_new_temp.get(i).get("type").toString().equals("customDimension")) {
                                    mCustomDimensionCheckedStrList.add(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString());
                                }
                            }
                        }
                    } else {
                        if (screemDataList.get(j).get("type").toString().equals("price")) {
                            if (ResourcesScreeningNewAdapter.getedittextmap().get("pricemin").toString().length() > 0 &&
                                    ResourcesScreeningNewAdapter.getedittextmap().get("pricemax").toString().length() > 0) {
                                if (Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("pricemin").toString(), 1),1)) >
                                        Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("pricemax").toString(), 1),1))) {
                                    ResourcesScreeningNewAdapter.getedittextmap().put("pricemin","");
                                    ResourcesScreeningNewAdapter.getedittextmap().put("pricemax","");
                                    MessageUtils.showToast(getResources().getString(R.string.resourcesscreening_edittext_priceerror));
                                    resourcesScreeningNewAdapter.notifyDataSetChanged();
                                    return;
                                }
                            }
                            mScreenMinPrice = ResourcesScreeningNewAdapter.getedittextmap().get("pricemin").toString();
                            mScreenMaxPrice = ResourcesScreeningNewAdapter.getedittextmap().get("pricemax").toString();
                        }
                    }
                }
                updataSizeLv(true);
                if (mFieldinfoPwDepositCB.isChecked()) {
                    mFieldinfoDepositCB.setChecked(true);
                } else {
                    mFieldinfoDepositCB.setChecked(false);
                }
                if (mFieldinfoPwHaveDepositCB.isChecked()) {
                    mFieldinfoHaveDepositCB.setChecked(true);
                } else {
                    mFieldinfoHaveDepositCB.setChecked(false);
                }
                if (mFieldinfoPwLeaseTermTypeCB.isChecked()) {
                    mFieldinfoLeaseTermTypeCB.setChecked(true);
                } else {
                    mFieldinfoLeaseTermTypeCB.setChecked(false);
                }
                if (mFieldinfoPwSizeCB.isChecked()) {
                    mFieldinfoSizeCB.setChecked(true);
                } else {
                    mFieldinfoSizeCB.setChecked(false);
                }
                mFieldinfoScreenPW.dismiss();
            }
        });
        mFieldinfoPwDepositCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFieldinfoPwDepositCB.isChecked()) {
                    mFieldinfoPwDepositCB.setChecked(true);
                    if (!(boolean)ResourcesScreeningItemAdapter.getresourcescreeninglist().get("deposit" +
                            getResources().getString(R.string.fieldinfo_no_deposit_str))) {
                        ResourcesScreeningItemAdapter.getresourcescreeninglist().put("deposit" +
                                getResources().getString(R.string.fieldinfo_no_deposit_str),true);
                        resourcesScreeningNewAdapter.notifyDataSetChanged();
                    }
                    setScreenStatus(1);
                } else {
                    mFieldinfoPwDepositCB.setChecked(false);
                    if ((boolean)ResourcesScreeningItemAdapter.getresourcescreeninglist().get("deposit" +
                            getResources().getString(R.string.fieldinfo_no_deposit_str))) {
                        ResourcesScreeningItemAdapter.getresourcescreeninglist().put("deposit" +
                                getResources().getString(R.string.fieldinfo_no_deposit_str),false);
                        resourcesScreeningNewAdapter.notifyDataSetChanged();
                    }
                    if (!getScreenTrueStatus()) {
                        setScreenStatus(0);
                    }
                }
            }
        });
        mFieldinfoPwHaveDepositCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFieldinfoPwHaveDepositCB.isChecked()) {
                    mFieldinfoPwHaveDepositCB.setChecked(true);
                    if (!(boolean)ResourcesScreeningItemAdapter.getresourcescreeninglist().get("deposit" +
                            getResources().getString(R.string.fieldinfo_have_deposit_str))) {
                        ResourcesScreeningItemAdapter.getresourcescreeninglist().put("deposit" +
                                getResources().getString(R.string.fieldinfo_have_deposit_str),true);
                        resourcesScreeningNewAdapter.notifyDataSetChanged();
                    }
                    setScreenStatus(1);
                } else {
                    mFieldinfoPwHaveDepositCB.setChecked(false);
                    if ((boolean)ResourcesScreeningItemAdapter.getresourcescreeninglist().get("deposit" +
                            getResources().getString(R.string.fieldinfo_have_deposit_str))) {
                        ResourcesScreeningItemAdapter.getresourcescreeninglist().put("deposit" +
                                getResources().getString(R.string.fieldinfo_have_deposit_str),false);
                        resourcesScreeningNewAdapter.notifyDataSetChanged();
                    }
                    if (!getScreenTrueStatus()) {
                        setScreenStatus(0);
                    }
                }
            }
        });
        mFieldinfoPwLeaseTermTypeCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFieldinfoPwLeaseTermTypeCB.isChecked()) {
                    mFieldinfoPwLeaseTermTypeCB.setChecked(true);
                    if (!(boolean)ResourcesScreeningItemAdapter.getresourcescreeninglist().get("lease_term_type_id" +
                            mFieldinfoPwLeaseTermTypeCB.getText().toString().split(" ")[1])) {
                        ResourcesScreeningItemAdapter.getresourcescreeninglist().put("lease_term_type_id" +
                                mFieldinfoPwLeaseTermTypeCB.getText().toString().split(" ")[1],true);
                        resourcesScreeningNewAdapter.notifyDataSetChanged();
                    }
                    setScreenStatus(1);
                } else {
                    mFieldinfoPwLeaseTermTypeCB.setChecked(false);
                    if ((boolean)ResourcesScreeningItemAdapter.getresourcescreeninglist().get("lease_term_type_id" +
                            mFieldinfoPwLeaseTermTypeCB.getText().toString().split(" ")[1])) {
                        ResourcesScreeningItemAdapter.getresourcescreeninglist().put("lease_term_type_id" +
                                mFieldinfoPwLeaseTermTypeCB.getText().toString().split(" ")[1],false);
                        resourcesScreeningNewAdapter.notifyDataSetChanged();
                    }
                    if (!getScreenTrueStatus()) {
                        setScreenStatus(0);
                    }
                }
            }
        });
        mFieldinfoPwSizeCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFieldinfoPwSizeCB.isChecked()) {
                    mFieldinfoPwSizeCB.setChecked(true);
                    if (!(boolean)ResourcesScreeningItemAdapter.getresourcescreeninglist().get("size" +
                            mFieldinfoPwSizeCB.getText().toString())) {
                        ResourcesScreeningItemAdapter.getresourcescreeninglist().put("size" +
                                mFieldinfoPwSizeCB.getText().toString(),true);
                    }
                    resourcesScreeningNewAdapter.notifyDataSetChanged();
                    setScreenStatus(1);
                } else {
                    mFieldinfoPwSizeCB.setChecked(false);
                    if ((boolean)ResourcesScreeningItemAdapter.getresourcescreeninglist().get("size" +
                            mFieldinfoPwSizeCB.getText().toString())) {
                        ResourcesScreeningItemAdapter.getresourcescreeninglist().put("size" +
                                mFieldinfoPwSizeCB.getText().toString(),false);
                        resourcesScreeningNewAdapter.notifyDataSetChanged();
                    }
                    if (!getScreenTrueStatus()) {
                        setScreenStatus(0);
                    }
                }
            }
        });
        setScreenStatus(null);
    }
    //线程中更新筛选内容
    private void updataSizeLv(boolean isShow) {
        if (isShow) {
            showProgressDialog();
        }
        new Thread() {
            @Override
            public void run() {
                ArrayList<String> mLeaseTermTypeCheckedStrListTmp = new ArrayList<String>();
                ArrayList<String> mSizeCheckedStrListTmp = new ArrayList<String>();
                ArrayList<String> mDepositCheckedStrListTmp = new ArrayList<String>();
                ArrayList<String> mCustomDimensionCheckedStrListTmp = new ArrayList<String>();
                if (mDepositCheckedStrList.size() == 0) {
                    mDepositCheckedStrListTmp.addAll(mDepositStrList);
                } else {
                    mDepositCheckedStrListTmp.addAll(mDepositCheckedStrList);
                }

                if (mLeaseTermTypeCheckedStrList.size() == 0) {
                    mLeaseTermTypeCheckedStrListTmp.addAll(mLeaseTermTypeStrList);
                } else {
                    mLeaseTermTypeCheckedStrListTmp.addAll(mLeaseTermTypeCheckedStrList);
                }
                if (mSizeCheckedStrList.size() == 0) {
                    mSizeCheckedStrListTmp.addAll(mSizeStrList);
                } else {
                    mSizeCheckedStrListTmp.addAll(mSizeCheckedStrList);
                }
                if (mCustomDimensionCheckedStrList.size() > 0) {
                    mCustomDimensionCheckedStrListTmp.addAll(mCustomDimensionCheckedStrList);
                }
                ArrayList<FieldInfoSizeModel> mDimensionsDataListTmp = new ArrayList<>();
                String minPrice = "";
                if (mApiResourcesModel.getMin_price() != null && mApiResourcesModel.getMin_price().length() > 0) {
                    minPrice = mApiResourcesModel.getMin_price();
                }
                String maxPrice = "";
                if (mApiResourcesModel.getMax_price() != null && mApiResourcesModel.getMax_price().length() > 0) {
                    maxPrice = mApiResourcesModel.getMax_price();
                }
                for (int i = 0; i < mDimensionsDataScreenList.size(); i++) {
                    if (mDepositCheckedStrListTmp.size() == 2) {
                        if (mLeaseTermTypeCheckedStrListTmp.contains(mDimensionsDataScreenList.get(i).getDimension().getLease_term_type()) &&
                                mSizeCheckedStrListTmp.contains(mDimensionsDataScreenList.get(i).getDimension().getSize())) {
                            if (mCustomDimensionCheckedStrList.size() > 0) {
                                if (mDimensionsDataScreenList.get(i).getDimension().getCustom_dimension() != null &&
                                        mCustomDimensionCheckedStrListTmp.contains(mDimensionsDataScreenList.get(i).getDimension().getCustom_dimension())) {
                                    if (minPrice.length() > 0 || maxPrice.length() > 0 ||
                                            mScreenMinPrice.length() > 0 || mScreenMaxPrice.length() > 0) {
                                        ArrayList<FieldInfoSellResourceModel> resource = new ArrayList<>();
                                        ArrayList<FieldInfoSellResourceModel> screenResource = new ArrayList<>();
                                        for (int j = 0; j < mDimensionsDataScreenList.get(i).getResource().size(); j++) {
                                            if (minPrice.length() > 0) {
                                                if (maxPrice.length() > 0) {
                                                    if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                            Double.parseDouble(minPrice) &&
                                                            mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                    Double.parseDouble(maxPrice)) {
                                                        resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                    }
                                                } else {
                                                    if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                            Double.parseDouble(minPrice)) {
                                                        resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                    }
                                                }
                                            } else {
                                                if (maxPrice.length() > 0) {
                                                    if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                            Double.parseDouble(maxPrice)) {
                                                        resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                    }
                                                }
                                            }
                                            if (mScreenMinPrice.length() > 0 || mScreenMaxPrice.length() > 0) {
                                                if (mScreenMinPrice.length() > 0) {
                                                    if (mScreenMaxPrice.length() > 0) {
                                                        if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                Double.parseDouble(mScreenMinPrice)*100 &&
                                                                mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                        Double.parseDouble(mScreenMaxPrice)*100) {
                                                            screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                        }
                                                    } else {
                                                        if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                Double.parseDouble(mScreenMinPrice)*100) {
                                                            screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                        }
                                                    }
                                                } else {
                                                    if (mScreenMaxPrice.length() > 0) {
                                                        if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                Double.parseDouble(mScreenMaxPrice)*100) {
                                                            screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        ArrayList<FieldInfoSellResourceModel> sizeResource = new ArrayList<>();
                                        if (resource.size() > 0 && screenResource.size() > 0) {
                                            for (int k = 0; k < screenResource.size(); k++) {
                                                for (int l = 0; l < resource.size(); l++) {
                                                    if (resource.get(l).getId() - screenResource.get(k).getId() == 0) {
                                                        sizeResource.add(screenResource.get(k));
                                                        break;
                                                    }
                                                }
                                            }
                                        } else {
                                            if (resource.size() > 0) {
                                                if (mScreenMinPrice.length() == 0 || mScreenMaxPrice.length() == 0) {
                                                    sizeResource.addAll(resource);
                                                }
                                            } else if (screenResource.size() > 0) {
                                                if (minPrice.length() == 0 || maxPrice.length() == 0) {
                                                    sizeResource.addAll(screenResource);
                                                }
                                            }
                                        }
                                        if (sizeResource.size() > 0) {
                                            FieldInfoSizeModel fieldInfoSizeModel = new FieldInfoSizeModel();
                                            ArrayList<FieldInfoSizeModel> dimensionsTmpList = new ArrayList<>();
                                            try {
                                                dimensionsTmpList = (ArrayList<FieldInfoSizeModel>) com.linhuiba.linhuifield.connector.Constants.deepCopy(mDimensionsDataScreenList);
                                                fieldInfoSizeModel = dimensionsTmpList.get(i);
                                                fieldInfoSizeModel.setResource(sizeResource);
                                                mDimensionsDataListTmp.add(fieldInfoSizeModel);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (ClassNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        mDimensionsDataListTmp.add(mDimensionsDataScreenList.get(i));
                                    }
                                }
                            } else {
                                if (minPrice.length() > 0 || maxPrice.length() > 0 || mScreenMinPrice.length() > 0 || mScreenMaxPrice.length() > 0) {
                                    ArrayList<FieldInfoSellResourceModel> resource = new ArrayList<>();
                                    ArrayList<FieldInfoSellResourceModel> screenResource = new ArrayList<>();
                                    for (int j = 0; j < mDimensionsDataScreenList.get(i).getResource().size(); j++) {
                                        if (minPrice.length() > 0) {
                                            if (maxPrice.length() > 0) {
                                                if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                        Double.parseDouble(minPrice) &&
                                                        mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                Double.parseDouble(maxPrice)) {
                                                    resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                }
                                            } else {
                                                if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                        Double.parseDouble(minPrice)) {
                                                    resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                }
                                            }
                                        } else {
                                            if (maxPrice.length() > 0) {
                                                if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                        Double.parseDouble(maxPrice)) {
                                                    resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                }
                                            }
                                        }
                                        if (mScreenMinPrice.length() > 0 || mScreenMaxPrice.length() > 0) {
                                            if (mScreenMinPrice.length() > 0) {
                                                if (mScreenMaxPrice.length() > 0) {
                                                    if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                            Double.parseDouble(mScreenMinPrice)*100 &&
                                                            mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                    Double.parseDouble(mScreenMaxPrice)*100) {
                                                        screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                    }
                                                } else {
                                                    if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                            Double.parseDouble(mScreenMinPrice) *100) {
                                                        screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                    }
                                                }
                                            } else {
                                                if (mScreenMaxPrice.length() > 0) {
                                                    if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                            Double.parseDouble(mScreenMaxPrice)*100) {
                                                        screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    ArrayList<FieldInfoSellResourceModel> sizeResource = new ArrayList<>();
                                    if (resource.size() > 0 && screenResource.size() > 0) {
                                        for (int k = 0; k < screenResource.size(); k++) {
                                            for (int l = 0; l < resource.size(); l++) {
                                                if (resource.get(l).getId() - screenResource.get(k).getId() == 0) {
                                                    sizeResource.add(screenResource.get(k));
                                                    break;
                                                }
                                            }
                                        }
                                    } else {
                                        if (resource.size() > 0) {
                                            if (mScreenMinPrice.length() == 0 || mScreenMaxPrice.length() == 0) {
                                                sizeResource.addAll(resource);
                                            }
                                        } else if (screenResource.size() > 0) {
                                            if (minPrice.length() == 0 || maxPrice.length() == 0) {
                                                sizeResource.addAll(screenResource);
                                            }
                                        }
                                    }
                                    if (sizeResource.size() > 0) {
                                        FieldInfoSizeModel fieldInfoSizeModel = new FieldInfoSizeModel();
                                        ArrayList<FieldInfoSizeModel> dimensionsTmpList = new ArrayList<>();
                                        try {
                                            dimensionsTmpList = (ArrayList<FieldInfoSizeModel>) com.linhuiba.linhuifield.connector.Constants.deepCopy(mDimensionsDataScreenList);
                                            fieldInfoSizeModel = dimensionsTmpList.get(i);
                                            fieldInfoSizeModel.setResource(sizeResource);
                                            mDimensionsDataListTmp.add(fieldInfoSizeModel);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    mDimensionsDataListTmp.add(mDimensionsDataScreenList.get(i));
                                }
                            }
                        }
                    } else if (mDepositCheckedStrListTmp.size() == 1) {
                        if (mDepositCheckedStrListTmp.get(0).equals(getResources().getString(R.string.fieldinfo_no_deposit_str))) {
                            if (mLeaseTermTypeCheckedStrListTmp.contains(mDimensionsDataScreenList.get(i).getDimension().getLease_term_type()) &&
                                    mSizeCheckedStrListTmp.contains(mDimensionsDataScreenList.get(i).getDimension().getSize())) {
                                ArrayList<FieldInfoSellResourceModel> sizeResource = new ArrayList<>();
                                ArrayList<FieldInfoSellResourceModel> resource = new ArrayList<>();
                                ArrayList<FieldInfoSellResourceModel> screenResource = new ArrayList<>();
                                if (mCustomDimensionCheckedStrListTmp.size() > 0) {
                                    if (mDimensionsDataScreenList.get(i).getDimension().getCustom_dimension() != null &&
                                            mCustomDimensionCheckedStrListTmp.contains(mDimensionsDataScreenList.get(i).getDimension().getCustom_dimension())) {
                                        for (int j = 0; j < mDimensionsDataScreenList.get(i).getResource().size(); j++) {
                                            if (mDimensionsDataScreenList.get(i).getResource().get(j).getDeposit() == null ||
                                                    mDimensionsDataScreenList.get(i).getResource().get(j).getDeposit() == 0) {
                                                if (minPrice.length() > 0 || maxPrice.length() > 0 ||
                                                        mScreenMinPrice.length() > 0 || mScreenMaxPrice.length() > 0) {
                                                    if (minPrice.length() > 0) {
                                                        if (maxPrice.length() > 0) {
                                                            if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                    Double.parseDouble(minPrice) &&
                                                                    mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                            Double.parseDouble(maxPrice)) {
                                                                resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                            }
                                                        } else {
                                                            if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                    Double.parseDouble(minPrice)) {
                                                                resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                            }
                                                        }
                                                    } else {
                                                        if (maxPrice.length() > 0) {
                                                            if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                    Double.parseDouble(maxPrice)) {
                                                                resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                            }
                                                        }
                                                    }
                                                    if (mScreenMinPrice.length() > 0 || mScreenMaxPrice.length() > 0) {
                                                        if (mScreenMinPrice.length() > 0) {
                                                            if (mScreenMaxPrice.length() > 0) {
                                                                if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                        Double.parseDouble(mScreenMinPrice)*100 &&
                                                                        mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                                Double.parseDouble(mScreenMaxPrice)*100) {
                                                                    screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                                }
                                                            } else {
                                                                if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                        Double.parseDouble(mScreenMinPrice)*100) {
                                                                    screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                                }
                                                            }
                                                        } else {
                                                            if (mScreenMaxPrice.length() > 0) {
                                                                if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                        Double.parseDouble(mScreenMaxPrice)*100) {
                                                                    screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (resource.size() > 0 && screenResource.size() > 0) {
                                                        for (int k = 0; k < screenResource.size(); k++) {
                                                            for (int l = 0; l < resource.size(); l++) {
                                                                if (resource.get(l).getId() - screenResource.get(k).getId() == 0) {
                                                                    sizeResource.add(screenResource.get(k));
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if (resource.size() > 0) {
                                                            if (mScreenMinPrice.length() == 0 || mScreenMaxPrice.length() == 0) {
                                                                sizeResource.addAll(resource);
                                                            }
                                                        } else if (screenResource.size() > 0) {
                                                            if (minPrice.length() == 0 || maxPrice.length() == 0) {
                                                                sizeResource.addAll(screenResource);
                                                            }
                                                        }
                                                    }
                                                    if (sizeResource.size() > 0) {
                                                        FieldInfoSizeModel fieldInfoSizeModel = new FieldInfoSizeModel();
                                                        ArrayList<FieldInfoSizeModel> dimensionsTmpList = new ArrayList<>();
                                                        try {
                                                            dimensionsTmpList = (ArrayList<FieldInfoSizeModel>) com.linhuiba.linhuifield.connector.Constants.deepCopy(mDimensionsDataScreenList);
                                                            fieldInfoSizeModel = dimensionsTmpList.get(i);
                                                            fieldInfoSizeModel.setResource(sizeResource);
                                                            mDimensionsDataListTmp.add(fieldInfoSizeModel);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (ClassNotFoundException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                } else {
                                                    sizeResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    for (int j = 0; j < mDimensionsDataScreenList.get(i).getResource().size(); j++) {
                                        if (mDimensionsDataScreenList.get(i).getResource().get(j).getDeposit() == null ||
                                                mDimensionsDataScreenList.get(i).getResource().get(j).getDeposit() == 0) {
                                            if (minPrice.length() > 0 || maxPrice.length() > 0 ||
                                                    mScreenMinPrice.length() > 0 || mScreenMaxPrice.length() > 0) {
                                                if (minPrice.length() > 0) {
                                                    if (maxPrice.length() > 0) {
                                                        if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                Double.parseDouble(minPrice) &&
                                                                mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                        Double.parseDouble(maxPrice)) {
                                                            resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                        }
                                                    } else {
                                                        if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                Double.parseDouble(minPrice)) {
                                                            resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                        }
                                                    }
                                                } else {
                                                    if (maxPrice.length() > 0) {
                                                        if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                Double.parseDouble(maxPrice)) {
                                                            resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                        }
                                                    }
                                                }
                                                if (mScreenMinPrice.length() > 0 || mScreenMaxPrice.length() > 0) {
                                                    if (mScreenMinPrice.length() > 0) {
                                                        if (mScreenMaxPrice.length() > 0) {
                                                            if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                    Double.parseDouble(mScreenMinPrice)*100 &&
                                                                    mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                            Double.parseDouble(mScreenMaxPrice)*100) {
                                                                screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                            }
                                                        } else {
                                                            if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                    Double.parseDouble(mScreenMinPrice)*100) {
                                                                screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                            }
                                                        }
                                                    } else {
                                                        if (mScreenMaxPrice.length() > 0) {
                                                            if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                    Double.parseDouble(mScreenMaxPrice)*100) {
                                                                screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                            }
                                                        }
                                                    }
                                                }
                                                if (resource.size() > 0 && screenResource.size() > 0) {
                                                    for (int k = 0; k < screenResource.size(); k++) {
                                                        for (int l = 0; l < resource.size(); l++) {
                                                            if (resource.get(l).getId() - screenResource.get(k).getId() == 0) {
                                                                sizeResource.add(screenResource.get(k));
                                                                break;
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    if (resource.size() > 0) {
                                                        if (mScreenMinPrice.length() == 0 || mScreenMaxPrice.length() == 0) {
                                                            sizeResource.addAll(resource);
                                                        }
                                                    } else if (screenResource.size() > 0) {
                                                        if (minPrice.length() == 0 || maxPrice.length() == 0) {
                                                            sizeResource.addAll(screenResource);
                                                        }
                                                    }
                                                }
                                                if (sizeResource.size() > 0) {
                                                    FieldInfoSizeModel fieldInfoSizeModel = new FieldInfoSizeModel();
                                                    ArrayList<FieldInfoSizeModel> dimensionsTmpList = new ArrayList<>();
                                                    try {
                                                        dimensionsTmpList = (ArrayList<FieldInfoSizeModel>) com.linhuiba.linhuifield.connector.Constants.deepCopy(mDimensionsDataScreenList);
                                                        fieldInfoSizeModel = dimensionsTmpList.get(i);
                                                        fieldInfoSizeModel.setResource(sizeResource);
                                                        mDimensionsDataListTmp.add(fieldInfoSizeModel);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    } catch (ClassNotFoundException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            } else {
                                                sizeResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                            }
                                        }
                                    }
                                }
                                if (sizeResource.size() > 0) {
                                    FieldInfoSizeModel fieldInfoSizeModel = new FieldInfoSizeModel();
                                    ArrayList<FieldInfoSizeModel> dimensionsTmpList = new ArrayList<>();
//                                    dimensionsTmpList = (ArrayList<FieldInfoSizeModel>) mDimensionsDataScreenList.clone();
//                                    fieldInfoSizeModel = dimensionsTmpList.get(i);
//                                    fieldInfoSizeModel.setResource(resource);
//                                    mDimensionsDataListTmp.add(fieldInfoSizeModel);
                                    try {
                                        dimensionsTmpList = (ArrayList<FieldInfoSizeModel>) com.linhuiba.linhuifield.connector.Constants.deepCopy(mDimensionsDataScreenList);
                                        fieldInfoSizeModel = dimensionsTmpList.get(i);
                                        fieldInfoSizeModel.setResource(sizeResource);
                                        mDimensionsDataListTmp.add(fieldInfoSizeModel);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        } else if (mDepositCheckedStrListTmp.get(0).equals(getResources().getString(R.string.fieldinfo_have_deposit_str))) {
                            if (mLeaseTermTypeCheckedStrListTmp.contains(mDimensionsDataScreenList.get(i).getDimension().getLease_term_type()) &&
                                    mSizeCheckedStrListTmp.contains(mDimensionsDataScreenList.get(i).getDimension().getSize())) {
                                ArrayList<FieldInfoSellResourceModel> sizeResource = new ArrayList<>();
                                ArrayList<FieldInfoSellResourceModel> resource = new ArrayList<>();
                                ArrayList<FieldInfoSellResourceModel> screenResource = new ArrayList<>();
                                if (mCustomDimensionCheckedStrListTmp.size() > 0) {
                                    if (mDimensionsDataScreenList.get(i).getDimension().getCustom_dimension() != null &&
                                            mCustomDimensionCheckedStrListTmp.contains(mDimensionsDataScreenList.get(i).getDimension().getCustom_dimension())) {
                                        for (int j = 0; j < mDimensionsDataScreenList.get(i).getResource().size(); j++) {
                                            if (mDimensionsDataScreenList.get(i).getResource().get(j).getDeposit() != null &&
                                                    mDimensionsDataScreenList.get(i).getResource().get(j).getDeposit() > 0) {
                                                if (minPrice.length() > 0 || maxPrice.length() > 0 ||
                                                        mScreenMinPrice.length() > 0 || mScreenMaxPrice.length() > 0) {
                                                    if (minPrice.length() > 0) {
                                                        if (maxPrice.length() > 0) {
                                                            if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                    Double.parseDouble(minPrice) &&
                                                                    mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                            Double.parseDouble(maxPrice)) {
                                                                resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                            }
                                                        } else {
                                                            if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                    Double.parseDouble(minPrice)) {
                                                                resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                            }
                                                        }
                                                    } else {
                                                        if (maxPrice.length() > 0) {
                                                            if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                    Double.parseDouble(maxPrice)) {
                                                                resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                            }
                                                        }
                                                    }
                                                    if (mScreenMinPrice.length() > 0 || mScreenMaxPrice.length() > 0) {
                                                        if (mScreenMinPrice.length() > 0) {
                                                            if (mScreenMaxPrice.length() > 0) {
                                                                if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                        Double.parseDouble(mScreenMinPrice)*100 &&
                                                                        mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                                Double.parseDouble(mScreenMaxPrice)*100) {
                                                                    screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                                }
                                                            } else {
                                                                if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                        Double.parseDouble(mScreenMinPrice)*100) {
                                                                    screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                                }
                                                            }
                                                        } else {
                                                            if (mScreenMaxPrice.length() > 0) {
                                                                if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                        Double.parseDouble(mScreenMaxPrice)*100) {
                                                                    screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (resource.size() > 0 && screenResource.size() > 0) {
                                                        for (int k = 0; k < screenResource.size(); k++) {
                                                            for (int l = 0; l < resource.size(); l++) {
                                                                if (resource.get(l).getId() - screenResource.get(k).getId() == 0) {
                                                                    sizeResource.add(screenResource.get(k));
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if (resource.size() > 0) {
                                                            if (mScreenMinPrice.length() == 0 || mScreenMaxPrice.length() == 0) {
                                                                sizeResource.addAll(resource);
                                                            }
                                                        } else if (screenResource.size() > 0) {
                                                            if (minPrice.length() == 0 || maxPrice.length() == 0) {
                                                                sizeResource.addAll(screenResource);
                                                            }
                                                        }
                                                    }
                                                    if (sizeResource.size() > 0) {
                                                        FieldInfoSizeModel fieldInfoSizeModel = new FieldInfoSizeModel();
                                                        ArrayList<FieldInfoSizeModel> dimensionsTmpList = new ArrayList<>();
                                                        try {
                                                            dimensionsTmpList = (ArrayList<FieldInfoSizeModel>) com.linhuiba.linhuifield.connector.Constants.deepCopy(mDimensionsDataScreenList);
                                                            fieldInfoSizeModel = dimensionsTmpList.get(i);
                                                            fieldInfoSizeModel.setResource(sizeResource);
                                                            mDimensionsDataListTmp.add(fieldInfoSizeModel);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (ClassNotFoundException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                } else {
                                                    sizeResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    for (int j = 0; j < mDimensionsDataScreenList.get(i).getResource().size(); j++) {
                                        if (mDimensionsDataScreenList.get(i).getResource().get(j).getDeposit() != null &&
                                                mDimensionsDataScreenList.get(i).getResource().get(j).getDeposit() > 0) {
                                            if (minPrice.length() > 0 || maxPrice.length() > 0 ||
                                                    mScreenMinPrice.length() > 0 || mScreenMaxPrice.length() > 0) {
                                                if (minPrice.length() > 0) {
                                                    if (maxPrice.length() > 0) {
                                                        if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                Double.parseDouble(minPrice) &&
                                                                mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                        Double.parseDouble(maxPrice)) {
                                                            resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                        }
                                                    } else {
                                                        if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                Double.parseDouble(minPrice)) {
                                                            resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                        }
                                                    }
                                                } else {
                                                    if (maxPrice.length() > 0) {
                                                        if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                Double.parseDouble(maxPrice)) {
                                                            resource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                        }
                                                    }
                                                }
                                                if (mScreenMinPrice.length() > 0 || mScreenMaxPrice.length() > 0) {
                                                    if (mScreenMinPrice.length() > 0) {
                                                        if (mScreenMaxPrice.length() > 0) {
                                                            if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                    Double.parseDouble(mScreenMinPrice)*100 &&
                                                                    mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                            Double.parseDouble(mScreenMaxPrice)*100) {
                                                                screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                            }
                                                        } else {
                                                            if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() >=
                                                                    Double.parseDouble(mScreenMinPrice)*100) {
                                                                screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                            }
                                                        }
                                                    } else {
                                                        if (mScreenMaxPrice.length() > 0) {
                                                            if (mDimensionsDataScreenList.get(i).getResource().get(j).getMin_after_subsidy() <=
                                                                    Double.parseDouble(mScreenMaxPrice)*100) {
                                                                screenResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                                            }
                                                        }
                                                    }
                                                }
                                                if (resource.size() > 0 && screenResource.size() > 0) {
                                                    for (int k = 0; k < screenResource.size(); k++) {
                                                        for (int l = 0; l < resource.size(); l++) {
                                                            if (resource.get(l).getId() - screenResource.get(k).getId() == 0) {
                                                                sizeResource.add(screenResource.get(k));
                                                                break;
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    if (resource.size() > 0) {
                                                        if (mScreenMinPrice.length() == 0 || mScreenMaxPrice.length() == 0) {
                                                            sizeResource.addAll(resource);
                                                        }
                                                    } else if (screenResource.size() > 0) {
                                                        if (minPrice.length() == 0 || maxPrice.length() == 0) {
                                                            sizeResource.addAll(screenResource);
                                                        }
                                                    }
                                                }
                                                if (sizeResource.size() > 0) {
                                                    FieldInfoSizeModel fieldInfoSizeModel = new FieldInfoSizeModel();
                                                    ArrayList<FieldInfoSizeModel> dimensionsTmpList = new ArrayList<>();
                                                    try {
                                                        dimensionsTmpList = (ArrayList<FieldInfoSizeModel>) com.linhuiba.linhuifield.connector.Constants.deepCopy(mDimensionsDataScreenList);
                                                        fieldInfoSizeModel = dimensionsTmpList.get(i);
                                                        fieldInfoSizeModel.setResource(sizeResource);
                                                        mDimensionsDataListTmp.add(fieldInfoSizeModel);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    } catch (ClassNotFoundException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            } else {
                                                sizeResource.add(mDimensionsDataScreenList.get(i).getResource().get(j));
                                            }
                                        }
                                    }
                                }
                                if (sizeResource.size() > 0) {
                                    FieldInfoSizeModel fieldInfoSizeModel = new FieldInfoSizeModel();
                                    ArrayList<FieldInfoSizeModel> dimensionsTmpList = new ArrayList<>();
                                    try {
                                        dimensionsTmpList = (ArrayList<FieldInfoSizeModel>) com.linhuiba.linhuifield.connector.Constants.deepCopy(mDimensionsDataScreenList);
                                        fieldInfoSizeModel = dimensionsTmpList.get(i);
                                        fieldInfoSizeModel.setResource(sizeResource);
                                        mDimensionsDataListTmp.add(fieldInfoSizeModel);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
                Message msg = new Message();
                Bundle bundle=new Bundle();
                bundle.putSerializable("FieldInfoSizeModel",mDimensionsDataListTmp);//bundle中也可以放序列化或包裹化的类对象数据
                msg = updataSizeHandler.obtainMessage();//每发送一次都要重新获取
                msg.what = 1;
                msg.setData(bundle);
                updataSizeHandler.sendMessage(msg);//用handler向主线程发送信息
            }
        }.start();

    }
    private Handler updataSizeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //更新筛选数据
                Bundle bundle = msg.getData();
                ArrayList<FieldInfoSizeModel> mDimensionsDataListTmp =
                        (ArrayList<FieldInfoSizeModel>) bundle.getSerializable("FieldInfoSizeModel");
                if (mDimensionsDataListTmp.size() > 0) {
                    mFieldinfoNoScreenLL.setVisibility(View.GONE);
                    mFieldinfoSizeNoTV.setVisibility(View.GONE);
                    if (mDimensionsDataList != null) {
                        mDimensionsDataList.clear();
                    }
                    mDimensionsDataList.addAll(mDimensionsDataListTmp);
                    List<List<FieldInfoSellResourceModel>> datas = new ArrayList<>();
                    for (int i = 0; i < mDimensionsDataList.size(); i++) {
                        datas.add(mDimensionsDataList.get(i).getResource());
                    }
                    adapter = new FieldInfoPriceSizeAdapter(
                            FieldInfoActivity.this,FieldInfoActivity.this,mDimensionsDataList,datas);
                    mFieldInfoSizeLV.setAdapter(adapter);
                    mFieldInfoSizeLV.setGroupIndicator(null);
                    for (int i = 0; i < mDimensionsDataList.size(); i++) {
                        mFieldInfoSizeLV.expandGroup(i);
                    }
                } else {
                    if (mDimensionsDataList != null) {
                        mDimensionsDataList.clear();
                    }
                    List<List<FieldInfoSellResourceModel>> datas = new ArrayList<>();
                    adapter = new FieldInfoPriceSizeAdapter(
                            FieldInfoActivity.this,FieldInfoActivity.this,mDimensionsDataList,datas);
                    mFieldInfoSizeLV.setAdapter(adapter);
                    mFieldInfoSizeLV.setGroupIndicator(null);
                    for (int i = 0; i < mDimensionsDataList.size(); i++) {
                        mFieldInfoSizeLV.expandGroup(i);
                    }
                    final ArrayList<HashMap<Object,Object>> noScreenList = new ArrayList<HashMap<Object,Object>>();
                    if (mDepositCheckedStrList.size() > 0) {
                        for (int i = 0; i < mDepositCheckedStrList.size(); i++) {
                            HashMap<Object,Object> map = new HashMap<>();
                            map.put("type","deposit");
                            map.put("name",mDepositCheckedStrList.get(i));
                            noScreenList.add(map);
                        }
                    }
                    if (mLeaseTermTypeCheckedStrList.size() > 0) {
                        for (int i = 0; i < mLeaseTermTypeCheckedStrList.size(); i++) {
                            HashMap<Object,Object> map = new HashMap<>();
                            map.put("type","lease_term_type_id");
                            map.put("name",mLeaseTermTypeCheckedStrList.get(i));
                            noScreenList.add(map);
                        }
                    }
                    if (mSizeCheckedStrList.size() > 0) {
                        for (int i = 0; i < mSizeCheckedStrList.size(); i++) {
                            HashMap<Object,Object> map = new HashMap<>();
                            map.put("type","size");
                            map.put("name",mSizeCheckedStrList.get(i));
                            noScreenList.add(map);
                        }
                    }
                    if (mCustomDimensionCheckedStrList.size() > 0) {
                        for (int i = 0; i < mCustomDimensionCheckedStrList.size(); i++) {
                            HashMap<Object,Object> map = new HashMap<>();
                            map.put("type","customDimension");
                            map.put("name",mCustomDimensionCheckedStrList.get(i));
                            noScreenList.add(map);
                        }
                    }
                    if (noScreenList.size() > 0) {
                        final ResourcesScreeningItemAdapter resourcesScreeningItemAdapter = new ResourcesScreeningItemAdapter(FieldInfoActivity.this,
                                FieldInfoActivity.this, noScreenList,2,0);
                        mFieldinfoNoScreenGV.setAdapter(resourcesScreeningItemAdapter);
                        resourcesScreeningItemAdapter.notifyDataSetChanged(true,
                                noScreenList);

                        mFieldinfoNoScreenGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (noScreenList.get(position).get("name").toString().equals(getResources().getString(R.string.fieldinfo_no_deposit_str))) {
                                    if (mFieldinfoDepositCB.isChecked()) {
                                        mFieldinfoDepositCB.setChecked(false);
                                    } else {
                                        mFieldinfoDepositCB.setChecked(true);
                                    }
                                } else if (noScreenList.get(position).get("name").toString().equals(getResources().getString(R.string.fieldinfo_have_deposit_str))) {
                                    if (mFieldinfoHaveDepositCB.isChecked()) {
                                        mFieldinfoHaveDepositCB.setChecked(false);
                                    } else {
                                        mFieldinfoHaveDepositCB.setChecked(true);
                                    }
                                } else if (mFieldinfoLeaseTermTypeCB.getText().toString().split(" ")[1].equals(noScreenList.get(position).get("name").toString())) {
                                    if (mFieldinfoLeaseTermTypeCB.isChecked()) {
                                        mFieldinfoLeaseTermTypeCB.setChecked(false);
                                    } else {
                                        mFieldinfoLeaseTermTypeCB.setChecked(true);
                                    }
                                } else if (mFieldinfoSizeCB.getText().toString().equals(noScreenList.get(position).get("name").toString())) {
                                    if (mFieldinfoSizeCB.isChecked()) {
                                        mFieldinfoSizeCB.setChecked(false);
                                    } else {
                                        mFieldinfoSizeCB.setChecked(true);
                                    }
                                }
                                ResourcesScreeningItemAdapter.getresourcescreeninglist().put(
                                        noScreenList.get(position).get("type").toString() +
                                                noScreenList.get(position).get("name").toString(),false);
                                if (!getScreenTrueStatus()) {
                                    setScreenStatus(0);
                                } else {
                                    setScreenStatus(1);
                                }
                                noScreenList.remove(position);
                                resourcesScreeningItemAdapter.notifyDataSetChanged();
                                if (mDepositCheckedStrList != null) {
                                    mDepositCheckedStrList.clear();
                                }
                                if (mLeaseTermTypeCheckedStrList != null) {
                                    mLeaseTermTypeCheckedStrList.clear();
                                }
                                if (mSizeCheckedStrList != null) {
                                    mSizeCheckedStrList.clear();
                                }
                                if (mCustomDimensionCheckedStrList != null) {
                                    mCustomDimensionCheckedStrList.clear();
                                }
                                for (int i = 0; i <noScreenList.size(); i++) {
                                    if (noScreenList.get(i).get("type").toString().equals("deposit")) {
                                        mDepositCheckedStrList.add(noScreenList.get(i).get("name").toString());
                                    } else if (noScreenList.get(i).get("type").toString().equals("lease_term_type_id")) {
                                        mLeaseTermTypeCheckedStrList.add(noScreenList.get(i).get("name").toString());
                                    } else if (noScreenList.get(i).get("type").toString().equals("size")) {
                                        mSizeCheckedStrList.add(noScreenList.get(i).get("name").toString());
                                    } else if (noScreenList.get(i).get("type").toString().equals("customDimension")) {
                                        mCustomDimensionCheckedStrList.add(noScreenList.get(i).get("name").toString());
                                    }
                                }
                                updataSizeLv(true);
                                setScreenStatus(null);
                            }
                        });
                        mFieldinfoNoScreenLL.setVisibility(View.VISIBLE);
                        mFieldinfoSizeNoTV.setVisibility(View.GONE);
                    } else {
                        mFieldinfoNoScreenLL.setVisibility(View.GONE);
                        mFieldinfoSizeNoTV.setVisibility(View.VISIBLE);
                    }
                }
                hideProgressDialog();
            }
        }
    };

    private void setScreenStatus(Integer status) {
        if (status == null) {
            if (mDepositCheckedStrList.size() > 0 ||
                    mLeaseTermTypeCheckedStrList.size() > 0 ||
                    mSizeCheckedStrList.size() > 0 ||
                    mCustomDimensionCheckedStrList.size() > 0 ||
                    mScreenMinPrice.length() > 0 ||
                    mScreenMaxPrice.length() > 0) {
                mFieldinfoScreenTV.setTextColor(getResources().getColor(R.color.default_bluebg));
                mFieldinfoPwScreenTV.setTextColor(getResources().getColor(R.color.default_bluebg));
                mFieldinfoScreenTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_fieldinfo_screen_checkbox_false_bg));
                mFieldinfoPwScreenTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_fieldinfo_screen_checkbox_false_bg));
                if (mFieldinfoScreenPW != null &&
                        mFieldinfoScreenPW.isShowing()) {
                    mFieldinfoScreenTV.setCompoundDrawables(null, null, mSortBlueUpDrawable, null);
                    mFieldinfoPwScreenTV.setCompoundDrawables(null, null, mSortBlueUpDrawable, null);
                } else {
                    mFieldinfoScreenTV.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                    mFieldinfoPwScreenTV.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                }
            } else {
                mFieldinfoScreenTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
                mFieldinfoPwScreenTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
                mFieldinfoScreenTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.module_activity_fieldinfo_screen_check_bg));
                mFieldinfoPwScreenTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.module_activity_fieldinfo_screen_check_bg));
                if (mFieldinfoScreenPW != null &&
                        mFieldinfoScreenPW.isShowing()) {
                    mFieldinfoScreenTV.setCompoundDrawables(null, null, mSortGreyUpDrawable, null);
                    mFieldinfoPwScreenTV.setCompoundDrawables(null, null, mSortGreyUpDrawable, null);
                } else {
                    mFieldinfoScreenTV.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                    mFieldinfoPwScreenTV.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                }
            }
        } else {
            if (status == 1) {
                mFieldinfoPwScreenTV.setTextColor(getResources().getColor(R.color.default_bluebg));
                mFieldinfoPwScreenTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_fieldinfo_screen_checkbox_false_bg));
                mFieldinfoPwScreenTV.setCompoundDrawables(null, null, mSortBlueUpDrawable, null);
            } else if (status == 0) {
                mFieldinfoPwDepositCB.setChecked(false);
                mFieldinfoPwHaveDepositCB.setChecked(false);
                mFieldinfoPwLeaseTermTypeCB.setChecked(false);
                mFieldinfoPwSizeCB.setChecked(false);
                mFieldinfoPwScreenTV.setCompoundDrawables(null, null, mSortGreyUpDrawable, null);
                mFieldinfoPwScreenTV.setTextColor(getResources().getColor(R.color.headline_tv_color));
                mFieldinfoPwScreenTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_search_field_area_tv_bg));
            }
        }
    }
    private boolean getScreenTrueStatus() {
        boolean result = false;
        if (screemDataList != null && screemDataList.size() > 0) {
            for (int j = 0;j < screemDataList.size(); j++ ) {
                if (screemDataList.get(j).get("datalist") != null &&
                        ((ArrayList<HashMap<Object, Object>>) screemDataList.get(j).get("datalist")).size() > 0) {
                    ArrayList<HashMap<Object, Object>> data_new_temp = new ArrayList<>();
                    data_new_temp.addAll(((ArrayList<HashMap<Object, Object>>) (screemDataList.get(j).get("datalist"))));
                    for (int i = 0; i < data_new_temp.size(); i++) {
                        if ((boolean)ResourcesScreeningItemAdapter.getresourcescreeninglist().get(
                                data_new_temp.get(i).get("type").toString() +
                                        data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())) {
                            return true;
                        }
                    }
                }
            }
        }
        return result;
    }
    @Override
    public void onFieldinfoFailure(boolean superresult, Throwable error) {
        //2018/1/18固ui显示
        mFieldinfoNoDataLL.setVisibility(View.VISIBLE);
        if (isNoResources(error,-204) || isNoResources(error,-4)) {
            mno_resources_layout.setVisibility(View.VISIBLE);
            maction_layout_top.setVisibility(View.GONE);
            if (resourceinfolist.getPhysical_resource().getIs_activity() == 0) {
                mno_resources_textview.setText(getResources().getString(R.string.fieldinfo_no_resources_one_text) +
                        getResources().getString(R.string.leftimg_text)+
                        getResources().getString(R.string.fieldinfo_no_resources_two_text));
            } else if (resourceinfolist.getPhysical_resource().getIs_activity() == 1) {
                mno_resources_textview.setText(getResources().getString(R.string.fieldinfo_no_resources_one_text) +
                        getResources().getString(R.string.calendar_activity_txt)+
                        getResources().getString(R.string.fieldinfo_no_resources_two_text));
            }
            mno_resources_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FieldInfoActivity.this.finish();
                    Intent maintabintent = new Intent(FieldInfoActivity.this, MainTabActivity.class);
                    maintabintent.putExtra("new_tmpintent", "goto_homepage");
                    startActivity(maintabintent);
                }
            });
        } else {
            mno_resources_layout.setVisibility(View.GONE);
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
        }
    }

    @Override
    public void onFieldinfoResSuccess(PhyResInfoModel resInfo) {
        resourceinfolist = resInfo;
        if (resourceinfolist != null) {
            //场地规格
            mLeaseTermTypeMap = new HashMap<>();
            mLeaseTermTypeStrList = new ArrayList<>();
            mSizeStrList = new ArrayList<>();
            mCustomDimensionStrList = new ArrayList<>();
            mDepositStrList = new ArrayList<>();
            if (resourceinfolist.getSize() != null && resourceinfolist.getSize().size() > 0) {
                mDimensionsDataList = resourceinfolist.getSize();
//                    mDimensionsDataScreenList = (ArrayList<FieldInfoSizeModel>) mDimensionsDataList.clone();
                try {
                    mDimensionsDataScreenList = (ArrayList<FieldInfoSizeModel>) com.linhuiba.linhuifield.connector.Constants.deepCopy(mDimensionsDataList);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                //2018/1/8 规格筛选
                if (mDimensionsDataList.size() > 5) {
                    mFieldinfoChooseSizeLL.setVisibility(View.VISIBLE);
                } else {
                    int size = 0;
                    for (int i = 0; i < mDimensionsDataList.size(); i++) {
                        if (mDimensionsDataList.get(i).getResource().size() > 1) {
                            for (int j = 0; j < mDimensionsDataList.get(i).getResource().size(); j++) {
                                size++;
                            }
                        } else {
                            size++;
                        }
                    }
                    if (size > 5) {
                        mFieldinfoChooseSizeLL.setVisibility(View.VISIBLE);
                    } else {
                        mFieldinfoChooseSizeLL.setVisibility(View.GONE);
                    }
                }
                for (int i = 0; i < mDimensionsDataList.size(); i++) {
                    if (mDimensionsDataList.get(i).getDimension().getLease_term_type() != null &&
                            mDimensionsDataList.get(i).getDimension().getLease_term_type().length() > 0 &&
                            !mLeaseTermTypeStrList.contains(mDimensionsDataList.get(i).getDimension().getLease_term_type())) {
                        if (mDimensionsDataList.get(i).getDimension().getLease_term_type().equals(
                                getResources().getString(R.string.term_types_unit_txt))) {
                            mLeaseTermTypeMap.put(mDimensionsDataList.get(i).getDimension().getLease_term_type(),
                                    -1);
                        } else {
                            if (mDimensionsDataList.get(i).getResource().get(0).getSelling_resource_price().get(0) != null &&
                                    mDimensionsDataList.get(i).getResource().get(0).getSelling_resource_price().get(0).getLease_term_type_id() != null) {
                                mLeaseTermTypeMap.put(mDimensionsDataList.get(i).getDimension().getLease_term_type(),
                                        mDimensionsDataList.get(i).getResource().get(0).getSelling_resource_price().get(0).getLease_term_type_id());
                            }
                        }
                        mLeaseTermTypeStrList.add(mDimensionsDataList.get(i).getDimension().getLease_term_type());
                        if (i == 0) {
                            mFieldinfoLeaseTermTypeCB.setText(getResources().getString(R.string.fieldinfo_screen_lease_term_type_str_first) + " " +
                                    mDimensionsDataList.get(i).getDimension().getLease_term_type() + " " +
                                    getResources().getString(R.string.fieldinfo_screen_lease_term_type_str_second));
                            mFieldinfoPwLeaseTermTypeCB.setText(getResources().getString(R.string.fieldinfo_screen_lease_term_type_str_first) + " " +
                                    mDimensionsDataList.get(i).getDimension().getLease_term_type() + " " +
                                    getResources().getString(R.string.fieldinfo_screen_lease_term_type_str_second));
                        }
                    }
                    if (mDimensionsDataList.get(i).getDimension().getSize() != null &&
                            mDimensionsDataList.get(i).getDimension().getSize().length() > 0 &&
                            !mSizeStrList.contains(mDimensionsDataList.get(i).getDimension().getSize())) {
                        mSizeStrList.add(mDimensionsDataList.get(i).getDimension().getSize());
                        if (i == 0) {
                            mFieldinfoSizeCB.setText(mDimensionsDataList.get(i).getDimension().getSize());
                            mFieldinfoPwSizeCB.setText(mDimensionsDataList.get(i).getDimension().getSize());
                        }
                    }
                    if (mDimensionsDataList.get(i).getDimension().getCustom_dimension() != null &&
                            mDimensionsDataList.get(i).getDimension().getCustom_dimension().length() > 0 &&
                            !mCustomDimensionStrList.contains(mDimensionsDataList.get(i).getDimension().getCustom_dimension())) {
                        mCustomDimensionStrList.add(mDimensionsDataList.get(i).getDimension().getCustom_dimension());
                    }
                }
                mDepositStrList.add(getResources().getString(R.string.fieldinfo_no_deposit_str));
                mDepositStrList.add(getResources().getString(R.string.fieldinfo_have_deposit_str));
//                    showResourceScreeningPw();

                mfieldinfo_pricelist_layout.setVisibility(View.VISIBLE);
                List<List<FieldInfoSellResourceModel>> datas = new ArrayList<>();
                for (int i = 0; i < mDimensionsDataList.size(); i++) {
                    datas.add(mDimensionsDataList.get(i).getResource());
                }
                adapter = new FieldInfoPriceSizeAdapter(
                        FieldInfoActivity.this, FieldInfoActivity.this, mDimensionsDataList, datas);
                mFieldInfoSizeLV.setAdapter(adapter);
                mFieldInfoSizeLV.setGroupIndicator(null);
                for (int i = 0; i < mDimensionsDataList.size(); i++) {
                    mFieldInfoSizeLV.expandGroup(i);
                }
                mFieldInfoReserveTV.setVisibility(View.GONE);
            } else {
                mFieldinfoChooseSizeLL.setVisibility(View.GONE);
                mFieldInfoReserveTV.setVisibility(View.GONE);
                mFieldinfoFocusonLL.setVisibility(View.GONE);
            }
            if (isRefreshSize) {
                isRefreshSize = false;
                return;
            }
            getfieldid = String.valueOf(resourceinfolist.getPhysical_resource().getId());
            //展位下的所有供给（活动）
            if (getfieldid != null && getfieldid.length() > 0 && isSellResource) {
                if (mCommunityId > 0) {
                    mFieldinfoMvpPresenter.getOtherPhyRes(mCommunityId, 1, 100, Integer.parseInt(getfieldid), mApiResourcesModel);
                }
                mFieldinfoMvpPresenter.getSellRes(getfieldid, "3");
            }
            if (resourceinfolist.getPhysical_resource().getCommunity() != null) {
                if (resourceinfolist.getPhysical_resource().getCommunity().getLng() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getLat() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            initGetLocation();
                        }
                    }).start();
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getCity() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getCity().getId() > 0) {
                    mCityId = resourceinfolist.getPhysical_resource().getCommunity().getCity().getId();
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getCategory() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getCategory().getId() > 0) {
                    mCategoryId = resourceinfolist.getPhysical_resource().getCommunity().getCategory().getId();
                }
                mFieldinfoRecommendPageInt = 1;
                mFieldinfoMvpPresenter.getRecommendedRes(mCommunityId, 10, mFieldinfoRecommendPageInt,mCityId,mCategoryId);
                if (mCommunityId == 0 &&
                        resourceinfolist.getPhysical_resource().getCommunity().getId() > 0) {
                    mCommunityId = resourceinfolist.getPhysical_resource().getCommunity().getId();
                    //推荐场地
                    if (getfieldid != null && getfieldid.length() > 0) {
                        mFieldinfoMvpPresenter.getOtherPhyRes(mCommunityId, 1, 100, Integer.parseInt(getfieldid), mApiResourcesModel);
                    }
                    if (LoginManager.isLogin()) {
                        //未领取
                        mCouponsMvpPresenter.getCommunityCoupons(mCommunityId, 1, 100, 1);
                        ///已领取
                        mCouponsMvpPresenter.getCommunityCoupons(mCommunityId, 2, 1, 1);
                    }
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getCategory_full_name() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getCategory_full_name().length() > 0) {
                    mCommunityInfoTypeTV.setText(resourceinfolist.getPhysical_resource().getCommunity().getCategory_full_name());
                } else {
                    mCommunityInfoTypeTV.setText(getResources().getString(R.string.order_nodata_toast));
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getBuilding_area() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getBuilding_area().length() > 0) {
                    mCommunityInfoAreaTV.setText(resourceinfolist.getPhysical_resource().getCommunity().getBuilding_area() +
                            getResources().getString(R.string.module_community_info_area_unit));
                } else {
                    mCommunityInfoAreaTV.setText(getResources().getString(R.string.order_nodata_toast));
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getTradingarea() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getTradingarea().getName() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getTradingarea().getName().length() > 0) {
                    mCommunityInfoTradingareaTV.setText(resourceinfolist.getPhysical_resource().getCommunity().getTradingarea().getName());
                    mCommunityInfoTradingareaLL.setVisibility(View.VISIBLE);
                } else {
                    mCommunityInfoTradingareaLL.setVisibility(View.GONE);
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getBuild_year() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getBuild_year().length() > 0) {
                    mCommunityInfoYearTV.setText(resourceinfolist.getPhysical_resource().getCommunity().getBuild_year());
                    mCommunityInfoYearLL.setVisibility(View.VISIBLE);
                } else {
                    mCommunityInfoYearLL.setVisibility(View.GONE);
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getDescription() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getDescription().length() > 0) {
                    mCommunityInfoDespLL.setVisibility(View.VISIBLE);
                    mCommunityInfoDespTV.setText(resourceinfolist.getPhysical_resource().getCommunity().getDescription());
                } else {
                    mCommunityInfoDespLL.setVisibility(View.GONE);
                }

                if (resourceinfolist.getPhysical_resource().getCommunity().getAttributes() != null && resourceinfolist.getPhysical_resource().getCommunity().getAttributes().size() > 0) {
                    for (int j = 0; j < resourceinfolist.getPhysical_resource().getCommunity().getAttributes().size(); j++) {
                        if (resourceinfolist.getPhysical_resource().getCommunity().getAttributes().get(j).getName() != null &&
                                resourceinfolist.getPhysical_resource().getCommunity().getAttributes().get(j).getName().length() > 0) {
                            if (resourceinfolist.getPhysical_resource().getCommunity().getAttributes().get(j).getOptions() != null &&
                                    resourceinfolist.getPhysical_resource().getCommunity().getAttributes().get(j).getOptions().size() > 0) {
                                if (resourceinfolist.getPhysical_resource().getCommunity().getAttributes().get(j).getType() == 1 ||
                                        resourceinfolist.getPhysical_resource().getCommunity().getAttributes().get(j).getType() == 2) {
                                    String tvStr = "";
                                    ArrayList<FieldAddfieldAttributesModel> options = resourceinfolist.getPhysical_resource().getCommunity().getAttributes().get(j).getOptions();
                                    if (resourceinfolist.getPhysical_resource().getCommunity().getAttributes().get(j).getType() == 1) {
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
                                    final ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this, 10);
                                    mCommunityInfoAttributesLL.addView(moduleViewAddfieldCommunityInfo);
                                    moduleViewAddfieldCommunityInfo.mTextView.setText(resourceinfolist.getPhysical_resource().getCommunity().getAttributes().get(j).getName() + "：");
                                    moduleViewAddfieldCommunityInfo.mBackTextView.setText(tvStr);
                                } else {
                                    ArrayList<FieldAddfieldAttributesModel> options = resourceinfolist.getPhysical_resource().getCommunity().getAttributes().get(j).getOptions();
                                    if (options.get(0).getMark() != null &&
                                            options.get(0).getMark().length() > 0) {
                                        final ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this, 10);
                                        mCommunityInfoAttributesLL.addView(moduleViewAddfieldCommunityInfo);
                                        moduleViewAddfieldCommunityInfo.mTextView.setText(resourceinfolist.getPhysical_resource().getCommunity().getAttributes().get(j).getName() + "：");
                                        moduleViewAddfieldCommunityInfo.mBackTextView.setText(options.get(0).getMark());
                                    }
                                }
                            }
                        }
                    }
                }
                //男女比例等数据
                if (resourceinfolist.getPhysical_resource().getCommunity().getMale_proportion() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getMale_proportion().length() > 0) {
                    ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this, 10);
                    mCommunityOtherAttributeInfoLL.addView(moduleViewAddfieldCommunityInfo);
                    moduleViewAddfieldCommunityInfo.mTextView.setText(getResources().
                            getString(R.string.txt_editgender_ratiotxt));
                    moduleViewAddfieldCommunityInfo.mBackTextView.setText(getResources().
                            getString(R.string.fieldinfo_man_proportion_text) +
                            resourceinfolist.getPhysical_resource().getCommunity().getMale_proportion() +
                            getResources().
                                    getString(R.string.fieldinfo_man_proportion_unit_text) + "、" +
                            getResources().
                                    getString(R.string.fieldinfo_woman_proportion_text) +
                            String.valueOf(100 - Integer.parseInt(resourceinfolist.getPhysical_resource().getCommunity().getMale_proportion())) +
                            getResources().
                                    getString(R.string.fieldinfo_man_proportion_unit_text));
                }
                if ((resourceinfolist.getPhysical_resource().getCommunity().getUnmarried_proportion() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getUnmarried_proportion().length() > 0) ||
                        (resourceinfolist.getPhysical_resource().getCommunity().getMarried_unpregnant_proportion() != null &&
                                resourceinfolist.getPhysical_resource().getCommunity().getMarried_unpregnant_proportion().length() > 0) ||
                        (resourceinfolist.getPhysical_resource().getCommunity().getMarried_pregnant_proportion() != null &&
                                resourceinfolist.getPhysical_resource().getCommunity().getMarried_pregnant_proportion().length() > 0)) {
                    String str = "";
                    if ((resourceinfolist.getPhysical_resource().getCommunity().getUnmarried_proportion() != null &&
                            resourceinfolist.getPhysical_resource().getCommunity().getUnmarried_proportion().length() > 0)) {
                        str = str + getResources().
                                getString(R.string.module_community_info_unmarried) +
                                resourceinfolist.getPhysical_resource().getCommunity().getUnmarried_proportion() +
                                getResources().
                                        getString(R.string.fieldinfo_man_proportion_unit_text);
                    }
                    if (resourceinfolist.getPhysical_resource().getCommunity().getMarried_unpregnant_proportion() != null &&
                            resourceinfolist.getPhysical_resource().getCommunity().getMarried_unpregnant_proportion().length() > 0) {
                        if (str.length() > 0) {
                            str = str + "、";
                        }
                        str = str + getResources().
                                getString(R.string.module_community_info_married_unpregnant) +
                                resourceinfolist.getPhysical_resource().getCommunity().getMarried_unpregnant_proportion() +
                                getResources().
                                        getString(R.string.fieldinfo_man_proportion_unit_text);
                    }
                    if (resourceinfolist.getPhysical_resource().getCommunity().getMarried_pregnant_proportion() != null &&
                            resourceinfolist.getPhysical_resource().getCommunity().getMarried_pregnant_proportion().length() > 0) {
                        if (str.length() > 0) {
                            str = str + "、";
                        }
                        str = str + getResources().
                                getString(R.string.module_community_info_married_pregnant) +
                                resourceinfolist.getPhysical_resource().getCommunity().getMarried_pregnant_proportion() +
                                getResources().
                                        getString(R.string.fieldinfo_man_proportion_unit_text);
                    }
                    if (str.length() > 0) {
                        ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this, 10);
                        mCommunityOtherAttributeInfoLL.addView(moduleViewAddfieldCommunityInfo);
                        moduleViewAddfieldCommunityInfo.mTextView.setText(getResources().
                                getString(R.string.module_community_info_marital_status));
                        moduleViewAddfieldCommunityInfo.mBackTextView.setText(str);
                    }

                }
                if ((resourceinfolist.getPhysical_resource().getCommunity().getUndergraduate_proportion() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getUndergraduate_proportion().length() > 0) ||
                        (resourceinfolist.getPhysical_resource().getCommunity().getGraduate_proportion() != null &&
                                resourceinfolist.getPhysical_resource().getCommunity().getGraduate_proportion().length() > 0) ||
                        (resourceinfolist.getPhysical_resource().getCommunity().getSpecialty_proportion() != null &&
                                resourceinfolist.getPhysical_resource().getCommunity().getSpecialty_proportion().length() > 0)) {
                    String str = "";
                    if ((resourceinfolist.getPhysical_resource().getCommunity().getUndergraduate_proportion() != null &&
                            resourceinfolist.getPhysical_resource().getCommunity().getUndergraduate_proportion().length() > 0)) {
                        str = str + getResources().
                                getString(R.string.module_community_info_undergraduate) +
                                resourceinfolist.getPhysical_resource().getCommunity().getUndergraduate_proportion() +
                                getResources().
                                        getString(R.string.fieldinfo_man_proportion_unit_text);
                    }
                    if (resourceinfolist.getPhysical_resource().getCommunity().getGraduate_proportion() != null &&
                            resourceinfolist.getPhysical_resource().getCommunity().getGraduate_proportion().length() > 0) {
                        if (str.length() > 0) {
                            str = str + "、";
                        }
                        str = str + getResources().
                                getString(R.string.module_community_info_graduate_unpregnant) +
                                resourceinfolist.getPhysical_resource().getCommunity().getGraduate_proportion() +
                                getResources().
                                        getString(R.string.fieldinfo_man_proportion_unit_text);
                    }
                    if (resourceinfolist.getPhysical_resource().getCommunity().getSpecialty_proportion() != null &&
                            resourceinfolist.getPhysical_resource().getCommunity().getSpecialty_proportion().length() > 0) {
                        if (str.length() > 0) {
                            str = str + "、";
                        }
                        str = str + getResources().
                                getString(R.string.module_community_info_specialty_pregnant) +
                                resourceinfolist.getPhysical_resource().getCommunity().getSpecialty_proportion() +
                                getResources().
                                        getString(R.string.fieldinfo_man_proportion_unit_text);
                    }
                    if (str.length() > 0) {
                        ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this, 10);
                        mCommunityOtherAttributeInfoLL.addView(moduleViewAddfieldCommunityInfo);
                        moduleViewAddfieldCommunityInfo.mTextView.setText(getResources().
                                getString(R.string.module_community_info_degree_level));
                        moduleViewAddfieldCommunityInfo.mBackTextView.setText(str);
                    }
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getCareer_background() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getCareer_background().size() > 0) {
                    String str = "";
                    for (int i = 0; i < resourceinfolist.getPhysical_resource().getCommunity().getCareer_background().size(); i++) {
                        if (resourceinfolist.getPhysical_resource().getCommunity().getCareer_background().get(i).getDisplay_name() != null &&
                                resourceinfolist.getPhysical_resource().getCommunity().getCareer_background().get(i).getDisplay_name().length() > 0 &&
                                resourceinfolist.getPhysical_resource().getCommunity().getCareer_background().get(i).getPivot() != null &&
                                resourceinfolist.getPhysical_resource().getCommunity().getCareer_background().get(i).getPivot().get("proportion") != null &&
                                resourceinfolist.getPhysical_resource().getCommunity().getCareer_background().get(i).getPivot().get("proportion").toString().length() > 0) {
                            if (str.length() > 0) {
                                str = str + "、";
                            }
                            str = str + resourceinfolist.getPhysical_resource().getCommunity().getCareer_background().get(i).getDisplay_name() +
                                    resourceinfolist.getPhysical_resource().getCommunity().getCareer_background().get(i).getPivot().get("proportion") +
                                    getResources().
                                            getString(R.string.fieldinfo_man_proportion_unit_text);
                        }
                    }
                    if (str.length() > 0) {
                        ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this, 10);
                        mCommunityOtherAttributeInfoLL.addView(moduleViewAddfieldCommunityInfo);
                        moduleViewAddfieldCommunityInfo.mTextView.setText(getResources().
                                getString(R.string.module_community_info_career_background));
                        moduleViewAddfieldCommunityInfo.mBackTextView.setText(str);
                    }
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getAge_group() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getAge_group().size() > 0) {
                    String str = "";
                    for (int i = 0; i < resourceinfolist.getPhysical_resource().getCommunity().getAge_group().size(); i++) {
                        if (resourceinfolist.getPhysical_resource().getCommunity().getAge_group().get(i).getDisplay_name() != null &&
                                resourceinfolist.getPhysical_resource().getCommunity().getAge_group().get(i).getDisplay_name().length() > 0 &&
                                resourceinfolist.getPhysical_resource().getCommunity().getAge_group().get(i).getPivot() != null &&
                                resourceinfolist.getPhysical_resource().getCommunity().getAge_group().get(i).getPivot().get("proportion") != null &&
                                resourceinfolist.getPhysical_resource().getCommunity().getAge_group().get(i).getPivot().get("proportion").toString().length() > 0) {
                            if (str.length() > 0) {
                                str = str + "、";
                            }
                            str = str + resourceinfolist.getPhysical_resource().getCommunity().getAge_group().get(i).getDisplay_name() +
                                    resourceinfolist.getPhysical_resource().getCommunity().getAge_group().get(i).getPivot().get("proportion") +
                                    getResources().
                                            getString(R.string.fieldinfo_man_proportion_unit_text);
                        }
                    }
                    if (str.length() > 0) {
                        ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this, 10);
                        mCommunityOtherAttributeInfoLL.addView(moduleViewAddfieldCommunityInfo);
                        moduleViewAddfieldCommunityInfo.mTextView.setText(getResources().
                                getString(R.string.module_community_info_age_group));
                        moduleViewAddfieldCommunityInfo.mBackTextView.setText(str);
                    }
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getPrivate_car_proportion() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getPrivate_car_proportion().length() > 0) {
                    ModuleViewAddfieldCommunityInfo moduleViewAddfieldCommunityInfo = new ModuleViewAddfieldCommunityInfo(this, 10);
                    mCommunityOtherAttributeInfoLL.addView(moduleViewAddfieldCommunityInfo);
                    moduleViewAddfieldCommunityInfo.mTextView.setText(getResources().
                            getString(R.string.module_community_info_private_car));
                    moduleViewAddfieldCommunityInfo.mBackTextView.setText(getResources().
                            getString(R.string.module_community_info_has_car) +
                            resourceinfolist.getPhysical_resource().getCommunity().getPrivate_car_proportion() +
                            getResources().
                                    getString(R.string.fieldinfo_man_proportion_unit_text) + "、" +
                            getResources().
                                    getString(R.string.module_community_info_no_car) +
                            String.valueOf(100 - Integer.parseInt(resourceinfolist.getPhysical_resource().getCommunity().getPrivate_car_proportion())) +
                            getResources().
                                    getString(R.string.fieldinfo_man_proportion_unit_text));
                }
                mCommunityLL.setVisibility(View.VISIBLE);
                if (resourceinfolist.getPhysical_resource().getCommunity().getName() != null) {
                    mCommunityNameTV.setText(resourceinfolist.getPhysical_resource().getCommunity().getName());
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getFloor_price() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getFloor_price().length() > 0) {
                    if (resourceinfolist.getPhysical_resource().getCommunity().getMax_price() != null &&
                            resourceinfolist.getPhysical_resource().getCommunity().getMax_price().length() > 0 &&
                            Double.parseDouble(resourceinfolist.getPhysical_resource().getCommunity().getMax_price()) >
                            Double.parseDouble(resourceinfolist.getPhysical_resource().getCommunity().getFloor_price())) {
                        mCommunityPriceTV.setText(getResources().getString(R.string.order_field_listitem_price_unit_text) +
                                Constants.getpricestring(resourceinfolist.getPhysical_resource().getCommunity().getFloor_price(),0.01) +
                        " - " + getResources().getString(R.string.order_field_listitem_price_unit_text) +
                                Constants.getpricestring(resourceinfolist.getPhysical_resource().getCommunity().getMax_price(),0.01));
                    } else {
                        mCommunityPriceTV.setText(getResources().getString(R.string.order_field_listitem_price_unit_text) +
                                Constants.getpricestring(resourceinfolist.getPhysical_resource().getCommunity().getFloor_price(),0.01));
                    }
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getTradingarea() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getTradingarea().getName() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getTradingarea().getName().length() > 0) {
                    mCommunityBusinessTV.setText(resourceinfolist.getPhysical_resource().getCommunity().getTradingarea().getName());
                    mCommunityBusinessLL.setVisibility(View.VISIBLE);
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getBuild_year() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getBuild_year().length() > 0) {
                    mCommunityBuildYearTV.setText(resourceinfolist.getPhysical_resource().getCommunity().getBuild_year() +
                            getResources().getString(R.string.module_fieldinfo_community_build_year_unit));
                    mCommunityBuildYearLL.setVisibility(View.VISIBLE);
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getTotal_number_of_people() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getTotal_number_of_people().length() > 0) {
                    mCommunityPeopleTV.setText(resourceinfolist.getPhysical_resource().getCommunity().getTotal_number_of_people());
                    mCommunityPeopleLL.setVisibility(View.VISIBLE);
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getCommunity_img() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getCommunity_img().getPic_url() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getCommunity_img().getPic_url().length() > 0) {
                    Picasso.with(FieldInfoActivity.this).load(resourceinfolist.getPhysical_resource().getCommunity().getCommunity_img().getPic_url() +
                            com.linhuiba.linhuipublic.config.Config.Linhui_Min_Watermark).resize(
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldInfoActivity.this,100),
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldInfoActivity.this,80)).into(mCommunityImgv);
                }
            } else {
                mCommunityLL.setVisibility(View.GONE);
            }

            if (LoginManager.getInstance().getFieldinfoGuidance() != 1) {
                mFieldinfoNoDataLL.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_xiangqingyeyindao));
                mFieldinfoShowMoreRL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginManager.getInstance().setFieldinfoGuidance(1);
                        mFieldinfoNoDataLL.setVisibility(View.GONE);
                    }
                });
            } else {
                mFieldinfoNoDataLL.setVisibility(View.GONE);
            }
            mscrollview.smoothScrollTo(0, 0);
            mFavoriteStatus = resourceinfolist.getPhysical_resource().getFavorite_status();
            if (mFavoriteStatus == 1 && LoginManager.isLogin()) {
                mFieldInfoFousonImgv.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_collection_red));
            } else {
                mFieldInfoFousonImgv.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_ic_collection));
            }
            //询价列表显示
            if (resourceinfolist.getEnquiry_resource() != null &&
                    resourceinfolist.getEnquiry_resource().size() > 0) {
                FieldInfoEnquiryAdapter fieldInfoEnquiryAdapter = new FieldInfoEnquiryAdapter(FieldInfoActivity.this,
                        FieldInfoActivity.this, resourceinfolist.getEnquiry_resource());
                mFieldinfoEnquiryLV.setAdapter(fieldInfoEnquiryAdapter);
                mFieldInfoReserveTV.setVisibility(View.GONE);
            }
            //2017/10/24 天气字段
            if (resourceinfolist.getWeather() != null) {
                if (resourceinfolist.getWeather().size() > 0) {
                    weatherliststr = resourceinfolist.getWeather();
                }
            }

            if (mPicList != null) {
                mPicList.clear();
            }
            if (mCasePicList != null) {
                mCasePicList.clear();
            }
            if (resourceinfolist.getPhysical_resource().getActivity_cases() != null &&
                    resourceinfolist.getPhysical_resource().getActivity_cases().size() > 0) {
                for (int i = 0; i < resourceinfolist.getPhysical_resource().getActivity_cases().size(); i++) {
                    if (resourceinfolist.getPhysical_resource().getActivity_cases().get(i).getActivity_case_url() != null &&
                            resourceinfolist.getPhysical_resource().getActivity_cases().get(i).getActivity_case_url().size() > 0) {
                        for (int j = 0; j < resourceinfolist.getPhysical_resource().getActivity_cases().get(i).getActivity_case_url().size(); j++) {
                            if (resourceinfolist.getPhysical_resource().getActivity_cases().get(i).getActivity_case_url().get(j).getActivity_case_url() != null &&
                                    resourceinfolist.getPhysical_resource().getActivity_cases().get(i).getActivity_case_url().get(j).getActivity_case_url().length() > 0) {
                                mCasePicList.add(resourceinfolist.getPhysical_resource().getActivity_cases().get(i).getActivity_case_url().get(j).getActivity_case_url());
                            }
                        }
                    }
                }
            }
            if (resourceinfolist.getPhysical_resource().getPhysical_resource_imgs() != null) {
                if (resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().size() > 0) {
                    // FIXME: 2018/12/10 分享修改界面
                    if (resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().get(0).get("pic_url") != null &&
                            resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().get(0).get("pic_url").length() > 0) {
                        ShareIconStr = resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().get(0).get("pic_url").toString() + com.linhuiba.linhuipublic.config.Config.Linhui_Mid_Watermark;
                        Picasso.with(FieldInfoActivity.this).load(ShareIconStr).placeholder(R.drawable.ic_jiazai_big).error(R.drawable.ic_no_pic_big).resize(370, 250).into(mShareImageView);
                    }
                    for (int i = 0; i < resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().size(); i++) {
                        if (resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().get(i).get("pic_url") != null &&
                                resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().get(i).get("pic_url").length() > 0) {
                            mPicList.add(resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().get(i).get("pic_url").toString() + com.linhuiba.linhuipublic.config.Config.Linhui_Max_Watermark);
                        }
                    }
                    if (mCasePicList != null && mCasePicList.size() > 0) {
                        mPicList.addAll(mCasePicList);
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
                        //显示数字指示器
                        mFieldInfoBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
                        //设置指示器位置（当banner模式中有指示器时）
                        mFieldInfoBanner.setIndicatorGravity(BannerConfig.RIGHT);
                        //banner设置方法全部调用完毕时最后调用
                        mFieldInfoBanner.setOnBannerClickListener(new OnBannerClickListener() {
                            @Override
                            public void OnBannerClick(int position) {
                                if (zoom_picture_dialog != null && zoom_picture_dialog.isShowing()) {
                                    zoom_picture_dialog.dismiss();
                                }
                                showZoomPicDialog((position - 1) % mPicList.size());
                            }
                        });
                        mFieldInfoBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                if (position % mPicList.size() == 0) {
                                    mFieldinfoBannerNumTV.setText(String.valueOf(mPicList.size()));
                                } else {
                                    mFieldinfoBannerNumTV.setText(String.valueOf(position % mPicList.size()));
                                }
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                        mFieldinfoBannerSizeTV.setText("/" + String.valueOf(mPicList.size()));
                        mFieldInfoBanner.start();
                    }
                }

                if (resourceinfolist.getPhysical_resource().getIs_activity() == 0) {
                    if (resourceinfolist.getPhysical_resource().getField_type() != null &&
                            resourceinfolist.getPhysical_resource().getField_type().getId() > 0 &&
                            resourceinfolist.getPhysical_resource().getField_type().getDisplay_name() != null &&
                            resourceinfolist.getPhysical_resource().getField_type().getDisplay_name().length() > 0) {
                        mtxt_fieldtitle.setText(resourceinfolist.getPhysical_resource().getField_type().getDisplay_name());
                        SharedescriptionStr = resourceinfolist.getPhysical_resource().getField_type().getDisplay_name() + "  ";
                    } else {
                        mtxt_fieldtitle.setVisibility(View.GONE);
                    }
                    mactivityinfo_layout.setVisibility(View.GONE);
                } else if (resourceinfolist.getPhysical_resource().getIs_activity() == 1) {
                    isActivityRes = true;
                    TitleBarUtils.setTitleText(this, getResources().getString(R.string.fieldinfo_activitys_title_text));
                    if (resourceinfolist.getSize().size() > 0 &&
                            resourceinfolist.getSize().get(0).getResource().size() > 0 &&
                            resourceinfolist.getSize().get(0).getResource().get(0).getActivity_type() != null &&
                            resourceinfolist.getSize().get(0).getResource().get(0).getActivity_type().getId() > 0 &&
                            resourceinfolist.getSize().get(0).getResource().get(0).getActivity_type().getDisplay_name() != null &&
                            resourceinfolist.getSize().get(0).getResource().get(0).getActivity_type().getDisplay_name().length() > 0) {
                        mtxt_fieldtitle.setText(resourceinfolist.getSize().get(0).getResource().get(0).getActivity_type().getDisplay_name());
                        SharedescriptionStr = resourceinfolist.getSize().get(0).getResource().get(0).getActivity_type().getDisplay_name() + "  ";
                    } else {
                        mtxt_fieldtitle.setVisibility(View.GONE);
                    }

                    if (resourceinfolist.getSize().size() > 0 &&
                            resourceinfolist.getSize().get(0).getResource().size() > 0 &&
                            resourceinfolist.getSize().get(0).getResource().get(0).getDescription() != null &&
                            resourceinfolist.getSize().get(0).getResource().get(0).getDescription().length() > 0) {
                        mActivityDescription = resourceinfolist.getSize().get(0).getResource().get(0).getDescription();
                        mFieldinfoActivityDescriptionLL.setVisibility(View.VISIBLE);
                        mFieldInfoActivityDescriptionTV.setText(mActivityDescription);
                    }
                    mactivityinfo_layout.setVisibility(View.VISIBLE);

                    if (resourceinfolist.getSize() != null && resourceinfolist.getSize().size() > 0 &&
                            resourceinfolist.getSize().get(0).getResource() != null &&
                            resourceinfolist.getSize().get(0).getResource().size() > 0 &&
                            resourceinfolist.getSize().get(0).getResource().get(0).getActivity_start_date() != null &&
                            resourceinfolist.getSize().get(0).getResource().get(0).getActivity_start_date().length() > 0 &&
                            resourceinfolist.getSize().get(0).getResource().get(0).getDeadline() != null &&
                            resourceinfolist.getSize().get(0).getResource().get(0).getDeadline().length() > 0) {
                        mactivityinfo_start_date_text.setText(resourceinfolist.getSize().get(0).getResource().get(0).getActivity_start_date().replaceAll("-", "."));
                        mactivityinfo_end_date_text.setText(resourceinfolist.getSize().get(0).getResource().get(0).getDeadline().replaceAll("-", "."));
                        mShareActivityTimeTV.setText(mactivityinfo_start_date_text.getText().toString() + "-" +
                                mactivityinfo_end_date_text.getText().toString());
                        mActivityStartDate = resourceinfolist.getSize().get(0).getResource().get(0).getActivity_start_date();
                        mDeadline = resourceinfolist.getSize().get(0).getResource().get(0).getDeadline();
                    }
                    if (resourceinfolist.getSize().size() > 0 &&
                            resourceinfolist.getSize().get(0).getResource().size() > 0 &&
                            resourceinfolist.getSize().get(0).getResource().get(0).isExpired()) {
                        mactivityinfo_start_date_text.setText(getResources().getString(R.string.fieldinfo_activity_expired_text));
                        mactivityinfo_end_date_text.setVisibility(View.GONE);
                        mactivityinfo_date_text.setVisibility(View.GONE);
                        mactivity_overdue_btn.setVisibility(View.GONE);
                    } else {
                        mactivity_overdue_btn.setVisibility(View.GONE);
                    }
                }
                if (resourceinfolist.getPhysical_resource().getIs_activity() == 1 &&
                        resourceinfolist.getSize().size() > 0 &&
                        resourceinfolist.getSize().get(0).getResource().size() > 0 &&
                        resourceinfolist.getSize().get(0).getResource().get(0).getCustom_name() != null &&
                        resourceinfolist.getSize().get(0).getResource().get(0).getCustom_name().length() > 0) {
                    ShareTitleStr = resourceinfolist.getSize().get(0).getResource().get(0).getCustom_name();
                } else {
                    if (resourceinfolist.getPhysical_resource().getCommunity().getName() != null) {
                        if (resourceinfolist.getPhysical_resource().getName() != null) {
                            ShareTitleStr = resourceinfolist.getPhysical_resource().getCommunity().getName() +
                                    resourceinfolist.getPhysical_resource().getName().toString();
                        } else {
                            ShareTitleStr = resourceinfolist.getPhysical_resource().getCommunity().getName();
                        }
                    } else {
                        if (resourceinfolist.getPhysical_resource().getName() != null) {
                            ShareTitleStr = resourceinfolist.getPhysical_resource().getName().toString();
                        }
                    }
                }
                mtxt_field_description.setText(Constants.getDifferentColorStr(".........." +
                        ShareTitleStr, 0, 10, getResources().getColor(R.color.color_null)));
                String shareUrl = "";
                if (mApiResourcesModel != null) {
                    String min_price = "";
                    String max_price = "";
                    String min_area = "";
                    String max_area = "";
                    String min_person_flow = "";
                    String max_person_flow = "";
                    if (mApiResourcesModel.getMin_price() != null && mApiResourcesModel.getMin_price().length() > 0) {
                        min_price = ("&min_price=" + Constants.getpricestring(mApiResourcesModel.getMin_price(), 0.01));
                    }
                    if (mApiResourcesModel.getMax_price() != null && mApiResourcesModel.getMax_price().length() > 0) {
                        max_price = ("&max_price=" + Constants.getpricestring(mApiResourcesModel.getMax_price(), 0.01));
                    }
                    if (mApiResourcesModel.getMin_area() != null && mApiResourcesModel.getMin_area().length() > 0) {
                        min_area = ("&min_area=" + mApiResourcesModel.getMin_area());
                    }
                    if (mApiResourcesModel.getMax_area() != null && mApiResourcesModel.getMax_area().length() > 0) {
                        max_area = ("&max_area=" + mApiResourcesModel.getMax_area());
                    }
                    if (mApiResourcesModel.getMin_person_flow() != null && mApiResourcesModel.getMin_person_flow().length() > 0) {
                        min_person_flow = ("&min_person_flow=" + mApiResourcesModel.getMin_person_flow());
                    }
                    if (mApiResourcesModel.getMax_person_flow() != null && mApiResourcesModel.getMax_person_flow().length() > 0) {
                        max_person_flow = ("&max_person_flow=" + mApiResourcesModel.getMax_person_flow());
                    }
                    shareUrl = min_price + max_price + min_area + max_area + min_person_flow + max_person_flow;
                }
                //2018/11/14 分享供给详情
                if (resourceinfolist.getPhysical_resource().getIs_activity() == 0) {
                    sharewxMiniShareLinkUrl = Config.WX_MINI_SHARE_FIELDS_INFO_URL + getfieldid + shareUrl;
                    share_linkurl = Config.SHARE_FIELDS_INFO_URL + getfieldid + "?BackKey=1&is_app=1" + shareUrl;
                } else if (resourceinfolist.getPhysical_resource().getIs_activity() == 1) {
                    sharewxMiniShareLinkUrl = Config.WX_MINI_SHARE_ACTIVITY_INFO_URL + mSellResId + shareUrl;
                    share_linkurl = Config.SHARE_ACTIVITIES_INFO_URL + mSellResId + "?res_type_id=3&BackKey=1&is_app=1" + shareUrl;
                }
                if (resourceinfolist.getPhysical_resource().getNumber_of_people() != null &&
                        resourceinfolist.getPhysical_resource().getNumber_of_people() > 0) {
                    mFieldinfoNumberOfPeopleTV.setText(
                            String.valueOf(resourceinfolist.getPhysical_resource().getNumber_of_people()));
                } else {
                    mFieldinfoNumberOfPeopleTV.setText(getResources().getString(R.string.groupbooding_no_data_str));
                }
                if (resourceinfolist.getPhysical_resource().getNumber_of_order() != null) {
                    mtxt_transaction_success.setText(
                            String.valueOf(resourceinfolist.getPhysical_resource().getNumber_of_order()));
                } else {
                    mtxt_transaction_success.setText(getResources().getString(R.string.groupbooding_no_data_str));
                }
                if (resourceinfolist.getIndustry_str() != null &&
                        resourceinfolist.getIndustry_str().length() > 0) {
                    mFieldInfoIndustryTV.setText(resourceinfolist.getIndustry_str());
                } else {
                    mFieldInfoIndustryTV.setText(getResources().getString(R.string.groupbooding_no_data_str));
                }

                //标签使用labels
                if (resourceinfolist.getPhysical_resource().getLabels() != null &&
                        resourceinfolist.getPhysical_resource().getLabels().size() > 0) {
                    mFieldInfoAgeLabelLL.setVisibility(View.VISIBLE);
                    for (int i = 0; i < resourceinfolist.getPhysical_resource().getLabels().size(); i++) {
                        if (i < 3) {
                            if (i == 0) {
                                if (resourceinfolist.getPhysical_resource().getLabels().get(i).getDisplay_name() != null &&
                                        resourceinfolist.getPhysical_resource().getLabels().get(i).getDisplay_name().length() > 0) {
                                    mFieldInfoLabelTV0.setVisibility(View.VISIBLE);
                                    mFieldInfoLabelTV0.setText(resourceinfolist.getPhysical_resource().getLabels().get(i).getDisplay_name());
                                } else {
                                    if (resourceinfolist.getPhysical_resource().getLabels().get(i).getName() != null &&
                                            resourceinfolist.getPhysical_resource().getLabels().get(i).getName().length() > 0) {
                                        mFieldInfoLabelTV0.setVisibility(View.VISIBLE);
                                        mFieldInfoLabelTV0.setText(resourceinfolist.getPhysical_resource().getLabels().get(i).getName());
                                    } else {
                                        mFieldInfoLabelTV0.setVisibility(View.GONE);
                                    }
                                }
                            } else if (i == 1) {
                                if (resourceinfolist.getPhysical_resource().getLabels().get(i).getDisplay_name() != null &&
                                        resourceinfolist.getPhysical_resource().getLabels().get(i).getDisplay_name().length() > 0) {
                                    mFieldInfoLabelTV1.setVisibility(View.VISIBLE);
                                    mFieldInfoLabelTV1.setText(resourceinfolist.getPhysical_resource().getLabels().get(i).getDisplay_name());
                                } else {
                                    if (resourceinfolist.getPhysical_resource().getLabels().get(i).getName() != null &&
                                            resourceinfolist.getPhysical_resource().getLabels().get(i).getName().length() > 0) {
                                        mFieldInfoLabelTV1.setVisibility(View.VISIBLE);
                                        mFieldInfoLabelTV1.setText(resourceinfolist.getPhysical_resource().getLabels().get(i).getName());
                                    } else {
                                        mFieldInfoLabelTV1.setVisibility(View.GONE);
                                    }
                                }
                            } else if (i == 2) {
                                if (resourceinfolist.getPhysical_resource().getLabels().get(i).getDisplay_name() != null &&
                                        resourceinfolist.getPhysical_resource().getLabels().get(i).getDisplay_name().length() > 0) {
                                    mFieldInfoLabelTV2.setVisibility(View.VISIBLE);
                                    mFieldInfoLabelTV2.setText(resourceinfolist.getPhysical_resource().getLabels().get(i).getDisplay_name());
                                } else {
                                    if (resourceinfolist.getPhysical_resource().getLabels().get(i).getName() != null &&
                                            resourceinfolist.getPhysical_resource().getLabels().get(i).getName().length() > 0) {
                                        mFieldInfoLabelTV2.setVisibility(View.VISIBLE);
                                        mFieldInfoLabelTV2.setText(resourceinfolist.getPhysical_resource().getLabels().get(i).getName());
                                    } else {
                                        mFieldInfoLabelTV2.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    mFieldInfoAgeLabelLL.setVisibility(View.GONE);
                }
                String cityName = "";
                if (resourceinfolist.getPhysical_resource().getCommunity().getDetailed_address() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getDetailed_address().length() > 0) {
                    if (resourceinfolist.getPhysical_resource().getCommunity().getDistrict() != null &&
                            resourceinfolist.getPhysical_resource().getCommunity().getDistrict().getName() != null &&
                            resourceinfolist.getPhysical_resource().getCommunity().getDistrict().getName().length() > 0) {
                        mResAddressStr = resourceinfolist.getPhysical_resource().getCommunity().getDistrict().getName() +
                                resourceinfolist.getPhysical_resource().getCommunity().getDetailed_address();
                    } else {
                        mResAddressStr = resourceinfolist.getPhysical_resource().getCommunity().getDetailed_address();
                    }

                    //2017/10/19 分享时的描述
                    String mShareResPrice = "";
                    if (resourceinfolist.getPhysical_resource().getMin_price() != null) {
                        mSharePriceLL.setVisibility(View.VISIBLE);
                        mSharePriceTV.setText(Constants.getpricestring(String.valueOf(resourceinfolist.getPhysical_resource().getMin_price()), 0.01));
                        if (isShareSubsidy) {
                            mShareSubsidyImgv.setVisibility(View.VISIBLE);
                        }
                    }
                    if (resourceinfolist.getPhysical_resource().getCommunity().getCity() != null &&
                            resourceinfolist.getPhysical_resource().getCommunity().getCity().getName() != null &&
                            resourceinfolist.getPhysical_resource().getCommunity().getCity().getName().length() > 0) {
                        cityName = resourceinfolist.getPhysical_resource().getCommunity().getCity().getName();
                        mResAddressStr = resourceinfolist.getPhysical_resource().getCommunity().getCity().getName() + mResAddressStr;
                        SharedescriptionStr = SharedescriptionStr + "\n" +
                                mResAddressStr;
                    }
                    mtxt_address.setText(mResAddressStr);
                    // FIXME: 2018/12/10 分享修改
                    mShareDescriptionTV.setText(SharedescriptionStr);
                }
                //朋友圈分享标题
                mSharePYQTitleStr = getResources().getString(R.string.fieldinfo_share_text) + "(" +
                        cityName + ShareTitleStr + ")" +
                        getResources().getString(R.string.fieldinfo_share_linhuiba_mark_text);

                if (resourceinfolist.getPhysical_resource().getDoLocation() != null &&
                        resourceinfolist.getPhysical_resource().getDoLocation().length() > 0) {
                    mFieldinfoDoLocationTV.setText(resourceinfolist.getPhysical_resource().getDoLocation());
                    mFieldinfoLocationTV.setText(getResources().getString(R.string.fieldinfo_dolocation_tv_str) +
                            resourceinfolist.getPhysical_resource().getDoLocation());
                    if (resourceinfolist.getPhysical_resource().getIndoor() != null) {
                        if (resourceinfolist.getPhysical_resource().getIndoor() == 0) {
                            mFieldinfoLocationIndoorTV.setText(getResources().getString(R.string.fieldinfo_location_unindoor_tv_str));
                            mFieldinfoLocationIndoorTV.setVisibility(View.VISIBLE);
                        } else if (resourceinfolist.getPhysical_resource().getIndoor() == 1) {
                            mFieldinfoLocationIndoorTV.setText(getResources().getString(R.string.fieldinfo_location_indoor_tv_str));
                            mFieldinfoLocationIndoorTV.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mFieldinfoLocationIndoorTV.setVisibility(View.GONE);
                    }
                } else {
                    mFieldinfoDoLocationTV.setText(getResources().getString(R.string.fieldinfo_no_parameter_message));
                }

                if (resourceinfolist.getPhysical_resource().getDo_begin() != null &&
                        resourceinfolist.getPhysical_resource().getDo_finish() != null &&
                        resourceinfolist.getPhysical_resource().getDo_begin().length() > 0 &&
                        resourceinfolist.getPhysical_resource().getDo_finish().length() > 0) {
                    mtxt_stall_time.setText(resourceinfolist.getPhysical_resource().getDo_begin() + "-" +
                            resourceinfolist.getPhysical_resource().getDo_finish());
                } else {
                    mtxt_stall_time.setText(getResources().getString(R.string.groupbooding_no_data_str));
                }
                if (resourceinfolist.getPhysical_resource().getTotal_area() != null &&
                        resourceinfolist.getPhysical_resource().getTotal_area() > 0) {
                    mtxt_total_area.setText(String.valueOf(resourceinfolist.getPhysical_resource().getTotal_area()) +
                            getResources().getString(R.string.myselfinfo_company_demand_area_unit_text));
                } else {
                    mtxt_total_area.setText(getResources().getString(R.string.groupbooding_no_data_str));
                }
                if (resourceinfolist.getCoupons() != null && resourceinfolist.getCoupons().size() > 0) {
                    mFieldinfoReceiveCouponsLL.setVisibility(View.VISIBLE);
                    if (resourceinfolist.getCoupons().get(0).getAmount() != null &&
                            resourceinfolist.getCoupons().get(0).getAmount().toString().length() > 0) {
                        mFieldinfoCouponsPrice.setText(Constants.getpricestring(resourceinfolist.getCoupons().get(0).getAmount(),
                                1));
                    }
                    for (int i = 0; i < resourceinfolist.getCoupons().size(); i++) {
                        if (i < 3) {
                            if (i == 0) {
                                mFieldinfoCoupons1.setVisibility(View.VISIBLE);
                                if (resourceinfolist.getCoupons().get(i).getMin_goods_amount() != null &&
                                        resourceinfolist.getCoupons().get(i).getMin_goods_amount().length() > 0 &&
                                        Double.parseDouble(resourceinfolist.getCoupons().get(i).getMin_goods_amount()) > 0) {
                                    mFieldinfoCoupons1.setText(getResources().getString(R.string.module_coupons_first_register_item_amount_first_str) +
                                            Constants.getpricestring(resourceinfolist.getCoupons().get(i).getMin_goods_amount(), 1) +
                                            getResources().getString(R.string.module_my_coupons_item_amount_last_str) +
                                            Constants.getpricestring(resourceinfolist.getCoupons().get(i).getAmount(), 1));
                                } else {
                                    mFieldinfoCoupons1.setText(Constants.getpricestring(resourceinfolist.getCoupons().get(i).getAmount(), 1) +
                                            getResources().getString(R.string.term_types_unit_txt) +
                                            getResources().getString(R.string.module_fieldinfo_coupons_no_threshold));
                                }

                            } else if (i == 1) {
                                mFieldinfoCoupons2.setVisibility(View.VISIBLE);
                                if (resourceinfolist.getCoupons().get(i).getMin_goods_amount() != null &&
                                        resourceinfolist.getCoupons().get(i).getMin_goods_amount().length() > 0 &&
                                        Double.parseDouble(resourceinfolist.getCoupons().get(i).getMin_goods_amount()) > 0) {
                                    mFieldinfoCoupons2.setText(getResources().getString(R.string.module_coupons_first_register_item_amount_first_str) +
                                            Constants.getpricestring(resourceinfolist.getCoupons().get(i).getMin_goods_amount(), 1) +
                                            getResources().getString(R.string.module_my_coupons_item_amount_last_str) +
                                            Constants.getpricestring(resourceinfolist.getCoupons().get(i).getAmount(), 1));
                                } else {
                                    mFieldinfoCoupons2.setText(Constants.getpricestring(resourceinfolist.getCoupons().get(i).getAmount(), 1) +
                                            getResources().getString(R.string.term_types_unit_txt) +
                                            getResources().getString(R.string.module_fieldinfo_coupons_no_threshold));
                                }
                            } //                        else if (i == 2) {
//                            mFieldinfoCoupons3.setVisibility(View.VISIBLE);
//                            if (resourceinfolist.getCoupons().get(i).getMin_goods_amount() != null &&
//                                    resourceinfolist.getCoupons().get(i).getMin_goods_amount().length() > 0 &&
//                                    Double.parseDouble(resourceinfolist.getCoupons().get(i).getMin_goods_amount()) > 0) {
//                                mFieldinfoCoupons3.setText(getResources().getString(R.string.module_coupons_first_register_item_amount_first_str) +
//                                        Constants.getpricestring(resourceinfolist.getCoupons().get(i).getMin_goods_amount(),1) +
//                                        getResources().getString(R.string.module_my_coupons_item_amount_last_str) +
//                                        Constants.getpricestring(resourceinfolist.getCoupons().get(i).getAmount(),1));
//
//                            } else {
//                                mFieldinfoCoupons3.setText(Constants.getpricestring(resourceinfolist.getCoupons().get(i).getAmount(),1) +
//                                        getResources().getString(R.string.term_types_unit_txt) +
//                                        getResources().getString(R.string.module_fieldinfo_coupons_no_threshold));
//                            }
//                        }

                        }
                    }
                } else {
                    mFieldinfoReceiveCouponsLL.setVisibility(View.GONE);
                }
                //物业要求显示
                if ((resourceinfolist.getPhysical_resource().getRequirement() != null && resourceinfolist.getPhysical_resource().getRequirement().size() > 0) ||
                        (resourceinfolist.getPhysical_resource().getProperty_requirement() != null && resourceinfolist.getPhysical_resource().getProperty_requirement().length() > 0)) {
                    mtxt_property_requirement_layout.setVisibility(View.VISIBLE);
                    String property_requirement = "";
                    if (resourceinfolist.getPhysical_resource().getRequirement() != null && resourceinfolist.getPhysical_resource().getRequirement().size() > 0) {
                        for (int i = 0; i < resourceinfolist.getPhysical_resource().getRequirement().size(); i++) {
                            if (resourceinfolist.getPhysical_resource().getRequirement().get(i).getDisplay_name() != null &&
                                    resourceinfolist.getPhysical_resource().getRequirement().get(i).getDisplay_name().length() > 0) {
                                if (i != 0) {
                                    property_requirement = property_requirement + "、" + resourceinfolist.getPhysical_resource().getRequirement().get(i).getDisplay_name();
                                } else {
                                    property_requirement = property_requirement + resourceinfolist.getPhysical_resource().getRequirement().get(i).getDisplay_name();
                                }
                            }
                        }
                    }
                    if (resourceinfolist.getPhysical_resource().getProperty_requirement() != null && resourceinfolist.getPhysical_resource().getProperty_requirement().length() > 0) {
                        if (resourceinfolist.getPhysical_resource().getRequirement() != null && resourceinfolist.getPhysical_resource().getRequirement().size() > 0) {
                            property_requirement = property_requirement + "、" + resourceinfolist.getPhysical_resource().getProperty_requirement();
                        } else {
                            property_requirement = property_requirement + resourceinfolist.getPhysical_resource().getProperty_requirement();
                        }
                    }
                    mtxt_property_requirement.setText(property_requirement);
                } else {
                    mtxt_property_requirement.setText(getResources().getString(R.string.groupbooding_no_data_str));
                }

                if ((resourceinfolist.getPhysical_resource().getContraband() != null && resourceinfolist.getPhysical_resource().getContraband().size() > 0) ||
                        (resourceinfolist.getPhysical_resource().getOther_contraband() != null && resourceinfolist.getPhysical_resource().getOther_contraband().length() > 0)) {
                    String contraband_str = "";
                    if (resourceinfolist.getPhysical_resource().getContraband() != null && resourceinfolist.getPhysical_resource().getContraband().size() > 0) {
                        for (int i = 0; i < resourceinfolist.getPhysical_resource().getContraband().size(); i++) {
                            if (resourceinfolist.getPhysical_resource().getContraband().get(i).getDisplay_name() != null &&
                                    resourceinfolist.getPhysical_resource().getContraband().get(i).getDisplay_name().length() > 0) {
                                if (i != 0) {
                                    contraband_str = contraband_str + "、" + resourceinfolist.getPhysical_resource().getContraband().get(i).getDisplay_name();
                                } else {
                                    contraband_str = contraband_str + resourceinfolist.getPhysical_resource().getContraband().get(i).getDisplay_name();
                                }
                            }
                        }
                    }
                    if (resourceinfolist.getPhysical_resource().getOther_contraband() != null && resourceinfolist.getPhysical_resource().getOther_contraband().length() > 0) {
                        if (resourceinfolist.getPhysical_resource().getContraband() != null && resourceinfolist.getPhysical_resource().getContraband().size() > 0) {
                            contraband_str = contraband_str + "、" + resourceinfolist.getPhysical_resource().getOther_contraband();
                        } else {
                            contraband_str = contraband_str + resourceinfolist.getPhysical_resource().getOther_contraband();
                        }
                    }
                    mtxt_contraband.setText(contraband_str);
                    mContrabandLL.setVisibility(View.VISIBLE);
                } else {
                    mContrabandLL.setVisibility(View.GONE);
                }
                if (resourceinfolist.getPhysical_resource().getPeak_time() != null &&
                        resourceinfolist.getPhysical_resource().getPeak_time().getDisplay_name() != null &&
                        resourceinfolist.getPhysical_resource().getPeak_time().getDisplay_name().length() > 0) {
                    mtxt_number_of_people_peak_time.setText(resourceinfolist.getPhysical_resource().getPeak_time().getDisplay_name());
                } else {
                    mtxt_number_of_people_peak_time.setText(getResources().getString(R.string.groupbooding_no_data_str));
                }
                if (resourceinfolist.getPhysical_resource().getMale_proportion() != null &&
                        resourceinfolist.getPhysical_resource().getMale_proportion() > 0) {
                    mgender_ratio.setVisibility(View.VISIBLE);
                    mtxt_gender_ratio.setText(getResources().getString(R.string.fieldinfo_man_proportion_text) +
                            String.valueOf(resourceinfolist.getPhysical_resource().getMale_proportion()) + getResources().getString(R.string.fieldinfo_man_proportion_unit_text) +
                            "," + getResources().getString(R.string.fieldinfo_woman_proportion_text) +
                            String.valueOf(100 - resourceinfolist.getPhysical_resource().getMale_proportion()) + getResources().getString(R.string.fieldinfo_man_proportion_unit_text));
                } else {
                    mgender_ratio.setVisibility(View.GONE);
                }

                // 配套设施


                if (resourceinfolist.getPhysical_resource().getFacade() != null && resourceinfolist.getPhysical_resource().getFacade() > 0) {
                    mtxt_number_of_people_facade.setText(String.valueOf(resourceinfolist.getPhysical_resource().getFacade()) + "面");
                } else {
                    mtxt_number_of_people_facade.setText(getResources().getString(R.string.fieldinfo_no_parameter_message));
                }
                if (resourceinfolist.getPhysical_resource().getDescription() != null &&
                        resourceinfolist.getPhysical_resource().getDescription().length() > 0) {
                    mtxt_description_ll.setVisibility(View.VISIBLE);
                    mtxt_description.setText(resourceinfolist.getPhysical_resource().getDescription());
                } else {
                    mtxt_description_ll.setVisibility(View.GONE);
                }

                if (resourceinfolist.getPhysical_resource().getParticipation_level() != null &&
                        resourceinfolist.getPhysical_resource().getParticipation_level().getDisplay_name() != null &&
                        resourceinfolist.getPhysical_resource().getParticipation_level().getDisplay_name().length() > 0) {
                    mtxt_participation.setText(resourceinfolist.getPhysical_resource().getParticipation_level().getDisplay_name());
                }

                if (resourceinfolist.getPhysical_resource().getConsumption_level() != null &&
                        resourceinfolist.getPhysical_resource().getConsumption_level().getDisplay_name() != null &&
                        resourceinfolist.getPhysical_resource().getConsumption_level().getDisplay_name().length() > 0) {
                    mtxt_consumption_level.setText(resourceinfolist.getPhysical_resource().getConsumption_level().getDisplay_name());
                }


                //历史单量字段确认
                if (resourceinfolist.getPhysical_resource().getInformation() != null &&
                        resourceinfolist.getPhysical_resource().getInformation().length() > 0) {
                    mFieldinfoSaleVolumTV.setText(resourceinfolist.getPhysical_resource().getInformation());
                } else {
                    mFieldinfoSaleVolumLL.setVisibility(View.GONE);
                }

                //评价列表显示
                if (resourceinfolist.getPhysical_resource().getCount_of_reviews() != null) {
                    mreview_number_txt.setText("(" + String.valueOf(resourceinfolist.getPhysical_resource().getCount_of_reviews()) + ")");
                }

                //评论列表
                mFieldinfoMvpPresenter.getResInfoReview(getfieldid, "1", "2");
                //筛选条件
                boolean isScreenShow = false;
                String priceStr = "";
                String areaStr = "";
                String personStr = "";
                if (mApiResourcesModel != null) {
                    if (mApiResourcesModel.getMin_price() != null && mApiResourcesModel.getMin_price().length() > 0) {
                        isScreenShow = true;
                        priceStr = Constants.getpricestring(mApiResourcesModel.getMin_price(), 0.01);
                    }
                    if (mApiResourcesModel.getMax_price() != null && mApiResourcesModel.getMax_price().length() > 0) {
                        isScreenShow = true;
                        if (priceStr.length() > 0) {
                            priceStr = priceStr + "-" + Constants.getpricestring(mApiResourcesModel.getMax_price(), 0.01);
                        } else {
                            priceStr = Constants.getpricestring(mApiResourcesModel.getMax_price(), 0.01) +
                                    getResources().getString(R.string.module_fieldinfo_screen_max);
                        }
                    } else {
                        if (priceStr.length() > 0) {
                            priceStr = priceStr + getResources().getString(R.string.module_fieldinfo_screen_min);
                        }
                    }
                    if (mApiResourcesModel.getMin_area() != null && mApiResourcesModel.getMin_area().length() > 0) {
                        isScreenShow = true;
                        areaStr = mApiResourcesModel.getMin_area();
                    }
                    if (mApiResourcesModel.getMax_area() != null && mApiResourcesModel.getMax_area().length() > 0) {
                        isScreenShow = true;
                        if (areaStr.length() > 0) {
                            areaStr = areaStr + "-" + mApiResourcesModel.getMax_area();
                        } else {
                            areaStr = mApiResourcesModel.getMax_area() +
                                    getResources().getString(R.string.module_fieldinfo_screen_max);
                        }
                    } else {
                        if (areaStr.length() > 0) {
                            areaStr = areaStr + getResources().getString(R.string.module_fieldinfo_screen_min);
                        }
                    }
                    if (mApiResourcesModel.getMin_person_flow() != null && mApiResourcesModel.getMin_person_flow().length() > 0) {
                        isScreenShow = true;
                        personStr = mApiResourcesModel.getMin_person_flow();
                    }
                    if (mApiResourcesModel.getMax_person_flow() != null && mApiResourcesModel.getMax_person_flow().length() > 0) {
                        isScreenShow = true;
                        if (personStr.length() > 0) {
                            personStr = personStr + "-" + mApiResourcesModel.getMax_person_flow();
                        } else {
                            personStr = mApiResourcesModel.getMax_person_flow() +
                                    getResources().getString(R.string.module_fieldinfo_screen_max);
                        }
                    } else {
                        if (personStr.length() > 0) {
                            personStr = personStr + getResources().getString(R.string.module_fieldinfo_screen_min);
                        }
                    }
                }
                if (isScreenShow == true) {
                    mFieldinfoScreenConditionLL.setVisibility(View.VISIBLE);
                    if (priceStr.length() > 0) {
                        mFieldinfoScreenPriceTV.setVisibility(View.VISIBLE);
                        mFieldinfoScreenPriceTV.setText(getResources().getString(R.string.module_searchlist_screen_price_tv_str) +
                                priceStr);
                        updataSizeLv(false);
                    }
                    // FIXME: 2018/12/27 筛选条件 展位面积和人流量代码  取消这两个条件
                    if (areaStr.length() > 0) {
                        mFieldinfoScreenAreaTV.setVisibility(View.GONE);
                        mFieldinfoScreenAreaTV.setText(getResources().getString(R.string.module_searchlist_screen_area_tv_str) +
                                areaStr + getResources().getString(R.string.module_community_info_area_unit));
                    }
                    if (personStr.length() > 0) {
                        mFieldinfoScreenPersonTV.setVisibility(View.GONE);
                        mFieldinfoScreenPersonTV.setText(getResources().getString(R.string.fieldinfo_number_of_people_text) +
                                personStr);
                    }
                }
                // FIXME: 2018/12/19 顾问
                if (resourceinfolist.getService_representative() != null &&
                        resourceinfolist.getService_representative().getId() > 0) {
                    mFieldinfoCounseLL.setVisibility(View.VISIBLE);
                    if (resourceinfolist.getService_representative().getAvatar() != null &&
                            resourceinfolist.getService_representative().getAvatar().length() > 0) {
                        Picasso.with(FieldInfoActivity.this).load(resourceinfolist.getService_representative().getAvatar()).resize(
                                com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldInfoActivity.this,100),
                                com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldInfoActivity.this,106)
                        ).into(mFieldinfoCounselorImgv);
                    }
                    if (resourceinfolist.getService_representative().getName() != null &&
                            resourceinfolist.getService_representative().getName().length() > 0) {
                        mFieldinfoCounselorNameTV.setText(resourceinfolist.getService_representative().getName());
                    }
                    if (resourceinfolist.getService_representative().getProfile() != null &&
                            resourceinfolist.getService_representative().getProfile().length() > 0) {
                        mFieldinfoCounselorDescriptionTV.setText(resourceinfolist.getService_representative().getProfile());
                    }
                }
                // FIXME: 2018/12/22 图文详情

                if (resourceinfolist.getPhysical_resource().getIs_activity() == 1) {
                    if (resourceinfolist.getSize() != null && resourceinfolist.getSize().size() > 0 &&
                            resourceinfolist.getSize().get(0).getResource() != null &&
                            resourceinfolist.getSize().get(0).getResource().size() > 0 &&
                            resourceinfolist.getSize().get(0).getResource().get(0).getImg_description() != null &&
                            resourceinfolist.getSize().get(0).getResource().get(0).getImg_description().length() > 0) {
                        mFieldinfoPicWordLL.setVisibility(View.VISIBLE);
                        mFieldinfoPicWordWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
                        mFieldinfoPicWordWebview.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
                        mFieldinfoPicWordWebview.getSettings().setSupportZoom(true);//是否可以缩放，默认true
                        mFieldinfoPicWordWebview.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
                        mFieldinfoPicWordWebview.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
                        mFieldinfoPicWordWebview.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
                        mFieldinfoPicWordWebview.getSettings().setAppCacheEnabled(false);//是否使用缓存
                        mFieldinfoPicWordWebview.getSettings().setDomStorageEnabled(true);//DOM Storage
                        mFieldinfoPicWordWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                        mFieldinfoPicWordWebview.loadUrl(Config.FIELDINFO_PIC_WORD_URL + "?type=3&id=" + mSellResId);
                    }
                } else {
                    if (resourceinfolist.getPhysical_resource().getImg_description() != null &&
                            resourceinfolist.getPhysical_resource().getImg_description().length() > 0) {
                        mFieldinfoPicWordLL.setVisibility(View.VISIBLE);
                        mFieldinfoPicWordWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
                        mFieldinfoPicWordWebview.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
                        mFieldinfoPicWordWebview.getSettings().setSupportZoom(true);//是否可以缩放，默认true
                        mFieldinfoPicWordWebview.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
                        mFieldinfoPicWordWebview.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
                        mFieldinfoPicWordWebview.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
                        mFieldinfoPicWordWebview.getSettings().setAppCacheEnabled(false);//是否使用缓存
                        mFieldinfoPicWordWebview.getSettings().setDomStorageEnabled(true);//DOM Storage
                        mFieldinfoPicWordWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                        mFieldinfoPicWordWebview.loadUrl(Config.FIELDINFO_PIC_WORD_URL + "?type=2&id=" + getfieldid);
                    }
                }
            }
        }
    }
    private void createOrderIntent() {
        Intent OrderConfirm = new Intent(FieldInfoActivity.this,OrderConfirmActivity.class);
        ArrayList<Integer> community_ids = new ArrayList<>();
        community_ids.add(mCommunityId);
        OrderConfirm.putExtra("community_ids",(Serializable) community_ids);
        OrderConfirm.putExtra("ordertype",1);
        OrderConfirm.putExtra("submitorderlist", (Serializable) getsubmitorderlist());
        OrderConfirm.putExtra("deposit",deposit);
        startActivity(OrderConfirm);
        mFieldinfoChooseSizeDialog.dismiss();
    }
    private void startEnquiryIntent(int position) {
        Intent enquiryIntent = new Intent(FieldInfoActivity.this, AboutUsActivity.class);
        enquiryIntent.putExtra("type",Config.ENQUIRY_WEB_INT);
        enquiryIntent.putExtra("id",resourceinfolist.getEnquiry_resource().get(position).getId());
        startActivity(enquiryIntent);
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
            mFieldEvaluationAdapter = new Fieldinfo_ReviewAdapter(FieldInfoActivity.this, mReviewList,FieldInfoActivity.this);
            mreview_listview.setAdapter(mFieldEvaluationAdapter);
        }
    }

    @Override
    public void onResReviewFailure(boolean superresult, Throwable error) {
        mreview_listview.setVisibility(View.GONE);
    }

    @Override
    public void onGroupinfoResSuccess(GroupBookingInfoModel resInfo) {

    }

    @Override
    public void onNearbyResSuccess(ArrayList<ResourceSearchItemModel> list) {

    }

    @Override
    public void onNearbyResMoreSuccess(ArrayList<ResourceSearchItemModel> list) {

    }

    @Override
    public void onOtherPhyResSuccess(ArrayList<ResourceSearchItemModel> list,int csort,int total) {
        mFieldInfoOtherDataList = list;
        mCsort = csort;
        if (mActivityAdapter != null) {
            mActivityAdapter.sort = csort;
            mActivityAdapter.notifyDataSetChanged();
        }
        mFieldinfoPhySizeTV.setText(getResources().getString(R.string.module_fieldinfo_phy_str) +
        String.valueOf(csort));
        if (mActivityAdapter != null) {
            mActivityAdapter.notifyDataSetChanged();
        } else {
            //展位下的所有供给（活动）
            mFieldinfoMvpPresenter.getSellRes(getfieldid,"3");
        }
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
                    Intent fieldinfo = null;
                    if (mFieldInfoOtherDataList.get(position).getType() != null) {
                        if (mFieldInfoOtherDataList.get(position).getType().equals(com.linhuiba.business.config.Config.JUMP_COMMUNITY_RES)) {
                            fieldinfo = new Intent(FieldInfoActivity.this, CommunityInfoActivity.class);
                            fieldinfo.putExtra("city_id", mCityId);
                            fieldinfo.putExtra("id", mFieldInfoOtherDataList.get(position).getCommunity_id());
                            startActivity(fieldinfo);
                        } else if (mFieldInfoOtherDataList.get(position).getType().equals(com.linhuiba.business.config.Config.JUMP_PHYSICAL_RES)) {
                            if (mFieldInfoOtherDataList.get(position).getTop_resource_id() != null) {
                                fieldinfo = new Intent(FieldInfoActivity.this, FieldInfoActivity.class);
                                fieldinfo.putExtra("good_type", 1);
                                fieldinfo.putExtra("fieldId", String.valueOf(mFieldInfoOtherDataList.get(position).getTop_resource_id()));
                                fieldinfo.putExtra("community_id", mFieldInfoOtherDataList.get(position).getCommunity_id());
                                startActivity(fieldinfo);
                            }
                        } else if (mFieldInfoOtherDataList.get(position).getType().equals(com.linhuiba.business.config.Config.JUMP_SELLING_RES)) {
                            if (mFieldInfoOtherDataList.get(position).getTop_resource_id() != null) {
                                fieldinfo = new Intent(FieldInfoActivity.this, FieldInfoActivity.class);
                                fieldinfo.putExtra("sell_res_id", String.valueOf(mFieldInfoOtherDataList.get(position).getTop_resource_id()));
                                fieldinfo.putExtra("is_sell_res", true);
                                fieldinfo.putExtra("community_id", mFieldInfoOtherDataList.get(position).getCommunity_id());
                                startActivity(fieldinfo);
                            }
                        }
                    }
                }
            });
        } else {
            mFieldinfoOtherResLL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onOtherPhyResMoreSuccess(ArrayList<ResourceSearchItemModel> list) {
        //展位其他活动供给
        mFieldInfoActivityList = list;
        if (mFieldInfoActivityList != null && mFieldInfoActivityList.size() > 0) {
            mFieldinfoActivityLL.setVisibility(View.VISIBLE);
            mActivityAdapter = new FieldinfoRecommendResourcesAdapter(this,this,
                    R.layout.activity_fieldinfo_recommend_resources_item,mFieldInfoActivityList, 3,mCsort);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            mFieldinfoActivityRV.setLayoutManager(gridLayoutManager);
            mFieldinfoActivityRV.setAdapter(mActivityAdapter);
            mActivityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent fieldinfo = new Intent(FieldInfoActivity.this, FieldInfoActivity.class);
                    fieldinfo.putExtra("is_sell_res", true);
                    fieldinfo.putExtra("sell_res_id", String.valueOf(mFieldInfoActivityList.get(position).getId()));
                    fieldinfo.putExtra("community_id", mCommunityId);
                    fieldinfo.putExtra("model", (Serializable) mApiResourcesModel);
                    startActivity(fieldinfo);
                }
            });
        } else {
            mFieldinfoActivityLL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCommunityInfoSuccess(CommunityInfoModel resInfo) {

    }

    @Override
    public void onRecommendResSuccess(ArrayList<ResourceSearchItemModel> list) {
        mRecommendDateList = list;
        if (mRecommendDateList != null && mRecommendDateList.size() > 0) {
            mFieldinfoRecommendTypeLL.setVisibility(View.VISIBLE);
            mRecommendAdapter = new FieldinfoRecommendResourcesAdapter(this,this,
                    R.layout.activity_fieldinfo_recommend_resources_item,mRecommendDateList, 1);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            mFieldinfoRecommendRV.setLayoutManager(gridLayoutManager);
            mFieldinfoRecommendRV.setAdapter(mRecommendAdapter);
            mFieldInfoLoadMoreLL.setVisibility(View.GONE);
            if (mRecommendDateList.size() < 10) {
                mFieldInfoNullTV.setVisibility(View.VISIBLE);
            }
            mRecommendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent fieldinfo = null;
                    if (mRecommendDateList.get(position).getType() != null) {
                        if (mRecommendDateList.get(position).getType().equals(com.linhuiba.business.config.Config.JUMP_COMMUNITY_RES)) {
                            fieldinfo = new Intent(FieldInfoActivity.this, CommunityInfoActivity.class);
                            fieldinfo.putExtra("city_id", mRecommendDateList.get(position).getCity().getId());
                            fieldinfo.putExtra("id", mRecommendDateList.get(position).getId());
                            startActivity(fieldinfo);
                        } else if (mRecommendDateList.get(position).getType().equals(com.linhuiba.business.config.Config.JUMP_PHYSICAL_RES)) {
                            if (mRecommendDateList.get(position).getTop_physical_id() != null) {
                                fieldinfo = new Intent(FieldInfoActivity.this, FieldInfoActivity.class);
                                fieldinfo.putExtra("good_type", 1);
                                fieldinfo.putExtra("fieldId", String.valueOf(mRecommendDateList.get(position).getTop_physical_id()));
                                fieldinfo.putExtra("community_id", mRecommendDateList.get(position).getId());
                                startActivity(fieldinfo);
                            }
                        } else if (mRecommendDateList.get(position).getType().equals(com.linhuiba.business.config.Config.JUMP_SELLING_RES)) {
                            if (mRecommendDateList.get(position).getTop_physical_id() != null) {
                                fieldinfo = new Intent(FieldInfoActivity.this, FieldInfoActivity.class);
                                fieldinfo.putExtra("sell_res_id", String.valueOf(mRecommendDateList.get(position).getTop_physical_id()));
                                fieldinfo.putExtra("is_sell_res", true);
                                fieldinfo.putExtra("community_id", mRecommendDateList.get(position).getId());
                                startActivity(fieldinfo);
                            }
                        }
                    }
                }
            });
            mscrollview.setOnScrollToBottomLintener(new Field_MyScrollview.OnScrollToBottomListener() {
                @Override
                public void onScrollBottomListener(boolean isBottom) {
                    if (isBottom && mFieldInfoNullTV.getVisibility() == View.GONE &&
                            mFieldInfoLoadMoreLL.getVisibility() == View.GONE) {
                        mFieldInfoLoadMoreLL.setVisibility(View.VISIBLE);
                        mFieldinfoRecommendPageInt ++;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mFieldinfoMvpPresenter.getRecommendedRes(mCommunityId, 10, mFieldinfoRecommendPageInt,mCityId,mCategoryId);
                            }
                        }).start();
                        }
                }
            });
        } else {
            mFieldinfoRecommendTypeLL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRecommendResMoreSuccess(ArrayList<ResourceSearchItemModel> list) {
        ArrayList<ResourceSearchItemModel> tmp = list;
        if (tmp != null && tmp.size() > 0) {
            for( ResourceSearchItemModel fieldDetail: tmp ){
                mRecommendDateList.add(fieldDetail);
            }
            mRecommendAdapter.notifyDataSetChanged();
            mFieldInfoLoadMoreLL.setVisibility(View.GONE);
            if (tmp.size() < 10) {
                mFieldInfoNullTV.setVisibility(View.VISIBLE);
            }
        } else {
            mFieldInfoLoadMoreLL.setVisibility(View.GONE);
            mFieldInfoNullTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFeedbacksSuccess() {
        txt_error_correction_submit.setEnabled(true);
        if (newdialog.isShowing()) {
            newdialog.dismiss();
        }
        MessageUtils.showToast(getResources().getString(R.string.fieldinfo_error_recovery_content_remindsuccess_text));
    }

    @Override
    public void onFeedbacksFailure(boolean superresult, Throwable error) {
        txt_error_correction_submit.setEnabled(true);
        if (!superresult) {
            MessageUtils.showToast(getContext(), error.getMessage());
        }
        checkAccess_new(error);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
    }

    //详情优惠券领取
    private void showCouponsPW () {
        isPwCanReceive = false;
        isPwReceived = false;
        View myView = FieldInfoActivity.this.getLayoutInflater().inflate(R.layout.module_pw_fieldinfo_receive_coupon, null);
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

        mCouponsPW.showAtLocation(FieldInfoActivity.this.findViewById(R.id.all_relative_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                Intent myCouponsIntent = new Intent(FieldInfoActivity.this,MyCouponsActivity.class);
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
        mCanReceiveAdapter = new MyCouponsAdapter(R.layout.module_recycle_item_coupons,canReceiveCouponsList,FieldInfoActivity.this,
                2,FieldInfoActivity.this,1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        canReceiveRV.setLayoutManager(linearLayoutManager);
        canReceiveRV.setAdapter(mCanReceiveAdapter);
        mCanReceiveAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!LoginManager.isLogin()) {
                    Intent loginIntent = new Intent(FieldInfoActivity.this,LoginActivity.class);
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
        mReceivedAdapter = new MyCouponsAdapter(R.layout.module_recycle_item_coupons,receivedCouponsList,FieldInfoActivity.this,
                2,FieldInfoActivity.this,2);
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
                CustomDialog.Builder builder = new CustomDialog.Builder(FieldInfoActivity.this);
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
                com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(FieldInfoActivity.this,mIntegralDialog);
                mIntegralDialog.show();
            }
        } else {
            showProgressDialog();
            mCouponsMvpPresenter.receiveCoupons(coupon_id,1);
        }
    }

    //2018/11/12 计算天规格的最大押金
    private String getMaxDeposit(ArrayList<Date> dateList) {
        String deposit = "0";
        for (int i = 0; i < dateList.size(); i++) {
            int weekInt = com.linhuiba.linhuifield.connector.Constants.getDayForWeek(mSimpleDateFormat.format(dateList.get(i)));
            if (weekInt > -1) {
                if (mWeekLeaseTermDepositMap.get(weekInt) != null &&
                        mWeekLeaseTermDepositMap.get(weekInt).length() > 0 &&
                        Double.parseDouble(mWeekLeaseTermDepositMap.get(weekInt)) >
                        Double.parseDouble(deposit)) {
                    deposit = mWeekLeaseTermDepositMap.get(weekInt);
                }
            }
        }
        return deposit;
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
            CustomDialog.Builder builder = new CustomDialog.Builder(FieldInfoActivity.this);
            mDemandSuccessDialog = builder
                    .cancelTouchout(true)
                    .view(R.layout.field_activity_field_orders_success_dialog)
                    .showView(R.id.dialog_long_btn_tv,View.VISIBLE)
                    .showView(R.id.btn_cancel,View.GONE)
                    .showView(R.id.btn_perfect,View.GONE)
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
                                    resourceinfolist.getService_phone().toString())
                    .build();
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(FieldInfoActivity.this,mDemandSuccessDialog);
            mDemandSuccessDialog.show();
        }
    }
    private void initGetLocation() {
        AndPermission.with(FieldInfoActivity.this)
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
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                double mLocationLat = location.getLatitude();
                double mLocationLng = location.getLongitude();
                if (resourceinfolist.getPhysical_resource().getCommunity().getLat() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getLng() != null &&
                        mLocationLat > 0 &&
                        mLocationLng > 0) {
                    int distance = Integer.parseInt(Constants.getorderdoublepricestring(DistanceUtil.getDistance(new LatLng(mLocationLat,mLocationLng)
                            , new LatLng(resourceinfolist.getPhysical_resource().getCommunity().getLat(),
                                    resourceinfolist.getPhysical_resource().getCommunity().getLng())),1));
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
    private void startDemandActivity(){
        Intent demandIntent = new Intent(FieldInfoActivity.this,SubmitDemandActivity.class);
        demandIntent.putExtra("physical_res_id", String.valueOf(resourceinfolist.getPhysical_resource().getId()));
        demandIntent.putExtra("community_res_id", String.valueOf(resourceinfolist.getPhysical_resource().getCommunity().getId()));
        ArrayList<Integer> cityIds = new ArrayList<>();
        if (resourceinfolist.getPhysical_resource().getCommunity().getCity() != null &&
                resourceinfolist.getPhysical_resource().getCommunity().getCity().getId() > 0) {
            cityIds.add(resourceinfolist.getPhysical_resource().getCommunity().getCity().getId());
        }
        ArrayList<Integer> communityTypeIds = new ArrayList<>();
        if (resourceinfolist.getPhysical_resource().getCommunity().getCategory() != null &&
                resourceinfolist.getPhysical_resource().getCommunity().getCategory().getId() > 0) {
            communityTypeIds.add(resourceinfolist.getPhysical_resource().getCommunity().getCategory().getId());
        }
        demandIntent.putExtra("city_ids", (Serializable) cityIds);
        demandIntent.putExtra("community_type_ids", (Serializable) communityTypeIds);
        startActivityForResult(demandIntent,DEMAND_RESULTCODE);
    }
    private void showCounselorDialog(int showWxcode, boolean isCall, final int callPhoneCode) {
        if (mIntegralDialog == null || !mIntegralDialog.isShowing()) {
            View.OnClickListener uploadListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.app_defaylt_dialog_counselor_save_ll:
                            if (resourceinfolist.getService_representative() != null &&
                                    resourceinfolist.getService_representative().getQrcode() != null &&
                                    resourceinfolist.getService_representative().getQrcode().length() > 0) {
                                LinearLayout counselorLL = (LinearLayout) mIntegralDialog.getView().findViewById(R.id.app_defaylt_dialog_counselor_ll);
                                LinearLayout counselorWXLL = (LinearLayout) mIntegralDialog.getView().findViewById(R.id.fieldinfo_counselor_save_wx_code_ll);
                                ImageView counselorWXCodeImgv = (ImageView) mIntegralDialog.getView().findViewById(R.id.fieldinfo_counselor_save_wx_code_imgv);
                                counselorLL.setVisibility(View.GONE);
                                counselorWXLL.setVisibility(View.VISIBLE);
                                Picasso.with(FieldInfoActivity.this).load(resourceinfolist.getService_representative().getQrcode()
                                        + com.linhuiba.linhuipublic.config.Config.Linhui_Min_Watermark).resize(
                                        mDisplayMetrics.widthPixels - com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldInfoActivity.this,164 + 20),
                                        mDisplayMetrics.widthPixels - com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldInfoActivity.this,164+ 20)).into(counselorWXCodeImgv);
                            } else {
                                MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                            }
                            break;
                        case R.id.app_defaylt_dialog_counselor_call_ll:
                            AndPermission.with(FieldInfoActivity.this)
                                    .requestCode(CALL_PHONE_CODE)
                                    .permission(
                                            Manifest.permission.CALL_PHONE,
                                            Manifest.permission.READ_PHONE_STATE)
                                    .callback(listener)
                                    .start();
                            mIntegralDialog.dismiss();
                            break;
                        case R.id.app_defaylt_close_img_btn:
                            mIntegralDialog.dismiss();
                            break;
                        case R.id.fieldinfo_counselor_save_wx_code_btn_ll:
                            if (resourceinfolist.getService_representative() != null &&
                                    resourceinfolist.getService_representative().getQrcode() != null &&
                                    resourceinfolist.getService_representative().getQrcode().length() > 0) {
                                showProgressDialog();
                                new Thread(){
                                    public void run(){
                                        com.linhuiba.linhuifield.connector.Constants.saveToSystemGallery(FieldInfoActivity.this,
                                                resourceinfolist.getService_representative().getQrcode());// FIXME: 2018/12/19 测试url
                                        mHandler.sendEmptyMessage(2);
                                    }
                                }.start();
                            } else {
                                MessageUtils.showToast(getResources().getString(R.string.review_error_text));
                            }

                            break;
                        case R.id.app_defaylt_cancel_tv:
                            mIntegralDialog.dismiss();
                            break;
                        case R.id.app_defaylt_confirm_tv:
                            mIntegralDialog.dismiss();
                            AndPermission.with(FieldInfoActivity.this)
                                    .requestCode(callPhoneCode)
                                    .permission(
                                            Manifest.permission.CALL_PHONE,
                                            Manifest.permission.READ_PHONE_STATE)
                                    .callback(listener)
                                    .start();
                            break;
                        default:
                            break;
                    }
                }
            };
            if (isCall) {
                String phone = "";
                if (callPhoneCode == 111) {
                    phone = resourceinfolist.getService_phone();
                } else {
                    phone = resourceinfolist.getService_representative().getTel();
                }
                CustomDialog.Builder builder = new CustomDialog.Builder(FieldInfoActivity.this);
                mIntegralDialog = builder
                        .cancelTouchout(true)
                        .view(R.layout.activity_fieldinfo_refund_price_popuwindow)
                        .addViewOnclick(R.id.app_defaylt_cancel_tv,uploadListener)
                        .addViewOnclick(R.id.app_defaylt_confirm_tv,uploadListener)
                        .setText(R.id.app_defaylt_title_tv,phone)
                        .setText(R.id.app_defaylt_confirm_tv,getResources().getString(R.string.module_fieldinfo_call))
                        .showView(R.id.app_defaylt_dialog_ll,View.VISIBLE)
                        .showView(R.id.app_defaylt_dialog_remind_ll,View.VISIBLE)
                        .showView(R.id.app_defaylt_content_tv,View.GONE)
                        .build();
            } else {
                String name = "";
                String call = "";
                String profile = "";
                String imgUrl = com.linhuiba.linhuipublic.config.Config.LINHUIBA_LOGO_URL;// FIXME: 2018/12/19 测试数据
                String qrcodeUrl = com.linhuiba.linhuipublic.config.Config.LINHUIBA_LOGO_URL;// FIXME: 2018/12/19 测试数据
                if (resourceinfolist.getService_representative().getName() != null) {
                    name = resourceinfolist.getService_representative().getName();
                }
                if (resourceinfolist.getService_representative().getTel() != null) {
                    call = resourceinfolist.getService_representative().getTel();
                }
                if (resourceinfolist.getService_representative().getProfile() != null) {
                    profile = resourceinfolist.getService_representative().getProfile();
                }
                if (resourceinfolist.getService_representative().getAvatar() != null) {
                    imgUrl = resourceinfolist.getService_representative().getAvatar();
                }
                if (resourceinfolist.getService_representative().getQrcode() != null) {
                    qrcodeUrl = resourceinfolist.getService_representative().getQrcode();
                }

                CustomDialog.Builder builder = new CustomDialog.Builder(FieldInfoActivity.this);
                mIntegralDialog = builder
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
                        .setOvalImgvUrl(FieldInfoActivity.this,R.id.fieldinfo_counselor_imgv,imgUrl,// FIXME: 2018/12/19 测试数据
                                com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldInfoActivity.this,80),
                                com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldInfoActivity.this,80))
                        .setImgvUrl(FieldInfoActivity.this,R.id.app_defaylt_dialog_counselor_qrcode_imgv,qrcodeUrl,
                                com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldInfoActivity.this,40),
                                com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldInfoActivity.this,40))
                        .setImgvUrl(FieldInfoActivity.this,R.id.app_defaylt_dialog_counselor_avatar_imgv,imgUrl,
                                com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldInfoActivity.this,40),
                                com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldInfoActivity.this,40))
                        .showView(R.id.app_defaylt_dialog_ll,View.VISIBLE)
                        .showView(R.id.app_defaylt_dialog_counselor_ll,View.VISIBLE)
                        .showView(R.id.app_defaylt_close_img_btn,View.VISIBLE)
                        .showView(R.id.fieldinfo_counselor_contact_ll,View.GONE)
                        .build();
            }
            com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(FieldInfoActivity.this,mIntegralDialog);
            mIntegralDialog.show();
            if (showWxcode == 1) {
                LinearLayout counselorLL = (LinearLayout) mIntegralDialog.getView().findViewById(R.id.app_defaylt_dialog_counselor_ll);
                LinearLayout counselorWXLL = (LinearLayout) mIntegralDialog.getView().findViewById(R.id.fieldinfo_counselor_save_wx_code_ll);
                ImageView counselorWXCodeImgv = (ImageView) mIntegralDialog.getView().findViewById(R.id.fieldinfo_counselor_save_wx_code_imgv);
                counselorLL.setVisibility(View.GONE);
                counselorWXLL.setVisibility(View.VISIBLE);
                Picasso.with(FieldInfoActivity.this).load(resourceinfolist.getService_representative().getQrcode()
                        + com.linhuiba.linhuipublic.config.Config.Linhui_Min_Watermark).resize(
                        mDisplayMetrics.widthPixels - com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldInfoActivity.this,164),
                        mDisplayMetrics.widthPixels - com.linhuiba.linhuifield.connector.Constants.Dp2Px(FieldInfoActivity.this,164)).into(counselorWXCodeImgv);
            }
        }
    }
}
