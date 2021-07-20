package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author ：lycheeshell
 * @Date ：Created in 14:57 2019/8/27
 */
@FeignClient("verify-agent")
public interface VerifyService {

    @PostMapping(value = "/verify/user/check")
    ResultData check(@RequestParam("name") String name, @RequestParam("idno") String idno);

}
