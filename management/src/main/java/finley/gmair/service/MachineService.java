package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("machine-agent")
public interface MachineService {

    @GetMapping("/machine/goods/model/list")
    ResultData modelList();
}
