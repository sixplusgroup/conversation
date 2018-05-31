package finley.gmair.service;

import finley.gmair.model.machine.IdleMachine;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/31
 */
public interface IdleMachineService {
    ResultData create(IdleMachine idleMachine);

    ResultData fetch(Map<String, Object> condition);

    ResultData modify(Map<String, Object> condition);

    ResultData modifyIdle(Map<String, Object> condition);
}
