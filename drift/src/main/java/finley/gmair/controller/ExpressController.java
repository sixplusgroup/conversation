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

    @Value("${url}")
    private String url;

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

    /**
     * 根据orderId查询快递信息
     * @param orderId
     * @return
     */
    @GetMapping
    public ResultData getExpress(String orderId){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(orderId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请输入orderId");
            return result;
        }
        Map<String,Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("orderId", orderId);
        ResultData response = expressService.fetchExpress(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setData(response.getData());
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("查询成功");
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未找到相关记录");
        }else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询失败");
        }
        return result;
    }
}
