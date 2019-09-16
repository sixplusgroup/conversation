package finley.gmair.controller;

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

    /**
     * 订阅快递消息
     * @param expressCompany
     * @param expressNo
     * @param callbackUrl
     * @return
     */
    @PostMapping("/subscribe")
    public String subscribeData(String expressCompany, String expressNo, String callbackUrl) {
        StringBuilder param = new StringBuilder("{");
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
}
