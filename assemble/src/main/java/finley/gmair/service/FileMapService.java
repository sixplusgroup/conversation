package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "resource-agent")
public interface FileMapService {
    @RequestMapping(method = RequestMethod.POST, value = "/resource/createpic")
    ResultData createPicMap(@RequestParam("fileUrl") String fileUrl);

    @RequestMapping(method = RequestMethod.POST, value = "/resource/filemap/create")
    ResultData create(@RequestParam("url") String url,
                      @RequestParam("actualPath") String actualPath,
                      @RequestParam("filename") String filename);
}