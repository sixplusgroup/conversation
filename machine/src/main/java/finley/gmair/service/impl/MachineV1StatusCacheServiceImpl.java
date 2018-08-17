package finley.gmair.service.impl;

import finley.gmair.model.machine.MachineV1Status;
import finley.gmair.model.machine.v1.MachineStatus;
import finley.gmair.service.MachineV1StatusCacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;


@Service
@EnableCaching
public class MachineV1StatusCacheServiceImpl implements MachineV1StatusCacheService {

    @Cacheable(value = "machineV1Status", key = "#uid", unless = "#result == null")
    public MachineStatus fetch(String uid) {
        return null;
    }
}
