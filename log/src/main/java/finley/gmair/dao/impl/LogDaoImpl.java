package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.LogDao;
import finley.gmair.model.log.MachineComLog;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LogDaoImpl extends BaseDao implements LogDao {

    @Override
    public ResultData insertMachineComLog(MachineComLog machineComLog) {
        ResultData result = new ResultData();
        try {
            mongoTemplate.insert(machineComLog, "machinecom_log");
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryMachineComLog(String uid) {
        ResultData result = new ResultData();
        try {
            System.out.println(uid);
            List<MachineComLog> list = mongoTemplate.find(new Query(Criteria.where("uid").is(uid)), MachineComLog.class, "machinecom_log");
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryAllMachineComLog() {
        ResultData result = new ResultData();
        try {
            List<MachineComLog> list = mongoTemplate.findAll(MachineComLog.class, "machinecom_log");
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
