package finley.gmair.dao;

import finley.gmair.model.consumer.Address;
import finley.gmair.util.ResultData;
import org.springframework.transaction.annotation.Transactional;

public interface AddressDao {
    @Transactional
    ResultData insert(Address address, String consumerId);
}
