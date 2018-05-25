package finley.gmair.service;

import finley.gmair.model.machine.v2.MachineStatus;
import finley.gmair.util.ResultData;
import org.springframework.cache.annotation.CachePut;

public interface MachineStatusCacheService {

    @CachePut(value = "machineStatus", key = "#machineStatus.uid")
    default void generate(MachineStatus machineStatus){};

    ResultData fetch(String uid);

}
