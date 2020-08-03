package finley.gmair.dao;

import finley.gmair.model.machine.MachineEfficientFilter;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author: ck
 * @date: 2020/7/25 13:51
 * @description: TODO
 */
public interface MachineEfficientFilterDao {

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);

    ResultData add(MachineEfficientFilter machineEfficientFilter);

    ResultData queryNeedRemindFirst();

    ResultData queryNeedRemindSecond();
}
