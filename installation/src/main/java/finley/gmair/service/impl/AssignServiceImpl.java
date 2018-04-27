package finley.gmair.service.impl;

import finley.gmair.dao.AssignDao;
import finley.gmair.model.installation.Assign;
import finley.gmair.service.AssignService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AssignServiceImpl implements AssignService {
    @Autowired
    private AssignDao assignDao;

    @Override
    public ResultData createAssign(Assign assign){
        ResultData result = new ResultData();
        ResultData response = assignDao.insertAssign(assign);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to insert assign");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert assign" + assign.toString());
        }
        return result;
    }

    @Override
    public ResultData fetchAssign(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = assignDao.queryAssign(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to fetch assign");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No assign found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch assign");
        }
        return result;
    }

    @Override
    public  ResultData updateAssign(Assign assign){
        ResultData result = new ResultData();
        ResultData response = assignDao.updateAssign(assign);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to update assign");
        }
        else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update assign");
        }
        return result;
    }
}
