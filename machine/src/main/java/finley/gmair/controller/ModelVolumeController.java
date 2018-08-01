package finley.gmair.controller;

import finley.gmair.model.machine.BoundaryPM2_5;
import finley.gmair.model.machine.ModelVolume;
import finley.gmair.service.BoundaryPM2_5Service;
import finley.gmair.service.ModelVolumeService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/machine/model/volume")
public class ModelVolumeController {

    @Autowired
    private ModelVolumeService modelVolumeService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultData createModelVolume(String modelId, int minVolume, int maxVolume) {

        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(modelId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide modelId");
            return result;
        }

        //create model volume
        ModelVolume modelVolume = new ModelVolume(modelId, minVolume, maxVolume);
        ResultData response = modelVolumeService.create(modelVolume);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to create model volume");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
            result.setDescription("success to create model volume");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/probe/by/modelId", method = RequestMethod.GET)
    public ResultData probeModelVolumeByModelId(String modelId) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(modelId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the modelId");
            return result;
        }

        //probe boundary pm2.5 by modelId
        Map<String, Object> condition = new HashMap<>();
        condition.put("modelId", modelId);
        condition.put("blockFlag", false);
        ResultData response = modelVolumeService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to probe volume by modelId");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find volume by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to find volume by modelId");
        result.setData(response.getData());
        return result;
    }

    @RequestMapping(value = "/modify/by/modelId", method = RequestMethod.POST)
    public ResultData modifyModelVolumeByModelId(String modelId,String minVolume,String maxVolume) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(modelId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the modelId");
            return result;
        }

        //modify
        Map<String, Object> condition = new HashMap<>();
        condition.put("modelId", modelId);
        condition.put("minVolume", minVolume);
        condition.put("maxVolume", maxVolume);
        condition.put("blockFlag", false);
        ResultData response = modelVolumeService.updateByModelId(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to modify volume by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to modify volume by modelId");
        result.setData(response.getData());
        return result;
    }
}
