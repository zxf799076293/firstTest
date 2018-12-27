package com.linhuiba.linhuifield.fieldmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/8/5.
 */
public class ChildAccountManagementModel implements Serializable{
    private ArrayList<HashMap<String,String>> res_ids;
    private String id;
    private String name;
    private String mobile;
    private String email;

    public ArrayList<HashMap<String,String>> getRes_ids() {
        return res_ids;
    }

    public void setRes_ids(ArrayList<HashMap<String,String>> res_ids) {
        this.res_ids = res_ids;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
