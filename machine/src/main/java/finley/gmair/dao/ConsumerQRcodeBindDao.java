package finley.gmair.dao;

import finley.gmair.model.machine.ConsumerQRcodeBind;
import finley.gmair.model.machine.MachineListDaily;
import finley.gmair.util.ResultData;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface ConsumerQRcodeBindDao {
    @Transactional
    ResultData insert(ConsumerQRcodeBind consumerQRcodeBind);

    ResultData query(Map<String, Object> condition);

    ResultData query_view(Map<String, Object> condition);

    @Transactional
    ResultData update(Map<String, Object> condition);

    @Transactional
    ResultData remove(String bindId);

    ResultData queryMachineListView(Map<String, Object> condition);

    ResultData queryMachineListSecondView(Map<String, Object> condition);
}
