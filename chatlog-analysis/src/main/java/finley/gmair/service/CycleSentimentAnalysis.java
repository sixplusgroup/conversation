package finley.gmair.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import finley.gmair.bert.BertProperties;
import finley.gmair.dto.chatlog.KafkaMessage;
import finley.gmair.model.chatlog.Message;
import org.apache.ibatis.annotations.Param;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Controller
@EnableKafka
@ResponseBody
public class CycleSentimentAnalysis {

    @Resource
    KafkaTemplate<Object, Object> kafkaTemplate;

    //    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

//    @Value("kafka.topics.to-bert")
//    String topicToAnalysis;
//
//    @Value("kafka.topics.after-bert")
//    String topicAfterAnalysis;

    @Autowired
    private DataDeduplicationService dataDeduplicationService;

    public void analyze(int sessionId, List<KafkaMessage> messageList) {
        List<Message> deduplicatedMessages =
                dataDeduplicationService.deduplicate(messageList);
        deduplicatedMessages.forEach(message -> kafkaTemplate.send("${kafka.topics.to-bert}", JSON.toJSONString(message)));
    }

    @KafkaListener(id = "consumer-after-bert", idIsGroup = false, topics = "${kafka.topics.after-bert}")
    public void afterAnalyze(ConsumerRecord<String, String> record) {
        List<Message> messages = JSON.parseObject(record.value(), new TypeReference<ArrayList<Message>>() {
        });
//        Message message = JSON.parseObject(record.value(), Message.class);
//        System.out.println(message);
        for (Message mes : messages) {
            System.out.println(mes.toString());
        }
        threadPoolTaskExecutor.execute(() -> {
            store(messages);
        });
    }

    private void store(List<Message> messages) {
    }


    // -----------------------------------TEST-----------------------------------------

    @GetMapping("/api/kafka")
    public String sendKafkaTest(@Param("data") String data) {
        kafkaTemplate.send("kafka_demo", data);
        return "done";
    }

    @GetMapping("/api/consumer")
    public String consumer() throws IOException {
        kafkaTemplate.send("kafka_demo1", JSON.toJSONString(new Message().setMessageId(100).setContent("test有病呀！！！！")));
        kafkaTemplate.send("kafka_demo1", JSON.toJSONString(new Message().setMessageId(101).setContent("test气死了气死了")));
        kafkaTemplate.send("kafka_demo1", JSON.toJSONString(new Message().setMessageId(102).setContent("test哼讨厌")));
        kafkaTemplate.send("kafka_demo1", JSON.toJSONString(new Message().setMessageId(100).setContent("test吾问无为谓哇哇哇哇哇哇哇哇哇哇哇哇")));
        kafkaTemplate.send("kafka_demo1", JSON.toJSONString(new Message().setMessageId(101).setContent("test哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈")));
        kafkaTemplate.send("kafka_demo1", JSON.toJSONString(new Message().setMessageId(102).setContent("test开心开心")));
        String exe = "D:\\anaconda\\python.exe";
        String command = "E:\\毕设\\BERT_Chinese_Classification-master\\consumer-kafka.py ";
//        System.out.println(JSON.toJSONString(new BertProperties()));
        String[] commands = new String[]{exe, command, JSON.toJSONString(new BertProperties())};
        final Process process = Runtime.getRuntime().exec(commands);
        PythonOutStream error = new PythonOutStream(process.getErrorStream());
        PythonOutStream output = new PythonOutStream(process.getInputStream());
        error.start();
        output.start();
        return "done";
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