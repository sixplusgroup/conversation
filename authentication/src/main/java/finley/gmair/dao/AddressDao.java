package finley.gmair.dao;

import finley.gmair.model.consumer.Address;
import finley.gmair.util.ResultData;
import org.apache.ibatis.annotations.Param;

public interface AddressDao {
    ResultData insert(@Param("address") Address address, @Param("consumerId")String consumerId);
}
