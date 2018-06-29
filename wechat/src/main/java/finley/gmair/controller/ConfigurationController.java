package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import finley.gmair.util.Configuration;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.WechatConfig;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wechat/config")
public class ConfigurationController {

    @CrossOrigin({"https://reception.gmair.net"})
    @PostMapping("/init")
    public ResultData config(String url) {
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(url) || StringUtils.isEmpty(url.split("#")[0])) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure the url is valid.");
            return result;
        }
        url = url.split("#")[0];
        Configuration configuration = WechatConfig.config(url);
        System.out.println(new StringBuffer("Configuration").append("-info-").append(JSON.toJSONString(configuration)));
        result.setData(configuration);
        return result;
    }
}
