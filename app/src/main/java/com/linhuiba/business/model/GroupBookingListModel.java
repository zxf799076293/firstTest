package com.linhuiba.business.model;

import java.util.Date;

/**
 * Created by Administrator on 2017/9/20.
 */

public class GroupBookingListModel {
    private Integer id;
    private String activity_start;
    private String activity_end;
    private GroupBookingResourceSellResModel selling_resource = new GroupBookingResourceSellResModel();
    private Double origin_price;
    private Integer time_left;
    private Integer min;
    private Integer max;
    private Integer status;
    private Integer number_of_group_purchase_now;
    private Date group_start;
    private Date group_end;

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

    public GroupBookingResourceSellResModel getSelling_resource() {
        return selling_resource;
    }

    public void setSelling_resource(GroupBookingResourceSellResModel selling_resource) {
        this.selling_resource = selling_resource;
    }

    public Double getOrigin_price() {
        return origin_price;
    }

    public void setOrigin_price(Double origin_price) {
        this.origin_price = origin_price;
    }

    public Integer getTime_left() {
        return time_left;
    }

    public void setTime_left(Integer time_left) {
        this.time_left = time_left;
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

    public Integer getNumber_of_group_purchase_now() {
        return number_of_group_purchase_now;
    }

    public void setNumber_of_group_purchase_now(Integer number_of_group_purchase_now) {
        this.number_of_group_purchase_now = number_of_group_purchase_now;
    }

    public Date getGroup_start() {
        return group_start;
    }

    public void setGroup_start(Date group_start) {
        this.group_start = group_start;
    }

    public Date getGroup_end() {
        return group_end;
    }

    public void setGroup_end(Date group_end) {
        this.group_end = group_end;
    }
}
