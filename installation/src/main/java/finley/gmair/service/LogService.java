package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("log-agent")
public interface LogService {

    ResultData createModuleLog(@RequestParam("eventStatus") String eventStatus, @RequestParam("module") String module, @RequestParam("logDetail") String logDetail, @RequestParam("ip") String ip);
}
