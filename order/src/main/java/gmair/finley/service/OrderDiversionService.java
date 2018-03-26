package gmair.finley.service;


import finley.gmair.model.order.OrderDiversion;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * Created by hushe on 2018/1/22.
 */
public interface OrderDiversionService {
    ResultData fetch(Map<String, Object> condition);

    ResultData create(OrderDiversion orderDiversion);

    ResultData modify(OrderDiversion orderDiversion);

    ResultData delete(String diversionId);
}
