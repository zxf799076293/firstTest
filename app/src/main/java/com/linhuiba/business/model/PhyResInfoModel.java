package com.linhuiba.business.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PhyResInfoModel implements Serializable {
    private PhysicalResourceModel physical_resource = new PhysicalResourceModel();
    private ArrayList<FieldInfoSizeModel> size = new ArrayList<>();
    private HashMap<String,String> weather;
    private ArrayList<FieldInfoSellResourceModel> enquiry_resource = new ArrayList<>();
    private String industry_str;
    private String service_phone;
    private ArrayList<MyCouponsModel> coupons = new ArrayList<>();
    private FieldinfoCounselorModel service_representative;
    public PhysicalResourceModel getPhysical_resource() {
        return physical_resource;
    }

    public void setPhysical_resource(PhysicalResourceModel physical_resource) {
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

    public ArrayList<MyCouponsModel> getCoupons() {
        return coupons;
    }

    public void setCoupons(ArrayList<MyCouponsModel> coupons) {
        this.coupons = coupons;
    }

    public FieldinfoCounselorModel getService_representative() {
        return service_representative;
    }

    public void setService_representative(FieldinfoCounselorModel service_representative) {
        this.service_representative = service_representative;
    }
}
