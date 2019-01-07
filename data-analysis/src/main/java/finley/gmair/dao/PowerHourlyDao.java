package finley.gmair.dao;

import finley.gmair.model.dataAnalysis.PowerHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface PowerHourlyDao {

    ResultData query(Map<String, Object> condition);

    ResultData insertBatch(List<PowerHourly> list);

}
