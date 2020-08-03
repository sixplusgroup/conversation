package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: Bright Chan
 * @date: 2020/8/2 21:30
 * @description: TODO
 */

@FeignClient("machine-agent")
public interface MachineFeignService {

    @GetMapping("/machine/efficientFilter/updateByRemain")
    ResultData updateByRemain(@RequestParam("remain") int remain, @RequestParam("uid") String uid);
}
