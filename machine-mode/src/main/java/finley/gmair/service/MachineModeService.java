package finley.gmair.service;

import finley.gmair.model.mode.MachineMode;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineModeService {
    ResultData create(MachineMode machineMode);

    ResultData fetch(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
