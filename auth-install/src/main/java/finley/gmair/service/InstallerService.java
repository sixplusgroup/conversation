package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("install-agent")
public interface InstallerService {

    @PostMapping("/installation/member/exist")
    ResultData exist(String openid);

}
