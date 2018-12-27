package com.linhuiba.business.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/8/2.
 */
public class TrackModel implements Serializable {
    private String field_order_item_id;
    private String cur_commnity_id;
    private String track_id;
    private String number_of_people;
    private String sale;
    private String number_of_fans;
    private ArrayList<HashMap<String,String>> pic_url;
    private String name;
    private String start;
    private String size;
    private double lat;
    private double lng;

    public String getField_order_item_id() {
        return field_order_item_id;
    }

    public void setField_order_item_id(String field_order_item_id) {
        this.field_order_item_id = field_order_item_id;
    }

    public String getCur_commnity_id() {
        return cur_commnity_id;
    }

    public void setCur_commnity_id(String cur_commnity_id) {
        this.cur_commnity_id = cur_commnity_id;
    }

    public String getTrack_id() {
        return track_id;
    }

    public void setTrack_id(String track_id) {
        this.track_id = track_id;
    }

    public String getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(String number_of_people) {
        this.number_of_people = number_of_people;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getNumber_of_fans() {
        return number_of_fans;
    }

    public void setNumber_of_fans(String number_of_fans) {
        this.number_of_fans = number_of_fans;
    }

    public ArrayList<HashMap<String, String>> getPic_url() {
        return pic_url;
    }

    public void setPic_url(ArrayList<HashMap<String, String>> pic_url) {
        this.pic_url = pic_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
