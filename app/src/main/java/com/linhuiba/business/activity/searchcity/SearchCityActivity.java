package com.linhuiba.business.activity.searchcity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baselib.app.util.MessageUtils;
import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.pinyinhelper.PinyinMapDict;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.searchcity.indexlib.IndexBar.bean.BaseIndexPinyinBean;
import com.linhuiba.business.activity.searchcity.indexlib.IndexBar.widget.IndexBar;
import com.linhuiba.business.activity.searchcity.indexlib.suspension.SuspensionDecoration;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.model.SearchCityModel;
import com.linhuiba.business.mvpview.SearchCityMvpView;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SearchCityActivity extends BaseMvpActivity implements SearchCityMvpView{
    @InjectView(R.id.searchcity_current_city_ll)
    LinearLayout mCurrentLL;
    @InjectView(R.id.searchcity_current_city_tv)
    TextView mCurrentTV;
    @InjectView(R.id.search_city_unlimited_ll)
    LinearLayout mUnlimitedLL;
    @InjectView(R.id.search_city_unlimited_tv)
    TextView mUnlimitedTV;

    private Context mContext;
    private MeituanAdapter mAdapter;
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;//RecyclerView添加HeaderView FooterView的装饰Adapter类
    private LinearLayoutManager mManager;
    //设置给InexBar、ItemDecoration的完整数据集
    private List<BaseIndexPinyinBean> mSourceDatas;
    //头部数据源 定位 热门
    private List<MeituanHeaderBean> mHeaderDatas;
    //主体部分数据源（城市数据）
    private List<MeiTuanBean> mBodyDatas = new ArrayList<>();//所有城市的数据集
    private SuspensionDecoration mDecoration;//悬停的自定义数据样式自定义
    private IndexBar mIndexBar;//左侧滑动栏
    private RecyclerView mRv;//城市名和head显示的rv
    private TextView mTvSideBarHint;//滑动变化的选中城市对应的字母
    private LinearLayout mLocationLL;//定位到的城市ll
    private TextView mNoOpenCityTV;//提示未开通
    private TextView mOpenLocationTV;//开启定位按钮
    private LinearLayout mOpenCityLL;//开启定位的ll
    private TextView mLocationTV;//定位到的城市tv
    private LinearLayout mSearchLocationCityLL;//定位到的城市all ll
    private ArrayList<HashMap<String, String>> mCityListMap;
    private List<SearchCityModel> mSearchCityList = new ArrayList<>();
    private String mCurrentCityName;
    private String mCurrentCityId;
    private static final int RESULT_INT = 1;//城市点击或者搜索返回 resultCode
    private static final int LOACTION_REQUEST_INT = 10;//权限 requestcode
    private static final int LOACTION_RESULT_INT = 2;//开启定位后返回的 requestcode
    private LocationClient mLocationClient = null;
    private boolean isOnCreate;
    private String mLocationCity;//定位到的城市str
    public int mIsCommunitySearchCity;
    private static final int ADDFIELD_COMMUNITY_RESULT_INT = 3;//发布场地信息搜索城市返回 resultCode
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_searchcity);
        ButterKnife.inject(this);
        initView();
        initData();
    }
    private void initView() {
        isOnCreate = true;
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if (intent.getExtras().get("searchcity_list") != null) {
                mSearchCityList = (List<SearchCityModel>) intent.getExtras().get("searchcity_list");
            } else {
                if (intent.getExtras().get("addfield_searchcity_list") != null) {
                    if (intent.getExtras().get("is_community_search_city") != null &&
                            intent.getExtras().getInt("is_community_search_city") == 1) {
                        mUnlimitedLL.setVisibility(View.GONE);
                        mIsCommunitySearchCity = 1;
                    } else {
                        mUnlimitedLL.setVisibility(View.VISIBLE);
                        mUnlimitedTV.setText(getResources().getString(R.string.map_search_fieldtype_unlimited_str));
                    }
                    ArrayList<Field_AddResourceCreateItemModel> cityList = (ArrayList<Field_AddResourceCreateItemModel>) intent.getExtras().get("addfield_searchcity_list");
                    mSearchCityList = new ArrayList<>();
                    for (Field_AddResourceCreateItemModel model : cityList) {
                        SearchCityModel searchCityModel = new SearchCityModel();
                        searchCityModel.setId(String.valueOf(model.getId()));
                        searchCityModel.setName(String.valueOf(model.getName()));
                        mSearchCityList.add(searchCityModel);
                    }
                }
            }
            if (intent.getExtras().get("name") != null &&
                    intent.getExtras().get("id") != null &&
                    intent.getExtras().get("name").toString().length() > 0 &&
                    intent.getExtras().get("id").toString().length() > 0) {
                mCurrentCityName = intent.getExtras().get("name").toString();
                mCurrentCityId = intent.getExtras().get("id").toString();
            }
        }
        mContext = this;
        mRv = (RecyclerView) findViewById(R.id.rv);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(this));
        mSourceDatas = new ArrayList<>();
        mHeaderDatas = new ArrayList<>();
        List<SearchCityModel> locationCitys = new ArrayList<>();

        mHeaderDatas.add(new MeituanHeaderBean(locationCitys, 
                getResources().getString(R.string.module_searchcity_target_currect_city_str), 
                getResources().getString(R.string.module_searchcity_target_currect_city_indexbar_str)));

        List<SearchCityModel> hotCitys = new ArrayList<>();
        mHeaderDatas.add(new MeituanHeaderBean(hotCitys,
                getResources().getString(R.string.module_searchcity_hot_city_str),
                getResources().getString(R.string.module_searchcity_hot_city_indexbar_str)));
        mSourceDatas.addAll(mHeaderDatas);
        //2018/4/8 城市item
        mAdapter = new MeituanAdapter(this, R.layout.module_activity_searchcity_recycle_item_select_city, mBodyDatas,SearchCityActivity.this);
        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mAdapter) {
            @Override
            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o) {
                switch (layoutId) {
                    case R.layout.module_activity_searchcity_recycle_item_header:
                        final MeituanHeaderBean meituanHeaderBean = (MeituanHeaderBean) o;
                        //网格
                        RecyclerView recyclerView = holder.getView(R.id.rvCity);
                        //2018/4/8 热门城市item
                        recyclerView.setAdapter(
                                new CommonAdapter<SearchCityModel>(mContext, R.layout.module_activity_searchcity_recycle_item_header_item, meituanHeaderBean.getCityList()) {
                                    @Override
                                    public void convert(ViewHolder holder, final SearchCityModel searchCityModel) {
                                        holder.setText(R.id.tvName, searchCityModel.getName());
                                        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent();
                                                intent.putExtra("id",searchCityModel.getId());
                                                intent.putExtra("name",searchCityModel.getName());
                                                if (mIsCommunitySearchCity == 1) {
                                                    setResult(ADDFIELD_COMMUNITY_RESULT_INT,intent);
                                                } else {
                                                    setResult(RESULT_INT,intent);
                                                }
                                                finish();
                                            }
                                        });
                                    }
                                });
                        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                        break;
                    case R.layout.module_activity_searchcity_recycle_item_header_top:
                        //2018/4/8 当前item
                        mLocationLL = (LinearLayout) holder.getConvertView().findViewById(R.id.searchcity_loaction_ll);
                        mNoOpenCityTV = (TextView) holder.getConvertView().findViewById(R.id.searchcity_noopen_tv);
                        mOpenCityLL  = (LinearLayout) holder.getConvertView().findViewById(R.id.searchcity_open_location_ll);
                        mOpenLocationTV  = (TextView) holder.getConvertView().findViewById(R.id.searchcity_open_location_tv);
                        mLocationTV = (TextView) holder.getConvertView().findViewById(R.id.searchcity_location_tv);
                        mSearchLocationCityLL = (LinearLayout) holder.getConvertView().findViewById(R.id.searchcity_location_city_ll);
                        mOpenLocationTV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //跳转GPS设置界面
                                Intent intent =  new Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS);
                                startActivityForResult(intent,LOACTION_RESULT_INT);
                            }
                        });
                        mLocationLL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mNoOpenCityTV.getVisibility() == View.GONE) {
                                    SearchCityModel searchCityModel = getLocationInfo(mLocationCity);
                                    if (searchCityModel != null &&
                                            searchCityModel.getName() != null &&
                                            searchCityModel.getId() != null) {
                                        Intent intent = new Intent();
                                        intent.putExtra("id",searchCityModel.getId());
                                        intent.putExtra("name",searchCityModel.getName());
                                        if (mIsCommunitySearchCity == 1) {
                                            setResult(ADDFIELD_COMMUNITY_RESULT_INT,intent);
                                        } else {
                                            setResult(RESULT_INT,intent);
                                        }
                                        finish();
                                    }
                                }
                            }
                        });
//                        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(mContext, "cityName:" +  meituanTopHeaderBean.getTxt(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
                        break;
                    default:
                        break;
                }
            }
        };
        //2018/4/8 列表头部分组
        mHeaderAdapter.setHeaderView(0, R.layout.module_activity_searchcity_recycle_item_header_top, new MeituanTopHeaderBean("当前：上海徐汇"));
        mHeaderAdapter.setHeaderView(1, R.layout.module_activity_searchcity_recycle_item_header, mHeaderDatas.get(0));


        mRv.setAdapter(mHeaderAdapter);
        //2018/4/8 提示头的样式 和 添加 悬停的样式
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(this, mSourceDatas)
                .setmTitleHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44, getResources().getDisplayMetrics()))
                .setColorTitleBg(0xfff8f8f8)
                .setTitleFontSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()))
                .setColorTitleFont(mContext.getResources().getColor(R.color.register_edit_color))
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - mHeaderDatas.size()));
//        mRv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        //使用indexBar
        mTvSideBarHint = (TextView) findViewById(R.id.sidebarhint_tv);//HintTextView
        mIndexBar = (IndexBar) findViewById(R.id.indexbar);//IndexBar

        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - mHeaderDatas.size());
        //2018/4/9 测试xml数据
        setCityDatas();
        AndPermission.with(SearchCityActivity.this)
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
    private void initData() {
        String hotCityList = LoginManager.getHot_citylist();
        if (hotCityList != null && hotCityList.length() > 0) {
            JSONArray jsonArray = JSONArray.parseArray(hotCityList);
            if (jsonArray != null && jsonArray.size() > 0) {
                MeituanHeaderBean header3 = mHeaderDatas.get(0);
                ArrayList<SearchCityModel> searchCityList = (ArrayList<SearchCityModel>) JSONArray.parseArray(jsonArray.toJSONString(),
                        SearchCityModel.class);
                header3.setCityList(searchCityList);
                mHeaderAdapter.notifyItemRangeChanged(1, 1);
            }
        }
    }

    @OnClick({
            R.id.searchcity_search_et,
            R.id.searchcity_close_iv,
            R.id.search_city_unlimited_tv
    })
    public void OnClicK(View view) {
        switch (view.getId()) {
            case R.id.searchcity_search_et:
                Intent searchKeyIntent = new Intent(SearchCityActivity.this,SearchCityKeyWordActivity.class);
                searchKeyIntent.putExtra("citylist",(Serializable) mBodyDatas);
                startActivityForResult(searchKeyIntent,RESULT_INT);
                break;
            case R.id.searchcity_close_iv:
                finish();
                break;
            case R.id.search_city_unlimited_tv:
                Intent intent = new Intent();
                intent.putExtra("id","0");
                intent.putExtra("name",getResources().getString(R.string.map_search_fieldtype_unlimited_str));
                if (mIsCommunitySearchCity == 1) {
                    setResult(ADDFIELD_COMMUNITY_RESULT_INT,intent);
                } else {
                    setResult(RESULT_INT,intent);
                }
                finish();
                break;
            default:
                break;
        }
    }
    //组织数据源
    private void setCityDatas() {
        if (mSearchCityList != null && mSearchCityList.size() > 0) {
            for (int i = 0; i < mSearchCityList.size(); i++) {
                MeiTuanBean cityBean = new MeiTuanBean();
                cityBean.setName(mSearchCityList.get(i).getName());//设置城市名称
                cityBean.setId(mSearchCityList.get(i).getId());//设置城市名称
                mBodyDatas.add(cityBean);
            }
        }
        //先排序
        if (mBodyDatas != null && mBodyDatas.size() > 0) {
            mIndexBar.getDataHelper().sortSourceDatas(mBodyDatas);
            mAdapter.setDatas(mBodyDatas);
            mHeaderAdapter.notifyDataSetChanged();
            mSourceDatas.addAll(mBodyDatas);
            mIndexBar.setmSourceDatas(mSourceDatas)//设置数据
                    .invalidate();
            mDecoration.setmDatas(mSourceDatas);
        }

    }
    @Override
    public void onHotCitySuccess(JSONArray jsonArray) {

    }

    @Override
    public void onHotCityFailure(boolean superresult, Throwable error) {
        if (!superresult)
            MessageUtils.showToast(error.getMessage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOACTION_RESULT_INT) {
            AndPermission.with(SearchCityActivity.this)
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
        if (resultCode == RESULT_INT) {
            if (data.getExtras() != null &&
                    data.getExtras().get("id") != null &&
                    data.getExtras().get("name") != null) {
                Intent intent = new Intent();
                intent.putExtra("id",data.getExtras().get("id").toString());
                intent.putExtra("name",data.getExtras().get("name").toString());
                if (mIsCommunitySearchCity == 1) {
                    setResult(ADDFIELD_COMMUNITY_RESULT_INT,intent);
                } else {
                    setResult(RESULT_INT,intent);
                }
                finish();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if(requestCode == LOACTION_REQUEST_INT) {
                //定位
                //2018/4/10 测试权限
                if (isOnCreate) {
                    mLocationClient = new LocationClient(SearchCityActivity.this);
                    mLocationClient.registerLocationListener(new MyLocationListener());//注册定位监听接口
                    initLocation();
                }
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
             if(requestCode == LOACTION_REQUEST_INT) {
                 locationHandler.sendEmptyMessage(2);
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
            if (city != null ) {
                if (city.length() != 0) {
                    //2018/4/10 显示定位置
                    mLocationCity = city;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            locationHandler.sendEmptyMessage(1);
                        }
                    }).start();

                }
            }
            mLocationClient.stop();
            mLocationClient = null;
        }
    }
    private Handler locationHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (mLocationCity != null &&
                        mLocationCity.length() > 0) {
                    mLocationTV.setText(mLocationCity);
                    if (!isOpenCity(mLocationCity)) {
                        mNoOpenCityTV.setVisibility(View.VISIBLE);
                    } else {
                        mNoOpenCityTV.setVisibility(View.GONE);
                        SearchCityModel searchCityModel = getLocationInfo(mLocationCity);
                        if (searchCityModel.getId().equals(mCurrentCityId) &&
                                searchCityModel.getName().equals(mCurrentCityName)) {
                            mCurrentLL.setVisibility(View.GONE);
                        } else {
                            mCurrentLL.setVisibility(View.VISIBLE);
                            mCurrentTV.setText(mCurrentCityName);
                        }
                    }
                    mOpenCityLL.setVisibility(View.GONE);
                    mSearchLocationCityLL.setVisibility(View.VISIBLE);
                }
            } else {
                mOpenCityLL.setVisibility(View.VISIBLE);
                mSearchLocationCityLL.setVisibility(View.GONE);
            }
        }
    };
    private boolean isOpenCity(String city) {
        boolean isOpenCity = false;
        if (mSearchCityList != null && mSearchCityList.size() > 0) {
            for (int i = 0; i < mSearchCityList.size(); i++) {
                if (mSearchCityList.get(i).getName().contains(city)) {
                    isOpenCity = true;
                    break;
                }
            }
        }

        return isOpenCity;
    }
    private SearchCityModel getLocationInfo(String city) {
        SearchCityModel searchCityModel = null;
        if (mSearchCityList != null //友盟错误日志
                && mSearchCityList.size() > 0 && city != null) {
            for (int i = 0; i < mSearchCityList.size(); i++) {
                if (mSearchCityList.get(i).getName() != null &&
                        mSearchCityList.get(i).getName().contains(city)) {
                    searchCityModel = mSearchCityList.get(i);
                    break;
                }
            }
        }
        return searchCityModel;
    }
}
