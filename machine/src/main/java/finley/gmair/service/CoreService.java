package finley.gmair.service;


import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("core-agent")
public interface CoreService {

    @GetMapping("/core/repo/{machineId}/online")
    ResultData isOnline(@PathVariable("machineId") String machineId);

    @PostMapping("/core/com/config/power")
    ResultData configPower(String uid, int power);

    @PostMapping("/core/com/config/lock")
    ResultData configLock(String uid, int lock);

    @PostMapping("/core/com/config/light")
    ResultData configLight(String uid, int light);

    @PostMapping("/core/com/config/heat")
    ResultData configHeat(String uid, int heat);
}
