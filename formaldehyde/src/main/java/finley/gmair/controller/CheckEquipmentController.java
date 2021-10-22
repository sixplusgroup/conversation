package finley.gmair.controller;

import finley.gmair.service.CheckEquipmentService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/formaldehyde/check/equipment")
@RestController
public class CheckEquipmentController {

    @Autowired
    private CheckEquipmentService checkEquipmentService;
    @GetMapping("/fetch")
    public ResultData getEquipmentById(String equipmentId){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(equipmentId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the equipmentId");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("equipmentId",equipmentId);
        condition.put("blockFlag",false);
        ResultData response = checkEquipmentService.fetch(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch equipment");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find equipment");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to fetch equipment");
        return result;
    }
}
