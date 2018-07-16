package finley.gmair.controller;

import finley.gmair.service.CoreService;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * This method is responsible to fetch response from machine-agent
 */
@RestController
@RequestMapping("/management/machine")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @Autowired
    private CoreService coreService;


    @GetMapping("/model/list")
    public ResultData modelList() {
        return machineService.modelList();
    }

    @GetMapping("/batch/list")
    public ResultData batchList(String modelId) {
        return machineService.batchList(modelId);
    }

    @PostMapping("/qrcode/check")
    public ResultData checkQrcode(String candidate) {
        return machineService.check(candidate);
    }

    @GetMapping("/core/repo/{machineId}/online")
    public ResultData online(@PathVariable("machineId") String machineId) {
        return coreService.isOnline(machineId);
    }

    @PostMapping("/config/control/option")
    public ResultData configControlOption(String optionName,String optionComponent,String modelId,String actionName,String actionOperator,String commandValue){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(optionName)||StringUtils.isEmpty(optionComponent)||StringUtils.isEmpty(modelId)||StringUtils.isEmpty(actionName)||StringUtils.isEmpty(actionOperator)||StringUtils.isEmpty(commandValue)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        ResultData response = machineService.setControlOption(optionName,optionComponent,modelId,actionName,actionOperator,commandValue);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to config");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to config machine control option");
            return result;
        }
        return result;
    }
}
