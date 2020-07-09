package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author CK
 */

@FeignClient("location-agent")
public interface LocationService {

    @GetMapping("/location/id/profile")
    ResultData idProfile (@RequestParam("id") String id);
}
