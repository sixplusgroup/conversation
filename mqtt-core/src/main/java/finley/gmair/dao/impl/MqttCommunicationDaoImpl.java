package finley.gmair.dao.impl;

import finley.gmair.dao.MqttCommunicationDao;
import finley.gmair.model.machine.MachineSensorData;
import finley.gmair.model.machine.MachineStateData;
import finley.gmair.repo.MachineSensorDataRepository;
import finley.gmair.repo.MachineStateDataRepository;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MqttCommunicationDaoImpl implements MqttCommunicationDao {

    @Autowired
    private MachineStateDataRepository machineStateDataRepository;

    @Autowired
    private MachineSensorDataRepository machineSensorDataRepository;

    @Override
    public ResultData insertMachineStateData(MachineStateData stateData) {
        ResultData result = new ResultData();
        try {
            machineStateDataRepository.save(stateData);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertMachineSensorData(MachineSensorData sensorData) {
        ResultData result = new ResultData();
        try {
            machineSensorDataRepository.save(sensorData);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
