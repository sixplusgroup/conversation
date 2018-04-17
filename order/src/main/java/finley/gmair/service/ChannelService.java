package finley.gmair.service;

import finley.gmair.model.order.OrderChannel;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ChannelService {
    ResultData fetchChannel(Map<String, Object> condition);

    ResultData createChannel(OrderChannel channel);

    ResultData reviseChannel(OrderChannel channel);
}
