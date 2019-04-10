package finley.gmair.controller;

import finley.gmair.service.InstallerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class MemberController {
    @Autowired
    private InstallerService installerService;

    //根据用户的openid获取用户的基本信息
    @GetMapping("/profile/openid")
    public ResultData profile(String openid) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(openid)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供用户的openid");
            return result;
        }
        ResultData response = installerService.profileByOpenid(openid);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询失败，请稍后尝试");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/installer")
    public Principal user(Principal user) {
        return user;
    }


}
