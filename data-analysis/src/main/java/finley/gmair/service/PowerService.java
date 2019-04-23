package finley.gmair.service;

import finley.gmair.model.dataAnalysis.PowerHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface PowerService {

    ResultData insertBatchHourly(List<PowerHourly> list);

    ResultData fetchHourly(Map<String, Object> condition);

    ResultData insertBatchDaily(List<PowerHourly> list);

    ResultData fetchDaily(Map<String, Object> condition);
}
