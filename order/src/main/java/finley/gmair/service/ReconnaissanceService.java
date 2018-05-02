package finley.gmair.service;


import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("installation-agent")
public interface ReconnaissanceService {

    @RequestMapping(value = "/installation/reconnaissance/create", method = RequestMethod.POST)
    ResultData createReconnaissance(@RequestParam("orderId") String orderId);
}
