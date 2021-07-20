package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("location-agent")
public interface LocationService {
    @RequestMapping(method = RequestMethod.POST, value = "/location/address/resolve")
    ResultData geocoder(@RequestParam("address") String address);

    @RequestMapping(method = RequestMethod.GET, value = "/location/province/list")
    ResultData province();

    @RequestMapping(method = RequestMethod.GET, value = "/location/{provinceId}/cities")
    ResultData city(@PathVariable("provinceId") String province);

    @RequestMapping(method = RequestMethod.GET, value = "/location/{cityId}/districts")
    ResultData district(@PathVariable("cityId") String cityId);

    @GetMapping("/location/city/profile")
    ResultData cityProfile(@RequestParam("cityId") String cityId);
}
