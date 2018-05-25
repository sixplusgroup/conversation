package finley.gmair.dao;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineStatusMongoDao {


    ResultData query(Map<String, Object> condition);

}
