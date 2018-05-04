package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "resource-agent")
public interface FileMapService {
    @RequestMapping(method = RequestMethod.POST, value = "/resource/filemap/createpic")
    ResultData createPicMap(@RequestParam("fileUrl") String fileUrl);
}