package finley.gmair.dao;

import finley.gmair.model.machine.ModelEfficientConfig;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.FilterUpdByMQTTConfig;

import java.util.List;
import java.util.Map;

public interface ModelEfficientConfigDao {
    ResultData insert(ModelEfficientConfig modelEfficientConfig);

    ResultData query(Map<String, Object> condition);

    List<FilterUpdByMQTTConfig> queryHasFirstRemind();

    ResultData updateByModelId(Map<String, Object> condition);
}
