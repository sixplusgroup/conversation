package finley.gmair.dao;

import finley.gmair.model.machine.MachineSensorData;
import finley.gmair.model.machine.MachineStateData;
import finley.gmair.util.ResultData;

public interface MqttCommunicationDao {
    ResultData insertMachineStateData(MachineStateData stateData);

    ResultData insertMachineSensorData(MachineSensorData sensorData);
}
