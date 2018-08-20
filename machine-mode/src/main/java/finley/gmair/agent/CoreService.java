package finley.gmair.agent;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("core-agent")
public interface CoreService {

    @PostMapping("/core/com/config/power")
    ResultData configPower(@RequestParam("uid") String uid,
                           @RequestParam("power") int power,
                           @RequestParam("version") int version);

    @PostMapping("/core/com/config/speed")
    ResultData configSpeed(@RequestParam("uid") String uid,
                           @RequestParam("speed") int speed,
                           @RequestParam("version") int version);

    @PostMapping("/core/com/config/heat")
    ResultData configHeat(@RequestParam("uid") String uid,
                          @RequestParam("speed") int heat,
                          @RequestParam("version") int version);
}
