package finley.gmair.service;

import finley.gmair.model.machine.ModelEfficientConfig;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ModelEfficientConfigService {
    ResultData create(ModelEfficientConfig modelEfficientConfig);

    ResultData fetch(Map<String, Object> condition);

    ResultData updateByModelId(Map<String, Object> condition);
}
