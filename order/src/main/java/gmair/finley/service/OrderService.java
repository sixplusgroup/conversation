package gmair.finley.service;

import finley.gmair.model.order.OrderChannel;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface OrderService {
    ResultData fetchPlatformOrderChannel(Map<String, Object> condition);

    ResultData createPlatformOrderChannel(OrderChannel channel);

    ResultData updatePlatformOrderChannel(OrderChannel channel);
}
