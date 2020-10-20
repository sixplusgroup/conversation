package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.express.ExpressCompanyForm;
import finley.gmair.form.express.ExpressParcelForm;
import finley.gmair.service.ExpressService;
import finley.gmair.service.InstallService;

import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@CrossOrigin
@RestController
@RequestMapping("/management/express")
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    @Autowired
    private InstallService installService;


    @GetMapping({"/company/query", "/company/{companyId}/query"})
    public ResultData companyFetch(@PathVariable(required = false, name = "companyId") String companyId) {
        return StringUtils.isEmpty(companyId) ? expressService.companyQuery() : expressService.companyQuery(companyId);
    }

    @GetMapping("/order/query/{orderId}")
    public ResultData orderFetch(@PathVariable("orderId") String orderId) {
        return expressService.orderQuery(orderId);
    }

    @GetMapping("/parcel/query/{parentExpress}")
    public ResultData queryAllParcels(@PathVariable("parentExpress") String parentExpress) {
        return expressService.queryAllParcels(parentExpress);
    }

    @PostMapping("/parcel/receive/confirm")
    public ResultData confirmDelivered(String expressId) {
        return expressService.confirmReceived(expressId);
    }

    @PostMapping("/company/create")
    public ResultData createExpressCompany(ExpressCompanyForm expressCompanyForm) {
        return expressService.createCompany(expressCompanyForm.getCompanyCode(),
                expressCompanyForm.getCompanyName(), expressCompanyForm.getCompanyUrl());
    }
}
