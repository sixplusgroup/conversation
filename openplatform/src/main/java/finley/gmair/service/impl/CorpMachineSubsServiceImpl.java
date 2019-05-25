package finley.gmair.service.impl;

import finley.gmair.dao.CorpMachineSubsDao;
import finley.gmair.model.openplatform.MachineSubscription;
import finley.gmair.service.CorpMachineSubsService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CorpMachineSubsServiceImpl implements CorpMachineSubsService {

    private Logger logger = LoggerFactory.getLogger(CorpMachineSubsServiceImpl.class);

    @Autowired
    private CorpMachineSubsDao corpMachineSubsDao;

    @Override
    public ResultData create(MachineSubscription machineSubscription){
        ResultData result = new ResultData();
        ResultData response = corpMachineSubsDao.insert(machineSubscription);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = corpMachineSubsDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData remove(String subscriptionId) {
        ResultData result = new ResultData();
        ResultData response = corpMachineSubsDao.remove(subscriptionId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }


}
