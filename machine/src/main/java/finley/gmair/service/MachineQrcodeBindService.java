package finley.gmair.service;

import finley.gmair.model.machine.MachineQrcodeBind;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineQrcodeBindService {
    ResultData fetch(Map<String, Object> condition);
    ResultData insert(MachineQrcodeBind machineQrcodeBind);
    ResultData modifyByQRcode(Map<String, Object> condition);
}
