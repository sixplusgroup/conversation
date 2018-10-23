package finley.gmair.dao;

import finley.gmair.model.drift.Equipment;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface EquipmentDao {
    ResultData insert(Equipment equipment);

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
