package finley.gmair.controller;

import finley.gmair.model.wechat.WechatUser;
import finley.gmair.service.WechatUserService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wechat/user")
public class WechatUserController {
    private Logger logger = LoggerFactory.getLogger(WechatUserController.class);

    @Autowired
    private WechatUserService wechatUserService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultData create(WechatUser user) {
        ResultData result = new ResultData();
        ResultData resposne = wechatUserService.create(user);
        if (resposne.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(resposne.getData());
        } else {
            result.setResponseCode(resposne.getResponseCode());
            result.setDescription(resposne.getDescription());
        }
        return result;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultData query() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = wechatUserService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(response.getDescription());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultData update(WechatUser user) {
        ResultData result = new ResultData();
        ResultData response = wechatUserService.modify(user);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }
}