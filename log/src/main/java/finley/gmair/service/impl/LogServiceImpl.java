package finley.gmair.service.impl;

import finley.gmair.dao.LogDao;
import finley.gmair.model.express.ExpressCompany;
import finley.gmair.model.log.MachineComLog;
import finley.gmair.model.log.SystemEventLog;
import finley.gmair.service.LogService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogDao logDao;

    @Override
    public ResultData createMachineComLog(MachineComLog machineComLog) {
        ResultData result = new ResultData();
        ResultData response = logDao.insertMachineComLog(machineComLog);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to store machineComLog with UID: ").append(machineComLog.getUid()).toString());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetchMachineComLog(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = logDao.queryMachineComLog(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No MachineComlog found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch MachineComLog from database");
        }
        return result;
    }

    @Override
    public ResultData createModuleLog(SystemEventLog systemEventLog) {
        ResultData result = new ResultData();
        ResultData response = logDao.insertModuleLog(systemEventLog);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store systemLog");
        return result;
    }

}
