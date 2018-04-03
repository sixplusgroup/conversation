package finley.gmair.dao;

import finley.gmair.model.order.OrderChannel;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ChannelDao {
    ResultData queryChannel(Map<String, Object> condition);

    ResultData insertChannel(OrderChannel channel);

    ResultData updateChannel(OrderChannel channel);
}
