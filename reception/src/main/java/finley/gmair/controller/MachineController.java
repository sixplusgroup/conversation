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

    @GetMapping("/check/existname")
    public ResultData checkDeviceNameExist(String qrcode, String deviceName) {
        ResultData result = new ResultData();
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String consumerId = (String) authConsumerService.getConsumerId(phone).getData();
        ResultData response = machineService.checkDeviceNameExist(consumerId,deviceName,qrcode);
        if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription(response.getDescription());
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("this device name can be used");
            return result;
        }
        return result;
    }

    //设备初始化时 将qrcode和consumerId绑定
    @PostMapping("/deviceinit")
    public ResultData deviceInit(String qrcode, String deviceName) {
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(qrcode)||StringUtils.isEmpty(deviceName)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String consumerId = (String) authConsumerService.getConsumerId(phone).getData();
        return machineService.bindConsumerWithQRcode(consumerId, deviceName, qrcode, Ownership.OWNER.getValue());
    }

    @RequestMapping(value = "/consumer/qrcode/unbind", method = RequestMethod.POST)
    public ResultData unbindConsumerWithQRcode(String qrcode){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(qrcode)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String consumerId = (String) authConsumerService.getConsumerId(phone).getData();
        return machineService.unbindConsumerWithQRcode(consumerId,qrcode);
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



    @PostMapping("/qrcode/status")
    public ResultData findStatusByQRcode(String qrcode) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all the information");
            return result;
        }
        ResultData response = machineService.checkQRcodeExist(qrcode);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("sorry,we can't find the qrcode");
            return result;
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get the status by qrcode");
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

    //发送遥控信息
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
            result.setDescription(response.getDescription());
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to operate");
            return result;
        }

        return result;
    }


    //设置配置项
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

    //根据modelId查询ControlOption
    @GetMapping("/control/option/probe")
    public ResultData probeControlOption(String modelId){
        return machineService.probeControlOptionByModelId(modelId);
    }

    //根据当前的qrcode查询这台机器的各种值(co2,pm2.5等)
    @RequestMapping(value = "/info/probe", method = RequestMethod.GET)
    public ResultData getMachineInfo(String qrcode) {
        return machineService.getMachineStatusByQRcode(qrcode);
    }

    //根据consumerId获取用户的machine list
    @RequestMapping(value = "/devicelist", method = RequestMethod.GET)
    public ResultData getUserDeviceList(){
        ResultData result = new ResultData();
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String consumerId = (String) authConsumerService.getConsumerId(phone).getData();
        ResultData response = machineService.getMachineListByConsumerId(consumerId);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get machine list");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find");
            return result;
        }else{
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        }
    }


}
