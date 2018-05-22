package finley.gmair.service.impl;

import finley.gmair.model.machine.MachineStatus;
import finley.gmair.service.MachineStatusCacheService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class MachineStatusCacheServiceImpl implements MachineStatusCacheService{

    private Map<String, MachineStatus> machineStatusMap = new ConcurrentHashMap<>();


    @Cacheable("machineStatus")
    public ResultData generate(MachineStatus machineStatus) {
        ResultData resultData = new ResultData();
        try {
            machineStatusMap.put(machineStatus.getUid(), machineStatus);
        } catch (Exception e) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return resultData;
    }

    @Cacheable("machineStatus")
    public ResultData fetch(String uid) {
        ResultData resultData = new ResultData();
        try {
            resultData.setData(machineStatusMap.get(uid));
        } catch (Exception e) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return resultData;
    }
}
