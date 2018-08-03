package finley.gmair.controller;

import finley.gmair.model.machine.ModelLight;
import finley.gmair.service.ModelLightService;
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
@RequestMapping("/machine/model/light")
public class ModelLightController {

    @Autowired
    private ModelLightService modelLightService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultData createModelLight(String modelId, int minLight, int maxLight) {

        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(modelId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide modelId");
            return result;
        }

        //create model light
        ModelLight modelLight = new ModelLight(modelId, minLight, maxLight);
        ResultData response = modelLightService.create(modelLight);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to create model light");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
            result.setDescription("success to create model light");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/probe/by/modelId", method = RequestMethod.GET)
    public ResultData probeModelLightByModelId(String modelId) {
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
        ResultData response = modelLightService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to probe light by modelId");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find light by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to find light by modelId");
        result.setData(response.getData());
        return result;
    }

    @RequestMapping(value = "/modify/by/modelId", method = RequestMethod.POST)
    public ResultData modifyModelLightByModelId(String modelId,String minLight,String maxLight) {
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
        condition.put("minLight", minLight);
        condition.put("maxLight", minLight);
        condition.put("blockFlag", false);
        ResultData response = modelLightService.updateByModelId(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to modify light by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to modify light by modelId");
        result.setData(response.getData());
        return result;
    }
}
