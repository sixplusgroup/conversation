package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: Bright Chan
 * @date: 2020/7/16 21:12
 * @description: TODO
 */

@FeignClient("aligenie-control-agent")
public interface TmallGenieService {

    @PostMapping("/tmallgenie/list/update")
    ResultData updateListNotify(@RequestParam("accessToken") String accessToken);
}
