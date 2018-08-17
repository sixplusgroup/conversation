package finley.gmair.controller;


import finley.gmair.form.air.MachineAirQualityForm;
import finley.gmair.model.air.MachineAirQuality;
import finley.gmair.model.machine.BoardVersion;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/machine")
public class MachineAirQualityController {

    @Autowired
    private MachineAirQualityService machineAirQualityService;

    @Autowired
    private MachineStatusCacheService machineStatusCacheService;

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    @Autowired
    private MachineV1StatusCacheService machineV1StatusCacheService;

    @Autowired
    private BoardVersionService boardVersionService;

    @RequestMapping(value = "/qirquality/create", method = RequestMethod.POST)
    private ResultData createMachineAirQuality(MachineAirQualityForm form) {
        ResultData result = new ResultData();

        if (form.getQrcode() == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("parameter qrcode is required");
            return result;
        }

        MachineAirQuality machineAirQuality = new MachineAirQuality();
        machineAirQuality.setQrcode(form.getQrcode());
        machineAirQuality.setPm25(form.getPm25());
        machineAirQuality.setHumidity(form.getHumidity());
        machineAirQuality.setTemperature(form.getTemperature());
        machineAirQuality.setCo2(form.getCo2());

        ResultData response = machineAirQualityService.add(machineAirQuality);

        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器忙，请稍后再试.");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else {
            result.setData(response.getData());
        }

        return result;
    }

    //从缓存中获取v2的machineStatus
    @RequestMapping (value = "/status/{uid}",method = RequestMethod.GET)
    public ResultData machineV2Status(@PathVariable("uid") String uid) {
        ResultData result = new ResultData();
        MachineStatus machineStatus = machineStatusCacheService.fetch(uid);
        if(machineStatus==null){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find machine status in cache");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(machineStatusCacheService.fetch(uid));
        result.setDescription("success to find machine status in cache");
        return result;
    }

    //才能够缓存中获取v1的machineStatus
    @RequestMapping (value = "/v1/status", method = RequestMethod.GET)
    public ResultData machineV1Status(String uid){
        ResultData result = new ResultData();
        finley.gmair.model.machine.v1.MachineStatus machineStatus = machineV1StatusCacheService.fetch(uid);
        if(machineStatus==null){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find machine v1 status in cache");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(machineStatus);
        result.setDescription("success to find machine v1 status in cache");
        return result;
    }

    //根据qrcode解析machineId,version,进而通过缓存获取machineStatus
    @RequestMapping (value ="/status/byqrcode",method = RequestMethod.GET)
    public ResultData getMachineStatusByQRcode(String qrcode){
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the machineId by qrcode");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find the machineId by qrcode.");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to find the machineId by qrcode.");
        }
        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();
        condition.clear();
        condition.put("machineId",machineId);
        condition.put("blockFlag",false);
        response = boardVersionService.fetchBoardVersion(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find board version by machineId");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find borad version by machineId");
            return result;
        }
        int version = ((List<BoardVersion>)response.getData()).get(0).getVersion();
        switch (version){
            case 1: return machineV1Status(machineId);
            case 2: return machineV2Status(machineId);
            default:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("this board version wrong");
                return result;
        }
    }
}
