package finley.gmair.dao;

import finley.gmair.model.machine.IdleMachine;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/31
 */
public interface IdleMachineDao {
    ResultData insert(IdleMachine idleMachine);

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);

    ResultData updateIdle(Map<String, Object> condition);
}
