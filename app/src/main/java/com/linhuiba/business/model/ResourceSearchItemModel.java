package com.linhuiba.business.model;

import android.content.Intent;

import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/13.
 */
public class ResourceSearchItemModel {
    private int resource_id;
    private String resource_name;
    private String address;
    private int score;
    private String price;//分
    private String price_unit;
    private int reviewed_count;
    private int order_count;
    private String pic_url;
    private String relation;
    private String subsidy_fee;
    private String unit;
    private String lift;
    private String frame;
    private String number_of_people;
    private String field_type;
    private String ad_type;
    private String district;
    private String ad_print_type;
    private String occupancy_rate;
    private int HeaderId;
    private String resource_type;
    private String deadline;
    private String activity_start_date;
    private boolean expired;
    private double lat;
    private double lng;
    private Integer cooperation_type_id;//12 是自营 13:代理 14：物业
    private String station = "";
    private String distance;//附近几公里
    private Integer is_enquiry;//0表示不是询价场地，1表示询价场地
    private Integer refer_min_price;//询价最小值
    private Integer refer_max_price;//询价最大值
    //同社区其他资源字段
    private Integer id;
    private String name;
    private int sort;
    //火爆拼团
    private Integer number_of_group_purchase_now;
    //首页显示
    private int res_type_id;
    private int is_hot;
    private Double deposit;
    private String subsidy_str;
    //服务商
    private String company;
    private String office_location;
    private String mobile;
    private Field_AddResourceCreateItemModel city = new Field_AddResourceCreateItemModel();
    private ArrayList<Field_AddResourceCreateItemModel> many_service_items = new ArrayList<>();
    //新的字段
    private ArrayList<Field_AddResourceCreateItemModel> labels = new ArrayList<>();
    private int physical_resource_counts;
    private int order_quantity;
    private int is_subsidy;
    private int has_coupons;// 是否有优惠券  0/否 1/是
    private String floor_price;
    private Field_AddResourceCreateItemModel community_img;
    private Integer top_physical_id;//跳转的资源（展位/供给 ：根据type类型决定 （3.8不做接口升级，字段名延用当前字段名））ID （null 表示无展位）
    private int is_fast_booking;//是否快速定 0/否 1/是
    private String category;
    private String type;//跳转详情类型  （community_resource：场地、physical_resource：展位、selling_resource：供给（活动详情））
    private int has_selling;//展位有供给
    private String subsidy_price;//展位补贴后价格
    private String community;//场地名称
    private boolean is_only_enquiry;//展位是否是询价
    private String total_area;//面积
    private Field_AddResourceCreateItemModel location_type;//位置类型
    //活动
    private int number_of_order;
    private String min_price;
    private boolean sign_up_end;
    private String activity_title;
    private String district_name;
    private String detailed_address;
    private String activity_type;
    private int physical_resource_id;
    private int selling_resource_id;
    private int community_id;//场地id
    private boolean is_only_activity;//是否只有活动供给
    private Integer activity_id;//活动id（可能为null）

    //同场地其他展位
    private Field_AddResourceCreateItemModel physical_resource_first_img;
    private Integer indoor;
    private int selected;
    //详情页的展位下的活动
    private String custom_name;//活动名称
    private String selling_resource_img;//活动图片 （默认为 null）

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }
    public String getResource_name() {
        return resource_name;
    }

    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_unit() {
        return price_unit;
    }

    public void setPrice_unit(String price_unit) {
        this.price_unit = price_unit;
    }

    public int getReviewed_count() {
        return reviewed_count;
    }

    public void setReviewed_count(int reviewed_count) {
        this.reviewed_count = reviewed_count;
    }

    public int getOrder_count() {
        return order_count;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getSubsidy_fee() {
        return subsidy_fee;
    }

    public void setSubsidy_fee(String subsidy_fee) {
        this.subsidy_fee = subsidy_fee;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLift() {
        return lift;
    }

    public void setLift(String lift) {
        this.lift = lift;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(String number_of_people) {
        this.number_of_people = number_of_people;
    }

    public String getField_type() {
        return field_type;
    }

    public void setField_type(String field_type) {
        this.field_type = field_type;
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAd_print_type() {
        return ad_print_type;
    }

    public void setAd_print_type(String ad_print_type) {
        this.ad_print_type = ad_print_type;
    }

    public String getOccupancy_rate() {
        return occupancy_rate;
    }

    public void setOccupancy_rate(String occupancy_rate) {
        this.occupancy_rate = occupancy_rate;
    }

    public int getHeaderId() {
        return HeaderId;
    }

    public void setHeaderId(int headerId) {
        HeaderId = headerId;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getActivity_start_date() {
        return activity_start_date;
    }

    public void setActivity_start_date(String activity_start_date) {
        this.activity_start_date = activity_start_date;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
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

    public Integer getCooperation_type_id() {
        return cooperation_type_id;
    }

    public void setCooperation_type_id(Integer cooperation_type_id) {
        this.cooperation_type_id = cooperation_type_id;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Integer getIs_enquiry() {
        return is_enquiry;
    }

    public void setIs_enquiry(Integer is_enquiry) {
        this.is_enquiry = is_enquiry;
    }

    public Integer getRefer_min_price() {
        return refer_min_price;
    }

    public void setRefer_min_price(Integer refer_min_price) {
        this.refer_min_price = refer_min_price;
    }

    public boolean isSign_up_end() {
        return sign_up_end;
    }

    public void setSign_up_end(boolean sign_up_end) {
        this.sign_up_end = sign_up_end;
    }

    public String getActivity_title() {
        return activity_title;
    }

    public void setActivity_title(String activity_title) {
        this.activity_title = activity_title;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getDetailed_address() {
        return detailed_address;
    }

    public void setDetailed_address(String detailed_address) {
        this.detailed_address = detailed_address;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public int getPhysical_resource_id() {
        return physical_resource_id;
    }

    public void setPhysical_resource_id(int physical_resource_id) {
        this.physical_resource_id = physical_resource_id;
    }

    public int getSelling_resource_id() {
        return selling_resource_id;
    }

    public void setSelling_resource_id(int selling_resource_id) {
        this.selling_resource_id = selling_resource_id;
    }

    public int getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(int community_id) {
        this.community_id = community_id;
    }

    public boolean isIs_only_activity() {
        return is_only_activity;
    }

    public void setIs_only_activity(boolean is_only_activity) {
        this.is_only_activity = is_only_activity;
    }

    public Integer getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(Integer activity_id) {
        this.activity_id = activity_id;
    }

    public Field_AddResourceCreateItemModel getPhysical_resource_first_img() {
        return physical_resource_first_img;
    }

    public void setPhysical_resource_first_img(Field_AddResourceCreateItemModel physical_resource_first_img) {
        this.physical_resource_first_img = physical_resource_first_img;
    }

    public Integer getIndoor() {
        return indoor;
    }

    public void setIndoor(Integer indoor) {
        this.indoor = indoor;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public String getCustom_name() {
        return custom_name;
    }

    public void setCustom_name(String custom_name) {
        this.custom_name = custom_name;
    }

    public String getSelling_resource_img() {
        return selling_resource_img;
    }

    public void setSelling_resource_img(String selling_resource_img) {
        this.selling_resource_img = selling_resource_img;
    }

    public Integer getRefer_max_price() {
        return refer_max_price;
    }

    public void setRefer_max_price(Integer refer_max_price) {
        this.refer_max_price = refer_max_price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Integer getNumber_of_group_purchase_now() {
        return number_of_group_purchase_now;
    }

    public void setNumber_of_group_purchase_now(Integer number_of_group_purchase_now) {
        this.number_of_group_purchase_now = number_of_group_purchase_now;
    }

    public int getRes_type_id() {
        return res_type_id;
    }

    public void setRes_type_id(int res_type_id) {
        this.res_type_id = res_type_id;
    }

    public int getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(int is_hot) {
        this.is_hot = is_hot;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public String getSubsidy_str() {
        return subsidy_str;
    }

    public void setSubsidy_str(String subsidy_str) {
        this.subsidy_str = subsidy_str;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getOffice_location() {
        return office_location;
    }

    public void setOffice_location(String office_location) {
        this.office_location = office_location;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Field_AddResourceCreateItemModel getCity() {
        return city;
    }

    public void setCity(Field_AddResourceCreateItemModel city) {
        this.city = city;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getMany_service_items() {
        return many_service_items;
    }

    public void setMany_service_items(ArrayList<Field_AddResourceCreateItemModel> many_service_items) {
        this.many_service_items = many_service_items;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<Field_AddResourceCreateItemModel> labels) {
        this.labels = labels;
    }

    public int getPhysical_resource_counts() {
        return physical_resource_counts;
    }

    public void setPhysical_resource_counts(int physical_resource_counts) {
        this.physical_resource_counts = physical_resource_counts;
    }

    public int getOrder_quantity() {
        return order_quantity;
    }

    public void setOrder_quantity(int order_quantity) {
        this.order_quantity = order_quantity;
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

    public String getFloor_price() {
        return floor_price;
    }

    public void setFloor_price(String floor_price) {
        this.floor_price = floor_price;
    }

    public Field_AddResourceCreateItemModel getCommunity_img() {
        return community_img;
    }

    public void setCommunity_img(Field_AddResourceCreateItemModel community_img) {
        this.community_img = community_img;
    }

    public Integer getTop_physical_id() {
        return top_physical_id;
    }

    public void setTop_physical_id(Integer top_physical_id) {
        this.top_physical_id = top_physical_id;
    }

    public int getIs_fast_booking() {
        return is_fast_booking;
    }

    public void setIs_fast_booking(int is_fast_booking) {
        this.is_fast_booking = is_fast_booking;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubsidy_price() {
        return subsidy_price;
    }

    public void setSubsidy_price(String subsidy_price) {
        this.subsidy_price = subsidy_price;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public boolean isIs_only_enquiry() {
        return is_only_enquiry;
    }

    public void setIs_only_enquiry(boolean is_only_enquiry) {
        this.is_only_enquiry = is_only_enquiry;
    }

    public String getTotal_area() {
        return total_area;
    }

    public void setTotal_area(String total_area) {
        this.total_area = total_area;
    }

    public Field_AddResourceCreateItemModel getLocation_type() {
        return location_type;
    }

    public void setLocation_type(Field_AddResourceCreateItemModel location_type) {
        this.location_type = location_type;
    }

    public int getHas_selling() {
        return has_selling;
    }

    public void setHas_selling(int has_selling) {
        this.has_selling = has_selling;
    }

    public int getNumber_of_order() {
        return number_of_order;
    }

    public void setNumber_of_order(int number_of_order) {
        this.number_of_order = number_of_order;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }
}
