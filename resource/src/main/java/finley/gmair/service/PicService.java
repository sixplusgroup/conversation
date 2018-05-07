package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "installation-agent")
public interface PicService {
    @RequestMapping(method = RequestMethod.POST, value = "/installation/pic/delete")
    ResultData deleteByUrl(@RequestParam("fileUrl") String fileUrl);
}
