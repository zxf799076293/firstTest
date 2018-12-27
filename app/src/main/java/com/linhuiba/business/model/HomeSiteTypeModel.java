package com.linhuiba.business.model;

import com.linhuiba.business.activity.ResourcesCartItemsActivity;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/11.
 */

public class HomeSiteTypeModel implements Serializable {
    private Integer id;
    private String name;
    private Integer serial_number;
    private String home_map_pic_url;
    private String list_page_pic_url;
    private ArrayList<Field_AddResourceCreateItemModel> field_type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(Integer serial_number) {
        this.serial_number = serial_number;
    }

    public String getHome_map_pic_url() {
        return home_map_pic_url;
    }

    public void setHome_map_pic_url(String home_map_pic_url) {
        this.home_map_pic_url = home_map_pic_url;
    }

    public String getList_page_pic_url() {
        return list_page_pic_url;
    }

    public void setList_page_pic_url(String list_page_pic_url) {
        this.list_page_pic_url = list_page_pic_url;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getField_type() {
        return field_type;
    }

    public void setField_type(ArrayList<Field_AddResourceCreateItemModel> field_type) {
        this.field_type = field_type;
    }
}
