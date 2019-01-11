package com.linhuiba.business.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baselib.app.activity.BaseFragment;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.activity.AdvertisingInfoActivity;
import com.linhuiba.business.activity.FieldInfoActivity;
import com.linhuiba.business.activity.MainTabActivity;
import com.linhuiba.business.activity.SearchFieldAreaActivity;
import com.linhuiba.business.activity.SearchListActivity;
import com.linhuiba.business.activity.SelfSupportShopActivity;
import com.linhuiba.business.adapter.GroupBookingListAdapter;
import com.linhuiba.business.adapter.SearchAdvListAdapter;
import com.linhuiba.business.adapter.SearchListAdapter;
import com.linhuiba.business.basemvp.BaseMvpFragment;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.model.ApiAdvResourcesModel;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.model.ResourceSearchItemModel;
import com.linhuiba.business.model.SearchListAttributesModel;
import com.linhuiba.business.model.SearchSellResModel;
import com.linhuiba.business.mvpmodel.LoginMvpModel;
import com.linhuiba.business.mvppresenter.SearchResListMvpPresenter;
import com.linhuiba.business.mvpview.SearchResListMvpView;
import com.linhuiba.business.network.Request;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.business.view.LoadMoreListView;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/9/6.
 */

public class SelfSupportShopFragment extends BaseMvpFragment implements SwipeRefreshLayout.OnRefreshListener,
        LoadMoreListView.OnLoadMore,SearchResListMvpView {
    @InjectView(R.id.selfsupportshop_statusbar_ll)
    LinearLayout mSelfSupportShopStatusBarLL;
    @InjectView(R.id.selfsupportshop_lv)
    LoadMoreListView mSelfSupportShopListLV;
    @InjectView(R.id.selfsupportshop_swipe)
    SwipeRefreshLayout mSelfSupportShopListSwipe;
    private View mMainContent;
    private ArrayList<SearchSellResModel> mHomeRecentActivityList;
    private SearchListAdapter mAdapter;
    private SearchAdvListAdapter mSelfSupportAdapter;
    private int mPagePosition;
    private ApiResourcesModel apiResourcesModel = new ApiResourcesModel();//搜索列表的model
    private View mNoDataRL;
    private String mCity_Id = "";
    private SelfSupportShopActivity mSelfSupportShopActivity;
    private SearchResListMvpPresenter mSearchResListMvpPresenter;
    private ApiAdvResourcesModel mApiAdvResourcesModel = new ApiAdvResourcesModel();//搜索列表的model
    private String mShareTitleStr = "";//分享的标题
    private String ShareIconStr ="";//分享的图片的url
    private Bitmap ShareBitmap = null;//分享需要的bitmap
    private Bitmap miniShareBitmap = null;//小程序分享需要的bitmap
    private String mWxShareLinkUrl = "";//微信分享的url
    private String mWxMiniShareShareLinkUrl = "";//小程序分享的url
    private Dialog mShareDialog;
    private IWXAPI mWXApi;
    private static final int LOACTION_REQUEST_INT = 10;//权限 requestcode
    private LocationClient mLocationClient = null;
    private double latitude;//地图中心点纬度(不传则表示不限定区域）
    private double longitude;//地图中心点经度(不传则表示不限定区域）
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mMainContent == null) {
            mMainContent = inflater.inflate(R.layout.fragment_selfsupportshop, container, false);
            initView();
        }
        return mMainContent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSearchResListMvpPresenter != null) {
            mSearchResListMvpPresenter.detachView();
        }
    }

    private void initView() {
        ButterKnife.inject(this,mMainContent);
        mSelfSupportShopActivity = (SelfSupportShopActivity) SelfSupportShopFragment.this.getActivity();
        mSearchResListMvpPresenter = new SearchResListMvpPresenter();
        mSearchResListMvpPresenter.attachView(this);
        mNoDataRL = (View) mMainContent.findViewById(R.id.no_data_view);
        ImageView mNoDataImg= (ImageView) mNoDataRL.findViewById(R.id.no_data_img);
        TextView mNoDataTV= (TextView) mNoDataRL.findViewById(R.id.no_data_tv);
        Button mNoDataBtn= (Button) mNoDataRL.findViewById(R.id.no_data_btn);
        mNoDataImg.setImageResource(R.drawable.emptystates_shopself);
        mNoDataTV.setText(getResources().getString(R.string.selfsupportshop_no_data_tv_str));
        mNoDataBtn.setText(getResources().getString(R.string.confirmorder_back_homepage));
        mNoDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maintabintent = new Intent(SelfSupportShopFragment.this.getActivity(), MainTabActivity.class);
                maintabintent.putExtra("new_tmpintent", "goto_homepage");
                startActivity(maintabintent);
            }
        });
        mNoDataRL.setVisibility(View.GONE);
        if (mSelfSupportShopActivity.mResTypeId == 3) {
            TitleBarUtils.setTitleText(mMainContent,getResources().getString(R.string.home_hot_activity_title_text));
        } else {
            TitleBarUtils.setTitleText(mMainContent,getResources().getString(R.string.selfsupportshop_title_other_str));
        }
        AndPermission.with(SelfSupportShopFragment.this)
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
        if (SelfSupportShopFragment.this.getActivity() instanceof SelfSupportShopActivity) {
            mSelfSupportShopStatusBarLL.setVisibility(View.GONE);
            TitleBarUtils.showBackImg(mMainContent,SelfSupportShopFragment.this.getActivity(),true);
        }
        mSelfSupportShopStatusBarLL.setBackgroundColor(getResources().getColor(R.color.default_bluebg));
        mSelfSupportShopListSwipe.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSelfSupportShopListSwipe.setOnRefreshListener(this);
        mSelfSupportShopListLV.setLoadMoreListen(this);
        Intent intent = SelfSupportShopFragment.this.getActivity().getIntent();
        if (intent.getExtras() != null &&
                intent.getExtras().get("city_id") != null &&
                intent.getExtras().get("city_id").toString().length() > 0) {
            mCity_Id = intent.getExtras().get("city_id").toString();
        } else {
            mCity_Id = LoginManager.getInstance().getTrackcityid();
        }
        showProgressDialog();
        initData();
        mSelfSupportShopListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < mHomeRecentActivityList.size()) {
                    Intent fieldinfo = new Intent(SelfSupportShopFragment.this.getActivity(), FieldInfoActivity.class);
                    if (mSelfSupportShopActivity.mResTypeId == 3) {
                        fieldinfo.putExtra("sell_res_id", String.valueOf(mHomeRecentActivityList.get(position).getSelling_resource_id()));
                        fieldinfo.putExtra("is_sell_res", true);
                    } else {
                        if (mHomeRecentActivityList.get(position).getRes_type_id() == 3) {
                            if (mHomeRecentActivityList.get(position).getResource_id() > 0) {
                                fieldinfo.putExtra("sell_res_id", String.valueOf(mHomeRecentActivityList.get(position).getResource_id()));
                                fieldinfo.putExtra("is_sell_res", true);
                            } else {
                                return;
                            }
                        } else {
                            fieldinfo.putExtra("fieldId", String.valueOf(mHomeRecentActivityList.get(position).getPhysical_resource_id()));
                        }
                    }
                    fieldinfo.putExtra("community_id", mHomeRecentActivityList.get(position).getCommunity_id());
                    startActivity(fieldinfo);
                }
            }
        });
        mWXApi = WXAPIFactory.createWXAPI(SelfSupportShopFragment.this.getContext(), Constants.APP_ID);
        mWXApi.registerApp(Constants.APP_ID);
        mShareTitleStr = getResources().getString(R.string.module_searchlist_activity_share_title);
        mWxShareLinkUrl = Config.SHARE_ACTIVITIES_LIST_URL + mCity_Id;
        mWxMiniShareShareLinkUrl = Config.WX_MINI_SHARE_ACTIVITIES_LIST_URL + mCity_Id;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResources().getString(R.string.selfsupportshop_fragment_name_str));
        if (mCity_Id.length() > 0 &&
                !mCity_Id.equals(LoginManager.getInstance().getTrackcityid())) {
            mSelfSupportShopListLV.set_refresh();
            showProgressDialog();
            initData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResources().getString(R.string.selfsupportshop_fragment_name_str));
    }

    private void initData() {
        mPagePosition = 1;
        if (mSelfSupportShopActivity.mResTypeId == 3) {
            mSearchResListMvpPresenter.getActivityList(mCity_Id,latitude,longitude,mPagePosition);
            sendBrowseHistories();
        } else {
            mApiAdvResourcesModel.setOrder_by("default_sort");
            mApiAdvResourcesModel.setOrder("desc");
            mApiAdvResourcesModel.setPage(String.valueOf(mPagePosition));
            mApiAdvResourcesModel.setCity_id(mCity_Id);
            mApiAdvResourcesModel.setResource_type(String.valueOf(1));
            mApiAdvResourcesModel.setPageSize("10");
            mApiAdvResourcesModel.setSelf_support(1);
            mApiAdvResourcesModel.setLat(latitude);
            mApiAdvResourcesModel.setLng(longitude);
            mSearchResListMvpPresenter.getSelfResList(mApiAdvResourcesModel);
        }
    }
    @Override
    public void onRefresh() {
        mSelfSupportShopListLV.set_refresh();
        initData();
    }

    @Override
    public void loadMore() {
        mPagePosition ++;
        apiResourcesModel.setPage(String.valueOf(mPagePosition));
        if (mSelfSupportShopActivity.mResTypeId == 3) {
            mSearchResListMvpPresenter.getActivityList(mCity_Id,latitude,longitude,mPagePosition);
            sendBrowseHistories();
        } else {
            mApiAdvResourcesModel.setPage(String.valueOf(mPagePosition));
            mSearchResListMvpPresenter.getSelfResList(mApiAdvResourcesModel);
        }
    }

    @Override
    public void onSearchSellResListSuccess(ArrayList<SearchSellResModel> list, Response response) {
        if(mSelfSupportShopListSwipe.isShown()){
            mSelfSupportShopListSwipe.setRefreshing(false);
        }
        //2017/10/20 暂无数据
        mHomeRecentActivityList = list;
        if( mHomeRecentActivityList == null ||  mHomeRecentActivityList.isEmpty()) {
            mNoDataRL.setVisibility(View.VISIBLE);
            return;
        }
        mNoDataRL.setVisibility(View.GONE);
        if (mHomeRecentActivityList.size() < 10) {
            mSelfSupportShopListLV.set_loaded();
        }
        if (mSelfSupportShopActivity.mResTypeId == 3) {
            mAdapter = new SearchListAdapter(SelfSupportShopFragment.this.getContext(),mHomeRecentActivityList,3,SelfSupportShopFragment.this.getActivity(),false);
            mSelfSupportShopListLV.setAdapter(mAdapter);
            TitleBarUtils.showActionImg(SelfSupportShopFragment.this.getActivity(), true, getResources().getDrawable(R.drawable.popup_ic_share), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 分享功能
                    if (ShareBitmap == null || miniShareBitmap == null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (mHomeRecentActivityList.get(0).getPic_url()!= null &&
                                        mHomeRecentActivityList.get(0).getPic_url().length() > 0) {
                                    ShareIconStr = mHomeRecentActivityList.get(0).getPic_url()+ com.linhuiba.linhuipublic.config.Config.Linhui_Mid_Watermark;
                                    ShareBitmap = com.linhuiba.linhuifield.connector.Constants.GetLocalOrNetBitmap(ShareIconStr);
                                    ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                                    miniShareBitmap = Bitmap.createScaledBitmap(ShareBitmap, 120, 120, true);//压缩Bitmap
                                    if (ShareBitmap == null) {
                                        ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                                        ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                                    }
                                    if (miniShareBitmap == null) {
                                        miniShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                                    }
                                } else {
                                    ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                                    ShareBitmap = com.linhuiba.linhuifield.connector.Constants.compressImage(ShareBitmap);
                                    miniShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_pic_big);
                                }
                                mHandler.sendEmptyMessage(0);
                            }
                        }).start();
                    } else {
                        View myView = SelfSupportShopFragment.this.getActivity().getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                        mShareDialog = new AlertDialog.Builder(SelfSupportShopFragment.this.getActivity()).create();
                        if (mShareDialog!= null && mShareDialog.isShowing()) {
                            mShareDialog.dismiss();
                        }
                        Constants constants = new Constants(SelfSupportShopFragment.this.getActivity(),
                                ShareIconStr);
                        constants.shareWXMiniPopupWindow(SelfSupportShopFragment.this.getActivity(),myView,mShareDialog,mWXApi,mWxShareLinkUrl,
                                mShareTitleStr,
                                mShareTitleStr, ShareBitmap,mWxMiniShareShareLinkUrl,miniShareBitmap,mShareTitleStr);
                    }
                }
            });
        } else {
            mSelfSupportAdapter = new SearchAdvListAdapter(SelfSupportShopFragment.this.getContext(),mHomeRecentActivityList,1,SelfSupportShopFragment.this.getActivity(),true);
            mSelfSupportShopListLV.setAdapter(mSelfSupportAdapter);
        }
    }

    @Override
    public void onSearchResListSuccess(ArrayList<ResourceSearchItemModel> list, Response response) {

    }

    @Override
    public void onSearchResListFailure(boolean superresult, Throwable error) {
        if(mSelfSupportShopListSwipe.isShown()){
            mSelfSupportShopListSwipe.setRefreshing(false);
        }
        if (!superresult)
            MessageUtils.showToast(getContext(), error.getMessage());
    }

    @Override
    public void onSearchResListMoreSuccess(ArrayList<ResourceSearchItemModel> list, Response response) {

    }

    @Override
    public void onSearchSellResListMoreSuccess(ArrayList<SearchSellResModel> list, Response response) {
        mSelfSupportShopListLV.onLoadComplete();
        ArrayList<SearchSellResModel> tmp = list;
        if( tmp == null ||  tmp.isEmpty()) {
            mPagePosition = mPagePosition - 1;
            mSelfSupportShopListLV.set_loaded();
            return;
        }
        for( SearchSellResModel listitem: tmp ){
            mHomeRecentActivityList.add(listitem);
        }
        if (mSelfSupportShopActivity.mResTypeId == 3) {
            mAdapter.notifyDataSetChanged();
        } else {
            mSelfSupportAdapter.notifyDataSetChanged();
        }
        if (tmp.size() < 10) {
            mSelfSupportShopListLV.set_loaded();
        }
    }

    @Override
    public void onSearchResListMoreFailure(boolean superresult, Throwable error) {
        mSelfSupportShopListLV.onLoadComplete();
        if (!superresult)
            MessageUtils.showToast(getContext(), error.getMessage());
    }

    @Override
    public void onAttributesSuccess(ArrayList<SearchListAttributesModel> list) {

    }

    @Override
    public void onAttributesFailure(boolean superresult, Throwable error) {

    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    final View myView = SelfSupportShopFragment.this.getActivity().getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
                    mShareDialog = new AlertDialog.Builder(SelfSupportShopFragment.this.getActivity()).create();
                    if (mShareDialog!= null && mShareDialog.isShowing()) {
                        mShareDialog.dismiss();
                    }
                    Constants constants = new Constants(SelfSupportShopFragment.this.getActivity(),
                            ShareIconStr);
                    constants.shareWXMiniPopupWindow(SelfSupportShopFragment.this.getActivity(),myView,mShareDialog,mWXApi,mWxShareLinkUrl,
                            mShareTitleStr,
                            mShareTitleStr, ShareBitmap, mWxMiniShareShareLinkUrl,miniShareBitmap,mShareTitleStr);
                    break;
                default:
                    break;
            }
        }

    };
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if(requestCode == LOACTION_REQUEST_INT) {
                //定位
                mLocationClient = new LocationClient(SelfSupportShopFragment.this.getActivity());
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
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                //定位后更新列表
                initData();
            }
            mLocationClient.stop();
            mLocationClient = null;
        }
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
    private HashMap<String,String> getBrowseHistoriesUrl(String city_id,double lat, double lng, int page) {
        HashMap<String,String> paramsMap = new HashMap<>();
        paramsMap.put("city_id",city_id);
        if (lat > 0) {
            paramsMap.put("latitude", String.valueOf(lat));
        }
        if (lng > 0) {
            paramsMap.put("longitude", String.valueOf(lng));
        }
        return paramsMap;
    }
    private void sendBrowseHistories() {
        //浏览记录
        if (LoginManager.isLogin() && mSelfSupportShopActivity.mResTypeId == 3) {
            try {
                String parameter = "?"+ Request.urlEncode(getBrowseHistoriesUrl(mCity_Id,latitude,longitude,mPagePosition));
                LoginMvpModel.sendBrowseHistories("activity_list",parameter,mCity_Id);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
