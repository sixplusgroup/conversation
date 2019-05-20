package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.LogDao;
import finley.gmair.model.log.*;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class LogDaoImpl extends BaseDao implements LogDao {

    private final static String Collection_MachineComLog = "machinecom_log";

    private final static String Collection_SystemEventLog = "sys_event_log";

    private final static String Collection_UserActionLog = "user_machine_operation_log";

    private final static String Collection_Server2MachineLog = "server_command_log";

    private final static String Collection_UserLog = "user_account_operation_log";

    private final static String Collection_MqttAckLog = "mqtt_ack_log";

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
            if (condition.containsKey("uid")) {
                list = mongoTemplate.find(new Query(Criteria.where("uid").is(condition.get("uid"))), MachineComLog.class, Collection_MachineComLog);
            } else {
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

    @Override
    public ResultData insertUserActionLog(UserMachineOperationLog userActionLog) {
        ResultData result = new ResultData();
        userActionLog.setLogId(IDGenerator.generate("USL"));
        try {
            mongoTemplate.insert(userActionLog, Collection_UserActionLog);
            result.setData(userActionLog);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryUserActionLog(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<UserMachineOperationLog> list;
            if (condition.containsKey("userId") && condition.containsKey("machineValue")) {
                list = mongoTemplate.find(new Query(Criteria.where("userId").is(condition.get("userId"))
                        .and("machineValue").is(condition.get("machineValue"))), UserMachineOperationLog.class, Collection_UserActionLog);
            } else if (condition.containsKey("userId")) {
                list = mongoTemplate.find(new Query(Criteria.where("userId").is(condition.get("userId"))), UserMachineOperationLog.class, Collection_UserActionLog);
            } else if (condition.containsKey("machineValue")) {
                list = mongoTemplate.find(new Query(Criteria.where("machineValue").is(condition.get("machineValue"))), UserMachineOperationLog.class, Collection_UserActionLog);
            } else {
                list = mongoTemplate.findAll(UserMachineOperationLog.class, Collection_UserActionLog);
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

    @Override
    public ResultData insertServer2MachineLog(Server2MachineLog server2MachineLog) {
        ResultData result = new ResultData();
        server2MachineLog.setLogId(IDGenerator.generate("STM"));
        try {
            mongoTemplate.insert(server2MachineLog, Collection_Server2MachineLog);
            result.setData(server2MachineLog);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryServer2MachineLog(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Server2MachineLog> list;
            if (condition.containsKey("machineValue")) {
                list = mongoTemplate.find(new Query(Criteria.where("machineValue").is(condition.get("machineValue"))), Server2MachineLog.class, Collection_Server2MachineLog);
            } else {
                list = mongoTemplate.findAll(Server2MachineLog.class, Collection_Server2MachineLog);
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

    @Override
    public ResultData insertUserLog(UserAccountOperationLog userLog) {
        ResultData result = new ResultData();
        userLog.setLogId(IDGenerator.generate("USL"));
        try {
            mongoTemplate.insert(userLog, Collection_UserLog);
            result.setData(userLog);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryUserLog(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<UserAccountOperationLog> list;
            if (condition.containsKey("userId")) {
                list = mongoTemplate.find(new Query(Criteria.where("userId").is(condition.get("userId"))), UserAccountOperationLog.class, Collection_UserLog);
            } else {
                list = mongoTemplate.findAll(UserAccountOperationLog.class, Collection_UserLog);
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

    @Override
    public ResultData insertMqttAckLog(MqttAckLog mqttAckLog) {
        ResultData result = new ResultData();
        mqttAckLog.setLogId(IDGenerator.generate("MAL"));
        try {
            mongoTemplate.insert(mqttAckLog, Collection_MqttAckLog);
            result.setData(mqttAckLog);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryMqttAckLog(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MqttAckLog> list;
            if (condition.containsKey("machineId") && condition.containsKey("ackId")) {
                list = mongoTemplate.find(new Query(Criteria.where("machineId").is(condition.get("machineId"))
                .and("ackId").is(condition.get("ackId"))), MqttAckLog.class, Collection_MqttAckLog);
            } else if (condition.containsKey("machineId")) {
                list = mongoTemplate.find(new Query(Criteria.where("machineId").is(condition.get("machineId"))), MqttAckLog.class, Collection_MqttAckLog);
            } else if (condition.containsKey("ackId")) {
                list = mongoTemplate.find(new Query(Criteria.where("ackId").is(condition.get("ackId"))), MqttAckLog.class, Collection_MqttAckLog);
            } else {
                list = mongoTemplate.findAll(MqttAckLog.class, Collection_MqttAckLog);
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
