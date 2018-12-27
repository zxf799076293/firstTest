package com.linhuiba.business.model;

import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/27.
 */
public class ApiResourcesModel implements Serializable {
    private String keywords;
    private int has_physical;//有展位 1代表有展位筛选 0或不传代表全部
    private ArrayList<Integer> city_ids;
    private ArrayList<Integer> district_ids;
    private ArrayList<Integer> trading_area_ids;
    private String in_trading_area;//是否含有商圈(1：含有商圈的都会被筛选出来)
    private ArrayList<Integer> community_type_ids;
    private ArrayList<Integer> label_ids;
    private ArrayList<Integer> location_type_ids;
    private ArrayList<Integer> field_cooperation_type_ids;//
    private String min_price;
    private String max_price;
    private Integer min_year;
    private Integer max_year;
    private ArrayList<Integer> developer_ids;
    private String min_area;
    private String max_area;
    private ArrayList<SearchListAttributesModel> attributes;
    private double lat;//地图中心点纬度(不传则表示不限定区域）
    private double lng;//地图中心点经度(不传则表示不限定区域）
    private String min_person_flow;//人流量
    private String max_person_flow;

    //地图所需字段
    private String city_id;
    private double latitude;//地图中心点纬度
    private double longitude;//地图中心点经度
    private double latitude_delta;//纬度范围
    private double longitude_delta;//经度范围
    private int nearby = -1;//附近距离(单位：M,如果传入该参数则忽略latitude_delta、longitude_delta参数,并使用附近筛选,不传则表示不限定范围)
    private float zoom_level;
    private ArrayList<Integer> community_ids;
    private ArrayList<Integer> subway_station_ids;
    private HashMap<String,Object> mSearchAddressBackMap = new HashMap<>();//搜索百度地址的信息
    private boolean mBooleanSearchMapAddressBack;//是否搜索过百度中的地址
    private MapCommunityInfoModel mNearbyInfoModel;//要搜索附近的点model
    //地图所需字段
    private Integer not_need_deposit;//是否有押金(1：无押金)
    private Integer has_subsidy;//是否有优惠(1：有优惠)
    private Integer has_activity;//是否有活动（1：有）
    private ArrayList<Integer> activity_type_ids;
    private ArrayList<Integer> age_level_ids;
    private ArrayList<Integer> indoor;
    private ArrayList<String> facilities;
    private String order_by;
    private String order;
    private Integer navigation;//是否导航栏过来(1：是，0：不是)
    private int page_size;
    private String city_name;
    //动态的参数 动态配置的场地类型 首页
    private String dynamic_name;
    private ArrayList<Integer> dynamic_id;
    private String page;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getHas_physical() {
        return has_physical;
    }

    public void setHas_physical(int has_physical) {
        this.has_physical = has_physical;
    }

    public ArrayList<Integer> getCity_ids() {
        return city_ids;
    }

    public void setCity_ids(ArrayList<Integer> city_ids) {
        this.city_ids = city_ids;
    }

    public ArrayList<Integer> getDistrict_ids() {
        return district_ids;
    }

    public void setDistrict_ids(ArrayList<Integer> district_ids) {
        this.district_ids = district_ids;
    }

    public ArrayList<Integer> getTrading_area_ids() {
        return trading_area_ids;
    }

    public void setTrading_area_ids(ArrayList<Integer> trading_area_ids) {
        this.trading_area_ids = trading_area_ids;
    }

    public String getIn_trading_area() {
        return in_trading_area;
    }

    public void setIn_trading_area(String in_trading_area) {
        this.in_trading_area = in_trading_area;
    }

    public ArrayList<Integer> getCommunity_type_ids() {
        return community_type_ids;
    }

    public void setCommunity_type_ids(ArrayList<Integer> community_type_ids) {
        this.community_type_ids = community_type_ids;
    }

    public ArrayList<Integer> getLabel_ids() {
        return label_ids;
    }

    public void setLabel_ids(ArrayList<Integer> label_ids) {
        this.label_ids = label_ids;
    }

    public ArrayList<Integer> getLocation_type_ids() {
        return location_type_ids;
    }

    public void setLocation_type_ids(ArrayList<Integer> location_type_ids) {
        this.location_type_ids = location_type_ids;
    }

    public ArrayList<Integer> getField_cooperation_type_ids() {
        return field_cooperation_type_ids;
    }

    public void setField_cooperation_type_ids(ArrayList<Integer> field_cooperation_type_ids) {
        this.field_cooperation_type_ids = field_cooperation_type_ids;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public Integer getMin_year() {
        return min_year;
    }

    public void setMin_year(Integer min_year) {
        this.min_year = min_year;
    }

    public Integer getMax_year() {
        return max_year;
    }

    public void setMax_year(Integer max_year) {
        this.max_year = max_year;
    }

    public ArrayList<Integer> getDeveloper_ids() {
        return developer_ids;
    }

    public void setDeveloper_ids(ArrayList<Integer> developer_ids) {
        this.developer_ids = developer_ids;
    }

    public String getMin_area() {
        return min_area;
    }

    public void setMin_area(String min_area) {
        this.min_area = min_area;
    }

    public String getMax_area() {
        return max_area;
    }

    public void setMax_area(String max_area) {
        this.max_area = max_area;
    }

    public ArrayList<SearchListAttributesModel> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<SearchListAttributesModel> attributes) {
        this.attributes = attributes;
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

    public String getMin_person_flow() {
        return min_person_flow;
    }

    public void setMin_person_flow(String min_person_flow) {
        this.min_person_flow = min_person_flow;
    }

    public String getMax_person_flow() {
        return max_person_flow;
    }

    public void setMax_person_flow(String max_person_flow) {
        this.max_person_flow = max_person_flow;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
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

    public ArrayList<Integer> getCommunity_ids() {
        return community_ids;
    }

    public void setCommunity_ids(ArrayList<Integer> community_ids) {
        this.community_ids = community_ids;
    }

    public ArrayList<Integer> getSubway_station_ids() {
        return subway_station_ids;
    }

    public void setSubway_station_ids(ArrayList<Integer> subway_station_ids) {
        this.subway_station_ids = subway_station_ids;
    }

    public HashMap<String, Object> getSearchAddressBackMap() {
        return mSearchAddressBackMap;
    }

    public void setSearchAddressBackMap(HashMap<String, Object> searchAddressBackMap) {
        mSearchAddressBackMap = searchAddressBackMap;
    }

    public boolean isBooleanSearchMapAddressBack() {
        return mBooleanSearchMapAddressBack;
    }

    public void setBooleanSearchMapAddressBack(boolean booleanSearchMapAddressBack) {
        mBooleanSearchMapAddressBack = booleanSearchMapAddressBack;
    }

    public MapCommunityInfoModel getNearbyInfoModel() {
        return mNearbyInfoModel;
    }

    public void setNearbyInfoModel(MapCommunityInfoModel nearbyInfoModel) {
        mNearbyInfoModel = nearbyInfoModel;
    }

    public Integer getNot_need_deposit() {
        return not_need_deposit;
    }

    public void setNot_need_deposit(Integer not_need_deposit) {
        this.not_need_deposit = not_need_deposit;
    }

    public Integer getHas_subsidy() {
        return has_subsidy;
    }

    public void setHas_subsidy(Integer has_subsidy) {
        this.has_subsidy = has_subsidy;
    }

    public Integer getHas_activity() {
        return has_activity;
    }

    public void setHas_activity(Integer has_activity) {
        this.has_activity = has_activity;
    }

    public ArrayList<Integer> getActivity_type_ids() {
        return activity_type_ids;
    }

    public void setActivity_type_ids(ArrayList<Integer> activity_type_ids) {
        this.activity_type_ids = activity_type_ids;
    }

    public ArrayList<Integer> getAge_level_ids() {
        return age_level_ids;
    }

    public void setAge_level_ids(ArrayList<Integer> age_level_ids) {
        this.age_level_ids = age_level_ids;
    }

    public ArrayList<Integer> getIndoor() {
        return indoor;
    }

    public void setIndoor(ArrayList<Integer> indoor) {
        this.indoor = indoor;
    }

    public ArrayList<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(ArrayList<String> facilities) {
        this.facilities = facilities;
    }

    public String getOrder_by() {
        return order_by;
    }

    public void setOrder_by(String order_by) {
        this.order_by = order_by;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getNavigation() {
        return navigation;
    }

    public void setNavigation(Integer navigation) {
        this.navigation = navigation;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getDynamic_name() {
        return dynamic_name;
    }

    public void setDynamic_name(String dynamic_name) {
        this.dynamic_name = dynamic_name;
    }

    public ArrayList<Integer> getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(ArrayList<Integer> dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
