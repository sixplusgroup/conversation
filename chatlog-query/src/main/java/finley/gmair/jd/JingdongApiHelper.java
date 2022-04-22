package finley.gmair.jd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.request.im.ImPopSessionlistGetRequest;
import com.jd.open.api.sdk.response.im.ChatSessionPage;
import finley.gmair.common.JingdongCommon;
import finley.gmair.common.TimeCommon;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class JingdongApiHelper {

    JdClient client;
    JingDongProperties properties;

    @Autowired
    public JingdongApiHelper(JingDongProperties properties) {
        client = new DefaultJdClient(
                properties.getServerUrl(),
                properties.getAccessToken(),
                properties.getAppKey(),
                properties.getAppSecret());
        this.properties = properties;
    }


    /**
     * 获取指定时间段内的会话列表页面
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 会话列表页面
     */
    public Optional<ChatSessionPage> querySessionList(String beginTime, String endTime) {
        try {
            ImPopSessionlistGetRequest request = new ImPopSessionlistGetRequest();
            request.setStartTime(TimeCommon.DATE_FORMAT.parse(beginTime));
            request.setEndTime(TimeCommon.DATE_FORMAT.parse(endTime));
            return Optional.of(client.execute(request).getChatSessionPage());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    /**
     * 获得某个顾客指定时间段的聊天记录
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @param customer  用户名
     * @return 聊天记录
     */
    public Optional<String> queryChatLog(String beginTime, String endTime, String customer) {
        try {
            JSONObject object = new JSONObject();
            object.put("startTime", beginTime);
            object.put("endTime", endTime);
            object.put("customer", customer);
            return Optional.of(executeUrl(getUrl(object, JingdongCommon.CHATLOG_GET_API)));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * URL拼接
     */
    private String getUrl(JSONObject object, String method) {
        String timeStamp = TimeCommon.DATE_FORMAT.format(new Date());
        return JingdongCommon.BASE_URL
                + "?360buy_param_json=" + object
                + "&access_token=" + properties.getAccessToken()
                + "&app_key=" + properties.getAppKey()
                + "&method=" + method
                + "&v=" + JingdongCommon.VERSION
                + "&timestamp=" + timeStamp + "&sign=D70825340F4084360B9362B60DFD7930";
    }

    /**
     * 执行拼装好的api调用url，返回结果
     */
    private String executeUrl(String url_string) throws Exception {
        URL url = new URL(url_string);
        URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String responseContent = EntityUtils.toString(entity, JingdongCommon.DEFAULT_CHARSET);
        response.close();
        httpClient.close();
        return responseContent;
    }

    /**
     * 将聊天记录信息转换为指定格式
     *
     * @param chatLog 聊天记录
     * @return 返回处理后的json格式聊天记录信息
     */
    public JSONObject formatChatLog(String chatLog) {
        AtomicReference<String> sid = new AtomicReference<>();
        AtomicReference<String> customer = new AtomicReference<>();
        AtomicReference<String> waiter = new AtomicReference<>();
        AtomicReference<String> product = new AtomicReference<>();
        AtomicReference<Long> timestamp = new AtomicReference<>();
        JSONArray chatMessages = new JSONArray();

        JSON.parseObject(chatLog)
                .getJSONObject("jingdong_im_pop_chatlog_get_responce")
                .getJSONObject("ChatLogPage")
                .getJSONArray("chatLogList")
                .stream()
                .map(Object::toString)
                .map(JSON::parseObject)
                .forEach(chatLogItem -> {
                    // 第一条message的时间作为会话时间
                    System.out.println(timestamp.get());
                    if (timestamp.get() == null) timestamp.set(chatLogItem.getLongValue("time"));
                    if (!chatLogItem.getString("content").equals("")) {
                        JSONObject item = new JSONObject();
                        sid.set(chatLogItem.getString("sid"));
                        customer.set(chatLogItem.getString("customer"));
                        waiter.set(chatLogItem.getString("waiter"));
                        product.set(chatLogItem.getString("skuId"));
                        item.put(JingdongCommon.KEY_CONTENT, chatLogItem.getString("content"));
                        item.put(JingdongCommon.KEY_IS_FROM_WAITER, chatLogItem.getBooleanValue("waiterSend"));
                        item.put(JingdongCommon.KEY_TIMESTAMP, chatLogItem.getLongValue("time"));
                        chatMessages.add(item);
                    }
                });

        JSONObject resChatLog = new JSONObject();
        resChatLog.put(JingdongCommon.KEY_SESSION_ID, sid.get());
        resChatLog.put(JingdongCommon.KEY_USER_NAME, customer.get());
        resChatLog.put(JingdongCommon.KEY_WAITER_NAME, waiter.get());
        resChatLog.put(JingdongCommon.KEY_PRODUCT_ID, product.get());
        resChatLog.put(JingdongCommon.KEY_TIMESTAMP, timestamp.get());
        resChatLog.put(JingdongCommon.KEY_CHAT_MESSAGES, chatMessages);
        return resChatLog;

    }
}
