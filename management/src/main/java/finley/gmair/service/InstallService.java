package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "install-agent")
public interface InstallService {

    @GetMapping("/installation/team/list")
    ResultData fetchTeamList();
}
