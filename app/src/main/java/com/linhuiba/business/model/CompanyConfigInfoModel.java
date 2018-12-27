package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/7.
 */

public class CompanyConfigInfoModel {
    private ArrayList<Field_AddResourceCreateItemModel> pushfrequency;
    private ArrayList<Field_AddResourceCreateItemModel> agelevel;
    private ArrayList<Field_AddResourceCreateItemModel> consumptionlevel;
    private ArrayList<Field_AddResourceCreateItemModel> spreadway;
    private ArrayList<Integer> catering_industry_id;

    public ArrayList<Field_AddResourceCreateItemModel> getPushfrequency() {
        return pushfrequency;
    }

    public void setPushfrequency(ArrayList<Field_AddResourceCreateItemModel> pushfrequency) {
        this.pushfrequency = pushfrequency;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getAgelevel() {
        return agelevel;
    }

    public void setAgelevel(ArrayList<Field_AddResourceCreateItemModel> agelevel) {
        this.agelevel = agelevel;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getConsumptionlevel() {
        return consumptionlevel;
    }

    public void setConsumptionlevel(ArrayList<Field_AddResourceCreateItemModel> consumptionlevel) {
        this.consumptionlevel = consumptionlevel;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getSpreadway() {
        return spreadway;
    }

    public void setSpreadway(ArrayList<Field_AddResourceCreateItemModel> spreadway) {
        this.spreadway = spreadway;
    }

    public ArrayList<Integer> getCatering_industry_id() {
        return catering_industry_id;
    }

    public void setCatering_industry_id(ArrayList<Integer> catering_industry_id) {
        this.catering_industry_id = catering_industry_id;
    }
}
