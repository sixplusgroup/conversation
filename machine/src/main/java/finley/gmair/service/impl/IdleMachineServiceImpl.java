package finley.gmair.service.impl;

import finley.gmair.dao.IdleMachineDao;
import finley.gmair.model.machine.IdleMachine;
import finley.gmair.service.IdleMachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/31
 */
@Service
public class IdleMachineServiceImpl implements IdleMachineService {
    @Autowired
    private IdleMachineDao idleMachineDao;
    @Override
    public ResultData create(IdleMachine idleMachine) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", idleMachine.getMachineId());
        ResultData response = idleMachineDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("The idleMachine is already exists");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Query errors, please try again");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            response = idleMachineDao.insert(idleMachine);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to store idleMachine to database");
            }
        }
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = idleMachineDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No idleMachine found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve idleMachine from database");
        }
        return result;
    }

    @Override
    public ResultData modify(Map<String, Object> condition) {
        ResultData result = idleMachineDao.update(condition);
        return result;
    }

    @Override
    public ResultData modifyIdle(Map<String, Object> condition) {
        ResultData result = idleMachineDao.updateIdle(condition);
        return result;
    }
}