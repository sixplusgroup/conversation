package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

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

    //QrcodeController
    @GetMapping("/machine/qrcode/findbyqrcode")
    ResultData findMachineIdByCodeValue(@RequestParam("codeValue") String codeValue);

    @GetMapping("/machine/qrcode/findbyqrcode/consumer")
    ResultData findMachineIdByCodeValueFacetoConsumer(@RequestParam("codeValue") String codeValue);

    @GetMapping("/machine/qrcode/check/existqrcode")
    ResultData checkQRcodeExist(@RequestParam("codeValue") String codeValue);

    @PostMapping("machine/qrcode/probe/byurl")
    ResultData probeQRcodeByUrl(@RequestParam("codeUrl") String codeUrl);

    @PostMapping("/machine/qrcode/prebind/to/bind")
    ResultData prebindToBind(@RequestParam("qrcode") String qrcode);

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

    //MachineAirQualityController
    @GetMapping ("/machine/status/byqrcode")
    ResultData getMachineStatusByQRcode(@RequestParam("qrcode") String qrcode);

    //MachineDefaultLocationController
    @GetMapping("/machine/default/location/probe/cityid")
    ResultData probeCityIdByQRcode(@RequestParam("qrcode") String qrcode);
    @PostMapping("/machine/default/location/update/cityid")
    ResultData updateCityIdByQRcode(@RequestParam("cityId") String cityId,
                                    @RequestParam("qrcode") String qrcode);

    //GoodsController
    @GetMapping("/machine/goods/model/query/by/modelid")
    ResultData queryModelByModelId(@RequestParam("modelId") String modelId);
}
