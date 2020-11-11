package finley.gmair.dao;

import finley.gmair.dto.MachineEfficientFilterInfo;
import finley.gmair.dto.MachinePrimaryFilterInfo;
import finley.gmair.dto.MachineTypeInfo;

import java.util.List;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/11/9 13:45
 * @description: MachineFilterInfoDao
 */
public interface MachineFilterInfoDao {

    List<MachinePrimaryFilterInfo> fetchMachinePrimaryFilterInfo(Map<String, Object> condition);

    List<MachineEfficientFilterInfo> fetchMachineEfficientFilterInfo(Map<String, Object> condition);

    List<MachineTypeInfo> fetchMachineTypeInfo();
}
