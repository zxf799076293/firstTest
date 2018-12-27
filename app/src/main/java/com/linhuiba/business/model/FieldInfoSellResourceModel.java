package com.linhuiba.business.model;

import com.linhuiba.business.fragment.SelfSupportShopFragment;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/20.
 */

public class FieldInfoSellResourceModel implements Serializable {
    private Integer id;
    private Integer favorite_status = 0;//关注
    private Integer identification;//1 场地认证
    private Integer cooperation_type_id;//12 是自营 13:代理 14：物业
    private Integer is_agent;
    private HashMap<String,String> user;
    private ArrayList<FieldInfoSellResourcePriceModel> selling_resource_price = new ArrayList<>();//原价
    private Integer has_power;
    private Integer has_chair;
    private Integer has_tent;
    private Integer overnight_material;
    private Integer leaflet;
    private Integer days_in_advance;//提前几天预定
    private Integer minimum_booking_days;//几天起订
    private ArrayList<HashMap<String,String>> calendars;
    private Double deposit;//是否有押金
    private Double tax_point;//税率普通
    private Double special_tax_point;//专用税率
    private Double subsidy_rate;//补贴率
    private Double min_price;
    private Double max_price;
    private Integer price;
    private Double min_after_subsidy;//补贴后最小值
    private Double max_after_subsidy;//补贴后最大值

    //活动
    private String activity_start_date;
    private String deadline;
    private Field_AddResourceCreateItemModel activity_type;
    private boolean expired;
    private String custom_name;
    private String description;
    //广告
    //询价
    private Integer refer_min_price;
    private Integer refer_max_price;
    private String img_description;//图文详情
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFavorite_status() {
        return favorite_status;
    }

    public void setFavorite_status(Integer favorite_status) {
        this.favorite_status = favorite_status;
    }

    public Integer getIdentification() {
        return identification;
    }

    public void setIdentification(Integer identification) {
        this.identification = identification;
    }

    public Integer getCooperation_type_id() {
        return cooperation_type_id;
    }

    public void setCooperation_type_id(Integer cooperation_type_id) {
        this.cooperation_type_id = cooperation_type_id;
    }

    public Integer getIs_agent() {
        return is_agent;
    }

    public void setIs_agent(Integer is_agent) {
        this.is_agent = is_agent;
    }

    public HashMap<String, String> getUser() {
        return user;
    }

    public void setUser(HashMap<String, String> user) {
        this.user = user;
    }

    public ArrayList<FieldInfoSellResourcePriceModel> getSelling_resource_price() {
        return selling_resource_price;
    }

    public void setSelling_resource_price(ArrayList<FieldInfoSellResourcePriceModel> selling_resource_price) {
        this.selling_resource_price = selling_resource_price;
    }

    public Integer getHas_power() {
        return has_power;
    }

    public void setHas_power(Integer has_power) {
        this.has_power = has_power;
    }

    public Integer getHas_chair() {
        return has_chair;
    }

    public void setHas_chair(Integer has_chair) {
        this.has_chair = has_chair;
    }

    public Integer getHas_tent() {
        return has_tent;
    }

    public void setHas_tent(Integer has_tent) {
        this.has_tent = has_tent;
    }

    public Integer getOvernight_material() {
        return overnight_material;
    }

    public void setOvernight_material(Integer overnight_material) {
        this.overnight_material = overnight_material;
    }

    public Integer getLeaflet() {
        return leaflet;
    }

    public void setLeaflet(Integer leaflet) {
        this.leaflet = leaflet;
    }

    public Integer getDays_in_advance() {
        return days_in_advance;
    }

    public void setDays_in_advance(Integer days_in_advance) {
        this.days_in_advance = days_in_advance;
    }

    public Integer getMinimum_booking_days() {
        return minimum_booking_days;
    }

    public void setMinimum_booking_days(Integer minimum_booking_days) {
        this.minimum_booking_days = minimum_booking_days;
    }

    public ArrayList<HashMap<String, String>> getCalendars() {
        return calendars;
    }

    public void setCalendars(ArrayList<HashMap<String, String>> calendars) {
        this.calendars = calendars;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Double getTax_point() {
        return tax_point;
    }

    public void setTax_point(Double tax_point) {
        this.tax_point = tax_point;
    }

    public Double getSpecial_tax_point() {
        return special_tax_point;
    }

    public void setSpecial_tax_point(Double special_tax_point) {
        this.special_tax_point = special_tax_point;
    }

    public Double getSubsidy_rate() {
        return subsidy_rate;
    }

    public void setSubsidy_rate(Double subsidy_rate) {
        this.subsidy_rate = subsidy_rate;
    }

    public Double getMin_price() {
        return min_price;
    }

    public void setMin_price(Double min_price) {
        this.min_price = min_price;
    }

    public Double getMax_price() {
        return max_price;
    }

    public void setMax_price(Double max_price) {
        this.max_price = max_price;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Double getMin_after_subsidy() {
        return min_after_subsidy;
    }

    public void setMin_after_subsidy(Double min_after_subsidy) {
        this.min_after_subsidy = min_after_subsidy;
    }

    public Double getMax_after_subsidy() {
        return max_after_subsidy;
    }

    public void setMax_after_subsidy(Double max_after_subsidy) {
        this.max_after_subsidy = max_after_subsidy;
    }

    public String getActivity_start_date() {
        return activity_start_date;
    }

    public void setActivity_start_date(String activity_start_date) {
        this.activity_start_date = activity_start_date;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Field_AddResourceCreateItemModel getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(Field_AddResourceCreateItemModel activity_type) {
        this.activity_type = activity_type;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getCustom_name() {
        return custom_name;
    }

    public void setCustom_name(String custom_name) {
        this.custom_name = custom_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRefer_min_price() {
        return refer_min_price;
    }

    public void setRefer_min_price(Integer refer_min_price) {
        this.refer_min_price = refer_min_price;
    }

    public Integer getRefer_max_price() {
        return refer_max_price;
    }

    public void setRefer_max_price(Integer refer_max_price) {
        this.refer_max_price = refer_max_price;
    }

    public String getImg_description() {
        return img_description;
    }

    public void setImg_description(String img_description) {
        this.img_description = img_description;
    }
}
