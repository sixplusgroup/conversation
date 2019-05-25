package finley.gmair.dao;

import finley.gmair.model.openplatform.MachineSubscription;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface CorpMachineSubsDao {

    ResultData insert(MachineSubscription machineSubscription);

    ResultData query(Map<String, Object> condition);
}
