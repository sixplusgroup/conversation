package finley.gmair.dao;

import finley.gmair.model.machine.MachineEfficientFilter;
import finley.gmair.model.machine.MachineEfficientInformation;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author: ck
 * @date: 2020/9/14 13:51
 * @description: TODO
 */
public interface MachineEfficientInformationDao {

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);

    ResultData add(MachineEfficientInformation machineEfficientInformation);
}
