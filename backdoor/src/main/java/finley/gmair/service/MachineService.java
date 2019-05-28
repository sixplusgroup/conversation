package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("machine-agent")
public interface MachineService {
    @PostMapping({"/machine/control/option/config/speed"})
    ResultData configSpeed(@RequestParam("qrcode") String qrcode, @RequestParam("speed") int speed);

    @GetMapping({"/machine/status/byqrcode"})
    ResultData getMachineStatusByQRcode(@RequestParam("qrcode") String qrcode);
}
