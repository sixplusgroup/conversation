package finley.gmair.service.impl;

import finley.gmair.dao.LogDao;
import finley.gmair.model.log.*;
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

    @Override
    public ResultData fetchModuleLog(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = logDao.queryModuleLog(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No moduleLog found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve moduleLog");
        }
        return result;
    }

    @Override
    public ResultData createUserActionLog(UserMachineOperationLog userActionLog) {
        ResultData result = new ResultData();
        ResultData response = logDao.insertUserActionLog(userActionLog);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store userAction log");
        return result;
    }

    @Override
    public ResultData fetchUserActionLog(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = logDao.queryUserActionLog(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No user action log found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve user action log");
        }
        return result;
    }

    @Override
    public ResultData createServer2MachineLog(Server2MachineLog server2MachineLog) {
        ResultData result = new ResultData();
        ResultData response = logDao.insertServer2MachineLog(server2MachineLog);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store server-machine log");
        return result;
    }

    @Override
    public ResultData fetchServer2MachineLog(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = logDao.queryServer2MachineLog(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No server-machine log found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve server-machine log");
        }
        return result;
    }

    @Override
    public ResultData createUserLog(UserAccountOperationLog userLog) {
        ResultData result = new ResultData();
        ResultData response = logDao.insertUserLog(userLog);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store user log");
        return result;
    }

    @Override
    public ResultData fetchUserLog(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = logDao.queryUserLog(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No user log found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve user log");
        }
        return result;
    }

    @Override
    public ResultData createMqttAckLog(MqttAckLog mqttAckLog) {
        ResultData result = new ResultData();
        ResultData response = logDao.insertMqttAckLog(mqttAckLog);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store mqtt ack log to mongo");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetchMqttAckLog(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = logDao.queryMqttAckLog(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No mqtt ack log found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve mqtt ack log");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData createAdminLog(AdminAccountOperationLog adminAccountOperationLog) {
        ResultData result = new ResultData();
        ResultData response = logDao.insertAdminLog(adminAccountOperationLog);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store admin log");
        return result;
    }

    @Override
    public ResultData fetchAdminLog(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = logDao.queryAdminLog(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No user log found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve admin log");
        }
        return result;
    }

}
