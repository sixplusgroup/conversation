package finley.gmair.service.impl;

import finley.gmair.dao.MqttCommunicationDao;
import finley.gmair.model.machine.MachineStatusV3;
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
    public ResultData create(MachineStatusV3 status) {
        ResultData result = new ResultData();
        ResultData response = communicationDao.insertV3(status);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }
}
