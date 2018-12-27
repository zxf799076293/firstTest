package com.linhuiba.business.network;


import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Created by snowd on 15/3/26.
 */
public class Response {

    static Coder coder;
//    {
//        "code": 0,
//        "msg": "",
//        "result":{
//        }
//    }
    public int code;
    public String msg;
    public Object data;
    public int total;
    public String detailScore;

    public static Response parseResponse(String response, boolean resultArray) throws BabyHttpResponseException {

        //从服务器获取数据时，可能含有bom。
        if ( response != null ){
            int index =  response.indexOf("{");
            response = response.substring(index);
        }

        JSONObject json = JSONObject.parseObject(response);
//        HttpResponse resp = HttpResponse.parse(json);
//        if (resp == null) throw new BabyHttpResponseException(null);
//        if (resp.data == null) throw new BabyHttpResponseException(resp.msg, resp.code);
        Response babyResponse = new Response();
        if (json.get("total") != null) {
            babyResponse.total = json.getIntValue("total");
        }
        if (json.get("detailScore") != null) {
            babyResponse.detailScore = json.getString("detailScore");
        }

        babyResponse.code = json.getIntValue("code");
        babyResponse.msg = json.getString("msg");
        String result = json.getString("result");
        if (TextUtils.isEmpty(result) || (!result.contains("{") && !result.contains("["))) {
            babyResponse.data = result;
        } else {
            if (babyResponse.code == 1) {
                babyResponse.data = resultArray || (result.startsWith("[") && result.endsWith("]"))
                        ? json.getJSONArray("result")
                        : json.getJSONObject("result");
            } else {
                babyResponse.data = result;
            }
        }
        return babyResponse;
    }

    public static Response parseResponse(byte[] bytedata, boolean resultArray)
            throws UnsupportedEncodingException, BabyHttpResponseException {
        return parseResponse(new String(bytedata, "UTF-8"), resultArray);
    }

    public String getDataValue(String valueKey) {
        if (data == null) throw new LinhuiResponseException(msg, code);
        String value;
        if (data instanceof JSONObject) {
            value = ((JSONObject) data).getString(valueKey);
        } else {
            value = getDataValue(valueKey, null).toString();
        }
        if (TextUtils.isEmpty(value)) return null;
        return coder != null ? coder.decode(value) : value;
    }

    public Object getDataValue(String valueKey, Class clazz) {
        if (TextUtils.isEmpty(valueKey)) throw new NullPointerException("valueKey is null");
        if (data == null) throw new LinhuiResponseException(msg, code);
        if (!data.getClass().equals(clazz)) return null;
        try {
            Method method = clazz != null
                    ? clazz.getMethod("get"
                    + new String(new char[]{valueKey.charAt(0)}).toUpperCase(Locale.US)
                    + (valueKey.length() > 1 ? valueKey.substring(1) : ""))
                    : data.getClass().getMethod("get"
                    + new String(new char[]{valueKey.charAt(0)}).toUpperCase(Locale.US)
                    + (valueKey.length() > 1 ? valueKey.substring(1) : ""));
            Object value = method.invoke(data);
            return value;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Coder getCoder() {
        return coder;
    }

    public static String decode(String str) {
        return coder.decode(str);
    }

    public static String encode(String source) {
        return coder.decode(source);
    }

//    static class HttpResponse {
//
//        public String code;
//        public String msg;
//        public boolean success;
//        public JSONObject data;
//
//        public static HttpResponse parse(JSONObject json) throws BabyHttpResponseException {
//            String code = json.getString("code");
//            boolean success = json.getBoolean("success");
//            if (!TextUtils.isEmpty(code)) {
//                HttpResponse resp = new HttpResponse();
//                resp.code = code;
//                resp.success = success;
//                resp.msg = json.getString("errmsg");
//                resp.data = json.getJSONObject("result");
//                return resp;
//            } else if(!success) {
//                throw new BabyHttpResponseException(code);
//            }
//            return null;
//        }
//    }

    public static class BabyHttpResponseException extends Exception {
        public String code;
        BabyHttpResponseException(String msg, String code) {
            super(msg);
            this.code = code;
        }
        BabyHttpResponseException(String code) {
            this.code = code;
        }
    }

    public static class LinhuiResponseException extends RuntimeException {
        public int code;
        LinhuiResponseException(String msg, int code) {
            super(msg);
            this.code = code;
        }
    }

    public static boolean isAccesstokenError(Throwable throwable) {
        if (throwable != null && throwable instanceof LinhuiResponseException) {
            if (((LinhuiResponseException) throwable).code == -99) {
                return true;
            }
        }
        return false;
    }
    public static boolean isFieldinfo_NoResources(Throwable throwable,int code) {
        if (throwable != null && throwable instanceof LinhuiResponseException) {
            if (((LinhuiResponseException) throwable).code == code) {
                return true;
            }
        }
        return false;
    }

}