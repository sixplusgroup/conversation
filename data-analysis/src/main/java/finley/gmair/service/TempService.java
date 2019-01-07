package finley.gmair.service;

import finley.gmair.model.dataAnalysis.TempHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface TempService {

    ResultData insertBatchHourly(List<TempHourly> list);

    ResultData fetchHourly(Map<String, Object> condition);

}
