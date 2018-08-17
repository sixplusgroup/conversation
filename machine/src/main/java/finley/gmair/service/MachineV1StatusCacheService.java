package finley.gmair.service;

import finley.gmair.model.machine.v1.MachineStatus;
import org.springframework.cache.annotation.CachePut;

public interface MachineV1StatusCacheService {
    @CachePut(value = "machineV1Status", key = "#machineV1Status.uid", condition = "#machineV1Status != null")
    default MachineStatus generate(MachineStatus machineV1Status) {
        //System.out.println("时间戳:"+new Timestamp(System.currentTimeMillis())+",此方法被执行!");
        return machineV1Status;
    }

    MachineStatus fetch(String uid);
}
