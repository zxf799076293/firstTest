package com.linhuiba.business.model;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/13.
 */

public class GroupBookingOrderSellResModel {
    private Integer id;
    private Integer physical_resource_id;
    private GroupBookingOrderGroupResModel group_purchase = new GroupBookingOrderGroupResModel();
    private GroupBookingOrderPhysicalResModel physical_resource = new GroupBookingOrderPhysicalResModel();
    private HashMap<String,String> first_selling_resource_price;
    //发票字段
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GroupBookingOrderGroupResModel getGroup_purchase() {
        return group_purchase;
    }

    public void setGroup_purchase(GroupBookingOrderGroupResModel group_purchase) {
        this.group_purchase = group_purchase;
    }

    public Integer getPhysical_resource_id() {
        return physical_resource_id;
    }

    public void setPhysical_resource_id(Integer physical_resource_id) {
        this.physical_resource_id = physical_resource_id;
    }

    public GroupBookingOrderPhysicalResModel getPhysical_resource() {
        return physical_resource;
    }

    public void setPhysical_resource(GroupBookingOrderPhysicalResModel physical_resource) {
        this.physical_resource = physical_resource;
    }

    public HashMap<String, String> getFirst_selling_resource_price() {
        return first_selling_resource_price;
    }

    public void setFirst_selling_resource_price(HashMap<String, String> first_selling_resource_price) {
        this.first_selling_resource_price = first_selling_resource_price;
    }
}
