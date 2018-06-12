package finley.gmair.dao;

import finley.gmair.model.machine.MachinePm2_5;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface MachineStatusStatisticsDao {
    ResultData insertHourlyBatch(List<MachinePm2_5> list);
    ResultData selectHourly(Map<String, Object> condition);
    ResultData insertDailyBatch(List<MachinePm2_5> list);
    ResultData selectDaily(Map<String, Object> condition);
    ResultData insertMonthlyBatch(List<MachinePm2_5> list);
    ResultData selectMonthly(Map<String, Object> condition);
}
