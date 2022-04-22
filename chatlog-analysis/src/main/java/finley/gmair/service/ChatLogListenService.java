package finley.gmair.service;

import com.alibaba.fastjson.JSON;
import finley.gmair.bert.BertProcessHandler;
import finley.gmair.dto.chatlog.KafkaSession;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@EnableKafka
public class ChatLogListenService {

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private BertProcessHandler bertProcessHandler;

//    @Value("kafka.topics.fetch")
//    private String topicFetch;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    /**
     * 以session为单位，监听query传入的会话
     * 会话消息去重
     * 会话传送给bert模型，轮询bert运行状态
     *
     * @param record record
     */
    @KafkaListener(id = "consumer-fetch", idIsGroup = false, topics = "${kafka.topics.fetch}")
    public void listenChatLog(ConsumerRecord<String, String> record) {
        KafkaSession session = JSON.parseObject(record.value(), KafkaSession.class);
//        System.out.println("listen from query:" + session.toString() + "\n");
        threadPoolTaskExecutor.execute(() -> {
            sentimentAnalysisService.analyze(session);
            boolean startBert = bertProcessHandler.runBertProcess();
            System.out.println("start bert:" + startBert);
        });

    }

}
