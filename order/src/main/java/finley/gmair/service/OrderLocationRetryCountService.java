package finley.gmair.service;

import finley.gmair.model.location.OrderLocationRetryCount;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface OrderLocationRetryCountService {

    ResultData insert(OrderLocationRetryCount orderLocationRetryCount);

    ResultData insertBatch(List<OrderLocationRetryCount> list);

    ResultData fetch(Map<String, Object> condition);

    ResultData update(OrderLocationRetryCount orderLocationRetryCount);

    ResultData delete(Map<String, Object> condition);
}
