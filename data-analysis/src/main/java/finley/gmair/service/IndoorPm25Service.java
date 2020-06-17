package finley.gmair.service;

import finley.gmair.model.analysis.IndoorPm25Hourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface IndoorPm25Service {

    ResultData insertBatchHourly(List<IndoorPm25Hourly> list);

    ResultData fetchHourly(Map<String, Object> condition);

    ResultData insertBatchDaily(List<IndoorPm25Hourly> list);

    ResultData fetchDaily(Map<String, Object> condition);
}
