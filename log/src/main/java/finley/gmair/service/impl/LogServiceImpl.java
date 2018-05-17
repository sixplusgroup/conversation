package finley.gmair.service.impl;

import finley.gmair.dao.LogDao;
import finley.gmair.model.express.ExpressCompany;
import finley.gmair.model.log.MachineComLog;
import finley.gmair.service.LogService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
