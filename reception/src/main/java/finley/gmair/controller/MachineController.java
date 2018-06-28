package finley.gmair.controller;

import finley.gmair.model.machine.Ownership;
import finley.gmair.service.AuthConsumerService;
import finley.gmair.service.MachineService;
import finley.gmair.service.RepositoryService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/reception/machine")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private AuthConsumerService authConsumerService;

    @PostMapping("/deviceinit")
    public ResultData deviceInit(String qrcode, String deviceName) {
        ResultData result = new ResultData();
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String consumerId = (String) authConsumerService.getConsumerId(phone).getData();
        ResultData response = machineService.bindConsumerWithQRcode(consumerId, deviceName, qrcode, Ownership.OWNER.getValue());
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to init");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to init");
        }
        return result;
    }

    /**
     * This method helps to check whether the qrcode is online or not
     *
     * @return online result, RESPONSE_OK & RESPONSE_NULL
     * @Param qrcode, the code value of the specified machine
     */
    @GetMapping("/checkonline")
    public ResultData checkOnline(String qrcode) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all the information");
            return result;
        }
        //check whether the qrcode is online
        ResultData response = machineService.findMachineIdByCodeValue(qrcode);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("is not online");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Object> preBindCodeList = (ArrayList<Object>) response.getData();
            LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap<String, Object>) (preBindCodeList.get(0));
            String machineId = (String) linkedHashMap.get("machineId");
            response = repositoryService.isOnilne(machineId);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("is online");
                return result;
            } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("is not online");
                return result;
            }
        }

        return result;
    }

    @PostMapping("/distribution")
    public ResultData distribution(String consumerId) {
        ResultData result = new ResultData();
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
    public ResultData configComponentStatus(@PathVariable("component") String component, @PathVariable("operation") String operation, String qrcode) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(component) || StringUtils.isEmpty(operation) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure all required information is provided");
            return result;
        }

        ResultData response = machineService.chooseComponent(qrcode,component,operation);
        if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to operate");
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to operate");
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
