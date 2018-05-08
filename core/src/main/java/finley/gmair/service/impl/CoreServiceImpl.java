package finley.gmair.service.impl;

import finley.gmair.dao.CoreDao;
import finley.gmair.service.CoreService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoreServiceImpl implements CoreService {

    @Autowired
    private CoreDao coreDao;

    @Override
    public ResultData insert() {
        ResultData result = new ResultData();
        ResultData response = coreDao.insert();
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to store express company with name: ").append("").toString());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }
}
