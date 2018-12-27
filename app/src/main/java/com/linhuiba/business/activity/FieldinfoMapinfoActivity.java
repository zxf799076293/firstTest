package com.linhuiba.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baselib.app.activity.BaseActivity;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.R;
import com.linhuiba.business.basemvp.BaseMvpActivity;
import com.linhuiba.business.connector.Constants;
import com.linhuiba.business.model.MapSearchModel;
import com.linhuiba.business.util.TitleBarUtils;

import java.net.URISyntaxException;
import java.text.DecimalFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/2/22.
 */

public class FieldinfoMapinfoActivity extends BaseMvpActivity {
    @InjectView(R.id.fieldinfo_mapinfo_layout)
    LinearLayout mfieldinfo_mapinfo_layout;
    @InjectView(R.id.fieldinfomap_resourceaddress)
    TextView mfieldinfomap_resourceaddress;
    @InjectView(R.id.fieldinfomap_resourcename)
    TextView mfieldinfomap_resourcename;
    @InjectView(R.id.fieldinfomap_navigation_btn)
    TextView mfieldinfomap_navigation_btn;
    private MapView mmapinfoview;
    private BaiduMap baiduMap;
    private double latitude;
    private double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fieldinfomapinfo);
        ButterKnife.inject(this);
        mmapinfoview = (MapView)findViewById(R.id.maoinfo_baidumapView);
        baiduMap = mmapinfoview.getMap();
        TitleBarUtils.showBackImg(this,true);
        initview();
    }
    private void initview() {
        Intent resourceinfo = getIntent();
        if (resourceinfo.getExtras() != null) {
            if (resourceinfo.getExtras().get("longitude") != null &&
                    resourceinfo.getExtras().getDouble("longitude") > 0) {
                longitude = resourceinfo.getExtras().getDouble("longitude");
            }
            if (resourceinfo.getExtras().get("latitude") != null &&
                    resourceinfo.getExtras().getDouble("latitude") > 0) {
                latitude = resourceinfo.getExtras().getDouble("latitude");
            }
            if (resourceinfo.getExtras().get("resourcename") != null &&
                    resourceinfo.getExtras().get("resourcename").toString().length() > 0) {
                mfieldinfomap_resourcename.setText(resourceinfo.getExtras().get("resourcename").toString());
                TitleBarUtils.setTitleText(this,resourceinfo.getExtras().get("resourcename").toString());
            }
            if (resourceinfo.getExtras().get("address") != null &&
                    resourceinfo.getExtras().get("address").toString().length() > 0) {
                mfieldinfomap_resourceaddress.setText(resourceinfo.getExtras().get("address").toString());

            }
        }
        if (latitude > 0 && longitude > 0) {
            LatLng latLng = new LatLng(latitude,longitude);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(latLng,16);//设置缩放比例
            baiduMap.setMapStatus(u);
            BitmapDescriptor bitmap = null;
            OverlayOptions overlayOptions = null;
            Marker marker = null;
            TextView button = new TextView(FieldinfoMapinfoActivity.this);
            button.setBackgroundResource(R.drawable.ic_site_one_two_four_one);
            System.gc();
            button.setTextColor(getResources().getColor(R.color.white));
            button.setTextSize(getResources().getDimension(R.dimen.map_marker_textsize));
            button.setGravity(Gravity.CENTER_HORIZONTAL);
            button.setPadding(0, 6, 0, 0);
            bitmap = BitmapDescriptorFactory
                    .fromView(button);
            overlayOptions = new MarkerOptions().position(latLng)
                    .icon(bitmap).zIndex(5);
            //在地图上添加Marker，并显示
            marker = (Marker) (baiduMap.addOverlay(overlayOptions));
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
            System.gc();
        }
        mfieldinfo_mapinfo_layout.setVisibility(View.VISIBLE);
        mfieldinfomap_navigation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.isAvilible(FieldinfoMapinfoActivity.this,"com.baidu.BaiduMap")){//传入指定应用包名
                    try {
//                          intent = Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving®ion=西安&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                        Intent intent = Intent.getIntent("intent://map/direction?" +
                                //"origin=latlng:"+"34.264642646862,108.95108518068&" +   //起点  此处不传值默认选择当前位置
                                "destination=latlng:"+latitude+","+longitude+"|name:"+      //终点
                                mfieldinfomap_resourceaddress.getText().toString()+"&mode=driving&" +          //导航路线方式
                                "region=北京" +           //
                                "&src=慧医#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                        startActivity(intent); //启动调用
                    } catch (URISyntaxException e) {

                    }
                } else if (Constants.isAvilible(FieldinfoMapinfoActivity.this, "com.autonavi.minimap")) {
                    try{
                        Intent intent = Intent.getIntent("androidamap://navi?sourceApplication=amap&lat=" + latitude + "&lon=" + longitude + "&dev=1&style=0");
                        intent.setPackage("com.autonavi.minimap");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    MessageUtils.showToast("您尚未安装地图");
                }
            }
        });

    }
}
