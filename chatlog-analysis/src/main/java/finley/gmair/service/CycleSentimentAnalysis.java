package finley.gmair.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CycleSentimentAnalysis {

    @Autowired
    private DataDeduplicationService dataDeduplicationService;

    public void prepare(int sessionId, Map<Integer, String> messageIdContent) {
        Map<Integer, String> deduplicatedMessages =
                dataDeduplicationService.deduplicate(messageIdContent);

    }

    // 每日凌晨2点触发
    @Scheduled(cron = "0 15 2 * * ?")
    private void dailySentimentAnalysis(){}

    // 每3个月触发一次历史数据情感分析结果更新
    @Scheduled(cron = "0 15 2 * * ?")
    private void allSentimentAnalysis(){}

}
