package finley.gmair.service;

import finley.gmair.model.machine.MachineStatus;
import org.springframework.cache.annotation.CachePut;

public interface MachineStatusCacheService {

    @CachePut(value = "machineStatus", key = "#machineStatus.uid", condition = "#machineStatus != null")
    default MachineStatus generate(MachineStatus machineStatus) {
        return machineStatus;
    }

    MachineStatus fetch(String uid);
}
