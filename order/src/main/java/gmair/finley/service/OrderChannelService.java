package gmair.finley.service;

import finley.gmair.model.order.OrderChannel;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface OrderChannelService {

    ResultData create(OrderChannel orderChannel);

    ResultData fetchOrderChannel(Map<String, Object> condition);

    ResultData modifyOrderChannel(OrderChannel orderChannel);

    ResultData deleteOrderChannel(String channelId);
}
