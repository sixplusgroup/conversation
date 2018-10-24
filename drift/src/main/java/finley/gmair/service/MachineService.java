package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("machine-agent")
public interface MachineService {

    @GetMapping("/machine/qrcode/check/existqrcode")
    ResultData checkQrcode(@RequestParam("codeValue") String codeVale);
}
