package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("log-agent")
public interface LogService {
    @PostMapping("/log/machinecom/create")
    ResultData createMachineComLog(@RequestParam("uid") String uid, @RequestParam("action") String action, @RequestParam("logDetail") String logDetail, @RequestParam("ip") String ip);
}
