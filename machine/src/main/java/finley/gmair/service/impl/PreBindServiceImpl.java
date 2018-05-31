package finley.gmair.service.impl;

import finley.gmair.dao.PreBindDao;
import finley.gmair.model.machine.PreBindCode;
import finley.gmair.service.PreBindService;
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
public class PreBindServiceImpl implements PreBindService {
    @Autowired
    private PreBindDao preBindDao;
    @Override
    public ResultData create(PreBindCode preBindCode) {
        ResultData result = new ResultData();

        //First, determine whether the QRCode or machineId already exists
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", preBindCode.getMachineId());
        condition.put("codeValue", preBindCode.getCodeValue());

        ResultData response = preBindDao.queryBy2Id(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Machine or code is already used");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Query errors, please try again");
            return result;
        }
        //Don't exist -> insert
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            response = preBindDao.insert(preBindCode);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to store preBind to database");
            }
        }
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = preBindDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No preBind found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve preBind from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData deletePreBind(String codeValue) {
        ResultData result = preBindDao.delete(codeValue);
        return result;
    }
}