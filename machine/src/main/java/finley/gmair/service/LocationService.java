package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("location-agent")
public interface LocationService {
    @GetMapping("/location/city/profile")
    ResultData cityProfile(@RequestParam("cityId")String cityId);

    @PostMapping("/location/address/resolve")
    ResultData geocoder(@RequestParam("address")String address);
}
