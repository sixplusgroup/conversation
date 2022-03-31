package finley.gmair.service;

import com.alibaba.fastjson.JSON;
import finley.gmair.bert.BertProcessHandler;
import finley.gmair.dto.chatlog.KafkaMessage;
import finley.gmair.dto.chatlog.KafkaSession;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@EnableKafka
public class ChatLogListenService {

    @Autowired
    private CycleSentimentAnalysis cycleSentimentAnalysis;

    @Autowired
    private BertProcessHandler bertProcessHandler;

//    @Value("kafka.topics.fetch")
//    private String topicFetch;

    //    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @KafkaListener(id = "consumer-fetch", idIsGroup = false, topics = "${kafka.topics.fetch}")
    public void listenChatLog(ConsumerRecord<String, String> record) {
        KafkaSession session = JSON.parseObject(record.value(), KafkaSession.class);
        int sessionId = session.getSessionId();
        List<KafkaMessage> messageList = session.getMessages();

        threadPoolTaskExecutor.execute(() -> {
            cycleSentimentAnalysis.analyze(sessionId, messageList);
            bertProcessHandler.runBertProcess();
        });

    }

}
