package finley.gmair.service;

import finley.gmair.model.dataAnalysis.HeatHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface HeatService {

    ResultData insertBatchHourly(List<HeatHourly> list);

    ResultData fetchHourly(Map<String, Object> condition);

}
