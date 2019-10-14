package finley.gmair.dao;

import finley.gmair.model.drift.DriftOrder;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface OrderDao {

    ResultData insertOrder(DriftOrder order);

    ResultData queryOrder(Map<String, Object> condition);

    ResultData queryOrderPanel(Map<String, Object> condition);

    ResultData updateOrder(DriftOrder order);

    ResultData deleteOrder(String orderId);
}
