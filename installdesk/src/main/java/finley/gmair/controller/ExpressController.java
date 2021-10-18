package finley.gmair.controller;

import finley.gmair.model.installation.ExpressOrder;
import finley.gmair.model.installation.ExpressOrderStatus;
import finley.gmair.service.ExpressService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/express")
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    private Logger logger = LoggerFactory.getLogger(ExpressController.class);

    /**
     * 快递100推送消息接收
     * */
    @PostMapping("/create")
    public ResultData createOrderExpress(@RequestParam(value = "orderId", required = true)String orderId,
                                         @RequestParam(value = "expressNo", required = true)String expressNo,
                                         @RequestParam(value = "company", required = true)String company,
                                         @RequestParam(value = "status", required = true) int status,
                                         @RequestParam(value = "createTime", required = true) Timestamp createTime) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("status", ExpressOrderStatus.valueOf(status));
        condition.put("blockFlag", false);
        ResultData response = expressService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            condition.clear();
            condition.put("expressId",((List<ExpressOrder>)response.getData()).get(0).getExpressId());
            condition.put("expressNum",expressNo);
            condition.put("company",company);
            result = expressService.update(condition);
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            ExpressOrder expressOrder = new ExpressOrder("", orderId, company, expressNo,ExpressOrderStatus.valueOf(status),createTime);
            result = expressService.create(expressOrder);
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器正忙，请稍后重试");
        }
        return result;
    }

    /**
     * 快递查询接口
     * @return
     * */
    @GetMapping("/query")
    public ResultData query(@RequestParam(value = "orderId", required = true)String orderId,
                            @RequestParam(value = "expressNo", required = false)String expressNo,
                            @RequestParam(value = "company", required = false)String company,
                            @RequestParam(value = "status", required = false, defaultValue = "-1") int status) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("expressNo", expressNo);
        condition.put("company", company);
        condition.put("status", ExpressOrderStatus.valueOf(status));
        condition.put("blockFlag", false);
        ResultData response = expressService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未查询到相关订单：" + expressNo);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询失败，请稍后重试");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }
}
