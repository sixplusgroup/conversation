package finley.gmair.netty;

import com.alibaba.fastjson.JSONArray;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class GMRepository {
    private Map<String, ChannelHandlerContext> cache = new HashMap<>();

    @Cacheable("cache")
    public GMRepository push(String key, ChannelHandlerContext value) {
        cache.put(key, value);
        return this;
    }

    @Cacheable("cache")
    public ChannelHandlerContext retrieve(String key) {
        return cache.get(key);
    }

    @Cacheable("cache")
    public GMRepository remove(String key) {
        cache.remove(key);
        return this;
    }

    public ResultData list() {
        ResultData result = new ResultData();
        Iterator it = cache.entrySet().iterator();
        JSONArray ja = new JSONArray();
        while (it.hasNext()) {
            ja.add(((Map.Entry<Integer, String>) it.next()).getKey());
        }
        if (ja.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        result.setData(ja);
        return result;
    }
}
