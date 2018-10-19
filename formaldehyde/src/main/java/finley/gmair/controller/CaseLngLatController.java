package finley.gmair.controller;

import finley.gmair.model.formaldehyde.CaseLngLat;
import finley.gmair.service.CaseLngLatService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/formaldehyde/case/location")
public class CaseLngLatController {
    @Autowired
    private CaseLngLatService caseLngLatService;

    @GetMapping("/fetch")
    public ResultData getCaseLocationList(){
        ResultData result = new ResultData();
        ResultData response = caseLngLatService.fetch(new HashMap<>());
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find ");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to fetch case location list,list.size=" + ((List<CaseLngLat>)response.getData()).size());
        return result;
    }
}
