package finley.gmair.controller;

import com.netflix.discovery.converters.Auto;
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
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    private MachineQrcodeBindService machineQrcodeBindService;

    @Autowired
    private ControlOptionService controlOptionService;

    @Autowired
    private CoreService coreService;

    @Autowired
    private QRCodeService qrCodeService;

    //先查control_option表,如果对应的操作不存在,则创建.
    //如果存在,取出controlId,并根据传入的值新建control_option_action配置.
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
            controlId = ((List<ControlOption>) response.getData()).get(0).getControlId();
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

    @PostMapping(value = "/probe/bymodelid")
    public ResultData probeControlOptionByModelId(String modelId){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(modelId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the modelId");
            return result;
        }
        Map<String,Object> condition = new HashMap<>();
        condition.put("modelId",modelId);
        condition.put("blockFlag",false);
        ResultData response = controlOptionService.fetchControlOptionActionByModelId(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to probe control option by modelId");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find control option by modelId");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find control option by modelId");
        result.setResponseCode(ResponseCode.RESPONSE_OK);
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

        //根据qrcode 查 code_machine_bind表,取出machineId
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("sorry, can not find the qrcode or server is busy");
            return result;
        }
        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();

        //根据qrcode 查 qrcode表, 取出机器型号modelId
        response = qrCodeService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("sorry, can not find the qrcode or server is busy");
            return result;
        }
        String modelId = ((List<QRCode>) response.getData()).get(0).getModelId();

        //根据component查control_option表, 取出component对应的controlId
        condition.clear();
        condition.put("optionComponent", component);
        condition.put("blockFlag", false);
        response = controlOptionService.fetchControlOption(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("sorry, can not find the controlId with component");
            return result;
        }
        String controlId = ((List<ControlOption>) response.getData()).get(0).getControlId();

        //根据controlId,modelId and operation查control_action表, 取出应传给core模块的值commandValue
        condition.clear();
        condition.put("controlId", controlId);
        condition.put("modelId", modelId);
        condition.put("actionOperator", operation);
        condition.put("blockFlag", false);
        response = controlOptionService.fetchControlOptionAction(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find the action value with controlId,modelId,actionOperator");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the action value with controlId,modelId,actionOperator");
            return result;
        }
        int commandValue = ((List<ControlOptionActionVo>) response.getData()).get(0).getCommandValue();

        //according the value to control the machine
        if (component.equals("power"))
            response = coreService.configPower(machineId, commandValue);
        else if (component.equals("lock"))
            response = coreService.configLock(machineId, commandValue);
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
            result.setDescription("No channel found for the uid");
            return result;
        }
        return result;
    }


    //用户操作风量
    @RequestMapping(value="/config/speed",method = RequestMethod.POST)
    public ResultData configSpeed(String qrcode, int speed){

        ResultData result = new ResultData();
        //check empty
        if (ControlOptionController.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("provide all the speed");
            return result;
        }

        //根据qrcode 查 code_machine_bind表,取出machineId
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("sorry, can not find the qrcode");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the machine by qrcode");
            return result;
        }
        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();
        return coreService.configSpeed(machineId,speed);

    }

    //用户调节亮度
    @RequestMapping(value="/config/light",method = RequestMethod.POST)
    public ResultData configLight(String qrcode, int light){

        ResultData result = new ResultData();
        //check empty
        if (ControlOptionController.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the light");
            return result;
        }

        //根据qrcode 查 code_machine_bind表,取出machineId
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("sorry, can not find the qrcode");
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the machineId by qrcode");
            return result;
        }
        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();
        return coreService.configSpeed(machineId,light);

    }


    public static boolean isEmpty(String... args) {
        for (String arg : args) {
            if (StringUtils.isEmpty(arg))
                return true;
        }
        return false;
    }
}