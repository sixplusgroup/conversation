package finley.gmair.service.impl;

import finley.gmair.model.machine.MachineStatus;
import finley.gmair.service.MachineStatusCacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class MachineStatusCacheServiceImpl implements MachineStatusCacheService{

    @Cacheable(value = "machineStatus", key = "#uid", unless = "#result == null")
    public MachineStatus fetch(String uid) {
        System.out.println("fetch : " + uid);
        return null;
    }
}
