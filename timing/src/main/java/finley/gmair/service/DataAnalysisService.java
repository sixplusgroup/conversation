package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("mode-agent")
public interface MachineModeFeignService {
    @PostMapping("/machine/mode/hourly/power-saving")
    ResultData handleHourlyPowerSaving();
}
