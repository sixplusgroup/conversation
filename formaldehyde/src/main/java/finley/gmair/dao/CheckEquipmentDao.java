package finley.gmair.dao;

import finley.gmair.model.formaldehyde.CheckEquipment;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface CheckEquipmentDao {

    ResultData insert(CheckEquipment checkEquipment);

    ResultData query(Map<String, Object> condition);

}
