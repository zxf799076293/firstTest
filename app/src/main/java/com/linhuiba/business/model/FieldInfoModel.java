package com.linhuiba.business.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/21.
 */
public class FieldInfoModel {
    private CommunityInfoModel community;
    private FieldInfoPhysicalResourceModel physical_resource = new FieldInfoPhysicalResourceModel();
    private ArrayList<FieldInfoSizeModel> size = new ArrayList<>();
    private HashMap<String,String> weather;
    private ArrayList<FieldInfoSellResourceModel> enquiry_resource = new ArrayList<>();
    private String industry_str;
    private String service_phone;

    public CommunityInfoModel getCommunity() {
        return community;
    }

    public void setCommunity(CommunityInfoModel community) {
        this.community = community;
    }

    public FieldInfoPhysicalResourceModel getPhysical_resource() {
        return physical_resource;
    }

    public void setPhysical_resource(FieldInfoPhysicalResourceModel physical_resource) {
        this.physical_resource = physical_resource;
    }

    public ArrayList<FieldInfoSizeModel> getSize() {
        return size;
    }

    public void setSize(ArrayList<FieldInfoSizeModel> size) {
        this.size = size;
    }

    public HashMap<String, String> getWeather() {
        return weather;
    }

    public void setWeather(HashMap<String, String> weather) {
        this.weather = weather;
    }

    public ArrayList<FieldInfoSellResourceModel> getEnquiry_resource() {
        return enquiry_resource;
    }

    public void setEnquiry_resource(ArrayList<FieldInfoSellResourceModel> enquiry_resource) {
        this.enquiry_resource = enquiry_resource;
    }

    public String getIndustry_str() {
        return industry_str;
    }

    public void setIndustry_str(String industry_str) {
        this.industry_str = industry_str;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }
}
