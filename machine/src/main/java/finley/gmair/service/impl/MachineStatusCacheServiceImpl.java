package finley.gmair.service.impl;

import finley.gmair.model.machine.v2.MachineLiveStatus;
import finley.gmair.service.MachineStatusCacheService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class MachineStatusCacheServiceImpl implements MachineStatusCacheService{

    @Cacheable("machineStatus")
    public ResultData fetch(String uid) {
        System.out.println("fetch : " + uid);
        ResultData resultData = new ResultData();
        try {
            resultData.setData(machineStatusMap.get(uid));
        } catch (Exception e) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return resultData;
    }
}
