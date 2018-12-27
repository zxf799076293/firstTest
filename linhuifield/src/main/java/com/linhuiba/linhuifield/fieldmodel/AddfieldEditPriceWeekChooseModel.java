package com.linhuiba.linhuifield.fieldmodel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/1/4.
 */

public class AddfieldEditPriceWeekChooseModel {
    private HashMap<Integer,Integer> childWeekChooseList = new HashMap<>();//child 周一到周日选中标识
    private HashMap<Integer,Integer> groupWeekChooseList = new HashMap<>();//group 周一到周日选中标识
    private int itemChoose;//checkbox选中标识
    private String price;
    private String size;
    private Integer id;//规格id
    private Integer lease_term_type_id;
    private String lease_term_type_name;
    private String custom_dimension;
    private Integer count_of_frame;
    private int hiteView;
    private HashMap<Integer,Integer> child_week_id = new HashMap<>();//child 周一到周日选中标识
    private String deposit;
    public HashMap<Integer, Integer> getChildWeekChooseList() {
        return childWeekChooseList;
    }

    public void setChildWeekChooseList(HashMap<Integer, Integer> childWeekChooseList) {
        this.childWeekChooseList = childWeekChooseList;
    }

    public HashMap<Integer, Integer> getGroupWeekChooseList() {
        return groupWeekChooseList;
    }

    public void setGroupWeekChooseList(HashMap<Integer, Integer> groupWeekChooseList) {
        this.groupWeekChooseList = groupWeekChooseList;
    }

    public int getItemChoose() {
        return itemChoose;
    }

    public void setItemChoose(int itemChoose) {
        this.itemChoose = itemChoose;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLease_term_type_id() {
        return lease_term_type_id;
    }

    public void setLease_term_type_id(Integer lease_term_type_id) {
        this.lease_term_type_id = lease_term_type_id;
    }

    public String getLease_term_type_name() {
        return lease_term_type_name;
    }

    public void setLease_term_type_name(String lease_term_type_name) {
        this.lease_term_type_name = lease_term_type_name;
    }

    public String getCustom_dimension() {
        return custom_dimension;
    }

    public void setCustom_dimension(String custom_dimension) {
        this.custom_dimension = custom_dimension;
    }

    public Integer getCount_of_frame() {
        return count_of_frame;
    }

    public void setCount_of_frame(Integer count_of_frame) {
        this.count_of_frame = count_of_frame;
    }

    public int getHiteView() {
        return hiteView;
    }

    public void setHiteView(int hiteView) {
        this.hiteView = hiteView;
    }

    public HashMap<Integer, Integer> getChild_week_id() {
        return child_week_id;
    }

    public void setChild_week_id(HashMap<Integer, Integer> child_week_id) {
        this.child_week_id = child_week_id;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }
}
