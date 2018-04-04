package finley.gmair.dao;

import finley.gmair.model.order.OrderItem;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface OrderItemDao {
    ResultData insert(OrderItem item, String orderId);

    ResultData query(Map<String, Object> condition);

    ResultData update(OrderItem item);
}
