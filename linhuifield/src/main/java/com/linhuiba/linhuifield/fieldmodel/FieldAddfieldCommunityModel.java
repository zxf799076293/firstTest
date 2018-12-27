package com.linhuiba.linhuifield.fieldmodel;

import java.io.Serializable;
import java.util.ArrayList;

public class FieldAddfieldCommunityModel implements Serializable {
    private Integer community_id;
    private String name = "";
    private String address = "";
    private Integer province_id;
    private Integer city_id;
    private Integer district_id;
    private Integer category_id;
    private String build_year = "";
    private String building_area = "";//占地面积
    private String description = "";//场地描述
    private ArrayList<FieldAddfieldAttributesModel> resource_img = new ArrayList<>();
    private ArrayList<FieldAddfieldAttributesModel> attributes = new ArrayList<>();
    private FieldAddfieldAttributesModel category = new FieldAddfieldAttributesModel();
    private int editable = 1;//是否能编辑
    private Field_AddResourceCreateItemModel trading_area = new Field_AddResourceCreateItemModel();
    private Field_AddResourceCreateItemModel city = new Field_AddResourceCreateItemModel();
    private Field_AddResourceCreateItemModel district = new Field_AddResourceCreateItemModel();
    private Field_AddResourceCreateItemModel developer = new Field_AddResourceCreateItemModel();
    private int isSearchChoose;//是否是选择的场地 或者展位并且能编辑
    private int is_not_check;//选择的场地 或者展位并且能编辑 时不用检查
    public Integer getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(Integer community_id) {
        this.community_id = community_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getProvince_id() {
        return province_id;
    }

    public void setProvince_id(Integer province_id) {
        this.province_id = province_id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public Integer getCity_id() {
        return city_id;
    }

    public void setCity_id(Integer city_id) {
        this.city_id = city_id;
    }

    public Integer getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(Integer district_id) {
        this.district_id = district_id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<FieldAddfieldAttributesModel> getResource_img() {
        return resource_img;
    }

    public void setResource_img(ArrayList<FieldAddfieldAttributesModel> resource_img) {
        this.resource_img = resource_img;
    }

    public ArrayList<FieldAddfieldAttributesModel> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<FieldAddfieldAttributesModel> attributes) {
        this.attributes = attributes;
    }

    public FieldAddfieldAttributesModel getCategory() {
        return category;
    }

    public void setCategory(FieldAddfieldAttributesModel category) {
        this.category = category;
    }

    public int getEditable() {
        return editable;
    }

    public void setEditable(int editable) {
        this.editable = editable;
    }

    public Field_AddResourceCreateItemModel getTrading_area() {
        return trading_area;
    }

    public void setTrading_area(Field_AddResourceCreateItemModel trading_area) {
        this.trading_area = trading_area;
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

    public Field_AddResourceCreateItemModel getDeveloper() {
        return developer;
    }

    public void setDeveloper(Field_AddResourceCreateItemModel developer) {
        this.developer = developer;
    }

    public int getIsSearchChoose() {
        return isSearchChoose;
    }

    public void setIsSearchChoose(int isSearchChoose) {
        this.isSearchChoose = isSearchChoose;
    }

    public int getIs_not_check() {
        return is_not_check;
    }

    public void setIs_not_check(int is_not_check) {
        this.is_not_check = is_not_check;
    }
}
