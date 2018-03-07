package finley.gmair.dao;

import finley.gmair.model.consumer.Phone;
import finley.gmair.util.ResultData;
import org.apache.ibatis.annotations.Param;

public interface PhoneDao {
    ResultData insert(@Param("phone")Phone phone, @Param("consumerId") String consumerId);
}
