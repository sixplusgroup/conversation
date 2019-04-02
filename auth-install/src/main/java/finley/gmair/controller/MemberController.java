package finley.gmair.controller;

import finley.gmair.model.installation.Member;
import finley.gmair.service.InstallerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.WechatProperties;
import finley.gmair.util.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class InstallerController {
    @Autowired
    private InstallerService installerService;

    //根据用户的openid获取用户的基本信息
    @GetMapping("/profile")
    public ResultData profile(String openid) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("wechatId", openid);
        condition.put("blockFlag", false);
        ResultData response = installerService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(((List) response.getData()).get(0));
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询失败，请稍后尝试");
        }
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
        ResultData response = installerService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setDescription("No member with the specified phone number");
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find any member with the given phone number");
            return result;
        }

        response = installerService.bindWechat(wechatId, memberPhone);
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
