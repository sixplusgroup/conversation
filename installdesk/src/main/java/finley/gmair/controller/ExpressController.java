package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.express.Express;
import finley.gmair.service.ExpressService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
    @PostMapping("/receive")
    public ResultData receive(String param) {
        JSONObject json = JSONObject.parseObject(param);
        System.out.println(json);
        ResultData result = new ResultData();
        String status = json.getString("status");
        JSONObject lastResultJson = json.getJSONObject("lastResult");
        String state = lastResultJson.getString("state");
        String expressCompany = lastResultJson.getString("com");
        String expressNo = lastResultJson.getString("nu");
        String data = lastResultJson.getJSONArray("data").toJSONString();
        Express express = new Express(status, state, expressCompany, expressNo, data);
        Map<String, Object> condition = new HashMap<>();
        condition.put("expressNo", expressNo);
        condition.put("company", expressCompany);
        ResultData response = expressService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = expressService.update(express);
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result = expressService.create(express);
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
     * @param expressNo 物流单号
     * @param expressCompany 物流公司号
     * @return
     * */
    @GetMapping("/query")
    public ResultData query(String expressNo, String expressCompany) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(expressNo) || StringUtils.isEmpty(expressCompany)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please ensure you fill all the required fields");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("expressNo", expressNo);
        condition.put("company", expressCompany);
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
