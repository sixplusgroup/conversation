package finley.gmair.dao;

import finley.gmair.model.analysis.ModeHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface ModeDailyDao {

    ResultData query(Map<String, Object> condition);

    ResultData insertBatch(List<ModeHourly> list);

}
