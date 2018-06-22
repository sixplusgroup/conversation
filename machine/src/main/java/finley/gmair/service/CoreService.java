package finley.gmair.service;


import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("core-agent")
public interface CoreService {

    @GetMapping("/core/repo/{machineId}/online")
    ResultData isOnline(@PathVariable("machineId") String machineId);
}
