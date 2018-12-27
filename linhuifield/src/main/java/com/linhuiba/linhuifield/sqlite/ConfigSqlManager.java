package com.linhuiba.linhuifield.sqlite;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.linhuipublic.config.LoginManager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/24.
 */

public class ConfigSqlManager {
    public static void addCityParameter(Context context, String citylist) {
        String citylistlist = citylist;
        if (citylistlist != null && citylistlist.length() > 0) {
            ArrayList<ConfigCitiesModel> ConfigCitiesModels = (ArrayList<ConfigCitiesModel>) JSON.parseArray(citylistlist,ConfigCitiesModel.class);
            if (ConfigCitiesModels != null && ConfigCitiesModels.size() > 0) {
                //单例
                ConfigurationsModel.getInstance().setCitylist(ConfigCitiesModels);
                ArrayList<ConfigCityParameterModel> cities = new ArrayList<>();
                ArrayList<ConfigCityParameterModel> districts = new ArrayList<>();
                ArrayList<ConfigCityParameterModel> labels = new ArrayList<>();
                ArrayList<ConfigCityParameterModel> trading_areas = new ArrayList<>();
                ArrayList<ConfigCityParameterModel> subway_stations = new ArrayList<>();
                ArrayList<ConfigCityParameterModel> stations = new ArrayList<>();
                for (int i = 0; i < ConfigCitiesModels.size(); i++) {
                    if (ConfigCitiesModels.get(i) == null ||
                            ConfigCitiesModels.get(i).getCity_id() == 0) {
                        return;
                    }
                    ConfigCityParameterModel configCityParameterModel = new ConfigCityParameterModel();
                    configCityParameterModel.setCode(ConfigCitiesModels.get(i).getCode());
                    configCityParameterModel.setCity(ConfigCitiesModels.get(i).getCity());
                    configCityParameterModel.setCity_id(ConfigCitiesModels.get(i).getCity_id());
                    configCityParameterModel.setProvince(ConfigCitiesModels.get(i).getProvince());
                    configCityParameterModel.setProvince_id(ConfigCitiesModels.get(i).getProvince_id());
                    configCityParameterModel.setLatitude(ConfigCitiesModels.get(i).getLatitude());
                    configCityParameterModel.setLongitude(ConfigCitiesModels.get(i).getLongitude());
                    configCityParameterModel.setService_phone(ConfigCitiesModels.get(i).getService_phone());
                    configCityParameterModel.setDefault_city(ConfigCitiesModels.get(i).getDefault_city());
                    cities.add(configCityParameterModel);
                    if (ConfigCitiesModels.get(i).getDistricts() != null &&
                            ConfigCitiesModels.get(i).getDistricts().size() > 0) {
                        ArrayList<ConfigCityParameterModel> configCityParameterModels = ConfigCitiesModels.get(i).getDistricts();
                        for (int k = 0; k < configCityParameterModels.size(); k++) {
                            configCityParameterModels.get(k).setCity_id(ConfigCitiesModels.get(i).getCity_id());
                        }
                        districts.addAll(configCityParameterModels);
                    }
                    if (ConfigCitiesModels.get(i).getLabels() != null &&
                            ConfigCitiesModels.get(i).getLabels().size() > 0) {
                        ArrayList<ConfigCityParameterModel> configCityParameterModels = ConfigCitiesModels.get(i).getLabels();
                        for (int k = 0; k < configCityParameterModels.size(); k++) {
                            configCityParameterModels.get(k).setCity_id(ConfigCitiesModels.get(i).getCity_id());
                        }
                        labels.addAll(configCityParameterModels);
                    }
                    if (ConfigCitiesModels.get(i).getTrading_areas() != null &&
                            ConfigCitiesModels.get(i).getTrading_areas().size() > 0) {
                        ArrayList<ConfigCityParameterModel> configCityParameterModels = ConfigCitiesModels.get(i).getTrading_areas();
                        for (int k = 0; k < configCityParameterModels.size(); k++) {
                            configCityParameterModels.get(k).setCity_id(ConfigCitiesModels.get(i).getCity_id());
                        }
                        trading_areas.addAll(configCityParameterModels);
                    }
                    if (ConfigCitiesModels.get(i).getSubway_stations() != null &&
                            ConfigCitiesModels.get(i).getSubway_stations().size() > 0) {
                        ArrayList<ConfigCityParameterModel> configCityParameterModels = ConfigCitiesModels.get(i).getSubway_stations();
                        for (int k = 0; k < configCityParameterModels.size(); k++) {
                            configCityParameterModels.get(k).setCity_id(ConfigCitiesModels.get(i).getCity_id());
                        }
                        subway_stations.addAll(configCityParameterModels);
                        for (int j = 0; j < ConfigCitiesModels.get(i).getSubway_stations().size(); j++) {
                            stations.addAll(ConfigCitiesModels.get(i).getSubway_stations().get(j).getStations());
                        }
                    }
                }
                if (districts != null && districts.size() > 0) {
                    ConfigSqlOperation.addCityParameter(districts,1,0,true,context);
                }
                if (labels != null && labels.size() > 0) {
                    ConfigSqlOperation.addCityParameter(labels,2,0,true,context);
                }
                if (trading_areas != null && trading_areas.size() > 0) {
                    ConfigSqlOperation.addCityParameter(trading_areas,3,0,true,context);
                }
                if (subway_stations != null && subway_stations.size() > 0) {
                    ConfigSqlOperation.addCityParameter(subway_stations,4,0,true,context);
                }
                if (stations != null && stations.size() > 0) {
                    ConfigSqlOperation.addCityParameter(stations,5,0,true,context);
                }
                if (cities != null && cities.size() > 0) {
                    ConfigSqlOperation.addCityParameter(cities,6,0,true,context);
                }
                LoginManager.getInstance().setIs_insert_config(true);
            }
        }
    }
}
