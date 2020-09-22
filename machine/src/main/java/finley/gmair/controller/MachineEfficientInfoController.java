package finley.gmair.controller;

import finley.gmair.pool.MachinePool;
import finley.gmair.service.MachineEfficientInfoService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Bright Chan
 * @date: 2020/9/22 9:26
 * @description: MachineEfficientInfoController
 */

@RestController
@RequestMapping("/machine/efficientInfo")
public class MachineEfficientInfoController {

    private Logger logger = LoggerFactory.getLogger(MachineEfficientInfoController.class);

    @Autowired
    private MachineEfficientInfoService machineEfficientInfoService;

    @PostMapping("/update/daily")
    public ResultData efficientInfoDailyUpdate() {
        //avoid exception: read timed out at Timing service side.
        MachinePool.getMachinePool().execute(() -> {
            ResultData res = machineEfficientInfoService.dailyUpdate();
            if (res.getResponseCode() != ResponseCode.RESPONSE_OK) {
                logger.error("daily update: efficient info. failed!");
            }
        });
        return new ResultData();
    }

    @PostMapping("/update/hourly")
    public ResultData efficientInfoHourlyUpdate() {
        //avoid exception: read timed out at Timing service side.
        MachinePool.getMachinePool().execute(() -> {
            ResultData res = machineEfficientInfoService.hourlyUpdate();
            if (res.getResponseCode() != ResponseCode.RESPONSE_OK) {
                logger.error("hourly update: efficient info. failed!");
            }
        });
        return new ResultData();
    }
}
