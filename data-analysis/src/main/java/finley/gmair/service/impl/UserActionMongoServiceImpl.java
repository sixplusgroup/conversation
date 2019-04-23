package finley.gmair.service.impl;

import finley.gmair.dao.UserActionMongoDao;
import finley.gmair.model.dataAnalysis.UserActionDaily;
import finley.gmair.model.machine.UserAction;
import finley.gmair.service.UserActionMongoService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserActionMongoServiceImpl implements UserActionMongoService {

    @Autowired
    private UserActionMongoDao userActionMongoDao;

    public ResultData fetchData(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = userActionMongoDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("no mongo data found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("get mongo data error");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    public ResultData getDataGroupByUserId(List<UserAction> list) {
        ResultData result = new ResultData();
        if (list.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure the parameters exist");
            return result;
        }

        ResultData response = userActionMongoDao.queryUserActionByUserId(list);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("group by user action list with userId error");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData getDataGroupByComponent(List<UserAction> list) {
        ResultData result = new ResultData();
        if (list.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure the parameters exist");
            return result;
        }

        ResultData response = userActionMongoDao.queryUserActionByComponent(list);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("group by user action list with component error");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

}
