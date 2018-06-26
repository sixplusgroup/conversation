package finley.gmair.controller;

import finley.gmair.model.machine.PreBindCode;
import finley.gmair.service.MachineService;
import finley.gmair.service.RepositoryService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reception/machine")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @Autowired
    private RepositoryService repositoryService;


    @GetMapping("/checkonline")
    public ResultData checkOnline(String qrcode){
        ResultData result = new ResultData();
        //check whether the qrcode is online
        ResultData response = machineService.findMachineIdByCodeValue(qrcode);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("is not online");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            String machineId = ((List<PreBindCode>)response.getData()).get(0).getMachineId();
            response = repositoryService.isOnilne(machineId);
            if(response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("is online");
                return result;
            } else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("is not online");
            }
        }
        return result;
    }
    /**
     * This method is invoked to finish user-qrcode binding
     *
     * @return bind result, RESPONSE_OK & RESPONSE_ERROR
     * @Param qrcode, the code value of the specified machine
     */
    @PostMapping("/bind")
    public ResultData bind(String qrcode) {
        ResultData result = new ResultData();
        return result;
    }

    @PostMapping("/{component}/{operation}")
    public ResultData configComponentStatus(@PathVariable("operation") String operation, String qrcode) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(operation) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure all required information is provided");
            return result;
        }

        return result;
    }

    @PostMapping("/control/option/create")
    public ResultData setControlOption(String optionName, String optionComponent, String modelId, String actionName, String actionOperator) {
        ResultData result = new ResultData();
        ResultData response = machineService.setControlOption(optionName, optionComponent, modelId, actionName, actionOperator);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("error");
            return result;
        }
        return result;
    }

}
