package finley.gmair.service.impl;

import finley.gmair.dao.QrExCodeDao;
import finley.gmair.model.drift.QR_EXcode;
import finley.gmair.service.QrExCodeService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class QrExCodeServiceImpl implements QrExCodeService {

    @Autowired
    private QrExCodeDao qrExCodeDao;

    @Override
    public ResultData createQrExCode(QR_EXcode code) {
        ResultData result = new ResultData();
        ResultData response = qrExCodeDao.insert(code);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert code to database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData fetchQrExCode(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = qrExCodeDao.query(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No code found");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to retrieve code");
                break;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
        }
        return result;
    }
}
