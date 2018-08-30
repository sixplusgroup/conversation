package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import finley.gmair.dao.CommunicationDao;
import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.model.machine.MachineV1Status;
import finley.gmair.service.CommunicationService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunicationServiceImpl implements CommunicationService {

    @Autowired
    private CommunicationDao communicationDao;

    @Autowired
    private PacketNotifier notifier;

    @Override
    public ResultData create(MachineV1Status status){
        ResultData result = new ResultData();
        ResultData response = communicationDao.insertV1(status);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to insert machine status");
        }
        //notifier.sendV1(status.getMachineId());
        return result;
    }

    @Override
    public ResultData create(MachineStatus status) {
        ResultData result = new ResultData();
        ResultData response = communicationDao.insert(status);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert machine status ").append(JSON.toJSONString(status)).toString());
        }
        notifier.send(status.getUid());
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
            result.setDescription(new StringBuffer("Fail to insert partial status ").append(JSON.toJSONString(status)).toString());
        }
        notifier.sendPartialData(status.getUid());
        return result;
    }



}
