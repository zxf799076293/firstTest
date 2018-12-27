package com.linhuiba.business.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/28.
 */
public class OrderInformationAllModel {
    private String contact;
    private String mobile;
    private ArrayList<OrderInformationModel> resources;
    private String resources_total_price;
    private String tax;
    private String subsidy_fee;
    private String actual_fee;
    private String order_num;
    private String created_at;
    private String updated_at;
    private boolean offline_pay;
    private String voucher_image_url;
    private int delegated;
    private int need_decoration;
    private int need_transportation;
    private int point;
    private int deducted;
    private Double deposit;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ArrayList<OrderInformationModel> getResources() {
        return resources;
    }

    public void setResources(ArrayList<OrderInformationModel> resources) {
        this.resources = resources;
    }

    public String getResources_total_price() {
        return resources_total_price;
    }

    public void setResources_total_price(String resources_total_price) {
        this.resources_total_price = resources_total_price;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getSubsidy_fee() {
        return subsidy_fee;
    }

    public void setSubsidy_fee(String subsidy_fee) {
        this.subsidy_fee = subsidy_fee;
    }

    public String getActual_fee() {
        return actual_fee;
    }

    public void setActual_fee(String actual_fee) {
        this.actual_fee = actual_fee;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isOffline_pay() {
        return offline_pay;
    }

    public void setOffline_pay(boolean offline_pay) {
        this.offline_pay = offline_pay;
    }

    public String getVoucher_image_url() {
        return voucher_image_url;
    }

    public void setVoucher_image_url(String voucher_image_url) {
        this.voucher_image_url = voucher_image_url;
    }

    public int getDelegated() {
        return delegated;
    }

    public void setDelegated(int delegated) {
        this.delegated = delegated;
    }

    public int getNeed_decoration() {
        return need_decoration;
    }

    public void setNeed_decoration(int need_decoration) {
        this.need_decoration = need_decoration;
    }

    public int getNeed_transportation() {
        return need_transportation;
    }

    public void setNeed_transportation(int need_transportation) {
        this.need_transportation = need_transportation;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getDeducted() {
        return deducted;
    }

    public void setDeducted(int deducted) {
        this.deducted = deducted;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }
}
