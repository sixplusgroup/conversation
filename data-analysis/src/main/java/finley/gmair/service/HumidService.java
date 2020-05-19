package finley.gmair.service;

import finley.gmair.model.analysis.HumidHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface HumidService {

    ResultData insertBatchHourly(List<HumidHourly> list);

    ResultData fetchHourly(Map<String, Object> condition);

    ResultData insertBatchDaily(List<HumidHourly> list);

    ResultData fetchDaily(Map<String, Object> condition);

}
