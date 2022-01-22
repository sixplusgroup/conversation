package finley.gmair.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jd.open.api.sdk.response.im.ChatSession;
import finley.gmair.common.TimeCommon;
import finley.gmair.util.JingdongApiHelper;
import finley.gmair.util.TimeUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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


    // 每日凌晨0点触发
    @Scheduled(cron = "0 0 0 * * ?")
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
                        .forEach(this::sendKafka));
    }

    private void sendKafka(JSONObject chatLog) {

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
