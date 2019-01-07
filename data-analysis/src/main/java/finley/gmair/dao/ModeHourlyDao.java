package finley.gmair.dao;

import finley.gmair.model.dataAnalysis.ModeHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface ModeHourlyDao {

    ResultData query(Map<String, Object> condition);

    ResultData insertBatch(List<ModeHourly> list);

}
