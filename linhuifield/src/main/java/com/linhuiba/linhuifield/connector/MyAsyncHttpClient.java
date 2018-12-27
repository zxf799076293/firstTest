package com.linhuiba.linhuifield.connector;

import java.security.cert.CertificateException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/7/22.
 */
public class MyAsyncHttpClient {
    private static OkHttpClient okHttpClient;
    public static OkHttpClient MyAsyncHttpClient() {
        return getClient();
    }
    public static OkHttpClient MyAsyncHttpClient_version_two() {
        return getClient();
    }
    public static OkHttpClient MyAsyncHttpClient_version_three() {
        return getClient();
    }
    public static OkHttpClient MyAsyncHttpClient2() {
        return getClient();
    }
    public static OkHttpClient MyAsyncHttpClient3() {
        return getClient();
    }
    public static OkHttpClient MyAsyncHttpClient4() {
        return getClient();
    }

    public static OkHttpClient getClient(){
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            try {
                // 自定义一个信任所有证书的TrustManager，添加SSLSocketFactory的时候要用到
                final X509TrustManager trustAllCert =
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        };
                final SSLSocketFactory sslSocketFactory = new FieldSSLSocketFactoryCompat(trustAllCert);
                builder.sslSocketFactory(sslSocketFactory, trustAllCert);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

}
