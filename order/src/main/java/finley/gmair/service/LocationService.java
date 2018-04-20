package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("location-agent")
public interface LocationService {
    @RequestMapping(value = "/location/address/resolve", method = RequestMethod.POST)
    ResultData geocoder(@RequestParam("address") String address);
}
