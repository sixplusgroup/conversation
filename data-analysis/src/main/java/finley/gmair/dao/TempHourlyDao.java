package finley.gmair.dao;

import finley.gmair.model.dataAnalysis.TempHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface TempHourlyDao {

    ResultData query(Map<String, Object> condition);

    ResultData insertBatch(List<TempHourly> list);

}
