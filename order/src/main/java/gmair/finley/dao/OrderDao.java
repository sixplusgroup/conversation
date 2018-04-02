package gmair.finley.dao;

import finley.gmair.model.order.PlatformOrder;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface OrderDao {
    ResultData insertOrder(PlatformOrder order);

    ResultData queryOrder(Map<String, Object> condition);

    ResultData updateOrder(Map<String, Object> condition);
}
