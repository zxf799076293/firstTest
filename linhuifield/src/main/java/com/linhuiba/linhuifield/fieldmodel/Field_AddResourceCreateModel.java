package com.linhuiba.linhuifield.fieldmodel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/23.
 */

public class Field_AddResourceCreateModel implements Serializable {
    private ArrayList<Field_AddResourceCreateItemModel> city;
    private ArrayList<Field_AddResourceCreateItemModel> field_type;
    private ArrayList<Field_AddResourceCreateItemModel> activity_type;
    private ArrayList<Field_AddResourceCreateItemModel> custom_dimension;
    private ArrayList<Field_AddResourceCreateItemModel> contraband;
    private ArrayList<Field_AddResourceCreateItemModel> requirement;
    private ArrayList<Field_AddResourceCreateItemModel> lease_term_type;
    //编辑时获取的收款信息的字段
    private ArrayList<ReceiveAccountModel> user_receivables_informations = new ArrayList<>();
    private ArrayList<AddfieldCommunityCategoriesModel> categories;
    private ArrayList<String> building_years;
    private ArrayList<Field_AddResourceCreateItemModel> location_type;
    public ArrayList<Field_AddResourceCreateItemModel> getCity() {
        return city;
    }

    public void setCity(ArrayList<Field_AddResourceCreateItemModel> city) {
        this.city = city;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getField_type() {
        return field_type;
    }

    public void setField_type(ArrayList<Field_AddResourceCreateItemModel> field_type) {
        this.field_type = field_type;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(ArrayList<Field_AddResourceCreateItemModel> activity_type) {
        this.activity_type = activity_type;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getCustom_dimension() {
        return custom_dimension;
    }

    public void setCustom_dimension(ArrayList<Field_AddResourceCreateItemModel> custom_dimension) {
        this.custom_dimension = custom_dimension;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getContraband() {
        return contraband;
    }

    public void setContraband(ArrayList<Field_AddResourceCreateItemModel> contraband) {
        this.contraband = contraband;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getRequirement() {
        return requirement;
    }

    public void setRequirement(ArrayList<Field_AddResourceCreateItemModel> requirement) {
        this.requirement = requirement;
    }
    public ArrayList<Field_AddResourceCreateItemModel> getLease_term_type() {
        return lease_term_type;
    }

    public void setLease_term_type(ArrayList<Field_AddResourceCreateItemModel> lease_term_type) {
        this.lease_term_type = lease_term_type;
    }
    public ArrayList<ReceiveAccountModel> getUser_receivables_informations() {
        return user_receivables_informations;
    }

    public void setUser_receivables_informations(ArrayList<ReceiveAccountModel> user_receivables_informations) {
        this.user_receivables_informations = user_receivables_informations;
    }
    public ArrayList<AddfieldCommunityCategoriesModel> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<AddfieldCommunityCategoriesModel> categories) {
        this.categories = categories;
    }

    public ArrayList<String> getBuilding_years() {
        return building_years;
    }

    public void setBuilding_years(ArrayList<String> building_years) {
        this.building_years = building_years;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getLocation_type() {
        return location_type;
    }

    public void setLocation_type(ArrayList<Field_AddResourceCreateItemModel> location_type) {
        this.location_type = location_type;
    }
}
