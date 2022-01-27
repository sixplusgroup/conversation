package finley.gmair.service;

import com.alibaba.fastjson.JSONObject;
import com.jd.open.api.sdk.response.im.ChatSession;
import finley.gmair.common.JingdongCommon;
import finley.gmair.common.TimeCommon;
import finley.gmair.dao.JdUserDOMapper;
import finley.gmair.dao.JdWaiterDOMapper;
import finley.gmair.dao.SessionDOMapper;
import finley.gmair.dao.UserSessionDOMapper;
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

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    JdUserDOMapper userDOMapper;

    @Resource
    JdWaiterDOMapper waiterDOMapper;

    @Resource
    SessionDOMapper sessionDOMapper;

    @Resource
    UserSessionDOMapper userSessionDOMapper;


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
                        .map(this::storeUserSession)
                        .forEach(this::sendKafka));
    }

    private JSONObject storeUserSession(JSONObject chatLog) {
        int usrId = userDOMapper.getUserIdByName(chatLog.remove(JingdongCommon.KEY_USER_NAME).toString());
        int waiterId = waiterDOMapper.getWaiterIdByName(chatLog.remove(JingdongCommon.KEY_WAITER_NAME).toString());
        int sessionId = sessionDOMapper.insertSession(chatLog.remove(JingdongCommon.KEY_SESSION_ID).toString());
        String productId = chatLog.remove(JingdongCommon.KEY_PRODUCT_ID).toString();
        userSessionDOMapper.insertUserSession(usrId, waiterId, sessionId, productId);

        // 剩下sessionId + chatMessages
        chatLog.put(JingdongCommon.KEY_SESSION_ID, sessionId);
        return chatLog;
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

    private void sendKafka(JSONObject chatLog) {
        kafkaTemplate.send("topic", chatLog.toJSONString());
    }

}
