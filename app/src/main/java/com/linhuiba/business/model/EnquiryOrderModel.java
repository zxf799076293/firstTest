package com.linhuiba.business.model;

/**
 * Created by Administrator on 2017/12/27.
 */

public class EnquiryOrderModel {
    private String enquiry_num;//押金，元
    private String name;//名称
    private Double price;//报价，元
    private double deposit;//押金，元
    private double service_fee;//押金，元
    private String start;//开始时间
    private String end;//结束时间
    private String size;//面积
    private String plan_url;//活动方案url
    private String status;//询价状态（enquiring[询价中],enquired[已报价],fail[询价失败]）
    private String created_at;//提交询价时间
    private String reason;//拒绝理由
    private String pic_url;//图片url

    public String getEnquiry_num() {
        return enquiry_num;
    }

    public void setEnquiry_num(String enquiry_num) {
        this.enquiry_num = enquiry_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getService_fee() {
        return service_fee;
    }

    public void setService_fee(double service_fee) {
        this.service_fee = service_fee;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPlan_url() {
        return plan_url;
    }

    public void setPlan_url(String plan_url) {
        this.plan_url = plan_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }
}

