package finley.gmair.controller;

import finley.gmair.model.machine.Ownership;
import finley.gmair.service.AuthConsumerService;
import finley.gmair.service.MachineService;
import finley.gmair.service.RepositoryService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reception/machine")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private AuthConsumerService authConsumerService;

    @GetMapping("/check/device/name/binded")
    public ResultData checkDeviceNameExist(String deviceName) {
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String consumerId = (String) authConsumerService.getConsumerId(phone).getData();
        return machineService.checkDeviceNameExist(consumerId, deviceName);
    }

    @GetMapping("/check/device/binded")
    public ResultData checkDeviceBinded(String qrcode) {
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String consumerId = (String) authConsumerService.getConsumerId(phone).getData();
        return machineService.checkDeviceBinded(consumerId, qrcode);
    }

    //设备初始化时 将qrcode和consumerId绑定
    @PostMapping("/deviceinit")
    public ResultData deviceInit(String qrcode, String deviceName) {
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String consumerId = (String) authConsumerService.getConsumerId(phone).getData();
        return machineService.bindConsumerWithQRcode(consumerId, deviceName, qrcode, Ownership.OWNER.getValue());
    }

    @RequestMapping(value = "/consumer/qrcode/unbind", method = RequestMethod.POST)
    public ResultData unbindConsumerWithQRcode(String qrcode) {
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String consumerId = (String) authConsumerService.getConsumerId(phone).getData();
        return machineService.unbindConsumerWithQRcode(consumerId, qrcode);
    }

    @PostMapping("/device/bind/share")
    public ResultData acquireControlOn(String qrcode, String deviceName) {
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String consumerId = (String) authConsumerService.getConsumerId(phone).getData();
        return machineService.bindConsumerWithQRcode(consumerId, deviceName, qrcode, Ownership.SHARE.getValue());
    }

    /**
     * This method helps to check whether the qrcode is online or not
     *
     * @return online result, RESPONSE_OK & RESPONSE_NULL
     * @Param qrcode, the code value of the specified machine
     */
    @GetMapping("/checkonline")
    public ResultData checkOnline(String qrcode) {
        return machineService.checkOnline(qrcode);
    }

    @PostMapping("/qrcode/status")
    public ResultData findStatusByQRcode(String qrcode) {
        return machineService.checkQRcodeExist(qrcode);
    }

    //发送遥控信息
    @PostMapping("/operate/{component}/{operation}")
    public ResultData configComponentStatus(@PathVariable("component") String component, @PathVariable("operation") String operation, String qrcode) {
        return machineService.chooseComponent(qrcode, component, operation);
    }

    //配置风量
    @PostMapping("/config/speed")
    public ResultData configSpeed(String qrcode, int speed) {
        return machineService.configSpeed(qrcode, speed);
    }

    @PostMapping("/config/light")
    public ResultData configLight(String qrcode, int light) {
        return machineService.configLight(qrcode, light);
    }

    //设置配置项
    @PostMapping("/control/option/create")
    public ResultData setControlOption(String optionName, String optionComponent, String modelId, String actionName, String actionOperator) {
        return machineService.setControlOption(optionName, optionComponent, modelId, actionName, actionOperator);
    }

    //根据modelId查询ControlOption
    @GetMapping("/control/option/probe")
    public ResultData probeControlOption(String modelId) {
        return machineService.probeControlOptionByModelId(modelId);
    }

    //根据当前的qrcode查询这台机器的各种值(co2,pm2.5等)
    @RequestMapping(value = "/info/probe", method = RequestMethod.GET)
    public ResultData getMachineInfo(String qrcode) {
        return machineService.getMachineStatusByQRcode(qrcode);
    }

    //根据consumerId获取用户的machine list
    @RequestMapping(value = "/devicelist", method = RequestMethod.GET)
    public ResultData getUserDeviceList() {
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String consumerId = (String) authConsumerService.getConsumerId(phone).getData();
        return machineService.getMachineListByConsumerId(consumerId);
    }


    @RequestMapping(value = "/probe/qrcode/byurl", method = RequestMethod.POST)
    public ResultData probeQRcodeByUrl(String codeUrl) {
        return machineService.probeQRcodeByUrl(codeUrl);
    }


    @RequestMapping(value = "/model/query/by/modelid", method = RequestMethod.GET)
    public ResultData queryModelByModelId(String modelId) {
        return machineService.queryModelByModelId(modelId);
    }

    @RequestMapping(value = "/probe/cityId/byqrcode", method = RequestMethod.GET)
    public ResultData probeCityIdByQRcode(String qrcode) {
        return machineService.probeCityIdByQRcode(qrcode);
    }

    @RequestMapping(value = "/probe/volume", method = RequestMethod.GET)
    public ResultData probeVolumeLimitationByModelId(String modelId) {
        return machineService.probeModelVolumeByModelId(modelId);
    }

    @RequestMapping(value = "/probe/light", method = RequestMethod.GET)
    public ResultData probeLightLimitationByModelId(String modelId) {
        return machineService.probeModelLightByModelId(modelId);
    }


    @RequestMapping(value = "/probe/board/version", method = RequestMethod.GET)
    public ResultData probeBoardVersionByQRcode(String qrcode) {
        return machineService.findBoardVersionByQRcode(qrcode);
    }

    @RequestMapping(value = "/probe/hourly/pm25", method = RequestMethod.GET)
    public ResultData probeMachineHourlyPm2_5(String qrcode) {
        return machineService.fetchMachineHourlyPm2_5(qrcode);
    }

    @RequestMapping(value = "/probe/daily/pm25", method = RequestMethod.GET)
    public ResultData probeMachineDailyPm2_5(String qrcode) {
        return machineService.fetchMachineDailyPm2_5(qrcode);
    }

    @RequestMapping(value = "/modify/bind/name", method = RequestMethod.POST)
    public ResultData modifyBindName(String qrcode, String bindName) {
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String consumerId = (String) authConsumerService.getConsumerId(phone).getData();
        return machineService.modifyBindName(qrcode, bindName, consumerId);
    }

    @RequestMapping(value = "/consumer/bind/probe/byqrcode", method = RequestMethod.GET)
    public ResultData probeConsumerQRcodeBindInfo(String qrcode) {
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String consumerId = (String) authConsumerService.getConsumerId(phone).getData();
        return machineService.probeBindByQRcode(qrcode, consumerId);
    }
}
