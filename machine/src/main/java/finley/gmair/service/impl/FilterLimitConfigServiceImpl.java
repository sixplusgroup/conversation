package finley.gmair.service.impl;

import finley.gmair.dao.FilterLimitConfigDao;
import finley.gmair.service.FilterLimitConfigService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FilterLimitConfigServiceImpl implements FilterLimitConfigService {
    @Autowired
    private FilterLimitConfigDao filterLimitConfigDao;

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = filterLimitConfigDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch filter limit config from database.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No filter limit config found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find filter limit config day");
        return result;
    }
}
