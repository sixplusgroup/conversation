package finley.gmair.dao;

import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.util.ResultData;

public interface CommunicationDao {
    ResultData insert(MachineStatus status);

    ResultData insert(MachinePartialStatus status);
}
