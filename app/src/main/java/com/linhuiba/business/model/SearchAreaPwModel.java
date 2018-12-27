package com.linhuiba.business.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/15.
 */

public class SearchAreaPwModel implements Serializable {
    private int id;
    private String name;
    private int subway_line_id;
    private String station_name;
    private double lng;
    private double lat;
    private int seq;
    private boolean isChoose;
    private ArrayList<SearchAreaPwModel> secondList;
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

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public ArrayList<SearchAreaPwModel> getSecondList() {
        return secondList;
    }

    public void setSecondList(ArrayList<SearchAreaPwModel> secondList) {
        this.secondList = secondList;
    }
}
