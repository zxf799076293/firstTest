package com.linhuiba.business.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.linhuiba.business.R;
import com.linhuiba.linhuifield.connector.Constants;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/18.
 */

public class ResourceMapModel implements ClusterItem,Serializable {
    private final LatLng mPosition;
    private Context context;

    private String title;
    private int resource_id;
    private String image;
    private String address;
    private String do_location;
    private int number_of_people;
    private int preferential_price;
    private double latitude;
    private double longitude;
    private int count;
    private String unit;
    private int order_count;
    public ResourceMapModel(LatLng latLng, Context context) {
        this.mPosition = latLng;
        this.context = context;
    }
    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.activity_baidumap_resourcemap_marker, null);
        TextView textView = (TextView) view.findViewById(R.id.resourcemap_marker_textview);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.resourcemap_marker_layout);
        if (count > 0) {
            linearLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.map_marker_bg));
            textView.setText(title+ "\n" + String.valueOf(count));
        } else {
            textView.setText(com.linhuiba.linhuifield.connector.Constants.getPriceUnitStr(context,context.getResources().getString(R.string.order_listitem_price_unit_text)+
                    com.linhuiba.business.connector.Constants.getpricestring(String.valueOf(preferential_price),0.01),9));
        }
        return BitmapDescriptorFactory
                .fromView(view);
    }

    public LatLng getmPosition() {
        return mPosition;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDo_location() {
        return do_location;
    }

    public void setDo_location(String do_location) {
        this.do_location = do_location;
    }

    public int getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(int number_of_people) {
        this.number_of_people = number_of_people;
    }

    public int getPreferential_price() {
        return preferential_price;
    }

    public void setPreferential_price(int preferential_price) {
        this.preferential_price = preferential_price;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getOrder_count() {
        return order_count;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }
}
