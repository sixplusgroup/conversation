package finley.gmair.controller;

import finley.gmair.model.wechat.AccessToken;
import finley.gmair.service.AccessTokenService;
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
@RequestMapping("/wechat/token")
public class AccessTokenController {
    private Logger logger = LoggerFactory.getLogger(AccessTokenController.class);

    @Autowired
    private AccessTokenService accessTokenService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultData createAccessToken(AccessToken token) {
        ResultData result = new ResultData();
        ResultData response = accessTokenService.create(token);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultData queryAccessToken() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = accessTokenService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultData updateAccessToken(AccessToken token) {
        ResultData result = new ResultData();
        ResultData response = accessTokenService.modify(token);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        }
        return result;
    }
}