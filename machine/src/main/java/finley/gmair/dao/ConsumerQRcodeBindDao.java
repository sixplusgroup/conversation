package finley.gmair.dao;

import finley.gmair.model.machine.ConsumerQRcodeBind;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ConsumerQRcodeBindDao {
    ResultData insert(ConsumerQRcodeBind consumerQRcodeBind);

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
