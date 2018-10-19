package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineMapService {
    ResultData fetchMachineMapList(Map<String, Object> condition);

}
