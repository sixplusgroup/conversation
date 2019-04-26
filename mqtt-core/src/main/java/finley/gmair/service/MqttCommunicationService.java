package finley.gmair.service;

import finley.gmair.model.machine.MachineSensorData;
import finley.gmair.model.machine.MachineStateData;
import finley.gmair.util.ResultData;

public interface MqttCommunicationService {
    ResultData createStateData(MachineStateData stateData);

    ResultData createSensorData(MachineSensorData sensorData);
}
