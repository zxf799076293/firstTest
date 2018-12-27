package com.linhuiba.business.fieldbusiness;


import com.linhuiba.business.config.Config;
import com.linhuiba.business.network.LinhuiAsyncHttpResponseHandler;
import com.linhuiba.business.network.Request;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;


/**
 * Created by snowd on 15/3/26.
 */
public class Field_FieldApi {
    //七牛获取认证token/申请发布权限/企业认证/对公打款token
    public static void getuptoken_certs (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.QINIU_TOKEN_CERTS,
                "",
                paramsMap,1));
        call.enqueue(handler);
    }
    //七牛获取评论/轨迹/公司信息token/个人头像
    public static void getuptoken_comment (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.QINIU_TOKEN_COMMENT,
                "",
                paramsMap,1));
        call.enqueue(handler);
    }
    //物业获取订单个数
    public static void getfieldorderlistitemscount (OkHttpClient client, LinhuiAsyncHttpResponseHandler handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        Call call = client.newCall(Request.RequestGet(Config.BASE_API_URL_PHP,
                "order_items/count",
                paramsMap,1));
        call.enqueue(handler);
    }
}
