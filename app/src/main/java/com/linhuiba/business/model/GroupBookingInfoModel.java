package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/19.
 */

public class GroupBookingInfoModel {
    private Integer id;
    private Integer physical_resource_id;
    private Integer has_power;
    private Integer has_chair;
    private Integer has_tent;
    private Integer overnight_material;
    private Integer leaflet;
    private GroupBookingOrderGroupResModel group_purchase = new GroupBookingOrderGroupResModel();
    private HashMap<String,String> first_selling_resource_price;
    private GroupBookingPhysicalResourceInfoModel physical_resource = new GroupBookingPhysicalResourceInfoModel();
    private String industry_str;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPhysical_resource_id() {
        return physical_resource_id;
    }

    public void setPhysical_resource_id(Integer physical_resource_id) {
        this.physical_resource_id = physical_resource_id;
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

    public GroupBookingOrderGroupResModel getGroup_purchase() {
        return group_purchase;
    }

    public void setGroup_purchase(GroupBookingOrderGroupResModel group_purchase) {
        this.group_purchase = group_purchase;
    }

    public HashMap<String, String> getFirst_selling_resource_price() {
        return first_selling_resource_price;
    }

    public void setFirst_selling_resource_price(HashMap<String, String> first_selling_resource_price) {
        this.first_selling_resource_price = first_selling_resource_price;
    }

    public GroupBookingPhysicalResourceInfoModel getPhysical_resource() {
        return physical_resource;
    }

    public void setPhysical_resource(GroupBookingPhysicalResourceInfoModel physical_resource) {
        this.physical_resource = physical_resource;
    }

    public String getIndustry_str() {
        return industry_str;
    }

    public void setIndustry_str(String industry_str) {
        this.industry_str = industry_str;
    }
}
