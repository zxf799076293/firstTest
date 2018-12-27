package com.linhuiba.business.model;

/**
 * Created by Administrator on 2016/8/6.
 */
public class CommunityResourcesItemsModel {
    private String id;
    private String name;
    private String price;
    private String discount_rate;
    private String res_type_id;
    private String res_type;
    private String img_url;
    private String address;
    private String community_name;
    private boolean headshow;
    private int HeaderId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(String discount_rate) {
        this.discount_rate = discount_rate;
    }

    public String getRes_type_id() {
        return res_type_id;
    }

    public void setRes_type_id(String res_type_id) {
        this.res_type_id = res_type_id;
    }

    public String getRes_type() {
        return res_type;
    }

    public void setRes_type(String res_type) {
        this.res_type = res_type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public boolean isHeadshow() {
        return headshow;
    }

    public void setHeadshow(boolean headshow) {
        this.headshow = headshow;
    }

    public int getHeaderId() {
        return HeaderId;
    }

    public void setHeaderId(int headerId) {
        HeaderId = headerId;
    }
}
