package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;

@FeignClient(name="resource-agent")
public interface TempFileMapService {
    @RequestMapping("/resource/tempfilemap/createpic")
    ResultData createPicMap(File file);
}