package finley.gmair.controller;

import finley.gmair.service.*;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data/analysis/machine/status")
public class MachineStatusController {

    @Autowired
    private MachineStatusService machineStatusService;

    @Autowired
    private IndoorPm25Service indoorPm25Service;

    @Autowired
    private VolumeService volumeService;

    @Autowired
    private HumidService humidService;

    @Autowired
    private TempService tempService;

    @Autowired
    private Co2Service co2Service;

    @Autowired
    private PowerService powerService;

    @Autowired
    private HeatService heatService;

    @Autowired
    private ModeService modeService;

    @PostMapping("/schedule/statistical/hourly")
    public ResultData statisticalDataHourly() {
        ResultData response = machineStatusService.handleHourlyStatisticalData();
        return response;
    }

    @PostMapping("/schedule/statistical/daily")
    public ResultData statisticalDataDaily() {
        ResultData response = machineStatusService.handleDailyStatisticalData();
        return response;
    }
}
