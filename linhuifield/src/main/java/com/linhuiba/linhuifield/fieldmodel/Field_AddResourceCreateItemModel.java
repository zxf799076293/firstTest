package com.linhuiba.linhuifield.fieldmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/5/23.
 */

public class Field_AddResourceCreateItemModel implements Serializable {
    private int id;
    private String name = "";
    private String display_name = "";
    private int province_id;
    private int city_id;
    private int period;
    private String center;
    private String spread_way;
    private double lat;
    private double lng;
    private HashMap<String,Object> search_map_address_back_map;
    private String pic_url = "";
    private Integer field_id;
    private String activity_case_url = "";
    private String title = "";
    private String value = "";
    private HashMap<String,String> pivot;
    private List<Field_AddResourceCreateItemModel> districts;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getSpread_way() {
        return spread_way;
    }

    public void setSpread_way(String spread_way) {
        this.spread_way = spread_way;
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

    public HashMap<String, Object> getSearch_map_address_back_map() {
        return search_map_address_back_map;
    }

    public void setSearch_map_address_back_map(HashMap<String, Object> search_map_address_back_map) {
        this.search_map_address_back_map = search_map_address_back_map;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public Integer getField_id() {
        return field_id;
    }

    public void setField_id(Integer field_id) {
        this.field_id = field_id;
    }

    public String getActivity_case_url() {
        return activity_case_url;
    }

    public void setActivity_case_url(String activity_case_url) {
        this.activity_case_url = activity_case_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public HashMap<String, String> getPivot() {
        return pivot;
    }

    public void setPivot(HashMap<String, String> pivot) {
        this.pivot = pivot;
    }

    public List<Field_AddResourceCreateItemModel> getDistricts() {
        return districts;
    }

    public void setDistricts(List<Field_AddResourceCreateItemModel> districts) {
        this.districts = districts;
    }
}
