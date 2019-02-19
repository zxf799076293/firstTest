package com.linhuiba.business.model;

/**
 * Created by Administrator on 2016/8/2.
 */
public class FavoritesModel {
    private String target_id;
    private String name;
    private int fixed_price;
    private String detail_address;
    private Integer resource_type;
    private String field_type;
    private String activity_type;
    private String ad_type;
    private String pic_url;
    private String pid;

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFixed_price() {
        return fixed_price;
    }

    public void setFixed_price(int fixed_price) {
        this.fixed_price = fixed_price;
    }

    public String getDetail_address() {
        return detail_address;
    }

    public void setDetail_address(String detail_address) {
        this.detail_address = detail_address;
    }

    public Integer getResource_type() {
        return resource_type;
    }

    public void setResource_type(Integer resource_type) {
        this.resource_type = resource_type;
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

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
