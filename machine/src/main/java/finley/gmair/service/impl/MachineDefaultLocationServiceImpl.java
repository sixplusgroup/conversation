package finley.gmair.service.impl;

import finley.gmair.dao.MachineDefaultLocationDao;
import finley.gmair.model.machine.MachineDefaultLocation;
import finley.gmair.service.MachineDefaultLocationService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class MachineDefaultLocationServiceImpl implements MachineDefaultLocationService {
    @Autowired
    private MachineDefaultLocationDao machineDefaultLocationDao;

    @Override
    public ResultData create(MachineDefaultLocation machineDefaultLocation) {
        ResultData result = new ResultData();
        ResultData response = machineDefaultLocationDao.insert(machineDefaultLocation);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to insert");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert");
        }
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = machineDefaultLocationDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No machine default location found from database");
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail");
        }
        return result;
    }

    @Override
    public ResultData modify(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = machineDefaultLocationDao.update(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to modify");
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to modify");
            return result;
        }
        return result;
    }

}