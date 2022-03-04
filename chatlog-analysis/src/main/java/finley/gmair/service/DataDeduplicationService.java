package finley.gmair.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.netflix.discovery.converters.Auto;
import finley.gmair.dao.SessionMessageDOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataDeduplicationService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private SessionMessageDOMapper sessionMessageDOMapper;

    public Map<Integer, String> deduplicate(Map<Integer, String> messageIdContent) {
        Map<Integer, String> analyzedIdRes = new HashMap<>();
        Map<Integer, String> toAnalyseIdContent = new HashMap<>();
        messageIdContent.forEach((id, content) -> {
            String redisKey = getRedisKey(content);
            if (redisCheckHasKey(redisKey)) analyzedIdRes.put(id, redisGet(redisKey));
            else toAnalyseIdContent.put(id, content);
        });
        storeAnalyzedRes(analyzedIdRes);
        return toAnalyseIdContent;
    }

    private void storeAnalyzedRes(Map<Integer, String> idRes) {
        sessionMessageDOMapper.updateSentimentAnalysis(idRes);
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
