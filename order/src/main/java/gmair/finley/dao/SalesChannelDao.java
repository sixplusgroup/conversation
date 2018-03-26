package gmair.finley.dao;


import finley.gmair.model.order.OrderChannel;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * Created by hushe on 2017/12/16.
 */
public interface SalesChannelDao {
    ResultData insert(OrderChannel orderChannel);

    ResultData query(Map<String, Object> condition);

    ResultData update(OrderChannel orderChannel);

    ResultData delete(String channelId);
}
