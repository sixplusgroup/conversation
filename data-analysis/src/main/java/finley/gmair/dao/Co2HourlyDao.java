package finley.gmair.dao;

import finley.gmair.model.dataAnalysis.Co2Hourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface Co2HourlyDao {

    ResultData query(Map<String, Object> condition);

    ResultData insertBatch(List<Co2Hourly> list);

}
