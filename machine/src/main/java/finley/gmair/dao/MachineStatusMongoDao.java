package finley.gmair.dao;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineStatusMongoDao {


    ResultData query(Map<String, Object> condition);

    ResultData queryHourlyPm25();

    ResultData queryPartialLatestPm25(String uid,String name);

    ResultData queryPartialAveragePm25();

    ResultData queryMachineV1Status(Map<String, Object> condition);
}
