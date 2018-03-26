package gmair.finley.dao;


import finley.gmair.model.order.OrderDiversion;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * Created by hushe on 2018/1/22.
 */
public interface OrderDiversionDao {
    ResultData query(Map<String, Object> condition);

    ResultData insert(OrderDiversion orderDiversion);

    ResultData update(OrderDiversion orderDiversion);

    ResultData delete(String diversionId);
}
