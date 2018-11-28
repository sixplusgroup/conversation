package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("machine-agent")
public interface MachineFeignService {

    @PostMapping("/machine/status/schedule/hourly")
    ResultData handleMachineStatusHourly();

    @PostMapping("/machine/status/schedule/daily")
    ResultData handleMachineStatusDaily();

    @PostMapping("/machine/status/schedule/monthly")
    ResultData handleMachineStatusMonthly();

    @GetMapping("/machine/partial/status/pm25/probe/hourly")
    ResultData probePartialPM2_5Hourly();

    @PostMapping("/machine/partial/status/pm25/save/daily")
    ResultData savePartialPm25Daily();

    @PostMapping("/machine/partial/status/screen/on/daily")
    ResultData turnOnScreenDaily();

    @PostMapping("/machine/partial/status/screen/off/hourly")
    ResultData turnOffScreenHourly();

    @GetMapping("/machine/power/onoff/schedule/half/list")
    ResultData powerTurnOnOff();
}
