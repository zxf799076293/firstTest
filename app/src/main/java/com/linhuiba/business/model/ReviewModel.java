package com.linhuiba.business.model;

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
}
