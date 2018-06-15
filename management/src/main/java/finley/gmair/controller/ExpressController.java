package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.express.ExpressParcelForm;
import finley.gmair.service.ExpressService;
import finley.gmair.service.InstallService;
import finley.gmair.service.OrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/management/express")
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    @Autowired
    private InstallService installService;

    @Autowired
    private OrderService orderService;

    @GetMapping({"/company/query", "/company/{companyId}/query"})
    public ResultData companyFetch(@PathVariable(required = false, name = "companyId") String companyId) {
        return StringUtils.isEmpty(companyId) ? expressService.companyQuery() : expressService.companyQuery();
    }

    @GetMapping("/order/query/{orderId}")
    public ResultData orderFetch(@PathVariable("orderId") String orderId) {
        return expressService.orderQuery(orderId);
    }

    @GetMapping("/parcel/query/{parentExpress}")
    public ResultData queryAllParcels(@PathVariable("parentExpress") String parentExpress) {
        return expressService.queryAllParcels(parentExpress);
    }

    @PostMapping("/parcel/create/{orderId}")
    public ResultData createParcel(@PathVariable("orderId") String orderId, ExpressParcelForm form) {
        ResultData result = new ResultData();
        if (form.getParcelType() == 0) {
            ResultData response = orderService.orderInfo(orderId);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(response.getResponseCode());
                result.setDescription(response.getDescription());
                return result;
            }
            JSONObject jsonObject = new JSONObject((LinkedHashMap) response.getData());
            String consignee = jsonObject.getString("consignee");
            String phone = jsonObject.getString("phone");
            String address = jsonObject.getString("address");
            response = installService.createInstallationAssign(form.getCodeValue(), consignee, phone, address);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(response.getResponseCode());
                result.setDescription(response.getDescription());
                return result;
            }
            return expressService.createParcel(form.getParentExpress(), form.getExpressNo(),
                    form.getParcelType(), form.getCodeValue());
        } else {
            return expressService.createParcel(form.getParentExpress(), form.getExpressNo(),
                    form.getParcelType(), null);
        }
    }

}
