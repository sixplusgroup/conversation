package finley.gmair.dao;

import finley.gmair.model.machine.ModelEfficientConfig;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ModelEfficientConfigDao {
    ResultData insert(ModelEfficientConfig modelEfficientConfig);

    ResultData query(Map<String, Object> condition);

    ResultData updateByModelId(Map<String, Object> condition);
}
