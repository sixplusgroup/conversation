package gmair.finley.dao;


import finley.gmair.model.order.OrderMission;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface OrderMissionDao {
	ResultData insert(OrderMission mission);

	ResultData query(Map<String, Object> condition);
}
