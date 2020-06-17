package finley.gmair.dao;

import finley.gmair.util.ResultData;
import finley.gmair.model.payment.ReturnInfo;
import java.util.Map;

public interface ReturnInfoDao {

    ResultData insert(ReturnInfo returnInfo);

    ResultData query(Map<String, Object> condition);

}
