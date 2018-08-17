package finley.gmair.dao;

import finley.gmair.model.enterpriseselling.MerchantProfile;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MerchantProfileDao {
    ResultData insert(MerchantProfile merchantProfile);

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
