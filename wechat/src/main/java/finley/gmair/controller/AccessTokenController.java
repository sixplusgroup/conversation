package finley.gmair.controller;

import finley.gmair.service.AccessTokenService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/accessToken")
@PropertySource("classpath:wechat.properties")
public class AccessTokenController {

    @Value("${wechat_appid}")
    private String wechatAppId;

    @Autowired
    private AccessTokenService accessTokenService;

    @GetMapping(value = "/query")
    public ResultData getToken(String appId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (StringUtils.isEmpty(appId)) {
            condition.put("appId", wechatAppId);
        } else {
            condition.put("appId", appId);
        }
        condition.put("blockFlag", false);
        ResultData response = accessTokenService.fetch(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No access token found");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Query error, please try again later");
                break;
        }
        return result;
    }
}
