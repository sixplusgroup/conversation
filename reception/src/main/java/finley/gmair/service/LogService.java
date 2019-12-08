package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("log-agent")
public interface LogService {
    @PostMapping("/log/useraction/create")
    ResultData createUserMachineOperationLog(@RequestParam("userId") String userId, @RequestParam("machineValue") String machineValue, @RequestParam("component") String component, @RequestParam("logDetail") String logDetail, @RequestParam("ip") String ip, @RequestParam("actionValue") String actionValue);

    @PostMapping("/log/userlog/create")
    ResultData createUserAccountOperationLog(@RequestParam("userId") String userId, @RequestParam("component") String component, @RequestParam("logDetail") String logDetail, @RequestParam("ip") String ip);
}
