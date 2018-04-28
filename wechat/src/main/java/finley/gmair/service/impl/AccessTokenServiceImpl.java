package finley.gmair.service.impl;

import finley.gmair.dao.AccessTokenDao;
import finley.gmair.model.wechat.AccessToken;
import finley.gmair.service.AccessTokenService;
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
public class AccessTokenServiceImpl implements AccessTokenService {
    private Logger logger = LoggerFactory.getLogger(AccessTokenServiceImpl.class);

    @Autowired
    private AccessTokenDao accessTokenDao;

    @Override
    @Transactional
    public ResultData create(AccessToken token) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("accessToken", token.getAccessToken());
        ResultData response = accessTokenDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("AccessToken already exist");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query accesstoken information from database");
            return result;
        }
        response = accessTokenDao.insert(token);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store accesstoken to database");
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = accessTokenDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No accessToken found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve accesstoken from database");
        }
        return result;
    }

    @Override
    public ResultData modify(AccessToken token) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("accessToken", token.getAccessToken());
        ResultData response = accessTokenDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No accesstoken found from database");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query accesstoken information from database");
            return result;
        }
        response = accessTokenDao.update(token);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to update accesstoken to database");
        return result;
    }

    @Override
    public boolean existToken(Map<String, Object> condition) {
        Map<String, Object> con = new HashMap<>();
        for (Map.Entry<String, Object> e : condition.entrySet()) {
            con.clear();
            con.put(e.getKey(), e.getValue());
            ResultData response = accessTokenDao.query(con);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                return true;
            }
        }
        return false;
    }
}