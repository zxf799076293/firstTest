package com.linhuiba.business.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.adapter.TrackAdapter;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.connector.FieldApi;
import com.linhuiba.business.connector.MyAsyncHttpClient;
import com.linhuiba.business.fieldcallback.Field_AddFieldChoosePictureCallBack;
import com.linhuiba.business.fieldcallback.Field_MyAllCallBack;
import com.linhuiba.business.fieldview.Field_NewGalleryView;
import com.linhuiba.business.model.TrackModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Response;
import com.linhuiba.business.util.TitleBarUtils;
import com.linhuiba.linhuifield.sqlite.ConfigCitiesModel;
import com.linhuiba.linhuifield.sqlite.ConfigCityParameterModel;
import com.linhuiba.linhuifield.sqlite.ConfigSqlOperation;
import com.linhuiba.linhuifield.sqlite.ConfigurationsModel;
import com.linhuiba.linhuifield.sqlite.DBManager;
import com.linhuiba.linhuipublic.config.LoginManager;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/20.
 */
public class TrackActivity extends BaseMvpActivity implements Field_AddFieldChoosePictureCallBack.AddfieldFourCall {
    @InjectView(R.id.track_markerinfolistiew)
    ListView mtrack_markerinfolistiew;
    private MapView mfootprintView;
    private BaiduMap mBaiduMap = null;
    private int mImageList_size;//图片数量
    private int newPosition;//显示的图片position
    private ArrayList<HashMap<String, String>> pathtmp = new ArrayList<>();
    private Marker clickmarker;
    private boolean clickmarkerboolen = false;
    private TreeMap<String,ArrayList<TrackModel>> trackmapdata = new TreeMap<>();
    private TrackAdapter trackAdapter;
    private ArrayList<TrackModel> markerlistdata = new ArrayList<>();
    private HashMap<String,String> trackmarkernumber = new HashMap<>();
    private int deletepicurlposition;
    private Dialog pw;
    private IWXAPI api;
    private Bitmap ShareBitmap;
    private int tracklist_total;
    private String track_share_linkurl = "";
    private String cityid = "";
    private Dialog mZoomPictureDialog;
    private List<ImageView> mImageViewList = new ArrayList<>();
    private boolean mIsRefreshZoomImageview = true;
    private double longitude = 0;
    private double latitude = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityfootprint);
        ButterKnife.inject(this);
        mfootprintView = (MapView)findViewById(R.id.footprintView);
        TitleBarUtils.setTitleText(this, getResources().getString(R.string.myselfinfo_footprint_txt));
        TitleBarUtils.showBackImg(this, true);
        initview();
    }
    private void initview() {
        if (LoginManager.getInstance().getTrackcityid().length() != 0) {
            cityid = LoginManager.getInstance().getTrackcityid();
        } else if (LoginManager.getInstance().getCurrentCitycode().length() !=0) {
            cityid = LoginManager.getInstance().getCurrentCitycode();
        }
        getlongitude_latitude(cityid);
        mBaiduMap = mfootprintView.getMap();
        LatLng latLng = new LatLng(latitude,longitude);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(latLng, 12);//设置缩放比例
        mBaiduMap.setMapStatus(u);
        showProgressDialog();
        FieldApi.gettracklistitems(MyAsyncHttpClient.MyAsyncHttpClient_version_two(), GettracklistitemHandler, cityid);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                HashMap<String,ArrayList<TrackModel>> info = (HashMap<String,ArrayList<TrackModel>>) marker.getExtraInfo().get("info");
                ArrayList<TrackModel> markerlistdatatmp = new ArrayList<>();
                String key="";
                for (Map.Entry<String, ArrayList<TrackModel>> entry : info.entrySet()) {
                    key = entry.getKey();
                    ArrayList<TrackModel> value = (ArrayList<TrackModel>) entry.getValue();
                    markerlistdatatmp.addAll(value);
                }
                if (markerlistdata != null) {
                    markerlistdata.clear();
                }
                markerlistdata.addAll(markerlistdatatmp);
                trackAdapter = new TrackAdapter(TrackActivity.this,TrackActivity.this,markerlistdata,0);
                mtrack_markerinfolistiew.setAdapter(trackAdapter);
                clickmarker = marker;
                marker.setIcon(getmarkerbitmap(R.drawable.iconfontprint_firstpositioning, Integer.parseInt(trackmarkernumber.get(key).toString())));

                mtrack_markerinfolistiew.setVisibility(View.VISIBLE);

                clickmarkerboolen = true;
                return false;
            }
        });
        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            /**
             * 当用户触摸地图时回调函数
             * @param event 触摸事件
             */
            public void onTouch(MotionEvent event) {
                if (clickmarkerboolen == true) {
                    HashMap<String,ArrayList<TrackModel>> info = (HashMap<String,ArrayList<TrackModel>>) clickmarker.getExtraInfo().get("info");
                    String key="";
                    for (Map.Entry<String, ArrayList<TrackModel>> entry : info.entrySet()) {
                        key = entry.getKey();
                    }
                    mtrack_markerinfolistiew.setVisibility(View.GONE);
                    clickmarker.setIcon(getmarkerbitmap(R.drawable.iconfontprint_otherpositioning, Integer.parseInt(trackmarkernumber.get(key).toString())));
                    clickmarkerboolen = false;
                }
            }
        });
//        TitleBarUtils.showActionImg(TrackActivity.this, true, TrackActivity.this.getResources().getDrawable(R.drawable.tab_ic_share), new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showProgressDialog();
//                FieldApi.gettrack_share_link(MyAsyncHttpClient.MyAsyncHttpClient(),track_share_link_Handler,cityid);
//            }
//        });

    }
    private void getlongitude_latitude(String mCityCode) {
        List<ConfigCityParameterModel> cities = ConfigSqlOperation.selectSQL(6,0,TrackActivity.this);
        if (cities != null &&
                cities.size() > 0) {
            for (int i = 0; i < cities.size(); i++) {
                String id = String.valueOf(cities.get(i).getCity_id());
                int defaultCity = cities.get(i).getDefault_city();
                if (mCityCode != null && mCityCode.length() > 0) {
                    if (mCityCode.equals(id)) {
                        if (cities.get(i).getLongitude() != null) {
                            longitude = cities.get(i).getLongitude();
                        }
                        if (cities.get(i).getLatitude() != null) {
                            latitude = cities.get(i).getLatitude();
                        }
                    }
                } else {
                    if (defaultCity == 1) {
                        cityid = id;
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
                    String id = String.valueOf(cities.get(i).getCity_id());
                    int defaultCity = cities.get(i).getDefault_city();
                    if (mCityCode != null && mCityCode.length() > 0) {
                        if (mCityCode.equals(String.valueOf(citylist.get(i).getCity_id()))) {
                            if (citylist.get(i).getLongitude() != null) {
                                longitude = citylist.get(i).getLongitude();
                            }
                            if (citylist.get(i).getLatitude() != null) {
                                latitude = citylist.get(i).getLatitude();
                            }
                        }
                    } else {
                        if (defaultCity == 1) {
                            cityid = id;
                            if (cities.get(i).getLongitude() != null) {
                                longitude = cities.get(i).getLongitude();
                            }
                            if (cities.get(i).getLatitude() != null) {
                                latitude = cities.get(i).getLatitude();
                            }
                        }

                    }
                }
            }
        }
    }
    private void PolylineOptions() {
        addmarkerk();
    }
    //区域标注
    private void addmarkerk() {
        mBaiduMap.clear();
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        BitmapDescriptor bitmap = null;
        ArrayList<String> trackmapkeylist = new ArrayList<>();
        for (Map.Entry<String,ArrayList<TrackModel>> entry : trackmapdata.entrySet()) {
            String key = entry.getKey();
            trackmapkeylist.add(key);
        }
        if (trackmapdata.get(trackmapkeylist.get(trackmapkeylist.size() - 1).toString()).size() > 0) {
            if (trackmapdata.get(trackmapkeylist.get(trackmapkeylist.size() - 1).toString()).get(0) != null) {
                LatLng latLng = new LatLng(trackmapdata.get(trackmapkeylist.get(trackmapkeylist.size() - 1).toString()).get(0).getLat(),trackmapdata.get(trackmapkeylist.get(trackmapkeylist.size() - 1).toString()).get(0).getLng());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(latLng, 12);//设置缩放比例
                mBaiduMap.setMapStatus(u);
                if (trackmarkernumber != null) {
                    trackmarkernumber.clear();
                }
                int maporder = 1;
                for (int i = 0; i < trackmapkeylist.size(); i++) {
                    int mapordertmp = trackmapdata.get(trackmapkeylist.get(i).toString()).size();
                    trackmarkernumber.put(trackmapkeylist.get(i), String.valueOf(maporder));
                    TextView button = new TextView(TrackActivity.this);
                    button.setBackgroundResource(R.drawable.iconfontprint_otherpositioning);
                    button.setTextColor(getResources().getColor(R.color.white));
                    button.setText(String.valueOf(maporder));
                    maporder = maporder + mapordertmp;
                    button.setTextSize(12);
                    button.setGravity(Gravity.CENTER_HORIZONTAL);
                    button.setPadding(0, 6, 0, 0);
                    bitmap = BitmapDescriptorFactory
                            .fromView(button);
                    LatLng mapsearchlatLng = new LatLng(trackmapdata.get(trackmapkeylist.get(i).toString()).get(0).getLat(), trackmapdata.get(trackmapkeylist.get(i).toString()).get(0).getLng());

                    overlayOptions = new MarkerOptions().position(mapsearchlatLng)
                            .icon(bitmap).zIndex(5);
                    //在地图上添加Marker，并显示
                    marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
                    final Bundle bundle = new Bundle();
                    HashMap<String,ArrayList<TrackModel>> trackmapdatatmp = new HashMap<>();
                    trackmapdatatmp.put(trackmapkeylist.get(i).toString(), trackmapdata.get(trackmapkeylist.get(i).toString()));
                    bundle.putSerializable("info", trackmapdatatmp);
                    marker.setExtraInfo(bundle);
                    // 构造折线点坐标
                    if (trackmapkeylist.size() > 1) {
                        List<LatLng> points = new ArrayList<LatLng>();
                        for (Map.Entry<String,ArrayList<TrackModel>> entry : trackmapdata.entrySet()) {
                            ArrayList<TrackModel> value = entry.getValue();
                            points.add(new LatLng(value.get(0).getLat(), value.get(0).getLng()));
                        }
                        OverlayOptions Polyline = new PolylineOptions().width(5)
                                .color(getResources().getColor(R.color.title_bar_txtcolor)).points(points).dottedLine(true);
                        //添加在地图中
                        Polyline mPolyline = (Polyline) mBaiduMap.addOverlay(Polyline);
                    }
                }
            } else {
                MessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
            }
        } else {
            MessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
        }

    }
    @Override
    public void getAddfieldFouritemcall(int position) {
        deletepicurlposition = position;
        if (pathtmp != null) {
            pathtmp.clear();
        }
        if (markerlistdata.get(position).getPic_url() != null) {
            for (int i = 0; i < markerlistdata.get(position).getPic_url().size(); i++) {
                if (markerlistdata.get(position).getPic_url().get(i).get("url") != null && markerlistdata.get(position).getPic_url().get(i).get("pic_id") != null) {
                    if (markerlistdata.get(position).getPic_url().get(i).get("url").length() > 0 && markerlistdata.get(position).getPic_url().get(i).get("pic_id").length() > 0) {
                        pathtmp.add(markerlistdata.get(position).getPic_url().get(i));
                    }
                }
            }
        }
        if (pathtmp.size() > 0) {
            ArrayList<String> imagetlist = new ArrayList<>();
            for (int i = 0; i < pathtmp.size(); i++) {
                imagetlist.add(pathtmp.get(i).get("url").toString());
            }
            initPreviewZoomView(imagetlist);
        }

    }
    private void initPreviewZoomView(ArrayList<String> pathtmp) {
        mZoomPictureDialog = new AlertDialog.Builder(this).create();
        View mView = getLayoutInflater().inflate(com.linhuiba.linhuifield.R.layout.activity_fieldinfo_preview_zoom_picture_dialog, null);
        final TextView mShowPictureSizeTV = (TextView)mView.findViewById(com.linhuiba.linhuifield.R.id.showpicture_dialog_sizetxt);
        TextView mShowPictureBackTV = (TextView)mView.findViewById(com.linhuiba.linhuifield.R.id.showpicture_dialog_back);
        ViewPager mZoomViewPage = (ViewPager)mView.findViewById(com.linhuiba.linhuifield.R.id.zoom_dialog_viewpage);
        com.linhuiba.linhuifield.connector.Constants mConstants = new com.linhuiba.linhuifield.connector.Constants(TrackActivity.this,TrackActivity.this,newPosition,mImageList_size,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_WIDTH,
                com.linhuiba.linhuifield.connector.Constants.SHOW_IMG_PIXEL_HEIGHT);
        mConstants.showPicture(mView,mShowPictureSizeTV,mZoomViewPage,
                mZoomPictureDialog,mImageViewList,pathtmp,mIsRefreshZoomImageview,
                0,false);
        mIsRefreshZoomImageview = false;
        mZoomViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                newPosition = position % mImageViewList.size();
            }

            @Override
            public void onPageSelected(int position) {
                mShowPictureSizeTV.setText(String.valueOf(position % mImageViewList.size()+1)+"/" + String.valueOf(mImageViewList.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void getconfigurate(int position) {
        Field_MyAllCallBack test = new Field_MyAllCallBack(position);
        test.AddfieldFouritemcall(this);
    }
    private BitmapDescriptor getmarkerbitmap(int drawable,int position) {
        BitmapDescriptor bitmap = null;
        TextView button = new TextView(TrackActivity.this);
        button.setBackgroundResource(drawable);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setText(String.valueOf(position));
        button.setTextSize(12);
        button.setGravity(Gravity.CENTER_HORIZONTAL);
        button.setPadding(0, 6, 0, 0);
        bitmap = BitmapDescriptorFactory
                .fromView(button);
        return bitmap;
    }
    private LinhuiAsyncHttpResponseHandler GettracklistitemHandler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, final Response response, final Object data) {
            hideProgressDialog();
            if (response.total > 0) {
                tracklist_total = response.total;
            }
            if (response.data != null) {
                if (response.data.toString().length() > 0) {
                    if (response.data instanceof JSONArray) {
                        if (!(JSONArray.parseArray(response.data.toString()).size() > 0 )) {
                            MessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
                        }
                    } else {
                        for (Map.Entry<String, Object> entry : JSONObject.parseObject(response.data.toString()).entrySet()) {
                            String key = entry.getKey();
                            ArrayList<TrackModel> value = (ArrayList<TrackModel>) JSON.parseArray(entry.getValue().toString(), TrackModel.class);
                            trackmapdata.put(key,value);
                        }
                    }
                } else {
                    MessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
                }
            } else {
                MessageUtils.showToast(getResources().getString(R.string.order_nodata_toast));
            }
            if (trackmapdata != null) {
                if (trackmapdata.size() > 0) {
                    PolylineOptions();
                }
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(getContext(), error.getMessage());
            checkAccess(error);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 0:
                if (data != null) {
                    if (data.getExtras() != null) {
                        int addposition = data.getExtras().getInt("editorposition");
                    }
                }
                break;
            case 1:
                int editorposition = data.getExtras().getInt("editorposition");
                TrackModel list = (TrackModel) data.getSerializableExtra("trackmodel");
                HashMap<String,ArrayList<TrackModel>> info = (HashMap<String,ArrayList<TrackModel>>) clickmarker.getExtraInfo().get("info");
                ArrayList<TrackModel> markerlistdatatmp = new ArrayList<>();
                String key="";
                ArrayList<TrackModel> value = new ArrayList<>();
                for (Map.Entry<String, ArrayList<TrackModel>> entry : info.entrySet()) {
                    key = entry.getKey();
                    value = (ArrayList<TrackModel>) entry.getValue();
                    markerlistdatatmp.addAll(value);
                }
                markerlistdatatmp.remove(editorposition);
                markerlistdatatmp.add(editorposition, list);
                HashMap<String,ArrayList<TrackModel>> infotmp = new HashMap<>();
                infotmp.put(key, markerlistdatatmp);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", infotmp);
                clickmarker.setExtraInfo(bundle);
                markerlistdata.remove(editorposition);
                markerlistdata.add(editorposition,list);
                mtrack_markerinfolistiew.setSelection(editorposition);
                trackAdapter.notifyDataSetChanged();
                mIsRefreshZoomImageview = true;
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mfootprintView.onDestroy();
        if (ShareBitmap != null) {
            ShareBitmap.recycle();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mfootprintView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mfootprintView.onPause();
    }
    private void share_show(String description) {
        api = WXAPIFactory.createWXAPI(TrackActivity.this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        ShareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sharelogo);
        View myView = TrackActivity.this.getLayoutInflater().inflate(R.layout.activity_fieldinfo_popupwindow, null);
        pw = new AlertDialog.Builder(this).create();
        Constants constants = new Constants(TrackActivity.this,
                "");
        Constants.share_showPopupWindow(TrackActivity.this,myView,pw,api,track_share_linkurl,
                getResources().getString(R.string.track_share_title_text),
                getResources().getString(R.string.track_share_description_one_text)+description+
                        getResources().getString(R.string.track_share_description_two_text), ShareBitmap);
    }
    private LinhuiAsyncHttpResponseHandler track_share_link_Handler = new LinhuiAsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data) {
            hideProgressDialog();
            if (data != null && data instanceof JSONObject && JSONObject.parseObject(data.toString()) != null &&
                    JSONObject.parseObject(data.toString()).get("url") != null &&
                    JSONObject.parseObject(data.toString()).get("url").toString().length() > 0) {
                track_share_linkurl = JSONObject.parseObject(data.toString()).get("url").toString();
                share_show(String.valueOf(tracklist_total));
            }
        }

        @Override
        public void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error) {
            hideProgressDialog();
            if (!superresult)
                MessageUtils.showToast(error.getMessage());
            checkAccess_new(error);
        }
    };
}
