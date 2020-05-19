package finley.gmair.dao;

import finley.gmair.model.analysis.HeatHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface HeatHourlyDao {

    ResultData query(Map<String, Object> condition);

    ResultData insertBatch(List<HeatHourly> list);

}
