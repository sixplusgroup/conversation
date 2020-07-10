package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import javax.xml.transform.Result;

@FeignClient("machine-agent")
public interface MachineService {

    @GetMapping("/machine/qrcode/model")
    ResultData findModel(@RequestParam("codeValue") String codeValue);

    //ControlOptionController
    @GetMapping("/machine/control/option/create")
    ResultData setControlOption(@RequestParam("optionName") String optionName,
                                @RequestParam("optionComponent") String optionComponent,
                                @RequestParam("modelId") String modelId,
                                @RequestParam("actionName") String actionName,
                                @RequestParam("actionOperator") String actionOperator);

    @PostMapping("/machine/control/option/probe/bymodelid")
    ResultData probeControlOptionByModelId(@RequestParam("modelId") String modelId);

    @PostMapping("/machine/control/option/operate")
    ResultData chooseComponent(@RequestParam("qrcode") String qrcode,
                               @RequestParam("component") String component,
                               @RequestParam("operation") String operation);

    @PostMapping("/machine/control/option/config/speed")
    ResultData configSpeed(@RequestParam("qrcode") String qrcode, @RequestParam("speed") int speed);

    @PostMapping("/machine/control/option/config/light")
    ResultData configLight(@RequestParam("qrcode") String qrcode, @RequestParam("light") int light);

    @PostMapping("/machine/control/option/config/temp")
    ResultData configTemp(@RequestParam("qrcode") String qrcode, @RequestParam("temp") int temp);

    @PostMapping("/machine/control/option/config/timing")
    ResultData configTiming(@RequestParam("qrcode") String qrcode, @RequestParam("countdown") int countdown);

    //QrcodeController
    @GetMapping("/machine/qrcode/findbyqrcode")
    ResultData findMachineIdByCodeValue(@RequestParam("codeValue") String codeValue);

    @GetMapping("/machine/qrcode/findbyqrcode/consumer")
    ResultData findMachineIdByCodeValueFacetoConsumer(@RequestParam("codeValue") String codeValue);

    @GetMapping("/machine/qrcode/check/existqrcode")
    ResultData checkQRcodeExist(@RequestParam("codeValue") String codeValue);

    @PostMapping("machine/qrcode/probe/byurl")
    ResultData probeQRcodeByUrl(@RequestParam("codeUrl") String codeUrl);

    @GetMapping("/machine/{qrcode}/isonline")
    ResultData checkOnline(@PathVariable("qrcode") String qrcode);

    //ConsumerQRcodeController
    @PostMapping("/machine/consumer/qrcode/bind")
    ResultData bindConsumerWithQRcode(@RequestParam("consumerId") String consumerId,
                                      @RequestParam("bindName") String bindName,
                                      @RequestParam("qrcode") String qrcode,
                                      @RequestParam("ownership") int ownership);

    @PostMapping("/machine/consumer/qrcode/unbind")
    ResultData unbindConsumerWithQRcode(@RequestParam("consumerId") String consumerId,
                                        @RequestParam("qrcode") String qrcode);

    @GetMapping("/machine/consumer/check/devicename/exist")
    ResultData checkDeviceNameExist(@RequestParam("consumerId") String consumerId,
                                    @RequestParam("bindName") String bindName);

    @GetMapping("/machine/consumer/check/device/binded")
    ResultData checkDeviceBinded(@RequestParam("consumerId") String consumerId,
                                 @RequestParam("qrcode") String qrcode);


    @PostMapping("/machine/consumer/check/consumerid/accessto/qrcode")
    ResultData checkConsumerAccesstoQRcode(@RequestParam("consumerId") String consumerId,
                                           @RequestParam("qrcode") String qrcode);

    @GetMapping("/machine/consumer/machinelist")
    ResultData getMachineListByConsumerId(@RequestParam("consumerId") String consumerId);

    @GetMapping("/machine/consumer/machine/list")
    ResultData obtainMachineList(@RequestParam("consumerId") String consumerId);

    @PostMapping("/machine/consumer/modify/bind/name")
    ResultData modifyBindName(@RequestParam("qrcode") String qrcode, @RequestParam("bindName") String bindName, @RequestParam("consumerId") String consumerId);

    @GetMapping("/machine/consumer/probe/by/qrcode")
    ResultData probeBindByQRcode(@RequestParam("qrcode") String qrcode, @RequestParam("consumerId") String consumerId);

    /**
     * 获取设备的运行状态信息
     *
     * @param qrcode
     * @return
     */
    @GetMapping("/machine/status/byqrcode")
    ResultData getMachineStatusByQRcode(@RequestParam("qrcode") String qrcode);

    @GetMapping("/machine/{qrcode}/status")
    ResultData runningStatus(@PathVariable("qrcode") String qrcode);

    //MachineDefaultLocationController
    @GetMapping("/machine/default/location/probe/cityid")
    ResultData probeCityIdByQRcode(@RequestParam("qrcode") String qrcode);

    @PostMapping("/machine/default/location/update/cityid")
    ResultData updateCityIdByQRcode(@RequestParam("cityId") String cityId, @RequestParam("qrcode") String qrcode);

    //GoodsController
    @GetMapping("/machine/goods/model/query/by/modelid")
    ResultData queryModelByModelId(@RequestParam("modelId") String modelId);

    //ModelVolumeController
    @GetMapping(value = "/machine/model/volume/probe/by/modelId")
    ResultData probeModelVolumeByModelId(@RequestParam("modelId") String modelId);

    //ModelLightController
    @GetMapping(value = "/machine/model/light/probe/by/modelId")
    ResultData probeModelLightByModelId(@RequestParam("modelId") String modelId);

    //BoardVersionController
    @GetMapping("/machine/board/by/qrcode")
    ResultData findBoardVersionByQRcode(@RequestParam("qrcode") String qrcode);

    //MachineStatusController
    @GetMapping("/machine/status/hourly")
    ResultData fetchMachineHourlyPm2_5(@RequestParam("qrcode") String qrcode);

    @GetMapping("/machine/status/daily")
    ResultData fetchMachineDailyPm2_5(@RequestParam("qrcode") String qrcode);

    //ModelEnabledComponentController
    @GetMapping("/machine/model/enabled/component/fetch")
    ResultData fetchModelEnabledComponent(@RequestParam("modelId") String modelId,
                                          @RequestParam("componentName") String componentName);

    //MachineOnOffController
    @PostMapping("/machine/power/onoff/confirm")
    ResultData confirmPowerOnoff(@RequestParam("qrcode") String qrcode,
                                 @RequestParam("startHour") int startHour,
                                 @RequestParam("startMinute") int startMinute,
                                 @RequestParam("endHour") int endHour,
                                 @RequestParam("endMinute") int endMinute,
                                 @RequestParam("status") boolean status);

    @GetMapping("/machine/power/onoff/get/record/by/code")
    ResultData getRecord(@RequestParam("qrcode") String qrcode);

    @GetMapping("/machine/filter/clean")
    ResultData filterNeedCleanOrNot(@RequestParam("qrcode") String qrcode);

    @GetMapping("/machine/filter/clean/isOpen")
    ResultData filterCleanRemindIsOpen(@RequestParam("qrcode") String qrcode);

    @PostMapping("/machine/filter/clean/change")
    ResultData changeFilterCleanRemindStatus(@RequestParam("qrcode") String qrcode,
                                                    @RequestParam("cleanRemindStatus") boolean cleanRemindStatus);

    @GetMapping("/machine/filter/clean/confirm")
    ResultData confirmClean(@RequestParam("qrcode") String qrcode);

}
