package finley.gmair.dao;

import finley.gmair.model.analysis.Co2Hourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface Co2DailyDao {

    ResultData query(Map<String, Object> condition);

    ResultData insertBatch(List<Co2Hourly> list);

}
