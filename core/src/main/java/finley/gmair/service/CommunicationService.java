package finley.gmair.service;

import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.repo.MachinePartialStatusRepository;
import finley.gmair.util.ResultData;

public interface CommunicationService {
    ResultData create(MachineStatus status);

    ResultData create(MachinePartialStatus status);
}
