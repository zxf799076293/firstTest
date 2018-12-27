package com.linhuiba.business.fieldmodel;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/6.
 */
public class Field_AddResourcesModel {
    private String name;
    private String mobile;
    private String field_type_id;
    private String community_id;
    private String capacity;
    private String number_of_people;
    private String do_begin;
    private String do_finish;
    private String do_location;
    private String has_power;
    private String has_chair;
    private String allow_stay_night;
    private String allow_leaflet;
    private String ticket;
    private String tax_point;
    private String contraband;
    private String description;
    private ArrayList<String> images;
    private String res_type_id;
    private ArrayList<Map<Object,Object>> dimensions;
    private ArrayList<Map<Object,Object>> newdimensions;


    private String discount_rate;
    private String ad_type_id;
    private String unit;
    private String lift;
    private String frame;




    private String fixed_price;
    private String restaurant;
    private String facilities;
    private String enterprises;
    private String park;
    private String participation;
    private String age_group;
    private String gender_ratio;
    private String sales_volume;
    private String consumption_level;
    private String property_requirement;

    private String resource_id;
    private String resource_type;
    private String resource_name;
    private String price;
    private String power;
    private String chairs;
    private String goodshelp;
    private String leaflet;
    private String time;
    private String doLocation;
    private String address;
    private ArrayList<String> pic_url;
    private String field_type;
    private String ad_type;
    private String subsidy_fee;
    private String build_year;
    private String property_costs;
    private String number_of_company;
    private String rates;
    private String households;
    private String linhui_discount_rate;
    private String adv_type_id;
    private String province_id;
    private String city_id;
    private String district_id;
    private String community_name;
    private String company_comment;
    private String closing_dates;
    private String deadline;
    private String total_area;


    static Field_AddResourcesModel sInstance;
    Field_AddResourcesModel() { }
    public static Field_AddResourcesModel getInstance() {
        if (sInstance == null) {
            sInstance = new Field_AddResourcesModel();
        }
        return sInstance;
    }
    public static Field_AddResourcesModel getnewInstance() {
        sInstance = new Field_AddResourcesModel();
        return sInstance;
    }
    public static Field_AddResourcesModel getnewInstance(Field_AddResourcesModel field_addResourcesModel) {
        sInstance = new Field_AddResourcesModel();
        sInstance = field_addResourcesModel;
        return sInstance;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getField_type_id() {
        return field_type_id;
    }

    public void setField_type_id(String field_type_id) {
        this.field_type_id = field_type_id;
    }

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(String discount_rate) {
        this.discount_rate = discount_rate;
    }

    public String getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(String number_of_people) {
        this.number_of_people = number_of_people;
    }

    public String getContraband() {
        return contraband;
    }

    public void setContraband(String contraband) {
        this.contraband = contraband;
    }

    public String getDo_begin() {
        return do_begin;
    }

    public void setDo_begin(String do_begin) {
        this.do_begin = do_begin;
    }

    public String getDo_finish() {
        return do_finish;
    }

    public void setDo_finish(String do_finish) {
        this.do_finish = do_finish;
    }

    public String getDo_location() {
        return do_location;
    }

    public void setDo_location(String do_location) {
        this.do_location = do_location;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTax_point() {
        return tax_point;
    }

    public void setTax_point(String tax_point) {
        this.tax_point = tax_point;
    }

    public String getHas_power() {
        return has_power;
    }

    public void setHas_power(String has_power) {
        this.has_power = has_power;
    }

    public String getHas_chair() {
        return has_chair;
    }

    public void setHas_chair(String has_chair) {
        this.has_chair = has_chair;
    }

    public String getAllow_stay_night() {
        return allow_stay_night;
    }

    public void setAllow_stay_night(String allow_stay_night) {
        this.allow_stay_night = allow_stay_night;
    }

    public String getAllow_leaflet() {
        return allow_leaflet;
    }

    public void setAllow_leaflet(String allow_leaflet) {
        this.allow_leaflet = allow_leaflet;
    }

    public String getRes_type_id() {
        return res_type_id;
    }

    public void setRes_type_id(String res_type_id) {
        this.res_type_id = res_type_id;
    }

    public String getAd_type_id() {
        return ad_type_id;
    }

    public void setAd_type_id(String ad_type_id) {
        this.ad_type_id = ad_type_id;
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

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<Map<Object,Object>> getDimensions() {
        return dimensions;
    }

    public void setDimensions(ArrayList<Map<Object,Object>> dimensions) {
        this.dimensions = dimensions;
    }

    public ArrayList<Map<Object, Object>> getNewdimensions() {
        return newdimensions;
    }

    public void setNewdimensions(ArrayList<Map<Object, Object>> newdimensions) {
        this.newdimensions = newdimensions;
    }

    public String getFixed_price() {
        return fixed_price;
    }

    public void setFixed_price(String fixed_price) {
        this.fixed_price = fixed_price;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public String getEnterprises() {
        return enterprises;
    }

    public void setEnterprises(String enterprises) {
        this.enterprises = enterprises;
    }

    public String getPark() {
        return park;
    }

    public void setPark(String park) {
        this.park = park;
    }

    public String getParticipation() {
        return participation;
    }

    public void setParticipation(String participation) {
        this.participation = participation;
    }

    public String getAge_group() {
        return age_group;
    }

    public void setAge_group(String age_group) {
        this.age_group = age_group;
    }

    public String getGender_ratio() {
        return gender_ratio;
    }

    public void setGender_ratio(String gender_ratio) {
        this.gender_ratio = gender_ratio;
    }

    public String getSales_volume() {
        return sales_volume;
    }

    public void setSales_volume(String sales_volume) {
        this.sales_volume = sales_volume;
    }

    public String getConsumption_level() {
        return consumption_level;
    }

    public void setConsumption_level(String consumption_level) {
        this.consumption_level = consumption_level;
    }

    public String getProperty_requirement() {
        return property_requirement;
    }

    public void setProperty_requirement(String property_requirement) {
        this.property_requirement = property_requirement;
    }

    public String getResource_id() {
        return resource_id;
    }

    public void setResource_id(String resource_id) {
        this.resource_id = resource_id;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public String getResource_name() {
        return resource_name;
    }

    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getChairs() {
        return chairs;
    }

    public void setChairs(String chairs) {
        this.chairs = chairs;
    }

    public String getGoodshelp() {
        return goodshelp;
    }

    public void setGoodshelp(String goodshelp) {
        this.goodshelp = goodshelp;
    }

    public String getLeaflet() {
        return leaflet;
    }

    public void setLeaflet(String leaflet) {
        this.leaflet = leaflet;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoLocation() {
        return doLocation;
    }

    public void setDoLocation(String doLocation) {
        this.doLocation = doLocation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getPic_url() {
        return pic_url;
    }

    public void setPic_url(ArrayList<String> pic_url) {
        this.pic_url = pic_url;
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

    public String getSubsidy_fee() {
        return subsidy_fee;
    }

    public void setSubsidy_fee(String subsidy_fee) {
        this.subsidy_fee = subsidy_fee;
    }

    public String getBuild_year() {
        return build_year;
    }

    public void setBuild_year(String build_year) {
        this.build_year = build_year;
    }

    public String getProperty_costs() {
        return property_costs;
    }

    public void setProperty_costs(String property_costs) {
        this.property_costs = property_costs;
    }

    public String getNumber_of_company() {
        return number_of_company;
    }

    public void setNumber_of_company(String number_of_company) {
        this.number_of_company = number_of_company;
    }

    public String getRates() {
        return rates;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }

    public String getHouseholds() {
        return households;
    }

    public void setHouseholds(String households) {
        this.households = households;
    }

    public String getLinhui_discount_rate() {
        return linhui_discount_rate;
    }

    public void setLinhui_discount_rate(String linhui_discount_rate) {
        this.linhui_discount_rate = linhui_discount_rate;
    }

    public String getAdv_type_id() {
        return adv_type_id;
    }

    public void setAdv_type_id(String adv_type_id) {
        this.adv_type_id = adv_type_id;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public String getCompany_comment() {
        return company_comment;
    }

    public void setCompany_comment(String company_comment) {
        this.company_comment = company_comment;
    }

    public String getClosing_dates() {
        return closing_dates;
    }

    public void setClosing_dates(String closing_dates) {
        this.closing_dates = closing_dates;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getTotal_area() {
        return total_area;
    }

    public void setTotal_area(String total_area) {
        this.total_area = total_area;
    }

    public static Field_AddResourcesModel getsInstance() {
        return sInstance;
    }

    public static void setsInstance(Field_AddResourcesModel sInstance) {
        Field_AddResourcesModel.sInstance = sInstance;
    }
}
