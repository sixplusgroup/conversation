package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("mqtt-logger")
public interface MqttLoggerService {

    @GetMapping("/logger/list/{machineId}")
    ResultData list(@RequestParam("machineId") String machineId);
}
