package finley.gmair.service;

import finley.gmair.model.machine.ModelEnabledComponent;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ModelEnabledComponentService {
    ResultData create(ModelEnabledComponent modelEnabledComponent);

    ResultData fetch(Map<String, Object> condition);

}
