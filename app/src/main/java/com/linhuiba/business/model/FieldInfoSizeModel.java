package com.linhuiba.business.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/20.
 */

public class FieldInfoSizeModel implements Serializable, Cloneable {
    private FieldInfoSizeDimensionModel dimension = new FieldInfoSizeDimensionModel();
    private ArrayList<FieldInfoSellResourceModel> resource = new ArrayList<>();

    public FieldInfoSizeDimensionModel getDimension() {
        return dimension;
    }

    public void setDimension(FieldInfoSizeDimensionModel dimension) {
        this.dimension = dimension;
    }

    public ArrayList<FieldInfoSellResourceModel> getResource() {
        return resource;
    }

    public void setResource(ArrayList<FieldInfoSellResourceModel> resource) {
        this.resource = resource;
    }

    public Object clone() {
        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }
        return o;
    }
}
