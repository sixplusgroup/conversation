package finley.gmair.controller;

import finley.gmair.form.machine.ControlOptionForm;
import finley.gmair.model.machine.BoardVersion;
import finley.gmair.model.machine.ControlOption;
import finley.gmair.model.machine.ControlOptionAction;
import finley.gmair.model.machine.ModelVolume;
import finley.gmair.service.*;
import finley.gmair.service.impl.RedisService;
import finley.gmair.util.MachineConstant;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.ControlOptionActionVo;
import finley.gmair.vo.machine.GoodsModelDetailVo;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(ControlOptionController.class);

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    @Autowired
    private ControlOptionService controlOptionService;

    @Autowired
    private CoreV2Service coreV2Service;

    @Autowired
    private CoreV1Service coreV1Service;

    @Autowired
    private CoreV3Service coreV3Service;

    @Autowired
    private FanCoreService fanCoreService;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private BoardVersionService boardVersionService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PreBindService preBindService;

    @Autowired
    private ModelVolumeService modelVolumeService;

    public static boolean isEmpty(String... args) {
        for (String arg : args) {
            if (StringUtils.isEmpty(arg))
                return true;
        }
        return false;
    }

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

    /**
     * 用户控制设备
     *
     * @param qrcode
     * @param component
     * @param operation
     * @return
     */
    @PostMapping("/operate")
    public ResultData chooseComponent(String qrcode, String component, String operation) {
        ResultData result = new ResultData();
        //check empty
        if (isEmpty(qrcode, component, operation)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("provide all the information");
            return result;
        }
        //根据qrcode 查 code_machine_bind表,取出machineId
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);

        // 检查machineId是否已获取，如果没有则进行相应的处理
        response = preBindService.checkMachineId(response, qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return response;
        }

        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();
        //根据qrcode 查设备商品及型号详情
        response = qrCodeService.profile(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前未能查询到设备二维码对应的详细信息");
            return result;
        }
        GoodsModelDetailVo vo = (GoodsModelDetailVo) response.getData();
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
        condition.put("modelId", vo.getModelId());
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
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result = boardVersionResponse(machineId, response);
            return result;
        }
        int version = ((List<BoardVersion>) response.getData()).get(0).getVersion();
        //todo 添加关于屏幕开关的调用
        //according the value to control the machine
        if (component.equals("power")) {
            switch (version) {
                case 1:
                    response = coreV1Service.configPower(machineId, commandValue, version);
                    break;
                case 2:
                    response = coreV2Service.configPower(machineId, commandValue, version);
                    break;
                case 3:
                    response = coreV3Service.configPower(machineId, commandValue);
                    break;
                case 4:
                    response = fanCoreService.config(vo.getModelName(), machineId, commandValue, null, null, null, null, null, null, null, null);
                    break;
                default:
                    logger.error("Unrecognized board version in power");

            }
        } else if (component.equals("lock")) {
            switch (version) {
                case 1:
                    response = coreV1Service.configLock(machineId, commandValue);
                    break;
                case 2:
                    response = coreV2Service.configLock(machineId, commandValue);
                    break;
                case 3:
                    response = coreV3Service.configLock(machineId, commandValue);
                    break;
                default:
                    logger.error("Unrecognized board version in lock");

            }
        } else if (component.equals("heat")) {
            switch (version) {
                case 1:
                    response = coreV1Service.configHeat(machineId, commandValue, version);
                    break;
                case 2:
                    response = coreV2Service.configHeat(machineId, commandValue, version);
                    break;
                case 3:
                    response = coreV3Service.configHeat(machineId, commandValue);
                    break;
                case 4:
                    response = fanCoreService.config(vo.getModelName(), machineId, null, null, null, null, commandValue, null, null, null, null);
                    break;
                default:
                    logger.error("Unrecognized board version in heat");

            }
        } else if (component.equals("mode")) {
            switch (version) {
                case 1:
                    response = coreV1Service.configMode(machineId, commandValue, version);
                    break;
                case 2:
                    response = coreV2Service.configMode(machineId, commandValue, version);
                    break;
                case 3:
                    response = coreV3Service.configMode(machineId, commandValue);
                    break;
                case 4:
                    response = fanCoreService.config(vo.getModelName(), machineId, null, null, commandValue, null, null, null, null, null, null);
                    break;
                default:
                    logger.error("Unrecognized board version in heat");

            }
        } else if (component.equals("sweep")) {
            switch (version) {
                case 4:
                    response = fanCoreService.config(vo.getModelName(), machineId, null, null, null, commandValue, null, null, null, null, null);
                    break;
                default:
                    logger.error("当前设备".concat(qrcode).concat("不支持扫风调节"));
            }
        } else if (component.equals("buzz")) {
            switch (version) {
                case 4:
                    response = fanCoreService.config(vo.getModelName(), machineId, null, null, null, null, null, null, null, commandValue, null);
                    break;
                default:
                    logger.error("当前设备".concat(qrcode).concat("不支持蜂鸣器调节"));
            }
        } else if (component.equals("uv")) {
            switch (version) {
                case 4:
                    response = fanCoreService.config(vo.getModelName(), machineId, null, null, null, null, null, null, null, null, commandValue);
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

    /**
     * 调节设备风量
     *
     * @param qrcode
     * @param speed
     * @return
     */
    @RequestMapping(value = "/config/speed", method = RequestMethod.POST)
    public ResultData configSpeed(String qrcode, int speed) {
        ResultData result = new ResultData();
        //check empty
        if (ControlOptionController.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("provide all the speed");
            return result;
        }
        //根据qrcode 查 qrcode表，取出model_id
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = qrCodeService.profile(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能找到二维码对应的商品及型号信息");
            return result;
        }
        GoodsModelDetailVo vo = (GoodsModelDetailVo) response.getData();
        String modelId = vo.getModelId();
        //根据modelId查 model_volume_config表，取风量范围
        condition.clear();
        condition.put("modelId", modelId);
        condition.put("blockFlag", false);
        response = modelVolumeService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("sorry, can not find the volume config");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the volume config");
            return result;
        }
        List<ModelVolume> list = (List<ModelVolume>) response.getData();
        int minVolume = list.get(0).getMinVolume(), maxVolume = list.get(0).getMaxVolume();
        if (list.size() > 1) {
            for (ModelVolume config : list) {
                minVolume = minVolume < config.getMinVolume() ? minVolume : config.getMinVolume();
                maxVolume = maxVolume > config.getMaxVolume() ? maxVolume : config.getMaxVolume();
            }
        }

        //根据modelId查 model_volume_config表，取隐藏风量
        response = modelVolumeService.fetchTurboVolume(modelId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
            maxVolume = (int)response.getData();
        }

        //根据风量范围判断是否可运行
        if (speed < minVolume) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("volume is too low");
            return result;
        } else if (speed > maxVolume) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("volume is too high");
            return result;
        }
        //根据qrcode 查 code_machine_bind表,取出machineId
        condition.clear();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        response = machineQrcodeBindService.fetch(condition);
        // 检查machineId是否已经获取到
        response = preBindService.checkMachineId(response, qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return response;
        }

        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();
        //根据machineId查board_version表,获取version
        condition.clear();
        condition.put("machineId", machineId);
        condition.put("blockFlag", false);
        response = boardVersionService.fetchBoardVersion(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result = boardVersionResponse(machineId, response);
            return result;
        }
        int version = ((List<BoardVersion>) response.getData()).get(0).getVersion();
        switch (version) {
            case 1:
                coreV1Service.configMode(machineId, MachineConstant.MACHINE_V1_MANUAL, version);
                try {
                    new Thread().sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response = coreV1Service.configSpeed(machineId, speed, version);
                break;
            case 2:
                coreV2Service.configMode(machineId, MachineConstant.MACHINE_V2_MANUAL, version);
                try {
                    new Thread().sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response = coreV2Service.configSpeed(machineId, speed, version);
                break;
            case 3:
                coreV3Service.configMode(machineId, MachineConstant.MACHINE_V3_MANUAL);
                response = coreV3Service.configSpeed(machineId, speed);
                break;
            case 4:
                response = fanCoreService.config(vo.getModelName(), machineId, null, speed, null, null, null, null, null, null, null);
                break;
            default:
                logger.error("Unrecognized board version in heat");

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

        // 检查machine_id是否获取到
        response = preBindService.checkMachineId(response, qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return response;
        }

        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();

        //根据machineId查board_version表,获取version
        condition.clear();
        condition.put("machineId", machineId);
        condition.put("blockFlag", false);
        response = boardVersionService.fetchBoardVersion(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result = boardVersionResponse(machineId, response);
            return result;
        }
        int version = ((List<BoardVersion>) response.getData()).get(0).getVersion();
        switch (version) {
            case 1:
                response = coreV1Service.configLight(machineId, light, version);
                break;
            case 2:
                response = coreV2Service.configLight(machineId, light, version);
                break;
            case 3:
                response = coreV3Service.configLight(machineId, light);
                break;
            default:
                logger.info("Unrecognized board version in light control");
        }
        return response;

    }

    @PostMapping("/config/screen")
    public ResultData configScreen(String qrcode, int screen) {
        ResultData result = new ResultData();
        //根据qrcode 查 code_machine_bind表,取出machineId
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);
        // 检查machine_id是否获取到
        response = preBindService.checkMachineId(response, qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return response;
        }

        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();
        //根据machineId查board_version表,获取version
        condition.clear();
        condition.put("machineId", machineId);
        condition.put("blockFlag", false);
        response = boardVersionService.fetchBoardVersion(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result = boardVersionResponse(machineId, response);
            return result;
        }
        int version = ((List<BoardVersion>) response.getData()).get(0).getVersion();
        switch (version) {
            case 1:
                //第一代控制板不具备滤网提示功能
                break;
            case 2:
                response = coreV2Service.configScreen(machineId, screen);
                break;
            case 3:
                response = coreV3Service.configScreen(machineId, screen);
                break;
            default:
                logger.info("Unrecognized board version in screen control");
        }
        if (version == 2)
            response = coreV2Service.configScreen(machineId, screen);
        return response;
    }

    @PostMapping("/config/temp")
    public ResultData configTemp(String qrcode, int temp) {
        ResultData result = new ResultData();
        //根据qrcode 查 code_machine_bind表,取出machineId
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);

        // 检查machine_id是否获取到
        response = preBindService.checkMachineId(response, qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return response;
        }

        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();
        //根据二维码查询设备的商品及型号信息
        response = qrCodeService.profile(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能找到二维码对应的商品及型号信息");
            return result;
        }
        GoodsModelDetailVo vo = (GoodsModelDetailVo) response.getData();
        //根据machineId查board_version表,获取version
        condition.clear();
        condition.put("machineId", machineId);
        condition.put("blockFlag", false);
        response = boardVersionService.fetchBoardVersion(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能够查询到二维码" + qrcode + "对应的控制板版本信息");
            return result;
        }
        int version = ((List<BoardVersion>) response.getData()).get(0).getVersion();
        switch (version) {
            case 4:
                result = fanCoreService.config(vo.getModelName(), machineId, null, null, null, null, null, null, temp, null, null);
                break;
            default:
                response.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("当前控制板不支持设置目标温度");
                logger.error("当前控制板不支持设置目标温度");
        }
        return result;
    }

    @PostMapping("/config/timing")
    public ResultData configTiming(String qrcode, int countdown) {
        ResultData result = new ResultData();
        //根据qrcode 查 code_machine_bind表,取出machineId
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);

        // 检查machine_id是否获取到
        response = preBindService.checkMachineId(response, qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return response;
        }

        String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();
        //根据二维码查询设备的商品及型号信息
        response = qrCodeService.profile(qrcode);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能找到二维码对应的商品及型号信息");
            return result;
        }
        GoodsModelDetailVo vo = (GoodsModelDetailVo) response.getData();
        //根据machineId查board_version表,获取version
        condition.clear();
        condition.put("machineId", machineId);
        condition.put("blockFlag", false);
        response = boardVersionService.fetchBoardVersion(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能够查询到二维码" + qrcode + "对应的控制板版本信息");
            return result;
        }
        int version = ((List<BoardVersion>) response.getData()).get(0).getVersion();
        switch (version) {
            case 4:
                result = fanCoreService.config(vo.getModelName(), machineId, null, null, null, null, null, countdown, null, null, null);
                break;
            default:
                response.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("当前控制板不支持定时");
                logger.error("当前控制板不支持定时");
        }
        return result;
    }

    /**
     * 根据设备二维码及设备组件查询
     *
     * @param modelId
     * @param component
     * @return
     */
    @GetMapping("/search")
    public ResultData search(String modelId, String component, String operation, String value) {
        ResultData result = new ResultData();
        //若没有型号参数，则无法进行查询
        if (StringUtils.isEmpty(modelId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the modelId");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("modelId", modelId);
        if (!StringUtils.isEmpty(component)) {
            Map<String, Object> con = new HashMap<>();
            con.put("optionComponent", component);
            ResultData res = controlOptionService.fetchControlOption(con);
            if (res.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                res.setResponseCode(ResponseCode.RESPONSE_ERROR);
                res.setDescription("fail to find the option_id by component");
                return result;
            }
            if (res.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                res.setResponseCode(ResponseCode.RESPONSE_ERROR);
                res.setDescription("sorry, can not find the option_id");
                return result;
            }
            ControlOption controlOption = ((List<ControlOption>) res.getData()).get(0);
            String controlId = controlOption.getControlId();
            condition.put("controlId", controlId);
            if (!StringUtils.isEmpty(operation)) {
                condition.put("actionOperator", operation);
            }
            if (!StringUtils.isEmpty(value)) {
                condition.put("commandValue", value);
            }
        }
        ResultData response = controlOptionService.fetchControlOptionAction(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("sorry, can not find the action name");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the action name");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    private ResultData boardVersionResponse(String machineId, ResultData response) {
        ResultData result = new ResultData();
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能查找到" + machineId + "所对应的控制板版本号");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("控制板版本号查找出现异常，请稍后尝试");
            return result;
        }
        return result;
    }
}