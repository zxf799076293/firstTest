package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchListAttributesModel implements Serializable {
    private int id;
    private ArrayList<Integer> option_ids = new ArrayList<>();
    private String name;
    private ArrayList<Field_AddResourceCreateItemModel> options;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getOption_ids() {
        return option_ids;
    }

    public void setOption_ids(ArrayList<Integer> option_ids) {
        this.option_ids = option_ids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Field_AddResourceCreateItemModel> options) {
        this.options = options;
    }
}
