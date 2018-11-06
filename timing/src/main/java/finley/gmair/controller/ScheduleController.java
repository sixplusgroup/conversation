package finley.gmair.controller;

import finley.gmair.service.MachineFeignService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    private MachineFeignService machineFeignService;

    @RequestMapping("/probe/filter")
    public ResultData probeFilter() {
        ResultData result = new ResultData();
        new Thread(() -> {
            machineFeignService.probePartialPM2_5Hourly();
        }).start();
        return result;
    }

    @RequestMapping("/save/partial/pm25/daily")
    public ResultData savePartialPm25Daily() {
        ResultData result = new ResultData();
        new Thread(() -> {
            machineFeignService.savePartialPm25Daily();
        }).start();
        return result;
    }
}
