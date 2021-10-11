package finley.gmair.service.impl;

import finley.gmair.dao.ModelVolumeDao;
import finley.gmair.dao.QRCodeDao;
import finley.gmair.model.machine.ModelVolume;
import finley.gmair.model.machine.QRCode;
import finley.gmair.service.ModelVolumeService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModelVolumeServiceImpl implements ModelVolumeService {
    @Autowired
    private ModelVolumeDao modelVolumeDao;

    @Autowired
    private QRCodeDao qrCodeDao;

    @Override
    public ResultData create(ModelVolume modelVolume){
        ResultData result = new ResultData();
        ResultData response = modelVolumeDao.insert(modelVolume);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert model volume into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = modelVolumeDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch model volume.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No model volume found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find model volume");
        return result;
    }

    @Override
    public ResultData updateByModelId(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = modelVolumeDao.updateByModelId(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update volume by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to update volume by modelId");
        return result;
    }

    @Override
    public ResultData isNeedTurboVolume(String qrcode) {
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
                result.setDescription("is need turbo volume");
            }
        }
        else {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no such qrcode");
        }
        return result;
    }

    @Override
    public ResultData fetchTurboVolume(String modelId) {
        ResultData result = new ResultData();
        ResultData response = modelVolumeDao.queryTurboVolumeValue(modelId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch turbo volume.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No turbo volume found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find turbo volume");
        return result;
    }
}
