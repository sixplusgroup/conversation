package finley.gmair.service;

import finley.gmair.model.enterpriseselling.MerchantProfile;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MerchantProfileService {
    ResultData create(MerchantProfile merchantProfile);

    ResultData fetch(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
