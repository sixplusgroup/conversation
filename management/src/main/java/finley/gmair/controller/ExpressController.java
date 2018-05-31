package finley.gmair.controller;

import finley.gmair.service.ExpressService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management/express")
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    @GetMapping("/company/query")
    public ResultData companyFetch() {
        return expressService.companyQuery();
    }

    @GetMapping("/order/query/{orderId}")
    public ResultData orderFetch(@PathVariable("orderId") String orderId) {
        return expressService.orderQuery(orderId);
    }
}
