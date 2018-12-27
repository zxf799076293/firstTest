package com.linhuiba.linhuifield.fieldmodel;

import java.io.Serializable;
import java.util.ArrayList;

public class AddfieldCommunityCategoriesModel implements Serializable {
    private int id;
    private String name = "";
    private int level;
    private ArrayList<AddfieldCommunityCategoriesModel> categories;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ArrayList<AddfieldCommunityCategoriesModel> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<AddfieldCommunityCategoriesModel> categories) {
        this.categories = categories;
    }
}
