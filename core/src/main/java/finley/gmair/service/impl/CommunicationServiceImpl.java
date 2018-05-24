package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import finley.gmair.dao.CommunicationDao;
import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.model.machine.v2.MachineLiveStatus;
import finley.gmair.service.CommunicationService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunicationServiceImpl implements CommunicationService {

    @Autowired
    private CommunicationDao communicationDao;

    @Override
    public ResultData create(MachineLiveStatus status) {
        ResultData result = new ResultData();
        ResultData response = communicationDao.insert(status);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert ").append(JSON.toJSONString(status)).toString());
        }
        return result;
    }

    @Override
    public ResultData create(MachinePartialStatus status) {
        ResultData result = new ResultData();
        ResultData response = communicationDao.insert(status);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert ").append(JSON.toJSONString(status)).toString());
        }
        return result;
    }
}
