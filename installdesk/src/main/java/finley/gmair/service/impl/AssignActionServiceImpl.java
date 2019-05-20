package finley.gmair.service.impl;

import finley.gmair.dao.AssignActionDao;
import finley.gmair.model.installation.AssignAction;
import finley.gmair.service.AssignActionService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName: AssignActionServiceImpl
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/15 3:16 PM
 */
@Service
public class AssignActionServiceImpl implements AssignActionService {

    @Autowired
    private AssignActionDao assignActionDao;

    @Override
    public ResultData create(AssignAction action) {
        ResultData result = new ResultData();
        ResultData response = assignActionDao.insert(action);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = assignActionDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else {
            result.setData(response.getData());
        }
        return result;
    }
}
