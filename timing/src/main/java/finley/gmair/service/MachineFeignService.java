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

    @GetMapping("/machine/status/partial/schedule/hourly")
    ResultData probePartialPM2_5Hourly();

    @PostMapping("/machine/status/screen/schedule/daily")
    ResultData configScreenDaily();
}
