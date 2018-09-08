package finley.gmair.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.service.CoreService;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * This method is responsible to fetch response from machine-agent
 */
@CrossOrigin
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
    public ResultData configControlOption(String optionName,String optionComponent,String modelId,String actions){
        ResultData result = new ResultData();
        JSONArray actionList = JSONArray.parseArray(actions);
        for (int i=0;i<actionList.size();i++){
            JSONObject action = actionList.getJSONObject(i);
            ResultData response = machineService.setControlOption(optionName,optionComponent,modelId,
                    action.getString("actionName"),
                    action.getString("actionOperator"),
                    action.getString("commandValue"));
            if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("some error happen when create control option");
                return result;
            }
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("success to create control option with its actions");
        return result;
    }
}
