package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("resource-agent")
public interface ResourceService {

    @PostMapping("/resource/createpic")
    ResultData save(@RequestParam("fileUrl") String fileUrl);
}
