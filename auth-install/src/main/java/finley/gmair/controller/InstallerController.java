package finley.gmair.controller;

import finley.gmair.model.installation.Member;
import finley.gmair.service.InstallerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.WechatProperties;
import finley.gmair.util.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class InstallerController {
    @Autowired
    private InstallerService installerService;

    @PostMapping("/openid")
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

    /**
     * if any member with the given phone, set his/her openid
     *
     * @param
     * @return
     */
    @RequestMapping("/bind")
    public ResultData bind(String memberPhone, String wechatId) {
        ResultData result = new ResultData();

        memberPhone = memberPhone.trim();
        wechatId = wechatId.trim();

        //check empty input
        if (StringUtils.isEmpty(memberPhone) || StringUtils.isEmpty(wechatId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all required information");
            return result;
        }

        //according to the memberPhone,find the member
        Map<String, Object> condition = new HashMap<>();
        condition.put("memberPhone", memberPhone);
        condition.put("blockFlag", false);
        ResultData response = installerService.fetchInstaller(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setDescription("No member with the specified phone number");
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find any member with the given phone number");
            return result;
        }
        Member member = ((List<Member>) response.getData()).get(0);
        //update the member
        member.setWechatId(wechatId);
        response = installerService.reviseInstaller(member);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/installer")
    public Principal user(Principal user) {
        return user;
    }
}
