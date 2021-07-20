package finley.gmair.dao;

import finley.gmair.util.ResultData;

public interface MachineStatusRedisDao {
    ResultData queryHourlyStatus();
}
