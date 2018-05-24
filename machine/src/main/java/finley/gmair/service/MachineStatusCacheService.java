package finley.gmair.service;

import finley.gmair.model.machine.v2.MachineLiveStatus;
import finley.gmair.util.ResultData;
import org.springframework.cache.annotation.CachePut;

public interface MachineStatusCacheService {

    @CachePut(value = "machineStatus", key = "#machineStatus.uid")
    default void generate(MachineLiveStatus machineStatus){};

    ResultData fetch(String uid);

}
