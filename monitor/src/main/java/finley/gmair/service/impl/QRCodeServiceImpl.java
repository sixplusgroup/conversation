package finley.gmair.service.impl;

import finley.gmair.dao.QRCodeDao;
import finley.gmair.service.QRCodeService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author fan
 * @create_time 2019-2019/2/27 10:45 AM
 */
@Service
public class QRCodeServiceImpl implements QRCodeService {
    private Logger logger = LoggerFactory.getLogger(QRCodeServiceImpl.class);

    @Autowired
    private QRCodeDao qrCodeDao;

    @Override
    public ResultData fetchProfile(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = qrCodeDao.queryProfile(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else{
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    @Override
    public ResultData fetchAds(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = qrCodeDao.queryAds(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else{
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }
}
