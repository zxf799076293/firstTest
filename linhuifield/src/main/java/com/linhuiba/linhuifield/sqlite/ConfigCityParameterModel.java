package com.linhuiba.linhuifield.sqlite;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/24.
 */

public class ConfigCityParameterModel implements Serializable {
    private int district_id;
    private String name = "";
    private int code;
    private int city_id;
    private int id;
    private String display_name = "";
    private Double lat;
    private Double lng;
    private int subway_line_id;
    private String station_name = "";
    private String detail_address = "";
    private int seq;
    private String city = "";
    private String province = "";
    private int province_id;
    private Double latitude;
    private Double longitude;
    private String service_phone = "";
    private int default_city;
    private ArrayList<ConfigCityParameterModel> stations = new ArrayList<>();

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public int getSubway_line_id() {
        return subway_line_id;
    }

    public void setSubway_line_id(int subway_line_id) {
        this.subway_line_id = subway_line_id;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String getDetail_address() {
        return detail_address;
    }

    public void setDetail_address(String detail_address) {
        this.detail_address = detail_address;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    public int getDefault_city() {
        return default_city;
    }

    public void setDefault_city(int default_city) {
        this.default_city = default_city;
    }

    public ArrayList<ConfigCityParameterModel> getStations() {
        return stations;
    }

    public void setStations(ArrayList<ConfigCityParameterModel> stations) {
        this.stations = stations;
    }
}
