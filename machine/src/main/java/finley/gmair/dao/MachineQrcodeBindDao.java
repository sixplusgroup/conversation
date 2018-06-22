package finley.gmair.dao;

import finley.gmair.model.machine.MachineQrcodeBind;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineQrcodeBindDao {
    ResultData insert(MachineQrcodeBind machineQrcodeBind);
    ResultData select(Map<String, Object> condition);
}
