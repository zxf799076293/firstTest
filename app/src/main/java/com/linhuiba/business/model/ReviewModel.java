package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldSellResDimensionsModel;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;
import com.linhuiba.linhuifield.fieldmvpview.FieldAddFieldContactMvpViewl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/3.
 */
public class ReviewModel implements Serializable {
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
    private ArrayList<Field_AddResourceCreateItemModel> promotion_purposes;
    private ArrayList<Field_AddResourceCreateItemModel> spread_ways;
    private Integer score_of_visitorsflowrate;
    private int type;
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

    public ArrayList<Field_AddResourceCreateItemModel> getPromotion_purposes() {
        return promotion_purposes;
    }

    public void setPromotion_purposes(ArrayList<Field_AddResourceCreateItemModel> promotion_purposes) {
        this.promotion_purposes = promotion_purposes;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getSpread_ways() {
        return spread_ways;
    }

    public void setSpread_ways(ArrayList<Field_AddResourceCreateItemModel> spread_ways) {
        this.spread_ways = spread_ways;
    }

    public Integer getScore_of_visitorsflowrate() {
        return score_of_visitorsflowrate;
    }

    public void setScore_of_visitorsflowrate(Integer score_of_visitorsflowrate) {
        this.score_of_visitorsflowrate = score_of_visitorsflowrate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
