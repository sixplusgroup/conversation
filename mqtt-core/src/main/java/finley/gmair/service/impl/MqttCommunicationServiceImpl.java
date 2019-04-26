package finley.gmair.service.impl;

import finley.gmair.dao.MqttCommunicationDao;
import finley.gmair.model.machine.MachineSensorData;
import finley.gmair.model.machine.MachineStateData;
import finley.gmair.service.MqttCommunicationService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttCommunicationServiceImpl implements MqttCommunicationService {

    @Autowired
    private MqttCommunicationDao communicationDao;

    @Override
    public ResultData createStateData(MachineStateData stateData) {
        ResultData result = new ResultData();
        ResultData response = communicationDao.insertMachineStateData(stateData);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert machine state data");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        return result;
    }

    @Override
    public ResultData createSensorData(MachineSensorData sensorData) {
        ResultData result = new ResultData();
        ResultData response = communicationDao.insertMachineSensorData(sensorData);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert machine sensor data");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        return result;
    }
}
