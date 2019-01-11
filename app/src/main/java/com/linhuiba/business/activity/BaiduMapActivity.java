package com.linhuiba.business.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baselib.app.util.MessageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.MapOtherPhyAdapter;
import com.linhuiba.business.adapter.ResourcesScreeningItemAdapter;
import com.linhuiba.business.adapter.ResourcesScreeningNewAdapter;
import com.linhuiba.business.adapter.SearchAreaPwAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.LonLatClass;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fragment.SearchListFragment;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.model.MapCommunitySearchListModel;
import com.linhuiba.business.model.MapCommunityInfoModel;
import com.linhuiba.business.model.MapSearchModel;
import com.linhuiba.business.model.ResourceMapModel;
import com.linhuiba.business.model.MapCommunityInfoModel;
import com.linhuiba.business.model.SearchAreaPwModel;
import com.linhuiba.business.model.SearchAreaSubwayPwModel;
import com.linhuiba.business.model.SearchListAttributesModel;
import com.linhuiba.business.mvpmodel.LoginMvpModel;
import com.linhuiba.business.mvppresenter.CommunityMapMvpPresenter;
import com.linhuiba.business.mvpview.CommunityMapMvpView;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Request;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.fieldactivity.FieldAddFieldGuideActivity;
import com.linhuiba.linhuifield.fieldactivity.Field_FieldListActivity;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuifield.sqlite.ConfigCitiesModel;
import com.linhuiba.linhuifield.sqlite.ConfigCityParameterModel;
import com.linhuiba.linhuifield.sqlite.ConfigSqlOperation;
import com.linhuiba.linhuifield.sqlite.ConfigurationsModel;
import com.linhuiba.linhuipublic.config.Config;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.linhuiba.linhuipublic.config.SupportPopupWindow;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.squareup.picasso.Picasso;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/22.
 */
public class BaiduMapActivity extends BaseMvpActivity implements BaiduMap.OnMapLoadedCallback,
        CommunityMapMvpView {
    @InjectView(R.id.map_hide_relativelayout)
    LinearLayout map_hide_relativelayout;
    @InjectView(R.id.mapsearch_resourcename)
    TextView mapsearch_resourcename;
    @InjectView(R.id.map_searchadvertising_img)
    ImageView map_searchadvertising_img;
    @InjectView(R.id.map_search_address)
    TextView mmap_search_address;
    @InjectView(R.id.map_community_type_tv)
    TextView mMapCommunityTypeTV;
    @InjectView(R.id.map_distance_tv)
    TextView mMapDistanceTV;
    @InjectView(R.id.resource_map_release_demand_textview)
    TextView mMapDemandTV;
    @InjectView(R.id.baidumap_location_imageview)
    ImageView mbaidumap_location_imageview;
    @InjectView(R.id.map_headrelativelayout)
    LinearLayout mmap_headrelativelayout;
    @InjectView(R.id.resource_map_search_look_fim_textview)
    EditText mresource_map_search_look_fim_textview;
    @InjectView(R.id.resource_map_search_size_textview)
    TextView mresource_map_search_size_textview;
    @InjectView(R.id.resource_map_search_size_remiand_textview)
    TextView tv_resource_map_search_size_remiand;
    @InjectView(R.id.resource_map_search_size_layout)
    LinearLayout mresource_map_search_size_layout;
    @InjectView(R.id.map_search_close_search_condition_layout)
    LinearLayout mmap_search_close_search_condition_layout;
//    @InjectView(R.id.resource_map_no_searchlist)
//    LinearLayout mresource_map_no_searchlist;
    @InjectView(R.id.resource_map_nearby_search_name_textview)
    TextView tv_resource_map_nearby_search_name;
    @InjectView(R.id.search_area_type_textview)
    TextView msearch_area_type_textview;
    @InjectView(R.id.View_view)
    View View_view;
    @InjectView(R.id.screening_txt)
    TextView mscreening_txt;
    @InjectView(R.id.map_search_location_name_tv)
    TextView mMapSearchLocationNameTV;
    @InjectView(R.id.map_search_location_address_tv)
    TextView mMapSearchLocationAddressTV;
    @InjectView(R.id.map_search_location_ll)
    LinearLayout mMapSearchLocationLL;
    @InjectView(R.id.resource_map_search_info_layout)
    RelativeLayout mMapResourceInfoRL;
    @InjectView(R.id.map_search_area_ll)
    LinearLayout mMapSearchAreaLL;
    @InjectView(R.id.map_other_phy_rv)
    RecyclerView mMapOtherPhyRV;
    @InjectView(R.id.map_other_phy_rv_ll)
    LinearLayout mMapOtherPhyListLL;
    @InjectView(R.id.map_other_phy_no_ll)
    LinearLayout mMapOtherPhyNullListLL;
    @InjectView(R.id.map_nearby_imgv)
    LinearLayout mMapNearbyImgv;
    @InjectView(R.id.map_nearby_tv)
    TextView mMapNearbyTV;

    private static BaiduMap mBaiduMap = null;
    private MapView mBaiduMapView;
    private int mSearchType = 1;//资源类型
    private String mCityCode;
    private String mCityName;
    private String mSearchAddressCityName;
    private ArrayList<MapCommunityInfoModel> mMapSearchList = new ArrayList<MapCommunityInfoModel>();
    private String mKeyWords = "";
    private Marker mClickedMarker;
    private MapCommunityInfoModel mMapSearchModel;
    public ArrayList<Call> mCallsList = new ArrayList<>();
    private MapStatus mMapStatus;
    private ClusterManager<ResourceMapModel> mClusterManager;
    private List<ResourceMapModel> mResourceMapModelList = new ArrayList<ResourceMapModel>();
    private final int MAP_STATUS_CHANGE = 100;
    private LatLng mStatusLatLng;
    private float mStatuszoom;
    public LocationClient mMapLocationClient = null;
    private ArrayList<Field_AddResourceCreateItemModel> mMapCityList = new ArrayList<>();
    private int mNearby = -1;//附近几公里
    private HashMap<String,Object> mSearchAddressBackMap = new HashMap<>();//搜索百度地址的信息
    private boolean mBooleanSearchMapAddressBack;//是否搜索过百度中的地址
    private Marker mMapLocationMarker;//搜索百度地址后的marker
    boolean isRefreshStatus = true;//地图改变状态是否请求数据
    private MapCommunityInfoModel mNearbyInfoModel = new MapCommunityInfoModel();//要搜索附近的点model
    private LatLngBounds mLatLngBounds = null;
    private LatLng mNearByLatLng;
    private ApiResourcesModel apiResourcesModel = new ApiResourcesModel();//搜索列表的model
    private Drawable mSortGreyUpDrawable;//排序
    private Drawable mSortGreyDownDrawable;//排序
    private Drawable mSortBlueUpDrawable;//排序
    private Drawable mSortBlueDownDrawable;//排序
    private SupportPopupWindow mSearchSortPw;
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
    private ArrayList<SearchAreaPwModel> mTradingAreasList;//配置文件中商圈解析
    private ArrayList<SearchAreaSubwayPwModel> mSubwayList;//配置文件中的地铁
    private ArrayList<SearchAreaPwModel> mSubwayLineList;//配置文件中的地铁线
    private ArrayList<SearchAreaPwModel> mDistrictList;//配置文件中城市解析
    private ArrayList<SearchAreaPwModel> mSearchAreaList;//区域第一列列表
    private HashMap<String, String> mCategoryMap;//配置文件中类目
    private List<ConfigCityParameterModel> field_labels_list  = new ArrayList<>();//邻汇推荐中所有的名称要显示在界面上的数组
    private ArrayList<String> mCategoryList = new ArrayList<String>();//类目中所有的名称要显示在界面上的数组
    private ArrayList<SearchListAttributesModel> mAttributesList = new ArrayList<>();//属性中所有的名称要显示在界面上的数组
    private HashMap<Integer,HashMap<String,Integer>> mAttributesMap = new HashMap<>();//属性

    public int GridviewNumColumns = 0;//根据屏幕尺寸设置gridview的一行显示个数
    public int field_labels_int = 0;//判断邻汇推荐是否有选中 1选中显示全部反之显示3条数据
    public int category_id_int = 0;//判断类目是否有选中 1选中显示全部反之显示3条数据
    public HashMap<String,Integer> attributesChooseMap;//判断属性是否有选中 1选中显示全部反之显示3条数据
    private ListView mresourcesscreening_stickygridview_new;// 筛选pw的listview
    private ResourcesScreeningNewAdapter resourcesScreeningNewAdapter;// 筛选pw的adapter
    private SupportPopupWindow screening_pw;//筛选pw
    private double mIntentLatitudeDelta;//传过来的纬度
    private double mIntentLongitudeDelta;//传过来的经度
    private boolean isChooseDistricts;//是否选中区域 要画线
    private boolean isSearchAreaReset;//是否区域重置 判断点了重置是否点了确定
    private List<String> mChooseDistrictList = new ArrayList<>();//选中的区域列表
    private Dialog mCustomDialog;
    private ImageView mMapGuidePageImgeV;
    private int mMapGuidePageInt;
    private String mSearchAreaTvStr;
    private double longitude = 0;
    private double latitude = 0;
    //新版数据
    private ArrayList<MapCommunityInfoModel> mOtherPhyList = new ArrayList<>();
    private MapOtherPhyAdapter mMapOtherPhyAdapter;
    private CommunityMapMvpPresenter mPresenter;
    private ArrayList<Marker> mReadMarkerList = new ArrayList<>();
    private Overlay mNearByOptions;
    private Point mNearByPoint;
    private ArrayList<SearchAreaPwModel> mStationChoseList = new ArrayList<>();
    private InputMethodManager inputMethodManager;
    private long mClickTime;
    private ArrayList<HashMap<Object,Object>> data_new;//筛选的list
    private ArrayList<HashMap<Object,Object>> mCategoryAdapterList = new ArrayList<>();
    private double mLocationLat;
    private double mLocationLng;
    private boolean isLocationClick;
    private final int defaultNearBy = 3000;
    private final int SERVECE_CODE = 110;
    private Dialog mSearchShareDialog;
    private IWXAPI mIWXAPI;
    private Bitmap mShareBitmap;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MAP_STATUS_CHANGE:
                    MapStatus mapStatus = (MapStatus) msg.obj;
                    if(mapStatus!=null){
                        if (mKeyWords != null) {
                            if (mKeyWords.length() != 0) {
                                mKeyWords = "";
                            }
                        }
                        getmapresourcesmarks(0);
                    }
                    break;
                case 0:
                    View myView = BaiduMapActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                    String shareurl = com.linhuiba.business.config.Config.SHARE_FIELDS_LIST_URL+ getshareurl() + "&BackKey=1&is_app=1";
                    String sharewxMinShareLinkUrl = com.linhuiba.business.config.Config.WX_MINI_SHARE_FIELDS_LIST_URL+ getshareurl() + "&BackKey=1&is_app=1";
                    String ShareIconStr = "";//分享列表图片的url
                    Constants constants = new Constants(BaiduMapActivity.this,
                            ShareIconStr);
                    constants.shareWXMiniPopupWindow(BaiduMapActivity.this,myView,mSearchShareDialog,mIWXAPI,shareurl,
                            getResources().getString(R.string.search_fieldlist_activvity_sharetitle_text),
                            getResources().getString(R.string.search_fieldlist_activvity_sharetitle_text)
                            ,mShareBitmap,sharewxMinShareLinkUrl,mShareBitmap,getResources().getString(R.string.search_fieldlist_activvity_sharetitle_text));
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidumap);
        ButterKnife.inject(this);
        initview();
    }
    @OnClick({
            R.id.map_area_backimg,
            R.id.map_search_img,
            R.id.baidumap_location_imageview,
            R.id.map_search_close_search_condition_layout,
            R.id.resource_map_nearby_textview,
            R.id.resource_map_release_demand_textview,
            R.id.resource_map_search_size_layout,
            R.id.search_area_type_layout,
            R.id.screening_txt,
            R.id.map_jump_searchlist_img,
            R.id.map_service_tv,
            R.id.map_share_imgbtn
    })
    public void MapOnclick(View view) {
        switch (view.getId()) {
            case R.id.map_area_backimg:
                if (map_hide_relativelayout.getVisibility() == View.GONE) {
                    finish();
                } else {
                    map_hide_relativelayout.setVisibility(View.GONE);
                    mBaiduMapView.showZoomControls(true);
                    if (check_input()) {
                        mmap_search_close_search_condition_layout.setVisibility(View.VISIBLE);
                    } else {
                        mmap_search_close_search_condition_layout.setVisibility(View.GONE);
                    }
                    mbaidumap_location_imageview.setVisibility(View.VISIBLE);
                    mMapDemandTV.setVisibility(View.VISIBLE);
                    mmap_headrelativelayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.map_search_img:
                Intent mapsearchintent = new Intent(BaiduMapActivity.this,MapSearchActivity.class);
                mapsearchintent.putExtra("cityname",mCityName);
                startActivityForResult(mapsearchintent,1);
                break;
            case R.id.baidumap_location_imageview:
                isLocationClick = true;
                AndPermission.with(BaiduMapActivity.this)
                        .requestCode(Constants.PermissionRequestCode)
                        .permission(Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.CHANGE_WIFI_STATE,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                        .callback(listener)
                        .start();
                break;
            case R.id.map_search_close_search_condition_layout:
                //2017/12/19 清除搜索条件
                apiResourcesModel = new ApiResourcesModel();
//                apiResourcesModel.setResource_type("1");
                isChooseDistricts = false;
                if (mStationChoseList != null &&
                        mStationChoseList.size() > 0) {
                    mStationChoseList.clear();
                }
                mNearby = -1;
                mBooleanSearchMapAddressBack = false;
                mMapLocationMarker = null;
                tv_resource_map_nearby_search_name.setVisibility(View.GONE);
                mresource_map_search_look_fim_textview.setText("");
//                tv_resource_map_nearby.setEnabled(false);
                Drawable drawable_add_pressed = getResources().getDrawable(R.drawable.ic_screening);
                drawable_add_pressed.setBounds(0, 0, drawable_add_pressed.getMinimumWidth(), drawable_add_pressed.getMinimumHeight());
                mscreening_txt.setCompoundDrawables(null, null, drawable_add_pressed, null);
                mscreening_txt.setTextColor(getResources().getColor(R.color.headline_tv_color));
                mscreening_txt.setText(getResources().getString(R.string.search_fieldlist_title_txt));
                msearch_area_type_textview.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                msearch_area_type_textview.setTextColor(getResources().getColor(R.color.headline_tv_color));
                msearch_area_type_textview.setText(getResources().getString(R.string.invoiceinfo_area_txt));
                getmapresourcesmarks(0);
                break;
            case R.id.resource_map_nearby_textview:
                if (mClickedMarker != null) {
                    if (mresource_map_search_look_fim_textview.getText().toString().trim().length() > 0) {
                        mNearby = Integer.parseInt(mresource_map_search_look_fim_textview.getText().toString().trim());
                        if (mNearby > 0) {
                            mNearbyInfoModel = (MapCommunityInfoModel) mClickedMarker.getExtraInfo().get("info");
                            mNearByLatLng = mClickedMarker.getPosition();
                            map_hide_relativelayout.setVisibility(View.GONE);
                            mBaiduMapView.showZoomControls(true);
                            if (check_input()) {
                                mmap_search_close_search_condition_layout.setVisibility(View.VISIBLE);
                            } else {
                                mmap_search_close_search_condition_layout.setVisibility(View.GONE);
                            }
                            mbaidumap_location_imageview.setVisibility(View.VISIBLE);
                            mMapDemandTV.setVisibility(View.VISIBLE);
                            mmap_headrelativelayout.setVisibility(View.VISIBLE);
                            tv_resource_map_nearby_search_name.setText(mapsearch_resourcename.getText().toString().trim());
                            mIntentLatitudeDelta = 0.06557440399674519;
                            mIntentLongitudeDelta = 0.05623392492461221;
                            if (mNearby > -1) {
                                if (mNearby > 100000) {
                                    mStatuszoom = 8;
                                } else if (mNearby > 70000) {
                                    mStatuszoom = 9;
                                } else if (mNearby > 45000) {
                                    mStatuszoom = 10;
                                } else if (mNearby > 20000) {
                                    mStatuszoom = 11;
                                } else if (mNearby > 9500) {
                                    mStatuszoom = 12;
                                } else if (mNearby > 4300) {
                                    mStatuszoom = 13;
                                } else if (mNearby > 1300) {
                                    mStatuszoom = 14;
                                } else if (mNearby > 800) {
                                    mStatuszoom = 16;
                                } else if (mNearby > 300) {
                                    mStatuszoom = 17;
                                } else if (mNearby > 110) {
                                    mStatuszoom = 18;
                                } else {
                                    mStatuszoom = 19;
                                }
                            }
                            inputMethodManager.hideSoftInputFromWindow(mresource_map_search_look_fim_textview.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            getmapresourcesmarks((int)mStatuszoom);
                        } else {
                            MessageUtils.showToast(getResources().getString(R.string.module_map_near_by_min_hint));
                        }
                    } else {
                        MessageUtils.showToast(getResources().getString(R.string.module_map_near_by_hint));
                    }
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.field_tupesize_errortoast));
                }
                break;
            case R.id.resource_map_release_demand_textview:
                Intent AddDemand = new Intent(BaiduMapActivity.this,AboutUsActivity.class);
                AddDemand.putExtra("type", com.linhuiba.business.config.Config.ADD_DEMAND_WEB_INT);
                startActivity(AddDemand);
                break;
            case R.id.resource_map_search_size_layout:
                //2017/12/19 跳转到列表
                Intent searchIntent = new Intent();
                searchIntent.setClass(BaiduMapActivity.this,SearchListActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("is_home_page",4);
                apiResourcesModel.setCity_name(mCityName);
//                apiResourcesModel.setmSearchAddressBackMap(mSearchAddressBackMap);
                apiResourcesModel.setBooleanSearchMapAddressBack(mBooleanSearchMapAddressBack);
//                apiResourcesModel.setmMapLocationMarker(mMapLocationMarker);
                apiResourcesModel.setNearbyInfoModel(mNearbyInfoModel);
//                apiResourcesModel.setmNearbyStatusLatLng(mNearbyStatusLatLng);
                mBundle.putSerializable("ApiResourcesModel",apiResourcesModel);
                searchIntent.putExtras(mBundle);
                startActivity(searchIntent);
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
            case R.id.screening_txt:
                showResourceScreeningPw();
                break;
            case R.id.map_jump_searchlist_img:
                Intent searchJumpIntent = new Intent();
                searchJumpIntent.setClass(BaiduMapActivity.this,SearchListActivity.class);
                Bundle mSearchBundle = new Bundle();
                mSearchBundle.putInt("is_home_page",4);
                apiResourcesModel.setCity_name(mCityName);
                apiResourcesModel.setBooleanSearchMapAddressBack(mBooleanSearchMapAddressBack);
//                apiResourcesModel.setmMapLocationMarker(mMapLocationMarker);
                apiResourcesModel.setNearbyInfoModel(mNearbyInfoModel);
//                apiResourcesModel.setmNearbyStatusLatLng(mNearbyStatusLatLng);
                mSearchBundle.putSerializable("ApiResourcesModel",apiResourcesModel);
                mSearchBundle.putSerializable("mSearchAddressBackMap",mSearchAddressBackMap);
                searchJumpIntent.putExtras(mSearchBundle);
                startActivity(searchJumpIntent);
                break;
            case R.id.map_service_tv:
                //客服功能
                AndPermission.with(BaiduMapActivity.this)
                        .requestCode(SERVECE_CODE)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(listener)
                        .start();

                break;
            case R.id.map_share_imgbtn:
                shareDialog();
                break;
            default:
                break;
        }
    }
    private void getlongitude_latitude() {
        List<ConfigCityParameterModel> cities = ConfigSqlOperation.selectSQL(6,0,BaiduMapActivity.this);
        if (cities != null &&
                cities.size() > 0) {
            for (int i = 0; i < cities.size(); i++) {
                if (mCityCode != null) {
                    if (mCityCode.equals(String.valueOf(cities.get(i).getCity_id()))) {
                        if (cities.get(i).getLongitude() != null) {
                            longitude = cities.get(i).getLongitude();
                        }
                        if (cities.get(i).getLatitude() != null) {
                            latitude = cities.get(i).getLatitude();
                        }
                    }
                }
            }
        } else {
            ArrayList<ConfigCitiesModel> citylist = ConfigurationsModel.getInstance().getCitylist();
            if (citylist != null && citylist.size() > 0) {
                for (int i = 0; i < citylist.size(); i++) {
                    if (mCityCode != null) {
                        if (mCityCode.equals(String.valueOf(citylist.get(i).getCity_id()))) {
                            if (citylist.get(i).getLongitude() != null) {
                                longitude = citylist.get(i).getLongitude();
                            }
                            if (citylist.get(i).getLatitude() != null) {
                                latitude = citylist.get(i).getLatitude();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                if (data != null && data.getExtras() != null) {
                    if (data.getExtras().toString().length() != 0) {
                        //隐藏所有弹出的内容
                        mBooleanSearchMapAddressBack = true;
                        mSearchAddressBackMap = (HashMap<String, Object>) data.getExtras().get("map_marker_info");
                        mNearby = -1;
                        apiResourcesModel = new ApiResourcesModel();
//                        apiResourcesModel.setResource_type("1");
                        isChooseDistricts = false;
                        tv_resource_map_nearby_search_name.setVisibility(View.GONE);
                        mresource_map_search_look_fim_textview.setText("");
                        Drawable drawable_add_pressed = getResources().getDrawable(R.drawable.ic_screening);
                        drawable_add_pressed.setBounds(0, 0, drawable_add_pressed.getMinimumWidth(), drawable_add_pressed.getMinimumHeight());
                        mscreening_txt.setCompoundDrawables(null, null, drawable_add_pressed, null);
                        mscreening_txt.setTextColor(getResources().getColor(R.color.headline_tv_color));
                        mscreening_txt.setText(getResources().getString(R.string.search_fieldlist_title_txt));
                        msearch_area_type_textview.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                        msearch_area_type_textview.setTextColor(getResources().getColor(R.color.headline_tv_color));
                        msearch_area_type_textview.setText(getResources().getString(R.string.invoiceinfo_area_txt));

                        String mCityNameTmp = mSearchAddressCityName;
                        mSearchAddressCityName = mSearchAddressBackMap.get("city").toString();
                        if (mSearchAddressBackMap.get("city") != null &&
                                !mSearchAddressBackMap.get("city").toString().equals(mCityNameTmp)) {
                            if (mMapCityList.size() > 0) {
                                boolean HaveCity = false;//判断是否存在城市
                                for (int i = 0; i < mMapCityList.size(); i++) {
                                    if (mMapCityList.get(i).getName() != null &&
                                            mMapCityList.get(i).getName().equals(mSearchAddressBackMap.get("city").toString())) {
                                        if (mMapCityList.get(i).getId() > 0) {
                                            mCityCode = String.valueOf(mMapCityList.get(i).getId());
                                            HaveCity = true;
                                            break;
                                        }
                                    }
                                }
                                if (!HaveCity) {
                                    mCityCode = "0";
                                    mMapSearchAreaLL.setVisibility(View.GONE);
                                } else {
                                    getCityMap();
                                    getScreenData();
                                    getSearchAreaData();
                                    showScreeningState();
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
                                    getAreaList();
                                    showChooseAreaState();
                                }
                            } else {
                                mCityCode = "0";
                                mMapSearchAreaLL.setVisibility(View.GONE);
                            }
                        } else {
                            mMapSearchAreaLL.setVisibility(View.VISIBLE);
                        }
                        if (mSearchAddressBackMap.get("location") != null) {
                            mStatuszoom = 17;
                            mStatusLatLng = new LatLng(Double.parseDouble(mSearchAddressBackMap.get("latitude").toString()),
                                    Double.parseDouble(mSearchAddressBackMap.get("longitude").toString()));
                            mMapStatus = new MapStatus.Builder().target(mStatusLatLng).zoom(mStatuszoom).build();
                            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
                        }
                        map_hide_relativelayout.setVisibility(View.VISIBLE);
                        mBaiduMapView.showZoomControls(false);
                        mmap_search_close_search_condition_layout.setVisibility(View.GONE);
                        mbaidumap_location_imageview.setVisibility(View.GONE);
                        mMapDemandTV.setVisibility(View.GONE);
                        mmap_headrelativelayout.setVisibility(View.GONE);
                        mMapOtherPhyListLL.setVisibility(View.GONE);
                        mMapOtherPhyNullListLL.setVisibility(View.VISIBLE);
                        mresource_map_search_look_fim_textview.setText(String.valueOf(defaultNearBy));
                        mMapSearchLocationLL.setVisibility(View.VISIBLE);
                        mMapResourceInfoRL.setVisibility(View.GONE);
                        if (mSearchAddressBackMap.get("name") != null) {
                            mMapSearchLocationNameTV.setText(mSearchAddressBackMap.get("name").toString());
                        }
                        if (mSearchAddressBackMap.get("address") != null) {
                            mMapSearchLocationAddressTV.setText(mSearchAddressBackMap.get("address").toString());
                        }
                    }
                    //搜索显示地图所需功能
                    getmapresourcesmarks(0);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void initview() {
        mPresenter = new CommunityMapMvpPresenter();
        mPresenter.attachView(this);
        mIWXAPI = WXAPIFactory.createWXAPI(BaiduMapActivity.this, Constants.APP_ID);
        mIWXAPI.registerApp(Constants.APP_ID);
        mBaiduMapView = (MapView)findViewById(R.id.baidumapView);
        mBaiduMap = mBaiduMapView.getMap();
        mSortGreyUpDrawable = getResources().getDrawable(R.drawable.ic_open_gary_one_button_normal_three);
        mSortGreyUpDrawable.setBounds(0, 0, mSortGreyUpDrawable.getMinimumWidth(), mSortGreyUpDrawable.getMinimumHeight());
        mSortGreyDownDrawable = getResources().getDrawable(R.drawable.ic_open_gary_button_normal_three);
        mSortGreyDownDrawable.setBounds(0, 0, mSortGreyDownDrawable.getMinimumWidth(), mSortGreyDownDrawable.getMinimumHeight());
        mSortBlueUpDrawable = getResources().getDrawable(R.drawable.ic_openup_button_blue_normal_three);
        mSortBlueUpDrawable.setBounds(0, 0, mSortBlueUpDrawable.getMinimumWidth(), mSortBlueUpDrawable.getMinimumHeight());
        mSortBlueDownDrawable = getResources().getDrawable(R.drawable.ic_open_one_button_blue_normal_three);
        mSortBlueDownDrawable.setBounds(0, 0, mSortBlueDownDrawable.getMinimumWidth(), mSortBlueDownDrawable.getMinimumHeight());
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //地图上新功能的引导页
        if (LoginManager.getInstance().getMap_show_guide() == 0) {
            Intent addfield = new Intent(BaiduMapActivity.this, FieldAddFieldGuideActivity.class);
            addfield.putExtra("show_type",2);
            startActivity(addfield);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent searchlistintent = getIntent();
                if (searchlistintent != null) {
                    if (searchlistintent.getExtras() != null) {
                        mCityCode = searchlistintent.getExtras().getString("citycode");
                        getlongitude_latitude();
                        if (searchlistintent.getExtras().get("ApiResourcesModel") != null) {
                            apiResourcesModel = (ApiResourcesModel) searchlistintent.getSerializableExtra("ApiResourcesModel");
                            mBooleanSearchMapAddressBack = apiResourcesModel.isBooleanSearchMapAddressBack();
//                    mMapLocationMarker = apiResourcesModel.getmMapLocationMarker();
                            mNearbyInfoModel = apiResourcesModel.getNearbyInfoModel();
                            if (mNearbyInfoModel != null &&
                                    mNearbyInfoModel.getLat() != null &&
                                    mNearbyInfoModel.getLng() != null) {
                                mNearByLatLng = new LatLng(mNearbyInfoModel.getLat(),mNearbyInfoModel.getLng());
                            }
//                    mNearbyStatusLatLng = apiResourcesModel.getmNearbyStatusLatLng();
                            mNearby = apiResourcesModel.getNearby();
                            if (apiResourcesModel.getCity_name() != null) {
                                mCityName = apiResourcesModel.getCity_name();
                            }
                        }
                        if (searchlistintent.getExtras().get("mSearchAddressBackMap") != null) {
                            mSearchAddressBackMap = (HashMap<String, Object>) searchlistintent.getExtras().get("mSearchAddressBackMap");
                        }
                    }
                } else {
                    mSearchType = 1;
                }
//                apiResourcesModel.setResource_type(String.valueOf(mSearchType));
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
                LatLng latLng;
                if (apiResourcesModel.getLatitude() > 0 &&
                        apiResourcesModel.getLongitude() > 0) {
                    latLng = new LatLng(apiResourcesModel.getLatitude(),
                            apiResourcesModel.getLongitude());
                } else {
                    latLng = new LatLng(latitude,longitude);
                }
                if (apiResourcesModel.getZoom_level() > 0) {
                    mMapStatus = new MapStatus.Builder().target(latLng).zoom(apiResourcesModel.getZoom_level()).build();
                } else {
                    mMapStatus = new MapStatus.Builder().target(latLng).zoom(12).build();
                }
                if (apiResourcesModel.getLatitude_delta() > 0) {
                    mIntentLatitudeDelta = apiResourcesModel.getLatitude_delta();
                }
                if (apiResourcesModel.getLongitude_delta() > 0) {
                    mIntentLongitudeDelta = apiResourcesModel.getLongitude_delta();
                }
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
                mBaiduMap.setOnMapLoadedCallback(BaiduMapActivity.this);
                getCityMap();
                getScreenData();
                getSearchAreaData();
                getAreaList();
                mInitViewHandler.sendEmptyMessage(1);
            }
        }).start();
        mMapNearbyImgv.setOnTouchListener(new View.OnTouchListener() {
            int lastX, lastY, firstX, firstY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int ea = event.getAction();
                DisplayMetrics dm = getResources().getDisplayMetrics();
                int screenWidth = dm.widthPixels;
//            int screenHeight = dm.heightPixels - 100;//需要减掉图片的高度
                int screenHeight = dm.heightPixels;//需要减掉图片的高度
                switch (ea) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();//获取触摸事件触摸位置的原始X坐标
                        firstX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        firstY = (int) event.getRawY();
                    case MotionEvent.ACTION_MOVE:
                        //event.getRawX();获得移动的位置
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        int l = v.getLeft() + dx;
                        int b = v.getBottom() + dy;
                        int r = v.getRight() + dx;
                        int t = v.getTop() + dy;

                        //下面判断移动是否超出屏幕
//                        if (l < 0) {
//                            l = 0;
//                            r = l + v.getWidth();
//                        }
//                        if (t < 0) {
//                            t = 0;
//                            b = t + v.getHeight();
//                        }
//                        if (r > screenWidth) {
//                            r = screenWidth;
//                            l = r - v.getWidth();
//                        }
//                        if (b > screenHeight) {
//                            b = screenHeight;
//                            t = b - v.getHeight();
//                        }
//                        setLayout(mMapNearbyImgv,lastX,lastY);
//                        v.layout(l, t, r, b);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
//                        v.postInvalidate();
                        if (l == 10 || l < 10 || r > screenWidth - 10 || r == screenWidth - 10) {
                            if (mClickTime  == 0) {
                                mClickTime = System.currentTimeMillis();
                            }
                            if (System.currentTimeMillis() - mClickTime > 500) {
                                //此处做双击具体业务逻辑
                                mNearby = mNearby + 100;
                                mMapNearbyTV.setText(String.valueOf(mNearby) + "m");
                                mClickTime = System.currentTimeMillis();
                                if (map_hide_relativelayout.getVisibility() == View.VISIBLE) {
                                    mresource_map_search_look_fim_textview.setText(String.valueOf(mNearby));
                                }
                            }
                        } else {
                            if (Math.abs(dx) > Math.abs(dy)) {
                                setLayout(mMapNearbyImgv,lastX,
                                        mNearByPoint.y);
                                mNearby = Integer.parseInt(Constants.getorderdoublepricestring(DistanceUtil.getDistance(new LatLng(mNearbyInfoModel.getLat(),mNearbyInfoModel.getLng())
                                        , mBaiduMap.getProjection().fromScreenLocation(new Point(lastX,mNearByPoint.y))),1));
                                if (mNearByOptions != null) {
                                    mNearByOptions.remove();
                                }
                                drawCircle(mNearByLatLng,mNearby,true);
                                mMapNearbyTV.setText(String.valueOf(mNearby) + "m");
                                if (map_hide_relativelayout.getVisibility() == View.VISIBLE) {
                                    mresource_map_search_look_fim_textview.setText(String.valueOf(mNearby));
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mNearby > -1) {
                            if (mNearby > 100000) {
                                mStatuszoom = 8;
                            } else if (mNearby > 70000) {
                                mStatuszoom = 9;
                            } else if (mNearby > 45000) {
                                mStatuszoom = 10;
                            } else if (mNearby > 20000) {
                                mStatuszoom = 11;
                            } else if (mNearby > 9500) {
                                mStatuszoom = 12;
                            } else if (mNearby > 4300) {
                                mStatuszoom = 13;
                            } else if (mNearby > 1300) {
                                mStatuszoom = 14;
                            } else if (mNearby > 800) {
                                mStatuszoom = 16;
                            } else if (mNearby > 300) {
                                mStatuszoom = 17;
                            } else if (mNearby > 110) {
                                mStatuszoom = 18;
                            } else {
                                mStatuszoom = 19;
                            }
                        }
                        getmapresourcesmarks((int)mStatuszoom);
                        break;
                }
                return true;
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (mClickedMarker != null && mMapLocationMarker != mClickedMarker) {
                    MapCommunityInfoModel info = (MapCommunityInfoModel) mClickedMarker.getExtraInfo().get("info");
                    if (mNearby > -1 && info.getId() == mNearbyInfoModel.getId()) {
                        mClickedMarker.setIcon(getNearbyBitmapDescriptor());
                    } else {
                        if (mMapSearchModel != null) {
                            mClickedMarker.setIcon(getBitmapDescriptor(true,"",
                                    mMapSearchModel.getDisplay_price(),mKeyWords,false));
                        }
                    }
                }

                if (marker != mMapLocationMarker) {
                    if (mStatuszoom < 15 || mStatuszoom == 15) {//聚合的点点击进入聚合
                        final LatLng ll = marker.getPosition();
                        mBaiduMap.clear();
                        mStatuszoom = 16;
                        mMapStatus = new MapStatus.Builder().target(ll).zoom(mStatuszoom).build();
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
                        double lat = ll.latitude;
                        double lng = ll.longitude;
                        call_cancel();
                        if (isunacncel()) {
                            mCallsList.clear();
                            if (mSearchType == 1) {
                                if (mBooleanSearchMapAddressBack && mSearchAddressBackMap != null &&//百度地址搜索过
                                        mSearchAddressBackMap.get("location") != null) {
                                    MapCommunityInfoModel mapSearchModel = new MapCommunityInfoModel();
                                    mapSearchModel.setLat(Double.parseDouble(mSearchAddressBackMap.get("latitude").toString()));
                                    mapSearchModel.setLng(Double.parseDouble(mSearchAddressBackMap.get("longitude").toString()));
                                    mapSearchModel.setLatitude(Double.parseDouble(mSearchAddressBackMap.get("latitude").toString()));
                                    mapSearchModel.setLongitude(Double.parseDouble(mSearchAddressBackMap.get("longitude").toString()));
                                    mapSearchModel.setName((String) mSearchAddressBackMap.get("name"));
                                    mapSearchModel.setDetailed_address((String) mSearchAddressBackMap.get("address"));
                                    OverlayOptions overlayOptions = null;
                                    overlayOptions = new MarkerOptions().position(new LatLng(Double.parseDouble(mSearchAddressBackMap.get("latitude").toString()),
                                            Double.parseDouble(mSearchAddressBackMap.get("longitude").toString())))
                                            .icon(BitmapDescriptorFactory
                                                    .fromResource(R.drawable.ic_map_select)).zIndex(5);
                                    //在地图上添加Marker，并显示
                                    mMapLocationMarker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
                                    final Bundle bundle = new Bundle();
                                    bundle.putSerializable("info", mapSearchModel);
                                    mMapLocationMarker.setExtraInfo(bundle);
                                }
                                apiResourcesModel.setLatitude(lat);
                                apiResourcesModel.setLongitude(lng);
                                apiResourcesModel.setLatitude_delta(0.018136696664951657);
                                apiResourcesModel.setLongitude_delta(0.013178141831375001);
                                apiResourcesModel.setKeywords(mKeyWords);
                                apiResourcesModel.setNearby(mNearby);
                                apiResourcesModel.setZoom_level(mStatuszoom);
                                apiResourcesModel.setCity_id(mCityCode);
                                ArrayList<Integer> cityList = new ArrayList();
                                cityList.add(Integer.parseInt(mCityCode));
                                apiResourcesModel.setCity_ids(cityList);
                                MessageUtils.showMapToast(getResources().getString(R.string.module_map_resource_loading_msg));
                                if (mMapSearchList != null) {
                                    mMapSearchList.clear();
                                }
                                mPresenter.getMapResourcesList(BaiduMapActivity.this,apiResourcesModel);
                                browseHistories();
                            }
                        }
                    } else {
                        if (mNearby == -1 && isRefreshStatus) {//不是附近几公里
                            if (marker.getExtraInfo() != null &&
                                    marker.getExtraInfo().get("info") != null) {//是否有数据
                                final MapCommunityInfoModel info = (MapCommunityInfoModel) marker.getExtraInfo().get("info");
                                mClickedMarker = marker;
                                mMapSearchModel = info;
                                mClickedMarker.setIcon(getBitmapDescriptor(false,"",
                                        mMapSearchModel.getDisplay_price(),mKeyWords,true)); //普通的选中marker
                                showMapMarkerCommunityInfo(info);
                                //搜索周边初始化
                                mStatusLatLng = marker.getPosition();
                                mMapStatus = new MapStatus.Builder().target(mStatusLatLng).zoom(mStatuszoom).build();
                                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
                            }
                        } else { //附近几公里
                            if (marker.getExtraInfo() != null &&
                                    marker.getExtraInfo().get("info") != null) {
                                final MapCommunityInfoModel info = (MapCommunityInfoModel) marker.getExtraInfo().get("info");
                                mClickedMarker = marker;
                                mMapSearchModel = info;
                                if (info.getId() == mNearbyInfoModel.getId()) {
                                    mClickedMarker.setIcon(getNearbyBitmapDescriptor());
                                }
                                showMapMarkerCommunityInfo(info);
                                mStatusLatLng = marker.getPosition();
                                mMapStatus = new MapStatus.Builder().target(mStatusLatLng).zoom(mStatuszoom).build();
                                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
                            }
                        }
                        mReadMarkerList.add(mClickedMarker);
                    }
                } else {
                    if (mMapLocationMarker.getExtraInfo() != null &&
                            mMapLocationMarker.getExtraInfo().get("info") != null) {
                        mClickedMarker = mMapLocationMarker;
                        final MapCommunityInfoModel info = (MapCommunityInfoModel) mMapLocationMarker.getExtraInfo().get("info");
                        map_hide_relativelayout.setVisibility(View.VISIBLE);
                        mBaiduMapView.showZoomControls(false);
                        mmap_search_close_search_condition_layout.setVisibility(View.GONE);
                        mbaidumap_location_imageview.setVisibility(View.GONE);
                        mMapDemandTV.setVisibility(View.GONE);
                        mmap_headrelativelayout.setVisibility(View.GONE);
                        mMapSearchLocationLL.setVisibility(View.VISIBLE);
                        mMapResourceInfoRL.setVisibility(View.GONE);
                        if (info.getName() != null) {
                            mMapSearchLocationNameTV.setText(info.getName());
                        }
                        if (info.getDetailed_address() != null) {
                            mMapSearchLocationAddressTV.setText(info.getDetailed_address());
                        }
                        mStatusLatLng = marker.getPosition();
                        mMapStatus = new MapStatus.Builder().target(mStatusLatLng).zoom(mStatuszoom).build();
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
                    }
                }
                return false;
            }
        });
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mClickedMarker != null) {
                    MapCommunityInfoModel info = (MapCommunityInfoModel) mClickedMarker.getExtraInfo().get("info");
                    if (mNearby > -1 && info.getId() == mNearbyInfoModel.getId()) {
                        mClickedMarker.setIcon(getNearbyBitmapDescriptor());
                    } else {
                        if (mMapSearchModel != null) {
                            mClickedMarker.setIcon(getBitmapDescriptor(true,"",
                                    mMapSearchModel.getDisplay_price(),mKeyWords,false));
                        }
                    }
                    inputMethodManager.hideSoftInputFromWindow(mresource_map_search_look_fim_textview.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if (map_hide_relativelayout.getVisibility() == View.VISIBLE) {
                        map_hide_relativelayout.setVisibility(View.GONE);
                        mBaiduMapView.showZoomControls(true);
                        if (check_input()) {
                            mmap_search_close_search_condition_layout.setVisibility(View.VISIBLE);
                        } else {
                            mmap_search_close_search_condition_layout.setVisibility(View.GONE);
                        }
                        mbaidumap_location_imageview.setVisibility(View.VISIBLE);
                        mMapDemandTV.setVisibility(View.VISIBLE);
                        mmap_headrelativelayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        AndPermission.with(BaiduMapActivity.this)
                .requestCode(Constants.PermissionRequestCode)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .callback(listener)
                .start();
    }
    private void getmapresourcesmarks(int mOtherStatuszoom) {
        mMapNearbyImgv.setVisibility(View.GONE);
        mStatusLatLng = mBaiduMap.getMapStatus().target;
        double lat = mStatusLatLng.latitude;
        double lng = mStatusLatLng.longitude;
        mLatLngBounds = mBaiduMap.getMapStatus().bound;
        if (mOtherStatuszoom > 0) {
            mStatuszoom = mOtherStatuszoom;
        } else {
            mStatuszoom = mBaiduMap.getMapStatus().zoom;
        }
        mMapStatus = new MapStatus.Builder().target(mStatusLatLng).zoom(mStatuszoom).build();
        mBaiduMap.clear();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
        mresource_map_search_size_layout.setVisibility(View.GONE);//搜索到的场地数量的提醒
        call_cancel();
        if (isunacncel()) {
            mCallsList.clear();
            if (mBooleanSearchMapAddressBack && mSearchAddressBackMap != null &&
                    mSearchAddressBackMap.get("location") != null) {
                MapCommunityInfoModel mapSearchModel = new MapCommunityInfoModel();
                mapSearchModel.setLatitude(Double.parseDouble(mSearchAddressBackMap.get("latitude").toString()));
                mapSearchModel.setLongitude(Double.parseDouble(mSearchAddressBackMap.get("longitude").toString()));
                mapSearchModel.setLat(Double.parseDouble(mSearchAddressBackMap.get("latitude").toString()));
                mapSearchModel.setLng(Double.parseDouble(mSearchAddressBackMap.get("longitude").toString()));
                mapSearchModel.setName((String) mSearchAddressBackMap.get("name").toString());
                mapSearchModel.setDetailed_address((String) mSearchAddressBackMap.get("address").toString());
                OverlayOptions overlayOptions = null;
                overlayOptions = new MarkerOptions().position(new LatLng(Double.parseDouble(mSearchAddressBackMap.get("latitude").toString()),
                        Double.parseDouble(mSearchAddressBackMap.get("longitude").toString())))
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.ic_map_select)).zIndex(5);
                //在地图上添加Marker，并显示
                mMapLocationMarker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
                final Bundle bundle = new Bundle();
                bundle.putSerializable("info", mapSearchModel);
                mMapLocationMarker.setExtraInfo(bundle);
                if (mMapSearchLocationLL.getVisibility() == View.VISIBLE) {
                    mClickedMarker = mMapLocationMarker;
                }
            }
            if (mIntentLatitudeDelta > 0 && mIntentLongitudeDelta > 0) {
                apiResourcesModel.setLatitude_delta(mIntentLatitudeDelta);
                apiResourcesModel.setLongitude_delta(mIntentLongitudeDelta);
                mIntentLatitudeDelta = 0;
                mIntentLongitudeDelta = 0;
            } else {
                apiResourcesModel.setLatitude_delta(mLatLngBounds.northeast.latitude - mLatLngBounds.southwest.latitude);
                apiResourcesModel.setLongitude_delta(mLatLngBounds.northeast.longitude - mLatLngBounds.southwest.longitude);
            }
            apiResourcesModel.setKeywords(mKeyWords);
            apiResourcesModel.setNearby(mNearby);
            if (mNearby > -1) {
                apiResourcesModel.setLatitude(mNearByLatLng.latitude);
                apiResourcesModel.setLongitude(mNearByLatLng.longitude);
            } else {
                apiResourcesModel.setLatitude(lat);
                apiResourcesModel.setLongitude(lng);
            }
            apiResourcesModel.setZoom_level(mStatuszoom);
            apiResourcesModel.setCity_id(mCityCode);
            ArrayList<Integer> cityList = new ArrayList();
            cityList.add(Integer.parseInt(mCityCode));
            apiResourcesModel.setCity_ids(cityList);
            MessageUtils.showMapToast(getResources().getString(R.string.module_map_resource_loading_msg));
            if (mMapSearchList != null) {
                mMapSearchList.clear();
            }
            mPresenter.getMapResourcesList(BaiduMapActivity.this,apiResourcesModel);
            browseHistories();
        }
    }
    //区域标注
    private void addmarkerk(ArrayList<MapCommunityInfoModel> mapSearchlist) {
        ArrayList<MapCommunityInfoModel> mapsearchlist = new ArrayList<>();
        mapsearchlist.addAll(mapSearchlist);
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        if (mKeyWords != null && mKeyWords.length() > 0) {
            LatLng latLng = new LatLng(mapsearchlist.get(0).getLatitude(),mapsearchlist.get(0).getLongitude());
            mMapStatus = new MapStatus.Builder().target(latLng).zoom(17).build();
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
        } else {
            mMapStatus = new MapStatus.Builder().target(mStatusLatLng).zoom(mStatuszoom).build();
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
        }
        for (MapCommunityInfoModel info : mapsearchlist) {
            LatLng mapsearchlatLng = null;
            if (mStatuszoom < 15 || mStatuszoom == 15) {
                if (info != null &&
                        info.getLatitude() != null &&
                        info.getLongitude() != null) {
                    mapsearchlatLng = new LatLng(info.getLatitude(),info.getLongitude());
                } else {
                    continue;
                }
            } else {
                if (info != null &&
                        info.getLat() != null &&
                        info.getLng() != null) {
                    mapsearchlatLng = new LatLng(info.getLat(),info.getLng());
                } else {
                    continue;
                }
            }
            if (!mLatLngBounds.contains(mapsearchlatLng)) {
                continue;
            }
            if (mNearby > -1 && mNearbyInfoModel != null && info.getId() > 0 &&
                    mNearbyInfoModel.getId() > 0 &&
                    info.getId() == mNearbyInfoModel.getId()) {
                overlayOptions = new MarkerOptions().position(mapsearchlatLng)
                        .icon(getNearbyBitmapDescriptor()).zIndex(6);
            } else {
                if (mStatuszoom < 15 || mStatuszoom == 15) {
                    overlayOptions = new MarkerOptions().position(mapsearchlatLng)
                            .icon(getDistrictBitmapDescriptor(info.getCount(),info.getTitle(),
                                    "",mKeyWords,false)).zIndex(4);
                } else if (mStatuszoom > 15) {
                    boolean is_read = false;
                    if (mReadMarkerList != null &&
                            mReadMarkerList.size() > 0) {
                        for (int i = 0; i < mReadMarkerList.size(); i++) {
                            if (mReadMarkerList.get(i).getExtraInfo().get("info") != null) {
                                MapCommunityInfoModel mapCommunityInfoModel = (MapCommunityInfoModel) mReadMarkerList.get(i).getExtraInfo().get("info");
                                if (mapCommunityInfoModel.getId() == info.getId()) {
                                    is_read = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (map_hide_relativelayout.getVisibility() == View.VISIBLE) {
                        MapCommunityInfoModel mapCommunityInfoModel = (MapCommunityInfoModel) mClickedMarker.getExtraInfo().get("info");
                        if (info.getId() == mapCommunityInfoModel.getId()) {
                            overlayOptions = new MarkerOptions().position(mapsearchlatLng)
                                    .icon(getBitmapDescriptor(false,"",
                                            info.getDisplay_price(),mKeyWords,true)).zIndex(5);
                        } else {
                            overlayOptions = new MarkerOptions().position(mapsearchlatLng)
                                    .icon(getBitmapDescriptor(is_read,"",
                                            info.getDisplay_price(),mKeyWords,false)).zIndex(5);
                        }
                    } else {
                        overlayOptions = new MarkerOptions().position(mapsearchlatLng)
                                .icon(getBitmapDescriptor(is_read,"",
                                        info.getDisplay_price(),mKeyWords,false)).zIndex(5);
                    }
                }
            }
            //在地图上添加Marker，并显示
            marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
            final Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }
    }
    //点聚合显示
//    public void addClusterMarkers(ArrayList<MapSearchModel> mapSearchlist) {
//        ArrayList<MapSearchModel> mapsearchlist = new ArrayList<>();
//        mapsearchlist.addAll(mapSearchlist);
//        if (mResourceMapModelList != null) {
//            mResourceMapModelList.clear();
//        }
//        for (MapSearchModel info : mapsearchlist) {
//            LatLng mapsearchlatLng = new LatLng(info.getLatitude(),info.getLongitude());
//            ResourceMapModel resourceMapModel = new ResourceMapModel(mapsearchlatLng,this);
//            resourceMapModel.setTitle(info.getTitle());
//            resourceMapModel.setAddress(info.getAddress());
//            resourceMapModel.setDo_location(info.getDo_location());
//            resourceMapModel.setPreferential_price(info.getPreferential_price());
//            resourceMapModel.setCount(info.getCount());
//            resourceMapModel.setResource_id(info.getResource_id());
//            resourceMapModel.setImage(info.getImage());
//            resourceMapModel.setNumber_of_people(info.getNumber_of_people());
//            resourceMapModel.setLatitude(info.getLatitude());
//            resourceMapModel.setLongitude(info.getLongitude());
//            resourceMapModel.setOrder_count(info.getOrder_count());
//            resourceMapModel.setUnit(info.getUnit());
//            mResourceMapModelList.add(resourceMapModel);
//        }
//        mClusterManager.clearItems();
//        mClusterManager.addItems(mResourceMapModelList);
//        mMapStatus = new MapStatus.Builder().target(mStatusLatLng).zoom(mStatuszoom).build();
//        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
//        mClusterManager.change_status(mMapStatus);
//        if (mBooleanSearchMapAddressBack && mSearchAddressBackMap != null &&
//                mSearchAddressBackMap.get("location") != null) {
//            MapSearchModel mapSearchModel = new MapSearchModel();
//            mapSearchModel.setLatitude(Double.parseDouble(mSearchAddressBackMap.get("latitude").toString()));
//            mapSearchModel.setLongitude(Double.parseDouble(mSearchAddressBackMap.get("longitude").toString()));
//            mapSearchModel.setTitle((String) mSearchAddressBackMap.get("name").toString());
//            mapSearchModel.setAddress((String) mSearchAddressBackMap.get("address").toString());
//            OverlayOptions overlayOptions = null;
//            overlayOptions = new MarkerOptions().position(new LatLng(Double.parseDouble(mSearchAddressBackMap.get("latitude").toString()),
//                    Double.parseDouble(mSearchAddressBackMap.get("longitude").toString())))
//                    .icon(BitmapDescriptorFactory
//                            .fromResource(R.drawable.ic_map_select)).zIndex(5);
//            //在地图上添加Marker，并显示
//            mMapLocationMarker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
//            final Bundle bundle = new Bundle();
//            bundle.putSerializable("info", mapSearchModel);
//            mMapLocationMarker.setExtraInfo(bundle);
//        }
//
//    }
    public BitmapDescriptor getBitmapDescriptor(boolean is_read,String title,String preferential_price,String keywords,boolean clicked) {
        View view;
        view = LayoutInflater.from(this).inflate(R.layout.activity_baidumap_resourcemap_marker, null);
        TextView textView = (TextView) view.findViewById(R.id.resourcemap_marker_textview);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.resourcemap_marker_layout);
        if (keywords != null && keywords.length() > 0) {
            linearLayout.setBackgroundResource(R.drawable.click_marker_redbg);
        } else {
            if (clicked) {
                linearLayout.setBackgroundResource(R.drawable.click_marker_redbg);
            } else {
                if (is_read) {
                    linearLayout.setBackgroundResource(R.drawable.ic_gray_three_six_one);
                } else {
                    linearLayout.setBackgroundResource(R.drawable.click_marker_bg);
                }
            }
        }
        if (title != null && title.length() > 0) {
            textView.setText(title);
        } else {
            if (preferential_price != null && preferential_price.length() > 0) {
                if (preferential_price.equals(getResources().getString(R.string.enquary_order_discuss_personally_tv_str))) {
                    textView.setText(preferential_price);
                } else {
                    textView.setText(getResources().getString(R.string.order_listitem_price_unit_text)+
                            Constants.getpricestring(preferential_price,1));
                }
            }
        }
        return BitmapDescriptorFactory
                .fromView(view);
    }
    public BitmapDescriptor getDistrictBitmapDescriptor(int count,String title,String preferential_price,String keywords,boolean clicked) {
        View view;
        view = LayoutInflater.from(this).inflate(R.layout.activity_baidumap_resourcemap_marker, null);
        TextView textView = (TextView) view.findViewById(R.id.resourcemap_marker_textview);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.resourcemap_marker_layout);
        if (count > 0) {
            linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_range_threesixone));
            if (mStatuszoom > 13) {
                textView.setText(String.valueOf(count));
            } else {
                if (title != null && title.length() > 0) {
                    textView.setText(title+ "\n" + String.valueOf(count));
                } else {
                    textView.setText(String.valueOf(count));
                }
            }
        }
        return BitmapDescriptorFactory
                .fromView(view);
    }

    public BitmapDescriptor getNearbyBitmapDescriptor() {
        View view;
        view = LayoutInflater.from(this).inflate(R.layout.activity_baidumap_resourcemap_marker, null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.resourcemap_marker_layout);
        linearLayout.setBackgroundResource(R.drawable.ic_site_one_two_four_one);
        return BitmapDescriptorFactory
                .fromView(view);
    }

    private void call_cancel() {
        for (int i = 0; i < mCallsList.size(); i++) {
            if (!mCallsList.get(i).isCanceled()) {
                mCallsList.get(i).cancel();
            }
        }
    }
    private boolean isunacncel() {
        boolean uncancel = true;
        for (int i = 0; i < mCallsList.size(); i++) {
            if (!mCallsList.get(i).isCanceled()) {
                uncancel = false;
                break;
            }
        }
        return uncancel;
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mMapLocationClient.setLocOption(option);
        mMapLocationClient.start();
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if(requestCode == Constants.PermissionRequestCode) {
                //定位
                mMapLocationClient = new LocationClient(BaiduMapActivity.this);
                mMapLocationClient.registerLocationListener(new MyMapLocationListener());//注册定位监听接口
                initLocation();
            } else if (requestCode == SERVECE_CODE) {
                MQConfig.init(BaiduMapActivity.this, com.linhuiba.linhuipublic.config.Config.MQAppkey, new OnInitCallback() {
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
                            Intent intent = new MQIntentBuilder(BaiduMapActivity.this)
                                    .setClientInfo(clientInfo)
                                    .setCustomizedId(LoginManager.getUid())
                                    .build();
                            startActivityForResult(intent,10);
                        } else {
                            Intent intent = new MQIntentBuilder(BaiduMapActivity.this)
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
            if(requestCode == Constants.PermissionRequestCode || requestCode == SERVECE_CODE) {
                MessageUtils.showToast(getResources().getString(R.string.permission_message_permission_failed));
            }
        }
    };

    @Override
    public void onOtherPhyResSuccess(ArrayList<MapCommunityInfoModel> list) {
        if (mOtherPhyList != null) {
            mOtherPhyList.clear();
        }
        if (mMapOtherPhyAdapter == null) {
            mOtherPhyList = list;
        } else {
            mOtherPhyList.addAll(list);
        }
        if (mOtherPhyList != null && mOtherPhyList.size() > 0) {
            mMapOtherPhyListLL.setVisibility(View.VISIBLE);
            mMapOtherPhyNullListLL.setVisibility(View.GONE);
            if (mMapOtherPhyAdapter == null) {
                mMapOtherPhyAdapter = new MapOtherPhyAdapter(this,this,
                        R.layout.module_recycle_item_map_other_phy,mOtherPhyList, 1);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mMapOtherPhyRV.setLayoutManager(linearLayoutManager);
                mMapOtherPhyRV.setAdapter(mMapOtherPhyAdapter);
                mMapOtherPhyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        Intent fieldinfo = new Intent(BaiduMapActivity.this, FieldInfoActivity.class);
                        fieldinfo.putExtra("fieldId", String.valueOf(mOtherPhyList.get(position).getId()));
                        fieldinfo.putExtra("community_id", mOtherPhyList.get(position).getCommunity_id());
                        startActivity(fieldinfo);
                    }
                });
            } else {
                mMapOtherPhyAdapter.notifyDataSetChanged();
            }
        } else {
            mMapOtherPhyListLL.setVisibility(View.GONE);
            mMapOtherPhyNullListLL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onOtherPhyResFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
        mMapOtherPhyListLL.setVisibility(View.GONE);
        mMapOtherPhyNullListLL.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMapSearchSuccess(MapCommunitySearchListModel model,Response response) {
        if (check_input() && map_hide_relativelayout.getVisibility() == View.GONE) {
            mmap_search_close_search_condition_layout.setVisibility(View.VISIBLE);
        } else {
            mmap_search_close_search_condition_layout.setVisibility(View.GONE);
        }
        if (model != null && model.getCommunities() != null &&
                model.getCommunities().size() > 0) {
            mMapSearchList = model.getCommunities();
            if (mMapSearchList.size() > 0) {
                if (mNearby > -1) {
                    tv_resource_map_nearby_search_name.setVisibility(View.VISIBLE);
                    tv_resource_map_search_size_remiand.setText(getResources().getString(R.string.map_search_nearby_size_remind_second_str));
                    mresource_map_search_size_textview.setText(String.valueOf(response.total));
                } else {
                    tv_resource_map_nearby_search_name.setVisibility(View.GONE);
                    if (mStatuszoom < 15) {
                        tv_resource_map_search_size_remiand.setText(getResources().getString(R.string.map_search_size_remind_search_result_str));
                    } else {
                        tv_resource_map_search_size_remiand.setText(getResources().getString(R.string.map_search_size_remind_first_str));
                    }                            mresource_map_search_size_textview.setText(String.valueOf(response.total));
                }
                mresource_map_search_size_layout.setVisibility(View.VISIBLE);
                boolean isAddMark = false;
                if ((mStationChoseList != null &&
                        mStationChoseList.size() > 0) || (
                        model.getTrading_areas() != null &&
                                model.getTrading_areas().size() > 0)) {
                    if (mStatuszoom > 14 || mStatuszoom == 14) {
                        isAddMark = true;
                    } else {
                        isAddMark = false;
                    }
                } else {
                    isAddMark = true;
                }
                if (isAddMark) {
                    if (mMapSearchList != null && mMapSearchList.size() > 0) {
                        if (mMapSearchList.size() > 100) {
                            final int part = mMapSearchList.size() / 100;//分批数
                            List<List<MapCommunityInfoModel>> mMapSearchListList = getMoreMarketList(mMapSearchList,part);
                            for (int i = 0; i < part; i++) {
                                final List<MapCommunityInfoModel> tmp = new ArrayList<>();
                                tmp.addAll(mMapSearchListList.get(i));
                                new Thread(new Runnable() {
                                    public void run() {
                                        addmarkerk((ArrayList<MapCommunityInfoModel>) tmp);
                                    }
                                }).start();
                            }
                        } else {
                            new Thread(new Runnable() {
                                public void run() {
                                    addmarkerk(mMapSearchList);
                                }
                            }).start();
                        }
                    }
                }
            } else {
                mKeyWords = "";
                if (mNearby > -1) {
                    tv_resource_map_nearby_search_name.setVisibility(View.VISIBLE);
                    tv_resource_map_search_size_remiand.setText(getResources().getString(R.string.map_search_nearby_size_remind_second_str));
                    mresource_map_search_size_textview.setText("0");
                } else {
                    tv_resource_map_nearby_search_name.setVisibility(View.GONE);
                    if (mStatuszoom < 15) {
                        tv_resource_map_search_size_remiand.setText(getResources().getString(R.string.map_search_size_remind_search_result_str));
                    } else {
                        tv_resource_map_search_size_remiand.setText(getResources().getString(R.string.map_search_size_remind_first_str));
                    }
                    mresource_map_search_size_textview.setText("0");
                }
                mresource_map_search_size_layout.setVisibility(View.VISIBLE);
            }
            if (model.getTrading_areas() != null && model.getTrading_areas().size() > 0) {
                for (int i = 0; i < model.getTrading_areas().size(); i++) {
                    if (model.getTrading_areas().get(i).getName() != null &&
                            model.getTrading_areas().get(i).getName().length() > 0 &&
                            model.getTrading_areas().get(i).getLng() != null &&
                            model.getTrading_areas().get(i).getLat() != null &&
                            model.getTrading_areas().get(i).getMinLat() != null &&
                            model.getTrading_areas().get(i).getMinLng() != null &&
                            model.getTrading_areas().get(i).getMaxLat() != null &&
                            model.getTrading_areas().get(i).getMaxLng() != null) {
                        drawCircle(new LatLng(model.getTrading_areas().get(i).getLat(),
                                        model.getTrading_areas().get(i).getLng()),
                                Integer.parseInt(Constants.getorderdoublepricestring(DistanceUtil.getDistance(new LatLng(model.getTrading_areas().get(i).getMinLat(),
                                                model.getTrading_areas().get(i).getMinLng()),
                                        new LatLng(model.getTrading_areas().get(i).getMaxLat(),
                                                model.getTrading_areas().get(i).getMaxLng())) * 0.5
                                        , 1)),false);
                        OverlayOptions overlayOptions = new MarkerOptions().position(new LatLng(model.getTrading_areas().get(i).getLat(),
                                model.getTrading_areas().get(i).getLng()))
                                .icon(getBitmapDescriptor(false,model.getTrading_areas().get(i).getName(),
                                        "","",true)).zIndex(6);
                        mBaiduMap.addOverlay(overlayOptions);
                    }
                }
            }
        } else {
            if (mNearby > -1) {
                tv_resource_map_nearby_search_name.setVisibility(View.VISIBLE);
                tv_resource_map_search_size_remiand.setText(getResources().getString(R.string.map_search_nearby_size_remind_second_str));
                mresource_map_search_size_textview.setText("0");

            } else {
                tv_resource_map_nearby_search_name.setVisibility(View.GONE);
                if (mStatuszoom < 15) {
                    tv_resource_map_search_size_remiand.setText(getResources().getString(R.string.map_search_size_remind_search_result_str));
                } else {
                    tv_resource_map_search_size_remiand.setText(getResources().getString(R.string.map_search_size_remind_first_str));
                }
                mresource_map_search_size_textview.setText("0");
            }
            mresource_map_search_size_layout.setVisibility(View.VISIBLE);
            mKeyWords = "";
        }
        if (mNearby > -1) {
            if (mNearByOptions != null) {
                mNearByOptions.remove();
            }
            drawCircle(mNearByLatLng,mNearby,true);
            double lat = 0;
            double lng = 0;
            LonLatClass lonLatClass = new LonLatClass(lat,lng);
            lonLatClass.computerThatLonLat(mNearByLatLng.longitude,
                    mNearByLatLng.latitude,90,mNearby);
            mNearByPoint = mBaiduMap.getProjection().toScreenLocation(new LatLng(lonLatClass.latitude,lonLatClass.longitude));
            mNearByPoint.x = mNearByPoint.x -
                    com.linhuiba.linhuifield.connector.Constants.Dp2Px(BaiduMapActivity.this,25);
            setLayout(mMapNearbyImgv,mNearByPoint.x,mNearByPoint.y);
            mMapNearbyImgv.setVisibility(View.VISIBLE);
            mMapNearbyTV.setText(String.valueOf(mNearby) + "m");
        }
        if (mStationChoseList != null &&
                mStationChoseList.size() > 0) {
            for (int i = 0; i < mStationChoseList.size(); i++) {
                if (mStationChoseList.get(i).getLat() > 0 &&
                        mStationChoseList.get(i).getLng() > 0 &&
                        mStationChoseList.get(i).getStation_name() != null &&
                        mStationChoseList.get(i).getStation_name().length() > 0) {
                    OverlayOptions overlayOptions = new MarkerOptions().position(new LatLng(mStationChoseList.get(i).getLat(),
                            mStationChoseList.get(i).getLng()))
                            .icon(getBitmapDescriptor(false,mStationChoseList.get(i).getStation_name() +
                                    getResources().getString(R.string.module_map_station_str),
                                    "","",true)).zIndex(6);
                    mBaiduMap.addOverlay(overlayOptions);
                    drawCircle(new LatLng(mStationChoseList.get(i).getLat(),
                            mStationChoseList.get(i).getLng()),1500,false);
                }
            }
        }
        if (isChooseDistricts) {
            if (mChooseDistrictList != null && mCityName != null &&
                    mChooseDistrictList.size() > 0 &&  mCityName.length() > 0) {
                for (int i = 0; i < mChooseDistrictList.size(); i++) {
                    getAreaRange(mCityName,mChooseDistrictList.get(i));
                }
            }
        }

    }

    @Override
    public void onMapSearchFailure(boolean superresult, Throwable error) {
        if (check_input()) {
            mmap_search_close_search_condition_layout.setVisibility(View.VISIBLE);
        } else {
            mmap_search_close_search_condition_layout.setVisibility(View.GONE);
        }
        if (mNearby > -1) {
            tv_resource_map_nearby_search_name.setVisibility(View.VISIBLE);
            tv_resource_map_search_size_remiand.setText(getResources().getString(R.string.map_search_nearby_size_remind_second_str));
            mresource_map_search_size_textview.setText("0");

        } else {
            tv_resource_map_nearby_search_name.setVisibility(View.GONE);
            if (mStatuszoom < 15) {
                tv_resource_map_search_size_remiand.setText(getResources().getString(R.string.map_search_size_remind_search_result_str));
            } else {
                tv_resource_map_search_size_remiand.setText(getResources().getString(R.string.map_search_size_remind_first_str));
            }
            mresource_map_search_size_textview.setText("0");
        }
        mresource_map_search_size_layout.setVisibility(View.VISIBLE);
        mKeyWords = "";
    }

    @Override
    public void onAttributesSuccess(ArrayList<SearchListAttributesModel> list) {
        hideProgressDialog();
        if (list != null && list.size() > 0) {
            mAttributesList = list;
            apiResourcesModel.setAttributes(null);
            setResScreeningAdapter();
        }
    }

    @Override
    public void onAttributesFailure(boolean superresult, Throwable error) {
        if (mAttributesList != null) {
            mAttributesList.clear();
        }
        setResScreeningAdapter();
    }

    public class MyMapLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            mLocationLat = location.getLatitude();
            mLocationLng = location.getLongitude();
            if (!isLocationClick) {
                return;
            }
            mBaiduMap.setMyLocationEnabled(true);
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            MyLocationConfiguration configuration
                    = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,true,null);
            //设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效，参见 setMyLocationEnabled(boolean)
            mBaiduMap.setMyLocationConfiguration(configuration);

            mStatusLatLng = new LatLng(location.getLatitude(),location.getLongitude());
            mStatuszoom = 18;
            mMapStatus = new MapStatus.Builder().target(mStatusLatLng).zoom(mStatuszoom).build();
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
        }
    }
    private void getCityMap() {
        if (mMapCityList != null) {
            mMapCityList.clear();
        }
        List<ConfigCityParameterModel> cities = ConfigSqlOperation.selectSQL(6,0,BaiduMapActivity.this);
        if (cities != null &&
                cities.size() > 0) {
            for (int i = 0; i < cities.size(); i++) {
                String key = String.valueOf(cities.get(i).getCity_id());
                String value = cities.get(i).getCity();
                Field_AddResourceCreateItemModel field_addResourceCreateItemModel = new Field_AddResourceCreateItemModel();
                field_addResourceCreateItemModel.setId(Integer.parseInt(key));
                field_addResourceCreateItemModel.setName(value);
                mMapCityList.add(field_addResourceCreateItemModel);
                if (key.equals(mCityCode)) {
                    if (value != null && value.length() > 0) {
                        if (!mBooleanSearchMapAddressBack) {
                            mCityName = value;
                            mSearchAddressCityName = value;
                        }
                    }
                }
            }
        } else {
            ArrayList<ConfigCitiesModel> citylist = ConfigurationsModel.getInstance().getCitylist();
            if (citylist != null && citylist.size() > 0) {
                for (int i = 0; i < citylist.size(); i++) {
                    String key = String.valueOf(citylist.get(i).getCity_id());
                    String value = citylist.get(i).getCity();
                    Field_AddResourceCreateItemModel field_addResourceCreateItemModel = new Field_AddResourceCreateItemModel();
                    field_addResourceCreateItemModel.setId(Integer.parseInt(key));
                    field_addResourceCreateItemModel.setName(value);
                    mMapCityList.add(field_addResourceCreateItemModel);
                    if (key.equals(mCityCode)) {
                        if (value != null && value.length() > 0) {
                            if (!mBooleanSearchMapAddressBack) {
                                mCityName = value;
                                mSearchAddressCityName = value;
                            }
                        }
                    }
                }
            }
        }
    }
    //判断是否显示清除搜索条件
    private boolean check_input() {
        boolean check = false;
        if (mKeyWords.length() > 0) {
            return true;
        }
        if (mNearby > -1) {
            return true;
        }
        if (mBooleanSearchMapAddressBack && mMapLocationMarker != null) {
            return true;
        }
        if (((apiResourcesModel.getSubway_station_ids() != null &&
                apiResourcesModel.getSubway_station_ids().size() > 0) ||
                (apiResourcesModel.getDistrict_ids() != null &&
                        apiResourcesModel.getDistrict_ids().size() > 0) ||
                (apiResourcesModel.getTrading_area_ids() != null &&
                        apiResourcesModel.getTrading_area_ids().size() > 0))) {
            return true;
        }
//        if ((apiResourcesModel.getMinimum_areas() != null && apiResourcesModel.getMinimum_area().length() > 0) ||
//                (apiResourcesModel.getMaximum_area() != null && apiResourcesModel.getMaximum_area().length() > 0) ||
//                (apiResourcesModel.getMaximum_peoples() != null && apiResourcesModel.getMaximum_peoples().length() > 0)
//                || (apiResourcesModel.getMinimum_peoples() != null && apiResourcesModel.getMinimum_peoples().length() > 0)
//                || (apiResourcesModel.getMinimum_build_year() != null && apiResourcesModel.getMinimum_build_year().length() > 0)
//                || (apiResourcesModel.getMaximum_build_year() != null && apiResourcesModel.getMaximum_build_year().length() > 0)
//                || (apiResourcesModel.getHighPrice() != null && apiResourcesModel.getHighPrice().length() > 0)
//                || (apiResourcesModel.getLowPrice() != null && apiResourcesModel.getLowPrice().length() > 0) ||
//                (apiResourcesModel.getMinimum_households() != null && apiResourcesModel.getMinimum_households().length() > 0)
//                || (apiResourcesModel.getMaximum_households() != null && apiResourcesModel.getMaximum_households().length() > 0) ||
//                (apiResourcesModel.getMinimum_property_costs() != null && apiResourcesModel.getMinimum_property_costs().length() > 0)
//                || (apiResourcesModel.getMaximum_property_costs() != null && apiResourcesModel.getMaximum_property_costs().length() > 0) ||
//                (apiResourcesModel.getMaximum_house_price() != null && apiResourcesModel.getMaximum_house_price().length() > 0)
//                || (apiResourcesModel.getMaximum_house_price() != null && apiResourcesModel.getMaximum_house_price().length() > 0)
//                || (apiResourcesModel.getFacilities() != null && apiResourcesModel.getFacilities().size() > 0)
//                || (apiResourcesModel.getField_type_id() != null && apiResourcesModel.getField_type_id().size() > 0)
//                || (apiResourcesModel.getActivity_type_id() != null && apiResourcesModel.getActivity_type_id().size() > 0)
//                || (apiResourcesModel.getIndoor() != null && apiResourcesModel.getIndoor().size() > 0)
//                || (apiResourcesModel.getLabel_id() != null && apiResourcesModel.getLabel_id().size() > 0)
//                || (apiResourcesModel.getAd_type_id() != null && apiResourcesModel.getAd_type_id().size() > 0)
//                || apiResourcesModel.getHot() == 1 || apiResourcesModel.getSubsidy() == 1) {
//            return true;
//        }

        return check;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭定位
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);
        }
        if(mMapLocationClient != null &&
                mMapLocationClient.isStarted()){
            mMapLocationClient.stop();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mShareBitmap != null) {
            mShareBitmap.recycle();
        }
    }
    @Override
    public void onMapLoaded() {
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if (map_hide_relativelayout.getVisibility() == View.GONE) {
                finish();
            } else {
                map_hide_relativelayout.setVisibility(View.GONE);
                mBaiduMapView.showZoomControls(true);
                if (check_input()) {
                    mmap_search_close_search_condition_layout.setVisibility(View.VISIBLE);
                } else {
                    mmap_search_close_search_condition_layout.setVisibility(View.GONE);
                }
                mbaidumap_location_imageview.setVisibility(View.VISIBLE);
                mMapDemandTV.setVisibility(View.VISIBLE);
                mmap_headrelativelayout.setVisibility(View.VISIBLE);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void getAreaRange(String cityName, String districtsName) {
        OnGetDistricSearchResultListener listener = new OnGetDistricSearchResultListener() {

            @Override
            public void onGetDistrictResult(DistrictResult districtResult) {
                districtResult.getCenterPt();//获取行政区中心坐标点
                districtResult.getCityName();//获取行政区域名称
                List<List<LatLng>> polyLines = districtResult.getPolylines();//获取行政区域边界坐标点
                //边界就是坐标点的集合，在地图上画出来就是多边形图层。有的行政区可能有多个区域，所以会有多个点集合。
                //构建用户绘制多边形的Option对象
                //绘制折线
                if (polyLines != null &&
                        polyLines.size() > 0) {//友盟错误日志修改
                    OverlayOptions Polyline = new PolygonOptions().points(polyLines.get(0))
                            .stroke(new Stroke(4,getResources().getColor(R.color.map_area_line_bg)))
                            .fillColor(getResources().getColor(R.color.map_area_line_fill_bg));
                    //添加在地图中
                    mBaiduMap.addOverlay(Polyline);
                }
//                OverlayOptions ooPolyline = new PolylineOptions().width(10)
//                        .color(0xAAFF0000).points(polyLines.get(0));
//                Polyline　mPolyline =(Polyline) mBaiduMap.addOverlay(ooPolyline);
            }

        };
        DistrictSearch mDistrictSearch = DistrictSearch.newInstance();//初始化行政区检索
        mDistrictSearch.setOnDistrictSearchListener(listener );//设置回调监听
        DistrictSearchOption districtSearchOption = new DistrictSearchOption();
        districtSearchOption.cityName(cityName);//检索城市名称
        districtSearchOption.districtName(districtsName);//检索的区县名称
        mDistrictSearch.searchDistrict(districtSearchOption);//请求行政区数据
    }
    //区域点击弹出pw
    private void showAreaScreenPW () {
        View myView = BaiduMapActivity.this.getLayoutInflater().inflate(R.layout.activity_search_area_screen_pw, null);
        DisplayMetrics metric = new DisplayMetrics();
        BaiduMapActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metric);
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
        RelativeLayout pwAllRL = (RelativeLayout) myView.findViewById(R.id.map_search_pw_rl);
        pwAllRL.setPadding(0, 0, 0,height *452/Config.DESIGN_HEIGHT);
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
                                secondAdapter = new SearchAreaPwAdapter(BaiduMapActivity.this, mSearchAreaList.get(position).getSecondList(),4);
                        } else {
                            secondChooseInt = -1;
                            secondAdapter = new SearchAreaPwAdapter(BaiduMapActivity.this, mSearchAreaList.get(position).getSecondList(),2);
                        }
                        secondLv.setAdapter(secondAdapter);
                        secondLv.setVisibility(View.VISIBLE);
                        if (mSearchAreaList.get(firstChooseInt).getId() == 1) {
                            secondChooseInt = 0;
                            SearchAreaPwAdapter.clearIsThirdChoose();
                            for (int i = 0; i < mSubwayList.get(0).getStations().size(); i++) {
                                SearchAreaPwAdapter.getIsThirdChoose().put(mSubwayList.get(0).getStations().get(i).getId(),false);
                            }
                            thirdAdapter = new SearchAreaPwAdapter(BaiduMapActivity.this, mSubwayList.get(0).getStations(),3);
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
                        thirdAdapter = new SearchAreaPwAdapter(BaiduMapActivity.this, mSubwayList.get(position).getStations(),3);
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
                if (mStationChoseList != null) {
                    mStationChoseList.clear();
                }
                String searchAreaStr = "";
                if (secondChooseInt > -1) {
                    ArrayList<Integer> mSubwayIntList = new ArrayList<>();
                    for (int i = 0; i < mSubwayList.get(secondChooseInt).getStations().size(); i++) {
                        if (SearchAreaPwAdapter.getIsThirdChoose().get(mSubwayList.get(secondChooseInt).getStations().get(i).getId())) {
                            mSubwayIntList.add(mSubwayList.get(secondChooseInt).getStations().get(i).getId());
                            mStationChoseList.add(mSubwayList.get(secondChooseInt).getStations().get(i));
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
                        isChooseDistricts = false;
                    } else {
                        if (chooseAreaType == mSearchAreaList.get(firstChooseInt).getId()) {
                            apiResourcesModel.setSubway_station_ids(null);
                            apiResourcesModel.setDistrict_ids(null);
                            apiResourcesModel.setTrading_area_ids(null);
                            isChooseDistricts = false;
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
                } else {
                    if (mSearchAreaList.get(firstChooseInt).getId() == 0) {
                        ArrayList<Integer> mDistrictIntList = new ArrayList<>();
                        if (mChooseDistrictList != null) {
                            mChooseDistrictList.clear();
                        }
                        for (int i = 0; i < mSearchAreaList.get(firstChooseInt).getSecondList().size(); i++) {
                            if (SearchAreaPwAdapter.getIsSecondChoose().get(mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getId())) {
                                mDistrictIntList.add(mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getId());
                                if (searchAreaStr.length() == 0) {
                                    searchAreaStr = searchAreaStr + mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getName();
                                } else {
                                    searchAreaStr = searchAreaStr + "," +
                                            mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getName();
                                }
                                isChooseDistricts = true;
                                mChooseDistrictList.add(mSearchAreaList.get(firstChooseInt).getSecondList().get(i).getName());
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
                                isChooseDistricts = false;
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
                            isChooseDistricts = false;
                        } else {
                            if (chooseAreaType == mSearchAreaList.get(firstChooseInt).getId()) {
                                apiResourcesModel.setTrading_area_ids(null);
                                apiResourcesModel.setDistrict_ids(null);
                                apiResourcesModel.setSubway_station_ids(null);
                                isChooseDistricts = false;
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
                    } else {

                    }
                }
                //点击了重置按钮操作
                if (isSearchAreaReset) {
                    apiResourcesModel.setSubway_station_ids(null);
                    apiResourcesModel.setDistrict_ids(null);
                    isChooseDistricts = false;
                    apiResourcesModel.setTrading_area_ids(null);
                    isSearchAreaReset = false;
                }
                //2017/12/19 获取数据
                if (apiResourcesModel.getDistrict_ids() != null &&
                        apiResourcesModel.getDistrict_ids().size() > 0) {
                    //选择地区域后
                    getmapresourcesmarks(13);
                } else {
                    if (apiResourcesModel.getSubway_station_ids() != null &&
                            apiResourcesModel.getSubway_station_ids().size() > 0) {
                        mStatuszoom = 12;
                        mMapStatus = new MapStatus.Builder().target(mStatusLatLng).zoom(mStatuszoom).build();
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
                    }
                    getmapresourcesmarks(12);
                }
                mSearchSortPw.dismiss();

            }
        });
        //重置
        mSearchAreaResteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearchAreaReset = true;
                thirdLv.setVisibility(View.GONE);
                showChooseAreaLV(true);
                chooseAreaType = -1;
                mSearchAreaTvStr = msearch_area_type_textview.getText().toString();
                msearch_area_type_textview.setCompoundDrawables(null, null, mSortGreyDownDrawable, null);
                msearch_area_type_textview.setTextColor(getResources().getColor(R.color.headline_tv_color));
                msearch_area_type_textview.setText(getResources().getString(R.string.invoiceinfo_area_txt));
            }
        });
    }
    //区域筛选数据整合
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
                firstAdapter = new SearchAreaPwAdapter(BaiduMapActivity.this, mSearchAreaList,1);
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
                    secondAdapter = new SearchAreaPwAdapter(BaiduMapActivity.this, mSearchAreaList.get(firstChooseInt).getSecondList(),4);
                } else {
                    secondAdapter = new SearchAreaPwAdapter(BaiduMapActivity.this, mSearchAreaList.get(firstChooseInt).getSecondList(),2);
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
                firstAdapter = new SearchAreaPwAdapter(BaiduMapActivity.this, mSearchAreaList,1);
                firstLv.setAdapter(firstAdapter);
                firstLv.setVisibility(View.VISIBLE);
                SearchAreaPwAdapter.clearIsSecondChoose();
                for (int i = 0; i < mSubwayLineList.size(); i++) {
                    SearchAreaPwAdapter.getIsSecondChoose().put(mSubwayLineList.get(i).getId(),false);
                }
                secondAdapter = new SearchAreaPwAdapter(BaiduMapActivity.this, mSubwayLineList,4);
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
                    thirdAdapter = new SearchAreaPwAdapter(BaiduMapActivity.this, mSubwayList.get(mSubWayLinePosition).getStations(),3);
                    thirdLv.setAdapter(thirdAdapter);
                    thirdLv.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    //区域选择显示选择内容状态
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
                            if (mChooseDistrictList != null) {
                                mChooseDistrictList.clear();
                            }
                            for (int j = 0; j < mSearchAreaList.get(firstChooseInt).getSecondList().size(); j++) {
                                if (apiResourcesModel.getDistrict_ids() != null &&
                                        apiResourcesModel.getDistrict_ids().contains(mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getId())) {
                                    if (searchAreaStr.length() == 0) {
                                        searchAreaStr = searchAreaStr + mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getName();
                                    } else {
                                        searchAreaStr = searchAreaStr + "," +
                                                mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getName();
                                    }
                                    isChooseDistricts = true;
                                    mChooseDistrictList.add(mSearchAreaList.get(firstChooseInt).getSecondList().get(j).getName());
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
                        } else if (mSearchAreaList.get(firstChooseInt).getId() == 1) {
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
                                    break ok;
                                }
                            }
                        }
                    }
                }
                if (mSubWayLinePosition > -1) {
                    secondChooseInt = mSubWayLinePosition;
                    String searchAreaStr = "";
                    if (mStationChoseList != null) {
                        mStationChoseList.clear();
                    }
                    for (int i = 0; i < mSubwayList.get(mSubWayLinePosition).getStations().size(); i++) {
                        if (apiResourcesModel.getSubway_station_ids() != null &&
                                apiResourcesModel.getSubway_station_ids().contains(mSubwayList.get(mSubWayLinePosition).getStations().get(i).getId())) {
                            mStationChoseList.add(mSubwayList.get(mSubWayLinePosition).getStations().get(i));
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
    private void getSearchAreaData() {
        if (mCityCode != null && mCityCode.length() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mDistrictList= new ArrayList<>();
                    mSubwayList = new ArrayList<>();
                    mTradingAreasList = new ArrayList<>();
                    field_labels_list = new ArrayList<>();
                    mSubwayLineList = new ArrayList<>();
                    List<ConfigCityParameterModel> districts = ConfigSqlOperation.selectSQL(1,Integer.parseInt(mCityCode),BaiduMapActivity.this);
                    List<ConfigCityParameterModel> labels = ConfigSqlOperation.selectSQL(2,Integer.parseInt(mCityCode),BaiduMapActivity.this);
                    List<ConfigCityParameterModel> trading_areas = ConfigSqlOperation.selectSQL(3,Integer.parseInt(mCityCode),BaiduMapActivity.this);
                    List<ConfigCityParameterModel> subway_stations = ConfigSqlOperation.selectSQL(4,Integer.parseInt(mCityCode),BaiduMapActivity.this);
                    List<ConfigCityParameterModel> districtsList = new ArrayList<>();
                    List<ConfigCityParameterModel> labelsList = new ArrayList<>();
                    List<ConfigCityParameterModel> trading_areasList = new ArrayList<>();
                    List<ConfigCityParameterModel> subway_stationsList = new ArrayList<>();
                    ArrayList<ConfigCitiesModel> citylist = ConfigurationsModel.getInstance().getCitylist();
                    int defaultCityInt = -1;
                    if (citylist != null && citylist.size() > 0) {
                        for (int i = 0; i < citylist.size(); i++) {
                            if (String.valueOf(citylist.get(i).getCity_id()).equals(mCityCode)) {
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
                                        searchAreaPwModel.setLat(subway_stationsList.get(i).getStations().get(j).getLat());
                                        searchAreaPwModel.setLng(subway_stationsList.get(i).getStations().get(j).getLng());
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
                                List<ConfigCityParameterModel> stationsList = ConfigSqlOperation.selectSQL(5,subway_stationsList.get(i).getSubway_line_id(),BaiduMapActivity.this);
                                if (stationsList != null &&
                                        stationsList.size() > 0) {
                                    ArrayList<SearchAreaPwModel> stations = new ArrayList<>();
                                    for (int j = 0; j < stationsList.size(); j++) {
                                        SearchAreaPwModel searchAreaPwModel = new SearchAreaPwModel();
                                        searchAreaPwModel.setId(stationsList.get(j).getId());
                                        searchAreaPwModel.setName(stationsList.get(j).getName());
                                        searchAreaPwModel.setLat(stationsList.get(j).getLat());
                                        searchAreaPwModel.setLng(stationsList.get(j).getLng());
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
    }
    private void showResourceScreeningPw() {
        field_labels_int = 0;
        category_id_int = 0;
        attributesChooseMap= new HashMap<>();
        View myView = BaiduMapActivity.this.getLayoutInflater().inflate(R.layout.activity_resourcesscreening, null);
        mresourcesscreening_stickygridview_new  = (ListView)myView.findViewById(R.id.resourcesscreening_stickygridview_new);
        Button mresetbtn = (Button)myView.findViewById(R.id.resetbtn);
        Button mconfirmbtn = (Button)myView.findViewById(R.id.confirmbtn);
        DisplayMetrics metric = new DisplayMetrics();
        BaiduMapActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;

        int width_screening = metric.widthPixels;   // 屏幕宽度（像素）
        if ((width_screening -40)/4 > 174 ||(width_screening -40)/4 == 174 || (width_screening -40)/4 > 166) {
            GridviewNumColumns = 4;
        } else  {
            GridviewNumColumns = 3;
        }
        setResScreeningAdapter();
        mconfirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭软键盘
                InputMethodManager imm = (InputMethodManager) BaiduMapActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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
                        String minPrice = "";
                        String maxPrice = "";
                        String min_person_flow_edit = "";
                        String max_person_flow_edit = "";
                        ArrayList<Integer> fieldlabel_list = new ArrayList<>();
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
                                } else if (data_new.get(j).get("type").toString().equals("price")) {
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
                                    minPrice = ResourcesScreeningNewAdapter.getedittextmap().get("pricemin").toString();
                                    maxPrice = ResourcesScreeningNewAdapter.getedittextmap().get("pricemax").toString();
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
                        if (minPrice.length() > 0) {
                            apiResourcesModel.setMin_price(Constants.getpricestring(minPrice,100));
                        } else {
                            apiResourcesModel.setMin_price(null);
                        }
                        if (maxPrice.length() > 0) {
                            apiResourcesModel.setMax_price(Constants.getpricestring(maxPrice,100));
                        } else {
                            apiResourcesModel.setMax_price(null);
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
                        if (apiResourcesModel.getMin_area() != null || apiResourcesModel.getMax_area() != null ||
                                apiResourcesModel.getMin_price() != null  || apiResourcesModel.getMax_price() != null ||
                                apiResourcesModel.getMin_person_flow() != null  || apiResourcesModel.getMax_person_flow() != null
                                || apiResourcesModel.getLabel_ids().size() > 0
                                || apiResourcesModel.getCommunity_type_ids().size() > 0
                                || apiResourcesModel.getAttributes().size() > 0) {
                            mscreening_txt.setTextColor(getResources().getColor(R.color.default_bluebg));
                            Drawable drawable_add_pressed = getResources().getDrawable(R.drawable.ic_screening_select);
                            drawable_add_pressed.setBounds(0, 0, drawable_add_pressed.getMinimumWidth(), drawable_add_pressed.getMinimumHeight());
                            mscreening_txt.setCompoundDrawables(null, null, drawable_add_pressed, null);
                        } else {
                            Drawable drawable_add_pressed = getResources().getDrawable(R.drawable.ic_screening);
                            drawable_add_pressed.setBounds(0, 0, drawable_add_pressed.getMinimumWidth(), drawable_add_pressed.getMinimumHeight());
                            mscreening_txt.setCompoundDrawables(null, null, drawable_add_pressed, null);
                            mscreening_txt.setTextColor(getResources().getColor(R.color.headline_tv_color));
                        }
                        //2017/12/19 获取数据
                        getmapresourcesmarks(0);
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
    private void getScreenData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mCategoryMap = new HashMap<>();
                mCategoryList = new ArrayList<>();
                mAttributesMap = new HashMap<>();
                mAttributesList = new ArrayList<>();
                String categoryStr = LoginManager.getCommunity_type();
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
            }
        }).start();

    }
    private void showScreeningState() {
        if ((apiResourcesModel.getMin_area() != null && apiResourcesModel.getMin_area().length() > 0) ||
                (apiResourcesModel.getMax_area() != null && apiResourcesModel.getMax_area().length() > 0) ||
                (apiResourcesModel.getMin_price() != null && apiResourcesModel.getMin_price().length() > 0) ||
        (apiResourcesModel.getMax_price() != null && apiResourcesModel.getMax_price().length() > 0) ||
        (apiResourcesModel.getMin_person_flow() != null && apiResourcesModel.getMin_person_flow().length() > 0) ||
                (apiResourcesModel.getMax_person_flow() != null && apiResourcesModel.getMax_person_flow().length() > 0)
                || (apiResourcesModel.getLabel_ids() != null && apiResourcesModel.getLabel_ids().size() > 0)
                || (apiResourcesModel.getCommunity_type_ids() != null && apiResourcesModel.getCommunity_type_ids().size() > 0)
                || (apiResourcesModel.getAttributes() != null && apiResourcesModel.getAttributes().size() > 0)) {
            mscreening_txt.setTextColor(getResources().getColor(R.color.default_bluebg));
            Drawable drawable_add_pressed = getResources().getDrawable(R.drawable.ic_screening_select);
            drawable_add_pressed.setBounds(0, 0, drawable_add_pressed.getMinimumWidth(), drawable_add_pressed.getMinimumHeight());
            mscreening_txt.setCompoundDrawables(null, null, drawable_add_pressed, null);
        }
    }
    public static <T> List<List<T>> getMoreMarketList(List<T> source,int n){
        List<List<T>> result=new ArrayList<List<T>>();
        int remaider=source.size()%n;  //(先计算出余数)
        int number=source.size()/n;  //然后是商
        int offset=0;//偏移量
        for(int i=0;i<n;i++){
            List<T> value=null;
            if(remaider>0){
                value=source.subList(i*number+offset, (i+1)*number+offset+1);
                remaider--;
                offset++;
            }else{
                value=source.subList(i*number+offset, (i+1)*number+offset);
            }
            result.add(value);
        }
        return result;
    }
    //初始化页面的处理
    private Handler mInitViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showScreeningState();
                    showChooseAreaState();
                    mClusterManager = new ClusterManager<ResourceMapModel>(BaiduMapActivity.this, mBaiduMap);
                    getmapresourcesmarks(0);
                    mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
                    mClusterManager.setHandler(mHandler, MAP_STATUS_CHANGE);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
                    break;
                default:
                    break;
            }
        }
    };
    private void drawCircle(LatLng ll, int mRadius, boolean is_near_by) {
        if (mRadius < 0) {
            mRadius = mRadius * -1;
        }
        //画圆，主要是这里
        OverlayOptions ooCircle = new CircleOptions().fillColor(getResources().getColor(R.color.module_map_marker_fill_color))
                .center(ll).stroke(new Stroke(3, getResources().getColor(R.color.default_bluebg)))
                .radius(mRadius);
        if (is_near_by) {
            mNearByOptions = mBaiduMap.addOverlay(ooCircle);
        } else {
            mBaiduMap.addOverlay(ooCircle);
        }

//        OverlayOptions overlayOptions = null;
//        overlayOptions = new MarkerOptions().position(ll)
//                .icon(BitmapDescriptorFactory
//                        .fromResource(R.drawable.ic_map_select)).zIndex(9);
//        //在地图上添加Marker，并显示
//        mMapLocationMarker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
//        mMapLocationMarker.setDraggable(true);
    }
    public static void setLayout(View view,int x,int y) {
        //                    mBaiduMap.getProjection().fromScreenLocation(point);
//                    mBaiduMap.getProjection().toScreenLocation(latlng);
        RelativeLayout.MarginLayoutParams margin=new RelativeLayout.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x,y, 0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }
    private void showMapMarkerCommunityInfo(final MapCommunityInfoModel info) {
        map_hide_relativelayout.setVisibility(View.VISIBLE);
        mBaiduMapView.showZoomControls(false);
        mmap_search_close_search_condition_layout.setVisibility(View.GONE);
        mbaidumap_location_imageview.setVisibility(View.GONE);
        mMapDemandTV.setVisibility(View.GONE);
        mmap_headrelativelayout.setVisibility(View.GONE);
        mMapSearchLocationLL.setVisibility(View.GONE);
        mMapResourceInfoRL.setVisibility(View.VISIBLE);
        if (info.getName() != null) {
            mapsearch_resourcename.setText(info.getName());
        }
        if (info.getCommunity_img() != null) {
            if (info.getCommunity_img().length() > 0) {
                Picasso.with(BaiduMapActivity.this).load(info.getCommunity_img()+Config.Linhui_Min_Watermark).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).resize(180, 180).into(map_searchadvertising_img);
            } else {
                Picasso.with(BaiduMapActivity.this).load(R.drawable.ic_no_pic_small).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(map_searchadvertising_img);
            }
        } else {
            Picasso.with(BaiduMapActivity.this).load(R.drawable.ic_no_pic_small).placeholder(R.drawable.ic_jiazai_small).error(R.drawable.ic_no_pic_small).into(map_searchadvertising_img);
        }
        mmap_search_address.setText(getResources().getString(R.string.field_list_item_address) +
                info.getDetailed_address());
        String label = "";
        if (info.getCommunity_type() != null && info.getCommunity_type().getDisplay_name() != null &&
                info.getCommunity_type().getDisplay_name().length() > 0) {
            label = label + info.getCommunity_type().getDisplay_name();
        }
        if (info.getBuild_year() != null  &&
                info.getBuild_year().length() > 0) {
            if (label.length() > 0) {
                label = label + " " + "|" + " ";
            }
            label = label + info.getBuild_year() +
                    getResources().getString(R.string.module_map_buile_year_hint);
        }
        if (info.getHas_carport() == 1) {
            if (label.length() > 0) {
                label = label + " " + "|" + " ";
            }
            label = label +
                    getResources().getString(R.string.fieldinfo_carport_text);
        }
        mMapCommunityTypeTV.setText(label);
        if (info.getLat() != null &&
                info.getLng() != null &&
                mLocationLat > 0 &&
                mLocationLng > 0) {
            int distance = Integer.parseInt(Constants.getorderdoublepricestring(DistanceUtil.getDistance(new LatLng(mLocationLat,mLocationLng)
                    , new LatLng(info.getLat(),info.getLng())),1));
            mMapDistanceTV.setText(Constants.getpricestring(String.valueOf(distance),0.001) + "km"
            );
        }
        if (info.getPhysical_resources_count() > 0) {
            //其他展位请求
            mPresenter.getCommunityAllPhyRes(info.getId());
        } else {
            mMapOtherPhyNullListLL.setVisibility(View.VISIBLE);
            mMapOtherPhyListLL.setVisibility(View.GONE);
        }
        if (mNearby > -1) {
            mresource_map_search_look_fim_textview.setText(String.valueOf(mNearby));
        } else {
            if (mresource_map_search_look_fim_textview.getText().toString().length() == 0) {
                mresource_map_search_look_fim_textview.setText(String.valueOf(defaultNearBy));
            }
        }
        mMapResourceInfoRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map_hide_relativelayout.setVisibility(View.GONE);
                mBaiduMapView.showZoomControls(true);
                if (check_input()) {
                    mmap_search_close_search_condition_layout.setVisibility(View.VISIBLE);
                } else {
                    mmap_search_close_search_condition_layout.setVisibility(View.GONE);
                }
                mbaidumap_location_imageview.setVisibility(View.VISIBLE);
                mMapDemandTV.setVisibility(View.VISIBLE);
                mmap_headrelativelayout.setVisibility(View.VISIBLE);
                Intent fieldinfo = null;
                fieldinfo = new Intent(BaiduMapActivity.this, CommunityInfoActivity.class);
                fieldinfo.putExtra("city_id", Integer.parseInt(mCityCode));
                fieldinfo.putExtra("id", info.getId());
                startActivity(fieldinfo);
//                if (info.getTop_physical_id() != null) {
//                    fieldinfo = new Intent(BaiduMapActivity.this, FieldInfoActivity.class);
//                    fieldinfo.putExtra("fieldId", String.valueOf(item.getTop_physical_id()));
//                    fieldinfo.putExtra("community_id", info.getId());
//                    startActivity(fieldinfo);
//                } else {
//
//                }
            }
        });
    }
    /* 判断是否有长按动作发生
     * @param lastX 按下时X坐标
     * @param lastY 按下时Y坐标
     * @param thisX 移动时X坐标
     * @param thisY 移动时Y坐标
     * @param lastDownTime 按下时间
     * @param thisEventTime 移动时间
     * @param longPressTime 判断长按时间的阀值
     */
    private boolean isLongPressed(float lastX,float lastY,
                                  float thisX,float thisY,
                                  long lastDownTime,long thisEventTime,
                                  long longPressTime){
        float offsetX = Math.abs(thisX - lastX);
        float offsetY = Math.abs(thisY - lastY);
        long intervalTime = thisEventTime - lastDownTime;
        if(offsetX <= 10 && offsetY <= 10 && intervalTime >= longPressTime){
            return true;
        }
        return false;
    }
    public void getAttributesList() {
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
            mPresenter.getAttributesList(Category);
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
        //价格
        HashMap<Object,Object> pricemap = new HashMap<Object,Object>();
        pricemap.put("type","price");
        pricemap.put("itemtype",1);
        data_new.add(pricemap);
        //场地面积
        HashMap<Object,Object> areamap = new HashMap<Object,Object>();
        areamap.put("type","area");
        areamap.put("itemtype",1);
        data_new.add(areamap);
        //建筑年份
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
        ResourcesScreeningItemAdapter.clear_resourcescreeninglist();
        //是否选中的标志
        for (int j = 0; j < data_new.size(); j++ ) {
            if (data_new.get(j).get("datalist") != null && ((ArrayList<HashMap<Object,Object>>)data_new.get(j).get("datalist")).size() > 0) {
                ArrayList<HashMap<Object,Object>> data_new_temp = new ArrayList<>();
                data_new_temp.addAll(((ArrayList<HashMap<Object,Object>>) (data_new.get(j).get("datalist"))));
                for (int i = 0; i <data_new_temp.size(); i++) {
                    if (data_new_temp.get(i).get("itemtype") != null && ((Integer)data_new_temp.get(i).get("itemtype") == 1 ||
                            (Integer)data_new_temp.get(i).get("itemtype") == 2)) {

                    } else {
                        ResourcesScreeningItemAdapter.getresourcescreeninglist().put(data_new_temp.get(i).get("type").toString() + data_new_temp.get(i).get(data_new_temp.get(i).get("type").toString()).toString(), false);
                        if (data_new_temp.get(i).get("type").toString().equals("field_labels")) {
                            if (apiResourcesModel.getLabel_ids() != null) {
                                if (apiResourcesModel.getLabel_ids().size() > 0) {
                                    field_labels_int = 1;
                                    if (apiResourcesModel.getLabel_ids().contains(Integer.parseInt(data_new_temp.get(i).get("id").toString()))) {
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
                } else if (data_new.get(j).get("type").toString().equals("price")) {
                    if (apiResourcesModel.getMin_price() != null && apiResourcesModel.getMin_price().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("pricemin",
                                Constants.getpricestring(apiResourcesModel.getMin_price(),0.01));
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("pricemin","");
                    }
                    if (apiResourcesModel.getMax_price() != null && apiResourcesModel.getMax_price().length() > 0) {
                        ResourcesScreeningNewAdapter.getedittextmap().put("pricemax",
                                Constants.getpricestring(apiResourcesModel.getMax_price(),0.01));
                    }  else {
                        ResourcesScreeningNewAdapter.getedittextmap().put("pricemax","");
                    }
                }
            }
        }
        resourcesScreeningNewAdapter = new ResourcesScreeningNewAdapter(BaiduMapActivity.this,BaiduMapActivity.this,data_new,0);
        mresourcesscreening_stickygridview_new.setAdapter(resourcesScreeningNewAdapter);
    }
    private HashMap<String,String> getBrowseHistoriesUrl() {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (apiResourcesModel.getZoom_level() > 0) {
            paramsMap.put("zoom_level",String.valueOf(apiResourcesModel.getZoom_level()));
        }
        if (apiResourcesModel.getCity_id() != null &&
                apiResourcesModel.getCity_id().length() > 0) {
            paramsMap.put("city_id",apiResourcesModel.getCity_id());
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
        if (apiResourcesModel.getKeywords() != null && apiResourcesModel.getKeywords().length() > 0) {
            paramsMap.put("keywords", apiResourcesModel.getKeywords());
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
        String cityStr = "";
        if (apiResourcesModel.getCity_ids() != null &&
                apiResourcesModel.getCity_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getCity_ids().size(); i++) {
                if (cityStr.length() > 0) {
                    cityStr = cityStr + "," + String.valueOf(apiResourcesModel.getCity_ids().get(i));
                } else {
                    cityStr = String.valueOf(apiResourcesModel.getCity_ids().get(i));
                }
            }
        }
        if (cityStr.length() > 0) {
            paramsMap.put("city_ids",cityStr);
        }
        //类目属性
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
        if (apiResourcesModel.getNearby() > 0) {
            paramsMap.put("nearby", String.valueOf(apiResourcesModel.getNearby()));
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
        return paramsMap;
    }
    private void shareDialog() {
        mSearchShareDialog = new AlertDialog.Builder(BaiduMapActivity.this).create();
        if (mSearchShareDialog!= null && mSearchShareDialog.isShowing()) {
            mSearchShareDialog.dismiss();
        }
        if (mShareBitmap == null) {
            mShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_ditufenxiang);
            mShareBitmap = Bitmap.createScaledBitmap(mShareBitmap, 300, 220, true);//压缩Bitmap
            mHandler.sendEmptyMessage(0);
        } else {
            View myView = BaiduMapActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
            String shareurl = com.linhuiba.business.config.Config.SHARE_FIELDS_LIST_URL+ getshareurl() + "&BackKey=1&is_app=1";
            String sharewxMinShareLinkUrl = com.linhuiba.business.config.Config.WX_MINI_SHARE_FIELDS_LIST_URL+ getshareurl() + "&BackKey=1&is_app=1";
            String ShareIconStr = "";//分享列表图片的url
            Constants constants = new Constants(BaiduMapActivity.this,
                    ShareIconStr);
            constants.shareWXMiniPopupWindow(BaiduMapActivity.this,myView,mSearchShareDialog,mIWXAPI,shareurl,
                    getResources().getString(R.string.search_fieldlist_activvity_sharetitle_text),
                    getResources().getString(R.string.search_fieldlist_activvity_sharetitle_text)
                    ,mShareBitmap,sharewxMinShareLinkUrl,mShareBitmap,getResources().getString(R.string.search_fieldlist_activvity_sharetitle_text));
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
        //分享时加位置类型
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
    private void browseHistories() {
        //浏览记录
        if (LoginManager.isLogin()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    try {
                        String parameter = "?"+ Request.urlEncode(getBrowseHistoriesUrl());
                        LoginMvpModel.sendBrowseHistories("map_list",parameter,apiResourcesModel.getCity_id());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }, 1000);
        }
    }
}
