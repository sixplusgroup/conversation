package finley.gmair.service;

import finley.gmair.model.analysis.TempHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface TempService {

    ResultData insertBatchHourly(List<TempHourly> list);

    ResultData fetchHourly(Map<String, Object> condition);

    ResultData insertBatchDaily(List<TempHourly> list);

    ResultData fetchDaily(Map<String, Object> condition);

}
