package finley.gmair.dao;

import finley.gmair.model.consumer.Address;
import finley.gmair.util.ResultData;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface AddressDao {
    @Transactional
    ResultData insert(Address address, String consumerId);

    ResultData update(Map<String, Object> condition);

    ResultData query(Map<String, Object> condition);
}
