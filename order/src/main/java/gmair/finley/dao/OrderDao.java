package gmair.finley.dao;

import finley.gmair.util.ResultData;
import gmair.finley.model.GmairOrder;

public interface OrderDao {
    ResultData insert(GmairOrder order);
}
