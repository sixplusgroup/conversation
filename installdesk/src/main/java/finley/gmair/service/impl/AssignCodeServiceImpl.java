package finley.gmair.service.impl;

import finley.gmair.dao.AssignActionDao;
import finley.gmair.dao.AssignCodeDao;
import finley.gmair.model.installation.AssignAction;
import finley.gmair.model.installation.AssignCode;
import finley.gmair.service.AssignActionService;
import finley.gmair.service.AssignCodeService;
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
public class AssignCodeServiceImpl implements AssignCodeService {

    @Autowired
    private AssignCodeDao assignCodeDao;

    @Override
    public ResultData create(AssignCode assignCode) {
        ResultData result = new ResultData();
        ResultData response = assignCodeDao.insert(assignCode);
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
        ResultData response = assignCodeDao.query(condition);
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
