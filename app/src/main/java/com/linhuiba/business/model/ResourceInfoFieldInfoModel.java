package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/26.
 */

public class ResourceInfoFieldInfoModel {
    private int min_price;
    private int max_price;
    private String number_of_order;
    private String count_of_reviews;
    private ArrayList<HashMap<String,String>> calendars;
    private HashMap<String,String> user;
    private String has_power;
    private String has_chair;
    private String allow_stay_night;
    private String allow_leaflet;
    private String self_supply_invoice;//物业是否自开发票
    private String tax_point;
    private String special_tax_point;
    private String charging_standard;
    private String has_tent;
    private String enterprises;
    private int has_carport;
    private String company_comment;//邻汇评价
    private int favorite_status;
    private ArrayList<String> size_list;
    private Field_AddResourceCreateItemModel field_type;
    private Field_AddResourceCreateItemModel ad_type;
    private String deadline;
    private boolean expired;
    private int discount_rate;
    private ArrayList<String> custom_dimension_list;
    private String field_type_id;
    private int is_agent = -1;
    private int indoor = -1;
    private String do_location;//摆摊位置
    private String total_area;//场地总面积
    private Integer number_of_people;//人流量
    private int facade;//人流量展示方向
    private Field_AddResourceCreateItemModel peak_time;
    private String do_begin;
    private String do_finish;
    private String activity_start_date;
    private Integer number_of_seat;
    private ArrayList<HashMap<String,String>> dimensions;
    private ArrayList<Field_AddResourceCreateItemModel> contraband;
    private ArrayList<Field_AddResourceCreateItemModel> requirements;
    private Field_AddResourceCreateItemModel consumption_level;
    private Field_AddResourceCreateItemModel participation_level;
    private Field_AddResourceCreateItemModel age_level;
    private String other_contraband;
    private String property_requirement;
    private String description;
    private String facilities;
    private Integer number_of_registered_users;
    private Double sale;
    private Integer number_of_download;
    private Integer number_of_follower;
    private String sales_volume;//历史单量
    private ArrayList<HashMap<String, String>> field_imgs;
    private int male_proportion = -1;
    private int days_in_advance = -1;
    private Integer minimum_order_quantity;
    private Double deposit;//押金
//    //编辑时多的字段
    private int id;
    private ArrayList<Field_AddResourceCreateItemModel> term_type_list;
    private String name;
    //community
   private ResourceInfoCommunityInfoModel community = new ResourceInfoCommunityInfoModel();



    public int getMin_price() {
        return min_price;
    }

    public void setMin_price(int min_price) {
        this.min_price = min_price;
    }

    public int getMax_price() {
        return max_price;
    }

    public void setMax_price(int max_price) {
        this.max_price = max_price;
    }

    public HashMap<String, String> getUser() {
        return user;
    }

    public void setUser(HashMap<String, String> user) {
        this.user = user;
    }

    public String getSelf_supply_invoice() {
        return self_supply_invoice;
    }

    public void setSelf_supply_invoice(String self_supply_invoice) {
        this.self_supply_invoice = self_supply_invoice;
    }

    public String getTax_point() {
        return tax_point;
    }

    public void setTax_point(String tax_point) {
        this.tax_point = tax_point;
    }

    public String getSpecial_tax_point() {
        return special_tax_point;
    }

    public void setSpecial_tax_point(String special_tax_point) {
        this.special_tax_point = special_tax_point;
    }

    public String getCharging_standard() {
        return charging_standard;
    }

    public void setCharging_standard(String charging_standard) {
        this.charging_standard = charging_standard;
    }

    public String getHas_tent() {
        return has_tent;
    }

    public void setHas_tent(String has_tent) {
        this.has_tent = has_tent;
    }

    public String getCompany_comment() {
        return company_comment;
    }

    public void setCompany_comment(String company_comment) {
        this.company_comment = company_comment;
    }

    public String getEnterprises() {
        return enterprises;
    }

    public void setEnterprises(String enterprises) {
        this.enterprises = enterprises;
    }

    public int getHas_carport() {
        return has_carport;
    }

    public void setHas_carport(int has_carport) {
        this.has_carport = has_carport;
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

    public ArrayList<HashMap<String, String>> getCalendars() {
        return calendars;
    }

    public void setCalendars(ArrayList<HashMap<String, String>> calendars) {
        this.calendars = calendars;
    }

    public String getCount_of_reviews() {
        return count_of_reviews;
    }

    public void setCount_of_reviews(String count_of_reviews) {
        this.count_of_reviews = count_of_reviews;
    }

    public String getNumber_of_order() {
        return number_of_order;
    }

    public void setNumber_of_order(String number_of_order) {
        this.number_of_order = number_of_order;
    }
    public ArrayList<String> getCustom_dimension_list() {
        return custom_dimension_list;
    }

    public void setCustom_dimension_list(ArrayList<String> custom_dimension_list) {
        this.custom_dimension_list = custom_dimension_list;
    }

    public int getFavorite_status() {
        return favorite_status;
    }

    public void setFavorite_status(int favorite_status) {
        this.favorite_status = favorite_status;
    }

    public ArrayList<String> getSize_list() {
        return size_list;
    }

    public void setSize_list(ArrayList<String> size_list) {
        this.size_list = size_list;
    }

    public Field_AddResourceCreateItemModel getField_type() {
        return field_type;
    }

    public void setField_type(Field_AddResourceCreateItemModel field_type) {
        this.field_type = field_type;
    }

    public Field_AddResourceCreateItemModel getAd_type() {
        return ad_type;
    }

    public void setAd_type(Field_AddResourceCreateItemModel ad_type) {
        this.ad_type = ad_type;
    }

    public Field_AddResourceCreateItemModel getConsumption_level() {
        return consumption_level;
    }

    public void setConsumption_level(Field_AddResourceCreateItemModel consumption_level) {
        this.consumption_level = consumption_level;
    }

    public Field_AddResourceCreateItemModel getParticipation_level() {
        return participation_level;
    }

    public void setParticipation_level(Field_AddResourceCreateItemModel participation_level) {
        this.participation_level = participation_level;
    }

    public Field_AddResourceCreateItemModel getAge_level() {
        return age_level;
    }

    public void setAge_level(Field_AddResourceCreateItemModel age_level) {
        this.age_level = age_level;
    }

    public String getField_type_id() {
        return field_type_id;
    }

    public void setField_type_id(String field_type_id) {
        this.field_type_id = field_type_id;
    }

    public int getIs_agent() {
        return is_agent;
    }

    public void setIs_agent(int is_agent) {
        this.is_agent = is_agent;
    }

    public int getIndoor() {
        return indoor;
    }

    public void setIndoor(int indoor) {
        this.indoor = indoor;
    }

    public String getDo_location() {
        return do_location;
    }

    public void setDo_location(String do_location) {
        this.do_location = do_location;
    }

    public String getTotal_area() {
        return total_area;
    }

    public void setTotal_area(String total_area) {
        this.total_area = total_area;
    }

    public Integer getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(Integer number_of_people) {
        this.number_of_people = number_of_people;
    }

    public int getFacade() {
        return facade;
    }

    public void setFacade(int facade) {
        this.facade = facade;
    }

    public Field_AddResourceCreateItemModel getPeak_time() {
        return peak_time;
    }

    public void setPeak_time(Field_AddResourceCreateItemModel peak_time) {
        this.peak_time = peak_time;
    }

    public String getActivity_start_date() {
        return activity_start_date;
    }

    public void setActivity_start_date(String activity_start_date) {
        this.activity_start_date = activity_start_date;
    }

    public Integer getNumber_of_seat() {
        return number_of_seat;
    }

    public void setNumber_of_seat(Integer number_of_seat) {
        this.number_of_seat = number_of_seat;
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

    public ArrayList<HashMap<String, String>> getDimensions() {
        return dimensions;
    }

    public void setDimensions(ArrayList<HashMap<String, String>> dimensions) {
        this.dimensions = dimensions;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getContraband() {
        return contraband;
    }

    public void setContraband(ArrayList<Field_AddResourceCreateItemModel> contraband) {
        this.contraband = contraband;
    }

    public String getOther_contraband() {
        return other_contraband;
    }

    public void setOther_contraband(String other_contraband) {
        this.other_contraband = other_contraband;
    }

    public String getProperty_requirement() {
        return property_requirement;
    }

    public void setProperty_requirement(String property_requirement) {
        this.property_requirement = property_requirement;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getRequirements() {
        return requirements;
    }

    public void setRequirements(ArrayList<Field_AddResourceCreateItemModel> requirements) {
        this.requirements = requirements;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public Integer getNumber_of_registered_users() {
        return number_of_registered_users;
    }

    public void setNumber_of_registered_users(Integer number_of_registered_users) {
        this.number_of_registered_users = number_of_registered_users;
    }

    public Double getSale() {
        return sale;
    }

    public void setSale(Double sale) {
        this.sale = sale;
    }

    public Integer getNumber_of_download() {
        return number_of_download;
    }

    public void setNumber_of_download(Integer number_of_download) {
        this.number_of_download = number_of_download;
    }

    public Integer getNumber_of_follower() {
        return number_of_follower;
    }

    public void setNumber_of_follower(Integer number_of_follower) {
        this.number_of_follower = number_of_follower;
    }

    public String getSales_volume() {
        return sales_volume;
    }

    public void setSales_volume(String sales_volume) {
        this.sales_volume = sales_volume;
    }

    public ArrayList<HashMap<String, String>> getField_imgs() {
        return field_imgs;
    }

    public void setField_imgs(ArrayList<HashMap<String, String>> field_imgs) {
        this.field_imgs = field_imgs;
    }

    public int getMale_proportion() {
        return male_proportion;
    }

    public void setMale_proportion(int male_proportion) {
        this.male_proportion = male_proportion;
    }

    public int getDays_in_advance() {
        return days_in_advance;
    }

    public void setDays_in_advance(int days_in_advance) {
        this.days_in_advance = days_in_advance;
    }

    public Integer getMinimum_order_quantity() {
        return minimum_order_quantity;
    }

    public void setMinimum_order_quantity(Integer minimum_order_quantity) {
        this.minimum_order_quantity = minimum_order_quantity;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public int getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(int discount_rate) {
        this.discount_rate = discount_rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getTerm_type_list() {
        return term_type_list;
    }

    public void setTerm_type_list(ArrayList<Field_AddResourceCreateItemModel> term_type_list) {
        this.term_type_list = term_type_list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourceInfoCommunityInfoModel getCommunity() {
        return community;
    }

    public void setCommunity(ResourceInfoCommunityInfoModel community) {
        this.community = community;
    }
}
