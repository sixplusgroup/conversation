package finley.gmair.service.impl;

import finley.gmair.dao.WechatResourceDao;
import finley.gmair.model.wechat.WechatResource;
import finley.gmair.service.WechatResourceService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class WechatResourceServiceImpl implements WechatResourceService {
    @Autowired
    private WechatResourceDao wechatResourceDao;

    @Override
    @Transactional
    public ResultData create(WechatResource resource) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("resourceName", resource.getResourceName());
        if (existResource(condition)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Resource already exist");
            return result;
        }
        ResultData response = wechatResourceDao.insert(resource);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store resource to database");
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = wechatResourceDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No resource found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve resource from database");
        }
        return result;
    }

    @Override
    public ResultData modify(WechatResource resource) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (StringUtils.isEmpty(resource.getResourceName())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("can't update without resourceName");
            return result;
        } else {
            condition.put("resourceName", resource.getResourceName());
            ResultData response = wechatResourceDao.query(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No resource found from database");
                return result;
            }
            if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to query resource information from database");
                return result;
            }
            response = wechatResourceDao.update(resource);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                return result;
            }
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update resource to database");
            return result;
        }
    }

    @Override
    public boolean existResource(Map<String, Object> condition) {
        Map<String, Object> con = new HashMap<>();
        for (Map.Entry<String, Object> entry : condition.entrySet()) {
            con.clear();
            con.put(entry.getKey(), entry.getValue());
            ResultData response = wechatResourceDao.query(con);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                return true;
            }
        }
        return false;
    }
}