package com.linhuiba.business.model;

import android.app.Dialog;

import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.io.Serializable;
import java.util.ArrayList;

public class MapCommunityInfoModel implements Serializable {
    private String title;
    private int resource_id;// 区域id
    private Double latitude;//纬度
    private Double longitude;//经度
    private int count;//场地总数 //点位聚合下的场地总数
    private ArrayList<Integer> resource_ids;// 点位聚合的场地id
    //不聚合
    private int id;
    private String name; // 聚合商圈name
    private String detailed_address;
    private Double lat;
    private Double lng;
    private Integer order_quantity;// 销量
    private String build_year;//建筑年份
    private int has_carport;//是否有车位  1/有车位  0/无车位
    private int physical_resources_count;//展位数量
    private String display_price;//价格  (可直接显示) 面议
    private String community_img;
    private Field_AddResourceCreateItemModel community_type;
    //聚合商圈
    private String center;
    private Double minLat;
    private Double maxLat;
    private Double minLng;
    private Double maxLng;
    private String distance;
    //地图所有展位
    private int community_id;
    private String discount;//折扣
    private String position;//室内/室外 标签
    private String deposit;
    private String number_of_people;
    private int number_of_order;
    private int is_subsidy;
    private int has_coupons;
    private Field_AddResourceCreateItemModel physical_resource_first_img;


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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<Integer> getResource_ids() {
        return resource_ids;
    }

    public void setResource_ids(ArrayList<Integer> resource_ids) {
        this.resource_ids = resource_ids;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetailed_address() {
        return detailed_address;
    }

    public void setDetailed_address(String detailed_address) {
        this.detailed_address = detailed_address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getOrder_quantity() {
        return order_quantity;
    }

    public void setOrder_quantity(Integer order_quantity) {
        this.order_quantity = order_quantity;
    }

    public String getBuild_year() {
        return build_year;
    }

    public void setBuild_year(String build_year) {
        this.build_year = build_year;
    }

    public int getHas_carport() {
        return has_carport;
    }

    public void setHas_carport(int has_carport) {
        this.has_carport = has_carport;
    }

    public int getPhysical_resources_count() {
        return physical_resources_count;
    }

    public void setPhysical_resources_count(int physical_resources_count) {
        this.physical_resources_count = physical_resources_count;
    }

    public String getDisplay_price() {
        return display_price;
    }

    public void setDisplay_price(String display_price) {
        this.display_price = display_price;
    }

    public String getCommunity_img() {
        return community_img;
    }

    public void setCommunity_img(String community_img) {
        this.community_img = community_img;
    }

    public Field_AddResourceCreateItemModel getCommunity_type() {
        return community_type;
    }

    public void setCommunity_type(Field_AddResourceCreateItemModel community_type) {
        this.community_type = community_type;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public Double getMinLat() {
        return minLat;
    }

    public void setMinLat(Double minLat) {
        this.minLat = minLat;
    }

    public Double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(Double maxLat) {
        this.maxLat = maxLat;
    }

    public Double getMinLng() {
        return minLng;
    }

    public void setMinLng(Double minLng) {
        this.minLng = minLng;
    }

    public Double getMaxLng() {
        return maxLng;
    }

    public void setMaxLng(Double maxLng) {
        this.maxLng = maxLng;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(int community_id) {
        this.community_id = community_id;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(String number_of_people) {
        this.number_of_people = number_of_people;
    }

    public int getNumber_of_order() {
        return number_of_order;
    }

    public void setNumber_of_order(int number_of_order) {
        this.number_of_order = number_of_order;
    }

    public int getIs_subsidy() {
        return is_subsidy;
    }

    public void setIs_subsidy(int is_subsidy) {
        this.is_subsidy = is_subsidy;
    }

    public int getHas_coupons() {
        return has_coupons;
    }

    public void setHas_coupons(int has_coupons) {
        this.has_coupons = has_coupons;
    }

    public Field_AddResourceCreateItemModel getPhysical_resource_first_img() {
        return physical_resource_first_img;
    }

    public void setPhysical_resource_first_img(Field_AddResourceCreateItemModel physical_resource_first_img) {
        this.physical_resource_first_img = physical_resource_first_img;
    }
}
