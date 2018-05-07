package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "resource-agent")
public interface TempFileMapService {
    @RequestMapping(method = RequestMethod.POST, value = "/resource/tempfilemap/createpic")
    ResultData createPicMap(@RequestParam("fileUrl") String fileUrl,
                            @RequestParam("actualPath")String actualPath,
                            @RequestParam("fileName")String fileName);

    @RequestMapping(method = RequestMethod.GET, value = "/resource/tempfilemap/fetchpath")
    ResultData actualPath(@RequestParam("fileUrl") String fileUrl);

    @RequestMapping(method = RequestMethod.GET, value = "/resource/tempfilemap/deletevalid")
    ResultData deleteValidPicMapFromTempFileMap(@RequestParam("fileUrl") String fileUrl);

}