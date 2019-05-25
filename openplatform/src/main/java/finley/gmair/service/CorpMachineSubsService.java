package finley.gmair.service;

import finley.gmair.model.openplatform.MachineSubscription;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface CorpMachineSubsService {

    ResultData create(MachineSubscription machineSubscription);

    ResultData fetch(Map<String, Object> condition);
}
