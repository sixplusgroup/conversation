package finley.gmair.service;


import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("install-agent")
public interface ReconnaissanceService {

    @RequestMapping(value = "/installation/reconnaissance/create", method = RequestMethod.POST)
    ResultData createReconnaissance(@RequestParam("orderId") String orderId);

    @RequestMapping(value = "/installation/reconnaissance/order/{orderId}", method = RequestMethod.GET)
    ResultData getReconnaissance(@PathVariable("orderId") String orderId);
}
