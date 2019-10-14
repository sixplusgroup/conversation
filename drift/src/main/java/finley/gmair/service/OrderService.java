package finley.gmair.service;

import finley.gmair.model.drift.DriftOrder;
import finley.gmair.model.drift.DriftOrderItem;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface OrderService {

    ResultData fetchDriftOrder(Map<String, Object> condition);

    ResultData fetchDriftOrderPanel(Map<String, Object> condition);

    ResultData createDriftOrder(DriftOrder order);

    ResultData updateDriftOrder(DriftOrder order);

    ResultData deleteDriftOrder(String orderId);

    ResultData cancelDriftOrder(DriftOrder order);

}
