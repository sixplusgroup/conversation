package finley.gmair.dao;

import finley.gmair.model.machine.Machine_on_off;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineOnOffDao {
    ResultData insert(Machine_on_off machine_on_off);

    ResultData query(Map<String, Object> condition);

    ResultData update(Machine_on_off machine_on_off);
}
