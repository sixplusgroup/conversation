package finley.gmair.dao;

import finley.gmair.model.machine.BoundaryPM2_5;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface BoundaryPM2_5Dao  {
    ResultData insert(BoundaryPM2_5 boundaryPM2_5);

    ResultData query(Map<String, Object> condition);

    ResultData updateByModelId(Map<String, Object> condition);
}
