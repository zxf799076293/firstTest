package com.linhuiba.linhuifield.fieldmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/6.
 */
public class Field_AddResourcesModel {
    private Field_AddResourcesInfoModel resource = new Field_AddResourcesInfoModel();

    //编辑时获取的详情多的字段
    private Field_AddResourceCreateModel options;

    //发布时需要保存显示的字段
    private List<String> customsizelist;//选中的固定摊位大小
    private List<String> default_dimensionlist;//选中的固定特殊规格
    private ArrayList<HashMap<String,String>> fieldsizelist;//输入的自定义摊位大小
    private ArrayList<HashMap<String,String>> custom_dimensionlist;//输入的自定义特殊规格
    private List<String> denominatedunitlist;//选中的时间单位
    //价格编辑是判断是否保存
    private ArrayList<String> sizelist;//上一页规格中大小list
    private ArrayList<Map<String,String>> lease_term_type_id_list;//上一页规格中时间list
    private ArrayList<Map<String,String>> custom_dimension_field_list;//上一页规格中特殊品类list
    //发布时判断是否填写完整的字段
    private int is_hava_field_info;
    private int is_hava_field_price_standard;
    private int is_hava_field_price;
    private int is_hava_field_rules;
    private int is_hava_field_services;
    private int is_hava_field_contact;
    private int is_hava_community_info;
    private int is_hava_activity_info;

    static Field_AddResourcesModel sInstance;
    Field_AddResourcesModel() { }
    public static Field_AddResourcesModel getInstance() {
        if (sInstance == null) {
            sInstance = new Field_AddResourcesModel();
        }
        return sInstance;
    }
    public static Field_AddResourcesModel getnewInstance() {
        sInstance = new Field_AddResourcesModel();
        return sInstance;
    }
    public static Field_AddResourcesModel getnewInstance(Field_AddResourcesModel field_addResourcesModel) {
        sInstance = new Field_AddResourcesModel();
        sInstance = field_addResourcesModel;
        return sInstance;
    }

    public Field_AddResourcesInfoModel getResource() {
        return resource;
    }

    public void setResource(Field_AddResourcesInfoModel resource) {
        this.resource = resource;
    }
    //新增场地时删除上一次选中的价格规格
    public void setPriceConfigNull() {
        this.customsizelist = null;
        this.default_dimensionlist = null;
        this.fieldsizelist = null;
        this.custom_dimensionlist = null;
        this.denominatedunitlist = null;
        this.sizelist = null;
        this.lease_term_type_id_list = null;
        this.custom_dimension_field_list = null;
    }

    public Field_AddResourceCreateModel getOptions() {
        return options;
    }

    public void setOptions(Field_AddResourceCreateModel options) {
        this.options = options;
    }

    public List<String> getCustomsizelist() {
        return customsizelist;
    }

    public void setCustomsizelist(List<String> customsizelist) {
        this.customsizelist = customsizelist;
    }

    public List<String> getDefault_dimensionlist() {
        return default_dimensionlist;
    }

    public void setDefault_dimensionlist(List<String> default_dimensionlist) {
        this.default_dimensionlist = default_dimensionlist;
    }

    public ArrayList<HashMap<String, String>> getFieldsizelist() {
        return fieldsizelist;
    }

    public void setFieldsizelist(ArrayList<HashMap<String, String>> fieldsizelist) {
        this.fieldsizelist = fieldsizelist;
    }

    public ArrayList<HashMap<String, String>> getCustom_dimensionlist() {
        return custom_dimensionlist;
    }

    public void setCustom_dimensionlist(ArrayList<HashMap<String, String>> custom_dimensionlist) {
        this.custom_dimensionlist = custom_dimensionlist;
    }

    public List<String> getDenominatedunitlist() {
        return denominatedunitlist;
    }

    public void setDenominatedunitlist(List<String> denominatedunitlist) {
        this.denominatedunitlist = denominatedunitlist;
    }

    public ArrayList<String> getSizelist() {
        return sizelist;
    }

    public void setSizelist(ArrayList<String> sizelist) {
        this.sizelist = sizelist;
    }

    public ArrayList<Map<String, String>> getLease_term_type_id_list() {
        return lease_term_type_id_list;
    }

    public void setLease_term_type_id_list(ArrayList<Map<String, String>> lease_term_type_id_list) {
        this.lease_term_type_id_list = lease_term_type_id_list;
    }

    public ArrayList<Map<String, String>> getCustom_dimension_field_list() {
        return custom_dimension_field_list;
    }

    public void setCustom_dimension_field_list(ArrayList<Map<String, String>> custom_dimension_field_list) {
        this.custom_dimension_field_list = custom_dimension_field_list;
    }

    public int getIs_hava_field_info() {
        return is_hava_field_info;
    }

    public void setIs_hava_field_info(int is_hava_field_info) {
        this.is_hava_field_info = is_hava_field_info;
    }

    public int getIs_hava_field_price_standard() {
        return is_hava_field_price_standard;
    }

    public void setIs_hava_field_price_standard(int is_hava_field_price_standard) {
        this.is_hava_field_price_standard = is_hava_field_price_standard;
    }

    public int getIs_hava_field_price() {
        return is_hava_field_price;
    }

    public void setIs_hava_field_price(int is_hava_field_price) {
        this.is_hava_field_price = is_hava_field_price;
    }

    public int getIs_hava_field_rules() {
        return is_hava_field_rules;
    }

    public void setIs_hava_field_rules(int is_hava_field_rules) {
        this.is_hava_field_rules = is_hava_field_rules;
    }

    public int getIs_hava_field_services() {
        return is_hava_field_services;
    }

    public void setIs_hava_field_services(int is_hava_field_services) {
        this.is_hava_field_services = is_hava_field_services;
    }

    public int getIs_hava_field_contact() {
        return is_hava_field_contact;
    }

    public void setIs_hava_field_contact(int is_hava_field_contact) {
        this.is_hava_field_contact = is_hava_field_contact;
    }

    public int getIs_hava_community_info() {
        return is_hava_community_info;
    }

    public void setIs_hava_community_info(int is_hava_community_info) {
        this.is_hava_community_info = is_hava_community_info;
    }

    public int getIs_hava_activity_info() {
        return is_hava_activity_info;
    }

    public void setIs_hava_activity_info(int is_hava_activity_info) {
        this.is_hava_activity_info = is_hava_activity_info;
    }
}
