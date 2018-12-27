package com.linhuiba.business.model;

import android.accounts.AbstractAccountAuthenticator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/8/11.
 */
public class ChildAccountManagementAllModel {
    private ArrayList<ChildAccountManagementModel> message;
    private ArrayList<HashMap<String,String>> resList;

    public ArrayList<ChildAccountManagementModel> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<ChildAccountManagementModel> message) {
        this.message = message;
    }

    public ArrayList<HashMap<String, String>> getResList() {
        return resList;
    }

    public void setResList(ArrayList<HashMap<String, String>> resList) {
        this.resList = resList;
    }
}
