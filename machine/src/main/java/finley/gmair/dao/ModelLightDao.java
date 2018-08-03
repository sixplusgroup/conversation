package finley.gmair.dao;

import finley.gmair.model.machine.ModelLight;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ModelLightDao {
    ResultData insert(ModelLight modelLight);

    ResultData query(Map<String, Object> condition);

    ResultData updateByModelId(Map<String, Object> condition);
}
