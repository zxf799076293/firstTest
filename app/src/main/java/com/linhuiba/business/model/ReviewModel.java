package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldSellResDimensionsModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuifield.fieldmvpview.FieldAddFieldContactMvpViewl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/3.
 */
public class ReviewModel {
    private String field_order_item_id;
    private String score;
    private String content;
    private String reviewed_at;
    private String name;
    private String people_flow;
    private ArrayList<HashMap<String,String>> tags;
    private ArrayList<String> images;
    private CommentScoreModel detailScore = new CommentScoreModel();
    private String size;//规格
    private String industry;//用户所属行业
    //评价中心
    private int id;
    private String doLocation;
    private String full_name;
    private Integer number_of_people;
    private ArrayList<FieldAddfieldSellResDimensionsModel> sizes;
    private ArrayList<Integer> field_order_item_ids;
    private int reviewed;
    private String execute_time;
    private Field_AddResourceCreateItemModel physical_resource_first_img;

    public String getField_order_item_id() {
        return field_order_item_id;
    }

    public void setField_order_item_id(String field_order_item_id) {
        this.field_order_item_id = field_order_item_id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReviewed_at() {
        return reviewed_at;
    }

    public void setReviewed_at(String reviewed_at) {
        this.reviewed_at = reviewed_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeople_flow() {
        return people_flow;
    }

    public void setPeople_flow(String people_flow) {
        this.people_flow = people_flow;
    }

    public ArrayList<HashMap<String, String>> getTags() {
        return tags;
    }

    public void setTags(ArrayList<HashMap<String, String>> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public CommentScoreModel getDetailScore() {
        return detailScore;
    }

    public void setDetailScore(CommentScoreModel detailScore) {
        this.detailScore = detailScore;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoLocation() {
        return doLocation;
    }

    public void setDoLocation(String doLocation) {
        this.doLocation = doLocation;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Integer getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(Integer number_of_people) {
        this.number_of_people = number_of_people;
    }

    public ArrayList<FieldAddfieldSellResDimensionsModel> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<FieldAddfieldSellResDimensionsModel> sizes) {
        this.sizes = sizes;
    }

    public ArrayList<Integer> getField_order_item_ids() {
        return field_order_item_ids;
    }

    public void setField_order_item_ids(ArrayList<Integer> field_order_item_ids) {
        this.field_order_item_ids = field_order_item_ids;
    }

    public int getReviewed() {
        return reviewed;
    }

    public void setReviewed(int reviewed) {
        this.reviewed = reviewed;
    }

    public String getExecute_time() {
        return execute_time;
    }

    public void setExecute_time(String execute_time) {
        this.execute_time = execute_time;
    }

    public Field_AddResourceCreateItemModel getPhysical_resource_first_img() {
        return physical_resource_first_img;
    }

    public void setPhysical_resource_first_img(Field_AddResourceCreateItemModel physical_resource_first_img) {
        this.physical_resource_first_img = physical_resource_first_img;
    }
}
