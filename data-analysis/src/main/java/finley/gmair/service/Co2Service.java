package finley.gmair.service;

import finley.gmair.model.dataAnalysis.Co2Hourly;
import finley.gmair.model.dataAnalysis.HumidHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface Co2Service {

    ResultData insertBatchHourly(List<Co2Hourly> list);

    ResultData fetchHourly(Map<String, Object> condition);

}
