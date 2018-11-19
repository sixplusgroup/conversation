package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("machine-agent")
public interface MachineService {
    @PostMapping(value = "/machine/qrcode/probe/byurl")
    ResultData getCodeValuebyCodeUrl(@RequestParam("codeUrl") String codeUrl);
}
