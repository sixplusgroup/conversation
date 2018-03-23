package finley.gmair.service.impl;

import finley.gmair.dao.AccessTokenDao;
import finley.gmair.model.wechat.AccessToken;
import finley.gmair.service.WechatService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WechatServiceImpl implements WechatService {
    private Logger logger = LoggerFactory.getLogger(WechatServiceImpl.class);

    @Autowired
    private AccessTokenDao accessTokenDao;

    /**
     * This method will store | update the token record in database
     *
     * @param token
     * @return
     */
    @Override
    public ResultData refreshAccessToken(AccessToken token) {
        ResultData result = new ResultData();
        //check whether any records exists in database
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = accessTokenDao.query(condition);
        //if query fail, then return error
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to tell if any existing token record(s)");
            return result;
        }
        //if no record found, call insert method
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            response = accessTokenDao.insert(token);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                return result;
            }
            //if insert fail, return error
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to save the token to database");
            return result;
        }
        //call update method
        response = accessTokenDao.update(token);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            //if update fail, return error
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update the new token to database");
        }
        return result;
    }


}
