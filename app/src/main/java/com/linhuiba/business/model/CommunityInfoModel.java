package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldAttributesModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.io.Serializable;
import java.util.ArrayList;

public class CommunityInfoModel implements Serializable {
    private int id;
    private String name = "";
    private String description = "";
    private String detailed_address = "";
    private String category_full_name = "";
    private Double lng;
    private Double lat;
    private String build_year = "";
    private String building_area = "";
    private String lowest_price = "";
    private String male_proportion;
    private String private_car_proportion;
    private String unmarried_proportion;
    private String married_unpregnant_proportion;
    private String married_pregnant_proportion;
    private String undergraduate_proportion;
    private String graduate_proportion;
    private String specialty_proportion;
    private String total_number_of_people;
    private Field_AddResourceCreateItemModel province;
    private Field_AddResourceCreateItemModel city;
    private Field_AddResourceCreateItemModel district;
    private Field_AddResourceCreateItemModel tradingarea;
    private Field_AddResourceCreateItemModel developer;
    private ArrayList<Field_AddResourceCreateItemModel> career_background;
    private ArrayList<Field_AddResourceCreateItemModel> age_group;
    private ArrayList<Field_AddResourceCreateItemModel> labels;
    private Field_AddResourceCreateItemModel category;
    private ArrayList<Field_AddResourceCreateItemModel> community_imgs;
    private ArrayList<FieldAddfieldAttributesModel> attributes = new ArrayList<>();
    private String service_phone;
    private String floor_price;
    private String max_price;
    private Field_AddResourceCreateItemModel community_img;
    private ArrayList<MyCouponsModel> coupons = new ArrayList<>();//优惠券
    private String industry_str;
    private FieldinfoCounselorModel service_representative;
    private int physical_resource_count;
    private String img_description;//图文详情
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

    public String getDetailed_address() {
        return detailed_address;
    }

    public void setDetailed_address(String detailed_address) {
        this.detailed_address = detailed_address;
    }

    public String getCategory_full_name() {
        return category_full_name;
    }

    public void setCategory_full_name(String category_full_name) {
        this.category_full_name = category_full_name;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getBuild_year() {
        return build_year;
    }

    public void setBuild_year(String build_year) {
        this.build_year = build_year;
    }

    public String getBuilding_area() {
        return building_area;
    }

    public void setBuilding_area(String building_area) {
        this.building_area = building_area;
    }

    public String getLowest_price() {
        return lowest_price;
    }

    public void setLowest_price(String lowest_price) {
        this.lowest_price = lowest_price;
    }

    public String getMale_proportion() {
        return male_proportion;
    }

    public void setMale_proportion(String male_proportion) {
        this.male_proportion = male_proportion;
    }

    public String getPrivate_car_proportion() {
        return private_car_proportion;
    }

    public void setPrivate_car_proportion(String private_car_proportion) {
        this.private_car_proportion = private_car_proportion;
    }

    public String getUnmarried_proportion() {
        return unmarried_proportion;
    }

    public void setUnmarried_proportion(String unmarried_proportion) {
        this.unmarried_proportion = unmarried_proportion;
    }

    public String getMarried_unpregnant_proportion() {
        return married_unpregnant_proportion;
    }

    public void setMarried_unpregnant_proportion(String married_unpregnant_proportion) {
        this.married_unpregnant_proportion = married_unpregnant_proportion;
    }

    public String getMarried_pregnant_proportion() {
        return married_pregnant_proportion;
    }

    public void setMarried_pregnant_proportion(String married_pregnant_proportion) {
        this.married_pregnant_proportion = married_pregnant_proportion;
    }

    public String getUndergraduate_proportion() {
        return undergraduate_proportion;
    }

    public void setUndergraduate_proportion(String undergraduate_proportion) {
        this.undergraduate_proportion = undergraduate_proportion;
    }

    public String getGraduate_proportion() {
        return graduate_proportion;
    }

    public void setGraduate_proportion(String graduate_proportion) {
        this.graduate_proportion = graduate_proportion;
    }

    public String getSpecialty_proportion() {
        return specialty_proportion;
    }

    public void setSpecialty_proportion(String specialty_proportion) {
        this.specialty_proportion = specialty_proportion;
    }

    public String getTotal_number_of_people() {
        return total_number_of_people;
    }

    public void setTotal_number_of_people(String total_number_of_people) {
        this.total_number_of_people = total_number_of_people;
    }

    public Field_AddResourceCreateItemModel getProvince() {
        return province;
    }

    public void setProvince(Field_AddResourceCreateItemModel province) {
        this.province = province;
    }

    public Field_AddResourceCreateItemModel getCity() {
        return city;
    }

    public void setCity(Field_AddResourceCreateItemModel city) {
        this.city = city;
    }

    public Field_AddResourceCreateItemModel getDistrict() {
        return district;
    }

    public void setDistrict(Field_AddResourceCreateItemModel district) {
        this.district = district;
    }

    public Field_AddResourceCreateItemModel getTradingarea() {
        return tradingarea;
    }

    public void setTradingarea(Field_AddResourceCreateItemModel tradingarea) {
        this.tradingarea = tradingarea;
    }

    public Field_AddResourceCreateItemModel getDeveloper() {
        return developer;
    }

    public void setDeveloper(Field_AddResourceCreateItemModel developer) {
        this.developer = developer;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getCareer_background() {
        return career_background;
    }

    public void setCareer_background(ArrayList<Field_AddResourceCreateItemModel> career_background) {
        this.career_background = career_background;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getAge_group() {
        return age_group;
    }

    public void setAge_group(ArrayList<Field_AddResourceCreateItemModel> age_group) {
        this.age_group = age_group;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<Field_AddResourceCreateItemModel> labels) {
        this.labels = labels;
    }

    public Field_AddResourceCreateItemModel getCategory() {
        return category;
    }

    public void setCategory(Field_AddResourceCreateItemModel category) {
        this.category = category;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getCommunity_imgs() {
        return community_imgs;
    }

    public void setCommunity_imgs(ArrayList<Field_AddResourceCreateItemModel> community_imgs) {
        this.community_imgs = community_imgs;
    }

    public ArrayList<FieldAddfieldAttributesModel> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<FieldAddfieldAttributesModel> attributes) {
        this.attributes = attributes;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    public String getFloor_price() {
        return floor_price;
    }

    public void setFloor_price(String floor_price) {
        this.floor_price = floor_price;
    }

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public Field_AddResourceCreateItemModel getCommunity_img() {
        return community_img;
    }

    public void setCommunity_img(Field_AddResourceCreateItemModel community_img) {
        this.community_img = community_img;
    }

    public ArrayList<MyCouponsModel> getCoupons() {
        return coupons;
    }

    public void setCoupons(ArrayList<MyCouponsModel> coupons) {
        this.coupons = coupons;
    }

    public String getIndustry_str() {
        return industry_str;
    }

    public void setIndustry_str(String industry_str) {
        this.industry_str = industry_str;
    }

    public FieldinfoCounselorModel getService_representative() {
        return service_representative;
    }

    public void setService_representative(FieldinfoCounselorModel service_representative) {
        this.service_representative = service_representative;
    }

    public int getPhysical_resource_count() {
        return physical_resource_count;
    }

    public void setPhysical_resource_count(int physical_resource_count) {
        this.physical_resource_count = physical_resource_count;
    }

    public String getImg_description() {
        return img_description;
    }

    public void setImg_description(String img_description) {
        this.img_description = img_description;
    }
}
