package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("location-agent")
public interface LocationService {
    @RequestMapping("/location/address/resolve")
    ResultData geocoder(String address);
}
