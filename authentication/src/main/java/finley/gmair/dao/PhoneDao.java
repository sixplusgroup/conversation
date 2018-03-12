package finley.gmair.dao;

import finley.gmair.model.consumer.Phone;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface PhoneDao {
    ResultData insert(Phone phone, final String consumerId);

    ResultData query(Map<String, Object> condition);
}
