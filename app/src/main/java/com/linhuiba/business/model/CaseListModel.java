package com.linhuiba.business.model;

import com.linhuiba.linhuifield.fieldmodel.Field_AddResourceCreateItemModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/5.
 */

public class CaseListModel {
    private int id;
    private String title = "";//标题
    private String cover_pic_url = "";//封面图片地址
    private ArrayList<Field_AddResourceCreateItemModel> activity_case_url = new ArrayList();
    private int source;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover_pic_url() {
        return cover_pic_url;
    }

    public void setCover_pic_url(String cover_pic_url) {
        this.cover_pic_url = cover_pic_url;
    }

    public ArrayList<Field_AddResourceCreateItemModel> getActivity_case_url() {
        return activity_case_url;
    }

    public void setActivity_case_url(ArrayList<Field_AddResourceCreateItemModel> activity_case_url) {
        this.activity_case_url = activity_case_url;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
