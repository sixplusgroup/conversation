package finley.gmair.service.impl;

import finley.gmair.dao.CheckEquipmentDao;
import finley.gmair.model.formaldehyde.CheckEquipment;
import finley.gmair.service.CheckEquipmentService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CheckEquipmentServiceImpl implements CheckEquipmentService {
    @Autowired
    private CheckEquipmentDao checkEquipmentDao;

    @Override
    public ResultData create(CheckEquipment checkEquipment){
        ResultData result = new ResultData();
        ResultData response = checkEquipmentDao.insert(checkEquipment);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert checkEquipment into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = checkEquipmentDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch from database.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("Not found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find");
        return result;
    }

}
