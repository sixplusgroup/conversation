package finley.gmair.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jd.open.api.sdk.response.im.ChatSession;
import finley.gmair.common.JingdongCommon;
import finley.gmair.common.TimeCommon;
import finley.gmair.dao.JdUserDOMapper;
import finley.gmair.dao.JdWaiterDOMapper;
import finley.gmair.dao.SessionMessageDOMapper;
import finley.gmair.dao.UserSessionDOMapper;
import finley.gmair.model.chatlog.KafkaRecordCommon;
import finley.gmair.util.JingdongApiHelper;
import finley.gmair.util.TimeUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.*;

@Service
@Log4j
public class ChatLogFetchService {

    @Autowired
    JingdongApiHelper jingdongApiHelper;

    @Resource
    KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    JdUserDOMapper userDOMapper;

    @Resource
    JdWaiterDOMapper waiterDOMapper;

    @Resource
    UserSessionDOMapper userSessionDOMapper;

    @Resource
    SessionMessageDOMapper sessionMessageDOMapper;


    // 每日凌晨0点触发
    @Scheduled(cron = "0 15 2 * * ?")
    public void fetch() {
        Timestamp current = TimeUtil.getTodayZeroTimestamp();
        String endTime = TimeCommon.DATE_FORMAT.format(current.getTime());
        String beginTime = TimeCommon.DATE_FORMAT.format(current.getTime() - TimeCommon.ONE_DAY_MILLIS);

        jingdongApiHelper.querySessionList(beginTime, endTime)
                .ifPresent(chatSessionPage -> chatSessionPage
                        .getChatSessionList()
                        .stream()
                        .map(ChatSession::getCustomer)
                        .map(customer -> jingdongApiHelper.queryChatLog(beginTime, endTime, customer))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(jingdongApiHelper::formatChatLog)
                        .map(this::storeAndTransformForKafka)
                        .forEach(this::sendKafka));
    }

    private JSONObject storeAndTransformForKafka(JSONObject chatLog) {
        // store user
        int usrId = userDOMapper.getUserIdByName(chatLog.getString(JingdongCommon.KEY_USER_NAME));

        // store waiter
        int waiterId = waiterDOMapper.getWaiterIdByName(chatLog.getString(JingdongCommon.KEY_WAITER_NAME));

        // store session
        String productId = chatLog.getString(JingdongCommon.KEY_PRODUCT_ID);
        String originalSessionId = chatLog.getString(JingdongCommon.KEY_SESSION_ID);
        int sessionId = userSessionDOMapper.insertUserSession(originalSessionId, usrId, waiterId, productId);

        // store message and transform to map list:(messageId, content)
        JSONArray rawMessages = chatLog.getJSONArray(JingdongCommon.KEY_CHAT_MESSAGES);
        JSONArray messageIdContent = storeAndTransformMessage(rawMessages);

        // prepare for kafka
        JSONObject sessionIdAndMessageListJson = new JSONObject();
        sessionIdAndMessageListJson.put(KafkaRecordCommon.KEY_INT_SESSION_ID, sessionId);
        sessionIdAndMessageListJson.put(KafkaRecordCommon.KEY_JSON_ARRAY_MESSAGES, messageIdContent);

        return sessionIdAndMessageListJson;
    }

    private JSONArray storeAndTransformMessage(JSONArray messages) {
        JSONArray messageIdContentJsonArray = new JSONArray();
        messages.stream()
                .map(Object::toString)
                .map(JSON::parseObject)
                .forEach(message -> {
                    String content = message.getString(JingdongCommon.KEY_CONTENT);
                    int messageId = sessionMessageDOMapper.insertSessionMessage(
                            message.getInteger(JingdongCommon.KEY_SESSION_ID),
                            message.getString(JingdongCommon.KEY_CONTENT),
                            message.getBoolean(JingdongCommon.KEY_IS_FROM_WAITER),
                            message.getLong(JingdongCommon.KEY_TIMESTAMP));
                    JSONObject item = new JSONObject();
                    item.put(KafkaRecordCommon.KEY_INT_MESSAGE_ID, messageId);
                    item.put(KafkaRecordCommon.KEY_STRING_MESSAGE_CONTENT, content);
                    messageIdContentJsonArray.add(item);
                });
        return messageIdContentJsonArray;
    }

    private void sendKafka(JSONObject sessionIdAndMessageListJson) {
        kafkaTemplate.send("test", sessionIdAndMessageListJson.toJSONString());
    }


    private void diskStore(String chatLog, String path) {
        try {
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(path, true)));
            out.write(chatLog + "\n");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
