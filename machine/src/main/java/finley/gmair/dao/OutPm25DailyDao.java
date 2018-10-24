package finley.gmair.dao;

import finley.gmair.model.machine.OutPm25Daily;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface OutPm25DailyDao {
    ResultData insert(OutPm25Daily outPm25Daily);

    ResultData query(Map<String, Object> condition);

}
