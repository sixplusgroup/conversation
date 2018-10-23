package finley.gmair.dao;

import finley.gmair.model.drift.EquipActivity;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface EquipActivityDao {
    ResultData query(Map<String, Object> condition);

    ResultData insert(EquipActivity equipActivity);
}
