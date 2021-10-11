package finley.gmair.dao;

import finley.gmair.model.machine.MachinePowerDaily;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface MachinePowerDailyDao {
    ResultData insert(MachinePowerDaily machinePowerDaily);

    ResultData query(Map<String, Object> condition);

    ResultData insertDailyBatch(List<MachinePowerDaily> list);

}
