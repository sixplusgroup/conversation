package finley.gmair.controller;

import finley.gmair.model.bill.BillInfo;
import finley.gmair.service.BillService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping(value = "/create")
    public ResultData createBill(String orderId, double orderPrice, double actualPrice) {
        ResultData result = new ResultData();
        BillInfo billInfo = new BillInfo(orderId, orderPrice, actualPrice);
        ResultData response = billService.createBill(billInfo);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("error");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }

        return result;
    }



}
