package finley.gmair.dao;

import finley.gmair.model.air.MachineAirQuality;
import finley.gmair.util.ResultData;

import java.util.List;

public interface MachineAirQualityDao {

    ResultData insert(MachineAirQuality machineAirQuality);

    ResultData insertBatch(List<MachineAirQuality> list);
}
