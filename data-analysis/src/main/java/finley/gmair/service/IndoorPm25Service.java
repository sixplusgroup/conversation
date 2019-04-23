package finley.gmair.service;

import finley.gmair.model.dataAnalysis.IndoorPm25Hourly;
import finley.gmair.model.dataAnalysis.V1MachineStatusHourly;
import finley.gmair.model.dataAnalysis.V2MachineStatusHourly;
import finley.gmair.model.machine.MachinePowerDaily;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface IndoorPm25Service {

    ResultData insertBatchHourly(List<IndoorPm25Hourly> list);

    ResultData fetchHourly(Map<String, Object> condition);

    ResultData insertBatchDaily(List<IndoorPm25Hourly> list);

    ResultData fetchDaily(Map<String, Object> condition);
}
