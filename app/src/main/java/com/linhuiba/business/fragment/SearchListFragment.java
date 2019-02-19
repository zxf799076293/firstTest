package com.linhuiba.business.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baselib.app.activity.BaseFragment;
import com.baselib.app.util.MessageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.AboutUsActivity;
import com.linhuiba.business.activity.BaiduMapActivity;
import com.linhuiba.business.activity.MainTabActivity;
import com.linhuiba.business.activity.SearchFieldAreaActivity;
import com.linhuiba.business.adapter.ResourcesScreeningItemAdapter;
import com.linhuiba.business.adapter.ResourcesScreeningNewAdapter;
import com.linhuiba.business.adapter.SearchAreaPwAdapter;
import com.linhuiba.business.adapter.SearchCommunityListAdapter;
import com.linhuiba.business.adapter.SearchSortAdapter;
import com.linhuiba.business.basemvp.BaseMvpFragment;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.business.model.SearchAreaPwModel;
import com.linhuiba.business.model.SearchAreaSubwayPwModel;
import com.linhuiba.business.model.SearchListAttributesModel;
import com.linhuiba.business.model.SearchSellResModel;
import com.linhuiba.business.mvpmodel.LoginMvpModel;
import com.linhuiba.business.mvppresenter.SearchResListMvpPresenter;
import com.linhuiba.business.mvpview.SearchResListMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Request;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.view.LoadMoreListView;
import com.linhuiba.linhuifield.fieldactivity.FieldAddFieldGuideActivity;
import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldAttributesModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuifield.sqlite.ConfigCitiesModel;
import com.linhuiba.linhuifield.sqlite.ConfigCityParameterModel;
import com.linhuiba.linhuifield.sqlite.ConfigSqlOperation;
import com.linhuiba.linhuifield.sqlite.ConfigurationsModel;
import com.linhuiba.linhuifield.sqlite.DBManager;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.linhuipublic.config.SupportPopupWindow;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/7.
 */

public class SearchListFragment extends BaseMvpFragment implements SwipeRefreshLayout.OnRefreshListener, SearchResListMvpView {
    private View mMainContent;
    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout SearchListswipList;
    @InjectView(R.id.View_view)
    View View_view;
    @InjectView(R.id.sort_txt)
    TextView msort_txt;
    @InjectView(R.id.edit_search_search)
    TextView medit_search;
    @InjectView(R.id.lay_no_searchlist)
    LinearLayout mlay_no_searchlist;
    @InjectView(R.id.searchlist_recommend_ll)
    LinearLayout mRecommedLL;
    @InjectView(R.id.search_jump_map_img)
    ImageButton msearch_jump_map_img;
    @InjectView(R.id.screening_txt)
    TextView mscreening_txt;
    @InjectView(R.id.search_area_type_textview)
    TextView msearch_area_type_textview;
    @InjectView(R.id.search_price_type_textview)
    TextView msearch_price_type_textview;
    @InjectView(R.id.searchlist_itemsize_text)
    TextView msearchlist_itemsize_text;
    @InjectView(R.id.searchlist_itemsize_ll)
    LinearLayout mSearchSizeLL;
    @InjectView(R.id.search_no_deposit_cb)
    CheckBox mSearchNoDepositCheckBox;//免押金
    @InjectView(R.id.search_has_phy_res_cb)
    CheckBox mSearchHasPhyResCheckBox;//有展位
    @InjectView(R.id.search_bestsell_rbtn)//有优惠
    CheckBox mSearchBestSellCheckBox;
    @InjectView(R.id.search_fast_cb)//快速订
    CheckBox mSearchFastCheckBox;
    @InjectView(R.id.search_fast_imgv)
    ImageView mSearchFastImgv;
    @InjectView(R.id.searchlist_field_bable_ll)
    LinearLayout mSearchLabelLL;
    @InjectView(R.id.search_area_backimg)
    TextView mSearchBackTV;
    @InjectView(R.id.order_list_rv)
    RecyclerView mSearchListRV;
    @InjectView(R.id.searchlist_index_app_barl)
    AppBarLayout mSearchListAppBarL;


    private SearchCommunityListAdapter mSearchListAdapter;
    private Dialog mSearchShareDialog;
    private SupportPopupWindow mSearchSortPw;
    private ArrayList<HashMap<String,String>> mSearchSortList = new ArrayList<>();
    private String intentCityName,getintentcity_code;
    private ArrayList<ResourceSearchItemModel> mSearchInfoList = new ArrayList<ResourceSearchItemModel>();
    private int fieldlistpagesize;
    private  String searchkeywords;
    private IWXAPI api;
    public ApiResourcesModel apiResourcesModel = new ApiResourcesModel();//搜索列表的model
    private String modelorder_by;
    private String modelorder;
    private String mDefaultOrderBy = "weight_score";
    private String mDefaultOrder = "desc";
    private Intent homeintent;
    private String SharedescriptionStr = "";
    private String ShareIconStr ="";
    private Bitmap ShareBitmap = null;
    private String shareurl= "";
    private String sharewxMinShareLinkUrl = "";//小程序分享的url
    private Bitmap miniShareBitmap = null;//小程序分享需要的bitmap
    private int price_mark_int = 0;//0没选中1从低到高2从高到低
    private Drawable drawable_gray_price_gray;//没选中
    private Drawable drawable_gray_price_top;//从低到高
    private Drawable drawable_gray_price_bottom;//从高到低
    private SupportPopupWindow screening_pw;

    private ListView mresourcesscreening_stickygridview_new;
    private HashMap<String, String> mCategoryMap;//配置文件中类目
    private ArrayList<SearchAreaPwModel> mTradingAreasList;//配置文件中商圈解析
    private ArrayList<SearchAreaSubwayPwModel> mSubwayList;//配置文件中的地铁
    private ArrayList<SearchAreaPwModel> mSubwayLineList;//配置文件中的地铁线
    private List<ConfigCityParameterModel> field_labels_list  = new ArrayList<>();//邻汇推荐中所有的名称要显示在界面上的数组
    private ArrayList<String> mCategoryList = new ArrayList<String>();//类目中所有的名称要显示在界面上的数组
    private List<ConfigCityParameterModel> mLocationTypeList = new ArrayList<>();//位置类型界面上显示的数组
    private ArrayList<SearchListAttributesModel> mAttributesList = new ArrayList<>();//属性中所有的名称要显示在界面上的数组
    private ArrayList<SearchAreaPwModel> mDistrictList;//配置文件中城市解析
    private ArrayList<SearchAreaPwModel> mSearchAreaList;//区域第一列列表
    private ResourcesScreeningNewAdapter resourcesScreeningNewAdapter;
    public int GridviewNumColumns = 0;//根据屏幕尺寸设置gridview的一行显示个数
    public int mLocationTypeChooseInt = 0;//2018/12/11 //判断位置类型是否有选中 1选中显示全部反之显示3条数据
    public int field_labels_int = 0;//判断邻汇推荐是否有选中 1选中显示全部反之显示3条数据
    public int category_id_int = 0;//判断类目是否有选中 1选中显示全部反之显示3条数据
    public HashMap<String,Integer> attributesChooseMap;//判断属性是否有选中 1选中显示全部反之显示3条数据
    private boolean screening_result_boolean;//判断是否显示搜索到的数据个数
    private final int HomePageTypeField = 1;
    private Drawable mSortGreyUpDrawable;//排序
    private Drawable mSortGreyDownDrawable;//排序
    private Drawable mSortBlueUpDrawable;//排序
    private Drawable mSortBlueDownDrawable;//排序
    private boolean mSortChecked;//排序选中
    boolean isShare;
    private SearchAreaPwAdapter firstAdapter;
    private SearchAreaPwAdapter secondAdapter;
    private SearchAreaPwAdapter thirdAdapter;
    private int chooseAreaType = -1;//上一次选中那个  确定后赋值
    private ListView firstLv;
    private ListView secondLv;
    private ListView thirdLv;
    private int mSubWayLinePosition = -1;//上一次选中地铁线那个 确定后赋值
    private int firstChooseInt;//第一列选中的position  确定后赋值
    private int secondChooseInt = -1;//第二列选中的position 确定后赋值
    private boolean isSearchAreaReset;//是否区域重置
    private String mSearchAreaTvStr;
    private HashMap<String,Object> mSearchAddressBackMap = new HashMap<>();//搜索百度地址的信息
    private ArrayList<HashMap<String,String>> mSearchPriceList = new ArrayList<>();//搜索价格排序list
    private boolean isOnResume;//是否onResume运行完
    private static final int LOACTION_REQUEST_INT = 10;//权限 requestcode
    private LocationClient mLocationClient = null;
    private double lat;//地图中心点纬度(不传则表示不限定区域）
    private double lng;//地图中心点经度(不传则表示不限定区域）
    public SearchResListMvpPresenter mSearchResListMvpPresenter;
    private ArrayList<HashMap<Object,Object>> mCategoryAdapterList = new ArrayList<>();
    private ArrayList<HashMap<Object,Object>> data_new;//筛选的list
    private boolean isShowScreenAttributes;
    private MainTabActivity mMainTabActivity;
    private Button mSearchConfirmBtn;
    private ApiResourcesModel mApiResourcesModel;//确定按钮数量需要用到
    private boolean isGetCategory;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mMainContent == null) {
            mMainContent = inflater.inflate(R.layout.activity_searchlist, container , false);
            mSearchResListMvpPresenter = new SearchResListMvpPresenter();
            mSearchResListMvpPresenter.attachView(this);
            ButterKnife.inject(this, mMainContent);
            AndPermission.with(SearchListFragment.this)
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
            homeintent = SearchListFragment.this.getActivity().getIntent();
            if (homeintent != null &&
                    homeintent.getExtras() != null) {
                if (homeintent.getExtras().get("is_home_page") != null) {
                    initview();
                    if (homeintent.getExtras().getInt("is_home_page") == 4) {
                        if (homeintent.getExtras().get("mSearchAddressBackMap") != null) {
                            mSearchAddressBackMap = (HashMap<String, Object>) homeintent.getExtras().get("mSearchAddressBackMap");
                        }
                        if (homeintent.getExtras().get("ApiResourcesModel") != null) {
                            apiResourcesModel = (ApiResourcesModel) homeintent.getSerializableExtra("ApiResourcesModel");
                            getorder_by();
                            fieldlistpagesize = 1;
                            if (apiResourcesModel.getLongitude_delta() == 0 &&
                                    apiResourcesModel.getLatitude_delta() == 0) {
                                apiResourcesModel.setHas_physical(1);
                            }
                            apiResourcesModel.setPage(String.valueOf(fieldlistpagesize));
                            apiResourcesModel.setPage_size(10);
                            getintentcity_code = String.valueOf(apiResourcesModel.getCity_ids().get(0));
                            showProgressDialog();
                            mSearchResListMvpPresenter.getPhyReslist(apiResourcesModel);
                        } else {
                            getintentcity_code = LoginManager.getInstance().getTrackcityid();
                            getorder_by();
                            fieldlistpagesize = 1;
                            if (modelorder != null &&
                                    modelorder_by != null &&
                                    modelorder.length() > 0 &&
                                    modelorder_by.length() > 0) {

                            } else {
                                modelorder_by = mDefaultOrderBy;
                                modelorder = mDefaultOrder;
                            }
                            apiResourcesModel.setOrder_by(modelorder_by);
                            apiResourcesModel.setOrder(modelorder);
                            ArrayList<Integer> city_ids = new ArrayList<>();
                            city_ids.add(Integer.parseInt(getintentcity_code));
                            apiResourcesModel.setCity_ids(city_ids);
                            apiResourcesModel.setPage(String.valueOf(fieldlistpagesize));
                            apiResourcesModel.setPage_size(10);
                            apiResourcesModel.setHas_physical(1);
                            showProgressDialog();
                            mSearchResListMvpPresenter.getPhyReslist(apiResourcesModel);
                        }
                    } else if (homeintent.getExtras().getInt("is_home_page") == HomePageTypeField) {
                        if (homeintent.getExtras().get("type") != null) {
                            getintentcity_code = LoginManager.getInstance().getTrackcityid();
                            if (homeintent.getExtras().getString("type").equals("daily_sale")) {
                                apiResourcesModel.setHas_subsidy(1);
                                mSearchBestSellCheckBox.setChecked(true);
                            } else if (homeintent.getExtras().getString("type").equals("new_field")) {
                                modelorder_by = mDefaultOrderBy;
                                modelorder = mDefaultOrder;
                            }
                            apiResourcesModel.setHas_physical(1);
                            getorder_by();
                            showProgressDialog();
                            initdata();
                        }
                    }
                    getlistdata();
                } else {
                    apiResourcesModel.setHas_physical(1);
                    intentCityName = homeintent.getStringExtra("cityname");
                    getintentcity_code = homeintent.getStringExtra("cityname_code");
                    searchkeywords = homeintent.getStringExtra("keywords");
                    if (searchkeywords != null && searchkeywords.length() != 0) {
                        medit_search.setText(homeintent.getStringExtra("keywords"));
                    }
                    if (intentCityName == null || getintentcity_code == null) {
                        mlay_no_searchlist.setVisibility(View.VISIBLE);
                    } else {
                        mlay_no_searchlist.setVisibility(View.GONE);
                        if (intentCityName.length() == 0 || getintentcity_code.length() == 0) {
                            mlay_no_searchlist.setVisibility(View.VISIBLE);
                        } else {
                            getorder_by();
                            getlistdata();
                            initview();
                            showProgressDialog();
                            initdata();
                        }

                    }
                }
            } else {
                apiResourcesModel.setHas_physical(1);
                intentCityName = LoginManager.getInstance().getTrackCityName();
                getintentcity_code = LoginManager.getInstance().getTrackcityid();
                searchkeywords = "";
                if (searchkeywords != null && searchkeywords.length() != 0) {
                    medit_search.setText(homeintent.getStringExtra("keywords"));
                }
                if (intentCityName == null || getintentcity_code == null) {
                    mlay_no_searchlist.setVisibility(View.VISIBLE);
                } else {
                    mlay_no_searchlist.setVisibility(View.GONE);
                    if (intentCityName.length() == 0 || getintentcity_code.length() == 0) {
                        mlay_no_searchlist.setVisibility(View.VISIBLE);
                    } else {
                        getorder_by();
                        getlistdata();
                        initview();
                        showProgressDialog();
                        initdata();
                    }
                }
            }
            api = WXAPIFactory.createWXAPI(SearchListFragment.this.getActivity(), Constants.APP_ID);
            api.registerApp(Constants.APP_ID);
            drawable_gray_price_gray = getResources().getDrawable(R.drawable.ic_arrows_one_two_four_one);
            drawable_gray_price_gray.setBounds(0, 0, drawable_gray_price_gray.getMinimumWidth(), drawable_gray_price_gray.getMinimumHeight());
            drawable_gray_price_bottom = getResources().getDrawable(R.drawable.ic_arrows_two_four_one);
            drawable_gray_price_bottom.setBounds(0, 0, drawable_gray_price_bottom.getMinimumWidth(), drawable_gray_price_bottom.getMinimumHeight());
            drawable_gray_price_top = getResources().getDrawable(R.drawable.ic_arrow_two_four_one);
            drawable_gray_price_top.setBounds(0, 0, drawable_gray_price_top.getMinimumWidth(), drawable_gray_price_top.getMinimumHeight());
            mSortGreyUpDrawable = getResources().getDrawable(R.drawable.ic_open_gary_one_button_normal_three);
            mSortGreyUpDrawable.setBounds(0, 0, mSortGreyUpDrawable.getMinimumWidth(), mSortGreyUpDrawable.getMinimumHeight());
            mSortGreyDownDrawable = getResources().getDrawable(R.drawable.ic_open_gary_button_normal_three);
            mSortGreyDownDrawable.setBounds(0, 0, mSortGreyDownDrawable.getMinimumWidth(), mSortGreyDownDrawable.getMinimumHeight());
            mSortBlueUpDrawable = getResources().getDrawable(R.drawable.ic_openup_button_blue_normal_three);
            mSortBlueUpDrawable.setBounds(0, 0, mSortBlueUpDrawable.getMinimumWidth(), mSortBlueUpDrawable.getMinimumHeight());
            mSortBlueDownDrawable = getResources().getDrawable(R.drawable.ic_open_one_button_blue_normal_three);
            mSortBlueDownDrawable.setBounds(0, 0, mSortBlueDownDrawable.getMinimumWidth(), mSortBlueDownDrawable.getMinimumHeight());
            getmscreening_txt_type();
            getAreaList();
            //区域选择状态
            chooseAreaType = -1;
            if (apiResourcesModel.getTrading_area_ids() != null
                    && apiResourcesModel.getTrading_area_ids().size() > 0) {
                chooseAreaType = 2;
            } else if (apiResourcesModel.getDistrict_ids() != null
                    && apiResourcesModel.getDistrict_ids().size() > 0) {
                chooseAreaType = 0;
            } else if (apiResourcesModel.getSubway_station_ids() != null
                    && apiResourcesModel.getSubway_station_ids().size() > 0) {//地铁
                chooseAreaType = 1;
            }
            showChooseAreaState();
        }

        return mMainContent;

    }
    @Override
    public void onResume() {
        super.onResume();
        if (SearchListFragment.this.getActivity() instanceof MainTabActivity) {
            if (mMainTabActivity == null) {
                mMainTabActivity = (MainTabActivity) SearchListFragment.this.getActivity();
            }
            mMainTabActivity.mSearchStatusBarLL.setVisibility(View.VISIBLE);
            if (mMainTabActivity.mNotchHeight > 0) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mMainTabActivity.mNotchHeight);
                mMainTabActivity.mSearchStatusBarLL.setLayoutParams(layoutParams);
            }
            mSearchBackTV.setVisibility(View.GONE);
        }
        MobclickAgent.onPageStart(getResources().getString(R.string.search_activity_name_str));
        MobclickAgent.onResume(SearchListFragment.this.getActivity());
        if (getintentcity_code != null &&
                !getintentcity_code.trim().equals(LoginManager.getInstance().getTrackcityid().trim())
                && (homeintent == null || (homeintent != null && homeintent.getExtras() == null))) {
            getintentcity_code = LoginManager.getInstance().getTrackcityid();
            getlistdata();
            ArrayList<Integer> city_list = new ArrayList<>();
            city_list.add(Integer.parseInt(LoginManager.getInstance().getTrackcityid()));
            apiResourcesModel.setCity_ids(city_list);
            apiResourcesModel.setDistrict_ids(null);
            apiResourcesModel.setTrading_area_ids(null);
            apiResourcesModel.setSubway_station_ids(null);
            msearch_area_type_textview.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
            msearch_area_type_textview.setTextColor(getResources().getColor(R.color.headline_tv_color));
            msearch_area_type_textview.setText(getResources().getString(R.string.invoiceinfo_area_txt));
            searchinitdata();
        } else {
            //浏览记录
            if (LoginManager.isLogin() &&
                    mMainTabActivity != null && mMainTabActivity.isClickTab && isOnResume) {
                try {
                    mMainTabActivity.isClickTab = false;
                    String parameter = "?"+ Request.urlEncode(getBrowseHistoriesUrl());
                    LoginMvpModel.sendBrowseHistories("field_list",parameter,getintentcity_code);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            // FIXME: 2019/1/16 切换环境后刷新
            if (mMainTabActivity != null &&
                    mMainTabActivity.isSearchListRefresh) {
                mMainTabActivity.isSearchListRefresh = false;
                searchinitdata();
            }
        }
        isOnResume = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResources().getString(R.string.search_activity_name_str));
        MobclickAgent.onPause(SearchListFragment.this.getActivity());
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
        if (mSearchResListMvpPresenter != null) {
            mSearchResListMvpPresenter.detachView();
        }
    }

    private void initview() {
        if (LoginManager.getInstance().getCommunity_list_show_guide() == 0) {
            Intent addfield = new Intent(SearchListFragment.this.getActivity(), FieldAddFieldGuideActivity.class);
            addfield.putExtra("show_type",4);
            startActivity(addfield);
        }
        SearchListswipList.setOnRefreshListener(this);
        SearchListswipList.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSearchNoDepositCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchNoDepositCheckBox.isChecked()) {
                    mSearchNoDepositCheckBox.setChecked(true);
                    //选中cb后的修改多选
                    apiResourcesModel.setNot_need_deposit(1);
                    showProgressDialog();
                    searchinitdata();
                } else {
                    mSearchNoDepositCheckBox.setChecked(false);
                    //选中cb后的修改多选
                    apiResourcesModel.setNot_need_deposit(0);
                    showProgressDialog();
                    searchinitdata();
                }
            }
        });
        mSearchBestSellCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchBestSellCheckBox.isChecked()) {
                    mSearchBestSellCheckBox.setChecked(true);
                    // 选中cb后的修改多选
                    apiResourcesModel.setHas_subsidy(1);
                    showProgressDialog();
                    searchinitdata();
                } else {
                    mSearchBestSellCheckBox.setChecked(false);
                    // 选中cb后的修改多选
                    apiResourcesModel.setHas_subsidy(0);
                    showProgressDialog();
                    searchinitdata();
                }
            }
        });
        mSearchHasPhyResCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchHasPhyResCheckBox.isChecked()) {
                    mSearchHasPhyResCheckBox.setChecked(true);
                    // 选中cb后的修改多选
                    apiResourcesModel.setHas_physical(1);
                    showProgressDialog();
                    searchinitdata();
                } else {
                    mSearchBestSellCheckBox.setChecked(false);
                    apiResourcesModel.setHas_physical(0);
                    showProgressDialog();
                    searchinitdata();
                }
            }
        });
        mSearchFastCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchFastCheckBox.isChecked()) {
                    mSearchFastCheckBox.setChecked(true);
                    mSearchFastImgv.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_kuaisuding1_blue_three_six_one));
                    // 选中cb后的修改多选
                    ArrayList<Integer> field_cooperation_type_ids = new ArrayList<>();
                    field_cooperation_type_ids.add(12);
                    apiResourcesModel.setField_cooperation_type_ids(field_cooperation_type_ids);
                    showProgressDialog();
                    searchinitdata();
                } else {
                    mSearchFastCheckBox.setChecked(false);
                    mSearchFastImgv.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_kuaisuding1_gary_three_six_one));
                    apiResourcesModel.setField_cooperation_type_ids(null);
                    showProgressDialog();
                    searchinitdata();
                }
            }
        });
//        mSearchListAppBarL.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (verticalOffset == 0) {
//                    mSearchStatusBarLL.setVisibility(View.VISIBLE);
//                } else if (verticalOffset < com.linhuiba.linhuifield.connector.Constants.Dp2Px(SearchListFragment.this.getContext(),69)) {
//                    mSearchStatusBarLL.setVisibility(View.GONE);
//                }
//            }
//        });
        //价格排序list创建
        if (mSearchPriceList != null) {
            mSearchPriceList.clear();
        }
        HashMap<String,String> map = new HashMap<>();
        map.put("id","min_price");
        map.put("name",getResources().getString(R.string.search_minprice_asc_name_str));
        map.put("order","asc");
        mSearchPriceList.add(map);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("id","min_price");
        hashMap.put("name",getResources().getString(R.string.search_minprice_desc_name_str));
        hashMap.put("order","desc");
        mSearchPriceList.add(hashMap);
    }
    private void initdata() {
        fieldlistpagesize = 1;
        if (mSearchInfoList != null) {
            mSearchInfoList.clear();
        }
        if (mSearchListAdapter != null) {
            mSearchListAdapter.notifyDataSetChanged();
        }
        String keywords = medit_search.getText().toString();
        if (modelorder != null &&
                modelorder_by != null &&
                modelorder.length() > 0 &&
                modelorder_by.length() > 0) {

        } else {
            modelorder_by = mDefaultOrderBy;
            modelorder = mDefaultOrder;
        }
        apiResourcesModel.setOrder_by(modelorder_by);
        apiResourcesModel.setOrder(modelorder);
        ArrayList<Integer> city_ids = new ArrayList<>();
        city_ids.add(Integer.parseInt(getintentcity_code));
        apiResourcesModel.setCity_ids(city_ids);
        apiResourcesModel.setKeywords(keywords);
        if (keywords != null && keywords.length() > 0) {
            apiResourcesModel.setHas_physical(0);
        }
        apiResourcesModel.setPage(String.valueOf(fieldlistpagesize));
        apiResourcesModel.setPage_size(10);
        apiResourcesModel.setLat(lat);
        apiResourcesModel.setLng(lng);
        mlay_no_searchlist.setVisibility(View.GONE);
        mSearchResListMvpPresenter.getPhyReslist(apiResourcesModel);
    }
    private void searchinitdata() {
        if (mSearchInfoList != null) {
            mSearchInfoList.clear();
        }
        if (mSearchListAdapter != null) {
            mSearchListAdapter.notifyDataSetChanged();
        }
        fieldlistpagesize = 1;
        apiResourcesModel.setPage(String.valueOf(fieldlistpagesize));
        apiResourcesModel.setPage_size(10);
        if (medit_search.getText().toString().length() > 0) {
            apiResourcesModel.setKeywords(medit_search.getText().toString());
        }
        mlay_no_searchlist.setVisibility(View.GONE);
        mSearchResListMvpPresenter.getPhyReslist(apiResourcesModel);
    }
    @Override
    public void onRefresh() {
        searchinitdata();
    }

    @OnClick({
            R.id.Sequence,
            R.id.search_area_type_layout,
            R.id.search_price_type_layout,
            R.id.search_area_backimg,
            R.id.edit_search_search,
            R.id.search_jump_map_img,
            R.id.searchlist_nodata_adda_demand_ll,
            R.id.search_list_share_imgv,
            R.id.searchlist_screening_ll
    })
    public void FieldListOnclick(View view) {
        switch (view.getId()) {
            case R.id.Sequence:
                listPopupWindow(mSearchSortList,0);
                break;
            case R.id.search_area_type_layout:
                //2017/12/9 区域选择
                showAreaScreenPW();
                if (msearch_area_type_textview.getText().equals(getResources().getString(R.string.search_area_type_txt))) {
                    msearch_area_type_textview.setCompoundDrawables(null, null, mSortGreyUpDrawable, null);
                } else {
                    msearch_area_type_textview.setCompoundDrawables(null, null, mSortBlueUpDrawable, null);
                }
                break;
            case R.id.search_price_type_layout:
                showSearchPricepw(mSearchPriceList);
                break;
            case R.id.search_area_backimg:
                SearchListFragment.this.getActivity().finish();
                break;
            case R.id.edit_search_search:
                Intent searchatea_seraintent = new Intent(SearchListFragment.this.getActivity(),SearchFieldAreaActivity.class);
                searchatea_seraintent.putExtra("search",3);
                searchatea_seraintent.putExtra("cityname_code",getintentcity_code);
                searchatea_seraintent.putExtra("ksywords", medit_search.getText().toString());
                searchatea_seraintent.putExtra("res_type_id", 1);
                startActivityForResult(searchatea_seraintent, 3);
                break;
            case R.id.search_jump_map_img:
                Intent baiduintent = new Intent(SearchListFragment.this.getActivity(), BaiduMapActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("citycode",getintentcity_code);
                mBundle.putSerializable("ApiResourcesModel",apiResourcesModel);
                mBundle.putSerializable("mSearchAddressBackMap",mSearchAddressBackMap);
                baiduintent.putExtras(mBundle);
                startActivity(baiduintent);
                break;
            case R.id.searchlist_nodata_adda_demand_ll:
                Intent AddDemand = new Intent(SearchListFragment.this.getActivity(),AboutUsActivity.class);
                AddDemand.putExtra("type", Config.ADD_DEMAND_WEB_INT);
                startActivity(AddDemand);
                break;
            case R.id.search_list_share_imgv:
                if (isShare && mSearchInfoList != null && mSearchInfoList.size() > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (mSearchInfoList.size() > 0 &&
                                    mSearchInfoList.get(0).getCommunity_img() != null &&
                                    mSearchInfoList.get(0).getCommunity_img().getPic_url() != null &&
                                    mSearchInfoList.get(0).getCommunity_img().getPic_url().length() > 0) {
                                ShareIconStr = mSearchInfoList.get(0).getCommunity_img().getPic_url().toString() + com.linhuiba.linhuipublic.config.Config.Linhui_Mid_Watermark;
                                ShareBitmap = com.linhuiba.linhuifield.connector.Constants.GetLocalOrNetBitmap(ShareIconStr);//压缩Bitma
                                miniShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 120, 120, true);//压缩Bitmap
                                ShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 360, 288, true);//压缩Bitmap
                                if (ShareBitmap == null) {
                                    ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                                }
                                if (miniShareBitmap == null) {
                                    miniShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                                }
                            } else {
                                ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                                miniShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                            }
                            mHandler.sendEmptyMessage(0);
                        }
                    }).start();
                }
                break;
            case R.id.searchlist_screening_ll:
                isShowScreenAttributes = true;
                show_resources_screening_pw();
                break;
            default:
                break;
        }
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    View myView = SearchListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                    mSearchShareDialog = new AlertDialog.Builder(SearchListFragment.this.getActivity()).create();
                    if (mSearchShareDialog!= null && mSearchShareDialog.isShowing()) {
                        mSearchShareDialog.dismiss();
                    }
                    shareurl = Config.Domain_Name + Config.SHARE_FIELDS_LIST_URL+ getshareurl() + "&BackKey=1&is_app=1";
                    sharewxMinShareLinkUrl = Config.WX_MINI_SHARE_FIELDS_LIST_URL+ getshareurl() + "&BackKey=1&is_app=1";
                    Constants constants = new Constants(SearchListFragment.this.getActivity(),
                            ShareIconStr);
                    constants.shareWXMiniPopupWindow(SearchListFragment.this.getActivity(),myView,mSearchShareDialog,api,shareurl,
                            getResources().getString(R.string.search_fieldlist_activvity_sharetitle_text),
                            getResources().getString(R.string.search_fieldlist_activvity_sharedescription_onetext)+
                                    SharedescriptionStr+getResources().getString(R.string.search_fieldlist_activvity_sharedescription_twotext)
                            ,ShareBitmap,sharewxMinShareLinkUrl,miniShareBitmap,getResources().getString(R.string.search_fieldlist_activvity_sharetitle_text));
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 3:
                if (data.getExtras().get("back") != null) {
                    medit_search.setText(data.getStringExtra("back"));
                } else {
                    medit_search.setText("");
                }
                for (int i = 0; i < mSearchSortList.size(); i++) {
                    if (mSearchSortList.get(i).get("id").equals(mDefaultOrderBy) &&
                            mSearchSortList.get(i).get("order").equals(mDefaultOrder)) {
                        msort_txt.setText(mSearchSortList.get(i).get("display_short_name"));
                        break;
                    }
                }
                msort_txt.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                msort_txt.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                mSortChecked = false;
                msearch_price_type_textview.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                msearch_price_type_textview.setCompoundDrawables(null, null, drawable_gray_price_gray, null);
                price_mark_int = 0;
                Drawable drawable_add_pressed = getResources().getDrawable(R.drawable.ic_screening);
                drawable_add_pressed.setBounds(0, 0, drawable_add_pressed.getMinimumWidth(), drawable_add_pressed.getMinimumHeight());
                mscreening_txt.setCompoundDrawables(null, null, drawable_add_pressed, null);
                mscreening_txt.setTextColor(getResources().getColor(R.color.headline_tv_color));
                apiResourcesModel = new ApiResourcesModel();
                mSearchNoDepositCheckBox.setChecked(false);
                mSearchBestSellCheckBox.setChecked(false);
                mSearchHasPhyResCheckBox.setChecked(false);
                mSearchFastCheckBox.setChecked(false);
                showProgressDialog();
                initdata();
                screening_result_boolean = true;
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void listPopupWindow (final ArrayList<HashMap<String,String>> list, final int type) {
        View myView = SearchListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.activit_search_sort_pw_layout, null);
        DisplayMetrics metric = new DisplayMetrics();
        SearchListFragment.this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        //通过view 和宽·高，构造PopopWindow
        mSearchSortPw = new SupportPopupWindow(myView, width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
        mSearchSortPw.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_bg));
        mSearchSortPw.getBackground().setAlpha(155);
        //设置焦点为可点击
        mSearchSortPw.setFocusable(true);//可以试试设为false的结果
        mSearchSortPw.showAsDropDown(View_view);
        mSearchSortPw.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mSearchSortPw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //将window视图显示在myButton下面
//          mSearchSortPw.showAsDropDown(View_view);
        if (mSortChecked) {
            msort_txt.setCompoundDrawables(null, null, mSortBlueUpDrawable, null);
        } else {
            msort_txt.setCompoundDrawables(null, null, mSortGreyUpDrawable, null);
        }
        mSearchSortPw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mSortChecked) {
                    msort_txt.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                } else {
                    msort_txt.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                }
            }
        });
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchSortPw.isShowing()) {
                    mSearchSortPw.dismiss();
                }
                if (mSortChecked) {
                    msort_txt.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                } else {
                    msort_txt.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                }
            }
        });
        final ListView lv = (ListView) myView.findViewById(R.id.search_sort_pw_lv);
        int selectposition = -1;
        for (int i = 0; i < list.size(); i ++) {
            if (list.get(i).get("id").equals(apiResourcesModel.getOrder_by()) &&
                    list.get(i).get("order").equals(apiResourcesModel.getOrder())) {
                selectposition = i;
            }
        }
        lv.setAdapter(new SearchSortAdapter(SearchListFragment.this.getActivity(), list,selectposition));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //execute the task
                        if (list.get(position).get("id").equals("distance")) {
                            if (lat > 0 && lng > 0) {

                            } else {
                                AndPermission.with(SearchListFragment.this)
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
                                return;
                            }
                        }
                        mSearchSortPw.dismiss();
                        if (mSortChecked) {
                            msort_txt.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                        } else {
                            msort_txt.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                        }
                        if (apiResourcesModel.getMax_price() != null &&
                                apiResourcesModel.getMax_price().length() > 0 &&
                                apiResourcesModel.getMin_price() != null &&
                                apiResourcesModel.getMin_price().length() > 0) {
                            msearch_price_type_textview.setCompoundDrawables(null, null, drawable_gray_price_gray, null);
                            msearch_price_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
                        } else {
                            msearch_price_type_textview.setCompoundDrawables(null, null, drawable_gray_price_gray, null);
                            msearch_price_type_textview.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                        }
                        price_mark_int = 0;//价格排序取消
                        if (type == 0) {
                            apiResourcesModel.setOrder_by(list.get(position).get("id"));
                            apiResourcesModel.setOrder(list.get(position).get("order"));
                            msort_txt.setText(list.get(position).get("display_short_name"));
                            msort_txt.setCompoundDrawables(null, null,mSortBlueDownDrawable , null);
                            msort_txt.setTextColor(getResources().getColor(R.color.default_bluebg));
                            mSortChecked = true;
                        }
                        showProgressDialog();
                        searchinitdata();
                    }

                }, 20);

            }
        });

    }
    private void showSearchPricepw(final ArrayList<HashMap<String,String>> list) {
        View myView = SearchListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.activity_search_price_pw, null);
        DisplayMetrics metric = new DisplayMetrics();
        SearchListFragment.this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        //通过view 和宽·高，构造PopopWindow
        mSearchSortPw = new SupportPopupWindow(myView, width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
        mSearchSortPw.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_bg));
        mSearchSortPw.getBackground().setAlpha(155);
        //设置焦点为可点击
        mSearchSortPw.setFocusable(true);//可以试试设为false的结果
        //将window视图显示在myButton下面
        mSearchSortPw.showAsDropDown(View_view);
        mSearchSortPw.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mSearchSortPw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchSortPw.isShowing()) {
                    mSearchSortPw.dismiss();
                }
                if (mSortChecked) {
                    msort_txt.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                } else {
                    msort_txt.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                }
            }
        });
        final ListView lv = (ListView) myView.findViewById(R.id.search_price_lv);
        final EditText minPriceET = (EditText) myView.findViewById(R.id.search_minimum_edit);
        final EditText maxPriceET = (EditText) myView.findViewById(R.id.search_maximum_edit);
        Button resetBtn = (Button) myView.findViewById(R.id.search_price_reset_btn);
        Button confirmBtn = (Button) myView.findViewById(R.id.search_price_confirm_btn);
        com.linhuiba.linhuifield.connector.Constants.textchangelistener(minPriceET);
        com.linhuiba.linhuifield.connector.Constants.textchangelistener(maxPriceET);
        if (apiResourcesModel.getMin_price() != null &&
                apiResourcesModel.getMin_price().length() > 0) {
            minPriceET.setText(Constants.getpricestring(apiResourcesModel.getMin_price(),0.01));
        }
        if (apiResourcesModel.getMax_price() != null &&
                apiResourcesModel.getMax_price().length() > 0) {
            maxPriceET.setText(Constants.getpricestring(apiResourcesModel.getMax_price(),0.01));
        }

        int selectposition = -1;
        if (price_mark_int == 1) {
            selectposition = 0;
        } else if (price_mark_int == 2) {
            selectposition = 1;
        }
        lv.setAdapter(new SearchSortAdapter(SearchListFragment.this.getActivity(), list,selectposition));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //execute the task
                        mSearchSortPw.dismiss();
                        for (int i = 0; i < mSearchSortList.size(); i++) {
                            if (mSearchSortList.get(i).get("id").equals(mDefaultOrderBy) &&
                                    mSearchSortList.get(i).get("order").equals(mDefaultOrder)) {
                                msort_txt.setText(mSearchSortList.get(i).get("display_short_name"));
                                break;
                            }
                        }
                        msort_txt.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                        msort_txt.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                        mSortChecked = false;
                        if ((price_mark_int == 0 || price_mark_int == 1) &&
                                mSearchPriceList.get(position).get("order") != null &&
                                mSearchPriceList.get(position).get("order").equals("desc") ) {
                            apiResourcesModel.setOrder_by("floor_price");
                            apiResourcesModel.setOrder("desc");
                            showProgressDialog();
                            searchinitdata();
                            price_mark_int = 2;
                            msearch_price_type_textview.setCompoundDrawables(null, null, drawable_gray_price_bottom, null);
                        } else if ((price_mark_int == 0 || price_mark_int == 2) &&
                                mSearchPriceList.get(position).get("order") != null &&
                                mSearchPriceList.get(position).get("order").equals("asc") ){
                            apiResourcesModel.setOrder_by("floor_price");
                            apiResourcesModel.setOrder("asc");
                            showProgressDialog();
                            searchinitdata();
                            msearch_price_type_textview.setCompoundDrawables(null, null, drawable_gray_price_top, null);
                            price_mark_int = 1;
                        }
                        msearch_price_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
                    }

                }, 20);

            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minPriceET.setText("");
                maxPriceET.setText("");
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minPriceET.getText().toString().trim().length() > 0 &&
                        maxPriceET.getText().toString().trim().length() > 0) {
                    if (Double.parseDouble(minPriceET.getText().toString().trim()) >
                            Double.parseDouble(maxPriceET.getText().toString().trim())) {
                        MessageUtils.showToast(getResources().getString(R.string.addfield_price_acceptable_price_error_text));
                        return;
                    }
                }
                if (minPriceET.getText().toString().trim().length() > 0) {
                    apiResourcesModel.setMin_price(Constants.getpricestring(Constants.getpricestring(minPriceET.getText().toString(),1),100));
                } else {
                    apiResourcesModel.setMin_price("");
                }
                if (maxPriceET.getText().toString().trim().length() > 0) {
                    apiResourcesModel.setMax_price(Constants.getpricestring(Constants.getpricestring(maxPriceET.getText().toString(),1),100));
                } else {
                    apiResourcesModel.setMax_price("");
                }
                mSearchSortPw.dismiss();
                showProgressDialog();
                searchinitdata();
                price_mark_int = 0;
                if ((apiResourcesModel.getMax_price() != null &&
                        apiResourcesModel.getMax_price().length() > 0) ||
                        (apiResourcesModel.getMin_price() != null &&
                        apiResourcesModel.getMin_price().length() > 0)) {
                    msearch_price_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
                } else {
                    msearch_price_type_textview.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                }
                msearch_price_type_textview.setCompoundDrawables(null, null, drawable_gray_price_gray, null);
            }
        });
    }
    private void showAreaScreenPW () {
        View myView = SearchListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.activity_search_area_screen_pw, null);
        DisplayMetrics metric = new DisplayMetrics();
        SearchListFragment.this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        //通过view 和宽·高，构造PopopWindow
        mSearchSortPw = new SupportPopupWindow(myView, width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
        mSearchSortPw.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_bg));
        mSearchSortPw.getBackground().setAlpha(155);
        //设置焦点为可点击
        mSearchSortPw.setFocusable(true);//可以试试设为false的结果
        //将window视图显示在myButton下面
        mSearchSortPw.showAsDropDown(View_view);
        mSearchSortPw.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mSearchSortPw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mSearchSortPw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (msearch_area_type_textview.getText().equals(getResources().getString(R.string.search_area_type_txt))) {
                    msearch_area_type_textview.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                } else {
                    msearch_area_type_textview.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                }
                if (isSearchAreaReset &&
                        mSearchAreaTvStr != null && mSearchAreaTvStr.length() > 0 &&
                        !mSearchAreaTvStr.equals(msearch_area_type_textview.getText().toString())) {
                    msearch_area_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
                    msearch_area_type_textview.setText(mSearchAreaTvStr);
                    msearch_area_type_textview.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                    isSearchAreaReset = false;
                }
            }
        });
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchSortPw.isShowing()) {
                    mSearchSortPw.dismiss();
                }
            }
        });
        firstLv = (ListView) myView.findViewById(R.id.search_area_pw_first_lv);
        secondLv = (ListView) myView.findViewById(R.id.search_area_pw_second_lv);
        thirdLv = (ListView) myView.findViewById(R.id.search_area_pw_third_lv);
        Button mSearchAreaConfirmBtn = (Button)myView.findViewById(R.id.search_area_confirm_btn);
        Button mSearchAreaResteBtn = (Button)myView.findViewById(R.id.search_area_reset_btn);
        //判断之前选中的是不是地铁（分两种类型）
        chooseAreaType = -1;
        if (apiResourcesModel.getTrading_area_ids() != null
                && apiResourcesModel.getTrading_area_ids().size() > 0) {
            chooseAreaType = 2;
        } else if (apiResourcesModel.getDistrict_ids() != null
                && apiResourcesModel.getDistrict_ids().size() > 0) {
            chooseAreaType = 0;
        } else if (apiResourcesModel.getSubway_station_ids() != null
                && apiResourcesModel.getSubway_station_ids().size() > 0) {//地铁
            chooseAreaType = 1;
        }
        getAreaList();
        showChooseAreaLV(false);
        firstLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                isSearchAreaReset = false;
                thirdLv.setVisibility(View.GONE);
                if (!SearchAreaPwAdapter.getIsFirstChoose().get(mSearchAreaList.get(position).getId())) {
                    firstChooseInt = position;
                    for (int i = 0; i < mSearchAreaList.size(); i++) {
                        if (position == i) {
                            SearchAreaPwAdapter.getIsFirstChoose().put(mSearchAreaList.get(i).getId(),true);
                        } else {
                            SearchAreaPwAdapter.getIsFirstChoose().put(mSearchAreaList.get(i).getId(),false);
                        }
                    }
                    firstAdapter.notifyDataSetChanged();
                    if (mSearchAreaList.get(position).getId() == chooseAreaType) {
                        showChooseAreaLV(false);
                    } else {
                        for (int i = 0; i < mSearchAreaList.get(position).getSecondList().size(); i++) {
                            if (i == 0
                                    && mSearchAreaList.get(firstChooseInt).getId() == 1) {
                                SearchAreaPwAdapter.getIsSecondChoose().put(mSearchAreaList.get(position).getSecondList().get(i).getId(),true);

                            } else {
                                SearchAreaPwAdapter.getIsSecondChoose().put(mSearchAreaList.get(position).getSecondList().get(i).getId(),false);
                            }
                        }
                        if (mSearchAreaList.get(firstChooseInt).getId() == 1) {
                            secondAdapter = new SearchAreaPwAdapter(SearchListFragment.this.getActivity(), mSearchAreaList.get(position).getSecondList(),4);
                        } else {
                            secondChooseInt = -1;
                            secondAdapter = new SearchAreaPwAdapter(SearchListFragment.this.getActivity(), mSearchAreaList.get(position).getSecondList(),2);
                        }
                        secondLv.setAdapter(secondAdapter);
                        secondLv.setVisibility(View.VISIBLE);
                        if (mSearchAreaList.get(firstChooseInt).getId() == 1) {
                            secondChooseInt = 0;
                            SearchAreaPwAdapter.clearIsThirdChoose();
                            for (int i = 0; i < mSubwayList.get(0).getStations().size(); i++) {
                                SearchAreaPwAdapter.getIsThirdChoose().put(mSubwayList.get(0).getStations().get(i).getId(),false);
                            }
                            thirdAdapter = new SearchAreaPwAdapter(SearchListFragment.this.getActivity(), mSubwayList.get(0).getStations(),3);
                            thirdLv.setAdapter(thirdAdapter);
                            thirdLv.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
        secondLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                isSearchAreaReset = false;
                if (mSearchAreaList.get(firstChooseInt).getId() == 1) {
                    if (!SearchAreaPwAdapter.getIsSecondChoose().get(mSearchAreaList.get(firstChooseInt).getSecondList().get(position).getId())) {
                        secondChooseInt = position;
                        for (int i = 0; i < mSearchAreaList.get(firstChooseInt).getSecondList().size(); i++) {
                            if (i == position) {
                                SearchAreaPwAdapter.getIsSecondChoose().put(mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getId(),true);
                            } else {
                                SearchAreaPwAdapter.getIsSecondChoose().put(mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getId(),false);
                            }
                        }
                        secondAdapter.notifyDataSetChanged();
                        SearchAreaPwAdapter.clearIsThirdChoose();
                        for (int i = 0; i < mSubwayList.get(position).getStations().size(); i++) {
                            SearchAreaPwAdapter.getIsThirdChoose().put(mSubwayList.get(position).getStations().get(i).getId(),false);
                        }
                        thirdAdapter = new SearchAreaPwAdapter(SearchListFragment.this.getActivity(), mSubwayList.get(position).getStations(),3);
                        thirdLv.setAdapter(thirdAdapter);
                        thirdLv.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (SearchAreaPwAdapter.getIsSecondChoose().get(mSearchAreaList.get(firstChooseInt).getSecondList().get(position).getId())) {
                        SearchAreaPwAdapter.getIsSecondChoose().put(mSearchAreaList.get(firstChooseInt).getSecondList().get(position).getId(),false);
                        secondAdapter.notifyDataSetChanged();
                    } else {
                        SearchAreaPwAdapter.getIsSecondChoose().put(mSearchAreaList.get(firstChooseInt).getSecondList().get(position).getId(),true);
                        secondAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        thirdLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isSearchAreaReset = false;
                if (secondChooseInt > -1) {
                    if (SearchAreaPwAdapter.getIsThirdChoose().get(mSubwayList.get(secondChooseInt).getStations().get(i).getId())) {
                        SearchAreaPwAdapter.getIsThirdChoose().put(mSubwayList.get(secondChooseInt).getStations().get(i).getId(),false);
                        thirdAdapter.notifyDataSetChanged();
                    } else {
                        SearchAreaPwAdapter.getIsThirdChoose().put(mSubwayList.get(secondChooseInt).getStations().get(i).getId(),true);
                        thirdAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (SearchAreaPwAdapter.getIsThirdChoose().get(mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getId())) {
                        SearchAreaPwAdapter.getIsThirdChoose().put(mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getId(),false);
                        thirdAdapter.notifyDataSetChanged();
                    } else {
                        SearchAreaPwAdapter.getIsThirdChoose().put(mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getId(),true);
                        thirdAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        mSearchAreaConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchAreaStr = "";
                if (secondChooseInt > -1) {
                    ArrayList<Integer> mSubwayIntList = new ArrayList<>();
                    for (int i = 0; i < mSubwayList.get(secondChooseInt).getStations().size(); i++) {
                        if (SearchAreaPwAdapter.getIsThirdChoose().get(mSubwayList.get(secondChooseInt).getStations().get(i).getId())) {
                            mSubwayIntList.add(mSubwayList.get(secondChooseInt).getStations().get(i).getId());
                            if (searchAreaStr.length() == 0) {
                                searchAreaStr = searchAreaStr + mSubwayList.get(secondChooseInt).getStations().get(i).getStation_name();
                            } else {
                                searchAreaStr = searchAreaStr + "," +
                                        mSubwayList.get(secondChooseInt).getStations().get(i).getStation_name();
                            }
                        }
                    }
                    if (mSubwayIntList.size() > 0) {
                        apiResourcesModel.setSubway_station_ids(mSubwayIntList);
                        apiResourcesModel.setDistrict_ids(null);
                        apiResourcesModel.setTrading_area_ids(null);
                    } else {
                        if (mSearchAreaList.size() > firstChooseInt) {//友盟错误日志修改
                            if (chooseAreaType == mSearchAreaList.get(firstChooseInt).getId()) {
                                apiResourcesModel.setSubway_station_ids(null);
                                apiResourcesModel.setDistrict_ids(null);
                                apiResourcesModel.setTrading_area_ids(null);
                                msearch_area_type_textview.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                                msearch_area_type_textview.setTextColor(getResources().getColor(R.color.headline_tv_color));
                                msearch_area_type_textview.setText(getResources().getString(R.string.invoiceinfo_area_txt));
                                chooseAreaType = -1;
                            }
                        }
                    }
                    if (searchAreaStr.length() > 0) {
                        msearch_area_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
                        msearch_area_type_textview.setText(searchAreaStr);
                        msearch_area_type_textview.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                    }
                } else {
                    if (mSearchAreaList.size() > firstChooseInt) {//友盟错误日志修改
                        if (mSearchAreaList.get(firstChooseInt).getId() == 0) {
                            ArrayList<Integer> mDistrictIntList = new ArrayList<>();
                            for (int i = 0; i < mSearchAreaList.get(firstChooseInt).getSecondList().size(); i++) {
                                if (SearchAreaPwAdapter.getIsSecondChoose().get(mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getId())) {
                                    mDistrictIntList.add(mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getId());
                                    if (searchAreaStr.length() == 0) {
                                        searchAreaStr = searchAreaStr + mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getName();
                                    } else {
                                        searchAreaStr = searchAreaStr + "," +
                                                mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getName();
                                    }
                                }
                            }
                            if (mDistrictIntList.size() > 0) {
                                apiResourcesModel.setDistrict_ids(mDistrictIntList);
                                apiResourcesModel.setTrading_area_ids(null);
                                apiResourcesModel.setSubway_station_ids(null);
                            } else {
                                if (chooseAreaType == mSearchAreaList.get(firstChooseInt).getId()) {
                                    apiResourcesModel.setDistrict_ids(null);
                                    apiResourcesModel.setTrading_area_ids(null);
                                    apiResourcesModel.setSubway_station_ids(null);
                                    msearch_area_type_textview.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                                    msearch_area_type_textview.setTextColor(getResources().getColor(R.color.headline_tv_color));
                                    msearch_area_type_textview.setText(getResources().getString(R.string.invoiceinfo_area_txt));
                                    chooseAreaType = -1;
                                }
                            }

                            if (searchAreaStr.length() > 0) {
                                msearch_area_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
                                msearch_area_type_textview.setText(searchAreaStr);
                                msearch_area_type_textview.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                            }
                        } else if (mSearchAreaList.get(firstChooseInt).getId() == 2) {
                            ArrayList<Integer> mTrading_areaIntList = new ArrayList<>();
                            for (int i = 0; i < mSearchAreaList.get(firstChooseInt).getSecondList().size(); i++) {
                                if (SearchAreaPwAdapter.getIsSecondChoose().get(mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getId())) {
                                    mTrading_areaIntList.add(mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getId());
                                    if (searchAreaStr.length() == 0) {
                                        searchAreaStr = searchAreaStr + mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getName();
                                    } else {
                                        searchAreaStr = searchAreaStr + "," +
                                                mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getName();
                                    }
                                }
                            }
                            if (mTrading_areaIntList.size() > 0) {
                                apiResourcesModel.setTrading_area_ids(mTrading_areaIntList);
                                apiResourcesModel.setDistrict_ids(null);
                                apiResourcesModel.setSubway_station_ids(null);
                            } else {
                                if (chooseAreaType == mSearchAreaList.get(firstChooseInt).getId()) {
                                    apiResourcesModel.setTrading_area_ids(null);
                                    apiResourcesModel.setDistrict_ids(null);
                                    apiResourcesModel.setSubway_station_ids(null);
                                    msearch_area_type_textview.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                                    msearch_area_type_textview.setTextColor(getResources().getColor(R.color.headline_tv_color));
                                    msearch_area_type_textview.setText(getResources().getString(R.string.invoiceinfo_area_txt));
                                    chooseAreaType = -1;
                                }
                            }
                            if (searchAreaStr.length() > 0) {
                                msearch_area_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
                                msearch_area_type_textview.setText(searchAreaStr);
                                msearch_area_type_textview.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                            }
                        }
                    }
                }
                //点击了重置按钮操作
                if (isSearchAreaReset) {
                    apiResourcesModel.setSubway_station_ids(null);
                    apiResourcesModel.setDistrict_ids(null);
                    apiResourcesModel.setTrading_area_ids(null);
                    isSearchAreaReset = false;
                }
                showProgressDialog();
                searchinitdata();
                mSearchSortPw.dismiss();
            }
        });
        mSearchAreaResteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearchAreaReset = true;
                chooseAreaType = -1;
                thirdLv.setVisibility(View.GONE);
                showChooseAreaLV(true);
                mSearchAreaTvStr = msearch_area_type_textview.getText().toString();
                msearch_area_type_textview.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                msearch_area_type_textview.setTextColor(getResources().getColor(R.color.headline_tv_color));
                msearch_area_type_textview.setText(getResources().getString(R.string.invoiceinfo_area_txt));
            }
        });
    }

    private void getorder_by() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String order_by = "";
                order_by = LoginManager.getfieldsort();
                if (order_by != null && order_by.length() > 0) {
                    com.alibaba.fastjson.JSONArray cityarray = JSON.parseArray(order_by);
                    if (cityarray != null) {
                        if (cityarray.size() > 0) {
                            if (mSearchSortList != null) {
                                mSearchSortList.clear();
                            }
                            for (int i = 0; i < cityarray.size(); i++) {
                                com.alibaba.fastjson.JSONObject jsonobject = cityarray.getJSONObject(i);
                                String keyorder_by = jsonobject.getString("order_by");
                                String order = jsonobject.getString("order");
                                String display_name = jsonobject.getString("display_name");
                                if (jsonobject.get("default_sort") != null) {
                                    int mDefaultSort = Integer.parseInt(jsonobject.getString("default_sort"));
                                    if (mDefaultSort == 1) {
                                        if (jsonobject.getString("order_by").length() > 0) {
                                            modelorder_by = jsonobject.getString("order_by");
                                            mDefaultOrderBy = jsonobject.getString("order_by");
                                        }
                                        if (jsonobject.getString("order").length() > 0) {
                                            modelorder = jsonobject.getString("order");
                                            mDefaultOrder = jsonobject.getString("order");
                                        }                                    }
                                }
                                if (!keyorder_by.equals("floor_price")) {
                                    if ((keyorder_by.equals("created_at") &&
                                            order.equals("asc"))) {

                                    } else {
                                        HashMap<String,String> map = new HashMap<>();
                                        map.put("id",keyorder_by);
                                        map.put("name",display_name);
                                        map.put("order",order);
                                        map.put("display_short_name",jsonobject.getString("display_short_name"));
                                        mSearchSortList.add(map);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (modelorder_by == null ||
                            modelorder == null ||
                            modelorder_by.length() == 0 ||
                            modelorder.length() == 0) {
                        modelorder_by = mDefaultOrderBy;
                        modelorder = mDefaultOrder;
                    }
                }
            }
        }).start();
    }
    //搜索条件的界面表示
    private void getmscreening_txt_type() {
        if ((apiResourcesModel.getMin_area() != null && apiResourcesModel.getMin_area().length() > 0) ||
                (apiResourcesModel.getMax_area() != null && apiResourcesModel.getMax_area().length() > 0) ||
                (apiResourcesModel.getMin_person_flow() != null && apiResourcesModel.getMin_person_flow().length() > 0) ||
                (apiResourcesModel.getMax_person_flow() != null && apiResourcesModel.getMax_person_flow().length() > 0) ||
                (apiResourcesModel.getLabel_ids() != null && apiResourcesModel.getLabel_ids().size() > 0)
                || (apiResourcesModel.getLocation_type_ids() != null && apiResourcesModel.getLocation_type_ids().size() > 0)//2018/12/11 位置类型
                || (apiResourcesModel.getCommunity_type_ids() != null && apiResourcesModel.getCommunity_type_ids().size() > 0)
                || (apiResourcesModel.getAttributes() != null && apiResourcesModel.getAttributes().size() > 0)) {
            mscreening_txt.setTextColor(getResources().getColor(R.color.default_bluebg));
            Drawable drawable_add_pressed = getResources().getDrawable(R.drawable.ic_screening_select);
            drawable_add_pressed.setBounds(0, 0, drawable_add_pressed.getMinimumWidth(), drawable_add_pressed.getMinimumHeight());
            mscreening_txt.setCompoundDrawables(null, null, drawable_add_pressed, null);
            screening_result_boolean = true;
        }
        if (apiResourcesModel.getOrder_by() != null && apiResourcesModel.getOrder_by().equals("floor_price")) {
            msearch_price_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
            if (apiResourcesModel.getOrder().equals("desc")) {
                msearch_price_type_textview.setCompoundDrawables(null, null, drawable_gray_price_bottom, null);
            } else if (apiResourcesModel.getOrder().equals("asc")) {
                msearch_price_type_textview.setCompoundDrawables(null, null, drawable_gray_price_top, null);
            }
            return;
        }
        if (apiResourcesModel.getOrder_by() != null &&
                apiResourcesModel.getOrder() != null) {
            if (mSearchSortList != null && mSearchSortList.size() > 0) {
                for (int i = 0; i < mSearchSortList.size(); i++) {
                    if (apiResourcesModel.getOrder_by().equals(mSearchSortList.get(i).get("id")) &&
                            apiResourcesModel.getOrder().equals(mSearchSortList.get(i).get("order"))) {
                        msort_txt.setText(mSearchSortList.get(i).get("display_short_name"));
                    }
                }
            }
        }
        msort_txt.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
        msort_txt.setTextColor(getResources().getColor(R.color.default_bluebg));
        mSortChecked = true;
        if (apiResourcesModel.getNot_need_deposit() != null &&
                apiResourcesModel.getNot_need_deposit() == 1) {
            mSearchNoDepositCheckBox.setChecked(true);
        }
        if (apiResourcesModel.getHas_subsidy() != null &&
                apiResourcesModel.getHas_subsidy() == 1) {
            mSearchBestSellCheckBox.setChecked(true);
        }
        if (apiResourcesModel.getField_cooperation_type_ids() != null &&
                apiResourcesModel.getField_cooperation_type_ids().size() > 0 &&
        apiResourcesModel.getField_cooperation_type_ids().contains(12)) {
            mSearchFastCheckBox.setChecked(true);
            mSearchFastImgv.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_kuaisuding1_blue_three_six_one));
        }
        if (apiResourcesModel.getNot_need_deposit() != null &&
                apiResourcesModel.getNot_need_deposit() == 1) {
            mSearchNoDepositCheckBox.setChecked(true);
        }
        if (apiResourcesModel.getHas_subsidy() != null &&
                apiResourcesModel.getHas_subsidy() == 1) {
            mSearchBestSellCheckBox.setChecked(true);
        }
        if (apiResourcesModel.getHas_physical() == 1) {
            mSearchHasPhyResCheckBox.setChecked(true);
        }
        if (apiResourcesModel.getCommunity_type_ids() != null &&
                apiResourcesModel.getCommunity_type_ids().size() > 0) {
            ArrayList<Integer> Category = new ArrayList<>();
            for (int i = 0; i < apiResourcesModel.getCommunity_type_ids().size(); i++) {
                Category.add(apiResourcesModel.getCommunity_type_ids().get(i));
            }
            if (Category.size() > 0) {
                mSearchResListMvpPresenter.getAttributesList(Category);
            }
        }

    }
    private String getshareurl() {
        String keywords = "";
        String has_physical = "";//有展位 1代表有展位筛选 0或不传代表全部
        String city_ids = "";
        String district_ids = "";
        String trading_area_ids = "";
        String subway_station_ids = "";
        String in_trading_area = "";
        String community_type_ids = "";
        String label_ids = "";
        String locationTypeIds = "";//2018/12/11 位置类型
        String field_cooperation_type_ids = "";//
        String activity_type_ids = "";
        String age_level_ids = "";
        String indoor = "";
        String facilities = "";
        String min_price = "";
        String max_price = "";
        String min_year = "";
        String max_year = "";
        String min_area = "";
        String max_area = "";
        String min_person_flow = "";
        String max_person_flow = "";
        String attributes = "";
        String lat = "";
        String lng = "";
        String latitude = "";
        String longitude = "";
        String latitude_delta = "";
        String longitude_delta = "";
        String nearby = "";
        String not_need_deposit = "";
        String has_subsidy = "";
        String order_by = "";
        String order = "";
        String navigation = "";
        String page_size = "";
        String page = "";
        String zoom_level = "";
        String shareurl = "";
        if (apiResourcesModel.getKeywords() != null) {
            if (apiResourcesModel.getKeywords().length() > 0) {
                keywords = "&keywords="+apiResourcesModel.getKeywords();
            }
        }
        if (apiResourcesModel.getHas_physical() == 1) {
            has_physical = "&has_physical="+String.valueOf(apiResourcesModel.getHas_physical());
        }
        city_ids = "city_ids=" + String.valueOf(apiResourcesModel.getCity_ids().get(0));
        if (apiResourcesModel.getDistrict_ids() != null && apiResourcesModel.getDistrict_ids().size() > 0) {
            String params = "";
            for (int i = 0; i < apiResourcesModel.getDistrict_ids().size(); i++) {
                if (params.length() > 0) {
                    params = params + ",";
                }
                params = params + String.valueOf(apiResourcesModel.getDistrict_ids().get(i));
            }
            district_ids = ("&district_ids=" + params);
        } else {
            district_ids = "&district_ids=";
        }
        if (apiResourcesModel.getTrading_area_ids() != null && apiResourcesModel.getTrading_area_ids().size() > 0) {
            String params = "";
            for (int i = 0; i < apiResourcesModel.getTrading_area_ids().size(); i++) {
                if (params.length() > 0) {
                    params = params + ",";
                }
                params = params + String.valueOf(apiResourcesModel.getTrading_area_ids().get(i));
            }
            trading_area_ids = ("&trading_area_ids=" + params);
        } else {
            trading_area_ids = "&trading_area_ids=";
        }
        if (apiResourcesModel.getSubway_station_ids() != null && apiResourcesModel.getSubway_station_ids().size() > 0) {
            String params = "";
            for (int i = 0; i < apiResourcesModel.getSubway_station_ids().size(); i++) {
                if (params.length() > 0) {
                    params = params + ",";
                }
                params = params + String.valueOf(apiResourcesModel.getSubway_station_ids().get(i));
            }
            subway_station_ids = ("&subway_station_ids=" + params);
        } else {
            subway_station_ids = "&subway_station_ids=";
        }
        if (apiResourcesModel.getIn_trading_area() != null &&
                apiResourcesModel.getIn_trading_area().length() > 0 &&
                apiResourcesModel.getIn_trading_area().equals("1")) {
            in_trading_area = "&subway_station_ids=" + apiResourcesModel.getIn_trading_area();
        }
        if (apiResourcesModel.getCommunity_type_ids() != null && apiResourcesModel.getCommunity_type_ids().size() > 0) {
            String params = "";
            for (int i = 0; i < apiResourcesModel.getCommunity_type_ids().size(); i++) {
                if (params.length() > 0) {
                    params = params + ",";
                }
                params = params + String.valueOf(apiResourcesModel.getCommunity_type_ids().get(i));
            }
            community_type_ids = ("&community_type_ids=" + params);
        }
        if (apiResourcesModel.getLabel_ids() != null && apiResourcesModel.getLabel_ids().size() > 0) {
            String params = "";
            for (int i = 0; i < apiResourcesModel.getLabel_ids().size(); i++) {
                if (params.length() > 0) {
                    params = params + ",";
                }
                params = params + String.valueOf(apiResourcesModel.getLabel_ids().get(i));
            }
            label_ids = ("&label_ids=" + params);
        }
        //2018/12/11 分享时加位置类型
        if (apiResourcesModel.getLocation_type_ids() != null && apiResourcesModel.getLocation_type_ids().size() > 0) {
            String params = "";
            for (int i = 0; i < apiResourcesModel.getLocation_type_ids().size(); i++) {
                if (params.length() > 0) {
                    params = params + ",";
                }
                params = params + String.valueOf(apiResourcesModel.getLocation_type_ids().get(i));
            }
            locationTypeIds = ("&location_type_ids=" + params);
        }
        if (apiResourcesModel.getField_cooperation_type_ids() != null && apiResourcesModel.getField_cooperation_type_ids().size() > 0) {
            String params = "";
            for (int i = 0; i < apiResourcesModel.getField_cooperation_type_ids().size(); i++) {
                if (params.length() > 0) {
                    params = params + ",";
                }
                params = params + String.valueOf(apiResourcesModel.getField_cooperation_type_ids().get(i));
            }
            field_cooperation_type_ids = ("&field_cooperation_type_ids=" + params);
        }
        if (apiResourcesModel.getActivity_type_ids() != null && apiResourcesModel.getActivity_type_ids().size() > 0) {
            String params = "";
            for (int i = 0; i < apiResourcesModel.getActivity_type_ids().size(); i++) {
                if (params.length() > 0) {
                    params = params + ",";
                }
                params = params + String.valueOf(apiResourcesModel.getActivity_type_ids().get(i));
            }
            activity_type_ids = ("&activity_type_ids=" + params);
        }
        if (apiResourcesModel.getAge_level_ids() != null && apiResourcesModel.getAge_level_ids().size() > 0) {
            String params = "";
            for (int i = 0; i < apiResourcesModel.getAge_level_ids().size(); i++) {
                if (params.length() > 0) {
                    params = params + ",";
                }
                params = params + String.valueOf(apiResourcesModel.getAge_level_ids().get(i));
            }
            age_level_ids = ("&age_level_ids=" + params);
        }
        if (apiResourcesModel.getIndoor() != null && apiResourcesModel.getIndoor().size() > 0) {
            String params = "";
            for (int i = 0; i < apiResourcesModel.getIndoor().size(); i++) {
                if (params.length() > 0) {
                    params = params + ",";
                }
                params = params + String.valueOf(apiResourcesModel.getIndoor().get(i));
            }
            indoor = ("&indoor=" + params);
        }
        if (apiResourcesModel.getFacilities() != null && apiResourcesModel.getFacilities().size() > 0) {
            String params = "";
            for (int i = 0; i < apiResourcesModel.getFacilities().size(); i++) {
                if (params.length() > 0) {
                    params = params + ",";
                }
                params = params + apiResourcesModel.getFacilities().get(i);
            }
            facilities = ("&facilities=" + params);
        }
        if (apiResourcesModel.getMin_price() != null && apiResourcesModel.getMin_price().length() > 0) {
            min_price = ("&min_price=" +  Constants.getpricestring(apiResourcesModel.getMin_price(),0.01));
        }
        if (apiResourcesModel.getMax_price() != null && apiResourcesModel.getMax_price().length() > 0) {
            max_price = ("&max_price=" + Constants.getpricestring(apiResourcesModel.getMax_price(),0.01));
        }
        if (apiResourcesModel.getMin_year() != null) {
            min_year = ("&min_year=" + String.valueOf(apiResourcesModel.getMin_year()));
        }
        if (apiResourcesModel.getMax_year() != null) {
            max_year = ("&max_year=" + String.valueOf(apiResourcesModel.getMax_year()));
        }
        if (apiResourcesModel.getMin_area() != null && apiResourcesModel.getMin_area().length() > 0) {
            min_area = ("&min_area=" + apiResourcesModel.getMin_area());
        }
        if (apiResourcesModel.getMax_area() != null && apiResourcesModel.getMax_area().length() > 0) {
            max_area = ("&max_area=" + apiResourcesModel.getMax_area());
        }
        if (apiResourcesModel.getMin_person_flow() != null && apiResourcesModel.getMin_person_flow().length() > 0) {
            min_person_flow = ("&min_person_flow=" + apiResourcesModel.getMin_person_flow());
        }
        if (apiResourcesModel.getMax_person_flow() != null && apiResourcesModel.getMax_person_flow().length() > 0) {
            max_person_flow = ("&max_person_flow=" + apiResourcesModel.getMax_person_flow());
        }
        if (apiResourcesModel.getAttributes() != null && apiResourcesModel.getAttributes().size() > 0) {
            attributes = ("&attributes=" + JSON.toJSONString(apiResourcesModel.getAttributes()));
        }
        if (apiResourcesModel.getLat() > 0) {
            lat = ("&lat=" + String.valueOf(apiResourcesModel.getLat()));
        }
        if (apiResourcesModel.getLng() > 0) {
            lng = ("&lng=" + String.valueOf(apiResourcesModel.getLng()));
        }
        if (apiResourcesModel.getLatitude() > 0) {
            latitude = ("&latitude=" + String.valueOf(apiResourcesModel.getLatitude()));
        }
        if (apiResourcesModel.getLongitude() > 0) {
            longitude = ("&longitude=" + String.valueOf(apiResourcesModel.getLongitude()));
        }
        if (apiResourcesModel.getLatitude_delta() > 0) {
            latitude_delta = ("&latitude_delta=" + String.valueOf(apiResourcesModel.getLatitude_delta()));
        }
        if (apiResourcesModel.getLongitude_delta() > 0) {
            longitude_delta = ("&longitude_delta=" + String.valueOf(apiResourcesModel.getLongitude_delta()));
        }
        if (apiResourcesModel.getNearby() > 0) {
            nearby = ("&nearby=" + String.valueOf(apiResourcesModel.getNearby()));
        }
        if (apiResourcesModel.getNot_need_deposit() != null &&
                apiResourcesModel.getNot_need_deposit() == 1) {
            not_need_deposit = ("&not_need_deposit=" + String.valueOf(apiResourcesModel.getNot_need_deposit()));
        }
        if (apiResourcesModel.getHas_subsidy() != null &&
                apiResourcesModel.getHas_subsidy() == 1) {
            has_subsidy = ("&has_subsidy=" + String.valueOf(apiResourcesModel.getHas_subsidy()));
        }
        if (apiResourcesModel.getOrder_by() != null &&
                apiResourcesModel.getOrder_by().length() > 0) {
            order_by = ("&order_by=" + apiResourcesModel.getOrder_by());
        }
        if (apiResourcesModel.getOrder() != null &&
                apiResourcesModel.getOrder().length() > 0) {
            order = ("&order=" + apiResourcesModel.getOrder());
        }
        if (apiResourcesModel.getNavigation() != null &&
                apiResourcesModel.getNavigation() == 1) {
            navigation = ("&navigation=" + String.valueOf(apiResourcesModel.getOrder()));
        }
        page_size = ("&page_size=" + String.valueOf(apiResourcesModel.getPage_size()));
        page = ("&page=" + apiResourcesModel.getPage());
        if (apiResourcesModel.getZoom_level() > 0) {
            zoom_level = ("&zoom_level=" + String.valueOf(apiResourcesModel.getOrder()));
        }

        shareurl = city_ids+keywords+has_physical+district_ids+trading_area_ids+subway_station_ids+in_trading_area+community_type_ids+label_ids+locationTypeIds+field_cooperation_type_ids+
                activity_type_ids+age_level_ids+indoor+ facilities + min_price+max_price+min_year+max_year+
                min_area+max_area+min_person_flow+max_person_flow+attributes+lat+lng+latitude+longitude+latitude_delta+longitude_delta+nearby+
                not_need_deposit+has_subsidy+order_by+order+navigation
                +page_size + page + zoom_level;
        return shareurl;
    }

    //筛选修改
    private void show_resources_screening_pw() {
        field_labels_int = 0;
        category_id_int = 0;
        mLocationTypeChooseInt = 0;
        attributesChooseMap= new HashMap<>();
        View myView = SearchListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.activity_resourcesscreening, null);
        mresourcesscreening_stickygridview_new  = (ListView)myView.findViewById(R.id.resourcesscreening_stickygridview_new);
        Button mresetbtn = (Button)myView.findViewById(R.id.resetbtn);
        mSearchConfirmBtn = (Button)myView.findViewById(R.id.confirmbtn);
        DisplayMetrics metric = new DisplayMetrics();
        SearchListFragment.this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;

        int width_screening = metric.widthPixels;   // 屏幕宽度（像素）
        if ((width_screening -40)/4 > 174 ||(width_screening -40)/4 == 174 || (width_screening -40)/4 > 166) {
            GridviewNumColumns = 4;
        } else  {
            GridviewNumColumns = 3;
        }
        setResScreeningAdapter();
        mSearchConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭软键盘
                InputMethodManager imm = (InputMethodManager) SearchListFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(mresourcesscreening_stickygridview_new.getWindowToken(), 0);
                }
                mresourcesscreening_stickygridview_new.clearFocus();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        String mminimum_yearedit = "";
                        String mmaximum_yearedit = "";
                        String area_edit = "";
                        String area_max_edit = "";
                        String min_person_flow_edit = "";
                        String max_person_flow_edit = "";
                        ArrayList<Integer> fieldlabel_list = new ArrayList<>();
                        ArrayList<Integer> locationTypeIdsList = new ArrayList<>();//2018/12/11 位置类型确认按钮操作
                        ArrayList<Integer> communityTypeList = new ArrayList<>();
                        ArrayList<SearchListAttributesModel> attributesList = new ArrayList<>();
                        for (int j = 0;j < data_new.size(); j++ ) {
                            if (data_new.get(j).get("datalist") != null && ((ArrayList<HashMap<Object,Object>>)data_new.get(j).get("datalist")).size() > 0) {
                                ArrayList<HashMap<Object,Object>> data_new_temp = new ArrayList<>();
                                data_new_temp.addAll(((ArrayList<HashMap<Object,Object>>) (data_new.get(j).get("datalist"))));
                                for (int i = 0; i <data_new_temp.size(); i++) {
                                    if (data_new_temp.get(i).get("itemtype") != null && ((Integer)data_new_temp.get(i).get("itemtype") == 1 ||
                                            (Integer)data_new_temp.get(i).get("itemtype") == 2)) {

                                    } else {
                                        if ((boolean)ResourcesScreeningItemAdapter.getresourcescreeninglist().get(data_new_temp.get(i).get("type").toString()+data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())) {
                                            if (data_new_temp.get(i).get("type").toString().equals("field_labels")) {
                                                fieldlabel_list.add(Integer.parseInt(data_new_temp.get(i).get("id").toString()));
                                            } else if (data_new_temp.get(i).get("type").toString().equals("location_types")) {//2018/12/11 位置类型
                                                locationTypeIdsList.add(Integer.parseInt(data_new_temp.get(i).get("id").toString()));
                                            } else if (data_new_temp.get(i).get("type").toString().equals("category")) {
                                                communityTypeList.add(Integer.parseInt(mCategoryMap.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())));
                                            } else if (data_new_temp.get(i).get("type").toString().indexOf("attributes") != -1) {
                                                if (attributesList.size() > 0) {
                                                    boolean isHasAttribute = false;
                                                    for (int l = 0; l <attributesList.size(); l++) {
                                                        if (attributesList.get(l).getId() ==
                                                                Integer.parseInt(data_new_temp.get(i).get("attribute_id").toString())) {
                                                            isHasAttribute = true;
                                                            attributesList.get(l).getOption_ids().add(Integer.parseInt(data_new_temp.get(i).get("id").toString()));
                                                        }
                                                    }
                                                    if (!isHasAttribute) {
                                                        SearchListAttributesModel searchListAttributesModel =  new SearchListAttributesModel();
                                                        searchListAttributesModel.setId(Integer.parseInt(data_new_temp.get(i).get("attribute_id").toString()));
                                                        ArrayList<Integer> option_ids = new ArrayList<>();
                                                        option_ids.add(Integer.parseInt(data_new_temp.get(i).get("id").toString()));
                                                        searchListAttributesModel.setOption_ids(option_ids);
                                                        attributesList.add(searchListAttributesModel);
                                                    }
                                                } else {
                                                    SearchListAttributesModel searchListAttributesModel =  new SearchListAttributesModel();
                                                    searchListAttributesModel.setId(Integer.parseInt(data_new_temp.get(i).get("attribute_id").toString()));
                                                    ArrayList<Integer> option_ids = new ArrayList<>();
                                                    option_ids.add(Integer.parseInt(data_new_temp.get(i).get("id").toString()));
                                                    searchListAttributesModel.setOption_ids(option_ids);
                                                    attributesList.add(searchListAttributesModel);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (data_new.get(j).get("type").toString().equals("year")) {
                                    if (ResourcesScreeningNewAdapter.getedittextmap().get("yearmin").toString().length() > 0) {
                                        if (Integer.parseInt(ResourcesScreeningNewAdapter.getedittextmap().get("yearmin").toString()) < 1900 ||
                                                Integer.parseInt(ResourcesScreeningNewAdapter.getedittextmap().get("yearmin").toString()) > 2099) {
                                            MessageUtils.showToast(getResources().getString(R.string.module_searchlist_buile_year_error_msg));
                                            ResourcesScreeningNewAdapter.getedittextmap().put("yearmin","");
                                            resourcesScreeningNewAdapter.notifyDataSetChanged();
                                            return;
                                        }
                                    }
                                    if (ResourcesScreeningNewAdapter.getedittextmap().get("yearmax").toString().length() > 0) {
                                        if (Integer.parseInt(ResourcesScreeningNewAdapter.getedittextmap().get("yearmax").toString()) < 1900 ||
                                                Integer.parseInt(ResourcesScreeningNewAdapter.getedittextmap().get("yearmax").toString()) > 2099) {
                                            MessageUtils.showToast(getResources().getString(R.string.module_searchlist_buile_year_error_msg));
                                            ResourcesScreeningNewAdapter.getedittextmap().put("yearmax","");
                                            resourcesScreeningNewAdapter.notifyDataSetChanged();
                                            return;
                                        }
                                    }
                                    if (ResourcesScreeningNewAdapter.getedittextmap().get("yearmin").toString().length() > 0 &&
                                            ResourcesScreeningNewAdapter.getedittextmap().get("yearmax").toString().length() > 0) {
                                        if (Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("yearmin").toString(), 1),1)) >
                                                Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("yearmax").toString(), 1),1))) {
                                            ResourcesScreeningNewAdapter.getedittextmap().put("yearmin","");
                                            ResourcesScreeningNewAdapter.getedittextmap().put("yearmax","");
                                            MessageUtils.showToast(getResources().getString(R.string.module_searchlist_buile_year_error_hind));
                                            resourcesScreeningNewAdapter.notifyDataSetChanged();
                                            return;
                                        }
                                    }
                                    mminimum_yearedit = ResourcesScreeningNewAdapter.getedittextmap().get("yearmin").toString();
                                    mmaximum_yearedit = ResourcesScreeningNewAdapter.getedittextmap().get("yearmax").toString();
                                } else if (data_new.get(j).get("type").toString().equals("area")) {
                                    if (ResourcesScreeningNewAdapter.getedittextmap().get("areamin").toString().length() > 0 &&
                                            ResourcesScreeningNewAdapter.getedittextmap().get("areamax").toString().length() > 0) {
                                        if (Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("areamin").toString(), 1),1)) >
                                                Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("areamax").toString(), 1),1))) {
                                            ResourcesScreeningNewAdapter.getedittextmap().put("areamin","");
                                            ResourcesScreeningNewAdapter.getedittextmap().put("areamax","");
                                            MessageUtils.showToast(getResources().getString(R.string.module_searchlist_area_error_hind));
                                            resourcesScreeningNewAdapter.notifyDataSetChanged();
                                            return;
                                        }
                                    }
                                    area_edit = ResourcesScreeningNewAdapter.getedittextmap().get("areamin").toString();
                                    area_max_edit = ResourcesScreeningNewAdapter.getedittextmap().get("areamax").toString();
                                } else if (data_new.get(j).get("type").toString().equals("numper_of_people")) {
                                    if (ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemin").toString().length() > 0 &&
                                            ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemax").toString().length() > 0) {
                                        if (Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemin").toString(), 1),1)) >
                                                Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemax").toString(), 1),1))) {
                                            ResourcesScreeningNewAdapter.getedittextmap().put("numper_of_peoplemin","");
                                            ResourcesScreeningNewAdapter.getedittextmap().put("numper_of_peoplemax","");
                                            MessageUtils.showToast(getResources().getString(R.string.module_searchlist_area_error_hind));
                                            resourcesScreeningNewAdapter.notifyDataSetChanged();
                                            return;
                                        }
                                    }
                                    min_person_flow_edit = ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemin").toString();
                                    max_person_flow_edit = ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemax").toString();
                                }
                            }
                        }
                        apiResourcesModel.setLabel_ids(fieldlabel_list);
                        apiResourcesModel.setLocation_type_ids(locationTypeIdsList);//2018/12/11 赋值位置类型到筛选
                        apiResourcesModel.setCommunity_type_ids(communityTypeList);
                        apiResourcesModel.setAttributes(attributesList);
                        if (mminimum_yearedit.length() > 0) {
                            apiResourcesModel.setMin_year(Integer.parseInt(mminimum_yearedit));
                        } else {
                            apiResourcesModel.setMin_year(null);
                        }
                        if (mmaximum_yearedit.length() > 0) {
                            apiResourcesModel.setMax_year(Integer.parseInt(mmaximum_yearedit));
                        } else {
                            apiResourcesModel.setMax_year(null);
                        }
                        if (area_edit.length() > 0) {
                            apiResourcesModel.setMin_area(Constants.getpricestring(area_edit,1));
                        } else {
                            apiResourcesModel.setMin_area(null);
                        }
                        if (area_max_edit.length() > 0) {
                            apiResourcesModel.setMax_area(Constants.getpricestring(area_max_edit,1));
                        } else {
                            apiResourcesModel.setMax_area(null);
                        }
                        if (min_person_flow_edit.length() > 0) {
                            apiResourcesModel.setMin_person_flow(Constants.getpricestring(min_person_flow_edit,1));
                        } else {
                            apiResourcesModel.setMin_person_flow(null);
                        }
                        if (max_person_flow_edit.length() > 0) {
                            apiResourcesModel.setMax_person_flow(Constants.getpricestring(max_person_flow_edit,1));
                        } else {
                            apiResourcesModel.setMax_person_flow(null);
                        }
                        if (apiResourcesModel.getMin_area() != null || apiResourcesModel.getMax_area() != null||
                                apiResourcesModel.getMin_person_flow() != null  || apiResourcesModel.getMax_person_flow() != null
                                || apiResourcesModel.getLabel_ids().size() > 0
                                || apiResourcesModel.getLocation_type_ids().size() > 0 //2018/12/11 位置类型
                                || apiResourcesModel.getCommunity_type_ids().size() > 0
                                || apiResourcesModel.getAttributes().size() > 0) {
                            mscreening_txt.setTextColor(getResources().getColor(R.color.default_bluebg));
                            Drawable drawable_add_pressed = getResources().getDrawable(R.drawable.ic_screening_select);
                            drawable_add_pressed.setBounds(0, 0, drawable_add_pressed.getMinimumWidth(), drawable_add_pressed.getMinimumHeight());
                            mscreening_txt.setCompoundDrawables(null, null, drawable_add_pressed, null);
                            screening_result_boolean = true;
                        } else {
                            Drawable drawable_add_pressed = getResources().getDrawable(R.drawable.ic_screening);
                            drawable_add_pressed.setBounds(0, 0, drawable_add_pressed.getMinimumWidth(), drawable_add_pressed.getMinimumHeight());
                            mscreening_txt.setCompoundDrawables(null, null, drawable_add_pressed, null);
                            mscreening_txt.setTextColor(getResources().getColor(R.color.headline_tv_color));
                            screening_result_boolean = false;
                        }
                        showProgressDialog();
                        searchinitdata();
                        if (screening_pw.isShowing()) {
                            screening_pw.dismiss();
                        }
                    }
                }, 100);
            }
        });
        mresetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResourcesScreeningItemAdapter.clear_resourcescreeninglist();
                for (int j = 0;j < data_new.size(); j++ ) {
                    if (data_new.get(j).get("datalist") != null && ((ArrayList<HashMap<Object,Object>>)data_new.get(j).get("datalist")).size() > 0) {
                        ArrayList<HashMap<Object,Object>> data_new_temp = new ArrayList<>();
                        data_new_temp.addAll(((ArrayList<HashMap<Object,Object>>) (data_new.get(j).get("datalist"))));
                        for (int i = 0; i <data_new_temp.size(); i++) {
                            if (data_new_temp.get(i).get("itemtype") != null && ((Integer)data_new_temp.get(i).get("itemtype") == 1 ||
                                    (Integer)data_new_temp.get(i).get("itemtype") == 2)) {
                            } else {
                                ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString() + data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(), false);
                            }
                        }
                    } else {
                        ResourcesScreeningNewAdapter.getedittextmap().put(data_new.get(j).get("type").toString()+"min","");
                        ResourcesScreeningNewAdapter.getedittextmap().put(data_new.get(j).get("type").toString()+"max","");
                    }
                }
                ArrayList<HashMap<Object,Object>> copyData = new ArrayList<>();
                for (int i = 0; i < data_new.size(); i++) {
                    if (data_new.get(i).get("type") != null &&
                            data_new.get(i).get("type").toString().indexOf("attributes") != -1) {
                    } else {
                        copyData.add(data_new.get(i));
                    }
                }
                data_new.clear();
                data_new.addAll(copyData);
                resourcesScreeningNewAdapter.notifyDataSetChanged();
                mSearchConfirmBtn.setEnabled(true);
                mSearchConfirmBtn.setBackgroundColor(getResources().getColor(R.color.default_bluebg));
                mSearchConfirmBtn.setText(getResources().getString(R.string.confirm));
            }
        });
        //通过view 和宽·高，构造PopopWindow
        screening_pw = new SupportPopupWindow(myView, width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
        screening_pw.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_bg));
        screening_pw.getBackground().setAlpha(155);
        //设置焦点为可点击
        screening_pw.setFocusable(true);//可以试试设为false的结果
        screening_pw.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        screening_pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //将window视图显示在myButton下面
        screening_pw.showAsDropDown(View_view);
        screening_pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (screening_pw.isShowing()) {
                    screening_pw.dismiss();
                }
            }
        });
    }
    private void getlistdata() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDistrictList= new ArrayList<>();
                mSubwayList = new ArrayList<>();
                mTradingAreasList = new ArrayList<>();
                field_labels_list = new ArrayList<>();
                mSubwayLineList = new ArrayList<>();
                mCategoryMap = new HashMap<>();
                mCategoryList = new ArrayList<>();
                if (getintentcity_code == null || getintentcity_code.length() == 0) {
                    getintentcity_code = "90";
                }
                List<ConfigCityParameterModel> districts = ConfigSqlOperation.selectSQL(1,Integer.parseInt(getintentcity_code),SearchListFragment.this.getContext());
                List<ConfigCityParameterModel> labels = ConfigSqlOperation.selectSQL(2,Integer.parseInt(getintentcity_code),SearchListFragment.this.getContext());
                List<ConfigCityParameterModel> trading_areas = ConfigSqlOperation.selectSQL(3,Integer.parseInt(getintentcity_code),SearchListFragment.this.getContext());
                List<ConfigCityParameterModel> subway_stations = ConfigSqlOperation.selectSQL(4,Integer.parseInt(getintentcity_code),SearchListFragment.this.getContext());
                List<ConfigCityParameterModel> districtsList = new ArrayList<>();
                List<ConfigCityParameterModel> labelsList = new ArrayList<>();
                List<ConfigCityParameterModel> trading_areasList = new ArrayList<>();
                List<ConfigCityParameterModel> subway_stationsList = new ArrayList<>();
                ArrayList<ConfigCitiesModel> citylist = ConfigurationsModel.getInstance().getCitylist();
                String categoryStr = LoginManager.getCommunity_type();
                String locationTypeStr = LoginManager.getLocation_type_ids();
                if (categoryStr != null && categoryStr.length() > 0) {
                    JSONArray categoryArray = JSONArray.parseArray(categoryStr);
                    if (categoryArray != null && categoryArray.size() > 0) {
                        for (int i = 0; i < categoryArray.size(); i++) {
                            com.alibaba.fastjson.JSONObject jsonobject = categoryArray.getJSONObject(i);
                            String key = jsonobject.getString("id");
                            String value = jsonobject.getString("display_name");
                            mCategoryList.add(value);
                            mCategoryMap.put(value,key);
                        }

                    }
                }
                if (locationTypeStr != null && locationTypeStr.length() > 0) {
                    mLocationTypeList = JSONArray.parseArray(locationTypeStr,ConfigCityParameterModel.class);
                }

                int defaultCityInt = -1;
                if (citylist != null && citylist.size() > 0) {
                    for (int i = 0; i < citylist.size(); i++) {
                        if (String.valueOf(citylist.get(i).getCity_id()).equals(getintentcity_code)) {
                            defaultCityInt = i;
                            break;
                        }
                    }
                }
                if (districts != null &&
                        districts.size() > 0) {
                    districtsList.addAll(districts);
                } else {
                    if (defaultCityInt > -1) {
                        districtsList.addAll(citylist.get(defaultCityInt).getDistricts());
                    }
                }
                if (labels != null &&
                        labels.size() > 0) {
                    labelsList.addAll(labels);
                } else {
                    if (defaultCityInt > -1) {
                        labelsList.addAll(citylist.get(defaultCityInt).getLabels());
                    }
                }
                if (trading_areas != null &&
                        trading_areas.size() > 0) {
                    trading_areasList.addAll(trading_areas);
                } else {
                    if (defaultCityInt > -1) {
                        trading_areasList.addAll(citylist.get(defaultCityInt).getTrading_areas());
                    }
                }

                if (districtsList != null && districtsList.size() > 0) {
                    for (int j = 0; j < districtsList.size(); j++) {
                        String name = districtsList.get(j).getName();
                        String district_id = String.valueOf(districtsList.get(j).getDistrict_id());
                        SearchAreaPwModel searchAreaPwModel = new SearchAreaPwModel();
                        searchAreaPwModel.setId(Integer.parseInt(district_id));
                        searchAreaPwModel.setName(name);
                        mDistrictList.add(searchAreaPwModel);

                    }
                }
                if (labelsList != null &&
                        labelsList.size() > 0) {
                    field_labels_list.addAll(labelsList);
                }
                if (trading_areasList != null &&
                        trading_areasList.size() > 0) {
                    for (int j = 0; j < trading_areasList.size(); j++) {
                        String labelkey = String.valueOf(trading_areasList.get(j).getId());
                        String labelvalue = trading_areasList.get(j).getName();
                        SearchAreaPwModel searchAreaPwModel = new SearchAreaPwModel();
                        searchAreaPwModel.setId(Integer.parseInt(labelkey));
                        searchAreaPwModel.setName(labelvalue);
                        mTradingAreasList.add(searchAreaPwModel);
                    }
                }
                if (defaultCityInt > -1 &&
                        citylist.get(defaultCityInt).getSubway_stations() != null &&
                        citylist.get(defaultCityInt).getSubway_stations().size() > 0) {
                    subway_stationsList.addAll(citylist.get(defaultCityInt).getSubway_stations());
                    if (subway_stationsList != null && subway_stationsList.size() > 0) {
                        for (int i = 0; i < subway_stationsList.size(); i++) {
                            if (subway_stationsList.get(i).getStations() != null &&
                                    subway_stationsList.get(i).getStations().size() > 0) {
                                ArrayList<SearchAreaPwModel> stations = new ArrayList<>();
                                for (int j = 0; j < subway_stationsList.get(i).getStations().size(); j++) {
                                    SearchAreaPwModel searchAreaPwModel = new SearchAreaPwModel();
                                    searchAreaPwModel.setId(subway_stationsList.get(i).getStations().get(j).getId());
                                    searchAreaPwModel.setName(subway_stationsList.get(i).getStations().get(j).getName());
                                    searchAreaPwModel.setStation_name(subway_stationsList.get(i).getStations().get(j).getStation_name());
                                    stations.add(searchAreaPwModel);
                                }
                                SearchAreaSubwayPwModel searchAreaSubwayPwModel = new SearchAreaSubwayPwModel();
                                searchAreaSubwayPwModel.setName(subway_stationsList.get(i).getName());
                                searchAreaSubwayPwModel.setId(String.valueOf(subway_stationsList.get(i).getId()));
                                searchAreaSubwayPwModel.setStations(stations);
                                mSubwayList.add(searchAreaSubwayPwModel);
                                SearchAreaPwModel subwayLineModel = new SearchAreaPwModel();
                                subwayLineModel.setName(subway_stationsList.get(i).getName());
                                subwayLineModel.setId(subway_stationsList.get(i).getId());
                                mSubwayLineList.add(subwayLineModel);
                            }
                        }
                    }

                } else {
                    subway_stationsList.addAll(subway_stations);
                    if (subway_stationsList != null && subway_stationsList.size() > 0) {
                        for (int i = 0; i < subway_stationsList.size(); i++) {
                            List<ConfigCityParameterModel> stationsList = ConfigSqlOperation.selectSQL(5,subway_stationsList.get(i).getSubway_line_id(),SearchListFragment.this.getContext());
                            if (stationsList != null &&
                                    stationsList.size() > 0) {
                                ArrayList<SearchAreaPwModel> stations = new ArrayList<>();
                                for (int j = 0; j < stationsList.size(); j++) {
                                    SearchAreaPwModel searchAreaPwModel = new SearchAreaPwModel();
                                    searchAreaPwModel.setId(stationsList.get(j).getId());
                                    searchAreaPwModel.setName(stationsList.get(j).getName());
                                    searchAreaPwModel.setStation_name(stationsList.get(j).getStation_name());
                                    stations.add(searchAreaPwModel);
                                }
                                SearchAreaSubwayPwModel searchAreaSubwayPwModel = new SearchAreaSubwayPwModel();
                                searchAreaSubwayPwModel.setName(subway_stationsList.get(i).getName());
                                searchAreaSubwayPwModel.setId(String.valueOf(subway_stationsList.get(i).getId()));
                                searchAreaSubwayPwModel.setStations(stations);
                                mSubwayList.add(searchAreaSubwayPwModel);
                                SearchAreaPwModel subwayLineModel = new SearchAreaPwModel();
                                subwayLineModel.setName(subway_stationsList.get(i).getName());
                                subwayLineModel.setId(subway_stationsList.get(i).getId());
                                mSubwayLineList.add(subwayLineModel);
                            }
                        }
                    }
                }
            }
        }).start();
    }
    private void getAreaList () {
        mSearchAreaList = new ArrayList<>();
        if (mDistrictList != null && mDistrictList.size() > 0) {
            SearchAreaPwModel firstModel = new SearchAreaPwModel();
            firstModel.setName(getResources().getString(R.string.search_list_area_district));
            firstModel.setId(0);
            firstModel.setSecondList(mDistrictList);
            mSearchAreaList.add(firstModel);
        }
        if (mSubwayList != null && mSubwayList.size() > 0) {
            SearchAreaPwModel secondModel = new SearchAreaPwModel();
            secondModel.setName(getResources().getString(R.string.search_list_area_subway));
            secondModel.setId(1);
            secondModel.setSecondList(mSubwayLineList);
            mSearchAreaList.add(secondModel);
        }
        if (mTradingAreasList != null &&
                mTradingAreasList.size() > 0) {
            SearchAreaPwModel thirdModel = new SearchAreaPwModel();
            thirdModel.setName(getResources().getString(R.string.search_list_area_trading_area));
            thirdModel.setId(2);
            thirdModel.setSecondList(mTradingAreasList);
            mSearchAreaList.add(thirdModel);
        }
    }
    private void showChooseAreaLV(boolean isReste) {
        secondChooseInt = -1;
        if (mSearchAreaList != null && mSearchAreaList.size() > 0) {
            if (chooseAreaType == 0 || chooseAreaType == -1 || chooseAreaType == 2 || isReste) {
                SearchAreaPwAdapter.clearIsFirstChoose();
                for (int i = 0; i < mSearchAreaList.size(); i++) {
                    if (chooseAreaType != -1 && !isReste) {
                        if (mSearchAreaList.get(i).getId() == chooseAreaType) {
                            firstChooseInt = i;
                            SearchAreaPwAdapter.getIsFirstChoose().put(mSearchAreaList.get(i).getId(),true);
                            mSearchAreaList.get(i).setChoose(true);
                        } else {
                            SearchAreaPwAdapter.getIsFirstChoose().put(mSearchAreaList.get(i).getId(),false);
                        }
                    } else {
                        if (i == 0) {
                            firstChooseInt = i;
                            SearchAreaPwAdapter.getIsFirstChoose().put(mSearchAreaList.get(i).getId(),true);
                            mSearchAreaList.get(i).setChoose(false);
                        } else {
                            SearchAreaPwAdapter.getIsFirstChoose().put(mSearchAreaList.get(i).getId(),false);
                            mSearchAreaList.get(i).setChoose(false);
                        }
                    }
                }
                firstAdapter = new SearchAreaPwAdapter(SearchListFragment.this.getActivity(), mSearchAreaList,1);
                firstLv.setAdapter(firstAdapter);
                firstLv.setVisibility(View.VISIBLE);
                SearchAreaPwAdapter.clearIsSecondChoose();
                if (chooseAreaType != -1 && !isReste) {
                    if (mSearchAreaList.get(firstChooseInt).getId() == chooseAreaType) {
                        if (mSearchAreaList.get(firstChooseInt).getId() == 0) {
                            String searchAreaStr = "";
                            for (int j = 0; j < mSearchAreaList.get(firstChooseInt).getSecondList().size(); j++) {
                                if (apiResourcesModel.getDistrict_ids() != null &&
                                        apiResourcesModel.getDistrict_ids().contains(mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getId())) {
                                    SearchAreaPwAdapter.getIsSecondChoose().put(mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getId(),true);
                                    if (searchAreaStr.length() == 0) {
                                        searchAreaStr = searchAreaStr + mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getName();
                                    } else {
                                        searchAreaStr = searchAreaStr + "," +
                                                mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getName();
                                    }
                                } else {
                                    SearchAreaPwAdapter.getIsSecondChoose().put(mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getId(),false);
                                }
                            }
                            if (searchAreaStr.length() > 0) {
                                msearch_area_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
                                msearch_area_type_textview.setText(searchAreaStr);
                                msearch_area_type_textview.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                            }
                        } else if (mSearchAreaList.get(firstChooseInt).getId() == 2) {
                            String searchAreaStr = "";
                            for (int j = 0; j < mSearchAreaList.get(firstChooseInt).getSecondList().size(); j++) {
                                if (apiResourcesModel.getTrading_area_ids() != null &&
                                        apiResourcesModel.getTrading_area_ids().contains(mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getId())) {
                                    SearchAreaPwAdapter.getIsSecondChoose().put(mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getId(),true);
                                    if (searchAreaStr.length() == 0) {
                                        searchAreaStr = searchAreaStr + mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getName();
                                    } else {
                                        searchAreaStr = searchAreaStr + "," +
                                                mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getName();
                                    }
                                } else {
                                    SearchAreaPwAdapter.getIsSecondChoose().put(mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getId(),false);
                                }
                            }
                            if (searchAreaStr.length() > 0) {
                                msearch_area_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
                                msearch_area_type_textview.setText(searchAreaStr);
                                msearch_area_type_textview.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                            }
                        }
                    }
                } else {
                    for (int j = 0; j < mSearchAreaList.get(0).getSecondList().size(); j++) {
                        SearchAreaPwAdapter.getIsSecondChoose().put(mSearchAreaList.get(0).getSecondList().get(j).getId(),false);
                    }
                }
                if (mSearchAreaList.get(firstChooseInt).getId() == 1) {
                    secondAdapter = new SearchAreaPwAdapter(SearchListFragment.this.getActivity(), mSearchAreaList.get(firstChooseInt).getSecondList(),4);
                } else {
                    secondAdapter = new SearchAreaPwAdapter(SearchListFragment.this.getActivity(), mSearchAreaList.get(firstChooseInt).getSecondList(),2);
                }
                secondLv.setAdapter(secondAdapter);
                secondLv.setVisibility(View.VISIBLE);
            } else if (chooseAreaType == 1) {
                SearchAreaPwAdapter.clearIsFirstChoose();
                for (int i = 0; i < mSearchAreaList.size(); i++) {
                    if (mSearchAreaList.get(i).getId() == chooseAreaType) {
                        firstChooseInt = i;
                        SearchAreaPwAdapter.getIsFirstChoose().put(mSearchAreaList.get(i).getId(),true);
                        mSearchAreaList.get(i).setChoose(true);
                    } else {
                        SearchAreaPwAdapter.getIsFirstChoose().put(mSearchAreaList.get(i).getId(),false);
                    }
                }
                firstAdapter = new SearchAreaPwAdapter(SearchListFragment.this.getActivity(), mSearchAreaList,1);
                firstLv.setAdapter(firstAdapter);
                firstLv.setVisibility(View.VISIBLE);
                SearchAreaPwAdapter.clearIsSecondChoose();
                for (int i = 0; i < mSubwayLineList.size(); i++) {
                    SearchAreaPwAdapter.getIsSecondChoose().put(mSubwayLineList.get(i).getId(),false);
                }
                secondAdapter = new SearchAreaPwAdapter(SearchListFragment.this.getActivity(), mSubwayLineList,4);
                secondLv.setAdapter(secondAdapter);
                secondLv.setVisibility(View.VISIBLE);
                SearchAreaPwAdapter.clearIsThirdChoose();
                if (mSubWayLinePosition < 0) {
                    ok:
                    for (int i = 0; i < mSubwayList.size(); i++) {
                        if (mSubwayList.get(i).getStations() != null) {
                            for (int j = 0; j < mSubwayList.get(i).getStations().size(); j++) {
                                if (apiResourcesModel.getSubway_station_ids().
                                        contains(mSubwayList.get(i).getStations().get(j).getId())) {
                                    mSubWayLinePosition = i;
                                    break ok ;
                                }
                            }
                        }
                    }
                }
                if (mSubWayLinePosition > -1) {
                    secondChooseInt = mSubWayLinePosition;
                    SearchAreaPwAdapter.getIsSecondChoose().put(mSubwayLineList.get(mSubWayLinePosition).getId(),true);
                    String searchAreaStr = "";
                    for (int i = 0; i < mSubwayList.get(mSubWayLinePosition).getStations().size(); i++) {
                        if (apiResourcesModel.getSubway_station_ids() != null &&
                                apiResourcesModel.getSubway_station_ids().contains(mSubwayList.get(mSubWayLinePosition).getStations().get(i).getId())) {
                            SearchAreaPwAdapter.getIsThirdChoose().put(mSubwayList.get(mSubWayLinePosition).getStations().get(i).getId(),true);
                            if (searchAreaStr.length() == 0) {
                                searchAreaStr = searchAreaStr + mSubwayList.get(mSubWayLinePosition).getStations().get(i).getStation_name();
                            } else {
                                searchAreaStr = searchAreaStr + "," +
                                        mSubwayList.get(mSubWayLinePosition).getStations().get(i).getStation_name();
                            }
                        } else {
                            SearchAreaPwAdapter.getIsThirdChoose().put(mSubwayList.get(mSubWayLinePosition).getStations().get(i).getId(),false);
                        }
                    }
                    if (searchAreaStr.length() > 0) {
                        msearch_area_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
                        msearch_area_type_textview.setText(searchAreaStr);
                        msearch_area_type_textview.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                    }
                    thirdAdapter = new SearchAreaPwAdapter(SearchListFragment.this.getActivity(), mSubwayList.get(mSubWayLinePosition).getStations(),3);
                    thirdLv.setAdapter(thirdAdapter);
                    thirdLv.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private void showChooseAreaState() {
        if (mSearchAreaList != null && mSearchAreaList.size() > 0) {
            chooseAreaType = -1;
            if (apiResourcesModel.getTrading_area_ids() != null
                    && apiResourcesModel.getTrading_area_ids().size() > 0) {
                chooseAreaType = 2;
            } else if (apiResourcesModel.getDistrict_ids() != null
                    && apiResourcesModel.getDistrict_ids().size() > 0) {
                chooseAreaType = 0;
            } else if (apiResourcesModel.getSubway_station_ids() != null
                    && apiResourcesModel.getSubway_station_ids().size() > 0) {//地铁
                chooseAreaType = 1;
            }
            for (int i = 0; i < mSearchAreaList.size(); i++) {
                if (mSearchAreaList.get(i).getId() == chooseAreaType) {
                    firstChooseInt = i;
                    break;
                }
            }
            if (chooseAreaType == 0 || chooseAreaType == -1 || chooseAreaType == 2) {
                if (chooseAreaType != -1) {
                    if (mSearchAreaList.get(firstChooseInt).getId() == chooseAreaType) {
                        if (mSearchAreaList.get(firstChooseInt).getId() == 0) {
                            String searchAreaStr = "";
                            for (int j = 0; j < mSearchAreaList.get(firstChooseInt).getSecondList().size(); j++) {
                                if (apiResourcesModel.getDistrict_ids() != null &&
                                        apiResourcesModel.getDistrict_ids().contains(mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getId())) {
                                    if (searchAreaStr.length() == 0) {
                                        searchAreaStr = searchAreaStr + mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getName();
                                    } else {
                                        searchAreaStr = searchAreaStr + "," +
                                                mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getName();
                                    }
                                }
                            }
                            if (searchAreaStr.length() > 0) {
                                msearch_area_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
                                msearch_area_type_textview.setText(searchAreaStr);
                                msearch_area_type_textview.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                            }
                        } else if (mSearchAreaList.get(firstChooseInt).getId() == 2) {
                            String searchAreaStr = "";
                            for (int j = 0; j < mSearchAreaList.get(firstChooseInt).getSecondList().size(); j++) {
                                if (apiResourcesModel.getTrading_area_ids() != null &&
                                        apiResourcesModel.getTrading_area_ids().contains(mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getId())) {
                                    if (searchAreaStr.length() == 0) {
                                        searchAreaStr = searchAreaStr + mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getName();
                                    } else {
                                        searchAreaStr = searchAreaStr + "," +
                                                mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getName();
                                    }
                                }
                            }
                            if (searchAreaStr.length() > 0) {
                                msearch_area_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
                                msearch_area_type_textview.setText(searchAreaStr);
                                msearch_area_type_textview.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                            }
                        }
                    }
                }
            } else if (chooseAreaType == 1) {
                if (mSubWayLinePosition < 0) {
                    ok:
                    for (int i = 0; i < mSubwayList.size(); i++) {
                        if (mSubwayList.get(i).getStations() != null) {
                            for (int j = 0; j < mSubwayList.get(i).getStations().size(); j++) {
                                if (apiResourcesModel.getSubway_station_ids().
                                        contains(mSubwayList.get(i).getStations().get(j).getId())) {
                                    mSubWayLinePosition = i;
                                    break ok ;
                                }
                            }
                        }
                    }
                }
                if (mSubWayLinePosition > -1) {
                    secondChooseInt = mSubWayLinePosition;
                    String searchAreaStr = "";
                    for (int i = 0; i < mSubwayList.get(mSubWayLinePosition).getStations().size(); i++) {
                        if (apiResourcesModel.getSubway_station_ids() != null &&
                                apiResourcesModel.getSubway_station_ids().contains(mSubwayList.get(mSubWayLinePosition).getStations().get(i).getId())) {
                            if (searchAreaStr.length() == 0) {
                                searchAreaStr = searchAreaStr + mSubwayList.get(mSubWayLinePosition).getStations().get(i).getStation_name();
                            } else {
                                searchAreaStr = searchAreaStr + "," +
                                        mSubwayList.get(mSubWayLinePosition).getStations().get(i).getStation_name();
                            }
                        }
                    }
                    if (searchAreaStr.length() > 0) {
                        msearch_area_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
                        msearch_area_type_textview.setText(searchAreaStr);
                        msearch_area_type_textview.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                    }
                }
            }
        }
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if(requestCode == LOACTION_REQUEST_INT) {
                //定位
                mLocationClient = new LocationClient(SearchListFragment.this.getActivity());
                mLocationClient.registerLocationListener(new MyLocationListener());//注册定位监听接口
                initLocation();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if(requestCode == LOACTION_REQUEST_INT) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            }
        }
    };
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
    public void onSearchResListSuccess(ArrayList<ResourceSearchItemModel> list, Response response) {
        isShare = false;
        mSearchSizeLL.setVisibility(View.GONE);
        int total = 0;
        JSONObject jsonObject = JSONObject.parseObject(response.data.toString());
        if (jsonObject.get("meta") != null &&
                jsonObject.get("meta").toString().length() > 0) {
            JSONObject metaJson = JSONObject.parseObject(jsonObject.get("meta").toString());
            if (metaJson.get("total") != null &&
                    metaJson.get("total").toString().length() > 0) {
                total = Integer.parseInt(metaJson.get("total").toString());
            }
        }
        SharedescriptionStr = String.valueOf(total);
        if (mSearchInfoList != null) {
            mSearchInfoList.clear();
        }
        if (mSearchListAdapter == null) {
            mSearchInfoList = list;
        } else {
            mSearchInfoList.addAll(list);
        }

        if(mSearchInfoList == null || mSearchInfoList.isEmpty()) {
            if (mlay_no_searchlist.getVisibility() == View.GONE) {
                mlay_no_searchlist.setVisibility(View.VISIBLE);
                mRecommedLL.setVisibility(View.GONE);
                if (apiResourcesModel.getCity_ids() != null &&
                        apiResourcesModel.getCity_ids().size() > 0) {
                    apiResourcesModel.setCity_id(String.valueOf(apiResourcesModel.getCity_ids().get(0)));
                } else {
                    apiResourcesModel.setCity_id(LoginManager.getInstance().getTrackcityid());
                }
                mSearchResListMvpPresenter.getPhyRecommendedList(apiResourcesModel);
            }
            if (SearchListswipList.isShown()) {
                SearchListswipList.setRefreshing(false);
            }
            return;
        }
        if (mlay_no_searchlist.getVisibility() == View.GONE) {
            if (screening_result_boolean ||
                    ((apiResourcesModel.getSubway_station_ids() != null &&
                            apiResourcesModel.getSubway_station_ids().size() > 0) ||
                            (apiResourcesModel.getDistrict_ids() != null &&
                                    apiResourcesModel.getDistrict_ids().size() > 0) ||
                            (apiResourcesModel.getTrading_area_ids() != null &&
                                    apiResourcesModel.getTrading_area_ids().size() > 0))) {
                mSearchSizeLL.setVisibility(View.VISIBLE);
                if (isAdded()) {
                    msearchlist_itemsize_text.setText(getResources().getString(R.string.search_fieldlist_results_one_text)+
                            SharedescriptionStr +getResources().getString(R.string.search_fieldlist_results_two_text));
                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (mSearchSizeLL.getVisibility() == View.VISIBLE) {
                            mSearchSizeLL.setVisibility(View.GONE);
                        }
                    }
                }, 3000);
            }
            isShare = true;
        } else {
            mRecommedLL.setVisibility(View.VISIBLE);
        }
        if (mSearchListAdapter == null) {
            mSearchListAdapter = new SearchCommunityListAdapter(
                    R.layout.searchlistactivity_listview_item,mSearchInfoList,
                    SearchListFragment.this.getActivity(),SearchListFragment.this);
            mSearchListAdapter.setPreLoadNumber(4);//预加载倒数第几个就实现onLoadMoreRequested
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchListFragment.this.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mSearchListRV.setLayoutManager(linearLayoutManager);
            mSearchListRV.setAdapter(mSearchListAdapter);
            mSearchListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    fieldlistpagesize  = fieldlistpagesize + 1;
                    apiResourcesModel.setPage(String.valueOf(fieldlistpagesize));
                    apiResourcesModel.setPage_size(10);
                    if (mlay_no_searchlist.getVisibility() == View.GONE) {
                        mSearchResListMvpPresenter.getPhyReslist(apiResourcesModel);
                    } else {
                        mSearchResListMvpPresenter.getPhyRecommendedList(apiResourcesModel);
                    }
                }
            });
        } else {
            mSearchListAdapter.notifyDataSetChanged();
        }
        mSearchListAdapter.loadMoreComplete();
        if (mSearchInfoList.size() < 10) {
            mSearchListAdapter.loadMoreEnd();
        }
        if (SearchListswipList.isShown()) {
            SearchListswipList.setRefreshing(false);
        }
    }

    @Override
    public void onSearchResListFailure(boolean superresult, Throwable error) {
        mSearchSizeLL.setVisibility(View.GONE);
        if (SearchListswipList.isShown()) {
            SearchListswipList.setRefreshing(false);
        }
        if (!superresult)
            MessageUtils.showToast(getContext(), error.getMessage());
    }

    @Override
    public void onSearchResListMoreSuccess(ArrayList<ResourceSearchItemModel> list, Response response) {
        ArrayList<ResourceSearchItemModel> tmp = list;
        if( (tmp == null || tmp.isEmpty())){
            fieldlistpagesize = fieldlistpagesize - 1;
            mSearchListAdapter.loadMoreEnd();
            return;
        }
        for( ResourceSearchItemModel order: tmp ){
            mSearchInfoList.add(order);
        }
        mSearchListAdapter.notifyDataSetChanged();
        mSearchListAdapter.loadMoreComplete();
        if (tmp.size() < 10) {
            mSearchListAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onSearchResListMoreFailure(boolean superresult, Throwable error) {
        mSearchListAdapter.loadMoreFail();
        fieldlistpagesize = fieldlistpagesize - 1;
        if (!superresult)
            MessageUtils.showToast(getContext(), error.getMessage());
    }

    @Override
    public void onAttributesSuccess(ArrayList<SearchListAttributesModel> list) {
        hideProgressDialog();
        if (list != null && list.size() > 0) {
            mAttributesList = list;
            if (isShowScreenAttributes) {
                apiResourcesModel.setAttributes(null);
                setResScreeningAdapter();
            }
        }

    }

    @Override
    public void onAttributesFailure(boolean superresult, Throwable error) {
        if (mAttributesList != null) {
            mAttributesList.clear();
        }
        if (isShowScreenAttributes) {
            setResScreeningAdapter();
        }
    }

    @Override
    public void onSearchSellResListSuccess(ArrayList<SearchSellResModel> list, Response response) {

    }

    @Override
    public void onSearchSellResListMoreSuccess(ArrayList<SearchSellResModel> list, Response response) {

    }

    @Override
    public void onSearchResListCountSuccess(int count) {
        if (count > 0) {
            mSearchConfirmBtn.setEnabled(true);
            mSearchConfirmBtn.setBackgroundColor(getResources().getColor(R.color.default_bluebg));
            if (mApiResourcesModel.getMin_area() != null || mApiResourcesModel.getMax_area() != null||
                    mApiResourcesModel.getMin_person_flow() != null  || mApiResourcesModel.getMax_person_flow() != null
                    || mApiResourcesModel.getLabel_ids().size() > 0
                    || mApiResourcesModel.getLocation_type_ids().size() > 0 //2018/12/11 位置类型
                    || mApiResourcesModel.getCommunity_type_ids().size() > 0
                    || mApiResourcesModel.getAttributes().size() > 0) {
                mSearchConfirmBtn.setText(getResources().getString(R.string.confirm) + "(" +
                        String.valueOf(count) + getResources().getString(R.string.module_searchlist_confirm_text) +
                        ")"
                );
            } else {
                mSearchConfirmBtn.setText(getResources().getString(R.string.confirm));
            }
        } else {
            mSearchConfirmBtn.setEnabled(false);
            mSearchConfirmBtn.setBackgroundColor(getResources().getColor(R.color.module_searchlist_screen_btn_no_data_color));
            mSearchConfirmBtn.setText(getResources().getString(R.string.confirm) + "(" +
                    String.valueOf(0) + getResources().getString(R.string.module_searchlist_confirm_text) +
                    ")"
            );
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null && lat == 0 &&
                    lng == 0) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                apiResourcesModel.setLat(lat);
                apiResourcesModel.setLng(lng);
                searchinitdata();
            }
            mLocationClient.stop();
            mLocationClient = null;
        }
    }
    public void getAttributesList() {
        isGetCategory = true;
        ArrayList<Integer> Category = new ArrayList<>();
        for (int i = 0; i < mCategoryAdapterList.size(); i++) {
            if (ResourcesScreeningItemAdapter.getresourcescreeninglist().get("category" + mCategoryAdapterList.get(i).get("category").toString()) != null &&
                    (Boolean) ResourcesScreeningItemAdapter.getresourcescreeninglist().get("category" + mCategoryAdapterList.get(i).get("category").toString()) == true) {
                if (mCategoryMap.get(mCategoryAdapterList.get(i).get("category").toString()).toString() != null) {
                    Category.add(Integer.parseInt(mCategoryMap.get(mCategoryAdapterList.get(i).get("category").toString()).toString()));
                }
            }
        }
        apiResourcesModel.setCommunity_type_ids(Category);
        if (Category.size() > 0) {
            showProgressDialog();
            mSearchResListMvpPresenter.getAttributesList(Category);
        } else {
            if (mAttributesList != null) {
                mAttributesList.clear();
            }
            setResScreeningAdapter();
        }

    }
    private void setResScreeningAdapter() {
        int HeaderId = 0;
        data_new = new ArrayList<>();
        //类目
        if (mCategoryList.size() > 0) {
            HeaderId++;
            if (mCategoryAdapterList != null) {
                mCategoryAdapterList.clear();
            }
            ArrayList<HashMap<Object,Object>> category_listdata = new ArrayList<HashMap<Object,Object>>();
            for (int i = 0; i < mCategoryList.size(); i++) {
                HashMap<Object,Object> map = new HashMap<Object,Object>();
                map.put("type","category");
                map.put("category",mCategoryList.get(i));
                map.put("HeaderId",HeaderId);
                category_listdata.add(map);
                mCategoryAdapterList.add(map);
            }
            HashMap<Object,Object> category_listmap = new HashMap<Object,Object>();
            category_listmap.put("type","category");
            category_listmap.put("datalist",category_listdata);
            data_new.add(category_listmap);
        }
        // 摆摊推荐
        if (field_labels_list.size() > 0) {
            HeaderId++;
            ArrayList<HashMap<Object,Object>> field_labels_listdata = new ArrayList<HashMap<Object,Object>>();
            for (int i = 0; i <field_labels_list.size(); i++) {
                HashMap<Object,Object> map = new HashMap<Object,Object>();
                map.put("type","field_labels");
                map.put("id",field_labels_list.get(i).getId());
                map.put("field_labels",field_labels_list.get(i).getDisplay_name());
                map.put("HeaderId",HeaderId);
                field_labels_listdata.add(map);
            }
            HashMap<Object,Object> field_labels_listmap = new HashMap<Object,Object>();
            field_labels_listmap.put("type","field_labels");
            field_labels_listmap.put("datalist",field_labels_listdata);
            data_new.add(field_labels_listmap);
        }
        //2018/12/11 位置类型
        if (mLocationTypeList.size() > 0) {
            HeaderId++;
            ArrayList<HashMap<Object,Object>> locationList = new ArrayList<HashMap<Object,Object>>();
            for (int i = 0; i <mLocationTypeList.size(); i++) {
                HashMap<Object,Object> map = new HashMap<Object,Object>();
                map.put("type","location_types");
                map.put("id",mLocationTypeList.get(i).getId());
                map.put("location_types",mLocationTypeList.get(i).getDisplay_name());
                map.put("HeaderId",HeaderId);
                locationList.add(map);
            }
            HashMap<Object,Object> locationMap = new HashMap<Object,Object>();
            locationMap.put("type","location_types");
            locationMap.put("datalist",locationList);
            data_new.add(locationMap);
        }

        //属性
        if (apiResourcesModel.getCommunity_type_ids() != null &&
                apiResourcesModel.getCommunity_type_ids().size() > 0&&
                mAttributesList != null && mAttributesList.size() > 0) {
            for (int i = 0; i < mAttributesList.size(); i++) {
                HeaderId++;
                ArrayList<HashMap<Object,Object>> attributes_listdata = new ArrayList<HashMap<Object,Object>>();
                if (mAttributesList.get(i).getOptions() != null &&
                        mAttributesList.get(i).getOptions().size() > 0) {
                    for (int j = 0; j < mAttributesList.get(i).getOptions().size(); j++) {
                        HashMap<Object,Object> map = new HashMap<Object,Object>();
                        map.put("type","attributes" + String.valueOf( mAttributesList.get(i).getId()));
                        map.put("attributes" + String.valueOf( mAttributesList.get(i).getId()),
                                mAttributesList.get(i).getOptions().get(j).getName());
                        map.put("attribute_id",mAttributesList.get(i).getId());
                        map.put("id",mAttributesList.get(i).getOptions().get(j).getId());
                        map.put("HeaderId",HeaderId);
                        attributes_listdata.add(map);
                    }
                    HashMap<Object,Object> attributes_listmap = new HashMap<Object,Object>();
                    attributes_listmap.put("type","attributes" + String.valueOf(mAttributesList.get(i).getId()));
                    attributes_listmap.put("name",mAttributesList.get(i).getName());
                    attributesChooseMap.put("attributes" + String.valueOf(mAttributesList.get(i).getId()),0);
                    attributes_listmap.put("datalist",attributes_listdata);
                    data_new.add(attributes_listmap);
                }

            }
        }
        //场地面积
        HashMap<Object,Object> areamap = new HashMap<Object,Object>();
        areamap.put("type","area");
        areamap.put("itemtype",1);
        data_new.add(areamap);
//        //建筑年份
//        HashMap<Object,Object> build_year_map = new HashMap<Object,Object>();
//        build_year_map.put("type","year");
//        build_year_map.put("itemtype",1);
//        data_new.add(build_year_map);
        //人流量
        HashMap<Object,Object> pepple_map = new HashMap<Object,Object>();
        pepple_map.put("type","numper_of_people");
        pepple_map.put("itemtype",1);
        data_new.add(pepple_map);
        //修改后的adapter
        if (!isGetCategory) {
            ResourcesScreeningItemAdapter.clear_resourcescreeninglist();
        }
        //是否选中的标志
        for (int j = 0; j < data_new.size(); j++ ) {
            if (data_new.get(j).get("datalist") != null && ((ArrayList<HashMap<Object,Object>>)data_new.get(j).get("datalist")).size() > 0) {
                ArrayList<HashMap<Object,Object>> data_new_temp = new ArrayList<>();
                data_new_temp.addAll(((ArrayList<HashMap<Object,Object>>) (data_new.get(j).get("datalist"))));
                for (int i = 0; i <data_new_temp.size(); i++) {
                    if (data_new_temp.get(i).get("itemtype") != null && ((Integer)data_new_temp.get(i).get("itemtype") == 1 ||
                            (Integer)data_new_temp.get(i).get("itemtype") == 2)) {

                    } else {
                        if (!isGetCategory || ResourcesScreeningItemAdapter.getresourcescreeninglist().get(data_new_temp.get(i).get("type").toString() + data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()) == null) {
                            ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString() + data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(), false);
                        } else {
                            if (data_new_temp.get(i).get("type").toString().indexOf("attributes") != -1) {
                                ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString() + data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(), false);
                            }
                        }
                        if (data_new_temp.get(i).get("type").toString().equals("field_labels")) {
                            if (apiResourcesModel.getLabel_ids() != null) {
                                if (apiResourcesModel.getLabel_ids().size() > 0) {
                                    field_labels_int = 1;
                                    if (apiResourcesModel.getLabel_ids().contains(Integer.parseInt(data_new_temp.get(i).get("id").toString()))) {
                                        ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString()+data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(),true);
                                    }
                                }
                            }

                        }
                        //2018/12/11 位置类型
                        else if (data_new_temp.get(i).get("type").toString().equals("location_types")) {
                            if (apiResourcesModel.getLocation_type_ids() != null) {
                                if (apiResourcesModel.getLocation_type_ids().size() > 0) {
                                    mLocationTypeChooseInt = 1;
                                    if (apiResourcesModel.getLocation_type_ids().contains(Integer.parseInt(data_new_temp.get(i).get("id").toString()))) {
                                        ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString()+data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(),true);
                                    }
                                }
                            }

                        } else if (data_new_temp.get(i).get("type").toString().equals("category") &&
                                mCategoryMap.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()) != null) {
                            if (apiResourcesModel.getCommunity_type_ids() != null) {
                                if (apiResourcesModel.getCommunity_type_ids().size() > 0) {
                                    category_id_int = 1;
                                    if (apiResourcesModel.getCommunity_type_ids().contains(Integer.parseInt(mCategoryMap.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()).toString()))) {
                                        ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString()+data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(),true);
                                    }
                                }
                            }
//
                        } else if (data_new_temp.get(i).get("type").toString().indexOf("attributes") != -1) {
                            if (apiResourcesModel.getAttributes() != null) {
                                if (apiResourcesModel.getAttributes().size() > 0) {
                                    for (int l = 0; l < apiResourcesModel.getAttributes().size(); l++) {
                                        if (apiResourcesModel.getAttributes().get(l).getId() == Integer.parseInt(data_new_temp.get(i).get("attribute_id").toString())) {
                                            if (apiResourcesModel.getAttributes().get(l).getOption_ids() != null &&
                                                    apiResourcesModel.getAttributes().get(l).getOption_ids().size() > 0) {
                                                if (attributesChooseMap.get(data_new_temp.get(i).get("type").toString()) != null) {
                                                    attributesChooseMap.put(data_new_temp.get(i).get("type").toString(),1);
                                                }
                                                if ( apiResourcesModel.getAttributes().get(l).getOption_ids().contains(Integer.parseInt(data_new_temp.get(i).get("id").toString()))) {
                                                    ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString()+data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(),true);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (data_new.get(j).get("type").toString().equals("year")) {
                    if (apiResourcesModel.getMin_year() != null) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("yearmin",String.valueOf(apiResourcesModel.getMin_year()));
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("yearmin","");
                    }
                    if (apiResourcesModel.getMax_year() != null) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("yearmax",String.valueOf(apiResourcesModel.getMax_year()));
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("yearmax","");
                    }
                } else if (data_new.get(j).get("type").toString().equals("area")) {
                    if (apiResourcesModel.getMin_area() != null && apiResourcesModel.getMin_area().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("areamin",apiResourcesModel.getMin_area());
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("areamin","");
                    }
                    if (apiResourcesModel.getMax_area() != null && apiResourcesModel.getMax_area().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("areamax",apiResourcesModel.getMax_area());
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("areamax","");
                    }
                } else if (data_new.get(j).get("type").toString().equals("numper_of_people")) {
                    if (apiResourcesModel.getMin_person_flow() != null && apiResourcesModel.getMin_person_flow().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("numper_of_peoplemin",apiResourcesModel.getMin_person_flow());
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("numper_of_peoplemin","");
                    }
                    if (apiResourcesModel.getMax_person_flow() != null && apiResourcesModel.getMax_person_flow().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("numper_of_peoplemax",apiResourcesModel.getMax_person_flow());
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("numper_of_peoplemax","");
                    }
                }
            }
        }
        if (isGetCategory) {
            isGetCategory = false;
        }
        resourcesScreeningNewAdapter = new ResourcesScreeningNewAdapter(SearchListFragment.this.getActivity(),SearchListFragment.this,data_new,0);
        if (resourcesScreeningNewAdapter != null) {
            mresourcesscreening_stickygridview_new.setAdapter(resourcesScreeningNewAdapter);
        }
    }
    private HashMap<String,String> getBrowseHistoriesUrl() {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (apiResourcesModel.getKeywords() != null && apiResourcesModel.getKeywords().length() > 0) {
            paramsMap.put("keywords", apiResourcesModel.getKeywords());
        }
        if (apiResourcesModel.getHas_physical() == 1) {
            paramsMap.put("has_physical", String.valueOf(apiResourcesModel.getHas_physical()));
        }

        if (apiResourcesModel.getCity_ids() != null &&
                apiResourcesModel.getCity_ids().size() > 0) {
            String cityStr = "";
            for (int i = 0; i < apiResourcesModel.getCity_ids().size(); i++) {
                if (cityStr.length() > 0) {
                    cityStr = cityStr + "," + String.valueOf(apiResourcesModel.getCity_ids().get(i));
                } else {
                    cityStr = String.valueOf(apiResourcesModel.getCity_ids().get(i));
                }
            }
            if (cityStr.length() > 0) {
                paramsMap.put("city_ids",cityStr);
            }
        }
        if (apiResourcesModel.getDistrict_ids() != null &&
                apiResourcesModel.getDistrict_ids().size() > 0) {
            String district_ids = "";
            for (int i = 0; i < apiResourcesModel.getDistrict_ids().size(); i++) {
                if (district_ids.length() > 0) {
                    district_ids = district_ids + "," + String.valueOf(apiResourcesModel.getDistrict_ids().get(i));
                } else {
                    district_ids = String.valueOf(apiResourcesModel.getDistrict_ids().get(i));
                }
            }
            if (district_ids.length() > 0) {
                paramsMap.put("district_ids",district_ids);
            }
        }
        if (apiResourcesModel.getTrading_area_ids() != null &&
                apiResourcesModel.getTrading_area_ids().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getTrading_area_ids().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getTrading_area_ids().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getTrading_area_ids().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put("trading_area_ids",ids);
            }
        }
        if (apiResourcesModel.getSubway_station_ids() != null &&
                apiResourcesModel.getSubway_station_ids().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getSubway_station_ids().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getSubway_station_ids().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getSubway_station_ids().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put("subway_station_ids",ids);
            }
        }
        if (apiResourcesModel.getIn_trading_area() != null && apiResourcesModel.getIn_trading_area().length() > 0) {
            paramsMap.put("in_trading_area", apiResourcesModel.getIn_trading_area());
        }
        if (apiResourcesModel.getCommunity_type_ids() != null &&
                apiResourcesModel.getCommunity_type_ids().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getCommunity_type_ids().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getCommunity_type_ids().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getCommunity_type_ids().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put("community_type_ids",ids);
            }
        }
        if (apiResourcesModel.getLabel_ids() != null &&
                apiResourcesModel.getLabel_ids().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getLabel_ids().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getLabel_ids().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getLabel_ids().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put("label_ids",ids);
            }
        }
        //2018/12/11 浏览记录位置类型
        if (apiResourcesModel.getLocation_type_ids() != null &&
                apiResourcesModel.getLocation_type_ids().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getLocation_type_ids().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getLocation_type_ids().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getLocation_type_ids().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put("location_type_ids",ids);
            }
        }

        if (apiResourcesModel.getField_cooperation_type_ids() != null &&
                apiResourcesModel.getField_cooperation_type_ids().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getField_cooperation_type_ids().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getField_cooperation_type_ids().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getField_cooperation_type_ids().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put("field_cooperation_type_ids",ids);
            }
        }
        if (apiResourcesModel.getMin_price() != null && apiResourcesModel.getMin_price().length() > 0) {
            paramsMap.put("min_price", apiResourcesModel.getMin_price());
        }
        if (apiResourcesModel.getMax_price() != null && apiResourcesModel.getMax_price().length() > 0) {
            paramsMap.put("max_price", apiResourcesModel.getMax_price());
        }
        if (apiResourcesModel.getMin_year() != null) {
            paramsMap.put("min_year", String.valueOf(apiResourcesModel.getMin_year()));
        }
        if (apiResourcesModel.getMax_year() != null) {
            paramsMap.put("max_year", String.valueOf(apiResourcesModel.getMax_year()));
        }
        if (apiResourcesModel.getMin_area() != null) {
            paramsMap.put("min_area", String.valueOf(apiResourcesModel.getMin_area()));
        }
        if (apiResourcesModel.getMax_area() != null) {
            paramsMap.put("max_area", String.valueOf(apiResourcesModel.getMax_area()));
        }
        if (apiResourcesModel.getMin_person_flow() != null) {
            paramsMap.put("min_person_flow", apiResourcesModel.getMin_person_flow());
        }
        if (apiResourcesModel.getMax_person_flow() != null) {
            paramsMap.put("max_person_flow", apiResourcesModel.getMax_person_flow());
        }
        //属性
        if (apiResourcesModel.getAttributes() != null &&
                apiResourcesModel.getAttributes().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getAttributes().size(); i++) {
                String attributes = "";
                for (int j = 0; j < apiResourcesModel.getAttributes().get(i).getOption_ids().size(); j++) {
                    if (attributes.length() == 0) {
                        attributes = "[" + String.valueOf(apiResourcesModel.getAttributes().get(i).getOption_ids().get(j));
                        if (j == apiResourcesModel.getAttributes().get(i).getOption_ids().size() - 1) {
                            attributes = attributes + "]";
                        }
                    } else {
                        attributes = attributes + "," + String.valueOf(apiResourcesModel.getAttributes().get(i).getOption_ids().get(j));
                        if (j == apiResourcesModel.getAttributes().get(i).getOption_ids().size() - 1) {
                            attributes = attributes + "]";
                        }
                    }
                }
                attributes = "{\"id\":" + String.valueOf(apiResourcesModel.getAttributes().get(i).getId()) +"," +
                        "\"option_ids\":" + attributes + "}";
                paramsMap.put("attributes[" + String.valueOf(i) + "]", attributes);
            }
        }
        if (apiResourcesModel.getLat() > 0) {
            paramsMap.put("lat", String.valueOf(apiResourcesModel.getLat()));
        }
        if (apiResourcesModel.getLng() > 0) {
            paramsMap.put("lng", String.valueOf(apiResourcesModel.getLng()));
        }
        if (apiResourcesModel.getLatitude() > 0) {
            paramsMap.put("latitude", String.valueOf(apiResourcesModel.getLatitude()));
        }
        if (apiResourcesModel.getLongitude() > 0) {
            paramsMap.put("longitude", String.valueOf(apiResourcesModel.getLongitude()));
        }
        if (apiResourcesModel.getLatitude_delta() > 0) {
            paramsMap.put("latitude_delta", String.valueOf(apiResourcesModel.getLatitude_delta()));
        }
        if (apiResourcesModel.getLongitude_delta() > 0) {
            paramsMap.put("longitude_delta", String.valueOf(apiResourcesModel.getLongitude_delta()));
        }
        if (apiResourcesModel.getNearby() > 0) {
            paramsMap.put("nearby", String.valueOf(apiResourcesModel.getNearby()));
        }
        if (apiResourcesModel.getNot_need_deposit() != null) {
            paramsMap.put("not_need_deposit", String.valueOf(apiResourcesModel.getNot_need_deposit()));
        }
        if (apiResourcesModel.getHas_subsidy() != null) {
            paramsMap.put("has_subsidy", String.valueOf(apiResourcesModel.getHas_subsidy()));
        }
        if (apiResourcesModel.getHas_activity() != null) {
            paramsMap.put("has_activity", String.valueOf(apiResourcesModel.getHas_activity()));
        }
        if (apiResourcesModel.getActivity_type_ids() != null &&
                apiResourcesModel.getActivity_type_ids().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getActivity_type_ids().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getActivity_type_ids().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getActivity_type_ids().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put("activity_type_ids",ids);
            }
        }
        if (apiResourcesModel.getAge_level_ids() != null &&
                apiResourcesModel.getAge_level_ids().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getAge_level_ids().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getAge_level_ids().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getAge_level_ids().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put("age_level_ids",ids);
            }
        }
        if (apiResourcesModel.getIndoor() != null &&
                apiResourcesModel.getIndoor().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getIndoor().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getIndoor().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getIndoor().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put("indoor",ids);
            }
        }
        if (apiResourcesModel.getFacilities() != null &&
                apiResourcesModel.getFacilities().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getFacilities().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getFacilities().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getFacilities().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put("facilities",ids);
            }
        }
        paramsMap.put("order", apiResourcesModel.getOrder());
        paramsMap.put("order_by", apiResourcesModel.getOrder_by());
        if (apiResourcesModel.getNavigation() != null) {
            paramsMap.put("navigation", String.valueOf(apiResourcesModel.getNavigation()));
        }
        if (apiResourcesModel.getDynamic_name() != null &&
                apiResourcesModel.getDynamic_name().length() > 0 &&
                apiResourcesModel.getDynamic_id() != null &&
                apiResourcesModel.getDynamic_id().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getDynamic_id().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getDynamic_id().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getDynamic_id().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put(apiResourcesModel.getDynamic_name(),ids);
            }
        }
        return paramsMap;
    }
    public void getListCount() {
        String json = JSON.toJSONString(apiResourcesModel);
        mApiResourcesModel = (ApiResourcesModel) JSONObject.parseObject(json,ApiResourcesModel.class);
        mresourcesscreening_stickygridview_new.clearFocus();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                String mminimum_yearedit = "";
                String mmaximum_yearedit = "";
                String area_edit = "";
                String area_max_edit = "";
                String min_person_flow_edit = "";
                String max_person_flow_edit = "";
                ArrayList<Integer> fieldlabel_list = new ArrayList<>();
                ArrayList<Integer> locationTypeIdsList = new ArrayList<>();//2018/12/11 位置类型确认按钮操作
                ArrayList<Integer> communityTypeList = new ArrayList<>();
                ArrayList<SearchListAttributesModel> attributesList = new ArrayList<>();
                for (int j = 0;j < data_new.size(); j++ ) {
                    if (data_new.get(j).get("datalist") != null && ((ArrayList<HashMap<Object,Object>>)data_new.get(j).get("datalist")).size() > 0) {
                        ArrayList<HashMap<Object,Object>> data_new_temp = new ArrayList<>();
                        data_new_temp.addAll(((ArrayList<HashMap<Object,Object>>) (data_new.get(j).get("datalist"))));
                        for (int i = 0; i <data_new_temp.size(); i++) {
                            if (data_new_temp.get(i).get("itemtype") != null && ((Integer)data_new_temp.get(i).get("itemtype") == 1 ||
                                    (Integer)data_new_temp.get(i).get("itemtype") == 2)) {

                            } else {
                                if ((boolean)ResourcesScreeningItemAdapter.getresourcescreeninglist().get(data_new_temp.get(i).get("type").toString()+data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())) {
                                    if (data_new_temp.get(i).get("type").toString().equals("field_labels")) {
                                        fieldlabel_list.add(Integer.parseInt(data_new_temp.get(i).get("id").toString()));
                                    } else if (data_new_temp.get(i).get("type").toString().equals("location_types")) {//2018/12/11 位置类型
                                        locationTypeIdsList.add(Integer.parseInt(data_new_temp.get(i).get("id").toString()));
                                    } else if (data_new_temp.get(i).get("type").toString().equals("category")) {
                                        communityTypeList.add(Integer.parseInt(mCategoryMap.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())));
                                    } else if (data_new_temp.get(i).get("type").toString().indexOf("attributes") != -1) {
                                        if (attributesList.size() > 0) {
                                            boolean isHasAttribute = false;
                                            for (int l = 0; l <attributesList.size(); l++) {
                                                if (attributesList.get(l).getId() ==
                                                        Integer.parseInt(data_new_temp.get(i).get("attribute_id").toString())) {
                                                    isHasAttribute = true;
                                                    attributesList.get(l).getOption_ids().add(Integer.parseInt(data_new_temp.get(i).get("id").toString()));
                                                }
                                            }
                                            if (!isHasAttribute) {
                                                SearchListAttributesModel searchListAttributesModel =  new SearchListAttributesModel();
                                                searchListAttributesModel.setId(Integer.parseInt(data_new_temp.get(i).get("attribute_id").toString()));
                                                ArrayList<Integer> option_ids = new ArrayList<>();
                                                option_ids.add(Integer.parseInt(data_new_temp.get(i).get("id").toString()));
                                                searchListAttributesModel.setOption_ids(option_ids);
                                                attributesList.add(searchListAttributesModel);
                                            }
                                        } else {
                                            SearchListAttributesModel searchListAttributesModel =  new SearchListAttributesModel();
                                            searchListAttributesModel.setId(Integer.parseInt(data_new_temp.get(i).get("attribute_id").toString()));
                                            ArrayList<Integer> option_ids = new ArrayList<>();
                                            option_ids.add(Integer.parseInt(data_new_temp.get(i).get("id").toString()));
                                            searchListAttributesModel.setOption_ids(option_ids);
                                            attributesList.add(searchListAttributesModel);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (data_new.get(j).get("type").toString().equals("year")) {
                            if (ResourcesScreeningNewAdapter.getedittextmap().get("yearmin").toString().length() > 0) {
                                if (Integer.parseInt(ResourcesScreeningNewAdapter.getedittextmap().get("yearmin").toString()) < 1900 ||
                                        Integer.parseInt(ResourcesScreeningNewAdapter.getedittextmap().get("yearmin").toString()) > 2099) {
                                    MessageUtils.showToast(getResources().getString(R.string.module_searchlist_buile_year_error_msg));
                                    ResourcesScreeningNewAdapter.getedittextmap().put("yearmin","");
                                    resourcesScreeningNewAdapter.notifyDataSetChanged();
                                    return;
                                }
                            }
                            if (ResourcesScreeningNewAdapter.getedittextmap().get("yearmax").toString().length() > 0) {
                                if (Integer.parseInt(ResourcesScreeningNewAdapter.getedittextmap().get("yearmax").toString()) < 1900 ||
                                        Integer.parseInt(ResourcesScreeningNewAdapter.getedittextmap().get("yearmax").toString()) > 2099) {
                                    MessageUtils.showToast(getResources().getString(R.string.module_searchlist_buile_year_error_msg));
                                    ResourcesScreeningNewAdapter.getedittextmap().put("yearmax","");
                                    resourcesScreeningNewAdapter.notifyDataSetChanged();
                                    return;
                                }
                            }
                            if (ResourcesScreeningNewAdapter.getedittextmap().get("yearmin").toString().length() > 0 &&
                                    ResourcesScreeningNewAdapter.getedittextmap().get("yearmax").toString().length() > 0) {
                                if (Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("yearmin").toString(), 1),1)) >
                                        Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("yearmax").toString(), 1),1))) {
                                    ResourcesScreeningNewAdapter.getedittextmap().put("yearmin","");
                                    ResourcesScreeningNewAdapter.getedittextmap().put("yearmax","");
                                    MessageUtils.showToast(getResources().getString(R.string.module_searchlist_buile_year_error_hind));
                                    resourcesScreeningNewAdapter.notifyDataSetChanged();
                                    return;
                                }
                            }
                            mminimum_yearedit = ResourcesScreeningNewAdapter.getedittextmap().get("yearmin").toString();
                            mmaximum_yearedit = ResourcesScreeningNewAdapter.getedittextmap().get("yearmax").toString();
                        } else if (data_new.get(j).get("type").toString().equals("area")) {
                            if (ResourcesScreeningNewAdapter.getedittextmap().get("areamin").toString().length() > 0 &&
                                    ResourcesScreeningNewAdapter.getedittextmap().get("areamax").toString().length() > 0) {
                                if (Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("areamin").toString(), 1),1)) >
                                        Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("areamax").toString(), 1),1))) {
                                    ResourcesScreeningNewAdapter.getedittextmap().put("areamin","");
                                    ResourcesScreeningNewAdapter.getedittextmap().put("areamax","");
                                    MessageUtils.showToast(getResources().getString(R.string.module_searchlist_area_error_hind));
                                    resourcesScreeningNewAdapter.notifyDataSetChanged();
                                    return;
                                }
                            }
                            area_edit = ResourcesScreeningNewAdapter.getedittextmap().get("areamin").toString();
                            area_max_edit = ResourcesScreeningNewAdapter.getedittextmap().get("areamax").toString();
                        } else if (data_new.get(j).get("type").toString().equals("numper_of_people")) {
                            if (ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemin").toString().length() > 0 &&
                                    ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemax").toString().length() > 0) {
                                if (Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemin").toString(), 1),1)) >
                                        Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemax").toString(), 1),1))) {
                                    ResourcesScreeningNewAdapter.getedittextmap().put("numper_of_peoplemin","");
                                    ResourcesScreeningNewAdapter.getedittextmap().put("numper_of_peoplemax","");
                                    MessageUtils.showToast(getResources().getString(R.string.module_searchlist_area_error_hind));
                                    resourcesScreeningNewAdapter.notifyDataSetChanged();
                                    return;
                                }
                            }
                            min_person_flow_edit = ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemin").toString();
                            max_person_flow_edit = ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemax").toString();
                        }
                    }
                }
                mApiResourcesModel.setLabel_ids(fieldlabel_list);
                mApiResourcesModel.setLocation_type_ids(locationTypeIdsList);//2018/12/11 赋值位置类型到筛选
                mApiResourcesModel.setCommunity_type_ids(communityTypeList);
                mApiResourcesModel.setAttributes(attributesList);
                if (mminimum_yearedit.length() > 0) {
                    mApiResourcesModel.setMin_year(Integer.parseInt(mminimum_yearedit));
                } else {
                    mApiResourcesModel.setMin_year(null);
                }
                if (mmaximum_yearedit.length() > 0) {
                    mApiResourcesModel.setMax_year(Integer.parseInt(mmaximum_yearedit));
                } else {
                    mApiResourcesModel.setMax_year(null);
                }
                if (area_edit.length() > 0) {
                    mApiResourcesModel.setMin_area(Constants.getpricestring(area_edit,1));
                } else {
                    mApiResourcesModel.setMin_area(null);
                }
                if (area_max_edit.length() > 0) {
                    mApiResourcesModel.setMax_area(Constants.getpricestring(area_max_edit,1));
                } else {
                    mApiResourcesModel.setMax_area(null);
                }
                if (min_person_flow_edit.length() > 0) {
                    mApiResourcesModel.setMin_person_flow(Constants.getpricestring(min_person_flow_edit,1));
                } else {
                    mApiResourcesModel.setMin_person_flow(null);
                }
                if (max_person_flow_edit.length() > 0) {
                    mApiResourcesModel.setMax_person_flow(Constants.getpricestring(max_person_flow_edit,1));
                } else {
                    mApiResourcesModel.setMax_person_flow(null);
                }
                showProgressDialog();
                mlay_no_searchlist.setVisibility(View.GONE);
                mSearchResListMvpPresenter.getPhyReslistCount(mApiResourcesModel);
            }
        }, 100);
    }
}
