package finley.gmair.dao;

import finley.gmair.model.drift.DriftAddress;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AddressDao {
    ResultData query(Map<String, Object> condition);

    ResultData insert(DriftAddress address);

    ResultData update(DriftAddress address);

    ResultData block(String addressId);
}
