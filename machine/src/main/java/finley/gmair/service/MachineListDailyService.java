package finley.gmair.service;

import finley.gmair.model.machine.MachineListDaily;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface MachineListDailyService {
    ResultData insertMachineListDailyBatch(List<MachineListDaily> list);

    ResultData queryMachineListDaily(Map<String, Object> condition);

    ResultData queryMachineListDaily(Map<String, Object> condition,int curPage,int pageSize);

    ResultData deleteMachineListDaily(String codeValue);
}
