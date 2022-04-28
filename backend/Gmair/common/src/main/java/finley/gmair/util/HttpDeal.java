package finley.gmair.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class HttpDeal {
    public static String getResponse(String url) {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet get = new HttpGet(new URI(url));
            HttpResponse response = httpClient.execute(get);
            String userJson = EntityUtils.toString(response.getEntity());
            return userJson;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getResponse(String url, Map<String, String> header) {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet get = new HttpGet(new URI(url));
            if (header.size() > 0) {
                for (String key : header.keySet()) {
                    get.setHeader(key, header.get(key));
                }
            }
            HttpResponse response = httpClient.execute(get);
            String userJson = EntityUtils.toString(response.getEntity());
            return userJson;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String postJSONResponse(String url, JSONObject param) {
        try {

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(2000) // 设置连接超时时间，单位毫秒
                    .setConnectionRequestTimeout(1000)
                    .setSocketTimeout(5000) // 请求获取数据的超时时间，单位毫秒
                    .build();
            HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
                @Override
                public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
                    return false;
                }
            };
            CloseableHttpClient client = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setRetryHandler(myRetryHandler)
                    .build();

            try {
                StringEntity paramEntity = new StringEntity(param.toString(), "UTF-8"); // 对参数进行编码
                paramEntity.setContentType("application/json; charset=utf-8");

                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(paramEntity);
                httpPost.setConfig(requestConfig);

                CloseableHttpResponse response = client.execute(httpPost);

                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    String responseStr = EntityUtils.toString(entity, "UTF-8");
                    if (responseStr == null || responseStr.trim().length() == 0) {
                        responseStr = "{}";
                    }
                    return responseStr;
                }
                response.close();
            } finally {
                client.close();
            }
            return null;
        } catch (Exception e) {

        }
        return null;
    }

    public static String postJSONResponse(String url, JSONObject param, Map<String, String> header) {
        try {

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(2000) // 设置连接超时时间，单位毫秒
                    .setConnectionRequestTimeout(1000)
                    .setSocketTimeout(5000) // 请求获取数据的超时时间，单位毫秒
                    .build();
            HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
                @Override
                public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
                    return false;
                }
            };
            CloseableHttpClient client = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setRetryHandler(myRetryHandler)
                    .build();

            try {
                StringEntity paramEntity = new StringEntity(param.toString(), "UTF-8"); // 对参数进行编码
                paramEntity.setContentType("application/json; charset=utf-8");

                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(paramEntity);
                if (header.size() > 0) {
                    for (String key : header.keySet()) {
                        httpPost.setHeader(key, header.get(key));
                    }
                }
                httpPost.setConfig(requestConfig);

                CloseableHttpResponse response = client.execute(httpPost);

                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    String responseStr = EntityUtils.toString(entity, "UTF-8");
                    if (responseStr == null || responseStr.trim().length() == 0) {
                        responseStr = "{}";
                    }
                    return responseStr;
                }
                response.close();
            } finally {
                client.close();
            }
            return null;
        } catch (Exception e) {

        }
        return null;
    }
}