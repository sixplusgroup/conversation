package finley.gmair.controller;

import finley.gmair.form.machine.ControlOptionForm;
import finley.gmair.model.machine.ControlOption;
import finley.gmair.model.machine.ControlOptionAction;
import finley.gmair.model.machine.PreBindCode;
import finley.gmair.model.machine.QRCode;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.ControlOptionActionVo;
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

    @Autowired
    private QRCodeService qrCodeService;

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
                form.getActionName(), form.getActionOperator(), form.getCommandValue());
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

        //according to qrcode find the modelId
        response = qrCodeService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("sorry, can not find the qrcode or server is busy");
            return result;
        }
        String modelId = ((List<QRCode>) response.getData()).get(0).getModelId();

        //according to the component find the control_id
        condition.clear();
        condition.put("optionComponent", component);
        condition.put("blockFlag", false);
        response = controlOptionService.fetchControlOption(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("sorry, can not find the option or server is busy");
            return result;
        }
        String controlId = ((List<ControlOptionVo>) response.getData()).get(0).getControlId();

        //according the  controlId,modelId and operation find the commandValue
        condition.clear();
        condition.put("controlId", controlId);
        condition.put("modelId", modelId);
        condition.put("actionOperator", operation);
        condition.put("blockFlag", false);
        response = controlOptionService.fetchControlOptionAction(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("sorry, can not find the action value or server is busy now");
            return result;
        }
        int commandValue = ((List<ControlOptionActionVo>) response.getData()).get(0).getCommandValue();

        //according the value to control the machine
        if (component.equals("power"))
            response = coreService.configPower(machineId, commandValue);
        else if (component.equals("lock"))
            response = coreService.configLock(machineId, commandValue);
        else if (component.equals("light"))
            response = coreService.configLight(machineId, commandValue);
        else if (component.equals("heat"))
            response = coreService.configHeat(machineId, commandValue);
        else if (component.equals("mode"))
            response = coreService.configMode(machineId, commandValue);
        else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("no such component");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to operate the machine");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to operate the machine");
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