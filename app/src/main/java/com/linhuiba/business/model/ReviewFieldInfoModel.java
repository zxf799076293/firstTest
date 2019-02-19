package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.FieldAddfieldSellResDimensionsModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/28.
 */

public class ReviewFieldInfoModel {
    private String name;
    private String price;
    private int res_type_id;
    private String expired;
    private String pic_url;
    private boolean is_reviewed;
    private int anonymity;//是否匿名
    private String content = "";
    private int score;//整体评分
    private Integer score_of_visitorsflowrate;//人流量评分
    private Integer score_of_userparticipation;//用户参与度评分
    private Integer score_of_propertymatching;//物业配合度评分
    private Integer score_of_goalcompletion;//目标完成度评分
    private ArrayList<String> review_images = new ArrayList<>();
    private int number_of_people;
    private String size;
    private String type;//规格 - 日期
    private String execute_time;
    private ArrayList<Integer> field_order_item_ids;
    private String custom_dimension;
    private ArrayList<FieldAddfieldSellResDimensionsModel> sizes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getRes_type_id() {
        return res_type_id;
    }

    public void setRes_type_id(int res_type_id) {
        this.res_type_id = res_type_id;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public boolean isIs_reviewed() {
        return is_reviewed;
    }

    public void setIs_reviewed(boolean is_reviewed) {
        this.is_reviewed = is_reviewed;
    }

    public int getAnonymity() {
        return anonymity;
    }

    public void setAnonymity(int anonymity) {
        this.anonymity = anonymity;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Integer getScore_of_visitorsflowrate() {
        return score_of_visitorsflowrate;
    }

    public void setScore_of_visitorsflowrate(Integer score_of_visitorsflowrate) {
        this.score_of_visitorsflowrate = score_of_visitorsflowrate;
    }

    public Integer getScore_of_userparticipation() {
        return score_of_userparticipation;
    }

    public void setScore_of_userparticipation(Integer score_of_userparticipation) {
        this.score_of_userparticipation = score_of_userparticipation;
    }

    public Integer getScore_of_propertymatching() {
        return score_of_propertymatching;
    }

    public void setScore_of_propertymatching(Integer score_of_propertymatching) {
        this.score_of_propertymatching = score_of_propertymatching;
    }

    public Integer getScore_of_goalcompletion() {
        return score_of_goalcompletion;
    }

    public void setScore_of_goalcompletion(Integer score_of_goalcompletion) {
        this.score_of_goalcompletion = score_of_goalcompletion;
    }

    public ArrayList<String> getReview_images() {
        return review_images;
    }

    public void setReview_images(ArrayList<String> review_images) {
        this.review_images = review_images;
    }

    public int getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(int number_of_people) {
        this.number_of_people = number_of_people;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExecute_time() {
        return execute_time;
    }

    public void setExecute_time(String execute_time) {
        this.execute_time = execute_time;
    }

    public ArrayList<Integer> getField_order_item_ids() {
        return field_order_item_ids;
    }

    public void setField_order_item_ids(ArrayList<Integer> field_order_item_ids) {
        this.field_order_item_ids = field_order_item_ids;
    }

    public String getCustom_dimension() {
        return custom_dimension;
    }

    public void setCustom_dimension(String custom_dimension) {
        this.custom_dimension = custom_dimension;
    }

    public ArrayList<FieldAddfieldSellResDimensionsModel> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<FieldAddfieldSellResDimensionsModel> sizes) {
        this.sizes = sizes;
    }
}
