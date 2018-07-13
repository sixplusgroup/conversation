package finley.gmair.controller;

import com.sun.research.ws.wadl.Link;
import finley.gmair.model.machine.MachineDefaultLocation;
import finley.gmair.model.machine.v2.MachineStatus;
import finley.gmair.service.AirqualityService;
import finley.gmair.service.AuthConsumerService;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


@RestController
@RequestMapping("/reception/airquality")
public class AirqualityController {
    @Autowired
    private MachineService machineService;

    @Autowired
    private AirqualityService airqualityService;




    //根据qrcode给出那个城市当天的pm2.5记录
    @RequestMapping(value="/city/hourly/aqi",method = RequestMethod.GET)
    ResultData getCityHourlyAqi(String qrcode){
        ResultData result = new ResultData();

        //check empty
        if(StringUtils.isEmpty(qrcode)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("input can not be empty");
            return result;
        }
        //find the cityId by qrcode
        ResultData response = machineService.probeCityIdByQRcode(qrcode);
        if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find");
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        }
        List<LinkedHashMap> linkedHashMaps = (List<LinkedHashMap>) response.getData();
        String cityId = (String)linkedHashMaps.get(0).get("cityId");

        response = airqualityService.getHourlyCityAqi(cityId);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setData(response.getData());
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to get city hourly airquality");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("sorry,can not get any data");
            return result;
        }

        return result;
    }

    //根据qrcode给出那个城市当周的pm2.5记录
    @RequestMapping(value="/city/daily/aqi",method = RequestMethod.GET)
    ResultData getCityDailyAqi(String qrcode){
        ResultData result = new ResultData();

        //check empty
        if(StringUtils.isEmpty(qrcode)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("input can not be empty");
            return result;
        }

        //find the cityId by qrcode
        ResultData response = machineService.probeCityIdByQRcode(qrcode);
        if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find");
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now.");
            return result;
        }
        List<LinkedHashMap> linkedHashMaps = (List<LinkedHashMap>) response.getData();
        String cityId = (String)linkedHashMaps.get(0).get("cityId");

        response = airqualityService.getDailyCityAqi(cityId);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setData(response.getData());
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to get city weekly airquality");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("sorry,can not get any data!");
            return result;
        }

        return result;
    }

    //根据用户选择的城市返回该城市空气质量
    @RequestMapping(value="/change/defaultcity/",method = RequestMethod.GET)
    ResultData getCityHourlyAqi(String qrcode,String cityId){
        ResultData result = new ResultData();

        //check empty
        if(StringUtils.isEmpty(qrcode)||StringUtils.isEmpty(cityId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("input can not be empty");
            return result;
        }

        //fetch the city hourly pm2.5
        ResultData response = airqualityService.getHourlyCityAqi(cityId);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get city hourly airquality");
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("sorry,can not get any data");
        }

        //update default location
        response = machineService.updateCityIdByQRcode(cityId,qrcode);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setDescription(result.getDescription()+",success to change the default city!");
        }
        else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setDescription(result.getDescription()+",but fail to change the default city!");
        }
        return result;
    }
}
