package finley.gmair.service.impl;

import finley.gmair.dao.ApiBoundDao;
import finley.gmair.model.mqtt.ApiBound;
import finley.gmair.service.ApiBoundService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ApiBoundServiceImpl implements ApiBoundService {

    @Autowired
    private ApiBoundDao apiBoundDao;

    @Override
    public ResultData createApiBound(ApiBound bound) {
        return apiBoundDao.insertApiBound(bound);
    }

    @Override
    public ResultData fetchApiBound(Map<String, Object> condition) {
        return apiBoundDao.queryApiBound(condition);
    }

    @Override
    public ResultData modifyApiBound(Map<String, Object> condition) {
        return apiBoundDao.updateApiBound(condition);
    }
}
