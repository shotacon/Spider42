package top.shotacon.application.spider;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.SystemDefaultCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class UrlUtil {

    public static boolean proxy = true;
    public static String proxyHost = "127.0.0.1";
    public static int proxyPort = 8001;

    static String getHtml(String strUrl) throws Exception {
        String str = null;
        if (proxy) {
            SystemDefaultCredentialsProvider credentialsProvider = new SystemDefaultCredentialsProvider();
            credentialsProvider.setCredentials(new AuthScope(proxyHost, proxyPort),
                    new UsernamePasswordCredentials("", ""));
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultCredentialsProvider(credentialsProvider).setSSLSocketFactory(ssl()).build();

            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
            HttpGet getMethod = new HttpGet(strUrl);
            getMethod.setConfig(config);
            CloseableHttpResponse rsp = httpClient.execute(getMethod);
            str = EntityUtils.toString(rsp.getEntity());
        } else {
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(ssl()).build();
            HttpGet getMethod = new HttpGet(strUrl);
            CloseableHttpResponse rsp = httpClient.execute(getMethod);
            str = EntityUtils.toString(rsp.getEntity());
        }

        return str;
    }

    private static SSLConnectionSocketFactory ssl()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            // 信任所有
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        return sslsf;
    }

    /**
     * 获取链接，并且处理异常
     *
     * @param url 待获取的链接
     * @return
     */
    static Document getDocByUrl(String url) {
        Connection connect = Jsoup.connect(url);
        try {
            return connect.get();
        } catch (IOException e) {

        }
        return null;
    }
}
