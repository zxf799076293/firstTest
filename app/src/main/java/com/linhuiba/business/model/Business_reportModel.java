package com.linhuiba.business.model;

/**
 * Created by Administrator on 2016/12/13.
 */

public class Business_reportModel {
    private int count_of_today;
    private int finished_days;
    private double total_consumption;

    public int getCount_of_today() {
        return count_of_today;
    }

    public void setCount_of_today(int count_of_today) {
        this.count_of_today = count_of_today;
    }

    public double getTotal_consumption() {
        return total_consumption;
    }

    public void setTotal_consumption(double total_consumption) {
        this.total_consumption = total_consumption;
    }

    public int getFinished_days() {
        return finished_days;
    }

    public void setFinished_days(int finished_days) {
        this.finished_days = finished_days;
    }
}
