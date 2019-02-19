package com.linhuiba.business.network;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baselib.app.util.MessageUtils;
import com.linhuiba.business.config.Config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.Call;
import okhttp3.Callback;

/**
 * Created by chentefu on 15/12/1.
 */
public abstract class LinhuiAsyncHttpResponseHandler implements Callback {

    public Class<?> clazz;
    public boolean isArray = false;
    public Object context;
    private Handler mDelivery;
    public LinhuiAsyncHttpResponseHandler() {
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public LinhuiAsyncHttpResponseHandler(Class<?> clazz) {
        mDelivery = new Handler(Looper.getMainLooper());
        this.clazz = clazz;
    }
    public LinhuiAsyncHttpResponseHandler(Class<?> clazz, boolean isArray) {
        mDelivery = new Handler(Looper.getMainLooper());
        this.clazz = clazz;
        this.isArray = isArray;
    }

    @Override
    public void onResponse(Call call, okhttp3.Response responses) throws IOException {
        int statusCode = responses.code();
        okhttp3.internal.http2.Header[] headers= null;
        byte[] responseBody = responses.body().bytes();
        if (statusCode == 200) {
            try {
//                String resourcemsg = new String(responseBody,"UTF-8");
//                if (resourcemsg != null) {
//                    JSONObject json = JSONObject.parseObject(resourcemsg);
//                    Object result = json.get("result");
//                    if (result != null) {
//                        if (result instanceof JSONArray) {
//                            if (isArray == false) {
//                                isArray = true;
//                            }
//                        }
//                    }
//                }
                Response response = Response.parseResponse(responseBody, isArray);
                if (response != null && response.code > 0 && clazz != null && response.data != null
                        && (response.data instanceof JSONObject
                          || response.data instanceof JSONArray)) {
                    Object data = null;
                    if (isArray) {
                        data = JSON.parseArray(response.data.toString(), clazz);
                    } else {
                        data = JSON.parseObject(response.data.toString(), clazz);
                    }
                    sendSuccessResultCallback(statusCode, headers, response, data);
                } else if (response != null) {
                    if (response.code <= 0) {
                        sendFailedStringCallback(false,statusCode, headers, responseBody,
                                new Response.LinhuiResponseException(response.msg, response.code));
                    } else {
                        sendSuccessResultCallback(statusCode, headers, response, response.data);
                    }
                } else {
                    sendFailedStringCallback(false,statusCode, headers, responseBody,
                            new NullPointerException("Response is null"));
                }
            } catch (Throwable throwable) {
                sendFailedStringCallback(false,statusCode, headers, responseBody,
                        throwable);
            }
        } else {
            Throwable error = null;
            if (statusCode == 404) {
                sendFailedStringCallback(false, statusCode, headers, responseBody, new NullPointerException("出错(404)"));
            } else if (statusCode == 405) {
                sendFailedStringCallback(false, statusCode, headers, responseBody, new NullPointerException("出错(405)"));
            } else if (statusCode == 500) {
                String errormsg = "";
                if (!Config.BASE_API_URL_PHP.equals(Config.BASE_API_URL_PHP_PE)) {//se弹出错误
                    try {
                        errormsg = new String(responseBody,"UTF-8");
                        Log.i("报500错的msg:",errormsg);
                        JSONObject jsonObject = JSONObject.parseObject(errormsg);
                        String showerrormsg = "";
                        if (jsonObject != null &&
                                jsonObject.get("message") != null) {
                            showerrormsg = jsonObject.get("message").toString();
                            Log.i("errer:500:",showerrormsg);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                sendFailedStringCallback(false, statusCode, headers, responseBody, new NullPointerException("服务器内部出错(500)" +
                        errormsg));
            } else {
                sendFailedStringCallback(false, statusCode, headers, responseBody, new NullPointerException("系统错误"));
            }
        }
        context = null;
    }
    public abstract void onSuccess(int statusCode, okhttp3.internal.http2.Header[] headers, Response response, Object data);


    @Override
    public void onFailure(Call call, IOException e) {
        //        System.out.println("Network error class=" + error.getClass().getCanonicalName());
        Throwable error = null;
        int statusCode = 0;
        okhttp3.internal.http2.Header[] headers = null;
        byte[] responseBody = null;
        sendFailedStringCallback(false, statusCode, headers, responseBody, new NullPointerException("网络连接错误，请检查网络情况"));
    }
    public abstract void onFailure(boolean superresult, int statusCode, okhttp3.internal.http2.Header[] headers, byte[] responseBody, Throwable error);
    private void sendFailedStringCallback(boolean superresult, final int statusCode, final okhttp3.internal.http2.Header[] headers, final byte[] responseBody, final Throwable error) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                onFailure(false, statusCode, headers, responseBody, error);
            }
        });
    }

    private void sendSuccessResultCallback(final int statusCode, final okhttp3.internal.http2.Header[] headers, final Response response, final Object data) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(statusCode,headers, response, data);
            }
        });
    }

}
