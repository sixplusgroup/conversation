package finley.gmair.dao;

import finley.gmair.model.machine.MachineStatusV3;
import finley.gmair.util.ResultData;

public interface MqttCommunicationDao {

    ResultData insertV3(MachineStatusV3 status);
}
