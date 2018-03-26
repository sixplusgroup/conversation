package gmair.finley.dao;



import finley.gmair.model.order.Commodity;
import finley.gmair.util.ResultData;


import java.util.Map;

/**
 * Created by hushe on 2018/1/8.
 */
public interface CommodityDao {
    ResultData query(Map<String, Object> condition);
    ResultData insert(Commodity commodity);
    ResultData update(Commodity commodity);
}
