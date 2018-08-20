package finley.gmair.dao;

import finley.gmair.model.mode.MachineMode;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineModeDao {
    ResultData insert(MachineMode machineMode);

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
