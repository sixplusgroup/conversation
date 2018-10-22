package finley.gmair.service;

import finley.gmair.model.drift.EquipActivity;
import finley.gmair.model.drift.Equipment;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface EquipmentService {
    ResultData createEquipment(Equipment equipment);

    ResultData fetchEquipment(Map<String, Object> condition);

    ResultData modifyEquipment(Map<String, Object> condition);

    ResultData fetchRelationship(Map<String, Object> condition);

    ResultData createRelationship(EquipActivity equipActivity);
}
