package finley.gmair.controller;

import finley.gmair.util.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: PaymentController
 * @Description: TODO
 * @Author fan
 * @Date 2019/7/23 4:25 PM
 */
@RestController
@RequestMapping("/payment/bill")
public class BillController {
    @PostMapping("/create")
    public ResultData createBill() {
        ResultData result = new ResultData();

        return result;
    }

    @PostMapping("/notify")
    public ResultData notified() {
        ResultData result = new ResultData();

        return result;
    }
}
