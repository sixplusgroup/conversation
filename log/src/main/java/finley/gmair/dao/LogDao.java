package finley.gmair.dao;

import finley.gmair.model.log.*;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface LogDao {
    ResultData insertMachineComLog(MachineComLog machineComLog);

    ResultData queryMachineComLog(Map<String, Object> condition);

    ResultData insertModuleLog(SystemEventLog systemEventLog);

    ResultData queryModuleLog(Map<String, Object> condition);

    ResultData insertUserActionLog(UserMachineOperationLog userActionLog);

    ResultData queryUserActionLog(Map<String, Object> condition);

    ResultData insertServer2MachineLog(Server2MachineLog server2MachineLog);

    ResultData queryServer2MachineLog(Map<String, Object> condition);

    ResultData insertUserLog(UserAccountOperationLog userLog);

    ResultData queryUserLog(Map<String, Object> condition);

    ResultData insertMqttAckLog(MqttAckLog mqttAckLog);

    ResultData queryMqttAckLog(Map<String, Object> condition);
}
