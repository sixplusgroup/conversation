package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("machine-agent")
public interface MachineService {
    @PostMapping("/machine/redis/v1/status/cacheput")
    ResultData setV1Cache(@RequestParam("uid") String uid,
                          @RequestParam("pm25") int pm25,
                          @RequestParam("tempurature")int tempurature,
                          @RequestParam("humidity") int humidity,
                          @RequestParam("co2") int co2,
                          @RequestParam("velocity") int velocity,
                          @RequestParam("power") int power,
                          @RequestParam("mode") int mode,
                          @RequestParam("heat") int heat,
                          @RequestParam("light") int light);

    @PostMapping("/machine/redis/v2/status/cacheput")
    ResultData setV2Cache(@RequestParam("uid") String uid,
                          @RequestParam("pm2_5") int pm2_5,
                          @RequestParam("temp") int temp,
                          @RequestParam("humid") int humid,
                          @RequestParam("co2") int co2,
                          @RequestParam("volume") int volume,
                          @RequestParam("power") int power,
                          @RequestParam("mode") int mode,
                          @RequestParam("heat") int heat,
                          @RequestParam("light") int light,
                          @RequestParam("lock") int lock);
}
