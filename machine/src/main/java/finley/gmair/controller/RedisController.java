package finley.gmair.controller;

import finley.gmair.service.impl.RedisService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finley.gmair.model.machine.v1.MachineStatus;

@RestController
@RequestMapping("machine/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping("/v1/status/cacheput")
    public ResultData setV1Cache(String uid, int pm25, int tempurature, int humidity, int co2, int velocity,
                                 int power, int mode, int heat, int light) {
        ResultData result = new ResultData();
        MachineStatus machineStatus = new MachineStatus(uid, pm25, tempurature, humidity, co2, velocity, power, mode, heat, light);
        boolean flag = redisService.set(uid, machineStatus, (long) 120);
        if (flag == true) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            return result;
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
    }

    @PostMapping("/v2/status/cacheput")
    public ResultData setV2Cache(String uid, int pm2_5, int temp, int humid, int co2, int volume, int power, int mode, int heat, int light, int lock) {
        ResultData result = new ResultData();
        finley.gmair.model.machine.MachineStatus machineStatus = new finley.gmair.model.machine.MachineStatus(uid, pm2_5, temp, humid, co2, volume, power, mode, heat, light, lock);
        boolean flag = redisService.set(uid, machineStatus, (long) 120);
        if (flag == true) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            return result;
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
    }
}
