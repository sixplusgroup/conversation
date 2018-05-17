package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.LogDao;
import finley.gmair.model.log.MachineComLog;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

@Repository
public class LogDaoImpl extends BaseDao implements LogDao {

    @Override
    public ResultData insertMachineComLog(MachineComLog machineComLog) {
        ResultData result = new ResultData();
        try {
            mongoTemplate.insert(machineComLog, "MachineComLog");
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
