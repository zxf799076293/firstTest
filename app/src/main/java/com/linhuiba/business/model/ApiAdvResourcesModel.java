package com.linhuiba.business.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ApiAdvResourcesModel implements Serializable {
    private String keywords;
    private String city_id;
    private String city_name;
    private String resource_type;
    private ArrayList<Integer> field_type_id;
    private ArrayList<Integer> ad_type_id;
    private ArrayList<Integer> activity_type_id;
    private ArrayList<Integer> district_id;
    private ArrayList<Integer> trading_area_id;
    private ArrayList<Integer> community_type_id;
    private ArrayList<Integer> indoor;
    private ArrayList<String> facilities;
    private ArrayList<Integer> label_id;
    private ArrayList<Integer> subway_station_id;//地铁

    private String lowPrice;
    private String highPrice;
    private String minimum_peoples;
    private String maximum_peoples;
    private String minimum_build_year;
    private String maximum_build_year;
    private String minimum_households;
    private String maximum_households;
    private String minimum_property_costs;
    private String maximum_property_costs;
    private String minimum_house_price;
    private String maximum_house_price;
    private String minimum_area;
    private String maximum_area;
    private String order;
    private String order_by;
    private String page;
    private String pageSize;
    private int hot;
    private int subsidy;
    private int is_home_page;
    private Integer self_support;
    //专题
    private int id;
    private String name;
    private String description;
    private String pic_url;
    private String link;
    private String created_at;
    private String lease_term_type_id;

    //地图所需字段
    private double latitude;
    private double longitude;
    private double latitude_delta;
    private double longitude_delta;
    private int nearby = -1;
    private float zoom_level;
    private HashMap<String,Object> mSearchAddressBackMap = new HashMap<>();//搜索百度地址的信息
    private boolean mBooleanSearchMapAddressBack;//是否搜索过百度中的地址
    //    private Marker mMapLocationMarker;//搜索百度地址后的marker
//    private LatLng mNearbyStatusLatLng;//要搜索附近的点的坐标
    private MapSearchModel mNearbyInfoModel;//要搜索附近的点model
    private double lat;//当前用户位置的纬度
    private double lng;//当前用户位置的纬度
    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public ArrayList<Integer> getField_type_id() {
        return field_type_id;
    }

    public void setField_type_id(ArrayList<Integer> field_type_id) {
        this.field_type_id = field_type_id;
    }

    public ArrayList<Integer> getAd_type_id() {
        return ad_type_id;
    }

    public void setAd_type_id(ArrayList<Integer> ad_type_id) {
        this.ad_type_id = ad_type_id;
    }

    public ArrayList<Integer> getActivity_type_id() {
        return activity_type_id;
    }

    public void setActivity_type_id(ArrayList<Integer> activity_type_id) {
        this.activity_type_id = activity_type_id;
    }

    public ArrayList<Integer> getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(ArrayList<Integer> district_id) {
        this.district_id = district_id;
    }

    public ArrayList<Integer> getTrading_area_id() {
        return trading_area_id;
    }

    public void setTrading_area_id(ArrayList<Integer> trading_area_id) {
        this.trading_area_id = trading_area_id;
    }

    public ArrayList<Integer> getCommunity_type_id() {
        return community_type_id;
    }

    public void setCommunity_type_id(ArrayList<Integer> community_type_id) {
        this.community_type_id = community_type_id;
    }

    public ArrayList<Integer> getIndoor() {
        return indoor;
    }

    public void setIndoor(ArrayList<Integer> indoor) {
        this.indoor = indoor;
    }

    public ArrayList<Integer> getLabel_id() {
        return label_id;
    }

    public void setLabel_id(ArrayList<Integer> label_id) {
        this.label_id = label_id;
    }

    public ArrayList<Integer> getSubway_station_id() {
        return subway_station_id;
    }

    public void setSubway_station_id(ArrayList<Integer> subway_station_id) {
        this.subway_station_id = subway_station_id;
    }

    public ArrayList<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(ArrayList<String> facilities) {
        this.facilities = facilities;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public String getMinimum_peoples() {
        return minimum_peoples;
    }

    public void setMinimum_peoples(String minimum_peoples) {
        this.minimum_peoples = minimum_peoples;
    }

    public String getMaximum_peoples() {
        return maximum_peoples;
    }

    public void setMaximum_peoples(String maximum_peoples) {
        this.maximum_peoples = maximum_peoples;
    }

    public String getMinimum_build_year() {
        return minimum_build_year;
    }

    public void setMinimum_build_year(String minimum_build_year) {
        this.minimum_build_year = minimum_build_year;
    }

    public String getMaximum_build_year() {
        return maximum_build_year;
    }

    public void setMaximum_build_year(String maximum_build_year) {
        this.maximum_build_year = maximum_build_year;
    }

    public String getMinimum_households() {
        return minimum_households;
    }

    public void setMinimum_households(String minimum_households) {
        this.minimum_households = minimum_households;
    }

    public String getMaximum_households() {
        return maximum_households;
    }

    public void setMaximum_households(String maximum_households) {
        this.maximum_households = maximum_households;
    }

    public String getMinimum_property_costs() {
        return minimum_property_costs;
    }

    public void setMinimum_property_costs(String minimum_property_costs) {
        this.minimum_property_costs = minimum_property_costs;
    }

    public String getMaximum_property_costs() {
        return maximum_property_costs;
    }

    public void setMaximum_property_costs(String maximum_property_costs) {
        this.maximum_property_costs = maximum_property_costs;
    }

    public String getMinimum_house_price() {
        return minimum_house_price;
    }

    public void setMinimum_house_price(String minimum_house_price) {
        this.minimum_house_price = minimum_house_price;
    }

    public String getMaximum_house_price() {
        return maximum_house_price;
    }

    public void setMaximum_house_price(String maximum_house_price) {
        this.maximum_house_price = maximum_house_price;
    }

    public String getMinimum_area() {
        return minimum_area;
    }

    public void setMinimum_area(String minimum_area) {
        this.minimum_area = minimum_area;
    }

    public String getMaximum_area() {
        return maximum_area;
    }

    public void setMaximum_area(String maximum_area) {
        this.maximum_area = maximum_area;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrder_by() {
        return order_by;
    }

    public void setOrder_by(String order_by) {
        this.order_by = order_by;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(int subsidy) {
        this.subsidy = subsidy;
    }

    public int getIs_home_page() {
        return is_home_page;
    }

    public void setIs_home_page(int is_home_page) {
        this.is_home_page = is_home_page;
    }

    public Integer getSelf_support() {
        return self_support;
    }

    public void setSelf_support(Integer self_support) {
        this.self_support = self_support;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLease_term_type_id() {
        return lease_term_type_id;
    }

    public void setLease_term_type_id(String lease_term_type_id) {
        this.lease_term_type_id = lease_term_type_id;
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

    public double getLatitude_delta() {
        return latitude_delta;
    }

    public void setLatitude_delta(double latitude_delta) {
        this.latitude_delta = latitude_delta;
    }

    public double getLongitude_delta() {
        return longitude_delta;
    }

    public void setLongitude_delta(double longitude_delta) {
        this.longitude_delta = longitude_delta;
    }

    public int getNearby() {
        return nearby;
    }

    public void setNearby(int nearby) {
        this.nearby = nearby;
    }

    public float getZoom_level() {
        return zoom_level;
    }

    public void setZoom_level(float zoom_level) {
        this.zoom_level = zoom_level;
    }

    public HashMap<String, Object> getmSearchAddressBackMap() {
        return mSearchAddressBackMap;
    }

    public void setmSearchAddressBackMap(HashMap<String, Object> mSearchAddressBackMap) {
        this.mSearchAddressBackMap = mSearchAddressBackMap;
    }

    public boolean ismBooleanSearchMapAddressBack() {
        return mBooleanSearchMapAddressBack;
    }

    public void setmBooleanSearchMapAddressBack(boolean mBooleanSearchMapAddressBack) {
        this.mBooleanSearchMapAddressBack = mBooleanSearchMapAddressBack;
    }

//    public Marker getmMapLocationMarker() {
//        return mMapLocationMarker;
//    }
//
//    public void setmMapLocationMarker(Marker mMapLocationMarker) {
//        this.mMapLocationMarker = mMapLocationMarker;
//    }

//    public LatLng getmNearbyStatusLatLng() {
//        return mNearbyStatusLatLng;
//    }
//
//    public void setmNearbyStatusLatLng(LatLng mNearbyStatusLatLng) {
//        this.mNearbyStatusLatLng = mNearbyStatusLatLng;
//    }

    public MapSearchModel getmNearbyInfoModel() {
        return mNearbyInfoModel;
    }

    public void setmNearbyInfoModel(MapSearchModel mNearbyInfoModel) {
        this.mNearbyInfoModel = mNearbyInfoModel;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
