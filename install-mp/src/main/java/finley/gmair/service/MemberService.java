package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("install-agent")
public interface MemberService {

    @PostMapping("/install/member/bind")
    ResultData bindWechat(@RequestParam(value = "openid") String openid, @RequestParam(value = "phone") String phone);
}
