package finley.gmair.service;

import finley.gmair.model.enterpriseselling.MerchantContact;
import finley.gmair.model.formaldehyde.CaseLngLat;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface CaseLngLatService {
    ResultData create(CaseLngLat caseLngLat);

    ResultData fetch(Map<String, Object> condition);

}
