package com.linhuiba.linhuipublic.config;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/31.
 */

public class BaseUrlFactory {
    public static String makeupUrl(String baseUrl, RequestParameter param, ParameterEncoder encoder) {
        if (TextUtils.isEmpty(baseUrl)) return baseUrl;
        if (param != null && param.mEncodeParameters != null
                && !param.mEncodeParameters.isEmpty()) {
            Iterator<Map.Entry<String, String>> it = param.mEncodeParameters.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String plain = entry.getValue();
                if (TextUtils.isEmpty(entry.getValue())) continue;
                if (encoder != null) {
                    String encoded = encoder.encode(plain);
                    param.addParameter(entry.getKey(), encoded, false);
                } else {
                    param.addParameter(entry.getKey(), entry.getValue(), false);
                }
            }
        }
        if (param != null && param.mPlainParameters != null
                && !param.mPlainParameters.isEmpty()) {
            StringBuilder builder = new StringBuilder(baseUrl);
            Iterator<Map.Entry<String, String>> it = param.mPlainParameters.entrySet().iterator();
            while (it.hasNext()) {
                if (builder.indexOf("?") < 0) {
                    builder.append("?");
                }
                char last = builder.charAt(builder.length() - 1);
                if (last != '?' && last != '&') {
                    builder.append("&");
                }

                Map.Entry<String, String> entry = it.next();
                try {
                    builder.append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return builder.toString();
        }
        return null;
    }

    public static class RequestParameter {
        HashMap<String, String> mEncodeParameters = new HashMap<String, String>();
        HashMap<String, String> mPlainParameters = new HashMap<String, String>();

        public RequestParameter addParameter(String key, String value, boolean needEncode) {
            if (needEncode) mEncodeParameters.put(key, value);
            else mPlainParameters.put(key, value);
            return this;
        }
    }

    public interface ParameterEncoder {
        public String encode(String source);
    }
}
