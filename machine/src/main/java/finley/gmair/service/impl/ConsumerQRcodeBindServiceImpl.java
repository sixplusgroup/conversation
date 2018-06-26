package finley.gmair.service.impl;

import finley.gmair.dao.ConsumerQRcodeBindDao;
import finley.gmair.model.machine.ConsumerQRcodeBind;
import finley.gmair.service.ConsumerQRcodeBindService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConsumerQRcodeBindServiceImpl implements ConsumerQRcodeBindService {
    @Autowired
    private ConsumerQRcodeBindDao consumerQRcodeBindDao;

    @Override
    public ResultData createConsumerQRcodeBind(ConsumerQRcodeBind consumerQRcodeBind){
        ResultData result = new ResultData();
        ResultData response = consumerQRcodeBindDao.insert(consumerQRcodeBind);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store bind with: " + consumerQRcodeBind.toString());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetchConsumerQRcodeBind(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = consumerQRcodeBindDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No bind found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find bind");
        }
        return result;
    }

}
