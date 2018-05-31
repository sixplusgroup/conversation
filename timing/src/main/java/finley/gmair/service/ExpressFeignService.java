package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("express-agent")
public interface ExpressFeignService {

    @RequestMapping(value = "/express/schedule/order", method = RequestMethod.POST)
    ResultData updateOrderStatus();

    @RequestMapping(value = "/express/schedule/parcel", method = RequestMethod.POST)
    ResultData updateParcelStatus();
}
