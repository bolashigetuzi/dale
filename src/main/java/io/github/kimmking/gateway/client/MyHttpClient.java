package io.github.kimmking.gateway.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class MyHttpClient {
    public static CloseableHttpClient httpClient = null;
    static {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(600);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(600);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(6000)
                .setSocketTimeout(6000)
                .setConnectTimeout(6000)
                .build();

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        httpClient = httpClientBuilder.build();
    }

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("zgq","zgq");
        try {
            doPostJson("http://127.0.0.1:8888/", JSON.toJSONString(jsonObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String doPostJson(String url, String json) throws Exception {
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            HttpEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if(status == 200) {
                entity = response.getEntity();
                return EntityUtils.toString(entity, "utf-8");
            } else {
                throw new Exception("访问异常，status：" + status + "！");
            }
        } catch (Exception e) {
            if(e instanceof HttpHostConnectException || e instanceof ConnectTimeoutException) {
                throw new Exception("连接异常，请检查URL地址或启动该服务！");
            }
            throw new Exception("连接异常！", e);
        } finally {
            try {
                if (response != null) response.close();
            } catch (IOException e) {
            }
        }
    }
}
