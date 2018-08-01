package finley.gmair.controller;

import finley.gmair.model.machine.BoundaryPM2_5;
import finley.gmair.service.BoundaryPM2_5Service;
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
@RequestMapping("/machine/boundary/pm2_5")
public class BoundaryPM2_5Controller {

    @Autowired
    private BoundaryPM2_5Service boundaryPM2_5Service;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultData createBoundaryPM2_5(String modelId, int pm2_5_info, int pm2_5_warning) {

        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(modelId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide modelId");
            return result;
        }

        //create boundary pm2.5
        BoundaryPM2_5 boundaryPM2_5 = new BoundaryPM2_5(modelId, pm2_5_info, pm2_5_warning);
        ResultData response = boundaryPM2_5Service.create(boundaryPM2_5);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to create boundary pm2.5");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
            result.setDescription("success to create boundary pm2.5");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/probe/by/modelId", method = RequestMethod.GET)
    public ResultData probeBoundaryPM2_5ByModelId(String modelId) {
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
        ResultData response = boundaryPM2_5Service.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to probe boundary pm2.5 by modelId");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find boundary pm2.5 by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to find boundary pm2.5 by modelId");
        result.setData(response.getData());
        return result;
    }

    @RequestMapping(value = "/modify/by/modelId", method = RequestMethod.POST)
    public ResultData modifyBoundaryPM2_5ByModelId(String modelId,String pm2_5_info,String pm2_5_warning) {
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
        condition.put("pm2_5_info", pm2_5_info);
        condition.put("pm2_5_warning", pm2_5_warning);
        condition.put("blockFlag", false);
        ResultData response = boundaryPM2_5Service.updateByModelId(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to modify boundary pm2.5 by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to modify boundary pm2.5 by modelId");
        result.setData(response.getData());
        return result;
    }
}
