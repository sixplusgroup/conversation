package finley.gmair.service;

import finley.gmair.model.machine.MachineStatus;
import finley.gmair.util.ResultData;

public interface CommunicationService {
    ResultData create(MachineStatus status);
}
