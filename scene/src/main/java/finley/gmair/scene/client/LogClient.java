package finley.gmair.scene.client;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author : Lyy
 * @create : 2021-01-13 20:35
 **/
@FeignClient("log-agent")
@RequestMapping("/log")
public interface LogClient {

    @GetMapping(value = "/machinecom/query/{uid}")
    ResultData queryMachineComLog(@PathVariable("uid") String uid);

    @PostMapping(value = "/userlog/query")
    ResultData queryUserLog(@RequestParam(value = "userId") String userId);
}
