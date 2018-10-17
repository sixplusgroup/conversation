package finley.gmair.service.Impl;

import finley.gmair.dao.MachineMapDao;
import finley.gmair.service.MachineMapService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MachineMapServiceImpl implements MachineMapService {

    @Autowired
    private MachineMapDao machineMapDao;

    @Override
    public ResultData fetchMachineMapList(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = machineMapDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch from database.");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not found.");
            return result;
        }
        result.setData(response.getData());
        return result;
    }
}
