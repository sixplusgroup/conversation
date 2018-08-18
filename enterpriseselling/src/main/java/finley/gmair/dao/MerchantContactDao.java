package finley.gmair.dao;

import finley.gmair.model.enterpriseselling.MerchantContact;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MerchantContactDao {
    ResultData insert(MerchantContact merchantContact);

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
