package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.LogDao;
import finley.gmair.model.log.MachineComLog;
import finley.gmair.model.log.SystemEventLog;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class LogDaoImpl extends BaseDao implements LogDao {

    private final static String Collection_MachineComLog = "machinecom_log";

    private final static String Collection_SystemEventLog = "SystemEvent_log";

    @Override
    public ResultData insertMachineComLog(MachineComLog machineComLog) {
        ResultData result = new ResultData();
        try {
            mongoTemplate.insert(machineComLog, Collection_MachineComLog);
            result.setData(machineComLog);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryMachineComLog(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachineComLog> list;
            if(condition.containsKey("uid")) {
                list = mongoTemplate.find(new Query(Criteria.where("uid").is(condition.get("uid"))), MachineComLog.class, Collection_MachineComLog);
            }else{
                list = mongoTemplate.findAll(MachineComLog.class, Collection_MachineComLog);
            }
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
    public ResultData insertModuleLog(SystemEventLog systemEventLog) {
        ResultData result = new ResultData();
        systemEventLog.setEventId(IDGenerator.generate("SEL"));
        try {
            mongoTemplate.insert(systemEventLog, Collection_SystemEventLog);
            result.setData(systemEventLog);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryModuleLog(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<SystemEventLog> list;
            if (condition.containsKey("module")) {
                list = mongoTemplate.find(new Query(Criteria.where("module").is(condition.get("module"))), SystemEventLog.class, Collection_SystemEventLog);
            } else {
                list = mongoTemplate.findAll(SystemEventLog.class, Collection_SystemEventLog);
            }
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

}
