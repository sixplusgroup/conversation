package finley.gmair.netty;

import io.netty.channel.Channel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GMRepository {
    private Map<String, Channel> cache = new HashMap<>();

    @Cacheable("cache")
    public GMRepository push(String key, Channel value) {
        cache.put(key, value);
        return this;
    }

    @Cacheable("cache")
    public Channel retrieve(String key) {
        return cache.get(key);
    }

    @Cacheable("cache")
    public GMRepository remove(String key) {
        cache.remove(key);
        return this;
    }
}
