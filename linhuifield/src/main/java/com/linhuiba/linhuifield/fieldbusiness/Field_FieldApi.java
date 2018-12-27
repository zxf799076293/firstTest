package com.linhuiba.linhuifield.fieldbusiness;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.linhuiba.linhuifield.config.Config;
import com.linhuiba.linhuifield.fieldactivity.FieldAddFieldSearchResActivity;
import com.linhuiba.linhuifield.fieldmodel.Field_AddResourcesModel;
import com.linhuiba.linhuifield.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.linhuifield.network.Request;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;


public class Field_FieldApi {

    //2.0API
    //七牛获取token
    public static void getuptoken (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.QINIU_TOKEN,
                "",
                paramsMap,1));
        call.enqueue(handler);
    }
    //发布资源 物理、售卖资源一起发布3.0
    public static void addresources(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        Call call = client.newCall(Request.RequestJsonPost(Config.BASE_API_URL_PHP,
                "physical_selling_resource",
                JSON.toJSONString(Field_AddResourcesModel.getInstance().getResource(),false)
                ,1));
        call.enqueue(handler);
    }
    //修改已发布资源 物理、售卖资源一起编辑接口 3.0
    public static void editorresources(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,String resourceid) {
        Call call = client.newCall(Request.RequestJsonPost(Config.BASE_API_URL_PHP,
                "physical_selling_resource/"+ resourceid,
                JSON.toJSONString(Field_AddResourcesModel.getInstance().getResource(),false)
                ,1));
        call.enqueue(handler);
    }
    //发布售卖资源3.0
    public static void addSellingResources(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        Call call = client.newCall(Request.RequestJsonPost(Config.BASE_API_URL_PHP,
                "selling_resources",
                JSON.toJSONString(Field_AddResourcesModel.getInstance().getResource(),false)
                ,1));
        call.enqueue(handler);
    }
    // 编辑售卖资源 3.0
    public static void editorSellingResources(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,String resourceid) {
        Call call = client.newCall(Request.RequestJsonPost(Config.BASE_API_URL_PHP,
                "selling_resources/"+ resourceid,
                JSON.toJSONString(Field_AddResourcesModel.getInstance().getResource(),false)
                ,1));
        call.enqueue(handler);
    }
    //获取资源列表 3.0
    public static void getresourceslist (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,int res_type_id, String status, String page,String pageSize) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("status", status);
        paramsMap.put("page", page);
        paramsMap.put("pageSize", pageSize);
        paramsMap.put("res_type_id",String.valueOf(res_type_id));
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "managed_resources",
                paramsMap,3));
        call.enqueue(handler);
    }
    //售卖资源下架接口
    public static void editFieldStatusRefused(Context context,OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                              String fieldId,String off_shelves_reason,String auto_on_shelves_date) {
        HashMap<String, String> paramsMap = new HashMap<>();
        HashMap<String,String> map = new HashMap<>();
        map.put("id",fieldId);
        map.put("off_shelves_reason",off_shelves_reason);
        if (auto_on_shelves_date.length() > 0) {
            map.put("auto_on_shelves_date", auto_on_shelves_date);
        }
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        list.add(map);
        paramsMap.put("ids",JSONArray.toJSONString(list,true));
        Call call = client.newCall(Request.RequestJsonDelete(Config.BASE_API_URL_PHP,
                "managed_resources/available",
                JSONArray.toJSONString(paramsMap,SerializerFeature.WriteMapNullValue)
                ,1));
        call.enqueue(handler);
    }
    //售卖资源上架接口
    public static void editFieldStatusthrough (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler, String fieldId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        HashMap<String,String> map = new HashMap<>();
        map.put("id",fieldId);
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        list.add(map);
        paramsMap.put("ids",JSONArray.toJSONString(list,true));
        Call call = client.newCall(Request.RequestPut(Config.BASE_API_URL_PHP,
                "managed_resources/available",
                paramsMap,3));
        call.enqueue(handler);
    }
    //物业资源端获取订单个数 3.0
    public static void getfieldorderlistitemscount (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "order_items/count",
                paramsMap,2));
        call.enqueue(handler);
    }
    //物业获取订单行列表接口 3.6.1
    public static void getfieldorderlistitems (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler, String status,
                                               String page,String pageSize) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (pageSize != null) {
            if (pageSize.length() > 0) {
                paramsMap.put("pageSize", pageSize);
            }
        }
        paramsMap.put("status", status);
        paramsMap.put("page", page);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "order_items",
                paramsMap,2));
        call.enqueue(handler);
    }
    //物业或管理员同意订单项的接口 3.0
    public static void fieldorderlistitemapproved (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,String id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestPut(Config.BASE_API_URL_PHP,
                "order_items/"+id+"/approved",
                paramsMap,2));
        call.enqueue(handler);
    }
    //物业或管理员拒绝订单项的接口 3.0
    public static void fieldorderlistitemRefused (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,Context context,String id,String objection) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (objection != null) {
            if (objection.length() > 0) {
                paramsMap.put("objection",objection);
            }
        }
        Call call = client.newCall(Request.RequestDelete(Config.BASE_API_URL_PHP,
                "order_items/"+id+"/deny",
                paramsMap,2));
        call.enqueue(handler);
    }
    //增加通知用户 3.0
    public static void addmessageuser(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                      String mobile,String name,
                                      String email,String resources) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", mobile);
        paramsMap.put("name", name);
        if (email != null) {
            if (email.length() > 0) {
                paramsMap.put("email", email);
            }
        }
        paramsMap.put("resources", resources);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "message",
                paramsMap,2));
        call.enqueue(handler);
    }
    //修改用户通知的信息 3.0
    public static void editormessageuser(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler, String id,
                                         String mobile,String name,
                                         String email,JSON resources) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", mobile);
        paramsMap.put("name", name);
        if (email != null) {
            if (email.length() > 0) {
                paramsMap.put("email", email);
            }
        }
        paramsMap.put("resources", resources.toJSONString());
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "message/"+id,
                paramsMap,2));
        call.enqueue(handler);
    }
    //删除场地的通知用户
    public static void deletemessageuser(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler, Context context,String id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestDelete(Config.BASE_API_URL_PHP,
                "message/"+id,
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取某个物业的通知人员信息
    public static void getmessageuserlistitems(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                               String page,String pageSize) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page", page);
        if (pageSize != null) {
            if (pageSize.length() > 0) {
                paramsMap.put("pageSize", pageSize);
            }
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "message",
                paramsMap,1));
        call.enqueue(handler);
    }
    //设置场地指定日期不可用3.0
    public static void setcalendarnouser (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,String res_id,String date) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("res_id",res_id);
        paramsMap.put("date",date);
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "calendar",
                paramsMap,2));
        call.enqueue(handler);
    }
    //删除场地指定日期不可用 3.0
    public static void deletecalendarnouser (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,Context context,String res_id,String date) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("res_id",res_id);
        paramsMap.put("date",date);
        Call call = client.newCall(Request.RequestDelete(Config.BASE_API_URL_PHP,
                "calendar",
                paramsMap,2));
        call.enqueue(handler);
    }
    //供给详情 ( 场地发布 ： id 为供给ID) 3.6.1
    public static void getresources (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler, String fieldId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "selling_resources/" + fieldId,
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取资源日历的订单 3.0
    public static void getresourcecalendar_order_items(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                                       String res_id,int resList,String status,String date_start,String date_end) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (res_id != null) {
            if (res_id.length() > 0) {
                paramsMap.put("res_id", res_id);
            }
        }
        if (resList == 0 || resList == 1) {
            paramsMap.put("resList", String.valueOf(resList));
        }
        paramsMap.put("status", status);
        paramsMap.put("date_start", date_start);
        paramsMap.put("date_end", date_end);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "schedule4realstate_order_items",
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取专属页面信息
    public static void getusershoplistitems(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,String id) {
        Request req = null;
        String function = "";
        if (id != null) {
            if (id.equals("userall")) {
                function =  "user-shop";
            } else {
                function = "communities/"+id+"/resources";
            }
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                function,
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取配置
    public static void getconfigurations(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,String version) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (version == null || (version != null && version.length() == 0)) {
            paramsMap.put("version", "0");
        } else {
            paramsMap.put("version", version);
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "configurations",
                paramsMap,4));
        call.enqueue(handler);
    }
    //获取物业报表接口 3.0
    public static void getproperty_report(OkHttpClient client,
                                          LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/current/property_report",
                paramsMap,3));
        call.enqueue(handler);
    }
    //获取收款人信息接口 2.3(3.0)
    public static void getbeneficiary_info(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,String uid) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/"+uid+"/beneficiary_info",
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取当前用户提醒标记接口/3.0
    public static void getbadge_info(OkHttpClient client,
                                     LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/current/badge_info",
                paramsMap,3));
        call.enqueue(handler);
    }
    //版本更新
    public static void version(OkHttpClient client,
                               LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "config/app_latest_version/android",
                paramsMap,1));
        call.enqueue(handler);
    }
    //搜索场地名接口(如果传入apikey则可以搜索到自己未上架和待审核的场地 3.0
    public static void getsearch_resources (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler, String keyword,
                                        FieldAddFieldSearchResActivity activity) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (keyword.length() > 0) {
            paramsMap.put("keyword",keyword);
        }
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "resources/search",
                paramsMap,2));
        call.enqueue(handler);
        activity.callslist.add(call);
    }
    //获取专属页面资源列表 3.0
    public static void getexclusive_resources (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler, int resource_type,
                                            int page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("resource_type",String.valueOf(resource_type));
        paramsMap.put("page",String.valueOf(page));
        paramsMap.put("pageSize",String.valueOf(10));
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "exclusive_resources",
                paramsMap,2));
        call.enqueue(handler);
    }
    //获取后台发布页面信息接口
    public static void getresources_create (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "admin/resources/create",
                paramsMap,1));
        call.enqueue(handler);
    }
    //下载网络图片
    public static void downloadPic(OkHttpClient client, Callback callback, String url) {
        okhttp3.Request request = new okhttp3.Request.Builder()
                .get()
                .url(url)
                .build();
        Call httpCall  = client.newCall(request);
        httpCall.enqueue(callback);
    }
    //搜索场地接口 （场地发布）
    public static void searchCommunites (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
                                         int city_id, int district_id,
                                         int category_id,int res_type_id,String keywords,FieldAddFieldSearchResActivity activity) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (city_id > 0) {
            paramsMap.put("city_id",String.valueOf(city_id));
        }
        if (district_id > 0) {
            paramsMap.put("district_id",String.valueOf(district_id));
        }

        paramsMap.put("category_id",String.valueOf(category_id));
        paramsMap.put("res_type_id",String.valueOf(res_type_id));
        paramsMap.put("keywords",keywords);
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "communities/search",
                paramsMap,1));
        call.enqueue(handler);
        activity.callslist.add(call);
    }
    //获取指定类目下的属性模板
    public static void getAttributes (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler,
            int category_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("category_id",String.valueOf(category_id));
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "category/attributes",
                paramsMap,1));
        call.enqueue(handler);
    }
    //场地、展位、供给一起发布3.6.1
    public static void addCommunities(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        Call call = client.newCall(Request.RequestJsonPost(Config.BASE_API_URL_PHP,
                "communities",
                JSON.toJSONString(Field_AddResourcesModel.getInstance().getResource(), SerializerFeature.WriteMapNullValue)
                ,1));
        call.enqueue(handler);
    }
    //场地、展位、供给一起编辑3.6.1
    public static void updateCommunities(int id, OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        Call call = client.newCall(Request.RequestJsonPost(Config.BASE_API_URL_PHP,
                "communities/update/"+String.valueOf(id),
                JSON.toJSONString(Field_AddResourcesModel.getInstance().getResource(),SerializerFeature.WriteMapNullValue)
                ,1));
        call.enqueue(handler);
    }
    //获取用户收款人信息接口 3.6.1
    public static void getBeneficiary(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "users/beneficiary_info",
                paramsMap,3));
        call.enqueue(handler);
    }
    //设置虚拟号被叫
    public static void getVirtualNumber(OkHttpClient client,LinhuiAsyncHttpResponseHandler handler,
                                        String field_order_item_id,int type) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("field_order_item_id",field_order_item_id);
        paramsMap.put("type",String.valueOf(type));
        Call call = client.newCall(Request.RequestPost(Config.BASE_API_URL_PHP,
                "virtual_number",
                paramsMap,1));
        call.enqueue(handler);
    }
    //获取默认联系人 下单  发布
    public static void getDefaultAddress(OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "default_address",
                paramsMap,1));
        call.enqueue(handler);
    }
}
