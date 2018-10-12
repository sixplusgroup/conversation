package finley.gmair.netty;

import com.alibaba.fastjson.JSON;
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
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class GMRepository {
    private Map<String, ChannelHandlerContext> cache;

    @Autowired
    private LogService logService;

    public GMRepository() {
        super();
//        cache = ExpiringMap.builder()
//                .expiration(2, TimeUnit.MINUTES)
//                .expirationPolicy(ExpirationPolicy.ACCESSED)
//                .expirationListener((key, ctx) -> CorePool.getCleanPool().submit(() -> (((ChannelHandlerContext) ctx).close())))
//                .build();
        cache = new ConcurrentHashMap<>();
    }

    public GMRepository push(String key, ChannelHandlerContext value) {
        if (cache.get(key) != value && !StringUtils.isEmpty(cache.get(key))) {
            cache.get(key).close();
        }
        cache.put(key, value);
        return this;
    }

    public ChannelHandlerContext retrieve(String key) {
        return cache.get(key);
    }

    public GMRepository remove(ChannelHandlerContext ctx) {
        for (String key : cache.keySet()) {
            if (cache.get(key).equals(ctx)) {
                CorePool.getLogExecutor().execute(() -> logService.createMachineComLog(key, "Lose connection", new StringBuffer("Client: ").append(key).append(" of 2nd version loses connection to server").toString(), ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress()));
                cache.remove(key);
                break;
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

    public ResultData isOnline(String machineId) {
        ResultData result = new ResultData();
        if (cache.containsKey(machineId)) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription(new StringBuffer("Machine: ").append(machineId).append(" is online at the moment.").toString());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("Machine: ").append(machineId).append(" is not available at the moment").toString());
        }
        return result;
    }

    public ResultData onlineList(String machineIdList) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(machineIdList)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        JSONArray array = new JSONArray();
        try {
            array = JSON.parseArray(machineIdList);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        if (array.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        }

        JSONArray onlineList = new JSONArray();
        for (Object machineId : array) {
            if (cache.containsKey((String) machineId)) {
                onlineList.add(machineId);
            }
        }
        if (onlineList.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        result.setData(onlineList);
        return result;
    }
}
