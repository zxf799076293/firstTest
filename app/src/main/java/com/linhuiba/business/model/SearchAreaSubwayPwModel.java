package com.linhuiba.business.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/15.
 */

public class SearchAreaSubwayPwModel implements Serializable {
    private String id;
    private String name;
    private ArrayList<SearchAreaPwModel> stations;

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

    public ArrayList<SearchAreaPwModel> getStations() {
        return stations;
    }

    public void setStations(ArrayList<SearchAreaPwModel> stations) {
        this.stations = stations;
    }
}
