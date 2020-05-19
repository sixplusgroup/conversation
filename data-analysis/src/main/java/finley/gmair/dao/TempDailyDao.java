package finley.gmair.dao;

import finley.gmair.model.analysis.TempHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface TempDailyDao {

    ResultData query(Map<String, Object> condition);

    ResultData insertBatch(List<TempHourly> list);

}
