package finley.gmair.dao.impl;

import finley.gmair.dao.MqttCommunicationDao;
import finley.gmair.model.machine.MachineStatusV3;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class MqttCommunicationDaoImpl implements MqttCommunicationDao {
    private Logger logger = LoggerFactory.getLogger(MqttCommunicationDaoImpl.class);


    @Override
    public ResultData insertV3(MachineStatusV3 status) {
        ResultData result = new ResultData();
        try {
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
