package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/18.
 */
public class CompanyModel {
    private String company;
    private String product;
    private String industry_id;
    private String industry_name;
    private ArrayList<String> images;
    private String cert_url;
    private Field_AddResourceCreateItemModel spread_way;
    private ArrayList<String> food_safety_license;
    private Double demand_area;
    private Field_AddResourceCreateItemModel pushing_frequency_level;
    private Double acceptable_minimum_price;
    private Double acceptable_maximum_price;
    private ArrayList<Field_AddResourceCreateItemModel> consumption_level;
    private ArrayList<Field_AddResourceCreateItemModel> age_level;
    private String feature_description;
    private int pushing_frequency_level_id  = -1;
    private String consumption_level_id;
    private String age_level_id;
    private int spread_way_id = -1;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getIndustry_id() {
        return industry_id;
    }

    public void setIndustry_id(String industry_id) {
        this.industry_id = industry_id;
    }

    public String getIndustry_name() {
        return industry_name;
    }

    public void setIndustry_name(String industry_name) {
        this.industry_name = industry_name;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getCert_url() {
        return cert_url;
    }

    public void setCert_url(String cert_url) {
        this.cert_url = cert_url;
    }

    public Field_AddResourceCreateItemModel getSpread_way() {
        return spread_way;
    }

    public void setSpread_way(Field_AddResourceCreateItemModel spread_way) {
        this.spread_way = spread_way;
    }

    public ArrayList<String> getFood_safety_license() {
        return food_safety_license;
    }

    public void setFood_safety_license(ArrayList<String> food_safety_license) {
        this.food_safety_license = food_safety_license;
    }

    public Double getDemand_area() {
        return demand_area;
    }

    public void setDemand_area(Double demand_area) {
        this.demand_area = demand_area;
    }

    public Field_AddResourceCreateItemModel getPushing_frequency_level() {
        return pushing_frequency_level;
    }

    public void setPushing_frequency_level(Field_AddResourceCreateItemModel pushing_frequency_level) {
        this.pushing_frequency_level = pushing_frequency_level;
    }

    public Double getAcceptable_minimum_price() {
        return acceptable_minimum_price;
    }

    public void setAcceptable_minimum_price(Double acceptable_minimum_price) {
        this.acceptable_minimum_price = acceptable_minimum_price;
    }

    public Double getAcceptable_maximum_price() {
        return acceptable_maximum_price;
    }

    public void setAcceptable_maximum_price(Double acceptable_maximum_price) {
        this.acceptable_maximum_price = acceptable_maximum_price;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getConsumption_level() {
        return consumption_level;
    }

    public void setConsumption_level(ArrayList<Field_AddResourceCreateItemModel> consumption_level) {
        this.consumption_level = consumption_level;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getAge_level() {
        return age_level;
    }

    public void setAge_level(ArrayList<Field_AddResourceCreateItemModel> age_level) {
        this.age_level = age_level;
    }

    public String getFeature_description() {
        return feature_description;
    }

    public void setFeature_description(String feature_description) {
        this.feature_description = feature_description;
    }

    public int getPushing_frequency_level_id() {
        return pushing_frequency_level_id;
    }

    public void setPushing_frequency_level_id(int pushing_frequency_level_id) {
        this.pushing_frequency_level_id = pushing_frequency_level_id;
    }

    public String getConsumption_level_id() {
        return consumption_level_id;
    }

    public void setConsumption_level_id(String consumption_level_id) {
        this.consumption_level_id = consumption_level_id;
    }

    public String getAge_level_id() {
        return age_level_id;
    }

    public void setAge_level_id(String age_level_id) {
        this.age_level_id = age_level_id;
    }

    public int getSpread_way_id() {
        return spread_way_id;
    }

    public void setSpread_way_id(int spread_way_id) {
        this.spread_way_id = spread_way_id;
    }
}
