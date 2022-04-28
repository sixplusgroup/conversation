package finley.gmair.service;

import finley.gmair.dao.SessionMessageDOMapper;
import finley.gmair.dao.UserSessionDOMapper;
import finley.gmair.dto.chatlog.KafkaSession;
import finley.gmair.dto.chatlog.RedisMessage;
import finley.gmair.enums.chatlog.SentimentLabel;
import finley.gmair.model.chatlog.Message;
import finley.gmair.util.RedisUtil;
import finley.gmair.util.SessionAnalysisStatisticUtil;
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

    @Autowired
    private SessionAnalysisStatisticUtil statisticUtil;

    @Resource
    private SessionMessageDOMapper sessionMessageDOMapper;

    @Resource
    private UserSessionDOMapper sessionDOMapper;


    public void deduplicate(KafkaSession session) {
        // 会话消息列表
        List<Message> messageList = session.getMessages();

        // 缓存有分析记录的消息，存入数据库
        List<Message> analyzedMessages = new ArrayList<>();

        // 需要分析的新消息
        List<Message> toAnalyzeMessages = new ArrayList<>();

        messageList.forEach(kafkaMessage -> {
            String redisKey = redisUtil.getRedisKeyForMessageContent(kafkaMessage.getContent());

            // 模板消息过滤，设置label=neutral，score=0
            if (templateMessageUtil.isTemplate(kafkaMessage.getContent())) {
                analyzedMessages.add(kafkaMessage
                        .setLabel(SentimentLabel.NEUTRAL)
                        .setScore(0));
            }
            // redis击中，完成session统计值更新
            else if (redisUtil.redisCheckHasKey(redisKey)) {
                redisUtil.refreshExpireTime(redisKey, 1, TimeUnit.DAYS);
                RedisMessage redisMessage = redisUtil.redisGet(redisKey);
                System.out.println("\n\nredis hit:" + redisMessage.toString());
                kafkaMessage.setLabel(redisMessage.getLabel());
                kafkaMessage.setScore(redisMessage.getScore());
                analyzedMessages.add(kafkaMessage);
            }
            // 剩余加入待分析消息列表
            else {
                toAnalyzeMessages.add(kafkaMessage);
            }
        });
        // session更新：将redis击中的message转化到统计值中，未击中的保留在message列表里由bert转化统计
        if (analyzedMessages.size() > 0) {
            storeAnalyzedRes(analyzedMessages);
            statisticUtil.refreshSessionStatistics(session, analyzedMessages);
            session.setMessages(toAnalyzeMessages);
        }

    }

    private void storeAnalyzedRes(List<Message> messages) {
        sessionMessageDOMapper.batchStoreMessagesAnalysisRes(messages);
    }

}
