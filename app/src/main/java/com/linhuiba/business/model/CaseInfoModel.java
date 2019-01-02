package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/5.
 */

public class CaseInfoModel {
    private int id;
    private String title = "";//标题
    private String description = "";//描述内容
    private String address = "";//地址
    private Field_AddResourceCreateItemModel province = new Field_AddResourceCreateItemModel();
    private Field_AddResourceCreateItemModel city = new Field_AddResourceCreateItemModel();
    private Field_AddResourceCreateItemModel district = new Field_AddResourceCreateItemModel();
    private CaseInfoPhysicalResources physical_resources = new CaseInfoPhysicalResources();
    private ArrayList<Field_AddResourceCreateItemModel> activity_case_url = new ArrayList<>();
    private Integer last;
    private Integer next;
    private int source;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Field_AddResourceCreateItemModel getProvince() {
        return province;
    }

    public void setProvince(Field_AddResourceCreateItemModel province) {
        this.province = province;
    }

    public Field_AddResourceCreateItemModel getCity() {
        return city;
    }

    public void setCity(Field_AddResourceCreateItemModel city) {
        this.city = city;
    }

    public Field_AddResourceCreateItemModel getDistrict() {
        return district;
    }

    public void setDistrict(Field_AddResourceCreateItemModel district) {
        this.district = district;
    }

    public CaseInfoPhysicalResources getPhysical_resources() {
        return physical_resources;
    }

    public void setPhysical_resources(CaseInfoPhysicalResources physical_resources) {
        this.physical_resources = physical_resources;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getActivity_case_url() {
        return activity_case_url;
    }

    public void setActivity_case_url(ArrayList<Field_AddResourceCreateItemModel> activity_case_url) {
        this.activity_case_url = activity_case_url;
    }

    public Integer getLast() {
        return last;
    }

    public void setLast(Integer last) {
        this.last = last;
    }

    public Integer getNext() {
        return next;
    }

    public void setNext(Integer next) {
        this.next = next;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
