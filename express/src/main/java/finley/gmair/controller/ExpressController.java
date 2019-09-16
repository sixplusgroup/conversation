package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.express.Express;
import finley.gmair.service.ExpressService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/express")
@PropertySource("classpath:express.properties")
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    @Value("${key}")
    private String key;

    @Value("${callbackurl}")
    private String callbackUrl;

    /**
     * 订阅快递消息
     * @param expressCompany
     * @param expressNo
     * @return
     */
    @PostMapping("/subscribe")
    public String subscribeData(String expressCompany, String expressNo) {
        StringBuffer param = new StringBuffer("{");
        param.append("\"company\":\"").append(expressCompany).append("\"");
        param.append(",\"number\":\"").append(expressNo).append("\"");
        param.append(",\"key\":\"").append(this.key).append("\"");
        param.append(",\"parameters\":{");
        param.append("\"callbackurl\":\"").append(callbackUrl).append("\"");
        param.append(",\"resultv2\":1");
        param.append(",\"autoCom\":0");
        param.append(",\"interCom\":0");
        param.append("}");
        param.append("}");
        Map<String, String> params = new HashMap<>();
        params.put("schema", "json");
        params.put("param", param.toString());
        return expressService.post(params);
    }

    @PostMapping("/post")
    public ResultData postData(HttpServletRequest request) throws Exception {
        ResultData result = new ResultData();
        String param = request.getParameter("param");
        JSONObject paramJson = JSONObject.parseObject(param);
        String status = paramJson.getString("status");
        JSONObject lastResultJson = paramJson.getJSONObject("lastResult");
        String state = lastResultJson.getString("state");
        String company = lastResultJson.getString("com");
        String expressNo = lastResultJson.getString("nu");
        String data = lastResultJson.getJSONArray("data").toJSONString();
        Express express = new Express(status, state, company, expressNo, data);
        Map<String, Object> condition = new HashMap<>();
        condition.put("expressNo", expressNo);
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
}
