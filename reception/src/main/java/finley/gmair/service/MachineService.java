package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    //MachineSettingController

    //QrcodeController
    @GetMapping("/machine/qrcode/findbyqrcode")
    ResultData findMachineIdByCodeValue(@RequestParam("codeValue") String codeValue);

    //ConsumerQRcodeController
    @PostMapping("/machine/consumer/bindwithqrcode")
    ResultData bindConsumerWithQRcode(@RequestParam("consumerId") String consumerId,
                                      @RequestParam("bindName") String bindName,
                                      @RequestParam("qrcode") String qrcode,
                                      @RequestParam("ownership") int ownership);

}
