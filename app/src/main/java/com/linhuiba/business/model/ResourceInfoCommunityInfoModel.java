package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/27.
 */

public class ResourceInfoCommunityInfoModel implements Serializable{
    private Integer id;
    private String community_name;//拼团社区名称
    private String name;//社区名称
    private String detailed_address;//社区地址
    private String build_year;//建筑年代
    private String occupancy_rate;//入住率
    private int property_costs;//物业费，单位:分
    private int rent;//租金，单位:分
    private int number_of_enterprises;//企业数
    private int house_price;//房价
    private int number_of_households;//户数
    private double lat;
    private double lng;
    private Field_AddResourceCreateItemModel enterprise_type;
    private Field_AddResourceCreateItemModel community_type;//社区类型
    private Field_AddResourceCreateItemModel tradingarea;
    private Field_AddResourceCreateItemModel construction_class;
    private Field_AddResourceCreateItemModel supermarket_type;
    private Field_AddResourceCreateItemModel shopping_mall_type;
    private double building_area;
    private int total_number_of_people;
    private ArrayList<ResourceInfoCommunityInfoResourcesModel> resources = new ArrayList<>();
    private Field_AddResourceCreateItemModel district = new Field_AddResourceCreateItemModel();
    private Field_AddResourceCreateItemModel house_attribute;
    private String average_fare;
    private String facilities;//配套设施字段
    //2017/10/24 配套餐厅字段
    private Integer number_of_seat;
    private int restaurant;
    private Field_AddResourceCreateItemModel city = new Field_AddResourceCreateItemModel();
    private Integer total_floor;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
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

    public String getBuild_year() {
        return build_year;
    }

    public void setBuild_year(String build_year) {
        this.build_year = build_year;
    }

    public String getOccupancy_rate() {
        return occupancy_rate;
    }

    public void setOccupancy_rate(String occupancy_rate) {
        this.occupancy_rate = occupancy_rate;
    }

    public int getProperty_costs() {
        return property_costs;
    }

    public void setProperty_costs(int property_costs) {
        this.property_costs = property_costs;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public int getNumber_of_enterprises() {
        return number_of_enterprises;
    }

    public void setNumber_of_enterprises(int number_of_enterprises) {
        this.number_of_enterprises = number_of_enterprises;
    }

    public int getHouse_price() {
        return house_price;
    }

    public void setHouse_price(int house_price) {
        this.house_price = house_price;
    }

    public int getNumber_of_households() {
        return number_of_households;
    }

    public void setNumber_of_households(int number_of_households) {
        this.number_of_households = number_of_households;
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

    public Field_AddResourceCreateItemModel getEnterprise_type() {
        return enterprise_type;
    }

    public void setEnterprise_type(Field_AddResourceCreateItemModel enterprise_type) {
        this.enterprise_type = enterprise_type;
    }

    public Field_AddResourceCreateItemModel getCommunity_type() {
        return community_type;
    }

    public void setCommunity_type(Field_AddResourceCreateItemModel community_type) {
        this.community_type = community_type;
    }

    public Field_AddResourceCreateItemModel getTradingarea() {
        return tradingarea;
    }

    public void setTradingarea(Field_AddResourceCreateItemModel tradingarea) {
        this.tradingarea = tradingarea;
    }

    public Field_AddResourceCreateItemModel getConstruction_class() {
        return construction_class;
    }

    public void setConstruction_class(Field_AddResourceCreateItemModel construction_class) {
        this.construction_class = construction_class;
    }

    public Field_AddResourceCreateItemModel getSupermarket_type() {
        return supermarket_type;
    }

    public void setSupermarket_type(Field_AddResourceCreateItemModel supermarket_type) {
        this.supermarket_type = supermarket_type;
    }

    public Field_AddResourceCreateItemModel getShopping_mall_type() {
        return shopping_mall_type;
    }

    public void setShopping_mall_type(Field_AddResourceCreateItemModel shopping_mall_type) {
        this.shopping_mall_type = shopping_mall_type;
    }

    public double getBuilding_area() {
        return building_area;
    }

    public void setBuilding_area(double building_area) {
        this.building_area = building_area;
    }

    public int getTotal_number_of_people() {
        return total_number_of_people;
    }

    public void setTotal_number_of_people(int total_number_of_people) {
        this.total_number_of_people = total_number_of_people;
    }

    public ArrayList<ResourceInfoCommunityInfoResourcesModel> getResources() {
        return resources;
    }

    public void setResources(ArrayList<ResourceInfoCommunityInfoResourcesModel> resources) {
        this.resources = resources;
    }

    public Field_AddResourceCreateItemModel getDistrict() {
        return district;
    }

    public void setDistrict(Field_AddResourceCreateItemModel district) {
        this.district = district;
    }

    public Field_AddResourceCreateItemModel getHouse_attribute() {
        return house_attribute;
    }

    public void setHouse_attribute(Field_AddResourceCreateItemModel house_attribute) {
        this.house_attribute = house_attribute;
    }

    public String getAverage_fare() {
        return average_fare;
    }

    public void setAverage_fare(String average_fare) {
        this.average_fare = average_fare;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public Integer getNumber_of_seat() {
        return number_of_seat;
    }

    public void setNumber_of_seat(Integer number_of_seat) {
        this.number_of_seat = number_of_seat;
    }

    public int getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(int restaurant) {
        this.restaurant = restaurant;
    }

    public Field_AddResourceCreateItemModel getCity() {
        return city;
    }

    public void setCity(Field_AddResourceCreateItemModel city) {
        this.city = city;
    }

    public Integer getTotal_floor() {
        return total_floor;
    }

    public void setTotal_floor(Integer total_floor) {
        this.total_floor = total_floor;
    }
}
