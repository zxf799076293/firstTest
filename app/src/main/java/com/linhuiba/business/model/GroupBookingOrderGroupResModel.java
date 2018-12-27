package com.linhuiba.business.model;

/**
 * Created by Administrator on 2017/10/13.
 */

public class GroupBookingOrderGroupResModel {
    private Integer id;
    private String activity_start;
    private String activity_end;
    private Double price;
    private Double origin_price;
    private String field_length;
    private String field_width;
    private Integer group_status;
    private String activity_allow;
    private Integer time_left;
    //拼团详情group res
    private Integer number_of_group_purchase_now;
    private Integer min;
    private Integer max;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivity_start() {
        return activity_start;
    }

    public void setActivity_start(String activity_start) {
        this.activity_start = activity_start;
    }

    public String getActivity_end() {
        return activity_end;
    }

    public void setActivity_end(String activity_end) {
        this.activity_end = activity_end;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getOrigin_price() {
        return origin_price;
    }

    public void setOrigin_price(Double origin_price) {
        this.origin_price = origin_price;
    }

    public String getField_length() {
        return field_length;
    }

    public void setField_length(String field_length) {
        this.field_length = field_length;
    }

    public String getField_width() {
        return field_width;
    }

    public void setField_width(String field_width) {
        this.field_width = field_width;
    }

    public Integer getGroup_status() {
        return group_status;
    }

    public void setGroup_status(Integer group_status) {
        this.group_status = group_status;
    }

    public String getActivity_allow() {
        return activity_allow;
    }

    public void setActivity_allow(String activity_allow) {
        this.activity_allow = activity_allow;
    }

    public Integer getTime_left() {
        return time_left;
    }

    public void setTime_left(Integer time_left) {
        this.time_left = time_left;
    }

    public Integer getNumber_of_group_purchase_now() {
        return number_of_group_purchase_now;
    }

    public void setNumber_of_group_purchase_now(Integer number_of_group_purchase_now) {
        this.number_of_group_purchase_now = number_of_group_purchase_now;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
