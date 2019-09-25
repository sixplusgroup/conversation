package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("resource-agent")
public interface ResourcesService {

    @GetMapping("/resource/tempfilemap/md5path")
    ResultData getTempFileMap(@RequestParam("url") String url);

}
