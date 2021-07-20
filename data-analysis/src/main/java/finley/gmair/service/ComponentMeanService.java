package finley.gmair.service;

import finley.gmair.model.analysis.ComponentMean;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface ComponentMeanService {

    ResultData insertBatchDaily(List<ComponentMean> list);

    ResultData fetchDaily(Map<String, Object> condition);
}
