package finley.gmair.service;

import finley.gmair.model.machine.MachinePowerDaily;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachinePowerService {
    ResultData create(MachinePowerDaily machinePowerDaily);

    ResultData fetch(Map<String, Object> condition);

    ResultData handleMachinePowerStatusDaily();

}
