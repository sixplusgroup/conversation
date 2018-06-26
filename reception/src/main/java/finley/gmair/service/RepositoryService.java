package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("core-agent")
public interface RepositoryService {

    @GetMapping("/core/repo/{machineId}/online")
    ResultData isOnilne(@RequestParam("machineId") String machineId);
}
