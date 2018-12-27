package com.linhuiba.business.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/5.
 */

public class CaseThemeModel {
    private int id;
    private String name = "";
    private String display_name = "";
    private ArrayList<CaseThemeModel> spread_ways = new ArrayList<>();
    private ArrayList<CaseThemeModel> promotion_purposes = new ArrayList<>();
    private ArrayList<CaseThemeModel> categories = new ArrayList<>();
    private ArrayList<CaseThemeModel> industries = new ArrayList<>();
    private ArrayList<CaseThemeModel> labels = new ArrayList<>();
    private ArrayList<CaseThemeModel> cities = new ArrayList<>();
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

    public ArrayList<CaseThemeModel> getSpread_ways() {
        return spread_ways;
    }

    public void setSpread_ways(ArrayList<CaseThemeModel> spread_ways) {
        this.spread_ways = spread_ways;
    }

    public ArrayList<CaseThemeModel> getPromotion_purposes() {
        return promotion_purposes;
    }

    public void setPromotion_purposes(ArrayList<CaseThemeModel> promotion_purposes) {
        this.promotion_purposes = promotion_purposes;
    }

    public ArrayList<CaseThemeModel> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CaseThemeModel> categories) {
        this.categories = categories;
    }

    public ArrayList<CaseThemeModel> getIndustries() {
        return industries;
    }

    public void setIndustries(ArrayList<CaseThemeModel> industries) {
        this.industries = industries;
    }

    public ArrayList<CaseThemeModel> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<CaseThemeModel> labels) {
        this.labels = labels;
    }

    public ArrayList<CaseThemeModel> getCities() {
        return cities;
    }

    public void setCities(ArrayList<CaseThemeModel> cities) {
        this.cities = cities;
    }
}
