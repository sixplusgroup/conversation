package finley.gmair.service.impl;

import finley.gmair.dao.VerificationDao;
import finley.gmair.model.drift.VerifyInfo;
import finley.gmair.service.VerificationService;
import finley.gmair.service.VerifyService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ：lycheeshell
 * @Date ：Created in 16:01 2019/8/16
 */
@Service
@PropertySource({"classpath:verification.properties"})
public class VerificationServiceImpl implements VerificationService {

    private Logger logger = LoggerFactory.getLogger(VerificationServiceImpl.class);

    @Autowired
    private VerificationDao verificationDao;

    @Autowired
    private VerifyService verifyService;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = verificationDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能找到相关信息的记录");
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData verify(String openid, String idCard, String name) {
        ResultData result = new ResultData();

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("idCard", idCard);
        queryMap.put("name", name);
        ResultData existResult = verificationDao.query(queryMap);
        if (existResult.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(Boolean.valueOf(true));
            return result;
        }
        if (existResult.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            logger.error("query error : " + existResult.getDescription());
        }

        ResultData verifyResult = verifyService.check(name, idCard);

        if (verifyResult.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(Boolean.valueOf(true));
            VerifyInfo info = new VerifyInfo(openid, idCard, name);
            verificationDao.insert(info);
        } else {
            result.setData(Boolean.valueOf(false));
            //logger.warn("result : " + resp.getResult() + " , description : " + resp.getDescription());
        }
        result.setDescription(verifyResult.getDescription());

        return result;
    }
}
