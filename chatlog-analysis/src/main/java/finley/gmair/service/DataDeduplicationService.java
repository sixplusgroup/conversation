package finley.gmair.service;

import cn.hutool.crypto.digest.DigestUtil;
import finley.gmair.dao.SessionMessageDOMapper;
import finley.gmair.dto.chatlog.KafkaMessage;
import finley.gmair.model.chatlog.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataDeduplicationService {
    //    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private SessionMessageDOMapper sessionMessageDOMapper;

    public List<Message> deduplicate(List<KafkaMessage> messageList) {
        List<Message> analyzedMessages = new ArrayList<>();
        List<Message> toAnalyzeMessages = new ArrayList<>();
        messageList.forEach(message -> {
            String redisKey = getRedisKey(message.getContent());
            if (redisCheckHasKey(redisKey))
                analyzedMessages.add(new Message()
                        .setMessageId(message.getMessageId())
                        .setContent(message.getContent())
                        .setScore(Double.parseDouble(redisGet(redisKey))));
            else toAnalyzeMessages.add(new Message()
                    .setMessageId(message.getMessageId())
                    .setContent(message.getContent()));
        });
        storeAnalyzedRes(analyzedMessages);
        return toAnalyzeMessages;
    }

    private void storeAnalyzedRes(List<Message> messages) {
//        sessionMessageDOMapper.updateSentimentAnalysis(idRes);
    }

    private boolean redisCheckHasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    private String redisGet(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    private String getRedisKey(String origin) {
        return "sa_message_" + DigestUtil.md5Hex(origin);
    }


}
