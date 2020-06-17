package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("log-agent")
public interface LogService {

    @PostMapping("/log/adminlog/create")
    ResultData createAdminAccountOperationLog(@RequestParam("userId") String userId, @RequestParam("component") String component, @RequestParam("logDetail") String logDetail, @RequestParam("ip") String ip);
}
