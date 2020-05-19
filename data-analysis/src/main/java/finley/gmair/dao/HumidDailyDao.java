package finley.gmair.dao;

import finley.gmair.model.analysis.HumidHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface HumidDailyDao {

    ResultData query(Map<String, Object> condition);

    ResultData insertBatch(List<HumidHourly> list);

}
