package finley.gmair.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.request.im.ImPopSessionlistGetRequest;
import com.jd.open.api.sdk.response.im.ChatSessionPage;
import finley.gmair.common.JingdongCommon;
import finley.gmair.common.TimeCommon;
import finley.gmair.config.JingDongProperties;
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
import java.util.List;
import java.util.Optional;

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
     * @param endTime 结束时间
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
            return Optional.of(executeUrl(getQueryChatLogUrl(beginTime, endTime, customer)));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 查询用户聊天记录API调用URL拼接
     *
     */
    private String getQueryChatLogUrl(String beginTime, String endTime, String customer) {
        JSONObject object = new JSONObject();
        object.put("startTime", beginTime);
        object.put("endTime", endTime);
        object.put("customer", customer);
        String timeStamp = TimeCommon.DATE_FORMAT.format(new Date());
        return JingdongCommon.BASE_URL
                + "?360buy_param_json=" + object
                + "&access_token=" + properties.getAccessToken()
                + "&app_key=" + properties.getAppKey()
                + "&method=" + JingdongCommon.GET_CHAT_LOG_METHOD
                + "&v=" + JingdongCommon.VERSION
                + "&timestamp=" + timeStamp + "&sign=D70825340F4084360B9362B60DFD7930";
    }

    /**
     * 执行拼装好的api调用url，返回结果
     *
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
     * @param chatLog
     * @return 返回处理后的json格式聊天记录信息
     */
    public JSONObject formatChatLog(String chatLog) {
        JSONObject chatLogJson = JSON.parseObject(chatLog);
        JSONObject response=chatLogJson.getJSONObject("jingdong_im_pop_chatlog_get_responce");
        JSONObject chatLogPage=response.getJSONObject("ChatLogPage");
        int totalRecord=chatLogPage.getInteger("totalRecord");
        JSONArray chatLogList=chatLogPage.getJSONArray("chatLogList");
        for(Object chatLogItem:chatLogList){
            // TODO 没想好
        }
        return null;

    }
}
