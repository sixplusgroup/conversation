package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import com.sun.research.ws.wadl.Link;
import finley.gmair.model.machine.ConsumerQRcodeBind;
import finley.gmair.service.AirqualityService;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/monitor")
@CrossOrigin
public class MonitorController {

    @Autowired
    private MachineService machineService;

    @Autowired
    private AirqualityService airqualityService;

    @GetMapping("/machine/online")
    public ResultData isOnline(String codeValue) {
        return machineService.checkOnline(codeValue);
    }

    @GetMapping("/machine/status")
    public ResultData getMachineStatus(String qrcode) {
        return machineService.getMachineStatusByQRcode(qrcode);
    }

    @GetMapping("/cityid/probe")
    public ResultData getCityId(String qrcode){ return machineService.probeCityIdByQRcode(qrcode); }

    @GetMapping("/city/air")
    public ResultData getCityLatestAirquality(String cityId, String provinceId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(cityId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide cityId");
            return result;
        }

        //先根据城市来查找空气质量
        ResultData response = airqualityService.getLatestCityAirQuality(cityId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
            result.setDescription("success to fetch the air quality by cityId");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch the air quality by cityId");
            return result;
        }

        //根据省份来查找空气质量
        response = airqualityService.provinceAirQuality(provinceId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch the air quality");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find the air quality by provinceId");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to fetch the air quality by provinceId");
        return result;
    }

    @GetMapping("/consumer/machine/list/status")
    public ResultData getMachineListStatus(String consumerId){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(consumerId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the consumerId");
            return result;
        }
        ResultData response = machineService.getMachineListByConsumerId(consumerId);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(response.getResponseCode());
            return result;
        }
        List<LinkedHashMap> list = (List<LinkedHashMap>)response.getData();
        List<Object> resultLst = new ArrayList<>();

        for(LinkedHashMap cqb:list){
            ResultData rd = machineService.getMachineStatusByQRcode((String)cqb.get("codeValue"));
            if(rd.getResponseCode()!=ResponseCode.RESPONSE_OK)
                continue;
            LinkedHashMap linkedHashMap = (LinkedHashMap) rd.getData();
            JSONObject obj = new JSONObject();
            obj.put("codeValue",cqb.get("codeValue"));
            obj.put("bindName",cqb.get("bindName"));
            obj.put("bindTime",cqb.get("createAt"));
            obj.put("uid",linkedHashMap.get("uid"));
            obj.put("pm2_5",linkedHashMap.get("pm2_5"));
            obj.put("temp",linkedHashMap.get("temp"));
            obj.put("humid",linkedHashMap.get("humid"));
            obj.put("co2",linkedHashMap.get("co2"));
            obj.put("volume",linkedHashMap.get("volume"));
            obj.put("power",linkedHashMap.get("power"));
            obj.put("mode",linkedHashMap.get("mode"));
            obj.put("heat",linkedHashMap.get("heat"));
            obj.put("light",linkedHashMap.get("light"));
            resultLst.add(obj);
        }
        result.setData(resultLst);
        return result;
    }
}
