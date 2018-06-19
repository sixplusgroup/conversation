package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.PathParam;

@FeignClient("express-agent")
public interface ExpressService {

    @GetMapping("/express/company/query")
    ResultData companyQuery();

    @GetMapping("/express/company/{companyId}/query")
    ResultData companyQuery(@PathVariable("companyId") String companyId);

    @GetMapping("/express/order/query/{orderId}")
    ResultData orderQuery(@PathVariable("orderId") String orderId);

    @PostMapping("/express/order/create")
    ResultData createExpressForm(@RequestParam("orderId") String orderId,
                                 @RequestParam("companyName") String companyName,
                                 @RequestParam("expressNo") String expressNo,
                                 @RequestParam("qrcode") String qrcode);

    @GetMapping("/express/parcel/query/{parentExpress}")
    ResultData queryAllParcels(@PathVariable("parentExpress") String parentExpress);

    @PostMapping("/express/parcel/create")
    ResultData createParcel(@RequestParam("parentExpress") String parentExpress,
                            @RequestParam("expressNo") String expressNo,
                            @RequestParam("parcelType") int parcelType,
                            @RequestParam("codeValue") String codeValue);

    @PostMapping("/express/receive/confirm")
    ResultData confirmReceived(@RequestParam("expressId") String expressId);
}
