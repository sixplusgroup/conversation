package finley.gmair.service;


import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("core-agent")
public interface CoreService {

    @GetMapping("/core/repo/{machineId}/online")
    ResultData isOnline(@PathVariable("machineId") String machineId);

    @PostMapping("/core/com/config/power")
    ResultData configPower(@RequestParam("uid") String uid,
                           @RequestParam("power") int power,
                           @RequestParam("version") int version);

    @PostMapping("/core/com/config/lock")
    ResultData configLock(@RequestParam("uid") String uid,
                          @RequestParam("lock") int lock);

    @PostMapping("/core/com/config/light")
    ResultData configLight(@RequestParam("uid") String uid,
                           @RequestParam("light") int light,
                           @RequestParam("version") int version);

    @PostMapping("/core/com/config/heat")
    ResultData configHeat(@RequestParam("uid") String uid,
                          @RequestParam("heat") int heat,
                          @RequestParam("version") int version);

    @PostMapping("/core/com/config/mode")
    ResultData configMode(@RequestParam("uid") String uid,
                          @RequestParam("mode") int mode,
                          @RequestParam("version") int version);

    @PostMapping("/core/com/config/speed")
    ResultData configSpeed(@RequestParam("uid") String uid,
                           @RequestParam("speed") int speed,
                           @RequestParam("version") int version);

    @GetMapping("/core/com/probe/partial/pm25")
    ResultData probePartialPm25(@RequestParam("uid") String uid);

    @PostMapping("core/com/config/screen")
    ResultData configScreen(@RequestParam("uid") String uid,
                            @RequestParam("screen") int screen);
}
