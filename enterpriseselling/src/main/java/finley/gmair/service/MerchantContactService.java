package finley.gmair.service;

import finley.gmair.model.enterpriseselling.MerchantContact;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MerchantContactService {
    ResultData create(MerchantContact merchantContact);

    ResultData fetch(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
