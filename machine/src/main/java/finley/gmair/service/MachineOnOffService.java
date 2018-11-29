package finley.gmair.service;

import finley.gmair.model.machine.Machine_on_off;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineOnOffService {
    ResultData create(Machine_on_off machine_on_off);

    ResultData fetch(Map<String, Object> condition);

    ResultData update(Machine_on_off machine_on_off);
}
