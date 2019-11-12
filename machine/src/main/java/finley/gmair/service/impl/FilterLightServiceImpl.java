package finley.gmair.service.impl;

import finley.gmair.dao.FilterLightDao;
import finley.gmair.model.machine.FilterLight;
import finley.gmair.service.FilterLightService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FilterLightServiceImpl implements FilterLightService {
    @Autowired
    private FilterLightDao filterLightDao;

    @Override
    public ResultData create(FilterLight filterLight) {
        ResultData result = new ResultData();
        ResultData response = filterLightDao.insert(filterLight);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert filter light into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = filterLightDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch out filter light from database.");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No filter light found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find filter light");
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = filterLightDao.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to update");
            return result;
        }
        result.setDescription("success to update");
        return result;
    }

}

