package finley.gmair.config;

import finley.gmair.model.auth.VerificationCode;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        Map<String, Long> expires = new HashMap<>();
        expires.put("serials", 300L);
        rcm.setExpires(expires);
        /*
        cacheManager.setUsePrefix(false）时，存入redis时有一个维护key的列表
        cacheManager.setUsePrefix(true）时，开启使用前缀，将会用冒号隔离，而不使用keys维护key值
         */
        rcm.setUsePrefix(true);
        return rcm;
    }

    @Bean
    public RedisTemplate<Object, VerificationCode> redisTemplate(
            RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, VerificationCode> template = new RedisTemplate<Object, VerificationCode>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<VerificationCode> verificationCodeJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<VerificationCode>(VerificationCode.class);
        template.setDefaultSerializer(verificationCodeJackson2JsonRedisSerializer);
        return template;
    }



}