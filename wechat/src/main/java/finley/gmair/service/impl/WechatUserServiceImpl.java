package finley.gmair.service.impl;

import finley.gmair.dao.WechatUserDao;
import finley.gmair.model.wechat.WechatUser;
import finley.gmair.service.WechatUserService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class WechatUserServiceImpl implements WechatUserService {
    private Logger logger = LoggerFactory.getLogger(WechatUserServiceImpl.class);

    @Autowired
    private WechatUserDao wechatUserDao;

    @Override
    @Transactional
    public ResultData create(WechatUser user) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("wechatId", user.getWechatId());
        ResultData response = wechatUserDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("Wechatuser already exist");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query wechatuser information from database");
            return result;
        }
        response = wechatUserDao.insert(user);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store wechatuser to database");
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = wechatUserDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No wechatuser found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve wechatuser from database");
        }
        return result;
    }

    @Override
    public ResultData modify(WechatUser user) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("userId", user.getUserId());
        ResultData response = wechatUserDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No wechatuser found from database");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query wechatuser information from database");
            return result;
        }
        response = wechatUserDao.update(user);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to update wechatuser to database");
        return result;
    }
}