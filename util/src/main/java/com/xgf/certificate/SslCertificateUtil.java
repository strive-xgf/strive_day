package com.xgf.certificate;

import com.xgf.common.LogUtil;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author strive_day
 * @create 2023-03-28 1:33
 * @description java实现SSL证书验证工具类
 */
public class SslCertificateUtil {


    /**
     * 禁用SSL验证【注意：会禁用所有SSL验证，包括恶意攻击者可能使用的验证！！】
     *
     * 暂时用于解决：使用  `java.net.URL`  或者 `org.jsoup.Jsoup` 等工具，去获取网页信息的时候，可能会因为证书原因报错
     *      javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
     *
     * todo: 如果需要验证证书或主机名，需要自行实现相应的逻辑
     */
    public static void forbiddenCheckSslCertificate() {

        try {
            // 使用SSLContext类手动设置SSL证书验证
            SSLContext sslContext = SSLContext.getInstance("SSL");
            // X509TrustManager类用于信任所有证书
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }

            }}, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            // HostnameVerifier 类用于信任所有主机名
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            LogUtil.warn("execute exception = {}", e.getLocalizedMessage(), e);
        }

    }


}
