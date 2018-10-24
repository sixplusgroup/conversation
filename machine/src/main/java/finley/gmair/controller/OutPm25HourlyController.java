package finley.gmair.controller;

import finley.gmair.model.machine.OutPm25Hourly;
import finley.gmair.service.OutPm25HourlyService;
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
public class OutPm25HourlyController {
    @Autowired
    private OutPm25HourlyService outPm25HourlyService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultData createOutPm25Hourly(String machineId, int pm2_5) {

        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(machineId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide machineId");
            return result;
        }

        //create boundary pm2.5
        OutPm25Hourly outPm25Hourly = new OutPm25Hourly(machineId, pm2_5, 0);
        ResultData response = outPm25HourlyService.create(outPm25Hourly);
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
    public ResultData probeOutPm25ByMachineId(String machineId) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(machineId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the machineId");
            return result;
        }

        //probe pm2.5 by modelId
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", machineId);
        condition.put("blockFlag", false);
        ResultData response = outPm25HourlyService.fetch(condition);
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
    public ResultData modifyOutPm25ByModelId(String machineId, int pm2_5) {
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
        ResultData response = outPm25HourlyService.updateByMachineId(condition);
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
