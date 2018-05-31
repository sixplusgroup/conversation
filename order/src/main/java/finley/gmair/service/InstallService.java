package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "install-agent")
public interface InstallService {

    @RequestMapping(value = "/installation/assign/create", method = RequestMethod.POST)
    ResultData create(@RequestParam("qrcode") String qrcode,
                      @RequestParam("consumerConsignee") String consumerConsignee,
                      @RequestParam("consumerPhone") String consumerPhone,
                      @RequestParam("consumerAddress") String consumerAddress
    );

    @RequestMapping(value = "/installation/assign/byqrcode", method = RequestMethod.GET)
    ResultData getAssign(@RequestParam("qrcode") String qrcode);
}
