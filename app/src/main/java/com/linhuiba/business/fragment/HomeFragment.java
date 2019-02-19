package com.linhuiba.business.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baselib.app.util.MessageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linhuiba.business.BuildConfig;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.AboutUsActivity;
import com.linhuiba.business.activity.ActivityCaseActivity;
import com.linhuiba.business.activity.ActivityCaseInfoActivity;
import com.linhuiba.business.activity.AdvertisingInfoActivity;
import com.linhuiba.business.activity.BaiduMapActivity;
import com.linhuiba.business.activity.CommunityInfoActivity;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.activity.MainTabActivity;
import com.linhuiba.business.activity.MyWalletActivity;
import com.linhuiba.business.activity.PublishReviewActivity;
import com.linhuiba.business.activity.SearchAdvListActivity;
import com.linhuiba.business.activity.SearchFieldAreaActivity;
import com.linhuiba.business.activity.SearchListActivity;
import com.linhuiba.business.activity.SelfSupportShopActivity;
import com.linhuiba.business.activity.searchcity.SearchCityActivity;
import com.linhuiba.business.adapter.FieldinfoAllResourceInfoViewPagerAdapter;
import com.linhuiba.business.adapter.GlideImageLoader;
import com.linhuiba.business.adapter.HomeDynamicCommunityAdapter;
import com.linhuiba.business.adapter.HomeFragmentCooperationCaseAdapter;
import com.linhuiba.business.adapter.HomeHotResAdapter;

import com.linhuiba.business.adapter.HomeModuleBarAdapter;
import com.linhuiba.business.basemvp.BaseMvpFragment;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.OnMultiClickListener;
import com.linhuiba.business.connector.PriceNumAnim;
import com.linhuiba.business.model.ArticleslistModel;
import com.linhuiba.business.model.HomeDynamicCommunityModel;
import com.linhuiba.business.model.HomeHoverModel;
import com.linhuiba.business.model.HomeMessageModel;
import com.linhuiba.business.model.SearchCityModel;
import com.linhuiba.business.model.SearchListAttributesModel;
import com.linhuiba.business.model.SearchSellResModel;
import com.linhuiba.business.mvpmodel.LoginMvpModel;
import com.linhuiba.business.mvppresenter.HomeMvpPresenter;
import com.linhuiba.business.mvppresenter.SearchResListMvpPresenter;
import com.linhuiba.business.mvppresenter.VersionMvpPresenter;
import com.linhuiba.business.mvpview.HomeMvpView;
import com.linhuiba.business.mvpview.SearchResListMvpView;
import com.linhuiba.business.mvpview.VersionMvpView;
import com.linhuiba.business.view.HomeCommunityItemLL;
import com.linhuiba.business.view.HorizontalListView;
import com.linhuiba.business.view.MySwipeRefreshLayout;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.model.HomeNoticesModel;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.business.model.Version;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.view.MyScrollView;
import com.linhuiba.linhuifield.fieldactivity.FieldAddFieldGuideActivity;
import com.linhuiba.linhuifield.fieldview.CustomDialog;
import com.linhuiba.linhuifield.sqlite.ConfigCitiesModel;
import com.linhuiba.linhuifield.sqlite.ConfigCityParameterModel;
import com.linhuiba.linhuifield.sqlite.ConfigSqlOperation;
import com.linhuiba.linhuifield.sqlite.ConfigurationsModel;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.linhuipublic.config.SupportPopupWindow;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.linhuiba.business.config.Config.BASE_API_URL_PHP;

/**
 * Created by Administrator on 2016/3/1.
 */
public class HomeFragment extends BaseMvpFragment implements MySwipeRefreshLayout.OnRefreshListener,
        MyScrollView.OnScrollListener, HomeMvpView, VersionMvpView, SearchResListMvpView {
    private View mMainContent;
    private SupportPopupWindow pw;
    @InjectView(R.id.edit_area)
    TextView medit_area;
    @InjectView(R.id.edit_search)
    TextView medit_search;
    @InjectView(R.id.hide_all_layout)
    LinearLayout mhide_all_layout;
    @InjectView(R.id.hide_layout)
    LinearLayout hide_layout;
    @InjectView(R.id.homefragment_swipe_refresh)
    MySwipeRefreshLayout HomeReviewListswipList;
    @InjectView(R.id.home_cooperation_case_horizontallistview)
    HorizontalListView mHomeCooperationCaseHLV;
    @InjectView(R.id.home_cooperation_case_typelayout)
    LinearLayout mHomeCooperationCaseLL;
    @InjectView(R.id.fragmenthome_scrollview)
    MyScrollView mScrollView;
    @InjectView(R.id.home_banner)
    Banner mHomeBanner;
    @InjectView(R.id.home_navbar_title_layout_tmp) LinearLayout mHomeNavbarTtileTmpLL;
    @InjectView(R.id.home_navbar_title_layout) LinearLayout mHomeNavbarTtileLL;
    @InjectView(R.id.home_navbar_title_coverage_ll)LinearLayout mHomeTitleCoverageLL;
    //2018/3/20 头条滚动
    @InjectView(R.id.fragment_flipper)
    ViewFlipper mFragmentFlipper;
    @InjectView(R.id.fragment_flipper_ll)
    LinearLayout mFragmentFlipperLL;
    @InjectView(R.id.home_service_provider_ll)
    LinearLayout mHomeServiceProviderLL;
    @InjectView(R.id.home_service_provider_horizontallistview)
    HorizontalListView mHomeServiceProviderLV;
    @InjectView(R.id.home_community_list_ll)
    LinearLayout mHomeCommunityListLL;
    @InjectView(R.id.home_search_ll)
    LinearLayout mHomeSearchLL;
    @InjectView(R.id.home_search_view)
    View mHomeSearchView;
    @InjectView(R.id.home_module_bar_rv)
    RecyclerView mHomeModuleBarRV;
    @InjectView(R.id.home_community_bar_rv)
    RecyclerView mHomeCommunityBarRV;
    @InjectView(R.id.home_linhui_data_rental_area_tv)
    TextView mHomeLinhuiDataRentalAreaTV;
    @InjectView(R.id.home_linhui_data_cover_city_tv)
    TextView mHomeLinhuiDataCoverCityTV;
    @InjectView(R.id.home_linhui_data_num_of_people_tv)
    TextView mHomeLinhuiDataNumOfPeopleTV;
    @InjectView(R.id.home_linhui_data_num_of_res_tv)
    TextView mHomeLinhuiDataNumOfResTV;
    @InjectView(R.id.home_linhui_data_num_of_res_unit_tv)
    TextView mHomeLinhuiDataNumOfResUnitTV;
    @InjectView(R.id.home_linhui_data_ll)
    LinearLayout mHomeLinhuiDataLL;
    @InjectView(R.id.home_status_bar_ll)
    LinearLayout mHomeDefaultStatusBarLL;
    @InjectView(R.id.home_suspend_imgv)
    ImageView mHomeSuspendImgv;
    @InjectView(R.id.home_suspend_imgv_temp)
    TextView mHomeSuspendImgvTemp;


    private ArrayList<String> mPicList = new ArrayList<String>();
    private HashMap<String, String> mBunnerListMap;
    public String mCityIdStr = "90";
    private ArrayList<HashMap<String, String>> mCityListMap;
    private HashMap<String, String> mProvinceMap;
    public LocationClient mLocationClient = null;
    private int mUpdateCityIdInt = 1;
    private ApiResourcesModel apiResourcesModel = new ApiResourcesModel();
    private HomeFragmentCooperationCaseAdapter mCooperationCaseAdapter;
    private DisplayMetrics mDisplayMetrics;
    public boolean mShowNotices;//是否显示通知
    private boolean mOnResume;
    private MyBroadcastReciver myBroadcastReciver;
    private ArrayList<ArticleslistModel> mCooperationCaseDataList = new ArrayList<>();
    private ArrayList<ArticleslistModel> mHeadlinesDataList = new ArrayList<>();
    private GlideImageLoader mGlideImageLoader;
    private boolean isForcedUpdating;
    private CustomDialog mCustomDialog;
    private HomeMvpPresenter mHomeMvpPresenter;
    private List<SearchCityModel> mSearchCityModelList = new ArrayList<>();
    private static final int SEARCH_CITY_RESULT_INT = 1;
    private static final int CAMER_REQUEST_INT = 10;
    private ArrayList<ResourceSearchItemModel> mServiceProviderDataList = new ArrayList<>();
    private HomeHotResAdapter mHomeServiceProviderAdapter;
    public boolean isMobileClick;//item点击和item中的mobile点击冲突
    private VersionMvpPresenter mVersionPresenter;
    private CustomDialog mMessageDialog;
    private ArrayList<ConfigCitiesModel> ConfigCitiesModels;//所有城市
    private ArrayList<HomeDynamicCommunityModel> mDynamicCommunity = new ArrayList<>();
    private HashMap<Integer,LinearLayout[]> mDynamicCommunityNoLLMap = new HashMap<>();
    private HashMap<Integer,TextView[]> mDynamicCommunityNoTVMap = new HashMap<>();
    private HashMap<Integer,List<ResourceSearchItemModel>[]> mDynamicCommunityListMap = new HashMap<>();
    private HashMap<Integer,HomeDynamicCommunityAdapter[]> mDynamicCommunityAdapterMap = new HashMap<>();
    private HashMap<Integer,RecyclerView[]> mDynamicCommunityRVMap = new HashMap<>();
    private ArrayList<HomeCommunityItemLL> mHomeCommunityItemLLS = new ArrayList<>();
    private HashMap<Integer,Integer> mDynamicCommunityCheckMap = new HashMap<>();
    private HashMap<Integer,String[]> mDynamicCommunityTlStrMap = new HashMap<>();
    private SearchResListMvpPresenter mSearchResListMvpPresenter;
    private int mDynamicCommunityViewPosition;
    private int mDynamicCommunityItemPageSelectedPosition;
    private ArrayList<HomeDynamicCommunityModel> mCategoriesBarList;
    private Boolean isSynchronization = true;//动态场地列表初始化标志
    private int mSynchronizationSize = 0;//动态场地列表初始化标志 动态场地列表模块的数组个数
    private MainTabActivity mMainTabActivity;
    private int mNavbarTtileHeight = 65;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mMainContent == null) {
            mMainContent = inflater.inflate(R.layout.fragment_home, container , false);
            ButterKnife.inject(this, mMainContent);
            AndPermission.with(HomeFragment.this)
                    .requestCode(Constants.PermissionRequestCode)
                    .permission(Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CAMERA)
                    .callback(listener)
                    .start();
            //配置信息获取延迟后 更新界面的通知
            registReceiver();
            initView();
        }

        return mMainContent;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mMainTabActivity == null) {
            mMainTabActivity = (MainTabActivity) HomeFragment.this.getActivity();
            if (mMainTabActivity.mNotchHeight > 0) {
                //2018/12/5 适配刘海屏
                mNavbarTtileHeight = 40 + com.linhuiba.linhuifield.connector.Constants.Px2Dp(HomeFragment.this.getContext(),
                        mMainTabActivity.mNotchHeight);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),mNavbarTtileHeight));
                mHomeNavbarTtileTmpLL.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),mNavbarTtileHeight + 5));
                mHomeNavbarTtileLL.setLayoutParams(layoutParams1);
                mHomeTitleCoverageLL.setPadding(0, mMainTabActivity.mNotchHeight,0,0);
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        mMainTabActivity.mNotchHeight);
                mHomeDefaultStatusBarLL.setLayoutParams(layoutParams2);
            }
        }
        if (HomeFragment.this.getActivity() instanceof MainTabActivity) {
            mMainTabActivity.mSearchStatusBarLL.setVisibility(View.GONE);
        }
        MobclickAgent.onPageStart(getResources().getString(R.string.home_fragment_name_str));
        mUpdateCityIdInt = 1;
        if (LoginManager.getInstance().getNoticesshow() == 1 && LoginManager.getAccessToken() != null &&
                LoginManager.getAccessToken().length() > 0 && mOnResume == true && mShowNotices == false) {
            mShowNotices = true;
            LoginManager.getInstance().setNoticesshow(0);
        }
        if (mOnResume == true && mShowNotices == true && LoginManager.getAccessToken() != null &&
                LoginManager.getAccessToken().length() > 0) {
            mHomeMvpPresenter.getNotices();
        }
        if (!isForcedUpdating && mCustomDialog != null &&
                mCustomDialog.isShowing()) {
            mCustomDialog.dismiss();
        }
        if (mHomeServiceProviderAdapter != null) {
            mHomeServiceProviderAdapter.notifyDataSetChanged();
        }
        isMobileClick = false;
        //浏览记录
        if (LoginManager.isLogin() && mMainTabActivity.isClickTab) {
            mMainTabActivity.isClickTab = false;
            LoginMvpModel.sendBrowseHistories("home_page",null,mCityIdStr);
        }
        // FIXME: 2019/1/16 切换环境后刷新
        if (mMainTabActivity.isHomeRefresh) {
            mMainTabActivity.isHomeRefresh = false;
            initData();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        mHomeBanner.startAutoPlay();
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResources().getString(R.string.home_fragment_name_str));
        mUpdateCityIdInt = 0;
    }
    @Override
    public void onStop() {
        super.onStop();
        mUpdateCityIdInt = 0;
        mHomeBanner.stopAutoPlay();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myBroadcastReciver != null) {
            HomeFragment.this.getContext().unregisterReceiver(myBroadcastReciver);
            myBroadcastReciver = null;
        }
        if (mHomeMvpPresenter != null) {//友盟错误日志
            mHomeMvpPresenter.detachView();
        }
        if (mVersionPresenter != null) {
            mVersionPresenter.detachView();
        }
        if (mSearchResListMvpPresenter != null) {
            mSearchResListMvpPresenter.detachView();
        }
    }
    @Override
    public void onRefresh() {
        initData();
    }
    private void addCmmunityView() {
        mDynamicCommunityRVMap.clear();
        mDynamicCommunityNoLLMap.clear();
        mDynamicCommunityNoTVMap.clear();
        mDynamicCommunityAdapterMap.clear();
        mDynamicCommunityListMap.clear();
        mHomeCommunityItemLLS.clear();
        mDynamicCommunityTlStrMap.clear();
        mDynamicCommunityCheckMap.clear();
        mHomeCommunityListLL.removeAllViews();
        mHomeCommunityListLL.setVisibility(View.VISIBLE);
        for (int i = 0; i < mDynamicCommunity.size(); i++) {
            HomeCommunityItemLL homeCommunityItemLL = new HomeCommunityItemLL(HomeFragment.this.getContext());
            homeCommunityItemLL.mTextView.setText(mDynamicCommunity.get(i).getName());
            LayoutInflater inflater = HomeFragment.this.getActivity().getLayoutInflater();
            if (mDynamicCommunity.get(i).getData() != null &&
                    mDynamicCommunity.get(i).getData().size() > 0) {
                String mTabTextViewList[];
                ArrayList<View> mListViews = new ArrayList<View>();
                mTabTextViewList = new String[mDynamicCommunity.get(i).getData().size()];
                RecyclerView[] mRecyclerViews = new RecyclerView[mDynamicCommunity.get(i).getData().size()];
                LinearLayout[] linearLayouts = new LinearLayout[mDynamicCommunity.get(i).getData().size()];
                TextView[] textViews = new TextView[mDynamicCommunity.get(i).getData().size()];
                HomeDynamicCommunityAdapter[] homeDynamicCommunityAdapters = new HomeDynamicCommunityAdapter[mDynamicCommunity.get(i).getData().size()];
                List<ResourceSearchItemModel>[] mLists = new ArrayList[mDynamicCommunity.get(i).getData().size()] ;
                for (int n = 0; n < mDynamicCommunity.get(i).getData().size(); n++) {
                    mListViews.add(inflater.inflate(R.layout.module_home_community_list_recycle, null));
                    mTabTextViewList[n] = mDynamicCommunity.get(i).getData().get(n).getDisplay_name();
                    mRecyclerViews[n] = (RecyclerView) mListViews.get(n).findViewById(R.id.home_community_recycler_view);
                    linearLayouts[n] = (LinearLayout) mListViews.get(n).findViewById(R.id.home_community_no_data_ll);
                    textViews[n] = (TextView) mListViews.get(n).findViewById(R.id.home_community_no_data_tv);
                    mLists[n] = new ArrayList<>();
                    homeDynamicCommunityAdapters[n] = null;
                }
                mDynamicCommunityRVMap.put(i,mRecyclerViews);
                mDynamicCommunityNoLLMap.put(i,linearLayouts);
                mDynamicCommunityNoTVMap.put(i,textViews);
                mDynamicCommunityAdapterMap.put(i,homeDynamicCommunityAdapters);
                mDynamicCommunityListMap.put(i,mLists);
                mDynamicCommunityTlStrMap.put(i,mTabTextViewList);
                int width = mDisplayMetrics.widthPixels;
                int height = ((width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 262 / 338 +
                        com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),62)) * 2;
                LinearLayout.LayoutParams paramgroups = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        height);
                homeCommunityItemLL.mInvoiceTitleVP.setLayoutParams(paramgroups);
                homeCommunityItemLL.mInvoiceTitleVP.setAdapter(new FieldinfoAllResourceInfoViewPagerAdapter(mListViews));
                homeCommunityItemLL.mInvoiceTitleVP.setOnPageChangeListener(new PagerChangeListener(i));
                homeCommunityItemLL.mInvoiceTitleTL.setupWithViewPager(homeCommunityItemLL.mInvoiceTitleVP);
                for (int j = 0; j < homeCommunityItemLL.mInvoiceTitleTL.getTabCount(); j++) {
                    homeCommunityItemLL.mInvoiceTitleTL.getTabAt(j).setText(mTabTextViewList[j]);
                }
                homeCommunityItemLL.mShowAllTextView.setText(getResources().getString(R.string.home_look_more_list_tv_str) +
                        mTabTextViewList[0] +
                        getResources().getString(R.string.leftimg_text));
                mDynamicCommunityNoTVMap.get(i)[0].setText(getResources().getString(R.string.module_home_no_community_data_msg) +
                        mTabTextViewList[0] + getResources().getString(R.string.module_home_no_community_data_other_msg));
                mHomeCommunityListLL.addView(homeCommunityItemLL);
                mHomeCommunityItemLLS.add(homeCommunityItemLL);
                mDynamicCommunityCheckMap.put(i,0);
            }
        }
        ApiResourcesModel apiResourcesModel = new ApiResourcesModel();
        apiResourcesModel.setOrder_by("weight_score");
        apiResourcesModel.setOrder("desc");
        ArrayList<Integer> city_ids = new ArrayList<>();
        city_ids.add(Integer.parseInt(mCityIdStr));
        apiResourcesModel.setCity_ids(city_ids);
        apiResourcesModel.setPage("1");
        apiResourcesModel.setPage_size(4);
        apiResourcesModel.setDynamic_name(mDynamicCommunity.get(0).
                getData().get(0).getName());
        ArrayList<Integer> dynamic_id = new ArrayList<>();
        dynamic_id.add(mDynamicCommunity.get(0).
                getData().get(0).getId());
        apiResourcesModel.setDynamic_id(dynamic_id);
        mSearchResListMvpPresenter.getCommunityList(apiResourcesModel);
        for (int l = 0; l < mHomeCommunityItemLLS.size(); l++) {
            final int finalL = l;
            mHomeCommunityItemLLS.get(l).mShowAllTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApiResourcesModel apiResourcesModel = new ApiResourcesModel();
                    apiResourcesModel.setOrder_by("weight_score");
                    apiResourcesModel.setOrder("desc");
                    ArrayList<Integer> city_ids = new ArrayList<>();
                    city_ids.add(Integer.parseInt(mCityIdStr));
                    apiResourcesModel.setCity_ids(city_ids);
                    apiResourcesModel.setPage("1");
                    apiResourcesModel.setPage_size(10);
                    if (mDynamicCommunity.get(finalL).
                            getData().get(mDynamicCommunityCheckMap.get(finalL)).getName().equals("community_type_ids")) {
                        ArrayList<Integer> dynamic_id = new ArrayList<>();
                        dynamic_id.add(mDynamicCommunity.get(finalL).
                                getData().get(mDynamicCommunityCheckMap.get(finalL)).getId());
                        apiResourcesModel.setCommunity_type_ids(dynamic_id);
                    } else if (mDynamicCommunity.get(finalL).
                            getData().get(mDynamicCommunityCheckMap.get(finalL)).getName().equals("developer_ids")) {
                        ArrayList<Integer> dynamic_id = new ArrayList<>();
                        dynamic_id.add(mDynamicCommunity.get(finalL).
                                getData().get(mDynamicCommunityCheckMap.get(finalL)).getId());
                        apiResourcesModel.setDeveloper_ids(dynamic_id);
                    } else if (mDynamicCommunity.get(finalL).
                            getData().get(mDynamicCommunityCheckMap.get(finalL)).getName().equals("label_ids")) {
                        ArrayList<Integer> dynamic_id = new ArrayList<>();
                        dynamic_id.add(mDynamicCommunity.get(finalL).
                                getData().get(mDynamicCommunityCheckMap.get(finalL)).getId());
                        apiResourcesModel.setLabel_ids(dynamic_id);
                    }
                    Intent searchListIntent = new Intent(HomeFragment.this.getActivity(),SearchListActivity.class);
                    searchListIntent.putExtra("is_home_page",4);
                    searchListIntent.putExtra("ApiResourcesModel",(Serializable) apiResourcesModel);
                    startActivity(searchListIntent);
                }
            });
        }
    }
    private void initView() {
        mHomeMvpPresenter = new HomeMvpPresenter();
        mHomeMvpPresenter.attachView(this);
        mVersionPresenter = new VersionMvpPresenter();
        mVersionPresenter.attachView(this);
        mSearchResListMvpPresenter = new SearchResListMvpPresenter();
        mSearchResListMvpPresenter.attachView(this);
        //2018/12/5 适配刘海屏
        if (mMainTabActivity != null && mMainTabActivity.mNotchHeight > 0) {
            mNavbarTtileHeight = 40 + com.linhuiba.linhuifield.connector.Constants.Px2Dp(HomeFragment.this.getContext(),
                    mMainTabActivity.mNotchHeight);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),mNavbarTtileHeight));
            mHomeNavbarTtileTmpLL.setLayoutParams(layoutParams);
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),mNavbarTtileHeight + 5));
            mHomeNavbarTtileLL.setLayoutParams(layoutParams1);
            mHomeTitleCoverageLL.setPadding(0, mMainTabActivity.mNotchHeight,0,0);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    mMainTabActivity.mNotchHeight);
            mHomeDefaultStatusBarLL.setLayoutParams(layoutParams2);
        }
//        mNavbarTtileHeight = 80;
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),mNavbarTtileHeight));
//        mHomeNavbarTtileTmpLL.setLayoutParams(layoutParams);
//        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),mNavbarTtileHeight));
//        mHomeNavbarTtileLL.setLayoutParams(layoutParams1);
//        mHomeTitleCoverageLL.setPadding(0, com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),40),0,0);


        mDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mScrollView.setOnScrollListener(this);
        mMainContent.findViewById(R.id.fragment_home_all_layout).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScroll(mScrollView.getScrollY());
                System.out.println(mScrollView.getScrollY());
            }
        });
        mScrollView.setOnScrollSlideLintener(new MyScrollView.OnScrollSlideLintener() {
            @Override
            public void onScrollSlide(boolean isSlide) {
                if (isSlide) {//开始滑动
                    mHomeSuspendImgvTemp.setVisibility(View.GONE);

                } else {//结束滑动
                    mHomeSuspendImgvTemp.setVisibility(View.VISIBLE);
                }
            }
        });
        mGlideImageLoader = new GlideImageLoader(HomeFragment.this.getActivity(),mDisplayMetrics.widthPixels,mDisplayMetrics.widthPixels / 2);
        HomeReviewListswipList.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        HomeReviewListswipList.setOnRefreshListener(this);
        LoginManager.getInstance().setRegisterCurrentCityid(Constants.getDefault_cityid(HomeFragment.this.getContext()));//默认定位到杭州
        if (LoginManager.getInstance().getHome_show_guide() == 0) {
            Intent addfield = new Intent(HomeFragment.this.getActivity(), FieldAddFieldGuideActivity.class);
            addfield.putExtra("show_type",3);
            startActivity(addfield);
        }
        //显示的默认城市（根据定位）
//        getCurrentCity();
        //配置文件中的城市list
        getCityList();
        //模块功能导航
        addModuleBarView();
        //界面的数据请求 包括刷新和点击换城市后的重新请求更新界面
        initData();
        //版本更新
        checkUpdate();
        //通知接口
        mHomeMvpPresenter.getNotices();
        //推送消息打开的app 跳转到消息界面 还包含一个闪屏的
        //2017/10/14 推送跳转app界面
        if (LoginManager.getInstance().getUMmsg_start_app() != null && LoginManager.getInstance().getUMmsg_start_app().length() > 0) {
            String data = LoginManager.getInstance().getUMmsg_start_app();
            Constants.pushUrlJumpActivity(data,HomeFragment.this.getContext(),false);
            LoginManager.getInstance().setUMmsg_start_app("");
        } else {
            //上一次退出的是物业功能就直接跳到物业模块
            if (LoginManager.isLogin() && (LoginManager.isRight_to_publish() || LoginManager.isIs_supplier()) &&
                    (LoginManager.getInstance().getFieldexit() == -1 || LoginManager.getInstance().getFieldexit() == 1)) {
                Intent fieldorderlist = new Intent(HomeFragment.this.getActivity(), com.linhuiba.linhuifield.fieldactivity.FieldMainTabActivity.class);
                fieldorderlist.putExtra("new_tmpintent", "fieldlist");
                startActivity(fieldorderlist);
                LoginManager.getInstance().setFieldexit(1);
            }
        }
        mHomeCooperationCaseHLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent caseIntent = new Intent(HomeFragment.this.getActivity(), ActivityCaseInfoActivity.class);
                caseIntent.putExtra("id", mCooperationCaseDataList.get(position).getId());
                startActivity(caseIntent);
                /* 经典案例加载web
                if (((mCooperationCaseDataList.get(position).getOrigin() != null &&
                        mCooperationCaseDataList.get(position).getOrigin().length() > 0) ||
                        (mCooperationCaseDataList.get(position).getContent() != null &&
                                mCooperationCaseDataList.get(position).getContent().length() > 0))) {
                    Intent integer = new Intent(HomeFragment.this.getActivity(), AboutUsActivity.class);
                    integer.putExtra("type", Config.HOME_NEW_SIGN_INT);
                    if (mCooperationCaseDataList.get(position).getOrigin() != null && mCooperationCaseDataList.get(position).getOrigin().length() > 0) {
                        integer.putExtra("web_url", mCooperationCaseDataList.get(position).getOrigin());
                    } else if (mCooperationCaseDataList.get(position).getContent() != null && mCooperationCaseDataList.get(position).getContent().length() > 0) {
                        integer.putExtra("web_html_content", mCooperationCaseDataList.get(position).getContent());
                    }
                    integer.putExtra("id", mCooperationCaseDataList.get(position).getId());
                    integer.putExtra("title", getResources().getString(R.string.home_case_title_text));
                    startActivity(integer);
                }
                **/
            }
        });
        if (!com.linhuiba.linhuifield.connector.Constants.isNotificationsEnabled(HomeFragment.this.getContext())) {
            new AlertDialog.Builder(HomeFragment.this.getActivity())
                    .setTitle(getResources().getString(R.string.module_home_push_notification_title))
                    .setMessage(getResources().getString(R.string.module_home_push_notification_description))
                    .setCancelable(false)
                    .setNegativeButton(getResources().getString(R.string.module_home_another_time), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.module_home_notification_open), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            Intent localIntent = new Intent();
                            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (Build.VERSION.SDK_INT >= 9) {
                                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                localIntent.setData(Uri.fromParts("package", HomeFragment.this.getActivity().getPackageName(), null));
                            } else if (Build.VERSION.SDK_INT <= 8) {
                                localIntent.setAction(Intent.ACTION_VIEW);
                                localIntent.setClassName("com.android.settings",
                                        "com.android.settings.InstalledAppDetails");
                                localIntent.putExtra("com.android.settings.ApplicationPkgName",
                                        HomeFragment.this.getActivity().getPackageName());
                            }
                            startActivity(localIntent);

                        }
                    }).show();
        }
    }
    //轮播图显示
    private void showBanner() {
        int width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        int height = width / 2;
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mHomeBanner.getLayoutParams();
        //设置图片显示高度
        linearParams.height = height;
        linearParams.width = width;
        mHomeBanner.setLayoutParams(linearParams);
        //设置图片加载器
        mHomeBanner.setImageLoader(mGlideImageLoader);
        //设置图片集合
        mHomeBanner.setImages(mPicList);
        //设置banner动画效果
        mHomeBanner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        mHomeBanner.isAutoPlay(true);
        //设置轮播时间
        mHomeBanner.setDelayTime(5000);
        //设置指示器位置（当banner模式中有指示器时）
        mHomeBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mHomeBanner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                String link = URLDecoder.decode(mBunnerListMap.get(mPicList.get(position - 1)));
//                String link = "https://www.linhuiba.com/fields/index?city_id=77&label_id=428";
                Constants.pushUrlJumpActivity(link,HomeFragment.this.getContext(),false);
            }
        });
        mHomeBanner.start();
    }
    //获取轮播图的接口
    private void getBannerUrl() {
        mHomeMvpPresenter.getBanners(mCityIdStr);
    }
    @OnClick({
            R.id.edit_area,
            R.id.edit_search,
            R.id.home_service_provider_more_ll,
            R.id.home_case_more_ll,
            R.id.home_service_provider_settled_tv
    })
    public void HomeOnclick(View view) {
        switch (view.getId()) {
            case R.id.edit_area:
                //2018/4/9 城市选择新功能
                Intent searchCityIntent = new Intent(HomeFragment.this.getActivity(), SearchCityActivity.class);
                searchCityIntent.putExtra("searchcity_list",(Serializable) mSearchCityModelList);
                searchCityIntent.putExtra("name",medit_area.getText().toString().trim());
                searchCityIntent.putExtra("id",mCityIdStr);
                startActivityForResult(searchCityIntent,SEARCH_CITY_RESULT_INT);
                break;
            case R.id.edit_search:
                Intent searchatea_seraintent = new Intent(HomeFragment.this.getActivity(),SearchFieldAreaActivity.class);
                searchatea_seraintent.putExtra("search",2);
                searchatea_seraintent.putExtra("cityname_code",mCityIdStr);
                startActivityForResult(searchatea_seraintent,2);
                break;
            case R.id.home_service_provider_more_ll:
                Intent resources_web = new Intent(HomeFragment.this.getActivity(), AboutUsActivity.class);
                resources_web.putExtra("type", Config.FACILITATOR_INT);
                resources_web.putExtra("city_id", mCityIdStr);
                startActivity(resources_web);
                break;
            case R.id.home_case_more_ll:
                Intent caseIntent = new Intent(HomeFragment.this.getActivity(),ActivityCaseActivity.class);
                startActivity(caseIntent);
                break;
            case R.id.home_service_provider_settled_tv:
                Intent commonIntent = new Intent(HomeFragment.this.getActivity(), AboutUsActivity.class);
                commonIntent.putExtra("type", com.linhuiba.business.config.Config.COMMON_WEB_INT);
                commonIntent.putExtra("web_url",Config.SERVICE_PROVIDER_SETTLED_URL);
                startActivity(commonIntent);
                break;
            default:
                break;
        }
    }
    //获取所有城市列表
    private void getCityList() {
        List<ConfigCityParameterModel> configCityParameterModels = ConfigSqlOperation.selectSQL(6,0,HomeFragment.this.getContext());
        if (configCityParameterModels != null && configCityParameterModels.size() > 0) {
            mCityListMap= new ArrayList<>();
            mProvinceMap = new HashMap<>();
            if (mSearchCityModelList != null) {
                mSearchCityModelList.clear();
            }
            boolean isDefaultCity = false;
            if (LoginManager.getInstance().getCurrentCitycode().length() == 0 || LoginManager.getInstance().getCurrentCity().length() == 0) {
                isDefaultCity = true;
            } else {
                mCityIdStr = LoginManager.getInstance().getCurrentCitycode();
                medit_area.setText(LoginManager.getInstance().getCurrentCity());
            }
            for (int i = 0; i < configCityParameterModels.size(); i++) {
                String key = configCityParameterModels.get(i).getCity();
                String value = String.valueOf(configCityParameterModels.get(i).getCity_id());
                String default_city = String.valueOf(configCityParameterModels.get(i).getDefault_city());
                if (isDefaultCity) {
                    if (default_city.equals("1")) {
                        mCityIdStr = value;
                        medit_area.setText(key);
                    }
                }
                HashMap<String,String> map = new HashMap<>();
                map.put("id",value);
                map.put("name",key);
                mCityListMap.add(map);
                SearchCityModel searchCityModel = new SearchCityModel();
                searchCityModel.setId(value);
                searchCityModel.setName(key);
                mSearchCityModelList.add(searchCityModel);
                mProvinceMap.put(key,configCityParameterModels.get(i).getProvince());
            }
        } else {
            List<ConfigCitiesModel> configCitiesModels = ConfigurationsModel.getInstance().getCitylist();
            if (configCitiesModels != null && configCitiesModels.size() > 0) {
                mCityListMap= new ArrayList<>();
                mProvinceMap = new HashMap<>();
                if (mSearchCityModelList != null) {
                    mSearchCityModelList.clear();
                }
                boolean isDefaultCity = false;
                if (LoginManager.getInstance().getCurrentCitycode().length() == 0 || LoginManager.getInstance().getCurrentCity().length() == 0) {
                    isDefaultCity = true;
                } else {
                    mCityIdStr = LoginManager.getInstance().getCurrentCitycode();
                    medit_area.setText(LoginManager.getInstance().getCurrentCity());
                }
                for (int i = 0; i < configCitiesModels.size(); i++) {
                    String key = configCitiesModels.get(i).getCity();
                    String value = String.valueOf(configCitiesModels.get(i).getCity_id());
                    String default_city = String.valueOf(configCitiesModels.get(i).getDefault_city());
                    if (isDefaultCity) {
                        if (default_city.equals("1")) {
                            mCityIdStr = value;
                            medit_area.setText(key);
                        }
                    }
                    HashMap<String,String> map = new HashMap<>();
                    map.put("id",value);
                    map.put("name",key);
                    mCityListMap.add(map);
                    SearchCityModel searchCityModel = new SearchCityModel();
                    searchCityModel.setId(value);
                    searchCityModel.setName(key);
                    mSearchCityModelList.add(searchCityModel);
                    mProvinceMap.put(key,configCitiesModels.get(i).getProvince());
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case SEARCH_CITY_RESULT_INT:
                if (data.getExtras() != null &&
                        data.getExtras().get("id") != null &&
                        data.getExtras().get("name") != null) {
                    medit_area.setText(data.getExtras().get("name").toString());
                    mCityIdStr = data.getExtras().get("id").toString();
                    medit_search.setText("");
                    showProgressDialog();
                    initData();
                }
                break;
            case 2:
                String backstringmore = data.getStringExtra("back");
                Intent SearchFieldlistIntent = new Intent(getActivity(), SearchListActivity.class);
                SearchFieldlistIntent.putExtra("cityname", medit_area.getText().toString());
                SearchFieldlistIntent.putExtra("cityname_code", mCityIdStr);
                SearchFieldlistIntent.putExtra("keywords", backstringmore);
                startActivity(SearchFieldlistIntent);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //界面数据的各种接口
    private void initData() {
        getBannerUrl();
        mHomeCooperationCaseLL.setVisibility(View.GONE);
        mFragmentFlipperLL.setVisibility(View.GONE);
        mHomeServiceProviderLL.setVisibility(View.GONE);
        mHomeLinhuiDataLL.setVisibility(View.GONE);
        mHomeSuspendImgv.setVisibility(View.GONE);
        LoginManager.getInstance().setTrackcityid(mCityIdStr);
        LoginManager.getInstance().setTrackCityName(medit_area.getText().toString().trim());
        if (mProvinceMap != null && mProvinceMap.get(medit_area.getText().toString().trim()) != null) {
            LoginManager.getInstance().setTrackProvinceName(mProvinceMap.get(medit_area.getText().toString().trim()));
        }
        if (mCooperationCaseDataList != null) {
            mCooperationCaseDataList.clear();
        }
        if (mHeadlinesDataList != null) {
            mHeadlinesDataList.clear();
        }
        if (mServiceProviderDataList != null) {
            mServiceProviderDataList.clear();
        }
        //导航栏配置
        mHomeMvpPresenter.getCategoriesBarList();
        //头条
        mHomeMvpPresenter.getCaseList("17",null);
        //动态场地列表
        isSynchronization = true;
        mSynchronizationSize = 0;
        mHomeCommunityListLL.setVisibility(View.GONE);
        mHomeMvpPresenter.getNavigationBar(2);

        //合作案例
        mHomeMvpPresenter.getHomeCaseList(1,null,null,null,null,null,null,1,0);
        //服务商列表
        mHomeMvpPresenter.getServiceProviderList("6","1",mCityIdStr);
        //邻汇数据
        mHomeMvpPresenter.getLinhuiData();
        //运营消息
        mHomeMvpPresenter.getmessage();
        //悬浮框
        mHomeMvpPresenter.getAppHoverWindow();
    }
    private void addModuleBarView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeFragment.this.getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHomeModuleBarRV.setLayoutManager(linearLayoutManager);
        List<HomeDynamicCommunityModel> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HomeDynamicCommunityModel resourceSearchItemModel = new HomeDynamicCommunityModel();
            resourceSearchItemModel.setId(i);
            if (i == 0) {
                resourceSearchItemModel.setName(getResources().getString(R.string.myselfinfo_add_demand_title_str));
                resourceSearchItemModel.setDrawable(R.drawable.ic_demand_threesieone);
            } else if (i == 1) {
                resourceSearchItemModel.setName(getResources().getString(R.string.module_home_show_map));
                resourceSearchItemModel.setDrawable(R.drawable.ic_map_one_normal_three);
            } else if (i == 2) {
                resourceSearchItemModel.setName(getResources().getString(R.string.selfsupportshop_title_other_str));
                resourceSearchItemModel.setDrawable(R.drawable.ic_dangtianding_three_two);
            } else if (i == 3) {
                resourceSearchItemModel.setName(getResources().getString(R.string.module_home_show_adv_list));
                resourceSearchItemModel.setDrawable(R.drawable.ic_ad_three_six_one);
            } else if (i == 4) {
                resourceSearchItemModel.setName(getResources().getString(R.string.calendar_activity_txt));
                resourceSearchItemModel.setDrawable(R.drawable.ic_activity_three_six_one);
            }
            data.add(resourceSearchItemModel);
        }
        HomeModuleBarAdapter homeModuleBarAdapter = new HomeModuleBarAdapter(
                R.layout.module_recycle_item_home_dynamic_bar, data,
                HomeFragment.this.getContext(),HomeFragment.this,2);
        mHomeModuleBarRV.setAdapter(homeModuleBarAdapter);
        homeModuleBarAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    Intent AddDemand = new Intent(HomeFragment.this.getActivity(),AboutUsActivity.class);
                    AddDemand.putExtra("type", Config.ADD_DEMAND_WEB_INT);
                    startActivity(AddDemand);
                } else if (position == 1) {
                    Intent baiduintent = new Intent(HomeFragment.this.getActivity(), BaiduMapActivity.class);
                    baiduintent.putExtra("citycode",mCityIdStr);
                    baiduintent.putExtra("goodtype",1);
                    startActivity(baiduintent);
                    MobclickAgent.onEvent(HomeFragment.this.getContext(),com.linhuiba.linhuifield.config.Config.UM_HOME_MAP_CLICK);
                } else if (position == 2) {
                    Intent maintabintent = new Intent(HomeFragment.this.getActivity(), SelfSupportShopActivity.class);
                    startActivity(maintabintent);
                    MobclickAgent.onEvent(HomeFragment.this.getContext(), com.linhuiba.linhuifield.config.Config.UM_HOME_SELF_SUPPORT_SHOP_CLICK);
                } else if (position == 3) {
                    Intent AdvertisinglistIntent = new Intent(getActivity(), SearchAdvListActivity.class);
                    AdvertisinglistIntent.putExtra("cityname",LoginManager.getInstance().getTrackCityName());
                    AdvertisinglistIntent.putExtra("cityname_code",LoginManager.getInstance().getTrackcityid());
                    AdvertisinglistIntent.putExtra("good_type",2);
                    startActivity(AdvertisinglistIntent);
                } else if (position == 4) {
                    Intent maintabintent = new Intent(HomeFragment.this.getActivity(), SelfSupportShopActivity.class);
                    maintabintent.putExtra("res_type_id",3);
                    startActivity(maintabintent);
                }
            }
        });
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
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
    public void onScroll(int scrollY) {
        int mBuyLayout2ParentTop = Math.max(scrollY, mHomeNavbarTtileLL.getTop());
        mHomeNavbarTtileTmpLL.layout(0, mBuyLayout2ParentTop, mHomeNavbarTtileTmpLL.getWidth(), mBuyLayout2ParentTop + mHomeNavbarTtileTmpLL.getHeight());
        if(scrollY < 0) {
            mHomeNavbarTtileTmpLL.getBackground().mutate().setAlpha(0);
            mHomeTitleCoverageLL.setBackgroundDrawable(getResources().getDrawable(R.drawable.fragment_home_navbar_bg));
            mHomeDefaultStatusBarLL.setVisibility(View.GONE);
        } else {
            if (scrollY < mDisplayMetrics.widthPixels / 2 -
                    com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),mNavbarTtileHeight)) {
                int progress = (int) (new Float(scrollY) / new Float(mDisplayMetrics.widthPixels / 2 -
                        com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),mNavbarTtileHeight)) * 200);
                mHomeNavbarTtileTmpLL.getBackground().mutate().setAlpha(progress);
                mHomeTitleCoverageLL.setBackgroundDrawable(getResources().getDrawable(R.drawable.fragment_home_navbar_bg));
                mHomeSearchLL.getBackground().mutate().setAlpha(255);
                mHomeSearchView.setVisibility(View.GONE);
                mHomeDefaultStatusBarLL.setVisibility(View.GONE);
            } else {
                mHomeNavbarTtileTmpLL.getBackground().mutate().setAlpha(255);
                int progress = (int) (new Float(scrollY) / new Float(mDisplayMetrics.widthPixels / 2 -
                        com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),mNavbarTtileHeight)) * 200);
                mHomeSearchLL.getBackground().mutate().setAlpha(progress);
                mHomeTitleCoverageLL.setBackgroundColor(getResources().getColor(R.color.color_null));
                mHomeSearchLL.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_fieldinfo_show_all_resource_info_bg));
                mHomeSearchView.setVisibility(View.VISIBLE);
                mHomeDefaultStatusBarLL.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onSearchResListSuccess(ArrayList<ResourceSearchItemModel> list, Response response) {
        if (list != null) {
            if (list.size() == 1 || list.size() == 3) {
                ResourceSearchItemModel resourceSearchItemModel = new ResourceSearchItemModel();
                list.add(resourceSearchItemModel);
            }
            if (isSynchronization && mSynchronizationSize < mDynamicCommunity.size()) {
                mDynamicCommunityListMap.get(mSynchronizationSize)
                        [0] = list;
                mDynamicCommunityAdapterMap.get(mSynchronizationSize)
                        [0] = new HomeDynamicCommunityAdapter(R.layout.fragment_homeactivity_item,
                        mDynamicCommunityListMap.get(mSynchronizationSize)
                                [0],HomeFragment.this.getContext(),
                        HomeFragment.this);
                GridLayoutManager linearLayoutManager = new GridLayoutManager(HomeFragment.this.getActivity(),2);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mDynamicCommunityRVMap.get(mSynchronizationSize)
                        [0].setLayoutManager(linearLayoutManager);
                mDynamicCommunityRVMap.get(mSynchronizationSize)
                        [0].setAdapter( mDynamicCommunityAdapterMap.get(mSynchronizationSize)
                        [0]);
                mDynamicCommunityRVMap.get(mSynchronizationSize)
                        [0].setNestedScrollingEnabled(false);
                setDynamicCommunityView(mSynchronizationSize,0);
                mSynchronizationSize ++;
                if (mSynchronizationSize - mDynamicCommunity.size() == 0) {
                    isSynchronization = false;
                    return;
                }
                ApiResourcesModel apiResourcesModel = new ApiResourcesModel();
                apiResourcesModel.setOrder_by("weight_score");
                apiResourcesModel.setOrder("desc");
                ArrayList<Integer> city_ids = new ArrayList<>();
                city_ids.add(Integer.parseInt(mCityIdStr));
                apiResourcesModel.setCity_ids(city_ids);
                apiResourcesModel.setPage("1");
                apiResourcesModel.setPage_size(4);
                apiResourcesModel.setDynamic_name(mDynamicCommunity.get(mSynchronizationSize).
                        getData().get(0).getName());
                ArrayList<Integer> dynamic_id = new ArrayList<>();
                dynamic_id.add(mDynamicCommunity.get(mSynchronizationSize).
                        getData().get(0).getId());
                apiResourcesModel.setDynamic_id(dynamic_id);
                mSearchResListMvpPresenter.getCommunityList(apiResourcesModel);
            } else {
                if (mDynamicCommunityAdapterMap.get(mDynamicCommunityViewPosition)
                        [mDynamicCommunityItemPageSelectedPosition] == null) {
                    mDynamicCommunityListMap.get(mDynamicCommunityViewPosition)
                            [mDynamicCommunityItemPageSelectedPosition] = list;
                } else {
                    if (mDynamicCommunityListMap.get(mDynamicCommunityViewPosition)
                            [mDynamicCommunityItemPageSelectedPosition] != null) {
                        mDynamicCommunityListMap.get(mDynamicCommunityViewPosition)
                                [mDynamicCommunityItemPageSelectedPosition].clear();
                        mDynamicCommunityListMap.get(mDynamicCommunityViewPosition)
                                [mDynamicCommunityItemPageSelectedPosition].addAll(list);
                    }
                }
                if (mDynamicCommunityAdapterMap.get(mDynamicCommunityViewPosition)
                        [mDynamicCommunityItemPageSelectedPosition] == null) {
                    mDynamicCommunityAdapterMap.get(mDynamicCommunityViewPosition)
                            [mDynamicCommunityItemPageSelectedPosition] = new HomeDynamicCommunityAdapter(R.layout.fragment_homeactivity_item,
                            mDynamicCommunityListMap.get(mDynamicCommunityViewPosition)
                                    [mDynamicCommunityItemPageSelectedPosition],HomeFragment.this.getContext(),
                            HomeFragment.this);

                    GridLayoutManager linearLayoutManager = new GridLayoutManager(HomeFragment.this.getActivity(),2);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mDynamicCommunityRVMap.get(mDynamicCommunityViewPosition)
                            [mDynamicCommunityItemPageSelectedPosition].setLayoutManager(linearLayoutManager);
                    mDynamicCommunityRVMap.get(mDynamicCommunityViewPosition)
                            [mDynamicCommunityItemPageSelectedPosition].setAdapter( mDynamicCommunityAdapterMap.get(mDynamicCommunityViewPosition)
                            [mDynamicCommunityItemPageSelectedPosition]);
                    mDynamicCommunityRVMap.get(mDynamicCommunityViewPosition)
                            [mDynamicCommunityItemPageSelectedPosition].setNestedScrollingEnabled(false);
                } else {
                    mDynamicCommunityAdapterMap.get(mDynamicCommunityViewPosition)
                            [mDynamicCommunityItemPageSelectedPosition].notifyDataSetChanged();
                }
                setDynamicCommunityView(-1,-1);
            }
        }
    }

    @Override
    public void onSearchResListFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
    }

    @Override
    public void onSearchResListMoreSuccess(ArrayList<ResourceSearchItemModel> list, Response response) {

    }

    @Override
    public void onSearchResListMoreFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onAttributesSuccess(ArrayList<SearchListAttributesModel> list) {

    }

    @Override
    public void onAttributesFailure(boolean superresult, Throwable error) {

    }

    @Override
    public void onSearchSellResListSuccess(ArrayList<SearchSellResModel> list, Response response) {

    }

    @Override
    public void onSearchSellResListMoreSuccess(ArrayList<SearchSellResModel> list, Response response) {

    }

    @Override
    public void onSearchResListCountSuccess(int count) {

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            //市
            final String city;
            city = location.getCity();
            //区
            final String city_area = location.getDistrict();
            //路
            String area_address = location.getStreet();
            //address
            String areas = location.getAddrStr();
            String area = location.getAddrStr();
            //2018/4/10 测试定位
            boolean replace_city = false;
            if (city != null ) {
                if (city.length() != 0) {
                    final List<ConfigCityParameterModel> configCityParameterModels = ConfigSqlOperation.selectSQL(6,0,HomeFragment.this.getContext());
                    String defaultcity = "";
                    String defaultcitycode = "";
                    ConfigCitiesModels = null;
                    if (configCityParameterModels != null && configCityParameterModels.size() > 0) {
                        for (int i = 0; i < configCityParameterModels.size(); i++) {
                            String configurationcity = configCityParameterModels.get(i).getCity();
                            String citycode = String.valueOf(configCityParameterModels.get(i).getCity_id());
                            String default_city = String.valueOf(configCityParameterModels.get(i).getDefault_city());
                            if (default_city.equals("1")) {
                                defaultcity = configurationcity;
                                defaultcitycode = citycode;
                            }
                            if (city.equals(configurationcity)) {
                                replace_city = true;
                                LoginManager.getInstance().setRegisterCurrentCityid(citycode);
                            }
                        }
                    } else {
                        ConfigCitiesModels = ConfigurationsModel.getInstance().getCitylist();
                        if (ConfigCitiesModels != null && ConfigCitiesModels.size() > 0) {
                            for (int i = 0; i < ConfigCitiesModels.size(); i++) {
                                String configurationcity = ConfigCitiesModels.get(i).getCity();
                                String citycode = String.valueOf(ConfigCitiesModels.get(i).getCity_id());
                                String default_city = String.valueOf(ConfigCitiesModels.get(i).getDefault_city());
                                if (default_city.equals("1")) {
                                    defaultcity = configurationcity;
                                    defaultcitycode = citycode;
                                }
                                if (city.equals(configurationcity)) {
                                    replace_city = true;
                                    LoginManager.getInstance().setRegisterCurrentCityid(citycode);
                                }
                            }
                        }
                    }
                    if ((configCityParameterModels != null && configCityParameterModels.size() > 0) ||
                            (ConfigCitiesModels != null && ConfigCitiesModels.size() > 0)) {
                        if (LoginManager.getInstance().getCurrentCity().length() == 0) {
                            if (!(city.equals(defaultcity))) {
                                if (mUpdateCityIdInt == 1) {
                                    if (!(defaultcity.equals(city))) {
                                        if (defaultcity.length() != 0) {
                                            if (replace_city == true) {
                                                new AlertDialog.Builder(HomeFragment.this.getActivity())
                                                        .setTitle("当前位置为" + city + "是否切换？")
                                                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        })
                                                        .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                                dialog.dismiss();
                                                                if (configCityParameterModels != null && configCityParameterModels.size() > 0) {
                                                                    for (int j = 0; j < configCityParameterModels.size(); j++) {
                                                                        String city_tmp = configCityParameterModels.get(j).getCity();
                                                                        String citycode_tmp = String.valueOf(configCityParameterModels.get(j).getCity_id());
                                                                        if (city.equals(city_tmp)) {
                                                                            LoginManager.getInstance().setCurrentCitycode(citycode_tmp);
                                                                            LoginManager.getInstance().setCurrentCity(city);
                                                                            mCityIdStr = citycode_tmp;
                                                                            new Thread(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    location_handler.sendEmptyMessage(1);
                                                                                }
                                                                            }).start();
                                                                            break;
                                                                        }
                                                                    }
                                                                } else {
                                                                    if (ConfigCitiesModels != null && ConfigCitiesModels.size() > 0) {
                                                                        for (int j = 0; j < ConfigCitiesModels.size(); j++) {
                                                                            String city_tmp = ConfigCitiesModels.get(j).getCity();
                                                                            String citycode_tmp = String.valueOf(ConfigCitiesModels.get(j).getCity_id());
                                                                            if (city.equals(city_tmp)) {
                                                                                LoginManager.getInstance().setCurrentCitycode(citycode_tmp);
                                                                                LoginManager.getInstance().setCurrentCity(city);
                                                                                mCityIdStr = citycode_tmp;
                                                                                new Thread(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        location_handler.sendEmptyMessage(1);
                                                                                    }
                                                                                }).start();
                                                                                break;
                                                                            }
                                                                        }
                                                                    }

                                                                }
                                                            }
                                                        }).show();
                                            }
                                        }
                                    }
                                } else {
                                }
                            } else {
                                LoginManager.getInstance().setCurrentCitycode(defaultcitycode);
                                LoginManager.getInstance().setCurrentCity(defaultcity);
                            }
                        } else {
                            if (!(city.equals(LoginManager.getInstance().getCurrentCity()))) {
                                if (mUpdateCityIdInt == 1) {
                                    if (replace_city == true) {
                                        new AlertDialog.Builder(HomeFragment.this.getActivity())
                                                .setTitle("当前位置为" + city + "是否切换？")
                                                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();

                                                    }
                                                })
                                                .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        dialog.dismiss();
                                                        if (configCityParameterModels != null && configCityParameterModels.size() > 0) {
                                                            for (int j = 0; j < configCityParameterModels.size(); j++) {
                                                                String city_tmp = configCityParameterModels.get(j).getCity();
                                                                String citycode_tmp = String.valueOf(configCityParameterModels.get(j).getCity_id());
                                                                if (city.equals(city_tmp)) {
                                                                    LoginManager.getInstance().setCurrentCitycode(citycode_tmp);
                                                                    LoginManager.getInstance().setCurrentCity(city);
                                                                    mCityIdStr = citycode_tmp;
                                                                    new Thread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            location_handler.sendEmptyMessage(1);
                                                                        }
                                                                    }).start();
                                                                    break;
                                                                }
                                                            }
                                                        } else {
                                                            if (ConfigCitiesModels != null && ConfigCitiesModels.size() > 0) {
                                                                for (int j = 0; j < ConfigCitiesModels.size(); j++) {
                                                                    String city_tmp = ConfigCitiesModels.get(j).getCity();
                                                                    String citycode_tmp = String.valueOf(ConfigCitiesModels.get(j).getCity_id());
                                                                    if (city.equals(city_tmp)) {
                                                                        LoginManager.getInstance().setCurrentCitycode(citycode_tmp);
                                                                        LoginManager.getInstance().setCurrentCity(city);
                                                                        mCityIdStr = citycode_tmp;
                                                                        new Thread(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                location_handler.sendEmptyMessage(1);
                                                                            }
                                                                        }).start();
                                                                        break;
                                                                    }
                                                                }
                                                            }

                                                        }

                                                    }
                                                }).show();
                                    }
                                }
                            } else {

                            }
                        }
                    }
                }
            }
            mLocationClient.stop();
            mLocationClient = null;
        }


    }
    private Handler location_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                medit_area.setText(LoginManager.getInstance().getCurrentCity());
                showProgressDialog();
                initData();
            }
        }
    };
    private void checkUpdate() {
        if (System.currentTimeMillis() - LoginManager.getInstance().getuodatatime() > 24 * 3600 * 1000) {
            mVersionPresenter.getVersion();
        }
    }

    private void registReceiver() {
        myBroadcastReciver = new MyBroadcastReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ConfigBroadcast");
        HomeFragment.this.getContext().registerReceiver(myBroadcastReciver, intentFilter);
    }
    private class  MyBroadcastReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("ConfigBroadcast")) {
                LoginManager.getInstance().setRegisterCurrentCityid(Constants.getDefault_cityid(HomeFragment.this.getContext()));//默认定位到杭州
                getCityList();
                initData();
            }
        }
    }
    private void showpw_notices(final ArrayList<HomeNoticesModel> homeNoticesModel, boolean new_notices) {
        View myView = null;
        if (homeNoticesModel.get(0).getUrl() != null && homeNoticesModel.get(0).getUrl().length() > 0) {
            myView = HomeFragment.this.getActivity().getLayoutInflater().inflate(R.layout.fragment_homeactivity_exclusive_show, null);
        } else {
            myView = HomeFragment.this.getActivity().getLayoutInflater().inflate(R.layout.fragment_homeactivity_exclusive_gone_show, null);
        }
        ImageView close_imageview = (ImageView) myView.findViewById(R.id.fragment_home_exclusive_show_imageview);
        TextView mfragment_home_exclusive_show_btn = (TextView) myView.findViewById(R.id.fragment_home_exclusive_show_btn);
        TextView mfragment_home_exclusive_show_describe = (TextView) myView.findViewById(R.id.fragment_home_exclusive_show_describe);
        TextView mfragment_home_exclusive_show_title = (TextView) myView.findViewById(R.id.fragment_home_exclusive_show_title);
        LinearLayout mfragment_home_exclusive_show_all_layout = (LinearLayout)myView.findViewById(R.id.fragment_home_exclusive_show_all_layout);
        LinearLayout mfragment_home_exclusive_show_title_layout = (LinearLayout)myView.findViewById(R.id.fragment_home_exclusive_show_title_layout);
        int width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        int height = (width-144) * 700 / 600;
        if (homeNoticesModel.get(0).getUrl() != null && homeNoticesModel.get(0).getUrl().length() > 0) {
            mfragment_home_exclusive_show_btn.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams paramgroups= new LinearLayout.LayoutParams((width-144), height);
            mfragment_home_exclusive_show_all_layout.setLayoutParams(paramgroups);
            LoginManager.getInstance().setNoticesUrl(homeNoticesModel.get(0).getUrl()+"&is_app=1");
        } else {
            int hidejeight = (width-144) * 94 / 600;
            LinearLayout.LayoutParams hide_paramgroup = new LinearLayout.LayoutParams((width-144), height-hidejeight);
            mfragment_home_exclusive_show_all_layout.setLayoutParams(hide_paramgroup);
            mfragment_home_exclusive_show_title_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 2.03f));
        }
        if (homeNoticesModel.get(0).getTitle() != null && homeNoticesModel.get(0).getTitle().length() > 0) {
            mfragment_home_exclusive_show_title.setText(homeNoticesModel.get(0).getTitle());
        }
        if (homeNoticesModel.get(0).getContent() != null && homeNoticesModel.get(0).getContent().length() > 0) {
            mfragment_home_exclusive_show_describe.setText(homeNoticesModel.get(0).getContent());
        }
        final Dialog dialog = new AlertDialog.Builder(HomeFragment.this.getContext()).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(myView);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        close_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        mfragment_home_exclusive_show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent about_notices = new Intent(HomeFragment.this.getActivity(),AboutUsActivity.class);
                about_notices.putExtra("type", Config.REPORT_WEB_INT);
                about_notices.putExtra("url", homeNoticesModel.get(0).getUrl()+"&is_app=1");
                startActivity(about_notices);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        LoginManager.getInstance().setNoticesid(homeNoticesModel.get(0).getId());
        if (homeNoticesModel.get(0).getTitle() != null && homeNoticesModel.get(0).getTitle().length() > 0) {
            LoginManager.getInstance().setNoticesTitle(homeNoticesModel.get(0).getTitle());
        }
        if (new_notices == true) {
            LoginManager.getInstance().setNoticescount(1);
        } else {
            LoginManager.getInstance().setNoticescount(LoginManager.getInstance().getNoticescount() + 1);
        }
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if(requestCode == Constants.PermissionRequestCode) {
                //定位
                //2018/4/10 测试权限
                mLocationClient = new LocationClient(HomeFragment.this.getActivity());
                mLocationClient.registerLocationListener(new MyLocationListener());//注册定位监听接口
                initLocation();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if (!deniedPermissions.contains("android.permission.ACCESS_COARSE_LOCATION") &&
                    !deniedPermissions.contains("android.permission.ACCESS_FINE_LOCATION") &&
                    !deniedPermissions.contains("android.permission.READ_PHONE_STATE") &&
                    !deniedPermissions.contains("android.permission.WRITE_EXTERNAL_STORAGE")) {
                mLocationClient = new LocationClient(HomeFragment.this.getActivity());
                mLocationClient.registerLocationListener(new MyLocationListener());//注册定位监听接口
                initLocation();
            }
            if(requestCode == Constants.PermissionRequestCode) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            } else if(requestCode == CAMER_REQUEST_INT) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            }
        }
    };
    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0) {
                return true;
            } else {
                return true;
            }
        }
    };
    @Override
    public void onHomeFailure(boolean superresult, Throwable error) {
        if(HomeReviewListswipList.isShown()){
            HomeReviewListswipList.setRefreshing(false);
        }
        if (!superresult)
            MessageUtils.showToast(getContext(), error.getMessage());
    }

    @Override
    public void onHomeCommunitySuccess(ArrayList<HomeDynamicCommunityModel> list) {
        if(HomeReviewListswipList.isShown()) {
            HomeReviewListswipList.setRefreshing(false);
        }
        //动态场地类目列表
        mDynamicCommunity = list;
        if (list != null && list.size() > 0) {
            mHomeCommunityListLL.setVisibility(View.VISIBLE);
            addCmmunityView();
        }
    }

    @Override
    public void onHomeCaseSuccess(ArrayList<ArticleslistModel> list) {
        if(HomeReviewListswipList.isShown()){
            HomeReviewListswipList.setRefreshing(false);
        }
        mCooperationCaseDataList = list;
        if( mCooperationCaseDataList == null ||  mCooperationCaseDataList.isEmpty()) {
            mHomeCooperationCaseLL.setVisibility(View.GONE);
            return;
        }
        mHomeCooperationCaseLL.setVisibility(View.VISIBLE);
        int width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        int height = (width * 480 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 270 / 480;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height +
                com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),60));
        mHomeCooperationCaseHLV.setLayoutParams(params);
        mCooperationCaseAdapter = new HomeFragmentCooperationCaseAdapter(HomeFragment.this.getContext(),HomeFragment.this,mCooperationCaseDataList,5);
        mHomeCooperationCaseHLV.setAdapter(mCooperationCaseAdapter);
        mHomeCooperationCaseHLV.setItemWidth(
                com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),190));
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mHomeCooperationCaseHLV.setSelection(1);
            }
        }, 500);
    }

    @Override
    public void onHomeHeadlinesSuccess(ArrayList<ArticleslistModel> list) {
        if(HomeReviewListswipList.isShown()){
            HomeReviewListswipList.setRefreshing(false);
        }
        mHeadlinesDataList = list;
        if( mHeadlinesDataList == null ||  mHeadlinesDataList.isEmpty()) {
            mFragmentFlipperLL.setVisibility(View.GONE);
            return;
        }
        mFragmentFlipperLL.setVisibility(View.VISIBLE);
        //2018/3/20 头条滚动显示
        for (int i = 0; i < mHeadlinesDataList.size(); i++) {
            final View contentLL = View.inflate(HomeFragment.this.getActivity(), R.layout.switchview_girdview_item, null);
            TextView contentTV = (TextView) contentLL.findViewById(R.id.switchView_girdview_textview);
            contentTV.setText(mHeadlinesDataList.get(i).getName());
            contentTV.setTag(i);
            contentTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHeadlinesDataList.get((Integer) v.getTag()) != null) {
                        Intent integer = new Intent(HomeFragment.this.getActivity(), AboutUsActivity.class);
                        integer.putExtra("type", Config.HOME_NEW_SIGN_INT);
                        if (mHeadlinesDataList.get((Integer) v.getTag()).getOrigin() != null &&
                                mHeadlinesDataList.get((Integer) v.getTag()).getOrigin().length() > 0) {
                            integer.putExtra("web_url", mHeadlinesDataList.get((Integer) v.getTag()).getOrigin());
                        } else if (mHeadlinesDataList.get((Integer) v.getTag()).getContent() != null &&
                                mHeadlinesDataList.get((Integer) v.getTag()).getContent().length() > 0) {
                            integer.putExtra("web_html_content", mHeadlinesDataList.get((Integer) v.getTag()).getContent());
                        }
                        integer.putExtra("id", mHeadlinesDataList.get((Integer) v.getTag()).getId());
                        integer.putExtra("title", getResources().getString(R.string.home_news_title_text));
                        startActivity(integer);
                    }
                }
            });
            mFragmentFlipper.addView(contentLL);
        }
    }

    @Override
    public void onServiceProviderSuccess(ArrayList<ResourceSearchItemModel> list) {
        if(HomeReviewListswipList.isShown()){
            HomeReviewListswipList.setRefreshing(false);
        }
        mServiceProviderDataList = list;
        if( mServiceProviderDataList == null ||  mServiceProviderDataList.isEmpty()) {
            return;
        }
        mHomeServiceProviderLL.setVisibility(View.VISIBLE);
        int width = mDisplayMetrics.widthPixels;     // 屏幕宽度（像素）
        int height = (width * 330 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 256 / 330;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height +
                com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),75));
        mHomeServiceProviderLV.setLayoutParams(params);
        mHomeServiceProviderAdapter = new HomeHotResAdapter(HomeFragment.this.getContext(),HomeFragment.this,mServiceProviderDataList,3);
        mHomeServiceProviderLV.setAdapter(mHomeServiceProviderAdapter);
        mHomeServiceProviderLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isMobileClick) {
                    Intent aboutusintent = new Intent(HomeFragment.this.getActivity(), AboutUsActivity.class);
                    aboutusintent.putExtra("type", com.linhuiba.business.config.Config.FACILITATOR_INFO_INT);
                    aboutusintent.putExtra("resource_id",mServiceProviderDataList.get(position).getId());
                    startActivity(aboutusintent);
                }
            }
        });
    }

    @Override
    public void onNoticesSuccess(ArrayList<HomeNoticesModel> list) {
        mOnResume = true;
        LoginManager.getInstance().setNoticesshow(0);
        ArrayList<HomeNoticesModel> homeNoticesModellist = list;
        if (homeNoticesModellist != null && homeNoticesModellist.size() > 0) {
            mShowNotices = false;
            if (homeNoticesModellist.get(0).getCount() > 0) {
                if (homeNoticesModellist.get(0).getId() > 0) {
                    if (homeNoticesModellist.get(0).getId() == LoginManager.getInstance().getNoticesid()) {
                        if (LoginManager.getInstance().getNoticescount() < homeNoticesModellist.get(0).getCount()) {
                            if (isAdded()) {
                                showpw_notices(homeNoticesModellist,false);
                            }
                        }
                    } else {
                        if (isAdded()) {
                            showpw_notices(homeNoticesModellist,true);
                        }
                    }
                }
            }
        } else {
            if (LoginManager.getAccessToken() != null && LoginManager.getAccessToken().length() > 0) {
                mShowNotices = false;
            } else {
                mShowNotices = true;
            }
        }
    }

    @Override
    public void onNoticesFailure(boolean superresult, Throwable error) {
        LoginManager.getInstance().setNoticesshow(0);
        if (LoginManager.getAccessToken() != null && LoginManager.getAccessToken().length() > 0) {
            mShowNotices = false;
        } else {
            mShowNotices = true;
        }
    }

    @Override
    public void onBannerSuccess(JSONArray jsonArray) {
        com.alibaba.fastjson.JSONArray bannerarray = jsonArray;
        mBunnerListMap = new HashMap<String, String>();
        if (mPicList != null) {
            mPicList.clear();
        }
        if (bannerarray != null && bannerarray.size() > 0) {
            for (int i = 0; i < bannerarray.size(); i++) {
                com.alibaba.fastjson.JSONObject jsonobject = bannerarray.getJSONObject(i);
                if (jsonobject.get("pic_url") != null) {
                    String key = jsonobject.getString("pic_url");
                    String value = "";
                    if (jsonobject.get("link") != null) {
                        value = jsonobject.getString("link");
                    }
                    mBunnerListMap.put(key, value);
                    mPicList.add(key);
                }
            }
        }
        final List<ImageView> imageList = new ArrayList<>();
        if (mPicList.size() > 0) {
            for (int i = 0; i < mPicList.size(); i++) {
                if (HomeFragment.this.getContext() != null) {
                    if (mPicList.get(i) != null && mPicList.get(i).toString().length() > 0) {

                    }

                }
            }
        }
        showBanner();
    }

    @Override
    public void onMessageSuccess(ArrayList<HomeMessageModel> list) {
        final ArrayList<HomeMessageModel> homeMessageModels = list;
        if (list != null && list.size() > 0) {
            if (mMessageDialog == null || !mMessageDialog.isShowing()) {
                View.OnClickListener uploadListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.upgrade_version_btn:
                                //2018/11/23 推送跳转
                                Constants.pushUrlJumpActivity(homeMessageModels.get(0).getUrl(),HomeFragment.this.getContext(),false);
                                mMessageDialog.dismiss();
                                mHomeMvpPresenter.deleteMessageNotices(String.valueOf(homeMessageModels.get(0).getId()));
                                break;
                            case R.id.upgrade_version_close_imgv:
                                mMessageDialog.dismiss();
                                mHomeMvpPresenter.deleteMessageNotices(String.valueOf(homeMessageModels.get(0).getId()));
                                break;
                        }
                    }
                };
                int width = mDisplayMetrics.widthPixels;
                CustomDialog.Builder builder = new CustomDialog.Builder(HomeFragment.this.getContext());
                //2017/12/8 判断是否强制更新
                mMessageDialog = builder
                        .cancelTouchout(false)
                        .view(R.layout.app_upgrade_version)
                        .showView(R.id.app_upgrade_new_version_tv,View.GONE)
                        .addViewOnclick(R.id.upgrade_version_btn,uploadListener)
                        .addViewOnclick(R.id.upgrade_version_close_imgv,uploadListener)
                        .showView(R.id.app_oval_imgv,View.VISIBLE)
                        .showView(R.id.upgrade_version_close_imgv,View.VISIBLE)
                        .showView(R.id.app_upgrade_imgv,View.GONE)
                        .setText(R.id.upgrade_version_content_tv,homeMessageModels.get(0).getTitle())
                        .setTextColor(R.id.upgrade_version_content_tv,getResources().getColor(R.color.headline_tv_color))
                        .setTextSize(R.id.upgrade_version_content_tv,15)
                        .setText(R.id.upgrade_version_btn,getResources().getString(R.string.home_exclusive_show_btntext))
                        .setOvalImgvUrl(HomeFragment.this.getActivity(),R.id.app_oval_imgv,homeMessageModels.get(0).getImage(),width-
                                        com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),50),
                                (width-com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),50))*420/560)
                        .build();
                mMessageDialog.setOnKeyListener(keylistener);
                com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(HomeFragment.this.getContext(),mMessageDialog);
                mMessageDialog.show();
                Window window = mMessageDialog.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//            if (role == 3) {
//                window.setBackgroundDrawableResource(R.color.switch_layout_bg_color);
//            } else if (role == 2) {
//                window.setBackgroundDrawableResource(R.color.white);
//            }

                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
                lp.height = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
                mMessageDialog.getWindow().setAttributes(lp);
            }
        }
    }

    @Override
    public void onNavigationsSuccess(ArrayList<HomeDynamicCommunityModel> list) {
        //类目导航
        mCategoriesBarList = list;
        if (mCategoriesBarList != null &&
                mCategoriesBarList.size() > 0) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeFragment.this.getActivity(),4);
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mHomeCommunityBarRV.setLayoutManager(gridLayoutManager);
            HomeModuleBarAdapter homeModuleBarAdapter1 = new HomeModuleBarAdapter(
                    R.layout.module_recycle_item_home_dynamic_bar, mCategoriesBarList,
                    HomeFragment.this.getContext(),HomeFragment.this,1);
            mHomeCommunityBarRV.setAdapter(homeModuleBarAdapter1);
            homeModuleBarAdapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    ArrayList<Integer> community_type_ids = new ArrayList<>();
                    if (mCategoriesBarList.get(position).getCategories() != null &&
                            mCategoriesBarList.get(position).getCategories().size() > 0) {
                        for (int i = 0; i < mCategoriesBarList.get(position).getCategories().size(); i++) {
                            community_type_ids.add(mCategoriesBarList.get(position).getCategories().get(i).getId());
                        }
                    }
                    apiResourcesModel = new ApiResourcesModel();
                    apiResourcesModel.setOrder_by("weight_score");
                    apiResourcesModel.setOrder("desc");
                    ArrayList<Integer> city_ids = new ArrayList<>();
                    city_ids.add(Integer.parseInt(mCityIdStr));
                    apiResourcesModel.setCity_ids(city_ids);
                    apiResourcesModel.setCommunity_type_ids(community_type_ids);
                    Intent searchListIntent = new Intent(HomeFragment.this.getActivity(),SearchListActivity.class);
                    searchListIntent.putExtra("is_home_page",4);
                    searchListIntent.putExtra("ApiResourcesModel",(Serializable) apiResourcesModel);
                    startActivity(searchListIntent);
                }
            });
        }
    }

    @Override
    public void onLinhuiDataSuccess(Object data) {
        if (data != null && data.toString().length() > 0) {
            JSONObject jsonObject = JSONObject.parseObject(data.toString());
            if (jsonObject != null && jsonObject.get("opened_city_counts") != null &&
                    jsonObject.get("physical_counts") != null &&
                    jsonObject.get("physical_area_sums") != null &&
                    jsonObject.get("physical_people_sums") != null) {
                mHomeLinhuiDataLL.setVisibility(View.VISIBLE);
                if (jsonObject.get("opened_city_counts").toString().length() > 0) {
                    mHomeLinhuiDataCoverCityTV.setText(Constants.subZeroAndDot(Constants.addComma(jsonObject.get("opened_city_counts").toString(),1)));
                } else {
                    mHomeLinhuiDataCoverCityTV.setText(getResources().getString(R.string.fieldinfo_no_parameter_message));
                }
                if (jsonObject.get("physical_counts").toString().length() > 0) {
                    mHomeLinhuiDataNumOfResTV.setText(Constants.subZeroAndDot(Constants.addComma(jsonObject.get("physical_counts").toString(),1)));
                    mHomeLinhuiDataNumOfResUnitTV.setText("("+
                            getResources().getString(R.string.mTxt_addfield_tw_unit)+")");
                } else {
                    mHomeLinhuiDataNumOfResTV.setText(getResources().getString(R.string.fieldinfo_no_parameter_message));
                    mHomeLinhuiDataNumOfResUnitTV.setText("");
                }
                if (jsonObject.get("physical_area_sums").toString().length() > 0) {
                    mHomeLinhuiDataRentalAreaTV.setText(Constants.subZeroAndDot(Constants.addComma(jsonObject.get("physical_area_sums").toString(),1)));
                } else {
                    mHomeLinhuiDataRentalAreaTV.setText(getResources().getString(R.string.fieldinfo_no_parameter_message));
                }
                if (jsonObject.get("physical_people_sums").toString().length() > 0) {
                    mHomeLinhuiDataNumOfPeopleTV.setText(Constants.subZeroAndDot(Constants.addComma(jsonObject.get("physical_people_sums").toString(),1)));
                } else {
                    mHomeLinhuiDataNumOfPeopleTV.setText(getResources().getString(R.string.fieldinfo_no_parameter_message));
                }
            }
        }
    }

    @Override
    public void onHoverWindowSuccess(final HomeHoverModel data) {
        if (data.getShow_hover_window() != null &&
                data.getShow_hover_window().getField_value() != null &&
                data.getShow_hover_window().getField_value().trim().length() > 0 &&
                data.getShow_hover_window().getField_value().trim().equals("true")) {
            if (data.getHover_image_href() != null &&
                    data.getHover_image_href().getField_value() != null &&
                    data.getHover_image_href().getField_value().trim().length() > 0) {
                if (data.getHover_image_url() != null &&
                        data.getHover_image_url().getField_value() != null &&
                        data.getHover_image_url().getField_value().trim().length() > 0) {
                    mHomeSuspendImgv.setVisibility(View.VISIBLE);
                    mHomeSuspendImgv.setVisibility(View.VISIBLE);
                    Picasso.with(HomeFragment.this.getContext()).load(data.getHover_image_url().getField_value()).resize(
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),48),
                            com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),48)
                    ).into(mHomeSuspendImgv);
                    mHomeSuspendImgv.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            Constants.pushUrlJumpActivity(data.getHover_image_href().getField_value(),HomeFragment.this.getContext(),false);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onVersionSuccess(Version versions) {
        final Version version = versions;
        if ((version != null && version.getVid() > BuildConfig.VERSION_CODE) ||
                (version != null && version.getForce_update() == 1)) {
            if (mCustomDialog == null || !mCustomDialog.isShowing()) {
                View.OnClickListener uploadListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.upgrade_version_btn:
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Uri content_url = Uri.parse(version.getUrl());
                                intent.setData(content_url);
                                HomeFragment.this.getActivity().startActivity(intent);
                                //2017/12/11 强制更新不关闭
                                if (version.getForce_update() == 1) {

                                } else {
                                    LoginManager.getInstance().setupdatatime();
                                    mCustomDialog.dismiss();
                                }
                                break;
                            case R.id.upgrade_version_close_imgv:
                                mCustomDialog.dismiss();
                                LoginManager.getInstance().setupdatatime();
                                break;
                        }
                    }
                };
                CustomDialog.Builder builder = new CustomDialog.Builder(HomeFragment.this.getContext());
                //2017/12/8 判断是否强制更新
                if (version.getForce_update() == 1) {
                    isForcedUpdating = true;
                    mCustomDialog = builder
                            .cancelTouchout(false)
                            .view(R.layout.app_upgrade_version)
                            .addViewOnclick(R.id.upgrade_version_btn,uploadListener)
                            .showView(R.id.upgrade_version_close_imgv,View.GONE)
                            .setText(R.id.upgrade_version_content_tv,version.getNewfeature())
                            .build();
                    mCustomDialog.setOnKeyListener(keylistener);
                } else {
                    mCustomDialog = builder
                            .cancelTouchout(false)
                            .view(R.layout.app_upgrade_version)
                            .addViewOnclick(R.id.upgrade_version_btn,uploadListener)
                            .addViewOnclick(R.id.upgrade_version_close_imgv,uploadListener)
                            .setText(R.id.upgrade_version_content_tv,version.getNewfeature())
                            .build();
                }
                com.linhuiba.linhuifield.connector.Constants.hideUploadPictureLine(HomeFragment.this.getContext(),mCustomDialog);
                mCustomDialog.show();
            }
        }
    }

    @Override
    public void onVersionFailure(boolean superresult, Throwable error) {

    }
    public class PagerChangeListener implements ViewPager.OnPageChangeListener {
        int typePosition;
        public PagerChangeListener(int type){
            typePosition = type;
        }
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(final int position) {
            mDynamicCommunityItemPageSelectedPosition = position;
            mDynamicCommunityViewPosition = typePosition;
            mDynamicCommunityCheckMap.put(mDynamicCommunityViewPosition,mDynamicCommunityItemPageSelectedPosition);
            mHomeCommunityItemLLS.get(mDynamicCommunityViewPosition).mShowAllTextView.setText(
                    getResources().getString(R.string.home_look_more_list_tv_str) +
                            mDynamicCommunityTlStrMap.get(mDynamicCommunityViewPosition)[mDynamicCommunityItemPageSelectedPosition] +
                            getResources().getString(R.string.leftimg_text)
            );
            mDynamicCommunityNoTVMap.get(mDynamicCommunityViewPosition)[mDynamicCommunityItemPageSelectedPosition].setText(
                    getResources().getString(R.string.module_home_no_community_data_msg) +
                    mDynamicCommunityTlStrMap.get(mDynamicCommunityViewPosition)[mDynamicCommunityItemPageSelectedPosition] +
                    getResources().getString(R.string.module_home_no_community_data_other_msg));
            if (mDynamicCommunityListMap.get(mDynamicCommunityViewPosition)
                    [mDynamicCommunityItemPageSelectedPosition] == null ||
                    mDynamicCommunityListMap.get(mDynamicCommunityViewPosition)
                            [mDynamicCommunityItemPageSelectedPosition].size() == 0) {
                ApiResourcesModel apiResourcesModel = new ApiResourcesModel();
                apiResourcesModel.setOrder_by("weight_score");
                apiResourcesModel.setOrder("desc");
                ArrayList<Integer> city_ids = new ArrayList<>();
                city_ids.add(Integer.parseInt(mCityIdStr));
                apiResourcesModel.setCity_ids(city_ids);
                apiResourcesModel.setPage("1");
                apiResourcesModel.setPage_size(4);
                apiResourcesModel.setDynamic_name(mDynamicCommunity.get(mDynamicCommunityViewPosition).
                        getData().get(mDynamicCommunityItemPageSelectedPosition).getName());
                ArrayList<Integer> dynamic_id = new ArrayList<>();
                dynamic_id.add(mDynamicCommunity.get(mDynamicCommunityViewPosition).
                        getData().get(mDynamicCommunityItemPageSelectedPosition).getId());
                apiResourcesModel.setDynamic_id(dynamic_id);
                mSearchResListMvpPresenter.getCommunityList(apiResourcesModel);
            } else {
                setDynamicCommunityView(-1,-1);
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    //各种数据缺失的默认显示
    private void setDynamicCommunityView(int viewPosition,int selectedPosition) {//参数代表 初始化的每个分组的第一项
        if (viewPosition > -1 && selectedPosition > -1) {
            mDynamicCommunityViewPosition = viewPosition;
            mDynamicCommunityItemPageSelectedPosition = selectedPosition;
        }
        if (mDynamicCommunityListMap.get(mDynamicCommunityViewPosition)
                [mDynamicCommunityItemPageSelectedPosition].size() == 0) {
            int height = (com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),160));
            LinearLayout.LayoutParams paramgroups = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    height);
            mHomeCommunityItemLLS.get(mDynamicCommunityViewPosition).mInvoiceTitleVP.setLayoutParams(paramgroups);
            mHomeCommunityItemLLS.get(mDynamicCommunityViewPosition).mShowAllTextView.setVisibility(View.GONE);
            mDynamicCommunityNoLLMap.get(mDynamicCommunityViewPosition)
                    [mDynamicCommunityItemPageSelectedPosition].setVisibility(View.VISIBLE);
            mDynamicCommunityRVMap.get(mDynamicCommunityViewPosition)
                    [mDynamicCommunityItemPageSelectedPosition].setVisibility(View.GONE);
        } else {
            if (mDynamicCommunityListMap.get(mDynamicCommunityViewPosition)
                    [mDynamicCommunityItemPageSelectedPosition].size() == 2) {
                int width = mDisplayMetrics.widthPixels;
                int height = ((width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 262 / 338 +
                        com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),62));
                LinearLayout.LayoutParams paramgroups = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        height);
                mHomeCommunityItemLLS.get(mDynamicCommunityViewPosition).mInvoiceTitleVP.setLayoutParams(paramgroups);
                mHomeCommunityItemLLS.get(mDynamicCommunityViewPosition).mShowAllTextView.setVisibility(View.GONE);
                mDynamicCommunityNoLLMap.get(mDynamicCommunityViewPosition)
                        [mDynamicCommunityItemPageSelectedPosition].setVisibility(View.GONE);
                mDynamicCommunityRVMap.get(mDynamicCommunityViewPosition)
                        [mDynamicCommunityItemPageSelectedPosition].setVisibility(View.VISIBLE);
            } else {
                mHomeCommunityItemLLS.get(mDynamicCommunityViewPosition).mShowAllTextView.setVisibility(View.VISIBLE);
                if (mDynamicCommunityListMap.get(mDynamicCommunityViewPosition)
                        [mDynamicCommunityItemPageSelectedPosition].size() == 4) {
                    if (mDynamicCommunityListMap.get(mDynamicCommunityViewPosition)
                            [mDynamicCommunityItemPageSelectedPosition].get(3).getId() == null) {
                        mHomeCommunityItemLLS.get(mDynamicCommunityViewPosition).mShowAllTextView.setVisibility(View.GONE);
                    }
                }
                int width = mDisplayMetrics.widthPixels;
                int height = ((width* 338 / com.linhuiba.linhuipublic.config.Config.DESIGN_WIDTH) * 262 / 338 +
                        com.linhuiba.linhuifield.connector.Constants.Dp2Px(HomeFragment.this.getContext(),62)) * 2;
                LinearLayout.LayoutParams paramgroups = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        height);
                mHomeCommunityItemLLS.get(mDynamicCommunityViewPosition).mInvoiceTitleVP.setLayoutParams(paramgroups);
                mDynamicCommunityNoLLMap.get(mDynamicCommunityViewPosition)
                        [mDynamicCommunityItemPageSelectedPosition].setVisibility(View.VISIBLE);
                mDynamicCommunityRVMap.get(mDynamicCommunityViewPosition)
                        [mDynamicCommunityItemPageSelectedPosition].setVisibility(View.VISIBLE);
            }
        }
    }
}
