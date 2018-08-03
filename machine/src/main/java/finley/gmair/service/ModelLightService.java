package finley.gmair.service;

import finley.gmair.model.machine.ModelLight;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ModelLightService {
    ResultData create(ModelLight modelLight);

    ResultData fetch(Map<String, Object> condition);

    ResultData updateByModelId(Map<String, Object> condition);
}
