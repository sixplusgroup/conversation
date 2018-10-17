package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("machine-agent")
public interface MachineService {

    @GetMapping("/machine/qrcode/checkonline")
    ResultData checkOnline(@RequestParam("qrcode") String qrcode);

    @GetMapping("/machine/qrcode/model")
    ResultData getModel(@RequestParam("codeValue") String codeValue);

    @GetMapping("/machine/status/byuid")
    ResultData machineStatus(@RequestParam("uid") String uid);


}
