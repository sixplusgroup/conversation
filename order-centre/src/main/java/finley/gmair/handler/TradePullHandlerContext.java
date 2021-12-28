package finley.gmair.handler;

import com.alibaba.fastjson.JSON;
import finley.gmair.model.request.BatchPullRequest;
import finley.gmair.model.result.PullResult;
import finley.gmair.model.request.SinglePullRequest;
import finley.gmair.model.domain.UnifiedShop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-21 21:10
 * @description ：
 */

@Component
public class TradePullHandlerContext {

    private Logger logger = LoggerFactory.getLogger(TradePullHandlerContext.class);

    private static final Map<Integer, AbstractTradePullHandler<?>> HANDLER_MAP = new HashMap<>();

    public static void registerHandler(int platform, AbstractTradePullHandler<?> handler) {
        HANDLER_MAP.put(platform, handler);
    }

    public PullResult handleSinglePull(SinglePullRequest request, UnifiedShop shop) {
        logger.info("handleSinglePull,request:{},shop:{}", JSON.toJSONString(request), JSON.toJSONString(shop));
        AbstractTradePullHandler<?> handler = HANDLER_MAP.get(shop.getPlatform());
        if (null == handler) {
            throw new IllegalArgumentException("no handler for platform " + shop.getPlatform());
        }
        return handler.handleSinglePull(request, shop);
    }

    public PullResult handleBatchPull(BatchPullRequest request, UnifiedShop shop) {
        logger.info("handleBatchPull,request:{},shop:{}", JSON.toJSONString(request), JSON.toJSONString(shop));
        AbstractTradePullHandler<?> handler = HANDLER_MAP.get(shop.getPlatform());
        if (null == handler) {
            throw new IllegalArgumentException("no handler for platform " + shop.getPlatform());
        }
        return handler.handleBatchPull(request, shop);
    }
}
