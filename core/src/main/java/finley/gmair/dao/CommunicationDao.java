package finley.gmair.dao;

import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.model.machine.MachineV1Status;
import finley.gmair.util.ResultData;

public interface CommunicationDao {
    ResultData insertV1(MachineV1Status status);

    ResultData insert(MachineStatus status);

    ResultData insert(MachinePartialStatus status);
}
