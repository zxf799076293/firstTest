package com.linhuiba.business.connector;


import android.os.Build;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Administrator on 2017/3/14.
 */

public class SSLSocketFactoryCompat extends SSLSocketFactory {
    public SSLSocketFactory defaultFactory;
    // 在4.4及以下不支持TLS协议的解决方法  OKHttp不包括自己的SSL / TLS库，因此它只是使用由Android提供的标准的SSLSocket。
    //什么TLS版本支持（并启用）取决于所使用的Andr​​oid版本。
    //在一些手机TLS 1.2支持，但默认不启用。在这种情况下，你可以通过实施内部使用默认的自定义包装SSLSocketFactory的启用它的SSLSocketFactory和每一个Socket调用 setEnabledProtocols（新的String {“TLS1.2”}）的被创建。
    // Android 5.0+ (API level21) provides reasonable default settings
    // but it still allows SSLv3
    // https://developer.android.com/about/versions/android-5.0-changes.html#ssl

    // Android 5.0+（API level21）提供了最佳的默认设置
    //但它仍然采用SSLv3
    // https://developer.android.com/about/versions/android-5.0-changes.html#ssl
    static String protocols[] = null, cipherSuites[] = null;
    static {
        try {
            SSLSocket socket = (SSLSocket)SSLSocketFactory.getDefault().createSocket();
            if (socket != null) {
                /* set reasonable protocol versions
                // - enable all supported protocols (enables TLSv1.1 and TLSv1.2 on Android <5.0)
                // - remove all SSL versions (especially SSLv3) because they're insecure now
                * 设置合理的协议版本
                  启用所有支持的协议（在Android <5.0上启用TLSv1.1和TLSv1.2）
                  删除所有SSL版本（特别是SSLv3），因为它们现在不安全
                */

                List<String> protocols = new LinkedList<>();
                for (String protocol : socket.getSupportedProtocols())
                    if (!protocol.toUpperCase().contains("SSL"))
                        protocols.add(protocol);
                SSLSocketFactoryCompat.protocols = protocols.toArray(new String[protocols.size()]);
                /* set up reasonable cipher suites
                * 设置合理的密码套件
                * */
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    /* choose known secure cipher suites
                    选择已知的安全密码套件*/
                    List<String> allowedCiphers = Arrays.asList(
                            // TLS 1.2
                            "TLS_RSA_WITH_AES_256_GCM_SHA384",
                            "TLS_RSA_WITH_AES_128_GCM_SHA256",
                            "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
                            "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
                            "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
                            "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
                            "TLS_ECHDE_RSA_WITH_AES_128_GCM_SHA256",
                            // maximum interoperability
                            "TLS_RSA_WITH_3DES_EDE_CBC_SHA",
                            "TLS_RSA_WITH_AES_128_CBC_SHA",
                            // additionally
                            "TLS_RSA_WITH_AES_256_CBC_SHA",
                            "TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA",
                            "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
                            "TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
                            "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA");
                    List<String> availableCiphers = Arrays.asList(socket.getSupportedCipherSuites());
                    // take all allowed ciphers that are available and put them into preferredCiphers 采取所有可用的密码并将其放入preferredCiphers
                    HashSet<String> preferredCiphers = new HashSet<>(allowedCiphers);
                    preferredCiphers.retainAll(availableCiphers);
                    /* For maximum security, preferredCiphers should *replace* enabled ciphers (thus disabling
                     * ciphers which are enabled by default, but have become unsecure), but I guess for
                     * the security level of DAVdroid and maximum compatibility, disabling of insecure
                     * ciphers should be a server-side task */
                    /*为了最大限度的安全性，PreferredCipher应该*替换*启用的密码（因此禁用
                      *默认启用的密码，但已变得不安全），但我想
                      * DAVdroid的安全级别和最大兼容性，禁用不安全
                      *密码应该是服务器端任务*/
                    // add preferred ciphers to enabled ciphers 添加首选密码到启用的密码
                    HashSet<String> enabledCiphers = preferredCiphers;
                    enabledCiphers.addAll(new HashSet<>(Arrays.asList(socket.getEnabledCipherSuites())));
                    SSLSocketFactoryCompat.cipherSuites = enabledCiphers.toArray(new String[enabledCiphers.size()]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public SSLSocketFactoryCompat(X509TrustManager tm) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, (tm != null) ? new X509TrustManager[] { tm } : null, null);
            defaultFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new AssertionError(); // The system has no TLS. Just give up. 该系统没有TLS
        }
    }
    private void upgradeTLS(SSLSocket ssl) {
        // Android 5.0+ (API level21) provides reasonable default settings
        // but it still allows SSLv3
        // https://developer.android.com/about/versions/android-5.0-changes.html#ssl
        if (protocols != null) {
            ssl.setEnabledProtocols(protocols);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && cipherSuites != null) {
            ssl.setEnabledCipherSuites(cipherSuites);
        }
    }
    @Override
    public String[] getDefaultCipherSuites() {
        return cipherSuites;
    }
    @Override
    public String[] getSupportedCipherSuites() {
        return cipherSuites;
    }
    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        Socket ssl = defaultFactory.createSocket(s, host, port, autoClose);
        if (ssl instanceof SSLSocket)
            upgradeTLS((SSLSocket)ssl);
        return ssl;
    }
    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        Socket ssl = defaultFactory.createSocket(host, port);
        if (ssl instanceof SSLSocket)
            upgradeTLS((SSLSocket)ssl);
        return ssl;
    }
    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        Socket ssl = defaultFactory.createSocket(host, port, localHost, localPort);
        if (ssl instanceof SSLSocket)
            upgradeTLS((SSLSocket)ssl);
        return ssl;
    }
    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        Socket ssl = defaultFactory.createSocket(host, port);
        if (ssl instanceof SSLSocket)
            upgradeTLS((SSLSocket)ssl);
        return ssl;
    }
    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        Socket ssl = defaultFactory.createSocket(address, port, localAddress, localPort);
        if (ssl instanceof SSLSocket)
            upgradeTLS((SSLSocket)ssl);
        return ssl;
    }
}