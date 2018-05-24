package finley.gmair.dao;

import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.model.machine.v2.MachineLiveStatus;
import finley.gmair.util.ResultData;

public interface CommunicationDao {
    ResultData insert(MachineLiveStatus status);

    ResultData insert(MachinePartialStatus status);
}
