package finley.gmair.utils;

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

    public String redisGet(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void redisSet(String key, String value, long expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
    }


}
