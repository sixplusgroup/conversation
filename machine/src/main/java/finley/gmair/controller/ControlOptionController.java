package finley.gmair.controller;

import finley.gmair.form.machine.ControlOptionForm;
import finley.gmair.model.machine.*;
import finley.gmair.model.machine.v1.MachineStatus;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.ControlOptionActionVo;
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
    private CoreV1Service coreV1Service;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private BoardVersionService boardVersionService;

    @Autowired
    private MachineV1StatusCacheService machineV1StatusCacheService;

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
    public ResultData probeControlOptionByModelId(String modelId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(modelId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the modelId");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("modelId", modelId);
        condition.put("blockFlag", false);
        ResultData response = controlOptionService.fetchControlOptionActionByModelId(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to probe control option by modelId");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
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
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find the action value with controlId,modelId,actionOperator");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the action value with controlId,modelId,actionOperator");
            return result;
        }
        int commandValue = ((List<ControlOptionActionVo>) response.getData()).get(0).getCommandValue();

        //根据machineId查board_version表,获取version
        condition.clear();
        condition.put("machineId", machineId);
        condition.put("blockFlag", false);
        response = boardVersionService.fetchBoardVersion(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find board version by machineId");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find borad version by machineId");
            return result;
        }
        int version = ((List<BoardVersion>) response.getData()).get(0).getVersion();

        //according the value to control the machine
        if (component.equals("power")) {
            if (version == 2)
                response = coreService.configPower(machineId, commandValue, version);
            else if (version == 1) {
                response = coreV1Service.configPower(machineId, commandValue, version);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    MachineStatus machineV1Status = machineV1StatusCacheService.fetch(machineId);
                    if(machineV1Status!=null) {
                        machineV1Status.setPower(commandValue);
                        machineV1StatusCacheService.generate(machineV1Status);
                    }
                }
            }
        } else if (component.equals("lock")) {
            if (version == 2)
                response = coreService.configLock(machineId, commandValue);
            else if (version == 1) {
                response.setResponseCode(ResponseCode.RESPONSE_ERROR);
                response.setDescription("board v1 have no component lock");
                return response;
            }
        } else if (component.equals("heat")) {
            if (version == 2)
                response = coreService.configHeat(machineId, commandValue, version);
            else if (version == 1) {
                response = coreV1Service.configHeat(machineId, commandValue, version);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    MachineStatus machineV1Status = machineV1StatusCacheService.fetch(machineId);
                    if(machineV1Status!=null) {
                        machineV1Status.setHeat(commandValue);
                        machineV1StatusCacheService.generate(machineV1Status);
                    }
                }
            }
        } else if (component.equals("mode")) {
            if (version == 2)
                response = coreService.configMode(machineId, commandValue, version);
            else if (version == 1) {
                response = coreV1Service.configMode(machineId, commandValue, version);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    MachineStatus machineV1Status = machineV1StatusCacheService.fetch(machineId);
                    if(machineV1Status!=null) {
                        machineV1Status.setMode(commandValue);
                        machineV1StatusCacheService.generate(machineV1Status);
                    }
                }
            }
        } else {
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
    @RequestMapping(value = "/config/speed", method = RequestMethod.POST)
    public ResultData configSpeed(String qrcode, int speed) {

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
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the machine by qrcode");
            return result;
        }
        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();

        //根据machineId查board_version表,获取version
        condition.clear();
        condition.put("machineId", machineId);
        condition.put("blockFlag", false);
        response = boardVersionService.fetchBoardVersion(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find board version by machineId");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find borad version by machineId");
            return result;
        }
        int version = ((List<BoardVersion>) response.getData()).get(0).getVersion();
        if (version == 2)
            response = coreService.configSpeed(machineId, speed, version);
        else if (version == 1) {
            coreV1Service.configMode(machineId, 2, version);
            try {
                new Thread().sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }

            response = coreV1Service.configSpeed(machineId, speed, version);
            //设置成功则更新缓存
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                MachineStatus machineV1Status = machineV1StatusCacheService.fetch(machineId);
                if(machineV1Status!=null) {
                    machineV1Status.setVolume(speed);
                    machineV1StatusCacheService.generate(machineV1Status);
                }
            }
        }
        return response;
    }

    //用户调节亮度
    @RequestMapping(value = "/config/light", method = RequestMethod.POST)
    public ResultData configLight(String qrcode, int light) {

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
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the machineId by qrcode");
            return result;
        }
        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();

        //根据machineId查board_version表,获取version
        condition.clear();
        condition.put("machineId", machineId);
        condition.put("blockFlag", false);
        response = boardVersionService.fetchBoardVersion(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find board version by machineId");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find borad version by machineId");
            return result;
        }
        int version = ((List<BoardVersion>) response.getData()).get(0).getVersion();
        if (version == 2)
            response = coreService.configLight(machineId, light, version);
        else if (version == 1) {
            response = coreV1Service.configLight(machineId, light, version);

            //设置成功更新缓存
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                MachineStatus machineV1Status = machineV1StatusCacheService.fetch(machineId);
                if(machineV1Status!=null) {
                    machineV1Status.setLight(light);
                    machineV1StatusCacheService.generate(machineV1Status);
                }
            }
        }
        return response;

    }


    public static boolean isEmpty(String... args) {
        for (String arg : args) {
            if (StringUtils.isEmpty(arg))
                return true;
        }
        return false;
    }
}