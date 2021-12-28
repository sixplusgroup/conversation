package finley.gmair.handler;

import finley.gmair.model.result.PullResult;
import finley.gmair.model.domain.UnifiedShop;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 0:14
 * @description ：
 */

@Component
public class SkuItemPullHandlerContext {
    private static final Map<Integer, AbstractSkuItemPullHandler<?>> HANDLER_MAP = new HashMap<>();

    public static void registerHandler(int platform, AbstractSkuItemPullHandler<?> handler) {
        HANDLER_MAP.put(platform, handler);
    }

    public PullResult handlePull(UnifiedShop shop) {
        AbstractSkuItemPullHandler<?> handler = HANDLER_MAP.get(shop.getPlatform());
        if (null == handler) {
            throw new IllegalArgumentException("no handler for platform " + shop.getPlatform());
        }
        return handler.handlePull(shop);
    }
}
