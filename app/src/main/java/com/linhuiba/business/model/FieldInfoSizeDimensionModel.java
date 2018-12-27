package com.linhuiba.business.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/20.
 */

public class FieldInfoSizeDimensionModel implements Serializable {
    private String size = "";
    private String lease_term_type;
    private String custom_dimension;
    private Double min_price;//分 几元起

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLease_term_type() {
        return lease_term_type;
    }

    public void setLease_term_type(String lease_term_type) {
        this.lease_term_type = lease_term_type;
    }
    public String getCustom_dimension() {
        return custom_dimension;
    }

    public void setCustom_dimension(String custom_dimension) {
        this.custom_dimension = custom_dimension;
    }

    public Double getMin_price() {
        return min_price;
    }

    public void setMin_price(Double min_price) {
        this.min_price = min_price;
    }
}
