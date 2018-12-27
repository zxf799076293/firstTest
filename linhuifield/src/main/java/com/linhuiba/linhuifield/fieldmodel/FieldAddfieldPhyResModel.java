package com.linhuiba.linhuifield.fieldmodel;

import java.io.Serializable;
import java.util.ArrayList;

public class FieldAddfieldPhyResModel implements Serializable {
    // 需要查看的字段 physical_id name field_type_id
    private Integer physical_id;//展位ID
    private String name;//展位名称
    private Integer indoor;//摆摊位置是否室内 （0/室外 1/室内）
    private String do_location;//摆摊位置（具体的摆摊位置）
    private Integer field_type_id;//场地类型ID
    private Integer location_type_id;//展位位置类型ID
    private String other_location_type;//其他展位位置类型
    private String total_area;//展位总面积(单位:米,精确到小数点后两位)
    private String do_begin;//摆摊时间段开始(有效时间格式e.g. 10:00)
    private String do_finish;//摆摊时间段结束(有效时间格式e.g. 10:00)
    private Integer peak_time_id;//人流量高峰期
    private Integer number_of_people;
    private Integer facade;//展示方向(可选值1~4)
    private ArrayList<FieldAddfieldAttributesModel> contraband = new ArrayList<>();//禁摆品类
    private ArrayList<FieldAddfieldAttributesModel> requirement = new ArrayList<>();//物业要求
    private String other_contraband;//其他禁摆品类
    private String property_requirement;//其它物业要求
    private ArrayList<FieldAddfieldAttributesModel> resource_img = new ArrayList<>();//资源图片
    private int editable = 1;
    private String description;//展位描述
    private Field_AddResourceCreateItemModel field_type;
    private Field_AddResourceCreateItemModel location_type;
    private int isSearchChoose;//是否是选择的场地 或者展位并且能编辑
    private int is_not_check;//选择的场地 或者展位并且能编辑 时不用检查
    private int has_selling_resource;//1/存在供给 0/不存在供给
    private int selling_resource_id;//本用户存在的供给id,不存在则null
    public Integer getPhysical_id() {
        return physical_id;
    }

    public void setPhysical_id(Integer physical_id) {
        this.physical_id = physical_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndoor() {
        return indoor;
    }

    public void setIndoor(Integer indoor) {
        this.indoor = indoor;
    }

    public String getDo_location() {
        return do_location;
    }

    public void setDo_location(String do_location) {
        this.do_location = do_location;
    }

    public Integer getField_type_id() {
        return field_type_id;
    }

    public void setField_type_id(Integer field_type_id) {
        this.field_type_id = field_type_id;
    }

    public Integer getLocation_type_id() {
        return location_type_id;
    }

    public void setLocation_type_id(Integer location_type_id) {
        this.location_type_id = location_type_id;
    }

    public String getOther_location_type() {
        return other_location_type;
    }

    public void setOther_location_type(String other_location_type) {
        this.other_location_type = other_location_type;
    }

    public String getTotal_area() {
        return total_area;
    }

    public void setTotal_area(String total_area) {
        this.total_area = total_area;
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

    public Integer getPeak_time_id() {
        return peak_time_id;
    }

    public void setPeak_time_id(Integer peak_time_id) {
        this.peak_time_id = peak_time_id;
    }

    public Integer getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(Integer number_of_people) {
        this.number_of_people = number_of_people;
    }

    public Integer getFacade() {
        return facade;
    }

    public void setFacade(Integer facade) {
        this.facade = facade;
    }

    public ArrayList<FieldAddfieldAttributesModel> getContraband() {
        return contraband;
    }

    public void setContraband(ArrayList<FieldAddfieldAttributesModel> contraband) {
        this.contraband = contraband;
    }

    public ArrayList<FieldAddfieldAttributesModel> getRequirement() {
        return requirement;
    }

    public void setRequirement(ArrayList<FieldAddfieldAttributesModel> requirement) {
        this.requirement = requirement;
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

    public ArrayList<FieldAddfieldAttributesModel> getResource_img() {
        return resource_img;
    }

    public void setResource_img(ArrayList<FieldAddfieldAttributesModel> resource_img) {
        this.resource_img = resource_img;
    }
    public int getEditable() {
        return editable;
    }

    public void setEditable(int editable) {
        this.editable = editable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Field_AddResourceCreateItemModel getField_type() {
        return field_type;
    }

    public void setField_type(Field_AddResourceCreateItemModel field_type) {
        this.field_type = field_type;
    }

    public Field_AddResourceCreateItemModel getLocation_type() {
        return location_type;
    }

    public void setLocation_type(Field_AddResourceCreateItemModel location_type) {
        this.location_type = location_type;
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

    public int getHas_selling_resource() {
        return has_selling_resource;
    }

    public void setHas_selling_resource(int has_selling_resource) {
        this.has_selling_resource = has_selling_resource;
    }

    public int getSelling_resource_id() {
        return selling_resource_id;
    }

    public void setSelling_resource_id(int selling_resource_id) {
        this.selling_resource_id = selling_resource_id;
    }
}
