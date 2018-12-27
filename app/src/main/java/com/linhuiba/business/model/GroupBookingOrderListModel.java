package com.linhuiba.business.model;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/13.
 */

public class GroupBookingOrderListModel {
    private Integer id;
    private String order_status;
    private Integer deducted;
    private Integer selling_resource_id;
    private Integer field_order_id;
    private String objection;
    private String service_phone;
    private Double real_cost;
    private GroupBookingOrderSellResModel selling_resource = new GroupBookingOrderSellResModel();
    private HashMap<String,String> field_order;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public Integer getDeducted() {
        return deducted;
    }

    public void setDeducted(Integer deducted) {
        this.deducted = deducted;
    }

    public Integer getSelling_resource_id() {
        return selling_resource_id;
    }

    public void setSelling_resource_id(Integer selling_resource_id) {
        this.selling_resource_id = selling_resource_id;
    }

    public Integer getField_order_id() {
        return field_order_id;
    }

    public void setField_order_id(Integer field_order_id) {
        this.field_order_id = field_order_id;
    }

    public String getObjection() {
        return objection;
    }

    public void setObjection(String objection) {
        this.objection = objection;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    public Double getReal_cost() {
        return real_cost;
    }

    public void setReal_cost(Double real_cost) {
        this.real_cost = real_cost;
    }

    public GroupBookingOrderSellResModel getSelling_resource() {
        return selling_resource;
    }

    public void setSelling_resource(GroupBookingOrderSellResModel selling_resource) {
        this.selling_resource = selling_resource;
    }

    public HashMap<String, String> getField_order() {
        return field_order;
    }

    public void setField_order(HashMap<String, String> field_order) {
        this.field_order = field_order;
    }
}
