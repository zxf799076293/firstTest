package com.linhuiba.linhuifield.fieldmodel;

import android.content.Intent;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/3/21.
 */

public class ReceiveAccountModel implements Serializable {
    private int id;
    private String account_owner;
    private String account;
    private String pay_type;
    private String opening_bank;
    private Integer flag;
    private Integer user_recevice_id;
    private HashMap<String,String> payment_method = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount_owner() {
        return account_owner;
    }

    public void setAccount_owner(String account_owner) {
        this.account_owner = account_owner;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getOpening_bank() {
        return opening_bank;
    }

    public void setOpening_bank(String opening_bank) {
        this.opening_bank = opening_bank;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getUser_recevice_id() {
        return user_recevice_id;
    }

    public void setUser_recevice_id(Integer user_recevice_id) {
        this.user_recevice_id = user_recevice_id;
    }

    public HashMap<String, String> getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(HashMap<String, String> payment_method) {
        this.payment_method = payment_method;
    }
}
