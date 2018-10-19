package finley.gmair.service;

import finley.gmair.model.enterpriseselling.MerchantContact;
import finley.gmair.model.formaldehyde.CheckEquipment;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface CheckEquipmentService {
    ResultData create(CheckEquipment checkEquipment);

    ResultData fetch(Map<String, Object> condition);

}
