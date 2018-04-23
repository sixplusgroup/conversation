package finley.gmair.dao;

import finley.gmair.model.location.OrderLocationRetryCount;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface OrderLocationRetryCountDao {

    ResultData insert(OrderLocationRetryCount orderLocationRetryCount);

    ResultData insertBatch(List<OrderLocationRetryCount> list);

    ResultData query(Map<String, Object> condition);

    ResultData update(OrderLocationRetryCount orderLocationRetryCount);

    ResultData delete(Map<String, Object> condition);

}
