package finley.gmair.service;


import finley.gmair.model.order.EnterpriseOrder;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface EnterpriseOrderService {
    ResultData create(EnterpriseOrder enterpriseOrder);

    ResultData createBatch(List<EnterpriseOrder> list);

    ResultData fetch(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
