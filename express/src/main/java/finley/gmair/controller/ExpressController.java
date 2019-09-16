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
@RequestMapping("/drift/express")
@PropertySource("classpath:express.properties")
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    @Value("${key}")
    private String key;

    /**
     * 订阅快递信息
     * @param company
     * @param number
     * @param from
     * @param to
     * @param callbackurl
     * @param salt
     * @param resultv2
     * @param autoCom
     * @param interCom
     * @param departureCountry
     * @param departureCom
     * @param destinationCountry
     * @param destinationCom
     * @param phone
     * @return
     */
    @PostMapping("/subscribe")
    public String subscribeData(String company, String number, String from, String to, String callbackurl, String salt, int resultv2, int autoCom,
                                    int interCom, String departureCountry, String departureCom, String destinationCountry, String destinationCom, String phone) {

        StringBuilder param = new StringBuilder("{");
        param.append("\"company\":\"").append(company).append("\"");
        param.append(",\"number\":\"").append(number).append("\"");
        param.append(",\"from\":\"").append(from).append("\"");
        param.append(",\"to\":\"").append(to).append("\"");
        param.append(",\"key\":\"").append(this.key).append("\"");
        param.append(",\"parameters\":{");
        param.append("\"callbackurl\":\"").append(callbackurl).append("\"");
        param.append(",\"salt\":\"").append(salt).append("\"");
        if(1 == resultv2) {
            param.append(",\"resultv2\":1");
        } else {
            param.append(",\"resultv2\":0");
        }
        if(1 == autoCom) {
            param.append(",\"autoCom\":1");
        } else {
            param.append(",\"autoCom\":0");
        }
        if(1 == interCom) {
            param.append(",\"interCom\":1");
        } else {
            param.append(",\"interCom\":0");
        }
        param.append(",\"departureCountry\":\"").append(departureCountry).append("\"");
        param.append(",\"departureCom\":\"").append(departureCom).append("\"");
        param.append(",\"destinationCountry\":\"").append(destinationCountry).append("\"");
        param.append(",\"destinationCom\":\"").append(destinationCom).append("\"");
        param.append(",\"phone\":\"").append(phone).append("\"");
        param.append("}");
        param.append("}");

        Map<String, String> params = new HashMap<>();
        params.put("schema", "json");
        params.put("param", param.toString());

        return expressService.post(params);
    }
}
