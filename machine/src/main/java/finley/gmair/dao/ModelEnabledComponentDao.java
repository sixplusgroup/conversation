package finley.gmair.dao;

import finley.gmair.model.machine.ModelEnabledComponent;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ModelEnabledComponentDao {
    ResultData insert(ModelEnabledComponent modelEnabledComponent);

    ResultData query(Map<String, Object> condition);

}
