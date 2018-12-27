package com.linhuiba.business.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/12.
 */
public class ScheduleCalendarModel {
    private int field;
    private int ad;
    private int active;
    private int deal;
    private ArrayList<ScheduleCalendarlistModel> info;
    private String weather;

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }

    public int getAd() {
        return ad;
    }

    public void setAd(int ad) {
        this.ad = ad;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getDeal() {
        return deal;
    }

    public void setDeal(int deal) {
        this.deal = deal;
    }

    public ArrayList<ScheduleCalendarlistModel> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<ScheduleCalendarlistModel> info) {
        this.info = info;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
