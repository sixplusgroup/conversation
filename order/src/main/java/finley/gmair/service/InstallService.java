package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "installation-agent")
public interface InstallService {

    @RequestMapping(value = "/installation/assign/create", method = RequestMethod.POST)
    ResultData create(@RequestParam("qrcode") String qrcode,
                      @RequestParam("consumerConsignee") String consumerConsignee,
                      @RequestParam("consumerPhone") String consumerPhone,
                      @RequestParam("consumerAddress") String consumerAddress,
                      @RequestParam("access_token") String access_token
                      );
}
