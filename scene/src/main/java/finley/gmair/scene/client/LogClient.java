package finley.gmair.scene.client;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author : Lyy
 * @create : 2021-01-13 20:35
 **/
@FeignClient("log-agent")
public interface LogClient {

    @GetMapping(value = "/log/machinecom/query/{uid}")
    ResultData queryLogByUid(@PathVariable("uid") String uid);
}
