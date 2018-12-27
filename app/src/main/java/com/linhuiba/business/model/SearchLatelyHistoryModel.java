package com.linhuiba.business.model;

import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */

public class SearchLatelyHistoryModel {
    private int city_id;
    private int resources_type;
    private List<String> hotword_list;

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public List<String> getHotword_list() {
        return hotword_list;
    }

    public void setHotword_list(List<String> hotword_list) {
        this.hotword_list = hotword_list;
    }

    public int getResources_type() {
        return resources_type;
    }

    public void setResources_type(int resources_type) {
        this.resources_type = resources_type;
    }
}
