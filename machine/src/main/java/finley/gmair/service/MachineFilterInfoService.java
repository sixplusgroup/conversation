package finley.gmair.service;

import finley.gmair.dto.MachineEfficientFilterInfo;
import finley.gmair.dto.MachinePrimaryFilterInfo;
import finley.gmair.form.machine.MachineFilterInfoQuery;

import java.util.List;

/**
 * @author: Bright Chan
 * @date: 2020/11/8 14:36
 * @description: MachineFilterInfoService
 */
public interface MachineFilterInfoService {

    List<MachinePrimaryFilterInfo> queryMachinePrimaryFilterInfo(MachineFilterInfoQuery query);

    long queryMachinePrimaryFilterInfoSize(MachineFilterInfoQuery query);

    List<MachineEfficientFilterInfo> queryMachineEfficientFilterInfo(MachineFilterInfoQuery query);

    long queryMachineEfficientFilterInfoSize(MachineFilterInfoQuery query);

    List<String> queryMachineModelName();

    List<String> queryMachineModelCode(String modelName);

}
