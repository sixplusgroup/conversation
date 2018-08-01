package finley.gmair.controller;

import finley.gmair.model.machine.MachineDefaultLocation;
import finley.gmair.service.MachineDefaultLocationService;
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
@RequestMapping("/machine/default/location")
public class MachineDefaultLocationController {
    @Autowired
    private MachineDefaultLocationService machineDefaultLocationService;

    @RequestMapping(value="/bind/cityid",method=RequestMethod.POST)
    public ResultData bindCityIdWithQRcode(String cityId,String qrcode){
        ResultData result = new ResultData();
        //check empty
        if(StringUtils.isEmpty(cityId)||StringUtils.isEmpty(qrcode)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        //insert (cityId,qrcode) into database
        MachineDefaultLocation machineDefaultLocation = new MachineDefaultLocation();
        machineDefaultLocation.setCityId(cityId);
        machineDefaultLocation.setCodeValue(qrcode);
        ResultData response = machineDefaultLocationService.create(machineDefaultLocation);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to bind");
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        }
        return result;
    }

    @RequestMapping(value="/probe/cityid",method = RequestMethod.GET)
    public ResultData probeCityIdByQRcode(String qrcode){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(qrcode)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        Map<String,Object> condition = new HashMap<>();
        condition.put("codeValue",qrcode);
        condition.put("blockFlag",false);
        ResultData response = machineDefaultLocationService.fetch(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to find cityId by qrcode");
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        }else {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can't find cityId by qrcode");
            return result;
        }
    }

    @RequestMapping(value="/update/cityid",method = RequestMethod.POST)
    public ResultData updateCityIdByQRcode(String cityId,String qrcode){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(qrcode)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }

        //查询qrcode是否已经存在于machine_default_location中,若不存在,则插入,若存在,则update
        Map<String,Object> condition = new HashMap<>();
        condition.put("codeValue",qrcode);
        condition.put("blockFlag",false);
        ResultData response = machineDefaultLocationService.fetch(condition);

        //qrcode已存在,则更新
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK) {
            condition.clear();
            condition.put("cityId", cityId);
            condition.put("codeValue", qrcode);
            condition.put("blockFlag", false);
            response = machineDefaultLocationService.modify(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                result.setDescription("success to update");
                return result;
            } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("fail to update");
                return result;
            }
        }
        //qrcode不存在,则插入
        else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            MachineDefaultLocation machineDefaultLocation = new MachineDefaultLocation();
            machineDefaultLocation.setCityId(cityId);
            machineDefaultLocation.setCodeValue(qrcode);
            response = machineDefaultLocationService.create(machineDefaultLocation);
            if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                result.setDescription("success to bind");
                return result;
            }else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("fail to create");
                return result;
            }
        }
        return result;
    }
}
