package finley.gmair.service;

import finley.gmair.model.analysis.HeatHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface HeatService {

    ResultData insertBatchHourly(List<HeatHourly> list);

    ResultData fetchHourly(Map<String, Object> condition);

    ResultData insertBatchDaily(List<HeatHourly> list);

    ResultData fetchDaily(Map<String, Object> condition);
}
