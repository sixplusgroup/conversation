package finley.gmair.controller;

import finley.gmair.form.machine.ControlOptionForm;
import finley.gmair.model.machine.ControlOption;
import finley.gmair.model.machine.ControlOptionAction;
import finley.gmair.model.machine.PreBindCode;
import finley.gmair.service.ControlOptionService;
import finley.gmair.service.CoreService;
import finley.gmair.service.PreBindService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.ControlOptionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/22
 */
@RestController
@RequestMapping("/machine/control/option")
public class ControlOptionController {

    @Autowired
    private ControlOptionService controlOptionService;

    @Autowired
    private PreBindService preBindService;

    @Autowired
    private CoreService coreService;

    //创建ControlOption
    @PostMapping(value = "/create")
    public ResultData setControlOption(ControlOptionForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getOptionName()) || StringUtils.isEmpty(form.getOptionComponent())
                || StringUtils.isEmpty(form.getModelId()) || StringUtils.isEmpty(form.getActionName())
                || StringUtils.isEmpty(form.getActionOperator())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }

        String controlId = null;
        Map<String, Object> condition = new HashMap<>();
        condition.put("optionName", form.getOptionName());
        condition.put("optionComponent", form.getOptionComponent());
        ResultData response = controlOptionService.fetchControlOption(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("System parse error, please try later");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            controlId = ((List<ControlOptionVo>) response.getData()).get(0).getControlId();
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            ControlOption option = new ControlOption(form.getOptionName(), form.getOptionComponent());
            response = controlOptionService.createControlOption(option);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(response.getDescription());
                return result;
            }
            controlId = ((ControlOption) response.getData()).getControlId();
        }
        ControlOptionAction action = new ControlOptionAction(controlId, form.getModelId(),
                form.getActionName(), form.getActionOperator());
        response = controlOptionService.createControlOptionAction(action);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    //用户操作机器
    @PostMapping("/operate")
    public ResultData chooseComponent(String qrcode, String component, String operation) {
        ResultData result = new ResultData();
        //check empty
        if (ControlOptionController.isEmpty(qrcode, component, operation)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("provide all the information");
            return result;
        }

        //according to qrcode find the machineId
        Map<String, Object> condition = new HashMap<>();
        condition.put("qrcode", qrcode);
        condition.put("blockFlag", false);
        ResultData response = preBindService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("sorry, can not find the qrcode or server is busy");
            return result;
        }
        String machineId = ((List<PreBindCode>) response.getData()).get(0).getMachineId();

        //according the component and opration to control the machine
        if (component.equals("power")) {
            int power = 0;
            if (operation.equals("off")) {
                power = 0;
            } else if (operation.equals("on")) {
                power = 1;
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("power value must be 0 or 1");
                return result;
            }
            ResultData optionResponse = coreService.configPower(machineId,power);
            if(optionResponse.getResponseCode() == ResponseCode.RESPONSE_OK){
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("success");
                return result;
            }else if(optionResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR){
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("No channel found for machine");
                return result;
            }

        } else if (component.equals("lock")) {
            int lock = 0;
            if (operation.equals("off")) {
                lock = 0;
            } else if (operation.equals("on")) {
                lock = 1;
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("lock value must be 0 or 1");
                return result;
            }
            ResultData optionResponse = coreService.configLock(machineId,lock);
            if(optionResponse.getResponseCode() == ResponseCode.RESPONSE_OK){
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("success.");
                return result;
            }else if(optionResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR){
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("No channel found for machineId");
                return result;
            }
        } else if (component.equals("light")) {
            int light = 0;
            if (operation.equals("0")) {
                light = 0;
            } else if (operation.equals("1")) {
                light = 1;
            } else if(operation.equals("2")){
                light = 2;
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("light value must be 0 or 1 or 2");
                return result;
            }
            ResultData optionResponse = coreService.configLight(machineId,light);
            if(optionResponse.getResponseCode() == ResponseCode.RESPONSE_OK){
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("success!");
                return result;
            }else if(optionResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR){
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("No channel found for machineId");
                return result;
            }
        } else if (component.equals("heat")) {
            int heat = 0;
            if(operation.equals("0")){
                heat = 0;
            } else if (operation.equals("500")){
                heat = 1;
            } else if (operation.equals("1000")){
                heat = 2;
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("heat value must be 0 or 500 or 1000");
                return result;
            }
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("no component");
            return result;
        }
        return result;
    }

    public static boolean isEmpty(String... args) {
        for (String arg : args) {
            if (StringUtils.isEmpty(arg))
                return true;
        }
        return false;
    }
}