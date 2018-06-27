package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "location-agent")
public interface LocationService {


    @RequestMapping(method = RequestMethod.POST, value = "/location/ip/resolve")
    ResultData ip2address(@RequestParam("ip") String ip);

}
