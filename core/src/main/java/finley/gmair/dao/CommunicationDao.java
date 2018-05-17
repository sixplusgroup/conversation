package finley.gmair.dao;

import finley.gmair.model.machine.MachineStatus;
import finley.gmair.util.ResultData;

public interface CommunicationDao {
    ResultData insert(MachineStatus status);
}
