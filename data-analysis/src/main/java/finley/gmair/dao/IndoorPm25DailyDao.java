package finley.gmair.dao;

import finley.gmair.model.analysis.IndoorPm25Hourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface IndoorPm25DailyDao {

    ResultData query(Map<String, Object> condition);

    ResultData insertBatch(List<IndoorPm25Hourly> list);

}
