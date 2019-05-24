package finley.gmair.service.impl;

import finley.gmair.dao.CorpProfileDao;
import finley.gmair.model.openplatform.CorpProfile;
import finley.gmair.service.CorpProfileService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CorpProfileServiceImpl implements CorpProfileService {
    private Logger logger = LoggerFactory.getLogger(CorpProfileServiceImpl.class);

    @Autowired
    private CorpProfileDao corpProfileDao;

    @Override
    public ResultData create(CorpProfile corpProfile) {
        ResultData result = new ResultData();
        ResultData response = corpProfileDao.insert(corpProfile);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
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
        ResultData response = corpProfileDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }
}
