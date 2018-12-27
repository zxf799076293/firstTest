package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/19.
 */

public class GroupBookingPhysicalResourceInfoModel {
    private Integer id;
    private String name;
    private Integer number_of_people;//人流量
    private Integer number_of_order;
    private String doBegin;//摆摊起始时间
    private String doFinish;//摆摊结束时间
    private String doLocation;//摆摊位置
    private Double total_area;//场地总面积(单位:米,精确到小数点后两位)
    private Integer community_id;
    private Field_AddResourceCreateItemModel peak_time;//人流量高峰时间段
    private Integer indoor;//是否在室内(默认值NULL表示未录入)
    private Integer male_proportion;//男女比例
    private Field_AddResourceCreateItemModel consumption_level;//消费水平
    private Field_AddResourceCreateItemModel age_level;//年龄层
    private Field_AddResourceCreateItemModel participation_level;//用户参与度等级
    private ArrayList<HashMap<String, String>> physical_resource_imgs;//场地图
    private Integer facade;//人流量展示方向;
    private String description;//描述
    private ArrayList<Field_AddResourceCreateItemModel> requirement;//物业要求
    private String property_requirement;//其他物业要求
    private ArrayList<Field_AddResourceCreateItemModel> contraband;//禁摆
    private String other_contraband;//其他禁摆
    private Integer count_of_reviews;//评论个数
    private ResourceInfoCommunityInfoModel community = new ResourceInfoCommunityInfoModel();
    private Field_AddResourceCreateItemModel field_type = new Field_AddResourceCreateItemModel();
    private Integer number_of_registered_users;//用户注册
    private Double sale;
    private Integer number_of_download;
    private Integer number_of_follower;
    private String sales_volume;//历史单量
    private String information;
    private int has_carport;
    private String charging_standard;
    private String company_comment;//邻汇评价

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

    public Integer getNumber_of_people() {
        return number_of_people;
    }

    public Integer getNumber_of_order() {
        return number_of_order;
    }

    public void setNumber_of_order(Integer number_of_order) {
        this.number_of_order = number_of_order;
    }

    public void setNumber_of_people(Integer number_of_people) {
        this.number_of_people = number_of_people;
    }

    public String getDoBegin() {
        return doBegin;
    }

    public void setDoBegin(String doBegin) {
        this.doBegin = doBegin;
    }

    public String getDoFinish() {
        return doFinish;
    }

    public void setDoFinish(String doFinish) {
        this.doFinish = doFinish;
    }

    public String getDoLocation() {
        return doLocation;
    }

    public void setDoLocation(String doLocation) {
        this.doLocation = doLocation;
    }

    public Double getTotal_area() {
        return total_area;
    }

    public void setTotal_area(Double total_area) {
        this.total_area = total_area;
    }

    public Integer getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(Integer community_id) {
        this.community_id = community_id;
    }

    public Field_AddResourceCreateItemModel getPeak_time() {
        return peak_time;
    }

    public void setPeak_time(Field_AddResourceCreateItemModel peak_time) {
        this.peak_time = peak_time;
    }

    public Integer getIndoor() {
        return indoor;
    }

    public void setIndoor(Integer indoor) {
        this.indoor = indoor;
    }

    public Integer getMale_proportion() {
        return male_proportion;
    }

    public void setMale_proportion(Integer male_proportion) {
        this.male_proportion = male_proportion;
    }

    public Field_AddResourceCreateItemModel getConsumption_level() {
        return consumption_level;
    }

    public void setConsumption_level(Field_AddResourceCreateItemModel consumption_level) {
        this.consumption_level = consumption_level;
    }

    public Field_AddResourceCreateItemModel getAge_level() {
        return age_level;
    }

    public void setAge_level(Field_AddResourceCreateItemModel age_level) {
        this.age_level = age_level;
    }

    public Field_AddResourceCreateItemModel getParticipation_level() {
        return participation_level;
    }

    public void setParticipation_level(Field_AddResourceCreateItemModel participation_level) {
        this.participation_level = participation_level;
    }

    public ArrayList<HashMap<String, String>> getPhysical_resource_imgs() {
        return physical_resource_imgs;
    }

    public void setPhysical_resource_imgs(ArrayList<HashMap<String, String>> physical_resource_imgs) {
        this.physical_resource_imgs = physical_resource_imgs;
    }

    public Integer getFacade() {
        return facade;
    }

    public void setFacade(Integer facade) {
        this.facade = facade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getRequirement() {
        return requirement;
    }

    public void setRequirement(ArrayList<Field_AddResourceCreateItemModel> requirement) {
        this.requirement = requirement;
    }

    public String getProperty_requirement() {
        return property_requirement;
    }

    public void setProperty_requirement(String property_requirement) {
        this.property_requirement = property_requirement;
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

    public Integer getCount_of_reviews() {
        return count_of_reviews;
    }

    public void setCount_of_reviews(Integer count_of_reviews) {
        this.count_of_reviews = count_of_reviews;
    }

    public ResourceInfoCommunityInfoModel getCommunity() {
        return community;
    }

    public void setCommunity(ResourceInfoCommunityInfoModel community) {
        this.community = community;
    }

    public Field_AddResourceCreateItemModel getField_type() {
        return field_type;
    }

    public void setField_type(Field_AddResourceCreateItemModel field_type) {
        this.field_type = field_type;
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

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getHas_carport() {
        return has_carport;
    }

    public void setHas_carport(int has_carport) {
        this.has_carport = has_carport;
    }

    public String getCharging_standard() {
        return charging_standard;
    }

    public void setCharging_standard(String charging_standard) {
        this.charging_standard = charging_standard;
    }

    public String getCompany_comment() {
        return company_comment;
    }

    public void setCompany_comment(String company_comment) {
        this.company_comment = company_comment;
    }
}
