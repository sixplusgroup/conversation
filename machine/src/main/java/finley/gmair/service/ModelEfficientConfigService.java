package finley.gmair.service;

import finley.gmair.model.machine.ModelEfficientConfig;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.FilterUpdByMQTTConfig;

import java.util.List;
import java.util.Map;

public interface ModelEfficientConfigService {
    ResultData create(ModelEfficientConfig modelEfficientConfig);

    ResultData fetch(Map<String, Object> condition);

    List<FilterUpdByMQTTConfig> fetchHasFirstRemind();

    ResultData updateByModelId(Map<String, Object> condition);
}
