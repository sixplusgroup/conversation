package finley.gmair.controller;

import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.WechatProperties;
import finley.gmair.util.WechatUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class InstallerController {
    @PostMapping("/wechat/openid")
    public ResultData openid(String code) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(code)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you have the code when query this method");
            return result;
        }
        //query the openid by code, via wechat appid & secret
        String openid = WechatUtil.queryOauthOpenId(WechatProperties.getValue("wechat_appid"), WechatProperties.getValue("wechat_secret"), code);
        if (StringUtils.isEmpty(openid)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Invalid code, please try again later");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(openid);
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/installer")
    public Principal user(Principal user) {
        return user;
    }
}
