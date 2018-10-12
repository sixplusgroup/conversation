package finley.gmair.dao;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface CommodityDao {
    ResultData queryCommodity(Map<String, Object> condition);
}
