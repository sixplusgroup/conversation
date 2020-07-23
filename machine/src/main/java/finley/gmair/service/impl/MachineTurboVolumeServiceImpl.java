package finley.gmair.service.impl;

import finley.gmair.dao.MachineTurboVolumeDao;
import finley.gmair.dao.ModelVolumeDao;
import finley.gmair.dao.QRCodeDao;
import finley.gmair.model.machine.MachineTurboVolume;
import finley.gmair.model.machine.QRCode;
import finley.gmair.service.MachineTurboVolumeService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/18 15:37
 * @description: TODO
 */

@Service
public class MachineTurboVolumeServiceImpl implements MachineTurboVolumeService {

    @Autowired
    private MachineTurboVolumeDao machineTurboVolumeDao;

    @Autowired
    private ModelVolumeDao modelVolumeDao;

    @Autowired
    private QRCodeDao qrCodeDao;

    @Override
    public ResultData create(String qrcode) {
        ResultData res = new ResultData();

        //检测参数
        if (StringUtils.isEmpty(qrcode)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return res;
        }

        //检测是否已存在此二维码
        ResultData response = fetchByQRCode(qrcode);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("qrcode already exists");
            return res;
        }

        MachineTurboVolume machineTurboVolume = new MachineTurboVolume(qrcode, false);
        return machineTurboVolumeDao.add(machineTurboVolume);
    }

    @Override
    public ResultData modify(Map<String, Object> condition) {
        return machineTurboVolumeDao.update(condition);
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return machineTurboVolumeDao.query(condition);
    }

    @Override
    public ResultData fetchByQRCode(String qrcode) {
        ResultData res = new ResultData();

        //检测参数
        if (StringUtils.isEmpty(qrcode)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return res;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("qrcode", qrcode);
        condition.put("blockFlag", false);
        return fetch(condition);
    }

    @Override
    public ResultData getTurboVolumeValue(String qrcode) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>(5);
        condition.put("codeValue",qrcode);
        condition.put("blockFlag",false);
        ResultData responseModel = qrCodeDao.query(condition);
        if (responseModel.getResponseCode() == ResponseCode.RESPONSE_OK){
            QRCode qrCode = ((List<QRCode>)responseModel.getData()).get(0);
            ResultData response = modelVolumeDao.queryTurboVolumeValue(qrCode.getModelId());
            if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("is not need turbo volume");
            }
            else {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                Map<String,Object> resultData = new HashMap<>(5);
                resultData.put("qrcode",qrcode);
                resultData.put("turboVolume",response.getData());
                result.setData(resultData);
                result.setDescription("is need turbo volume");
            }
        }
        else {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no such qrcode");
        }
        return result;
    }
}
