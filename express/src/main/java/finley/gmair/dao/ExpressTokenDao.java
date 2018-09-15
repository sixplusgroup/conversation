package finley.gmair.dao;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface ExpressTokenDao {

    ResultData query(Map<String, Object> condition);

}
