package finley.gmair.util;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import finley.gmair.dto.chatlog.RedisMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Autowired
    RedisTemplate<String, String> redisTemplate;


    public boolean redisCheckHasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public RedisMessage redisGet(String key) {
        return JSON.parseObject(redisTemplate.opsForValue().get(key), RedisMessage.class);
    }

    public void redisSet(String key, String value, long expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
    }

    public void refreshExpireTime(String key, long expireTime, TimeUnit timeUnit) {
        redisTemplate.expire(key, expireTime, timeUnit);
    }

    public String getRedisKeyForMessageContent(String origin) {
        return "sa_message_" + DigestUtil.md5Hex(origin);
    }

}
