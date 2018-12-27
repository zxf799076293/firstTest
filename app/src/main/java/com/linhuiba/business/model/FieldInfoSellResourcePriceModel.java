package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/10.
 */

public class FieldInfoSellResourcePriceModel implements Serializable {
    private Integer price;
    private Integer count_of_frame;
    private Integer lease_term_type_id;
    private Integer selling_resource_id;
    private String deposit;
    private Field_AddResourceCreateItemModel lease_term_type = new Field_AddResourceCreateItemModel();

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCount_of_frame() {
        return count_of_frame;
    }

    public void setCount_of_frame(Integer count_of_frame) {
        this.count_of_frame = count_of_frame;
    }

    public Integer getLease_term_type_id() {
        return lease_term_type_id;
    }

    public void setLease_term_type_id(Integer lease_term_type_id) {
        this.lease_term_type_id = lease_term_type_id;
    }

    public Integer getSelling_resource_id() {
        return selling_resource_id;
    }

    public void setSelling_resource_id(Integer selling_resource_id) {
        this.selling_resource_id = selling_resource_id;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public Field_AddResourceCreateItemModel getLease_term_type() {
        return lease_term_type;
    }

    public void setLease_term_type(Field_AddResourceCreateItemModel lease_term_type) {
        this.lease_term_type = lease_term_type;
    }
}
