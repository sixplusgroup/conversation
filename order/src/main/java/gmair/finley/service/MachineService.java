package gmair.finley.service;

import model.machine.IdleMachine;
import pagination.DataTableParam;
import utils.ResultData;

import java.util.Map;

public interface MachineService {

    ResultData deleteDevice(String deviceId);
    
    ResultData createIdleMachine(IdleMachine machine);
    
    ResultData fetchIdleMachine(Map<String, Object> condition);

    ResultData updateIdleMachine(Map<String, Object> condition);

    ResultData queryMachineStatus(Map<String, Object> condition);

    ResultData queryMachineStatus(Map<String, Object> condition, DataTableParam param);

    ResultData queryMachineStata(Map<String, Object> condition);

    ResultData mdifyidle(Map<String, Object> condition);

    ResultData fetchRecord(Map<String, Object> condtion);

    ResultData queryMachineStatusRange(Map<String, Object> condition);
}
