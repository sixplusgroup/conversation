package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("machine-agent")
public interface MachineService {

    @GetMapping("/machine/goods/model/list")
    ResultData modelList();

    @GetMapping("/machine/qrcode/batch/list")
    ResultData batchList(@RequestParam("modelId") String modelId);

    @PostMapping("/machine/qrcode/check")
    ResultData check(@RequestParam("candidate") String candidate);
}
