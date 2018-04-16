package finley.gmair.dao;

import finley.gmair.model.consumer.Phone;
import finley.gmair.util.ResultData;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface PhoneDao {
    @Transactional
    ResultData insert(Phone phone, final String consumerId);

    ResultData query(Map<String, Object> condition);
}
