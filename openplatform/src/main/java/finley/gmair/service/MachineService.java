package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("machine-agent")
public interface MachineService {
    ResultData indoor(@RequestParam("qrcode") String qrcdoe);

    ResultData outdoor(@RequestParam("qrcode") String qrcode);
}
