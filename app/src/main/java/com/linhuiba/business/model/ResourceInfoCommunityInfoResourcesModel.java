package com.linhuiba.business.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/27.
 */

public class ResourceInfoCommunityInfoResourcesModel implements Serializable{
    private int id;
    private String name;
    private String doLocation;
    private int min_price;
    private int max_price;
    private int indoor = -1;
    private int is_agent = -1;
    private int number_of_people;

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

    public String getDoLocation() {
        return doLocation;
    }

    public void setDoLocation(String doLocation) {
        this.doLocation = doLocation;
    }

    public int getMin_price() {
        return min_price;
    }

    public void setMin_price(int min_price) {
        this.min_price = min_price;
    }

    public int getMax_price() {
        return max_price;
    }

    public void setMax_price(int max_price) {
        this.max_price = max_price;
    }

    public int getIndoor() {
        return indoor;
    }

    public void setIndoor(int indoor) {
        this.indoor = indoor;
    }

    public int getIs_agent() {
        return is_agent;
    }

    public void setIs_agent(int is_agent) {
        this.is_agent = is_agent;
    }

    public int getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(int number_of_people) {
        this.number_of_people = number_of_people;
    }
}
