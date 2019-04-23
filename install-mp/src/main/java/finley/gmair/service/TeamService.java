package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("install-agent")
public interface TeamService {

    @GetMapping("/install/team/{teamId}/info")
    ResultData profile(@PathVariable("teamId") String teamId);
}
