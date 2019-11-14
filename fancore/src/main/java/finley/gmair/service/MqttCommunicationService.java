package finley.gmair.service;

import finley.gmair.model.machine.MachineStatusV3;
import finley.gmair.util.ResultData;

public interface MqttCommunicationService {
    ResultData create(MachineStatusV3 status);
}
