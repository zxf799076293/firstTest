package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/20.
 */

public class GroupBookingResourceSellResModel {
    private Integer id;
    private Integer physical_resource_id;
    private HashMap<String,String> first_selling_resource_price;//原价
    private FieldInfoPhysicalResourceModel physical_resource = new FieldInfoPhysicalResourceModel();

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

    public HashMap<String, String> getFirst_selling_resource_price() {
        return first_selling_resource_price;
    }

    public void setFirst_selling_resource_price(HashMap<String, String> first_selling_resource_price) {
        this.first_selling_resource_price = first_selling_resource_price;
    }

    public FieldInfoPhysicalResourceModel getPhysical_resource() {
        return physical_resource;
    }

    public void setPhysical_resource(FieldInfoPhysicalResourceModel physical_resource) {
        this.physical_resource = physical_resource;
    }
}
