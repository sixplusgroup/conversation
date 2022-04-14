package finley.gmair.service;

import finley.gmair.dao.SessionMessageDOMapper;
import finley.gmair.dto.chatlog.KafkaMessage;
import finley.gmair.dto.chatlog.RedisMessage;
import finley.gmair.enums.chatlog.SentimentLabel;
import finley.gmair.model.chatlog.Message;
import finley.gmair.util.RedisUtil;
import finley.gmair.util.TemplateMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DataDeduplicationService {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TemplateMessageUtil templateMessageUtil;

    @Resource
    private SessionMessageDOMapper sessionMessageDOMapper;

    public List<Message> deduplicate(List<KafkaMessage> messageList) {
        List<Message> analyzedMessages = new ArrayList<>();
        List<Message> toAnalyzeMessages = new ArrayList<>();
        messageList.forEach(kafkaMessage -> {
            System.out.println(kafkaMessage);
            String redisKey = redisUtil.getRedisKeyForMessageContent(kafkaMessage.getContent());
            if (templateMessageUtil.isTemplate(kafkaMessage.getContent()))
                analyzedMessages.add(new Message()
                        .setId(kafkaMessage.getMessageId())
                        .setContent(kafkaMessage.getContent())
                        .setLabel(SentimentLabel.NEUTRAL)
                        .setScore(0));
            if (redisUtil.redisCheckHasKey(redisKey)) {
                redisUtil.refreshExpireTime(redisKey, 1, TimeUnit.DAYS);
                System.out.println("redis hit!!!!!!");
                RedisMessage redisMessage = redisUtil.redisGet(redisKey);
                analyzedMessages.add(new Message()
                        .setId(kafkaMessage.getMessageId())
                        .setContent(kafkaMessage.getContent())
                        .setScore(redisMessage.getScore())
                        .setLabel(redisMessage.getLabel()));
            } else toAnalyzeMessages.add(new Message()
                    .setId(kafkaMessage.getMessageId())
                    .setContent(kafkaMessage.getContent()));
        });
        if (analyzedMessages.size() > 0) storeAnalyzedRes(analyzedMessages);
        return toAnalyzeMessages;
    }

    private void storeAnalyzedRes(List<Message> messages) {
        sessionMessageDOMapper.batchStoreMessagesAnalysisRes(messages);
    }

}
