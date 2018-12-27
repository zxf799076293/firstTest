package com.linhuiba.linhuifield.network;

import android.util.Log;

import com.linhuiba.linhuifield.BuildConfig;
import com.linhuiba.linhuipublic.config.LoginManager;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Created by snowd on 15/3/26.
 */
public class Request {

    private Request() {

    }

    public static okhttp3.Request RequestPost(String baseUrl, String function,
                                              HashMap<String, String> paramsMap,int connector_version) {
        okhttp3.Request request = null;
        RequestBody builder = getRequestBody(paramsMap);
        if (LoginManager.getAccessToken() != null && LoginManager.getAccessToken().length() > 0) {
            request = new okhttp3.Request.Builder()
                    .url(baseUrl + function)
                    .addHeader("Accept","application/vnd.linhuiba.v"+ String.valueOf(connector_version) +"+json")
                    .addHeader("Authorization", "Bearer " + LoginManager.getAccessToken())
                    .addHeader("x-client","android")
                    .addHeader("x-client-version",BuildConfig.VERSION_NAME)
                    .post(builder)
                    .build();
        } else {
            request = new okhttp3.Request.Builder()
                    .url(baseUrl + function)
                    .addHeader("Accept","application/vnd.linhuiba.v"+ String.valueOf(connector_version) +"+json")
                    .addHeader("x-client","android")
                    .addHeader("x-client-version",BuildConfig.VERSION_NAME)
                    .post(builder)
                    .build();
        }
        return request;
    }
    public static okhttp3.Request RequestGet(String baseUrl, String function,
                                             HashMap<String, String> paramsMap, int connector_version) {
        okhttp3.Request request =null;
        String requestUrl = "";
        paramsMap.put("v", BuildConfig.VERSION_NAME);
        if (paramsMap != null && paramsMap.size() > 0) {
            try {
                requestUrl = baseUrl+function+"?"+ urlEncode(paramsMap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            requestUrl = baseUrl + function;
        }
        if (LoginManager.getAccessToken() != null && LoginManager.getAccessToken().length() > 0) {
            request = new okhttp3.Request.Builder()
                    .url(requestUrl)
                    .addHeader("Authorization", "Bearer " + LoginManager.getAccessToken())
                    .addHeader("Accept","application/vnd.linhuiba.v"+ String.valueOf(connector_version) +"+json")
                    .addHeader("x-client","android")
                    .addHeader("x-client-version",BuildConfig.VERSION_NAME)
                    .get()
                    .build();

        } else {
            request = new okhttp3.Request.Builder()
                    .url(requestUrl)
                    .addHeader("Accept","application/vnd.linhuiba.v"+ String.valueOf(connector_version) +"+json")
                    .addHeader("x-client","android")
                    .addHeader("x-client-version",BuildConfig.VERSION_NAME)
                    .get()
                    .build();
        }
        return request;
    }
    public static okhttp3.Request RequestPut(String baseUrl, String function,
                                             HashMap<String, String> paramsMap,int connector_version) {
        okhttp3.Request request = null;
        RequestBody builder = getRequestBody(paramsMap);
        if (LoginManager.getAccessToken() != null && LoginManager.getAccessToken().length() > 0) {
            request = new okhttp3.Request.Builder()
                    .url(baseUrl + function)
                    .addHeader("Accept","application/vnd.linhuiba.v"+ String.valueOf(connector_version) +"+json")
                    .addHeader("Authorization", "Bearer " + LoginManager.getAccessToken())
                    .addHeader("x-client","android")
                    .addHeader("x-client-version",BuildConfig.VERSION_NAME)
                    .put(builder)
                    .build();
        } else {
            request = new okhttp3.Request.Builder()
                    .url(baseUrl + function)
                    .addHeader("Accept","application/vnd.linhuiba.v"+ String.valueOf(connector_version) +"+json")
                    .addHeader("x-client","android")
                    .addHeader("x-client-version",BuildConfig.VERSION_NAME)
                    .put(builder)
                    .build();
        }
        return request;
    }
    public static okhttp3.Request RequestDelete(String baseUrl, String function,
                                                HashMap<String, String> paramsMap,int connector_version) {
        okhttp3.Request request =null;
        RequestBody builder = getRequestBody(paramsMap);
        if (LoginManager.getAccessToken() != null && LoginManager.getAccessToken().length() > 0) {
            request = new okhttp3.Request.Builder()
                    .url(baseUrl + function)
                    .addHeader("Accept","application/vnd.linhuiba.v"+ String.valueOf(connector_version) +"+json")
                    .addHeader("Authorization", "Bearer " + LoginManager.getAccessToken())
                    .addHeader("x-client","android")
                    .addHeader("x-client-version",BuildConfig.VERSION_NAME)
                    .delete(builder)
                    .build();
        } else {
            request = new okhttp3.Request.Builder()
                    .url(baseUrl + function)
                    .addHeader("Accept","application/vnd.linhuiba.v"+ String.valueOf(connector_version) +"+json")
                    .addHeader("x-client","android")
                    .addHeader("x-client-version",BuildConfig.VERSION_NAME)
                    .delete(builder)
                    .build();
        }
        return request;
    }
    public static okhttp3.Request RequestJsonPost(String baseUrl, String function,
                                              String jSONString,int connector_version) {
        okhttp3.Request request = null;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                jSONString);
        if (LoginManager.getAccessToken() != null && LoginManager.getAccessToken().length() > 0) {
            request = new okhttp3.Request.Builder()
                    .url(baseUrl + function)
                    .addHeader("Content-Type","application/json")
                    .addHeader("Accept","application/vnd.linhuiba.v"+ String.valueOf(connector_version) +"+json")
                    .addHeader("Authorization", "Bearer " + LoginManager.getAccessToken())
                    .addHeader("x-client","android")
                    .addHeader("x-client-version",BuildConfig.VERSION_NAME)
                    .post(requestBody)
                    .build();
        } else {
            request = new okhttp3.Request.Builder()
                    .url(baseUrl + function)
                    .addHeader("Content-Type","application/json")
                    .addHeader("Accept","application/vnd.linhuiba.v"+ String.valueOf(connector_version) +"+json")
                    .addHeader("x-client","android")
                    .addHeader("x-client-version",BuildConfig.VERSION_NAME)
                    .post(requestBody)
                    .build();
        }
        return request;
    }
    public static okhttp3.Request RequestJsonDelete(String baseUrl, String function,
                                                String jSONString,int connector_version) {
        okhttp3.Request request = null;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                jSONString);
        if (LoginManager.getAccessToken() != null && LoginManager.getAccessToken().length() > 0) {
            request = new okhttp3.Request.Builder()
                    .url(baseUrl + function)
                    .addHeader("Content-Type","application/json")
                    .addHeader("Accept","application/vnd.linhuiba.v"+ String.valueOf(connector_version) +"+json")
                    .addHeader("Authorization", "Bearer " + LoginManager.getAccessToken())
                    .addHeader("x-client","android")
                    .addHeader("x-client-version",BuildConfig.VERSION_NAME)
                    .delete(requestBody)
                    .build();
        } else {
            request = new okhttp3.Request.Builder()
                    .url(baseUrl + function)
                    .addHeader("Content-Type","application/json")
                    .addHeader("Accept","application/vnd.linhuiba.v"+ String.valueOf(connector_version) +"+json")
                    .addHeader("x-client","android")
                    .addHeader("x-client-version",BuildConfig.VERSION_NAME)
                    .delete(requestBody)
                    .build();
        }
        return request;
    }

    public static RequestBody getRequestBody(HashMap<String, String> paramsMap) {
        paramsMap.put("v", BuildConfig.VERSION_NAME);
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            builder.add(key, paramsMap.get(key));
        }
        return builder.build();
    }
    private static String urlEncode(HashMap<String, String> params)
            throws UnsupportedEncodingException {
        StringBuffer sb2 = new StringBuffer();
        for (HashMap.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                sb2.append(entry.getKey());
                sb2.append("=");
                sb2.append(URLEncoder.encode(entry.getValue(), "utf-8").toString());
                sb2.append("&");
            }
        }
        String s = "";
        if (sb2.length() != 0) {
            s = sb2.substring(0, sb2.length() - 1);
        }
        return s;
    }

}
