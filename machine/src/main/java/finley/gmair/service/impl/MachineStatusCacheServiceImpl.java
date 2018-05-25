package finley.gmair.service.impl;

import finley.gmair.service.MachineStatusCacheService;
import finley.gmair.util.ResultData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class MachineStatusCacheServiceImpl implements MachineStatusCacheService{

    @Cacheable("machineStatus")
    public ResultData fetch(String uid) {
        System.out.println("fetch : " + uid);
        ResultData resultData = new ResultData();
        // get data from mongo
        return resultData;
    }
}
