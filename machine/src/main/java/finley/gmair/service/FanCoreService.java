package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("fancore-agent")
public interface FanCoreService {
    @PostMapping("/core/com/status/config")
    ResultData config(@RequestParam("model") String model, @RequestParam("mac") String mac, @RequestParam("power") Integer power, @RequestParam("speed") Integer speed, @RequestParam("mode") Integer mode, @RequestParam("sweep") Integer sweep, @RequestParam("heat") Integer heat, @RequestParam("countdown") Integer countdown, @RequestParam("targettemp") Integer targettemp, @RequestParam("mutemode") Integer mutemode);
}
