package finley.gmair.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import finley.gmair.bert.BertProperties;
import finley.gmair.dao.SessionMessageDOMapper;
import finley.gmair.dao.TypicalSessionDOMapper;
import finley.gmair.dao.UserSessionDOMapper;
import finley.gmair.dto.chatlog.KafkaSession;
import finley.gmair.dto.chatlog.RedisMessage;
import finley.gmair.model.chatlog.Message;
import finley.gmair.model.chatlog.UserSession;
import finley.gmair.util.RedisUtil;
import finley.gmair.util.SessionAnalysisStatisticUtil;
import finley.gmair.util.TypicalSessionUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@EnableKafka
@ResponseBody
public class SentimentAnalysisService {

    @Resource
    KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    SessionMessageDOMapper messageDOMapper;

    @Autowired
    UserSessionDOMapper sessionDOMapper;

    @Autowired
    TypicalSessionDOMapper typicalSessionDOMapper;

    @Autowired
    RedisTemplate<String, String> redisTemplate;


    @Autowired
    RedisUtil redisUtil;

    @Value("${kafka.topics.to-bert}")
    String topicToAnalysis;

    @Value("${kafka.topics.after-bert}")
    String topicAfterAnalysis;

    @Autowired
    private DataDeduplicationService dataDeduplicationService;

    public void analyze(KafkaSession session) {
//        System.out.println(session);
        dataDeduplicationService.deduplicate(session);
        kafkaTemplate.send(topicToAnalysis, JSON.toJSONString(session));
    }

    @KafkaListener(id = "consumer-after-bert", idIsGroup = false, topics = "${kafka.topics.after-bert}")
    public void afterAnalyze(ConsumerRecord<String, String> record) {
        KafkaSession session = JSON.parseObject(record.value(), KafkaSession.class);
//        Message message = JSON.parseObject(record.value(), Message.class);
//        System.out.println(message);
        System.out.println("--------------------------------------------------------------------");
        System.out.println(session.toString());
        System.out.println("--------------------------------------------------------------------");
        threadPoolTaskExecutor.execute(() -> {
            List<Message> messages = session.getMessages();
            updateSessionAnalysis(session);
            updateMessageAnalysis(messages);
            storeTypicalCases(session);
        });
    }

    private void storeTypicalCases(KafkaSession session){
        if(TypicalSessionUtil.isTypicalSession(session)){
            typicalSessionDOMapper.insertTypicalSession(session);
        }
    }

    private void updateSessionAnalysis(KafkaSession session){
        SessionAnalysisStatisticUtil.refreshSessionStatistics(session, session.getMessages());
        sessionDOMapper.storeSessionAnalysisRes(session);
    }

    private void updateMessageAnalysis(List<Message> messages) {
        messageDOMapper.batchStoreMessagesAnalysisRes(messages);

        for (Message mes : messages) {
//            System.out.println(mes);
            String redisKey = redisUtil.getRedisKeyForMessageContent(mes.getContent());
            RedisMessage redisMessage = new RedisMessage().setLabel(mes.getLabel()).setScore(mes.getScore());
            redisUtil.redisSet(redisKey, JSON.toJSONString(redisMessage), 1, TimeUnit.DAYS);
        }
    }


    // -----------------------------------TEST-----------------------------------------

    @GetMapping("/api/kafka")
    public String sendKafkaTest(@Param("data") String data) {
        kafkaTemplate.send("kafka_demo", data);
        return "done";
    }

    @GetMapping("/api/consumer")
    public String consumer() throws IOException {
        kafkaTemplate.send("kafka_demo1", JSON.toJSONString(new Message().setId(100).setContent("test有病呀！！！！")));
        kafkaTemplate.send("kafka_demo1", JSON.toJSONString(new Message().setId(101).setContent("test气死了气死了")));
        kafkaTemplate.send("kafka_demo1", JSON.toJSONString(new Message().setId(102).setContent("test哼讨厌")));
        kafkaTemplate.send("kafka_demo1", JSON.toJSONString(new Message().setId(100).setContent("test吾问无为谓哇哇哇哇哇哇哇哇哇哇哇哇")));
        kafkaTemplate.send("kafka_demo1", JSON.toJSONString(new Message().setId(101).setContent("test哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈")));
        kafkaTemplate.send("kafka_demo1", JSON.toJSONString(new Message().setId(102).setContent("test开心开心")));
        String exe = "D:\\anaconda\\python.exe";
        String command = "E:\\毕设\\BERT_Chinese_Classification-master\\consumer-kafka.py ";
        String[] commands = new String[]{exe, command, JSON.toJSONString(new BertProperties()), topicToAnalysis, topicAfterAnalysis};
        final Process process = Runtime.getRuntime().exec(commands);
        PythonOutStream error = new PythonOutStream(process.getErrorStream());
        PythonOutStream output = new PythonOutStream(process.getInputStream());
        error.start();
        output.start();
        return "done";
    }

//    @GetMapping("/api/mysql")
//    public boolean mysqlTest() {
//        threadPoolTaskExecutor.execute(() ->
//                messageDOMapper.batchStoreMessagesAnalysisRes(Collections.singletonList(new Message().setId(3).setLabel(SentimentLabel.POSITIVE).setScore(4.9))));
//        System.out.println(messageDOMapper.getMessage());
//        return true;
//    }

    @GetMapping("api/redis")
    public boolean redisTest() {
        redisTemplate.opsForValue().set("test", "1111111111", 1, TimeUnit.SECONDS);
        System.out.println("get from redis:" + redisTemplate.opsForValue().get("test"));
        try {
            while (true) {
                if (redisTemplate.hasKey("test"))
                    System.out.println("get from redis:" + redisTemplate.opsForValue().get("test"));
                else {
                    System.out.println("key expired.");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    static class PythonOutStream extends Thread {
        InputStream is;

        public PythonOutStream(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                while (br.readLine() != null) {
                    continue;
                }
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }
        }
    }


}