package com.linhuiba.business.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/18.
 */
public class CommoditypayModel implements Serializable, Cloneable {
    private String shopping_cart_item_id;
    private String res_type_id;
    private boolean valid;
    private String resource_id;
    private String name;
    private String relation;
    private String date;
    private String price;
    private String lease_term_type;
    private String lease_term_type_id;
    private String size;
    private String pic_url;
    private String number_of_people;
    private String occupancy_rate;
    private String ad_print_type;
    private String ad_type;
    private String district;
    private String field_type;
    private String activity_type;
    private int count;// 数量
    private int count_of_time_unit;// 几周或几月
    private int maximum_count;// 数量最大值
    private String subsidy_fee;
    private String discount_rate;//折扣率
    private String tax_point;
    private String special_tax_point;
    private String custom_dimension;
    private Integer minimum_order_quantity;
    private Double deposit;
    private Integer days_in_advance;
    private int community_id;
    private int physical_resource_id;
    private int selling_resource_id;
    public String getShopping_cart_item_id() {
        return shopping_cart_item_id;
    }

    public void setShopping_cart_item_id(String shopping_cart_item_id) {
        this.shopping_cart_item_id = shopping_cart_item_id;
    }

    public String getRes_type_id() {
        return res_type_id;
    }

    public void setRes_type_id(String res_type_id) {
        this.res_type_id = res_type_id;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getResource_id() {
        return resource_id;
    }

    public void setResource_id(String resource_id) {
        this.resource_id = resource_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLease_term_type() {
        return lease_term_type;
    }

    public void setLease_term_type(String lease_term_type) {
        this.lease_term_type = lease_term_type;
    }

    public String getLease_term_type_id() {
        return lease_term_type_id;
    }

    public void setLease_term_type_id(String lease_term_type_id) {
        this.lease_term_type_id = lease_term_type_id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(String number_of_people) {
        this.number_of_people = number_of_people;
    }

    public String getOccupancy_rate() {
        return occupancy_rate;
    }

    public void setOccupancy_rate(String occupancy_rate) {
        this.occupancy_rate = occupancy_rate;
    }

    public String getAd_print_type() {
        return ad_print_type;
    }

    public void setAd_print_type(String ad_print_type) {
        this.ad_print_type = ad_print_type;
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getField_type() {
        return field_type;
    }

    public void setField_type(String field_type) {
        this.field_type = field_type;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount_of_time_unit() {
        return count_of_time_unit;
    }

    public void setCount_of_time_unit(int count_of_time_unit) {
        this.count_of_time_unit = count_of_time_unit;
    }

    public int getMaximum_count() {
        return maximum_count;
    }

    public void setMaximum_count(int maximum_count) {
        this.maximum_count = maximum_count;
    }

    public String getSubsidy_fee() {
        return subsidy_fee;
    }

    public void setSubsidy_fee(String subsidy_fee) {
        this.subsidy_fee = subsidy_fee;
    }

    public String getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(String discount_rate) {
        this.discount_rate = discount_rate;
    }

    public String getTax_point() {
        return tax_point;
    }

    public void setTax_point(String tax_point) {
        this.tax_point = tax_point;
    }

    public String getSpecial_tax_point() {
        return special_tax_point;
    }

    public void setSpecial_tax_point(String special_tax_point) {
        this.special_tax_point = special_tax_point;
    }

    public String getCustom_dimension() {
        return custom_dimension;
    }

    public void setCustom_dimension(String custom_dimension) {
        this.custom_dimension = custom_dimension;
    }

    public Integer getMinimum_order_quantity() {
        return minimum_order_quantity;
    }

    public void setMinimum_order_quantity(Integer minimum_order_quantity) {
        this.minimum_order_quantity = minimum_order_quantity;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Integer getDays_in_advance() {
        return days_in_advance;
    }

    public void setDays_in_advance(Integer days_in_advance) {
        this.days_in_advance = days_in_advance;
    }

    public int getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(int community_id) {
        this.community_id = community_id;
    }

    public int getPhysical_resource_id() {
        return physical_resource_id;
    }

    public void setPhysical_resource_id(int physical_resource_id) {
        this.physical_resource_id = physical_resource_id;
    }

    public int getSelling_resource_id() {
        return selling_resource_id;
    }

    public void setSelling_resource_id(int selling_resource_id) {
        this.selling_resource_id = selling_resource_id;
    }
}
