package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.wechat.AccessToken;
import finley.gmair.model.wechat.WechatUser;
import finley.gmair.service.AccessTokenService;
import finley.gmair.service.WechatUserService;
import finley.gmair.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wechat/user")
@PropertySource("classpath:wechat.properties")
public class WechatUserController {
    @Autowired
    private WechatUserService wechatUserService;

    @Value("${wechat_appid}")
    private String wechatAppId;

    @Autowired
    private AccessTokenService accessTokenService;

    @GetMapping(value = "/list")
    public ResultData userList() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = wechatUserService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("user list is empty");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("user query error, please inspect");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @PostMapping(value = "/getByOpenId")
    public ResultData getUserById(String openId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("wechatId", openId);
        ResultData response = wechatUserService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No consumer found");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Consumer query error");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @PostMapping(value = "/openid")
    public ResultData openid(String code) {
        ResultData result = new ResultData();
        final String appid = WechatProperties.getValue("wechat_appid");
        final String secret = WechatProperties.getValue("wechat_secret");
        try {
            String openid = WechatUtil.queryOauthOpenId(appid, secret, code);
            if (!StringUtils.isEmpty(openid)) {
                result.setData(openid);
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(new StringBuffer("Fail to resolve the current code: ").append(code).toString());
            }
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Code: ").append(code).append(" not valid").toString());
        }
        return result;
    }

    @PostMapping(value = "/create/byopenId")
    public ResultData createUser(String openId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("appId", wechatAppId);
        ResultData response = accessTokenService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("为能够查询到appid对应的access token");
            return result;
        }
        String accessToken = ((AccessToken) response.getData()).getAccessToken();
        String url = new StringBuffer("https://api.weixin.qq.com/cgi-bin/user/info?access_token=").append(accessToken)
                .append("&openid=").append(openId).append("&lang=zh_CN").toString();
        String resultStr = HttpDeal.getResponse(url);
        JSONObject json = JSON.parseObject(resultStr);
        WechatUser user = new WechatUser(openId, json);
        condition.clear();
        condition.put("wechatId", openId);
        response = wechatUserService.fetch(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_OK:
                result = wechatUserService.modify(user);
                break;
            case RESPONSE_NULL:
                result = wechatUserService.create(user);
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(response.getDescription());
                break;
        }
        return result;
    }
}