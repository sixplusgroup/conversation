package finley.gmair.controller;


import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.WechatProperties;
import finley.gmair.util.WechatUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/installation/wechat")
public class WechatController {

    @GetMapping("openid")
    public ResultData getOpenId(String code) {
        ResultData result = new ResultData();
        String openid = WechatUtil.queryOauthOpenId(WechatProperties.getWechatAppid(), WechatProperties.getWechatSecret(), code);
        if (openid != null && openid != "") {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(openid);
            result.setDescription("success to get the openid");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("can not get the openid");
        }
        return result;
    }
}
