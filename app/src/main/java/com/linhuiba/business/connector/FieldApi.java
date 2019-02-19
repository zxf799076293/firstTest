package com.linhuiba.business.connector;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linhuiba.business.activity.BaiduMapActivity;
import com.linhuiba.business.activity.SearchFieldAreaActivity;
import com.linhuiba.business.config.Config;
import com.linhuiba.business.fieldmodel.InvoiceInfomationModel;
import com.linhuiba.business.model.ApiAdvResourcesModel;
import com.linhuiba.business.model.ApiResourcesModel;
import com.linhuiba.business.mvpmodel.LoginMvpModel;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Request;
import com.linhuiba.linhuipublic.config.LoginManager;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/3/8.
 */
public class FieldApi {
    //添加到购物车 3.0
    public static void addshopcart_items(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                            JSON resources) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("resources", resources.toJSONString());
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "cart_items",
                paramsMap,2));
        call.enqueue(handler);
    }
    //删除购物车item
    public static void deleteshopcart_items(Context context,OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                 JSON cart_item_ids) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("cart_item_ids", cart_item_ids.toJSONString());
        Call call = client.newCall(Request.RequestDelete(Config.BASE_API_URL_PHP,
                "cart_items",
               paramsMap,1));
        call.enqueue(handler);
    }
    //获取购物车列表 3.0
    public static void getshopcart_itemslist(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "cart_items",
                paramsMap,3));
        call.enqueue(handler);
    }
    //获取购物车数量 3.0
    public static void getshopcart_itemscount(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "cart_items/count",
                paramsMap,2));
        call.enqueue(handler);
    }
    //每日特卖列表
    public static void getdailySale_resources(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                              String city_id,String page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("city_id",city_id);
        paramsMap.put("page",page);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "dailySale_resources",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取展位列表 3.0
    public static void getAdvReslist(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                        ApiAdvResourcesModel apiResourcesModel) {
        JSONArray jsonArray = new JSONArray();
        HashMap<String, String> paramsMap = new HashMap<>();
        if (apiResourcesModel.getHot() == 1) {
            paramsMap.put("hot", String.valueOf(apiResourcesModel.getHot()));
        }
        if (apiResourcesModel.getSubsidy() == 1) {
            paramsMap.put("subsidy", String.valueOf(apiResourcesModel.getSubsidy()));
        }
        if (apiResourcesModel.getSelf_support() != null && apiResourcesModel.getSelf_support() == 1) {
            paramsMap.put("self_support", String.valueOf(apiResourcesModel.getSelf_support()));
        }
        if (apiResourcesModel.getKeywords() != null) {
            if (apiResourcesModel.getKeywords().length() > 0) {
                paramsMap.put("keywords", apiResourcesModel.getKeywords());
            }
        }
        paramsMap.put("city_id", apiResourcesModel.getCity_id());
        paramsMap.put("resource_type", apiResourcesModel.getResource_type());

        if (apiResourcesModel.getField_type_id() != null) {
            if (apiResourcesModel.getField_type_id().size() > 0) {
                paramsMap.put("field_type_id", JSON.toJSONString(apiResourcesModel.getField_type_id(), true));
            } else {
                paramsMap.put("field_type_id", jsonArray.toJSONString());
            }
        } else {
            paramsMap.put("field_type_id", jsonArray.toJSONString());
        }
        if (apiResourcesModel.getActivity_type_id() != null) {
            if (apiResourcesModel.getActivity_type_id().size() > 0) {
                paramsMap.put("activity_type_id", JSON.toJSONString(apiResourcesModel.getActivity_type_id(), true));
            } else {
                paramsMap.put("activity_type_id", jsonArray.toJSONString());
            }
        } else {
            paramsMap.put("activity_type_id", jsonArray.toJSONString());
        }
        if (apiResourcesModel.getAd_type_id() != null) {
            if (apiResourcesModel.getAd_type_id().size() > 0) {
                paramsMap.put("ad_type_id", JSON.toJSONString(apiResourcesModel.getAd_type_id(), true));
            } else {
                paramsMap.put("ad_type_id", jsonArray.toJSONString());
            }
        } else {
            paramsMap.put("ad_type_id", jsonArray.toJSONString());
        }

        if (apiResourcesModel.getDistrict_id() != null) {
            if (apiResourcesModel.getDistrict_id().size() > 0) {
                paramsMap.put("district_id", JSON.toJSONString(apiResourcesModel.getDistrict_id(), true));
            } else {
                paramsMap.put("district_id", jsonArray.toJSONString());
            }
        } else {
            paramsMap.put("district_id", jsonArray.toJSONString());
        }
        if (apiResourcesModel.getIndoor() != null && apiResourcesModel.getIndoor().size() > 0) {
            paramsMap.put("indoor", JSON.toJSONString(apiResourcesModel.getIndoor(), true));
        } else {
            paramsMap.put("indoor", jsonArray.toJSONString());
        }
        if (apiResourcesModel.getCommunity_type_id() != null) {
            if (apiResourcesModel.getCommunity_type_id().size() > 0) {
                paramsMap.put("community_type_id", JSON.toJSONString(apiResourcesModel.getCommunity_type_id(), true));
            } else {
                paramsMap.put("community_type_id", jsonArray.toJSONString());
            }
        } else {
            paramsMap.put("community_type_id", jsonArray.toJSONString());
        }

        if (apiResourcesModel.getTrading_area_id() != null) {
            if (apiResourcesModel.getTrading_area_id().size() > 0) {
                paramsMap.put("trading_area_id", JSON.toJSONString(apiResourcesModel.getTrading_area_id(), true));
            } else {
                paramsMap.put("trading_area_id", jsonArray.toJSONString());
            }
        } else {
            paramsMap.put("trading_area_id", jsonArray.toJSONString());
        }

        if (apiResourcesModel.getLabel_id() != null) {
            if (apiResourcesModel.getLabel_id().size() > 0) {
                paramsMap.put("label_id", JSON.toJSONString(apiResourcesModel.getLabel_id(), true));
            } else {
                paramsMap.put("label_id", jsonArray.toJSONString());
            }
        } else {
            paramsMap.put("label_id", jsonArray.toJSONString());
        }
        if (apiResourcesModel.getSubway_station_id() != null) {
            if (apiResourcesModel.getSubway_station_id().size() > 0) {
                paramsMap.put("subway_station_id", JSON.toJSONString(apiResourcesModel.getSubway_station_id(), true));
            } else {
                paramsMap.put("subway_station_id", jsonArray.toJSONString());
            }
        } else {
            paramsMap.put("subway_station_id", jsonArray.toJSONString());
        }
        if (apiResourcesModel.getFacilities() != null) {
            if (apiResourcesModel.getFacilities().size() > 0) {
                paramsMap.put("facilities", JSON.toJSONString(apiResourcesModel.getFacilities(), true));
            } else {
                paramsMap.put("facilities", jsonArray.toJSONString());
            }
        } else {
            paramsMap.put("facilities", jsonArray.toJSONString());
        }

        if (apiResourcesModel.getLowPrice() != null) {
            if (apiResourcesModel.getLowPrice().length() > 0) {
                paramsMap.put("lowPrice", apiResourcesModel.getLowPrice());
            }
        }
        if (apiResourcesModel.getHighPrice() != null) {
            if (apiResourcesModel.getHighPrice().length() > 0) {
                paramsMap.put("highPrice", apiResourcesModel.getHighPrice());
            }
        }
        if (apiResourcesModel.getMinimum_peoples() != null) {
            if (apiResourcesModel.getMinimum_peoples().length() > 0) {
                paramsMap.put("minimum_peoples", apiResourcesModel.getMinimum_peoples());
            }
        }
        if (apiResourcesModel.getMaximum_peoples() != null) {
            if (apiResourcesModel.getMaximum_peoples().length() > 0) {
                paramsMap.put("maximum_peoples", apiResourcesModel.getMaximum_peoples());
            }
        }
        if (apiResourcesModel.getMinimum_build_year() != null) {
            if (apiResourcesModel.getMinimum_build_year().length() > 0) {
                paramsMap.put("minimum_build_year", apiResourcesModel.getMinimum_build_year());
            }
        }
        if (apiResourcesModel.getMaximum_build_year() != null) {
            if (apiResourcesModel.getMaximum_build_year().length() > 0) {
                paramsMap.put("maximum_build_year", apiResourcesModel.getMaximum_build_year());
            }
        }
        if (apiResourcesModel.getMinimum_households() != null) {
            if (apiResourcesModel.getMinimum_households().length() > 0) {
                paramsMap.put("minimum_households", apiResourcesModel.getMinimum_households());
            }
        }
        if (apiResourcesModel.getMaximum_households() != null) {
            if (apiResourcesModel.getMaximum_households().length() > 0) {
                paramsMap.put("maximum_households", apiResourcesModel.getMaximum_households());
            }
        }
        if (apiResourcesModel.getMinimum_property_costs() != null) {
            if (apiResourcesModel.getMinimum_property_costs().length() > 0) {
                paramsMap.put("minimum_property_costs", apiResourcesModel.getMinimum_property_costs());
            }
        }
        if (apiResourcesModel.getMaximum_property_costs() != null) {
            if (apiResourcesModel.getMaximum_property_costs().length() > 0) {
                paramsMap.put("maximum_property_costs", apiResourcesModel.getMaximum_property_costs());
            }
        }
        if (apiResourcesModel.getMinimum_house_price() != null) {
            if (apiResourcesModel.getMinimum_house_price().length() > 0) {
                paramsMap.put("minimum_house_price", apiResourcesModel.getMinimum_house_price());
            }
        }
        if (apiResourcesModel.getMaximum_house_price() != null) {
            if (apiResourcesModel.getMaximum_house_price().length() > 0) {
                paramsMap.put("maximum_house_price", apiResourcesModel.getMaximum_house_price());
            }
        }

        if (apiResourcesModel.getMinimum_area() != null) {
            if (apiResourcesModel.getMinimum_area().length() > 0) {
                paramsMap.put("minimum_area", apiResourcesModel.getMinimum_area());
            }
        }
        if (apiResourcesModel.getMaximum_area() != null) {
            if (apiResourcesModel.getMaximum_area().length() > 0) {
                paramsMap.put("maximum_area", apiResourcesModel.getMaximum_area());
            }
        }

        paramsMap.put("order", apiResourcesModel.getOrder());
        paramsMap.put("order_by", apiResourcesModel.getOrder_by());
        paramsMap.put("page", apiResourcesModel.getPage());
        if (apiResourcesModel.getPageSize() != null &&
                apiResourcesModel.getPageSize().length() > 0) {
            paramsMap.put("pageSize", apiResourcesModel.getPageSize());
        }
        if (apiResourcesModel.getIs_home_page() == 1) {
            paramsMap.put("is_home_page", String.valueOf(apiResourcesModel.getIs_home_page()));
        }
        if (apiResourcesModel.getLatitude() > 0) {
            paramsMap.put("latitude", String.valueOf(apiResourcesModel.getLatitude()));
        }
        if (apiResourcesModel.getLongitude() > 0) {
            paramsMap.put("longitude", String.valueOf(apiResourcesModel.getLongitude()));
        }
        if (apiResourcesModel.getLatitude_delta() > 0) {
            paramsMap.put("latitude_delta", String.valueOf(apiResourcesModel.getLatitude_delta()));
        }
        if (apiResourcesModel.getLongitude_delta() > 0) {
            paramsMap.put("longitude_delta", String.valueOf(apiResourcesModel.getLongitude_delta()));
        }
        if (apiResourcesModel.getNearby() > 0) {
            paramsMap.put("nearby", String.valueOf(apiResourcesModel.getNearby()));
        }
        if (apiResourcesModel.getLat() > 0) {
            paramsMap.put("lat", String.valueOf(apiResourcesModel.getLat()));
        }
        if (apiResourcesModel.getLng() > 0) {
            paramsMap.put("lng", String.valueOf(apiResourcesModel.getLng()));
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "resources",
                paramsMap,3));
        call.enqueue(handler);
    }

    /**
     * 展位列表 3.9
     * @param client
     * @param handler
     * @param apiResourcesModel 请求的参数model
     */
    public static void getPhyReslist(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                        ApiResourcesModel apiResourcesModel) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (apiResourcesModel.getCommunity_ids() != null &&
                apiResourcesModel.getCommunity_ids().size() > 0) {//比场地列表增加的参数
            for (int i = 0; i < apiResourcesModel.getCommunity_ids().size(); i++) {
                paramsMap.put("community_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getCommunity_ids().get(i)));
            }
        }
        if (apiResourcesModel.getKeywords() != null && apiResourcesModel.getKeywords().length() > 0) {
            paramsMap.put("keywords", apiResourcesModel.getKeywords());
        }
        if (apiResourcesModel.getHas_physical() == 1) {//没用到
            paramsMap.put("has_physical", String.valueOf(apiResourcesModel.getHas_physical()));
        }
        if (apiResourcesModel.getCity_ids() != null &&
                apiResourcesModel.getCity_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getCity_ids().size(); i++) {
                paramsMap.put("city_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getCity_ids().get(i)));
            }
        }
        if (apiResourcesModel.getDistrict_ids() != null &&
                apiResourcesModel.getDistrict_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getDistrict_ids().size(); i++) {
                paramsMap.put("district_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getDistrict_ids().get(i)));
            }
        }
        if (apiResourcesModel.getTrading_area_ids() != null &&
                apiResourcesModel.getTrading_area_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getTrading_area_ids().size(); i++) {
                paramsMap.put("trading_area_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getTrading_area_ids().get(i)));
            }
        }
        if (apiResourcesModel.getSubway_station_ids() != null &&
                apiResourcesModel.getSubway_station_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getSubway_station_ids().size(); i++) {
                paramsMap.put("subway_station_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getSubway_station_ids().get(i)));
            }
        }
        if (apiResourcesModel.getIn_trading_area() != null && apiResourcesModel.getIn_trading_area().length() > 0) {
            paramsMap.put("in_trading_area", apiResourcesModel.getIn_trading_area());
        }
        if (apiResourcesModel.getCommunity_type_ids() != null &&
                apiResourcesModel.getCommunity_type_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getCommunity_type_ids().size(); i++) {
                paramsMap.put("community_type_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getCommunity_type_ids().get(i)));
            }
        }
        //属性
        if (apiResourcesModel.getAttributes() != null &&
                apiResourcesModel.getAttributes().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getAttributes().size(); i++) {
                String attributes = "";
                for (int j = 0; j < apiResourcesModel.getAttributes().get(i).getOption_ids().size(); j++) {
                    if (attributes.length() == 0) {
                        attributes = "[" + String.valueOf(apiResourcesModel.getAttributes().get(i).getOption_ids().get(j));
                        if (j == apiResourcesModel.getAttributes().get(i).getOption_ids().size() - 1) {
                            attributes = attributes + "]";
                        }
                    } else {
                        attributes = attributes + "," + String.valueOf(apiResourcesModel.getAttributes().get(i).getOption_ids().get(j));
                        if (j == apiResourcesModel.getAttributes().get(i).getOption_ids().size() - 1) {
                            attributes = attributes + "]";
                        }
                    }
                }
                attributes = "{\"id\":" + String.valueOf(apiResourcesModel.getAttributes().get(i).getId()) +"," +
                        "\"option_ids\":" + attributes + "}";
                paramsMap.put("attributes[" + String.valueOf(i) + "]", attributes);
            }
        }
        if (apiResourcesModel.getLabel_ids() != null &&
                apiResourcesModel.getLabel_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getLabel_ids().size(); i++) {
                paramsMap.put("label_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getLabel_ids().get(i)));
            }
        }
        if (apiResourcesModel.getField_cooperation_type_ids() != null &&
                apiResourcesModel.getField_cooperation_type_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getField_cooperation_type_ids().size(); i++) {
                paramsMap.put("field_cooperation_type_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getField_cooperation_type_ids().get(i)));
            }
        }
        if (apiResourcesModel.getActivity_type_ids() != null &&
                apiResourcesModel.getActivity_type_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getActivity_type_ids().size(); i++) {
                paramsMap.put("activity_type_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getActivity_type_ids().get(i)));
            }
        }
        if (apiResourcesModel.getAge_level_ids() != null &&
                apiResourcesModel.getAge_level_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getAge_level_ids().size(); i++) {
                paramsMap.put("age_level_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getAge_level_ids().get(i)));
            }
        }
        if (apiResourcesModel.getIndoor() != null &&
                apiResourcesModel.getIndoor().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getIndoor().size(); i++) {
                paramsMap.put("indoor["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getIndoor().get(i)));
            }
        }
        if (apiResourcesModel.getFacilities() != null &&
                apiResourcesModel.getFacilities().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getFacilities().size(); i++) {
                paramsMap.put("facilities["+String.valueOf(i)+"]",apiResourcesModel.getFacilities().get(i));
            }
        }
        if (apiResourcesModel.getLocation_type_ids() != null &&
                apiResourcesModel.getLocation_type_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getLocation_type_ids().size(); i++) {
                paramsMap.put("location_type_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getLocation_type_ids().get(i)));
            }
        }
        if (apiResourcesModel.getNot_need_deposit() != null) {
            paramsMap.put("not_need_deposit", String.valueOf(apiResourcesModel.getNot_need_deposit()));
        }

        if (apiResourcesModel.getMin_price() != null && apiResourcesModel.getMin_price().length() > 0) {
            paramsMap.put("min_price", apiResourcesModel.getMin_price());
        }
        if (apiResourcesModel.getMax_price() != null && apiResourcesModel.getMax_price().length() > 0) {
            paramsMap.put("max_price", apiResourcesModel.getMax_price());
        }
        if (apiResourcesModel.getMin_area() != null) {
            paramsMap.put("min_area", String.valueOf(apiResourcesModel.getMin_area()));
        }
        if (apiResourcesModel.getMax_area() != null) {
            paramsMap.put("max_area", String.valueOf(apiResourcesModel.getMax_area()));
        }
        if (apiResourcesModel.getMin_person_flow() != null) {
            paramsMap.put("min_person_flow", apiResourcesModel.getMin_person_flow());
        }
        if (apiResourcesModel.getMax_person_flow() != null) {
            paramsMap.put("max_person_flow", apiResourcesModel.getMax_person_flow());
        }
        if (apiResourcesModel.getLat() > 0) {
            paramsMap.put("lat", String.valueOf(apiResourcesModel.getLat()));
        }
        if (apiResourcesModel.getLng() > 0) {
            paramsMap.put("lng", String.valueOf(apiResourcesModel.getLng()));
        }
        if (apiResourcesModel.getLatitude() > 0) {
            paramsMap.put("latitude", String.valueOf(apiResourcesModel.getLatitude()));
        }
        if (apiResourcesModel.getLongitude() > 0) {
            paramsMap.put("longitude", String.valueOf(apiResourcesModel.getLongitude()));
        }
        if (apiResourcesModel.getLatitude_delta() > 0) {
            paramsMap.put("latitude_delta", String.valueOf(apiResourcesModel.getLatitude_delta()));
        }
        if (apiResourcesModel.getLongitude_delta() > 0) {
            paramsMap.put("longitude_delta", String.valueOf(apiResourcesModel.getLongitude_delta()));
        }
        if (apiResourcesModel.getNearby() > 0) {
            paramsMap.put("nearby", String.valueOf(apiResourcesModel.getNearby()));
        }
        if (apiResourcesModel.getMin_year() != null) {
            paramsMap.put("min_year", String.valueOf(apiResourcesModel.getMin_year()));
        }
        if (apiResourcesModel.getMax_year() != null) {
            paramsMap.put("max_year", String.valueOf(apiResourcesModel.getMax_year()));
        }
        paramsMap.put("order", apiResourcesModel.getOrder());
        paramsMap.put("order_by", apiResourcesModel.getOrder_by());
        if (apiResourcesModel.getNavigation() != null) {
            paramsMap.put("navigation", String.valueOf(apiResourcesModel.getNavigation()));
        }
        if (apiResourcesModel.getPage_size() > 0) {
            paramsMap.put("page_size", String.valueOf(apiResourcesModel.getPage_size()));
        } else {
            paramsMap.put("page_size", "10");
        }
        paramsMap.put("page", apiResourcesModel.getPage());
        if (apiResourcesModel.getDynamic_name() != null &&
                apiResourcesModel.getDynamic_name().length() > 0 &&
                apiResourcesModel.getDynamic_id() != null &&
                apiResourcesModel.getDynamic_id().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getDynamic_id().size(); i++) {
                paramsMap.put(apiResourcesModel.getDynamic_name() +
                        "["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getDynamic_id().get(i)));
            }
        }
        //浏览记录
        if (LoginManager.isLogin() && apiResourcesModel.getCity_ids() != null && apiResourcesModel.getCity_ids().size() > 0 &&
                apiResourcesModel.getCity_ids().get(0) != null) {
            try {
                String parameter = "?"+ Request.urlEncode(paramsMap);
                LoginMvpModel.sendBrowseHistories("field_list",parameter,String.valueOf(apiResourcesModel.getCity_ids().get(0)));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "lists",
                paramsMap,1));
        call.enqueue(handler);
    }

    //获取社区列表 3.6.1
    public static void getresourceslist(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                        ApiResourcesModel apiResourcesModel) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (apiResourcesModel.getKeywords() != null && apiResourcesModel.getKeywords().length() > 0) {
            paramsMap.put("keywords", apiResourcesModel.getKeywords());
        }
        if (apiResourcesModel.getHas_physical() == 1) {
            paramsMap.put("has_physical", String.valueOf(apiResourcesModel.getHas_physical()));
        }
        if (apiResourcesModel.getCity_ids() != null &&
                apiResourcesModel.getCity_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getCity_ids().size(); i++) {
                paramsMap.put("city_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getCity_ids().get(i)));
            }
        }
        if (apiResourcesModel.getDistrict_ids() != null &&
                apiResourcesModel.getDistrict_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getDistrict_ids().size(); i++) {
                paramsMap.put("district_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getDistrict_ids().get(i)));
            }
        }
        if (apiResourcesModel.getTrading_area_ids() != null &&
                apiResourcesModel.getTrading_area_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getTrading_area_ids().size(); i++) {
                paramsMap.put("trading_area_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getTrading_area_ids().get(i)));
            }
        }
        if (apiResourcesModel.getSubway_station_ids() != null &&
                apiResourcesModel.getSubway_station_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getSubway_station_ids().size(); i++) {
                paramsMap.put("subway_station_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getSubway_station_ids().get(i)));
            }
        }
        if (apiResourcesModel.getIn_trading_area() != null && apiResourcesModel.getIn_trading_area().length() > 0) {
            paramsMap.put("in_trading_area", apiResourcesModel.getIn_trading_area());
        }
        if (apiResourcesModel.getCommunity_type_ids() != null &&
                apiResourcesModel.getCommunity_type_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getCommunity_type_ids().size(); i++) {
                paramsMap.put("community_type_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getCommunity_type_ids().get(i)));
            }
        }
        if (apiResourcesModel.getLabel_ids() != null &&
                apiResourcesModel.getLabel_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getLabel_ids().size(); i++) {
                paramsMap.put("label_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getLabel_ids().get(i)));
            }
        }
        if (apiResourcesModel.getField_cooperation_type_ids() != null &&
                apiResourcesModel.getField_cooperation_type_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getField_cooperation_type_ids().size(); i++) {
                paramsMap.put("field_cooperation_type_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getField_cooperation_type_ids().get(i)));
            }
        }
        if (apiResourcesModel.getMin_price() != null && apiResourcesModel.getMin_price().length() > 0) {
            paramsMap.put("min_price", apiResourcesModel.getMin_price());
        }
        if (apiResourcesModel.getMax_price() != null && apiResourcesModel.getMax_price().length() > 0) {
            paramsMap.put("max_price", apiResourcesModel.getMax_price());
        }
        if (apiResourcesModel.getMin_year() != null) {
            paramsMap.put("min_year", String.valueOf(apiResourcesModel.getMin_year()));
        }
        if (apiResourcesModel.getMax_year() != null) {
            paramsMap.put("max_year", String.valueOf(apiResourcesModel.getMax_year()));
        }
        if (apiResourcesModel.getDeveloper_ids() != null &&
                apiResourcesModel.getDeveloper_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getDeveloper_ids().size(); i++) {
                paramsMap.put("developer_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getDeveloper_ids().get(i)));
            }
        }
        if (apiResourcesModel.getMin_area() != null) {
            paramsMap.put("min_area", String.valueOf(apiResourcesModel.getMin_area()));
        }
        if (apiResourcesModel.getMax_area() != null) {
            paramsMap.put("max_area", String.valueOf(apiResourcesModel.getMax_area()));
        }
        if (apiResourcesModel.getMin_person_flow() != null) {
            paramsMap.put("min_person_flow", apiResourcesModel.getMin_person_flow());
        }
        if (apiResourcesModel.getMax_person_flow() != null) {
            paramsMap.put("max_person_flow", apiResourcesModel.getMax_person_flow());
        }
        //属性
        if (apiResourcesModel.getAttributes() != null &&
                apiResourcesModel.getAttributes().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getAttributes().size(); i++) {
                String attributes = "";
                for (int j = 0; j < apiResourcesModel.getAttributes().get(i).getOption_ids().size(); j++) {
                    if (attributes.length() == 0) {
                        attributes = "[" + String.valueOf(apiResourcesModel.getAttributes().get(i).getOption_ids().get(j));
                        if (j == apiResourcesModel.getAttributes().get(i).getOption_ids().size() - 1) {
                            attributes = attributes + "]";
                        }
                    } else {
                        attributes = attributes + "," + String.valueOf(apiResourcesModel.getAttributes().get(i).getOption_ids().get(j));
                        if (j == apiResourcesModel.getAttributes().get(i).getOption_ids().size() - 1) {
                            attributes = attributes + "]";
                        }
                    }
                }
                attributes = "{\"id\":" + String.valueOf(apiResourcesModel.getAttributes().get(i).getId()) +"," +
                        "\"option_ids\":" + attributes + "}";
                paramsMap.put("attributes[" + String.valueOf(i) + "]", attributes);
            }
        }
        if (apiResourcesModel.getLat() > 0) {
            paramsMap.put("lat", String.valueOf(apiResourcesModel.getLat()));
        }
        if (apiResourcesModel.getLng() > 0) {
            paramsMap.put("lng", String.valueOf(apiResourcesModel.getLng()));
        }
        if (apiResourcesModel.getLatitude() > 0) {
            paramsMap.put("latitude", String.valueOf(apiResourcesModel.getLatitude()));
        }
        if (apiResourcesModel.getLongitude() > 0) {
            paramsMap.put("longitude", String.valueOf(apiResourcesModel.getLongitude()));
        }
        if (apiResourcesModel.getLatitude_delta() > 0) {
            paramsMap.put("latitude_delta", String.valueOf(apiResourcesModel.getLatitude_delta()));
        }
        if (apiResourcesModel.getLongitude_delta() > 0) {
            paramsMap.put("longitude_delta", String.valueOf(apiResourcesModel.getLongitude_delta()));
        }
        if (apiResourcesModel.getNearby() > 0) {
            paramsMap.put("nearby", String.valueOf(apiResourcesModel.getNearby()));
        }
        if (apiResourcesModel.getNot_need_deposit() != null) {
            paramsMap.put("not_need_deposit", String.valueOf(apiResourcesModel.getNot_need_deposit()));
        }
        if (apiResourcesModel.getHas_subsidy() != null) {
            paramsMap.put("has_subsidy", String.valueOf(apiResourcesModel.getHas_subsidy()));
        }
        if (apiResourcesModel.getHas_activity() != null) {
            paramsMap.put("has_activity", String.valueOf(apiResourcesModel.getHas_activity()));
        }
        if (apiResourcesModel.getActivity_type_ids() != null &&
                apiResourcesModel.getActivity_type_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getActivity_type_ids().size(); i++) {
                paramsMap.put("activity_type_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getActivity_type_ids().get(i)));
            }
        }
        if (apiResourcesModel.getAge_level_ids() != null &&
                apiResourcesModel.getAge_level_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getAge_level_ids().size(); i++) {
                paramsMap.put("age_level_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getAge_level_ids().get(i)));
            }
        }
        if (apiResourcesModel.getIndoor() != null &&
                apiResourcesModel.getIndoor().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getIndoor().size(); i++) {
                paramsMap.put("indoor["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getIndoor().get(i)));
            }
        }
        if (apiResourcesModel.getFacilities() != null &&
                apiResourcesModel.getFacilities().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getFacilities().size(); i++) {
                paramsMap.put("facilities["+String.valueOf(i)+"]",apiResourcesModel.getFacilities().get(i));
            }
        }
        paramsMap.put("order", apiResourcesModel.getOrder());
        paramsMap.put("order_by", apiResourcesModel.getOrder_by());
        if (apiResourcesModel.getNavigation() != null) {
            paramsMap.put("navigation", String.valueOf(apiResourcesModel.getNavigation()));
        }
        if (apiResourcesModel.getPage_size() > 0) {
            paramsMap.put("page_size", String.valueOf(apiResourcesModel.getPage_size()));
        } else {
            paramsMap.put("page_size", "10");
        }
        paramsMap.put("page", apiResourcesModel.getPage());
        if (apiResourcesModel.getDynamic_name() != null &&
                apiResourcesModel.getDynamic_name().length() > 0 &&
                apiResourcesModel.getDynamic_id() != null &&
                apiResourcesModel.getDynamic_id().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getDynamic_id().size(); i++) {
                paramsMap.put(apiResourcesModel.getDynamic_name() +
                        "["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getDynamic_id().get(i)));
            }
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "communities",
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取专题列表
    public static void getthemeslitst(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                        String page,String pageSize,String type,String keyword,int is_home_page,String city_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page", page);
        paramsMap.put("pageSize", "10");
        if (type != null) {
            if (type.length() > 0) {
                paramsMap.put("type", type);
            }
        }
        if (keyword != null) {
            if (keyword.length() > 0) {
                paramsMap.put("keyword", keyword);
            }
        }
        if (is_home_page == 1) {
            paramsMap.put("is_home_page", String.valueOf(is_home_page));
        }
        paramsMap.put("city_id", city_id);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "themes",
                paramsMap,1));
        call.enqueue(handler);
    }
    //展位评论列表 3.10
    public static void get_resources_commentslist(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                              String fieldid,String page,String pageSize) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page", page);
        paramsMap.put("pageSize", pageSize);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "physical_resources/" + fieldid + "/comments",
                paramsMap,4));
        call.enqueue(handler);
    }
    //供给评论列表 3.10
    public static void getSellResCpmments(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                          String fieldid,String page,String pageSize) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page", page);
        paramsMap.put("pageSize", pageSize);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "selling_resources/" + fieldid + "/comments",
                paramsMap,3));
        call.enqueue(handler);
    }
    /**
     * 发表评论 3.10
     * @param client
     * @param handler
     * @param fieldid 子订单ID
     * @param score 评分星级
     * @param anonymity 是否匿名发表 0：否 1：是
     * @param score_of_visitorsflowrate 人流量评分星级 1：超过 2：差不多 3：不足
     * @param score_of_propertymatching 物业配合度评分星级 1：满意 2：一般 3：不满意
     * @param score_of_goalcompletion 目完成度评分星级 1：满意 2：一般 3：不满意
     * @param content 评论内容
     * @param tags 标签ID // [1,2,3]
     * @param images 图片URLjson数组(必须符合JSON格式,且最多4张)
     */
    public static void published_resources_comments(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                                  String fieldid,int score,int anonymity,int score_of_visitorsflowrate,
                                                    int score_of_propertymatching, int score_of_goalcompletion, String content,
                                                    ArrayList<Integer> tags,ArrayList<String> images) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("score", String.valueOf(score));
        paramsMap.put("anonymity", String.valueOf(anonymity));
        paramsMap.put("score_of_visitorsflowrate", String.valueOf(score_of_visitorsflowrate));//人流量
        paramsMap.put("score_of_propertymatching", String.valueOf(score_of_propertymatching));//物业配合度
        paramsMap.put("score_of_goalcompletion", String.valueOf(score_of_goalcompletion));//目标完成度评分星级
        if (content != null) {
            if (content.length() > 0) {
                paramsMap.put("content", content);
            }
        }
        if (tags != null && tags.size() > 0) {
            for (int i = 0; i < tags.size(); i++) {
                paramsMap.put("tags["+String.valueOf(i)+"]",String.valueOf(tags.get(i)));
            }
        }
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                paramsMap.put("images["+String.valueOf(i)+"]",images.get(i));
            }
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "order_items/" + fieldid + "/comments",
                paramsMap,3));
        call.enqueue(handler);
    }
    //获取订单列表 3.6.1
    public static void getordersitemlist(Call call,OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                                  String status,String page,String pageSize) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("status", status);
        paramsMap.put("page", page);
        paramsMap.put("pageSize", pageSize);
        call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "orders",
               paramsMap,3));
        call.enqueue(handler);
    }
    //获取待支付,待审核,进行中,待评价的个数
    public static void getordersitemlist(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "orders/count",
                paramsMap,1));
        call.enqueue(handler);
    }

    //移除订单
    public static void deleteordersitemlist(Context context, OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                            String id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestDelete(Config.BASE_API_URL_PHP,
                "orders/"+id,
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取已购资源列表（订单列表）3.6.1
    public static void getpurchased_resourceslist(Call call,OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                         String status,String page,String pageSize) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("status", status);
        paramsMap.put("page", page);
        paramsMap.put("pageSize", pageSize);
        call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "purchased_resources",
                paramsMap,3));
        call.enqueue(handler);
    }
    //修改联系人
    public static void editoraddresscontact(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                            String id,String contact,String phone,
                                            String province_id,String city_id,String district_id,
                                            String address,int state) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (contact != null) {
            if (contact.length() > 0) {
                paramsMap.put("name", contact);
            }
        }
        if (phone != null) {
            if (phone.length() > 0) {
                paramsMap.put("mobile", phone);
            }
        }
        if (province_id != null && city_id != null && district_id != null) {
            if (province_id.length() > 0 && city_id.length() > 0 && district_id.length() > 0) {
                paramsMap.put("province_id", province_id);
                paramsMap.put("city_id", city_id);
                paramsMap.put("district_id", district_id);
            }
        }
        if (address != null) {
            if (address.length() > 0) {
                paramsMap.put("address", address);
            }
        }
        if (state == 1 || state == 0) {
            paramsMap.put("is_default", String.valueOf(state));
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "address/"+id,
                paramsMap,1));
        call.enqueue(handler);
    }
    //删除联系人
    public static void deleteaddresscontact(Context context, OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                            String id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestDelete(Config.BASE_API_URL_PHP,
                "address/"+id,
                paramsMap,1));
        call.enqueue(handler);
    }
    //增加联系人
    public static void Addaddresscontact(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                            String contact,String phone,
                                            String province_id,String city_id,String district_id,
                                            String address,int state) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("name", contact);
        paramsMap.put("mobile", phone);
        if (province_id != null && city_id != null && district_id != null) {
            if (province_id.length() > 0 && city_id.length() > 0 && district_id.length() > 0) {
                paramsMap.put("province_id", province_id);
                paramsMap.put("city_id", city_id);
                paramsMap.put("district_id", district_id);
            }
        }

        if (address != null) {
            if (address.length() > 0) {
                paramsMap.put("address", address);
            }
        }
        if (state == 1 || state == 0) {
            paramsMap.put("is_default", String.valueOf(state));
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "address",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取联系人列表
    public static void getaddresscontactlist(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                         String page,String pageSize) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page", page);
        paramsMap.put("pageSize", "10");
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "address",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取已开票列表 获取已处理的开票信息
    public static void getalreadyinvoices(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                          String page,String pageSize) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page", page);
        paramsMap.put("pageSize", "10");
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "invoice",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取未开票列表 3.0
    public static void getnoinvoices(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                     String page,String pageSize,String tax_type) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page", page);
        paramsMap.put("pageSize", pageSize);
        paramsMap.put("tax_type", tax_type);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "invoices_list",
                paramsMap,3));
        call.enqueue(handler);
    }
    //显示票据信息预增加的详情 3.0
    public static void getcheckedinvoicelistitems(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                                  JSON field_order_item_ids) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("field_order_item_ids", field_order_item_ids.toJSONString());
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "invoice/create",
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取开票抬头信息
    public static void getInvoiceInfo(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "invoice-title-info",
                paramsMap,2));
        call.enqueue(handler);
    }

    //获取用户的足迹
    public static void gettracklistitems(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                     String city_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("city_id", city_id);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "track",
                paramsMap,2));
        call.enqueue(handler);
    }
    //增加用户的足迹备注
    public static void addtractremarks(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                         String field_order_item_id,String number_of_people,String sale,
                                       String number_of_fans,JSON images) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("field_order_item_id", field_order_item_id);
        if (number_of_people != null) {
            if (number_of_people.length() > 0) {
                paramsMap.put("number_of_people", number_of_people);
            }
        }
        if (sale != null) {
            if (sale.length() > 0) {
                paramsMap.put("sale", sale);
            }
        }
        if (number_of_fans != null) {
            if (number_of_fans.length() > 0) {
                paramsMap.put("number_of_fans", number_of_fans);
            }
        }
        if (images != null) {
            if (images.toJSONString().toString().length() > 0) {
                paramsMap.put("images", images.toJSONString());
            }
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "track",
                paramsMap,1));
        call.enqueue(handler);
    }
    //修改用户的足迹备注
    public static void editortractremarks(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                       String id,String field_order_item_id,String number_of_people,String sale,
                                       String number_of_fans,JSON images) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("field_order_item_id", field_order_item_id);
        if (number_of_people != null) {
            if (number_of_people.length() > 0) {
                paramsMap.put("number_of_people", number_of_people);
            }  else {
                paramsMap.put("number_of_people", "0");
            }
        }
        if (sale != null) {
            if (sale.length() > 0) {
                paramsMap.put("sale", sale);
            } else {
                paramsMap.put("sale", "0");
            }
        }
        if (number_of_fans != null) {
            if (number_of_fans.length() > 0) {
                paramsMap.put("number_of_fans", number_of_fans);
            } else {
                paramsMap.put("number_of_fans", "0");
            }
        }
        if (images != null) {
            if (JSONArray.parseArray(images.toString()).size() > 0) {
                paramsMap.put("images", images.toJSONString());
            }
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "track/"+id,
                paramsMap,1));
        call.enqueue(handler);
    }
    //删除用户的足迹图片
    public static void deletetractremarks(Context context,OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                            String id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestDelete(Config.BASE_API_URL_PHP,
                "track/img/"+id,
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取档期管理
    public static void gettrack_share_link(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                               String cityid) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "track/share_link",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取收藏列表 3.0
    public static void getfavoriteslistitem(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                            String page,String pageSize) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page", page);
        paramsMap.put("pageSize", "10");
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "favorites",
                paramsMap,2));
        call.enqueue(handler);
    }
    //添加收藏列表3.0
    public static void addfavoriteslistitem(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                            String target_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestPut(Config.BASE_API_URL_PHP,
                "resources/"+target_id+"/favorite",
                paramsMap,2));
        call.enqueue(handler);
    }
    //删除收藏
    public static void deletefavoriteslistitem(Context context,OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                          String id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestDelete(Config.BASE_API_URL_PHP,
                "resources/"+id+"/favorite",
                paramsMap,1));
        call.enqueue(handler);
    }
    //地图搜索资源接口 3.6.1
    public static void
    getMapResourcesList(BaiduMapActivity baiduMapActivity,OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                                ApiResourcesModel apiResourcesModel) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (apiResourcesModel.getZoom_level() > 0) {
            paramsMap.put("zoom_level",String.valueOf(apiResourcesModel.getZoom_level()));
        }
        if (apiResourcesModel.getCity_id() != null &&
                apiResourcesModel.getCity_id().length() > 0) {
            paramsMap.put("city_id",apiResourcesModel.getCity_id());
        }
        if (apiResourcesModel.getLatitude() > 0) {
            paramsMap.put("latitude", String.valueOf(apiResourcesModel.getLatitude()));
        }
        if (apiResourcesModel.getLongitude() > 0) {
            paramsMap.put("longitude", String.valueOf(apiResourcesModel.getLongitude()));
        }
        if (apiResourcesModel.getLatitude_delta() > 0) {
            paramsMap.put("latitude_delta", String.valueOf(apiResourcesModel.getLatitude_delta()));
        }
        if (apiResourcesModel.getLongitude_delta() > 0) {
            paramsMap.put("longitude_delta", String.valueOf(apiResourcesModel.getLongitude_delta()));
        }
        if (apiResourcesModel.getKeywords() != null && apiResourcesModel.getKeywords().length() > 0) {
            paramsMap.put("keywords", apiResourcesModel.getKeywords());
        }
        if (apiResourcesModel.getDistrict_ids() != null &&
                apiResourcesModel.getDistrict_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getDistrict_ids().size(); i++) {
                paramsMap.put("district_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getDistrict_ids().get(i)));
            }
        }
        if (apiResourcesModel.getTrading_area_ids() != null &&
                apiResourcesModel.getTrading_area_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getTrading_area_ids().size(); i++) {
                paramsMap.put("trading_area_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getTrading_area_ids().get(i)));
            }
        }
        if (apiResourcesModel.getSubway_station_ids() != null &&
                apiResourcesModel.getSubway_station_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getSubway_station_ids().size(); i++) {
                paramsMap.put("subway_station_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getSubway_station_ids().get(i)));
            }
        }
        if (apiResourcesModel.getCommunity_type_ids() != null &&
                apiResourcesModel.getCommunity_type_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getCommunity_type_ids().size(); i++) {
                paramsMap.put("community_type_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getCommunity_type_ids().get(i)));
            }
        }
        if (apiResourcesModel.getCity_ids() != null &&
                apiResourcesModel.getCity_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getCity_ids().size(); i++) {
                paramsMap.put("city_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getCity_ids().get(i)));
            }
        }
        //类目属性
        if (apiResourcesModel.getAttributes() != null &&
                apiResourcesModel.getAttributes().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getAttributes().size(); i++) {
                String attributes = "";
                for (int j = 0; j < apiResourcesModel.getAttributes().get(i).getOption_ids().size(); j++) {
                    if (attributes.length() == 0) {
                        attributes = "[" + String.valueOf(apiResourcesModel.getAttributes().get(i).getOption_ids().get(j));
                        if (j == apiResourcesModel.getAttributes().get(i).getOption_ids().size() - 1) {
                            attributes = attributes + "]";
                        }
                    } else {
                        attributes = attributes + "," + String.valueOf(apiResourcesModel.getAttributes().get(i).getOption_ids().get(j));
                        if (j == apiResourcesModel.getAttributes().get(i).getOption_ids().size() - 1) {
                            attributes = attributes + "]";
                        }
                    }
                }
                attributes = "{\"id\":" + String.valueOf(apiResourcesModel.getAttributes().get(i).getId()) +"," +
                        "\"option_ids\":" + attributes + "}";
                paramsMap.put("attributes[" + String.valueOf(i) + "]", attributes);
            }
        }
        if (apiResourcesModel.getLabel_ids() != null &&
                apiResourcesModel.getLabel_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getLabel_ids().size(); i++) {
                paramsMap.put("label_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getLabel_ids().get(i)));
            }
        }
        if (apiResourcesModel.getDeveloper_ids() != null &&
                apiResourcesModel.getDeveloper_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getDeveloper_ids().size(); i++) {
                paramsMap.put("developer_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getDeveloper_ids().get(i)));
            }
        }
        if (apiResourcesModel.getMin_price() != null && apiResourcesModel.getMin_price().length() > 0) {
            paramsMap.put("min_price", apiResourcesModel.getMin_price());
        }
        if (apiResourcesModel.getMax_price() != null && apiResourcesModel.getMax_price().length() > 0) {
            paramsMap.put("max_price", apiResourcesModel.getMax_price());
        }
        if (apiResourcesModel.getMin_year() != null) {
            paramsMap.put("min_year", String.valueOf(apiResourcesModel.getMin_year()));
        }
        if (apiResourcesModel.getMax_year() != null) {
            paramsMap.put("max_year", String.valueOf(apiResourcesModel.getMax_year()));
        }
        if (apiResourcesModel.getNearby() > 0) {
            paramsMap.put("nearby", String.valueOf(apiResourcesModel.getNearby()));
        }



        if (apiResourcesModel.getMin_area() != null) {
            paramsMap.put("min_area", String.valueOf(apiResourcesModel.getMin_area()));
        }
        if (apiResourcesModel.getMax_area() != null) {
            paramsMap.put("max_area", String.valueOf(apiResourcesModel.getMax_area()));
        }
        if (apiResourcesModel.getMin_person_flow() != null) {
            paramsMap.put("min_person_flow", apiResourcesModel.getMin_person_flow());
        }
        if (apiResourcesModel.getMax_person_flow() != null) {
            paramsMap.put("max_person_flow", apiResourcesModel.getMax_person_flow());
        }
        if (apiResourcesModel.getNot_need_deposit() != null) {
            paramsMap.put("not_need_deposit", String.valueOf(apiResourcesModel.getNot_need_deposit()));
        }
        if (apiResourcesModel.getHas_subsidy() != null) {
            paramsMap.put("has_subsidy", String.valueOf(apiResourcesModel.getHas_subsidy()));
        }
        if (apiResourcesModel.getHas_activity() != null) {
            paramsMap.put("has_activity", String.valueOf(apiResourcesModel.getHas_activity()));
        }
        if (apiResourcesModel.getActivity_type_ids() != null &&
                apiResourcesModel.getActivity_type_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getActivity_type_ids().size(); i++) {
                paramsMap.put("activity_type_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getActivity_type_ids().get(i)));
            }
        }
        if (apiResourcesModel.getAge_level_ids() != null &&
                apiResourcesModel.getAge_level_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getAge_level_ids().size(); i++) {
                paramsMap.put("age_level_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getAge_level_ids().get(i)));
            }
        }
        if (apiResourcesModel.getIndoor() != null &&
                apiResourcesModel.getIndoor().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getIndoor().size(); i++) {
                paramsMap.put("indoor["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getIndoor().get(i)));
            }
        }
        if (apiResourcesModel.getFacilities() != null &&
                apiResourcesModel.getFacilities().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getFacilities().size(); i++) {
                paramsMap.put("facilities["+String.valueOf(i)+"]",apiResourcesModel.getFacilities().get(i));
            }
        }
        paramsMap.put("order", apiResourcesModel.getOrder());
        paramsMap.put("order_by", apiResourcesModel.getOrder_by());
        if (apiResourcesModel.getNavigation() != null) {
            paramsMap.put("navigation", String.valueOf(apiResourcesModel.getNavigation()));
        }
        paramsMap.put("page_size", "10");
        paramsMap.put("page", apiResourcesModel.getPage());
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "communities/web_map",
                paramsMap,2));
        call.enqueue(handler);
        baiduMapActivity.mCallsList.add(call);
    }

    //获取档期管理 3.0
    public static void getschedule_order_items(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                            String status,String date_start,String date_end) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("status", status);
        paramsMap.put("date_start", date_start);
        paramsMap.put("date_end", date_end);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "schedule4business_order_items",
                paramsMap,2));
        call.enqueue(handler);
    }
    //上传线下支付凭证 2.1(订单)
    public static void upload_voucher(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                                       String orderid,String voucher_image_url) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("voucher_image_url", voucher_image_url);
        Call call = client.newCall(Request.RequestPut(Config.BASE_API_URL_PHP,
                "orders/"+orderid+"/upload_voucher",
                paramsMap,2));
        call.enqueue(handler);
    }
    //根据id获取订单详情 3.0
    public static void getorderinfo(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                      String orderid) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "orders/"+orderid,
                paramsMap,3));
        call.enqueue(handler);
    }
    //发布反馈(资源ID(不传ID代表针对整个系统发布反馈)) 3.0
    public static void release_feedbacks(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                    int resource_id,int community_id,String content) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (resource_id  > 0) {
            paramsMap.put("resource_id",String.valueOf(resource_id));
        }
        if (community_id  > 0) {
            paramsMap.put("community_id",String.valueOf(community_id));
        }
        paramsMap.put("feedback_type","1");
        paramsMap.put("content",content);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "feedbacks",
                paramsMap,2));
        call.enqueue(handler);
    }
    //立即下单（支付）创建订单 3.7
    public static void createorder_pay(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                       JSONArray resources,int total_actual_price,int delegated,int need_decoration,
                                       int need_transportation,int need_invoice,String contact,String mobile,
                                       String address,String cart_item_ids,
                                       InvoiceInfomationModel invoiceInfomationModeldata) {
        HashMap<String, String> paramsMap = new HashMap<>();
        for (int i = 0; i < resources.size(); i++) {
            if (resources.getJSONObject(i).get("id") != null &&
                    resources.getJSONObject(i).get("id").toString().length() > 0) {
                paramsMap.put("resources["+String.valueOf(i)+"]" +
                        "[id]", resources.getJSONObject(i).get("id").toString());
            }
            if (resources.getJSONObject(i).get("size") != null &&
                    resources.getJSONObject(i).get("size").toString().length() > 0) {
                paramsMap.put("resources["+String.valueOf(i)+"]" +
                        "[size]", resources.getJSONObject(i).get("size").toString());
            }
            if (resources.getJSONObject(i).get("lease_term_type_id") != null &&
                    resources.getJSONObject(i).get("lease_term_type_id").toString().length() > 0) {
                paramsMap.put("resources["+String.valueOf(i)+"]" +
                        "[lease_term_type_id]", resources.getJSONObject(i).get("lease_term_type_id").toString());
            }
            if (resources.getJSONObject(i).get("count") != null &&
                    resources.getJSONObject(i).get("count").toString().length() > 0) {
                paramsMap.put("resources["+String.valueOf(i)+"]" +
                        "[count]", resources.getJSONObject(i).get("count").toString());
            }
            if (resources.getJSONObject(i).get("count_of_time_unit") != null &&
                    resources.getJSONObject(i).get("count_of_time_unit").toString().length() > 0) {
                paramsMap.put("resources["+String.valueOf(i)+"]" +
                        "[count_of_time_unit]", resources.getJSONObject(i).get("count_of_time_unit").toString());
            }
            if (resources.getJSONObject(i).get("custom_dimension") != null &&
                    resources.getJSONObject(i).get("custom_dimension").toString().length() > 0) {
                paramsMap.put("resources["+String.valueOf(i)+"]" +
                        "[custom_dimension]", resources.getJSONObject(i).get("custom_dimension").toString());
            }
            if (resources.getJSONObject(i).get("date") != null &&
                    resources.getJSONObject(i).get("date").toString().length() > 0) {
                paramsMap.put("resources["+String.valueOf(i)+"]" +
                        "[date]", resources.getJSONObject(i).get("date").toString());
            }
            if (resources.getJSONObject(i).get("leave_words") != null &&
                    resources.getJSONObject(i).get("leave_words").toString().length() > 0) {
                paramsMap.put("resources["+String.valueOf(i)+"]" +
                        "[leave_words]", resources.getJSONObject(i).get("leave_words").toString());
            }
            if (resources.getJSONObject(i).get("enquiry_id") != null &&
                    resources.getJSONObject(i).get("enquiry_id").toString().length() > 0) {
                paramsMap.put("resources["+String.valueOf(i)+"]" +
                        "[enquiry_id]", resources.getJSONObject(i).get("enquiry_id").toString());
            }
            if (resources.getJSONObject(i).get("coupon_id") != null &&
                    resources.getJSONObject(i).get("coupon_id").toString().length() > 0) {
                paramsMap.put("resources["+String.valueOf(i)+"]" +
                        "[coupon_id]", resources.getJSONObject(i).get("coupon_id").toString());
            }
        }

        paramsMap.put("total_actual_price", String.valueOf(total_actual_price));
        paramsMap.put("delegated", String.valueOf(delegated));
        paramsMap.put("need_decoration", String.valueOf(need_decoration));
        paramsMap.put("need_transportation", String.valueOf(need_transportation));

        if (need_invoice == 1) {
            paramsMap.put("ticket_type", String.valueOf(invoiceInfomationModeldata.getTicket_type()));
            paramsMap.put("title", invoiceInfomationModeldata.getTitle());
            if (invoiceInfomationModeldata.getTicket_remarks() != null &&
                    invoiceInfomationModeldata.getTicket_remarks().length() > 0) {
                paramsMap.put("ticket_remarks", invoiceInfomationModeldata.getTicket_remarks());
            }

            if (invoiceInfomationModeldata.getMaterial() == 1) {
                paramsMap.put("material", "1");
                // 2018/3/5 开票增加信息
                paramsMap.put("consignee_addresses_ id", String.valueOf(invoiceInfomationModeldata.getAddress_id()));
                paramsMap.put("freight_collect", String.valueOf(invoiceInfomationModeldata.getFreight_collect()));
            } else {
                paramsMap.put("material", "0");
                paramsMap.put("email", invoiceInfomationModeldata.getEmail());
            }
            if (invoiceInfomationModeldata.getTicket_type() == 1) {

            } else if (invoiceInfomationModeldata.getTicket_type() == 2){
                paramsMap.put("invoice_content_id", String.valueOf(invoiceInfomationModeldata.getInvoice_content_id()));
                paramsMap.put("tax_number", invoiceInfomationModeldata.getTax_number());
            } else if (invoiceInfomationModeldata.getTicket_type() == 3){
                paramsMap.put("invoice_content_id", String.valueOf(invoiceInfomationModeldata.getInvoice_content_id()));
                paramsMap.put("tax_number", invoiceInfomationModeldata.getTax_number());
                paramsMap.put("company_region", invoiceInfomationModeldata.getCompany_region());
                paramsMap.put("company_address", invoiceInfomationModeldata.getCompany_address());
                paramsMap.put("company_mobile", invoiceInfomationModeldata.getCompany_mobile());
                paramsMap.put("bank", invoiceInfomationModeldata.getBank());
                paramsMap.put("account", invoiceInfomationModeldata.getAccount());
                paramsMap.put("general_taxpayer_qualification", invoiceInfomationModeldata.getGeneral_taxpayer_qualification());
            }
        } else {
            // 2018/3/6 重新设置
            paramsMap.put("ticket_type", "0");
        }

        paramsMap.put("contact", contact);
        paramsMap.put("mobile", mobile);
        paramsMap.put("address", address);
        if (cart_item_ids != null && cart_item_ids.length() > 0) {
            paramsMap.put("cart_item_ids", cart_item_ids);
        }
        paramsMap.put("channel", "1");
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "orders",
                paramsMap,9));
        call.enqueue(handler);
    }
    //订单支付（付款）(确认订单)3.0 (0:线下支付 1：微信app支付 2:微信公众号支付 3:微信原生扫码支付 4:钱包支付 6:5w微信支付)
    public static void payorder(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                String id, int payment_mode,int password) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("payment_mode",String.valueOf(payment_mode));
        if (password != -1 && String.valueOf(password).length() == 6) {
            paramsMap.put("password",String.valueOf(password));
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "orders/" + id,
                paramsMap,4));
        call.enqueue(handler);
    }
    //上传线下支付凭证 2.4.2(充值)
    public static void transactions_upload_voucher(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                      String id,String voucher_image_url) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("voucher_image_url", voucher_image_url);
        Call call = client.newCall(Request.RequestPut(Config.BASE_API_URL_PHP,
                "transactions/"+id+"/upload_voucher",
                paramsMap,2));
        call.enqueue(handler);
    }

    //提交开票申请 3.6
    public static void addInvoice(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                  ArrayList<HashMap<Object,Object>> content, int invoice_title_id,
                                  int is_paper, int invoice_content_id, int consignee_addresses_id,
                                  String email, String remark) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (content != null && content.size() > 0) {
            for (int i = 0; i < content.size(); i++) {
                if (content.get(i).get("order_item_id") != null &&
                        content.get(i).get("order_item_id").toString().length() > 0) {
                    paramsMap.put("content["+String.valueOf(i)+"]" +
                            "[order_item_id]", content.get(i).get("order_item_id").toString());
                }
            }
        }
        paramsMap.put("invoice_title_id", String.valueOf(invoice_title_id));
        paramsMap.put("invoice_content_id", String.valueOf(invoice_content_id));
        paramsMap.put("is_paper", String.valueOf(is_paper));
        if (is_paper == 1) {
            paramsMap.put("consignee_addresses_id", String.valueOf(consignee_addresses_id));
        } else if (is_paper == 0) {
            paramsMap.put("email", email);
        }
        if (remark != null && remark.length() > 0) {
            paramsMap.put("remark", remark);
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "invoice",
                paramsMap,4));
        call.enqueue(handler);
    }
    //创建发票订单接口 3.4
    public static void invoice_orders(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                              JSON field_order_item_ids, int is_paper, int ticket_type,
                                              InvoiceInfomationModel invoiceInfomationModeldata) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("title", invoiceInfomationModeldata.getTitle());
        paramsMap.put("order_item_ids", field_order_item_ids.toJSONString());
        paramsMap.put("ticket_type", String.valueOf(ticket_type));
        if (invoiceInfomationModeldata.getTicket_remarks() != null &&
                invoiceInfomationModeldata.getTicket_remarks().length() > 0) {
            paramsMap.put("ticket_remarks", invoiceInfomationModeldata.getTicket_remarks());
        }
        if (is_paper == 1) {
            paramsMap.put("material", "1");
            // 2018/3/5 开票增加信息
            paramsMap.put("consignee_addresses_id", String.valueOf(invoiceInfomationModeldata.getAddress_id()));
            paramsMap.put("freight_collect", String.valueOf(invoiceInfomationModeldata.getFreight_collect()));
        } else if (is_paper == 2) {
            paramsMap.put("material", "0");
            paramsMap.put("email", invoiceInfomationModeldata.getEmail());
        }
        if (ticket_type == 1) {

        } else if (ticket_type == 2){
            paramsMap.put("invoice_content_id", String.valueOf(invoiceInfomationModeldata.getInvoice_content_id()));
            paramsMap.put("tax_number", invoiceInfomationModeldata.getTax_number());
        } else if (ticket_type == 3){
            paramsMap.put("invoice_content_id", String.valueOf(invoiceInfomationModeldata.getInvoice_content_id()));
            paramsMap.put("tax_number", invoiceInfomationModeldata.getTax_number());
            paramsMap.put("company_region", invoiceInfomationModeldata.getCompany_region());
            paramsMap.put("company_address", invoiceInfomationModeldata.getCompany_address());
            paramsMap.put("company_mobile", invoiceInfomationModeldata.getCompany_mobile());
            paramsMap.put("bank", invoiceInfomationModeldata.getBank());
            paramsMap.put("account", invoiceInfomationModeldata.getAccount());
            paramsMap.put("general_taxpayer_qualification", invoiceInfomationModeldata.getGeneral_taxpayer_qualification());
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "invoice_orders",
                paramsMap,2));
        call.enqueue(handler);
    }
    //开票订单支付（付款）(确认订单)2.4.2(0:线下支付 1：微信app支付 2:微信公众号支付 3:微信原生扫码支付 4:钱包支付 6:5w微信支付10:支付宝APP支付)
    public static void pay_invoice_order(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                String id, int payment_mode,int password) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("payment_mode",String.valueOf(payment_mode));
        if (password != -1 && String.valueOf(password).length() == 6) {
            paramsMap.put("password",String.valueOf(password));
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "invoice_orders/" + id,
                paramsMap,1));
        call.enqueue(handler);
    }
    //首页根据商家所在行业推荐资源列表接口 3.0
    public static void getrecommending_resources(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                    int page,int resource_type) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page",String.valueOf(page));
        paramsMap.put("pageSize",String.valueOf(10));
        paramsMap.put("resource_type",String.valueOf(resource_type));
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "recommending_resources",
                paramsMap,2));
        call.enqueue(handler);
    }
    // 获取场地的其他展位接口 3.6.1
    public static void getOtherPhyRes(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                                 int community_id,
                                                 int page,int pageSize,int physical_resource_id,
                                      ApiResourcesModel apiResourcesModel) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page",String.valueOf(page));
        paramsMap.put("pageSize",String.valueOf(pageSize));
        if (physical_resource_id > 0) {
            paramsMap.put("physical_resource_id",String.valueOf(physical_resource_id));
        }
//        if (apiResourcesModel != null) {
//            if (apiResourcesModel.getMin_person_flow() != null && apiResourcesModel.getMin_person_flow().length() > 0) {
//                paramsMap.put("min_person_flow", apiResourcesModel.getMin_person_flow());
//            }
//            if (apiResourcesModel.getMax_person_flow() != null && apiResourcesModel.getMax_person_flow().length() > 0) {
//                paramsMap.put("max_person_flow", apiResourcesModel.getMax_person_flow());
//            }
//            if (apiResourcesModel.getMin_price() != null && apiResourcesModel.getMin_price().length() > 0) {
//                paramsMap.put("min_price", apiResourcesModel.getMin_price());
//            }
//            if (apiResourcesModel.getMax_price() != null && apiResourcesModel.getMax_price().length() > 0) {
//                paramsMap.put("max_price", apiResourcesModel.getMax_price());
//            }
//            if (apiResourcesModel.getMin_area() != null && apiResourcesModel.getMin_area().length() > 0) {
//                paramsMap.put("min_area", apiResourcesModel.getMin_area());
//            }
//            if (apiResourcesModel.getMax_area() != null && apiResourcesModel.getMax_area().length() > 0) {
//                paramsMap.put("max_area", apiResourcesModel.getMax_area());
//            }
//        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "communities/" + String.valueOf(community_id) +
                        "/other_physicals",
                paramsMap,2));//2018/12/20 版本号是2
        call.enqueue(handler);
    }
    //获取最新签约列表
    public static void getcontractlist(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,int number) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("number",String.valueOf(number));
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "contract",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取文章列表(合作案例) 3.0
    public static void getarticleslist(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,String article_type_id,
                                       String pageSize) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page",String.valueOf(1));
        if (pageSize != null &&
                pageSize.length() > 0) {
            paramsMap.put("pageSize",pageSize);
        }
        paramsMap.put("article_type_id",article_type_id);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "articles",
                paramsMap,2));
        call.enqueue(handler);
    }
    //评价模块获取场地信息 3.0
    public static void getreview_fieldinfo(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,String field_order_item_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("field_order_item_id",field_order_item_id);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "resource_info",
                paramsMap,3));
        call.enqueue(handler);
    }
    //获取分类列表 3.0
    public static void getSiteTypeList(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "admin/hot_category",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取拼团列表 3.0
    public static void getGroupBookingList(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                           int page,String city_id,int pageSize,int group_purchase_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page",String.valueOf(page));
        paramsMap.put("city_id",city_id);
        if (pageSize > 0) {
            paramsMap.put("pageSize",String.valueOf(pageSize));
        }
        if (group_purchase_id > 0) {
            paramsMap.put("group_purchase_id",String.valueOf(group_purchase_id));
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "groups",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取拼团详情 3.0
    public static void getGroupBookingInfo(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                           String id) {
        HashMap<String, String> paramsMap = new HashMap<>();

        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "groups/" + id,
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取拼团订单列表 3.0
    public static void getGroupBookingListOrders(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                           int page,String status) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page",String.valueOf(page));
        paramsMap.put("status",status);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "group_purchase_orders",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取社区的其他资源接口 3.0
    public static void getOtherResourcesList(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                                 int communityId, int current_resource_id,int res_type_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("current_resource_id",String.valueOf(current_resource_id));//当前售卖资源ID
        paramsMap.put("res_type_id",String.valueOf(res_type_id));
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "communities/"+String.valueOf(communityId)+"/other_resources",
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取拼团活动状态和第几次参团（调取拼团下单接口前需先调用此接口) 3.0
    public static void getGroupStatus(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                             String communityId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "group_status/"+communityId,
                paramsMap,1));
        call.enqueue(handler);
    }
    //闪屏 3.0
    public static void getSplashScreen(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                      String city_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (city_id != null &&
                city_id.length() > 0) {
            paramsMap.put("city_id",city_id);
        } else {
            paramsMap.put("city_id","90");
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "splash_screen",
                paramsMap,1));
        call.enqueue(handler);
    }
    //设置虚拟号被叫
    public static void getVirtualNumber(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                            String field_order_item_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("field_order_item_id",field_order_item_id);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "virtual_number",
                paramsMap,1));
        call.enqueue(handler);
    }
    //取消询价 3.1
    public static void deleteEnquiry(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                            String id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestDelete(Config.BASE_API_URL_PHP,
                "enquiry_information/"+id,
                paramsMap,1));
        call.enqueue(handler);
    }
    //快速搜索 3.2
    public static void getFastSearchList(SearchFieldAreaActivity activity, String keyword,
                                         String city_id, String res_type_id,
                                         OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("keyword",keyword);
        paramsMap.put("city_id",city_id);
        if (!res_type_id.equals("1")) {
            paramsMap.put("res_type_id",res_type_id);
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "fast/search",
                paramsMap,2));
        call.enqueue(handler);
        activity.mCallsList.add(call);
    }
    //周边场地 3.6.1
    public static void getNearbyRes(int id, int city_id, int page, int pageSize,
                                         OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("city_id",String.valueOf(city_id));
        paramsMap.put("page",String.valueOf(page));
        if (pageSize > 0) {
            paramsMap.put("pageSize",String.valueOf(pageSize));
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "communities/" + String.valueOf(id) + "/nearby",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取发票内容和税点3.2
    public static void getInvoiceTaxPointContent(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "invoice/content",
                paramsMap,1));
        call.enqueue(handler);
    }
    //搜索已经开通的城市 3.4
    public static void getHotCityList(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "city/hot",
                paramsMap,1));
        call.enqueue(handler);
    }
    //服务商列表 3.4
    public static void getServiceProviderList(String pageSize,String page,
            String city_id,
            OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("pageSize",pageSize);
        paramsMap.put("page",page);
        paramsMap.put("city_ids[0]",city_id);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "service_providers",
                paramsMap,2));
        call.enqueue(handler);
    }

    // 2018/4/13 测试配置
    //获取全国城市配置
    public static void getProvincesCityList(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler ,int version,
                                            int is_city, int is_district) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("version",String.valueOf(version));
        paramsMap.put("is_city",String.valueOf(is_city));
        paramsMap.put("is_district",String.valueOf(is_district));
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "province",
                paramsMap,1));
        call.enqueue(handler);
    }
    public static void getCaseList(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler, int is_home, ArrayList<Integer> community_type_ids, ArrayList<Integer> industry_ids,
                                   ArrayList<Integer> spread_way_ids,ArrayList<Integer> promotion_purpose_ids,
                                   ArrayList<Integer> city_ids, ArrayList<Integer> label_ids,final int page,int city_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (is_home == 1) {
            paramsMap.put("is_home_page",String.valueOf(is_home));
        }
        if (community_type_ids != null && community_type_ids.size() > 0) {
            for (int i = 0; i < community_type_ids.size(); i++) {
                paramsMap.put("community_type_ids["+String.valueOf(i)+"]",String.valueOf(community_type_ids.get(i)));
            }
        }
        if (industry_ids != null && industry_ids.size() > 0) {
            for (int i = 0; i < industry_ids.size(); i++) {
                paramsMap.put("industry_ids["+String.valueOf(i)+"]",String.valueOf(industry_ids.get(i)));
            }
        }
        if (spread_way_ids != null && spread_way_ids.size() > 0) {
            for (int i = 0; i < spread_way_ids.size(); i++) {
                paramsMap.put("spread_way_ids["+String.valueOf(i)+"]",String.valueOf(spread_way_ids.get(i)));
            }
        }
        if (promotion_purpose_ids != null && promotion_purpose_ids.size() > 0) {
            for (int i = 0; i < promotion_purpose_ids.size(); i++) {
                paramsMap.put("promotion_purpose_ids["+String.valueOf(i)+"]",String.valueOf(promotion_purpose_ids.get(i)));
            }
        }

        if (city_ids != null && city_ids.size() > 0) {
            for (int i = 0; i < city_ids.size(); i++) {
                paramsMap.put("city_ids["+String.valueOf(i)+"]",String.valueOf(city_ids.get(i)));
            }
        }

        if (label_ids != null && label_ids.size() > 0) {
            for (int i = 0; i < label_ids.size(); i++) {
                paramsMap.put("label_ids["+String.valueOf(i)+"]",String.valueOf(label_ids.get(i)));
            }
        }
        if (city_id > 0) {
            paramsMap.put("city_id", String.valueOf(city_id));
        } else {
            paramsMap.put("city_id", LoginManager.getInstance().getTrackcityid());
        }
        paramsMap.put("page",String.valueOf(page));
        Call call = client.newCall(Request.RequestJsonGet(Config.BASE_API_URL_PHP,
                "activity_cases",
                paramsMap,1));
        call.enqueue(handler);
    }
    public static void getOtherCaseList(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler, int id,
                                   int page,int city_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page",String.valueOf(page));
        if (city_id > 0) {
            paramsMap.put("city_id", String.valueOf(city_id));
        } else {
            paramsMap.put("city_id", LoginManager.getInstance().getTrackcityid());
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "activity_cases/same_other_cases/" + String.valueOf(id),
                paramsMap,1));
        call.enqueue(handler);
    }
    public static void getCaseInfo(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler, int id,
                                   ArrayList<Integer> community_type_ids, ArrayList<Integer> industry_ids,
                                   ArrayList<Integer> spread_way_ids,ArrayList<Integer> promotion_purpose_ids,
                                   ArrayList<Integer> city_ids,
                                   ArrayList<Integer> label_ids,int city_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (community_type_ids != null && community_type_ids.size() > 0) {
            for (int i = 0; i < community_type_ids.size(); i++) {
                paramsMap.put("community_type_ids["+String.valueOf(i)+"]",String.valueOf(community_type_ids.get(i)));
            }
        }
        if (industry_ids != null && industry_ids.size() > 0) {
            for (int i = 0; i < industry_ids.size(); i++) {
                paramsMap.put("industry_ids["+String.valueOf(i)+"]",String.valueOf(industry_ids.get(i)));
            }
        }
        if (spread_way_ids != null && spread_way_ids.size() > 0) {
            for (int i = 0; i < spread_way_ids.size(); i++) {
                paramsMap.put("spread_way_ids["+String.valueOf(i)+"]",String.valueOf(spread_way_ids.get(i)));
            }
        }
        if (promotion_purpose_ids != null && promotion_purpose_ids.size() > 0) {
            for (int i = 0; i < promotion_purpose_ids.size(); i++) {
                paramsMap.put("promotion_purpose_ids["+String.valueOf(i)+"]",String.valueOf(promotion_purpose_ids.get(i)));
            }
        }

        if (city_ids != null && city_ids.size() > 0) {
            for (int i = 0; i < city_ids.size(); i++) {
                paramsMap.put("city_ids["+String.valueOf(i)+"]",String.valueOf(city_ids.get(i)));
            }
        }
        if (label_ids != null && label_ids.size() > 0) {
            for (int i = 0; i < label_ids.size(); i++) {
                paramsMap.put("label_ids["+String.valueOf(i)+"]",String.valueOf(label_ids.get(i)));
            }
        }

        if (city_id > 0) {
            paramsMap.put("city_id", String.valueOf(city_id));
        } else {
            paramsMap.put("city_id", LoginManager.getInstance().getTrackcityid());
        }
        Call call = client.newCall(Request.RequestJsonGet(Config.BASE_API_URL_PHP,
                "activity_cases/" + String.valueOf(id),
                paramsMap,2));
        call.enqueue(handler);
    }
    //发票抬头信息列表 3.6
    public static void getInvoiceTitleList(OkHttpClient client,
                                           LinhuiAsyncHttpResponseHandler handler, int invoice_type,
                                           int page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (invoice_type > 0) {
            paramsMap.put("invoice_type",String.valueOf(invoice_type));
        }
        paramsMap.put("page",String.valueOf(page));
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "invoice-title-infos",
                paramsMap,1));
        call.enqueue(handler);
    }
    //新增发票抬头信息 3.6
    public static void addInvoiceTitle(OkHttpClient client,
                                           LinhuiAsyncHttpResponseHandler handler, InvoiceInfomationModel invoiceInfomationModel) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (invoiceInfomationModel != null &&
                invoiceInfomationModel.getInvoice_type() > 0) {
            paramsMap.put("invoice_type",String.valueOf(invoiceInfomationModel.getInvoice_type()));
            if (invoiceInfomationModel.getInvoice_type() == 1) {
                paramsMap.put("title",invoiceInfomationModel.getTitle());
            } else if (invoiceInfomationModel.getInvoice_type() == 2) {
                paramsMap.put("title",invoiceInfomationModel.getTitle());
                paramsMap.put("tax_number",invoiceInfomationModel.getTax_number());
            } else if (invoiceInfomationModel.getInvoice_type() == 3) {
                paramsMap.put("title",invoiceInfomationModel.getTitle());
                paramsMap.put("tax_number",invoiceInfomationModel.getTax_number());
                paramsMap.put("company_region",invoiceInfomationModel.getCompany_region());
                paramsMap.put("company_address",invoiceInfomationModel.getCompany_address());
                paramsMap.put("company_mobile",invoiceInfomationModel.getCompany_mobile());
                paramsMap.put("bank",invoiceInfomationModel.getBank());
                paramsMap.put("account",invoiceInfomationModel.getAccount());
                paramsMap.put("general_taxpayer_qualification",invoiceInfomationModel.getGeneral_taxpayer_qualification());
            }
            paramsMap.put("is_default",String.valueOf(invoiceInfomationModel.getIs_default()));
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "invoice-title-info",
                paramsMap,2));
        call.enqueue(handler);
    }
    //编辑发票抬头信息 3.6
    public static void editInvoiceTitle(OkHttpClient client,
                                       LinhuiAsyncHttpResponseHandler handler,
                                        InvoiceInfomationModel invoiceInfomationModel) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (invoiceInfomationModel != null &&
                invoiceInfomationModel.getInvoice_type() > 0) {
            paramsMap.put("invoice_type",String.valueOf(invoiceInfomationModel.getInvoice_type()));
            if (invoiceInfomationModel.getInvoice_type() == 1) {
                paramsMap.put("title",invoiceInfomationModel.getTitle());
            } else if (invoiceInfomationModel.getInvoice_type() == 2) {
                paramsMap.put("title",invoiceInfomationModel.getTitle());
                paramsMap.put("tax_number",invoiceInfomationModel.getTax_number());
            } else if (invoiceInfomationModel.getInvoice_type() == 3) {
                paramsMap.put("title",invoiceInfomationModel.getTitle());
                paramsMap.put("tax_number",invoiceInfomationModel.getTax_number());
                paramsMap.put("company_region",invoiceInfomationModel.getCompany_region());
                paramsMap.put("company_address",invoiceInfomationModel.getCompany_address());
                paramsMap.put("company_mobile",invoiceInfomationModel.getCompany_mobile());
                paramsMap.put("bank",invoiceInfomationModel.getBank());
                paramsMap.put("account",invoiceInfomationModel.getAccount());
                paramsMap.put("general_taxpayer_qualification",invoiceInfomationModel.getGeneral_taxpayer_qualification());
            }
            paramsMap.put("is_default",String.valueOf(invoiceInfomationModel.getIs_default()));
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "invoice-title-info/" + String.valueOf(invoiceInfomationModel.getId()),
                paramsMap,2));
        call.enqueue(handler);
    }
    //删除发票抬头信息
    public static void deleteInvoiceTitle(OkHttpClient client,
                                           LinhuiAsyncHttpResponseHandler handler, int id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestDelete(Config.BASE_API_URL_PHP,
                "invoice-title-info/" + String.valueOf(id),
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取用户开票抬头详情
    public static void getInvoiceTitle(OkHttpClient client,
                                          LinhuiAsyncHttpResponseHandler handler, int id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "invoice-title-info/" + String.valueOf(id),
                paramsMap,1));
        call.enqueue(handler);
    }
    //设置用户默认开票抬头
    public static void setInvoiceTitleDefault(OkHttpClient client,
                                       LinhuiAsyncHttpResponseHandler handler, int id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "invoice-title-info/" + String.valueOf(id) + "/is_default",
                paramsMap,1));
        call.enqueue(handler);
    }
    public static void getCommunityType(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "category/top_categories",
                paramsMap,1));
        call.enqueue(handler);
    }

    /**
     * 活动案例列表筛选项 3.9
     * @param client
     * @param handler
     */
    public static void getCaseSelection(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "activity_cases/selection",
                paramsMap,1));
        call.enqueue(handler);
    }

    //活动列表 3.6.1
    public static void getActivityList(OkHttpClient client,
                                              LinhuiAsyncHttpResponseHandler handler
            , String city_id,double lat, double lng, int page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("city_id",city_id);
        if (lat > 0) {
            paramsMap.put("latitude", String.valueOf(lat));
        }
        if (lng > 0) {
            paramsMap.put("longitude", String.valueOf(lng));
        }

        paramsMap.put("page",String.valueOf(page));
        paramsMap.put("page_size","10");
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "selling_resource/activities",
                paramsMap,4));
        call.enqueue(handler);
    }
    //地图搜索场地列表接口 3.6.1
    public static void getMapCommunityList(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                           ApiResourcesModel apiResourcesModel) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (apiResourcesModel.getZoom_level() > 0) {
            paramsMap.put("zoom_level",String.valueOf(apiResourcesModel.getZoom_level()));
        }
        if (apiResourcesModel.getCity_id() != null &&
                apiResourcesModel.getCity_id().length() > 0) {
            paramsMap.put("city_id",apiResourcesModel.getCity_id());
        }
        if (apiResourcesModel.getLatitude() > 0) {
            paramsMap.put("latitude", String.valueOf(apiResourcesModel.getLatitude()));
        }
        if (apiResourcesModel.getLongitude() > 0) {
            paramsMap.put("longitude", String.valueOf(apiResourcesModel.getLongitude()));
        }
        if (apiResourcesModel.getLatitude_delta() > 0) {
            paramsMap.put("latitude_delta", String.valueOf(apiResourcesModel.getLatitude_delta()));
        }
        if (apiResourcesModel.getLongitude_delta() > 0) {
            paramsMap.put("longitude_delta", String.valueOf(apiResourcesModel.getLongitude_delta()));
        }
        if (apiResourcesModel.getKeywords() != null && apiResourcesModel.getKeywords().length() > 0) {
            paramsMap.put("keywords", apiResourcesModel.getKeywords());
        }
        if (apiResourcesModel.getDistrict_ids() != null &&
                apiResourcesModel.getDistrict_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getDistrict_ids().size(); i++) {
                paramsMap.put("district_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getDistrict_ids().get(i)));
            }
        }
        if (apiResourcesModel.getTrading_area_ids() != null &&
                apiResourcesModel.getTrading_area_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getTrading_area_ids().size(); i++) {
                paramsMap.put("trading_area_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getTrading_area_ids().get(i)));
            }
        }
        if (apiResourcesModel.getSubway_station_ids() != null &&
                apiResourcesModel.getSubway_station_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getSubway_station_ids().size(); i++) {
                paramsMap.put("subway_station_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getSubway_station_ids().get(i)));
            }
        }
        if (apiResourcesModel.getCommunity_type_ids() != null &&
                apiResourcesModel.getCommunity_type_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getCommunity_type_ids().size(); i++) {
                paramsMap.put("community_type_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getCommunity_type_ids().get(i)));
            }
        }
        if (apiResourcesModel.getCity_ids() != null &&
                apiResourcesModel.getCity_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getCity_ids().size(); i++) {
                paramsMap.put("city_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getCity_ids().get(i)));
            }
        }
        //类目属性
        if (apiResourcesModel.getAttributes() != null &&
                apiResourcesModel.getAttributes().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getAttributes().size(); i++) {
                String attributes = "";
                for (int j = 0; j < apiResourcesModel.getAttributes().get(i).getOption_ids().size(); j++) {
                    if (attributes.length() == 0) {
                        attributes = "[" + String.valueOf(apiResourcesModel.getAttributes().get(i).getOption_ids().get(j));
                        if (j == apiResourcesModel.getAttributes().get(i).getOption_ids().size() - 1) {
                            attributes = attributes + "]";
                        }
                    } else {
                        attributes = attributes + "," + String.valueOf(apiResourcesModel.getAttributes().get(i).getOption_ids().get(j));
                        if (j == apiResourcesModel.getAttributes().get(i).getOption_ids().size() - 1) {
                            attributes = attributes + "]";
                        }
                    }
                }
                attributes = "{\"id\":" + String.valueOf(apiResourcesModel.getAttributes().get(i).getId()) +"," +
                        "\"option_ids\":" + attributes + "}";
                paramsMap.put("attributes[" + String.valueOf(i) + "]", attributes);
            }
        }
        if (apiResourcesModel.getLat() > 0) {
            paramsMap.put("lat", String.valueOf(apiResourcesModel.getLat()));
        }
        if (apiResourcesModel.getLng() > 0) {
            paramsMap.put("lng", String.valueOf(apiResourcesModel.getLng()));
        }
        if (apiResourcesModel.getLabel_ids() != null &&
                apiResourcesModel.getLabel_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getLabel_ids().size(); i++) {
                paramsMap.put("label_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getLabel_ids().get(i)));
            }
        }
        if (apiResourcesModel.getDeveloper_ids() != null &&
                apiResourcesModel.getDeveloper_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getDeveloper_ids().size(); i++) {
                paramsMap.put("developer_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getDeveloper_ids().get(i)));
            }
        }
        if (apiResourcesModel.getMin_price() != null && apiResourcesModel.getMin_price().length() > 0) {
            paramsMap.put("min_price", apiResourcesModel.getMin_price());
        }
        if (apiResourcesModel.getMax_price() != null && apiResourcesModel.getMax_price().length() > 0) {
            paramsMap.put("max_price", apiResourcesModel.getMax_price());
        }
        if (apiResourcesModel.getMin_year() != null) {
            paramsMap.put("min_year", String.valueOf(apiResourcesModel.getMin_year()));
        }
        if (apiResourcesModel.getMax_year() != null) {
            paramsMap.put("max_year", String.valueOf(apiResourcesModel.getMax_year()));
        }
        if (apiResourcesModel.getNearby() > 0) {
            paramsMap.put("nearby", String.valueOf(apiResourcesModel.getNearby()));
        }



        if (apiResourcesModel.getMin_area() != null) {
            paramsMap.put("min_area", String.valueOf(apiResourcesModel.getMin_area()));
        }
        if (apiResourcesModel.getMax_area() != null) {
            paramsMap.put("max_area", String.valueOf(apiResourcesModel.getMax_area()));
        }
        if (apiResourcesModel.getMin_person_flow() != null) {
            paramsMap.put("min_person_flow", apiResourcesModel.getMin_person_flow());
        }
        if (apiResourcesModel.getMax_person_flow() != null) {
            paramsMap.put("max_person_flow", apiResourcesModel.getMax_person_flow());
        }
        if (apiResourcesModel.getNot_need_deposit() != null) {
            paramsMap.put("not_need_deposit", String.valueOf(apiResourcesModel.getNot_need_deposit()));
        }
        if (apiResourcesModel.getHas_subsidy() != null) {
            paramsMap.put("has_subsidy", String.valueOf(apiResourcesModel.getHas_subsidy()));
        }
        if (apiResourcesModel.getHas_activity() != null) {
            paramsMap.put("has_activity", String.valueOf(apiResourcesModel.getHas_activity()));
        }
        if (apiResourcesModel.getActivity_type_ids() != null &&
                apiResourcesModel.getActivity_type_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getActivity_type_ids().size(); i++) {
                paramsMap.put("activity_type_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getActivity_type_ids().get(i)));
            }
        }
        if (apiResourcesModel.getAge_level_ids() != null &&
                apiResourcesModel.getAge_level_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getAge_level_ids().size(); i++) {
                paramsMap.put("age_level_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getAge_level_ids().get(i)));
            }
        }
        if (apiResourcesModel.getIndoor() != null &&
                apiResourcesModel.getIndoor().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getIndoor().size(); i++) {
                paramsMap.put("indoor["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getIndoor().get(i)));
            }
        }
        if (apiResourcesModel.getFacilities() != null &&
                apiResourcesModel.getFacilities().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getFacilities().size(); i++) {
                paramsMap.put("facilities["+String.valueOf(i)+"]",apiResourcesModel.getFacilities().get(i));
            }
        }
        paramsMap.put("order", apiResourcesModel.getOrder());
        paramsMap.put("order_by", apiResourcesModel.getOrder_by());
        if (apiResourcesModel.getNavigation() != null) {
            paramsMap.put("navigation", String.valueOf(apiResourcesModel.getNavigation()));
        }
        paramsMap.put("page_size", "10");
        paramsMap.put("page", apiResourcesModel.getPage());
        Call call = client.newCall(Request.RequestJsonGet(Config.BASE_API_URL_PHP,
                "communities/map_search",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取导航栏配置
    public static void getNavigationBar(OkHttpClient client,
                                       LinhuiAsyncHttpResponseHandler handler
            ,int plate) {//所属板块(1:导航栏配置，2:场地推荐区块配置)
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("plate",String.valueOf(plate));
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "home/navigation_bar",
                paramsMap,1));
        call.enqueue(handler);
    }
    //场地详情 3.6.1
    public static void getCommunityInfo(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                        int id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "communities/"+ String.valueOf(id),
                paramsMap,2));
        call.enqueue(handler);
    }
    //展位详情为您推荐场地 3.6.1
    public static void getRecommendedRes(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                        int id, int page_size,int page,int city_id,int category_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("community_id",String.valueOf(id));
        paramsMap.put("page",String.valueOf(page));
        paramsMap.put("page_size",String.valueOf(page_size));
        if (city_id > 0) {
            paramsMap.put("city_id",String.valueOf(city_id));
        }
        if (category_id > 0) {
            paramsMap.put("category_id",String.valueOf(category_id));
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "communities/recommends",
                paramsMap,2));
        call.enqueue(handler);
    }

    /**
     * 获取资源详情 展位详情 3.6.1
     * @param client
     * @param handler
     * @param id
     * @param apiResourcesModel
     */
    public static void getPhyResInfo(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                     String id,ApiResourcesModel apiResourcesModel) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (apiResourcesModel != null) {
            if (apiResourcesModel.getMin_price() != null && apiResourcesModel.getMin_price().length() > 0) {
                paramsMap.put("min_price", apiResourcesModel.getMin_price());
            }
            if (apiResourcesModel.getMax_price() != null && apiResourcesModel.getMax_price().length() > 0) {
                paramsMap.put("max_price", apiResourcesModel.getMax_price());
            }
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "lists/"+ id,
                paramsMap,2));//2018/12/20 版本号升级为2
        call.enqueue(handler);
    }
    //获取场地所有展位 3.6.1
    public static void getCommunityAllPhyRes(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                      int community_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "communities/" + String.valueOf(community_id) +
                        "/physical_resources",
                paramsMap,1));
        call.enqueue(handler);
    }
    //app导航栏配置列表 3.6.1
    public static void getCategoriesBarList(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "home/app_navigations",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取指定类目下的所有前端可筛选属性 3.6.1
    public static void getAttributesList(OkHttpClient client,
                                       LinhuiAsyncHttpResponseHandler handler
            , ArrayList<Integer> category_ids) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (category_ids != null && category_ids.size() > 0) {
            for (int i = 0; i < category_ids.size(); i++) {
                paramsMap.put("category_ids["+String.valueOf(i)+"]",String.valueOf(category_ids.get(i)));
            }
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "category/selectable/attributes",
                paramsMap,1));
        call.enqueue(handler);
    }
    // 一键兑换优惠券 3.7
    public static void exchangeCoupons(OkHttpClient client,
                                         LinhuiAsyncHttpResponseHandler handler
            , ArrayList<Integer> coupons) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("coupons", coupons);
        Call call = client.newCall(Request.RequestJsonPost(Config.BASE_API_URL_PHP,
                "coupons/exchange",
                jsonObject.toJSONString(),1));
        call.enqueue(handler);
    }
    // 新人礼包 3.7
    public static void getCouponsGifts(OkHttpClient client,
                                       LinhuiAsyncHttpResponseHandler handler
            , int page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page",String.valueOf(page));
        paramsMap.put("page_size","10");
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "coupons/gifts",
                paramsMap,1));
        call.enqueue(handler);
    }
    // 用户优惠券列表 3.7
    public static void getMyCoupons(OkHttpClient client,
                                       LinhuiAsyncHttpResponseHandler handler
            , int expired,int used, int page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("expired",String.valueOf(expired));
        paramsMap.put("used",String.valueOf(used));
        paramsMap.put("page",String.valueOf(page));
        paramsMap.put("page_size","10");
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "coupons",
                paramsMap,1));
        call.enqueue(handler);
    }
    // 积分兑换优惠券列表 3.7
    public static void getCouponsExchangeList(OkHttpClient client,
                                    LinhuiAsyncHttpResponseHandler handler
            , int page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "coupons/exchange-list",
                paramsMap,1));
        call.enqueue(handler);
    }
    // 领取优惠券 (兑换)（id为优惠券的ID） 3.7
    public static void receiveCoupons(OkHttpClient client,
                                              LinhuiAsyncHttpResponseHandler handler
            , int id, int quantity) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("quantity",String.valueOf(quantity));
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "coupons/" + String.valueOf(id) + "/receive",
                paramsMap,1));
        call.enqueue(handler);
    }
    // 场地所有优惠券列表 3.7
    public static void getCommunityCoupons(OkHttpClient client,
                                      LinhuiAsyncHttpResponseHandler handler,
            int community_id, int is_received, int page_size,int page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("is_received",String.valueOf(is_received));
        paramsMap.put("page_size",String.valueOf(page_size));
        paramsMap.put("page",String.valueOf(page));
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "communities/" + String.valueOf(community_id) + "/coupons",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取多个场地的优惠券列表 （提交订单时选择优惠券）3.7
    public static void getOrderCommunityCoupons(OkHttpClient client,
                                           LinhuiAsyncHttpResponseHandler handler,
                                           ArrayList<Integer> community_ids,
                                                ArrayList<Integer> coupon_ids) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (community_ids != null && community_ids.size() > 0) {
            for (int i = 0; i < community_ids.size(); i++) {
                paramsMap.put("community_ids["+String.valueOf(i)+"]",String.valueOf(community_ids.get(i)));
            }
        }
        if (coupon_ids != null && coupon_ids.size() > 0) {
            for (int i = 0; i < coupon_ids.size(); i++) {
                paramsMap.put("coupon_ids["+String.valueOf(i)+"]",String.valueOf(coupon_ids.get(i)));
            }
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "communities/coupons",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取首页邻汇数据 3.7
    public static void getLinhuiData(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "home/linhui_data",
                paramsMap,1));
        call.enqueue(handler);
    }
    //保存用户下单须知选中状态 3.7
    public static void setOrderNoticed(OkHttpClient client,
                                                LinhuiAsyncHttpResponseHandler handler,
                                                int ordering_noticed) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("ordering_noticed",String.valueOf(ordering_noticed));
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "users/ordering_noticed",
                paramsMap,1));
        call.enqueue(handler);
    }

    /**
     * 获取展位下的所有供给 3.8
     * @param client
     * @param handler
     * @param id 资源ID(ID为展位id)
     * @param res_type_id 供给类型 （3：活动）
     */
    public static void getSellRes(OkHttpClient client,
                                       LinhuiAsyncHttpResponseHandler handler,
                                       String id,String res_type_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("res_type_id",res_type_id);
        paramsMap.put("page_size","100");
        paramsMap.put("page","1");
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "lists/" + id + "/resources",
                paramsMap,1));
        call.enqueue(handler);
    }

    /**
     * 供给详情 3.8
     * @param client
     * @param handler
     * @param id 供给id
     */
    public static void getSellResInfo(OkHttpClient client,
                                  LinhuiAsyncHttpResponseHandler handler,
                                  String id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "resources/" + id,
                paramsMap,7));//2018/12/20 版本号升级为7
        call.enqueue(handler);
    }

    /**
     *
     * 发布需求 3.8
     * @param client
     * @param handler
     * @param product  产品名 取值范围: ..256
     * @param minimum_area 需求的最小面积(两位小数)
     * @param maximum_area 需求的最大面积
     * @param single_field_budget 单场预算(单位：元/天)
     * @param start 活动开始时间(必须为有效的日期格式e.g.2017-02-28)
     * @param end 活动结束时间(必须为有效的日期格式e.g.2019-01-31)
     * @param name 联系人 取值范围: ..32
     * @param mobile 手机号
     * @param community_resource_id 场地id
     * @param physical_resource_id 展位id
     * @param city_ids 所需城市ID数组(全国id传0)
     * @param community_type_ids 场地类型ID数组
     */
    public static void appealDemand(OkHttpClient client,
                                      LinhuiAsyncHttpResponseHandler handler,
                                      String product,String minimum_area,String maximum_area,
                                      String single_field_budget,String start, String end,
                                      String name, String mobile, String community_resource_id,
                                      String physical_resource_id,ArrayList<Integer>  city_ids,
                                      ArrayList<Integer> community_type_ids) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("product",product);
        paramsMap.put("minimum_area",minimum_area);
        paramsMap.put("maximum_area",maximum_area);
        paramsMap.put("single_field_budget",single_field_budget);
        paramsMap.put("start",start);
        paramsMap.put("end",end);
        paramsMap.put("name",name);
        paramsMap.put("mobile",mobile);
        if (community_resource_id != null &&
                community_resource_id.length() > 0) {
            paramsMap.put("community_resource_id",community_resource_id);
        }
        if (physical_resource_id != null &&
                physical_resource_id.length() > 0) {
            paramsMap.put("physical_resource_id",physical_resource_id);
        }
        if (city_ids != null &&
                city_ids.size() > 0) {
            for (int i = 0; i < city_ids.size(); i++) {
                paramsMap.put("city_ids[" + String.valueOf(i) + "]", String.valueOf(city_ids.get(i)));
            }
        }
        if (community_type_ids != null &&
                community_type_ids.size() > 0) {
            for (int i = 0; i < community_type_ids.size(); i++) {
                paramsMap.put("community_type_ids[" + String.valueOf(i) + "]", String.valueOf(community_type_ids.get(i)));
            }
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "appeal",
                paramsMap,4));
        call.enqueue(handler);
    }
    public static void getLabelType(int city_id,OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
//        paramsMap.put("city_ids[0]",String.valueOf(city_id)); //城市不传
        paramsMap.put("type","4");
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "label",
                paramsMap,1));
        call.enqueue(handler);
    }
    // 领券中心 3.9
    public static void getCouponCentre(OkHttpClient client,
                                    LinhuiAsyncHttpResponseHandler handler
            , int status, int page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("status",String.valueOf(status));
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "coupons/pools",
                paramsMap,1));
        call.enqueue(handler);
    }

    /**
     * 个人中心未读消息提醒
     * @param client
     * @param handler
     */
    public static void getUnreadNoticedCount(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/unread_noticed",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取首页邻汇数据 3.7
    public static void getAppHoverWindow(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "configurations/app_home_page_hover_window",
                paramsMap,1));
        call.enqueue(handler);
    }

    /**
     * 快速评价 3.10
     * @param client
     * @param handler
     * @param score 评分星级
     * @param anonymity 是否匿名发表 0：否 1：是
     * @param score_of_visitorsflowrate 人流量评分星级 1：超过 2：差不多 3：不足
     * @param score_of_propertymatching 物业配合度评分星级 1：满意 2：一般 3：不满意
     * @param score_of_goalcompletion 目完成度评分星级 1：满意 2：一般 3：不满意
     * @param content 评论内容
     * @param tags 标签ID // [1,2,3]
     * @param images 图片URLjson数组(必须符合JSON格式,且最多4张)
     * @param field_order_item_ids 小订单id数组 [1,2,3,4]
     */
    public static void commentsOrderItems(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                                    int score,int anonymity,int score_of_visitorsflowrate,
                                                    int score_of_propertymatching, int score_of_goalcompletion, String content,
                                                    ArrayList<Integer> tags,ArrayList<String> images,ArrayList<Integer> field_order_item_ids) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("score", String.valueOf(score));
        paramsMap.put("anonymity", String.valueOf(anonymity));
        paramsMap.put("score_of_visitorsflowrate", String.valueOf(score_of_visitorsflowrate));//人流量
        paramsMap.put("score_of_propertymatching", String.valueOf(score_of_propertymatching));//物业配合度
        paramsMap.put("score_of_goalcompletion", String.valueOf(score_of_goalcompletion));//目标完成度评分星级
        if (content != null) {
            if (content.length() > 0) {
                paramsMap.put("content", content);
            }
        }
        if (tags != null && tags.size() > 0) {
            for (int i = 0; i < tags.size(); i++) {
                paramsMap.put("tags["+String.valueOf(i)+"]",String.valueOf(tags.get(i)));
            }
        }
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                paramsMap.put("images["+String.valueOf(i)+"]",images.get(i));
            }
        }
        if (field_order_item_ids != null && field_order_item_ids.size() > 0) {
            for (int i = 0; i < field_order_item_ids.size(); i++) {
                paramsMap.put("field_order_item_ids["+String.valueOf(i)+"]",String.valueOf(field_order_item_ids.get(i)));
            }
        }
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "order_items/comments",
                paramsMap,1));
        call.enqueue(handler);
    }
    //评价中心订单列表 3.10
    public static void getCommentCentreOrders(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "orders/un-comments",
                paramsMap,1));
        call.enqueue(handler);
    }

    /**
     * 展位列表数据数量
     * @param client
     * @param handler
     * @param apiResourcesModel
     */
    public static void getPhyReslistCount(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                     ApiResourcesModel apiResourcesModel) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (apiResourcesModel.getCommunity_ids() != null &&
                apiResourcesModel.getCommunity_ids().size() > 0) {//比场地列表增加的参数
            for (int i = 0; i < apiResourcesModel.getCommunity_ids().size(); i++) {
                paramsMap.put("community_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getCommunity_ids().get(i)));
            }
        }
        if (apiResourcesModel.getKeywords() != null && apiResourcesModel.getKeywords().length() > 0) {
            paramsMap.put("keywords", apiResourcesModel.getKeywords());
        }
        if (apiResourcesModel.getHas_physical() == 1) {//没用到
            paramsMap.put("has_physical", String.valueOf(apiResourcesModel.getHas_physical()));
        }
        if (apiResourcesModel.getCity_ids() != null &&
                apiResourcesModel.getCity_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getCity_ids().size(); i++) {
                paramsMap.put("city_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getCity_ids().get(i)));
            }
        }
        if (apiResourcesModel.getDistrict_ids() != null &&
                apiResourcesModel.getDistrict_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getDistrict_ids().size(); i++) {
                paramsMap.put("district_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getDistrict_ids().get(i)));
            }
        }
        if (apiResourcesModel.getTrading_area_ids() != null &&
                apiResourcesModel.getTrading_area_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getTrading_area_ids().size(); i++) {
                paramsMap.put("trading_area_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getTrading_area_ids().get(i)));
            }
        }
        if (apiResourcesModel.getSubway_station_ids() != null &&
                apiResourcesModel.getSubway_station_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getSubway_station_ids().size(); i++) {
                paramsMap.put("subway_station_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getSubway_station_ids().get(i)));
            }
        }
        if (apiResourcesModel.getIn_trading_area() != null && apiResourcesModel.getIn_trading_area().length() > 0) {
            paramsMap.put("in_trading_area", apiResourcesModel.getIn_trading_area());
        }
        if (apiResourcesModel.getCommunity_type_ids() != null &&
                apiResourcesModel.getCommunity_type_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getCommunity_type_ids().size(); i++) {
                paramsMap.put("community_type_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getCommunity_type_ids().get(i)));
            }
        }
        //属性
        if (apiResourcesModel.getAttributes() != null &&
                apiResourcesModel.getAttributes().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getAttributes().size(); i++) {
                String attributes = "";
                for (int j = 0; j < apiResourcesModel.getAttributes().get(i).getOption_ids().size(); j++) {
                    if (attributes.length() == 0) {
                        attributes = "[" + String.valueOf(apiResourcesModel.getAttributes().get(i).getOption_ids().get(j));
                        if (j == apiResourcesModel.getAttributes().get(i).getOption_ids().size() - 1) {
                            attributes = attributes + "]";
                        }
                    } else {
                        attributes = attributes + "," + String.valueOf(apiResourcesModel.getAttributes().get(i).getOption_ids().get(j));
                        if (j == apiResourcesModel.getAttributes().get(i).getOption_ids().size() - 1) {
                            attributes = attributes + "]";
                        }
                    }
                }
                attributes = "{\"id\":" + String.valueOf(apiResourcesModel.getAttributes().get(i).getId()) +"," +
                        "\"option_ids\":" + attributes + "}";
                paramsMap.put("attributes[" + String.valueOf(i) + "]", attributes);
            }
        }
        if (apiResourcesModel.getLabel_ids() != null &&
                apiResourcesModel.getLabel_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getLabel_ids().size(); i++) {
                paramsMap.put("label_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getLabel_ids().get(i)));
            }
        }
        if (apiResourcesModel.getField_cooperation_type_ids() != null &&
                apiResourcesModel.getField_cooperation_type_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getField_cooperation_type_ids().size(); i++) {
                paramsMap.put("field_cooperation_type_ids[" + String.valueOf(i) + "]", String.valueOf(apiResourcesModel.getField_cooperation_type_ids().get(i)));
            }
        }
        if (apiResourcesModel.getActivity_type_ids() != null &&
                apiResourcesModel.getActivity_type_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getActivity_type_ids().size(); i++) {
                paramsMap.put("activity_type_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getActivity_type_ids().get(i)));
            }
        }
        if (apiResourcesModel.getAge_level_ids() != null &&
                apiResourcesModel.getAge_level_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getAge_level_ids().size(); i++) {
                paramsMap.put("age_level_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getAge_level_ids().get(i)));
            }
        }
        if (apiResourcesModel.getIndoor() != null &&
                apiResourcesModel.getIndoor().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getIndoor().size(); i++) {
                paramsMap.put("indoor["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getIndoor().get(i)));
            }
        }
        if (apiResourcesModel.getFacilities() != null &&
                apiResourcesModel.getFacilities().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getFacilities().size(); i++) {
                paramsMap.put("facilities["+String.valueOf(i)+"]",apiResourcesModel.getFacilities().get(i));
            }
        }
        if (apiResourcesModel.getLocation_type_ids() != null &&
                apiResourcesModel.getLocation_type_ids().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getLocation_type_ids().size(); i++) {
                paramsMap.put("location_type_ids["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getLocation_type_ids().get(i)));
            }
        }
        if (apiResourcesModel.getNot_need_deposit() != null) {
            paramsMap.put("not_need_deposit", String.valueOf(apiResourcesModel.getNot_need_deposit()));
        }

        if (apiResourcesModel.getMin_price() != null && apiResourcesModel.getMin_price().length() > 0) {
            paramsMap.put("min_price", apiResourcesModel.getMin_price());
        }
        if (apiResourcesModel.getMax_price() != null && apiResourcesModel.getMax_price().length() > 0) {
            paramsMap.put("max_price", apiResourcesModel.getMax_price());
        }
        if (apiResourcesModel.getMin_area() != null) {
            paramsMap.put("min_area", String.valueOf(apiResourcesModel.getMin_area()));
        }
        if (apiResourcesModel.getMax_area() != null) {
            paramsMap.put("max_area", String.valueOf(apiResourcesModel.getMax_area()));
        }
        if (apiResourcesModel.getMin_person_flow() != null) {
            paramsMap.put("min_person_flow", apiResourcesModel.getMin_person_flow());
        }
        if (apiResourcesModel.getMax_person_flow() != null) {
            paramsMap.put("max_person_flow", apiResourcesModel.getMax_person_flow());
        }
        if (apiResourcesModel.getLat() > 0) {
            paramsMap.put("lat", String.valueOf(apiResourcesModel.getLat()));
        }
        if (apiResourcesModel.getLng() > 0) {
            paramsMap.put("lng", String.valueOf(apiResourcesModel.getLng()));
        }
        if (apiResourcesModel.getLatitude() > 0) {
            paramsMap.put("latitude", String.valueOf(apiResourcesModel.getLatitude()));
        }
        if (apiResourcesModel.getLongitude() > 0) {
            paramsMap.put("longitude", String.valueOf(apiResourcesModel.getLongitude()));
        }
        if (apiResourcesModel.getLatitude_delta() > 0) {
            paramsMap.put("latitude_delta", String.valueOf(apiResourcesModel.getLatitude_delta()));
        }
        if (apiResourcesModel.getLongitude_delta() > 0) {
            paramsMap.put("longitude_delta", String.valueOf(apiResourcesModel.getLongitude_delta()));
        }
        if (apiResourcesModel.getNearby() > 0) {
            paramsMap.put("nearby", String.valueOf(apiResourcesModel.getNearby()));
        }
        if (apiResourcesModel.getMin_year() != null) {
            paramsMap.put("min_year", String.valueOf(apiResourcesModel.getMin_year()));
        }
        if (apiResourcesModel.getMax_year() != null) {
            paramsMap.put("max_year", String.valueOf(apiResourcesModel.getMax_year()));
        }
        paramsMap.put("order", apiResourcesModel.getOrder());
        paramsMap.put("order_by", apiResourcesModel.getOrder_by());
        if (apiResourcesModel.getNavigation() != null) {
            paramsMap.put("navigation", String.valueOf(apiResourcesModel.getNavigation()));
        }
        if (apiResourcesModel.getPage_size() > 0) {
            paramsMap.put("page_size", String.valueOf(apiResourcesModel.getPage_size()));
        } else {
            paramsMap.put("page_size", "10");
        }
        paramsMap.put("page", apiResourcesModel.getPage());
        if (apiResourcesModel.getDynamic_name() != null &&
                apiResourcesModel.getDynamic_name().length() > 0 &&
                apiResourcesModel.getDynamic_id() != null &&
                apiResourcesModel.getDynamic_id().size() > 0) {
            for (int i = 0; i < apiResourcesModel.getDynamic_id().size(); i++) {
                paramsMap.put(apiResourcesModel.getDynamic_name() +
                        "["+String.valueOf(i)+"]",String.valueOf(apiResourcesModel.getDynamic_id().get(i)));
            }
        }
        //浏览记录
        if (LoginManager.isLogin() && apiResourcesModel.getCity_ids() != null && apiResourcesModel.getCity_ids().size() > 0 &&
                apiResourcesModel.getCity_ids().get(0) != null) {
            try {
                String parameter = "?"+ Request.urlEncode(paramsMap);
                LoginMvpModel.sendBrowseHistories("field_list",parameter,String.valueOf(apiResourcesModel.getCity_ids().get(0)));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "lists/count",
                paramsMap,1));
        call.enqueue(handler);
    }
    public static void getPhyRecommendedList(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                             ApiResourcesModel apiResourcesModel) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (apiResourcesModel.getLat() > 0) {
            paramsMap.put("lat", String.valueOf(apiResourcesModel.getLat()));
        }
        if (apiResourcesModel.getLng() > 0) {
            paramsMap.put("lng", String.valueOf(apiResourcesModel.getLng()));
        }
        if (apiResourcesModel.getPage_size() > 0) {
            paramsMap.put("page_size", String.valueOf(apiResourcesModel.getPage_size()));
        } else {
            paramsMap.put("page_size", "10");
        }
        if (apiResourcesModel.getCity_id() != null && apiResourcesModel.getCity_id().length() > 0) {
            paramsMap.put("city_id", apiResourcesModel.getCity_id());
        } else {
            paramsMap.put("city_id", LoginManager.getTrackcityid());
        }
        paramsMap.put("page", apiResourcesModel.getPage());
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "lists/recommended",
                paramsMap,1));
        call.enqueue(handler);
    }
    public static void enquiry(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                             String sid, String name, String mobile) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("sid", sid);
        paramsMap.put("name", name);
        paramsMap.put("mobile", mobile);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "enquiry_information",
                paramsMap,2));
        call.enqueue(handler);
    }

}
