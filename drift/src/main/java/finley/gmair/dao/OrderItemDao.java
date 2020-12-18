package finley.gmair.dao;

import finley.gmair.model.drift.DriftOrderItem;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface OrderItemDao {

    ResultData queryOrderItem(Map<String, Object> condition);

    ResultData insertOrderItem(DriftOrderItem item);

    ResultData insertOrderItemWithId(DriftOrderItem item);

    ResultData updateOrderItem(DriftOrderItem item);

    ResultData deleteOrderItem(String itemId);
}
