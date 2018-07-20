package finley.gmair.controller;

import finley.gmair.service.WechatService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reception/wechat")
public class WechatController {
    @Autowired
    private WechatService wechatService;

    @PostMapping("/openid/bycode")
    public ResultData getOpenIdByCode(String code){
        ResultData result=new ResultData();
        //check empty
        if(StringUtils.isEmpty(code)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        //get openid by code
        ResultData response = wechatService.openid(code);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        }
        String openid = (String)response.getData();
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(openid);
        result.setDescription("success to get openId");
        return result;
    }
}
