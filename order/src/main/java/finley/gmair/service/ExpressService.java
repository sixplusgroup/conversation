package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("express-agent")
public interface ExpressService {
    @RequestMapping(value = "/express/order/create", method = RequestMethod.POST)
    ResultData addOrder(@RequestParam("orderId") String orderId,
                               @RequestParam("companyName") String companyId,
                               @RequestParam("expressNo") String expressNo);

    @RequestMapping(value = "/express/parcel/query/{codeValue}", method = RequestMethod.GET)
    ResultData getOrderByQrcode(@PathVariable("codeValue")  String codeValue) ;

    @RequestMapping(value = "/express/order/query/{orderId}", method = RequestMethod.GET)
    ResultData queryExpress(@PathVariable("orderId") String orderId);

    @RequestMapping(value = "/express/order/query/parcel/{orderId}", method = RequestMethod.GET)
    ResultData queryCodeValue(@PathVariable("orderId") String orderId);
}
