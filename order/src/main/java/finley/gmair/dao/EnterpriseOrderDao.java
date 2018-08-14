package finley.gmair.dao;

import finley.gmair.model.order.EnterpriseOrder;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface EnterpriseOrderDao {
    ResultData insert(EnterpriseOrder enterpriseOrder);

    ResultData insertBatch(List<EnterpriseOrder> list);

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
