package finley.gmair.netty;

import com.alibaba.fastjson.JSONArray;
import finley.gmair.pool.CorePool;
import finley.gmair.service.LogService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import io.netty.channel.ChannelHandlerContext;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class GMRepository {
//    private Map<String, ChannelHandlerContext> cache = new ConcurrentHashMap<>();

    private Map<String, ChannelHandlerContext> cache;

    public GMRepository() {
        super();
        cache = ExpiringMap.builder().expiration(2, TimeUnit.MINUTES).expirationPolicy(ExpirationPolicy.ACCESSED).expirationListener((key, ctx) -> CorePool.getCleanPool().submit(() -> (((ChannelHandlerContext) ctx).close()))).build();
    }

    @Autowired
    private LogService logService;

    public GMRepository push(String key, ChannelHandlerContext value) {
        cache.put(key, value);
        return this;
    }

    public ChannelHandlerContext retrieve(String key) {
        return cache.get(key);
    }

    public GMRepository remove(String key) {
        cache.remove(key);
        return this;
    }

    public GMRepository remove(ChannelHandlerContext ctx) {
        for (String key : cache.keySet()) {
            if (cache.get(key).equals(ctx)) {
                CorePool.getLogExecutor().execute(() -> logService.createMachineComLog(key, "Lose connection", new StringBuffer("Client: ").append(key).append(" of 2nd version loses connection to server").toString(), ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress()));
                return remove(key);
            }
        }
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
