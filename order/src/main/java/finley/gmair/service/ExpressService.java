package finley.gmair.service;

import finley.gmair.form.express.ExpressOrderForm;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("express-agent")
public interface ExpressService {
    @RequestMapping(value = "/express/order/create", method = RequestMethod.POST)
    public ResultData addOrder(@RequestParam("orderId") String orderId,
                               @RequestParam("companyId") String companyId,
                               @RequestParam("expressNo") String expressNo);
}
