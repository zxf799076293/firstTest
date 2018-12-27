package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeDynamicCommunityModel implements Serializable {
    private int id;
    private String name;
    private int plate;
    private int seq;
    private String display_name;
    private String type;
    private ArrayList<HomeDynamicCommunityModel> data;
    private int drawable;
    //导航栏配置
    private String title;
    private ArrayList<Field_AddResourceCreateItemModel> categories;
    private String pic_url;

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

    public int getPlate() {
        return plate;
    }

    public void setPlate(int plate) {
        this.plate = plate;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<HomeDynamicCommunityModel> getData() {
        return data;
    }

    public void setData(ArrayList<HomeDynamicCommunityModel> data) {
        this.data = data;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Field_AddResourceCreateItemModel> categories) {
        this.categories = categories;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }
}
