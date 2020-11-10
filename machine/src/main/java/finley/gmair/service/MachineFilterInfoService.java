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

    List<MachineEfficientFilterInfo> queryMachineEfficientFilterInfo(MachineFilterInfoQuery query);

}
