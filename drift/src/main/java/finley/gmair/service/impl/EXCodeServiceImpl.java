package finley.gmair.service.impl;

import finley.gmair.dao.ActivityDao;
import finley.gmair.dao.EXCodeDao;
import finley.gmair.model.drift.Activity;
import finley.gmair.model.drift.EXCode;
import finley.gmair.model.drift.EXCodeStatus;
import finley.gmair.service.EXCodeService;
import finley.gmair.util.EXSerialGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EXCodeServiceImpl implements EXCodeService {

    @Autowired
    private EXCodeDao exCodeDao;

    @Override
    public ResultData createEXCode(String activityId, int status, int num, double price) {
        ResultData result = new ResultData();
        if (num < 0) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("需要批量生成的兑换码的数量为:" + num + ", 无需进行处理");
            return result;
        }
        List<EXCode> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            String codeValue = new StringBuffer(EXSerialGenerator.generate()).toString();
            EXCode code = new EXCode(activityId, codeValue, price);
            if (status == 1) {
                code.setStatus(EXCodeStatus.EXCHANGED);
            }
            list.add(code);
        }
        ResultData response = new ResultData();
        for (EXCode code : list) {
            response = exCodeDao.insert(code);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("EXCode insert error");
            }
        }
        return result;
    }

    @Override
    public ResultData fetchEXCode(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = exCodeDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No EXCode found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve EXCode from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData modifyEXCode(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = exCodeDao.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to modify EXCode from database");
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("Success to modify EXCode from database");
        }
        return result;
    }
}
