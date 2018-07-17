package finley.gmair.service;

import finley.gmair.model.machine.MachineStatus;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.sql.Timestamp;

@Cacheable
public interface MachineStatusCacheService {

    @CachePut(value = "machineStatus", key = "#machineStatus.uid", condition = "#machineStatus != null")
    default MachineStatus generate(MachineStatus machineStatus) {
        //System.out.println("时间戳:"+new Timestamp(System.currentTimeMillis())+",此方法被执行!");
        return machineStatus;
    }

    MachineStatus fetch(String uid);
}
