package finley.gmair.controller;


import finley.gmair.service.CommodityService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/order")
@RestController
public class OrderCommodityController {

    @Autowired
    CommodityService commodityService;

    @RequestMapping(value = "/commodityList", method = RequestMethod.GET)
    public ResultData commodityList() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = commodityService.fetchCommodity(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription("服务器忙，请稍后再试");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        } else {
            result.setResponseCode(response.getResponseCode());
            result.setData(response.getData());
        }

        return result;
    }


}
