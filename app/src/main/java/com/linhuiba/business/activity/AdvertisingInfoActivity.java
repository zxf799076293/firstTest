package com.linhuiba.business.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.CalendarClass.ChooseSpecificationsActivity;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.AdvertisingInfoPricelistAdapter;
import com.linhuiba.business.adapter.Fieldinfo_ReviewAdapter;
import com.linhuiba.business.adapter.GlideImageLoader;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.model.FieldInfoModel;
import com.linhuiba.business.model.FieldInfoSellResourcePriceModel;
import com.linhuiba.business.model.ReviewModel;
import com.linhuiba.business.mvpmodel.LoginMvpModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.MyGridview;
import com.linhuiba.business.view.MyListView;
import com.linhuiba.linhuifield.fieldview.Field_MyScrollview;
import com.linhuiba.linhuifield.fieldview.ZoomImageView;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.meiqia.core.MQManager;
import com.meiqia.core.callback.OnEndConversationCallback;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/3/8.
 */
public class AdvertisingInfoActivity extends BaseMvpActivity implements Field_AddFieldChoosePictureCallBack.FieldreviewCall, Field_AddFieldChoosePictureCallBack.CalendarClickCall,
        Field_MyScrollview.OnScrollListener  {
    @InjectView(R.id.txt_fieldtitle)
    TextView mtxt_fieldtitle;
    @InjectView(R.id.txt_fieldtitle_layout)
    LinearLayout mtxt_fieldtitle_layout;
    @InjectView(R.id.txt_field_description)
    TextView mtxt_field_description;
    @InjectView(R.id.txt_transaction_success)
    TextView mtxt_transaction_success;
    @InjectView(R.id.txt_evaluation)
    TextView mtxt_evaluation;
    @InjectView(R.id.txt_field_price)
    EditText mtxt_field_price;
    @InjectView(R.id.txt_field_price_unit_textview)
    TextView mtxt_field_price_unit_textview;
    @InjectView(R.id.field_model)
    TextView mfield_model;
    @InjectView(R.id.certification_table_textview)
    ImageView mcertification_table_textview;
    @InjectView(R.id.txt_field_ticket)
    TextView mtxt_field_ticket;
    @InjectView(R.id.txt_field_hasPower)
    TextView mtxt_field_hasPower;
    @InjectView(R.id.txt_has_tent)
    TextView mtxt_has_tent;
    @InjectView(R.id.txt_field_hasChair)
    TextView mtxt_field_hasChair;
    @InjectView(R.id.txt_field_goodsHelp)
    TextView mtxt_field_goodsHelp;
    @InjectView(R.id.txt_field_leaflet)
    TextView mtxt_field_leaflet;
    @InjectView(R.id.txt_stall_time)
    TextView mtxt_stall_time;
    @InjectView(R.id.txt_number_of_people)
    TextView mtxt_number_of_people;
    @InjectView(R.id.txt_number_of_people_layout)
    LinearLayout mtxt_number_of_people_layout;
    @InjectView(R.id.txt_year)
    TextView mtxt_year;
    @InjectView(R.id.txt_propertyCosts)
    TextView mtxt_propertyCosts;
    @InjectView(R.id.txt_field_rates)
    TextView mtxt_field_rates;
    @InjectView(R.id.txt_field_houseHolds)
    TextView mtxt_field_houseHolds;
    @InjectView(R.id.txt_doLocation)
    TextView mtxt_doLocation;
    @InjectView(R.id.fieldinfo_doLocation_layout)
    RelativeLayout mfieldinfo_doLocation_layout;
    @InjectView(R.id.txt_contraband)
    TextView mtxt_contraband;
    @InjectView(R.id.txt_address)
    TextView mtxt_address;
    @InjectView(R.id.txt_description)
    TextView mtxt_description;
    @InjectView(R.id.review_number_txt)
    TextView mreview_number_txt;
    @InjectView(R.id.review_listview)
    MyListView mreview_listview;
    @InjectView(R.id.selecttime_text)
    TextView mselecttime_text;
    @InjectView(R.id.hide_rates)
    TableRow hide_rates;
    @InjectView(R.id.hide_year)
    TableRow hide_year;
    @InjectView(R.id.txtrates)
    TextView mtxtrates;
    @InjectView(R.id.txthouseHolds)
    TextView mtxthouseHolds;
    @InjectView(R.id.field_order_layout)
    LinearLayout mfield_order_layout;
    @InjectView(R.id.field_lable_tablelayout)
    TableLayout mtablelayout;
    @InjectView(R.id.txt_stall_time_text)
    TextView mtxt_stall_time_text;
    @InjectView(R.id.txt_doLocation_text)
    TextView mtxt_doLocation_text;
    @InjectView(R.id.txt_contraband_text)
    TextView mtxt_contraband_text;
    @InjectView(R.id.txt_description_text)
    TextView mtxt_description_text;
    @InjectView(R.id.txt_linhuievaluation)
    TextView mtxt_linhuievaluation;
    @InjectView(R.id.txt_linhuievaluation_lauout)
    LinearLayout mtxt_linhuievaluation_lauout;
    @InjectView(R.id.txt_linhuievaluation_view)
    View mtxt_linhuievaluation_view;
    @InjectView(R.id.txt_contraband_tablerow)
    LinearLayout mtxt_contraband_tablerow;
    @InjectView(R.id.txt_description_tablerow)
    TableRow mtxt_description_tablerow;
    @InjectView(R.id.evaluation_linearlayout)
    LinearLayout mevaluation_layout;
    @InjectView(R.id.fieldinfo_view_visibility)
    RelativeLayout mfieldinfo_view_visibility;
    @InjectView(R.id.table3)
    RelativeLayout mtable3;
    @InjectView(R.id.total_area_layout)
    LinearLayout mtotal_area_layout;
    @InjectView(R.id.txt_total_area)
    TextView mtxt_total_area;
    @InjectView(R.id.activity_overdue_btn)
    TextView mactivity_overdue_btn;
    @InjectView(R.id.fieldinfo_shopcard_imgbtn)  LinearLayout mfieldinfo_shopcard_imgbtn;

    @InjectView(R.id.txt_facilities)  TextView mtxt_facilities;
    @InjectView(R.id.txt_facilities_layout)  LinearLayout mtxt_facilities_layout;
    @InjectView(R.id.txt_restaurant)  TextView mtxt_restaurant;
    @InjectView(R.id.txt_restaurant_layout)  LinearLayout mtxt_restaurant_layout;
    @InjectView(R.id.txt_park)  TextView mtxt_park;
    @InjectView(R.id.txt_park_layout)  LinearLayout mtxt_park_layout;
    @InjectView(R.id.txt_sales_volume)  TextView mtxt_sales_volume;
    @InjectView(R.id.txt_sales_volume_layout)  LinearLayout mtxt_sales_volume_layout;
    @InjectView(R.id.txt_participation)  TextView mtxt_participation;
    @InjectView(R.id.participation)  TableRow mparticipation;
    @InjectView(R.id.txt_consumption_level)  TextView mtxt_consumption_level;
    @InjectView(R.id.txt_enterprises_type) TextView mtxt_enterprises_type;
    @InjectView(R.id.txt_enterprises_type_layout) LinearLayout mtxt_enterprises_type_layout;
    @InjectView(R.id.txt_number_of_people_facade) TextView mtxt_number_of_people_facade;
    @InjectView(R.id.txt_number_of_people_facade_layout) LinearLayout mtxt_number_of_people_facade_layout;
    @InjectView(R.id.txt_number_of_people_peak_time) TextView mtxt_number_of_people_peak_time;
    @InjectView(R.id.txt_number_of_people_peak_time_layout) LinearLayout mtxt_number_of_people_peak_time_layout;
    @InjectView(R.id.community_name_text) TextView mcommunity_name_text;
    @InjectView(R.id.community_name_layout) LinearLayout mcommunity_name_layout;
    @InjectView(R.id.community_type_text) TextView mcommunity_type_text;
    @InjectView(R.id.community_type_layout) LinearLayout mcommunity_type_layout;
    @InjectView(R.id.community_grade_text) TextView mcommunity_grade_text;
    @InjectView(R.id.community_grade_layout) LinearLayout mcommunity_grade_layout;
    @InjectView(R.id.community_business_district_text) TextView mcommunity_business_district_text;
    @InjectView(R.id.community_business_district_layout) LinearLayout mcommunity_business_district_layout;
    @InjectView(R.id.community_area_text) TextView mcommunity_area_text;
    @InjectView(R.id.community_area_layout) LinearLayout mcommunity_area_layout;
    @InjectView(R.id.community_total_number_of_people_text) TextView mcommunity_total_number_of_people_text;
    @InjectView(R.id.community_total_number_of_people_layout) LinearLayout mcommunity_total_number_of_people_layout;
    @InjectView(R.id.fieldinfo_community_buildyear_text) TextView mfieldinfo_community_buildyear_text;
    @InjectView(R.id.fieldinfo_community_house_price_text) TextView mfieldinfo_community_house_price_text;
    @InjectView(R.id.fieldinfo_community_rent_text) TextView mfieldinfo_community_rent_text;
    @InjectView(R.id.fieldinfo_community_number_of_households_text) TextView mfieldinfo_community_number_of_households_text;
    @InjectView(R.id.fieldinfo_community_occupancy_rate_text) TextView mfieldinfo_community_occupancy_rate_text;
    @InjectView(R.id.fieldinfo_community_property_costs_text) TextView mfieldinfo_community_property_costs_text;
    @InjectView(R.id.fieldinfo_community_number_of_enterprises_text) TextView mfieldinfo_community_number_of_enterprises_text;


    @InjectView(R.id.levelofconsumption)  TableRow mlevelofconsumption;
    @InjectView(R.id.txt_gender_ratio)  TextView mtxt_gender_ratio;
    @InjectView(R.id.gender_ratio)  TableRow mgender_ratio;
    @InjectView(R.id.txt_age_group)  TextView mtxt_age_group;
    @InjectView(R.id.age_group)  TableRow mage_group;
    @InjectView(R.id.txt_occupancy_rate)  TextView mtxt_occupancy_rate;//入住率
    @InjectView(R.id.txt_occupancy_rate_layout)  TableRow mtxt_occupancy_rate_layout;
    @InjectView(R.id.txt_property_requirement)  TextView mtxt_property_requirement;//物业要求
    @InjectView(R.id.txt_property_requirement_layout)  LinearLayout mtxt_property_requirement_layout;
    @InjectView(R.id.all_relative_layout) RelativeLayout mall_relative_layout;
    @InjectView(R.id.fieldinfo_service_imgbtn) LinearLayout mfieldinfo_service_imgbtn;
    @InjectView(R.id.select_time) RelativeLayout mselect_time;
    @InjectView(R.id.field_subsidy_textview)  TextView mfield_subsidy_textview;
    @InjectView(R.id.field_shop_layout)  LinearLayout mfield_shop_layout;
    @InjectView(R.id.field_shop_textview)  TextView mfield_shop_textview;
    @InjectView(R.id.field_order_textview)  TextView mfield_order_textview;
    @InjectView(R.id.fieldinfo_error_recovery_layout)  LinearLayout mfieldinfo_error_recovery_layout;
    @InjectView(R.id.fieldinfo_need_cash_pledge_layout) LinearLayout mfieldinfo_need_cash_pledge_layout;
    @InjectView(R.id.fieldinfo_integral_textview)  TextView mfieldinfo_integral_textview;
    @InjectView(R.id.no_resources_layout)  LinearLayout mno_resources_layout;
    @InjectView(R.id.no_resources_textview)  TextView mno_resources_textview;
    @InjectView(R.id.no_resources_btn)  Button mno_resources_btn;
    @InjectView(R.id.activityinfo_layout) LinearLayout mactivityinfo_layout;
    @InjectView(R.id.activityinfo_start_date_text) TextView mactivityinfo_start_date_text;
    @InjectView(R.id.activityinfo_end_date_text) TextView mactivityinfo_end_date_text;
    @InjectView(R.id.fieldinfo_specifications_price_listview) MyGridview mfieldinfo_specifications_price_listview;
    @InjectView(R.id.fieldinfo_pricelist_layout) LinearLayout mfieldinfo_pricelist_layout;
    @InjectView(R.id.fieldinfo_certification_table_layout) RelativeLayout mfieldinfo_certification_table_layout;
    @InjectView(R.id.fieldinfo_address_layout) LinearLayout mfieldinfo_address_layout;
    @InjectView(R.id.fieldinfo_information_classify_layout_tmp) LinearLayout mfieldinfo_information_classify_layout_tmp;
    @InjectView(R.id.fieldinfo_information_classify_layout) LinearLayout mfieldinfo_information_classify_layout;
    @InjectView(R.id.scrollview) Field_MyScrollview mscrollview;
    @InjectView(R.id.fieldinfo_recommend_type_view) View mfieldinfo_recommend_type_view;
    @InjectView(R.id.pricelist_unfold_text) TextView mpricelist_unfold_text;
    @InjectView(R.id.resource_communityinfo_layout) LinearLayout mresource_communityinfo_layout;
    @InjectView(R.id.resource_fieldinfo_layout) LinearLayout mresource_fieldinfo_layout;
    @InjectView(R.id.resourceinfo_classify_fieldinfo) TextView mresourceinfo_classify_fieldinfo;
    @InjectView(R.id.resourceinfo_classify_communityinfo) TextView mresourceinfo_classify_communityinfo;
    @InjectView(R.id.adv_community_info_layout) LinearLayout madv_community_info_layout;
    @InjectView(R.id.fieldinfo_community_office_building_info_layout) LinearLayout mfieldinfo_community_office_building_info_layout;
    @InjectView(R.id.fieldinfo_community_house_info_layout) LinearLayout mfieldinfo_community_house_info_layout;
    @InjectView(R.id.txt_supermarket_type) TextView mtxt_supermarket_type;
    @InjectView(R.id.txt_supermarket_type_layout) LinearLayout mtxt_supermarket_type_layout;
    @InjectView(R.id.txt_shopping_mall_type) TextView mtxt_shopping_mall_type;
    @InjectView(R.id.txt_shopping_mall_type_layout) LinearLayout mtxt_shopping_mall_type_layout;
    @InjectView(R.id.txt_do_location_text) TextView mtxt_do_location_text;
    @InjectView(R.id.txt_do_location_layout) LinearLayout mtxt_do_location_layout;
    @InjectView(R.id.txt_do_location_textview) TextView mtxt_do_location_textview;

    @InjectView(R.id.fieldinfo_community_number_of_households_textview) TextView mfieldinfo_community_number_of_households_textview;

    @InjectView(R.id.number_of_commercial_layout) LinearLayout mnumber_of_commercial_layout;
    @InjectView(R.id.number_of_commercial_textview) TextView mnumber_of_commercial_textview;
    @InjectView(R.id.housing_attribute_layout) LinearLayout mhousing_attribute_layout;
    @InjectView(R.id.housing_attribute_textview) TextView mhousing_attribute_textview;
    @InjectView(R.id.averagea_ticket_price_layout) LinearLayout maveragea_ticket_price_layout;
    @InjectView(R.id.averagea_ticket_price_textivew) TextView maveragea_ticket_price_textivew;
    @InjectView(R.id.field_deposit_textview) TextView tv_field_deposit;
    @InjectView(R.id.fieldinfo_card_size_tv) TextView tv_fieldinfo_card_size;
    @InjectView(R.id.fieldinfo_banner)
    Banner mFieldInfoBanner;
    @InjectView(R.id.advinfo_no_data_ll)
    RelativeLayout mAdvinfoNoDataLL;
    private ArrayList<String> mPicList = new ArrayList<String>();
    private ArrayList<String> JsonArrayDatelistString = new ArrayList<String>();//选择规格后的所有时间的组合string
    private String getfieldid;//场地id
    private ArrayList<ReviewModel> mReviewList;//评价的list
    private Fieldinfo_ReviewAdapter mFieldEvaluationAdapter;//场地评价的adapter
    private int good_type;//资源类型
    private ArrayList<Date> datelist = new ArrayList<>();//选中的下单或加入购物车的时间数组
    private IWXAPI api;
    private Dialog pw;
    private String ShareTitleStr = "";//分享的标题
    private String SharedescriptionStr = "";//分享的描述
    private String ShareIconStr ="";//分享的图片的url
    private Bitmap ShareBitmap = null;//分享需要的bitmap
    public FieldInfoModel resourceinfolist = new FieldInfoModel();//场地详情的model
    private ArrayList<HashMap<String,String>> dimensions = new ArrayList();//获取到的场地的规格的所有信息的list
    private ArrayList<HashMap<String,String>> dimensions_show_list = new ArrayList();//显示场地的规格的所有信息的list（不包含0元的）

    public String size = "";//选中的规格大小
    public String lease_term_type = "";//选中的规格时间
    public String custom_dimension= "";//选中的特殊规格
    private ArrayList<String> sizelist = new ArrayList<>();//规格中的场地大小list
    private ArrayList<String> custom_dimension_list = new ArrayList<>();//特殊规格的list
    private SimpleDateFormat chooseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ArrayList<String> Closing_dates = new ArrayList<>();//不能选择的规格的时间
    private String weatherliststr = "";//天气的string
    private int mtwcont = 0;//媒体位的个数
    private HashMap<String,String> Term_typesmap = new HashMap<>();//规格中的时间列表
    private PopupWindow gallerypw;
    private ToggleButton toggleButton;//关注按钮
    private RelativeLayout maction_layout_top;//taitlebar的layout
    private HashMap<String,String> term_type_days_map = new HashMap<String, String>();//规格中的时间的周期（例：半年是几天）
    public Date checkedstart_date;//选择规格的开始时间
    public Date checkedend_date;//选择规格的结束时间
    private AdvertisingInfoPricelistAdapter fieldinfoPricelistAdapter;//规格的adapter
    private boolean is_pricelist_unfold = true;
    private Drawable drawable_down;//筛选的条件从高到低
    private Drawable drawable_up;//删选的条件从低到高
    private Dialog newdialog;
    private String share_linkurl = "";//分享的url

    private int classify_info_layout_height;//吸顶控件要吸顶的高度
    private boolean get_classify_info_layout_height;//是否重新获取吸顶控件要吸顶的高度
    private int hide_layout_height;//吸顶控件要隐藏时滑动的高度
    private boolean get_layout_height;//是否重新获取吸顶控件要隐藏时滑动的高度
    private boolean random_info;//是否点击了随机
    private int previousPointEnale;//图片滑动隐藏的上一点的position
    private int days_in_advance = 2;//提前几天预约
    private Dialog zoom_picture_dialog;//详情页预览大图dialog
    private boolean mIsRefreshZoomImageview = true;//详情页变了是否重新获取预览大图
    private List<ImageView> mImageViewList;
    private int minimum_order_quantity = 1;//几天起订
    private double deposit;//押金
    private TextView txt_error_correction_submit;//纠错奖励提交按钮
    private GlideImageLoader mFieldinfoImageLoader;
    private DisplayMetrics mDisplayMetrics;
    private Dialog mReviewZoomPictureDialog;
    private List<ImageView> mDialogImageViewList = new ArrayList<>();
    private boolean mDislogIsRefreshZoomImg = true;
    private int mImageListSize;//图片数量
    private int mNewPosition;//显示的图片position
    private HashMap<Integer,Integer> mWeekLeaseTermMap = new HashMap<Integer,Integer>();
    private HashMap<Integer,String> mWeekLeaseTermPriceMap = new HashMap<Integer,String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising_info);
        ButterKnife.inject(this);
        initview();
    }
    private void initview() {
        mFieldinfoImageLoader = new GlideImageLoader(AdvertisingInfoActivity.this,750,600);
        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        TitleBarUtils.showBackImg(this, true);
        RelativeLayout mbelow_action_layout_top_ToggleButton = (RelativeLayout)findViewById(R.id.below_action_layout_top_ToggleButton);
        toggleButton = (ToggleButton)findViewById(R.id.common_title_bar).findViewById(R.id.below_action_img_top_ToggleButton);
        maction_layout_top = (RelativeLayout)findViewById(R.id.common_title_bar).findViewById(R.id.action_layout_top);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        drawable_down = getResources().getDrawable(R.drawable.ic_dropdown);
        drawable_down.setBounds(0, 0, drawable_down.getMinimumWidth(), drawable_down.getMinimumHeight());
        drawable_up = getResources().getDrawable(R.drawable.ic_dropup);
        drawable_up.setBounds(0, 0, drawable_up.getMinimumWidth(), drawable_up.getMinimumHeight());
        Intent getSearchlistintent = getIntent();
        if (getSearchlistintent.getExtras() != null) {
            getfieldid = getSearchlistintent.getStringExtra("fieldId");
            good_type = getSearchlistintent.getExtras().getInt("good_type");
            madv_community_info_layout.setVisibility(View.VISIBLE);
            TitleBarUtils.setTitleText(this, getResources().getString(R.string.fieldinfo_advertising_title_text));
            mselecttime_text.setText(getResources().getString(R.string.advertisinginfo_diolog_choosetspecifications));
        }
        mbelow_action_layout_top_ToggleButton.setVisibility(View.VISIBLE);
        mreview_listview.setFocusable(false);
        mreview_listview.setVisibility(View.GONE);
        mtable3.setVisibility(View.GONE);
        mno_resources_layout.setVisibility(View.GONE);
        maction_layout_top.setVisibility(View.VISIBLE);
        toggleButton.setVisibility(View.GONE);
        int width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        int height = width * 480 / 600;
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mFieldInfoBanner.getLayoutParams();
        //设置图片显示高度
        linearParams.height = height;
        linearParams.width = width;
        mFieldInfoBanner.setLayoutParams(linearParams);
        mfieldinfo_service_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(AdvertisingInfoActivity.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();

            }
        });
        mfieldinfo_shopcard_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartitemsintent = new Intent(AdvertisingInfoActivity.this, ResourcesCartItemsActivity.class);
                startActivity(cartitemsintent);
            }
        });
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
        mfieldinfo_need_cash_pledge_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        show_paysuccess_PopupWindow(2);
                    }
                },10);
            }
        });
        mpricelist_unfold_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_pricelist_unfold) {
                    mpricelist_unfold_text.setText(getResources().getString(R.string.fieldinfo_pricelist_unfold_text));
                    is_pricelist_unfold = false;
                    fieldinfoPricelistAdapter.notifyDataSetChanged(is_pricelist_unfold, dimensions_show_list);
                    mpricelist_unfold_text.setCompoundDrawables(null, null, drawable_down, null);
                } else {
                    is_pricelist_unfold = true;
                    mpricelist_unfold_text.setText(getResources().getString(R.string.fieldinfo_pricelist_pack_up_text));
                    fieldinfoPricelistAdapter.notifyDataSetChanged(is_pricelist_unfold, dimensions_show_list);
                    mpricelist_unfold_text.setCompoundDrawables(null, null, drawable_up, null);
                    mtxt_field_price.setEnabled(true);
                }
                get_layout_height = false;
                hide_layout_height = 0;
            }
        });
        mAdvinfoNoDataLL.setVisibility(View.VISIBLE);
        //浏览记录
        if (LoginManager.isLogin()) {
            String parameter = "/"+ getfieldid;
            LoginMvpModel.sendBrowseHistories("adv_detail",parameter,LoginManager.getTrackcityid());
        }
        initdata();
    }
    private void initdata() {
        if (getfieldid != null) {
            if (getfieldid.equals("random")) {
                FieldApi.getSellResInfo(MyAsyncHttpClient.MyAsyncHttpClient7(), getfieldidHandler,"");
            } else {
                FieldApi.getSellResInfo(MyAsyncHttpClient.MyAsyncHttpClient7(), getfieldidHandler, getfieldid);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResources().getString(R.string.adv_info_activity_name_str));
        MobclickAgent.onResume(this);
        if (LoginManager.isLogin()) {
            getCardSize();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResources().getString(R.string.adv_info_activity_name_str));
        MobclickAgent.onPause(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ShareBitmap != null) {
            ShareBitmap.recycle();
        }
        if (pw!= null && pw.isShowing()) {
            pw.dismiss();
        }
        if (gallerypw!= null && gallerypw.isShowing()) {
            gallerypw.dismiss();
        }

    }

    private LinhuiAsyncHttpResponseHandler FavoriteslistitemHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            if (toggleButton.isChecked()) {
                MessageUtils.showToast(getResources().getString(R.string.myselfinfo_addfocuson_txt));
            } else {
                MessageUtils.showToast(getResources().getString(R.string.myselfinfo_deletefocuson_txt));
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            if (!superresult) {
                MessageUtils.showToast(getContext(), error.getMessage());
            }
            if (toggleButton.isChecked()) {
                toggleButton.setChecked(false);
            } else {
                toggleButton.setChecked(true);
            }
            checkAccess_new(error);
        }
    };

    private LinhuiAsyncHttpResponseHandler getfieldidHandler = new LinhuiAsyncHttpResponseHandler(FieldInfoModel.class,false) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            resourceinfolist = (FieldInfoModel)data;
            if (data != null) {
                mAdvinfoNoDataLL.setVisibility(View.GONE);
                mtable3.setVisibility(View.VISIBLE);
                mscrollview.fullScroll(ScrollView.FOCUS_UP);
                mresourceinfo_classify_fieldinfo.setTextColor(getResources().getColor(R.color.default_bluebg));
                mresourceinfo_classify_communityinfo.setTextColor(getResources().getColor(R.color.register_edit_color));
                mresource_communityinfo_layout.setVisibility(View.GONE);
                mresource_fieldinfo_layout.setVisibility(View.VISIBLE);
                mresourceinfo_classify_fieldinfo.setBackgroundColor(getResources().getColor(R.color.white));
                mresourceinfo_classify_communityinfo.setBackgroundColor(getResources().getColor(R.color.tab_gb));
                if (resourceinfolist.getSize().get(0).getResource().get(0).getDays_in_advance() != null &&
                        resourceinfolist.getSize().get(0).getResource().get(0).getDays_in_advance() > -1) {
                    days_in_advance = resourceinfolist.getSize().get(0).getResource().get(0).getDays_in_advance();
                }
                if (resourceinfolist.getSize().get(0).getResource().get(0).getDeposit() != null &&
                        resourceinfolist.getSize().get(0).getResource().get(0).getDeposit() > 0) {
                    deposit = resourceinfolist.getSize().get(0).getResource().get(0).getDeposit();
                    mfieldinfo_need_cash_pledge_layout.setVisibility(View.VISIBLE);
                    tv_field_deposit.setVisibility(View.VISIBLE);
                    tv_field_deposit.setText(getResources().getString(R.string.orderconfirm_deposit_text) +
                    Constants.getdoublepricestring(resourceinfolist.getSize().get(0).getResource().get(0).getDeposit(),1) +
                                    getResources().getString(R.string.term_types_unit_txt));
                } else {
                    mfieldinfo_need_cash_pledge_layout.setVisibility(View.GONE);
                }
                if (resourceinfolist.getSize().get(0).getResource().get(0).getMinimum_booking_days() != null && resourceinfolist.getSize().get(0).getResource().get(0).getMinimum_booking_days() > 0) {
                    minimum_order_quantity = resourceinfolist.getSize().get(0).getResource().get(0).getMinimum_booking_days();
                    if (minimum_order_quantity > 1) {
                        mfield_shop_layout.setVisibility(View.GONE);
                    }
                }
                getfieldid = String.valueOf(resourceinfolist.getSize().get(0).getResource().get(0).getId());
                if (resourceinfolist.getSize().get(0).getResource().get(0).getFavorite_status() == 1) {
                    toggleButton.setChecked(true);
                } else {
                    toggleButton.setChecked(false);
                }
                //2017/10/25 规格
                if (resourceinfolist.getSize() != null && resourceinfolist.getSize().size() > 0) {
                    ArrayList<Integer> mTermTypeList = new ArrayList<>();
                    for (int i = 0; i < resourceinfolist.getSize().size(); i++) {
                        if (resourceinfolist.getSize().get(i).getDimension() != null) {
                            HashMap<String,String> map = new HashMap<>();
                            if (resourceinfolist.getSize().get(i).getDimension().getSize() != null) {
                                map.put("size",resourceinfolist.getSize().get(i).getDimension().getSize());
                                if (!sizelist.contains(resourceinfolist.getSize().get(i).getDimension().getSize())) {
                                    sizelist.add(resourceinfolist.getSize().get(i).getDimension().getSize());
                                }
                            }
                            if (resourceinfolist.getSize().get(i).getDimension().getLease_term_type() != null) {
                                map.put("lease_term_type_name",resourceinfolist.getSize().get(i).getDimension().getLease_term_type());
                                if (resourceinfolist.getSize().get(i).getDimension().getLease_term_type().equals(
                                        getResources().getString(R.string.groupbooding_list_item_residue_tiem_day_str))) {
                                    map.put("lease_term_type_id","-1");
                                    for (int j = 0; j < resourceinfolist.getSize().get(i).getResource().get(0).getSelling_resource_price().size(); j++) {
                                        FieldInfoSellResourcePriceModel model = resourceinfolist.getSize().get(i).getResource().get(0).getSelling_resource_price().get(j);
                                        if (model != null &&
                                                model.getLease_term_type_id() != null &&
                                                model.getPrice() != null) {
                                            mWeekLeaseTermPriceMap.put(model.getLease_term_type_id(),String.valueOf(model.getPrice()));
                                            if (model.getLease_term_type() != null &&
                                                    model.getLease_term_type().getId() > 0 &&
                                                    model.getLease_term_type().getName() != null) {
                                                if (model.getLease_term_type().getName().equals("Mon")) {
                                                    mWeekLeaseTermMap.put(0, model.getLease_term_type().getId());
                                                } else if (model.getLease_term_type().getName().equals("Tue")) {
                                                    mWeekLeaseTermMap.put(1, model.getLease_term_type().getId());
                                                } else if (model.getLease_term_type().getName().equals("Wed")) {
                                                    mWeekLeaseTermMap.put(2, model.getLease_term_type().getId());
                                                } else if (model.getLease_term_type().getName().equals("Thu")) {
                                                    mWeekLeaseTermMap.put(3, model.getLease_term_type().getId());
                                                } else if (model.getLease_term_type().getName().equals("Fri")) {
                                                    mWeekLeaseTermMap.put(4, model.getLease_term_type().getId());
                                                } else if (model.getLease_term_type().getName().equals("Sat")) {
                                                    mWeekLeaseTermMap.put(5, model.getLease_term_type().getId());
                                                } else if (model.getLease_term_type().getName().equals("Sun")) {
                                                    mWeekLeaseTermMap.put(6, model.getLease_term_type().getId());
                                                }
                                            }
                                        }
                                    }

                                } else {
                                    map.put("lease_term_type_id",String.valueOf(resourceinfolist.getSize().get(i).getResource().get(0).getSelling_resource_price().get(0).getLease_term_type_id()));

                                }
                                if (resourceinfolist.getSize().get(i).getResource().get(0).getSelling_resource_price().get(0) != null &&
                                        resourceinfolist.getSize().get(i).getResource().get(0).getSelling_resource_price().get(0).getLease_term_type_id() != null) {
                                    if (!mTermTypeList.contains(
                                            resourceinfolist.getSize().get(i).getResource().get(0).getSelling_resource_price().get(0).getLease_term_type_id())) {
                                        if (resourceinfolist.getSize().get(i).getDimension().getLease_term_type().equals(
                                                getResources().getString(R.string.groupbooding_list_item_residue_tiem_day_str))) {
                                            mTermTypeList.add(-1);
                                        } else {
                                            mTermTypeList.add(resourceinfolist.getSize().get(i).getResource().get(0).getSelling_resource_price().get(0).getLease_term_type_id());
                                        }
                                        if (resourceinfolist.getSize().get(i).getResource().get(0).getSelling_resource_price().get(0).getLease_term_type().getPeriod() > 0) {
                                            if (resourceinfolist.getSize().get(i).getDimension().getLease_term_type().equals(
                                                    getResources().getString(R.string.groupbooding_list_item_residue_tiem_day_str))) {
                                                term_type_days_map.put("-1",
                                                        String.valueOf(resourceinfolist.getSize().get(i).getResource().get(0).getSelling_resource_price().get(0).getLease_term_type().getPeriod()));
                                            } else {
                                                term_type_days_map.put(String.valueOf(resourceinfolist.getSize().get(i).getResource().get(0).getSelling_resource_price().get(0).getLease_term_type_id()),
                                                        String.valueOf(resourceinfolist.getSize().get(i).getResource().get(0).getSelling_resource_price().get(0).getLease_term_type().getPeriod()));
                                            }
                                        }
                                        if (resourceinfolist.getSize().get(i).getDimension().getLease_term_type() != null) {
                                            if (resourceinfolist.getSize().get(i).getDimension().getLease_term_type().equals(
                                                    getResources().getString(R.string.groupbooding_list_item_residue_tiem_day_str))) {
                                                Term_typesmap.put("-1",
                                                        resourceinfolist.getSize().get(i).getDimension().getLease_term_type());
                                            } else {
                                                Term_typesmap.put(String.valueOf(resourceinfolist.getSize().get(i).getResource().get(0).getSelling_resource_price().get(0).getLease_term_type_id()),
                                                        resourceinfolist.getSize().get(i).getDimension().getLease_term_type());
                                            }
                                        }
                                    }
                                }
                            }
                            if (resourceinfolist.getSize().get(i).getResource().get(0) != null &&
                                    resourceinfolist.getSize().get(i).getResource().get(0).getSelling_resource_price().get(0) != null &&
                                    resourceinfolist.getSize().get(i).getResource().get(0).getSelling_resource_price().get(0).getCount_of_frame() != null &&
                                    resourceinfolist.getSize().get(i).getResource().get(0).getSelling_resource_price().get(0).getCount_of_frame() > 0 ) {
                                map.put("count_of_frame",String.valueOf(resourceinfolist.getSize().get(i).getResource().get(0).getSelling_resource_price().get(0).getCount_of_frame()));
                            } else {
                                map.put("count_of_frame","0");
                            }
                            if (resourceinfolist.getSize().get(i).getResource().get(0).getMin_after_subsidy() != null) {
                                map.put("price",Constants.getdoublepricestring(resourceinfolist.getSize().get(i).getResource().get(0).getMin_after_subsidy(),1));
                            }
                            if (resourceinfolist.getSize().get(i).getDimension().getCustom_dimension() != null) {
                                map.put("custom_dimension",resourceinfolist.getSize().get(i).getDimension().getCustom_dimension());
                                if (resourceinfolist.getSize().get(i).getDimension().getCustom_dimension().length() > 0 &&
                                        !custom_dimension_list.contains(resourceinfolist.getSize().get(i).getDimension().getCustom_dimension())) {
                                    custom_dimension_list.add(resourceinfolist.getSize().get(i).getDimension().getCustom_dimension());
                                }
                            }
                            dimensions.add(map);
                            dimensions_show_list.add(map);
                        }
                    }
                    if (dimensions_show_list.size() > 6) {
                        is_pricelist_unfold = false;
                        mpricelist_unfold_text.setVisibility(View.VISIBLE);
                    } else {
                        mpricelist_unfold_text.setVisibility(View.GONE);
                    }
                    fieldinfoPricelistAdapter = new AdvertisingInfoPricelistAdapter(AdvertisingInfoActivity.this, AdvertisingInfoActivity.this,dimensions_show_list);
                    mfieldinfo_specifications_price_listview.setAdapter(fieldinfoPricelistAdapter);
                    fieldinfoPricelistAdapter.notifyDataSetChanged(is_pricelist_unfold, dimensions_show_list);
                    mfieldinfo_pricelist_layout.setVisibility(View.VISIBLE);
                }
                if (Closing_dates != null) {
                    Closing_dates.clear();
                }
                if (resourceinfolist.getPhysical_resource().getCalendars() != null) {
                    if (resourceinfolist.getPhysical_resource().getCalendars().size() > 0) {
                        for (int i = 0; i < resourceinfolist.getPhysical_resource().getCalendars().size(); i++) {
                            if (resourceinfolist.getPhysical_resource().getCalendars().get(i).get("exp_date") != null &&
                                    resourceinfolist.getPhysical_resource().getCalendars().get(i).get("exp_date").length() > 0) {
                                Closing_dates.add(resourceinfolist.getPhysical_resource().getCalendars().get(i).get("exp_date"));
                            }
                        }
                    }
                }
                if (resourceinfolist.getWeather() != null) {
                    if (resourceinfolist.getWeather().size() > 0) {
                        weatherliststr = resourceinfolist.getWeather().toString();
                    }
                }
                if (mPicList != null) {
                    mPicList.clear();
                }
                if (resourceinfolist.getPhysical_resource().getPhysical_resource_imgs()!= null) {
                    if (resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().size() > 0) {
                        for (int i = 0; i < resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().size(); i++) {
                            if (resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().get(i).get("pic_url") != null &&
                                    resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().get(i).get("pic_url").length() > 0) {
                                mPicList.add(resourceinfolist.getPhysical_resource().getPhysical_resource_imgs().get(i).get("pic_url").toString() + com.linhuiba.linhuipublic.config.Config.Linhui_Max_Watermark);
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
                                    if (zoom_picture_dialog != null && zoom_picture_dialog.isShowing()) {
                                        zoom_picture_dialog.dismiss();
                                    }
                                    show_zoom_picture_dialog((position-1)%mPicList.size());
                                }
                            });
                            mFieldInfoBanner.start();
                        }

                    }
                }
                if (good_type == 2) {
                    if (resourceinfolist.getPhysical_resource().getAd_type() != null && resourceinfolist.getPhysical_resource().getAd_type().getDisplay_name() != null &&
                            resourceinfolist.getPhysical_resource().getAd_type().getDisplay_name().length() > 0) {
                        mtxt_fieldtitle_layout.setVisibility(View.VISIBLE);
                        mtxt_fieldtitle.setText(resourceinfolist.getPhysical_resource().getAd_type().getDisplay_name());
                    } else {
                        mtxt_fieldtitle_layout.setVisibility(View.GONE);
                    }
                }
                if (resourceinfolist.getPhysical_resource().getName() != null) {
                    ShareTitleStr = getResources().getString(R.string.fieldinfo_share_text)+resourceinfolist.getPhysical_resource().getName().toString()+
                            getResources().getString(R.string.fieldinfo_share_linhuiba_mark_text);
                    mtxt_field_description.setText(resourceinfolist.getPhysical_resource().getName().toString());
                }
                int pic_light[] = new int[5];
                int pic_dark[] = new int[5];
                final ImageView img[] = new ImageView[5];
                img[0] = (ImageView) findViewById(R.id.score_img_one);
                img[1] = (ImageView) findViewById(R.id.score_img_two);
                img[2] = (ImageView) findViewById(R.id.score_img_three);
                img[3] = (ImageView) findViewById(R.id.score_img_four);
                img[4] = (ImageView) findViewById(R.id.score_img_five);
                for (int i = 0; i < 5; i++) {
                    pic_light[i] = R.drawable.ic_xingxing_click;
                    pic_dark[i] = R.drawable.ic_xingxing_normal;
                }
                //2017/10/25 评价几颗星
                if (resourceinfolist.getPhysical_resource().getAverage_score() != null) {
                    for (int i = 0; i < 5; i++) {
                        if (i < resourceinfolist.getPhysical_resource().getAverage_score()) {
                            img[i].setImageResource(pic_light[i]);
                        } else {
                            img[i].setImageResource(pic_dark[i]);
                        }
                    }
                }
                if (resourceinfolist.getPhysical_resource().getNumber_of_order() != null) {
                    mtxt_transaction_success.setText(getResources().getString(R.string.search_numberofpaid_first_str) + resourceinfolist.getPhysical_resource().getNumber_of_order().toString());
                }
                if (resourceinfolist.getPhysical_resource().getCount_of_reviews() != null) {
                    mtxt_evaluation.setText(getResources().getString(R.string.fieldinfo_review_count_text) + resourceinfolist.getPhysical_resource().getCount_of_reviews().toString());
                }
                if (resourceinfolist.getPhysical_resource().getCount_of_reviews() != null) {
                    mreview_number_txt.setText("(" + resourceinfolist.getPhysical_resource().getCount_of_reviews() + ")");
                }
                if (resourceinfolist.getPhysical_resource().getCount_of_reviews() != null) {
                    if (resourceinfolist.getPhysical_resource().getCount_of_reviews().toString().equals("0")) {
                        mreview_listview.setVisibility(View.GONE);
                    }
                }

                if (resourceinfolist.getPhysical_resource().getTotal_area() != null) {
                    if (resourceinfolist.getPhysical_resource().getTotal_area() > 0) {
                        mtotal_area_layout.setVisibility(View.VISIBLE);
                        mtxt_total_area.setText(resourceinfolist.getPhysical_resource().getTotal_area()+"m²");
                    }
                }
                //2018/1/12 广告补贴
                if (resourceinfolist.getSize().get(0).getResource().get(0).getPrice() != null &&
                        resourceinfolist.getSize().get(0).getResource().get(0).getPrice() > 0) {
                    mtxt_field_price_unit_textview.setVisibility(View.VISIBLE);
                    mtxt_field_price.setText(Constants.getpricestring(String.valueOf(resourceinfolist.getSize().get(0).getResource().get(0).getPrice()), 0.01));
                    mfieldinfo_integral_textview.setText(getResources().getString(R.string.fieldinfo_refunt_remindtext)+
                            Constants.getfieldinfo_integral(String.valueOf(resourceinfolist.getSize().get(0).getResource().get(0).getPrice()), 0.01)+getResources().getString(R.string.fieldinfo_integral_text));
                    if ((resourceinfolist.getSize().get(0).getResource().get(0).getPrice() -
                            resourceinfolist.getSize().get(0).getResource().get(0).getMin_after_subsidy() > 0)) {
                        mfield_subsidy_textview.setVisibility(View.VISIBLE);
                        mfield_subsidy_textview.setText(getResources().getString(R.string.order_listitem_subsidy_text)+
                                getResources().getString(R.string.order_listitem_price_unit_text)
                                +Constants.getpricestring(String.valueOf(
                                resourceinfolist.getSize().get(0).getResource().get(0).getPrice() -
                                        resourceinfolist.getSize().get(0).getResource().get(0).getMin_after_subsidy()), 0.01)+
                                getResources().getString(R.string.term_types_unit_txt));
                    } else {
                        mfield_subsidy_textview.setVisibility(View.GONE);
                    }
                } else {
                    mtxt_field_price_unit_textview.setVisibility(View.GONE);
                }

                if (resourceinfolist.getPhysical_resource().getCommunity().getDetailed_address() != null && resourceinfolist.getPhysical_resource().getCommunity().getDetailed_address().length() > 0) {
                    String address = "";
                    if (resourceinfolist.getPhysical_resource().getCommunity().getDistrict() != null &&
                            resourceinfolist.getPhysical_resource().getCommunity().getDistrict().getName() != null &&
                            resourceinfolist.getPhysical_resource().getCommunity().getDistrict().getName().length() > 0) {
                        address = resourceinfolist.getPhysical_resource().getCommunity().getDistrict().getName() +
                                resourceinfolist.getPhysical_resource().getCommunity().getDetailed_address();
                    } else {
                        address = resourceinfolist.getPhysical_resource().getCommunity().getDetailed_address();
                    }
                    SharedescriptionStr = "地址：" +address;
                    if (mtxt_field_price.getText().toString().trim().length() > 0) {
                        SharedescriptionStr ="地址：" + address+"\n" + "价格："+ mtxt_field_price.getText().toString();
                    }
                    mtxt_address.setText(address);
                }
                if (good_type == 2) {
                    if (resourceinfolist.getPhysical_resource().getCommunity().getOccupancy_rate() != null &&
                            resourceinfolist.getPhysical_resource().getCommunity().getOccupancy_rate().length() > 0 ) {
                        mtxt_occupancy_rate.setText(String.valueOf(resourceinfolist.getPhysical_resource().getCommunity().getOccupancy_rate()));
                    }
                    mfieldinfo_information_classify_layout.setVisibility(View.GONE);
                    mfieldinfo_information_classify_layout_tmp.setVisibility(View.GONE);
                    mfieldinfo_doLocation_layout.setVisibility(View.GONE);
                    mfieldinfo_address_layout.setVisibility(View.GONE);
                    mevaluation_layout.setVisibility(View.GONE);
                    mtablelayout.setVisibility(View.GONE);
                    mtotal_area_layout.setVisibility(View.GONE);
                    mtxt_stall_time_text.setText("时        间：");
                    if (resourceinfolist.getPhysical_resource().getDo_begin() != null &&
                            resourceinfolist.getPhysical_resource().getDo_finish() != null &&
                            resourceinfolist.getPhysical_resource().getDo_begin().length() > 0 &&
                            resourceinfolist.getPhysical_resource().getDo_finish().length() > 0) {
                        mtxt_stall_time.setText(resourceinfolist.getPhysical_resource().getDo_begin() + "  --  " +resourceinfolist.getPhysical_resource().getDo_finish());
                    }
                    mtxt_do_location_textview.setText("位        置：");
                    if (resourceinfolist.getPhysical_resource().getDoLocation() != null) {
                        mtxt_do_location_text.setText(resourceinfolist.getPhysical_resource().getDoLocation());
                    }
                    mtxt_contraband_tablerow.setVisibility(View.GONE);
                    if (resourceinfolist.getPhysical_resource().getDescription() != null) {
                        if (resourceinfolist.getPhysical_resource().getDescription().length() > 0) {
                            mtxt_description_tablerow.setVisibility(View.VISIBLE);
                            mtxt_description_text.setText("描        述：");
                            mtxt_description.setText(resourceinfolist.getPhysical_resource().getDescription());
                        } else {
                            mtxt_description_tablerow.setVisibility(View.GONE);
                        }
                    } else {
                        mtxt_description_tablerow.setVisibility(View.GONE);
                    }
                }
                //判断新加字段是否有数据，无数据隐藏
                if (resourceinfolist.getPhysical_resource().getNumber_of_people() != null) {
                    if (resourceinfolist.getPhysical_resource().getNumber_of_people() == 0) {
                        mtxt_number_of_people.setText(getResources().getString(R.string.fieldinfo_no_parameter_message));
                    } else {
                        mtxt_number_of_people.setText(resourceinfolist.getPhysical_resource().getNumber_of_people().toString());
                    }
                } else {
                    mtxt_number_of_people_layout.setVisibility(View.GONE);
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getNumber_of_seat() != null) {
                    mtxt_restaurant_layout.setVisibility(View.VISIBLE);
                    mtxt_restaurant.setText(getResources().getString(R.string.fieldinfo_restaurant_text) +
                            String.valueOf(resourceinfolist.getPhysical_resource().getCommunity().getNumber_of_seat()) +
                            getResources().getString(R.string.fieldinfo_restaurant_unit_text));
                } else {
                    mtxt_restaurant_layout.setVisibility(View.GONE);
                }
                if (resourceinfolist.getPhysical_resource().getCommunity().getFacilities() != null) {
                    if (resourceinfolist.getPhysical_resource().getCommunity().getFacilities().length() >0) {
                        mtxt_facilities_layout.setVisibility(View.VISIBLE);
                        mtxt_facilities.setText(resourceinfolist.getPhysical_resource().getCommunity().getFacilities().toString());
                    } else {
                        mtxt_facilities_layout.setVisibility(View.GONE);
                    }
                } else {
                    mtxt_facilities_layout.setVisibility(View.GONE);
                }

                if (resourceinfolist.getPhysical_resource().getPeak_time() != null &&
                        resourceinfolist.getPhysical_resource().getPeak_time().getDisplay_name() != null &&
                        resourceinfolist.getPhysical_resource().getPeak_time().getDisplay_name().length() > 0) {
                    mtxt_number_of_people_peak_time_layout.setVisibility(View.VISIBLE);
                    mtxt_number_of_people_peak_time.setText(resourceinfolist.getPhysical_resource().getPeak_time().getDisplay_name());
                } else {
                    mtxt_number_of_people_peak_time_layout.setVisibility(View.GONE);
                }
                if (resourceinfolist.getPhysical_resource().getFacade() != null &&
                        resourceinfolist.getPhysical_resource().getFacade() > 0) {
                    mtxt_number_of_people_facade_layout.setVisibility(View.VISIBLE);
                    mtxt_number_of_people_facade.setText(String.valueOf(resourceinfolist.getPhysical_resource().getFacade()) +"面");
                } else {
                    mtxt_number_of_people_facade_layout.setVisibility(View.GONE);
                }
                //企业类型
                if (resourceinfolist.getPhysical_resource().getCommunity().getEnterprise_type() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getEnterprise_type().getDisplay_name() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getEnterprise_type().getDisplay_name().length() > 0) {
                    mtxt_enterprises_type_layout.setVisibility(View.VISIBLE);
                    mtxt_enterprises_type.setText(resourceinfolist.getPhysical_resource().getCommunity().getEnterprise_type().getDisplay_name());
                } else {
                    mtxt_enterprises_type_layout.setVisibility(View.GONE);
                }
                //超市定位

                if (resourceinfolist.getPhysical_resource().getCommunity().getSupermarket_type() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getSupermarket_type().getDisplay_name() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getSupermarket_type().getDisplay_name().length() > 0) {
                    mtxt_supermarket_type_layout.setVisibility(View.VISIBLE);
                    mtxt_supermarket_type.setText(resourceinfolist.getPhysical_resource().getCommunity().getSupermarket_type().getDisplay_name());
                } else {
                    mtxt_supermarket_type_layout.setVisibility(View.GONE);
                }
                //商场定位
                if (resourceinfolist.getPhysical_resource().getCommunity().getShopping_mall_type() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getShopping_mall_type().getDisplay_name() != null &&
                        resourceinfolist.getPhysical_resource().getCommunity().getShopping_mall_type().getDisplay_name().length() > 0) {
                    mtxt_shopping_mall_type_layout.setVisibility(View.VISIBLE);
                    mtxt_shopping_mall_type.setText(resourceinfolist.getPhysical_resource().getCommunity().getShopping_mall_type().getDisplay_name());
                } else {
                    mtxt_shopping_mall_type_layout.setVisibility(View.GONE);
                }

                if (resourceinfolist.getPhysical_resource().getCompany_comment() != null) {
                    if (resourceinfolist.getPhysical_resource().getCompany_comment().length() >0) {
                        mtxt_linhuievaluation.setText(resourceinfolist.getPhysical_resource().getCompany_comment().toString());
                    } else {
                        mtxt_linhuievaluation_lauout.setVisibility(View.GONE);
                        mtxt_linhuievaluation_view.setVisibility(View.GONE);
                    }
                } else {
                    mtxt_linhuievaluation_lauout.setVisibility(View.GONE);
                    mtxt_linhuievaluation_view.setVisibility(View.GONE);
                }
                //车位费
                if (resourceinfolist.getPhysical_resource().getHas_carport() == 1) {
                    mtxt_park.setText(getResources().getString(R.string.fieldinfo_carport_text));
                    if (resourceinfolist.getPhysical_resource().getCharging_standard() != null && resourceinfolist.getPhysical_resource().getCharging_standard().length() > 0) {
                        mtxt_park.setText(getResources().getString(R.string.fieldinfo_carport_text) +
                                resourceinfolist.getPhysical_resource().getCharging_standard());
                    }

                } else {
                    mtxt_park_layout.setVisibility(View.GONE);
                }
                if (resourceinfolist.getPhysical_resource().getParticipation_level() != null &&
                        resourceinfolist.getPhysical_resource().getParticipation_level().getDisplay_name() != null &&
                        resourceinfolist.getPhysical_resource().getParticipation_level().getDisplay_name().length() > 0) {
                    mtxt_fieldtitle_layout.setVisibility(View.VISIBLE);
                    mtxt_participation.setText(resourceinfolist.getPhysical_resource().getParticipation_level().getDisplay_name());
                } else {
                    mparticipation.setVisibility(View.GONE);
                }
                if (resourceinfolist.getPhysical_resource().getAge_level() != null &&
                        resourceinfolist.getPhysical_resource().getAge_level().getDisplay_name() != null &&
                        resourceinfolist.getPhysical_resource().getAge_level().getDisplay_name().length() > 0) {
                    mtxt_fieldtitle_layout.setVisibility(View.VISIBLE);
                    mtxt_age_group.setText(resourceinfolist.getPhysical_resource().getAge_level().getDisplay_name());
                } else {
                    mage_group.setVisibility(View.GONE);
                }
                if (resourceinfolist.getPhysical_resource().getMale_proportion() != null &&
                        resourceinfolist.getPhysical_resource().getMale_proportion() > -1) {
                    mtxt_gender_ratio.setText(getResources().getString(R.string.fieldinfo_man_proportion_text) +
                    String.valueOf(resourceinfolist.getPhysical_resource().getMale_proportion()) + getResources().getString(R.string.fieldinfo_man_proportion_unit_text) +
                    "," +getResources().getString(R.string.fieldinfo_woman_proportion_text) +
                            String.valueOf(100 - resourceinfolist.getPhysical_resource().getMale_proportion()) +getResources().getString(R.string.fieldinfo_man_proportion_unit_text));

                } else {
                    mgender_ratio.setVisibility(View.GONE);
                }

                if (resourceinfolist.getPhysical_resource().getInformation() != null &&
                        resourceinfolist.getPhysical_resource().getInformation().length() > 0) {
                    mtxt_sales_volume.setText(resourceinfolist.getPhysical_resource().getInformation());
                }  else {
                    mtxt_sales_volume_layout.setVisibility(View.GONE);
                }

                if ((resourceinfolist.getPhysical_resource().getRequirements() != null && resourceinfolist.getPhysical_resource().getRequirements().size() > 0) ||
                        (resourceinfolist.getPhysical_resource().getProperty_requirement() != null && resourceinfolist.getPhysical_resource().getProperty_requirement().length() > 0)) {
                    mtxt_property_requirement_layout.setVisibility(View.VISIBLE);
                    String property_requirement = "";
                    if (resourceinfolist.getPhysical_resource().getRequirements() != null && resourceinfolist.getPhysical_resource().getRequirements().size() > 0) {
                        for (int i = 0; i < resourceinfolist.getPhysical_resource().getRequirements().size(); i++) {
                            if (resourceinfolist.getPhysical_resource().getRequirements().get(i).getDisplay_name() != null &&
                                    resourceinfolist.getPhysical_resource().getRequirements().get(i).getDisplay_name().length() > 0) {
                                if (i != 0) {
                                    property_requirement = property_requirement + "、" + resourceinfolist.getPhysical_resource().getRequirements().get(i).getDisplay_name();
                                } else {
                                    property_requirement = property_requirement + resourceinfolist.getPhysical_resource().getRequirements().get(i).getDisplay_name();
                                }
                            }
                        }
                    }
                    if (resourceinfolist.getPhysical_resource().getProperty_requirement() != null && resourceinfolist.getPhysical_resource().getProperty_requirement().length() > 0) {
                        if (resourceinfolist.getPhysical_resource().getRequirements() != null && resourceinfolist.getPhysical_resource().getRequirements().size() > 0) {
                            property_requirement = property_requirement + "、" + resourceinfolist.getPhysical_resource().getProperty_requirement();
                        } else {
                            property_requirement = property_requirement + resourceinfolist.getPhysical_resource().getProperty_requirement();
                        }
                    }
                    mtxt_property_requirement.setText(property_requirement);
                } else {
                    mtxt_property_requirement_layout.setVisibility(View.GONE);
                }
                if (resourceinfolist.getPhysical_resource().getConsumption_level() != null &&
                        resourceinfolist.getPhysical_resource().getConsumption_level().getDisplay_name() != null &&
                        resourceinfolist.getPhysical_resource().getConsumption_level().getDisplay_name().length() > 0) {
                    mtxt_fieldtitle_layout.setVisibility(View.VISIBLE);
                    mtxt_consumption_level.setText(resourceinfolist.getPhysical_resource().getConsumption_level().getDisplay_name());
                } else {
                    mlevelofconsumption.setVisibility(View.GONE);
                }

            } else {
                mtable3.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            mAdvinfoNoDataLL.setVisibility(View.VISIBLE);
            mtable3.setVisibility(View.GONE);
            if (isNoResources(error,-204) || isNoResources(error,-4)) {
                mno_resources_layout.setVisibility(View.VISIBLE);
                maction_layout_top.setVisibility(View.GONE);
                toggleButton.setVisibility(View.GONE);
                mno_resources_textview.setText(getResources().getString(R.string.fieldinfo_no_resources_one_text) +
                        getResources().getString(R.string.righttimg_text)+
                        getResources().getString(R.string.fieldinfo_no_resources_two_text));
                mno_resources_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AdvertisingInfoActivity.this.finish();
                        Intent maintabintent = new Intent(AdvertisingInfoActivity.this, MainTabActivity.class);
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
    };
    @OnClick({
            R.id.field_order_layout,
            R.id.select_time,
            R.id.evaluation_layout,
            R.id.field_shop_layout,
            R.id.fieldinfo_address_layout,
            R.id.resourceinfo_classify_fieldinfo,
            R.id.resourceinfo_classify_communityinfo
    })
    public void fieldinfoclick(View view) {
        switch (view.getId()) {
            case R.id.field_order_layout:
                if (LoginManager.isLogin()) {
                    if (lease_term_type.length() > 0 && size.length() > 0 && (
                            mtwcont > 0)) {
                        if (good_type == 2) {
                            Intent OrderConfirm = new Intent(this,OrderConfirmActivity.class);
                            OrderConfirm.putExtra("ordertype",1);
                            OrderConfirm.putExtra("submitorderlist", (Serializable) getadvertisingsubmitorderlist());
                            OrderConfirm.putExtra("deposit",deposit);
                            OrderConfirm.putExtra("is_adv",1);
                            startActivity(OrderConfirm);
                        }
                    } else {
                        MessageUtils.showToast(getResources().getString(R.string.fieldinfo_diolog_choosespecifications));
                        if (dimensions != null && Term_typesmap != null && sizelist != null) {
                            if (dimensions.size() > 0 && Term_typesmap.size() > 0 && sizelist.size() > 0) {
                                Intent selecttime = new Intent(AdvertisingInfoActivity.this, ChooseSpecificationsActivity.class);
                                Intent_ChooseSpecificationsActivity(selecttime);
                                selecttime.putExtra("operationtype",3);
                                startActivityForResult(selecttime, 3);
                            }
                        }

                        return;
                    }
                } else {
                    Intent loginintent = new Intent(AdvertisingInfoActivity.this, LoginActivity.class);
                    startActivity(loginintent);
                }

                break;
            case R.id.select_time:
                if (dimensions != null && Term_typesmap != null && sizelist != null) {
                    if (dimensions.size() > 0 && Term_typesmap.size() > 0 && sizelist.size() > 0) {
                        Intent selecttime = new Intent(AdvertisingInfoActivity.this, ChooseSpecificationsActivity.class);
                        Intent_ChooseSpecificationsActivity(selecttime);
                        selecttime.putExtra("operationtype",1);
                        startActivityForResult(selecttime, 1);
                    }
            }

                break;
            case R.id.evaluation_layout:
                if (LoginManager.isLogin()) {
                    Intent review = new Intent(AdvertisingInfoActivity.this, FieldEvaluationActivity.class);
                    review.putExtra("fieldid", getfieldid);
                    startActivity(review);
                } else {
                    new AlertDialog.Builder(AdvertisingInfoActivity.this)
                            .setTitle("登录后查看更多是否登录？")
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent Lofinintent = new Intent(AdvertisingInfoActivity.this, LoginActivity.class);
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
            case R.id.field_shop_layout:
                //加入购物车
                if (LoginManager.isLogin()) {
                    if (lease_term_type.length() > 0 && size.length() > 0 && ((datelist != null &&
                            datelist.size() > 0) ||
                            mtwcont > 0)) {
                            showProgressDialog();
                            FieldApi.addshopcart_items(MyAsyncHttpClient.MyAsyncHttpClient(), addToShoppingCartHandler, getadvertisingsubmitjsonstr());
                    } else {
                        MessageUtils.showToast(AdvertisingInfoActivity.this, getResources().getString(R.string.fieldinfo_diolog_choosespecifications));
                        if (dimensions != null && Term_typesmap != null && sizelist != null) {
                            if (dimensions.size() > 0 && Term_typesmap.size() > 0 && sizelist.size() > 0) {
                                Intent selecttime = new Intent(AdvertisingInfoActivity.this, ChooseSpecificationsActivity.class);
                                Intent_ChooseSpecificationsActivity(selecttime);
                                selecttime.putExtra("operationtype",2);
                                startActivityForResult(selecttime, 2);
                            }
                        }
                    }
                } else {
                    Intent loginintent = new Intent(AdvertisingInfoActivity.this, LoginActivity.class);
                    startActivity(loginintent);
                }
                break;
            case R.id.fieldinfo_address_layout:
                Intent mapinfo_intent = new Intent(AdvertisingInfoActivity.this,FieldinfoMapinfoActivity.class);
                mapinfo_intent.putExtra("longitude",resourceinfolist.getPhysical_resource().getCommunity().getLng());
                mapinfo_intent.putExtra("latitude",resourceinfolist.getPhysical_resource().getCommunity().getLat());
                mapinfo_intent.putExtra("resourcename",resourceinfolist.getPhysical_resource().getName());
                mapinfo_intent.putExtra("address",resourceinfolist.getPhysical_resource().getCommunity().getDetailed_address());
                startActivity(mapinfo_intent);
                break;
            case R.id.resourceinfo_classify_fieldinfo:
                mscrollview.smoothScrollTo(0, classify_info_layout_height - 300);
                mresourceinfo_classify_fieldinfo.setTextColor(getResources().getColor(R.color.default_bluebg));
                mresourceinfo_classify_communityinfo.setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                mresourceinfo_classify_fieldinfo.setBackgroundColor(getResources().getColor(R.color.white));
                mresourceinfo_classify_communityinfo.setBackgroundColor(getResources().getColor(R.color.tab_gb));
                mresource_communityinfo_layout.setVisibility(View.GONE);
                mresource_fieldinfo_layout.setVisibility(View.VISIBLE);
                get_layout_height = false;
                hide_layout_height = 0;
                break;
            case R.id.resourceinfo_classify_communityinfo:
                mscrollview.smoothScrollTo(0, classify_info_layout_height - 300);
                mresourceinfo_classify_fieldinfo.setTextColor(getResources().getColor(R.color.top_title_center_txt_color));
                mresourceinfo_classify_communityinfo.setTextColor(getResources().getColor(R.color.default_bluebg));
                mresourceinfo_classify_fieldinfo.setBackgroundColor(getResources().getColor(R.color.tab_gb));
                mresourceinfo_classify_communityinfo.setBackgroundColor(getResources().getColor(R.color.white));
                mresource_communityinfo_layout.setVisibility(View.VISIBLE);
                mresource_fieldinfo_layout.setVisibility(View.GONE);
                get_layout_height = false;
                hide_layout_height = 0;
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1 || resultCode == 2 || resultCode == 3) {
            JsonArrayDatelistString = (ArrayList<String>) data.getSerializableExtra("jsonarraydatastr");
            datelist = (ArrayList<Date>) data.getSerializableExtra("arraylist_date");
            if (data.getExtras().get("checkedstart_date") != null) {
                checkedstart_date = (Date) data.getExtras().get("checkedstart_date");
            }
            if (data.getExtras().get("checkedend_date") != null) {
                checkedend_date = (Date) data.getExtras().get("checkedend_date");
            }

            if (data.getExtras().get("size") != null) {
                if (data.getExtras().get("size").toString().length() > 0) {
                    size = data.getExtras().get("size").toString();
                }
            }
            if (data.getExtras().get("lease_term_type") != null) {
                if (data.getExtras().get("lease_term_type").toString().length() > 0) {
                    lease_term_type = data.getExtras().get("lease_term_type").toString();
                }
            }
            if (data.getExtras().get("custom_dimension") != null) {
                if (data.getExtras().get("custom_dimension").toString().length() > 0) {
                    custom_dimension = data.getExtras().get("custom_dimension").toString();
                } else {
                    custom_dimension = "";
                }
            } else {
                custom_dimension = "";
            }

            if (data.getExtras().get("cont") != null) {
                if (data.getExtras().getInt("cont") > 0) {
                    mtwcont = data.getExtras().getInt("cont");
                }
            }
            if (resultCode == 2) {
                    showProgressDialog();
                    FieldApi.addshopcart_items(MyAsyncHttpClient.MyAsyncHttpClient(), addToShoppingCartHandler, getadvertisingsubmitjsonstr());

            } else if (resultCode == 3) {
                    Intent OrderConfirm = new Intent(this,OrderConfirmActivity.class);
                    OrderConfirm.putExtra("ordertype",1);
                    OrderConfirm.putExtra("submitorderlist",(Serializable)getadvertisingsubmitorderlist());
                    OrderConfirm.putExtra("deposit",deposit);
                    OrderConfirm.putExtra("is_adv",1);
                    startActivity(OrderConfirm);
            }
            if (good_type == 2) {
                String term_type = "";
                if (Term_typesmap.get(lease_term_type) != null) {
                    term_type = term_type + "1" + "(" + Term_typesmap.get(lease_term_type) + ")";
                }
                mselecttime_text.setText(getResources().getString(R.string.order_listitem_sizetxt) + size + term_type + getResources().getString(R.string.choose_advertisingnumber) + String.valueOf(mtwcont));
            }
            if (size.length() > 0 && lease_term_type.length() > 0) {
                if (lease_term_type.equals("-1")) {//天
                    int dayprice = 0;
                    for (int i = 0; i < resourceinfolist.getSize().size(); i++) {
                        if (resourceinfolist.getSize().get(i).getDimension() != null) {
                            if (resourceinfolist.getSize().get(i).getDimension().getSize() != null) {
                                if (resourceinfolist.getSize().get(i).getDimension().getLease_term_type() != null) {
                                    if (resourceinfolist.getSize().get(i).getDimension().getLease_term_type().equals(
                                            getResources().getString(R.string.groupbooding_list_item_residue_tiem_day_str))) {
                                        if (resourceinfolist.getSize().get(i).getResource().get(0).getPrice() != null) {
                                            dayprice = resourceinfolist.getSize().get(i).getResource().get(0).getPrice();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (mfield_subsidy_textview.getVisibility() == View.VISIBLE && mfield_subsidy_textview.getText().toString().length() > 0) {
                        mfield_subsidy_textview.setText(getResources().getString(R.string.order_listitem_subsidy_text)+getResources().getString(R.string.order_listitem_price_unit_text)
                                +Constants.getdoublepricestring(resourceinfolist.getPhysical_resource().getCurrent_resource().getSubsidy_rate()* 0.01* dayprice*0.01,1)+
                                getResources().getString(R.string.term_types_unit_txt));
                    }
                    mtxt_field_price.setText(Constants.getpricestring(String.valueOf(dayprice), 0.01));
                    mfieldinfo_integral_textview.setText(getResources().getString(R.string.fieldinfo_refunt_remindtext)+
                            Constants.getfieldinfo_integral(String.valueOf(dayprice), 0.01)+getResources().getString(R.string.fieldinfo_integral_text));

                } else {
                    for (int i = 0; i < dimensions.size(); i++) {
                        if (dimensions.get(i).get("size").equals(size) &&
                                dimensions.get(i).get("lease_term_type_id").equals(lease_term_type)&&
                                dimensions.get(i).get("custom_dimension")!= null &&
                                dimensions.get(i).get("custom_dimension").equals(custom_dimension)) {
                            if (mfield_subsidy_textview.getVisibility() == View.VISIBLE && mfield_subsidy_textview.getText().toString().length() > 0) {
                                mfield_subsidy_textview.setText(getResources().getString(R.string.order_listitem_subsidy_text)+getResources().getString(R.string.order_listitem_price_unit_text)
                                        +Constants.getdoublepricestring(resourceinfolist.getSize().get(0).getResource().get(0).getSubsidy_rate()* 0.01* Integer.parseInt(dimensions.get(i).get("price"))*0.01,1)+
                                        getResources().getString(R.string.term_types_unit_txt));
                            }
                            mtxt_field_price.setText(Constants.getpricestring(dimensions.get(i).get("price"), 0.01));
                            mfieldinfo_integral_textview.setText(getResources().getString(R.string.fieldinfo_refunt_remindtext)+
                                    Constants.getfieldinfo_integral(dimensions.get(i).get("price"), 0.01)+getResources().getString(R.string.fieldinfo_integral_text));
                        }
                    }
                }
            }
        }
        if (resultCode == 4) {
            if (data.getExtras() != null && data.getExtras().get("id") != null
                    && data.getExtras().getInt("id") > 0) {
                getfieldid = String.valueOf(data.getExtras().getInt("id"));
                Intent fieldinfo = new Intent(AdvertisingInfoActivity.this, AdvertisingInfoActivity.class);
                fieldinfo.putExtra("fieldId", getfieldid);
                fieldinfo.putExtra("good_type", good_type);
                startActivity(fieldinfo);
                finish();
            }
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
                LayoutInflater mLayoutInflater = LayoutInflater.from(AdvertisingInfoActivity.this);
                final View mDialogView = mLayoutInflater.inflate(R.layout.activity_fieldinfo_dialogview, null);
                final Dialog mSuccessDialgo = new AlertDialog.Builder(AdvertisingInfoActivity.this).create();
                Constants.show_dialog(mDialogView,mSuccessDialgo);
                if (tv_fieldinfo_card_size.getText().toString().length() > 0) {
                    tv_fieldinfo_card_size.setVisibility(View.VISIBLE);
                    if (Double.parseDouble(tv_fieldinfo_card_size.getText().toString().trim()) > 99) {
                        tv_fieldinfo_card_size.setText("99+");
                    } else {
                        tv_fieldinfo_card_size.setText(String.valueOf(1+Integer.parseInt(tv_fieldinfo_card_size.getText().toString().trim())));
                    }
                } else {
                    tv_fieldinfo_card_size.setVisibility(View.VISIBLE);
                    tv_fieldinfo_card_size.setText("1");
                }
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
                    final View myView = AdvertisingInfoActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                    pw = new AlertDialog.Builder(AdvertisingInfoActivity.this).create();
                    share_linkurl = Config.Domain_Name + Config.FIELDINFO_ADV_URL + getfieldid + Config.FIELDINFO_END_URL+"&city_id="+LoginManager.getTrackcityid();
                    if (pw!= null && pw.isShowing()) {
                        pw.dismiss();
                    }
                    Constants constants = new Constants(AdvertisingInfoActivity.this,
                            ShareIconStr);
                    constants.share_showPopupWindow(AdvertisingInfoActivity.this,myView,pw,api,share_linkurl,
                            ShareTitleStr,
                            SharedescriptionStr, ShareBitmap);
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
    private JSON getadvertisingsubmitjsonstr() {
        JSONArray jsonArray = new JSONArray();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        JSONObject json = new JSONObject();
        json.put("id", resourceinfolist.getPhysical_resource().getCurrent_resource().getId());
        json.put("size",size);
        json.put("custom_dimension", custom_dimension);
        if (lease_term_type.equals("-1")) {
            int weekInt = com.linhuiba.linhuifield.connector.Constants.getDayForWeek(sdf.format(date));
            if (weekInt > -1) {
                if (mWeekLeaseTermPriceMap.get(
                        mWeekLeaseTermMap.get(weekInt)) != null) {
                    json.put("lease_term_type_id", mWeekLeaseTermMap.get(weekInt));
                }
            }
        } else {
            json.put("lease_term_type_id", Integer.parseInt(lease_term_type));
        }
        json.put("count", mtwcont);
        json.put("count_of_time_unit", 1);
        jsonArray.add(json);
        return jsonArray;
    }
    private ArrayList<HashMap<String,Object>> getadvertisingsubmitorderlist() {
        ArrayList<HashMap<String,Object>> jsonArray = new ArrayList<HashMap<String,Object>>();
        HashMap<String,Object> json = new HashMap<String,Object>();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String advertisignprice = "";
        String imagepath = "";
        if (mPicList != null) {
            if(mPicList.size() > 0) {
                imagepath = mPicList.get(0).toString();
            }
        }
        for (int i = 0; i < dimensions.size(); i++) {
            if (dimensions.get(i).get("size").equals(size) &&
                    dimensions.get(i).get("lease_term_type_id").equals(lease_term_type)) {
                advertisignprice = dimensions.get(i).get("price");
                break;
            }
        }
        json.put("id", resourceinfolist.getPhysical_resource().getCurrent_resource().getId());
        json.put("date",sdf.format(date));
        json.put("size",size);
        if (lease_term_type.equals("-1")) {
            int weekInt = com.linhuiba.linhuifield.connector.Constants.getDayForWeek(sdf.format(date));
            if (weekInt > -1) {
                if (mWeekLeaseTermPriceMap.get(
                        mWeekLeaseTermMap.get(weekInt)) != null) {
                    json.put("lease_term_type_id", mWeekLeaseTermMap.get(weekInt));
                }
            }
        } else {
            json.put("lease_term_type_id", Integer.parseInt(lease_term_type));
        }
        json.put("lease_term_type", Term_typesmap.get(lease_term_type));
        json.put("count", mtwcont);
        json.put("custom_dimension", custom_dimension);
        json.put("count_of_time_unit", 1);
        json.put("price",advertisignprice);
        json.put("subsidy_fee",resourceinfolist.getSize().get(0).getResource().get(0).getPrice() *
                resourceinfolist.getSize().get(0).getResource().get(0).getSubsidy_rate() * 0.01);
        json.put("resourcename", resourceinfolist.getPhysical_resource().getName());
        json.put("imagepath",imagepath);
        json.put("field_type", resourceinfolist.getPhysical_resource().getAd_type().getDisplay_name());
        if (resourceinfolist.getSize().get(0).getResource().get(0).getSubsidy_rate() > 0) {
            json.put("discount_rate", resourceinfolist.getSize().get(0).getResource().get(0).getSubsidy_rate());
        } else {
            json.put("discount_rate", "0");
        }
        if (resourceinfolist.getSize().get(0).getResource().get(0).getTax_point() != null) {
            if (resourceinfolist.getSize().get(0).getResource().get(0).getTax_point() > 0) {
                json.put("tax_point", resourceinfolist.getSize().get(0).getResource().get(0).getTax_point());
            } else {
                json.put("tax_point", "0");
            }
        } else {
            json.put("tax_point", "0");
        }
        if (resourceinfolist.getSize().get(0).getResource().get(0).getSpecial_tax_point() != null &&
                resourceinfolist.getSize().get(0).getResource().get(0).getSpecial_tax_point() > 0) {
            json.put("special_tax_point", resourceinfolist.getSize().get(0).getResource().get(0).getSpecial_tax_point());
        } else {
            json.put("special_tax_point", "0");
        }
        jsonArray.add(json);
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
        com.linhuiba.linhuifield.connector.Constants mConstants = new com.linhuiba.linhuifield.connector.Constants(AdvertisingInfoActivity.this, AdvertisingInfoActivity.this,mNewPosition,mImageListSize,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT);
        mConstants.showPicture(mView,mShowPictureSizeTV,mZoomViewPage,
                mReviewZoomPictureDialog,mDialogImageViewList,pathtmp,mDislogIsRefreshZoomImg,
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

    private void show_paysuccess_PopupWindow(int type) {
        LayoutInflater factory = LayoutInflater.from(AdvertisingInfoActivity.this);
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
        if (type == 0 || type == 2) {
            mrefunt_price_detailed_btn.setVisibility(View.VISIBLE);
            mfieldinfo_refunt_price_detailed_layout.setVisibility(View.VISIBLE);
            mfieldinfo_error_recovery_layout.setVisibility(View.GONE);
            if (type == 0) {
                mrefunt_price_detailed.setText(getResources().getString(R.string.fieldinfo_refunt_price_detailedtext)+
                        Constants.getService_Phone(AdvertisingInfoActivity.this) + getResources().getString(R.string.fieldinfo_refunt_price_detailed_twotext));
            } else if (type == 2) {
                mrefunt_price_detailed.setText(getResources().getString(R.string.fieldinfo_need_cash_pledge_content_one_text)+ Constants.getdoublepricestring(resourceinfolist.getSize().get(0).getResource().get(0).getDeposit(),1)+
                        getResources().getString(R.string.term_types_unit_txt) +getResources().getString(R.string.fieldinfo_need_cash_pledge_content_two_text));
                mrefunt_price_text.setText(getResources().getString(R.string.fieldinfo_need_cash_pledge_text));
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
            if (resourceinfolist.getPhysical_resource().getName() != null && resourceinfolist.getPhysical_resource().getName().length() > 0) {
                merror_recovery_resourcesname_text.setText(resourceinfolist.getPhysical_resource().getName());
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
                    FieldApi.release_feedbacks(MyAsyncHttpClient.MyAsyncHttpClient2(), release_feedbacksHandler,
                            resourceinfolist.getPhysical_resource().getCurrent_resource().getId(),0,
                            merror_recovery_content_edit.getText().toString());
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.fieldinfo_error_recovery_content_remindtext));
                    return;
                }
            }
        });
    }
    private LinhuiAsyncHttpResponseHandler release_feedbacksHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            txt_error_correction_submit.setEnabled(true);
            if (newdialog.isShowing()) {
                newdialog.dismiss();
            }
            MessageUtils.showToast(getResources().getString(R.string.fieldinfo_error_recovery_content_remindsuccess_text));
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            txt_error_correction_submit.setEnabled(true);
            if (!superresult) {
                MessageUtils.showToast(getContext(), error.getMessage());
            }
            checkAccess_new(error);
        }
    };
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if(requestCode == Constants.PermissionRequestCode) {
                MQConfig.init(AdvertisingInfoActivity.this, com.linhuiba.linhuipublic.config.Config.MQAppkey, new OnInitCallback() {
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
                            Intent intent = new MQIntentBuilder(AdvertisingInfoActivity.this)
                                    .setClientInfo(clientInfo)
                                    .setCustomizedId(LoginManager.getUid())
                                    .build();
                            startActivityForResult(intent,10);
                        } else {
                            Intent intent = new MQIntentBuilder(AdvertisingInfoActivity.this)
                                    .build();
                            startActivityForResult(intent,10);
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
            if(requestCode == Constants.PermissionRequestCode) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            }
        }
    };
    @Override
    public void onScroll(int scrollY) {
        if (random_info) {
            if (!get_layout_height && scrollY > 1 && scrollY < 100 && hide_layout_height == 0 && random_info) {
                hide_layout_height = Math.max(scrollY, mfieldinfo_recommend_type_view.getTop());
                if (hide_layout_height > 0) {
                    get_layout_height = true;
                    random_info = false;
                }
            }
            if (!get_classify_info_layout_height && scrollY > 1 && scrollY < 100 && classify_info_layout_height == 0 && random_info) {
                classify_info_layout_height = Math.max(scrollY, mfieldinfo_information_classify_layout.getTop());
                if (classify_info_layout_height > 0) {
                    get_classify_info_layout_height = true;
                    random_info = false;
                }
            }
        } else {
            if (!get_layout_height && scrollY > 1 && hide_layout_height == 0) {
                hide_layout_height = Math.max(scrollY, mfieldinfo_recommend_type_view.getTop());
                if (hide_layout_height > 0) {
                    get_layout_height = true;
                }
            }
            if (!get_classify_info_layout_height && scrollY > 1 && classify_info_layout_height == 0) {
                classify_info_layout_height = Math.max(scrollY, mfieldinfo_information_classify_layout.getTop());
                if (classify_info_layout_height > 0) {
                    get_classify_info_layout_height = true;
                }
            }

        }
        int mBuyLayout2ParentTop = Math.max(scrollY, mfieldinfo_information_classify_layout.getTop());
        mfieldinfo_information_classify_layout_tmp.layout(0, mBuyLayout2ParentTop, mfieldinfo_information_classify_layout_tmp.getWidth(), mBuyLayout2ParentTop + mfieldinfo_information_classify_layout_tmp.getHeight());
        if (hide_layout_height > 0) {
            if (scrollY > hide_layout_height - 88) {
                mfieldinfo_information_classify_layout.setVisibility(View.INVISIBLE);
                mfieldinfo_information_classify_layout_tmp.setVisibility(View.INVISIBLE);
            } else {
                mfieldinfo_information_classify_layout.setVisibility(View.VISIBLE);
                mfieldinfo_information_classify_layout_tmp.setVisibility(View.VISIBLE);
            }
        }
    }

    public void prices_list_OnClickListener(int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position);
        test.CalendarClicklisten(this);
    }

    @Override
    public void CalendarClick(int position) {
        if (LoginManager.isLogin()) {
            if (dimensions_show_list.get(position).get("size") != null && dimensions_show_list.get(position).get("size").length() > 0) {
                size = dimensions_show_list.get(position).get("size");
            } else {
                size = "";
            }
            if (dimensions_show_list.get(position).get("lease_term_type") != null &&
                    dimensions_show_list.get(position).get("lease_term_type").length() > 0) {
                JSONObject  jsonObject = JSONObject.parseObject(dimensions_show_list.get(position).get("lease_term_type").toString());
                if (jsonObject.get("id") != null && jsonObject.get("id").toString().length() > 0) {
                    lease_term_type = jsonObject.get("id").toString();
                } else {
                    lease_term_type = "";
                }
            } else {
                lease_term_type = "";
            }

            if (dimensions_show_list.get(position).get("custom_dimension") != null && dimensions_show_list.get(position).get("custom_dimension").length() > 0) {
                custom_dimension = dimensions_show_list.get(position).get("custom_dimension");
            } else {
                custom_dimension = "";
            }
            if (dimensions_show_list != null && Term_typesmap != null && sizelist != null) {
                if (dimensions_show_list.size() > 0 && Term_typesmap.size() > 0 && sizelist.size() > 0) {
                    Intent selecttime = new Intent(AdvertisingInfoActivity.this, ChooseSpecificationsActivity.class);
                    if (datelist != null) {
                        datelist.clear();
                    }
                    if (checkedstart_date != null) {
                        checkedstart_date = new Date();
                    }
                    if (checkedend_date != null) {
                        checkedend_date = new Date();
                    }
                    Intent_ChooseSpecificationsActivity(selecttime);
                    selecttime.putExtra("operationtype",3);
                    startActivityForResult(selecttime, 3);
                }
            }
            size = "";
            lease_term_type = "";
            custom_dimension = "";
        } else {
            Intent loginintent = new Intent(AdvertisingInfoActivity.this, LoginActivity.class);
            startActivity(loginintent);
        }
    }
    private void show_zoom_picture_dialog(int position) {
        View myView = AdvertisingInfoActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_preview_zoom_picture_dialog, null);
        zoom_picture_dialog = new AlertDialog.Builder(AdvertisingInfoActivity.this).create();
        Constants.show_dialog(myView,zoom_picture_dialog);
        TextView mshowpicture_sizetxt = (TextView)myView.findViewById(R.id.showpicture_dialog_sizetxt);
        TextView mshowpicture_back = (TextView)myView.findViewById(R.id.showpicture_dialog_back);
        ViewPager mzoom_viewpage = (ViewPager)myView.findViewById(R.id.zoom_dialog_viewpage);
        mshowpicture_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoom_picture_dialog.dismiss();
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
    //intent 选择规格界面传参数
    private void Intent_ChooseSpecificationsActivity(Intent selecttime) {
        selecttime.putExtra("minimum_order_quantity", minimum_order_quantity);
        selecttime.putExtra("days_in_advance", days_in_advance);
        selecttime.putExtra("arraylist_date", (Serializable) datelist);
        selecttime.putExtra("demensions", (Serializable) dimensions);
        selecttime.putExtra("term_typestr", (Serializable)Term_typesmap);
        selecttime.putExtra("sizelist", (Serializable) sizelist);
        if (custom_dimension_list != null && custom_dimension_list.size() > 0) {
            selecttime.putExtra("custom_dimension_list", (Serializable) custom_dimension_list);
        }
        if (term_type_days_map != null && term_type_days_map.size() > 0) {
            selecttime.putExtra("term_type_days_map", (Serializable) term_type_days_map);
        }
        if (checkedstart_date != null) {
            selecttime.putExtra("checkedstart_date", checkedstart_date);
        }
        if (checkedend_date != null) {
            selecttime.putExtra("checkedend_date", checkedend_date);
        }
        selecttime.putExtra("Closing_dates",(Serializable)Closing_dates);
        selecttime.putExtra("weatherstr",weatherliststr);
        selecttime.putExtra("resourcetype",good_type);
        if (resourceinfolist.getSize().get(0).getResource().get(0).getDeadline() != null) {
            if (resourceinfolist.getSize().get(0).getResource().get(0).getDeadline().length() > 0) {
                selecttime.putExtra("deadline",resourceinfolist.getSize().get(0).getResource().get(0).getDeadline());
            }
        }
        if (resourceinfolist.getSize().get(0).getResource().get(0).getActivity_start_date() != null) {
            if (resourceinfolist.getSize().get(0).getResource().get(0).getActivity_start_date().length() > 0) {
                selecttime.putExtra("activity_start_date",resourceinfolist.getSize().get(0).getResource().get(0).getActivity_start_date());
            }
        }
        if (lease_term_type != null) {
            if (lease_term_type.length() > 0) {
                selecttime.putExtra("lease_term_type", lease_term_type);
            }
        }
        if (size != null) {
            if (size.length() > 0) {
                selecttime.putExtra("size", size);
            }
        }
        if (custom_dimension != null && custom_dimension.length() > 0) {
            selecttime.putExtra("custom_dimension", custom_dimension);
        }

        if (mtwcont > 0) {
            selecttime.putExtra("cont", mtwcont);
        }
    }
    private void getCardSize() {
        FieldApi.getshopcart_itemscount(MyAsyncHttpClient.MyAsyncHttpClient(), getShoppingCartCountHandler);
    }
    private LinhuiAsyncHttpResponseHandler getShoppingCartCountHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            if (data != null) {
                JSONObject jsonobject = JSON.parseObject(data.toString());
                if (jsonobject.get("count") != null && jsonobject.get("count").toString().length() > 0) {
                    if (jsonobject.get("count").toString().equals("0")) {
                        tv_fieldinfo_card_size.setVisibility(View.GONE);
                    } else {
                        tv_fieldinfo_card_size.setVisibility(View.VISIBLE);
                        if (Double.parseDouble(jsonobject.get("count").toString()) > 99) {
                            tv_fieldinfo_card_size.setText("99+");
                        } else {
                            tv_fieldinfo_card_size.setText(jsonobject.get("count").toString());
                        }
                    }
                } else {
                    tv_fieldinfo_card_size.setVisibility(View.GONE);
                }
            } else {
                tv_fieldinfo_card_size.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

}
