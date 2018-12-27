package com.linhuiba.business.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/13.
 */
public class ResourceInfoModel implements Serializable{
    private ResourceInfoFieldInfoModel resource = new ResourceInfoFieldInfoModel();
    private String weather;

    public ResourceInfoFieldInfoModel getResource() {
        return resource;
    }

    public void setResource(ResourceInfoFieldInfoModel resource) {
        this.resource = resource;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}