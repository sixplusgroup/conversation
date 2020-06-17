package finley.gmair.dao;

import finley.gmair.model.machine.MachineListDaily;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface MachineListDailyDao {
    ResultData insertMachineListDailyBatch(List<MachineListDaily> list);

    ResultData queryMachineListView(Map<String, Object> condition);

    ResultData queryMachineListView(Map<String, Object> condition,int curPage,int pageSize);

    ResultData deleteMachineListDailyByCodeValue(String codeValue);
}
