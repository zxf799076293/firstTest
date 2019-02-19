package com.linhuiba.business.model;
/**
 * Created by Administrator on 2018/7/5.
 */

public class CaseInfoPhysicalResources {
    private int id;
    private String name = "";
    private Integer number_of_people;
    private Integer number_of_order;
    private String average_score;
    private Double price;
    private String physical_resource_first_img = "";
    private int res_type_id;
    private int selling_resource_id;
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

    public Integer getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(Integer number_of_people) {
        this.number_of_people = number_of_people;
    }

    public Integer getNumber_of_order() {
        return number_of_order;
    }

    public void setNumber_of_order(Integer number_of_order) {
        this.number_of_order = number_of_order;
    }

    public String getAverage_score() {
        return average_score;
    }

    public void setAverage_score(String average_score) {
        this.average_score = average_score;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPhysical_resource_first_img() {
        return physical_resource_first_img;
    }

    public void setPhysical_resource_first_img(String physical_resource_first_img) {
        this.physical_resource_first_img = physical_resource_first_img;
    }

    public int getRes_type_id() {
        return res_type_id;
    }

    public void setRes_type_id(int res_type_id) {
        this.res_type_id = res_type_id;
    }

    public int getSelling_resource_id() {
        return selling_resource_id;
    }

    public void setSelling_resource_id(int selling_resource_id) {
        this.selling_resource_id = selling_resource_id;
    }
}
