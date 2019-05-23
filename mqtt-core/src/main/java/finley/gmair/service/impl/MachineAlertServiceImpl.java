package finley.gmair.service.impl;

import finley.gmair.dao.MachineAlertDao;
import finley.gmair.model.mqtt.MachineAlert;
import finley.gmair.service.MachineAlertService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MachineAlertServiceImpl implements MachineAlertService {

    @Autowired
    private MachineAlertDao machineAlertDao;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = machineAlertDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No machine alert found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve machine alert");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData create(MachineAlert alert) {
        ResultData result = new ResultData();
        ResultData response = machineAlertDao.insert(alert);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert machine alert to database");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData modifySingle(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = machineAlertDao.updateSingle(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update machine alert");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("Success to update machine alert");
        return result;
    }

    @Override
    public ResultData modifyBatch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = machineAlertDao.updateBatch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to batch update machine alert");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("Success to batch update machine alert");
        return result;
    }
}
