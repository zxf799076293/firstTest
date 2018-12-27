package com.linhuiba.business.model;

/**
 * Created by Administrator on 2016/3/3.
 */
public class SearchListModel {
    private String field_id;
    private String field_name;
    private String address;
    private String mexclusive_txt;
    private String msubsidy_txt_img;
    private String msearchlist_item_spareprice;
    private String reviewed_count;
    private String price;
    public String getField_id() {
        return field_id;
    }
    public void setField_id(String field_id) {
        this.field_id = field_id;
    }

    public String getField_name()  {
        return field_name;
    }
    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getMexclusive_txt() {
        return mexclusive_txt;
    }
    public void setMexclusive_txt(String mexclusive_txt) {
        this.mexclusive_txt = mexclusive_txt;
    }

    public String getMsubsidy_txt_img() {
        return msubsidy_txt_img;
    }
    public void setMsubsidy_txt_img(String msubsidy_txt_img) {
        this.msubsidy_txt_img = msubsidy_txt_img;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getMsearchlist_item_spareprice() {
        return msearchlist_item_spareprice;
    }
    public void setMsearchlist_item_spareprice(String msearchlist_item_spareprice) {
        this.msearchlist_item_spareprice = msearchlist_item_spareprice;
    }

    public String getReviewed_count() {
        return reviewed_count;
    }
    public void setReviewed_count(String reviewed_count) {
        this.reviewed_count = reviewed_count;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

}
