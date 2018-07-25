package finley.gmair.service.impl;

import finley.gmair.dao.GoodsModelDao;
import finley.gmair.dao.QRCodeDao;
import finley.gmair.model.machine.QRCode;
import finley.gmair.model.machine.QRCodeStatus;
import finley.gmair.service.QRCodeService;
import finley.gmair.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/28
 */
@Service
public class QRCodeServiceImpl implements QRCodeService {
    @Autowired
    private QRCodeDao qrCodeDao;

    @Autowired
    private GoodsModelDao goodsModelDao;

    @Override
    @Transactional
    public ResultData create(String goodsId, String modelId, String batchValue, int num) {
        ResultData result = new ResultData();
        if (num < 0) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("需要批量生成的二维码的数量为:" + num + ", 无需进行处理");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("modelId", modelId);
        condition.put("goodsId", goodsId);
        ResultData resposne = goodsModelDao.query(condition);
        if (resposne.getResponseCode() == ResponseCode.RESPONSE_OK) {
            DecimalFormat principle = new DecimalFormat("000");
            for (int i = 0; i < num; i++) {
                String value = new StringBuffer(batchValue).append(principle.format(i)).append(QRSerialGenerator.generate()).toString();
                String url = "https://" + MachineProperties.getValue("domain_url") + MachineProperties.getValue("qrcode_base") + value;
                QRCode code = new QRCode(modelId, batchValue, value, url, QRCodeStatus.CREATED);
                resposne = qrCodeDao.insert(code);
                if (resposne.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(ResponseCode.RESPONSE_OK);
                    result.setData(resposne.getData());
                } else {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("QRcode store error");
                }
            }
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("二维码一致性错误，服务器不予处理");
        }
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = qrCodeDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No qrcode found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve qrcode from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData fetchBatch(Map<String, Object> condition) {
        return qrCodeDao.queryBatch(condition);
    }

    @Override
    public ResultData modifyByQRcode(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = qrCodeDao.updateByQRcode(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to modify qrcode from database");
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("Success to modify qrcode from database");
        }
        return result;
    }
}