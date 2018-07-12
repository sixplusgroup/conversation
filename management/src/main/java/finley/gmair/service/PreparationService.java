package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("preparation-agent")
public interface PreparationService {

    @PostMapping("/preparation/machine/bind")
    ResultData bindVersion(@RequestParam("machineId") String machineId,
                           @RequestParam("version") int version,
                           @RequestParam("codeValue") String codeValue);
}
