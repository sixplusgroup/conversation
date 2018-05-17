package finley.gmair.service;

import finley.gmair.model.air.MachineAirQuality;
import finley.gmair.util.ResultData;

import java.util.List;

public interface MachineAirQualityService {

    ResultData add(MachineAirQuality machineAirQuality);

    ResultData addBatch(List<MachineAirQuality> list);
}
