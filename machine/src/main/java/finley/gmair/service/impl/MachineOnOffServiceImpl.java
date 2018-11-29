package finley.gmair.service.impl;

import finley.gmair.dao.MachineOnOffDao;
import finley.gmair.model.machine.Machine_on_off;
import finley.gmair.service.MachineOnOffService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MachineOnOffServiceImpl implements MachineOnOffService {
    @Autowired
    private MachineOnOffDao machineOnOffDao;

    @Override
    public ResultData create(Machine_on_off machine_on_off) {
        ResultData result = new ResultData();
        ResultData response = machineOnOffDao.insert(machine_on_off);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert machine_on_off config to database");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = machineOnOffDao.query(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No config found from database");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to retrieve config");
                break;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
        }
        return result;
    }

    @Override
    public ResultData update(Machine_on_off machine_on_off) {
        ResultData result = new ResultData();
        ResultData response = machineOnOffDao.update(machine_on_off);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("Fail to update machine_on_off");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }
}
