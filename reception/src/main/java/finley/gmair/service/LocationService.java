package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "location-agent")
public interface LocationService {
    @RequestMapping(method = RequestMethod.POST, value = "/location/ip/resolve")
    ResultData ip2address(@RequestParam("ip") String ip);

    @RequestMapping(method = RequestMethod.GET, value = "/location/province/list")
    ResultData province();

    @RequestMapping(method = RequestMethod.GET, value = "/location/{provinceId}/cities")
    ResultData city(@PathVariable("provinceId") String province);

    @RequestMapping(method = RequestMethod.GET, value = "/location/{cityId}/districts")
    ResultData district(@PathVariable("cityId") String city);

    @GetMapping("/location/probe/provinceId")
    ResultData probeProvinceIdByCityId(@RequestParam("cityId") String cityId);

    @GetMapping("/location/probe/code/city")
    ResultData probeCityId(@RequestParam("code") String code);

    @GetMapping("/location/city/profile")
    ResultData profile(@RequestParam("cityId") String cityId);

    @GetMapping("/location/district/profile")
    ResultData getDistrict(@RequestParam("districtId") String districtId);
}
