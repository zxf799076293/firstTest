package com.linhuiba.business.fragment;

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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.AboutUsActivity;
import com.linhuiba.business.activity.AdvertisingInfoActivity;
import com.linhuiba.business.activity.BaiduMapActivity;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.activity.MainTabActivity;
import com.linhuiba.business.activity.SearchFieldAreaActivity;
import com.linhuiba.business.adapter.ResourcesScreeningItemAdapter;
import com.linhuiba.business.adapter.ResourcesScreeningNewAdapter;
import com.linhuiba.business.adapter.SearchAdvListAdapter;
import com.linhuiba.business.adapter.SearchAreaPwAdapter;
import com.linhuiba.business.adapter.SearchListAdapter;
import com.linhuiba.business.adapter.SearchSortAdapter;
import com.linhuiba.business.adapter.SearchTitlePopupAdapter;
import com.linhuiba.business.basemvp.BaseMvpFragment;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.model.ApiAdvResourcesModel;
import com.linhuiba.business.model.SearchAreaPwModel;
import com.linhuiba.business.model.SearchAreaSubwayPwModel;
import com.linhuiba.business.model.SearchSellResModel;
import com.linhuiba.business.mvpmodel.LoginMvpModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Request;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.view.LoadMoreListView;
import com.linhuiba.linhuifield.sqlite.ConfigCitiesModel;
import com.linhuiba.linhuifield.sqlite.ConfigCityParameterModel;
import com.linhuiba.linhuifield.sqlite.ConfigSqlOperation;
import com.linhuiba.linhuifield.sqlite.ConfigurationsModel;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.linhuipublic.config.SupportPopupWindow;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.io.InputStream;
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

public class SearchAdvListFragment extends BaseMvpFragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMore,
        LoadMoreListView.OnScrollListener {
    private View mMainContent;
    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout SearchListswipList;
    @InjectView(R.id.order_list)
    LoadMoreListView SearchListloadmoreList;
    @InjectView(R.id.View_view)
    View View_view;
    @InjectView(R.id.sort_txt)
    TextView msort_txt;
    @InjectView(R.id.edit_search_search)
    TextView medit_search;
    @InjectView(R.id.lay_no_searchlist)
    LinearLayout mlay_no_searchlist;
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
    @InjectView(R.id.search_selfsupportshop_rbtn)
    CheckBox mSearchSelfSupportShopCheckBox;//主题场地
    @InjectView(R.id.search_hotsell_rbtn)
    CheckBox mSearchHotSellCheckBox;
    @InjectView(R.id.search_new_res_cb)
    CheckBox mSearchNewResCheckBox;//上新
    @InjectView(R.id.search_bestsell_rbtn)
    CheckBox mSearchBestSellCheckBox;
    @InjectView(R.id.search_popup_view)
    View mSearchTitlePopUpView;
    @InjectView(R.id.searchlist_field_bable_ll)
    LinearLayout mSearchLabelLL;
    @InjectView(R.id.searchlist_status_bar_ll)
    LinearLayout mSearchStatusBarLL;
    @InjectView(R.id.search_area_backimg)
    TextView mSearchBackTV;

    private SearchAdvListAdapter mSearchListAdapter;
    private Dialog mSearchShareDialog;
    private SupportPopupWindow mSearchSortPw;
    private ArrayList<HashMap<String,String>> mSearchSortList = new ArrayList<>();
    private String getintentcity_code,search_reacode;
    private ArrayList<SearchSellResModel> mSearchInfoList = new ArrayList<SearchSellResModel>();
    private int fieldlistpagesize;
    public int good_type;
    private  String searchkeywords;
    private IWXAPI api;
    private ApiAdvResourcesModel apiResourcesModel = new ApiAdvResourcesModel();//搜索列表的model
    private String modelorder_by;
    private String modelorder;
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
    private ArrayList<String> mfieldtype_list = new ArrayList<String>();//场地类型所有的名称要显示在界面上的数组
    private HashMap<String, String> mfieldtype_map;//配置文件中的场地类型解析
    private ArrayList<String> madvertisingtype_list = new ArrayList<String>();//广告类型所有的名称要显示在界面上的数组
    private HashMap<String, String> madvertisingtype_map;//配置文件中的广告类型解析
    private ArrayList<String> facilities_list = new ArrayList<String>();//服务项目中所有的名称要显示在界面上的数组
    private HashMap<String, String> facilities_map;//配置文件中服务项目解析
    private ArrayList<String> mIsOutDoorList = new ArrayList<String>();//室内室外中所有的名称要显示在界面上的数组
    private ArrayList<String> mActivityTypesList = new ArrayList<String>();//活动类型 中所有的名称要显示在界面上的数组
    private HashMap<String, String> mIsOutDoorMap;//配置文件中室内室外
    private HashMap<String, String> mActivityTypesMap;//配置文件中活动类型
    private HashMap<String, String> field_labels_map;//配置文件中邻汇推荐解析
    private ArrayList<SearchAreaPwModel> mTradingAreasList;//配置文件中商圈解析
    private ArrayList<SearchAreaSubwayPwModel> mSubwayList;//配置文件中的地铁
    private ArrayList<SearchAreaPwModel> mSubwayLineList;//配置文件中的地铁线
    private ArrayList<String> field_labels_list = new ArrayList<String>();//邻汇推荐中所有的名称要显示在界面上的数组
    private ArrayList<SearchAreaPwModel> mDistrictList;//配置文件中城市解析
    private ArrayList<SearchAreaPwModel> mSearchAreaList;//区域第一列列表
    private ResourcesScreeningNewAdapter resourcesScreeningNewAdapter;
    public int GridviewNumColumns = 0;//根据屏幕尺寸设置gridview的一行显示个数
    public int district_id_int = 0;//判断城市区域是否有选中 1选中显示全部反之显示3条数据
    public int trading_areas_int = 0;//判断商圈是否有选中 1选中显示全部反之显示3条数据
    public int field_type_id_int = 0;//判断场地类型是否有选中 1选中显示全部反之显示3条数据
    public int field_labels_int = 0;//判断邻汇推荐是否有选中 1选中显示全部反之显示3条数据
    public int mIsOutDoorInt = 0;//判断场地位置是否有选中 1选中显示全部反之显示3条数据
    public int facilities_int = 0;//判断服务项目是否有选中 1选中显示全部反之显示3条数据
    public int ad_type_id_int = 0;//判断广告类型是否有选中 1选中显示全部反之显示3条数据
    public int activity_type_id_int = 0;//判断活动类型是否有选中 1选中显示全部反之显示3条数据
    private boolean screening_result_boolean;//判断是否显示搜索到的数据个数
    private final int HomePageTypeThemes = 4;
    private final int HomePageTypeActivity = 3;
    private final int HomePageTypeAdvertising = 2;
    private final int HomePageTypeField = 1;
    private Drawable mSortGreyUpDrawable;//排序
    private Drawable mSortGreyDownDrawable;//排序
    private Drawable mSortBlueUpDrawable;//排序
    private Drawable mSortBlueDownDrawable;//排序
    private boolean mSortChecked;//排序选中
    private SearchTitlePopupAdapter mSearchPuoupAdapter;
    private ArrayList<HashMap<String,Object>> mSearchPopUpDataList;
    boolean isShare;
    private ListView mSearchTitlePopUpLV;
    private SupportPopupWindow mSearchTitlePopUpPW;
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
    private boolean isCreate;//是否onCteate运行完
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mMainContent == null) {
            mMainContent = inflater.inflate(R.layout.module_fragment_search_adv_list, container , false);
            ButterKnife.inject(this, mMainContent);
            homeintent = SearchAdvListFragment.this.getActivity().getIntent();
            if (homeintent != null &&
                    homeintent.getExtras() != null) {
                if (homeintent.getExtras().get("is_home_page") != null) {
                    initview();
                    if (homeintent.getExtras().getInt("is_home_page") == HomePageTypeThemes) {
                        if (homeintent.getExtras().get("mSearchAddressBackMap") != null) {
                            mSearchAddressBackMap = (HashMap<String, Object>) homeintent.getExtras().get("mSearchAddressBackMap");
                        }
                        if (homeintent.getExtras().get("ApiResourcesModel") != null) {
                            apiResourcesModel = (ApiAdvResourcesModel) homeintent.getSerializableExtra("ApiResourcesModel");
                            if (apiResourcesModel.getCity_id() != null) {
                                getintentcity_code = apiResourcesModel.getCity_id();
                                search_reacode = getintentcity_code;
                            } else {
                                search_reacode = LoginManager.getInstance().getTrackcityid();
                            }
                            if (apiResourcesModel.getResource_type() != null) {
                                good_type = Integer.parseInt(apiResourcesModel.getResource_type());
                            } else {
                                good_type = HomePageTypeField;
                            }
                            getorder_by();
                            if (apiResourcesModel.getKeywords() != null) {
                                medit_search.setText(apiResourcesModel.getKeywords());
                            }
                            if (modelorder != null &&
                                    modelorder_by != null &&
                                    modelorder.length() > 0 &&
                                    modelorder_by.length() > 0) {

                            } else {
                                modelorder_by = "default_sort";
                                modelorder = "desc";
                            }
                            if (apiResourcesModel.getOrder() == null) {
                                apiResourcesModel.setOrder(modelorder);
                            }
                            if (apiResourcesModel.getOrder_by() == null) {
                                apiResourcesModel.setOrder_by(modelorder_by);
                            }
                            fieldlistpagesize = 1;
                            apiResourcesModel.setCity_id(search_reacode);
                            apiResourcesModel.setKeywords(medit_search.getText().toString());
                            apiResourcesModel.setResource_type(String.valueOf(good_type));
                            apiResourcesModel.setPage(String.valueOf(fieldlistpagesize));
                            apiResourcesModel.setPageSize("10");
                            showProgressDialog();
                            FieldApi.getAdvReslist(MyAsyncHttpClient.MyAsyncHttpClient_version_three(), getPublicFieldListHandler, apiResourcesModel);
                            sendBrowseHistories();
                        } else {
                            getintentcity_code = LoginManager.getInstance().getTrackcityid();
                            good_type = HomePageTypeField;
                            getorder_by();
                            fieldlistpagesize = 1;
                            search_reacode = getintentcity_code;
                            if (modelorder != null &&
                                    modelorder_by != null &&
                                    modelorder.length() > 0 &&
                                    modelorder_by.length() > 0) {

                            } else {
                                modelorder_by = "default_sort";
                                modelorder = "desc";
                            }
                            apiResourcesModel.setOrder_by(modelorder_by);
                            apiResourcesModel.setOrder(modelorder);
                            apiResourcesModel.setCity_id(search_reacode);
                            apiResourcesModel.setResource_type(String.valueOf(good_type));
                            apiResourcesModel.setPage(String.valueOf(fieldlistpagesize));
                            apiResourcesModel.setPageSize("10");
                            showProgressDialog();
                            FieldApi.getAdvReslist(MyAsyncHttpClient.MyAsyncHttpClient_version_three(), getPublicFieldListHandler, apiResourcesModel);
                            sendBrowseHistories();
                        }
                    } else if (homeintent.getExtras().getInt("is_home_page") == HomePageTypeActivity) {
                        getintentcity_code = LoginManager.getInstance().getTrackcityid();
                        good_type = HomePageTypeActivity;
                        getorder_by();
                        showProgressDialog();
                        initdata();
                    } else if (homeintent.getExtras().getInt("is_home_page") == HomePageTypeAdvertising) {
                        if (homeintent.getExtras().get("code") != null) {
                            ArrayList<Integer> adtypelist = new ArrayList<>();
                            adtypelist.add(homeintent.getExtras().getInt("code"));
                            apiResourcesModel.setAd_type_id(adtypelist);
                        }
                        getintentcity_code = LoginManager.getInstance().getTrackcityid();
                        good_type = HomePageTypeAdvertising;
                        getorder_by();
                        showProgressDialog();
                        initdata();
                    } else if (homeintent.getExtras().getInt("is_home_page") == HomePageTypeField) {
                        if (homeintent.getExtras().get("type") != null) {
                            getintentcity_code = LoginManager.getInstance().getTrackcityid();
                            good_type = HomePageTypeField;
                            if (homeintent.getExtras().getString("type").equals("daily_sale")) {
                                apiResourcesModel.setSubsidy(1);
                                mSearchBestSellCheckBox.setChecked(true);
                            } else if (homeintent.getExtras().getString("type").equals("new_field")) {
                                modelorder_by = "created_at";
                                modelorder = "desc";
                            } else if (homeintent.getExtras().getString("type").equals("hot_type")) {
                                apiResourcesModel.setField_type_id((ArrayList<Integer>) homeintent.getExtras().get("field_type_id"));
                            } else if (homeintent.getExtras().getString("type").equals("hot")) {
                                apiResourcesModel.setHot(1);
                                mSearchHotSellCheckBox.setChecked(true);
                            }
                            getorder_by();
                            showProgressDialog();
                            initdata();
                        }
                    }
                    getlistdata();
                    getfieldlistdata();
                    if (good_type == HomePageTypeAdvertising) {
                        getadlistdata();
                    }
                } else {
                    getintentcity_code = homeintent.getStringExtra("cityname_code");
                    good_type = homeintent.getExtras().getInt("good_type");
                    searchkeywords = homeintent.getStringExtra("keywords");
                    if (searchkeywords != null && searchkeywords.length() != 0) {
                        medit_search.setText(homeintent.getStringExtra("keywords"));
                    }
                    if (getintentcity_code == null) {
                        mlay_no_searchlist.setVisibility(View.VISIBLE);
                    } else {
                        mlay_no_searchlist.setVisibility(View.GONE);
                        if (getintentcity_code.length() == 0) {
                            mlay_no_searchlist.setVisibility(View.VISIBLE);
                        } else {
                            getorder_by();
                            getlistdata();
                            getfieldlistdata();
                            if (good_type == HomePageTypeAdvertising) {
                                getadlistdata();
                            }
                            initview();
                            showProgressDialog();
                            initdata();
                        }

                    }
                }
            } else {
                getintentcity_code = LoginManager.getInstance().getTrackcityid();
                good_type = 1;
                searchkeywords = "";
                if (searchkeywords != null && searchkeywords.length() != 0) {
                    medit_search.setText(homeintent.getStringExtra("keywords"));
                }
                if (getintentcity_code == null) {
                    mlay_no_searchlist.setVisibility(View.VISIBLE);
                } else {
                    mlay_no_searchlist.setVisibility(View.GONE);
                    if (getintentcity_code.length() == 0) {
                        mlay_no_searchlist.setVisibility(View.VISIBLE);
                    } else {
                        getorder_by();
                        getlistdata();
                        getfieldlistdata();
                        if (good_type == HomePageTypeAdvertising) {
                            getadlistdata();
                        }
                        initview();
                        showProgressDialog();
                        initdata();
                    }
                }
            }
            if (good_type == 1) {
                mSearchLabelLL.setVisibility(View.VISIBLE);
            } else {
                mSearchLabelLL.setVisibility(View.GONE);
            }
            api = WXAPIFactory.createWXAPI(SearchAdvListFragment.this.getActivity(), Constants.APP_ID);
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
            //区域选择状态
            chooseAreaType = -1;
            if (apiResourcesModel.getTrading_area_id() != null
                    && apiResourcesModel.getTrading_area_id().size() > 0) {
                chooseAreaType = 2;
            } else if (apiResourcesModel.getDistrict_id() != null
                    && apiResourcesModel.getDistrict_id().size() > 0) {
                chooseAreaType = 0;
            } else if (apiResourcesModel.getSubway_station_id() != null
                    && apiResourcesModel.getSubway_station_id().size() > 0) {//地铁
                chooseAreaType = 1;
            }
            getAreaList();
            showChooseAreaState();
            isCreate = true;
        }

        return mMainContent;

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResources().getString(R.string.search_activity_name_str));
        MobclickAgent.onResume(SearchAdvListFragment.this.getActivity());
        if (isCreate && getintentcity_code != null &&
                !getintentcity_code.trim().equals(LoginManager.getInstance().getTrackcityid().trim())
                && (homeintent == null || (homeintent != null && homeintent.getExtras() == null))) {
            getintentcity_code = LoginManager.getInstance().getTrackcityid();
            getlistdata();
            apiResourcesModel.setCity_id(LoginManager.getInstance().getTrackcityid());
            apiResourcesModel.setCity_name(LoginManager.getInstance().getTrackCityName());
            apiResourcesModel.setDistrict_id(null);
            apiResourcesModel.setTrading_area_id(null);
            apiResourcesModel.setLabel_id(null);
            msearch_area_type_textview.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
            msearch_area_type_textview.setTextColor(getResources().getColor(R.color.headline_tv_color));
            msearch_area_type_textview.setText(getResources().getString(R.string.invoiceinfo_area_txt));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResources().getString(R.string.search_activity_name_str));
        MobclickAgent.onPause(SearchAdvListFragment.this.getActivity());
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
    }

    private void initview() {
        if (SearchAdvListFragment.this.getActivity() instanceof MainTabActivity) {
            mSearchStatusBarLL.setVisibility(View.VISIBLE);
            mSearchBackTV.setVisibility(View.GONE);
        }
        if (good_type == HomePageTypeAdvertising) {
            msearch_jump_map_img.setVisibility(View.INVISIBLE);
        } else if (good_type == HomePageTypeField || good_type == HomePageTypeActivity) {
            msearch_jump_map_img.setVisibility(View.VISIBLE);
        }
        SearchListloadmoreList.setLoadMoreListen(this);
        SearchListswipList.setOnRefreshListener(this);
        SearchListloadmoreList.setOnScrollListener(this);
        SearchListloadmoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < mSearchInfoList.size()) {
                    Intent fieldinfo = null;
                    if (good_type != 2) {
                        fieldinfo = new Intent(SearchAdvListFragment.this.getActivity(), FieldInfoActivity.class);
                    } else {
                        fieldinfo = new Intent(SearchAdvListFragment.this.getActivity(), AdvertisingInfoActivity.class);
                    }
                    fieldinfo.putExtra("fieldId", String.valueOf(mSearchInfoList.get(position).getResource_id()));
                    fieldinfo.putExtra("good_type", mSearchInfoList.get(position).getRes_type_id());
                    startActivity(fieldinfo);
                }

            }
        });
        SearchListswipList.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mscreening_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_resources_screening_pw();
            }
        });
        mSearchSelfSupportShopCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchSelfSupportShopCheckBox.isChecked()) {
                    mSearchSelfSupportShopCheckBox.setChecked(true);
                    mSearchNewResCheckBox.setChecked(false);
                    mSearchHotSellCheckBox.setChecked(false);
                    mSearchBestSellCheckBox.setChecked(false);
                    apiResourcesModel.setResource_type("3");
                    apiResourcesModel.setHot(0);
                    apiResourcesModel.setSubsidy(0);
                    apiResourcesModel.setOrder_by("default_sort");
                    apiResourcesModel.setOrder("desc");
                    for (int i = 0; i < mSearchSortList.size(); i++) {
                        if (mSearchSortList.get(i).get("id").equals("default_sort") &&
                                mSearchSortList.get(i).get("order").equals("desc")) {
                            msort_txt.setText(mSearchSortList.get(i).get("display_short_name"));
                            break;
                        }
                    }
                    msort_txt.setCompoundDrawables(null, null,mSortBlueDownDrawable , null);
                    msort_txt.setTextColor(getResources().getColor(R.color.default_bluebg));
                    showProgressDialog();
                    searchinitdata();
                } else {
                    mSearchSelfSupportShopCheckBox.setChecked(false);
                    apiResourcesModel.setResource_type("1");
                    showProgressDialog();
                    searchinitdata();
                }
            }
        });
        mSearchHotSellCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchHotSellCheckBox.isChecked()) {
                    mSearchSelfSupportShopCheckBox.setChecked(false);
                    mSearchHotSellCheckBox.setChecked(true);
                    mSearchNewResCheckBox.setChecked(false);
                    mSearchBestSellCheckBox.setChecked(false);
                    apiResourcesModel.setResource_type("1");
                    apiResourcesModel.setHot(1);
                    apiResourcesModel.setSubsidy(0);
                    apiResourcesModel.setOrder_by("default_sort");
                    apiResourcesModel.setOrder("desc");
                    for (int i = 0; i < mSearchSortList.size(); i++) {
                        if (mSearchSortList.get(i).get("id").equals("default_sort") &&
                                mSearchSortList.get(i).get("order").equals("desc")) {
                            msort_txt.setText(mSearchSortList.get(i).get("display_short_name"));
                            break;
                        }
                    }
                    msort_txt.setCompoundDrawables(null, null,mSortBlueDownDrawable , null);
                    msort_txt.setTextColor(getResources().getColor(R.color.default_bluebg));
                    showProgressDialog();
                    searchinitdata();
                } else {
                    mSearchHotSellCheckBox.setChecked(false);
                    apiResourcesModel.setHot(0);
                    showProgressDialog();
                    searchinitdata();
                }
            }
        });
        mSearchBestSellCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchBestSellCheckBox.isChecked()) {
                    mSearchSelfSupportShopCheckBox.setChecked(false);
                    mSearchHotSellCheckBox.setChecked(false);
                    mSearchNewResCheckBox.setChecked(false);
                    mSearchBestSellCheckBox.setChecked(true);
                    apiResourcesModel.setResource_type("1");
                    apiResourcesModel.setHot(0);
                    apiResourcesModel.setSubsidy(1);
                    apiResourcesModel.setOrder_by("default_sort");
                    apiResourcesModel.setOrder("desc");
                    for (int i = 0; i < mSearchSortList.size(); i++) {
                        if (mSearchSortList.get(i).get("id").equals("default_sort") &&
                                mSearchSortList.get(i).get("order").equals("desc")) {
                            msort_txt.setText(mSearchSortList.get(i).get("display_short_name"));
                            break;
                        }
                    }
                    msort_txt.setCompoundDrawables(null, null,mSortBlueDownDrawable , null);
                    msort_txt.setTextColor(getResources().getColor(R.color.default_bluebg));
                    showProgressDialog();
                    searchinitdata();
                } else {
                    mSearchBestSellCheckBox.setChecked(false);
                    apiResourcesModel.setSubsidy(0);
                    showProgressDialog();
                    searchinitdata();
                }
            }
        });
        mSearchNewResCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchNewResCheckBox.isChecked()) {
                    mSearchSelfSupportShopCheckBox.setChecked(false);
                    mSearchHotSellCheckBox.setChecked(false);
                    mSearchNewResCheckBox.setChecked(true);
                    mSearchBestSellCheckBox.setChecked(false);
                    apiResourcesModel.setResource_type("1");
                    apiResourcesModel.setHot(0);
                    apiResourcesModel.setSubsidy(0);
                    apiResourcesModel.setOrder_by("created_at");
                    apiResourcesModel.setOrder("desc");
                    if (apiResourcesModel.getHighPrice() != null &&
                            apiResourcesModel.getHighPrice().length() > 0 &&
                            apiResourcesModel.getLowPrice() != null &&
                            apiResourcesModel.getLowPrice().length() > 0) {
                        msearch_price_type_textview.setCompoundDrawables(null, null, drawable_gray_price_gray, null);
                        msearch_price_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
                    } else {
                        msearch_price_type_textview.setCompoundDrawables(null, null, drawable_gray_price_gray, null);
                        msearch_price_type_textview.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                    }
                    price_mark_int = 0;
                    for (int i = 0; i < mSearchSortList.size(); i++) {
                        if (mSearchSortList.get(i).get("id").equals("created_at") &&
                                mSearchSortList.get(i).get("order").equals("desc")) {
                            msort_txt.setText(mSearchSortList.get(i).get("display_short_name"));
                            break;
                        }
                    }
                    msort_txt.setCompoundDrawables(null, null,mSortBlueDownDrawable , null);
                    msort_txt.setTextColor(getResources().getColor(R.color.default_bluebg));
                    mSortChecked = true;

                    showProgressDialog();
                    searchinitdata();
                } else {
                    mSearchBestSellCheckBox.setChecked(false);
                    apiResourcesModel.setOrder_by("default_sort");
                    apiResourcesModel.setOrder("desc");
                    showProgressDialog();
                    searchinitdata();
                    for (int i = 0; i < mSearchSortList.size(); i++) {
                        if (mSearchSortList.get(i).get("id").equals("default_sort") &&
                                mSearchSortList.get(i).get("order").equals("desc")) {
                            msort_txt.setText(mSearchSortList.get(i).get("display_short_name"));
                            break;
                        }
                    }
                    msort_txt.setCompoundDrawables(null, null,mSortGreyDownDrawable , null);
                    msort_txt.setTextColor(getResources().getColor(R.color.default_bluebg));
                    mSortChecked = true;
                }
            }
        });
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
        SearchListloadmoreList.set_refresh();
        search_reacode = getintentcity_code;
        fieldlistpagesize = 1;
        if (mSearchInfoList != null) {
            mSearchInfoList.clear();
        }
        mSearchListAdapter = new SearchAdvListAdapter(SearchAdvListFragment.this.getActivity(),mSearchInfoList,good_type,SearchAdvListFragment.this.getActivity(),false);
        SearchListloadmoreList.setAdapter(mSearchListAdapter);
        String keywords = medit_search.getText().toString();
        if ((homeintent != null && homeintent.getExtras() != null &&
                homeintent.getExtras().get("good_type") != null && homeintent.getExtras().getInt("good_type") == 3) ||
                (homeintent!= null && homeintent.getExtras() != null &&
                        homeintent.getExtras().get("is_home_page") != null && homeintent.getExtras().getInt("is_home_page") == 3)) {
            if (good_type == HomePageTypeActivity) {
                modelorder = "desc";
                modelorder_by = "activity_start_date";
            }
        }
        if (modelorder != null &&
                modelorder_by != null &&
                modelorder.length() > 0 &&
                modelorder_by.length() > 0) {

        } else {
            modelorder_by = "";
            modelorder = "desc";
        }
        apiResourcesModel.setOrder_by(modelorder_by);
        apiResourcesModel.setOrder(modelorder);
        apiResourcesModel.setCity_id(search_reacode);
        apiResourcesModel.setKeywords(keywords);
        apiResourcesModel.setResource_type(String.valueOf(good_type));
        apiResourcesModel.setPage(String.valueOf(fieldlistpagesize));
        apiResourcesModel.setPageSize("10");
        FieldApi.getAdvReslist(MyAsyncHttpClient.MyAsyncHttpClient(), getPublicFieldListHandler,apiResourcesModel);
        sendBrowseHistories();
    }
    private void searchinitdata() {
        SearchListloadmoreList.set_refresh();
        if (mSearchInfoList != null) {
            mSearchInfoList.clear();
        }
        mSearchListAdapter = new SearchAdvListAdapter(SearchAdvListFragment.this.getActivity(),mSearchInfoList,good_type,SearchAdvListFragment.this.getActivity(),false);
        SearchListloadmoreList.setAdapter(mSearchListAdapter);
        fieldlistpagesize = 1;
        apiResourcesModel.setPage(String.valueOf(fieldlistpagesize));
        apiResourcesModel.setPageSize("10");
        if (medit_search.getText().toString().length() > 0) {
            apiResourcesModel.setKeywords(medit_search.getText().toString());
        }
        FieldApi.getAdvReslist(MyAsyncHttpClient.MyAsyncHttpClient_version_three(), getPublicFieldListHandler,apiResourcesModel);
        sendBrowseHistories();
    }
    private void loadmoresearchfielddata() {
        if (mSearchInfoList != null) {
            if (mSearchInfoList.size() != 0) {
                fieldlistpagesize = fieldlistpagesize + 1;
                apiResourcesModel.setPage(String.valueOf(fieldlistpagesize));
                apiResourcesModel.setPageSize("10");
                FieldApi.getAdvReslist(MyAsyncHttpClient.MyAsyncHttpClient_version_three(), getMoreOrderListHandler,apiResourcesModel);
                sendBrowseHistories();
            } else {
                SearchListloadmoreList.onLoadComplete();
            }
        } else {
            SearchListloadmoreList.onLoadComplete();
        }

    }
    private LinhuiAsyncHttpResponseHandler getPublicFieldListHandler = new LinhuiAsyncHttpResponseHandler(SearchSellResModel.class,true) {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            isShare = false;
            mSearchSizeLL.setVisibility(View.GONE);
            SharedescriptionStr = String.valueOf(response.total);
            mSearchInfoList= (ArrayList<SearchSellResModel>)data;
            if( mSearchInfoList == null ||  mSearchInfoList.isEmpty()) {
                mlay_no_searchlist.setVisibility(View.VISIBLE);
                if (SearchListswipList.isShown()) {
                    SearchListswipList.setRefreshing(false);
                }
                return;
            }
            if (screening_result_boolean ||
                    ((apiResourcesModel.getSubway_station_id() != null &&
                            apiResourcesModel.getSubway_station_id().size() > 0) ||
                            (apiResourcesModel.getDistrict_id() != null &&
                                    apiResourcesModel.getDistrict_id().size() > 0) ||
                            (apiResourcesModel.getTrading_area_id() != null &&
                                    apiResourcesModel.getTrading_area_id().size() > 0))) {
                mSearchSizeLL.setVisibility(View.VISIBLE);
                if (apiResourcesModel.getSubway_station_id() != null &&
                        (apiResourcesModel.getDistrict_id() == null ||
                                apiResourcesModel.getDistrict_id().size() == 0) &&
                        (apiResourcesModel.getTrading_area_id() == null ||
                                apiResourcesModel.getTrading_area_id().size() == 0) &&
                        apiResourcesModel.getSubway_station_id().size() > 0) {
                    if (isAdded()) {
                        msearchlist_itemsize_text.setText(getResources().getString(R.string.search_list_subway_one_str)+
                                getResources().getString(R.string.search_fieldlist_results_one_text)+
                                SharedescriptionStr +getResources().getString(R.string.search_fieldlist_results_two_text));
                    }
                } else {
                    if (isAdded()) {
                        msearchlist_itemsize_text.setText(getResources().getString(R.string.search_fieldlist_results_one_text)+
                                SharedescriptionStr +getResources().getString(R.string.search_fieldlist_results_two_text));
                    }
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
            mlay_no_searchlist.setVisibility(View.GONE);
                mSearchListAdapter = new SearchAdvListAdapter(SearchAdvListFragment.this.getActivity(),mSearchInfoList,good_type,SearchAdvListFragment.this.getActivity(),false);

            SearchListloadmoreList.setAdapter(mSearchListAdapter);
            if (mSearchInfoList.size() < 10) {
                SearchListloadmoreList.set_loaded();
            }
            if (SearchListswipList.isShown()) {
                SearchListswipList.setRefreshing(false);
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            isShare = false;
            mSearchSizeLL.setVisibility(View.GONE);
            if (SearchListswipList.isShown()) {
                SearchListswipList.setRefreshing(false);
            }
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());

        }
    };
    private LinhuiAsyncHttpResponseHandler getMoreOrderListHandler = new LinhuiAsyncHttpResponseHandler(SearchSellResModel.class,true) {
        @Override
        public void onSuccess(int statusCode,okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            ArrayList<SearchSellResModel> tmp = (ArrayList<SearchSellResModel>) data;
            if( (tmp == null || tmp.isEmpty())){
                fieldlistpagesize = fieldlistpagesize-1;
                SearchListloadmoreList.onLoadComplete();
                SearchListloadmoreList.set_loaded();
                return;
            }
            for( SearchSellResModel order: tmp ){
                mSearchInfoList.add(order);
            }

            //广告多选模式datalist
            mSearchListAdapter.notifyDataSetChanged();
            SearchListloadmoreList.onLoadComplete();
            if (tmp.size() < 10) {
                SearchListloadmoreList.set_loaded();
            }

        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            SearchListloadmoreList.onLoadComplete();
            fieldlistpagesize = fieldlistpagesize - 1;
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());

        }
    };
    @Override
    public void loadMore() {

        loadmoresearchfielddata();
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
            R.id.search_add_demand_textview
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
//                apiResourcesModel.setOrder_by("number_of_order");
//                apiResourcesModel.setOrder("desc");
//                showProgressDialog();
//                searchinitdata();
//                msearch_area_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
//                msort_txt.setText(getResources().getString(R.string.search_fieldlist_default_sort));
//                msort_txt.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
//                msort_txt.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
//                mSortChecked = false;
//                msearch_price_type_textview.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
//                msearch_price_type_textview.setCompoundDrawables(null, null, drawable_gray_price_gray, null);
//                price_mark_int = 0;
                break;
            case R.id.search_price_type_layout:
                showSearchPricepw(mSearchPriceList);
                break;
            case R.id.search_area_backimg:
                SearchAdvListFragment.this.getActivity().finish();
                break;
            case R.id.edit_search_search:
                Intent searchatea_seraintent = new Intent(SearchAdvListFragment.this.getActivity(),SearchFieldAreaActivity.class);
                searchatea_seraintent.putExtra("search",3);
                searchatea_seraintent.putExtra("cityname_code",search_reacode);
                searchatea_seraintent.putExtra("ksywords", medit_search.getText().toString());
                if (good_type == HomePageTypeField) {
                    searchatea_seraintent.putExtra("res_type_id", 1);
                } else if (good_type == HomePageTypeAdvertising) {
                    searchatea_seraintent.putExtra("res_type_id", 2);
                } else if (good_type == HomePageTypeActivity) {
                    searchatea_seraintent.putExtra("res_type_id", 1);
                }
                startActivityForResult(searchatea_seraintent, 3);
                break;
            case R.id.search_jump_map_img:
                showTitlePopUp();
                break;
            case R.id.search_add_demand_textview:
                Intent AddDemand = new Intent(SearchAdvListFragment.this.getActivity(),AboutUsActivity.class);
                AddDemand.putExtra("type", Config.ADD_DEMAND_WEB_INT);
                startActivity(AddDemand);
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
                    View myView = SearchAdvListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                    mSearchShareDialog = new AlertDialog.Builder(SearchAdvListFragment.this.getActivity()).create();
                    if (good_type == HomePageTypeAdvertising) {
                        shareurl = Config.Domain_Name + "/advs/index?"+ getshareurl() + "&BackKey=1";
                    } else {
                        shareurl = Config.SHARE_FIELDS_LIST_URL+ getshareurl() + "&BackKey=1";
                        sharewxMinShareLinkUrl = Config.WX_MINI_SHARE_FIELDS_LIST_URL+ getshareurl();
                    }
                    if (mSearchShareDialog!= null && mSearchShareDialog.isShowing()) {
                        mSearchShareDialog.dismiss();
                    }
                    Constants constants = new Constants(SearchAdvListFragment.this.getActivity(),
                            ShareIconStr);
                    constants.shareWXMiniPopupWindow(SearchAdvListFragment.this.getActivity(),myView,mSearchShareDialog,api,shareurl,
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
                    if (mSearchSortList.get(i).get("id").equals("default_sort") &&
                            mSearchSortList.get(i).get("order").equals("desc")) {
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
                apiResourcesModel = new ApiAdvResourcesModel();
                mSearchSelfSupportShopCheckBox.setChecked(false);
                mSearchHotSellCheckBox.setChecked(false);
                mSearchBestSellCheckBox.setChecked(false);
                mSearchNewResCheckBox.setClickable(false);
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
        View myView = SearchAdvListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.activit_search_sort_pw_layout, null);
        DisplayMetrics metric = new DisplayMetrics();
        SearchAdvListFragment.this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
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
        lv.setAdapter(new SearchSortAdapter(SearchAdvListFragment.this.getActivity(), list,selectposition));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //execute the task
                        mSearchSortPw.dismiss();
                        if (mSortChecked) {
                            msort_txt.setCompoundDrawables(null, null, mSortBlueDownDrawable, null);
                        } else {
                            msort_txt.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                        }
                        if (apiResourcesModel.getHighPrice() != null &&
                                apiResourcesModel.getHighPrice().length() > 0 &&
                                apiResourcesModel.getLowPrice() != null &&
                                apiResourcesModel.getLowPrice().length() > 0) {
                            msearch_price_type_textview.setCompoundDrawables(null, null, drawable_gray_price_gray, null);
                            msearch_price_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
                        } else {
                            msearch_price_type_textview.setCompoundDrawables(null, null, drawable_gray_price_gray, null);
                            msearch_price_type_textview.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                        }
                        price_mark_int = 0;//价格排序取消
                        mSearchNewResCheckBox.setChecked(false);//上线取消
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
        View myView = SearchAdvListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.activity_search_price_pw, null);
        DisplayMetrics metric = new DisplayMetrics();
        SearchAdvListFragment.this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
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
        if (apiResourcesModel.getLowPrice() != null &&
                apiResourcesModel.getLowPrice().length() > 0 &&
                apiResourcesModel.getHighPrice() != null &&
                apiResourcesModel.getHighPrice().length() > 0) {
            minPriceET.setText(Constants.getpricestring(apiResourcesModel.getLowPrice(),0.01));
            maxPriceET.setText(Constants.getpricestring(apiResourcesModel.getHighPrice(),0.01));
        }

        int selectposition = -1;
        if (price_mark_int == 1) {
            selectposition = 0;
        } else if (price_mark_int == 2) {
            selectposition = 1;
        }
        lv.setAdapter(new SearchSortAdapter(SearchAdvListFragment.this.getActivity(), list,selectposition));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //execute the task
                        mSearchSortPw.dismiss();
                        for (int i = 0; i < mSearchSortList.size(); i++) {
                            if (mSearchSortList.get(i).get("id").equals("default_sort") &&
                                    mSearchSortList.get(i).get("order").equals("desc")) {
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
                            apiResourcesModel.setOrder_by("min_price");
                            apiResourcesModel.setOrder("desc");
                            showProgressDialog();
                            searchinitdata();
                            price_mark_int = 2;
                            mSearchNewResCheckBox.setChecked(false);
                            msearch_price_type_textview.setCompoundDrawables(null, null, drawable_gray_price_bottom, null);
                        } else if ((price_mark_int == 0 || price_mark_int == 2) &&
                                mSearchPriceList.get(position).get("order") != null &&
                                mSearchPriceList.get(position).get("order").equals("asc") ){
                            apiResourcesModel.setOrder_by("min_price");
                            apiResourcesModel.setOrder("asc");
                            showProgressDialog();
                            searchinitdata();
                            msearch_price_type_textview.setCompoundDrawables(null, null, drawable_gray_price_top, null);
                            price_mark_int = 1;
                            mSearchNewResCheckBox.setChecked(false);
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
                if (minPriceET.getText().toString().trim().length() == 0 &&
                        maxPriceET.getText().toString().trim().length() > 0) {
                    MessageUtils.showToast(getResources().getString(R.string.search_fieldlist_importprice_txt));
                    return;
                }
                if (maxPriceET.getText().toString().trim().length() == 0 &&
                        minPriceET.getText().toString().trim().length() > 0) {
                    MessageUtils.showToast(getResources().getString(R.string.search_fieldlist_importprice_txt));
                    return;
                }
                if (minPriceET.getText().toString().trim().length() > 0 &&
                        maxPriceET.getText().toString().trim().length() > 0) {
                    if (Double.parseDouble(minPriceET.getText().toString().trim()) >
                            Double.parseDouble(maxPriceET.getText().toString().trim())) {
                        MessageUtils.showToast(getResources().getString(R.string.addfield_price_acceptable_price_error_text));
                        return;
                    }
                }
                if (minPriceET.getText().toString().trim().length() == 0 &&
                        maxPriceET.getText().toString().trim().length() == 0) {
                    apiResourcesModel.setLowPrice("");
                    apiResourcesModel.setHighPrice("");
                } else {
                    apiResourcesModel.setLowPrice(Constants.getpricestring(Constants.getpricestring(minPriceET.getText().toString(),1),100));
                    apiResourcesModel.setHighPrice(Constants.getpricestring(Constants.getpricestring(maxPriceET.getText().toString(),1),100));
                }
                mSearchSortPw.dismiss();
                showProgressDialog();
                searchinitdata();
                price_mark_int = 0;
                if (apiResourcesModel.getHighPrice() != null &&
                        apiResourcesModel.getHighPrice().length() > 0 &&
                        apiResourcesModel.getLowPrice() != null &&
                        apiResourcesModel.getLowPrice().length() > 0) {
                    msearch_price_type_textview.setTextColor(getResources().getColor(R.color.default_bluebg));
                } else {
                    msearch_price_type_textview.setTextColor(getResources().getColor(R.color.orderconfirm_success_bottom_btntextcolor));
                }
                msearch_price_type_textview.setCompoundDrawables(null, null, drawable_gray_price_gray, null);
            }
        });
    }
    private void showAreaScreenPW () {
        View myView = SearchAdvListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.activity_search_area_screen_pw, null);
        DisplayMetrics metric = new DisplayMetrics();
        SearchAdvListFragment.this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
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
        if (apiResourcesModel.getTrading_area_id() != null
                && apiResourcesModel.getTrading_area_id().size() > 0) {
            chooseAreaType = 2;
        } else if (apiResourcesModel.getDistrict_id() != null
                && apiResourcesModel.getDistrict_id().size() > 0) {
            chooseAreaType = 0;
        } else if (apiResourcesModel.getSubway_station_id() != null
                && apiResourcesModel.getSubway_station_id().size() > 0) {//地铁
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
                            secondAdapter = new SearchAreaPwAdapter(SearchAdvListFragment.this.getActivity(), mSearchAreaList.get(position).getSecondList(),4);
                        } else {
                            secondChooseInt = -1;
                            secondAdapter = new SearchAreaPwAdapter(SearchAdvListFragment.this.getActivity(), mSearchAreaList.get(position).getSecondList(),2);
                        }
                        secondLv.setAdapter(secondAdapter);
                        secondLv.setVisibility(View.VISIBLE);
                        if (mSearchAreaList.get(firstChooseInt).getId() == 1) {
                            secondChooseInt = 0;
                            SearchAreaPwAdapter.clearIsThirdChoose();
                            for (int i = 0; i < mSubwayList.get(0).getStations().size(); i++) {
                                SearchAreaPwAdapter.getIsThirdChoose().put(mSubwayList.get(0).getStations().get(i).getId(),false);
                            }
                            thirdAdapter = new SearchAreaPwAdapter(SearchAdvListFragment.this.getActivity(), mSubwayList.get(0).getStations(),3);
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
                        thirdAdapter = new SearchAreaPwAdapter(SearchAdvListFragment.this.getActivity(), mSubwayList.get(position).getStations(),3);
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
                        apiResourcesModel.setSubway_station_id(mSubwayIntList);
                        apiResourcesModel.setDistrict_id(null);
                        apiResourcesModel.setTrading_area_id(null);
                    } else {
                        if (mSearchAreaList.size() > firstChooseInt) {//友盟错误日志修改
                            if (chooseAreaType == mSearchAreaList.get(firstChooseInt).getId()) {
                                apiResourcesModel.setSubway_station_id(null);
                                apiResourcesModel.setDistrict_id(null);
                                apiResourcesModel.setTrading_area_id(null);
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
                                apiResourcesModel.setDistrict_id(mDistrictIntList);
                                apiResourcesModel.setTrading_area_id(null);
                                apiResourcesModel.setSubway_station_id(null);
                            } else {
                                if (chooseAreaType == mSearchAreaList.get(firstChooseInt).getId()) {
                                    apiResourcesModel.setDistrict_id(null);
                                    apiResourcesModel.setTrading_area_id(null);
                                    apiResourcesModel.setSubway_station_id(null);
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
                                apiResourcesModel.setTrading_area_id(mTrading_areaIntList);
                                apiResourcesModel.setDistrict_id(null);
                                apiResourcesModel.setSubway_station_id(null);
                            } else {
                                if (chooseAreaType == mSearchAreaList.get(firstChooseInt).getId()) {
                                    apiResourcesModel.setTrading_area_id(null);
                                    apiResourcesModel.setDistrict_id(null);
                                    apiResourcesModel.setSubway_station_id(null);
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
                    apiResourcesModel.setSubway_station_id(null);
                    apiResourcesModel.setDistrict_id(null);
                    apiResourcesModel.setTrading_area_id(null);
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
                if (order_by != null) {
                    if (order_by.length() > 0) {
                        com.alibaba.fastjson.JSONArray cityarray = JSON.parseArray(order_by);
                        if (good_type == HomePageTypeField || good_type == HomePageTypeAdvertising) {
                            if (modelorder_by == null ||
                                    modelorder == null ||
                                    modelorder_by.length() == 0 ||
                                    modelorder.length() == 0) {
                                modelorder_by = "default_sort";
                                modelorder = "desc";
                            }
                        }
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
                                            if (good_type == HomePageTypeActivity) {
                                                if (jsonobject.getString("order_by").length() > 0) {
                                                    modelorder_by = jsonobject.getString("order_by");
                                                }
                                                if (jsonobject.getString("order").length() > 0) {
                                                    modelorder = jsonobject.getString("order");
                                                }
                                            }
                                        }
                                    }
                                    if (!keyorder_by.equals("preferential_price")) {
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
                            modelorder_by = "default_sort";
                            modelorder = "desc";
                        }
                    }
                } else {
                    if (modelorder_by == null ||
                            modelorder == null ||
                            modelorder_by.length() == 0 ||
                            modelorder.length() == 0) {
                        modelorder_by = "default_sort";
                        modelorder = "desc";
                    }
                }
            }
        }).start();
    }
    //搜索条件的界面表示
    private void getmscreening_txt_type() {
        if ((apiResourcesModel.getMinimum_area() != null && apiResourcesModel.getMinimum_area().length() > 0) ||
                (apiResourcesModel.getMaximum_area() != null && apiResourcesModel.getMaximum_area().length() > 0) ||
                (apiResourcesModel.getMaximum_peoples() != null && apiResourcesModel.getMaximum_peoples().length() > 0)
                || (apiResourcesModel.getMinimum_peoples() != null && apiResourcesModel.getMinimum_peoples().length() > 0)
                || (apiResourcesModel.getMinimum_build_year() != null && apiResourcesModel.getMinimum_build_year().length() > 0)
                || (apiResourcesModel.getMaximum_build_year() != null && apiResourcesModel.getMaximum_build_year().length() > 0)
                || (apiResourcesModel.getMinimum_households() != null && apiResourcesModel.getMinimum_households().length() > 0)
                || (apiResourcesModel.getMaximum_households() != null && apiResourcesModel.getMaximum_households().length() > 0) ||
                (apiResourcesModel.getMinimum_property_costs() != null && apiResourcesModel.getMinimum_property_costs().length() > 0)
                || (apiResourcesModel.getMaximum_property_costs() != null && apiResourcesModel.getMaximum_property_costs().length() > 0) ||
                (apiResourcesModel.getMaximum_house_price() != null && apiResourcesModel.getMaximum_house_price().length() > 0)
                || (apiResourcesModel.getMaximum_house_price() != null && apiResourcesModel.getMaximum_house_price().length() > 0)
                || (apiResourcesModel.getFacilities() != null && apiResourcesModel.getFacilities().size() > 0)
                || (apiResourcesModel.getField_type_id() != null && apiResourcesModel.getField_type_id().size() > 0)
                || (apiResourcesModel.getActivity_type_id() != null && apiResourcesModel.getActivity_type_id().size() > 0)
                || (apiResourcesModel.getIndoor() != null && apiResourcesModel.getIndoor().size() > 0)
                || (apiResourcesModel.getLabel_id() != null && apiResourcesModel.getLabel_id().size() > 0)
                || (apiResourcesModel.getAd_type_id() != null && apiResourcesModel.getAd_type_id().size() > 0)) {
            mscreening_txt.setTextColor(getResources().getColor(R.color.default_bluebg));
            Drawable drawable_add_pressed = getResources().getDrawable(R.drawable.ic_screening_select);
            drawable_add_pressed.setBounds(0, 0, drawable_add_pressed.getMinimumWidth(), drawable_add_pressed.getMinimumHeight());
            if (homeintent != null &&
                    homeintent.getExtras() != null && homeintent.getExtras().get("is_home_page") != null) {

            } else {
                mscreening_txt.setCompoundDrawables(null, null, drawable_add_pressed, null);
            }
            screening_result_boolean = true;
        }
        if (apiResourcesModel.getOrder_by() != null && apiResourcesModel.getOrder_by().equals("preferential_price")) {
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
        if (apiResourcesModel.getSelf_support() != null &&
                apiResourcesModel.getSelf_support() == 1) {
            mSearchSelfSupportShopCheckBox.setChecked(true);
        }
        if (apiResourcesModel.getHot() == 1) {
            mSearchHotSellCheckBox.setChecked(true);
        }
        if (apiResourcesModel.getSubsidy() == 1) {
            mSearchBestSellCheckBox.setChecked(true);
        }
    }
    public final static Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;

            conn = (HttpURLConnection) myFileUrl.openConnection();

            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    private String getshareurl() {
        String shareurl = "";
        String hot = "";
        String subsidy = "";
        String keywords = "";
        String city_id = "";
        String resource_type = "";
        String field_type_id = "";
        String activity_type_id = "";
        String indoor = "";
        String ad_type_id = "";
        String district_id = "";
        String trading_area_id = "";
        String community_type_id = "";
        String label_id = "";
        String facilities = "";
        String subway_station_id = "";
        String lowPrice = "";
        String highPrice = "";
        String minimum_peoples = "";
        String maximum_peoples = "";
        String minimum_area = "";
        String maximum_area = "";
        String order = "";
        String order_by = "";
        String page = "";
        String pageSize = "";
        String is_home_page = "";
        String minimum_build_year  = "";
        String maximum_build_year  = "";
        String minimum_households  = "";
        String maximum_households  = "";
        String minimum_property_costs  = "";
        String maximum_property_costs  = "";
        String minimum_house_price  = "";
        String maximum_house_price  = "";
        String nearby = "";
        String latitude = "";
        String longitude = "";
        String latitude_delta = "";
        String longitude_delta = "";
        String zoom_level = "";
        city_id = "city_id="+apiResourcesModel.getCity_id();
        resource_type = "&resource_type="+apiResourcesModel.getResource_type();
        if (apiResourcesModel.getHot() == 1) {
            hot = "&hot="+apiResourcesModel.getHot();
        }
        if (apiResourcesModel.getSubsidy() == 1) {
            subsidy = "&subsidy="+apiResourcesModel.getSubsidy();
        }
        if (apiResourcesModel.getKeywords() != null) {
            if (apiResourcesModel.getKeywords().length() > 0) {
                keywords = "&keywords="+apiResourcesModel.getKeywords();
            }
        }
        if (apiResourcesModel.getField_type_id() != null && apiResourcesModel.getField_type_id().size() > 0) {
            String jsonstr =  JSON.toJSONString(apiResourcesModel.getField_type_id(),true).trim();
            String substr  = jsonstr.substring(1, jsonstr.length()-1).trim();
            field_type_id = ("&field_type_id="+ substr);
        } else {
            field_type_id = "&field_type_id=";
        }
        if (apiResourcesModel.getActivity_type_id() != null && apiResourcesModel.getActivity_type_id().size() > 0) {
            String jsonstr =  JSON.toJSONString(apiResourcesModel.getActivity_type_id(),true).trim();
            String substr  = jsonstr.substring(1, jsonstr.length()-1).trim();
            activity_type_id = ("&activity_type_id="+ substr);
        } else {
            activity_type_id = "&activity_type_id=";
        }
        if (apiResourcesModel.getIndoor() != null && apiResourcesModel.getIndoor().size() > 0) {
            String jsonstr =  JSON.toJSONString(apiResourcesModel.getIndoor(),true).trim();
            String substr  = jsonstr.substring(1, jsonstr.length()-1).trim();
            indoor = ("&indoor="+ substr);
        } else {
            indoor = "&indoor=";
        }
        if (apiResourcesModel.getAd_type_id() != null && apiResourcesModel.getAd_type_id().size() > 0) {
            String jsonstr =  JSON.toJSONString(apiResourcesModel.getAd_type_id(),true).trim();
            String substr  = jsonstr.substring(1, jsonstr.length()-1).trim();
            ad_type_id = ("&ad_type_id="+substr);
        } else {
            ad_type_id = "&ad_type_id=";
        }

        if (apiResourcesModel.getDistrict_id() != null && apiResourcesModel.getDistrict_id().size() > 0) {
            String jsonstr =  JSON.toJSONString(apiResourcesModel.getDistrict_id(),true).trim();
            String substr  = jsonstr.substring(1, jsonstr.length()-1).trim();
            district_id = ("&district_id="+substr);
        } else {
            district_id = "&district_id=";
        }
        if (apiResourcesModel.getTrading_area_id() != null && apiResourcesModel.getTrading_area_id().size() > 0) {
            String jsonstr =  JSON.toJSONString(apiResourcesModel.getTrading_area_id(),true).trim();
            String substr  = jsonstr.substring(1, jsonstr.length()-1).trim();
            trading_area_id = ("&trading_area_id="+substr);
        } else {
            trading_area_id = "&trading_area_id=";
        }
        if (apiResourcesModel.getCommunity_type_id() != null && apiResourcesModel.getCommunity_type_id().size() > 0) {
            String jsonstr =  JSON.toJSONString(apiResourcesModel.getCommunity_type_id(),true).trim();
            String substr  = jsonstr.substring(1, jsonstr.length()-1).trim();
            community_type_id = ("&community_type_id="+substr);
        } else {
            community_type_id = "&community_type_id=";
        }
        if (apiResourcesModel.getLabel_id() != null && apiResourcesModel.getLabel_id().size() > 0) {
            String jsonstr =  JSON.toJSONString(apiResourcesModel.getLabel_id(),true).trim();
            String substr  = jsonstr.substring(1, jsonstr.length()-1).trim();
            label_id = ("&label_id="+substr);
        } else {
            label_id = "&label_id=";
        }
        if (apiResourcesModel.getFacilities() != null && apiResourcesModel.getFacilities().size() > 0) {
            String substr = "";
            for (int i = 0; i < apiResourcesModel.getFacilities().size(); i++) {
                if (i == 0) {
                    substr = substr +apiResourcesModel.getFacilities().get(i);
                } else {
                    substr = substr + "," + apiResourcesModel.getFacilities().get(i);
                }
            }
            facilities = ("&facilities="+substr);
        } else {
            facilities = "&facilities=";
        }
        if (apiResourcesModel.getSubway_station_id() != null && apiResourcesModel.getSubway_station_id().size() > 0) {
            String substr = "";
            for (int i = 0; i < apiResourcesModel.getSubway_station_id().size(); i++) {
                if (i == 0) {
                    substr = substr +apiResourcesModel.getSubway_station_id().get(i);
                } else {
                    substr = substr + "," + apiResourcesModel.getSubway_station_id().get(i);
                }
            }
            subway_station_id = ("&subway_station_id="+substr);
        } else {
            subway_station_id = "&subway_station_id=";
        }

        if (apiResourcesModel.getLowPrice() != null) {
            if (apiResourcesModel.getLowPrice().length() > 0) {
                lowPrice = "&lowPrice="+Constants.getpricestring(apiResourcesModel.getLowPrice(),0.01);
            }
        }
        if (apiResourcesModel.getHighPrice() != null) {
            if (apiResourcesModel.getHighPrice().length() > 0) {
                highPrice = "&highPrice="+Constants.getpricestring(apiResourcesModel.getHighPrice(),0.01);
            }
        }
        if (apiResourcesModel.getMinimum_peoples() != null) {
            if (apiResourcesModel.getMinimum_peoples().length() > 0) {
                minimum_peoples = "&minimum_peoples="+apiResourcesModel.getMinimum_peoples();
            }
        }
        if (apiResourcesModel.getMaximum_peoples() != null) {
            if (apiResourcesModel.getMaximum_peoples().length() > 0) {
                maximum_peoples = "&maximum_peoples="+apiResourcesModel.getMaximum_peoples();
            }
        }

        if (apiResourcesModel.getMinimum_area() != null) {
            if (apiResourcesModel.getMinimum_area().length() > 0) {
                minimum_area = "&minimum_area="+apiResourcesModel.getMinimum_area();
            }
        }
        if (apiResourcesModel.getMaximum_area() != null) {
            if (apiResourcesModel.getMaximum_area().length() > 0) {
                maximum_area = "&maximum_area="+apiResourcesModel.getMaximum_area();
            }
        }

        order = "&order="+apiResourcesModel.getOrder();
        order_by = "&order_by="+apiResourcesModel.getOrder_by();
        page = "&page=1";
        if (apiResourcesModel.getPageSize() != null) {
            if (apiResourcesModel.getPageSize().length() > 0) {
                pageSize = "&pageSize="+apiResourcesModel.getPageSize();
            }
        }
        if (apiResourcesModel.getIs_home_page() == 1) {
            is_home_page = "&is_home_page="+apiResourcesModel.getIs_home_page();
        }
        if (apiResourcesModel.getMinimum_build_year() != null) {
            if (apiResourcesModel.getMinimum_build_year().length() > 0) {
                minimum_build_year = "&minimum_build_year="+apiResourcesModel.getMinimum_build_year();
            }
        }
        if (apiResourcesModel.getMaximum_build_year() != null) {
            if (apiResourcesModel.getMaximum_build_year().length() > 0) {
                maximum_build_year = "&maximum_build_year="+apiResourcesModel.getMaximum_build_year();
            }
        }
        if (apiResourcesModel.getMinimum_households() != null) {
            if (apiResourcesModel.getMinimum_households().length() > 0) {
                minimum_households = "&minimum_households="+apiResourcesModel.getMinimum_households();
            }
        }
        if (apiResourcesModel.getMaximum_households() != null) {
            if (apiResourcesModel.getMaximum_households().length() > 0) {
                maximum_households = "&maximum_households="+apiResourcesModel.getMaximum_households();
            }
        }
        if (apiResourcesModel.getMinimum_property_costs() != null) {
            if (apiResourcesModel.getMinimum_property_costs().length() > 0) {
                minimum_property_costs = "&minimum_property_costs="+apiResourcesModel.getMinimum_property_costs();
            }
        }
        if (apiResourcesModel.getMaximum_property_costs() != null) {
            if (apiResourcesModel.getMaximum_property_costs().length() > 0) {
                maximum_property_costs = "&maximum_property_costs="+apiResourcesModel.getMaximum_property_costs();
            }
        }
        if (apiResourcesModel.getMinimum_house_price() != null) {
            if (apiResourcesModel.getMinimum_house_price().length() > 0) {
                minimum_house_price = "&minimum_house_price="+Constants.getpricestring(apiResourcesModel.getMinimum_house_price(),0.01);
            }
        }
        if (apiResourcesModel.getMaximum_house_price() != null) {
            if (apiResourcesModel.getMaximum_house_price().length() > 0) {
                maximum_house_price = "&maximum_house_price="+Constants.getpricestring(apiResourcesModel.getMaximum_house_price(),0.01);
            }
        }
        //
        if (apiResourcesModel.getNearby() > 0) {
            nearby = "&nearby="+ String.valueOf(apiResourcesModel.getNearby());
        }
        if (apiResourcesModel.getLatitude() > 0) {
            latitude = "&latitude="+ String.valueOf(apiResourcesModel.getLatitude());
        }
        if (apiResourcesModel.getLongitude() > 0) {
            longitude = "&longitude="+ String.valueOf(apiResourcesModel.getLongitude());
        }
        if (apiResourcesModel.getLatitude_delta() > 0) {
            latitude_delta = "&latitude_delta="+ String.valueOf(apiResourcesModel.getLatitude_delta());
        }
        if (apiResourcesModel.getLongitude_delta() > 0) {
            longitude_delta = "&longitude_delta="+ String.valueOf(apiResourcesModel.getLongitude_delta());
        }
        if (apiResourcesModel.getZoom_level() > 0) {
            zoom_level = "&zoom_level="+ String.valueOf(apiResourcesModel.getZoom_level());
        }

        shareurl = city_id+"&is_app=1"+hot+subsidy+keywords+resource_type+field_type_id+activity_type_id+indoor+ad_type_id+district_id+trading_area_id+
                community_type_id+label_id+facilities+ subway_station_id + lowPrice+highPrice+minimum_peoples+maximum_peoples+
                minimum_area+maximum_area+order+order_by+page+pageSize+is_home_page+minimum_build_year+maximum_build_year+
                minimum_households+maximum_households+minimum_property_costs+maximum_property_costs+minimum_house_price
                +maximum_house_price + nearby + latitude + longitude + latitude_delta + longitude_delta + zoom_level;
        return shareurl;
    }
    private void show_resources_screening_pw() {
        final ArrayList<HashMap<Object,Object>> data_new = new ArrayList<>();
        district_id_int = 0;
        field_type_id_int = 0;
        field_labels_int = 0;
        mIsOutDoorInt = 0;
        facilities_int = 0;
        ad_type_id_int = 0;
        activity_type_id_int = 0;
        View myView = SearchAdvListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.activity_resourcesscreening, null);
        mresourcesscreening_stickygridview_new  = (ListView)myView.findViewById(R.id.resourcesscreening_stickygridview_new);
        Button mresetbtn = (Button)myView.findViewById(R.id.resetbtn);
        Button mconfirmbtn = (Button)myView.findViewById(R.id.confirmbtn);
        DisplayMetrics metric = new DisplayMetrics();
        SearchAdvListFragment.this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;

        int HeaderId = 0;
        int width_screening = metric.widthPixels;   // 屏幕宽度（像素）
        if ((width_screening -40)/4 > 174 ||(width_screening -40)/4 == 174 || (width_screening -40)/4 > 166) {
            GridviewNumColumns = 4;
        } else  {
            GridviewNumColumns = 3;
        }
        if (good_type == HomePageTypeField) {
            ArrayList<HashMap<Object,Object>> mfieldtype_listdata = new ArrayList<HashMap<Object,Object>>();
            for (int i = 0; i <mfieldtype_list.size(); i++) {
                HashMap<Object,Object> map = new HashMap<Object,Object>();
                map.put("type","field_type_id");
                map.put("field_type_id",mfieldtype_list.get(i));
                map.put("HeaderId",HeaderId);
                mfieldtype_listdata.add(map);
            }
            HashMap<Object,Object> mfieldtype_listmap = new HashMap<Object,Object>();
            mfieldtype_listmap.put("type","field_type_id");
            mfieldtype_listmap.put("datalist",mfieldtype_listdata);
            data_new.add(mfieldtype_listmap);
        } else if (good_type == HomePageTypeActivity) {
            ArrayList<HashMap<Object,Object>> mfieldtype_listdata = new ArrayList<HashMap<Object,Object>>();
            for (int i = 0; i <mfieldtype_list.size(); i++) {
                HashMap<Object,Object> map = new HashMap<Object,Object>();
                map.put("type","field_type_id");
                map.put("field_type_id",mfieldtype_list.get(i));
                map.put("HeaderId",HeaderId);
                mfieldtype_listdata.add(map);
            }
            HashMap<Object,Object> mfieldtype_listmap = new HashMap<Object,Object>();
            mfieldtype_listmap.put("type","field_type_id");
            mfieldtype_listmap.put("datalist",mfieldtype_listdata);
            data_new.add(mfieldtype_listmap);
//            ArrayList<HashMap<Object,Object>> mactivitytype_listdata = new ArrayList<HashMap<Object,Object>>();
//            for (int i = 0; i <mActivityTypesList.size(); i++) {
//                HashMap<Object,Object> map = new HashMap<Object,Object>();
//                map.put("type","activity_type_id");
//                map.put("activity_type_id",mActivityTypesList.get(i));
//                map.put("HeaderId",HeaderId);
//                mactivitytype_listdata.add(map);
//            }
//            HashMap<Object,Object> mactivitytype_listmap = new HashMap<Object,Object>();
//            mactivitytype_listmap.put("type","activity_type_id");
//            mactivitytype_listmap.put("datalist",mactivitytype_listdata);
//            data_new.add(mactivitytype_listmap);
        } else if (good_type == HomePageTypeAdvertising) {
            ArrayList<HashMap<Object,Object>> madvertisingtype_listdata = new ArrayList<HashMap<Object,Object>>();
            for (int i = 0; i <madvertisingtype_list.size(); i++) {
                HashMap<Object,Object> map = new HashMap<Object,Object>();
                map.put("type","ad_type_id");
                map.put("ad_type_id",madvertisingtype_list.get(i));
                map.put("HeaderId",HeaderId);
                madvertisingtype_listdata.add(map);
            }
            HashMap<Object,Object> madvertisingtype_listmap = new HashMap<Object,Object>();
            madvertisingtype_listmap.put("type","ad_type_id");
            madvertisingtype_listmap.put("datalist",madvertisingtype_listdata);
            data_new.add(madvertisingtype_listmap);
        }
        if (good_type == HomePageTypeField || good_type == HomePageTypeActivity) {
            //服务项目
            HeaderId++;
            ArrayList<HashMap<Object,Object>> facilities_listdata = new ArrayList<HashMap<Object,Object>>();
            for (int i = 0; i <facilities_list.size(); i++) {
                HashMap<Object,Object> map = new HashMap<Object,Object>();
                map.put("type","facilities");
                map.put("facilities",facilities_list.get(i));
                map.put("HeaderId",HeaderId);
                facilities_listdata.add(map);
            }
            HashMap<Object,Object> facilities_listmap = new HashMap<Object,Object>();
            facilities_listmap.put("type","facilities");
            facilities_listmap.put("datalist",facilities_listdata);
            data_new.add(facilities_listmap);
            // 摆摊推荐
            HeaderId++;
            ArrayList<HashMap<Object,Object>> field_labels_listdata = new ArrayList<HashMap<Object,Object>>();
            for (int i = 0; i <field_labels_list.size(); i++) {
                HashMap<Object,Object> map = new HashMap<Object,Object>();
                map.put("type","field_labels");
                map.put("field_labels",field_labels_list.get(i));
                map.put("HeaderId",HeaderId);
                field_labels_listdata.add(map);
            }
            HashMap<Object,Object> field_labels_listmap = new HashMap<Object,Object>();
            field_labels_listmap.put("type","field_labels");
            field_labels_listmap.put("datalist",field_labels_listdata);
            data_new.add(field_labels_listmap);
            // 场地位置
            HeaderId++;
            ArrayList<HashMap<Object,Object>> mIsOutDoorListData = new ArrayList<HashMap<Object,Object>>();
            for (int i = 0; i < mIsOutDoorList.size(); i++) {
                HashMap<Object,Object> map = new HashMap<Object,Object>();
                map.put("type","is_outdoor");
                map.put("is_outdoor",mIsOutDoorList.get(i));
                map.put("HeaderId",HeaderId);
                mIsOutDoorListData.add(map);
            }
            HashMap<Object,Object> is_outdoor_listmap = new HashMap<Object,Object>();
            is_outdoor_listmap.put("type","is_outdoor");
            is_outdoor_listmap.put("datalist",mIsOutDoorListData);
            data_new.add(is_outdoor_listmap);

        }
        //人流量
        final HashMap<Object,Object> numper_of_peoplemap = new HashMap<Object,Object>();
        numper_of_peoplemap.put("type","numper_of_people");
        numper_of_peoplemap.put("itemtype",1);
        data_new.add(numper_of_peoplemap);
        //场地面积
        HashMap<Object,Object> areamap = new HashMap<Object,Object>();
        areamap.put("type","area");
        areamap.put("itemtype",1);
        data_new.add(areamap);
        //修改后的adapter
        ResourcesScreeningItemAdapter.clear_resourcescreeninglist();
        //是否选中的标志
        for (int j = 0;j < data_new.size(); j++ ) {
            if (data_new.get(j).get("datalist") != null && ((ArrayList<HashMap<Object,Object>>)data_new.get(j).get("datalist")).size() > 0) {
                ArrayList<HashMap<Object,Object>> data_new_temp = new ArrayList<>();
                data_new_temp.addAll(((ArrayList<HashMap<Object,Object>>) (data_new.get(j).get("datalist"))));
                for (int i = 0; i <data_new_temp.size(); i++) {
                    if (data_new_temp.get(i).get("itemtype") != null && ((Integer)data_new_temp.get(i).get("itemtype") == 1 ||
                            (Integer)data_new_temp.get(i).get("itemtype") == 2)) {

                    } else {
                        ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString() + data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(), false);
                        if (good_type == HomePageTypeField || good_type == HomePageTypeActivity) {
                            if (good_type == HomePageTypeField) {
                                if (mfieldtype_map.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()) != null) {
                                    if (apiResourcesModel.getField_type_id() != null) {
                                        if (apiResourcesModel.getField_type_id().size() > 0) {
                                            field_type_id_int = 1;
                                            if (apiResourcesModel.getField_type_id().contains(Integer.parseInt(mfieldtype_map.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()).toString()))) {
                                                ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString()+data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(),true);
                                            }
                                        }
                                    }
                                }

                            } else {
                                if (mActivityTypesMap.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()) != null) {
                                    if (apiResourcesModel.getActivity_type_id() != null) {
                                        if (apiResourcesModel.getActivity_type_id().size() > 0) {
                                            activity_type_id_int = 1;
                                            if (apiResourcesModel.getActivity_type_id().contains(Integer.parseInt(mActivityTypesMap.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()).toString()))) {
                                                ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString()+data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(),true);
                                            }
                                        }
                                    }
                                }

                            }
                            if (data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()) != null &&
                                    field_labels_map != null &&
                                    field_labels_map.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()) != null) {
                                if (apiResourcesModel.getLabel_id() != null) {
                                    if (apiResourcesModel.getLabel_id().size() > 0) {
                                        field_labels_int = 1;
                                        if (apiResourcesModel.getLabel_id().contains(Integer.parseInt(field_labels_map.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()).toString()))) {
                                            ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString()+data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(),true);
                                        }
                                    }
                                }

                            }
                            if (mIsOutDoorMap.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()) != null) {
                                if (apiResourcesModel.getIndoor() != null) {
                                    if (apiResourcesModel.getIndoor().size() > 0) {
                                        mIsOutDoorInt = 1;
                                        if (apiResourcesModel.getIndoor().contains(Integer.parseInt(mIsOutDoorMap.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()).toString()))) {
                                            ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString()+data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(),true);
                                        }
                                    }
                                }
                            }

                            if (facilities_map.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()) != null) {
                                if (apiResourcesModel.getFacilities() != null) {
                                    if (apiResourcesModel.getFacilities().size() > 0) {
                                        facilities_int = 1;
                                        if (apiResourcesModel.getFacilities().contains(facilities_map.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()).toString())) {
                                            ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString()+data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(),true);
                                        }
                                    }
                                }

                            }

                        } else if (good_type == HomePageTypeAdvertising) {
                            if (madvertisingtype_map.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()) != null) {
                                if (apiResourcesModel.getAd_type_id() != null) {
                                    if (apiResourcesModel.getAd_type_id().size() > 0) {
                                        ad_type_id_int = 1;
                                        if (apiResourcesModel.getAd_type_id().contains(Integer.parseInt(madvertisingtype_map.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()).toString()))) {
                                            ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString()+data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(),true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (data_new.get(j).get("type").toString().equals("price")) {
                    if (apiResourcesModel.getLowPrice() != null && apiResourcesModel.getLowPrice().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("pricemin",Constants.getpricestring(apiResourcesModel.getLowPrice(),0.01));
                    } else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("pricemin","");
                    }
                    if (apiResourcesModel.getHighPrice() != null && apiResourcesModel.getHighPrice().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("pricemax",Constants.getpricestring(apiResourcesModel.getHighPrice(),0.01));
                    } else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("pricemax","");
                    }

                } else if (data_new.get(j).get("type").toString().equals("numper_of_people")) {
                    if (apiResourcesModel.getMinimum_peoples() != null && apiResourcesModel.getMinimum_peoples().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("numper_of_peoplemin",apiResourcesModel.getMinimum_peoples());
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("numper_of_peoplemin","");
                    }
                    if (apiResourcesModel.getMaximum_peoples() != null && apiResourcesModel.getMaximum_peoples().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("numper_of_peoplemax",apiResourcesModel.getMaximum_peoples());
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("numper_of_peoplemax","");
                    }
                } else if (data_new.get(j).get("type").toString().equals("year")) {
                    if (apiResourcesModel.getMinimum_build_year() != null && apiResourcesModel.getMinimum_build_year().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("yearmin",apiResourcesModel.getMinimum_build_year());
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("yearmin","");
                    }
                    if (apiResourcesModel.getMaximum_build_year() != null && apiResourcesModel.getMaximum_build_year().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("yearmax",apiResourcesModel.getMaximum_build_year());
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("yearmax","");
                    }
                } else if (data_new.get(j).get("type").toString().equals("area")) {
                    if (apiResourcesModel.getMinimum_area() != null && apiResourcesModel.getMinimum_area().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("areamin",apiResourcesModel.getMinimum_area());
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("areamin","");
                    }
                    if (apiResourcesModel.getMaximum_area() != null && apiResourcesModel.getMaximum_area().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("areamax",apiResourcesModel.getMaximum_area());
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("areamax","");
                    }
                } else if (data_new.get(j).get("type").toString().equals("households")) {
                    if (apiResourcesModel.getMinimum_households() != null && apiResourcesModel.getMinimum_households().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("householdsmin",Constants.getpricestring(apiResourcesModel.getMinimum_households(),1));
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("householdsmin","");
                    }
                    if (apiResourcesModel.getMaximum_households() != null && apiResourcesModel.getMaximum_households().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("householdsmax",Constants.getpricestring(apiResourcesModel.getMaximum_households(),1));
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("householdsmax","");
                    }
                } else if (data_new.get(j).get("type").toString().equals("property_costs")) {
                    if (apiResourcesModel.getMinimum_property_costs() != null && apiResourcesModel.getMinimum_property_costs().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("property_costsmin",Constants.getpricestring(apiResourcesModel.getMinimum_property_costs(),0.01));
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("property_costsmin","");
                    }
                    if (apiResourcesModel.getMaximum_property_costs() != null && apiResourcesModel.getMaximum_property_costs().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("property_costsmax",Constants.getpricestring(apiResourcesModel.getMaximum_property_costs(),0.01));
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("property_costsmax","");
                    }
                } else if (data_new.get(j).get("type").toString().equals("house_price")) {
                    if (apiResourcesModel.getMinimum_house_price() != null && apiResourcesModel.getMinimum_house_price().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("house_pricemin",Constants.getpricestring(apiResourcesModel.getMinimum_house_price(),0.01));
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("house_pricemin","");
                    }
                    if (apiResourcesModel.getMaximum_house_price() != null && apiResourcesModel.getMaximum_house_price().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("house_pricemax",Constants.getpricestring(apiResourcesModel.getMaximum_house_price(),0.01));
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("house_pricemax","");
                    }
                }
            }
        }
        resourcesScreeningNewAdapter = new ResourcesScreeningNewAdapter(SearchAdvListFragment.this.getActivity(),SearchAdvListFragment.this,data_new,3);
        mresourcesscreening_stickygridview_new.setAdapter(resourcesScreeningNewAdapter);
        mconfirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭软键盘
                InputMethodManager imm = (InputMethodManager) SearchAdvListFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(mresourcesscreening_stickygridview_new.getWindowToken(), 0);
                }
                mresourcesscreening_stickygridview_new.clearFocus();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ArrayList<Integer> field_type_id_list = new ArrayList<>();
                        ArrayList<Integer> activity_type_id_list = new ArrayList<>();
                        ArrayList<Integer> mIsOutDoorIntList = new ArrayList<>();
                        ArrayList<Integer> ad_type_id_list = new ArrayList<>();
                        ArrayList<String> facilities_list = new ArrayList<>();
                        String mminimum_priceedit = "";
                        String mmaximum_priceedit = "";
                        String mminimum_numberofpeople_edit = "";
                        String mmaximum_numberofpeople_edit = "";
                        String mminimum_yearedit = "";
                        String mmaximum_yearedit = "";
                        String mminimum_householdsedit = "";
                        String mmaximum_householdsedit = "";
                        String mminimum_property_costsedit = "";
                        String mmaximum_property_costsedit = "";
                        String mminimum_house_priceedit = "";
                        String mmaximum_house_priceedit = "";
                        String area_edit = "";
                        String area_max_edit = "";
                        ArrayList<Integer> fieldlabel_list = new ArrayList<>();
                        for (int j = 0;j < data_new.size(); j++ ) {
                            if (data_new.get(j).get("datalist") != null && ((ArrayList<HashMap<Object,Object>>)data_new.get(j).get("datalist")).size() > 0) {
                                ArrayList<HashMap<Object,Object>> data_new_temp = new ArrayList<>();
                                data_new_temp.addAll(((ArrayList<HashMap<Object,Object>>) (data_new.get(j).get("datalist"))));
                                for (int i = 0; i <data_new_temp.size(); i++) {
                                    if (data_new_temp.get(i).get("itemtype") != null && ((Integer)data_new_temp.get(i).get("itemtype") == 1 ||
                                            (Integer)data_new_temp.get(i).get("itemtype") == 2)) {

                                    } else {
                                        if ((boolean)ResourcesScreeningItemAdapter.getresourcescreeninglist().get(data_new_temp.get(i).get("type").toString()+data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())) {
                                            if (data_new_temp.get(i).get("type").toString().equals("field_type_id")) {
                                                field_type_id_list.add(Integer.parseInt(mfieldtype_map.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())));
                                            } else if (data_new_temp.get(i).get("type").toString().equals("activity_type_id")) {
                                                activity_type_id_list.add(Integer.parseInt(mActivityTypesMap.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())));
                                            } else if (data_new_temp.get(i).get("type").toString().equals("is_outdoor")) {
                                                mIsOutDoorIntList.add(Integer.parseInt(mIsOutDoorMap.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())));
                                            } else if (data_new_temp.get(i).get("type").toString().equals("ad_type_id")) {
                                                ad_type_id_list.add(Integer.parseInt(madvertisingtype_map.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())));
                                            } else if (data_new_temp.get(i).get("type").toString().equals("facilities")) {
                                                facilities_list.add(facilities_map.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString()));
                                            } else if (data_new_temp.get(i).get("type").toString().equals("field_labels")) {
                                                fieldlabel_list.add(Integer.parseInt(field_labels_map.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())));
                                            } else if (data_new_temp.get(i).get("type").toString().equals("is_outdoor")) {
                                                fieldlabel_list.add(Integer.parseInt(mIsOutDoorMap.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())));
                                            } else if (data_new_temp.get(i).get("type").toString().equals("activity_types")) {
                                                fieldlabel_list.add(Integer.parseInt(mActivityTypesMap.get(data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString())));
                                            }
                                        }

                                    }
                                }
                            } else {
                                if (data_new.get(j).get("type").toString().equals("price")) {
                                    if (ResourcesScreeningNewAdapter.getedittextmap().get("pricemin").toString().length() > 0 &&
                                            ResourcesScreeningNewAdapter.getedittextmap().get("pricemax").toString().length() > 0) {
                                        if (Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("pricemin").toString(), 100),1)) >
                                                Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("pricemax").toString(), 100),1))) {
                                            ResourcesScreeningNewAdapter.getedittextmap().put("pricemin","");
                                            ResourcesScreeningNewAdapter.getedittextmap().put("pricemax","");
                                            MessageUtils.showToast(getResources().getString(R.string.resourcesscreening_edittext_priceerror));
                                            resourcesScreeningNewAdapter.notifyDataSetChanged();
                                            return;
                                        }
                                    }
                                    mminimum_priceedit = ResourcesScreeningNewAdapter.getedittextmap().get("pricemin").toString();
                                    mmaximum_priceedit = ResourcesScreeningNewAdapter.getedittextmap().get("pricemax").toString();
                                } else if (data_new.get(j).get("type").toString().equals("numper_of_people")) {
                                    if (ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemin").toString().length() > 0 &&
                                            ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemax").toString().length() > 0) {
                                        if (Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemin").toString(), 1),1)) >
                                                Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemax").toString(), 1),1))) {
                                            ResourcesScreeningNewAdapter.getedittextmap().put("numper_of_peoplemin","");
                                            ResourcesScreeningNewAdapter.getedittextmap().put("numper_of_peoplemax","");
                                            MessageUtils.showToast(getResources().getString(R.string.resourcesscreening_edittext_peopleerror));
                                            resourcesScreeningNewAdapter.notifyDataSetChanged();
                                            return;
                                        }
                                    }
                                    mminimum_numberofpeople_edit = ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemin").toString();
                                    mmaximum_numberofpeople_edit = ResourcesScreeningNewAdapter.getedittextmap().get("numper_of_peoplemax").toString();
                                } else if (data_new.get(j).get("type").toString().equals("year")) {
                                    if (ResourcesScreeningNewAdapter.getedittextmap().get("yearmin").toString().length() > 0 &&
                                            ResourcesScreeningNewAdapter.getedittextmap().get("yearmax").toString().length() > 0) {
                                        if (Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("yearmin").toString(), 1),1)) >
                                                Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("yearmax").toString(), 1),1))) {
                                            ResourcesScreeningNewAdapter.getedittextmap().put("yearmin","");
                                            ResourcesScreeningNewAdapter.getedittextmap().put("yearmax","");
                                            MessageUtils.showToast(getResources().getString(R.string.resourcesscreening_edittext_yearerror));
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
                                            MessageUtils.showToast(getResources().getString(R.string.resourcesscreening_edittext_householdserror));
                                            resourcesScreeningNewAdapter.notifyDataSetChanged();
                                            return;
                                        }
                                    }
                                    area_edit = ResourcesScreeningNewAdapter.getedittextmap().get("areamin").toString();
                                    area_max_edit = ResourcesScreeningNewAdapter.getedittextmap().get("areamax").toString();
                                } else if (data_new.get(j).get("type").toString().equals("households")) {
                                    if (ResourcesScreeningNewAdapter.getedittextmap().get("householdsmin").toString().length() > 0 &&
                                            ResourcesScreeningNewAdapter.getedittextmap().get("householdsmax").toString().length() > 0) {
                                        if (Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("householdsmin").toString(), 1),1)) >
                                                Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("householdsmax").toString(), 1),1))) {
                                            ResourcesScreeningNewAdapter.getedittextmap().put("householdsmin","");
                                            ResourcesScreeningNewAdapter.getedittextmap().put("householdsmax","");
                                            MessageUtils.showToast(getResources().getString(R.string.resourcesscreening_edittext_householdserror));
                                            resourcesScreeningNewAdapter.notifyDataSetChanged();
                                            return;
                                        }
                                    }
                                    mminimum_householdsedit = ResourcesScreeningNewAdapter.getedittextmap().get("householdsmin").toString();
                                    mmaximum_householdsedit = ResourcesScreeningNewAdapter.getedittextmap().get("householdsmax").toString();
                                } else if (data_new.get(j).get("type").toString().equals("property_costs")) {
                                    if (ResourcesScreeningNewAdapter.getedittextmap().get("property_costsmin").toString().length() > 0 &&
                                            ResourcesScreeningNewAdapter.getedittextmap().get("property_costsmax").toString().length() > 0) {
                                        if (Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("property_costsmin").toString(), 100),1)) >
                                                Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("property_costsmax").toString(), 100),1))) {
                                            ResourcesScreeningNewAdapter.getedittextmap().put("property_costsmin","");
                                            ResourcesScreeningNewAdapter.getedittextmap().put("property_costsmax","");
                                            MessageUtils.showToast(getResources().getString(R.string.resourcesscreening_edittext_priceerror));
                                            resourcesScreeningNewAdapter.notifyDataSetChanged();
                                            return;
                                        }
                                    }
                                    mminimum_property_costsedit = ResourcesScreeningNewAdapter.getedittextmap().get("property_costsmin").toString();
                                    mmaximum_property_costsedit = ResourcesScreeningNewAdapter.getedittextmap().get("property_costsmax").toString();
                                } else if (data_new.get(j).get("type").toString().equals("house_price")) {
                                    if (ResourcesScreeningNewAdapter.getedittextmap().get("house_pricemin").toString().length() > 0 &&
                                            ResourcesScreeningNewAdapter.getedittextmap().get("house_pricemax").toString().length() > 0) {
                                        if (Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("house_pricemin").toString(), 100),1)) >
                                                Double.parseDouble(Constants.getpricestring(Constants.getpricestring(ResourcesScreeningNewAdapter.getedittextmap().get("house_pricemax").toString(), 100),1))) {
                                            ResourcesScreeningNewAdapter.getedittextmap().put("house_pricemin","");
                                            ResourcesScreeningNewAdapter.getedittextmap().put("house_pricemax","");
                                            MessageUtils.showToast(getResources().getString(R.string.resourcesscreening_edittext_priceerror));
                                            resourcesScreeningNewAdapter.notifyDataSetChanged();
                                            return;
                                        }
                                    }
                                    mminimum_house_priceedit = ResourcesScreeningNewAdapter.getedittextmap().get("house_pricemin").toString();
                                    mmaximum_house_priceedit = ResourcesScreeningNewAdapter.getedittextmap().get("house_pricemax").toString();
                                }
                            }
                        }
                        apiResourcesModel.setField_type_id(field_type_id_list);
                        apiResourcesModel.setActivity_type_id(activity_type_id_list);
                        apiResourcesModel.setIndoor(mIsOutDoorIntList);
                        apiResourcesModel.setAd_type_id(ad_type_id_list);
                        apiResourcesModel.setFacilities(facilities_list);
                        apiResourcesModel.setLabel_id(fieldlabel_list);
                        if (mminimum_priceedit.length() > 0) {
                            apiResourcesModel.setLowPrice(Constants.getpricestring(Constants.getpricestring(mminimum_priceedit,1),100));
                        } else {
                            apiResourcesModel.setLowPrice(mminimum_priceedit);
                        }
                        if (mmaximum_priceedit.length() > 0) {
                            apiResourcesModel.setHighPrice(Constants.getpricestring(Constants.getpricestring(mmaximum_priceedit,1),100));
                        } else {
                            apiResourcesModel.setHighPrice(mmaximum_priceedit);
                        }
                        apiResourcesModel.setMinimum_peoples(mminimum_numberofpeople_edit);
                        apiResourcesModel.setMaximum_peoples(mmaximum_numberofpeople_edit);
                        apiResourcesModel.setMinimum_build_year(mminimum_yearedit);
                        apiResourcesModel.setMaximum_build_year(mmaximum_yearedit);
                        apiResourcesModel.setMinimum_households(mminimum_householdsedit);
                        apiResourcesModel.setMaximum_households(mmaximum_householdsedit);
                        if (mminimum_property_costsedit.length() > 0) {
                            apiResourcesModel.setMinimum_property_costs(Constants.getpricestring(Constants.getpricestring(mminimum_property_costsedit,1),100));
                        } else {
                            apiResourcesModel.setMinimum_property_costs(mminimum_property_costsedit);
                        }
                        if (mmaximum_property_costsedit.length() > 0) {
                            apiResourcesModel.setMaximum_property_costs(Constants.getpricestring(Constants.getpricestring(mmaximum_property_costsedit,1),100));
                        } else {
                            apiResourcesModel.setMaximum_property_costs(mmaximum_property_costsedit);
                        }
                        if (mminimum_house_priceedit.length() > 0) {
                            apiResourcesModel.setMinimum_house_price(Constants.getpricestring(Constants.getpricestring(mminimum_house_priceedit,1),100));
                        } else {
                            apiResourcesModel.setMinimum_house_price(mminimum_house_priceedit);
                        }
                        if (mmaximum_house_priceedit.length() > 0) {
                            apiResourcesModel.setMaximum_house_price(Constants.getpricestring(Constants.getpricestring(mmaximum_house_priceedit,1),100));
                        } else {
                            apiResourcesModel.setMaximum_house_price(mmaximum_house_priceedit);
                        }
                        apiResourcesModel.setMinimum_area(area_edit);
                        apiResourcesModel.setMaximum_area(area_max_edit);
                        if (apiResourcesModel.getMinimum_area().length() > 0 || apiResourcesModel.getMaximum_area().length() > 0||
                                apiResourcesModel.getMaximum_peoples().length() > 0
                                || apiResourcesModel.getMinimum_peoples().length() > 0|| apiResourcesModel.getMinimum_build_year().length() > 0 || apiResourcesModel.getMaximum_build_year().length() > 0
                                || apiResourcesModel.getMinimum_households().length() > 0 || apiResourcesModel.getMaximum_households().length() > 0 ||
                                apiResourcesModel.getMinimum_property_costs().length() > 0 || apiResourcesModel.getMaximum_property_costs().length() > 0 ||
                                apiResourcesModel.getMaximum_house_price().length() > 0 || apiResourcesModel.getMaximum_house_price().length() > 0
                                || apiResourcesModel.getFacilities().size() > 0 || apiResourcesModel.getField_type_id().size() > 0
                                || apiResourcesModel.getActivity_type_id().size() > 0
                                || apiResourcesModel.getIndoor().size() > 0
                                || apiResourcesModel.getLabel_id().size() > 0
                                || apiResourcesModel.getAd_type_id().size() > 0) {
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
                resourcesScreeningNewAdapter.notifyDataSetChanged();
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
                field_labels_map = new HashMap<>();
                mTradingAreasList = new ArrayList<>();
                field_labels_list = new ArrayList<>();
                mSubwayLineList = new ArrayList<>();
                if (getintentcity_code == null || getintentcity_code.length() == 0) {
                    getintentcity_code = "90";
                }
                List<ConfigCityParameterModel> districts = ConfigSqlOperation.selectSQL(1,Integer.parseInt(getintentcity_code),SearchAdvListFragment.this.getContext());
                List<ConfigCityParameterModel> labels = ConfigSqlOperation.selectSQL(2,Integer.parseInt(getintentcity_code),SearchAdvListFragment.this.getContext());
                List<ConfigCityParameterModel> trading_areas = ConfigSqlOperation.selectSQL(3,Integer.parseInt(getintentcity_code),SearchAdvListFragment.this.getContext());
                List<ConfigCityParameterModel> subway_stations = ConfigSqlOperation.selectSQL(4,Integer.parseInt(getintentcity_code),SearchAdvListFragment.this.getContext());
                List<ConfigCityParameterModel> districtsList = new ArrayList<>();
                List<ConfigCityParameterModel> labelsList = new ArrayList<>();
                List<ConfigCityParameterModel> trading_areasList = new ArrayList<>();
                List<ConfigCityParameterModel> subway_stationsList = new ArrayList<>();
                ArrayList<ConfigCitiesModel> citylist = ConfigurationsModel.getInstance().getCitylist();
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
                    for (int j = 0; j < labelsList.size(); j++) {
                        String labelkey = String.valueOf(labelsList.get(j).getId());
                        String labelvalue = labelsList.get(j).getDisplay_name();
                        field_labels_map.put(labelvalue, labelkey);
                        field_labels_list.add(labelvalue);
                    }
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
                            List<ConfigCityParameterModel> stationsList = ConfigSqlOperation.selectSQL(5,subway_stationsList.get(i).getSubway_line_id(),SearchAdvListFragment.this.getContext());
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
    private void getfieldlistdata() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String typelist,facilities,field_labels,is_outdoor,activity_types;
                typelist = LoginManager.getfieldtype();
                facilities = LoginManager.getFacility_tags();
                field_labels = LoginManager.getField_labels();
                is_outdoor = LoginManager.getIs_outdoor();
                activity_types = LoginManager.getActivity_types();
                com.alibaba.fastjson.JSONArray array = JSON.parseArray(typelist);
                com.alibaba.fastjson.JSONArray facilitiesarray = JSON.parseArray(facilities);
                com.alibaba.fastjson.JSONArray field_labelsarray = JSONArray.parseArray(field_labels);
                com.alibaba.fastjson.JSONArray mIsOutDoorArray = JSONArray.parseArray(is_outdoor);
                com.alibaba.fastjson.JSONArray mActivityTypesArray = JSONArray.parseArray(activity_types);
                mfieldtype_map = new HashMap<String, String>();
                facilities_map = new HashMap<>();
                mIsOutDoorMap = new HashMap<>();
                mActivityTypesMap = new HashMap<>();
                if(array != null) {
                    for (int i = 0; i < array.size(); i++) {
                        com.alibaba.fastjson.JSONObject jsonobject = array.getJSONObject(i);
                        String key = jsonobject.getString("field_type_id");
                        String value = jsonobject.getString("display_name");
                        if (!value.equals("不限")) {
                            mfieldtype_map.put(value, key);
                        }
                    }
                }
                if(facilitiesarray != null) {
                    for (int i = 0; i < facilitiesarray.size(); i++) {
                        com.alibaba.fastjson.JSONObject jsonobject = facilitiesarray.getJSONObject(i);
                        String tag = jsonobject.getString("tag");
                        String dispaly_name = jsonobject.getString("dispaly_name");
                        facilities_map.put(dispaly_name, tag);
                    }
                }
                if(mIsOutDoorArray != null) {
                    for (int i = 0; i < mIsOutDoorArray.size(); i++) {
                        com.alibaba.fastjson.JSONObject jsonobject = mIsOutDoorArray.getJSONObject(i);
                        String tag = jsonobject.getString("tag");
                        String dispaly_name = jsonobject.getString("display_name");
                        mIsOutDoorMap.put(dispaly_name, tag);
                    }
                }
                if(mActivityTypesArray != null) {
                    for (int i = 0; i < mActivityTypesArray.size(); i++) {
                        com.alibaba.fastjson.JSONObject jsonobject = mActivityTypesArray.getJSONObject(i);
                        String tag = jsonobject.getString("code");
                        String dispaly_name = jsonobject.getString("type");
                        mActivityTypesMap.put(dispaly_name, tag);
                    }
                }

                List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(
                        mfieldtype_map.entrySet());
                List<Map.Entry<String, String>> facilitiesinfoIds = new ArrayList<Map.Entry<String, String>>(
                        facilities_map.entrySet());
                List<Map.Entry<String, String>> mIsOutDoorSetList = new ArrayList<Map.Entry<String, String>>(
                        mIsOutDoorMap.entrySet());
                List<Map.Entry<String, String>> mActivityTypesSetList = new ArrayList<Map.Entry<String, String>>(
                        mActivityTypesMap.entrySet());

                Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
                    public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                        return (o1.getValue().compareTo(o2.getValue()));
                    }
                });

                Collections.sort(facilitiesinfoIds, new Comparator<Map.Entry<String, String>>() {
                    public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                        return (o1.getValue().compareTo(o2.getValue()));
                    }
                });
                Collections.sort(mActivityTypesSetList, new Comparator<Map.Entry<String, String>>() {
                    public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                        return (o1.getValue().compareTo(o2.getValue()));
                    }
                });
                if (mfieldtype_list != null) {
                    mfieldtype_list.clear();
                }
                for (int i = 0; i < infoIds.size(); i++) {
                    String name = infoIds.get(i).toString();
                    mfieldtype_list.add(name.substring(0, name.indexOf("=")));
                }

                if (facilities_list != null) {
                    facilities_list.clear();
                }
                for (int i = 0; i < facilitiesinfoIds.size(); i++) {
                    String name = facilitiesinfoIds.get(i).toString();
                    facilities_list.add(name.substring(0, name.indexOf("=")));
                }
                if (mIsOutDoorList != null) {
                    mIsOutDoorList.clear();
                }
                for (int i = 0; i < mIsOutDoorSetList.size(); i++) {
                    String name = mIsOutDoorSetList.get(i).toString();
                    mIsOutDoorList.add(name.substring(0, name.indexOf("=")));
                }
                if (mActivityTypesList != null) {
                    mActivityTypesList.clear();
                }
                for (int i = 0; i < mActivityTypesSetList.size(); i++) {
                    String name = mActivityTypesSetList.get(i).toString();
                    mActivityTypesList.add(name.substring(0, name.indexOf("=")));
                }
            }
        }).start();
    }
    private void getadlistdata() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ad_typelist;
                ad_typelist = LoginManager.getAd_type();
                com.alibaba.fastjson.JSONArray ad_type_array = JSON.parseArray(ad_typelist);
                madvertisingtype_map = new HashMap<>();
                if(ad_type_array != null) {
                    for (int i = 0; i < ad_type_array.size(); i++) {
                        com.alibaba.fastjson.JSONObject jsonobject = ad_type_array.getJSONObject(i);
                        String key = jsonobject.getString("code");
                        String value = jsonobject.getString("type");
                        if (!value.equals("不限")) {
                            madvertisingtype_map.put(value, key);
                        }
                    }
                }
                List<Map.Entry<String, String>> advertisinginfoIds = new ArrayList<Map.Entry<String, String>>(
                        madvertisingtype_map.entrySet());
                Collections.sort(advertisinginfoIds, new Comparator<Map.Entry<String, String>>() {
                    public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                        return (o1.getValue().compareTo(o2.getValue()));
                    }
                });

                if (madvertisingtype_list != null) {
                    madvertisingtype_list.clear();
                }
                for (int i = 0; i < advertisinginfoIds.size(); i++) {
                    String name = advertisinginfoIds.get(i).toString();
                    madvertisingtype_list.add(name.substring(0, name.indexOf("=")));
                }
            }
        }).start();
    }
    private void showTitlePopUp() {
        //通过view 和宽·高，构造PopopWindow
        View myView = SearchAdvListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.activity_search_title_popup_view, null);
        mSearchTitlePopUpPW = new SupportPopupWindow(myView,  LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //设置焦点为可点击
        mSearchTitlePopUpPW.setFocusable(true);//可以试试设为false的结果
        mSearchTitlePopUpPW.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mSearchTitlePopUpPW.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //将window视图显示在myButton下面
        mSearchTitlePopUpPW.showAsDropDown(mSearchTitlePopUpView);
        mSearchTitlePopUpLV = (ListView)myView.findViewById(R.id.search_title_popup_lv);
        if (mSearchPuoupAdapter != null) {
            mSearchTitlePopUpLV.setAdapter(mSearchPuoupAdapter);
        } else {
            mSearchPopUpDataList = new ArrayList<>();
            if (good_type == HomePageTypeField) {
                for (int i = 0; i < 2; i++) {
                    HashMap<String,Object> hashMap = new HashMap<>();
                    if (i == 0) {
                        hashMap.put("name",getResources().getString(R.string.search_popup_first_tv_str));
                        hashMap.put("resId",R.drawable.popup_ic_share);
                        hashMap.put("id",getResources().getString(R.string.search_popup_first_id_str));
                    } else if (i == 1) {
                        hashMap.put("name",getResources().getString(R.string.search_popup_second_tv_str));
                        hashMap.put("resId",R.drawable.ic_map_black_default_three);
                        hashMap.put("id",getResources().getString(R.string.search_popup_second_id_str));
                    }
                    mSearchPopUpDataList.add(hashMap);
                }
            } else {
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("name",getResources().getString(R.string.search_popup_first_tv_str));
                hashMap.put("resId",R.drawable.popup_ic_share);
                hashMap.put("id",getResources().getString(R.string.search_popup_first_id_str));
                mSearchPopUpDataList.add(hashMap);
            }
            mSearchPuoupAdapter = new SearchTitlePopupAdapter(SearchAdvListFragment.this.getActivity(),mSearchPopUpDataList);
            mSearchTitlePopUpLV.setAdapter(mSearchPuoupAdapter);
        }
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchTitlePopUpPW.isShowing()) {
                    mSearchTitlePopUpPW.dismiss();
                }
            }
        });
        mSearchTitlePopUpLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSearchPopUpDataList.get(position).get("id") != null &&
                        mSearchPopUpDataList.get(position).get("id").toString().equals(getResources().getString(R.string.search_popup_first_id_str))) {
                    if (isShare || !isShare) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (mSearchInfoList.size() > 0 &&
                                        mSearchInfoList.get(0).getPic_url() != null &&
                                        mSearchInfoList.get(0).getPic_url().length() > 0) {
                                    ShareIconStr = mSearchInfoList.get(0).getPic_url().toString() + com.linhuiba.linhuipublic.config.Config.Linhui_Mid_Watermark;
                                    ShareBitmap = com.linhuiba.linhuifield.connector.Constants.GetLocalOrNetBitmap(ShareIconStr);//压缩Bitma
                                    miniShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 120, 120, true);//压缩Bitmap
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
                } else if (mSearchPopUpDataList.get(position).get("id") != null &&
                        mSearchPopUpDataList.get(position).get("id").toString().equals(getResources().getString(R.string.search_popup_second_id_str))) {
                    Intent baiduintent = new Intent(SearchAdvListFragment.this.getActivity(), BaiduMapActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("goodtype",good_type);
                    mBundle.putString("citycode",getintentcity_code);
                    mBundle.putSerializable("ApiResourcesModel",apiResourcesModel);
                    mBundle.putSerializable("mSearchAddressBackMap",mSearchAddressBackMap);
                    baiduintent.putExtras(mBundle);
                    startActivity(baiduintent);
                }
                mSearchTitlePopUpPW.dismiss();
            }
        });

    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int scrollState) {

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
        /*
        if (mSubwayList != null && mSubwayList.size() > 0) {
            SearchAreaPwModel secondModel = new SearchAreaPwModel();
            secondModel.setName(getResources().getString(R.string.search_list_area_subway));
            secondModel.setId(1);
            secondModel.setSecondList(mSubwayLineList);
            mSearchAreaList.add(secondModel);
        }
        */
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
                firstAdapter = new SearchAreaPwAdapter(SearchAdvListFragment.this.getActivity(), mSearchAreaList,1);
                firstLv.setAdapter(firstAdapter);
                firstLv.setVisibility(View.VISIBLE);
                SearchAreaPwAdapter.clearIsSecondChoose();
                if (chooseAreaType != -1 && !isReste) {
                    if (mSearchAreaList.get(firstChooseInt).getId() == chooseAreaType) {
                        if (mSearchAreaList.get(firstChooseInt).getId() == 0) {
                            String searchAreaStr = "";
                            for (int j = 0; j < mSearchAreaList.get(firstChooseInt).getSecondList().size(); j++) {
                                if (apiResourcesModel.getDistrict_id() != null &&
                                        apiResourcesModel.getDistrict_id().contains(mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getId())) {
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
                                if (apiResourcesModel.getTrading_area_id() != null &&
                                        apiResourcesModel.getTrading_area_id().contains(mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getId())) {
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
                    secondAdapter = new SearchAreaPwAdapter(SearchAdvListFragment.this.getActivity(), mSearchAreaList.get(firstChooseInt).getSecondList(),4);
                } else {
                    secondAdapter = new SearchAreaPwAdapter(SearchAdvListFragment.this.getActivity(), mSearchAreaList.get(firstChooseInt).getSecondList(),2);
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
                firstAdapter = new SearchAreaPwAdapter(SearchAdvListFragment.this.getActivity(), mSearchAreaList,1);
                firstLv.setAdapter(firstAdapter);
                firstLv.setVisibility(View.VISIBLE);
                SearchAreaPwAdapter.clearIsSecondChoose();
                for (int i = 0; i < mSubwayLineList.size(); i++) {
                    SearchAreaPwAdapter.getIsSecondChoose().put(mSubwayLineList.get(i).getId(),false);
                }
                secondAdapter = new SearchAreaPwAdapter(SearchAdvListFragment.this.getActivity(), mSubwayLineList,4);
                secondLv.setAdapter(secondAdapter);
                secondLv.setVisibility(View.VISIBLE);
                SearchAreaPwAdapter.clearIsThirdChoose();
                if (mSubWayLinePosition < 0) {
                    ok:
                    for (int i = 0; i < mSubwayList.size(); i++) {
                        if (mSubwayList.get(i).getStations() != null) {
                            for (int j = 0; j < mSubwayList.get(i).getStations().size(); j++) {
                                if (apiResourcesModel.getSubway_station_id().
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
                        if (apiResourcesModel.getSubway_station_id() != null &&
                                apiResourcesModel.getSubway_station_id().contains(mSubwayList.get(mSubWayLinePosition).getStations().get(i).getId())) {
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
                    thirdAdapter = new SearchAreaPwAdapter(SearchAdvListFragment.this.getActivity(), mSubwayList.get(mSubWayLinePosition).getStations(),3);
                    thirdLv.setAdapter(thirdAdapter);
                    thirdLv.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private void showChooseAreaState() {
        if (mSearchAreaList != null && mSearchAreaList.size() > 0) {
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
                                if (apiResourcesModel.getDistrict_id() != null &&
                                        apiResourcesModel.getDistrict_id().contains(mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getId())) {
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
                                if (apiResourcesModel.getTrading_area_id() != null &&
                                        apiResourcesModel.getTrading_area_id().contains(mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getId())) {
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
                                if (apiResourcesModel.getSubway_station_id().
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
                        if (apiResourcesModel.getSubway_station_id() != null &&
                                apiResourcesModel.getSubway_station_id().contains(mSubwayList.get(mSubWayLinePosition).getStations().get(i).getId())) {
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
    private HashMap<String,String> getBrowseHistoriesUrl() {
        JSONArray jsonArray = new JSONArray();
        HashMap<String, String> paramsMap = new HashMap<>();
        if (apiResourcesModel.getHot() == 1) {
            paramsMap.put("hot", String.valueOf(apiResourcesModel.getHot()));
        }
        if (apiResourcesModel.getSubsidy() == 1) {
            paramsMap.put("subsidy", String.valueOf(apiResourcesModel.getSubsidy()));
        }
        if (apiResourcesModel.getSelf_support() != null && apiResourcesModel.getSelf_support() == 1) {
            paramsMap.put("self_support", String.valueOf(apiResourcesModel.getSelf_support()));
        }
        if (apiResourcesModel.getKeywords() != null) {
            if (apiResourcesModel.getKeywords().length() > 0) {
                paramsMap.put("keywords", apiResourcesModel.getKeywords());
            }
        }
        paramsMap.put("city_id", apiResourcesModel.getCity_id());
        paramsMap.put("resource_type", apiResourcesModel.getResource_type());

        if (apiResourcesModel.getActivity_type_id() != null &&
                apiResourcesModel.getActivity_type_id().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getActivity_type_id().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getActivity_type_id().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getActivity_type_id().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put("activity_type_id",ids);
            }
        }
        if (apiResourcesModel.getAd_type_id() != null &&
                apiResourcesModel.getAd_type_id().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getAd_type_id().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getAd_type_id().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getAd_type_id().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put("ad_type_id",ids);
            }
        }

        if (apiResourcesModel.getDistrict_id() != null &&
                apiResourcesModel.getDistrict_id().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getDistrict_id().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getDistrict_id().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getDistrict_id().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put("district_id",ids);
            }
        }
        if (apiResourcesModel.getIndoor() != null && apiResourcesModel.getIndoor().size() > 0) {
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
        if (apiResourcesModel.getCommunity_type_id() != null &&
                apiResourcesModel.getCommunity_type_id().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getCommunity_type_id().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getCommunity_type_id().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getCommunity_type_id().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put("community_type_id",ids);
            }
        }

        if (apiResourcesModel.getTrading_area_id() != null &&
                apiResourcesModel.getTrading_area_id().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getTrading_area_id().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getTrading_area_id().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getTrading_area_id().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put("trading_area_id",ids);
            }
        }

        if (apiResourcesModel.getLabel_id() != null &&
                apiResourcesModel.getLabel_id().size() > 0) {
            String ids = "";
            for (int i = 0; i < apiResourcesModel.getLabel_id().size(); i++) {
                if (ids.length() > 0) {
                    ids = ids + "," + String.valueOf(apiResourcesModel.getLabel_id().get(i));
                } else {
                    ids = String.valueOf(apiResourcesModel.getLabel_id().get(i));
                }
            }
            if (ids.length() > 0) {
                paramsMap.put("label_id",ids);
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

        if (apiResourcesModel.getLowPrice() != null) {
            if (apiResourcesModel.getLowPrice().length() > 0) {
                paramsMap.put("lowPrice", apiResourcesModel.getLowPrice());
            }
        }
        if (apiResourcesModel.getHighPrice() != null) {
            if (apiResourcesModel.getHighPrice().length() > 0) {
                paramsMap.put("highPrice", apiResourcesModel.getHighPrice());
            }
        }
        if (apiResourcesModel.getMinimum_peoples() != null) {
            if (apiResourcesModel.getMinimum_peoples().length() > 0) {
                paramsMap.put("minimum_peoples", apiResourcesModel.getMinimum_peoples());
            }
        }
        if (apiResourcesModel.getMaximum_peoples() != null) {
            if (apiResourcesModel.getMaximum_peoples().length() > 0) {
                paramsMap.put("maximum_peoples", apiResourcesModel.getMaximum_peoples());
            }
        }
        if (apiResourcesModel.getMinimum_build_year() != null) {
            if (apiResourcesModel.getMinimum_build_year().length() > 0) {
                paramsMap.put("minimum_build_year", apiResourcesModel.getMinimum_build_year());
            }
        }
        if (apiResourcesModel.getMaximum_build_year() != null) {
            if (apiResourcesModel.getMaximum_build_year().length() > 0) {
                paramsMap.put("maximum_build_year", apiResourcesModel.getMaximum_build_year());
            }
        }
        if (apiResourcesModel.getMinimum_households() != null) {
            if (apiResourcesModel.getMinimum_households().length() > 0) {
                paramsMap.put("minimum_households", apiResourcesModel.getMinimum_households());
            }
        }
        if (apiResourcesModel.getMaximum_households() != null) {
            if (apiResourcesModel.getMaximum_households().length() > 0) {
                paramsMap.put("maximum_households", apiResourcesModel.getMaximum_households());
            }
        }
        if (apiResourcesModel.getMinimum_property_costs() != null) {
            if (apiResourcesModel.getMinimum_property_costs().length() > 0) {
                paramsMap.put("minimum_property_costs", apiResourcesModel.getMinimum_property_costs());
            }
        }
        if (apiResourcesModel.getMaximum_property_costs() != null) {
            if (apiResourcesModel.getMaximum_property_costs().length() > 0) {
                paramsMap.put("maximum_property_costs", apiResourcesModel.getMaximum_property_costs());
            }
        }
        if (apiResourcesModel.getMinimum_house_price() != null) {
            if (apiResourcesModel.getMinimum_house_price().length() > 0) {
                paramsMap.put("minimum_house_price", apiResourcesModel.getMinimum_house_price());
            }
        }
        if (apiResourcesModel.getMaximum_house_price() != null) {
            if (apiResourcesModel.getMaximum_house_price().length() > 0) {
                paramsMap.put("maximum_house_price", apiResourcesModel.getMaximum_house_price());
            }
        }

        if (apiResourcesModel.getMinimum_area() != null) {
            if (apiResourcesModel.getMinimum_area().length() > 0) {
                paramsMap.put("minimum_area", apiResourcesModel.getMinimum_area());
            }
        }
        if (apiResourcesModel.getMaximum_area() != null) {
            if (apiResourcesModel.getMaximum_area().length() > 0) {
                paramsMap.put("maximum_area", apiResourcesModel.getMaximum_area());
            }
        }

        paramsMap.put("order", apiResourcesModel.getOrder());
        paramsMap.put("order_by", apiResourcesModel.getOrder_by());
        paramsMap.put("page", apiResourcesModel.getPage());
        if (apiResourcesModel.getPageSize() != null &&
                apiResourcesModel.getPageSize().length() > 0) {
            paramsMap.put("pageSize", apiResourcesModel.getPageSize());
        }
        if (apiResourcesModel.getIs_home_page() == 1) {
            paramsMap.put("is_home_page", String.valueOf(apiResourcesModel.getIs_home_page()));
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
        return paramsMap;
    }
    private void sendBrowseHistories() {
        //浏览记录
        if (LoginManager.isLogin()) {
            try {
                String parameter = "?"+ Request.urlEncode(getBrowseHistoriesUrl());
                LoginMvpModel.sendBrowseHistories("adv_list",parameter,apiResourcesModel.getCity_id());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
