package finley.gmair.service;

import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.model.machine.MachineV1Status;
import finley.gmair.util.ResultData;

public interface CommunicationService {
    ResultData create(MachineV1Status status);

    ResultData create(MachineStatus status);

    ResultData create(MachinePartialStatus status);
}
