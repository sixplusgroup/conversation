package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("install-agent")
public interface InstallerService {

    @GetMapping("/install/member/profile")
    ResultData profile(@RequestParam(value = "openid", required = false) String openid, @RequestParam(value = "phone", required = false) String phone);

    @GetMapping("/install/member/profile")
    ResultData profileByOpenid(@RequestParam(value = "openid", required = false) String openid);

    @GetMapping("/install/member/profile")
    ResultData profileByPhone(@RequestParam(value = "phone") String phone);
}
