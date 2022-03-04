package finley.gmair.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.chatlog.KafkaRecordCommon;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableKafka
public class ChatLogListenService {

    @Autowired
    private CycleSentimentAnalysis cycleSentimentAnalysis;


    @KafkaListener(id = "chat-log-listener", topics = "test")
    public void listenChatLog(ConsumerRecord<String, String> record) {
        JSONObject recordJson = JSON.parseObject(record.value());
        int sessionId = recordJson.getInteger(KafkaRecordCommon.KEY_INT_SESSION_ID);
        Map<Integer, String> messageIdContentMap
                = recordJson.getJSONArray(KafkaRecordCommon.KEY_JSON_ARRAY_MESSAGES)
                .toJavaList(JSONObject.class)
                .stream()
                .collect(Collectors.toMap(o -> o.getInteger(KafkaRecordCommon.KEY_INT_MESSAGE_ID),
                        o -> o.getString(KafkaRecordCommon.KEY_STRING_MESSAGE_CONTENT)));

        cycleSentimentAnalysis.prepare(sessionId, messageIdContentMap);
    }

}
