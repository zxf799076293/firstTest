package com.linhuiba.business.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/18.
 */
public class IndustriesModel {
    private int id;
    private int parent_id;
    private String name;
    private String display_name;
    private ArrayList<IndustriesModel> selected_children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
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

    public ArrayList<IndustriesModel> getSelected_children() {
        return selected_children;
    }

    public void setSelected_children(ArrayList<IndustriesModel> selected_children) {
        this.selected_children = selected_children;
    }
}
