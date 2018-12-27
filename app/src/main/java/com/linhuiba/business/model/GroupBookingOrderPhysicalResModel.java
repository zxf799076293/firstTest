package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/13.
 */

public class GroupBookingOrderPhysicalResModel {
    private Integer id;
    private String physical_resource_name;
    private HashMap<String,String> physical_resource_first_img;
    //发票字段
    private Integer field_type_id;
    private Field_AddResourceCreateItemModel field_type;
    private Field_AddResourceCreateItemModel activity_type;
    private Field_AddResourceCreateItemModel ad_type;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhysical_resource_name() {
        return physical_resource_name;
    }

    public void setPhysical_resource_name(String physical_resource_name) {
        this.physical_resource_name = physical_resource_name;
    }

    public HashMap<String, String> getPhysical_resource_first_img() {
        return physical_resource_first_img;
    }

    public void setPhysical_resource_first_img(HashMap<String, String> physical_resource_first_img) {
        this.physical_resource_first_img = physical_resource_first_img;
    }

    public Integer getField_type_id() {
        return field_type_id;
    }

    public void setField_type_id(Integer field_type_id) {
        this.field_type_id = field_type_id;
    }

    public Field_AddResourceCreateItemModel getField_type() {
        return field_type;
    }

    public void setField_type(Field_AddResourceCreateItemModel field_type) {
        this.field_type = field_type;
    }

    public Field_AddResourceCreateItemModel getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(Field_AddResourceCreateItemModel activity_type) {
        this.activity_type = activity_type;
    }

    public Field_AddResourceCreateItemModel getAd_type() {
        return ad_type;
    }

    public void setAd_type(Field_AddResourceCreateItemModel ad_type) {
        this.ad_type = ad_type;
    }
}
