package finley.gmair.controller;

import finley.gmair.model.wechat.WechatUser;
import finley.gmair.service.WechatUserService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.WechatProperties;
import finley.gmair.util.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wechat/user")
public class WechatUserController {
    @Autowired
    private WechatUserService wechatUserService;

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

    @PostMapping(value = "/openId")
    public String getOpenId(String code) {
        final String appid = WechatProperties.getValue("wechat_appid");
        final String secret = WechatProperties.getValue("wechat_secret");
        String openid = WechatUtil.queryOauthOpenId(appid, secret, code);
        return openid;
    }
}