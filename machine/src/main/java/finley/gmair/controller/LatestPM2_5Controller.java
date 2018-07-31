package finley.gmair.controller;

import finley.gmair.model.machine.LatestPM2_5;
import finley.gmair.service.LatestPM2_5Service;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/machine/latest/pm2_5")
public class LatestPM2_5Controller {
    @Autowired
    private LatestPM2_5Service latestPM2_5Service;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultData createLatestPM2_5(String machineId, double pm2_5) {

        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(machineId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide machineId");
            return result;
        }

        //create boundary pm2.5
        LatestPM2_5 latestPM2_5 = new LatestPM2_5(machineId, pm2_5);
        ResultData response = latestPM2_5Service.create(latestPM2_5);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to create latest pm2.5");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
            result.setDescription("success to create latest pm2.5");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/probe/by/machineId", method = RequestMethod.GET)
    public ResultData probeLatestPM2_5ByMachineId(String machineId) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(machineId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the machineId");
            return result;
        }

        //probe boundary pm2.5 by modelId
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", machineId);
        condition.put("blockFlag", false);
        ResultData response = latestPM2_5Service.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to probe latest pm2.5 by modelId");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find latest pm2.5 by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to find latest pm2.5 by modelId");
        result.setData(response.getData());
        return result;
    }

    @RequestMapping(value = "/modify/by/machineId", method = RequestMethod.POST)
    public ResultData modifyLatestPM2_5ByModelId(String machineId,String pm2_5) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(machineId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the machineId");
            return result;
        }

        //modify
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", machineId);
        condition.put("pm2_5", pm2_5);
        condition.put("blockFlag", false);
        ResultData response = latestPM2_5Service.updateByMachineId(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to modify latest pm2.5 by modelId");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to modify latest pm2.5 by modelId");
        result.setData(response.getData());
        return result;
    }
}
