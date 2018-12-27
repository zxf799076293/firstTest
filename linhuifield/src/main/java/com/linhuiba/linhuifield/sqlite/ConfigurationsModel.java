package com.linhuiba.linhuifield.sqlite;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/24.
 */

public class ConfigurationsModel {
    static ConfigurationsModel sInstance;
    public static ConfigurationsModel getInstance() {
        if (sInstance == null) {
            sInstance = new ConfigurationsModel();
        }
        return sInstance;
    }

    private ArrayList<ConfigCitiesModel> citylist = new ArrayList<>();

    public ArrayList<ConfigCitiesModel> getCitylist() {
        return citylist;
    }

    public void setCitylist(ArrayList<ConfigCitiesModel> citylist) {
        this.citylist = citylist;
    }
    public ArrayList<ConfigCityParameterModel> getDistrictsList(int city_id) {
        ArrayList<ConfigCityParameterModel> districtsList = new ArrayList<>();
        for (int i = 0; i < citylist.size(); i++) {
            if (citylist.get(i).getCity_id() == city_id) {
                districtsList.addAll(citylist.get(i).getDistricts());
                break;
            }
        }
        return districtsList;
    }

}
