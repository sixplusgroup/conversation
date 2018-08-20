package finley.gmair.service.impl;

import finley.gmair.dao.MachineModeDao;
import finley.gmair.model.mode.MachineMode;
import finley.gmair.service.MachineModeService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MachineModeServiceImpl implements MachineModeService {
    @Autowired
    private MachineModeDao machineModeDao;

    @Override
    public ResultData create(MachineMode machineMode){
        ResultData result = new ResultData();
        ResultData response = machineModeDao.insert(machineMode);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert machine mode into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = machineModeDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch machine mode from database.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No machine mode found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find machine mode");
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = machineModeDao.update(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update machine mode by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to update machine mode by modelId");
        return result;
    }
}
